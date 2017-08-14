package no.hvl.dat153.sortify.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import kaaes.spotify.webapi.android.SpotifyApi;
import no.hvl.dat153.sortify.R;
import no.hvl.dat153.sortify.TrackPlayer;

import static no.hvl.dat153.sortify.App.accessToken;
import static no.hvl.dat153.sortify.App.initSpotify;
import static no.hvl.dat153.sortify.App.spotify;
import static no.hvl.dat153.sortify.TrackPlayer.getPlayer;



public class MainActivity extends AppCompatActivity {

    private Button connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Sortify");

        System.out.println("Access token: " + accessToken);

        if (!accessToken.equals("")) {
            startApplication();
        }

        connect = (Button) findViewById(R.id.connectToSpotify);
        connect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startAuthentication();
            }
        });
    }


    void startAuthentication() {
        Intent intent = new Intent(this, AuthenticateActivity.class);
        startActivityForResult(intent, 1);
    }

    void startApplication() {
        initSpotify();
        getPlayer(this);

        Intent intent = new Intent(this, PlaylistActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                startApplication();
            }
        }
    }
}
