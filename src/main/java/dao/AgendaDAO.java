package dao;

import java.util.List;
import model.Agenda;

/**
 * Operacoes de persistencia de {@link Agenda}. As agendas sao identificadas
 * pelo id, ja que o nome pode se repetir.
 */
public interface AgendaDAO extends OperacoesDAO<Agenda> {

    /** Busca uma agenda pelo id (retorna {@code null} se nao existir). */
    Agenda buscarPorId(Long id);

    /**
     * Remove a agenda de id informado e, em cascata, todas as suas tarefas;
     * {@code false} se ela nao existir.
     */
    boolean removerPorId(Long id);

    /** Lista todas as agendas, da data mais antiga para a mais recente. */
    List<Agenda> listarPorData();
}
