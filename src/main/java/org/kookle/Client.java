package org.kookle;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final String username;
    private Socket socket;
    private BufferedReader input;
    private BufferedWriter output;

    public Client(String username) {
        this.username = username;
    }



//    public void readMessage() {
//        new Thread(() -> {
//            String responseMsg;
//            while (socket.isConnected()) {
//                try {
//                    responseMsg = input.readLine();
//                    System.out.println(responseMsg);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }).start();
//    }

    public void sendMessage(String msg) {
        try {
            if (output == null) {
                throw new InvalidObjectException(output.getClass().getName() + " has not created.");
            }
            output.write(msg);
            output.newLine();
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readMessage() {
        if (socket.isConnected()) {
            try {
                String message = input.readLine();
                return message;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public void start(String proxy, int port) throws IOException {
        socket = new Socket(proxy, port);

        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        output.write(username);
        output.newLine();
        output.flush();
    }

    public void close(Socket socket, BufferedWriter input, BufferedReader output) {
        try {
            if (socket != null && input != null && output != null) {
                socket.close();
                input.close();
                output.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        try {
            Client client = new Client("John");
            client.start("localhost", 5050);
            String response;
            while ((response = client.readMessage()) != "exit") {
                System.out.println(response);
                if(!response.equals("exit")){
                    System.out.print(" >>> ");
                    Scanner scanner = new Scanner(System.in);
                    String message = scanner.nextLine();
                    client.sendMessage(message);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
