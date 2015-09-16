package server;

import game.Game;

import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class SPPServer{

    private Phone player;
    private int authenticationNumber;
    
    private Game game;

    public SPPServer(Game game) {
    	this.game = game;
        this.player = new Phone();
        Random r = new Random();
        authenticationNumber = r.nextInt(9000) + 1000;
    }

    public Phone getPlayer(int playerNumber) {
        setPlayerNumber(playerNumber);
        StreamConnection connection = waitForConnection();
        setInputAndOutputStream(connection);
        waitForAck();
        authenticate();
        setPlayerName();
        sendACK();
        return player;
    }

    public void setPlayerNumber(int playerNumber) {
        player.setPlayerNumber(playerNumber);
    }

    private StreamConnection waitForConnection(){
        try {
            //Create a UUID for SPP
            UUID uuid = new UUID("1101", true);

            //Create the service url
            String connectionString = "btspp://localhost:" + uuid + ";name=Simple SPP Server";

            //open server url
            StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier) Connector.open(connectionString);

            //Wait for client connection
            System.out.println("\n *** Server Started. Waiting for client to connect...");
            System.out.println(" *** Authentication PIN number: " + authenticationNumber);

            StreamConnection conn = streamConnNotifier.acceptAndOpen();

            return conn;
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void setInputAndOutputStream(StreamConnection connection) {

        System.out.println(" --- setting input & output");
        InputStream inStream = null;
        OutputStream outStream = null;
        try {
            inStream = connection.openInputStream();
            outStream = connection.openOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.setInputStream(inStream);
        player.setOutputStream(outStream);

    }

    private void waitForAck(){
        System.out.println(" --- ack waiting...");
        int size;
        boolean ack = false;

        while(!ack){
            try {
                if((size = player.getInputStream().available()) >= 1){
                    byte[] b = new byte[size];
                    player.getInputStream().read(b);
                    System.out.println(" --- ack data: " + new String(b));
                    String a = new String(b);
                    if(Commands.ACK.toString().equals(a)){
                        ack = true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void authenticate() {
        System.out.println(" --- authenticating...");
        int size;
        boolean authenticated = false;

        try {
            player.getOutputStream().write(Commands.AUTHENTICATE.getBytes());

            while(!authenticated){
                try {
                    if((size = player.getInputStream().available()) >= 1){
                        byte[] b = new byte[size];
                        player.getInputStream().read(b);
                        System.out.println(" --- auth data: " + new String(b));
                        int answer = Integer.parseInt(new String(b));
                        if(answer == authenticationNumber){
                            player.setAuthenticated(true);
                            authenticated = true;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setPlayerName() {
        System.out.println(" --- setting player name...");
        int size;
        boolean nameSynchronized = false;

        try{
            player.getOutputStream().write(Commands.REQUEST_NAME.getBytes());

            while(!nameSynchronized){
                try {
                    if((size = player.getInputStream().available()) >= 1){
                        byte[] b = new byte[size];
                        player.getInputStream().read(b);
                        System.out.println(" --- player data: " + new String(b));
                        String name = new String(b);
                        player.setPlayerName(name);
                        nameSynchronized = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendACK(){
        try {
            player.getOutputStream().write(Commands.END.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        (new ReaderWriter(player.getInputStream(),player.getOutputStream(), player.getPlayerName(), game)).start();
    }
}
