package com.example.serveur2;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private boolean isRunning;
    private PatientDatabase patientDatabase;
    private int serverPort = 8080;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        patientDatabase = new PatientDatabase(this);

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
                        reader = new BufferedReader(new InputStreamReader(inputStream));

                        // Initialiser le flux d'écriture
                        OutputStream outputStream = clientSocket.getOutputStream();
                        writer = new BufferedWriter(new OutputStreamWriter(outputStream));

                        // Lire les informations d'authentification
                        String loginMessage = reader.readLine();
                        String[] loginInfo = loginMessage.split(" ");
                        String email = loginInfo[0];
                        String password = loginInfo[1];

                        // Vérifier l'existence de l'utilisateur dans la base de données
                        boolean userExists = patientDatabase.checkUser(email, password);

                        // Envoyer une réponse au client
                        if (userExists) {
                            writer.write("OK\n");
                        } else {
                            writer.write("ERREUR\n");
                        }
                        writer.flush();

                        // Fermer les connexions
                        reader.close();
                        writer.close();
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
