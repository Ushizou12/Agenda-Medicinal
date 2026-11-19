package dao;

import exception.RegraException;
import java.util.Collection;
import model.Tarefa;

public class TarefaDAOJpa extends GenericJpaDAO<Tarefa> implements TarefaDAO {

    public TarefaDAOJpa() {
        super(Tarefa.class);
    }

    @Override
    public boolean add(Tarefa t) {
        if (t == null) {
            throw new RegraException("Tarefa nao pode ser nula.");
        }
        emTransacao(em -> {
            em.persist(t);   // persist preenche o id na PROPRIA instancia t
            return null;
        });
        return true;
    }

    @Override
    public boolean remove(Tarefa t) {
        if (t == null) {
            throw new RegraException("Tarefa nao pode ser nula.");
        }
        return removerPorId(t.getId());
    }

    @Override
    public Tarefa find(Tarefa t) {
        if (t == null) {
            throw new RegraException("Tarefa de busca nao pode ser nula.");
        }
        return buscarPorId(t.getId());
    }

    @Override
    public boolean update(Tarefa t) {
        if (t == null) {
            throw new RegraException("Tarefa nao pode ser nula.");
        }
        return update(t.getId(), t);
    }

    @Override
    public boolean update(Long id, Tarefa nova) {
        if (id == null) {
            throw new RegraException("O id da tarefa nao pode ser nulo.");
        }
        if (nova == null) {
            throw new RegraException("Tarefa nao pode ser nula.");
        }
        return emTransacao(em -> {
            Tarefa existente = em.find(Tarefa.class, id);
            if (existente == null) {
                return false;
            }
            nova.setId(id);        // mantem o id: o merge atualiza a MESMA linha
            em.merge(nova);
            return true;
        });
    }

    @Override
    public Collection<Tarefa> getDados() {
        return listar();
    }
}
