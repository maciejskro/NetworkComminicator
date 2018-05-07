package com.sda.server;

import com.sda.client.ContactList;
import com.sda.encrypt.Cipher;
import com.sda.encrypt.CipherFactory;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WriteService implements Runnable {

    private Socket serverSocket;
    private Scanner scan;
    private String name;
    private Cipher encrypt;
    private ConcurrentLinkedQueue<ContactList> messagesToSend;

    public WriteService(Socket socketToServer, String name) {
        this.serverSocket = socketToServer;
        this.scan = new Scanner(System.in);
        this.name = name;
        this.encrypt = CipherFactory.create("Caesar");
        this.messagesToSend = new ConcurrentLinkedQueue<>();
    }


    private ContactList getMessages(List<String> listTo, String messages) {
        System.out.println("Send message:");
        String line = this.scan.nextLine();
        // przygotuj message od klienta
        ContactList cl = new ContactList(listTo, line);
        return  cl;
    }

    public void addMessageToQueue(ContactList messages) {
        this.messagesToSend.add( messages);
    }


    @Override
    public void run() {
        try (ObjectOutputStream printWriter = new ObjectOutputStream(serverSocket.getOutputStream())) {

            while (serverSocket.isConnected()) {
                //Odczytaj linijke od klienta

                if ( messagesToSend.isEmpty() ) {
                    ContactList cl = getMessages(null,null);
                    this.messagesToSend.add(cl);
                } else {
                    printWriter.writeObject(this.messagesToSend.poll());
                    //printWriter.println("Response from client: " + line);
                    //flush - wypchnij z bufora
                    printWriter.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }

    }
}
