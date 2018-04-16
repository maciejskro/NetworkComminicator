package com.sda.server;

import com.sda.client.ContactList;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer  {

    private Map<String, Socket>  clientPool;
    private Integer simpleKeyCipher;
    private String aesKeyCipher;

    public EchoServer() {
        Random random = new Random();
        this.simpleKeyCipher = random.nextInt(65533)+1;
        this.clientPool = new ConcurrentHashMap<>();
    }
    public static void main(String[] args) throws Exception {

        //Port na ktorym startuje serwer
        int port = 4444;

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println( "Started server on port " + port );
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        EchoServer echoServer = new EchoServer();

        while (true) {

            System.out.println("Waiting for client...");
            //Czekaj na klienta

            Socket socket1 = serverSocket.accept();
            Socket socket2 = serverSocket.accept();

            echoServer.sendAvailableClients();

            //Thread thread = new Thread(new RunnableServers(socket));
            //thread.start();
            //Thread.sleep(SLEEP_TIME);

            // executorService.submit(new ReaderService(socket));
            // executorService.submit(new WriteService(socket));
            echoServer.createCommunication(executorService,socket1, socket2, echoServer.clientPool );
        }
    }

    private void sendAvailableClients() throws IOException {
        ContactList cl = new ContactList(new ArrayList<>(clientPool.keySet()), null);
        if (clientPool.size() > 0 ) {
            for (String s: clientPool.keySet()) {
                OutputStream output = clientPool.get(s).getOutputStream();
                ObjectOutputStream pw = new ObjectOutputStream(output);
                pw.writeObject(cl);
            }

        }
    }

    public void createCommunication(ExecutorService ee , Socket input, Socket output, Map<String,Socket> clientMap)  {

            if (clientMap.size() >= 2) {
                ee.submit(new TaskHandlerProxy(input, output, clientMap));
                ee.submit(new TaskHandlerProxy(output, input, clientMap));
            }
    }

}
