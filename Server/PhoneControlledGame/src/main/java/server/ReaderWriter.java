package server;

import java.io.*;
import java.util.Arrays;

public class ReaderWriter extends Thread {

    InputStream in;
    OutputStream out;

    public ReaderWriter(InputStream inStream, OutputStream outStream) {
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
                    System.out.println(new String(b));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
