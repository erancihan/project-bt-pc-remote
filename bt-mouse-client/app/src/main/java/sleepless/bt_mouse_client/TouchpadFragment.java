package sleepless.bt_mouse_client;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Created by theraiderman15 on 02.06.2017.
 */

public class TouchpadFragment extends Fragment {
    private static final String TAG = "MainActivity";
    private static final boolean DEBUG = false;
    private SharedPreferences sp;

    private BluetoothIO mBluetoothIO;
    private TouchPad mTouchPad = null;
    private Button mL = null, mR = null;

    private double prevX = 0, prevY = 0;
    private double initialX = 0, initialY = 0;

    public void setBluetoothIO(BluetoothIO io) {
        mBluetoothIO = io;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.touchpad_layout, container, false);
        sp = getActivity().getSharedPreferences("AppData", Context.MODE_PRIVATE);
        float cursorSpeed = sp.getInt("cursor_speed", 1);
        mBluetoothIO.sendMessage("actionspeed," + cursorSpeed);

        mTouchPad = (TouchPad) view.findViewById(R.id.touchpadView);
        mL = (Button) view.findViewById(R.id.leftclick);
        mL.set
        mR = (Button) view.findViewById(R.id.rightclick);

        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.touchpadLayout);
        layout.setOnTouchListener(touchpadListener);

        mL.setOnClickListener(leftclickListener);
        mR.setOnClickListener(rightclicklistener);

        return view;
    }

    View.OnClickListener rightclicklistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mBluetoothIO.sendMessage("actionrightclick");
        }
    };

    View.OnClickListener leftclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mBluetoothIO.sendMessage("actionleftclick");
        }
    };

    View.OnTouchListener touchpadListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            double eventX = ((double) event.getX());
            double eventY = ((double) event.getY());
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    prevX = eventX;
                    prevY = eventY;
                    initialX = eventX;
                    initialY = eventY;
                    if (DEBUG) { System.out.println("For Down... X: " + initialX + " Y: " + initialY); }
                    break;
                case MotionEvent.ACTION_MOVE:
                    sendActionMove(eventX, eventY);
                    break;
                case MotionEvent.ACTION_UP:
                    if (DEBUG) { System.out.println("For Up... X: " + eventX + " Y: " + eventY); }
                    if(Math.abs(initialX - eventX) < 50 && Math.abs(initialY - eventY) < 50) {
                        mBluetoothIO.sendMessage("actionleftclick");
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    public void sendActionMove(double x, double y){
        mBluetoothIO.sendMessage("actionmove," + (x - prevX) + "," + (y - prevY));
        prevX = x;
        prevY = y;
    }
}
