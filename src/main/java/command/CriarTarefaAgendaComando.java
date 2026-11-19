package command;

import dto.AgendaDTO;
import dto.RemedioDTO;
import dto.TarefaDTO;
import facade.AgendaFacade;
import java.util.List;
import view.AgendaView;
import view.LeitorConsole;

/**
 * Opcao 4 - cria uma tarefa e uma agenda. Vincular um remedio e opcional: a
 * agenda pode existir sem remedio.
 *
 * @author mathe
 */
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

        String usuario = null;
        RemedioDTO remedio = null;

        if (leitor.lerSimNao("Deseja vincular um remedio a esta agenda? (s/n): ")) {
            usuario = leitor.lerTexto("Nome do usuario dono do remedio: ");
            List<RemedioDTO> remedios = facade.listarRemedios(usuario);

            if (remedios.isEmpty()) {
                System.out.println("Usuario sem remedios. A agenda sera criada sem remedio.");
            } else {
                System.out.println("Remedios disponiveis:");
                leitor.exibirOpcoes(remedios.stream().map(RemedioDTO::nome).toList());
                int indice = leitor.lerIndice("Escolha o numero do remedio: ", remedios.size());
                remedio = remedios.get(indice);
            }
        }

        TarefaDTO tarefa = leitor.lerTarefa();

        String titulo = leitor.lerTexto("Titulo da agenda: ");
        String descricao = leitor.lerTexto("Descricao da agenda: ");
        boolean concluida = leitor.lerSimNao("Agenda concluida? (s/n): ");

        AgendaDTO dto = new AgendaDTO(null, tarefa.dataLimite(), titulo, descricao,
                concluida, remedio, null, tarefa);
        AgendaDTO criada = facade.criarAgenda(usuario, dto);

        System.out.println("\nAgenda criada com sucesso!");
        System.out.println(AgendaView.formatar(criada));
    }
}
