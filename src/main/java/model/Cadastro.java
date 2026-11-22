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
import jakarta.persistence.OrderBy;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Representa o cadastro completo de um usuário do sistema, agrupando seus dados
 * pessoais ({@link Pessoa}), suas credenciais ({@link Usuario}), a lista de
 * remédios em uso e a lista de agendas ({@link Agenda}), cada uma com suas
 * próprias tarefas.
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
    @OrderBy("id")
    private List<Remedio> remedios;

    // Cadastro 1 --- N Agenda 1 --- N Tarefa. Remover o cadastro remove as
    // agendas (e, em cascata, as tarefas de cada uma).
    // FetchMode.SELECT: o Hibernate nao deixa buscar duas listas (bags) EAGER no
    // mesmo JOIN (MultipleBagFetchException); "remedios" continua no JOIN e as
    // agendas vem em um SELECT separado, ainda carregadas junto com o cadastro.
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "cadastro_id")
    @Fetch(FetchMode.SELECT)
    @OrderBy("data")
    private List<Agenda> agendas;

    protected Cadastro() {
        this.remedios = new ArrayList<>();
        this.agendas = new ArrayList<>();
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
        this.agendas = new ArrayList<>();
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
        boolean removido = remedios.remove(remedio);
        if (removido) {
            // As agendas que usavam o remedio continuam existindo, so ficam sem
            // remedio (o banco tambem faz isso com ON DELETE SET NULL).
            for (Agenda agenda : agendas) {
                if (mesmoRemedio(agenda.getRemedio(), remedio)) {
                    agenda.setRemedio(null);
                }
            }
        }
        return removido;
    }

    private boolean mesmoRemedio(Remedio a, Remedio b) {
        if (a == null || b == null) {
            return false;
        }
        return a == b || (a.getId() != null && a.getId().equals(b.getId()));
    }

    /**
     * @return as agendas do cadastro, da data mais antiga para a mais recente
     * (visão somente leitura; para alterar use {@link #adicionarAgenda(Agenda)})
     */
    public List<Agenda> getAgendas() {
        return Collections.unmodifiableList(agendas);
    }

    /**
     * Adiciona uma agenda à lista de agendas do cadastro.
     *
     * @param agenda agenda a adicionar; não pode ser nula
     * @throws IllegalArgumentException se {@code agenda} for nula
     */
    public void adicionarAgenda(Agenda agenda) {
        if (agenda == null) {
            throw new IllegalArgumentException("Agenda nao pode ser nula.");
        }
        agendas.add(agenda);
    }

    /**
     * Remove uma agenda do cadastro. Suas tarefas são removidas junto.
     *
     * @param agenda agenda a remover
     * @return {@code true} se a agenda foi removida
     */
    public boolean removerAgenda(Agenda agenda) {
        return agendas.remove(agenda);
    }

    /**
     * Passa a ter as mesmas agendas de outro cadastro. Usado ao atualizar os
     * dados de um cadastro: o objeto novo é montado a partir de um DTO que não
     * traz agendas, e sem esta cópia o {@code orphanRemoval} as apagaria.
     *
     * @param outro cadastro de onde copiar as agendas
     */
    public void copiarAgendasDe(Cadastro outro) {
        this.agendas = new ArrayList<>(outro.agendas);
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

        StringBuilder sbAgendas = new StringBuilder();
        for (Agenda a : agendas) {
            sbAgendas.append("- ").append(a.getNome())
                    .append(" (").append(a.getTarefas().size()).append(" tarefa(s))\n");
        }

        return """
           ========================================
                     DADOS DO CADASTRO
           ========================================
           
           %s
           
           %s
           
           Remedios cadastrados (%d):
           %s
           Agendas cadastradas (%d):
           %s
           ========================================
           """.formatted(
                pessoa,
                usuario,
                remedios.size(),
                sbRemedios.toString(),
                agendas.size(),
                sbAgendas.toString()
        );
    }

}
