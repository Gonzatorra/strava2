CREATE DATABASE IF NOT EXISTS strava_db;
USE strava_db;

DROP TABLE IF EXISTS reto_participantes;
DROP TABLE IF EXISTS entrenamientos;
DROP TABLE IF EXISTS retos;
DROP TABLE IF EXISTS usuarios;

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    nombre VARCHAR(255),
    peso FLOAT,
    altura FLOAT,
    fecha_nacimiento DATE,
    frecuencia_cardiaca_max FLOAT,
    frecuencia_cardiaca_reposo FLOAT,
    token VARCHAR(255),
    proveedor VARCHAR(50)
);

CREATE TABLE retos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    deporte VARCHAR(50) NOT NULL,
    creador VARCHAR(50) NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    fecha_inicio DATETIME NOT NULL,
    fecha_fin DATETIME NOT NULL,
    objetivo_distancia FLOAT,
    objetivo_tiempo FLOAT,
    FOREIGN KEY (creador) REFERENCES usuarios(username) ON DELETE CASCADE
);

CREATE TABLE entrenamientos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    deporte VARCHAR(50) NOT NULL,
    distancia DOUBLE NOT NULL,
    duracion DOUBLE NOT NULL,
    fecha_inicio DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    FOREIGN KEY (usuario) REFERENCES usuarios(username) ON DELETE CASCADE
);

CREATE TABLE reto_participantes (
  id INT NOT NULL AUTO_INCREMENT,
  usuario_id INT NOT NULL,
  reto_id INT NOT NULL,
  estado VARCHAR(255),
  PRIMARY KEY (id),
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
  FOREIGN KEY (reto_id) REFERENCES retos(id) ON DELETE CASCADE
);

ALTER TABLE reto_participantes
  ADD UNIQUE (usuario_id, reto_id);

