package dto;

import java.time.LocalDateTime;

/**
 * Dados de uma agenda trocados entre a view e a facade.
 *
 * <p>Ao criar uma agenda, {@code id} e {@code statusRemedio} ficam {@code null}.
 * Ao listar, a facade preenche todos os campos.</p>
 *
 * @param id id da agenda no banco ({@code null} se for nova)
 * @param remedio remédio vinculado, ou {@code null} se a agenda não tem remédio
 * @param statusRemedio texto de status do remédio (preenchido pela facade)
 * @param tarefa tarefa vinculada, ou {@code null} se a agenda não tem tarefa
 */
public record AgendaDTO(
        Long id,
        LocalDateTime data,
        String nome,
        String descricao,
        boolean concluida,
        RemedioDTO remedio,
        String statusRemedio,
        TarefaDTO tarefa) {
}
