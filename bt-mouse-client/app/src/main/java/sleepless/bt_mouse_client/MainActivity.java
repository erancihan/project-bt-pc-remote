package sleepless.bt_mouse_client;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final boolean DEBUG = true;

    // Message types sent from the BluetoothIO Handler
    public static final int MESSAGE_CONNECT = 1;
    public static final int MESSAGE_DISCONNECT = 2;
    public static final int MESSAGE_READ = 3;
    public static final int MESSAGE_WRITE = 4;
    public static final int MESSAGE_TOAST = 5;

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private static final String TAG = "MainActivity";
    public static final String TOAST = "toast";

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothIO mBluetoothIO = null;

    private TouchPad mMouseView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mMouseView = (TouchPad) findViewById(R.id.);

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
            switch (msg.what) {
                case MESSAGE_CONNECT:
                    //configureButtons(true);
                    break;
                case MESSAGE_DISCONNECT:
                    //configureButtons(false);
                    break;
                case MESSAGE_READ:
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    View.OnTouchListener touchpadListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            double eventX = ((double) event.getX()) / ((double)mMouseView.width) - 0.5;
            double eventY = ((double) event.getY()) / ((double)mMouseView.height) - 0.5;

            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    sendActionDown(eventX, eventY);
                    break;

                case MotionEvent.ACTION_MOVE:
                    sendActionMove(eventX, eventY);
                    break;

                case MotionEvent.ACTION_UP:
                    sendActionUp(eventX, eventY);
                    break;
            }
            return true;
        }
    };

    public void sendActionDown(double x, double y){
        if (DEBUG) Log.i(TAG, "press," + x + "," + y);
        mBluetoothIO.sendMessage("actiondown," + String.format("%.3f", x) + "," + String.format("%.3f", y));
    }

    public void sendActionMove(double x, double y){
        if (DEBUG) Log.i(TAG, "motion," + x + "," + y);
        mBluetoothIO.sendMessage("actionmove," + String.format("%.3f", x) + "," + String.format("%.3f", y));
    }

    public void sendActionUp(double x, double y){
        if (DEBUG) Log.i(TAG, "release," + x + "," + y);
        mBluetoothIO.sendMessage("actionup," + String.format("%.3f", x) + "," + String.format("%.3f", y));
    }

    public void pageDownButton(View view){
        if (DEBUG) Log.i(TAG, "pagedown");
        mBluetoothIO.sendMessage("pagedown");
    }

    public void pageUpButton(View view){
        if (DEBUG) Log.i(TAG, "pageup");
        mBluetoothIO.sendMessage("pageup");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    mBluetoothIO.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    Toast.makeText(this, "Bluetooth Enabled", Toast.LENGTH_SHORT).show();
                    mBluetoothIO = new BluetoothIO(this, mHandler);
                } else {
                    // User did not enable Bluetooth or an error occured
                    Toast.makeText(this, "Bluetooth Not Enabled", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.touchpad) {
            // Handle the camera action
        } else if (id == R.id.sensor) {

        } else if (id == R.id.settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();

        if (mBluetoothIO != null) {
            mBluetoothIO.stop();
        }
    }
}
