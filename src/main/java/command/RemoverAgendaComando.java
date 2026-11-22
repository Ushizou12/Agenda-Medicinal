package command;

import dto.AgendaDTO;
import facade.AgendaFacade;
import java.util.List;
import view.AgendaView;
import view.LeitorConsole;

/**
 * Opcao 10 - remove uma agenda. Todas as tarefas dela sao removidas junto.
 */
public class RemoverAgendaComando implements Comando {

    private final LeitorConsole leitor;
    private final AgendaFacade facade;

    public RemoverAgendaComando(LeitorConsole leitor, AgendaFacade facade) {
        this.leitor = leitor;
        this.facade = facade;
    }

    @Override
    public void executar() {
        System.out.println("\n=== REMOVER AGENDA ===");

        List<AgendaDTO> agendas = facade.listarAgendas();
        if (agendas.isEmpty()) {
            System.out.println("Nenhuma agenda cadastrada.");
            return;
        }

        System.out.println("Agendas cadastradas:");
        leitor.exibirOpcoes(agendas.stream().map(AgendaView::resumo).toList());
        int indice = leitor.lerIndice("Escolha o numero da agenda para remover: ", agendas.size());
        AgendaDTO escolhida = agendas.get(indice);

        if (!leitor.lerSimNao("Remover a agenda '" + escolhida.nome() + "' e suas "
                + escolhida.tarefas().size() + " tarefa(s)? (s/n): ")) {
            System.out.println("Remocao cancelada.");
            return;
        }

        facade.removerAgenda(escolhida.id());
        System.out.println("Agenda removida com sucesso! (as tarefas dela tambem foram removidas)");
    }
}
