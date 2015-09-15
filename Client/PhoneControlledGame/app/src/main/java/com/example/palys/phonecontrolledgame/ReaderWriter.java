package com.example.palys.phonecontrolledgame;

/**
 * Created by Natala on 2015-05-10.
 */
import java.io.*;
import java.util.Arrays;

public class ReaderWriter{

    InputStream in;
    OutputStream out;
    String pin;
    String name;

    public ReaderWriter(InputStream inStream, OutputStream outStream, String pin, String name) {
        this.in = inStream;
        this.out = outStream;
        this.pin = pin;
        this.name = name;
    }

    public int waitForCommand() {
        int t;
        while(true){
            try {
                if((t = in.available()) >= 1){
                    //BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    byte[] b = new byte[t];
                    in.read(b);
                    String c = new String(b);
                    if(c.equals(Commands.AUTHENTICATE.toString())){
                        System.out.println("authenticating ...");
                        out.write(pin.getBytes());
                        return 1;
                    } else if(c.equals(Commands.REQUEST_NAME.toString())){
                        System.out.println("sending name ...");
                        out.write(name.getBytes());
                        return 2;
                    } else if(c.equals(Commands.END.toString())){
                        System.out.println("ending ...");
                        return 3;
                    } else {
                        System.out.println("returning 0");
                        return 0;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}