/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasocketfiletransferserver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author 30039802 Caspian Maclean
 *
 * Question 7 – JMC needs a way to transfer data files (mainly CSV) using
 * sockets in a client-server application.
 *
 * This is the socket program.
 *
 * Socket connection code and user input-reading code based on provided example
 * "ClientServerDemo"
 */
public class JavaSocketFileTransferServer {

    public static void main(String[] args) {
        try ( ServerSocket serverSocket = new ServerSocket(1234)) { // server is on port 1234
            System.out.println("Server is listening on port #" + serverSocket.getLocalPort());
            try ( Socket clientSocket = serverSocket.accept()) { // wait, listen and accept connection
                String clientHostName = clientSocket.getInetAddress().getHostName(); // client name
                int clientPortNumber = clientSocket.getLocalPort(); // port used
                System.out.println("Connected from " + clientHostName + " on #" + clientPortNumber);

                DataInputStream inStream;
                inStream = new DataInputStream(clientSocket.getInputStream());

                DataOutputStream outStream; // output stream to client
                outStream = new DataOutputStream(clientSocket.getOutputStream());

                String fileName = "";
                boolean commandUpload = false;

                try {
                    commandUpload = inStream.readBoolean();
                    fileName = inStream.readUTF();
                    System.out.println("upload: " + commandUpload);
                    System.out.println("file: " + fileName);

                } catch (IOException ex) {
                    System.out.println(ex);
                    System.exit(1);
                }

                if (commandUpload) {
                    System.out.println("Trying receiving <upload>");
                    FileOutputStream fileOutputStream = new FileOutputStream(fileName);
                    int count = 0;
                    byte[] buffer = new byte[8];
                    try {
                        while ((count = inStream.read(buffer)) > 0) {
                            fileOutputStream.write(buffer, 0, count);
                        }
                    } catch (IOException ex) {
                        System.out.println(ex);
                        System.exit(1);
                    }
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } else {
                    System.out.println("Trying sending <download>");
                    int count;
                    byte[] buffer = new byte[1024];
                    try {
                        FileInputStream fileInputStream = new FileInputStream(fileName);
                        try {
                            while ((count = fileInputStream.read(buffer)) > 0) {
                                outStream.write(buffer, 0, count);
                            }
                        } catch (IOException ex) {
                            System.out.println(ex);
                            System.exit(1);
                        }
                    } catch (FileNotFoundException ex) {
                        // Show message that the file isn't found
                        System.out.println(ex);
                        System.exit(1);
                    }
                }

                inStream.close();
                outStream.close();
            }
        } catch (IOException e) {
            System.err.println("IOException occurred" + e);
        }
    } // main

}
