#!/usr/bin/env bash
#
# Start the Vaadin todo app on your own machine.
#
# Checks that everything the app needs is in place before starting it, so a
# missing tool or a skipped install gives you a sentence to read instead of a
# stack trace.
#
#   ./start.sh            start the app on http://localhost:8080
#   ./start.sh --port 4000  listen somewhere other than port 8080
#   ./start.sh --help     show this message
#
# This is for development. On a server, systemd starts the app instead.
#
# Works on macOS and Linux.

set -euo pipefail

# Vaadin 24+ requires Java 21 or newer.
REQUIRED_JAVA_MAJOR=21
PORT_OVERRIDE=""

# --- helpers ----------------------------------------------------------------

say() {
    printf '==> %s\n' "$1"
}

die() {
    printf 'ERROR: %s\n' "$1" >&2
    exit 1
}

usage() {
    awk 'NR>1 { if (/^#/) { sub(/^# ?/, ""); print } else { exit } }' "$0"
    exit 0
}

# --- options ----------------------------------------------------------------

while [ $# -gt 0 ]; do
    case "$1" in
        -p|--port)
            [ $# -ge 2 ] || die "--port needs a number, for example: --port 4000"
            case "$2" in
                ''|*[!0-9]*) die "--port needs a number, got '$2'" ;;
            esac
            PORT_OVERRIDE="$2"
            shift 2
            ;;
        -h|--help)
            usage
            ;;
        *)
            die "unknown option '$1' (try --help)"
            ;;
    esac
done

# Run from the project root no matter where this script was called from.
PROJECT_DIR="$(cd -P -- "$(dirname -- "${BASH_SOURCE[0]:-$0}")" && pwd -P)"
cd "$PROJECT_DIR"

# --- Step 1: Check required tools -------------------------------------------

command -v java >/dev/null 2>&1 \
    || die "Java is not installed. Get JDK ${REQUIRED_JAVA_MAJOR} or newer from https://adoptium.net/"

command -v javac >/dev/null 2>&1 \
    || die "javac (Java compiler) is not installed. Please install the full JDK ${REQUIRED_JAVA_MAJOR} (e.g., openjdk-21-jdk)."

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F. '{print $1}')
if [ "$JAVA_VERSION" = "1" ]; then
    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F. '{print $2}')
fi

if [ "$JAVA_VERSION" -lt "$REQUIRED_JAVA_MAJOR" ]; then
    die "Java version is too old (found Java $JAVA_VERSION). This Vaadin app needs Java ${REQUIRED_JAVA_MAJOR} or newer."
fi

[ -f pom.xml ] || die "no pom.xml here. Run this script from inside the project folder."
[ -f ./mvnw ] || die "no Maven wrapper (mvnw) found in project root."

# --- Step 2: Install project dependencies & build ---------------------------
# For Maven/Vaadin, running a quick compile/package ensures dependencies are 
# downloaded and the Vaadin frontend build runs successfully on first clone.

say "Installing dependencies and building frontend assets (this may take a moment on a fresh clone)..."
./mvnw dependency:resolve compile

# --- Step 3: Create anything the app needs (Database / Sample Data) ----------
# Spring Boot / Vaadin typically uses H2 or an embedded DB file for local dev.
# If your app uses an explicit file database (e.g., data/todo.db), ensure 
# the directory exists here:
# mkdir -p data

# --- Step 4: Start the app and print the URL ----------------------------------

PORT_IN_USE="${PORT_OVERRIDE:-8080}"
say "Starting Vaadin app on http://localhost:${PORT_IN_USE} -- press Ctrl+C to stop"

MAVEN_ARGS="spring-boot:run"
if [ -n "$PORT_OVERRIDE" ]; then
    MAVEN_ARGS="spring-boot:run -Dspring-boot.run.arguments=--server.port=$PORT_OVERRIDE"
fi

# exec replaces this shell with Java/Maven so Ctrl+C goes directly to the app
exec ./mvnw $MAVEN_ARGS