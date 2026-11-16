package dao;

import exception.RegraException;
import jakarta.persistence.EntityManager;
import java.util.HashSet;
import model.Cadastro;

/**
 *
 * @author Ushizou
 */
public class CadastroDAOJpa extends GenericJpaDAO<Cadastro> implements CadastroDAO{
    
    public CadastroDAOJpa() {
        super(Cadastro.class);
    }

    @Override
    public boolean add(Cadastro c) {
        if (c == null) {
            throw new RegraException("Cadastro nao pode ser nulo.");
        }
        emTransacao(em -> {
            em.persist(c);   // persist preenche o id na PROPRIA instancia c
            return null;
        });
        return true;
    }

    @Override
    public boolean remove(Cadastro c) {
        if (c == null) {
            throw new RegraException("Cadastro nao pode ser nulo.");
        }
        return emTransacao(em -> {
            Cadastro existente = (c.getId() != null)
                    ? em.find(Cadastro.class, c.getId())
                    : buscarPorUsuario(em, c.getUsuario().getNome());
            if (existente == null) {
                return false;
            }
            em.remove(existente);
            return true;
        });
    }

    @Override
    public Cadastro find(Cadastro c) {
        if (c == null) {
            throw new RegraException("Cadastro de busca nao pode ser nulo.");
        }
        return acharPorUsuario(c.getUsuario().getNome());
    }

    @Override
    public boolean update(Cadastro c) {
        if (c == null) {
            throw new RegraException("Cadastro nao pode ser nulo.");
        }
        return update(c.getUsuario().getNome(), c);
    }

    @Override
    public boolean update(String nomeAntigo, Cadastro novo) {
        if (nomeAntigo == null || nomeAntigo.isBlank()) {
            throw new RegraException("Nome do cadastro para atualizar nao pode ser vazio.");
        }
        if (novo == null) {
            throw new RegraException("Cadastro nao pode ser nulo.");
        }
        return emTransacao(em -> {
            Cadastro existente = buscarPorUsuario(em, novo.getUsuario().getNome());
            if (existente == null) {
                return false;
            }
            novo.setId(existente.getId());   // o DAO resolve o id; o DTO nao precisa dele
            em.merge(novo);
            return true;
        });
    }
    
    @Override
    public Cadastro acharPorUsuario(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new RegraException("Nome para busca nao pode ser vazio.");
        }
        return emTransacao(em -> buscarPorUsuario(em, nome));
    }
    
    @Override
    public HashSet<Cadastro> getDados() {
        return (HashSet<Cadastro>) listar();
    }

    private Cadastro buscarPorUsuario(EntityManager em, String nome) {
        return em.createQuery("SELECT c FROM Cadastro c WHERE c.nome = :nome", Cadastro.class)
                .setParameter("nome", nome)
                .getResultStream().findFirst().orElse(null);
    }
}
