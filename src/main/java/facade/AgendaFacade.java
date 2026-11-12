package facade;

import dao.CadastroDAO;
import dao.TarefaDAOMemoria;
import dto.CadastroDTO;
import dto.RemedioDTO;
import dto.TarefaDTO;
import exception.RegraException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import model.Agenda;
import model.Cadastro;
import model.Pessoa;
import model.Remedio;
import model.Tarefa;
import model.Usuario;
/**
 *
 * @author mathe
 */

public class AgendaFacade {

    private final CadastroDAO cadastroDAO;
    private final TarefaDAOMemoria tarefaDAO;
    private Agenda agendaAtual; // TODO: trocar por AgendaDAO quando a agenda for persistida

    public AgendaFacade(CadastroDAO cadastroDAO, TarefaDAOMemoria tarefaDAO) {
        this.cadastroDAO = cadastroDAO;
        this.tarefaDAO = tarefaDAO;
    }

    // =====================  CADASTRO  =====================
    public void cadastrar(CadastroDTO dto) {
        if (dto.remedios().isEmpty()) {
            throw new RegraException("Informe ao menos um remedio.");
        }
        if (cadastroDAO.acharPorUsuario(dto.nomeUsuario()) != null) {
            throw new RegraException("Ja existe um usuario com esse nome.");
        }
        cadastroDAO.add(toCadastro(dto));
    }

    public Optional<CadastroDTO> buscarPorUsuario(String nomeUsuario) {
        return Optional.ofNullable(cadastroDAO.acharPorUsuario(nomeUsuario))
                .map(this::toCadastroDTO);
    }

    /** Texto formatado de todos os cadastros (inclui IMC e status dos remedios). */
    public List<String> relatorioCadastros() {
        return cadastroDAO.getDados().stream()
                .map(Cadastro::toString)
                .toList();
    }

    public Optional<String> relatorioCadastro(String nomeUsuario) {
        return Optional.ofNullable(cadastroDAO.acharPorUsuario(nomeUsuario))
                .map(Cadastro::toString);
    }

    public void atualizarCadastro(String nomeAntigo, CadastroDTO dto) {
        if (dto.remedios().isEmpty()) {
            throw new RegraException("Informe ao menos um remedio.");
        }
        boolean mudouNome = !nomeAntigo.equals(dto.nomeUsuario());
        if (mudouNome && cadastroDAO.acharPorUsuario(dto.nomeUsuario()) != null) {
            throw new RegraException("Ja existe um usuario com esse nome.");
        }
        if (!cadastroDAO.update(nomeAntigo, toCadastro(dto))) {
            throw new RegraException("Usuario nao encontrado.");
        }
    }

    // =====================  REMEDIOS  =====================
    public List<RemedioDTO> listarRemedios(String nomeUsuario) {
        return buscarOuFalhar(nomeUsuario).getRemedios().stream()
                .map(this::toRemedioDTO)
                .toList();
    }

    public void adicionarRemedio(String nomeUsuario, RemedioDTO dto) {
        Remedio novo = toRemedio(dto);
        alterar(buscarOuFalhar(nomeUsuario), c -> c.adicionarRemedio(novo));
    }

    /** @return o nome do remedio removido */
    public String removerRemedio(String nomeUsuario, int indice) {
        Cadastro cadastro = buscarOuFalhar(nomeUsuario);
        if (indice < 0 || indice >= cadastro.getRemedios().size()) {
            throw new RegraException("Remedio invalido.");
        }
        Remedio removido = cadastro.getRemedios().get(indice);
        alterar(cadastro, c -> c.removerRemedio(removido));
        return removido.getNome();
    }

    // =====================  TAREFAS / AGENDA  =====================
    /** Tarefas ordenadas por prioridade (Tarefa implementa Comparable). */
    public List<TarefaDTO> listarTarefas() {
        return tarefaDAO.getDados().stream()
                .sorted()
                .map(this::toTarefaDTO)
                .toList();
    }

    public boolean atualizarTarefa(String nomeAntigo, TarefaDTO dto) {
        Tarefa antiga = tarefaDAO.acharPorNome(nomeAntigo);
        Tarefa nova = toTarefa(dto);

        boolean atualizou = tarefaDAO.update(nomeAntigo, nova);

        // O DAO substitui a tarefa por um objeto novo; se a agenda atual apontava
        // para a antiga, religa para a nova (senao ela continuaria mostrando o titulo velho).
        if (atualizou && agendaAtual != null && antiga != null
                && antiga.equals(agendaAtual.getTarefa())) {
            agendaAtual.setTarefa(nova);
        }
        return atualizou;
    }

