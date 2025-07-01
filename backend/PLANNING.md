# 🏥 Sistema Clínico - Documentação Arquitetural

## 📋 Visão Geral do Sistema

Sistema de gestão clínica desenvolvido em **Spring Boot 3.5.3** com **Java 21**, utilizando:
- **PostgreSQL** como banco de dados
- **Flyway** para migrações
- **JWT** para autenticação
- **Spring Security** para autorização
- **Spring Data JPA** com Hibernate
- **BCrypt** para criptografia de senhas
- **Maven** como gerenciador de dependências

## 🏗️ Arquitetura do Sistema

### Estrutura de Camadas
```
Controller ↔ Service ↔ Repository ↔ Database
     ↕         ↕
   DTO      Entity
     ↕
  Mapper
```

### Padrões Arquiteturais
- **MVC (Model-View-Controller)**
- **Repository Pattern**
- **DTO Pattern**
- **Mapper Pattern**
- **Dependency Injection**
- **RESTful API**

## 🗂️ Estrutura do Projeto

```
src/main/java/br/com/youx/clinica/
├── ClinicaApplication.java          # Classe principal
├── config/                          # Configurações
│   ├── JwtProperties.java          # Propriedades JWT
│   ├── SecurityConfig.java         # Configuração Spring Security
│   └── SecurityProperties.java     # Propriedades de segurança
├── controller/                      # Controllers REST
│   ├── ConsultaController.java
│   ├── PacienteController.java
│   └── UsuarioController.java
├── dto/                            # Data Transfer Objects
│   ├── consulta/
│   ├── paciente/
│   └── usuario/
├── enums/                          # Enumerações
│   └── Role.java
├── mapper/                         # Conversores DTO ↔ Entity
│   ├── ConsultaMapper.java
│   ├── PacienteMapper.java
│   └── UsuarioMapper.java
├── model/                          # Entidades JPA
│   ├── Consulta.java
│   ├── Paciente.java
│   └── Usuario.java
├── repository/                     # Repositórios Spring Data
│   ├── ConsultaRepository.java
│   ├── PacienteRepository.java
│   └── UsuarioRepository.java
├── security/                       # Sistema de segurança JWT
│   ├── JwtAuthenticationFilter.java
│   ├── JwtAuthenticationToken.java
│   └── SecurityUtils.java
└── service/                        # Lógica de negócio
    ├── ConsultaService.java
    ├── JwtService.java
    ├── PacienteService.java
    ├── PasswordService.java
    └── UsuarioService.java
```

## 🎭 Entidades do Sistema

### 1. Usuario (Usuários do Sistema)
```java
- id: Long (PK, auto-increment)
- nome: String (não nulo)
- cpf: String (único, não nulo, exatamente 11 dígitos)
- senha: String (criptografada com BCrypt)
- role: Role (MEDICO, ENFERMEIRA)
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

### 2. Paciente (Pacientes)
```java
- id: Long (PK, auto-increment)
- nome: String (não nulo)
- cpf: String (único, não nulo, exatamente 11 dígitos)
- telefone: String
- email: String
- dataNascimento: LocalDate
- endereco: String
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

### 3. Consulta (Consultas Médicas)
```java
- id: Long (PK, auto-increment)
- paciente: Paciente (FK, relacionamento Many-to-One)
- medico: Usuario (FK, relacionamento Many-to-One)
- dataHora: LocalDateTime (não nulo)
- observacoes: String (texto livre)
- status: String
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

## 🔐 Sistema de Autenticação JWT

### Configuração JWT
```yaml
clinica:
  jwt:
    secret: "clinica-youx-jwt-secret-key-2024-muito-secreta-e-longa"
    expiration: 86400000  # 24 horas em millisegundos
    issuer: "clinica-youx"
```

### Estrutura do Token JWT
```json
{
  "sub": "12345678900",        // CPF do usuário
  "userId": 1,                 // ID do usuário
  "nome": "Dr. João Silva",    // Nome do usuário
  "cpf": "12345678900",       // CPF do usuário
  "role": "MEDICO",           // Role do usuário
  "iss": "clinica-youx",      // Issuer
  "exp": 1640995200           // Timestamp de expiração
}
```

### Endpoints Públicos (Sem Autenticação)
- `POST /api/usuarios/login`
- `POST /api/usuarios/registrar`
- `POST /api/usuarios/validar-credenciais`

### Endpoints Protegidos (Requerem JWT)
- Todos os outros endpoints da API

## 🌐 API REST - Endpoints Disponíveis

### Base URL: `http://localhost:8080/api`

