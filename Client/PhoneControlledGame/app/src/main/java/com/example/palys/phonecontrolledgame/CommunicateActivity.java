package com.example.palys.phonecontrolledgame;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;


public class CommunicateActivity extends ActionBarActivity implements SensorEventListener {

    public static final String PIN = "pin";

    private String pin;

    private SensorManager sensorManager;

    private Sensor accelerometer;

    private Sensor gyroscope;

    private float[] currentAcceleration;

    private float[] currentGyro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicate);

        initSensors();
        currentAcceleration = new float[3];
        currentGyro = new float[3];
        
        pin = getIntent().getStringExtra(PIN);

        connect(pin);
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

    private void connect(String pin) {
        //TODO
    }

    private void send(String s) {
        //TODO
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
