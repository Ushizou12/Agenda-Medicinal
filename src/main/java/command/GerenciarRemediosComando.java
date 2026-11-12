/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package command;

import dto.RemedioDTO;
import facade.AgendaFacade;
import java.util.List;
import view.LeitorConsole;
/**
 *
 * @author mathe
 */

/** Opcao 6 - adiciona ou remove remedios de um cadastro. */
public class GerenciarRemediosComando implements Comando {

    private final LeitorConsole leitor;
    private final AgendaFacade facade;

    public GerenciarRemediosComando(LeitorConsole leitor, AgendaFacade facade) {
        this.leitor = leitor;
        this.facade = facade;
    }

    @Override
    public void executar() {
        System.out.println("\n=== ADICIONAR OU REMOVER REMEDIOS ===");

        String usuario = leitor.lerTexto("Nome do usuario: ");
        List<RemedioDTO> remedios = facade.listarRemedios(usuario); // falha se o usuario nao existir

        System.out.println("1 - Adicionar remedio");
        System.out.println("2 - Remover remedio");

        switch (leitor.lerInt("Opcao: ")) {
            case 1 -> {
                facade.adicionarRemedio(usuario, leitor.lerRemedio());
                System.out.println("Remedio adicionado com sucesso!");
            }
            case 2 -> remover(usuario, remedios);
            default -> System.out.println("Opcao invalida.");
        }
    }

    private void remover(String usuario, List<RemedioDTO> remedios) {
        if (remedios.isEmpty()) {
            System.out.println("Nenhum remedio cadastrado para este usuario.");
            return;
        }
        System.out.println("Remedios disponiveis:");
        leitor.exibirOpcoes(remedios.stream().map(RemedioDTO::nome).toList());

        int indice = leitor.lerIndice("Escolha o numero do remedio para remover: ", remedios.size());
        String nome = facade.removerRemedio(usuario, indice);
        System.out.println("Remedio '" + nome + "' removido com sucesso!");
    }
}