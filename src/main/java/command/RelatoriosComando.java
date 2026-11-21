package command;

import facade.AgendaFacade;
import java.time.format.DateTimeFormatter;
import view.LeitorConsole;

/** Opcao 12 - resumo das tarefas cadastradas e previsao de reposicao de remedios. */
public class RelatoriosComando implements Comando {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final LeitorConsole leitor;
    private final AgendaFacade facade;

    public RelatoriosComando(LeitorConsole leitor, AgendaFacade facade) {
        this.leitor = leitor;
        this.facade = facade;
    }

    @Override
    public void executar() {
        System.out.println("\n=== RESUMO DAS TAREFAS ===");

        System.out.println("\nQuantas tarefas existem em cada categoria:");
        var porCategoria = facade.tarefasPorCategoria();
        if (porCategoria.isEmpty()) {
            System.out.println("  Nenhuma tarefa cadastrada.");
        } else {
            porCategoria.forEach((categoria, qtd) ->
                    System.out.println("  " + categoria.getUso() + ": " + qtd + " tarefa(s)"));
        }

        System.out.println("\nTarefas em ordem de prazo (as mais proximas primeiro):");
        var porPrazo = facade.listarTarefasPorPrazo();
        if (porPrazo.isEmpty()) {
            System.out.println("  Nenhuma tarefa cadastrada.");
        } else {
            porPrazo.forEach(t -> System.out.println(
                    "  Prazo " + t.dataLimite().format(FMT) + " - " + t.nome()
                    + " (" + (t.concluida() ? "concluida" : "pendente") + ")"));
        }

        System.out.println("\n=== PREVISAO DE REPOSICAO DE REMEDIOS ===");
        String usuario = leitor.lerTexto("Nome do usuario: ");
        facade.proximoARepor(usuario).ifPresentOrElse(
                resumo -> System.out.println("Proximo remedio a repor: " + resumo),
                () -> System.out.println("Este usuario nao possui remedios cadastrados."));
    }
}