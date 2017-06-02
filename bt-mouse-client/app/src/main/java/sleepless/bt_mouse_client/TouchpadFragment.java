package sleepless.bt_mouse_client;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by theraiderman15 on 02.06.2017.
 */

public class TouchpadFragment extends Fragment {
    private static final boolean DEBUG = true;
    private static final String TAG = "MainActivity";

    private BluetoothIO mBluetoothIO;
    private TouchPad mMouseView = null;

    private double prevX = 0, prevY = 0;

    public void setBluetoothIO(BluetoothIO io) {
        mBluetoothIO = io;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.touchpad_layout, container, false);

        mMouseView = (TouchPad) view.findViewById(R.id.touchpadView);

        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.touchpadLayout);
        layout.setOnTouchListener(touchpadListener);

        return view;
    }

    View.OnTouchListener touchpadListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            double eventX = ((double) event.getX());
            double eventY = ((double) event.getY());
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

        mBluetoothIO.sendMessage("actionmove," + String.format("%.3f", x - prevX) + "," + String.format("%.3f", y - prevY));
        prevX = x;
        prevY = y;
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
}
