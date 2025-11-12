-- SCHEMA COMPLETO

CREATE TABLE perfil (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE,
    descricao VARCHAR(255)
);

CREATE TABLE funcionario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    data_contratacao DATE NOT NULL,
    registro_profissional VARCHAR(100),
    ativo BOOLEAN DEFAULT TRUE,
    id_perfil INTEGER NOT NULL,
    FOREIGN KEY (id_perfil) REFERENCES perfil(id) ON DELETE RESTRICT
);

CREATE TABLE log_acesso (
    id SERIAL PRIMARY KEY,
    login_tentativa VARCHAR(50) NOT NULL,
    data_hora TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    sucesso BOOLEAN NOT NULL,
    ip_acesso VARCHAR(45)
);

CREATE TABLE tutor (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    endereco VARCHAR(255) NOT NULL,
    cpf VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE especie (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE raca (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    id_especie INTEGER NOT NULL,
    FOREIGN KEY (id_especie) REFERENCES especie(id) ON DELETE RESTRICT,
    UNIQUE (nome, id_especie)
);

CREATE TABLE animal (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    data_nascimento DATE,
    peso NUMERIC(6, 2),
    observacoes TEXT,
    foto_url VARCHAR(255),
    id_tutor INTEGER NOT NULL,
    id_raca INTEGER NOT NULL,
    FOREIGN KEY (id_tutor) REFERENCES tutor(id) ON DELETE CASCADE,
    FOREIGN KEY (id_raca) REFERENCES raca(id) ON DELETE RESTRICT
);

CREATE TABLE prontuario (
    id SERIAL PRIMARY KEY,
    id_animal INTEGER NOT NULL UNIQUE,
    data_criacao DATE DEFAULT CURRENT_DATE,
    observacoes TEXT,                           -- <<<<<<<<<< AQUI
    FOREIGN KEY (id_animal) REFERENCES animal(id) ON DELETE CASCADE
);

CREATE TABLE consulta (
    id SERIAL PRIMARY KEY,
    data_agendamento TIMESTAMP WITH TIME ZONE NOT NULL,
    observacoes_clinicas TEXT,
    realizada BOOLEAN DEFAULT FALSE,
    id_prontuario INTEGER NOT NULL,
    id_veterinario INTEGER NOT NULL,
    FOREIGN KEY (id_prontuario) REFERENCES prontuario(id) ON DELETE CASCADE,
    FOREIGN KEY (id_veterinario) REFERENCES funcionario(id) ON DELETE RESTRICT
);

CREATE TABLE exame (
    id SERIAL PRIMARY KEY,
    tipo_exame VARCHAR(100) NOT NULL,
    data_exame DATE NOT NULL,
    resultado TEXT,
    documento_anexo_url VARCHAR(255),
    id_prontuario INTEGER NOT NULL,
    id_veterinario INTEGER NOT NULL,
    FOREIGN KEY (id_prontuario) REFERENCES prontuario(id) ON DELETE CASCADE,
    FOREIGN KEY (id_veterinario) REFERENCES funcionario(id) ON DELETE RESTRICT
);

CREATE TABLE vacinacao (
    id SERIAL PRIMARY KEY,
    tipo_vacina VARCHAR(100) NOT NULL,
    data_aplicacao DATE NOT NULL,
    lote VARCHAR(50),
    id_prontuario INTEGER NOT NULL,
    id_funcionario_aplicador INTEGER NOT NULL,
    FOREIGN KEY (id_prontuario) REFERENCES prontuario(id) ON DELETE CASCADE,
    FOREIGN KEY (id_funcionario_aplicador) REFERENCES funcionario(id) ON DELETE RESTRICT
);

CREATE TABLE convenio (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao TEXT,
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE financeiro_movimentacao (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    valor NUMERIC(10, 2) NOT NULL,
    tipo CHAR(1) NOT NULL CHECK (tipo IN ('R', 'D')),
    data_movimentacao TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    id_funcionario_responsavel INTEGER NOT NULL,
    FOREIGN KEY (id_funcionario_responsavel) REFERENCES funcionario(id) ON DELETE RESTRICT
);

CREATE TABLE pagamento (
    id SERIAL PRIMARY KEY,
    valor_pago NUMERIC(10, 2) NOT NULL,
    data_pagamento DATE NOT NULL,
    forma_pagamento VARCHAR(50) NOT NULL,
    id_tutor INTEGER NOT NULL,
    id_movimentacao INTEGER NOT NULL UNIQUE,
    id_consulta INTEGER,
    FOREIGN KEY (id_tutor) REFERENCES tutor(id) ON DELETE RESTRICT,
    FOREIGN KEY (id_movimentacao) REFERENCES financeiro_movimentacao(id) ON DELETE CASCADE,
    FOREIGN KEY (id_consulta) REFERENCES consulta(id) ON DELETE SET NULL
);

INSERT INTO perfil (nome, descricao) VALUES
('ADMINISTRADOR', 'Acesso total ao sistema, incluindo financeiro e gerenciamento de usuários.'),
('FUNCIONARIO', 'Acesso a agendamentos, cadastros de tutores e animais.'),
('VETERINARIO', 'Acesso a prontuários, consultas, exames e vacinas.');

INSERT INTO funcionario (nome, login, senha, email, data_contratacao, registro_profissional, id_perfil) VALUES
('Admin User', 'admin', '123456', 'admin@petsystem.com', '2023-01-01', NULL, (SELECT id FROM perfil WHERE nome = 'ADMINISTRADOR')),
('Laura Vitoria', 'laura', '123456', 'laura@petsystem.com', '2023-01-15', NULL, (SELECT id FROM perfil WHERE nome = 'FUNCIONARIO')),
('Dr. Carlos Santos', 'carlos', '123456', 'carlos@petsystem.com', '2023-01-10', 'CRMV-RS 12345', (SELECT id FROM perfil WHERE nome = 'VETERINARIO'));

INSERT INTO especie (nome) VALUES ('Cachorro'), ('Gato'), ('Ave');

INSERT INTO raca (nome, id_especie) VALUES
('Labrador', (SELECT id FROM especie WHERE nome = 'Cachorro')),
('Poodle', (SELECT id FROM especie WHERE nome = 'Cachorro')),
('Siamês', (SELECT id FROM especie WHERE nome = 'Gato')),
('Persa', (SELECT id FROM especie WHERE nome = 'Gato')),
('Papagaio', (SELECT id FROM especie WHERE nome = 'Ave'));

ALTER TABLE vacinacao ADD COLUMN observacoes TEXT;
ALTER TABLE exame ADD COLUMN observacoes TEXT;
ALTER TABLE pagamento ADD COLUMN forma varchar(20) NOT NULL DEFAULT 'DINHEIRO';
ALTER TABLE pagamento ADD COLUMN IF NOT EXISTS valor NUMERIC(12,2);



CREATE INDEX idx_funcionario_perfil ON funcionario (id_perfil);
CREATE INDEX idx_animal_tutor ON animal (id_tutor);
CREATE INDEX idx_animal_raca ON animal (id_raca);
CREATE INDEX idx_raca_especie ON raca (id_especie);
CREATE INDEX idx_prontuario_animal ON prontuario (id_animal);
CREATE INDEX idx_consulta_prontuario ON consulta (id_prontuario);
CREATE INDEX idx_consulta_veterinario ON consulta (id_veterinario);
CREATE INDEX idx_exame_prontuario ON exame (id_prontuario);
CREATE INDEX idx_vacinacao_prontuario ON vacinacao (id_prontuario);
CREATE INDEX idx_pagamento_tutor ON pagamento (id_tutor);


