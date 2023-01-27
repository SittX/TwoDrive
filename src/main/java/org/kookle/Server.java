package org.kookle;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedWriter output;
    private BufferedReader input;

    public static void main(String[] args) {
        Server server = new Server();
        server.start(5050);
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server listening on port: " + port);
            while (!serverSocket.isClosed()) {
                clientSocket = serverSocket.accept();
                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                String username = input.readLine();
                System.out.println(username + " is connected.");
                output.write("Hello " + username);
                output.newLine();
                output.flush();
                while (clientSocket.isConnected()) {
                    System.out.println(username + " replied : " + input.readLine());
                    System.out.print(" >>> ");
                    Scanner scanner = new Scanner(System.in);
                    String message = scanner.nextLine();
                    if(!message.equals("exit")){
                        output.write(message);
                        output.newLine();
                        output.flush();
                    }
                    else{
                        clientSocket.close();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void sendMessage(String msg) {
        try {
            if (output == null) {
                throw new InvalidObjectException(output.getClass().getName() + " has not created.");
            }

            while (!clientSocket.isClosed()) {
                output.write(msg);
                output.newLine();
                output.flush();
            }

        } catch (InvalidObjectException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
