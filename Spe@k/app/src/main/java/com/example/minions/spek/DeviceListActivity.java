package com.example.minions.spek;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class DeviceListActivity extends Activity {
    private static final String TAG = "DeviceListActivity";
    private static final boolean dBoolean = true;
    public static String EDADDRESS = "device_address";
    Button selectButton;
    TextView disBluetooth;
    private BluetoothAdapter BTAdapter;
    private ArrayAdapter<String> pairedDevicesList;
    private OnItemClickListener myDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            disBluetooth.setText("Connecting...");
            Log.i(TAG, "status 1");
            String information
                    = ((TextView) v).getText().toString();
            Log.i(TAG, "status 2");
            String adapterAddress = information.substring(information.length() - 17);
            Log.i(TAG, "status 3");

            Intent intent = new Intent(DeviceListActivity.this, MainActivity.class);
            Log.i(TAG, "status 4");
            intent.putExtra(EDADDRESS, adapterAddress);
            Log.i(TAG, "status 5");
            startActivity(intent);
            Log.i(TAG, "status 6");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkBTState();

        disBluetooth = (TextView) findViewById(R.id.connecting);
        disBluetooth.setTextSize(40);
        disBluetooth.setText(" ");

        pairedDevicesList = new ArrayAdapter<String>(this, R.layout.device_name);

        ListView pairedDeviceListView = (ListView) findViewById(R.id.paired_devices);
        pairedDeviceListView.setAdapter(pairedDevicesList);
        pairedDeviceListView.setOnItemClickListener(myDeviceClickListener);

        BTAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = BTAdapter.getBondedDevices();


        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);//make title viewable
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesList.add(device.getName() + "\n" + device.getAddress());
            }
        } else {

            pairedDevicesList.add("No Devices Paired");
        }
    }

    private void checkBTState() {

        BTAdapter = BluetoothAdapter.getDefaultAdapter();
        if (BTAdapter == null) {
            Toast.makeText(getBaseContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (BTAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {
                Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetoothIntent, 1);

            }
        }
    }
}