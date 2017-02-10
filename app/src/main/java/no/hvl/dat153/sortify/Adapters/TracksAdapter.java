package no.hvl.dat153.sortify.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.AudioFeaturesTrack;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;
import no.hvl.dat153.sortify.R;

public class TracksAdapter extends ArrayAdapter<PlaylistTrack> {
    private Context context;

    public TracksAdapter(Context context, ArrayList<PlaylistTrack> tracks) {
        super(context, 0, (List) tracks);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlaylistTrack track = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.track_item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.trackNameTextView);
        TextView artistAlbum = (TextView) convertView.findViewById(R.id.artistAlbumTextView);

        name.setText(track.track.name);
        artistAlbum.setText(track.track.artists.get(0).name + " - " + track.track.album.name);

        return convertView;
    }
}
