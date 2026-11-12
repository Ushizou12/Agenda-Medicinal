/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package command;
import facade.AgendaFacade;
import view.LeitorConsole;

/**
 *
 * @author mathe
 */

/** Opcao 3 - busca um cadastro pelo nome do usuario. */
public class BuscarCadastroComando implements Comando {

    private final LeitorConsole leitor;
    private final AgendaFacade facade;

    public BuscarCadastroComando(LeitorConsole leitor, AgendaFacade facade) {
        this.leitor = leitor;
        this.facade = facade;
    }

    @Override
    public void executar() {
        System.out.println("\n=== BUSCAR CADASTRO POR USUARIO ===");
        String nome = leitor.lerTexto("Nome do usuario: ");

        facade.relatorioCadastro(nome).ifPresentOrElse(
                cadastro -> {
                    System.out.println("Cadastro encontrado:");
                    System.out.println(cadastro);
                },
                () -> System.out.println("Usuario nao encontrado."));
    }
}
