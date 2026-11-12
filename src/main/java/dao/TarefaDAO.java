package dao;

import java.util.Collection;
import model.Tarefa;

/**
 *
 * @author Ushizou
 */
public interface TarefaDAO extends OperacoesDAO<Tarefa> {

    Tarefa acharPorNome(String nome);

    boolean update(String nomeAntigo, Tarefa nova);

    Collection<Tarefa> getDados();
}
