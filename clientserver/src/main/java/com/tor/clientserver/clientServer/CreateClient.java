package com.tor.clientserver.clientServer;

import com.tor.clientserver.model.Client;
import com.tor.clientserver.model.ClientNeighbours;
import com.tor.clientserver.model.Connect;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.*;

public class CreateClient {

    static final String URL_CREATE_CLIENT = "http://localhost:9000/";
    private ClientNeighbours clientNeighbours;

    private static String host = "localhost";
    private static int port = 9000;

    private Socket socket;

    public ResponseEntity<ClientNeighbours> createClient(String ip, String action) throws MalformedURLException {

        Client client = new Client(ip, action);

        RestTemplate restTemplate = new RestTemplate();

        // Data attached to the request.
        HttpEntity<Client> requestBody = new HttpEntity<>(client);

        // Send request with POST method.
        ResponseEntity<ClientNeighbours> result
                = restTemplate.postForEntity(URL_CREATE_CLIENT, requestBody, ClientNeighbours.class);

        System.out.println("Status code:" + result.getStatusCode());
        // Code = 200.
        if (result.getStatusCode() == HttpStatus.OK) {
            this.clientNeighbours = result.getBody();
            System.out.println("Get neighbours: " + clientNeighbours);
        }

        return result;
    }

    public void startAskingClient(String ip) throws IOException {
        String line = "";
        String neighbours;
        BufferedReader br;

        System.out.println("Client Address : " + ip);


        try {
            socket = new Socket(host, port);
            System.out.println("Connected");

            br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Enter action(Enter or Leave): ");
            line = br.readLine();


            createClient(ip, line);


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!line.equals("Leave")) {
            try {

                br = new BufferedReader(new InputStreamReader(System.in));


                System.out.println("Enter neighbour to connect with: " + clientNeighbours.getIps());
                line = br.readLine();

                if (!line.equals("Leave")) {
                    connectToNeighbour(line, ip);
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Socket read Error");
            }
        }

        System.out.println("Closing connection");

        // close connection
        socket.close();

    }


    public void connectToNeighbour(String connectToPort, String myPort) {
        try (Socket socket = new Socket(host, Integer.parseInt(connectToPort))) {
            System.out.println("Connected with neibs");
            RestTemplate restTemplate = new RestTemplate();

            Connect connect = new Connect();
            connect.setLetsConnect(true);
            connect.setPort(myPort);

            // Data attached to the request.
            HttpEntity<Connect> requestBody = new HttpEntity<>(connect);

            // Send request with POST method.
            ResponseEntity<Connect> result
                    = restTemplate.postForEntity("http://localhost:" + connectToPort, requestBody, Connect.class);

            // Code = 200.
            if (result.getStatusCode() == HttpStatus.OK) {
                Connect connect2 = result.getBody();


                if (connect2 != null) {
                    System.out.println("Connected with: http://localhost:" + connectToPort + " " + connect2.getLetsConnect());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
