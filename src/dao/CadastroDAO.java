/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import exception.RegraException;
import java.util.HashSet;
import model.Cadastro;

/**
 * DAO responsável por persistir (em memória) os cadastros de usuários.
 * @author Ushizou
 */
public class CadastroDAO implements OperacoesDAO<Cadastro> {

    private HashSet<Cadastro> lista = new HashSet<Cadastro>();

    /**
     * Adiciona um cadastro à base.
     *
     * @param c cadastro a ser adicionado, não pode ser nulo
     * @return {@code true} se adicionado com sucesso
     * @throws RegraException se {@code c} for nulo ou ocorrer falha ao adicionar
     */
    @Override
    public boolean add(Cadastro c) {
        try {
            if (c == null) {
                throw new RegraException("Cadastro não pode ser nulo.");
            }
            return lista.add(c);
        } catch (RegraException re) {
            throw re;
        } catch (Exception e) {
            throw new RegraException("Erro ao adicionar cadastro.", e);
        }
    }

    /**
     * Remove um cadastro da base.
     *
     * @param c cadastro a ser removido
     * @return {@code true} se removido com sucesso
     * @throws RegraException se {@code c} for nulo
     */
    @Override
    public boolean remove(Cadastro c) {
        try {
            if (c == null) {
                throw new RegraException("Cadastro não pode ser nulo.");
            }
            return lista.remove(c);
        } catch (RegraException re) {
            throw re;
        } catch (Exception e) {
            throw new RegraException("Erro ao remover cadastro.", e);
        }
    }

    /**
     * Procura um cadastro equivalente na base.
     *
     * @param c cadastro de referência para busca (via {@code equals})
     * @return o cadastro encontrado, ou {@code null} se não existir
     * @throws RegraException se {@code c} for nulo
     */
    @Override
    public Cadastro find(Cadastro c) {
        if (c == null) {
            throw new RegraException("Cadastro de busca não pode ser nulo.");
        }
        for (Cadastro cadastro : lista) {
            if (cadastro.equals(c)) {
                return cadastro;
            }
        }
        return null;
    }

    /**
     * @return todos os cadastros armazenados
     */
    public HashSet<Cadastro> getDados() {
        return lista;
    }

    /**
     * Busca um cadastro a partir do nome do usuário vinculado.
     *
     * @param nomeUsuario nome do usuário a procurar; não pode ser nulo ou vazio
     * @return o cadastro correspondente, ou {@code null} se não encontrado
     * @throws RegraException se {@code nomeUsuario} for nulo ou vazio
     */
    public Cadastro acharPorUsuario(String nomeUsuario) {
        if (nomeUsuario == null || nomeUsuario.isBlank()) {
            throw new RegraException("Nome de usuario para busca não pode ser vazio.");
        }
        for (Cadastro cadastro : lista) {
            if (nomeUsuario.equals(cadastro.getUsuario().getNome())) {
                return cadastro;
            }
        }
        return null;
    }
}
