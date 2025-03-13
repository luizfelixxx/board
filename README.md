
# Gerenciador de Boards

Este projeto é um sistema de gerenciamento de boards, desenvolvido em **Java 17** com **Gradle**, utilizando **PostgreSQL**, **Liquibase**, e **JDBC** para manipulação de dados. O sistema permite a criação, seleção e exclusão de boards, além da movimentação de cards entre colunas.

## 🚀 Tecnologias Utilizadas

-   **Java 17**

-   **Gradle**

-   **PostgreSQL**

-   **JDBC**

-   **Liquibase**

-   **Lombok**


## 🛠 Estrutura do Projeto

O projeto segue uma estrutura organizada em pacotes:

-   `dto/` - Objetos de transferência de dados

-   `exception/` - Tratamento de exceções

-   `persistence/` - DAO e configuração do banco de dados

-   `service/` - Regras de negócio

-   `ui/` - Interface de linha de comando


## 📂 Configuração do Ambiente

### 1. Instalar Dependências

Certifique-se de ter instalado em sua máquina:

-   Java 17

-   PostgreSQL

-   Gradle


### 2. Configurar o Banco de Dados

1.  Criar o banco de dados e usuário:

    ```
    CREATE DATABASE board;
    CREATE USER board WITH PASSWORD 'board';
    ALTER ROLE board SET client_encoding TO 'utf8';
    ALTER ROLE board SET default_transaction_isolation TO 'read committed';
    ALTER ROLE board SET timezone TO 'UTC';
    GRANT ALL PRIVILEGES ON DATABASE board TO board;
    ```

2.  Configurar o `application.properties`:

    ```
    changeLogFile=src/main/resources/db/changelog/db.changelog-master.yml
    url=jdbc:postgresql://localhost:5432/board
    username=board
    password=board
    driver=org.postgresql.Driver
    ```



## Funcionalidades

-   Criar um novo board

-   Selecionar um board existente

-   Excluir um board

-   Adicionar cards e movimentá-los entre colunas

-   Bloquear e desbloquear cards