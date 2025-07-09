# Endpoints de Consultas e Pacientes Paginados com Filtros

## Endpoint de Consultas Paginadas
```
GET /api/consultas/paginated
```

## Descrição
Endpoint para buscar consultas de forma paginada com filtros dinâmicos usando Spring Data JPA Specification.

## Parâmetros de Consulta (Query Parameters)

### Filtros (Opcionais)
- `nomePaciente` - Nome do paciente (busca parcial, case-insensitive)
- `nomeMedico` - Nome do médico (busca parcial, case-insensitive)
- `dataInicio` - Data de início do período (formato ISO: `yyyy-MM-ddTHH:mm:ss`)
- `dataFim` - Data de fim do período (formato ISO: `yyyy-MM-ddTHH:mm:ss`)
- `pacienteId` - ID do paciente (busca exata)
- `medicoId` - ID do médico (busca exata)

### Paginação
- `page` - Número da página (padrão: 0)
- `size` - Tamanho da página (padrão: 10)
- `sort` - Campo para ordenação (padrão: data)
- `direction` - Direção da ordenação: `asc` ou `desc` (padrão: desc)

## Exemplos de Uso

### 1. Buscar todas as consultas (sem filtros)
```bash
GET /api/consultas/paginated
```

### 2. Buscar consultas por nome do paciente
```bash
GET /api/consultas/paginated?nomePaciente=João
```

### 3. Buscar consultas por nome do médico
```bash
GET /api/consultas/paginated?nomeMedico=Dr.%20Carlos
```

### 4. Buscar consultas em um período específico
```bash
GET /api/consultas/paginated?dataInicio=2024-01-01T08:00:00&dataFim=2024-01-31T18:00:00
```

### 5. Buscar consultas com paginação customizada
```bash
GET /api/consultas/paginated?page=1&size=20&sort=id&direction=asc
```

### 6. Buscar consultas com múltiplos filtros
```bash
GET /api/consultas/paginated?nomePaciente=João&nomeMedico=Carlos&page=0&size=5
```

### 7. Buscar consultas por ID do paciente
```bash
GET /api/consultas/paginated?pacienteId=1
```

### 8. Buscar consultas por ID do médico
```bash
GET /api/consultas/paginated?medicoId=2
```

### 9. Buscar consultas a partir de uma data específica
```bash
GET /api/consultas/paginated?dataInicio=2024-01-15T08:00:00
```

### 10. Buscar consultas até uma data específica
```bash
GET /api/consultas/paginated?dataFim=2024-01-31T18:00:00
```

## Resposta (Response)

O endpoint retorna um objeto `Page<ConsultaResponseDTO>` com a seguinte estrutura:

```json
{
  "content": [
    {
      "id": 1,
      "paciente": {
        "id": 1,
        "nome": "João Silva",
        "cpf": "12345678901",
        "telefone": "(11) 99999-9999",
        "dataDeNascimento": "1990-01-01"
      },
      "medico": {
        "id": 1,
        "nome": "Dr. Carlos Santos",
        "cpf": "98765432100",
        "role": "MEDICO"
      },
      "data": "2024-01-15T10:00:00",
      "observacao": "Consulta de rotina",
      "createdAt": "2024-01-10T14:30:00",
      "updatedAt": "2024-01-10T14:30:00"
    }
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false
    },
    "pageNumber": 0,
    "pageSize": 10,
    "offset": 0,
    "unpaged": false,
    "paged": true
  },
  "totalElements": 25,
  "totalPages": 3,
  "last": false,
  "first": true,
  "numberOfElements": 10,
  "size": 10,
  "number": 0,
  "sort": {
    "sorted": true,
    "unsorted": false
  },
  "empty": false
}
```

## Campos de Ordenação Disponíveis

- `id` - ID da consulta
- `data` - Data da consulta (padrão)
- `paciente.nome` - Nome do paciente
- `medico.nome` - Nome do médico
- `createdAt` - Data de criação
- `updatedAt` - Data de atualização

## Características dos Filtros

### Filtros de Nome (nomePaciente e nomeMedico)
- Busca parcial: encontra registros que contenham o texto em qualquer parte do nome
- Case-insensitive: não diferencia maiúsculas de minúsculas
- Exemplo: `nomePaciente=joão` encontra "João Silva", "Maria João", etc.

### Filtros de Data
- Suporta períodos: ambas as datas, apenas início, apenas fim
- Formato ISO 8601: `yyyy-MM-ddTHH:mm:ss`
- Exemplo: `2024-01-15T10:30:00`

### Filtros de ID
- Busca exata por ID
- Útil para buscar consultas específicas de um paciente ou médico

## Códigos de Status HTTP

