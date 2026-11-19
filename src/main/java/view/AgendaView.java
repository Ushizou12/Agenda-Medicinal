package view;

import dto.AgendaDTO;
import java.time.format.DateTimeFormatter;

/**
 * Formata uma {@link AgendaDTO} para exibicao no console.
 */
public final class AgendaView {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private AgendaView() {
    }

    /** Resumo de uma linha, usado nas listas de escolha (ex.: remover agenda). */
    public static String resumo(AgendaDTO a) {
        return a.data().format(FORMATO_DATA) + " - " + a.nome();
    }

    /** Texto completo de uma agenda. */
    public static String formatar(AgendaDTO a) {
        String remedio = (a.remedio() != null)
                ? a.remedio().nome() + " - " + a.statusRemedio()
                : "Nenhum";
        String tarefa = (a.tarefa() != null) ? a.tarefa().nome() : "Nenhuma";

        return """
               ========================================
                             ITEM DA AGENDA
               ========================================
               Compromisso       : %s
               Data e Hora       : %s
               Descricao         : %s
               Status            : %s
               Remedio vinculado : %s
               Tarefa vinculada  : %s
               ========================================"""
                .formatted(a.nome(), a.data().format(FORMATO_DATA), a.descricao(),
                        a.concluida() ? "Concluido" : "Pendente", remedio, tarefa);
    }
}
