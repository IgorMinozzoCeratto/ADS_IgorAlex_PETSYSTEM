-- Script SQL para PostgreSQL - PetSystem
-- Criação do banco de dados e tabelas para o sistema de clínica veterinária

-- Criação do banco de dados
-- CREATE DATABASE petsystem_db;

-- Conectar ao banco de dados
-- \c petsystem_db

-- Tabela de Funcionários (inclui veterinários)
CREATE TABLE funcionario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    telefone VARCHAR(20),
    tipo VARCHAR(20) NOT NULL, -- FUNCIONARIO_1, FUNCIONARIO_2, VETERINARIO
    crmv VARCHAR(20), -- Número de registro para veterinários
    data_contratacao DATE,
    ativo BOOLEAN DEFAULT TRUE
);

-- Tabela de Tutores (donos dos animais)
CREATE TABLE tutor (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14),
    email VARCHAR(100),
    telefone VARCHAR(20) NOT NULL,
    endereco VARCHAR(200),
    senha VARCHAR(100),
    data_cadastro DATE DEFAULT CURRENT_DATE
);

-- Tabela de Animais
CREATE TABLE animal (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    especie VARCHAR(50) NOT NULL, -- Enum: CACHORRO, GATO, AVE, ROEDOR, REPTIL, OUTRO
    raca VARCHAR(100),
    data_nascimento DATE,
    peso NUMERIC(6,2),
    observacoes TEXT,
    id_tutor INTEGER NOT NULL,
    FOREIGN KEY (id_tutor) REFERENCES tutor(id)
);

-- Tabela de Consultas
CREATE TABLE consulta (
    id SERIAL PRIMARY KEY,
    data_consulta DATE NOT NULL,
    hora_consulta TIME NOT NULL,
    observacoes TEXT,
    realizada BOOLEAN DEFAULT FALSE,
    diagnostico TEXT,
    tratamento TEXT,
    id_animal INTEGER NOT NULL,
    id_funcionario INTEGER NOT NULL,
    FOREIGN KEY (id_animal) REFERENCES animal(id),
    FOREIGN KEY (id_funcionario) REFERENCES funcionario(id)
);

-- Tabela de Vacinação
CREATE TABLE vacinacao (
    id SERIAL PRIMARY KEY,
    tipo_vacina VARCHAR(100) NOT NULL,
    data_aplicacao DATE NOT NULL,
    lote VARCHAR(50),
    observacoes TEXT,
    id_animal INTEGER NOT NULL,
    id_funcionario INTEGER NOT NULL,
    FOREIGN KEY (id_animal) REFERENCES animal(id),
    FOREIGN KEY (id_funcionario) REFERENCES funcionario(id)
);

-- Tabela de Exames
CREATE TABLE exame (
    id SERIAL PRIMARY KEY,
    tipo_exame VARCHAR(100) NOT NULL,
    data_exame DATE NOT NULL,
    resultado TEXT,
    observacoes TEXT,
    id_animal INTEGER NOT NULL,
    id_funcionario INTEGER NOT NULL,
    FOREIGN KEY (id_animal) REFERENCES animal(id),
    FOREIGN KEY (id_funcionario) REFERENCES funcionario(id)
);

-- Inserção de dados iniciais para teste

-- Funcionários
INSERT INTO funcionario (nome, login, senha, email, telefone, tipo, crmv, data_contratacao)
VALUES 
('João Silva', 'joao', '123456', 'joao@petsystem.com', '(54) 99999-1111', 'FUNCIONARIO_1', NULL, '2023-01-15'),
('Maria Oliveira', 'maria', '123456', 'maria@petsystem.com', '(54) 99999-2222', 'FUNCIONARIO_2', NULL, '2023-02-20'),
('Dr. Carlos Santos', 'carlos', '123456', 'carlos@petsystem.com', '(54) 99999-3333', 'VETERINARIO', 'CRMV-12345', '2023-01-10');

-- Tutores
INSERT INTO tutor (nome, cpf, email, telefone, endereco, senha)
VALUES 
('Ana Pereira', '123.456.789-00', 'ana@email.com', '(54) 98888-1111', 'Rua das Flores, 123', '123456'),
('Pedro Souza', '987.654.321-00', 'pedro@email.com', '(54) 98888-2222', 'Av. Principal, 456', '123456');

-- Animais
INSERT INTO animal (nome, especie, raca, data_nascimento, peso, observacoes, id_tutor)
VALUES 
('Rex', 'CACHORRO', 'Labrador', '2020-05-10', 25.5, 'Alérgico a alguns medicamentos', 1),
('Mia', 'GATO', 'Siamês', '2021-03-15', 4.2, 'Muito dócil', 1),
('Thor', 'CACHORRO', 'Golden Retriever', '2019-11-20', 30.0, 'Tem problema na pata traseira direita', 2);

-- Consultas
INSERT INTO consulta (data_consulta, hora_consulta, observacoes, realizada, diagnostico, tratamento, id_animal, id_funcionario)
VALUES 
('2023-06-10', '14:30:00', 'Consulta de rotina', TRUE, 'Animal saudável', 'Manter alimentação balanceada', 1, 3),
('2023-06-15', '10:00:00', 'Animal com tosse', TRUE, 'Infecção respiratória leve', 'Antibiótico por 7 dias', 2, 3),
('2023-07-05', '16:00:00', 'Verificar pata machucada', FALSE, NULL, NULL, 3, 3);

-- Vacinação
INSERT INTO vacinacao (tipo_vacina, data_aplicacao, lote, observacoes, id_animal, id_funcionario)
VALUES 
('V10', '2023-05-20', 'L123456', 'Vacina anual', 1, 3),
('Antirrábica', '2023-05-20', 'L789012', 'Vacina anual', 1, 3),
('V4', '2023-04-15', 'L456789', 'Primeira dose', 2, 3);

-- Exames
INSERT INTO exame (tipo_exame, data_exame, resultado, observacoes, id_animal, id_funcionario)
VALUES 
('Hemograma Completo', '2023-06-10', 'Resultados dentro da normalidade', 'Exame de rotina', 1, 3),
('Raio-X', '2023-06-15', 'Pulmões sem alterações significativas', 'Realizado devido à tosse', 2, 3);
