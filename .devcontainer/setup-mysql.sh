#!/bin/bash
set -e  # Exit on error

# Ensure script is running as root
if [ "$(id -u)" -ne 0 ]; then
    echo "This script must be run as root."
    exit 1
fi

echo "🚀 Installing MySQL Community Server and MySQL Shell..."

# Install necessary dependencies
apt-get update && apt-get install -y gnupg wget lsb-release debconf-utils

# Add MySQL GPG key
wget -qO - https://repo.mysql.com/RPM-GPG-KEY-mysql-2023 | gpg --dearmor | tee /usr/share/keyrings/mysql.gpg > /dev/null

# Add MySQL APT repository
echo "deb [signed-by=/usr/share/keyrings/mysql.gpg] http://repo.mysql.com/apt/debian bullseye mysql-8.0" | tee /etc/apt/sources.list.d/mysql.list

# Update package list
apt-get update

# Install MySQL Server and MySQL Shell
DEBIAN_FRONTEND=noninteractive apt-get install -y mysql-server mysql-shell

# Start MySQL using `mysqld_safe`
echo "🚀 Starting MySQL..."
nohup mysqld_safe --datadir=/var/lib/mysql > /dev/null 2>&1 &

# Wait for MySQL to be ready
sleep 5

# Set up root password
echo "🔑 Configuring MySQL root user..."
mysql -u root -e "ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'rootpassword'; FLUSH PRIVILEGES;"

echo "🎉 MySQL and MySQL Shell installation complete!"