/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package command;
import dto.CadastroDTO;
import dto.RemedioDTO;
import exception.RegraException;
import facade.AgendaFacade;
import java.util.ArrayList;
import java.util.List;
import view.LeitorConsole;

/**
 *
 * @author mathe
 */


/** Opcao 7 - atualiza dados do usuario/pessoa e, se quiser, um remedio. */
public class AtualizarCadastroComando implements Comando {

    private final LeitorConsole leitor;
    private final AgendaFacade facade;

    public AtualizarCadastroComando(LeitorConsole leitor, AgendaFacade facade) {
        this.leitor = leitor;
        this.facade = facade;
    }

    @Override
    public void executar() {
        System.out.println("\n=== ATUALIZAR CADASTRO ===");

        String nomeAtual = leitor.lerTexto("Nome atual do usuario: ");
        CadastroDTO atual = facade.buscarPorUsuario(nomeAtual)
                .orElseThrow(() -> new RegraException("Usuario nao encontrado."));

        System.out.println("Informe os novos dados:");
        CadastroDTO novo = leitor.lerCadastro(() -> atualizarRemedios(atual.remedios()));

        facade.atualizarCadastro(nomeAtual, novo);
        System.out.println("Cadastro atualizado com sucesso!");
    }

    private List<RemedioDTO> atualizarRemedios(List<RemedioDTO> atuais) {
        List<RemedioDTO> remedios = new ArrayList<>(atuais);

        if (remedios.isEmpty()) {
            System.out.println("Este cadastro nao possui remedios. Cadastre um remedio.");
            remedios.add(leitor.lerRemedio());
        } else if (leitor.lerSimNao("Deseja atualizar algum remedio? (s/n): ")) {
            System.out.println("\nRemedios cadastrados:");
            leitor.exibirOpcoes(remedios.stream().map(RemedioDTO::nome).toList());
            int indice = leitor.lerIndice("Escolha o numero do remedio: ", remedios.size());
            remedios.set(indice, leitor.lerRemedio());
        }
        return remedios;
    }
}