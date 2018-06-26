package sudoku.newgame;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import sudoku.newgame.datahelpers.DataConstants;

public class SettingsFragment extends Fragment {
    @VisibleForTesting
    public ProgressDialog mProgressDialog;
    public boolean isActive = false;
    View fragment;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private SharedPreferences sp;
    int theme = 0;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_settings, container, false);
        fragment.findViewById(R.id.sign_in_button).setOnClickListener(onClick());
        fragment.findViewById(R.id.sign_out_button).setOnClickListener(onClick());
        sp = getActivity().getSharedPreferences("Structure", Context.MODE_PRIVATE);

        ToggleButton tbut = fragment.findViewById(R.id.theme);
        int theme = sp.getInt("Theme",0);
        if(theme == DataConstants.DARK) {
            tbut.setChecked(false);
        }
        else {
            tbut.setChecked(true);
        }
        changeTheme(theme);
        tbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sp.edit();
                if(((ToggleButton)view).isChecked()) {
                    editor.putInt("Theme", DataConstants.LIGHT);
                    changeTheme(DataConstants.LIGHT);
                }
                else {
                    editor.putInt("Theme", DataConstants.DARK);
                    changeTheme(DataConstants.DARK);
                }
                editor.apply();
            }
        });

        tbut = fragment.findViewById(R.id.bool_auth);
        boolean isAuth = sp.getBoolean("Auth", true);
        tbut.setChecked(isAuth);
        tbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("Auth", ((ToggleButton)view).isChecked());
                editor.apply();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        mAuth = FirebaseAuth.getInstance();
        updateUI();
        return fragment;
    }
    private void updateUI() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            fragment.findViewById(R.id.no_user).setVisibility(View.VISIBLE);
            fragment.findViewById(R.id.user).setVisibility(View.GONE);
        }
        else {
            fragment.findViewById(R.id.user).setVisibility(View.VISIBLE);
            ((TextView)fragment.findViewById(R.id.user_email)).setText(currentUser.getEmail());
            fragment.findViewById(R.id.no_user).setVisibility(View.GONE);
        }
    }
    private void changeTheme(int theme) {
        LinearLayout ll = fragment.findViewById(R.id.fragment_settings);
        ll.setBackgroundColor(DataConstants.getBackgroundColor(theme));
        ((TextView)fragment.findViewById(R.id.user_email)).setTextColor(DataConstants.getMainTextColor(theme));
        ((TextView)fragment.findViewById(R.id.theme_text)).setTextColor(DataConstants.getMainTextColor(theme));
        ((TextView)fragment.findViewById(R.id.account)).setTextColor(DataConstants.getMainTextColor(theme));
        ((Button)fragment.findViewById(R.id.sign_out_button)).setBackgroundColor(DataConstants.getBackgroundColor(theme));
        ((Button)fragment.findViewById(R.id.sign_out_button)).setTextColor(DataConstants.getMainTextColor(theme));
        ToggleButton tbut = fragment.findViewById(R.id.theme);
        tbut.setTextColor(DataConstants.getMainTextColor(theme));
        tbut.setBackgroundColor(DataConstants.getBackgroundColor(theme));
        tbut = fragment.findViewById(R.id.bool_auth);
        tbut.setTextColor(DataConstants.getMainTextColor(theme));
        tbut.setBackgroundColor(DataConstants.getBackgroundColor(theme));
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }

                        hideProgressDialog();
                    }
                });
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private View.OnClickListener onClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = view.getId();
                if (i == R.id.sign_in_button) {
                    signIn();
                } else if (i == R.id.sign_out_button) {
                    signOut();
                }
            }
        };
    }
    private void signOut() {
        mAuth.signOut();

        mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(),
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI();
                    }
                });
    }
}
