package com.sda.server;

import com.sda.client.ContactList;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProxyServer implements Runnable {

    private Map<String, Socket> clientPool;
    private Integer simpleKeyCipher;
    private String aesKeyCipher;

    public ProxyServer() {
        Random random = new Random();
        this.simpleKeyCipher = random.nextInt(65533) + 1;
        this.clientPool = new ConcurrentHashMap<>();
    }

    public static void main(String[] args) throws Exception {

        //Port na ktorym startuje serwer
        int port = 4444;

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Started server on port " + port);
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        ProxyServer proxyServer = new ProxyServer();

        while (true) {

            System.out.println("Waiting for client...");
            //Czekaj na klienta

            // zaakceptuj połączenie
            Socket socket1 = proxyServer.acceptClient(serverSocket);
            // odczytaj listę od klienta
            ContactList cl = proxyServer.getConnectionList(serverSocket);
            // dodaj klienta do mapy clintów
            String currClient = proxyServer.addConnectedClient(cl.getListContact().get(0), socket1);
            // wyslij liste dostępnych klientów oprócz currClient
            proxyServer.sendAvailableClients(currClient);

            Socket socket2 = serverSocket.accept();



            //Thread thread = new Thread(new RunnableServers(socket));
            //thread.start();
            //Thread.sleep(SLEEP_TIME);

            // executorService.submit(new ReaderService(socket));
            // executorService.submit(new WriteService(socket));

            proxyServer.createCommunication(executorService, socket1, socket2, proxyServer.clientPool);
        }
    }
    private  String addConnectedClient(String name , Socket socket) {
        this.clientPool.put(name,socket);
        return  name;
    }

    private void sendAvailableClients(String currentClient) throws IOException {
        ContactList cl = new ContactList(new ArrayList<>(clientPool.keySet()), null);
        if (clientPool.size() > 0) {
            for (String s : clientPool.keySet()) {
                if ( ! s.equals(currentClient)) {
                    OutputStream output = clientPool.get(s).getOutputStream();
                    ObjectOutputStream pw = new ObjectOutputStream(output);
                    pw.writeObject(cl);
                }
            }

        }
    }

    private ContactList getConnectionList(ServerSocket srvSocket) {
        ContactList cl = null;
        Socket result = acceptClient(srvSocket);
        try {
            ObjectInputStream inputStream = new ObjectInputStream(result.getInputStream());
            cl = (ContactList) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return cl;
    }

    private Socket acceptClient(ServerSocket srvsocket) {
        Socket result = null;
        try {
            result = srvsocket.accept();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    public void createCommunication(ExecutorService ee, Socket input, Socket output, Map<String, Socket> clientMap) {

        if (clientMap.size() >= 2) {
            ee.submit(new TaskHandlerProxy(input, output, clientMap));
            System.out.println("messages");
            ee.submit(new TaskHandlerProxy(output, input, clientMap));
        }
    }

    @Override
    public void run() {

    }
}
