package no.hvl.dat153.sortify;

import android.app.Activity;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

/**
 * Created by nataniel on 09.02.2017.
 */

public class Authenticate {
    public final String CLIENT_ID = "134e561475414239b04539e4b8ef7b3c";
    public final String REDIRECT_URI = "http://sortify.com";

    private Activity activity;

    public Authenticate(Activity activity) {
        this.activity = activity;
    }

    public void init() {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(
                CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(activity, REQUEST_CODE, request);
    }
}
