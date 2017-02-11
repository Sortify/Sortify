package no.hvl.dat153.sortify.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import kaaes.spotify.webapi.android.SpotifyApi;
import no.hvl.dat153.sortify.R;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;
import static no.hvl.dat153.sortify.App.CLIENT_ID;
import static no.hvl.dat153.sortify.App.REDIRECT_URI;
import static no.hvl.dat153.sortify.App.accessToken;
import static no.hvl.dat153.sortify.App.player;
import static no.hvl.dat153.sortify.App.spotify;

public class AuthenticateActivity extends AppCompatActivity implements ConnectionStateCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);

        setTitle("Authenticating");

        if (!accessToken.equals("")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(
                CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                accessToken = response.getAccessToken();

                SpotifyApi api = new SpotifyApi();
                api.setAccessToken(accessToken);

                spotify = api.getService();

                getPlayer();

                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
            }
        }
    }

    @Override
    public void onLoggedIn() {
        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);

        Log.d("MainActivity", "User logged in");
    }

    private void getPlayer() {
        Config playerConfig = new Config(this, accessToken, CLIENT_ID);
        Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
            @Override
            public void onInitialized(SpotifyPlayer spotifyPlayer) {
                player = spotifyPlayer;
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("TracksActivity", "Could not initialize player: " + throwable.getMessage());
            }
        });
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error i) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }
}