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

### Conductor Controller
- POST /conductor: Criar um condutor
    - Para cadastrar um condutor as seguintes regras devem ser seguidas:
        - "email" deve ser válido. Não pode ser vazio ou nulo. Não pode ser um email já cadastrado no sistema.
        - "address" não pode ser nulo ou vazio.
        - "name" não pode ser nulo ou vazio. Deve conter apenas letras e espaços. Deve ter entre 5 e 50 caracteres.
        - "phone" não pode ser nulo ou vazio. Deve conter apenas números de 11 digitos. Não pode ser um telefone já cadastrado no sistema.
        - Se for cadastrar um "vehicle": 
          - É possível cadastrar mais de um veículo por vez. Um mesmo veículo pode ser cadastrado para diferentes condutores, mas não pode ser cadastrado mais de uma vez para o mesmo condutor.
          - "plate" não pode ser nulo ou vazio.
          - "brand" não pode ser nulo ou vazio.
          - "model" não pode ser nulo ou vazio.
        - Se for cadastrar um "payment":
          - É possível cadastrar mais de um método de pagamento por vez. Um mesmo método de pagamento pode ser cadastrado para diferentes condutores, mas não pode ser cadastrado mais de uma vez para o mesmo condutor.
          - "paymentType" não pode ser nulo ou vazio. Deve ser um dos tipos de pagamento aceitos: CREDIT, DEBIT, PIX.
          - "cardNumber" só é obrigatório se for do tipo CREDIT, DEBIT.  Deve conter apenas números. Deve ter entre 13 e 16 caracteres.
          - "cardHolder" só é obrigatório se for do tipo CREDIT, DEBIT. Não pode ser nulo ou vazio. Deve conter apenas letras e espaços. 
          - "expirationDate" só é obrigatório se for do tipo CREDIT, DEBIT. Não pode ser nulo ou vazio. Deve estar no formato MM/YY.
          - "cardCvv" só é obrigatório se for do tipo CREDIT, DEBIT. Não pode ser nulo ou vazio. Deve conter apenas números. Deve ter 3 caracteres.
          - "isFavorite" obrigratoriamente um dos métodos de pagamento a ser cadastrado deve ser favorito.
    - JSON Body:
   ```JSON
  {
    "name": "Mayara Lima",
    "address": "Rua das ruas, 10. Cidade. Estado",
    "email": "mayar@email.com",
    "phone": "11904477693",
    "vehicles": [
        {
            "plate": "fxt-3458",
            "brand": "volks",
            "model": "up"
        }
    ],
    "paymentsList": [
        {
            "paymentType": "CREDIT",
            "cardNumber": "4308187095109506",
            "cardHolder": "mayara Lima",
            "expirationDate": "12/23",
            "cardCvv": "425",
            "isFavorite":true
        }
    ]
  }
   ```

- PUT /conductor{id}: é possivel atulizar os dados de um condutor:
    
    - Seguem as mesmas regras de validação do POST /conductor.
- JSON Body:
   ```JSON
  {
    "name": "Mayara Lima",
    "address": "Rua das ruas, 10. Cidade. Estado",
    "email": "may@email.com",
    "phone": "11904477693"
  }
   ```

- PUT /conductor/{id}: é possivel atulizar os dados de um condutor:
    - Seguem as mesmas regras de validação do POST /conductor.
- JSON Body:
   ```JSON
  {
      "name": "Mayara Lima",
      "address": "Rua das ruas, 10. Cidade. Estado",
      "email": "mayara@email.com",
      "phone": "11905477693"
  }
   ```

- PUT /conductor/{id}/vehicle: adicionar um veículo a um condutor:
- JSON Body:
   ```JSON
  {
      "plate": "fxt-3858",
      "brand": "volks",
      "model": "up"
  }
   ```

- PUT /conductor/{id}/payment: adicionar um pagamento a um condutor:
- JSON Body:
   ```JSON
  {
         "paymentType": "DEBIT",
         "cardNumber": "4308187095109506",
         "cardHolder": "mayara Lima",
         "expirationDate": "12/23",
         "cardCvv": "425",
         "isFavorite":false
    
  }
   ```
