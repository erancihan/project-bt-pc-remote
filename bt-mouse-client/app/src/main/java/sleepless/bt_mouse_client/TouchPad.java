package sleepless.bt_mouse_client;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by freedrone on 02.06.2017.
 */

public class TouchPad extends View {
    private static final boolean DEBUG = true;
    private static final String TAG = "TouchPad";

    public int height, width;

    public TouchPad(Context context, AttributeSet set) {
        super(context, set);
    }

    @Override
    protected void onSizeChanged(int new_width, int new_height, int xOld, int yOld) {
        super.onSizeChanged(new_width, new_height, xOld, yOld);

        width = new_width;
        height = new_height;

        if (DEBUG) Log.i(TAG, "onSizeChanged," + width + "," + height);
    }
}
