# 🚀 Guia de Instalação - Frontend Vue

## Passo a Passo para Executar o Projeto

### 1️⃣ Pré-requisitos

Certifique-se de ter instalado:
- ✅ Node.js 16+ 
- ✅ npm ou yarn
- ✅ Backend rodando em `http://localhost:8080`

Verificar versão do Node:
```bash
node --version
# Deve retornar v16.x.x ou superior
```

### 2️⃣ Instalação

Entre no diretório do projeto Vue:
```bash
cd /home/youx/projetos/desafio/frontend-vue
```

Instale as dependências:
```bash
npm install
```

Aguarde a instalação de todas as dependências. Isso pode levar alguns minutos na primeira vez.

### 3️⃣ Configuração

O arquivo `.env` já foi criado com as configurações padrão:
```env
VITE_API_URL=http://localhost:8080/api/v1
```

Se o backend estiver em outra porta ou host, edite este arquivo.

### 4️⃣ Executar em Desenvolvimento

Inicie o servidor de desenvolvimento:
```bash
npm run dev
```

Você verá uma mensagem como:
```
VITE v4.x.x  ready in XXX ms

➜  Local:   http://localhost:3001/
➜  Network: use --host to expose
```

Abra o navegador em: **http://localhost:3001**

### 5️⃣ Fazer Login

Use as credenciais de um usuário cadastrado no backend:
- **CPF**: (cadastrado no sistema)
- **Senha**: (senha do usuário)

Ou crie uma nova conta clicando em "Cadastre-se".

### 6️⃣ Build para Produção (Opcional)

Para criar uma versão otimizada para produção:

```bash
npm run build
```

Os arquivos serão gerados na pasta `dist/`.

Para visualizar a build:
```bash
npm run preview
```

## 🐛 Solução de Problemas

### Erro: "Cannot find module"
```bash
rm -rf node_modules package-lock.json
npm install
```

### Erro: "Port 3001 already in use"
```bash
# Mude a porta no vite.config.ts ou pare o processo que está usando a porta 3001
lsof -ti:3001 | xargs kill -9
```

### Erro de conexão com API
1. Verifique se o backend está rodando
2. Confirme a URL no arquivo `.env`
3. Verifique o console do navegador (F12) para mais detalhes

### Erro 401 (Unauthorized)
1. Limpe o localStorage do navegador (F12 > Application > Local Storage)
2. Faça login novamente

## 📦 Scripts Disponíveis

```bash
# Desenvolvimento (hot reload)
npm run dev

# Build para produção
npm run build

# Preview da build
npm run preview

# Lint (verificar código)
npm run lint
```

## 🎯 Próximos Passos

Após o login, você pode:
1. 📊 Ver o Dashboard com estatísticas
2. 👥 Gerenciar pacientes (CRUD completo)
3. 📅 Navegar pelas seções de consultas
4. 👤 Acessar seu perfil

## 📚 Documentação Completa

Para mais informações, consulte:
- `README.md` - Documentação completa
- `README-RESUMO.md` - Resumo rápido
- `/FRONTEND_COMPARISON.md` - Comparação React vs Vue

## ✅ Checklist de Verificação

Antes de reportar problemas, verifique:
- [ ] Node.js versão 16+ instalado
- [ ] Backend rodando e acessível
- [ ] Dependências instaladas (`node_modules` existe)
- [ ] Arquivo `.env` configurado corretamente
- [ ] Porta 3001 disponível
- [ ] Console do navegador sem erros críticos

## 🆘 Suporte

Se encontrar problemas:
1. Verifique o console do navegador (F12)
2. Verifique os logs do terminal
3. Consulte a documentação do README.md
4. Verifique se o backend está respondendo

---

**Desenvolvido com ❤️ usando Vue 3 + TypeScript + Vite**

