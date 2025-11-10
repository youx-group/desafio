# Sistema Clínico - Frontend Vue

Frontend Vue.js para o Sistema Clínico de Gerenciamento de Pacientes e Consultas.

## 🚀 Tecnologias

- **Vue 3** - Framework JavaScript progressivo
- **TypeScript** - Superset tipado do JavaScript
- **Vite** - Build tool e dev server extremamente rápido
- **Vue Router** - Roteamento oficial para Vue.js
- **Pinia** - Store oficial para gerenciamento de estado
- **Axios** - Cliente HTTP para requisições à API
- **Tailwind CSS** - Framework CSS utility-first
- **Lucide Vue Next** - Biblioteca de ícones
- **Vue Toastification** - Notificações toast elegantes

## 📋 Pré-requisitos

- Node.js 16+ instalado
- NPM ou Yarn
- Backend da aplicação rodando em `http://localhost:8080`

## 🔧 Instalação

1. Clone o repositório (se ainda não o fez):
```bash
cd /home/youx/projetos/desafio/frontend-vue
```

2. Instale as dependências:
```bash
npm install
```

3. Configure as variáveis de ambiente:
```bash
cp .env.example .env
```

Edite o arquivo `.env-exemple` se necessário para apontar para sua API:
```env
VITE_API_URL=http://localhost:8080/api/v1
```

## 🎯 Scripts Disponíveis

### Desenvolvimento
```bash
npm run dev
```
Inicia o servidor de desenvolvimento em `http://localhost:3001`

### Build de Produção
```bash
npm run build
```
Gera os arquivos otimizados para produção na pasta `dist/`

### Preview da Build
```bash
npm run preview
```
Visualiza a build de produção localmente

### Lint
```bash
npm run lint
```
Executa o linter para verificar e corrigir problemas no código

## 📁 Estrutura do Projeto

```
frontend-vue/
├── src/
│   ├── components/         # Componentes reutilizáveis
│   │   ├── Layout.vue     # Layout principal com sidebar e header
│   │   ├── Modal.vue      # Componente de modal reutilizável
│   │   ├── PacienteForm.vue   # Formulário de paciente
│   │   └── UsuarioForm.vue    # Formulário de usuário
│   ├── pages/             # Páginas da aplicação
│   │   ├── Login.vue      # Página de login
│   │   ├── Dashboard.vue  # Dashboard principal
│   │   ├── Pacientes.vue  # Gerenciamento de pacientes
│   │   ├── Consultas.vue  # Gerenciamento de consultas
│   │   ├── Usuarios.vue   # Gerenciamento de usuários
│   │   └── Perfil.vue     # Perfil do usuário
│   ├── router/            # Configuração de rotas
│   │   └── index.ts       # Rotas e guards de autenticação
│   ├── stores/            # Stores Pinia
│   │   └── auth.ts        # Store de autenticação
│   ├── services/          # Serviços da aplicação
│   │   └── api.ts         # Cliente HTTP e endpoints
│   ├── types/             # Definições de tipos TypeScript
│   │   └── index.ts       # Interfaces e enums
│   ├── utils/             # Utilitários
│   │   └── index.ts       # Funções auxiliares
│   ├── styles/            # Estilos globais
│   │   └── globals.css    # CSS global com Tailwind
│   ├── App.vue            # Componente raiz
│   └── main.ts            # Ponto de entrada da aplicação
├── public/                # Arquivos estáticos
├── index.html             # HTML principal
├── vite.config.ts         # Configuração do Vite
├── tailwind.config.js     # Configuração do Tailwind CSS
├── tsconfig.json          # Configuração do TypeScript
└── package.json           # Dependências e scripts
```

## 🔐 Autenticação

O sistema usa JWT (JSON Web Tokens) para autenticação:

1. O usuário faz login com CPF e senha
2. O backend retorna um token JWT
3. O token é armazenado no localStorage
4. Todas as requisições subsequentes incluem o token no header `Authorization`
5. Guards de rota protegem páginas que requerem autenticação

## 🎨 Funcionalidades Implementadas

