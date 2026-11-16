package model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa o cadastro completo de um usuário do sistema, agrupando seus dados
 * pessoais ({@link Pessoa}), suas credenciais ({@link Usuario}) e a lista de
 * remédios em uso.
 *
 * @author Ushizou
 */
@Entity
public class Cadastro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "nome", column = @Column(name = "nome_pessoa"))
    private Pessoa pessoa;

    @Embedded
    @AttributeOverride(name = "nome", column = @Column(name = "nome_usuario", unique = true))
    private Usuario usuario;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "cadastro_id")
    private List<Remedio> remedios;

    protected Cadastro() {
        this.remedios = new ArrayList<>();
    }

    /**
     * Cria um novo cadastro com um primeiro remédio já vinculado.
     *
     * @param pessoa dados pessoais; não pode ser nulo
     * @param usuario credenciais do usuário; não pode ser nulo
     * @param remedio primeiro remédio a cadastrar; não pode ser nulo
     * @throws IllegalArgumentException se algum dos parâmetros for nulo
     */
    public Cadastro(Pessoa pessoa, Usuario usuario, Remedio remedio) {
        if (pessoa == null) {
            throw new IllegalArgumentException("Pessoa nao pode ser nula.");
        }
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario nao pode ser nulo.");
        }
        this.pessoa = pessoa;
        this.usuario = usuario;
        this.remedios = new ArrayList<>();
        adicionarRemedio(remedio);
    }

    /**
     * @return o id do cadastro
     */
    public Long getId() {
        return id;
    }
    
    /**
     * @return os dados pessoais do cadastro
     */
    public Pessoa getPessoa() {
        return pessoa;
    }

    /**
     * @return as credenciais do usuário do cadastro
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @return a lista de remédios cadastrados
     */
    public List<Remedio> getRemedios() {
        return remedios;
    }

    /**
     * Adiciona um remédio à lista de remédios do cadastro.
     *
     * @param remedio remédio a adicionar; não pode ser nulo
     * @throws IllegalArgumentException se {@code remedio} for nulo
     */
    public void adicionarRemedio(Remedio remedio) {
        if (remedio == null) {
            throw new IllegalArgumentException("Remedio nao pode ser nulo.");
        }
        remedios.add(remedio);
    }

    /**
     * Remove um remédio da lista de remédios do cadastro.
     *
     * @param remedio remédio a remover
     * @return {@code true} se o remédio foi removido com sucesso
     */
    public boolean removerRemedio(Remedio remedio) {
        return remedios.remove(remedio);
    }
    
    /**
     * Define o id do cadastro.
     *
     * @param id id do cadastro;
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Define (ou substitui) o primeiro remédio da lista. Se a lista estiver
     * vazia, o remédio é adicionado; caso contrário, substitui o remédio na
     * posição 0.
     *
     * @param remedio remédio a definir; não pode ser nulo
     * @throws IllegalArgumentException se {@code remedio} for nulo
     */
    public void setRemedio(Remedio remedio) {
        if (remedio == null) {
            throw new IllegalArgumentException("Remedio nao pode ser nulo.");
        }
        if (remedios.isEmpty()) {
            remedios.add(remedio);
        } else {
            remedios.set(0, remedio);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.pessoa);
        hash = 89 * hash + Objects.hashCode(this.usuario);
        hash = 89 * hash + Objects.hashCode(this.remedios);
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
        final Cadastro other = (Cadastro) obj;

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
        StringBuilder sbRemedios = new StringBuilder();
        for (Remedio r : remedios) {
            sbRemedios.append(r.toString()).append("\n");
        }

        return """
           ========================================
                     DADOS DO CADASTRO
           ========================================
           
           %s
           
           %s
           
           Remedios cadastrados (%d):
           %s
           ========================================
           """.formatted(
                pessoa,
                usuario,
                remedios.size(),
                sbRemedios.toString()
        );
    }

}
