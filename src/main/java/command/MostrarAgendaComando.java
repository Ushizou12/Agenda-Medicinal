package command;

import dto.AgendaDTO;
import facade.AgendaFacade;
import java.util.List;
import view.AgendaView;

/**
 * Opcao 5 - lista todas as agendas, ordenadas por data.
 *
 * @author mathe
 */
public class MostrarAgendaComando implements Comando {

    private final AgendaFacade facade;

    public MostrarAgendaComando(AgendaFacade facade) {
        this.facade = facade;
    }

    @Override
    public void executar() {
        System.out.println("\n=== AGENDAS (ordenadas por data) ===");
        List<AgendaDTO> agendas = facade.listarAgendas();

        if (agendas.isEmpty()) {
            System.out.println("Nenhuma agenda foi criada ainda.");
            return;
        }
        agendas.forEach(a -> System.out.println(AgendaView.formatar(a)));
    }
}
