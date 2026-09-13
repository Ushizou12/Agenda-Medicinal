/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import dao.CadastroDAO;
import exception.RegraException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import model.*;

/**
 *
 * @author 2025001861
 */
public class main {

    public static int lerInteiroNaoNegativo(Scanner sc, String mensagem) {
        System.out.print(mensagem);
        int valor = Integer.parseInt(sc.nextLine());

        if (valor < 0) {
            throw new IllegalArgumentException("Nao sao permitidos valores negativos.");
        }

        return valor;
    }

    public static double lerDoubleNaoNegativo(Scanner sc, String mensagem) {
        System.out.print(mensagem);
        double valor = Double.parseDouble(sc.nextLine());

        if (valor < 0) {
            throw new IllegalArgumentException("Nao sao permitidos valores negativos.");
        }

        return valor;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        CadastroDAO dao = new CadastroDAO();
        Agenda agendaAtual = null;

        int opcao;

        try {
            do {
                System.out.println("\n===== MENU =====");
                System.out.println("1 - Cadastrar usuario/pessoa/remedio");
                System.out.println("2 - Listar cadastros");
                System.out.println("3 - Buscar cadastro por usuario");
                System.out.println("4 - Criar tarefa e agenda");
                System.out.println("5 - Mostrar agenda atual");
                System.out.println("6 - Adicionar ou remover remedios");
                System.out.println("0 - Sair");
                System.out.print("Escolha uma opcao: ");
                opcao = Integer.parseInt(sc.nextLine());

                switch (opcao) {

                    case 1 -> {
                        System.out.println("\n=== CADASTRO ===");

                        // -------- USUARIO --------
                        System.out.print("Nome do usuario: ");
                        String nomeUsuario = sc.nextLine();

                        System.out.print("Email do usuario: ");
                        String email = sc.nextLine();

                        System.out.print("Senha do usuario (minimo 6 caracteres): ");
                        String senha = sc.nextLine();

                        Usuario usuario = new Usuario(nomeUsuario, email, senha);

                        // -------- PESSOA --------
                        System.out.print("Nome da pessoa: ");
                        String nomePessoa = sc.nextLine();

                        System.out.print("Idade: ");
                        int idade = Integer.parseInt(sc.nextLine());
                        if (idade < 0) {
                            throw new IllegalArgumentException("Idade nao pode ser negativa.");
                        }

                        System.out.print("Altura (ex: 1.75): ");
                        double altura = Double.parseDouble(sc.nextLine());
                        if (altura < 0) {
                            throw new IllegalArgumentException("Altura nao pode ser negativa.");
                        }

                        System.out.print("Peso (ex: 78.5): ");
                        double peso = Double.parseDouble(sc.nextLine());
                        if (peso < 0) {
                            throw new IllegalArgumentException("Peso nao pode ser negativo.");
                        }

                        Pessoa pessoa = new Pessoa(nomePessoa, idade, altura, peso);

                        // -------- REMEDIO --------
                        System.out.print("Nome do remedio: ");
                        String nomeRemedio = sc.nextLine();

                        System.out.print("Descricao do remedio: ");
                        String descricao = sc.nextLine();

                        System.out.print("Quantidade de pilulas: ");
                        int qtdPilulas = Integer.parseInt(sc.nextLine());
                        if (qtdPilulas < 0) {
                            throw new IllegalArgumentException("Quantidade de pilulas nao pode ser negativa.");
                        }

                        System.out.print("Data de inicio da medicacao - ano: ");
                        int ano = Integer.parseInt(sc.nextLine());

                        System.out.print("Mes: ");
                        int mes = Integer.parseInt(sc.nextLine());

                        System.out.print("Dia: ");
                        int dia = Integer.parseInt(sc.nextLine());

                        LocalDate dataInicio = LocalDate.of(ano, mes, dia);

                        System.out.print("Preco do remedio: ");
                        double preco = Double.parseDouble(sc.nextLine());
                        if (preco < 0) {
                            throw new IllegalArgumentException("Preco nao pode ser negativo.");
                        }

                        System.out.print("Pilulas por dia: ");
                        int pilulasPorDia = Integer.parseInt(sc.nextLine());
                        if (pilulasPorDia < 0) {
                            throw new IllegalArgumentException("Pilulas por dia nao pode ser negativo.");
                        }

                        Remedio remedio = new Remedio(
                                nomeRemedio,
                                descricao,
                                qtdPilulas,
                                dataInicio,
                                preco,
                                pilulasPorDia
                        );

                        Cadastro cadastro = new Cadastro(pessoa, usuario, remedio);

                        // Perguntar se quer adicionar mais remedios
                        String continuar;
                        do {
                            System.out.print("Adicionar outro remedio? (s/n): ");
                            continuar = sc.nextLine();
                            if (continuar.equalsIgnoreCase("s")) {

                                System.out.print("Nome do remedio: ");
                                String nomeNovoRemedio = sc.nextLine();

                                System.out.print("Descricao do remedio: ");
                                String descricaoNovoRemedio = sc.nextLine();

                                System.out.print("Quantidade de pilulas: ");
                                int qtdNovas = Integer.parseInt(sc.nextLine());
                                if (qtdNovas < 0) {
                                    throw new IllegalArgumentException("Quantidade de pilulas nao pode ser negativa.");
                                }

                                System.out.print("Data de inicio da medicacao - ano: ");
                                int anoNovo = Integer.parseInt(sc.nextLine());

                                System.out.print("Mes: ");
                                int mesNovo = Integer.parseInt(sc.nextLine());

                                System.out.print("Dia: ");
                                int diaNovo = Integer.parseInt(sc.nextLine());

                                LocalDate dataNovaInicio = LocalDate.of(anoNovo, mesNovo, diaNovo);

                                System.out.print("Preco do remedio: ");
                                double precoNovo = Double.parseDouble(sc.nextLine());
                                if (precoNovo < 0) {
                                    throw new IllegalArgumentException("Preco nao pode ser negativo.");
                                }

                                System.out.print("Pilulas por dia: ");
                                int pilulasPorDiaNovo = Integer.parseInt(sc.nextLine());
                                if (pilulasPorDiaNovo < 0) {
                                    throw new IllegalArgumentException("Pilulas por dia nao pode ser negativo.");
                                }

                                Remedio novoRemedio = new Remedio(
                                        nomeNovoRemedio,
                                        descricaoNovoRemedio,
                                        qtdNovas,
                                        dataNovaInicio,
                                        precoNovo,
                                        pilulasPorDiaNovo
                                );

                                cadastro.adicionarRemedio(novoRemedio);
                                System.out.println("Remedio adicionado com sucesso!");
                            }
                        } while (continuar.equalsIgnoreCase("s"));
                        dao.add(cadastro);
                    }

                    case 2 -> {
                        System.out.println("\n=== LISTA DE CADASTROS ===");
                        if (dao.getDados().isEmpty()) {
                            System.out.println("Nenhum cadastro encontrado.");
                        } else {
                            for (Cadastro c : dao.getDados()) {
                                System.out.println(c);
                            }
                        }
                    }

                    case 3 -> {
                        System.out.println("\n=== BUSCAR CADASTRO POR USUARIO ===");

                        System.out.print("Nome do usuario: ");
                        String nomeUsuario = sc.nextLine();

                        Cadastro resultado = dao.acharPorUsuario(nomeUsuario);

                        if (resultado != null) {
                            System.out.println("Cadastro encontrado:");
                            System.out.println(resultado);
                        } else {
                            System.out.println("Usuario nao encontrado.");
                        }
                    }

                    case 4 -> {
                        System.out.println("\n=== CRIAR TAREFA E AGENDA ===");

                        System.out.print("Titulo da tarefa: ");
                        String titulo = sc.nextLine();

                        System.out.print("Descricao da tarefa: ");
                        String descricaoTarefa = sc.nextLine();

                        System.out.println("Data e hora da tarefa:");

                        System.out.print("Ano: ");
                        int ano = Integer.parseInt(sc.nextLine());

                        System.out.print("Mes: ");
                        int mes = Integer.parseInt(sc.nextLine());

                        System.out.print("Dia: ");
                        int dia = Integer.parseInt(sc.nextLine());

                        System.out.print("Hora: ");
                        int hora = Integer.parseInt(sc.nextLine());

                        System.out.print("Minuto: ");
                        int minuto = Integer.parseInt(sc.nextLine());

                        LocalDateTime dataHora = LocalDateTime.of(ano, mes, dia, hora, minuto);

                        // Buscar cadastro para pegar o remedio
                        System.out.print("Nome do usuario para vincular o remedio: ");
                        String nomeVinculo = sc.nextLine();
                        Cadastro cadastroVinculado = dao.acharPorUsuario(nomeVinculo);

                        Remedio remedioAgenda = null;
                        if (cadastroVinculado != null && !cadastroVinculado.getRemedios().isEmpty()) {
                            System.out.println("Remedios disponiveis:");
                            List<Remedio> lista = cadastroVinculado.getRemedios();
                            for (int i = 0; i < lista.size(); i++) {
                                System.out.println((i + 1) + " - " + lista.get(i).getNome());
                            }
                            System.out.print("Escolha o numero do remedio: ");
                            int escolha = Integer.parseInt(sc.nextLine()) - 1;
                            if (escolha >= 0 && escolha < lista.size()) {
                                remedioAgenda = lista.get(escolha);
                            }
                        } else {
                            System.out.println("Nenhum cadastro/remedio encontrado. Agenda sem remedio vinculado.");
                            break;
                        }

                        Categoria categoria = null;

                        while (categoria == null) {
                            System.out.println("\nEscolha a categoria:");
                            System.out.println("1 - ANTIBIOTICO");
                            System.out.println("2 - SUPLEMENTO");
                            System.out.println("3 - USO_CONTINUO");
                            System.out.print("Opcao: ");
                            int opCategoria = Integer.parseInt(sc.nextLine());

                            switch (opCategoria) {
                                case 1 ->
                                    categoria = Categoria.ANTIBIOTICO;
                                case 2 ->
                                    categoria = Categoria.SUPLEMENTO;
                                case 3 ->
                                    categoria = Categoria.USO_CONTINUO;
                                default ->
                                    System.out.println("Opcao invalida. Digite 1, 2 ou 3.");
                            }
                        }

                        int prioridade;

                        do {
                            System.out.print("Prioridade da tarefa (1 a 3): ");
                            prioridade = Integer.parseInt(sc.nextLine());

                            if (prioridade < 1 || prioridade > 3) {
                                System.out.println("Valor invalido! Digite apenas 1, 2 ou 3.");
                            }

                        } while (prioridade < 1 || prioridade > 3);

                        boolean concluida;

                        while (true) {
                            System.out.print("Tarefa concluida? (s/n): ");
                            String resposta = sc.nextLine().trim().toLowerCase();

                            if (resposta.equals("s")) {
                                concluida = true;
                                break;
                            } else if (resposta.equals("n")) {
                                concluida = false;
                                break;
                            } else {
                                System.out.println("Opcao invalida. Digite apenas 's' ou 'n'.");
                            }
                        }

                        Tarefa tarefa = new Tarefa(
                                titulo,
                                descricaoTarefa,
                                dataHora,
                                categoria,
                                prioridade,
                                concluida
                        );

                        System.out.print("Titulo da agenda: ");
                        String tituloAgenda = sc.nextLine();

                        System.out.print("Descricao da agenda: ");
                        String descricaoAgenda = sc.nextLine();

                        boolean agendaConcluida;

                        while (true) {
                            System.out.print("Agenda concluida? (s/n): ");
                            String resposta = sc.nextLine().trim().toLowerCase();

                            if (resposta.equals("s")) {
                                agendaConcluida = true;
                                break;
                            } else if (resposta.equals("n")) {
                                agendaConcluida = false;
                                break;
                            } else {
                                System.out.println("Opcao invalida. Digite apenas 's' ou 'n'.");
                            }
                        }

                        agendaAtual = new Agenda(
                                dataHora,
                                tituloAgenda,
                                descricaoAgenda,
                                remedioAgenda,
                                tarefa,
                                agendaConcluida
                        );

                        System.out.println("\nAgenda criada com sucesso!");
                        System.out.println(agendaAtual);
                    }

                    case 5 -> {
                        System.out.println("\n=== AGENDA ATUAL ===");
                        if (agendaAtual != null) {
                            System.out.println(agendaAtual);
                        } else {
                            System.out.println("Nenhuma agenda foi criada ainda.");
                        }
                    }

                    case 6 -> {
                        System.out.println("\n=== ADICIONAR OU REMOVER REMEDIOS ===");

                        System.out.print("Nome do usuario: ");
                        String nomeUsuario6 = sc.nextLine();

                        Cadastro cadastro6 = dao.acharPorUsuario(nomeUsuario6);

                        if (cadastro6 == null) {
                            System.out.println("Usuario nao encontrado.");
                            break;
                        }

                        System.out.println("Cadastro encontrado: " + cadastro6.getUsuario().getNome());
                        System.out.println("1 - Adicionar remedio");
                        System.out.println("2 - Remover remedio");
                        System.out.print("Opcao: ");
                        int opcao6 = Integer.parseInt(sc.nextLine());

                        switch (opcao6) {

                            case 1 -> {
                                System.out.print("Nome do remedio: ");
                                String nomeR = sc.nextLine();

                                System.out.print("Descricao: ");
                                String descR = sc.nextLine();

                                System.out.print("Quantidade de pilulas: ");
                                int qtdR = Integer.parseInt(sc.nextLine());
                                if (qtdR < 0) {
                                    throw new IllegalArgumentException("Quantidade de pilulas nao pode ser negativa.");
                                }

                                System.out.print("Ano de inicio: ");
                                int anoR = Integer.parseInt(sc.nextLine());

                                System.out.print("Mes: ");
                                int mesR = Integer.parseInt(sc.nextLine());

                                System.out.print("Dia: ");
                                int diaR = Integer.parseInt(sc.nextLine());

                                LocalDate dataR = LocalDate.of(anoR, mesR, diaR);

                                System.out.print("Preco: ");
                                double precoR = Double.parseDouble(sc.nextLine());
                                if (precoR < 0) {
                                    throw new IllegalArgumentException("Preco nao pode ser negativo.");
                                }

                                System.out.print("Pilulas por dia: ");
                                int ppd = Integer.parseInt(sc.nextLine());
                                if (ppd < 0) {
                                    throw new IllegalArgumentException("Pilulas por dia nao pode ser negativo.");
                                }

                                Remedio novoR = new Remedio(nomeR, descR, qtdR, dataR, precoR, ppd);
                                cadastro6.adicionarRemedio(novoR);
                                System.out.println("Remedio adicionado com sucesso!");
                            }

                            case 2 -> {
                                List<Remedio> remedios6 = cadastro6.getRemedios();

                                if (remedios6.isEmpty()) {
                                    System.out.println("Nenhum remedio cadastrado para este usuario.");
                                    break;
                                }

                                System.out.println("Remedios disponiveis:");
                                for (int i = 0; i < remedios6.size(); i++) {
                                    System.out.println((i + 1) + " - " + remedios6.get(i).getNome());
                                }

                                System.out.print("Escolha o numero do remedio para remover: ");
                                int escolhaR = Integer.parseInt(sc.nextLine()) - 1;

                                if (escolhaR >= 0 && escolhaR < remedios6.size()) {
                                    Remedio removido = remedios6.get(escolhaR);
                                    cadastro6.removerRemedio(removido);
                                    System.out.println("Remedio '" + removido.getNome() + "' removido com sucesso!");
                                } else {
                                    System.out.println("Opcao invalida.");
                                }
                            }

                            default ->
                                System.out.println("Opcao invalida.");
                        }
                    }

                    case 0 ->
                        System.out.println("Encerrando o programa...");

                    default ->
                        System.out.println("Opcao invalida.");
                }

            } while (opcao != 0);

            sc.close();
        } catch (NumberFormatException e) {
            System.out.println("Erro: valor numerico invalido.");
        } catch (DateTimeException e) {
            System.out.println("Erro: data ou hora invalida.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (RegraException re) {
            System.out.println("Erro de regra de negocio: " + re.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }
}
