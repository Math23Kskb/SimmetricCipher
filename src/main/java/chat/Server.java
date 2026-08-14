package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 5000;

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Servidor escutando na porta " + PORT + "...");

            try (Socket cliente1 = serverSocket.accept()) {

                System.out.println(
                        "Cliente 1 conectado: "
                                + cliente1.getInetAddress().getHostAddress()
                                + ":"
                                + cliente1.getPort()
                );

                try (Socket cliente2 = serverSocket.accept()) {

                    System.out.println(
                            "Cliente 2 conectado: "
                                    + cliente2.getInetAddress().getHostAddress()
                                    + ":"
                                    + cliente2.getPort()
                    );

                    BufferedReader entrada1 = new BufferedReader(
                            new InputStreamReader(cliente1.getInputStream())
                    );

                    PrintWriter saida1 = new PrintWriter(
                            cliente1.getOutputStream(),
                            true
                    );

                    BufferedReader entrada2 = new BufferedReader(
                            new InputStreamReader(cliente2.getInputStream())
                    );

                    PrintWriter saida2 = new PrintWriter(
                            cliente2.getOutputStream(),
                            true
                    );

                    System.out.println("Os dois clientes estão conectados.");
                    System.out.println("Chat iniciado.");

                    Thread cliente1ParaCliente2 = new Thread(() -> {
                        try {
                            String mensagem;

                            while ((mensagem = entrada1.readLine()) != null) {

                                if (mensagem.equalsIgnoreCase("/sair")) {
                                    saida2.println("/sair");
                                    break;
                                }

                                saida2.println(mensagem);
                            }

                        } catch (IOException e) {
                            System.out.println("Cliente 1 desconectado.");
                        }
                    });

                    Thread cliente2ParaCliente1 = new Thread(() -> {
                        try {
                            String mensagem;

                            while ((mensagem = entrada2.readLine()) != null) {

                                if (mensagem.equalsIgnoreCase("/sair")) {
                                    saida1.println("/sair");
                                    break;
                                }

                                saida1.println(mensagem);
                            }

                        } catch (IOException e) {
                            System.out.println("Cliente 2 desconectado.");
                        }
                    });

                    cliente1ParaCliente2.start();
                    cliente2ParaCliente1.start();

                    cliente1ParaCliente2.join();
                    cliente2ParaCliente1.join();

                    System.out.println("Chat encerrado.");
                }
            }

        } catch (IOException e) {
            System.out.println("Erro no servidor: " + e.getMessage());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Servidor interrompido.");
        }
    }
}