- GET /conductor: listar todos os condutores cadastrados
- GET /conductor/{id}: listar um condutor específico
- DELETE /conductor/{id}: deletar um condutor específico
- DELETE /conductor/vehicle/{vehicleId}: deletar um veículo específico de um condutor
- DELETE /conductor/payment/{paymentId}: deletar um método de pagamento específico de um condutor

### Vehicle Controller
- POST /vehicle/{id} inicializar umm periodo de estacionamento

  - {id} é o id do veículo que está estacionando
  - "parkingType" não pode ser nulo ou vazio. Deve ser um dos tipos de estacionamento aceitos: DURATION, FIXED.
  - "payedWith - id" não pode ser nulo ou vazio. Deve ser um dos métodos de pagamento cadastrados para o condutor.
  - Para o tipo DURATION, método de pagamento deve ser do tipo CREDIT ou DEBIT. Ele iniciado por 1h e extendido automaticamente por mais 1h até que seja finalizado.
  - Para o tipo FIXED o parametro "durationInMinutes" deve ser informado.
  - Não é possível iniciar um estacionamento se o veículo já estiver com status ACTIVE ou NEAR_EXPIRATION.

JSON Body:
   ```JSON
 {
    "parkingType":"DURATION",
    "payedWith": {
      "id": "654c2821cb8599102098b73b"
    }
}
   ```

JSON Body:
   ```JSON
 {
    "durationInMinutes": 30,
    "parkingType":"FIXED",
    "payedWith": {
      "id": "654c2821cb8599102098b73b"
    }
}
   ```
- PUT /vehicle/disableAutomaticExtension/{vehicleId} desativar a extensão automática do tempo de estacionamento.
- GET /vehicle: listar todos os veículos cadastrados
- GET /vehicle/{id}: listar um veículo específico

### Parking Controller
- GET /parking: listar todos os estacionamentos cadastrados

### Receipt Controller
- GET /receipt: listar todos os estacionamentos cadastrados
- GET /receipt/{id}: listar um estacionamento específico

## Relatório Técnico
Como ser um sistema escalavel era a principal preocupação, foi utilizado os seguintes serviços:
- Spring Boot: por ele oferecer uma configuração simplificada e suporte integrado para construção de aplicativos escaláveis.
- MongoDB: por ser um banco de dados NoSQL orientado a documentos. Ele tem uma escalabilidade horizontal, é fácil adicionar mais servidores para lidar com um maior volume de dados e tráfego. Além disso, MongoDB pode ter um desempenho melhor em operações de leitura e gravação distribuídas, especialmente em casos de cargas de trabalho intensivas em leitura.
- Redis: por ser um banco de dados em memória proporciona acesso muito rápido aos dados e é especialmente eficaz para operações de leitura e gravação rápidas. Além disso, o Redis proporciona acesso muito rápido aos dados e é especialmente eficaz para operações de leitura e gravação rápidas.
- Contêineres Docker: Para garantir que o sistema seja consistente em diferentes ambientes, facilitando a implantação e escalabilidade.

Para o sistema de monitoramento e alertas foram tomadas as seguintes decisões:
- Na classe ParkingService, no método checkExpiration, incorporamos a anotação @Scheduled para realizar execuções a cada intervalo de 5 minutos. Esse método efetua uma busca por todos os registros de estacionamento com os estados ACTIVE ou NEAR_EXPIRATION e avalia o tempo restante de estacionamento.
  - Quando o estado é ACTIVE, mas o estacionamento está prestes a expirar nos próximos 5 minutos, ocorre a alteração para NEAR_EXPIRATION, seguida pela geração de uma notificação indicando que "will expire in 5 minutes".
  - No caso de um estado NEAR_EXPIRATION com extensão automática, o período de estacionamento é prolongado por mais uma hora.
  - Se o tempo de estacionamento acabo e é sem extensão automática, é realizado a transição para EXPIRED, gerando simultaneamente um recibo que é armazenado para referência futura.
Essas otimizações no fluxo do sistema garantem um monitoramento eficaz, notificações proativas e a gestão adequada de períodos de estacionamento, proporcionando uma experiência aprimorada para os usuários.