/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import exception.RegraException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import model.Tarefa;

/**
 * DAO responsável por persistir (em memória) as tarefas cadastradas.
 *
 * @author 2025001861
 */
public class TarefaDAO implements OperacoesDAO<Tarefa> {

    private HashSet<Tarefa> lista = new HashSet<Tarefa>();

    /**
     * Adiciona uma tarefa à base.
     *
     * @param t tarefa a ser adicionada, não pode ser nula
     * @return {@code true} se adicionada com sucesso
     * @throws RegraException se {@code t} for nula ou ocorrer falha ao
     * adicionar
     */
    @Override
    public boolean add(Tarefa t) {
        try {
            if (t == null) {
                throw new RegraException("Tarefa nao pode ser nula.");
            }
            return lista.add(t);
        } catch (RegraException re) {
            throw re;
        } catch (Exception e) {
            throw new RegraException("Erro ao adicionar tarefa.", e);
        }
    }

    /**
     * Remove uma tarefa da base.
     *
     * @param t tarefa a ser removida, não pode ser nula
     * @return {@code true} se removida com sucesso
     * @throws RegraException se {@code t} for nula ou ocorrer falha ao remover
     */
    @Override
    public boolean remove(Tarefa t) {
        try {
            if (t == null) {
                throw new RegraException("Tarefa nao pode ser nula.");
            }
            return lista.remove(t);
        } catch (RegraException re) {
            throw re;
        } catch (Exception e) {
            throw new RegraException("Erro ao remover tarefa.", e);
        }
    }

    /**
     * Procura uma tarefa equivalente na base.
     *
     * @param t tarefa de referência para busca (via {@code equals})
     * @return a tarefa encontrada, ou {@code null} se não existir
     * @throws RegraException se {@code t} for nula
     */
    @Override
    public Tarefa find(Tarefa t) {
        if (t == null) {
            throw new RegraException("Tarefa de busca nao pode ser nula.");
        }
        for (Tarefa tarefa : lista) {
            if (tarefa.equals(t)) {
                return tarefa;
            }
        }
        return null;
    }

    /**
     * @return todas as tarefas armazenadas
     */
    public Collection<Tarefa> getDados() {
        return lista;
    }

    /**
     * Busca uma tarefa pelo nome.
     *
     * @param nome nome da tarefa a procurar; não pode ser nulo ou vazio
     * @return a tarefa correspondente, ou {@code null} se não encontrada
     * @throws RegraException se {@code nome} for nulo ou vazio
     */
    public Tarefa acharPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new RegraException("Nome para busca nao pode ser vazio.");
        }
        for (Tarefa tarefa : lista) {
            if (nome.equals(tarefa.getNome())) {
                return tarefa;
            }
        }
        return null;
    }

    /**
     * Busca uma tarefa pela prioridade.
     *
     * @param prioridade valor entre 1 e 3
     * @return a primeira tarefa encontrada com essa prioridade, ou {@code null}
     * @throws RegraException se {@code prioridade} estiver fora do intervalo
     * 1-3
     */
    public Tarefa acharPorPrioridade(int prioridade) {
        if (prioridade < 1 || prioridade > 3) {
            throw new RegraException("Prioridade de busca deve estar entre 1 e 3.");
        }
        for (Tarefa tarefa : lista) {
            if (prioridade == tarefa.getPrioridade()) {
                return tarefa;
            }
        }
        return null;
    }

    /**
     * Busca uma tarefa por data limite exata.
     *
     * @param data data limite a comparar; não pode ser nula
     * @return a tarefa correspondente, ou {@code null} se não encontrada
     * @throws RegraException se {@code data} for nula
     */
    public Tarefa acharPorDatalimite(LocalDateTime data) {
        if (data == null) {
            throw new RegraException("Data para busca nao pode ser nula.");
        }
        for (Tarefa tarefa : lista) {
            if (data.isEqual(tarefa.getDataLimite())) {
                return tarefa;
            }
        }
        return null;
    }

    @Override
    public boolean update(Tarefa t) {
        if (t == null) {
            throw new RegraException("Tarefa nao pode ser nula.");
        }

        Tarefa existente = acharPorNome(t.getNome());

        if (existente == null) {
            return false;
        }

        lista.remove(existente);
        lista.add(t);

        return true;
    }

    /**
     * Atualiza uma tarefa existente.
     *
     * A tarefa é localizada pelo nome antigo. Depois de encontrada, ela é
     * removida do HashSet e substituida pela nova tarefa.
     *
     * @param nomeAntigo nome atual da tarefa que será atualizada
     * @param t tarefa com os novos dados; não pode ser nula
     * @return {@code true} se a tarefa foi encontrada e atualizada,
     * {@code false} caso a tarefa não seja encontrada
     * @throws RegraException se a tarefa for nula ou o nome antigo for nulo ou
     * vazio
     */
    public boolean update(String nomeAntigo, Tarefa t) {
        if (nomeAntigo == null || nomeAntigo.isBlank()) {
            throw new RegraException(
                    "Nome da tarefa para atualizar nao pode ser vazio."
            );
        }

        if (t == null) {
            throw new RegraException(
                    "Tarefa nao pode ser nula."
            );
        }

        Tarefa existente = acharPorNome(nomeAntigo);

        if (existente == null) {
            return false;
        }

        lista.remove(existente);
        lista.add(t);

        return true;
    }

}
