package com.sda.server;

import com.sda.client.ContactList;

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

    private Integer simpleKeyCipher;
    private String aesKeyCipher;
    private static final Map<String, Socket> clientPool = new ConcurrentHashMap<>();;
    private static final ProxyHelper queue = new ProxyHelper();

    public ProxyServer() {
        Random random = new Random();
        this.simpleKeyCipher = random.nextInt(65533) + 1;
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
            // ?????????????
            //Socket socket1 = proxyServer.acceptClient(serverSocket);
            // odczytaj listę od klienta
            queue.push(proxyServer.getConnectionList(serverSocket));

            //ContactList cl = proxyServer.getConnectionList(serverSocket);
            // dodaj klienta do mapy clintów
            //String currClient = proxyServer.addConnectedClient(cl.getListContact().get(0), socket1);
            // wyslij liste dostępnych klientów oprócz currClient
            //proxyServer.sendAvailableClients(currClient);
            executorService.submit(new ProxyServer());

            Socket socket2 = serverSocket.accept();
            /*
            oczekuj na akceptacje przyjęcia połączenia
            jeśli się pojawi to przyjmij pobierz obiekt zapisz na stosie
            następnie w nowym wątku  weź obiekt ze stosu i wyślij do odpowiednich adresatów

             */

            //Thread thread = new Thread(new RunnableServers(socket));
            //thread.start();
            //Thread.sleep(SLEEP_TIME);

            // executorService.submit(new ReaderService(socket));
            // executorService.submit(new WriteService(socket));

            proxyServer.createCommunication(executorService, proxyServer.clientPool);
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
            clientPool.put(cl.getListContact().get(0), result);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return cl;
    }

    private Socket acceptClient(ServerSocket srvsocket) {
        Socket result = null;
        try {
            if ( srvsocket != null | ! srvsocket.isClosed()  ) {
                result = srvsocket.accept();
            } else
                throw  new NullPointerException("Not socket is settup");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public void createCommunication(ExecutorService ee,  Map<String, Socket> clientMap) {

        if (clientMap.size() >= 2) {
            ee.submit(new TaskHandlerProxy( clientMap));
            System.out.println("messages");
            ee.submit(new TaskHandlerProxy( clientMap));
        }
    }
    private  void sendObject(ExecutorService ee) {

        //ee.submit(new TaskHandlerProxy(null, ));
    }

    private  ContactList receiveObject() {
        return null;
    }

    @Override
    public void run() {
        ContactList cl = null;
        while (true) {
            if ( ! queue.isEmpty()) {
                cl = queue.pop();
            }
            for (String s : clientPool.keySet()) {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(clientPool.get(s).getOutputStream());
                    oos.writeObject(cl);
                    oos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
