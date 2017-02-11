package no.hvl.dat153.sortify;

import android.app.Application;
import android.content.SharedPreferences;

import com.spotify.sdk.android.player.Metadata;
import com.spotify.sdk.android.player.Player;

import kaaes.spotify.webapi.android.SpotifyService;

public class App extends Application {
    public static final String CLIENT_ID = "134e561475414239b04539e4b8ef7b3c";
    public static final String REDIRECT_URI = "http://sortify.com";

    public static SpotifyService spotify;
    public static String accessToken;
    public static Player player = null;

    public static Metadata.Track currentTrack = null;

    public static String userId;

    @Override
    public void onCreate() {
        super.onCreate();

        // Restore preferences
        SharedPreferences settings = getSharedPreferences("AUTH", 0);
        accessToken = settings.getString("ACCESS_TOKEN", "");
        //userId = settings.getString("USER_ID", "");
    }
}
