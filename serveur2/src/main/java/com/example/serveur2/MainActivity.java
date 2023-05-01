package com.example.serveur2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RdvDataBase rdvDataBase, mDatabaseHelper;
    private PatientDatabase patientDatabase;
    private String serverIP;
    private int serverPort;

    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mUserList;

    public void init() {
        rdvDataBase = new RdvDataBase(this);
        serverIP = "10.0.2.15";
        serverPort = 8080;
        mDatabaseHelper = new RdvDataBase(this);
        mUserList = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mUserList);
        patientDatabase = new PatientDatabase(this);
    }

    void verifUser() {
        ServerThread s = new ServerThread();
        s.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        verifUser();
    }

    class ServerThread extends Thread {
        private ServerSocket serverSocket;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(serverPort);

                while (true) {
                    System.out.println("waiting ");
                    final Socket clientSocket = serverSocket.accept();
                    System.out.println("connected");
                    // Get the input and output streams from the socket
                    InputStream inputStream = clientSocket.getInputStream();
                    OutputStream outputStream = clientSocket.getOutputStream();

                    // Create a reader and writer for the streams
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                    // Read the login credentials from the client
                    String message = reader.readLine();
                    if (message.startsWith("CONNEXION")) {
                        // Récupération de l'adresse e-mail et du mot de passe de l'utilisateur
                        String[] elements = message.split("\n");
                        String email = elements[1];
                        String motDePasse = elements[2];
                        System.out.println(email + " " + motDePasse);

                        // Vérification de l'existence de l'utilisateur dans la base de données des patients
                        boolean utilisateurExiste = patientDatabase.checkUser(email, motDePasse);
                        System.out.println(utilisateurExiste);
                        // Envoi de la réponse au client
                        DataOutputStream dataOutputStream = null;
                        try {
                            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        if (utilisateurExiste) {
                            try {
                                dataOutputStream.writeUTF("OK");
                                System.out.println("OK");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            try {
                                dataOutputStream.writeUTF("ERREUR");
                                System.out.println("Erreur");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else if (message.startsWith("AJOUT_RDV")) {
                        // Récupération des données du rendez-vous à ajouter
                        String[] elements = message.split("\n");
                        String nomPatient = elements[1];
                        String dateRdv = elements[2];
                        String heureRdv = elements[3];
                        System.out.println(nomPatient + " " + dateRdv + " " + heureRdv);

                        // Ajout du rendez-vous à la base de données des rendez-vous
                        boolean rdvAjoute = rdvDataBase.addRdv(nomPatient, dateRdv, heureRdv);
                        System.out.println(rdvAjoute);

                        // Envoi de la réponse au client
                        DataOutputStream dataOutputStream = null;
                        try {
                            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        if (rdvAjoute) {
                            try {
                                dataOutputStream.writeUTF("OK");
                                System.out.println("OK");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            try {
                                dataOutputStream.writeUTF("ERREUR");
                                System.out.println("Erreur");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
