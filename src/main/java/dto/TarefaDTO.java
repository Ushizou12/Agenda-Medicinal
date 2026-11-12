package dto;

import java.time.LocalDateTime;
import model.Categoria;

/**
 *
 * @author 2025001861
 */
public record TarefaDTO(
        String nome,
        String descricao,
        LocalDateTime dataLimite,
        Categoria categoria,
        int prioridade,
        boolean concluida) {
}