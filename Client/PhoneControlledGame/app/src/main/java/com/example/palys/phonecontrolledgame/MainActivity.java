package com.example.palys.phonecontrolledgame;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private Button viewValuesButton = null;

    private Button connectButton = null;

    private EditText pinEdit = null;

    private EditText nameEdit = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getViews();
        attachListeners();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void getViews() {
        viewValuesButton = (Button) findViewById(R.id.view_values_button);
        connectButton = (Button) findViewById(R.id.connect_button);
        pinEdit = (EditText) findViewById(R.id.pin_edit);
        nameEdit = (EditText) findViewById(R.id.name_edit);
    }

    private void attachListeners() {
        viewValuesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewValuesActivity.class);
                startActivity(intent);
            }
        });

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CommunicateActivity.class);
                String pin = pinEdit.getText().toString();
                String name = nameEdit.getText().toString();
                intent.putExtra(CommunicateActivity.PIN, pin);
                intent.putExtra(CommunicateActivity.NAME, name);
                startActivity(intent);
            }
        });
    }
}
