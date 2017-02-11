package no.hvl.dat153.sortify.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import kaaes.spotify.webapi.android.models.AudioFeaturesTrack;
import kaaes.spotify.webapi.android.models.AudioFeaturesTracks;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.UserPrivate;
import no.hvl.dat153.sortify.Adapters.PlaylistAdapter;
import no.hvl.dat153.sortify.Adapters.TracksAdapter;
import no.hvl.dat153.sortify.R;
import no.hvl.dat153.sortify.TrackSort;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static no.hvl.dat153.sortify.App.CLIENT_ID;
import static no.hvl.dat153.sortify.App.accessToken;
import static no.hvl.dat153.sortify.App.player;
import static no.hvl.dat153.sortify.App.spotify;
import static no.hvl.dat153.sortify.App.userId;

public class PlaylistActivity extends AppCompatActivity implements SpotifyPlayer.NotificationCallback, ConnectionStateCallback {
    private String playlist;
    private String playlistName;

    ArrayList<PlaylistTrack> tracks;
    ArrayList<AudioFeaturesTrack> afTracks = new ArrayList<>();
    ListView tListView;
    Spinner dropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        playlist = getIntent().getStringExtra("playlist");
        playlistName = getIntent().getStringExtra("playlistName");

        setTitle(playlistName);

        tListView = (ListView)findViewById(R.id.trackListView);
        dropdown = (Spinner)findViewById(R.id.spinner);

        String[] items = new String[]{"Danceability", "Energy", "Positivity"};
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(spinnerAdapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sortString = (String) dropdown.getAdapter().getItem(position);
                System.out.println(id + " " + sortString);

                if (!afTracks.isEmpty()) {
                    ArrayList<PlaylistTrack> sorted = new ArrayList<>();

                    if (id == 0) {
                        sorted = TrackSort.sortByDanceability(tracks, afTracks);
                    } else if (id == 1) {
                        sorted = TrackSort.sortByEnergy(tracks, afTracks);
                    } else if (id == 2) {
                        sorted = TrackSort.sortByValence(tracks, afTracks);
                    }

                    loadTrackListView(sorted);
                }

            }

            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (!accessToken.equals("")) {
            getPlayer();
            getMeId();
        }


    }

    private void getPlayer() {
        Config playerConfig = new Config(this, accessToken, CLIENT_ID);
        Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
            @Override
            public void onInitialized(SpotifyPlayer spotifyPlayer) {
                player = spotifyPlayer;
                player.addConnectionStateCallback(PlaylistActivity.this);
                player.addNotificationCallback(PlaylistActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("PlaylistActivity", "Could not initialize player: " + throwable.getMessage());
            }
        });
    }

    private void getMeId() {
        spotify.getMe(new Callback<UserPrivate>() {

            @Override
            public void success(UserPrivate userPrivate, Response response) {
                System.out.println(userPrivate.id);
                getTracks(userPrivate.id, playlist);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

    private void getTracks(String userId, String playlistId) {
        spotify.getPlaylistTracks(userId, playlistId, new Callback<Pager<PlaylistTrack>>() {
            @Override
            public void success(Pager<PlaylistTrack> trackPager, Response response) {
                if (trackPager.items.size() > 0) {
                    tracks = (ArrayList) trackPager.items;

                    String trackIds = "";

                    // Get track features
                    for (PlaylistTrack plTrack : tracks) {
                        trackIds += plTrack.track.id + ",";
                    }
                    getTrackFeatures(trackIds);
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void getTrackFeatures(String trackIds) {
        spotify.getTracksAudioFeatures(trackIds, new Callback<AudioFeaturesTracks>() {
            @Override
            public void success(AudioFeaturesTracks audioFeaturesTracks, Response response) {
                afTracks = (ArrayList) audioFeaturesTracks.audio_features;
                loadTrackListView(TrackSort.sortByDanceability(tracks, afTracks));
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

    private void loadTrackListView(ArrayList<PlaylistTrack> sorted) {
        TracksAdapter adapter = new TracksAdapter(this, sorted);
        tListView.setAdapter(adapter);
        tListView.setOnItemClickListener(onItemClickListener);
        adapter.notifyDataSetChanged();
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PlaylistTrack plt = (PlaylistTrack) tListView.getAdapter().getItem(position);
            player.playUri(null, plt.track.uri, 0, 0);
        }
    };

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {

    }

    @Override
    public void onPlaybackError(Error error) {

    }

    @Override
    public void onLoggedIn() {
        player.pause(null);
    }

    @Override
    public void onLoggedOut() {

    }

    @Override
    public void onLoginFailed(Error error) {

    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onConnectionMessage(String s) {

    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }
}