### 👤 Usuários (`/usuarios`)

#### Endpoints Públicos:
```http
POST /usuarios/login
Content-Type: application/json
{
  "cpf": "12345678900",
  "senha": "senha123"
}

Response 200:
{
  "token": "eyJhbGciOiJIUzM4NCJ9...",
  "usuario": {
    "id": 1,
    "nome": "Dr. João Silva",
    "cpf": "12345678900",
    "role": "MEDICO",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
}
```

```http
POST /usuarios/registrar
Content-Type: application/json
{
  "nome": "Dr. João Silva",
  "cpf": "12345678900",
  "senha": "senha123",
  "role": "MEDICO"
}

Response 201:
{
  "id": 1,
  "nome": "Dr. João Silva",
  "cpf": "12345678900",
  "role": "MEDICO",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

#### Endpoints Protegidos:
```http
GET /usuarios/me
Authorization: Bearer {token}

Response 200:
{
  "id": 1,
  "nome": "Dr. João Silva",
  "cpf": "12345678900",
  "role": "MEDICO",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

```http
POST /usuarios/refresh-token
Authorization: Bearer {token}

Response 200:
{
  "token": "eyJhbGciOiJIUzM4NCJ9...",
  "usuario": { ... }
}
```

```http
GET /usuarios/validate-token
Authorization: Bearer {token}

Response 200: { "valid": true }
```

```http
GET /usuarios
Authorization: Bearer {token}

Response 200: [array de usuários]
```

```http
GET /usuarios/{id}
Authorization: Bearer {token}

Response 200: {objeto usuário}
```

```http
PUT /usuarios/{id}
Authorization: Bearer {token}
Content-Type: application/json
{
  "nome": "Dr. João Silva Atualizado",
  "role": "MEDICO"
}
```

```http
DELETE /usuarios/{id}
Authorization: Bearer {token}

Response 204: No Content
```

```http
PUT /usuarios/{id}/alterar-senha
Authorization: Bearer {token}
Content-Type: application/json
{
  "senhaAtual": "senha123",
  "novaSenha": "novaSenha456"
}
```

### 🏥 Pacientes (`/pacientes`)

```http
GET /pacientes
Authorization: Bearer {token}

POST /pacientes
Authorization: Bearer {token}
Content-Type: application/json
{
  "nome": "Maria Silva",
  "cpf": "98765432100",
  "telefone": "(11) 99999-9999",
  "email": "maria@email.com",
  "dataNascimento": "1990-05-15",
  "endereco": "Rua das Flores, 123"
}

GET /pacientes/{id}
Authorization: Bearer {token}

PUT /pacientes/{id}
Authorization: Bearer {token}

DELETE /pacientes/{id}
Authorization: Bearer {token}
```

### 📅 Consultas (`/consultas`)

```http
GET /consultas
Authorization: Bearer {token}

POST /consultas
Authorization: Bearer {token}
Content-Type: application/json
{
  "pacienteId": 1,
  "medicoId": 1,
  "dataHora": "2024-12-31T14:30:00",
  "observacoes": "Consulta de rotina",
  "status": "AGENDADA"
}

GET /consultas/{id}
Authorization: Bearer {token}

PUT /consultas/{id}
Authorization: Bearer {token}

DELETE /consultas/{id}
Authorization: Bearer {token}

GET /consultas/paciente/{pacienteId}
Authorization: Bearer {token}

GET /consultas/medico/{medicoId}
Authorization: Bearer {token}
```

## 🔒 Sistema de Segurança

### Roles Disponíveis
- `MEDICO`: Acesso completo ao sistema
- `ENFERMEIRA`: Acesso limitado (definir permissões específicas no frontend)

### Filtro de Autenticação
- **JwtAuthenticationFilter**: Intercepta todas as requisições
- Valida tokens JWT automaticamente
- Define contexto de segurança Spring
- Permite acesso a endpoints públicos específicos

### Utilitários de Segurança (SecurityUtils)
```java
// Obter usuário atual autenticado
getCurrentUser() -> JwtAuthenticationToken
getCurrentUserId() -> Long
getCurrentUserCpf() -> String

// Verificar roles
isCurrentUserMedico() -> boolean
isCurrentUserEnfermeira() -> boolean
```

## 🗄️ Configuração do Banco de Dados

### PostgreSQL
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/clinica
    username: clinica_user
    password: clinica_password
    driver-class-name: org.postgresql.Driver
```

### Flyway (Migrações)
- Arquivo: `src/main/resources/db/migration/V1__create_initial_tables.sql`
- Cria tabelas: `usuario`, `paciente`, `consulta`
- Executa automaticamente na inicialização

## 🎨 Padrões de Código

### Naming Conventions
- **Classes**: PascalCase (ex: `UsuarioService`)
- **Métodos**: camelCase (ex: `buscarPorId`)
- **Variáveis**: camelCase (ex: `usuarioAtual`)
- **Constantes**: UPPER_SNAKE_CASE (ex: `JWT_SECRET`)
- **Pacotes**: lowercase (ex: `br.com.youx.clinica`)

### DTOs
- **Request**: Dados de entrada (`UsuarioRequestDTO`)
- **Response**: Dados de saída (`UsuarioResponseDTO`)
- Validações Bean Validation (`@NotNull`, `@Size`, etc.)

### Mappers
- Conversão entre DTOs e Entities
- Métodos estáticos para conversão
- Padrão: `toEntity()`, `toResponseDTO()`

## 🚀 Próximos Passos para o Frontend

### Tecnologias Sugeridas
- **React** ou **Vue.js** ou **Angular**
- **TypeScript** para tipagem
- **Axios** para requisições HTTP
- **React Router** ou equivalente para roteamento
- **Context API** ou **Redux** para gerenciamento de estado
- **Material-UI** ou **Tailwind CSS** para estilização

### Funcionalidades Essenciais do Frontend
1. **Tela de Login** (JWT)
2. **Dashboard** principal
3. **CRUD de Usuários** (admin)
4. **CRUD de Pacientes**
5. **CRUD de Consultas**
6. **Calendário de Consultas**
7. **Perfil do Usuário**
8. **Alteração de Senha**

### Estrutura Sugerida do Frontend
```
src/
├── components/          # Componentes reutilizáveis
├── pages/              # Páginas da aplicação
├── services/           # Serviços de API
├── hooks/              # Custom hooks
├── context/            # Contextos React
├── types/              # Tipos TypeScript
├── utils/              # Utilitários
└── styles/             # Estilos globais
```

### Gerenciamento de Estado JWT
```javascript
// Exemplo de estrutura para gerenciar JWT
const AuthContext = {
  token: string | null,
  user: User | null,
  login: (cpf, senha) => Promise<void>,
  logout: () => void,
  refreshToken: () => Promise<void>,
  isAuthenticated: boolean
}
```

### Interceptor para Requisições HTTP
```javascript
// Adicionar token JWT automaticamente
axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  }
);
```

## 🔧 Configurações de Desenvolvimento

### Porta da Aplicação
- **Backend**: `http://localhost:8080`
- **Frontend** (sugestão): `http://localhost:3000`

