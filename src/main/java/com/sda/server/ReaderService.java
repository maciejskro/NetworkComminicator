package com.sda.server;

import com.sda.client.ContactList;
import com.sda.encrypt.CipherFactory;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.sda.encrypt.Cipher;

public class ReaderService implements Runnable {

    private Socket serverSocket;
    private Cipher encrption;
    private CopyOnWriteArrayList<String> availableClientList;

    public ReaderService(Socket serverSocket, CopyOnWriteArrayList<String> availableClientList) {
        this.serverSocket = serverSocket;
        this.encrption = CipherFactory.create("Caesar");
        this.availableClientList = availableClientList;
    }

    public List<String> getAvailableClientList() {
        return this.availableClientList;
    }

    @Override
    public void run() {

        // Cipher
        try (ObjectInputStream reader = new ObjectInputStream(this.serverSocket.getInputStream()))
        {
            ContactList line = null;
            //String key = reader.readObject();

            while (( line = (ContactList) reader.readObject() ) != null ) {
                if (line.getMessageBody() =="hello") {
                    this.availableClientList.addIfAbsent( line.getListContact().get(0) );
                }
                else {
                    System.out.println("Received: " + line.getMessageBody());
                    line.getListContact().forEach(x -> {
                                System.out.println("client: " + x);
                                this.availableClientList.add(x);
                            }
                    );
                }
            }
        }
        catch (IOException e) {
            System.out.println("error" );
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
