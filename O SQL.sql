-- Script SQL para PostgreSQL - PetSystem (Completo e Corrigido)

-- Apagar tabelas existentes (se houver) na ordem correta para evitar erros de FK
DROP TABLE IF EXISTS vacinacao;
DROP TABLE IF EXISTS exame;
DROP TABLE IF EXISTS consulta;
DROP TABLE IF EXISTS animal;
DROP TABLE IF EXISTS funcionario;
DROP TABLE IF EXISTS tutor;

-- Criação da tabela tutor
CREATE TABLE tutor (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(15) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    endereco VARCHAR(100) NOT NULL,
    cpf VARCHAR(20) UNIQUE,
    senha VARCHAR(100) -- Senha pode ser NULL conforme entidade
);

-- Criação da tabela funcionario
CREATE TABLE funcionario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL, -- FUNCIONARIO_1, FUNCIONARIO_2, VETERINARIO
    email VARCHAR(100) NOT NULL UNIQUE,
    data_contratacao DATE NOT NULL,
    registro_profissional VARCHAR(100) -- Coluna que faltava
);

-- Criação da tabela animal
CREATE TABLE animal (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    especie VARCHAR(50) NOT NULL, -- CACHORRO, GATO, AVE, ROEDOR, REPTIL, OUTRO
    raca VARCHAR(50),
    data_nascimento DATE,
    peso DOUBLE PRECISION,
    observacoes VARCHAR(500),
    id_tutor INTEGER NOT NULL,
    FOREIGN KEY (id_tutor) REFERENCES tutor(id) ON DELETE CASCADE
);

-- Criação da tabela consulta
CREATE TABLE consulta (
    id SERIAL PRIMARY KEY,
    data_consulta DATE NOT NULL,
    hora_consulta TIME NOT NULL,
    observacoes VARCHAR(500),
    realizada BOOLEAN,
    id_animal INTEGER NOT NULL,
    id_funcionario INTEGER NOT NULL,
    FOREIGN KEY (id_animal) REFERENCES animal(id) ON DELETE CASCADE,
    FOREIGN KEY (id_funcionario) REFERENCES funcionario(id) ON DELETE RESTRICT
);

-- Criação da tabela exame
CREATE TABLE exame (
    id SERIAL PRIMARY KEY,
    tipo_exame VARCHAR(100) NOT NULL,
    data_exame DATE NOT NULL,
    resultado VARCHAR(1000),
    observacoes VARCHAR(500),
    id_animal INTEGER NOT NULL,
    id_funcionario INTEGER NOT NULL,
    FOREIGN KEY (id_animal) REFERENCES animal(id) ON DELETE CASCADE,
    FOREIGN KEY (id_funcionario) REFERENCES funcionario(id) ON DELETE RESTRICT
);

-- Criação da tabela vacinacao
CREATE TABLE vacinacao (
    id SERIAL PRIMARY KEY,
    tipo_vacina VARCHAR(100) NOT NULL,
    data_aplicacao DATE NOT NULL,
    lote VARCHAR(50),
    observacoes VARCHAR(500),
    id_animal INTEGER NOT NULL,
    id_funcionario INTEGER NOT NULL,
    FOREIGN KEY (id_animal) REFERENCES animal(id) ON DELETE CASCADE,
    FOREIGN KEY (id_funcionario) REFERENCES funcionario(id) ON DELETE RESTRICT
);

-- Inserção de dados iniciais para teste (Funcionários)
INSERT INTO funcionario (nome, login, senha, email, tipo, data_contratacao, registro_profissional)
VALUES 
('João Silva', 'joao', '123456', 'joao@petsystem.com', 'FUNCIONARIO_1', '2023-01-15', NULL),
('Laura Vitoria', 'laura', '123456', 'laura@petsystem.com', 'FUNCIONARIO_2', '2023-02-20', NULL),
('Dr. Carlos Santos', 'carlos', '123456', 'carlos@petsystem.com', 'VETERINARIO', '2023-01-10', 'CRMV-RS 12345');

-- Inserção de dados iniciais para teste (Tutores)
INSERT INTO tutor (nome, telefone, email, endereco, cpf)
VALUES
('Ana Souza', '(54) 98888-1111', 'ana.souza@email.com', 'Rua das Flores, 123', '111.222.333-44'),
('Pedro Lima', '(54) 97777-2222', 'pedro.lima@email.com', 'Avenida Central, 456', '555.666.777-88');

-- Inserção de dados iniciais para teste (Animais)
INSERT INTO animal (nome, especie, raca, data_nascimento, peso, id_tutor)
VALUES
('Rex', 'CACHORRO', 'Labrador', '2022-05-10', 25.5, (SELECT id FROM tutor WHERE email = 'ana.souza@email.com')),
('Mimi', 'GATO', 'Siamês', '2021-11-20', 4.2, (SELECT id FROM tutor WHERE email = 'pedro.lima@email.com')),
('Loro', 'AVE', 'Papagaio', NULL, 0.5, (SELECT id FROM tutor WHERE email = 'ana.souza@email.com'));

-- Adicionar índices pode ser útil para performance
CREATE INDEX idx_animal_tutor ON animal (id_tutor);
CREATE INDEX idx_consulta_animal ON consulta (id_animal);
CREATE INDEX idx_consulta_funcionario ON consulta (id_funcionario);
CREATE INDEX idx_exame_animal ON exame (id_animal);
CREATE INDEX idx_exame_funcionario ON exame (id_funcionario);
CREATE INDEX idx_vacinacao_animal ON vacinacao (id_animal);
CREATE INDEX idx_vacinacao_funcionario ON vacinacao (id_funcionario);


