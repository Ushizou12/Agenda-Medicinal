package dao;

import exception.RegraException;

/**
 * Contrato genérico de operações de persistência (CRUD básico) para os DAOs da
 * aplicação.
 *
 * @param <C> tipo da entidade manipulada por este DAO
 * @author Ushizou
 */
public interface OperacoesDAO<C> {

    /**
     * Adiciona um objeto à base de dados.
     *
     * @param o objeto a ser adicionado; não pode ser nulo
     * @return {@code true} se o objeto foi adicionado com sucesso
     * @throws RegraException se {@code o} for nulo ou ocorrer falha ao
     * adicionar
     */
    public boolean add(C o);

    /**
     * Remove um objeto da base de dados.
     *
     * @param o objeto a ser removido; não pode ser nulo
     * @return {@code true} se o objeto foi removido com sucesso
     * @throws RegraException se {@code o} for nulo ou ocorrer falha ao remover
     */
    public boolean remove(C o);

    /**
     * Procura, na base de dados, um objeto equivalente ao informado.
     *
     * @param o objeto de referência para a busca (via {@code equals})
     * @return o objeto encontrado, ou {@code null} caso não exista
     * @throws RegraException se {@code o} for nulo
     */
    public C find(C o);

    /**
     * Atualiza um objeto existente na base de dados.
     *
     * @param o objeto com os novos dados a serem utilizados na atualização; não
     * pode ser nulo
     * @return {@code true} se o objeto existente foi encontrado e atualizado,
     * {@code false} caso o objeto não seja encontrado
     * @throws RegraException se {@code o} for nulo ou ocorrer alguma falha
     * durante a atualização
     */
    boolean update(C o);
}
