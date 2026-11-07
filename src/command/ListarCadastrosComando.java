/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package command;
import facade.AgendaFacade;
import java.util.List;

/**
 *
 * @author mathe
 */

/** Opcao 2 - lista todos os cadastros. */
public class ListarCadastrosComando implements Comando {

    private final AgendaFacade facade;

    public ListarCadastrosComando(AgendaFacade facade) {
        this.facade = facade;
    }

    @Override
    public void executar() {
        System.out.println("\n=== LISTA DE CADASTROS ===");
        List<String> cadastros = facade.relatorioCadastros();
        if (cadastros.isEmpty()) {
            System.out.println("Nenhum cadastro encontrado.");
        } else {
            cadastros.forEach(System.out::println);
        }
    }
}
