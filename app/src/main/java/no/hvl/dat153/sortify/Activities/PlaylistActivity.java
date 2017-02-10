package no.hvl.dat153.sortify.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

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
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static no.hvl.dat153.sortify.App.accessToken;
import static no.hvl.dat153.sortify.App.spotify;
import static no.hvl.dat153.sortify.App.userId;

public class PlaylistActivity extends AppCompatActivity {
    private String playlist;
    private String playlistName;

    ArrayList<PlaylistTrack> tracks;
    ArrayList<AudioFeaturesTrack> afTracks;
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

        String[] items = new String[]{"Danceability", "High energy", "Slow beats"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(spinnerAdapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sortString = (String) dropdown.getAdapter().getItem(position);
                System.out.println(id + " " + sortString);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (!accessToken.equals("")) {
            getMeId();
        }


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
                loadTrackListView("DANCE");
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

    private void loadTrackListView(String sorting) {
        Collections.sort(afTracks, new Comparator<AudioFeaturesTrack>() {
            @Override
            public int compare(AudioFeaturesTrack o1, AudioFeaturesTrack o2) {
                System.out.println("comparing...");
                int i1 = (int)(o1.danceability * 100);
                int i2 = (int)(o2.danceability * 100);
                return Integer.valueOf(i1).compareTo(i2);
            }
        });

        for (AudioFeaturesTrack af : afTracks) {
            System.out.println(af.danceability);
        }

        ArrayList<PlaylistTrack> tracksSorted =  new ArrayList<>();

        for (AudioFeaturesTrack aft : afTracks) {
            for (PlaylistTrack plt : tracks) {
                if (aft.id.equals(plt.track.id)) {
                    System.out.println(plt.track.name + " " + aft.danceability);
                    tracksSorted.add(plt);
                }
            }
        }

        TracksAdapter adapter = new TracksAdapter(getApplicationContext(), tracksSorted);
        tListView.setAdapter(adapter);

        //adapter.notifyDataSetChanged();
        //plListView.setOnItemClickListener(onItemClickListener);
    }

}
