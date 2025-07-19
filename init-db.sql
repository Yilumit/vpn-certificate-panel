-- Cria o banco de dados
CREATE DATABASE IF NOT EXISTS vpnpanel_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Remove o usuário se já existir (e evitar conflito de senha)
DROP USER IF EXISTS 'vpnuser'@'localhost';

-- Cria o usuário com a senha correta
CREATE USER 'vpnuser'@'localhost' IDENTIFIED BY 'vpnuser';

-- Dá todas as permissões no banco
GRANT ALL PRIVILEGES ON vpnpanel_db.* TO 'vpnuser'@'localhost';
FLUSH PRIVILEGES;