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


/** Opcao 11 - remove um cadastro inteiro (e seus remedios, agendas e tarefas). */
public class RemoverCadastroComando implements Comando {

    private final LeitorConsole leitor;
    private final AgendaFacade facade;

    public RemoverCadastroComando(LeitorConsole leitor, AgendaFacade facade) {
        this.leitor = leitor;
        this.facade = facade;
    }

    @Override
    public void executar() {
        System.out.println("\n=== REMOVER CADASTRO ===");
        String usuario = leitor.lerTexto("Nome do usuario: ");
        if (!leitor.lerSimNao("Remover '" + usuario + "' e todos os seus remedios, agendas e tarefas? (s/n): ")) {
            System.out.println("Remocao cancelada.");
            return;
        }
        facade.removerCadastro(usuario);
        System.out.println("Cadastro removido com sucesso!");
    }
}