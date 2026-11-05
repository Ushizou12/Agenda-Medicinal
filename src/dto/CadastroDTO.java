/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import model.Pessoa;
import model.Usuario;
import model.Remedio;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author 2025001861
 */
public record CadastroDTO(Pessoa pessoa, Usuario usuario, List<Remedio> remedios) {

    public CadastroDTO {
        Objects.requireNonNull(pessoa, "A pessoa eh obrigatória");
        Objects.requireNonNull(usuario, "O nome eh obrigatorio");
        Objects.requireNonNull(remedios, "Os remedios sao obrigatorio");
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.pessoa);
        hash = 23 * hash + Objects.hashCode(this.usuario);
        hash = 23 * hash + Objects.hashCode(this.remedios);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CadastroDTO other = (CadastroDTO) obj;
        if (!Objects.equals(this.pessoa, other.pessoa)) {
            return false;
        }
        if (!Objects.equals(this.usuario, other.usuario)) {
            return false;
        }
        return Objects.equals(this.remedios, other.remedios);
    }

    @Override
    public String toString() {
        return "CadastroDTO{" + "pessoa=" + pessoa + ", usuario=" + usuario + ", remedios=" + remedios + '}';
    }

}
