package model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa um item de agenda, que pode estar vinculado a um remédio e,
 * opcionalmente, a uma tarefa.
 *
 * @author Ushizou
 */
public class Agenda {

    private LocalDateTime data;
    private String nome;
    private String descricao;
    private Remedio remedio;
    private Tarefa tarefa;
    private boolean concluida;

    // Construtor focado em remedio.
    /**
     * Cria um item de agenda focado em um remédio.
     *
     * @param data data e hora do compromisso; não pode ser nula
     * @param nome nome do compromisso; não pode ser nulo ou vazio
     * @param descricao descrição do compromisso
     * @param remedio remédio vinculado (pode ser {@code null})
     * @param concluida indica se o compromisso já foi concluído
     * @throws IllegalArgumentException se {@code data} ou {@code nome} forem
     * inválidos
     */
    public Agenda(Builder builder) {
        setData(builder.data);
        setNome(builder.nome);
        setDescricao(builder.descricao);
        this.remedio = builder.remedio;
        this.concluida = builder.concluida;
    }

    /**
     * Cria um item de agenda completo, vinculando remédio e tarefa.
     *
     * @param data data e hora do compromisso; não pode ser nula
     * @param nome nome do compromisso; não pode ser nulo ou vazio
     * @param descricao descrição do compromisso
     * @param remedio remédio vinculado (pode ser {@code null})
     * @param tarefa tarefa vinculada (pode ser {@code null})
     * @param concluida indica se o compromisso já foi concluído
     * @throws IllegalArgumentException se {@code data} ou {@code nome} forem
     * inválidos
     */
    public Agenda(Builder builder, boolean incluirTarefa) {
        this(builder);
        if (incluirTarefa) {
            this.tarefa = builder.tarefa;
        }
    }

    /**
     * @return a data e hora do compromisso
     */
    public LocalDateTime getData() {
        return data;
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
     * @return a tarefa vinculada a este item de agenda, ou {@code null} se não
     * houver
     */
    public Tarefa getTarefa() {
        return tarefa;
    }

    /**
     * Define a tarefa vinculada a este item de agenda.
     *
     * @param tarefa tarefa a vincular (pode ser {@code null})
     */
    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
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
        hash = 89 * hash + Objects.hashCode(this.tarefa);
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
        if (!Objects.equals(this.data, other.data)) {
            return false;
        }
        return Objects.equals(this.tarefa, other.tarefa);
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
           Tarefa Vinculada  : %s
           ========================================
           """.formatted(
                nome,
                data,
                descricao,
                concluida ? "Concluído" : "Pendente",
                remedio != null ? remedio.getNome() + " - " + remedio.getStatusMedicacao() : "Nenhum",
                tarefa != null ? tarefa.getNome() : "Nenhuma"
        );

    }

    public static class Builder {

        private LocalDateTime data;
        private String nome;
        private String descricao;
        private Remedio remedio;
        private Tarefa tarefa;
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

        public Builder tarefa(Tarefa tarefa) {
            this.tarefa = tarefa;
            return this;
        }

        public Builder concluida(boolean concluida) {
            this.concluida = concluida;
            return this;
        }

        public Agenda build() {
            return new Agenda(this);
        }

        public Agenda buildComTarefa() {
            return new Agenda(this, true);
        }
    }

}
