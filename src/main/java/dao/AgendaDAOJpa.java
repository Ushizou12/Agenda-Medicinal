/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import exception.RegraException;
import jakarta.persistence.EntityManager;
import java.util.Collection;
import model.Agenda;
import model.Tarefa;

/**
 *
 * @author Ushizou
 */
public class AgendaDAOJpa extends GenericJpaDAO<Agenda> implements AgendaDAO{
    
    public AgendaDAOJpa(){
        super(Agenda.class);
    }
    
    @Override
    public boolean add(Agenda a) {
        if (a == null) {
            throw new RegraException("Agenda nao pode ser nula.");
        }
        emTransacao(em -> {
            em.persist(a);   // persist preenche o id na PROPRIA instancia a
            return null;
        });
        return true;
    }

    @Override
    public boolean remove(Agenda a) {
        if (a == null) {
            throw new RegraException("Agenda nao pode ser nula.");
        }
        return emTransacao(em -> {
            Agenda existente = (a.getId() != null)
                    ? em.find(Agenda.class, a.getId())
                    : buscarPorNome(em, a.getNome());
            if (existente == null) {
                return false;
            }
            em.remove(existente);
            return true;
        });
    }

    @Override
    public Agenda find(Agenda a) {
        if (a == null) {
            throw new RegraException("Agenda de busca nao pode ser nula.");
        }
        return acharPorNome(a.getNome());
    }

    @Override
    public boolean update(Agenda a) {
        if (a == null) {
            throw new RegraException("Agenda nao pode ser nula.");
        }
        return update(a.getNome(), a);
    }

    public boolean update(String nomeAntigo, Agenda nova) {
        if (nomeAntigo == null || nomeAntigo.isBlank()) {
            throw new RegraException("Nome da tarefa para atualizar nao pode ser vazio.");
        }
        if (nova == null) {
            throw new RegraException("Agenda nao pode ser nula.");
        }
        return emTransacao(em -> {
            Agenda existente = buscarPorNome(em, nomeAntigo);
            if (existente == null) {
                return false;
            }
            nova.setId(existente.getId());   // o DAO resolve o id; o DTO nao precisa dele
            em.merge(nova);
            return true;
        });
    }
    
    public Agenda acharPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new RegraException("Nome para busca nao pode ser vazio.");
        }
        return emTransacao(em -> buscarPorNome(em, nome));
    }

    public Collection<Agenda> getDados() {
        return listar();
    }

    private Agenda buscarPorNome(EntityManager em, String nome) {
        return em.createQuery("SELECT a FROM Agenda a WHERE a.nome = :nome", Agenda.class)
                .setParameter("nome", nome)
                .getResultStream().findFirst().orElse(null);
    }
}
