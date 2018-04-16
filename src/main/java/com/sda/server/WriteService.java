package com.sda.server;

import com.sda.encrypt.Cipher;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class WriteService implements Runnable {

    private Socket clientSocket;
    private Scanner scan;
    private String name;
    private Cipher cipher;

    public WriteService(Socket clientSocket, String name) {
        this.clientSocket = clientSocket;
        this.scan  = new Scanner(System.in);
        this.name = name;
    }

    public void presentMe(PrintWriter printWriter) {
            printWriter.println("myname:" + name);
            printWriter.flush();
    }

    @Override
    public void run() {
        try ( PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream()) ) {
            presentMe(printWriter);

            while (clientSocket.isConnected()) {
                //Odczytaj linijke od klienta
                System.out.println("Send message: ");
                String line = cipher.encrypt(this.scan.nextLine());
                //Odpowiedz do klienta
                printWriter.println("Response from client: " + line);
                //flush - wypchnij z bufora
                printWriter.flush();
            }
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }

    }
}
