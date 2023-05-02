package com.example.serveur2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private ServerSocket serverSocket;
    private Socket clientSocket;

    private DatabaseHelper patientDatabase;
    private int serverPort = 8080;
    private InputStream inputStream = null;
    private OutputStream outputStream =null ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        patientDatabase = new DatabaseHelper(this);
        // Création d'un thread pour le serveur
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Création d'un serveur socket
                    serverSocket = new ServerSocket(serverPort);

                    while (true) {
                        // Attendre une connexion client
                        clientSocket = serverSocket.accept();

                        // reçevoir les données du Client
                        String message = getClientMessage(clientSocket);
                        System.out.println(message);

                        //séparation du email et mot de passe
                        String[] loginInfo = message.split(" ");
                        String email = loginInfo[0];
                        String password = loginInfo[1];

                        // Vérifier l'existence de l'utilisateur dans la base de données
                        boolean userExists = patientDatabase.checkUser(email, password);

                        // Initialiser le flux d'écriture
                        // Envoyer une réponse au client
                        String reponse ;
                        if (userExists) {
                            reponse = "OK";
                        } else {
                            reponse = "ERROR";
                        }
                        sendMessage(clientSocket,reponse);
                        // Fermer les connexions
                        close();

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


        serverThread.start();
    }

    private String getClientMessage(Socket clientSocket) {
        // Initialiser le flux de lecture
        try {
            inputStream = clientSocket.getInputStream();
            // Lire les informations d'authentification
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);
            String message = new String(buffer, 0, bytesRead);
            return message;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        }
        private void sendMessage(Socket clientSocket , String message){
            try {
                outputStream = clientSocket.getOutputStream();
                outputStream.write(message.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        public void close() throws IOException {
            inputStream.close();
            outputStream.close();
            clientSocket.close();
        }
    }

