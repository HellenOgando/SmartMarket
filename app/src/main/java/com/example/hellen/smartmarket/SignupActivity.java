package com.example.hellen.smartmarket;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.example.hellen.smartmarket.Controllers.AppHelper;

import mehdi.sakout.fancybuttons.FancyButton;

public class SignupActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private EditText etName;
    private EditText etConfirm;
    private FancyButton btnCreate;
    private String emailInput;
    private String passInput;
    private String confirmInput;
    private String nameInput;
    private ProgressDialog waitDialog;
    private AlertDialog userDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnCreate = (FancyButton) findViewById(R.id.btnCreate);
        etConfirm = (EditText) findViewById(R.id.etConfirm);
        etName = (EditText) findViewById(R.id.etName);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CognitoUserAttributes userAttributes = new CognitoUserAttributes();

                emailInput = etEmail.getText().toString();
                if(emailInput.isEmpty()){
                    Toast.makeText(SignupActivity.this, "Debe colocar un correo electrónico", Toast.LENGTH_SHORT).show();
                    return;
                }

                passInput = etPassword.getText().toString();
                if(passInput.isEmpty()){
                    Toast.makeText(SignupActivity.this, "Debe colocar una contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                confirmInput = etConfirm.getText().toString();
                if(!confirmInput.equals(passInput)){
                    Toast.makeText(SignupActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }

                nameInput = etName.getText().toString();
                if(!nameInput.isEmpty()){
                    userAttributes.addAttribute("name", nameInput);
                }

                showWaitDialog("Creando nueva cuenta...");
                AppHelper.getPool().signUpInBackground(emailInput, passInput, userAttributes, null, signUpHandler);
            }
        });
    }

    private void showWaitDialog(String message) {
        closeWaitDialog();
        waitDialog = new ProgressDialog(this);
        waitDialog.setTitle(message);
        waitDialog.show();
    }

    private void closeWaitDialog() {
        try {
            waitDialog.dismiss();
        }
        catch (Exception e) {
            //
        }
    }

    SignUpHandler signUpHandler = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, boolean signUpConfirmationState,
                              CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            closeWaitDialog();
            Boolean regState = signUpConfirmationState;
            if (signUpConfirmationState) {
                showDialogMessage("Registro exitoso!",emailInput+" ha sido confirmado", true);
            }
            else {
                confirmSignUp(cognitoUserCodeDeliveryDetails);
            }
        }

        @Override
        public void onFailure(Exception exception) {
            closeWaitDialog();
            showDialogMessage("Registro fallido", AppHelper.formatException(exception),false);
        }
    };

    private void confirmSignUp(CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
        Intent intent = new Intent(this, ConfirmAccountActivity.class);
        intent.putExtra("source","signup");
        intent.putExtra("name", emailInput);
        intent.putExtra("destination", cognitoUserCodeDeliveryDetails.getDestination());
        intent.putExtra("deliveryMed", cognitoUserCodeDeliveryDetails.getDeliveryMedium());
        intent.putExtra("attribute", cognitoUserCodeDeliveryDetails.getAttributeName());
        startActivityForResult(intent, 10);
    }


    private void showDialogMessage(String title, String body, final boolean exit) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                    if(exit) {
                        exit(emailInput);
                    }
                } catch (Exception e) {
                    if(exit) {
                        exit(emailInput);
                    }
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if(resultCode == RESULT_OK){
                String name = null;
                if(data.hasExtra("name")) {
                    name = data.getStringExtra("name");
                }
                exit(name, passInput);
            }
        }
    }

    private void exit(String uname) {
        exit(uname, null);
    }

    private void exit(String uname, String password) {
        Intent intent = new Intent();
        if (uname == null) {
            uname = "";
        }
        if (password == null) {
            password = "";
        }
        intent.putExtra("name", uname);
        intent.putExtra("password", password);
        setResult(RESULT_OK, intent);
        finish();
    }
}
