package dto;

import java.util.List;

/**
 *
 * @author 2025001861
 */

public record CadastroDTO(
        String nomeUsuario,
        String email,
        String senha,
        String nomePessoa,
        int idade,
        double altura,
        double peso,
        List<RemedioDTO> remedios) {

    public CadastroDTO {
        remedios = List.copyOf(remedios); 
    }

    @Override
    public String toString() {
        return "CadastroDTO{nomeUsuario=" + nomeUsuario
                + ", email=" + email
                + ", nomePessoa=" + nomePessoa
                + ", remedios=" + remedios.size() + "}";
    }
}