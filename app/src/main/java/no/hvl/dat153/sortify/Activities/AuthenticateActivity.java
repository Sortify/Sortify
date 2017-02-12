package no.hvl.dat153.sortify.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;

import kaaes.spotify.webapi.android.SpotifyApi;
import no.hvl.dat153.sortify.R;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;
import static no.hvl.dat153.sortify.App.CLIENT_ID;
import static no.hvl.dat153.sortify.App.REDIRECT_URI;
import static no.hvl.dat153.sortify.App.accessToken;
import static no.hvl.dat153.sortify.App.spotify;

public class AuthenticateActivity extends AppCompatActivity implements ConnectionStateCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);

        setTitle("Authenticating");


        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(
                CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming", "playlist-read-private"});
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

                // Store access token
                SharedPreferences sharedPref = getSharedPreferences("SORTIFY", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("ACCESS_TOKEN", accessToken);
                editor.apply();

                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("PlaylistActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("PlaylistActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error i) {
        Log.d("PlaylistActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("PlaylistActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("PlaylistActivity", "Received connection message: " + message);
    }
}