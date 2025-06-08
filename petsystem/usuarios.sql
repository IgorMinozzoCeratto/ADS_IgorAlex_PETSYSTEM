DELETE FROM funcionario WHERE login IN ('joao', 'maria', 'carlos');

INSERT INTO funcionario (nome, login, senha, email, telefone, tipo, crmv, data_contratacao)
VALUES 
('João Silva', 'joao', '123456', 'joao@petsystem.com', '(54) 99999-1111', 'FUNCIONARIO_1', NULL, '2023-01-15'),
('Maria Oliveira', 'maria', '123456', 'maria@petsystem.com', '(54) 99999-2222', 'FUNCIONARIO_2', NULL, '2023-02-20'),
('Dr. Carlos Santos', 'carlos', '123456', 'carlos@petsystem.com', '(54) 99999-3333', 'VETERINARIO', 'CRMV-12345', '2023-01-10');

SELECT id, nome, login, senha, tipo FROM funcionario;
