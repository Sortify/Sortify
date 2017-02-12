package no.hvl.dat153.sortify;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.PlaylistTrack;

public class App extends Application {
    public static final String CLIENT_ID = "134e561475414239b04539e4b8ef7b3c";
    public static final String REDIRECT_URI = "http://sortify.com";

    public static SpotifyService spotify;
    public static String accessToken;

    public static ArrayList<PlaylistTrack> currentPlaylist = null;
    public static int currentTrackPosition = -1;

    public static String userId;

    @Override
    public void onCreate() {
        super.onCreate();

        // Restore preferences
        SharedPreferences sharedPref = getSharedPreferences("SORTIFY", Context.MODE_PRIVATE);
        accessToken = sharedPref.getString("ACCESS_TOKEN", "");

    }

    public static void initSpotify() {
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(accessToken);

        spotify = api.getService();
    }
}
