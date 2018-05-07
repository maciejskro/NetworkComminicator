package com.sda.client;

import com.sda.server.ReaderService;
import com.sda.server.WriteService;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    private ContactList messages;
    private ConcurrentLinkedQueue<ContactList> queue;
    private CopyOnWriteArrayList<String> availableClient;

    public Client() {
        this.availableClient = new CopyOnWriteArrayList<>();
        this.queue = new ConcurrentLinkedQueue<>();
    }

    public static void main(String[] args) throws IOException {

        //host serwera
        String host = "localhost";
        //port serwera
        int port = 4444;
        Random rand = new Random();
        Client client = new Client();
        client.prepareHelloMessage(rand);

        ExecutorService executorService = Executors.newFixedThreadPool(6);

        //Tworzymy gniazdo do polaczenia z serwerem
        Socket socket = new Socket(host,port);

        //Wysyłamy widomość powitalną i czeka na nowy wpis
        WriteService writeService = new WriteService(socket, client.messages.getListContact().get(0));
        writeService.addMessageToQueue(client.messages);
        executorService.submit(writeService);

        ReaderService readerService = new ReaderService(socket,client.availableClient);
        executorService.submit( readerService );
    }

    private void prepareHelloMessage(Random rand) {
        String clientName = "ClientName" + (rand.nextInt(65533)+1);
        messages = new ContactList(Arrays.asList(clientName),"hello");
    }
}
