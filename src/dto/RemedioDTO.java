/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
