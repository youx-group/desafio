# 🏥 Sistema Clínico - Frontend

Frontend do sistema clínico desenvolvido em React com TypeScript, seguindo as especificações do backend Spring Boot.

## 🚀 Tecnologias Utilizadas

- **React 18** - Biblioteca para construção da interface
- **TypeScript** - Tipagem estática para JavaScript
- **Vite** - Build tool moderna e rápida
- **Tailwind CSS** - Framework CSS utilitário
- **React Router DOM** - Roteamento da aplicação
- **Axios** - Cliente HTTP para comunicação com API
- **React Hook Form** - Gerenciamento de formulários
- **Zod** - Validação de esquemas
- **Lucide React** - Ícones modernos
- **React Hot Toast** - Notificações elegantes
- **Date-fns** - Manipulação de datas

## 📁 Estrutura do Projeto

```
src/
├── components/          # Componentes reutilizáveis
│   └── Layout.tsx      # Layout principal da aplicação
├── context/            # Contextos React
│   └── AuthContext.tsx # Contexto de autenticação JWT
├── pages/              # Páginas da aplicação
│   ├── Login.tsx       # Página de login
│   └── Dashboard.tsx   # Dashboard principal
├── services/           # Serviços de API
│   └── api.ts         # Configuração Axios e serviços
├── types/              # Tipos TypeScript
│   └── index.ts       # Interfaces e types
├── utils/              # Funções utilitárias
│   └── index.ts       # Helpers e formatadores
├── styles/             # Estilos globais
│   └── globals.css    # CSS global com Tailwind
├── App.tsx            # Componente principal
└── main.tsx           # Ponto de entrada da aplicação
```

## 🔧 Configuração do Ambiente

### Pré-requisitos

- Node.js >= 18.0.0
- npm >= 8.0.0

### Instalação

```bash
# Clonar o repositório
git clone <url-do-repositorio>
cd frontend

# Instalar dependências
npm install

# Configurar variáveis de ambiente
cp .env.example .env

# Editar o arquivo .env com as configurações corretas
VITE_API_URL=http://localhost:8080/api
```

### Scripts Disponíveis

```bash
# Desenvolvimento
npm run dev

# Build para produção
npm run build

# Preview do build
npm run preview

# Linting
npm run lint
```

## 🔐 Autenticação

O sistema utiliza JWT (JSON Web Tokens) para autenticação:

- **Token armazenado no localStorage**
- **Interceptor Axios para adicionar token automaticamente**
- **Redirecionamento automático para login quando token expira**
- **Contexto React para gerenciar estado de autenticação**

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

## 👤 Roles e Permissões

### MEDICO
- Acesso completo ao sistema
- Gerenciar pacientes
- Gerenciar consultas
- Gerenciar usuários
- Visualizar relatórios

### ENFERMEIRA
- Acesso limitado
- Gerenciar pacientes
- Gerenciar consultas
- Sem acesso ao gerenciamento de usuários

## 📱 Funcionalidades Implementadas

### ✅ Já Implementadas

- **Login com CPF e senha**
- **Dashboard com estatísticas**
- **Autenticação JWT completa**
- **Layout responsivo**
- **Proteção de rotas**
- **Notificações toast**
- **Formatação automática de CPF**
- **Validação de formulários**

### 🚧 A Implementar

- **CRUD de Pacientes**
- **CRUD de Consultas**
- **CRUD de Usuários (apenas MEDICO)**
- **Calendário de Consultas**
- **Perfil do Usuário**
- **Alteração de Senha**
- **Relatórios**
- **Notificações em tempo real**

## 🎨 Design System

### Cores Principais

