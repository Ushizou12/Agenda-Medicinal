package dao;

import exception.RegraException;
import java.util.List;
import model.Agenda;

public class AgendaDAOJpa extends GenericJpaDAO<Agenda> implements AgendaDAO {

    public AgendaDAOJpa() {
        super(Agenda.class);
    }

    @Override
    public boolean add(Agenda a) {
        if (a == null) {
            throw new RegraException("Agenda nao pode ser nula.");
        }
        emTransacao(em -> {
            em.persist(a);   // o cascade PERSIST tambem salva a tarefa nova da agenda
            return null;
        });
        return true;
    }

    @Override
    public boolean remove(Agenda a) {
        if (a == null) {
            throw new RegraException("Agenda nao pode ser nula.");
        }
        return removerPorId(a.getId());
    }

    @Override
    public Agenda find(Agenda a) {
        if (a == null) {
            throw new RegraException("Agenda de busca nao pode ser nula.");
        }
        return buscarPorId(a.getId());
    }

    @Override
    public boolean update(Agenda a) {
        if (a == null) {
            throw new RegraException("Agenda nao pode ser nula.");
        }
        if (a.getId() == null) {
            throw new RegraException("O id da agenda nao pode ser nulo.");
        }
        return emTransacao(em -> {
            if (em.find(Agenda.class, a.getId()) == null) {
                return false;
            }
            em.merge(a);
            return true;
        });
    }

    @Override
    public List<Agenda> listarPorData() {
        return emTransacao(em -> em
                .createQuery("SELECT a FROM Agenda a ORDER BY a.data", Agenda.class)
                .getResultList());
    }
}
