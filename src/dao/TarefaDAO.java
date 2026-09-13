/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import model.Tarefa;

/**
 *
 * @author 2025001861
 */
public class TarefaDAO implements OperacoesDAO<Tarefa> {

    private HashSet<Tarefa> lista = new HashSet<Tarefa>();

    @Override
    public boolean add(Tarefa t) {
        if (t instanceof Tarefa) {
            lista.add(t);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Tarefa t) {
        if (t instanceof Tarefa) {
            lista.remove(t);
            return true;
        }
        return false;
    }

    @Override
    public Tarefa find(Tarefa t) {
        if (t instanceof Tarefa) {

            for (Tarefa tarefa : lista) {

                if (tarefa.equals(t)) {
                    return tarefa;
                }
            }
        }
        return null;
    }

    public Collection getDados() {
        return lista;
    }
    
    // Recebe um nome e retorna uma tarefa com o mesmo.
    public Tarefa acharPorNome(String nome) {

        for (Tarefa tarefa : lista) {

            if (nome.equals(tarefa.getNome())) {
                return tarefa;
            }
        }

        return null;
    }
    
    // Recebe um integer prioridade e retorna uma tarefa com esta prioridade.
    public Tarefa acharPorPrioridade(int prioridade) {

        for (Tarefa tarefa : lista) {

            if (prioridade == tarefa.getPrioridade()) {
                return tarefa;
            }
        }
        return null;
    }
    
    // Recebe uma data e retorna uma tarefa baseado nessa data.
    public Tarefa acharPorDatalimite(LocalDateTime data) {

        for (Tarefa tarefa : lista) {

            if (data.isEqual(tarefa.getDataLimite())) {
                return tarefa;
            }
        }
        return null;
    }
}
