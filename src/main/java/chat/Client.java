package chat;

import chat.cipher.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {

            System.out.print("Insira o IP do servidor: ");
            String host = scanner.nextLine().trim();

            System.out.print("Insira a porta do servidor: ");
            int port = Integer.parseInt(scanner.nextLine().trim());

            try (Socket socket = new Socket(host, port);
                 BufferedReader entrada = new BufferedReader(
                         new InputStreamReader(socket.getInputStream()));
                 PrintWriter saida = new PrintWriter(
                         socket.getOutputStream(), true)) {

                System.out.println("Servidor conectado.");

                Cipher cipher = selecionarCipher(scanner);

                if (cipher == null) {
                    System.out.println("Saindo...");
                    return;
                }

                System.out.println("\nChat iniciado.");
                System.out.println("Digite /sair para desconectar.");

                while (true) {

                    System.out.print("Você> ");
                    String mensagem = scanner.nextLine();

                    if (mensagem.equalsIgnoreCase("/sair")) {
                        saida.println("/sair");
                        System.out.println("Desconectando...");
                        break;
                    }

                    String mensagemCriptografada =
                            cipher.encrypt(mensagem);

                    saida.println(mensagemCriptografada);

                    String respostaCriptografada = entrada.readLine();

                    if (respostaCriptografada == null) {
                        System.out.println("Servidor desconectado.");
                        break;
                    }

                    if (respostaCriptografada.equalsIgnoreCase("/sair")) {
                        System.out.println("Outro cliente desconectou.");
                        break;
                    }

                    String resposta =
                            cipher.decrypt(respostaCriptografada);

                    System.out.println("Outro cliente> " + resposta);
                }

            } catch (IOException e) {
                System.out.println("Erro de conexão: " + e.getMessage());

            } catch (NumberFormatException e) {
                System.out.println("Número da porta inválido.");
            }
        }
    }

    private static Cipher selecionarCipher(Scanner scanner) {

        while (true) {

            System.out.println("\n=== Selecionar Criptografia ===");
            System.out.println("1 - Sem criptografia");
            System.out.println("2 - César");
            System.out.println("3 - Monoalfabética");
            System.out.println("4 - Playfair");
            System.out.println("5 - Vigenère");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            String opcao = scanner.nextLine().trim();

            switch (opcao) {

                case "1":
                    System.out.println("Selecionado: Sem criptografia");
                    return new NoCipher();

                case "2":
                    System.out.print("Insira a chave de César: ");

                    try {
                        int deslocamento =
                                Integer.parseInt(scanner.nextLine().trim());

                        System.out.println("Selecionado: César");

                        return new CaesarCipher(deslocamento);

                    } catch (NumberFormatException e) {
                        System.out.println("Chave de César inválida.");
                    }

                    break;

                case "3":
                    System.out.print("Insira a chave da cifra monoalfabética: ");
                    String chaveMono = scanner.nextLine().trim();

                    if (chaveMono.isEmpty()) {
                        System.out.println("A chave não pode estar vazia.");
                        break;
                    }

                    System.out.println("Selecionado: Monoalfabética");

                    return new MonoCipher(chaveMono);

                case "4":
                    System.out.print("Insira a chave da cifra Playfair: ");
                    String chavePlayfair = scanner.nextLine().trim();

                    if (chavePlayfair.isEmpty()) {
                        System.out.println("A chave não pode estar vazia.");
                        break;
                    }

                    System.out.println("Selecionado: Playfair");

                    // Quando implementar:
                    // return new PlayfairCipher(chavePlayfair);

                    System.out.println(
                            "A cifra Playfair ainda não foi implementada."
                    );

                    break;

                case "5":
                    System.out.print("Insira a chave da cifra Vigenère: ");
                    String chaveVigenere = scanner.nextLine().trim();

                    if (chaveVigenere.isEmpty()) {
                        System.out.println("A chave não pode estar vazia.");
                        break;
                    }

                    System.out.println("Selecionado: Vigenère");

                    return new VigenereCipher(chaveVigenere);

                case "0":
                    return null;

                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}