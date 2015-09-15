package client;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.*;
import java.util.Vector;

public class Client implements DiscoveryListener {

    //object used for waiting
    private static Object lock=new Object();
    //vector containing the devices discovered
    private static Vector vecDevices=new Vector();
    private static String connectionURL=null;

    public static void main(String[] args){

        Client client=new Client();

        //display local device address and name
        LocalDevice localDevice = null;
        try {
            localDevice = LocalDevice.getLocalDevice();
        } catch (BluetoothStateException e) {
            e.printStackTrace();
        }
        System.out.println("Address: "+localDevice.getBluetoothAddress());
        System.out.println("Name: "+localDevice.getFriendlyName());

        //find devices
        DiscoveryAgent agent = localDevice.getDiscoveryAgent();

        System.out.println("Starting device inquiry...");
        try {
            agent.startInquiry(DiscoveryAgent.GIAC, client);
        } catch (BluetoothStateException e) {
            e.printStackTrace();
        }

        try {
            synchronized(lock){
                lock.wait();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("Device Inquiry Completed. ");

        //print all devices in vecDevices
        int deviceCount=vecDevices.size();

        if(deviceCount <= 0){
            System.out.println("No Devices Found .");
            System.exit(0);
        }
        else{
            //print bluetooth device addresses and names in the format [ No. address (name) ]
            System.out.println("Bluetooth Devices: ");
            for (int i = 0; i <deviceCount; i++) {
                RemoteDevice remoteDevice=(RemoteDevice)vecDevices.elementAt(i);
                try {
                    System.out.println((i+1)+". "+remoteDevice.getBluetoothAddress()+" ("+remoteDevice.getFriendlyName(true)+")");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.print("Choose Device index: ");
        BufferedReader bReader=new BufferedReader(new InputStreamReader(System.in));

        String chosenIndex= null;
        try {
            chosenIndex = bReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int index=Integer.parseInt(chosenIndex.trim());

        //check for spp service
        RemoteDevice remoteDevice=(RemoteDevice)vecDevices.elementAt(index-1);
        UUID[] uuidSet = new UUID[1];
        uuidSet[0]=new UUID("1101",true);
        int[] attrSet={0x1101};

        System.out.println("\nSearching for service...");
        try {
            agent.searchServices(attrSet,uuidSet,remoteDevice,client);
        } catch (BluetoothStateException e) {
            e.printStackTrace();
        }

        try {
            synchronized(lock){
                lock.wait();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(connectionURL==null){
            System.out.println("Device does not support Simple SPP Service.");
            System.exit(0);
        }


        //connect to the server and send a line of text
        try{
            StreamConnection streamConnection=(StreamConnection)Connector.open(connectionURL);

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
            //e.printStackTrace();
        }

    }

    //methods of DiscoveryListener
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        //add the device to the vector
        if(!vecDevices.contains(btDevice)){
            vecDevices.addElement(btDevice);
        }
    }

    //implement this method since services are not being discovered
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        System.out.println("Inside Service Discovered");
        if(servRecord!=null && servRecord.length>0){
            connectionURL=servRecord[0].getConnectionURL(0,false);
            for (int i = 0; i < servRecord.length; i++) {

                DataElement serviceName = servRecord[i].getAttributeValue(0x0100);
                System.out.println("service " + serviceName.getValue() + " found ");
            }
/*
        	for (int i = 0; i < servRecord.length; i++) {
                String url = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
               // System.out.println("Connection URL="+url);
                if (url == null) {
                    continue;
                }
               // serviceFound.add(url);
                DataElement serviceName = servRecord[i].getAttributeValue(0x0100);
                if (serviceName != null) {
					System.out.println("service " + serviceName.getValue() + " found " + url);
	               // if(serviceName.getValue().equals("OBEX Object Push")){
	                   // sendMessageToDevice(url);
	                //}

                } else {
                    System.out.println("service found " + url);
                }
            }*/
        }
        synchronized(lock){
            lock.notify();
        }
    }

    //implement this method since services are not being discovered
    public void serviceSearchCompleted(int transID, int respCode) {
        synchronized(lock){
            lock.notify();
        }
    }


    public void inquiryCompleted(int discType) {
        synchronized(lock){
            lock.notify();
        }

    }//end method

}
