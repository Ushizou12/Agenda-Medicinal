package dto;

import java.util.List;

/**
 * Dados de um cadastro trocados entre a view e a facade.
 *
 * <p>O campo {@code senha} tem dois significados: vindo da view (cadastro ou
 * atualização) é a senha digitada; vindo da facade é apenas o <b>hash</b> da
 * senha, nunca o texto original.</p>
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