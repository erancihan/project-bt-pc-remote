package sleepless.bt_mouse_client;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_ENABLE_BT = 2;

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothIO mBluetoothIO = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }

    protected void onStart() {
        super.onStart();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            mBluetoothIO = new BluetoothIO(this, mHandler);
        }
        getActionBar().setDisplayShowHomeEnabled(false);
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };
}
