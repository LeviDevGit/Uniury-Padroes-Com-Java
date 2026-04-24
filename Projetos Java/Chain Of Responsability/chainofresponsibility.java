import java.util.Scanner; // Importa Scanner para ler dados do teclado

// Classe Base HANDLER

abstract class CaixaHandler { // Classe abstrata → serve como base para todas as notas
    protected CaixaHandler proximo; // Referência para o próximo elemento da cadeia

    // Define quem será o próximo da cadeia
    public void setProximo(CaixaHandler proximo) {
        this.proximo = proximo;
    }

    // Método abstrato → cada classe concreta deve implementar
    public abstract void sacar(int valor);
}

// HANDLERS CONCRETOS
// Cada classe representa uma nota

// Classe responsável por notas de 100
class Nota100 extends CaixaHandler {
    @Override
    public void sacar(int valor) {
        // Verifica se o valor pode usar notas de 100
        if (valor >= 100) {
            int qtd = valor / 100; // quantidade de notas de 100
            int resto = valor % 100; // valor restante
            System.out.println(qtd + " nota(s) de 100");
            // Se ainda sobrou valor, passa para o próximo handler
            if (resto != 0)
                proximo.sacar(resto);
        } else {
            // Se não pode usar 100, passa direto para o próximo handler
            proximo.sacar(valor);
        }
    }
}

// Classe responsável por notas de 50
class Nota50 extends CaixaHandler {
    @Override
    public void sacar(int valor) {
        if (valor >= 50) {
            int qtd = valor / 50; // quantidade de notas de 50
            int resto = valor % 50; // resto
            System.out.println(qtd + " nota(s) de 50");
            // Passa o restante
            if (resto != 0)
                proximo.sacar(resto);
        } else {
            proximo.sacar(valor);
        }
    }
}

// Classe responsável por notas de 20
class Nota20 extends CaixaHandler {
    @Override
    public void sacar(int valor) {
        if (valor >= 20) {
            int qtd = valor / 20;
            int resto = valor % 20;
            System.out.println(qtd + " nota(s) de 20");
            if (resto != 0)
                proximo.sacar(resto);
        } else {
            proximo.sacar(valor);
        }
    }
}

// Classe responsável por notas de 10 (última da cadeia)
class Nota10 extends CaixaHandler {
    @Override
    public void sacar(int valor) {
        if (valor >= 10) {
            int qtd = valor / 10;
            int resto = valor % 10;
            System.out.println(qtd + " nota(s) de 10");
            // Aqui é o fim da cadeia → não tem próximo
            if (resto != 0) {
                System.out.println("Valor restante não pode ser sacado: " + resto);
            }
        } else {
            // Se nem 10 consegue usar, valor inválido
            System.out.println("Valor inválido: " + valor);
        }
    }
}

// Classe Principal MAIN
public class chainofresponsibility {
    public static void main(String[] args) {
        // Try-with-resources → fecha o Scanner automaticamente
        try (Scanner sc = new Scanner(System.in)) {

            // montagem da cadeia

            // Cria cada handler
            CaixaHandler n100 = new Nota100();
            CaixaHandler n50 = new Nota50();
            CaixaHandler n20 = new Nota20();
            CaixaHandler n10 = new Nota10();

            // Liga a cadeia (ordem importa!)
            n100.setProximo(n50);
            n50.setProximo(n20);
            n20.setProximo(n10);

            while (true) {
                System.out.println("\nDigite o valor para saque (0 para sair): ");
                int valor = sc.nextInt();

                // Condição de saída
                if (valor == 0)
                    break;

                // Sempre começa pelo maior valor (100)
                n100.sacar(valor);
            }
        }
    }
}
