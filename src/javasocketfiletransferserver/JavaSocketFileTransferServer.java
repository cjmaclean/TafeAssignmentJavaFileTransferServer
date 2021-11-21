/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasocketfiletransferserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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

                BufferedReader inStream; // input stream from client
                inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                DataOutputStream outStream; // output stream to client
                outStream = new DataOutputStream(clientSocket.getOutputStream());

                while (true) { // chatting loop
                    String inLine = inStream.readLine(); // read a line from client
                    System.out.println("Received from client: " + inLine);

                    // if the client sends "quit", then stop
                    if (inLine.equalsIgnoreCase("quit")) {
                        System.out.println("Client Disconnected!!!");
                        break; // disconnect
                    }

                    String outLine = "You said '" + inLine + "'.";
                    outStream.writeBytes(outLine); // send a message to client
                    outStream.write(13); // carriage return
                    outStream.write(10); // line feed
                    outStream.flush(); // flush the stream line
                }
                inStream.close();
                outStream.close();
            }
        } catch (IOException e) {
            System.err.println("IOException occurred" + e);
        }
    } // main

}
