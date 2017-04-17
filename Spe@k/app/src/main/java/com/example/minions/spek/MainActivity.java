package com.example.minions.spek;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends Activity {
    private static final String TAG = "DeviceListActivity";
    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // String for MAC address
    private static String address;
    final int handlerState = 0;                        //used to identify handler message
    ImageButton imageButton;
    Handler bluetoothIn;

    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothSocket bluetoothSocket = null;
//    private StringBuilder recDataString = new StringBuilder();
    private ConnectedThread myConnectedThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "on create 1");
        setContentView(R.layout.activity_main);
        Log.i(TAG, "on create 2");
        //Link the buttons and textViews to respective views
//        btnOn = (Button) findViewById(R.id.buttonOn);
//        btnOff = (Button) findViewById(R.id.buttonOff);
//        txtString = (TextView) findViewById(R.id.txtString);
//        txtStringLength = (TextView) findViewById(R.id.testView1);
//        sensorView0 = (TextView) findViewById(R.id.sensorView0);
//        sensorView1 = (TextView) findViewById(R.id.sensorView1);
//        sensorView2 = (TextView) findViewById(R.id.sensorView2);
//        sensorView3 = (TextView) findViewById(R.id.sensorView3);
        Log.i(TAG, "on create 3");
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {                                     //if message is what we want
                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
//                    recDataString.append(readMessage);                                      //keep appending to string until ~
//                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
//                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
//                        String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
//                        txtString.setText("Data Received = " + dataInPrint);
//                        int dataLength = dataInPrint.length();                          //get length of data received
//                        txtStringLength.setText("String Length = " + String.valueOf(dataLength));
//
//                        if (recDataString.charAt(0) == '#')                             //if it starts with # we know it is what we are looking for
//                        {
//                            String sensor0 = recDataString.substring(1, 5);             //get sensor value from string between indices 1-5
//                            String sensor1 = recDataString.substring(6, 10);            //same again...
//                            String sensor2 = recDataString.substring(11, 15);
//                            String sensor3 = recDataString.substring(16, 20);
//
//                            sensorView0.setText(" Sensor 0 Voltage = " + sensor0 + "V");    //update the textviews with sensor values
//                            sensorView1.setText(" Sensor 1 Voltage = " + sensor1 + "V");
//                            sensorView2.setText(" Sensor 2 Voltage = " + sensor2 + "V");
//                            sensorView3.setText(" Sensor 3 Voltage = " + sensor3 + "V");
//                        }
//                        recDataString.delete(0, recDataString.length());                    //clear all string data
//                        // strIncom =" ";
//                        dataInPrint = " ";
//                    }
                }
            }
        };
        Log.i(TAG, "on create 4");
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();
        Log.i(TAG, "on create 5");
        // Set up onClick listeners for buttons to send 1 or 0 to turn on/off LED

        imageButton = (ImageButton) findViewById(R.id.listenButton);
        Log.i(TAG, "on create 6");
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.listenButton) {
                    askToSpeak();
                }
            }


        });
        Log.i(TAG, "on create 7");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.i(TAG, "on activity results 1");
        super.onActivityResult(requestCode, resultCode, intent);
        Log.i(TAG, "on activity results 1");
        switch (requestCode) {
            case 100:
                Log.i(TAG, "case");
                Log.i(TAG, "Compare " + resultCode + "  " + RESULT_OK);
                if (resultCode == RESULT_OK && intent != null) {
                    Log.i(TAG, "on activity results 2");
                    ArrayList<String> commands = new ArrayList<>();
                    if (intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) != null) {
                        commands = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        Log.i(TAG, Integer.toString(commands.size()));
                    } else {
                        Log.i(TAG, "null u fool");
                    }
                    Log.i(TAG, "on activity results 3");
                    Log.i(TAG, commands.get(0));
//                   for (int i = 0; i < commands.size(); i++) {
//                        Log.d(TAG, commands.get(i));
//                    }
                    if (commands.get(0).equals("test")) {
                        Log.i(TAG, "test 1");
                        myConnectedThread.write("x");
                        Log.i(TAG, "test 2");
                    } else {
                        Log.i(TAG, "not test");
                    }
                }
                break;
        }
    }

    public void askToSpeak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Waiting !!!");

        try {
            startActivityForResult(intent, 100);

        } catch (ActivityNotFoundException x) {
            Toast.makeText(MainActivity.this, "Sorry Device doesn't Support", Toast.LENGTH_LONG).show();
        }
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);

    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();

        address = intent.getStringExtra(DeviceListActivity.EDADDRESS);

        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);

        try {
            bluetoothSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }
        try {
            bluetoothSocket.connect();
        } catch (IOException e) {
            try {
                bluetoothSocket.close();
            } catch (IOException e2) {
            }
        }
        myConnectedThread = new ConnectedThread(bluetoothSocket);
        myConnectedThread.start();

        myConnectedThread.write("Test Reading");


    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            bluetoothSocket.close();
        } catch (IOException e2) {
        }

    }

    private void checkBTState() {
        if (bluetoothAdapter == null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (bluetoothAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream myBluetoothInStream;
        private final OutputStream myBluetoothOutStream;

        public ConnectedThread(BluetoothSocket bluetoothSocket) {
            InputStream tempInput = null;
            OutputStream tempOutput = null;

            try {
                tempInput = bluetoothSocket.getInputStream();
                tempOutput = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
            }

            myBluetoothInStream = tempInput;
            myBluetoothOutStream = tempOutput;
        }

        public void run() {
            byte[] bufferByte = new byte[256];
            int bytes;

            while (true) {
                try {
                    bytes = myBluetoothInStream.read(bufferByte);
                    String readMessage = new String(bufferByte, 0, bytes);
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }


        public void write(String input) {
            byte[] messageBuffer = input.getBytes();
            try {
                myBluetoothOutStream.write(messageBuffer);
            } catch (IOException e) {
                //if you cannot write, close the application
                Log.i(TAG,"Sure Entry");
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();

            }
        }
    }
}

