-- Criação da tabela de usuários
CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('MEDICO', 'ENFERMEIRA')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Criação da tabela de pacientes
CREATE TABLE paciente (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    data_de_nascimento DATE NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Criação da tabela de consultas
CREATE TABLE consulta (
    id BIGSERIAL PRIMARY KEY,
    id_paciente BIGINT NOT NULL,
    id_medico BIGINT NOT NULL,
    data TIMESTAMP NOT NULL,
    observacao TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_consulta_paciente FOREIGN KEY (id_paciente) REFERENCES paciente(id),
    CONSTRAINT fk_consulta_medico FOREIGN KEY (id_medico) REFERENCES usuario(id)
);

-- Índices para melhor performance
CREATE INDEX idx_usuario_cpf ON usuario(cpf);
CREATE INDEX idx_paciente_cpf ON paciente(cpf);
CREATE INDEX idx_consulta_paciente ON consulta(id_paciente);
CREATE INDEX idx_consulta_medico ON consulta(id_medico);
CREATE INDEX idx_consulta_data ON consulta(data); 