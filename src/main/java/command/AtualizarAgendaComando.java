/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package command;

import dto.AgendaDTO;
import facade.AgendaFacade;
import java.util.List;
import view.AgendaView;
import view.LeitorConsole;

/**
 *
 * @author mathe
 */


/** Opcao 9 - edita titulo, descricao e situacao de uma agenda. */
public class AtualizarAgendaComando implements Comando {

    private final LeitorConsole leitor;
    private final AgendaFacade facade;

    public AtualizarAgendaComando(LeitorConsole leitor, AgendaFacade facade) {
        this.leitor = leitor;
        this.facade = facade;
    }

    @Override
    public void executar() {
        System.out.println("\n=== ATUALIZAR AGENDA ===");
        List<AgendaDTO> agendas = facade.listarAgendas();
        if (agendas.isEmpty()) {
            System.out.println("Nenhuma agenda cadastrada.");
            return;
        }
        leitor.exibirOpcoes(agendas.stream().map(AgendaView::resumo).toList());
        AgendaDTO escolhida = agendas.get(leitor.lerIndice("Escolha o numero da agenda: ", agendas.size()));

        String titulo = leitor.lerTexto("Novo titulo: ");
        String descricao = leitor.lerTexto("Nova descricao: ");
        boolean concluida = leitor.lerSimNao("Agenda concluida? (s/n): ");

        facade.atualizarAgenda(escolhida.id(), titulo, descricao, concluida);
        System.out.println("Agenda atualizada com sucesso!");
    }
}