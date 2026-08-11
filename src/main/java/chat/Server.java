package chat;

import chat.cipher.Cipher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {
        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Servidor escutando na porta " + port + "...");

            try (Socket clientSocket = serverSocket.accept();
                 BufferedReader in = new BufferedReader(
                         new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(
                         clientSocket.getOutputStream(), true);
                 Scanner scanner = new Scanner(System.in)) {

                System.out.println(
                        "Cliente conectado: "
                                + clientSocket.getInetAddress().getHostAddress()
                                + ":"
                                + clientSocket.getPort()
                );

                Cipher cipher = selectCipher(scanner);

                System.out.println("\nChat iniciado.");

                while (true) {
                    String clientMessage = in.readLine();

                    if (clientMessage == null) {
                        System.out.println("Cliente desconectado.");
                        break;
                    }

                    if (clientMessage.equalsIgnoreCase("/sair")) {
                        System.out.println("Cliente encerrou o chat.");
                        break;
                    }

                    String decryptedMessage = cipher.decrypt(clientMessage);

                    System.out.println("Cliente: " + decryptedMessage);

                    System.out.print("Servidor> ");
                    String response = scanner.nextLine();

                    if (response.equalsIgnoreCase("/sair")) {
                        out.println("/sair");
                        break;
                    }

                    String encryptedResponse = cipher.encrypt(response);

                    out.println(encryptedResponse);
                }
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    private static Cipher selectCipher(Scanner scanner) {
        while (true) {
            System.out.println("\n=== Select Cipher ===");
            System.out.println("1 - NoCipher");
            System.out.println("2 - Caesar");
            System.out.println("3 - Monoalphabetic");
            System.out.println("4 - Playfair");
            System.out.println("5 - Vigenere");
            System.out.println("/sair - Sair");
            System.out.print("Option: ");

            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    System.out.println("Selected: NoCipher");
                    return new NoCipher();

                case "2":
                    System.out.print("Enter Caesar key: ");
                    int shift = Integer.parseInt(scanner.nextLine());

                    System.out.println("Selected: Caesar");
                    return new CaesarCipher(shift);

                case "3":
                    System.out.print("Enter Monoalphabetic key: ");
                    String monoKey = scanner.nextLine();

                    System.out.println("Selected: Monoalphabetic");
                    return new MonoalphabeticCipher(monoKey);

                case "4":
                    System.out.print("Enter Playfair key: ");
                    String playfairKey = scanner.nextLine();

                    System.out.println("Selected: Playfair");
                    return new PlayfairCipher(playfairKey);

                case "5":
                    System.out.print("Enter Vigenere key: ");
                    String vigenereKey = scanner.nextLine();

                    System.out.println("Selected: Vigenere");
                    return new VigenereCipher(vigenereKey);

                case "0":
                    System.out.println("Exiting...");
                    return null;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}