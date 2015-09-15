package server;

import java.io.*;
import java.util.Arrays;

import org.json.JSONObject;

public class ReaderWriter extends Thread {

    InputStream in;
    OutputStream out;

    public ReaderWriter(InputStream inStream, OutputStream outStream, String name) {
        this.in = inStream;
        this.out = outStream;
    }

    @Override
    public void run() {
        int t;
        while(true){
            try {
                if((t = in.available()) >= 1){
                    byte[] b = new byte[t];
                    in.read(b);
                    String jsonString = new String(b);
                    
                    JSONObject obj = new JSONObject(jsonString);
                    JSONObject acceleration = obj.getJSONObject("Acceleration");
                    
                    double x = acceleration.getDouble("X");
                    double y = acceleration.getDouble("Y");
                    double z = acceleration.getDouble("Z");
                    
                    
                    System.out.println(jsonString + "/n" + "x=" + x + " y=" + y + " z=" + z);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