    /**
     * Cria a tarefa e a agenda vinculando o remedio escolhido.
     *
     * @return texto da agenda criada
     */
    public String criarAgenda(String nomeUsuario, int indiceRemedio, TarefaDTO tarefaDTO,
            String tituloAgenda, String descricaoAgenda, boolean concluida) {

        List<Remedio> remedios = buscarOuFalhar(nomeUsuario).getRemedios();
        if (indiceRemedio < 0 || indiceRemedio >= remedios.size()) {
            throw new RegraException("Remedio invalido.");
        }

        Tarefa tarefa = toTarefa(tarefaDTO);
        Agenda agenda = new Agenda.Builder(tarefaDTO.dataLimite(), tituloAgenda, descricaoAgenda)
                .remedio(remedios.get(indiceRemedio))
                .tarefa(tarefa)
                .concluida(concluida)
                .buildComTarefa();

        tarefaDAO.add(tarefa);   // so persiste depois que a agenda foi validada
        agendaAtual = agenda;
        return agenda.toString();
    }

    public Optional<String> mostrarAgendaAtual() {
        return Optional.ofNullable(agendaAtual).map(Agenda::toString);
    }

    // =====================  PRIVADOS  =====================
    private Cadastro buscarOuFalhar(String nomeUsuario) {
        Cadastro cadastro = cadastroDAO.acharPorUsuario(nomeUsuario);
        if (cadastro == null) {
            throw new RegraException("Usuario nao encontrado.");
        }
        return cadastro;
    }

    /**
     * Cadastro esta num HashSet e seu hashCode depende da lista de remedios.
     * Por isso: remove ANTES de mudar (hash ainda valido), muda, e adiciona de
     * novo. Com JDBC este cuidado deixa de ser necessario.
     */
    private void alterar(Cadastro cadastro, Consumer<Cadastro> mudanca) {
        cadastroDAO.remove(cadastro);
        mudanca.accept(cadastro);
        cadastroDAO.add(cadastro);
    }

    // ---- DTO -> entidade (usa os Builders) ----
    private Cadastro toCadastro(CadastroDTO dto) {
        Usuario usuario = new Usuario(dto.nomeUsuario(), dto.email(), dto.senha());
        Pessoa pessoa = new Pessoa(dto.nomePessoa(), dto.idade(), dto.altura(), dto.peso());
        List<Remedio> remedios = dto.remedios().stream().map(this::toRemedio).toList();

        Cadastro cadastro = new Cadastro(pessoa, usuario, remedios.get(0));
        remedios.stream().skip(1).forEach(cadastro::adicionarRemedio);
        return cadastro;
    }

    private Remedio toRemedio(RemedioDTO dto) {
        return new Remedio.Builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .qtdPilulasAtual(dto.qtdPilulasAtual())
                .dataInicioMedicacao(dto.dataInicioMedicacao())
                .preco(dto.preco())
                .pilulasPorDia(dto.pilulasPorDia())
                .build();
    }

    private Tarefa toTarefa(TarefaDTO dto) {
        return new Tarefa.Builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .dataLimite(dto.dataLimite())
                .categoria(dto.categoria())
                .prioridade(dto.prioridade())
                .concluida(dto.concluida())
                .build();
    }

    // ---- entidade -> DTO ----
    private CadastroDTO toCadastroDTO(Cadastro c) {
        return new CadastroDTO(
                c.getUsuario().getNome(),
                c.getUsuario().getEmail(),
                c.getUsuario().getSenha(),
                c.getPessoa().getNome(),
                c.getPessoa().getIdade(),
                c.getPessoa().getAltura(),
                c.getPessoa().getPeso(),
                c.getRemedios().stream().map(this::toRemedioDTO).toList());
    }

    private RemedioDTO toRemedioDTO(Remedio r) {
        return new RemedioDTO(r.getNome(), r.getDescricao(), r.getQtdPilulasAtual(),
                r.getDataInicioMedicacao(), r.getPreco(), r.getPilulasPorDia());
    }

    private TarefaDTO toTarefaDTO(Tarefa t) {
        return new TarefaDTO(t.getNome(), t.getDescricao(), t.getDataLimite(),
                t.getCategoria(), t.getPrioridade(), t.isConcluida());
    }
}
