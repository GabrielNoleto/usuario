# User Service

Microsserviço responsável pelo gerenciamento de usuários em um sistema de gestão de tarefas baseado em arquitetura de microsserviços.

O serviço permite realizar cadastro, atualização, consulta e remoção de usuários, além de gerenciar informações relacionadas como telefones e endereços.

A aplicação também integra com a API externa ViaCEP para obtenção automática de dados de endereço a partir do CEP informado.

---

## Arquitetura

Este serviço faz parte de um sistema distribuído baseado em microsserviços.

Ele é responsável exclusivamente pelo domínio de **usuários**, garantindo separação de responsabilidades dentro da arquitetura.

---

## Tecnologias Utilizadas

### Backend

- Java
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA / Hibernate

### Integração

- OpenFeign
- API ViaCEP

### Banco de Dados

- PostgreSQL

### DevOps

- Docker
- Docker Compose
- SonarQube

### Versionamento

- Git
- GitFlow (branches: feature, develop e main)

---

## Segurança

A aplicação utiliza autenticação baseada em **JWT (JSON Web Token)** implementada com **Spring Security**.

A entidade `Usuario` implementa a interface `UserDetails`, permitindo integração direta com o mecanismo de autenticação do Spring.

Fluxo de autenticação:

1. Usuário realiza login
2. A API gera um token JWT
3. O token deve ser enviado nas requisições protegidas
4. O filtro de segurança valida o token antes de liberar acesso ao endpoint

---

## Integração com API Externa

O serviço realiza integração com a API **ViaCEP** para obtenção automática de informações de endereço.

Essa comunicação é realizada utilizando **FeignClient**.

Fluxo:

1. Usuário informa o CEP
2. O serviço consulta a API ViaCEP
3. Os dados retornados são utilizados para compor o endereço do usuário

---

## Modelo de Dados

### Entidade Usuario

Campos principais:

- id
- nome
- email
- senha
- enderecos
- telefones

Relacionamentos:

- Um usuário pode possuir múltiplos **endereços**
- Um usuário pode possuir múltiplos **telefones**

A entidade implementa `UserDetails`, permitindo integração com o Spring Security para autenticação baseada em email e senha.

---

## Endpoints da API

### Endpoints Protegidos (Requerem JWT)

GET /usuario  
Busca dados do usuário por email.

PUT /usuario  
Atualiza os dados do usuário.

PUT /usuario/telefone  
Atualiza telefone do usuário.

POST /usuario/telefone  
Salva telefone de usuário.

PUT /usuario/endereco  
Atualiza endereço do usuário.

POST /usuario/endereco  
Salva endereço de usuário.

DELETE /usuario/{email}  
Remove um usuário do sistema.

---

## Tratamento de Exceções

A aplicação utiliza um **tratamento global de exceções**, garantindo respostas padronizadas para erros da API.

Isso melhora:

- legibilidade das respostas
- padronização de erros
- experiência do cliente da API

---

## Qualidade de Código

O projeto utiliza **SonarQube** para análise de qualidade de código, garantindo boas práticas de desenvolvimento.

---

## Como Executar o Projeto

```bash

git clone https://github.com/GabrielNoleto/usuario.git

cd usuario

docker-compose up --build

http://localhost:8080/swagger-ui/index.html#/
