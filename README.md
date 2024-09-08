# Palestra Sincronização

Este projeto foi desenvolvido para ministrar uma palestra no **Caqui Inova** de Mogi das Cruzes, com o tema **sincronização de dados**. O objetivo é demonstrar conceitos práticos de sincronização e enriquecimento de dados utilizando uma arquitetura moderna com várias tecnologias integradas.

## Lógica Geral

### Bases de Dados

- **MySQL**: Armazena as informações principais das compras, como nome do produto, valor e informações básicas.
- **MongoDB**: Usado como base de contexto para armazenar informações enriquecidas, como o endereço obtido via geocodificação.

### Enriquecimento de Dados

- O sistema recebe as compras e as armazena na base core (MySQL).
- Após a compra ser registrada, ela é processada para obter informações de **geolocalização** baseadas nas coordenadas (latitude e longitude).
- O endereço obtido é adicionado à compra como contexto e salvo na base de contexto (MongoDB).
- Atualmente, o sistema usa uma geolocalização **mockada**, mas está preparado para ser integrado com serviços como **Amazon Service Location** ou **Google Geolocation API** no futuro.

### Processamento e Notificações

- O sistema utiliza **Apache Camel** para orquestrar o fluxo de dados e **Vert.x EventBus** para disparar eventos de compras realizadas.
- As compras são enviadas para um **WebSocket** que notifica interfaces conectadas em **tempo real** sobre novas compras e compras enriquecidas.

## Interfaces Frontend

- **Listagem de Compras**: Interface que exibe as compras realizadas em tempo real. Basta abrir o arquivo `index.html` localizado na pasta `front-listagem` em um navegador.
- **Criação de Compras**: Interface para simular a criação de compras. Você pode abrir o arquivo `index.html` localizado na pasta `front-compra` diretamente em um navegador.

## Como Rodar

### Utilizando Docker

Para rodar a aplicação e todos os serviços de suporte (MySQL, MongoDB, etc.), basta executar o comando:

```bash
docker-compose up
```

### Sem Docker

Caso prefira rodar sem Docker, você pode configurar a infraestrutura automaticamente utilizando o próprio **Quarkus**. Para isso, altere a seguinte propriedade no arquivo `application.properties`:

```properties
quarkus.devservices.enabled=true
```

Com essa flag ativada, o Quarkus criará automaticamente instâncias de MySQL e MongoDB para a aplicação, sem a necessidade de configurar contêineres separadamente.

## Tecnologias Utilizadas

- **Quarkus**: Framework principal da aplicação.
- **Apache Camel**: Orquestração do fluxo de dados.
- **MongoDB e MySQL**: Armazenamento de dados principais e enriquecidos.
- **Vert.x EventBus**: Para comunicação em tempo real e processamento de eventos.
- **WebSockets**: Utilizado para atualizar as interfaces frontend com dados em tempo real.
