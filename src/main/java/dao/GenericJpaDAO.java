/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

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
}
