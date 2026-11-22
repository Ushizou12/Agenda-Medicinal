package dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Dados de uma agenda trocados entre a view e a facade.
 *
 * <p>Ao criar uma agenda, {@code id} e {@code statusRemedio} ficam {@code null}.
 * Ao listar, a facade preenche todos os campos.</p>
 *
 * @param id id da agenda no banco ({@code null} se for nova)
 * @param remedio remédio vinculado, ou {@code null} se a agenda não tem remédio
 * @param statusRemedio texto de status do remédio (preenchido pela facade)
 * @param tarefas tarefas da agenda (lista vazia se não tiver nenhuma; nunca
 * {@code null})
 */
public record AgendaDTO(
        Long id,
        LocalDateTime data,
        String nome,
        String descricao,
        boolean concluida,
        RemedioDTO remedio,
        String statusRemedio,
        List<TarefaDTO> tarefas) {

    public AgendaDTO {
        tarefas = (tarefas == null) ? List.of() : List.copyOf(tarefas);
    }
}
