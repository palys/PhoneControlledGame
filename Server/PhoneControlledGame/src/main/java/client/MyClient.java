package client;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.*;

public class MyClient {

    private static String connectionURL;

    public static void main(String[] args){

        connectionURL = "btspp://B4749FD6CE10:1;authenticate=false;encrypt=false;master=false";
        try{
            StreamConnection streamConnection=(StreamConnection) Connector.open(connectionURL);

            //send string
            OutputStream outStream=streamConnection.openOutputStream();
            PrintWriter pWriter=new PrintWriter(new OutputStreamWriter(outStream));
            pWriter.write("Test String from SPP Client\r\n");
            pWriter.flush();


            //read response
            InputStream inStream=streamConnection.openInputStream();
            BufferedReader bReader2=new BufferedReader(new InputStreamReader(inStream));
            String lineRead=bReader2.readLine();
            System.out.println(lineRead);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
