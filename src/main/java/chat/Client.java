package chat;

import chat.cipher.*;
import chat.cipher.Cipher;

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

                Thread threadRecebimento = new Thread(() -> {

                    try {
                        String mensagemCriptografada;

                        while ((mensagemCriptografada = entrada.readLine()) != null) {

                            if (mensagemCriptografada.equalsIgnoreCase("/sair")) {
                                System.out.println("\nOutro cliente desconectou.");
                                socket.close();
                                break;
                            }

                            String mensagem =
                                    cipher.decrypt(mensagemCriptografada);

                            System.out.println("\nOutro cliente> " + mensagem);
                            System.out.print("Você> ");
                        }

                    } catch (IOException e) {
                        if (!socket.isClosed()) {
                            System.out.println("\nConexão encerrada.");
                        }
                    }
                });

                Thread threadEnvio = new Thread(() -> {

                    while (true) {

                        System.out.print("Você> ");
                        String mensagem = scanner.nextLine();

                        if (mensagem.equalsIgnoreCase("/sair")) {
                            saida.println("/sair");

                            try {
                                socket.close();
                            } catch (IOException e) {
                                System.out.println("Erro ao fechar conexão.");
                            }

                            break;
                        }

                        String mensagemCriptografada =
                                cipher.encrypt(mensagem);

                        saida.println(mensagemCriptografada);
                    }
                });

                threadRecebimento.start();
                threadEnvio.start();

                threadEnvio.join();
                threadRecebimento.join();

            } catch (IOException e) {
                System.out.println("Erro de conexão: " + e.getMessage());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Cliente interrompido.");

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

                    try{
                        return new MonoCipher(chaveMono);

                    }
                    catch(IllegalArgumentException e){
                        System.out.println("Chave inválida");

                    }


                case "4":
                    System.out.print("Insira a chave da cifra Playfair: ");
                    String chavePlayfair = scanner.nextLine().trim();

                    if (chavePlayfair.isEmpty()) {
                        System.out.println("A chave não pode estar vazia.");
                        break;
                    }

                    System.out.println("Selecionado: Playfair");

                    try{
                        return new PlayfairCipher(chavePlayfair);

                    }
                    catch(IllegalArgumentException e){
                        System.out.println("Chave inválida");

                    }



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
