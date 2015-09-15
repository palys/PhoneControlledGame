package com.example.palys.phonecontrolledgame;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class CommunicateActivity extends ActionBarActivity implements SensorEventListener {

    public static final String PIN = "pin";
    public static final String NAME = "name";

    private static final int TIMEOUT = 60;

    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private String pin;
    private String name;

    private boolean playerLinked = false;

    private SensorManager sensorManager;

    private Sensor accelerometer;

    private Sensor gyroscope;

    private float[] currentAcceleration;

    private float[] currentGyro;

    private BluetoothAdapter btAdapter;

    private BluetoothDevice device;

    private BroadcastReceiver mReceiver;

    private EditText pinEditText;

    private Button sendPinButton;

    private OutputStream os;
    private InputStream is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicate);
//        pinEditText = (EditText) findViewById(R.id.pinEditText);
//        sendPinButton = (Button) findViewById(R.id.sendPinButton);
//        sendPinButton.setText("Waiting...");
//        sendPinButton.setEnabled(false);
//        setSendPinButtonListener();
        initSensors();
        currentAcceleration = new float[3];
        currentGyro = new float[3];

        device = null;
        Log.i("L","start");
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.i("L","after default adapter");

//        if (!btAdapter.isEnabled()){
//            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBT, 0xDEADBEEF);
//        }

        btAdapter.startDiscovery();

        Log.i("L","discovery started");

        // Create a BroadcastReceiver for ACTION_FOUND
        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Log.i("L","onReceive");
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    Log.i("device",device.getAddress());
                    Log.i("device",device.getName());

                }
                Log.i("L","discovered");
                btAdapter.cancelDiscovery();
                pin = getIntent().getStringExtra(PIN);
                name = getIntent().getStringExtra(NAME);
                connect(pin, name);
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy


    }

    private void enableSendPinButton() {
        sendPinButton.setText("Send PIN");
        sendPinButton.setEnabled(true);
    }

    private void setSendPinButtonListener() {
        sendPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin = pinEditText.getText().toString();

                try {
                    os.write(pin.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initSensors() {

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.w("SENSORS", "Cannot find accelerometer");
        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
            gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.w("SENSORS", "Cannot find gyroscope");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_communicate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void connect(String pin, String name) {
        Log.i("BT", "connect");
        Log.i("PIN", "pin is " + pin);
        try {
            BluetoothSocket btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            Log.i("BT","socket opened");
            btSocket.connect();
            Log.i("BT","device connected");
            os = btSocket.getOutputStream();
            is = btSocket.getInputStream();

            final ReaderWriter rw = new ReaderWriter(is,os,pin, name);

            os.write(Commands.ACK.getBytes());
            Log.i("ACK", "ack");

            ExecutorService executor = Executors.newFixedThreadPool(4);

            int t;
            while((t = rw.waitForCommand()) != 3){
                Log.i("LOOP", "actual number = " + t);
            }
            playerLinked = true;

//            boolean flag = false;
//            int t;
//            while(!flag){
//                if((t = is.available()) >= 1){
//                    flag = true;
//                    Log.i("BT", "ack");
//                    byte[] b = new byte[t];
//                    is.read(b);
//                    //String answer = ByteBuffer.wrap(b).toString();
//                    Log.i("BT", new String(b));
//                }
//            }
//            String s = "Hello";
//            os.write(s.getBytes());
            //BufferedReader bReader=new BufferedReader(new InputStreamReader(is));
            //String lineRead=bReader.readLine();
            //Log.i("BT","received " + lineRead);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(String s) {
        if(playerLinked) {
            Log.i("SEND", "sending " + s);
            try {
                os.write(s.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.equals(accelerometer)) {
            System.arraycopy(event.values, 0, currentAcceleration, 0, 3);
        } else if (event.sensor.equals(gyroscope)) {
            System.arraycopy(event.values, 0, currentGyro, 0, 3);
        }

        try {
            String json = createJSON();
            send(json);
        } catch (JSONException e) {
            Log.w("COMMUNICATION", "Error during creation of json", e);
        }
    }

    private void putXYZ(JSONObject obj, float[] f) throws JSONException {
        obj.put("X", f[0]);
        obj.put("Y", f[1]);
        obj.put("Z", f[2]);
    }

    private String createJSON() throws JSONException {
        JSONObject obj = new JSONObject();

        JSONObject accObj = new JSONObject();
        putXYZ(accObj, currentAcceleration);

        JSONObject gyroObject = new JSONObject();
        putXYZ(gyroObject, currentGyro);

        obj.put("Acceleration", accObj);
        obj.put("Gyroscope", gyroObject);

        return obj.toString();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}


