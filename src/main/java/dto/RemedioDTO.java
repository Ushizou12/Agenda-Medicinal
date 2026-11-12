package dto;

import java.time.LocalDate;

/**
 *
 * @author 2025001861
 */

public record RemedioDTO(
        String nome,
        String descricao,
        int qtdPilulasAtual,
        LocalDate dataInicioMedicacao,
        double preco,
        int pilulasPorDia) {
}
