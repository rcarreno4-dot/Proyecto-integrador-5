-- ==============================================================================
-- ESQUEMA COMPLETO Y DATOS SEMILLA POSTGRESQL (ESTRUCTURA RELACIONAL SIGPRA)
-- ==============================================================================

DROP TABLE IF EXISTS evaluaciones_visita CASCADE;
DROP TABLE IF EXISTS asignaciones_practica CASCADE;
DROP TABLE IF EXISTS solicitudes_practica CASCADE;
DROP TABLE IF EXISTS plazas_practica CASCADE;
DROP TABLE IF EXISTS convenios CASCADE;
DROP TABLE IF EXISTS periodos_practica CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;

CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    documento VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE periodos_practica (
    id SERIAL PRIMARY KEY,
    codigo_periodo VARCHAR(20) UNIQUE NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    horas_requeridas INT NOT NULL DEFAULT 320,
    estado VARCHAR(20) DEFAULT 'En Curso'
);

CREATE TABLE convenios (
    id SERIAL PRIMARY KEY,
    nit_rut VARCHAR(20) UNIQUE NOT NULL,
    nombre_empresa VARCHAR(150) NOT NULL,
    numero_convenio VARCHAR(50) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    estado VARCHAR(20) DEFAULT 'Vigente'
);

CREATE TABLE plazas_practica (
    id SERIAL PRIMARY KEY,
    convenio_id INT REFERENCES convenios(id) ON DELETE CASCADE,
    titulo_plaza VARCHAR(150) NOT NULL,
    modalidad VARCHAR(30) CHECK (modalidad IN ('Presencial', 'HÃ­brida', 'Remota')),
    cupos INT NOT NULL DEFAULT 1,
    descripcion TEXT,
    estado VARCHAR(20) DEFAULT 'Disponible'
);

CREATE TABLE solicitudes_practica (
    id SERIAL PRIMARY KEY,
    estudiante_id INT REFERENCES usuarios(id),
    plaza_id INT REFERENCES plazas_practica(id),
    fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(20) DEFAULT 'Pendiente'
);

CREATE TABLE asignaciones_practica (
    id SERIAL PRIMARY KEY,
    solicitud_id INT REFERENCES solicitudes_practica(id),
    docente_id INT REFERENCES usuarios(id),
    periodo_id INT REFERENCES periodos_practica(id),
    fecha_asignacion DATE DEFAULT CURRENT_DATE,
    estado VARCHAR(20) DEFAULT 'Aprobada'
);

CREATE TABLE evaluaciones_visita (
    id SERIAL PRIMARY KEY,
    asignacion_id INT REFERENCES asignaciones_practica(id),
    numero_visita INT CHECK (numero_visita IN (1, 2, 3)),
    nota_tecnica NUMERIC(3,2) CHECK (nota_tecnica BETWEEN 0.0 AND 5.0),
    nota_etica NUMERIC(3,2) CHECK (nota_etica BETWEEN 0.0 AND 5.0),
    nota_comunicacion NUMERIC(3,2) CHECK (nota_comunicacion BETWEEN 0.0 AND 5.0),
    nota_promedio NUMERIC(3,2),
    observaciones TEXT,
    fecha_evaluacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO usuarios (documento, nombre, email, password_hash, rol) VALUES
('1001', 'Dr. Carlos Mendoza', 'director@udi.edu.co', 'director123', 'Director del Programa'),
('1002', 'Ing. Martha GÃ³mez', 'coordinador@udi.edu.co', 'coordinador123', 'Coordinador de PrÃ¡ctica'),
('1003', 'Prof. Roberto Silva', 'docente@udi.edu.co', 'docente123', 'Docente Asesor'),
('1004', 'Juan Rueda', 'estudiante@udi.edu.co', 'estudiante123', 'Estudiante'),
('9001', 'Tech Solutions S.A.S', 'empresa@techsolutions.com', 'empresa123', 'InstituciÃ³n Receptora');

INSERT INTO periodos_practica (codigo_periodo, fecha_inicio, fecha_fin, horas_requeridas, estado) VALUES
('2026-1', '2026-02-01', '2026-06-30', 320, 'En Curso'),
('2025-2', '2025-08-01', '2025-12-15', 320, 'Finalizado');

INSERT INTO convenios (nit_rut, nombre_empresa, numero_convenio, fecha_inicio, fecha_vencimiento, estado) VALUES
('900123456-1', 'Tech Solutions S.A.S', 'CONV-2025-089', '2025-01-15', '2028-12-31', 'Vigente');

INSERT INTO plazas_practica (convenio_id, titulo_plaza, modalidad, cupos, descripcion, estado) VALUES
(1, 'Desarrollador Junior Fullstack', 'HÃ­brida', 2, 'Desarrollo de microservicios en Node.js y React con PostgreSQL y MongoDB.', 'Disponible');

INSERT INTO solicitudes_practica (estudiante_id, plaza_id, estado) VALUES
(4, 1, 'Aprobada');

INSERT INTO asignaciones_practica (solicitud_id, docente_id, periodo_id, estado) VALUES
(1, 3, 1, 'Aprobada');

INSERT INTO evaluaciones_visita (asignacion_id, numero_visita, nota_tecnica, nota_etica, nota_comunicacion, nota_promedio, observaciones) VALUES
(1, 1, 4.8, 5.0, 4.7, 4.83, 'El practicante demuestra un alto desempeÃ±o tÃ©cnico y excelente compromiso.');