package com.example.homeautomation_hackathon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    private static final String INDICATOR_ON = "Bluetooth Connected";

    private TextView tvMessage, tvIndicator;
    private Button btnDiscover, btnPaired, btnIndicator;
    private BluetoothAdapter bluetoothAdapter;

    private Toolbar toolbar;
    private ImageButton bluetoothIcon;

    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDiscover = findViewById(R.id.btn_discover);
        bluetoothIcon = findViewById(R.id.ib_bluetooth_icon);
        btnPaired = findViewById(R.id.btn_paired);
        tvMessage = findViewById(R.id.tv_message);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter==null){
            showToast("Bluetooth Not Available");
        }else{
            showToast("Bluetooth Available");

        }
        checkBluetooth();

        bluetoothIcon.setOnClickListener(view -> {
            tvMessage.setText(" ");
            if (!bluetoothAdapter.isEnabled()){
                showToast("Turning on Bluetooth !");
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLE_BT);
            }else{
                bluetoothAdapter.disable();
                showToast("Turning off Bluetooth !");
            }
            checkBluetooth();
        });

        btnDiscover.setOnClickListener(view -> {
            tvMessage.setText(" ");
            if(!bluetoothAdapter.isDiscovering()){
                showToast("Making your Device Discoverable");
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                startActivityForResult(intent, REQUEST_DISCOVER_BT);
            }

        });

        btnPaired.setOnClickListener(view -> {

            if(bluetoothAdapter.isEnabled()){
                Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                String message = "";
                for(BluetoothDevice device: devices){
                    message += device.getName() +" \n " + device +"\n\n";
                }
                tvMessage.setText(""+message);
            }else{
                showToast("Turn on Bluetooth First");
            }

        });

    }

    private void showToast(String s) {
        Toast.makeText(this, ""+s, Toast.LENGTH_SHORT).show();
    }

    private void checkBluetooth(){
        if (bluetoothAdapter.isEnabled()){
            bluetoothIcon.setImageResource(R.drawable.ic_baseline_bluetooth_on);
        }else{
            bluetoothIcon.setImageResource(R.drawable.ic_baseline_bluetooth_off);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkBluetooth();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch(requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK){
                    bluetoothIcon.setImageResource(R.drawable.ic_baseline_bluetooth_on);
                    showToast("Bluetooth turned on");
                }else{
                    bluetoothIcon.setImageResource(R.drawable.ic_baseline_bluetooth_on);
                    showToast("Could not turn on Bluetooth");

                }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
//        bluetoothIcon = menu.findItem(R.id.action_bluetooth);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_bluetooth:
                Toast.makeText(this, "Bluetooth", Toast.LENGTH_SHORT).show();
//                if (flag){
//                    bluetoothIcon.setIcon(R.drawable.ic_baseline_bluetooth_on);
//                    flag = !flag;
//                }else{
//                    bluetoothIcon.setIcon(R.drawable.ic_baseline_bluetooth_off);
//                    flag = !flag;
//                }
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}