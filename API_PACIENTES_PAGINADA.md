
## 📝 Descrição
Endpoint para listar pacientes com paginação e busca unificada por nome ou CPF.

## 🔧 Parâmetros

### Query Parameters (Opcionais)
| Parâmetro | Tipo | Padrão | Descrição |
|-----------|------|--------|-----------|
| `busca` | string | - | Termo de busca (nome ou CPF) |
| `page` | number | 0 | Número da página (inicia em 0) |
| `size` | number | 20 | Itens por página |
| `sort` | string | "nome" | Campo para ordenação |
| `direction` | string | "asc" | Direção: "asc" ou "desc" |

### Campos de Ordenação Disponíveis
- `nome` - Nome do paciente (padrão)
- `id` - ID do paciente
- `cpf` - CPF do paciente
- `telefone` - Telefone
- `dataDeNascimento` - Data de nascimento
- `createdAt` - Data de criação
- `updatedAt` - Data de atualização

## 🚀 Exemplos de Uso

### 1. Buscar todos os pacientes (primeira página)
```bash
curl -X GET "http://localhost:8080/api/pacientes/pagina" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 2. Buscar por nome
```bash
curl -X GET "http://localhost:8080/api/pacientes/pagina?busca=João" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 3. Buscar por CPF
```bash
curl -X GET "http://localhost:8080/api/pacientes/pagina?busca=123.456.789-01" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 4. Buscar com paginação customizada
```bash
curl -X GET "http://localhost:8080/api/pacientes/pagina?page=1&size=10&sort=id&direction=desc" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 5. Buscar com filtro e ordenação
```bash
curl -X GET "http://localhost:8080/api/pacientes/pagina?busca=Silva&sort=dataDeNascimento&direction=desc" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 📊 Exemplo de Resposta

```json
{
  "content": [
    {
      "id": 1,
      "nome": "João Silva",
      "cpf": "12345678901",
      "telefone": "(11) 99999-9999",
      "dataDeNascimento": "1990-01-01",
      "createdAt": "2024-01-10T14:30:00",
      "updatedAt": "2024-01-10T14:30:00"
    },
    {
      "id": 2,
      "nome": "Maria Santos",
      "cpf": "98765432100",
      "telefone": "(11) 88888-8888",
      "dataDeNascimento": "1985-05-15",
      "createdAt": "2024-01-11T09:15:00",
      "updatedAt": "2024-01-11T09:15:00"
    }
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false
    },
    "pageNumber": 0,
    "pageSize": 20,
    "offset": 0,
    "unpaged": false,
    "paged": true
  },
  "totalElements": 2,
  "totalPages": 1,
  "last": true,
  "first": true,
  "numberOfElements": 2,
  "size": 20,
  "number": 0,
  "sort": {
    "sorted": true,
    "unsorted": false
  },
  "empty": false
}
```

## 🔍 Características da Busca

### Busca Unificada
- **Nome**: Busca parcial, case-insensitive
  - `busca=joão` → encontra "João Silva", "Maria João", etc.
- **CPF**: Remove automaticamente pontos e traços
  - `busca=123.456.789-01` → busca por `12345678901`
  - `busca=123456` → encontra CPFs que contenham "123456"

### Operação OR
- Um único termo busca em **nome OU CPF** simultaneamente
- Retorna pacientes que atendam qualquer um dos critérios

## 📈 Informações de Paginação

### Campos de Resposta
- `content`: Array com os dados dos pacientes
- `totalElements`: Total de registros encontrados
- `totalPages`: Total de páginas
- `number`: Página atual (0-indexed)
- `size`: Tamanho da página
- `first`: Se é a primeira página
- `last`: Se é a última página
- `empty`: Se não há resultados

### Navegação
- **Primeira página**: `page=0`
- **Próxima página**: `page=1`, `page=2`, etc.
- **Última página**: `page=totalPages-1`

## ⚠️ Códigos de Status

- `200 OK` - Sucesso
- `400 Bad Request` - Parâmetros inválidos
- `401 Unauthorized` - Token JWT inválido/expirado
- `403 Forbidden` - Acesso negado
- `500 Internal Server Error` - Erro interno

## 🔐 Autenticação

Este endpoint requer autenticação JWT. Obtenha o token fazendo login:

```bash
curl -X POST "http://localhost:8080/api/usuarios/login" \
  -H "Content-Type: application/json" \
  -d '{
    "cpf": "12345678900",
    "senha": "sua_senha"
  }'
```

## 🧪 Testando Rapidamente

### 1. Fazer login e obter token
```bash
TOKEN=$(curl -s -X POST "http://localhost:8080/api/usuarios/login" \
  -H "Content-Type: application/json" \
  -d '{"cpf":"12345678900","senha":"sua_senha"}' | jq -r '.token')
```

### 2. Testar paginação
```bash
curl -X GET "http://localhost:8080/api/pacientes/pagina?size=5" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" | jq '.'
```

## 💡 Dicas de Uso

1. **Performance**: Use `size` adequado (5-50 registros)
2. **Busca**: Combine termo de busca com ordenação
3. **Navegação**: Use `totalPages` para controlar paginação
4. **Flexibilidade**: CPF funciona com ou sem formatação
5. **Ordenação**: Combine múltiplos campos se necessário

## 🔧 Implementação Técnica

- **Spring Data JPA**: Paginação nativa
- **JPA Specification**: Filtros dinâmicos
- **Criteria API**: Queries otimizadas
- **PageableDefault**: Configuração padrão
- **Response Page**: Estrutura padronizada

---

**Autor**: Sistema Clínico Youx  
**Data**: 01/01/2025  
**Versão**: 1.0