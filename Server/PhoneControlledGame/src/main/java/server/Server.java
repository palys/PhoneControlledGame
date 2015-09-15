package server;

import com.sun.org.apache.xpath.internal.SourceTree;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import java.util.concurrent.*;

public class Server{

    private static final int TIMEOUT = 500;
    private static final int PLAYER1 = 1;
    private static final int PLAYER2 = 2;

    public void registerPlayer(){

    }

    public static void main(String[] args){

        System.out.println("Launching ...");

        try {
            LocalDevice device = LocalDevice.getLocalDevice();
            System.out.println("Device address: " + device.getBluetoothAddress() + " | Device name: " + device.getFriendlyName());
        } catch (BluetoothStateException e) {
            e.printStackTrace();
        }

        Player player1 = null;
        Player player2;
        ExecutorService executor = Executors.newFixedThreadPool(2);

        System.out.println("Waiting for player 1 to connect ...");

        FutureTask<Player> future1 =
                new FutureTask<Player>(new Callable<Player>() {
                    public Player call() {
                        SPPServer server = new SPPServer();
                        return server.getPlayer(PLAYER1);
                    }});
        executor.execute(future1);
        try {
            player1 = future1.get(TIMEOUT,TimeUnit.SECONDS);
            System.out.println("Player 1 connected successfully");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
