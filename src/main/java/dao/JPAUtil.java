/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
/**
 *
 * @author Ushizou
 */
public class JPAUtil {
    private static final EntityManagerFactory EMF
            = Persistence.createEntityManagerFactory("agendaPU");

    private JPAUtil() {
    }

    public static EntityManager criarEntityManager() {
        return EMF.createEntityManager();
    }

    public static void fechar() {
        EMF.close();
    }
}
