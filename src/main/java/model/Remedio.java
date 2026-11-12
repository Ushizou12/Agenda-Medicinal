package model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa um remédio em uso, controlando estoque de pílulas, preço e o
 * período de tratamento (calculado a partir da data de início e do consumo
 * diário).
 *
 * @author Ushizou
 */
public class Remedio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;
    private int qtdPilulasAtual;
    private LocalDate dataInicioMedicacao;
    private LocalDate dataFinalMedicacao;
    private double preco;
    private int pilulasPorDia;

    protected Remedio() { }

    /**
     * Cria um novo remédio, validando todos os campos e calculando
     * automaticamente a data final da medicação.
     *
     * @param nome nome do remédio; não pode ser nulo ou vazio
     * @param descricao descrição do remédio
     * @param qtdPilulas quantidade inicial de pílulas; não pode ser negativa
     * @param dataInicioMedicacao data de início do tratamento; não pode ser
     * nula
     * @param preco preço do remédio; não pode ser negativo
     * @param pilulasPorDia quantidade de pílulas consumidas por dia; deve ser
     * maior que zero
     * @throws IllegalArgumentException se algum dos valores for inválido
     */
    private Remedio(Builder builder) {
        setNome(builder.nome);
        setDescricao(builder.descricao);
        setPilulasPorDia(builder.pilulasPorDia);
        setQtdPilulasAtual(builder.qtdPilulasAtual);
        setDataInicioMedicacao(builder.dataInicioMedicacao);
        setPreco(builder.preco);
        calcularDataFinalMedicacao();
    }

    /**
     * Calcula quantos dias de tratamento restam com base no estoque atual.
     *
     * @param pilulasPorDia quantidade de pílulas consumidas por dia; deve ser
     * maior que zero
     * @return número inteiro de dias restantes (arredondado para baixo)
     * @throws IllegalArgumentException se {@code pilulasPorDia} for menor ou
     * igual a zero
     */
    public int calcularDiasRestantes(int pilulasPorDia) {
        if (pilulasPorDia <= 0) {
            throw new IllegalArgumentException("Pilulas por dia deve ser maior que zero.");
        }
        return this.qtdPilulasAtual / pilulasPorDia;
    }

    /**
     * Consome uma dose diária do remédio, reduzindo o estoque atual. Não faz
     * nada caso o estoque já esteja zerado.
     */
    public void tomarDose() {
        if (qtdPilulasAtual > 0) {
            qtdPilulasAtual -= pilulasPorDia;
        }
    }
    
    /**
     * @return o id do remédio
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Define o id do remedio.
     *
     * @param id id do remedio;
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * @return o nome do remédio
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do remédio.
     *
     * @param nome nome do remédio; não pode ser nulo ou vazio
     * @throws IllegalArgumentException se {@code nome} for nulo ou vazio
     */
    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome nao pode ser vazio.");
        }
        this.nome = nome;
    }

    /**
     * @return a descrição do remédio
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define a descrição do remédio.
     *
     * @param descricao descrição do remédio; não pode ser nula
     * @throws IllegalArgumentException se {@code descricao} for nula
     */
    public void setDescricao(String descricao) {
        if (descricao == null) {
            throw new IllegalArgumentException("Descricao nao pode ser nula.");
        }
        this.descricao = descricao;
    }

    /**
     * @return a quantidade atual de pílulas em estoque
     */
    public int getQtdPilulasAtual() {
        return qtdPilulasAtual;
    }

    /**
     * Define a quantidade atual de pílulas em estoque.
     *
     * @param qtdPilulasAtual quantidade de pílulas; não pode ser negativa
     * @throws IllegalArgumentException se {@code qtdPilulasAtual} for negativa
     */
    public void setQtdPilulasAtual(int qtdPilulasAtual) {
        if (qtdPilulasAtual < 0) {
            throw new IllegalArgumentException("Quantidade de pilulas nao pode ser negativa.");
        }
        this.qtdPilulasAtual = qtdPilulasAtual;
    }

    /**
     * @return a data de início da medicação
     */
    public LocalDate getDataInicioMedicacao() {
        return dataInicioMedicacao;
    }

    /**
     * Define a data de início da medicação.
     *
     * @param dataInicioMedicacao data de início; não pode ser nula
     * @throws IllegalArgumentException se {@code dataInicioMedicacao} for nula
     */
    public void setDataInicioMedicacao(LocalDate dataInicioMedicacao) {
        if (dataInicioMedicacao == null) {
            throw new IllegalArgumentException("Data de inicio nao pode ser nula.");
        }
        this.dataInicioMedicacao = dataInicioMedicacao;
    }

    /**
     * @return a data prevista para o fim da medicação
     */
    public LocalDate getDataFinalMedicacao() {
        return dataFinalMedicacao;
    }

    /**
     * Calcula e atualiza a data prevista para o fim da medicação, somando à
     * data de início os dias restantes de tratamento.
     */
    public void calcularDataFinalMedicacao() {
        this.dataFinalMedicacao = dataInicioMedicacao.plusDays(calcularDiasRestantes(pilulasPorDia));
    }

    /**
     * Verifica se a medicação ainda está dentro do prazo previsto.
     *
     * @return {@code "Prazo expirado"} se a data atual já passou da data final,
     * ou {@code "Dentro do prazo"} caso contrário
     */
    public String getPrazoMedicacao() {
        LocalDate hoje = LocalDate.now();

        if (hoje.isAfter(dataFinalMedicacao)) {
            return "Prazo expirado";
        }

        return "Dentro do prazo";
    }

    /**
     * Retorna o status do estoque de pílulas: se acabou, se está acabando (5 ou
     * menos) ou se está em quantidade normal.
     *
     * @return uma mensagem descrevendo o status atual do estoque
     */
    public String getStatusMedicacao() {
        if (qtdPilulasAtual <= 0) {
            return "Medicao acabou!";
        }

        if (qtdPilulasAtual <= 5) {
            return "Atencao: medicamento acabando!";
        }

        return "Estoque normal";
    }

    /**
     * @return o preço do remédio
     */
    public double getPreco() {
        return preco;
    }

    /**
     * Define o preço do remédio.
     *
     * @param preco preço do remédio; não pode ser negativo
     * @throws IllegalArgumentException se {@code preco} for negativo
     */
    public void setPreco(double preco) {
        if (preco < 0) {
            throw new IllegalArgumentException("Preco nao pode ser negativo.");
        }
        this.preco = preco;
    }

    /**
     * @return a quantidade de pílulas consumidas por dia
     */
    public int getPilulasPorDia() {
        return pilulasPorDia;
    }

    /**
     * Define a quantidade de pílulas consumidas por dia.
     *
     * @param pilulasPorDia quantidade diária; deve ser maior que zero
     * @throws IllegalArgumentException se {@code pilulasPorDia} for menor ou
     * igual a zero
     */
    public void setPilulasPorDia(int pilulasPorDia) {
        if (pilulasPorDia <= 0) {
            throw new IllegalArgumentException("Pilulas por dia deve ser maior que zero.");
        }
        this.pilulasPorDia = pilulasPorDia;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + Objects.hashCode(this.nome);
        hash = 13 * hash + Objects.hashCode(this.descricao);
        hash = 13 * hash + this.qtdPilulasAtual;
        hash = 13 * hash + Objects.hashCode(this.dataInicioMedicacao);
        hash = 13 * hash + Objects.hashCode(this.dataFinalMedicacao);
        hash = 13 * hash + (int) (Double.doubleToLongBits(this.preco) ^ (Double.doubleToLongBits(this.preco) >>> 32));
        hash = 13 * hash + this.pilulasPorDia;
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
        final Remedio other = (Remedio) obj;
        if (this.qtdPilulasAtual != other.qtdPilulasAtual) {
            return false;
        }
        if (Double.doubleToLongBits(this.preco) != Double.doubleToLongBits(other.preco)) {
            return false;
        }
        if (this.pilulasPorDia != other.pilulasPorDia) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        if (!Objects.equals(this.dataInicioMedicacao, other.dataInicioMedicacao)) {
            return false;
        }
        return Objects.equals(this.dataFinalMedicacao, other.dataFinalMedicacao);
    }

    @Override
    public String toString() {
        return """
           Remedio:
           Nome              : %s
           Descricao         : %s
           Quantidade Atual  : %d pilulas
           Inicio da Medicacao: %s
           Fim da Medicacao  : %s
           Preco             : R$ %.2f
           Pilulas por Dia   : %d
           Prazo             : %s
           Status            : %s
           """.formatted(
                nome,
                descricao,
                qtdPilulasAtual,
                dataInicioMedicacao,
                dataFinalMedicacao,
                preco,
                pilulasPorDia,
                getPrazoMedicacao(),
                getStatusMedicacao()
        );
    }

    public static class Builder {

        private String nome;
        private String descricao;
        private int qtdPilulasAtual;
        private LocalDate dataInicioMedicacao;
        private double preco;
        private int pilulasPorDia;

        public Builder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public Builder descricao(String descricao) {
            this.descricao = descricao;
            return this;
        }

        public Builder qtdPilulasAtual(int qtdPilulasAtual) {
            this.qtdPilulasAtual = qtdPilulasAtual;
            return this;
        }

        public Builder dataInicioMedicacao(LocalDate dataInicioMedicacao) {
            this.dataInicioMedicacao = dataInicioMedicacao;
            return this;
        }

        public Builder preco(double preco) {
            this.preco = preco;
            return this;
        }

        public Builder pilulasPorDia(int pilulasPorDia) {
            this.pilulasPorDia = pilulasPorDia;
            return this;
        }

        public Remedio build() {
            return new Remedio(this);
        }
    }
}
