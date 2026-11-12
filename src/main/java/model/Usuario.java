package model;

import java.util.Objects;

/**
 * Representa o usuário do sistema, responsável pelo login e identificação
 * da pessoa que utiliza a aplicação. Garante, por meio de validação no
 * construtor e nos setters, que nome, email e senha sempre estejam em
 * um estado consistente.
 * @author Ushizou
 */
public class Usuario {

    private String nome;
    private String email;
    private String senha;

    /**
     * Cria um novo usuário já validando nome, email e senha.
     *
     * @param nome  nome do usuário; não pode ser nulo ou vazio
     * @param email email do usuário; deve conter '@' e '.'
     * @param senha senha do usuário; deve ter no mínimo 6 caracteres
     * @throws IllegalArgumentException se algum dos valores for inválido
     */
    public Usuario(String nome, String email, String senha) {
        setNome(nome);
        setEmail(email);
        setSenha(senha);
    }

    /**
     * Define o nome do usuário.
     *
     * @param nome nome do usuário; não pode ser nulo ou vazio
     * @throws IllegalArgumentException se {@code nome} for nulo ou vazio
     */
    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome nao pode ser vazio.");
        }
        this.nome = nome;
    }

    /**
     * Define o email do usuário, validando seu formato.
     *
     * @param email email a ser definido; deve conter '@' e '.'
     * @throws IllegalArgumentException se o email for inválido
     */
    public void setEmail(String email) {
        if (!validarEmail(email)) {
            throw new IllegalArgumentException("Email invalido");
        }
        this.email = email;
    }

    /**
     * Define a senha do usuário, validando seu tamanho mínimo.
     *
     * @param senha senha a ser definida; deve ter no mínimo 6 caracteres
     * @throws IllegalArgumentException se a senha for inválida
     */
    public void setSenha(String senha) {
        if (!validarSenha(senha)) {
            throw new IllegalArgumentException("Senha invalida");
        }
        this.senha = senha;
    }

    /**
     * @return o nome do usuário
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return o email do usuário
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return a senha do usuário
     */
    public String getSenha() {
        return senha;
    }
    
    /**
     * Verifica se o email informado contém '@' e '.'.
     *
     * @param email valor a ser validado
     * @return {@code true} se o email for válido
     */
    public boolean validarEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
    
    /**
     * Verifica se a senha informada tem no mínimo 6 caracteres.
     *
     * @param senha valor a ser validado
     * @return {@code true} se a senha for válida
     */
    public boolean validarSenha(String senha) {
        return senha != null && senha.length() >= 6;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.nome);
        hash = 29 * hash + Objects.hashCode(this.email);
        hash = 29 * hash + Objects.hashCode(this.senha);
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
        final Usuario other = (Usuario) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        return Objects.equals(this.senha, other.senha);
    }

    @Override
    public String toString() {
        return """
           Usuario:
            Nome : %s
            Email: %s
            Senha: %s
           """.formatted(nome, email, senha);
    }

}
