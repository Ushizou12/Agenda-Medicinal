package dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import model.Categoria;

/**
 *
 * @author Ushizou
 */
public record AgendaDTO( 
    LocalDateTime data,
    String nome,
    String descricao,
    String nomeRemedio,
    String descricaoRemedio,
    int qtdPilulasAtual,
    LocalDate dataInicioMedicacao,
    LocalDate dataFinalMedicacao,
    double preco,
    int pilulasPorDia,
    String nomeTarefa,
    String descricaoTarefa,
    LocalDateTime dataLimite,
    Categoria categoria,    
    int prioridade,
    boolean concluida){
}
