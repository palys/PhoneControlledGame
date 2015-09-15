package server;

import game.InputServer;
import game.Player;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;

import maze.Maze;

public class Server{

    private static final int TIMEOUT = 500;
    private static final int PLAYER1 = 1;
    private static final int PLAYER2 = 2;

    public void registerPlayer(){

    }

    public static void main(String[] args){

        System.out.println("Launching ...");
        
    	InputServer sender = new Sender();
		
		Maze maze = new Maze(sender);
		maze.startGame();

        try {
            LocalDevice device = LocalDevice.getLocalDevice();
            System.out.println("Device address: " + device.getBluetoothAddress() + " | Device name: " + device.getFriendlyName());
        } catch (BluetoothStateException e) {
            e.printStackTrace();
        }

        Phone player1 = null;
        Phone player2;
        ExecutorService executor = Executors.newFixedThreadPool(2);

        System.out.println("Waiting for player 1 to connect ...");

        FutureTask<Phone> future1 =
                new FutureTask<Phone>(new Callable<Phone>() {
                    public Phone call() {
                        SPPServer server = new SPPServer();
                        return server.getPlayer(PLAYER1);
                    }});
        executor.execute(future1);
        try {
        	
            player1 = future1.get(TIMEOUT,TimeUnit.SECONDS);
            final Phone phone = player1;
            System.out.println("Player 1 connected successfully");
            Player player = new Player() {
				
				@Override
				public String getName() {
					
					return phone.getPlayerName();
				}
			};
			maze.onPlayerJoined(player);
			
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

//        try {
//            Thread.sleep(50000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        //waiting for player2 to connect
//        FutureTask<Player> future2 =
//                new FutureTask<Player>(new Callable<Player>() {
//                    public Player call() {
//                        SPPServer server = new SPPServer();
//                        return server.getPlayer(PLAYER2);
//                    }});
//        executor.execute(future2);
//        try {
//             player2 = future1.get(TIMEOUT,TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (TimeoutException e) {
//            e.printStackTrace();
//        }
        
    }
    
}
