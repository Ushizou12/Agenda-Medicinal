/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package command;
import facade.AgendaFacade;

/**
 *
 * @author mathe
 */

/** Opcao 5 - mostra a agenda criada mais recentemente. */
public class MostrarAgendaComando implements Comando {

    private final AgendaFacade facade;

    public MostrarAgendaComando(AgendaFacade facade) {
        this.facade = facade;
    }

    @Override
    public void executar() {
        System.out.println("\n=== AGENDA ATUAL ===");
        facade.mostrarAgendaAtual().ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Nenhuma agenda foi criada ainda."));
    }
}
