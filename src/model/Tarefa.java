/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa uma tarefa a ser cumprida, com prazo, categoria e prioridade (de 1
 * a 3). Implementa {@link Comparable} para permitir ordenação por prioridade.
 *
 * @author 2025001861
 */
public class Tarefa implements Comparable<Tarefa> {

    private String nome;
    private String descricao;
    private LocalDateTime dataLimite;
    private Categoria categoria;
    private int prioridade;
    private boolean concluida;

    /**
     * Cria uma nova tarefa, validando nome, data limite, categoria e
     * prioridade.
     *
     * @param nome nome da tarefa; não pode ser nulo ou vazio
     * @param descricao descrição da tarefa
     * @param dataLimite prazo final da tarefa; não pode ser nulo
     * @param categoria categoria da tarefa; não pode ser nula
     * @param prioridade prioridade da tarefa, entre 1 e 3
     * @param concluida indica se a tarefa já foi concluída
     * @throws IllegalArgumentException se algum dos valores for inválido
     */
    private Tarefa(Builder builder) {
        setNome(builder.nome);
        setDescricao(builder.descricao);
        setDataLimite(builder.dataLimite);
        setCategoria(builder.categoria);
        setPrioridade(builder.prioridade);
        this.concluida = builder.concluida;
    }

    /**
     * @return o nome da tarefa
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return a descrição da tarefa
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @return o prazo final da tarefa
     */
    public LocalDateTime getDataLimite() {
        return dataLimite;
    }

    /**
     * @return a categoria da tarefa
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * @return a prioridade da tarefa (1 a 3)
     */
    public int getPrioridade() {
        return prioridade;
    }

    /**
     * @return {@code true} se a tarefa já foi concluída
     */
    public boolean isConcluida() {
        return concluida;
    }

    /**
     * Define o nome da tarefa.
     *
     * @param nome nome da tarefa; não pode ser nulo ou vazio
     * @throws IllegalArgumentException se {@code nome} for nulo ou vazio
     */
    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome nao pode ser vazio.");
        }
        this.nome = nome;
    }

    /**
     * Define a descrição da tarefa.
     *
     * @param descricao descrição da tarefa; não pode ser nula
     * @throws IllegalArgumentException se {@code descricao} for nula
     */
    public void setDescricao(String descricao) {
        if (descricao == null) {
            throw new IllegalArgumentException("Descricao nao pode ser nula.");
        }
        this.descricao = descricao;
    }

    /**
     * Define o prazo final da tarefa.
     *
     * @param dataLimite prazo final; não pode ser nulo
     * @throws IllegalArgumentException se {@code dataLimite} for nulo
     */
    public void setDataLimite(LocalDateTime dataLimite) {
        if (dataLimite == null) {
            throw new IllegalArgumentException("Data limite nao pode ser nula.");
        }
        this.dataLimite = dataLimite;
    }

    /**
     * Define a categoria da tarefa.
     *
     * @param categoria categoria da tarefa; não pode ser nula
     * @throws IllegalArgumentException se {@code categoria} for nula
     */
    public void setCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria nao pode ser nula.");
        }
        this.categoria = categoria;
    }

    /**
     * Define a prioridade da tarefa.
     *
     * @param prioridade prioridade, deve estar entre 1 e 3
     * @throws IllegalArgumentException se {@code prioridade} estiver fora do
     * intervalo 1-3
     */
    public void setPrioridade(int prioridade) {
        if (prioridade < 1 || prioridade > 3) {
            throw new IllegalArgumentException("Prioridade deve estar entre 1 e 3.");
        }
        this.prioridade = prioridade;
    }

    /**
     * Define se a tarefa está concluída.
     *
     * @param concluida {@code true} se concluída
     */
    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    /**
     * Compara a data limite da tarefa com o momento atual, imprimindo no
     * console se o prazo já expirou ou não.
     */
    public void dataLimite() {
        LocalDateTime hoje = LocalDateTime.now();

        if (hoje.isAfter(dataLimite)) {
            System.out.println("O prazo expirou!");
        } else {
            System.out.println("Voce ainda este no prazo.");
        }
    }

    /**
     * Compara esta tarefa com outra com base na prioridade, em ordem
     * decrescente (prioridade mais alta primeiro).
     *
     * @param outra a outra tarefa a comparar
     * @return valor negativo, zero ou positivo conforme a prioridade
     */
    @Override
    public int compareTo(Tarefa outra) {
        return Integer.compare(outra.prioridade, this.prioridade);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.nome);
        hash = 47 * hash + Objects.hashCode(this.descricao);
        hash = 47 * hash + Objects.hashCode(this.dataLimite);
        hash = 47 * hash + Objects.hashCode(this.categoria);
        hash = 47 * hash + this.prioridade;
        hash = 47 * hash + (this.concluida ? 1 : 0);
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
        final Tarefa other = (Tarefa) obj;
        if (this.prioridade != other.prioridade) {
            return false;
        }
        if (this.concluida != other.concluida) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        if (!Objects.equals(this.dataLimite, other.dataLimite)) {
            return false;
        }
        return this.categoria == other.categoria;
    }

    @Override
    public String toString() {
        return """
            \n
            Nome           : %s
            Descricao      : %s
            Data Limite    : %s
            Categoria      : %s
            Prioridade     : %s
            Status:        : %s
           """.formatted(nome, descricao, dataLimite, categoria, prioridade, concluida ? "Concluído" : "Pendente");
    }

    public static class Builder {

        private String nome;
        private String descricao;
        private LocalDateTime dataLimite;
        private Categoria categoria;
        private int prioridade;
        private boolean concluida;

        public Builder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public Builder descricao(String descricao) {
            this.descricao = descricao;
            return this;
        }

        public Builder dataLimite(LocalDateTime dataLimite) {
            this.dataLimite = dataLimite;
            return this;
        }

        public Builder categoria(Categoria categoria) {
            this.categoria = categoria;
            return this;
        }

        public Builder prioridade(int prioridade) {
            this.prioridade = prioridade;
            return this;
        }

        public Builder concluida(boolean concluida) {
            this.concluida = concluida;
            return this;
        }

        public Tarefa build() {
            return new Tarefa(this);
        }
    }

    public static void main(String[] args) {

        Tarefa tt = new Tarefa.Builder()
                .nome("Tomar medicamento")
                .descricao("Tomar antibiótico após o almoço")
                .dataLimite(LocalDateTime.of(2026, 11, 4, 15, 30))
                .categoria(Categoria.ANTIBIOTICO)
                .prioridade(1)
                .concluida(true)
                .build();

        System.out.println(tt);
    }
}
