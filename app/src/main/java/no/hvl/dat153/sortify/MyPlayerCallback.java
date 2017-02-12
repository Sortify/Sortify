package no.hvl.dat153.sortify;

import android.util.Log;

import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;

/**
 * Created by kimhe on 12.02.2017.
 */

public class MyPlayerCallback implements Player.OperationCallback {
    public void onSuccess() {
        Log.d("OK","Success!");
    }
    public void onError(Error error) {
        Log.d("Feil oppstod","Error");
    }
}
