CREATE USER IF NOT EXISTS 'vaadin'@'localhost' IDENTIFIED BY 'Vaadin_Full_Stack!';
CREATE DATABASE IF NOT EXISTS velorim_crm;
GRANT ALL PRIVILEGES ON velorim_crm.* TO 'vaadin'@'localhost';
FLUSH PRIVILEGES;