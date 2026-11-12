/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import exception.RegraException;
import jakarta.persistence.EntityManager;
import java.util.Collection;
import model.Tarefa;
/**
 *
 * @author Ushizou
 */
public class TarefaDAOJpa extends GenericJpaDAO<Tarefa> implements TarefaDAO{

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
        return emTransacao(em -> {
            Tarefa existente = (t.getId() != null)
                    ? em.find(Tarefa.class, t.getId())
                    : buscarPorNome(em, t.getNome());
            if (existente == null) {
                return false;
            }
            em.remove(existente);
            return true;
        });
    }

    @Override
    public Tarefa find(Tarefa t) {
        if (t == null) {
            throw new RegraException("Tarefa de busca nao pode ser nula.");
        }
        return acharPorNome(t.getNome());
    }

    @Override
    public boolean update(Tarefa t) {
        if (t == null) {
            throw new RegraException("Tarefa nao pode ser nula.");
        }
        return update(t.getNome(), t);
    }

    @Override
    public boolean update(String nomeAntigo, Tarefa nova) {
        if (nomeAntigo == null || nomeAntigo.isBlank()) {
            throw new RegraException("Nome da tarefa para atualizar nao pode ser vazio.");
        }
        if (nova == null) {
            throw new RegraException("Tarefa nao pode ser nula.");
        }
        return emTransacao(em -> {
            Tarefa existente = buscarPorNome(em, nomeAntigo);
            if (existente == null) {
                return false;
            }
            nova.setId(existente.getId());   // o DAO resolve o id; o DTO nao precisa dele
            em.merge(nova);
            return true;
        });
    }

    @Override
    public Tarefa acharPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new RegraException("Nome para busca nao pode ser vazio.");
        }
        return emTransacao(em -> buscarPorNome(em, nome));
    }

    @Override
    public Collection<Tarefa> getDados() {
        return listar();
    }

    private Tarefa buscarPorNome(EntityManager em, String nome) {
        return em.createQuery("SELECT t FROM Tarefa t WHERE t.nome = :nome", Tarefa.class)
                .setParameter("nome", nome)
                .getResultStream().findFirst().orElse(null);
    }
}

