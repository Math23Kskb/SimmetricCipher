package chat;

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
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                System.out.println("Servidor conectado.");
                System.out.println("Digite /sair para desconectar");

                while (true) {
                    System.out.print("Cliente> ");
                    String message = scanner.nextLine();

                    if ("/sair".equalsIgnoreCase(message.trim())) {
                        System.out.println("Desconectando...");
                        break;
                    }

                    out.println(message);

                    String serverResponse = in.readLine();
                    if (serverResponse == null) {
                        System.out.println("Servidor desconectado.");
                        break;
                    }

                    System.out.println("Servidor: " + serverResponse);
                }
            } catch (IOException e) {
                System.out.println("Connection error: " + e.getMessage());
            }
        } catch (NumberFormatException e) {
            System.out.println("Número da porta inválida.");
        }
    }
}