### CORS (Configurar no Backend)
```java
// Adicionar configuração CORS para desenvolvimento
@CrossOrigin(origins = "http://localhost:3000")
```

### Variáveis de Ambiente
```bash
# Backend (.env ou application.yml)
DATABASE_URL=jdbc:postgresql://localhost:5432/clinica
DATABASE_USERNAME=clinica_user
DATABASE_PASSWORD=clinica_password
JWT_SECRET=clinica-youx-jwt-secret-key-2024-muito-secreta-e-longa
JWT_EXPIRATION=86400000

# Frontend (.env)
REACT_APP_API_URL=http://localhost:8080/api
```

## 📝 Validações e Regras de Negócio

### CPF
- Deve ter exatamente 11 dígitos numéricos
- Único no sistema (usuários e pacientes)
- Formato aceito: apenas números (sem formatação)

### Senhas
- Criptografadas com BCrypt
- Mínimo de caracteres (definir no frontend)

### Consultas
- Data/hora não pode ser no passado
- Um médico não pode ter consultas sobrepostas
- Validar horário de funcionamento da clínica

### Roles e Permissões
- `MEDICO`: Acesso total
- `ENFERMEIRA`: Acesso limitado (definir regras específicas)

## 🧪 Testes (Para Implementar)

### Backend
- Testes unitários (JUnit)
- Testes de integração
- Testes de repository
- Testes de controller (MockMvc)

### Frontend
- Testes de componentes
- Testes de integração
- Testes E2E

---

**📅 Última Atualização**: 30/06/2025
**👨‍💻 Desenvolvido por**: Youx
**🔧 Versão do Sistema**: 1.0.0 