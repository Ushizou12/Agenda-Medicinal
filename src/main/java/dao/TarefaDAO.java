package dao;

import java.util.Collection;
import model.Tarefa;

/**
 * Operacoes de persistencia de {@link Tarefa}. As tarefas sao identificadas
 * pelo id, ja que o nome pode se repetir.
 */
public interface TarefaDAO extends OperacoesDAO<Tarefa> {

    /** Busca uma tarefa pelo id (retorna {@code null} se nao existir). */
    Tarefa buscarPorId(Long id);

    /** Substitui os dados da tarefa de id informado; {@code false} se ela nao existir. */
    boolean update(Long id, Tarefa nova);
    
    /** Remove uma tarefa pelo id; {@code false} se ela nao existir. */
    boolean removerPorId(Long id);

    Collection<Tarefa> getDados();
}
