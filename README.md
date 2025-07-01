# 🏥 Sistema Clínico - Youx

Sistema completo de gestão clínica desenvolvido com **Spring Boot** (backend) e **React + TypeScript** (frontend).

## 📁 Estrutura do Projeto

```
desafio-youx/
├── backend/                 # API Spring Boot
│   ├── src/main/java/      # Código Java
│   ├── src/main/resources/ # Recursos e configurações
│   ├── PLANNING.md         # Documentação técnica detalhada
│   └── pom.xml            # Dependências Maven
├── frontend/               # Aplicação React
│   ├── src/               # Código TypeScript/React
│   ├── README.md          # Documentação do frontend
│   └── package.json       # Dependências npm
├── TASK.md                # Registro de tarefas
└── README.md              # Este arquivo
```

## 🚀 Como Executar o Projeto

### 📋 Pré-requisitos

- **Backend**: Java 21, Maven, PostgreSQL
- **Frontend**: Node.js 18+, npm
- **Ferramentas**: nvm (recomendado)

### 🔧 Configuração do Backend

1. **Configurar o banco PostgreSQL**:
```sql
CREATE DATABASE clinica;
CREATE USER clinica_user WITH PASSWORD 'clinica_password';
GRANT ALL PRIVILEGES ON DATABASE clinica TO clinica_user;
```

2. **Executar o backend**:
```bash
cd backend
./mvnw spring-boot:run
```

O backend estará disponível em: `http://localhost:8080`

### 🎨 Configuração do Frontend

1. **Configurar Node.js** (usando nvm):
```bash
nvm install 18
nvm use 18
```

2. **Instalar dependências e executar**:
```bash
cd frontend
npm install
cp .env.example .env
npm run dev
```

O frontend estará disponível em: `http://localhost:3000`

## 🎯 Funcionalidades Implementadas

### ✅ Backend (Spring Boot)
- **Sistema JWT completo** com autenticação e autorização
- **APIs RESTful** para Usuários, Pacientes e Consultas
- **Segurança robusta** com Spring Security
- **Banco PostgreSQL** com Flyway para migrações
- **Validações** Bean Validation em todos os endpoints
- **Documentação técnica** detalhada no PLANNING.md

### ✅ Frontend (React + TypeScript)
- **Interface moderna** com Tailwind CSS
- **Autenticação JWT** com context e interceptors
- **Layout responsivo** com sidebar e dashboard
- **Formulários validados** com React Hook Form + Zod
- **Roteamento protegido** por autenticação
- **Sistema de notificações** com react-hot-toast
- **Utilitários** para formatação e validação

## 🔐 Sistema de Autenticação

### Usuários Padrão
O sistema possui dois tipos de usuários:

- **MEDICO**: Acesso completo ao sistema
- **ENFERMEIRA**: Acesso limitado (sem gerenciar usuários)

### Fluxo de Autenticação
1. Login com CPF e senha
2. Recebimento de token JWT
3. Token armazenado no localStorage
4. Interceptor adiciona token automaticamente nas requisições
5. Redirecionamento automático quando token expira

## 📱 Páginas Implementadas

### 🖥️ Frontend
- **Login** (`/login`) - Autenticação com CPF e senha
- **Dashboard** (`/`) - Visão geral com estatísticas
- **Layout** - Sidebar responsiva com navegação

### 🔮 Próximas Funcionalidades
- **CRUD Pacientes** - Gerenciar cadastro de pacientes
- **CRUD Consultas** - Agendar e gerenciar consultas
- **CRUD Usuários** - Gerenciar usuários do sistema (MEDICO)
- **Calendário** - Vista de consultas agendadas
- **Perfil** - Gerenciar perfil do usuário
- **Relatórios** - Exportação de dados

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 21** - Linguagem principal
- **Spring Boot 3.5.3** - Framework principal
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Acesso a dados
- **PostgreSQL** - Banco de dados
- **Flyway** - Migrações de banco
- **JWT** - Tokens de autenticação
- **Maven** - Gerenciador de dependências

### Frontend
- **React 18** - Biblioteca de interface
- **TypeScript** - Tipagem estática
- **Vite** - Build tool moderna
- **Tailwind CSS** - Framework CSS
- **React Router** - Roteamento
- **Axios** - Cliente HTTP
- **React Hook Form** - Gerenciamento de formulários
- **Zod** - Validação de esquemas
- **Lucide React** - Ícones

## 📊 Status do Projeto

### ✅ Concluído (100%)
- Configuração completa do projeto
- Sistema de autenticação JWT
- Layout e design system
- Páginas base (Login, Dashboard)
- Documentação completa

### 🚧 Em Desenvolvimento (0%)
- CRUDs principais (Pacientes, Consultas, Usuários)
- Calendário de consultas
- Funcionalidades avançadas

## 📚 Documentação

- **[Backend PLANNING.md](backend/PLANNING.md)** - Documentação técnica completa do backend
- **[Frontend README.md](frontend/README.md)** - Documentação detalhada do frontend
- **[TASK.md](TASK.md)** - Registro de tarefas e progresso

## 🧪 Testando o Sistema

1. **Iniciar backend**: `cd backend && ./mvnw spring-boot:run`
2. **Iniciar frontend**: `cd frontend && npm run dev`
3. **Acessar**: `http://localhost:3000`
4. **Testar autenticação**: Criar usuário via API ou usar dados de teste

## 🎨 Design System

### Cores Principais
- **Primary**: Azul (#3B82F6)
- **Secondary**: Cinza (#6B7280)
- **Success**: Verde (#10B981)
- **Warning**: Laranja (#F59E0B)
- **Error**: Vermelho (#EF4444)

### Características
- **Interface limpa** e profissional
- **Responsivo** para desktop e mobile
- **Acessibilidade** considerada no design
- **Tema claro** com opção para escuro

## 🔒 Segurança

- **Tokens JWT** com expiração
- **Interceptors** para tratamento automático
- **Validação** em frontend e backend
- **Sanitização** de dados de entrada
- **Proteção** contra ataques comuns (XSS, CSRF)

## 📈 Métricas

- **Tempo de build frontend**: 128ms
- **Arquivos criados**: 62
- **Linhas de código**: ~2000+
- **Dependências**: 305 (npm) + Maven

## 🚀 Deploy

### Backend
```bash
cd backend
./mvnw clean package
java -jar target/clinica-*.jar
```

### Frontend
```bash
cd frontend
npm run build
# Servir pasta dist/ com servidor web
```

## 👨‍💻 Desenvolvedor

**Youx** - Sistema Clínico  
📅 **Desenvolvido em**: Junho/2025  
🔧 **Status**: Ativo e em expansão

---

Para mais detalhes técnicos, consulte a documentação específica de cada módulo. 