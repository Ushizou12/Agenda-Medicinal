/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exception;

/**
 * Exceção de negócio lançada quando uma regra do domínio da aplicação
 * (Usuario, Pessoa, Remedio, Tarefa, Agenda, Cadastro) é violada.
 *
 * Deve ser usada no lugar de exceções técnicas genéricas sempre que o
 * erro for proveniente de uma violação de regra de negócio, permitindo
 * que a camada de visão (main) trate os erros de forma padronizada.
 * @author mathe
 */

public class RegraException extends RuntimeException {

    /**
     * Cria a exceção com uma mensagem descritiva do erro de negócio.
     *
     * @param mensagem descrição do problema, exibida ao usuário final
     */
    public RegraException(String mensagem) {
        super(mensagem);
    }
    
    /**
     * Cria a exceção encapsulando a causa técnica original, preservando
     * o stacktrace original para depuração.
     *
     * @param mensagem descrição do problema
     * @param causa    exceção técnica original que motivou o erro
     */
    public RegraException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
