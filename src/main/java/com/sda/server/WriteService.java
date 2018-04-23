package com.sda.server;

import com.sda.client.ContactList;
import com.sda.encrypt.Cipher;
import com.sda.encrypt.CipherFactory;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WriteService implements Runnable {

    private Socket clientSocket;
    private Scanner scan;
    private String name;
    private Cipher encrypt;

    public WriteService(Socket clientSocket, String name) {
        this.clientSocket = clientSocket;
        this.scan = new Scanner(System.in);
        this.name = name;
        this.encrypt = CipherFactory.create("Caesar");
    }

    public ContactList presentMe(ObjectOutputStream printWriter) {
        ContactList result = null;
        try {
            List<String> names = new ArrayList<>();
            names.add(this.name);
            result = new ContactList(names, null);

            printWriter.writeObject(result);
            printWriter.flush();
            return result;
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void run() {
        try (ObjectOutputStream printWriter = new ObjectOutputStream(clientSocket.getOutputStream())) {
            presentMe(printWriter);

            while (clientSocket.isConnected()) {
                //Odczytaj linijke od klienta
                System.out.println("Send message: ");
                String line = this.scan.nextLine();
                //Odpowiedz do klienta
                ContactList cl = new ContactList(null, line);
                printWriter.writeObject(cl);
                //printWriter.println("Response from client: " + line);
                //flush - wypchnij z bufora
                printWriter.flush();
            }
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }

    }
}
