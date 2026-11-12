package view;

import command.AtualizarCadastroComando;
import command.AtualizarTarefaComando;
import command.BuscarCadastroComando;
import command.CadastrarComando;
import command.CriarTarefaAgendaComando;
import command.GerenciarRemediosComando;
import command.ListarCadastrosComando;
import command.Menu;
import command.MostrarAgendaComando;
import dao.CadastroDAO;
import dao.TarefaDAOJpa;
import dao.JPAUtil;
import facade.AgendaFacade;
import java.util.Scanner;

/**
 *
 * @author 2025001861
 */

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LeitorConsole leitor = new LeitorConsole(sc);
        AgendaFacade facade = new AgendaFacade(new CadastroDAO(), new TarefaDAOJpa());

        Menu menu = new Menu();
        menu.registrar(1, "Cadastrar usuario/pessoa/remedio", new CadastrarComando(leitor, facade));
        menu.registrar(2, "Listar cadastros", new ListarCadastrosComando(facade));
        menu.registrar(3, "Buscar cadastro por usuario", new BuscarCadastroComando(leitor, facade));
        menu.registrar(4, "Criar tarefa e agenda", new CriarTarefaAgendaComando(leitor, facade));
        menu.registrar(5, "Mostrar agenda atual", new MostrarAgendaComando(facade));
        menu.registrar(6, "Adicionar ou remover remedios", new GerenciarRemediosComando(leitor, facade));
        menu.registrar(7, "Atualizar cadastro", new AtualizarCadastroComando(leitor, facade));
        menu.registrar(8, "Atualizar tarefa", new AtualizarTarefaComando(leitor, facade));
        menu.registrar(0, "Sair", () -> System.out.println("Encerrando o programa...")); // lambda

        int opcao;
        do {
            menu.exibir();
            opcao = lerOpcao(leitor);
            menu.executar(opcao);
        } while (opcao != 0);
        JPAUtil.fechar();
        sc.close();
    }

    private static int lerOpcao(LeitorConsole leitor) {
        try {
            return leitor.lerInt("");
        } catch (NumberFormatException e) {
            return -1; // o Menu responde "Opcao invalida."
        }
    }
}