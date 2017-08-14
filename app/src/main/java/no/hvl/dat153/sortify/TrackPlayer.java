package no.hvl.dat153.sortify;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import static no.hvl.dat153.sortify.App.CLIENT_ID;
import static no.hvl.dat153.sortify.App.accessToken;



public class TrackPlayer {

    public static Player player = null;
    public static boolean isPlayerReady = false;

    public static void getPlayer(Context context) {
        Config playerConfig = new Config(context, accessToken, CLIENT_ID);
        Spotify.getPlayer(playerConfig, context, new SpotifyPlayer.InitializationObserver() {
            @Override
            public void onInitialized(SpotifyPlayer spotifyPlayer) {
                player = spotifyPlayer;
                isPlayerReady = true;
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("TracksActivity", "Could not initialize player: " + throwable.getMessage());
            }
        });
    }

}
