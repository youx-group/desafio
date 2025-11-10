#!/bin/bash

# Script de Instalação e Execução - Frontend Vue
# Sistema Clínico

echo "═══════════════════════════════════════════════════════════"
echo "  🎨 Sistema Clínico - Frontend Vue"
echo "  Instalação e Execução"
echo "═══════════════════════════════════════════════════════════"
echo ""

# Cores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Verificar Node.js
echo -e "${BLUE}[1/5]${NC} Verificando Node.js..."
if ! command -v node &> /dev/null; then
    echo -e "${RED}❌ Node.js não encontrado!${NC}"
    echo "   Por favor, instale Node.js 16+ primeiro."
    exit 1
fi

NODE_VERSION=$(node -v)
echo -e "${GREEN}✓${NC} Node.js instalado: $NODE_VERSION"
echo ""

# Verificar npm
echo -e "${BLUE}[2/5]${NC} Verificando npm..."
if ! command -v npm &> /dev/null; then
    echo -e "${RED}❌ npm não encontrado!${NC}"
    exit 1
fi

NPM_VERSION=$(npm -v)
echo -e "${GREEN}✓${NC} npm instalado: $NPM_VERSION"
echo ""

# Diretório do projeto
PROJECT_DIR="/home/youx/projetos/desafio/frontend-vue"

# Verificar se o diretório existe
if [ ! -d "$PROJECT_DIR" ]; then
    echo -e "${RED}❌ Diretório não encontrado: $PROJECT_DIR${NC}"
    exit 1
fi

# Entrar no diretório
cd "$PROJECT_DIR" || exit

# Verificar se já tem node_modules
if [ -d "node_modules" ]; then
    echo -e "${BLUE}[3/5]${NC} Dependências já instaladas."
    echo -e "${YELLOW}⚠ Pulando instalação...${NC}"
    echo ""
else
    # Instalar dependências
    echo -e "${BLUE}[3/5]${NC} Instalando dependências..."
    echo "   Isso pode levar alguns minutos..."
    
    npm install
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓${NC} Dependências instaladas com sucesso!"
        echo ""
    else
        echo -e "${RED}❌ Erro ao instalar dependências!${NC}"
        exit 1
    fi
fi

# Verificar arquivo .env
echo -e "${BLUE}[4/5]${NC} Verificando configurações..."
if [ ! -f ".env" ]; then
    echo -e "${YELLOW}⚠ Arquivo .env não encontrado. Criando...${NC}"
    echo "VITE_API_URL=http://localhost:8080/api/v1" > .env
    echo -e "${GREEN}✓${NC} Arquivo .env criado!"
else
    echo -e "${GREEN}✓${NC} Arquivo .env encontrado"
fi
echo ""

# Verificar se o backend está rodando
echo -e "${BLUE}[5/5]${NC} Verificando backend..."
if curl -s http://localhost:8080/api/v1 > /dev/null 2>&1; then
    echo -e "${GREEN}✓${NC} Backend está rodando!"
else
    echo -e "${YELLOW}⚠ Backend não está acessível em http://localhost:8080${NC}"
    echo "   Certifique-se de iniciar o backend antes de usar o frontend."
fi
echo ""

echo "═══════════════════════════════════════════════════════════"
echo -e "${GREEN}✓ Instalação concluída!${NC}"
echo "═══════════════════════════════════════════════════════════"
echo ""
echo -e "${BLUE}Para iniciar o servidor de desenvolvimento:${NC}"
echo ""
echo "   cd $PROJECT_DIR"
echo "   npm run dev"
echo ""
echo -e "${BLUE}O frontend estará disponível em:${NC}"
echo "   🌐 http://localhost:3001"
echo ""
echo "═══════════════════════════════════════════════════════════"
echo ""

# Perguntar se deseja iniciar agora
read -p "Deseja iniciar o servidor agora? (s/N) " -n 1 -r
echo ""

if [[ $REPLY =~ ^[SsYy]$ ]]; then
    echo ""
    echo "Iniciando servidor de desenvolvimento..."
    echo ""
    npm run dev
fi

