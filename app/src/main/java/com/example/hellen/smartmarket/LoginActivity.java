package com.example.hellen.smartmarket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChooseMfaContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.NewPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.example.hellen.smartmarket.Controllers.AppHelper;

import java.util.List;
import java.util.Locale;

import mehdi.sakout.fancybuttons.FancyButton;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private TextView tvChangePass;
    private FancyButton btnLogin;
    private FancyButton btnSignup;
    private String email;
    private String pass;
    private ProgressDialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (FancyButton) findViewById(R.id.btnLogin);
        btnSignup = (FancyButton) findViewById(R.id.btnSignup);
        tvChangePass = (TextView) findViewById(R.id.tvChangePass);

        AppHelper.init(getApplicationContext());

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivityForResult(i, 1);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
                if(email.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Debe colocar un correo electrónico", Toast.LENGTH_SHORT).show();
                    return;
                }

                AppHelper.setUser(email);

                pass = etPassword.getText().toString();
                if(pass.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Debe colocar una contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                showWaitDialog("Signing in...");
                AppHelper.getPool().getUser(email).getSessionInBackground(authenticationHandler);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:

                if(resultCode == RESULT_OK) {
                    String name = data.getStringExtra("name");
                    if (!name.isEmpty()) {
                        etEmail.setText(name);
                        etPassword.setText("");
                        etPassword.requestFocus();
                    }
                    String userPasswd = data.getStringExtra("password");
                    if (!userPasswd.isEmpty()) {
                        etPassword.setText(userPasswd);
                    }
                    if (!name.isEmpty() && !userPasswd.isEmpty()) {
                        email = name;
                        pass = userPasswd;
                        AppHelper.getPool().getUser(email).getSessionInBackground(authenticationHandler);
                    }
                }
                break;
            case 2:
                // Confirm register user
                if(resultCode == RESULT_OK) {
                    String name = data.getStringExtra("name");
                    if (!name.isEmpty()) {
                        etEmail.setText(name);
                        etPassword.setText("");
                        etPassword.requestFocus();
                    }
                }
                break;
        }
    }

    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession cognitoUserSession, CognitoDevice device) {
            Log.d("LoginActivity", " -- Auth Success");
            AppHelper.setCurrSession(cognitoUserSession);
            AppHelper.newDevice(device);
            closeWaitDialog();
            launchUser();
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String username) {
            closeWaitDialog();
            Locale.setDefault(Locale.US);
            getUserAuthentication(authenticationContinuation, username);
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation continuation) {

        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {

        }

        @Override
        public void onFailure(Exception exception) {

        }

    };

    private void closeWaitDialog() {
        try {
            waitDialog.dismiss();
        }
        catch (Exception e) {
            //
        }
    }

    private void launchUser() {
        Intent o = new Intent(this, UserActivity.class);
        o.putExtra("name", email);
        startActivity(o);
    }

    private void showWaitDialog(String message) {
        closeWaitDialog();
        waitDialog = new ProgressDialog(this);
        waitDialog.setTitle(message);
        waitDialog.show();
    }

    private void getUserAuthentication(AuthenticationContinuation continuation, String username) {
        if(username != null) {
            email = username;
            AppHelper.setUser(username);
        }
        if(pass == null) {
            etEmail.setText(username);
            pass = etPassword.getText().toString();
            if(pass.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Debe colocar una contraseña", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        AuthenticationDetails authenticationDetails = new AuthenticationDetails(email, pass, null);
        continuation.setAuthenticationDetails(authenticationDetails);
        continuation.continueTask();
    }
}
