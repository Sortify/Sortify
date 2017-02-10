package no.hvl.dat153.sortify.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import no.hvl.dat153.sortify.Adapters.PlaylistAdapter;
import no.hvl.dat153.sortify.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static no.hvl.dat153.sortify.App.accessToken;
import static no.hvl.dat153.sortify.App.spotify;

public class MainActivity extends AppCompatActivity {
    ArrayList<PlaylistSimple> playlists;
    ListView plListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("MAIN IS HERE.");

        plListView = (ListView)findViewById(R.id.playlistListView);

        if (!accessToken.equals(""))
            getPlaylists();
    }

    private void getPlaylists() {
        spotify.getMyPlaylists(new Callback<Pager<PlaylistSimple>>() {
            @Override
            public void success(Pager<PlaylistSimple> playlistSimplePager, Response response) {
                if (playlistSimplePager.items.size() > 0) {
                    playlists = (ArrayList) playlistSimplePager.items;
                    PlaylistAdapter adapter = new PlaylistAdapter(getApplicationContext(), playlistSimplePager.items);
                    plListView.setAdapter(adapter);
                    plListView.setOnItemClickListener(onItemClickListener);
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PlaylistSimple pl = (PlaylistSimple) plListView.getAdapter().getItem(position);
            Intent intent = new Intent(getApplicationContext(), PlaylistActivity.class);
            intent.putExtra("playlist", pl.id);
            intent.putExtra("playlistName", pl.name);
            startActivity(intent);
        }
    };

}