- `200 OK` - Requisição bem-sucedida
- `400 Bad Request` - Parâmetros inválidos (formato de data incorreto, etc.)
- `500 Internal Server Error` - Erro interno do servidor

## Exemplo Completo com cURL

```bash
curl -X GET "http://localhost:8080/api/consultas/paginated?nomePaciente=João&dataInicio=2024-01-01T08:00:00&page=0&size=10&sort=data&direction=desc" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

# Endpoint de Pacientes Paginados

## Endpoint
```
GET /api/pacientes/pagina
```

## Descrição
Endpoint para buscar pacientes de forma paginada com filtros dinâmicos usando Spring Data JPA Specification. Permite buscar por nome ou CPF em um único campo de busca.

## Parâmetros de Consulta (Query Parameters)

### Filtros (Opcionais)
- `busca` - Termo de busca que pode ser nome ou CPF do paciente (busca parcial, case-insensitive)

### Paginação
- `page` - Número da página (padrão: 0)
- `size` - Tamanho da página (padrão: 20)
- `sort` - Campo para ordenação (padrão: nome)
- `direction` - Direção da ordenação: `asc` ou `desc` (padrão: asc)

## Exemplos de Uso

### 1. Buscar todos os pacientes (sem filtros)
```bash
GET /api/pacientes/pagina
```

### 2. Buscar pacientes por nome
```bash
GET /api/pacientes/pagina?busca=João
```

### 3. Buscar pacientes por CPF
```bash
GET /api/pacientes/pagina?busca=12345678901
```

### 4. Buscar pacientes com CPF formatado
```bash
GET /api/pacientes/pagina?busca=123.456.789-01
```

### 5. Buscar pacientes com paginação customizada
```bash
GET /api/pacientes/pagina?page=1&size=10&sort=id&direction=desc
```

### 6. Buscar pacientes com filtro e paginação
```bash
GET /api/pacientes/pagina?busca=Silva&page=0&size=5&sort=nome&direction=asc
```

### 7. Buscar com parte do CPF
```bash
GET /api/pacientes/pagina?busca=123456
```

### 8. Ordenar por data de nascimento
```bash
GET /api/pacientes/pagina?sort=dataDeNascimento&direction=desc
```

## Resposta (Response)

O endpoint retorna um objeto `Page<PacienteResponseDTO>` com a seguinte estrutura:

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
  "totalElements": 45,
  "totalPages": 3,
  "last": false,
  "first": true,
  "numberOfElements": 20,
  "size": 20,
  "number": 0,
  "sort": {
    "sorted": true,
    "unsorted": false
  },
  "empty": false
}
```

## Campos de Ordenação Disponíveis

- `id` - ID do paciente
- `nome` - Nome do paciente (padrão)
- `cpf` - CPF do paciente
- `telefone` - Telefone do paciente
- `dataDeNascimento` - Data de nascimento
- `createdAt` - Data de criação
- `updatedAt` - Data de atualização

## Características dos Filtros

### Filtro de Busca Unificado
- **Busca por nome**: Busca parcial, case-insensitive
  - Exemplo: `busca=joão` encontra "João Silva", "Maria João", etc.
- **Busca por CPF**: Remove caracteres especiais automaticamente
  - Exemplo: `busca=123.456.789-01` é tratado como `busca=12345678901`
  - Exemplo: `busca=123456` encontra CPFs que contenham "123456"
- **Busca combinada**: Usa operador OR para buscar em nome OU CPF
  - Um único termo de busca pesquisa em ambos os campos simultaneamente

### Funcionalidades Especiais
- **Remoção automática de caracteres especiais**: CPF com pontos e traços funciona normalmente
- **Busca inteligente**: Detecta automaticamente se o termo é mais provável de ser nome ou CPF
- **Performance otimizada**: Usa índices do banco de dados para busca eficiente

## Códigos de Status HTTP

- `200 OK` - Requisição bem-sucedida
- `400 Bad Request` - Parâmetros inválidos
- `500 Internal Server Error` - Erro interno do servidor

## Exemplo Completo com cURL

```bash
curl -X GET "http://localhost:8080/api/pacientes/pagina?busca=João&page=0&size=20&sort=nome&direction=asc" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Notas Importantes

1. **Performance**: O uso de Specification permite construir consultas SQL otimizadas
2. **Flexibilidade**: Busca unificada por nome ou CPF em um único campo
3. **Paginação**: Evita sobrecarga do servidor com grandes volumes de dados
4. **Ordenação**: Permite ordenar por qualquer campo da entidade
5. **Segurança**: Endpoint protegido por JWT (se configurado)
6. **Compatibilidade**: Funciona com CPF formatado ou não formatado 