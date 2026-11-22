package command;

import dto.AgendaDTO;
import dto.TarefaDTO;
import facade.AgendaFacade;
import java.util.List;
import view.AgendaView;
import view.LeitorConsole;

/**
 * Opcao 13 - remove uma tarefa de uma agenda. A tarefa e apagada do banco de
 * dados; a agenda e as outras tarefas dela continuam existindo.
 */
public class RemoverTarefaAgendaComando implements Comando {

    private final LeitorConsole leitor;
    private final AgendaFacade facade;

    public RemoverTarefaAgendaComando(LeitorConsole leitor, AgendaFacade facade) {
        this.leitor = leitor;
        this.facade = facade;
    }

    @Override
    public void executar() {
        System.out.println("\n=== REMOVER TAREFA DE UMA AGENDA ===");

        List<AgendaDTO> agendas = facade.listarAgendas().stream()
                .filter(a -> !a.tarefas().isEmpty())
                .toList();
        if (agendas.isEmpty()) {
            System.out.println("Nenhuma agenda possui tarefas.");
            return;
        }

        System.out.println("Agendas com tarefas:");
        leitor.exibirOpcoes(agendas.stream().map(AgendaView::resumo).toList());
        AgendaDTO agenda = agendas.get(leitor.lerIndice("Escolha o numero da agenda: ", agendas.size()));

        List<TarefaDTO> tarefas = agenda.tarefas();
        System.out.println("\nTarefas da agenda '" + agenda.nome() + "':");
        leitor.exibirOpcoes(tarefas.stream().map(AgendaView::resumoTarefa).toList());
        TarefaDTO tarefa = tarefas.get(
                leitor.lerIndice("Escolha o numero da tarefa para remover: ", tarefas.size()));

        if (!leitor.lerSimNao("Remover a tarefa '" + tarefa.nome() + "'? (s/n): ")) {
            System.out.println("Remocao cancelada.");
            return;
        }

        facade.removerTarefa(agenda.id(), tarefa.id());
        System.out.println("Tarefa removida com sucesso!");
    }
}
