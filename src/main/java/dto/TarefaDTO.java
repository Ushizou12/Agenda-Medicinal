package dto;

import java.time.LocalDateTime;
import model.Categoria;

/**
 * Dados de uma tarefa trocados entre a view e a facade.
 *
 * @param id id da tarefa no banco ({@code null} se for nova); como o nome pode
 * se repetir, é o id que identifica a tarefa
 */
public record TarefaDTO(
        Long id,
        String nome,
        String descricao,
        LocalDateTime dataLimite,
        Categoria categoria,
        int prioridade,
        boolean concluida) {
}