### ✅ Autenticação
- Login com CPF e senha
- Registro de novos usuários
- Validação de token automática
- Logout
- Guards de rota para proteção de páginas

### ✅ Dashboard
- Estatísticas gerais do sistema
- Total de pacientes e consultas
- Consultas agendadas e pendentes
- Consultas recentes
- Ações rápidas

### ✅ Gerenciamento de Pacientes
- Listagem de pacientes com busca
- Cadastro de novos pacientes
- Edição de pacientes existentes
- Exclusão de pacientes
- Validação de CPF e campos obrigatórios
- Formatação automática de CPF e telefone
- Cálculo automático de idade

### 🔄 Em Desenvolvimento
- Gerenciamento completo de consultas
- Gerenciamento de usuários (apenas para médicos)
- Página de perfil do usuário

## 🎯 Rotas da Aplicação

### Públicas
- `/login` - Página de login

### Protegidas (Requer autenticação)
- `/` ou `/dashboard` - Dashboard principal
- `/pacientes` - Gerenciamento de pacientes
- `/consultas` - Listagem de consultas
- `/consultas/nova` - Nova consulta
- `/perfil` - Perfil do usuário

### Restritas (Requer role MEDICO)
- `/usuarios` - Gerenciamento de usuários

## 🛠️ Componentes Principais

### Layout
Componente que envolve todas as páginas protegidas, incluindo:
- Sidebar com navegação
- Header com notificações e perfil
- Área de conteúdo principal

### Modal
Componente reutilizável para exibição de conteúdo em modal:
- Suporta diferentes tamanhos (sm, md, lg, xl)
- Fecha com ESC ou clique fora
- Teleport para renderização no body

### Formulários
Formulários com validação em tempo real:
- Validação de CPF
- Formatação automática de campos
- Feedback visual de erros
- Estados de loading

## 📱 Responsividade

O sistema é totalmente responsivo e se adapta a diferentes tamanhos de tela:
- **Desktop**: Sidebar fixa, layout em grid
- **Tablet**: Sidebar colapsável, layout adaptativo
- **Mobile**: Menu hamburger, tabelas com scroll horizontal

## 🎨 Estilização

O projeto usa Tailwind CSS com componentes customizados:
- Classes utilitárias para estilização rápida
- Componentes reutilizáveis (botões, inputs, cards, etc.)
- Tema personalizado com cores primárias
- Animações e transições suaves

## 🔄 Estado Global

### Store de Autenticação (Pinia)
- `user`: Usuário autenticado
- `token`: Token JWT
- `isAuthenticated`: Status de autenticação
- `isLoading`: Estado de carregamento
- `login()`: Função de login
- `logout()`: Função de logout
- `validateToken()`: Valida token existente

## 🌐 Comunicação com API

Todas as requisições à API são feitas através do serviço `api.ts`:
- Interceptors para adicionar token automaticamente
- Tratamento de erros centralizado
- Redirecionamento automático em caso de 401

### Endpoints disponíveis:
- **Auth**: login, refresh token, validate token
- **Usuários**: CRUD completo
- **Pacientes**: CRUD completo
- **Consultas**: CRUD completo, filtros por paciente/médico

## 🚀 Deploy

Para fazer deploy da aplicação:

1. Configure a variável de ambiente de produção
2. Execute o build:
```bash
npm run build
```

3. Os arquivos gerados em `dist/` podem ser servidos por qualquer servidor web estático

### Exemplo com servidor HTTP simples:
```bash
npx serve -s dist -l 3001
```

## 🐛 Troubleshooting

### Erro de conexão com a API
- Verifique se o backend está rodando
- Confirme a URL da API no arquivo `.env`
- Verifique se há erros de CORS no console

### Erro de autenticação
- Limpe o localStorage do navegador
- Faça login novamente
- Verifique se o token não expirou

### Erros de build
- Delete `node_modules` e `package-lock.json`
- Execute `npm install` novamente
- Verifique se a versão do Node.js é compatível

## 📝 Licença

Este projeto foi desenvolvido como parte de um desafio técnico.

## 👤 Autor

**Youx**

---

⭐ Desenvolvido com Vue 3 e TypeScript

