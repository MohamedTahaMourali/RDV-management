package com.example.serveur2.view;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


import com.example.serveur2.R;

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
                        String[] messageRecu = message.split(" ");
                        String reponse="" ;
                        if (messageRecu.length == 2){
                            reponse = verifUser(messageRecu[0],messageRecu[1]);
                        }
                        else{
                            reponse = addUser(messageRecu[2],messageRecu[3],messageRecu[4],messageRecu[0],messageRecu[1]);
                        }
                        sendMessage(clientSocket,reponse);
                        // Vérifier l'existence de l'utilisateur dans la base de données

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
    String verifUser(String email,String password){
        boolean userExists = patientDatabase.checkUser(email, password);

        // Initialiser le flux d'écriture
        // Envoyer une réponse au client
        String reponse ;
        if (userExists) {
            reponse = "utiliateur existant";
        } else {
            reponse = "untilisateur n'existe pas dans la base ";
        }
        return reponse;
    }
    String addUser(String nom,String prenom,String age , String email, String password){
        if(patientDatabase.addUser(nom,prenom,age,email,password))
            return "Ajout Validé";
        return "ajout invalide";

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

