/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package command;
import dto.RemedioDTO;
import dto.TarefaDTO;
import facade.AgendaFacade;
import java.util.List;
import view.LeitorConsole;

/**
 *
 * @author mathe
 */

/** Opcao 4 - cria uma tarefa e uma agenda vinculada a um remedio do usuario. */
public class CriarTarefaAgendaComando implements Comando {

    private final LeitorConsole leitor;
    private final AgendaFacade facade;

    public CriarTarefaAgendaComando(LeitorConsole leitor, AgendaFacade facade) {
        this.leitor = leitor;
        this.facade = facade;
    }

    @Override
    public void executar() {
        System.out.println("\n=== CRIAR TAREFA E AGENDA ===");

        String usuario = leitor.lerTexto("Nome do usuario para vincular o remedio: ");
        List<RemedioDTO> remedios = facade.listarRemedios(usuario);
        if (remedios.isEmpty()) {
            System.out.println("Usuario sem remedios. Agenda nao criada.");
            return;
        }

        System.out.println("Remedios disponiveis:");
        leitor.exibirOpcoes(remedios.stream().map(RemedioDTO::nome).toList());
        int indice = leitor.lerIndice("Escolha o numero do remedio: ", remedios.size());

        TarefaDTO tarefa = leitor.lerTarefa();

        String tituloAgenda = leitor.lerTexto("Titulo da agenda: ");
        String descricaoAgenda = leitor.lerTexto("Descricao da agenda: ");
        boolean concluida = leitor.lerSimNao("Agenda concluida? (s/n): ");

        String agenda = facade.criarAgenda(usuario, indice, tarefa,
                tituloAgenda, descricaoAgenda, concluida);

        System.out.println("\nAgenda criada com sucesso!");
        System.out.println(agenda);
    }
}
