DROP USER IF EXISTS 'gestao-treinamentos'@'%';
CREATE USER 'gestao-treinamentos'@'%' IDENTIFIED BY 'senha123';
GRANT ALL PRIVILEGES ON `gestao-treinamentos`.* TO 'gestao-treinamentos'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;