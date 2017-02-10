package no.hvl.dat153.sortify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import no.hvl.dat153.sortify.R;

/**
 * Created by nataniel on 09.02.2017.
 */

public class PlaylistAdapter extends ArrayAdapter<PlaylistSimple> {

    private Context context;

    public PlaylistAdapter(Context context, List<PlaylistSimple> artists) {
        super(context, 0, artists);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlaylistSimple album = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.imageView);
        TextView name = (TextView) convertView.findViewById(R.id.nameTextView);

        if (album.images.size() > 0) {
            Image im = album.images.get(0);
            Picasso.with(getContext())
                    .load(im.url)
                    .resize(250, 250)
                    .into(image);
        }
        name.setText(album.name);

        return convertView;
    }
}