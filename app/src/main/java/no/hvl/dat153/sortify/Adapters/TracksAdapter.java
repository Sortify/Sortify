package no.hvl.dat153.sortify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;
import no.hvl.dat153.sortify.R;

public class TracksAdapter extends ArrayAdapter<Track> {

    public TracksAdapter(Context context, List<PlaylistTrack> tracks) {
        super(context, 0, (List) tracks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Track track = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.nameTextView);
        //TextView subText = (TextView) convertView.findViewById(R.id.subTextView);

        name.setText(track.name);

        return convertView;
    }
}
