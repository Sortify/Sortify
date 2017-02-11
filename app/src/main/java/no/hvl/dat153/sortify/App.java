package no.hvl.dat153.sortify;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import kaaes.spotify.webapi.android.SpotifyService;
import no.hvl.dat153.sortify.Activities.AuthenticateActivity;

public class App extends Application {
    public static final String CLIENT_ID = "134e561475414239b04539e4b8ef7b3c";
    public static final String REDIRECT_URI = "http://sortify.com";

    public static SpotifyService spotify;
    public static String accessToken;
    public static Player player;

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
