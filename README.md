# Bicos Backend

API REST do marketplace Bicos, desenvolvida em **Spring Boot 4.0** com **Java 21+**.

## Tecnologias

| Framework / Lib | Versão |
|----------------|--------|
| Spring Boot | 4.0.6 |
| Java | 21+ |
| Maven | 3.14+ |
| PostgreSQL | 18+ |
| Hibernate | 7.2 |
| Spring Security | (JWT) |
| Lombok | — |

## Estrutura

```
api/
├── src/main/java/project/bicos/api/
│   ├── controller/     # Endpoints REST
│   ├── service/        # Lógica de negócio
│   ├── repository/     # Acesso a dados (Spring Data JPA)
│   ├── models/         # Entidades JPA
│   ├── dto/            # Objetos de requisição/resposta
│   ├── security/       # Configuração JWT
│   ├── config/         # Configurações gerais
│   └── exceptions/     # Tratamento de erros
├── src/main/resources/
│   ├── application.properties          # Config local (gitignored)
│   └── application.properties.example  # Template para copiar
├── database.sql       # Script completo de criação do banco
```

## Endpoints principais

### Autenticação
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/auth/login/cliente` | Login cliente |
| POST | `/auth/login/prestador` | Login prestador |
| POST | `/auth/recuperar-senha` | Solicitar código |
| POST | `/auth/redefinir-senha` | Redefinir senha |

### Clientes
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/clientes` | Cadastro |
| GET | `/clientes/{id}` | Buscar por ID |
| PUT | `/clientes/{id}` | Atualizar dados |

### Prestadores
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/prestadores` | Cadastro |
| GET | `/prestadores/{id}` | Buscar por ID |
| PUT | `/prestadores/{id}` | Atualizar dados |
| GET | `/prestadores/categoria/{nome}` | Listar por categoria |
| GET | `/prestadores/proximos` | Próximos (geoloc) |
| GET | `/prestadores/busca?q=` | Busca textual |

### Solicitações
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/solicitacoes` | Criar solicitação |
| GET | `/solicitacoes/cliente/{id}` | Listar por cliente |
| GET | `/solicitacoes/prestador/{id}` | Listar por prestador |
| PATCH | `/solicitacoes/{id}/status` | Avançar status |
| PATCH | `/solicitacoes/{id}/confirmar-pagamento` | Confirmar pagamento |

### Chat
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/mensagens/{solicitacaoId}` | Listar mensagens |
| POST | `/mensagens` | Enviar mensagem |

### Avaliações
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/avaliacoes` | Criar avaliação |
| GET | `/avaliacoes?prestadorId=X` | Avaliações do prestador |
| GET | `/avaliacoes?clienteId=X` | Avaliações do cliente |
| GET | `/avaliacoes/media?prestadorId=X` | Média do prestador |

## Setup para Windows

1. Instalar Java 21+ e PostgreSQL
2. Abrir **pgAdmin** (vem com PostgreSQL) ou terminal e criar o banco:

```sql
CREATE DATABASE bicos;
```

3. No pgAdmin, abra a query tool no banco "bicos" e execute o arquivo `database.sql`
4. Ou via linha de comando:

```bash
psql -U postgres -d bicos -f database.sql
```

5. Configurar o `application.properties`:

```bash
# Copie o arquivo de exemplo (o real está no .gitignore)
cd api
copy src\main\resources\application.properties.example ^
     src\main\resources\application.properties

# Edite com seu usuário e senha do PostgreSQL:
# spring.datasource.username=postgres
# spring.datasource.password=SUA_SENHA_AQUI
```

6. Iniciar o servidor:

```bash
cd api
.\mvnw.cmd spring-boot:run
```

A API será iniciada em `http://localhost:8080`.

## Pré-requisitos

- Java 21+ (ou versão compatível)
- PostgreSQL rodando
- Maven (via `./mvnw` incluso)

## Configuração

```bash
# Copie o arquivo de configuração
cp src/main/resources/application.properties.example \
   src/main/resources/application.properties

# Edite com seus dados do PostgreSQL
# spring.datasource.username=SEU_USUARIO
# spring.datasource.password=SUA_SENHA
```

## Como executar

```bash
cd api
./mvnw spring-boot:run
```

A API será iniciada em `http://localhost:8080`.

## Banco de dados

O script `database.sql` cria todas as tabelas e insere as categorias. Execute no PostgreSQL antes de iniciar a aplicação.

```bash
psql -U seu_usuario -d bicos -f database.sql
```

## Status do projeto

Projeto acadêmico — 5º período de Análise e Desenvolvimento de Sistemas.
