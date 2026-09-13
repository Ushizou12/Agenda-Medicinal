/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.HashSet;
import model.Cadastro;

/**
 *
 * @author Ushizou
 */
public class CadastroDAO implements OperacoesDAO<Cadastro> {

    private HashSet<Cadastro> lista = new HashSet<Cadastro>();

    @Override
    public boolean add(Cadastro c) {
        if (c instanceof Cadastro) {
            lista.add(c);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Cadastro c) {
        if (c instanceof Cadastro) {
            lista.remove(c);
            return true;
        }
        return false;
    }

    @Override
    public Cadastro find(Cadastro c) {
        if (c instanceof Cadastro) {

            for (Cadastro tarefa : lista) {

                if (tarefa.equals(c)) {
                    return tarefa;
                }
            }
        }
        return null;
    }

    public HashSet<Cadastro> getDados() {
    return lista;
    }

    // Recebe um String nome de usuario para achar o usuario, retornando o cadastro do usuario.
    public Cadastro acharPorUsuario(String nomeUsuario) {
        for (Cadastro cadastro : lista) {
            if (nomeUsuario.equals(cadastro.getUsuario().getNome())) {
                return cadastro;
            }
        }
        return null;
    }
}
