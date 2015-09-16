package server;

import game.Game;
import game.Move;
import game.MoveImpl;
import game.Player;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONObject;

public class ReaderWriter extends Thread {

    private InputStream in;
    private OutputStream out;
    private Game game;
    private String name;

    public ReaderWriter(InputStream inStream, OutputStream outStream, String name, Game game) {
        this.in = inStream;
        this.out = outStream;
        this.game = game;
        this.name = name;
    }

    @Override
    public void run() {
        int t;
        
        long prevTime = System.currentTimeMillis();
        
        while(true){
            try {
                if((t = in.available()) >= 1){
                    byte[] b = new byte[t];
                    in.read(b);
                    
                    long time = System.currentTimeMillis();
                    long diff = time - prevTime;
                    prevTime = time;
                    
                    String jsonString = new String(b);
                    
                    JSONObject obj = new JSONObject(jsonString);
                    JSONObject acceleration = obj.getJSONObject("Acceleration");
                    
                    double x = -acceleration.getDouble("X")/20;
                    double z = 0;
                    double y = -9.81 + (9.81 -acceleration.getDouble("Y")) / 20;
                    
                    float[] acc = new float[]{(float)x, (float)y, (float)z};
                    float duration = diff / 1000.f;
                    
                    Move m = new MoveImpl(acc, duration);
                    
                    Player p = Players.get(name);
                    
                    game.onPlayerMoved(p, m);
                    
                    System.out.println(jsonString + "\n" + "x=" + x + " y=" + y + " z=" + z);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
