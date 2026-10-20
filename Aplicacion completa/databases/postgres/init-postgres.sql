-- ==============================================================================
-- BASE DE DATOS POSTGRESQL - SIGPRA (MÃ³dulo Relacional)
-- ==============================================================================

CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    documento VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(30) NOT NULL CHECK (rol IN ('Director del Programa', 'Coordinador de PrÃ¡ctica', 'Docente Asesor', 'Estudiante', 'InstituciÃ³n Receptora')),
    estado VARCHAR(20) DEFAULT 'Activo'
);

CREATE TABLE IF NOT EXISTS programas_academicos (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    director_id INT REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS periodos_practica (
    id SERIAL PRIMARY KEY,
    codigo_periodo VARCHAR(20) NOT NULL, -- p.ej. 2026-1
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    horas_requeridas INT NOT NULL DEFAULT 320,
    estado VARCHAR(20) DEFAULT 'Programado'
);

CREATE TABLE IF NOT EXISTS instituciones_convenios (
    id SERIAL PRIMARY KEY,
    nit_rut VARCHAR(30) UNIQUE NOT NULL,
    nombre_empresa VARCHAR(150) NOT NULL,
    numero_convenio VARCHAR(50) NOT NULL,
    fecha_vencimiento_convenio DATE NOT NULL,
    contacto_nombre VARCHAR(100),
    contacto_email VARCHAR(100),
    estado VARCHAR(20) DEFAULT 'Vigente'
);

CREATE TABLE IF NOT EXISTS plazas_practica (
    id SERIAL PRIMARY KEY,
    institucion_id INT REFERENCES instituciones_convenios(id),
    titulo_plaza VARCHAR(120) NOT NULL,
    descripcion TEXT,
    cupos_disponibles INT DEFAULT 1,
    modalidad VARCHAR(30) DEFAULT 'Presencial',
    estado VARCHAR(20) DEFAULT 'Disponible'
);

CREATE TABLE IF NOT EXISTS asignaciones_practica (
    id SERIAL PRIMARY KEY,
    estudiante_id INT REFERENCES usuarios(id),
    plaza_id INT REFERENCES plazas_practica(id),
    docente_id INT REFERENCES usuarios(id),
    periodo_id INT REFERENCES periodos_practica(id),
    fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado_solicitud VARCHAR(30) DEFAULT 'Asignada' -- Pendiente, Aprobada, Rechazada, Asignada
);

CREATE TABLE IF NOT EXISTS rubricas_evaluacion (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    porcentaje_peso DECIMAL(5,2) DEFAULT 100.00
);

CREATE TABLE IF NOT EXISTS criterios_rubrica (
    id SERIAL PRIMARY KEY,
    rubrica_id INT REFERENCES rubricas_evaluacion(id),
    criterio VARCHAR(150) NOT NULL,
    puntaje_maximo DECIMAL(4,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS visitas_evaluaciones (
    id SERIAL PRIMARY KEY,
    asignacion_id INT REFERENCES asignaciones_practica(id),
    docente_id INT REFERENCES usuarios(id),
    numero_visita INT NOT NULL,
    fecha_visita DATE NOT NULL,
    observaciones_pedagogicas TEXT,
    calificacion_final DECIMAL(4,2),
    estado VARCHAR(20) DEFAULT 'Completada'
);

-- Datos Semilla (Seed Data)
INSERT INTO usuarios (documento, nombre, email, password_hash, rol) VALUES
('1001', 'Dr. Carlos Mendoza', 'director@udi.edu.co', 'hash123', 'Director del Programa'),
('1002', 'Ing. Martha GÃ³mez', 'coordinador@udi.edu.co', 'hash123', 'Coordinador de PrÃ¡ctica'),
('1003', 'Prof. Roberto Silva', 'docente@udi.edu.co', 'hash123', 'Docente Asesor'),
('1004', 'Juan Rueda', 'estudiante@udi.edu.co', 'hash123', 'Estudiante'),
('1005', 'Tech Solutions S.A.S', 'contacto@techsolutions.com', 'hash123', 'InstituciÃ³n Receptora')
ON CONFLICT DO NOTHING;

INSERT INTO periodos_practica (codigo_periodo, fecha_inicio, fecha_fin, horas_requeridas, estado) VALUES
('2026-1', '2026-02-01', '2026-06-30', 320, 'En Curso')
ON CONFLICT DO NOTHING;

INSERT INTO instituciones_convenios (nit_rut, nombre_empresa, numero_convenio, fecha_vencimiento_convenio, contacto_nombre, contacto_email) VALUES
('900123456-1', 'Tech Solutions S.A.S', 'CONV-2025-089', '2028-12-31', 'Ana MartÃ­nez', 'contacto@techsolutions.com')
ON CONFLICT DO NOTHING;

INSERT INTO plazas_practica (institucion_id, titulo_plaza, descripcion, cupos_disponibles, modalidad) VALUES
(1, 'Desarrollador Junior Fullstack', 'Desarrollo de microservicios y portales web en Node.js y PostgreSQL/MongoDB', 2, 'HÃ­brida')
ON CONFLICT DO NOTHING;

INSERT INTO asignaciones_practica (estudiante_id, plaza_id, docente_id, periodo_id, estado_solicitud) VALUES
(4, 1, 3, 1, 'Aprobada')
ON CONFLICT DO NOTHING;
