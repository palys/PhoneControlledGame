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
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.MessageFormat;


public class ViewValuesActivity extends ActionBarActivity implements SensorEventListener {

    private SensorManager sensorManager;

    private Sensor accelerometer;

    private Sensor gyroscope;

    private TextView accX;
    private TextView accY;
    private TextView accZ;

    private TextView gyroX;
    private TextView gyroY;
    private TextView gyroZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_values);

        getViews();
        initSensors();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_values, menu);
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.equals(accelerometer)) {
            displayAcceleration(event);
        } else if (event.sensor.equals(gyroscope)) {
            displayGyroscope(event);
        }
    }

    private void setValue(TextView textView, float value) {
        textView.setText(String.format("%.3f ", value));
    }

    private void displayAcceleration(SensorEvent event) {
        setValue(accX, event.values[0]);
        setValue(accY, event.values[1]);
        setValue(accZ, event.values[2]);
    }

    private void displayGyroscope(SensorEvent event) {
        setValue(gyroX, event.values[0]);
        setValue(gyroY, event.values[1]);
        setValue(gyroZ, event.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void getViews() {

        accX = (TextView) findViewById(R.id.accelerometer_x);
        accY = (TextView) findViewById(R.id.accelerometer_y);
        accZ = (TextView) findViewById(R.id.accelerometer_z);

        gyroX = (TextView) findViewById(R.id.gyroscope_x);
        gyroY = (TextView) findViewById(R.id.gyroscope_y);
        gyroZ = (TextView) findViewById(R.id.gyroscope_z);
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

}
