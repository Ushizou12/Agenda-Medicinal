package command;

import dto.AgendaDTO;
import dto.RemedioDTO;
import dto.TarefaDTO;
import facade.AgendaFacade;
import java.util.List;
import view.AgendaView;
import view.LeitorConsole;

/**
 * Opcao 4 - cria uma tarefa. Toda tarefa pertence a uma agenda, entao o usuario
 * escolhe entre criar uma agenda nova para ela ou vincula-la a uma agenda que
 * ja existe. Vincular um remedio a uma agenda nova e opcional.
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
        System.out.println("\n=== CRIAR TAREFA ===");

        // As agendas pertencem a um cadastro, entao o usuario e sempre necessario.
        String usuario = leitor.lerTexto("Nome do usuario: ");
        List<AgendaDTO> agendas = facade.listarAgendas(usuario);   // falha se o usuario nao existir

        System.out.println("1 - Criar uma nova agenda para esta tarefa");
        System.out.println("2 - Vincular a uma agenda existente");

        switch (leitor.lerInt("Opcao: ")) {
            case 1 -> novaAgenda(usuario);
            case 2 -> agendaExistente(usuario, agendas);
            default -> System.out.println("Opcao invalida.");
        }
    }

    /** Cria uma agenda nova ja contendo a tarefa. */
    private void novaAgenda(String usuario) {
        RemedioDTO remedio = escolherRemedio(usuario);

        TarefaDTO tarefa = leitor.lerTarefa();

        String titulo = leitor.lerTexto("Titulo da agenda: ");
        String descricao = leitor.lerTexto("Descricao da agenda: ");
        boolean concluida = leitor.lerSimNao("Agenda concluida? (s/n): ");

        AgendaDTO dto = new AgendaDTO(null, tarefa.dataLimite(), titulo, descricao,
                concluida, remedio, null, List.of(tarefa));
        AgendaDTO criada = facade.criarAgenda(usuario, dto);

        System.out.println("\nAgenda criada com sucesso!");
        System.out.println(AgendaView.formatar(criada));
    }

    /** Acrescenta a tarefa a uma das agendas que o usuario ja tem. */
    private void agendaExistente(String usuario, List<AgendaDTO> agendas) {
        if (agendas.isEmpty()) {
            System.out.println("Este usuario ainda nao possui agendas. Vamos criar uma nova.");
            novaAgenda(usuario);
            return;
        }

        System.out.println("Agendas do usuario:");
        leitor.exibirOpcoes(agendas.stream().map(AgendaView::resumo).toList());
        AgendaDTO escolhida = agendas.get(leitor.lerIndice("Escolha o numero da agenda: ", agendas.size()));

        TarefaDTO tarefa = leitor.lerTarefa();
        facade.adicionarTarefa(escolhida.id(), tarefa);

        System.out.println("Tarefa vinculada a agenda '" + escolhida.nome() + "' com sucesso!");
    }

    /** Pergunta se quer vincular um remedio do usuario; devolve {@code null} se nao. */
    private RemedioDTO escolherRemedio(String usuario) {
        if (!leitor.lerSimNao("Deseja vincular um remedio a esta agenda? (s/n): ")) {
            return null;
        }
        List<RemedioDTO> remedios = facade.listarRemedios(usuario);
        if (remedios.isEmpty()) {
            System.out.println("Usuario sem remedios. A agenda sera criada sem remedio.");
            return null;
        }
        System.out.println("Remedios disponiveis:");
        leitor.exibirOpcoes(remedios.stream().map(RemedioDTO::nome).toList());
        return remedios.get(leitor.lerIndice("Escolha o numero do remedio: ", remedios.size()));
    }
}
