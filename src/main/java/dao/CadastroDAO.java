/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.Collection;
import model.Cadastro;

/**
 *
 * @author Ushizou
 */
public interface CadastroDAO extends OperacoesDAO<Cadastro>{
    
    public Cadastro acharPorUsuario(String nomeUsuario);
    
    public boolean update(String nomeAntigo, Cadastro c);
    
    public Collection<Cadastro> getDados();
}
