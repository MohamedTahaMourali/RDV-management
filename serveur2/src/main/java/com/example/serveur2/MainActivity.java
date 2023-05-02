package com.example.serveur2;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private boolean isRunning;
    private DatabaseHelper patientDatabase;
    private int serverPort = 8080;


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
                    Log.d("SERVER", "Server started on port " + serverPort);

                    while (isRunning) {
                        // Attendre une connexion client
                        clientSocket = serverSocket.accept();
                        Log.d("SERVER", "Client connected from " + clientSocket.getInetAddress().getHostName());

                        // Initialiser le flux de lecture
                        InputStream inputStream = clientSocket.getInputStream();

                        // Initialiser le flux d'écriture
                        OutputStream outputStream = clientSocket.getOutputStream();



                        // Lire les informations d'authentification
                        byte[] buffer = new byte[1024];
                        int bytesRead = inputStream.read(buffer);
                        String message = new String(buffer, 0, bytesRead);
                        System.out.println(message);

                        //séparation du email et mot de passe
                        String[] loginInfo = message.split(" ");
                        String email = loginInfo[0];
                        String password = loginInfo[1];
                        System.out.println(email +" "+password);
                        // Vérifier l'existence de l'utilisateur dans la base de données
                        boolean userExists = patientDatabase.checkUser(email, password);

                        // Envoyer une réponse au client
                        if (userExists) {
                            System.out.println("existe");
                            outputStream.write("OK".getBytes());

                        } else {
                            System.out.println("pas existe");
                            outputStream.write("ERROR".getBytes());
                        }
                        //writer.flush();

                        // Fermer les connexions

                        outputStream.close();
                        clientSocket.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        isRunning = true;
        serverThread.start();
    }


}
