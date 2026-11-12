/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package command;
import dto.TarefaDTO;
import facade.AgendaFacade;
import java.util.List;
import view.LeitorConsole;

/**
 *
 * @author mathe
 */

/** Opcao 8 - atualiza uma tarefa existente (localizada pelo nome). */
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
        tarefas.forEach(t -> System.out.println("- " + t.nome() + " (prioridade " + t.prioridade() + ")"));

        String nomeAtual = leitor.lerTexto("Nome atual da tarefa: ");
        boolean existe = tarefas.stream().anyMatch(t -> t.nome().equals(nomeAtual));
        if (!existe) {
            System.out.println("Tarefa nao encontrada.");
            return;
        }

        System.out.println("Informe os novos dados:");
        TarefaDTO nova = leitor.lerTarefa();

        System.out.println(facade.atualizarTarefa(nomeAtual, nova)
                ? "Tarefa atualizada com sucesso!"
                : "Nao foi possivel atualizar a tarefa.");
    }
}