# Feira de Ciências API

Repositório: [https://github.com/vicaw/feira-ciencias-api](https://github.com/vicaw/feira-ciencias-api)

Este projeto é o backend do sistema de gestão para Feiras de Ciências Escolares. Ele foi construído utilizando **Quarkus** e **Java**, e desenhado com base em um padrão de **Monólito Modular** guiado por princípios de **Domain-Driven Design (DDD)** e **Clean Architecture**.

O objetivo desta arquitetura é garantir que o sistema seja fácil de manter, altamente testável e que, caso haja necessidade futura, seus módulos possam ser facilmente extraídos para Microsserviços independentes.

---

## 🏗️ Estrutura de Módulos (Maven Modules)

O projeto está dividido em múltiplos módulos independentes gerenciados pelo Maven:

```text
feira-ciencias/
├── bootstrap/          # Módulo principal (Entrypoint da aplicação)
├── shared-kernel/      # Código compartilhado entre todos os módulos funcionais
├── users-module/       # Módulo responsável pelos Usuários (Alunos, Professores)
├── events-module/      # Módulo responsável pelos Eventos (Feiras)
├── projects-module/    # Módulo responsável pelos Projetos dos Alunos
└── storage-module/     # Módulo responsável pelo armazenamento de arquivos
```

### 1. `bootstrap` (Módulo de Inicialização)
É o único módulo que sabe da existência de todos os outros. Ele não contém regras de negócio.
* **Responsabilidade:** Reunir todos os módulos, ler as configurações (`application.properties`), iniciar o servidor Quarkus, lidar com o ciclo de vida da aplicação e expor a API gerada.
* **Conteúdo:** Configurações (ex: `application.properties`), `Dockerfile`, etc.

### 2. `shared-kernel` (Núcleo Compartilhado)
Contém componentes transversais que pertencem a toda a aplicação, garantindo padronização sem gerar acoplamento de negócios.
* **Responsabilidade:** Prover blocos de construção comuns.
* **Conteúdo:** 
  * Exceções Globais (`BusinessRuleException`, `EntityNotFoundException`).
  * Tratadores de Exceção JAX-RS (`ExceptionMapper`).
  * Enums globais (quando estritamente necessários).

### 3. Módulos Funcionais (`users-module`, `events-module`, `projects-module`, `storage-module`)
São as "fatias" da aplicação. Cada módulo representa um Bounded Context (Contexto Delimitado) do DDD. **Eles não dependem uns dos outros (exceto via chamadas controladas, se necessário)**, eles dependem apenas do `shared-kernel`.

*   **`users-module`**: Gestão de Alunos e Professores, autenticação e perfis.
*   **`events-module`**: Gestão das edições da Feira de Ciências (Datas, Status).
*   **`projects-module`**: O coração do sistema; inscrições, diários de bordo e materiais.
*   **`storage-module`**: Abstração técnica para armazenamento de arquivos e anexos.

---

## 🏛️ Clean Architecture (Dentro de cada Módulo Funcional)

Ao olhar para dentro de um módulo funcional (por exemplo, `users-module`), você encontrará a seguinte divisão de pacotes, refletindo a Clean Architecture:

```text
users-module/src/main/java/br/com/escola/feiraciencias/users/
├── api/                  # Camada de Interface / Adapters de Entrada
│   ├── dto/              # Objetos de Transferência de Dados
│   │   ├── requests/     # Payloads de entrada (ex: LoginRequest)
│   │   └── responses/    # Retornos de saída (ex: TokenResponse)
│   ├── mappers/          # Conversores Automáticos (MapStruct: DTO <-> Domínio)
│   └── resources/        # Endpoints HTTP (Controllers JAX-RS)
│
├── application/          # Camada de Aplicação
│   ├── usecases/         # Casos de Uso (Fluxos de negócio únicos, ex: CadastrarAluno)
│   └── services/         # Serviços de Aplicação (Orquestração repetitiva/ajudantes)
│
├── domain/               # Camada de Domínio (O coração do sistema)
│   ├── model/            # Entidades de Domínio Ricas (Regras de negócio e estado)
│   ├── services/         # Interfaces de Serviços de Domínio (ex: PasswordService)
│   └── repositories/     # Interfaces de Repositório (Contratos de persistência)
│
└── infrastructure/       # Camada de Infraestrutura / Adapters de Saída
    ├── security/         # Implementações de Segurança (ex: BCrypt)
    ├── client/           # Integrações com APIs externas (Clients/Gateways)
    └── persistence/      # Implementações de Banco de Dados
        ├── entities/     # Entidades JPA (@Entity)
        ├── mappers/      # Conversores Automáticos (MapStruct: JPA <-> Domínio)
        └── repositories/ # Implementação concreta do repositório (ex: Panache)
```

### Detalhamento das Camadas (Regra de Dependência)
*A regra fundamental é que as dependências sempre apontam para o centro (Domínio).*

1. **`domain` (Domínio)**: É o coração. Contém a lógica de negócio mais pura. Define **o que** o sistema faz e os contratos (interfaces) que ele precisa que o mundo externo cumpra.
2. **`application` (Aplicação)**: Orquestra os fluxos de dados. Usa os modelos e repositórios do domínio para realizar tarefas específicas. Os **Use Cases** são as ações do sistema, e os **Services** aqui ajudam a evitar repetição de código de orquestração.
3. **`api` (Interface)**: É como o mundo externo (Web, Mobile) fala com o sistema. Traduz requisições para chamadas da Aplicação e Domínio.
4. **`infrastructure` (Infraestrutura)**: Contém as implementações técnicas das interfaces do Domínio. Aqui vive o código que fala com o Banco de Dados, APIs de Terceiros ou sistemas de Hashing.

---

## 🛠️ Tecnologias Utilizadas

* **Quarkus**: Framework Java Cloud-Native super rápido.
* **Hibernate ORM com Panache**: Para persistência simplificada no padrão Active Record / Repository.
* **PostgreSQL**: Banco de dados relacional.
* **Flyway**: Ferramenta de versionamento de banco de dados (Migrações modulares separadas por funcionalidade).
* **MapStruct**: Geração automática de código para Mapeamento de objetos (DTO -> Model -> JPA).
* **Lombok**: Redução de código boilerplate (Getters, Setters, Construtores).
* **SmallRye JWT**: Autenticação e Autorização via tokens JWT.

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
* **Java 21** ou superior.
* **Maven 3.9+**.
* **Docker e Docker Compose**.

### Modo de Desenvolvimento (Hot Reload)

1. **Subir o Banco de Dados**:
   Na raiz do projeto, execute:
   ```bash
   docker-compose up -d
   ```

2. **Rodar a Aplicação**:
   Para rodar o projeto com recarga automática de código:
   ```bash
   mvn quarkus:dev -pl bootstrap -am
   ```

O sistema estará disponível em: `http://localhost:8080`
O Swagger UI estará disponível em: `http://localhost:8080/q/swagger-ui`

### 🔑 Usuário de Teste (Modo Dev)
Ao iniciar em modo dev, um professor padrão é criado automaticamente para testes:
* **Email:** `professor@escola.com`
* **Senha:** `123456`

### Gerando o Executável (Produção)
Para gerar o JAR otimizado:

```bash
mvn clean package
```
