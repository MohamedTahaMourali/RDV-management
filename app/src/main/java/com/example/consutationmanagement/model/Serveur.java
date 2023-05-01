package com.example.consutationmanagement.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.consutationmanagement.controller.RdvDataBase;

import java.net.*;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Server {

    private ServerSocket serverSocket;

    public void run(RendezVous rdv) {
        try {
            serverSocket = new ServerSocket(4444);
            while (true) {
                System.out.println("Waiting for client connection...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
                handleClient(clientSocket, rdv);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket, RendezVous rdv) throws IOException {

        LocalDate requestedDate = rdv.getDateRdv();
        LocalDateTime requestedTime = rdv.getTempsRdv();
        LocalDate lastAppointmentDate = getLastAppointmentDate();
        


        if (lastAppointmentDate == null || requestedTime - lastAppointmentDate >= 15 * 60 * 1000) {

            saveAppointment(firstName, lastName, requestedDate);
        } else {

        }
        clientSocket.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private LocalDate getLastAppointmentDate() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        LocalDate lastAppointmentDate = null;

        RdvDataBase rd = new RdvDataBase();

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:appointments.db");

            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT MAX(datetime) FROM appointments");

            if (rs.next()) {
                lastAppointmentDate = rd.getLastDate()
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return lastAppointmentDate;
    }

    private void saveAppointment(String firstName, String lastName, Date date) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:appointments.db");

            stmt = conn.prepareStatement("INSERT INTO appointments (first_name, last_name, datetime) VALUES (?, ?, ?)");
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setTimestamp(3, new Timestamp(date.getTime()));
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

