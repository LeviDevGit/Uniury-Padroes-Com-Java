import java.util.List; // Interface de lista (usada para armazenar histórico)
import java.util.Scanner; // Usada para ler entrada do teclado
import java.util.Stack; // Estrutura LIFO (último que entra é o primeiro que sai)

// O Originator
class EditorDeTexto {
  private String conteudo; // Armazena o texto atual do editor

  // Construtor → define o texto inicial
  public EditorDeTexto(String textoInicial) {
    this.conteudo = textoInicial;
  }

  // Método para adicionar uma nova frase ao texto existente
  public void adicionarFrase(String novaFrase) {
    this.conteudo += " " + novaFrase;
  }

  // Retorna o conteúdo atual
  public String getConteudo() {
    return this.conteudo;
  }

  // Salva o estado atual (cria um "snapshot")
  public EditorMemento salvar() {
    return new EditorMemento(conteudo);
  }

  // Restaura o conteúdo a partir de um estado salvo (memento)
  public void restaurar(EditorMemento memento) {
    this.conteudo = memento.getConteudoSalvo();
  }
}

// O Memento
class EditorMemento {
  private final String conteudo; // Conteúdo salvo (imutável)

  // Construtor → salva o estado
  public EditorMemento(String conteudo) {
    this.conteudo = conteudo;
  }

  // Retorna o conteúdo salvo
  public String getConteudoSalvo() {
    return conteudo;
  }
}

// O Caretaker (guardar e organizar o histórico)
class Historico {
  private Stack<EditorMemento> estados = new Stack<>(); // Pilha de estados (último salvo será o primeiro a desfazer)

  // Adiciona um novo estado ao histórico
  public void adicionar(EditorMemento memento) {
    estados.push(memento);
  }

  // Remove e retorna o último estado (Ctrl+Z)
  public EditorMemento desfazer() {
    if (!estados.isEmpty()) {
      return estados.pop(); // remove o topo da pilha
    }
    return null; // se não houver histórico
  }

  // Retorna todos os estados (para listar no menu)
  public List<EditorMemento> getListaMemento() {
    return estados;
  }
}

// Classe principal para executar o programa
public class Main {
  public static void main(String[] args) {
    EditorDeTexto meuEditor = new EditorDeTexto("Era uma vez..."); // Cria o editor com texto inicial
    Historico historico = new Historico(); // Cria o histórico de estados

    Scanner teclado = new Scanner(System.in); // Scanner para ler entrada do usuário

    executarMenu(teclado, meuEditor, historico); // Chama o menu principal

    // Mostra resultado final
    System.out.println("\nConteúdo final: \n" + meuEditor.getConteudo());
    System.out.println("Sistema encerrado. Até logo!");
    teclado.close(); // Fecha o Scanner
  }

  // Menu Interativo
  public static void executarMenu(Scanner teclado, EditorDeTexto meuEditor, Historico historico) {
    int opcao = -1; // controla o loop

    System.out.println("--- BEM-VINDO AO SISTEMA ---");

    // Loop principal (roda até usuário escolher sair)
    while (opcao != 0) {
      limparConsole();
      List<EditorMemento> lista = historico.getListaMemento(); // Pega lista de estados salvos
      // Mostra conteúdo atual
      System.out.println("\n------------------------------");
      System.out.println("\nConteúdo atual: \n" + "\033[3m\"" + meuEditor.getConteudo() + "\"\033[0m");
      System.out.println("\n------------------------------");
      // Menu de opções
      System.out.println("1 - Adicionar frase");
      // Só mostra opções extras se houver histórico
      if (!lista.isEmpty()) {
        System.out.println("2 - Desfazer (Ctrl+Z)");
        System.out.println("3 - Ver Histórico e Restaurar (" + lista.size() + ") ");
      }
      System.out.println("0 - Sair");
      System.out.print("Escolha uma opção: ");

      // Validação → impede entrada inválida
      while (!teclado.hasNextInt()) {
        System.out.println("Erro: Isso não é um número válido!");
        System.out.print("Tente novamente: ");
        teclado.next();
      }
      // Lê opção
      opcao = teclado.nextInt();
      teclado.nextLine(); // Limpa buffer

      // Controle de Opções
      switch (opcao) {
        case 1:
          historico.adicionar(meuEditor.salvar()); // Salva estado atual antes de modificar
          System.out.print("Digite uma nova frase: ");

          String textoDigitado = teclado.nextLine();
          meuEditor.adicionarFrase(textoDigitado); // Adiciona nova frase

          System.out.println("Texto atualizado!");
          break;

        case 2:
          EditorMemento anterior = historico.desfazer(); // Desfazer última ação
          if (anterior != null) {
            meuEditor.restaurar(anterior); // restaura estado
            System.out.println("Ação desfeita com sucesso!");
          } else {
            System.out.println("Opção inválida.");
          }
          break;
        case 3:
          // Exibir histórico completo
          if (lista.isEmpty()) {
            System.out.println("Histórico vazio.");
          } else {
            System.out.println("--- Histórico de Estados ---");
            // Lista todos os estados
            for (int i = 0; i < lista.size(); i++) {
              System.out.println(i + " : " + lista.get(i).getConteudoSalvo());
            }
            // Escolher qual restaurar
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

  // Limpa o console1
  public static void limparConsole() {
    // Apenas imprime várias linhas para "empurrar" o conteúdo para cima, simulando
    // um console limpo, deixando tela limpa
    for (int i = 0; i < 50; i++) {
      System.out.println();
    }
  }
}