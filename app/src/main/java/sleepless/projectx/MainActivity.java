package sleepless.projectx;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mLight, mGyro, mAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    public final void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public final void onSensorChanged(SensorEvent event) {
        // Do something with this sensor value.
        Sensor sensor = event.sensor;

        TextView tvlux = (TextView)findViewById(R.id.displaylux);
        TextView tvgyro = (TextView)findViewById(R.id.displaygyro);
        TextView tvacc = (TextView)findViewById(R.id.displayacc);

        if (sensor.getType() == Sensor.TYPE_LIGHT) {
            tvlux.setText("" + event.values[0]);
        }
        if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            tvgyro.setText("" + event.values[0] + " " + event.values[1] + " " + event.values[2]);
        }
        if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            tvacc.setText("" + event.values[0] + " " + event.values[1] + " " + event.values[2]);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyro, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mAcc, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
