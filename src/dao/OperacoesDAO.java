/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

/**
 *
 * @author Ushizou
 */
public interface OperacoesDAO<C> {

    public boolean add(C o);

    public boolean remove(C o);

    public C find(C o);
}
