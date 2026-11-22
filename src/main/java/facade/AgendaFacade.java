package facade;

import dao.AgendaDAO;
import dao.CadastroDAO;
import dao.TarefaDAO;
import dto.AgendaDTO;
import dto.CadastroDTO;
import dto.RemedioDTO;
import dto.TarefaDTO;
import exception.RegraException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;
import model.Categoria;
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
        Cadastro atual = buscarOuFalhar(nomeAntigo);
        Cadastro novo = toCadastro(dto);
        // O DTO nao traz agendas: sem copiar, o orphanRemoval apagaria todas.
        novo.copiarAgendasDe(atual);
        if (!cadastroDAO.update(nomeAntigo, novo)) {
            throw new RegraException("Usuario nao encontrado.");
        }
    }
    
    public void removerCadastro(String nomeUsuario) {
        cadastroDAO.remove(buscarOuFalhar(nomeUsuario));
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
     * Tarefas ordenadas pelo prazo, usando uma classe anonima de Comparator.
     */
    public List<TarefaDTO> listarTarefasPorPrazo() {
        Comparator<TarefaDTO> porPrazo = new Comparator<>() {   // classe anonima
            @Override
            public int compare(TarefaDTO a, TarefaDTO b) {
                return a.dataLimite().compareTo(b.dataLimite());
            }
        };
        return tarefaDAO.getDados().stream()
                .map(this::toTarefaDTO)
                .sorted(porPrazo)
                .toList();
    }

    /**
     * Atualiza a tarefa de id informado. O id nao muda e a tarefa continua
     * pertencendo a mesma agenda.
     *
     * @return {@code true} se a tarefa existia e foi atualizada
     */
    public boolean atualizarTarefa(Long id, TarefaDTO dto) {
        return tarefaDAO.update(id, toTarefa(dto));
    }
    
    /**
     * Retira uma tarefa de uma agenda. A tarefa e apagada do banco (a lista de
     * tarefas da agenda usa {@code orphanRemoval}).
     *
     * @param agendaId id da agenda dona da tarefa
     * @param tarefaId id da tarefa a remover
     * @throws RegraException se a agenda nao existir ou a tarefa nao for dela
     */
    public void removerTarefa(Long agendaId, Long tarefaId) {
        Agenda agenda = agendaDAO.buscarPorId(agendaId);
        if (agenda == null) {
            throw new RegraException("Agenda nao encontrada.");
        }
        if (!agenda.removerTarefaPorId(tarefaId)) {
            throw new RegraException("Essa tarefa nao pertence a agenda informada.");
        }
        agendaDAO.update(agenda);   // o merge faz o orphanRemoval apagar a tarefa
    }

    // =====================  AGENDA  =====================
    /**
     * Cria uma agenda nova DENTRO do cadastro do usuario, ja com as tarefas
     * informadas, numa unica gravacao.
     *
     * @param nomeUsuario dono da agenda (obrigatorio: toda agenda pertence a um
     * cadastro)
     * @param dto dados da agenda; {@code dto.remedio()} (opcional) so precisa do
     * id e {@code dto.tarefas()} pode ter zero, uma ou varias tarefas
     * @return a agenda criada (os ids so sao gerados no banco; para te-los,
     * liste as agendas de novo)
     */
    public AgendaDTO criarAgenda(String nomeUsuario, AgendaDTO dto) {
        Cadastro cadastro = buscarOuFalhar(nomeUsuario);

        Remedio remedio = null;
        if (dto.remedio() != null) {
            remedio = buscarRemedio(cadastro, dto.remedio().id());
        }

        Agenda.Builder builder = new Agenda.Builder(dto.data(), dto.nome(), dto.descricao())
                .remedio(remedio)
                .concluida(dto.concluida());
        dto.tarefas().forEach(t -> builder.tarefa(toTarefa(t)));
        Agenda agenda = builder.build();

        alterar(cadastro, c -> c.adicionarAgenda(agenda));   // cascade salva agenda e tarefas
        return toAgendaDTO(agenda);
    }

    /**
     * Vincula uma tarefa nova a uma agenda que ja existe.
     *
     * @param agendaId id da agenda que vai receber a tarefa
     * @param dto dados da tarefa
     */
    public void adicionarTarefa(Long agendaId, TarefaDTO dto) {
        Agenda agenda = agendaDAO.buscarPorId(agendaId);
        if (agenda == null) {
            throw new RegraException("Agenda nao encontrada.");
        }
        agenda.adicionarTarefa(toTarefa(dto));
        agendaDAO.update(agenda);   // o cascade MERGE salva a tarefa nova
    }

    /** Todas as agendas (de todos os usuarios), da data mais antiga para a mais recente. */
    public List<AgendaDTO> listarAgendas() {
        return agendaDAO.listarPorData().stream()
                .map(this::toAgendaDTO)
                .toList();
    }

    /** Agendas de um usuario, da data mais antiga para a mais recente. */
    public List<AgendaDTO> listarAgendas(String nomeUsuario) {
        return buscarOuFalhar(nomeUsuario).getAgendas().stream()
                .map(this::toAgendaDTO)
                .toList();
    }

    /** Remove a agenda e, em cascata, todas as tarefas dela. */
    public void removerAgenda(Long id) {
        if (!agendaDAO.removerPorId(id)) {
            throw new RegraException("Agenda nao encontrada.");
        }
    }
    
    /** Atualiza os dados da agenda. */
    public void atualizarAgenda(Long id, String nome, String descricao, boolean concluida) {
        Agenda agenda = agendaDAO.buscarPorId(id);
        if (agenda == null) {
            throw new RegraException("Agenda nao encontrada.");
        }
        agenda.setNome(nome);
        agenda.setDescricao(descricao);
        agenda.setConcluida(concluida);
        agendaDAO.update(agenda);
    }
    
    // =====================  RELATORIOS (wrapper classes)  =====================
    /**
     * Remedio que acaba primeiro e em quantos dias (Integer, wrapper).
     * @param nomeUsuario
     * @return
     */
    public Optional<String> proximoARepor(String nomeUsuario) {
        return buscarOuFalhar(nomeUsuario).getRemedios().stream()
                .min(Comparator.comparingInt(
                        r -> r.getQtdPilulasRestantes() / r.getPilulasPorDia()))
                .map(r -> {
                    Integer dias = r.getQtdPilulasRestantes() / r.getPilulasPorDia(); // int -> Integer
                    return r.getNome() + " acaba em " + dias + " dia(s)";
                });
    }

    /**
     * Quantidade de tarefas por categoria.counting() produz Long (wrapper).
     * @return 
     */
    public Map<Categoria, Long> tarefasPorCategoria() {
        return tarefaDAO.getDados().stream()
                .collect(Collectors.groupingBy(
                        Tarefa::getCategoria,
                        () -> new EnumMap<>(Categoria.class),
                        Collectors.counting()));
    }

    // =====================  PRIVADOS  =====================
    private Cadastro buscarOuFalhar(String nomeUsuario) {
        Cadastro cadastro = cadastroDAO.acharPorUsuario(nomeUsuario);
        if (cadastro == null) {
            throw new RegraException("Usuario nao encontrado.");
        }
        return cadastro;
    }

    private Remedio buscarRemedio(Cadastro cadastro, Long remedioId) {
        if (remedioId == null) {
            throw new RegraException("Informe o remedio.");
        }
        return cadastro.getRemedios().stream()
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
                a.getTarefas().stream().sorted().map(this::toTarefaDTO).toList());
    }
}
