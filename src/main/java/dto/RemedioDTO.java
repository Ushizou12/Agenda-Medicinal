package dto;

import java.time.LocalDate;

/**
 * Dados de um remédio trocados entre a view e a facade.
 *
 * <p>O {@code id} é {@code null} para um remédio ainda não salvo. Ele é o que
 * permite à facade atualizar o remédio existente em vez de recriá-lo (e assim
 * não perder o vínculo das agendas).</p>
 *
 * @param id id do remédio no banco ({@code null} se for novo)
 */
public record RemedioDTO(
        Long id,
        String nome,
        String descricao,
        int qtdPilulasAtual,
        LocalDate dataInicioMedicacao,
        double preco,
        int pilulasPorDia) {

    /**
     * Cria uma cópia deste remédio com outro id. Usado quando o usuário edita
     * um remédio: os dados novos assumem o id do remédio original.
     *
     * @param novoId id a ser usado na cópia
     * @return um novo {@code RemedioDTO} com os mesmos dados e o id informado
     */
    public RemedioDTO comId(Long novoId) {
        return new RemedioDTO(novoId, nome, descricao, qtdPilulasAtual,
                dataInicioMedicacao, preco, pilulasPorDia);
    }
}
