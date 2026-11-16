package view;

import dao.JPAUtil;
import dao.TarefaDAOJpa;
import java.time.LocalDateTime;
import model.Categoria;
import model.Tarefa;

public class TesteJpa {

    public static void main(String[] args) {
        JPAUtil.criarEntityManager().close();
        JPAUtil.fechar();
    }
}
