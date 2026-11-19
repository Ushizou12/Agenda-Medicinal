package dao;

import util.JPAUtil;
import exception.RegraException;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.function.Function;
/**
 *
 * @author Ushizou
 */
public abstract class GenericJpaDAO<T> {
    private final Class<T> classe;

    protected GenericJpaDAO(Class<T> classe) {
        this.classe = classe;
    }

    protected <R> R emTransacao(Function<EntityManager, R> acao) {
        EntityManager em = JPAUtil.criarEntityManager();
        try {
            em.getTransaction().begin();
            R resultado = acao.apply(em);
            em.getTransaction().commit();
            return resultado;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            if (e instanceof RegraException) {
                throw e;
            }
            throw new RegraException("Erro de persistencia: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public List<T> listar() {
        return emTransacao(em -> em
                .createQuery("SELECT e FROM " + classe.getSimpleName() + " e", classe)
                .getResultList());
    }

    /**
     * Busca uma entidade pelo id, usando {@code em.find}.
     *
     * @param id id da entidade
     * @return a entidade, ou {@code null} se nao existir
     */
    public T buscarPorId(Long id) {
        if (id == null) {
            throw new RegraException("O id nao pode ser nulo.");
        }
        return emTransacao(em -> em.find(classe, id));
    }

    /**
     * Remove a entidade com o id informado.
     *
     * @param id id da entidade
     * @return {@code true} se removeu, {@code false} se nao existia
     */
    public boolean removerPorId(Long id) {
        if (id == null) {
            throw new RegraException("O id nao pode ser nulo.");
        }
        return emTransacao(em -> {
            T existente = em.find(classe, id);
            if (existente == null) {
                return false;
            }
            em.remove(existente);
            return true;
        });
    }
}
