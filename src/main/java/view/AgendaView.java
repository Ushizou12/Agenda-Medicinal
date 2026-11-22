package view;

import dto.AgendaDTO;
import dto.TarefaDTO;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * Formata uma {@link AgendaDTO} para exibicao no console.
 */
public final class AgendaView {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private AgendaView() {
    }

    /** Resumo de uma linha, usado nas listas de escolha (ex.: remover agenda). */
    public static String resumo(AgendaDTO a) {
        return a.data().format(FORMATO_DATA) + " - " + a.nome()
                + " (" + a.tarefas().size() + " tarefa(s))";
    }

    /** Resumo de uma linha de uma tarefa, usado nas listas de escolha. */
    public static String resumoTarefa(TarefaDTO t) {
        return t.nome() + " (prioridade " + t.prioridade() + ", prazo "
                + t.dataLimite().format(FORMATO_DATA) + ", "
                + (t.concluida() ? "concluida" : "pendente") + ")";
    }

    /** Texto completo de uma agenda. */
    public static String formatar(AgendaDTO a) {
        String remedio = (a.remedio() != null)
                ? a.remedio().nome() + " - " + a.statusRemedio()
                : "Nenhum";
        // uma tarefa por linha, alinhadas abaixo do valor (20 = largura do rotulo)
        String tarefas = a.tarefas().isEmpty()
                ? "Nenhuma"
                : a.tarefas().stream()
                        .map(t -> "- " + resumoTarefa(t))
                        .collect(Collectors.joining("\n" + " ".repeat(20)));

        return """
               ========================================
                             ITEM DA AGENDA
               ========================================
               Compromisso       : %s
               Data e Hora       : %s
               Descricao         : %s
               Status            : %s
               Remedio vinculado : %s
               Tarefas           : %s
               ========================================"""
                .formatted(a.nome(), a.data().format(FORMATO_DATA), a.descricao(),
                        a.concluida() ? "Concluido" : "Pendente", remedio, tarefas);
    }
}
