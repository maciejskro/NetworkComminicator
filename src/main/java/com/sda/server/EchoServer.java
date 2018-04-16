package com.sda.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer  {

    public static Map<String, Socket>  clientPool;

    public static void main(String[] args) throws Exception {


        //Port na ktorym startuje serwer
        int port = 4444;

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Started server on port " + port);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        EchoServer echoServer = new EchoServer();

        while (true) {

            System.out.println("Waiting for client...");
            //Czekaj na klienta

            echoServer.createSocketServer(serverSocket);

            Socket socket1 = serverSocket.accept();
            Socket socket2 = serverSocket.accept();

            //Thread thread = new Thread(new RunnableServers(socket));
            //thread.start();
            //Thread.sleep(SLEEP_TIME);

            // executorService.submit(new ReaderService(socket));
            // executorService.submit(new WriteService(socket));

            executorService.submit(new TaskHandlerProxy(socket1.getInputStream(), socket2.getOutputStream()) );
            executorService.submit(new TaskHandlerProxy(socket2.getInputStream(), socket1.getOutputStream()) );

        }
    }

    private void createSocketServer(ServerSocket serverSocket) throws IOException {
        if ( clientPool != null) {
            this.clientPool = new HashMap<>();
        }
        Socket socket = serverSocket.accept();
        String name = socket.getInputStream().toString();
        this.clientPool.put(name, socket);

    }
}
