package model;

import java.util.Objects;

/**
 * Representa os dados físicos de uma pessoa (nome, idade, altura e peso),
 * usados para o cálculo do IMC. Garante, via construtor e setters, que
 * nenhum valor inconsistente (negativo ou nulo) seja armazenado.
 * @author Ushizou
 */
public class Pessoa {

    private String nome;
    private int idade;
    private double altura;
    private double peso;

    /**
     * Cria uma nova pessoa já validando todos os campos.
     *
     * @param nome   nome da pessoa; não pode ser nulo ou vazio
     * @param idade  idade em anos; não pode ser negativa
     * @param altura altura em metros; deve ser maior que zero
     * @param peso   peso em quilos; não pode ser negativo
     * @throws IllegalArgumentException se algum dos valores for inválido
     */
    public Pessoa(String nome, int idade, double altura, double peso) {
        setNome(nome);
        setIdade(idade);
        setAltura(altura);
        setPeso(peso);
    }

    /**
     * Define o nome da pessoa.
     *
     * @param nome nome da pessoa; não pode ser nulo ou vazio
     * @throws IllegalArgumentException se {@code nome} for nulo ou vazio
     */
    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome nao pode ser vazio.");
        }
        this.nome = nome;
    }

    /**
     * Define a idade da pessoa.
     *
     * @param idade idade em anos; não pode ser negativa
     * @throws IllegalArgumentException se {@code idade} for negativa
     */
    public void setIdade(int idade) {
        if (idade < 0) {
            throw new IllegalArgumentException("Idade nao pode ser negativa.");
        }
        this.idade = idade;
    }

    /**
     * Define o peso da pessoa.
     *
     * @param peso peso em quilos; não pode ser negativo
     * @throws IllegalArgumentException se {@code peso} for negativo
     */
    public void setPeso(double peso) {
        if (peso < 0) {
            throw new IllegalArgumentException("Peso nao pode ser negativo.");
        }
        this.peso = peso;
    }

    /**
     * Define a altura da pessoa.
     *
     * @param altura altura em metros; deve ser maior que zero
     * @throws IllegalArgumentException se {@code altura} for menor ou igual a zero
     */
    public void setAltura(double altura) {
        if (altura <= 0) {
            throw new IllegalArgumentException("Altura deve ser maior que zero.");
        }
        this.altura = altura;
    }

    /**
     * @return o nome da pessoa
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return a idade da pessoa, em anos
     */
    public int getIdade() {
        return idade;
    }

    /**
     * @return a altura da pessoa, em metros
     */
    public double getAltura() {
        return altura;
    }

    /**
     * @return o peso da pessoa, em quilos
     */
    public double getPeso() {
        return peso;
    }
    
    /**
     * Calcula o IMC (Índice de Massa Corporal) da pessoa, usando altura e peso
     * atuais.
     *
     * @return o IMC calculado
     * @throws ArithmeticException se a altura for zero
     */
    public double calcularIMC() {
        if (altura == 0) {
            throw new ArithmeticException("Altura não pode ser zero");
        }
        return peso / (altura * altura);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.nome);
        hash = 41 * hash + this.idade;
        hash = 41 * hash + (int) (Double.doubleToLongBits(this.altura) ^ (Double.doubleToLongBits(this.altura) >>> 32));
        hash = 41 * hash + (int) (Double.doubleToLongBits(this.peso) ^ (Double.doubleToLongBits(this.peso) >>> 32));
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
        final Pessoa other = (Pessoa) obj;
        if (this.idade != other.idade) {
            return false;
        }
        if (Double.doubleToLongBits(this.altura) != Double.doubleToLongBits(other.altura)) {
            return false;
        }
        if (Double.doubleToLongBits(this.peso) != Double.doubleToLongBits(other.peso)) {
            return false;
        }
        return Objects.equals(this.nome, other.nome);
    }

    @Override
    public String toString() {
        return """
           Pessoa:
            Nome: %s
            Idade: %d anos
            Altura: %.2f m
            Peso: %.2f kg
            IMC: %.2f 
           """.formatted(nome, idade, altura, peso, calcularIMC());
    }

}
