package todolist2.deepak.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * Created by Deepak on 22-Feb-16.
 */
public class LoginMainActivity extends Activity {
    private CallbackManager callbackManager;
    private TextView textView;
    int flag=0;

    private AccessTokenTracker accessTokenTracker,accessTokenTracker2;
    private ProfileTracker profileTracker,profileTracker2;

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            displayMessage(profile);
        }

        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(),"Login Cancelled",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException e) {
            Toast.makeText(getApplicationContext(),"Error!! Please Try Again",Toast.LENGTH_SHORT).show();
        }
    };

    public LoginMainActivity() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        flag=0;
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login);
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        textView = (TextView) findViewById(R.id.textView);

        loginButton.setReadPermissions("user_friends");
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
            }
        };
        loginButton.registerCallback(callbackManager, callback);
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void displayMessage(Profile profile){
        if(profile != null){

            if(flag==0) {
                textView.setText(profile
                        .getName());
                flag=1;
                Intent intent = new Intent(LoginMainActivity.this, MainActivity.class);
                intent.putExtra("name", profile.getName());
                startActivity(intent);
            }else{
                finish();
            }
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(AccessToken.getCurrentAccessToken()!=null) {
            Profile profile = Profile.getCurrentProfile();
            displayMessage(profile);
        }

    }
}
