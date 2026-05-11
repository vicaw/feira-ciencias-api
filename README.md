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
│   ├── requests/         # Objetos de Entrada HTTP (Payloads)
│   ├── responses/        # Objetos de Saída HTTP (Retornos)
│   ├── mappers/          # Conversores Automáticos (MapStruct: DTO <-> Domínio)
│   └── resources/        # Endpoints HTTP (Controllers JAX-RS)
│
├── application/          # Camada de Aplicação
│   └── usecases/         # Casos de Uso (Lógica de orquestração do negócio)
│
├── domain/               # Camada de Domínio (O coração do sistema)
│   ├── model/            # Entidades de Domínio puras (POJOs com regras de negócio)
│   └── repositories/     # Interfaces de Repositório (Contratos de persistência)
│
└── infrastructure/       # Camada de Infraestrutura / Adapters de Saída
    ├── client/           # Integrações com APIs externas (Clients/Gateways)
    └── persistence/      # Implementações de Banco de Dados
        ├── entities/     # Entidades JPA (@Entity)
        ├── mappers/      # Conversores Automáticos (MapStruct: JPA <-> Domínio)
        └── repositories/ # Implementação concreta do repositório (ex: Panache)
```

### Detalhamento das Camadas (Regra de Dependência)
*A regra fundamental é que as dependências sempre apontam para o centro (Domínio).*

1. **`domain` (Domínio)**: É o coração. Não possui nenhuma anotação de banco de dados ou framework web (sem JPA, sem Jackson). Apenas Java puro e anotações simples como Lombok. Define **o que** são os objetos e as interfaces do que o sistema precisa (ex: `UsuarioRepository`).
2. **`application` (Aplicação / UseCases)**: Orquestra o fluxo de dados. Recebe chamadas da API, busca dados no domínio via repositórios (interfaces), aplica a lógica de negócio e salva as mudanças. Depende apenas do Domínio.
3. **`api` (Interface)**: É como o mundo externo fala com o nosso módulo. Recebe requisições REST, traduz os DTOs usando os `Mappers` e chama a camada de Aplicação. Depende da Aplicação e do Domínio.
4. **`infrastructure` (Infraestrutura)**: É **como** as coisas funcionam tecnicamente. Implementa a interface do repositório do Domínio usando Hibernate/Panache. Traduz os modelos de Domínio para Entidades JPA usando os `Mappers`. Depende da Aplicação e do Domínio.

---

## 🛠️ Tecnologias Utilizadas

* **Quarkus**: Framework Java Cloud-Native super rápido.
* **Hibernate ORM com Panache**: Para persistência simplificada no padrão Active Record / Repository.
* **PostgreSQL**: Banco de dados relacional.
* **Flyway**: Ferramenta de versionamento de banco de dados (Migrações modulares separadas por funcionalidade).
* **MapStruct**: Geração automática de código para Mapeamento de objetos (DTO -> Model -> JPA).
* **Lombok**: Redução de código boilerplate (Getters, Setters, Construtores).
* **SmallRye JWT**: Autenticação e Autorização via tokens JWT.
* **Testcontainers (Dev Services)**: Subida automática de instâncias do PostgreSQL via Docker durante o desenvolvimento local sem necessidade de configuração.

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
* **Java 21** ou superior.
* **Maven 3.9+**.
* **Docker** (O Quarkus utiliza o *Dev Services* para subir o banco de dados automaticamente).

### Modo de Desenvolvimento (Hot Reload)
Para rodar o projeto com recarga automática de código e banco de dados automático:

```bash
mvn quarkus:dev -pl bootstrap -am
```

O sistema estará disponível em: `http://localhost:8080`
O Swagger UI estará disponível em: `http://localhost:8080/q/dev-ui` (ou `/q/swagger-ui` se habilitado).

### Gerando o Executável (Produção)
Para gerar o JAR otimizado:

```bash
mvn clean package
```
