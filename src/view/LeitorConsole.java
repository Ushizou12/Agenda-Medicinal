/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;
import dto.CadastroDTO;
import dto.RemedioDTO;
import dto.TarefaDTO;
import exception.RegraException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import model.Categoria;

/**
 *
 * @author mathe
 */

/**
 * Concentra toda a leitura do console. Os comandos usam esta classe em vez de
 * repetir Scanner/parse/validacao, e devolvem DTOs prontos para a Facade.
 */
public class LeitorConsole {

    private final Scanner sc;

    public LeitorConsole(Scanner sc) {
        this.sc = sc;
    }

    // ---------- leituras primitivas ----------
    public String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return sc.nextLine();
    }

    public int lerInt(String mensagem) {
        return Integer.parseInt(lerTexto(mensagem).trim());
    }

    public int lerIntNaoNegativo(String mensagem) {
        int valor = lerInt(mensagem);
        if (valor < 0) {
            throw new IllegalArgumentException("Nao sao permitidos valores negativos.");
        }
        return valor;
    }

    public double lerDoubleNaoNegativo(String mensagem) {
        double valor = Double.parseDouble(lerTexto(mensagem).trim().replace(',', '.'));
        if (valor < 0) {
            throw new IllegalArgumentException("Nao sao permitidos valores negativos.");
        }
        return valor;
    }

    public boolean lerSimNao(String mensagem) {
        while (true) {
            String resposta = lerTexto(mensagem).trim().toLowerCase();
            if (resposta.equals("s")) {
                return true;
            }
            if (resposta.equals("n")) {
                return false;
            }
            System.out.println("Opcao invalida. Digite apenas 's' ou 'n'.");
        }
    }

    public LocalDate lerData(String titulo) {
        System.out.println(titulo);
        int ano = lerInt("Ano: ");
        int mes = lerInt("Mes: ");
        int dia = lerInt("Dia: ");
        return LocalDate.of(ano, mes, dia);
    }

    public LocalDateTime lerDataHora(String titulo) {
        LocalDate data = lerData(titulo);
        int hora = lerInt("Hora: ");
        int minuto = lerInt("Minuto: ");
        return data.atTime(hora, minuto);
    }

    public Categoria lerCategoria(String titulo) {
        Categoria[] valores = Categoria.values();
        while (true) {
            System.out.println("\n" + titulo);
            for (int i = 0; i < valores.length; i++) {
                System.out.println((i + 1) + " - " + valores[i]);
            }
            int op = lerInt("Opcao: ");
            if (op >= 1 && op <= valores.length) {
                return valores[op - 1];
            }
            System.out.println("Opcao invalida. Digite de 1 a " + valores.length + ".");
        }
    }

    public int lerPrioridade() {
        while (true) {
            int p = lerInt("Prioridade da tarefa (1 a 3): ");
            if (p >= 1 && p <= 3) {
                return p;
            }
            System.out.println("Valor invalido! Digite apenas 1, 2 ou 3.");
        }
    }

    // ---------- listas numeradas ----------
    /** Mostra "1 - nome", "2 - nome"... usando IntStream. */
    public void exibirOpcoes(List<String> nomes) {
        IntStream.range(0, nomes.size())
                .forEach(i -> System.out.println((i + 1) + " - " + nomes.get(i)));
    }

    /** Le um numero de 1..tamanho e devolve o indice (base 0). */
    public int lerIndice(String mensagem, int tamanho) {
        int indice = lerInt(mensagem) - 1;
        if (indice < 0 || indice >= tamanho) {
            throw new RegraException("Opcao invalida.");
        }
        return indice;
    }

    // ---------- leituras que devolvem DTO ----------
    public RemedioDTO lerRemedio() {
        String nome = lerTexto("Nome do remedio: ");
        String descricao = lerTexto("Descricao do remedio: ");
        int qtd = lerIntNaoNegativo("Quantidade de pilulas: ");
        LocalDate inicio = lerData("Data de inicio da medicacao:");
        double preco = lerDoubleNaoNegativo("Preco do remedio: ");
        int pilulasPorDia = lerIntNaoNegativo("Pilulas por dia: ");
        return new RemedioDTO(nome, descricao, qtd, inicio, preco, pilulasPorDia);
    }

    public TarefaDTO lerTarefa() {
        String titulo = lerTexto("Titulo da tarefa: ");
        String descricao = lerTexto("Descricao da tarefa: ");
        LocalDateTime dataHora = lerDataHora("Data e hora da tarefa:");
        Categoria categoria = lerCategoria("Escolha a categoria:");
        int prioridade = lerPrioridade();
        boolean concluida = lerSimNao("Tarefa concluida? (s/n): ");
        return new TarefaDTO(titulo, descricao, dataHora, categoria, prioridade, concluida);
    }

    /**
     * Le usuario + pessoa e pede a lista de remedios ao
     * {@code fornecedorRemedios}. Assim cada comando decide COMO obter os
     * remedios (cadastrar varios, editar um existente...) sem quebrar a ordem
     * das perguntas.
     */
    public CadastroDTO lerCadastro(Supplier<List<RemedioDTO>> fornecedorRemedios) {
        String nomeUsuario = lerTexto("Nome do usuario: ");
        String email = lerTexto("Email do usuario: ");
        String senha = lerTexto("Senha do usuario (minimo 6 caracteres): ");
        String nomePessoa = lerTexto("Nome da pessoa: ");
        int idade = lerIntNaoNegativo("Idade: ");
        double altura = lerDoubleNaoNegativo("Altura (ex: 1.75): ");
        double peso = lerDoubleNaoNegativo("Peso (ex: 78.5): ");
        return new CadastroDTO(nomeUsuario, email, senha, nomePessoa,
                idade, altura, peso, fornecedorRemedios.get());
    }
}
