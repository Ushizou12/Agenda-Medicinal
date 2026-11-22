package model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Representa um item de agenda, que pode estar vinculado a um remédio e possui
 * uma lista de tarefas (zero, uma ou várias).
 *
 * <p>As tarefas pertencem à agenda: ao remover a agenda, todas as suas tarefas
 * são removidas junto ({@code cascade = ALL}); ao tirar uma tarefa da lista, ela
 * é apagada do banco ({@code orphanRemoval = true}).</p>
 *
 * @author Ushizou
 */

@Entity
public class Agenda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime data;
    private String nome;
    private String descricao;
    
    // Se o remedio for apagado, a agenda continua existindo, so que sem remedio
    // (o banco coloca remedio_id = NULL).
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Remedio remedio;
    
    // Lado inverso (mappedBy): a chave estrangeira fica em Tarefa.agenda.
    // ALL: salvar/atualizar/REMOVER a agenda faz o mesmo com as tarefas.
    // orphanRemoval: tarefa retirada da lista e apagada do banco.
    // FetchMode.SELECT: evita MultipleBagFetchException (Cadastro ja tem outras
    // listas EAGER); a lista continua sendo carregada junto com a agenda.
    @OneToMany(mappedBy = "agenda", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @OrderBy("id")
    private List<Tarefa> tarefas = new ArrayList<>();
    
    private boolean concluida;
    
    protected Agenda() { }
    
    /**
     * Cria um item de agenda a partir do {@link Builder}. As tarefas do builder
     * (se houver) são vinculadas à nova agenda.
     *
     * @param builder dados da agenda; data e nome não podem ser nulos/vazios
     * @throws IllegalArgumentException se {@code data}, {@code nome} ou
     * {@code descricao} forem inválidos
     */
    public Agenda(Builder builder) {
        setData(builder.data);
        setNome(builder.nome);
        setDescricao(builder.descricao);
        this.remedio = builder.remedio;
        this.concluida = builder.concluida;
        builder.tarefas.forEach(this::adicionarTarefa);
    }
    
    /**
     * @return o id da Agenda
     */
    public Long getId() {
        return id;
    }

    /**
     * @return a data e hora do compromisso
     */
    public LocalDateTime getData() {
        return data;
    }
    
    /**
     * Define o id da agenda.
     *
     * @param id id da agenda;
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Define a data e hora do compromisso.
     *
     * @param data data e hora; não pode ser nula
     * @throws IllegalArgumentException se {@code data} for nula
     */
    public void setData(LocalDateTime data) {
        if (data == null) {
            throw new IllegalArgumentException("Data nao pode ser nula.");
        }
        this.data = data;
    }

    /**
     * @return o nome do compromisso
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do compromisso.
     *
     * @param nome nome do compromisso; não pode ser nulo ou vazio
     * @throws IllegalArgumentException se {@code nome} for nulo ou vazio
     */
    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome nao pode ser vazio.");
        }
        this.nome = nome;
    }

    /**
     * @return a descrição do compromisso
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define a descrição do compromisso.
     *
     * @param descricao descrição do compromisso
     * @throws IllegalArgumentException se {@code nome} for nulo ou vazio
     */
    public void setDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descricao nao pode ser vazio.");
        }
        this.descricao = descricao;
    }

    /**
     * @return as tarefas desta agenda (visão somente leitura; para alterar use
     * {@link #adicionarTarefa(Tarefa)} e {@link #removerTarefaPorId(Long)})
     */
    public List<Tarefa> getTarefas() {
        return Collections.unmodifiableList(tarefas);
    }

    /**
     * Adiciona uma tarefa a esta agenda, tornando-a dona da tarefa.
     *
     * @param tarefa tarefa a adicionar; não pode ser nula
     * @throws IllegalArgumentException se {@code tarefa} for nula
     */
    public void adicionarTarefa(Tarefa tarefa) {
        if (tarefa == null) {
            throw new IllegalArgumentException("Tarefa nao pode ser nula.");
        }
        tarefa.setAgenda(this);
        tarefas.add(tarefa);
    }

    /**
     * Remove desta agenda a tarefa de id informado. Como a lista usa
     * {@code orphanRemoval}, a tarefa é apagada do banco quando a agenda for
     * salva. A busca é feita pelo id (e não por {@code equals}) porque duas
     * tarefas diferentes podem ter exatamente os mesmos dados.
     *
     * @param id id da tarefa a remover
     * @return {@code true} se a tarefa estava nesta agenda e foi removida
     */
    public boolean removerTarefaPorId(Long id) {
        if (id == null) {
            return false;
        }
        Iterator<Tarefa> it = tarefas.iterator();
        while (it.hasNext()) {
            if (id.equals(it.next().getId())) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * @return {@code true} se o item de agenda já foi concluído
     */
    public boolean isConcluida() {
        return concluida;
    }

    /**
     * Define se o item de agenda está concluído.
     *
     * @param concluida {@code true} se concluído
     */
    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    /**
     * @return o remédio vinculado a este item de agenda, ou {@code null} se não
     * houver
     */
    public Remedio getRemedio() {
        return remedio;
    }

    /**
     * Define o remédio vinculado a este item de agenda.
     *
     * @param remedio remédio a vincular (pode ser {@code null})
     */
    public void setRemedio(Remedio remedio) {
        this.remedio = remedio;
    }

    /**
     * Consulta o status de estoque do remédio vinculado a este item de agenda.
     *
     * @return o status do remédio, ou uma mensagem indicando que não há remédio
     * vinculado
     */
    public String getStatusRemedio() {
        if (remedio == null) {
            return "Nenhum remédio vinculado";
        }
        return remedio.getStatusMedicacao();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.data);
        hash = 89 * hash + Objects.hashCode(this.nome);
        hash = 89 * hash + Objects.hashCode(this.descricao);
        hash = 89 * hash + (this.concluida ? 1 : 0);
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
        final Agenda other = (Agenda) obj;
        if (this.concluida != other.concluida) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        return Objects.equals(this.data, other.data);
    }

    @Override
    public String toString() {
        return """
           ========================================
                         ITEM DA AGENDA
           ========================================
           Compromisso       : %s
           Data e Hora       : %s
           Descricao         : %s
           Status            : %s
           Remedio Vinculado : %s
           Tarefas           : %s
           ========================================
           """.formatted(
                nome,
                data,
                descricao,
                concluida ? "Concluído" : "Pendente",
                remedio != null ? remedio.getNome() + " - " + remedio.getStatusMedicacao() : "Nenhum",
                tarefas.isEmpty() ? "Nenhuma"
                        : tarefas.stream().map(Tarefa::getNome).collect(Collectors.joining(", "))
        );

    }

    public static class Builder {

        private LocalDateTime data;
        private String nome;
        private String descricao;
        private Remedio remedio;
        private final List<Tarefa> tarefas = new ArrayList<>();
        private boolean concluida;

        public Builder(LocalDateTime data, String nome, String descricao) {
            this.data = data;
            this.nome = nome;
            this.descricao = descricao;
        }

        public Builder remedio(Remedio remedio) {
            this.remedio = remedio;
            return this;
        }

        /** Acrescenta uma tarefa; pode ser chamado varias vezes. */
        public Builder tarefa(Tarefa tarefa) {
            this.tarefas.add(tarefa);
            return this;
        }

        public Builder concluida(boolean concluida) {
            this.concluida = concluida;
            return this;
        }

        public Agenda build() {
            return new Agenda(this);
        }
    }

}
