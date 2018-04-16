package com.sda.server;

import java.net.ServerSocket;
import java.net.Socket;
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

            //Thread thread = new Thread(new RunnableServers(socket));
            //thread.start();
            //Thread.sleep(SLEEP_TIME);

            // executorService.submit(new ReaderService(socket));
            // executorService.submit(new WriteService(socket));
            echoServer.createCommunication(executorService,socket1, socket2, echoServer.clientPool );
        }
    }

    public void createCommunication(ExecutorService ee , Socket input, Socket output, Map<String,Socket> clientMap)  {
            ee.submit(new TaskHandlerProxy(input, output, clientMap) );
            ee.submit(new TaskHandlerProxy(output, input, clientMap) );
    }

}
