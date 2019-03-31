package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientListner implements Runnable {

    private int port;
    private boolean isRunning = true;
    public ServerSocket serverSocket;

    public ClientListner(int port) {
        this.port = port;
    }


    public void run() {
        openServerSocket();
        while (isRunning) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if (isRunning) {
                    System.out.println("Server Stopped.");
                    return;
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }
            new Thread(new SenderReceiver(clientSocket)).start();
        }
        System.out.println("Server Stopped.");
    }

    public synchronized void stop() {
        this.isRunning = false;
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.port, 20);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port 9000", e);
        }

    }
}
