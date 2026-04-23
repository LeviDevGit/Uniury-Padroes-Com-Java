import java.util.List;
import java.util.Scanner;
import java.util.Stack;

// O Originator
class EditorDeTexto {
  private String conteudo;

  public EditorDeTexto(String textoInicial) {
    this.conteudo = textoInicial;
  }

  public void adicionarFrase(String novaFrase) {
    this.conteudo += " " + novaFrase;
  }

  public String getConteudo() {
    return this.conteudo;
  }

  public EditorMemento salvar() {
    return new EditorMemento(conteudo);
  }

  public void restaurar(EditorMemento memento) {
    this.conteudo = memento.getConteudoSalvo();
  }
}

// O Memento
class EditorMemento {
  private final String conteudo;

  public EditorMemento(String conteudo) {
    this.conteudo = conteudo;
  }

  public String getConteudoSalvo() {
    return conteudo;
  }
}

// O Caretaker
class Historico {
  private Stack<EditorMemento> estados = new Stack<>();

  public void adicionar(EditorMemento memento) {
    estados.push(memento);
  }

  public EditorMemento desfazer() {
    if (!estados.isEmpty()) {
      return estados.pop();
    }
    return null;
  }

  public List<EditorMemento> getListaMemento() {
    return estados;
  }
}

public class Main {
  public static void main(String[] args) {
    EditorDeTexto meuEditor = new EditorDeTexto("Era uma vez...");
    Historico historico = new Historico();

    Scanner teclado = new Scanner(System.in);

    executarMenu(teclado, meuEditor, historico);

    System.out.println("\nConteúdo final: \n" + meuEditor.getConteudo());
    System.out.println("Sistema encerrado. Até logo!");
    teclado.close();
  }

  public static void executarMenu(Scanner teclado, EditorDeTexto meuEditor, Historico historico) {
    int opcao = -1;

    System.out.println("--- BEM-VINDO AO SISTEMA ---");

    while (opcao != 0) {
      limparConsole();
      List<EditorMemento> lista = historico.getListaMemento();
      System.out.println("\n------------------------------");
      System.out.println("\nConteúdo atual: \n" + "\033[3m\"" + meuEditor.getConteudo() + "\"\033[0m");
      System.out.println("\n------------------------------");
      System.out.println("1 - Adicionar frase");
      if(!lista.isEmpty()){
        System.out.println("2 - Desfazer (Ctrl+Z)");
        System.out.println("3 - Ver Histórico e Restaurar (" + lista.size() + ") ");
      }
      System.out.println("0 - Sair");
      System.out.print("Escolha uma opção: ");

      while (!teclado.hasNextInt()) {
        System.out.println("Erro: Isso não é um número válido!");
        System.out.print("Tente novamente: ");
        teclado.next();
      }

      opcao = teclado.nextInt();
      teclado.nextLine();

      switch (opcao){
        case 1:
          historico.adicionar(meuEditor.salvar());
          System.out.print("Digite uma nova frase: ");

          String textoDigitado = teclado.nextLine();
          meuEditor.adicionarFrase(textoDigitado);

          System.out.println("Texto atualizado!");
          break;

        case 2:
          EditorMemento anterior = historico.desfazer();
          if (anterior != null) {
            meuEditor.restaurar(anterior);
            System.out.println("Ação desfeita com sucesso!");
          } else {
            System.out.println("Opção inválida.");
          }
          break;
        case 3:
          if (lista.isEmpty()) {
            System.out.println("Histórico vazio.");
          } else {
            System.out.println("--- Histórico de Estados ---");
            for (int i = 0; i < lista.size(); i++) {
              System.out.println(i + " : " + lista.get(i).getConteudoSalvo());
            }
            System.out.print("Escolha o índice para restaurar (ou -1 para cancelar): ");
            int indice = teclado.nextInt();
            if (indice >= 0 && indice < lista.size()) {
              meuEditor.restaurar(lista.get(indice));
            }
          }
          break;
        case 0:
          System.out.println("Encerrando editor...");
          break;

        default:
          System.out.println("Opção inválida.");
      }
    }
  }

  public static void limparConsole() {
    for (int i = 0; i < 50; i++) {
      System.out.println();
    }
  }
}