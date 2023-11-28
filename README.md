# Sua Barbearia

Sua Barbearia é uma aplicação web desenvolvida em React que permite agendar serviços em barbearias, gerenciar agendamentos e realizar pagamentos automaticamente. A aplicação se integra a um sistema de gerenciamento desenvolvido em Spring Boot para a gestão das barbearias e seus serviços.

## Funcionalidades

- **Agendamento de Serviços:** Os clientes podem agendar serviços em suas barbearias favoritas, escolhendo a data, horário e profissional de sua preferência.

- **Gerenciamento de Barbearias:** Proprietários de barbearias podem gerenciar suas operações, incluindo a criação e edição de serviços, a programação de horários e o acompanhamento de agendamentos e do faturamento.

- **Pagamento Automático:** A aplicação permite o pagamento automático dos serviços agendados, oferecendo uma experiência conveniente aos clientes.

## Rotas

### Usuários
```
POST /users/signup: Cadastro de novos usuários.
POST /users/signin: Autenticação de usuários.
GET /users/profile: Informações do perfil do usuário logado.
GET /users/{id}: Detalhes de um usuário específico.
PATCH /users/edit/{id}: Atualização de informações do usuário.
DELETE /users/delete/{id}: Exclusão de um usuário.
POST /users/fav/barbershop/{id}: Adicionar barbearia aos favoritos.
POST /users/unfav/barbershop/{id}: Remover barbearia dos favoritos.
GET /users/schedulings: Lista de agendamentos do usuário.
GET /users/schedulings/{initialDate}/{endDate}: Lista de agendamentos em um período específico.
GET /users/barbershops: Lista de barbearias favoritas do usuário.
```

### Agendamentos
```
GET /schedulings/{id}: Detalhes de um agendamento específico.
POST /schedulings/create: Criação de um novo agendamento.
PATCH /schedulings/edit/{id}: Atualização de um agendamento.
PATCH /schedulings/cancel/{id}: Cancelamento de um agendamento.
```

### Serviços
```
GET /services/{id}: Detalhes de um serviço específico.
POST /services/create: Criação de um novo serviço.
PATCH /services/edit/{id}: Atualização de informações de um serviço.
PATCH /services/enable/{id}: Ativar um serviço.
PATCH /services/disable/{id}: Desativar um serviço.
```

### Barbearias
```
GET /barbershops: Lista de todas as barbearias.
GET /barbershops/{id}: Detalhes de uma barbearia específica.
GET /barbershops/profile: Informações do perfil da barbearia logada.
POST /barbershops/signup: Cadastro de novas barbearias.
POST /barbershops/signin: Autenticação de barbearias.
PATCH /barbershops/edit/{id}: Atualização de informações da barbearia.
DELETE /barbershops/delete/{id}: Exclusão de uma barbearia.
GET /barbershops/users: Lista de usuários associados à barbearia.
GET /barbershops/employees: Lista de funcionários da barbearia.
GET /barbershops/services: Lista de serviços oferecidos pela barbearia.
GET /barbershops/schedulings: Lista de agendamentos da barbearia.
GET /barbershops/schedulings/{initialDate}/{endDate}: Lista de agendamentos em um período específico.
PATCH /barbershops/schedulings/finish/{id}: Concluir um agendamento.
PATCH /barbershops/schedulings/foul/{id}: Registrar uma falta em um agendamento.
GET /barbershops/earnings/total: Total de ganhos da barbearia.
GET /barbershops/earnings/{initialDate}/{endDate}: Ganhos da barbearia em um período específico.
```

### Funcionários
```
GET /employee: Lista de todos os funcionários.
GET /employee/{id}: Detalhes de um funcionário específico.
POST /employee/create: Cadastro de novos funcionários.
POST /employee/signin: Autenticação de funcionários.
PATCH /employee/edit/{id}: Atualização de informações do funcionário.
DELETE /employee/delete/{id}: Exclusão de um funcionário.
GET /employee/comission/total: Total de comissões do funcionário.
GET /employee/comission/{initialDate}/{endDate}: Comissões do funcionário em um período específico.
PATCH /employee/block/{date}: Bloquear agenda do funcionário em uma data específica.
```

## Licença

Este projeto está licenciado sob a Licença MIT - consulte o arquivo [LICENSE.md](LICENSE.md) para obter detalhes.
