package facade;

import dao.AgendaDAO;
import dao.CadastroDAO;
import dao.TarefaDAO;
import dto.AgendaDTO;
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
 * Fachada do sistema: a view (comandos) so conversa com esta classe, que por
 * sua vez usa os DAOs e converte entre DTOs e entidades.
 *
 * @author mathe
 */
public class AgendaFacade {

    private final CadastroDAO cadastroDAO;
    private final TarefaDAO tarefaDAO;
    private final AgendaDAO agendaDAO;

    public AgendaFacade(CadastroDAO cadastroDAO, TarefaDAO tarefaDAO, AgendaDAO agendaDAO) {
        this.cadastroDAO = cadastroDAO;
        this.tarefaDAO = tarefaDAO;
        this.agendaDAO = agendaDAO;
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

    /**
     * Remove um remedio do usuario. As agendas que apontavam para ele nao sao
     * apagadas: o banco apenas tira o vinculo (remedio_id = NULL).
     *
     * @return o nome do remedio removido
     */
    public String removerRemedio(String nomeUsuario, int indice) {
        Cadastro cadastro = buscarOuFalhar(nomeUsuario);
        if (indice < 0 || indice >= cadastro.getRemedios().size()) {
            throw new RegraException("Remedio invalido.");
        }
        Remedio removido = cadastro.getRemedios().get(indice);
        alterar(cadastro, c -> c.removerRemedio(removido));
        return removido.getNome();
    }

    // =====================  TAREFAS  =====================
    /** Tarefas ordenadas por prioridade (Tarefa implementa Comparable). */
    public List<TarefaDTO> listarTarefas() {
        return tarefaDAO.getDados().stream()
                .sorted()
                .map(this::toTarefaDTO)
                .toList();
    }

    /**
     * Atualiza a tarefa de id informado. O id nao muda, entao as agendas que
     * apontam para a tarefa continuam apontando para ela.
     *
     * @return {@code true} se a tarefa existia e foi atualizada
     */
    public boolean atualizarTarefa(Long id, TarefaDTO dto) {
        return tarefaDAO.update(id, toTarefa(dto));
    }

    // =====================  AGENDA  =====================
    /**
     * Cria a agenda (e a tarefa vinculada) numa unica gravacao.
     *
     * @param nomeUsuario dono do remedio; so e usado se {@code dto.remedio()}
     * for informado (pode ser {@code null} numa agenda sem remedio)
     * @param dto dados da agenda; {@code dto.remedio()} so precisa do id
     * @return a agenda criada, ja com o id gerado
     */
    public AgendaDTO criarAgenda(String nomeUsuario, AgendaDTO dto) {
        Remedio remedio = null;
        if (dto.remedio() != null) {
            remedio = buscarRemedio(nomeUsuario, dto.remedio().id());
        }
        Tarefa tarefa = (dto.tarefa() != null) ? toTarefa(dto.tarefa()) : null;

        Agenda agenda = new Agenda.Builder(dto.data(), dto.nome(), dto.descricao())
                .remedio(remedio)
                .tarefa(tarefa)
                .concluida(dto.concluida())
                .buildComTarefa();

        agendaDAO.add(agenda);   // o cascade PERSIST salva a tarefa junto
        return toAgendaDTO(agenda);
    }

    /** Todas as agendas, da data mais antiga para a mais recente. */
    public List<AgendaDTO> listarAgendas() {
        return agendaDAO.listarPorData().stream()
                .map(this::toAgendaDTO)
                .toList();
    }

    /** Remove a agenda. A tarefa vinculada (se houver) e mantida. */
    public void removerAgenda(Long id) {
        if (!agendaDAO.removerPorId(id)) {
            throw new RegraException("Agenda nao encontrada.");
        }
    }

    // =====================  PRIVADOS  =====================
    private Cadastro buscarOuFalhar(String nomeUsuario) {
        Cadastro cadastro = cadastroDAO.acharPorUsuario(nomeUsuario);
        if (cadastro == null) {
            throw new RegraException("Usuario nao encontrado.");
        }
        return cadastro;
    }

    private Remedio buscarRemedio(String nomeUsuario, Long remedioId) {
        if (nomeUsuario == null || remedioId == null) {
            throw new RegraException("Informe o usuario e o remedio.");
        }
        return buscarOuFalhar(nomeUsuario).getRemedios().stream()
                .filter(r -> remedioId.equals(r.getId()))
                .findFirst()
                .orElseThrow(() -> new RegraException("Remedio invalido."));
    }

    /**
     * O cadastro vem do DAO ja com id (e com os remedios carregados). Aplica a
     * mudanca no objeto e manda o DAO atualizar a linha (merge).
     */
    private void alterar(Cadastro cadastro, Consumer<Cadastro> mudanca) {
        mudanca.accept(cadastro);
        cadastroDAO.update(cadastro);
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
        Remedio remedio = new Remedio.Builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .qtdPilulasAtual(dto.qtdPilulasAtual())
                .dataInicioMedicacao(dto.dataInicioMedicacao())
                .preco(dto.preco())
                .pilulasPorDia(dto.pilulasPorDia())
                .build();
        if (dto.id() != null) {
            remedio.setId(dto.id());   // remedio ja existente: sera atualizado, nao recriado
        }
        return remedio;
    }

    private Tarefa toTarefa(TarefaDTO dto) {
        Tarefa tarefa = new Tarefa.Builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .dataLimite(dto.dataLimite())
                .categoria(dto.categoria())
                .prioridade(dto.prioridade())
                .concluida(dto.concluida())
                .build();
        if (dto.id() != null) {
            tarefa.setId(dto.id());
        }
        return tarefa;
    }

    // ---- entidade -> DTO ----
    private CadastroDTO toCadastroDTO(Cadastro c) {
        return new CadastroDTO(
                c.getUsuario().getNome(),
                c.getUsuario().getEmail(),
                c.getUsuario().getSenha(),   // ja e o HASH da senha, nunca o texto original
                c.getPessoa().getNome(),
                c.getPessoa().getIdade(),
                c.getPessoa().getAltura(),
                c.getPessoa().getPeso(),
                c.getRemedios().stream().map(this::toRemedioDTO).toList());
    }

    private RemedioDTO toRemedioDTO(Remedio r) {
        return new RemedioDTO(r.getId(), r.getNome(), r.getDescricao(), r.getQtdPilulasAtual(),
                r.getDataInicioMedicacao(), r.getPreco(), r.getPilulasPorDia());
    }

    private TarefaDTO toTarefaDTO(Tarefa t) {
        return new TarefaDTO(t.getId(), t.getNome(), t.getDescricao(), t.getDataLimite(),
                t.getCategoria(), t.getPrioridade(), t.isConcluida());
    }

    private AgendaDTO toAgendaDTO(Agenda a) {
        return new AgendaDTO(
                a.getId(),
                a.getData(),
                a.getNome(),
                a.getDescricao(),
                a.isConcluida(),
                a.getRemedio() != null ? toRemedioDTO(a.getRemedio()) : null,
                a.getStatusRemedio(),
                a.getTarefa() != null ? toTarefaDTO(a.getTarefa()) : null);
    }
}
