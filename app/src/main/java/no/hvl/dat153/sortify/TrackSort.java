package no.hvl.dat153.sortify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import kaaes.spotify.webapi.android.models.AudioFeaturesTrack;
import kaaes.spotify.webapi.android.models.PlaylistTrack;



public class TrackSort {
    public static ArrayList<PlaylistTrack> sortByDanceability(ArrayList<PlaylistTrack> uplt, ArrayList<AudioFeaturesTrack> uaft) {
        ArrayList<PlaylistTrack> sorted = new ArrayList<>();

        Collections.sort(uaft, new Comparator<AudioFeaturesTrack>() {
            @Override
            public int compare(AudioFeaturesTrack o1, AudioFeaturesTrack o2) {
                int i1 = (int)(o1.danceability * 100);
                int i2 = (int)(o2.danceability * 100);
                return Integer.valueOf(i1).compareTo(i2);
            }
        });

        for (AudioFeaturesTrack aft : uaft) {
            for (PlaylistTrack plt : uplt) {
                if (aft.id.equals(plt.track.id)) {
                    System.out.println(plt.track.name + " " + aft.danceability);
                    sorted.add(plt);
                }
            }
        }

        return sorted;
    }


    public static ArrayList<PlaylistTrack> sortByEnergy(ArrayList<PlaylistTrack> uplt, ArrayList<AudioFeaturesTrack> uaft) {
        ArrayList<PlaylistTrack> sorted = new ArrayList<>();

        Collections.sort(uaft, new Comparator<AudioFeaturesTrack>() {
            @Override
            public int compare(AudioFeaturesTrack o1, AudioFeaturesTrack o2) {
                int i1 = (int)(o1.energy * 100);
                int i2 = (int)(o2.energy * 100);
                return Integer.valueOf(i1).compareTo(i2);
            }
        });

        for (AudioFeaturesTrack aft : uaft) {
            for (PlaylistTrack plt : uplt) {
                if (aft.id.equals(plt.track.id)) {
                    System.out.println(plt.track.name + " " + aft.energy);
                    sorted.add(plt);
                }
            }
        }

        return sorted;
    }

    public static ArrayList<PlaylistTrack> sortByValence(ArrayList<PlaylistTrack> uplt, ArrayList<AudioFeaturesTrack> uaft) {
        ArrayList<PlaylistTrack> sorted = new ArrayList<>();

        Collections.sort(uaft, new Comparator<AudioFeaturesTrack>() {
            @Override
            public int compare(AudioFeaturesTrack o1, AudioFeaturesTrack o2) {
                int i1 = (int)(o1.valence * 1000);
                int i2 = (int)(o2.valence * 1000);
                return Integer.valueOf(i1).compareTo(i2);
            }
        });

        for (AudioFeaturesTrack aft : uaft) {
            for (PlaylistTrack plt : uplt) {
                if (aft.id.equals(plt.track.id)) {
                    System.out.println(plt.track.name + " " + aft.valence);
                    sorted.add(plt);
                }
            }
        }

        return sorted;
    }
}
