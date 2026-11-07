/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package command;

/**
 *
 * @author mathe
 */

import exception.RegraException;
import java.time.DateTimeException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Menu {

    private record Item(String descricao, Comando comando) {}

    private final Map<Integer, Item> itens = new LinkedHashMap<>();

    public void registrar(int opcao, String descricao, Comando comando) {
        itens.put(opcao, new Item(descricao, comando));
    }

    public void exibir() {
        System.out.println("\n===== MENU =====");
        itens.forEach((opcao, item) -> System.out.println(opcao + " - " + item.descricao()));
        System.out.print("Escolha uma opcao: ");
    }

    public void executar(int opcao) {
        Item item = itens.get(opcao);
        if (item == null) {
            System.out.println("Opcao invalida.");
            return;
        }
        try {
            item.comando().executar();
        } catch (NumberFormatException e) {
            System.out.println("Erro: valor numerico invalido.");
        } catch (DateTimeException e) {
            System.out.println("Erro: data ou hora invalida.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (RegraException e) {
            System.out.println("Erro de regra de negocio: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }
}
