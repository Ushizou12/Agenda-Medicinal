# Agenda-Medicinal

## Descrição

Agenda médica que facilita o controle de medicamentos e tarefas do dia a dia, permitindo administrar compromissos pessoais, dosagens medicinais e prioridades de cada tarefa cadastrada.  

## Funcionalidades

- Calcula quando comprar novo remédio baseado na quantidade pílulas que o usuário tem;
- Cadastro de usuários com validação de e-mail e senha, vinculados a dados pessoais (nome, idade, altura, peso) e cálculo de IMC;
- Gerenciamento de remédios com controle de quantidade de pílulas, cálculo automático da data final da medicação e alertas de estoque baixo ou esgotado;
- Cada cadastro tem uma lista de agendas, e cada agenda tem uma lista de tarefas (uma agenda pode ter várias tarefas; cada tarefa pertence a uma única agenda);
- Agenda de compromissos com data/hora, podendo vincular um remédio (opcional: se ele for removido, a agenda continua existindo, sem remédio);
- Ao criar uma tarefa, é possível criar uma nova agenda para ela ou vinculá-la a uma agenda já existente;
- Listagem das agendas ordenadas por data; remover uma agenda remove também as suas tarefas;
- Remoção de uma tarefa de uma agenda (a tarefa é apagada do banco de dados);
- Cadastro de tarefas com descrição, categoria (Antibiótico, Suplemento, Uso Contínuo), prioridade (1 a 3) e prazo, com ordenação por prioridade;
- Busca de cadastros por nome de usuário;
- Senhas guardadas apenas como hash (PBKDF2 com salt), nunca em texto puro.

## Banco de dados

Os dados são gravados com JPA/Hibernate em um banco **H2** em arquivo (`data/agenda.mv.db`, criado automaticamente na pasta de execução). Não é necessário instalar nenhum banco: as tabelas são criadas na primeira execução. Para começar do zero, feche o programa e apague a pasta `data`.

## Objetivo

Ajudar pessoas com sua organização medicinal de maneira simples, tal qual pessoas de idade, ou, muito atarefadas; tendo em vista também o aprendizado, aprofundando o conhecimento em POO (Programação Orientada a Objetos).
