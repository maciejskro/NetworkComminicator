package com.sda.server;

import com.sda.client.ContactList;
import com.sda.encrypt.CipherFactory;

import java.io.*;
import java.net.Socket;
import com.sda.encrypt.Cipher;

public class ReaderService implements Runnable {

    private Socket clientSocket;
    private Cipher encrption;

    public ReaderService(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.encrption = CipherFactory.create("Ceasar");
    }

    public ObjectInputStream getListAvailableClients() throws IOException{
        ObjectInputStream result = null;

        return result;
    }

    @Override
    public void run() {

        // Cipher
        try (ObjectInputStream reader = new ObjectInputStream(this.clientSocket.getInputStream()))
        {
            ContactList line = null;
            //String key = reader.readObject();

            while (( line = (ContactList) reader.readObject() ) != null ) {
                System.out.println("Received: " + line);
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
