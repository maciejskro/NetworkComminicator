package com.sda.server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class WriteService implements Runnable {

    private Socket clientSocket;
    private Scanner scan;


    public WriteService(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.scan  = new Scanner(System.in);
    }

    public void presentMe(String name) {
        try( PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream())) {
            printWriter.println("myname:" + name);
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try ( PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream()) ) {
            while (clientSocket.isConnected()) {
                //Odczytaj linijke od klienta
                System.out.println("Send message: ");
                String line = this.scan.nextLine();
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
