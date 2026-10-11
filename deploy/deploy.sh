#!/usr/bin/env bash
#
# Push this project to an EC2 instance that has already been prepared with
# deploy/setup-ec2.sh.
#
# Run it from the project root on your own machine (macOS or Linux):
#
#   ./deploy/deploy.sh -h 54.12.34.56 -i ~/keys/webdev.pem
#
# What it does:
#    1. Builds the Spring Boot / Vaadin JAR locally using Maven wrapper.
#    2. Copies the JAR file to /opt/crm-app/app.jar on the server.
#    3. Restarts the systemd service and verifies status.
#
# Options:
#   -h HOST   public IP or DNS name of the instance   (required)
#   -i KEY    path to your .pem private key           (optional if ssh-agent has it)
#   -u USER   SSH login user (default: ubuntu; use 'ec2-user' for Amazon Linux)
#   -d DIR    remote directory (default: /opt/todo-app)

set -euo pipefail

REMOTE_HOST=""
SSH_KEY=""
REMOTE_USER="ubuntu"
REMOTE_DIR="/opt/crm-app"
SERVICE="crm-app"

# Print the comment block at the top of this file as the help text.
usage() {
    awk 'NR>1 { if (/^#/) { sub(/^# ?/, ""); print } else { exit } }' "$0"
    exit 1
}

# getopts is built into every shell and behaves the same on macOS and Linux.
# (The `getopt` *command* does not -- macOS ships the old BSD version, so avoid it.)
while getopts ":h:i:u:d:" option; do
    case "$option" in
        h) REMOTE_HOST="$OPTARG" ;;
        i) SSH_KEY="$OPTARG" ;;
        u) REMOTE_USER="$OPTARG" ;;
        d) REMOTE_DIR="$OPTARG" ;;
        :) echo "ERROR: -$OPTARG needs a value" >&2; usage ;;
        \?) echo "ERROR: unknown option -$OPTARG" >&2; usage ;;
    esac
done

[ -n "$REMOTE_HOST" ] || { echo "ERROR: -h HOST is required" >&2; usage; }

# Run from the project root no matter where the script was called from.
PROJECT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "$PROJECT_DIR"

[ -f pom.xml ] || { echo "ERROR: no pom.xml in $PROJECT_DIR" >&2; exit 1; }

# Build the ssh options once and reuse them for both ssh and rsync.
SSH_OPTS=(-o StrictHostKeyChecking=accept-new)
if [ -n "$SSH_KEY" ]; then
    [ -f "$SSH_KEY" ] || { echo "ERROR: key file not found: $SSH_KEY" >&2; exit 1; }
    SSH_OPTS+=(-i "$SSH_KEY")
fi

TARGET="${REMOTE_USER}@${REMOTE_HOST}"

echo "==> Building Spring Boot application locally..."
./mvnw clean package -DskipTests

JAR_FILE=$(ls target/*.jar | head -n 1)
[ -f "$JAR_FILE" ] || { echo "ERROR: No compiled JAR found in target/" >&2; exit 1; }

echo "==> Ensuring remote directory exists and permissions are set"
ssh "${SSH_OPTS[@]}" "$TARGET" "sudo mkdir -p ${REMOTE_DIR} && sudo chown ${REMOTE_USER}:${REMOTE_USER} ${REMOTE_DIR}"

echo "==> Copying JAR file to ${TARGET}:${REMOTE_DIR}/app.jar"
scp "${SSH_OPTS[@]}" "$JAR_FILE" "${TARGET}:${REMOTE_DIR}/app.jar"

echo "==> Restarting the systemd service"
ssh "${SSH_OPTS[@]}" "$TARGET" "
    set -e
    sudo systemctl restart ${SERVICE}
    sleep 3
    sudo systemctl is-active --quiet ${SERVICE} && echo 'service is running'
"

echo "==> Checking application response"
if curl -fsS --max-time 10 "http://${REMOTE_HOST}/" >/dev/null; then
    printf '\n\n==> Deployed successfully. Open http://%s in your browser.\n' "$REMOTE_HOST"
else
    printf '\n'
    echo "WARNING: application did not respond immediately (Vaadin apps can take a few seconds to start up)." >&2
    echo "Check logs with:" >&2
    echo "  ssh ${SSH_OPTS[*]} $TARGET 'journalctl -u ${SERVICE} -n 50'" >&2
fi