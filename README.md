# Pós-Tech Arquitetura e Desenvolvimento Java
- Fase 3: Nesta terceira fase, o objetivo era criar um serviço de parquímetro escalável e confiável.

## Índice

- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Uso](#uso)
- [Relatório técnico](#relatório-técnico)

## Pré-requisitos
Para rodar o projeto na sua máquina é necessário:
- Java 17
- Maven
- Docker

## Instalação
Siga as etapas abaixo para configurar e executar o projeto em seu ambiente local:
1. Clone o repositório
   ```sh
   git clone https://github.com/mayaravlima/tech-challenge-fase-3
   ```  
2. Navegue até o diretório do projeto:
   ```sh
   cd tech-challenge-fase-3
   ```
3. Rode o comando para utilizar o Docker Compose e subir o banco de dados:
   ```sh
    docker-compose up --force-recreate -d --build
    ```
7. Acesse os endpoints
   ```sh
   localhost:8080/
   ```
## Uso
Foi utilizado o Swagger para documentar a API. Para acessar a documentação basta acessar o endpoint:

```sh
    http://localhost:8080/swagger-ui/index.html#/
```
Considerações sobre alguns endpoints:

ParkingService
private final int FIVE_MINUTES = 60 * 1000;

ParkingEntity
parkingEntity.setDurationInMinutes(2);
final int oneHourExtension = 5;


### User Controller
- POST /user: Cria um usuário.
    - Para cadastrar um user as seguintes regras devem ser seguidas:
        - "username" não pode ser nulo ou vazio. Deve contar apenas letras, números, traços e underscores. Deve ter entre 3 e 20 caracteres.
        - "email" deve ser válido. Não pode ser vazio ou nulo.
        - "name" não pode ser nulo ou vazio. Deve conter apenas letras e espaços. Deve ter entre 5 e 50 caracteres.
        - "cpf" deve ser válido. Não pode ser vazio ou nulo.
        - "dateOfBirth" não pode ser nulo ou vazio. Deve ser uma data do passado.
        - "gender" deve ser ou "FEMALE" ou "MALE"
        - "username", "email", "cpf" devem ser únicos.
    - JSON Body:
   ```JSON
  {
    "username": "maylima",
    "email": "may@email.com",
    "name": "Mayara Lima",
    "date_of_birth": "26/08/1994",
    "gender": "FEMALE",
    "cpf": "02594680028"
   }
   ```

- GET /user/search: Busca usuário com os seguintes paramentros:
    - GET /user/search?username={username}: Busca usuário pelo username.
    - GET /user/search?email={email}: Busca usuário pelo email.
    - GET /user/search?username={username}&email={email}: Busca pelo username e email.

## Relatório Técnico
Dependências novas utilizadas:
