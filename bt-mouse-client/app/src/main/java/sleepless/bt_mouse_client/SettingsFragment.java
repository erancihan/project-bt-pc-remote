package sleepless.bt_mouse_client;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

/**
 * Created by theraiderman15 on 02.06.2017.
 */

public class SettingsFragment extends Fragment {

    private BluetoothIO mBluetoothIO;
    private SeekBar seekBar;
    private SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_layout, container, false);
        this.seekBar = (SeekBar)view.findViewById(R.id.cursor_speed);
        sp = getActivity().getSharedPreferences("AppData", Context.MODE_PRIVATE);
        this.seekBar.setProgress(sp.getInt("cursor_speed",1));
        Button button = (Button)view.findViewById(R.id.save_settings);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                saveSettings(view);
            }
        });
        return view;
    }

    public void setBluetoothIO(BluetoothIO bluetoothIO){
        this.mBluetoothIO = bluetoothIO;
    }

    public void saveSettings(View view){
        System.out.println("Hello Ma Man"); //TODO DEBUG
        int value = seekBar.getProgress();
        mBluetoothIO.sendMessage("actionspeed," + value);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("cursor_speed", value);
        editor.commit();
        //todo toast message
    }
}