- **Primary**: Azul (#3B82F6)
- **Secondary**: Cinza (#6B7280)
- **Success**: Verde (#10B981)
- **Warning**: Laranja (#F59E0B)
- **Error**: Vermelho (#EF4444)

### Componentes Estilizados

```css
/* Botões */
.btn              # Base button
.btn-primary      # Botão primário
.btn-secondary    # Botão secundário
.btn-outline      # Botão outline
.btn-ghost        # Botão fantasma

/* Inputs */
.input            # Input padrão

/* Cards */
.card             # Container card
.card-header      # Cabeçalho do card
.card-content     # Conteúdo do card

/* Tabelas */
.table            # Tabela
.table-row        # Linha da tabela
.table-cell       # Célula da tabela
```

## 🔄 Integração com Backend

### Base URL da API
```
http://localhost:8080/api
```

### Endpoints Utilizados

```typescript
// Autenticação
POST /usuarios/login
POST /usuarios/refresh-token
GET  /usuarios/validate-token
GET  /usuarios/me

// Usuários
GET    /usuarios
POST   /usuarios/registrar
GET    /usuarios/{id}
PUT    /usuarios/{id}
DELETE /usuarios/{id}
PUT    /usuarios/{id}/alterar-senha

// Pacientes
GET    /pacientes
POST   /pacientes
GET    /pacientes/{id}
PUT    /pacientes/{id}
DELETE /pacientes/{id}

// Consultas
GET    /consultas
POST   /consultas
GET    /consultas/{id}
PUT    /consultas/{id}
DELETE /consultas/{id}
GET    /consultas/paciente/{pacienteId}
GET    /consultas/medico/{medicoId}
```

## 🛠️ Utilitários

### Formatação

```typescript
formatCPF(cpf: string)           // Formatar CPF
formatPhone(phone: string)       // Formatar telefone
formatDate(date: string)         // Formatar data
formatDateTime(dateTime: string) // Formatar data/hora
```

### Validação

```typescript
isValidCPF(cpf: string)          // Validar CPF
isValidEmail(email: string)      // Validar email
```

### Helpers

```typescript
capitalizeWords(str: string)     // Capitalizar palavras
truncateText(text: string, max)  // Truncar texto
removeAccents(str: string)       // Remover acentos
getInitials(name: string)        // Obter iniciais
stringToColor(str: string)       // Gerar cor por string
```

## 🔍 Estrutura de Dados

### Usuario
```typescript
interface Usuario {
  id: number;
  nome: string;
  cpf: string;
  role: 'MEDICO' | 'ENFERMEIRA';
  createdAt: string;
  updatedAt: string;
}
```

### Paciente
```typescript
interface Paciente {
  id: number;
  nome: string;
  cpf: string;
  telefone?: string;
  email?: string;
  dataNascimento?: string;
  endereco?: string;
  createdAt: string;
  updatedAt: string;
}
```

### Consulta
```typescript
interface Consulta {
  id: number;
  paciente: Paciente;
  medico: Usuario;
  dataHora: string;
  observacoes?: string;
  status?: string;
  createdAt: string;
  updatedAt: string;
}
```

## 🧪 Desenvolvimento

### Executar em Desenvolvimento

```bash
npm run dev
```

A aplicação estará disponível em: `http://localhost:3000`

### Build para Produção

```bash
npm run build
```

### Preview da Produção

```bash
npm run preview
```

## 📝 Padrões de Código

- **Componentes em PascalCase**
- **Arquivos em camelCase**
- **Funções em camelCase**
- **Constantes em UPPER_SNAKE_CASE**
- **Comentários em português**
- **Commits em português**

## 🐛 Debugging

### Logs de Desenvolvimento

```typescript
// Habilitar logs da API
localStorage.setItem('debug', 'api:*');

// Ver estado de autenticação
localStorage.getItem('token');
localStorage.getItem('user');
```

## 🔒 Segurança

- **Tokens JWT com expiração**
- **Validação de entrada em todos os formulários**
- **Sanitização de dados**
- **Proteção contra XSS**
- **Interceptors para tratamento de erros**

## 🚀 Deploy

### Variáveis de Ambiente para Produção

```env
VITE_API_URL=https://api.clinica.com/api
```

### Build e Deploy

```bash
npm run build
# Servir pasta dist/ com servidor web
```

---

**🏥 Sistema Clínico - Youx**  
**Versão**: 1.0.0  
**Desenvolvido com**: ❤️ e ☕ 