package no.hvl.dat153.sortify.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Pager;
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

    ArrayList<Track> tracks;
    ListView tListView;

    ListView plListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        playlist = getIntent().getStringExtra("playlist");
        playlistName = getIntent().getStringExtra("playlistName");

        setTitle(playlistName);

        tListView = (ListView)findViewById(R.id.trackListView);


        if (!accessToken.equals("")) {
            getMeId();
            //System.out.println(spotify.getMe().id);
            //System.out.println(playlist);

            //getTracks(spotify.getMe().id, playlist);
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

                    ArrayList<String> items = new ArrayList<String>();

                    for (PlaylistTrack track : trackPager.items) {
                        System.out.println(track.track.name);
                        items.add(track.track.name);
                    }


                    tracks = (ArrayList) trackPager.items;
                    //TracksAdapter adapter = new TracksAdapter(getApplicationContext(), trackPager.items);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, items);
                    tListView.setAdapter(adapter);
                    //plListView.setOnItemClickListener(onItemClickListener);
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }



}
