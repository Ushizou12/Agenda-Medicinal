package command;

import dto.TarefaDTO;
import facade.AgendaFacade;
import java.util.List;
import view.LeitorConsole;

/**
 * Opcao 8 - atualiza uma tarefa existente. A tarefa e escolhida pelo numero
 * na lista (e atualizada pelo id), entao nomes repetidos nao causam confusao.
 *
 * @author mathe
 */
public class AtualizarTarefaComando implements Comando {

    private final LeitorConsole leitor;
    private final AgendaFacade facade;

    public AtualizarTarefaComando(LeitorConsole leitor, AgendaFacade facade) {
        this.leitor = leitor;
        this.facade = facade;
    }

    @Override
    public void executar() {
        System.out.println("\n=== ATUALIZAR TAREFA ===");

        List<TarefaDTO> tarefas = facade.listarTarefas();
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa cadastrada.");
            return;
        }

        System.out.println("\nTarefas cadastradas (por prioridade):");
        leitor.exibirOpcoes(tarefas.stream()
                .map(t -> t.nome() + " (prioridade " + t.prioridade() + ")")
                .toList());
        int indice = leitor.lerIndice("Escolha o numero da tarefa: ", tarefas.size());
        TarefaDTO escolhida = tarefas.get(indice);

        System.out.println("Informe os novos dados:");
        TarefaDTO nova = leitor.lerTarefa();

        System.out.println(facade.atualizarTarefa(escolhida.id(), nova)
                ? "Tarefa atualizada com sucesso!"
                : "Nao foi possivel atualizar a tarefa.");
    }
}
