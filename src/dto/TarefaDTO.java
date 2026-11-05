/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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