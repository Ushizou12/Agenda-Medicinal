/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package command;

import dto.RemedioDTO;
import facade.AgendaFacade;
import java.util.ArrayList;
import java.util.List;
import view.LeitorConsole;
/**
 *
 * @author mathe
 */

/** Opcao 1 - cadastra usuario, pessoa e um ou mais remedios. */
public class CadastrarComando implements Comando {

    private final LeitorConsole leitor;
    private final AgendaFacade facade;

    public CadastrarComando(LeitorConsole leitor, AgendaFacade facade) {
        this.leitor = leitor;
        this.facade = facade;
    }

    @Override
    public void executar() {
        System.out.println("\n=== CADASTRO ===");
        facade.cadastrar(leitor.lerCadastro(this::lerRemedios));
        System.out.println("Cadastro realizado com sucesso!");
    }

    private List<RemedioDTO> lerRemedios() {
        List<RemedioDTO> remedios = new ArrayList<>();
        do {
            remedios.add(leitor.lerRemedio());
        } while (leitor.lerSimNao("Adicionar outro remedio? (s/n): "));
        return remedios;
    }
}