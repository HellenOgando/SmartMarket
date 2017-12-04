package com.example.hellen.smartmarket;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;
import com.example.hellen.smartmarket.Controllers.AppHelper;

import mehdi.sakout.fancybuttons.FancyButton;

public class ConfirmAccountActivity extends AppCompatActivity {

    private FancyButton btnConfirm;
    private FancyButton btnRequest;
    private EditText etEmail;
    private EditText etCode;
    private String email;
    private String code;
    private AlertDialog userDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_account);

        btnConfirm = (FancyButton) findViewById(R.id.btnConfirm);
        btnRequest = (FancyButton) findViewById(R.id.btnRequest);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etCode = (EditText) findViewById(R.id.etCode);

        Bundle extras = getIntent().getExtras();
        if (extras !=null) {
            if(extras.containsKey("name")) {
                email = extras.getString("name");
                etEmail.setText(email);

                etCode.requestFocus();

                if(extras.containsKey("destination")) {
                    String dest = extras.getString("destination");
                    String delMed = extras.getString("deliveryMed");

                    if(dest != null && delMed != null && dest.length() > 0 && delMed.length() > 0) {
                        Toast.makeText(ConfirmAccountActivity.this, "El código de confirmación ha sido enviado a " + dest + " via " + delMed, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(ConfirmAccountActivity.this, "El código de confirmación ha sido enviado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else {
                Toast.makeText(ConfirmAccountActivity.this, "Solicita un código de confirmación o utiliza uno ya enviado", Toast.LENGTH_SHORT).show();
            }

        }

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
                code = etCode.getText().toString();

                if(email.isEmpty()){
                    Toast.makeText(ConfirmAccountActivity.this, "Debe colocar un correo electrónico", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(code.isEmpty()){
                    Toast.makeText(ConfirmAccountActivity.this, "Debe colocar un código", Toast.LENGTH_SHORT).show();
                    return;
                }

                AppHelper.getPool().getUser(email).confirmSignUpInBackground(code, true, confHandler);
            }
        });

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
                if(email.isEmpty()){
                    Toast.makeText(ConfirmAccountActivity.this, "Debe colocar un correo electrónico", Toast.LENGTH_SHORT).show();
                    return;
                }

                AppHelper.getPool().getUser(email).resendConfirmationCodeInBackground(resendConfCodeHandler);
            }
        });
    }

    GenericHandler confHandler = new GenericHandler() {
        @Override
        public void onSuccess() {
            showDialogMessage("Éxito!","Su correo ha sido confirmado!", true);
        }

        @Override
        public void onFailure(Exception exception) {

            showDialogMessage("Confirmacion fallida", "Ha ocurrido un error", false);
        }
    };

    VerificationHandler resendConfCodeHandler = new VerificationHandler() {
        @Override
        public void onSuccess(CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            etCode.requestFocus();
            showDialogMessage("Se ha enviado el código","Código enviado a "+cognitoUserCodeDeliveryDetails.getDestination()+" via "+cognitoUserCodeDeliveryDetails.getDeliveryMedium()+".", false);
        }

        @Override
        public void onFailure(Exception exception) {
            showDialogMessage("Confirmacion fallida", "Ha ocurrido un error", false);
        }
    };

    private void showDialogMessage(String title, String body, final boolean exitActivity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                    if(exitActivity) {
                        exit();
                    }
                } catch (Exception e) {
                    exit();
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }

    private void exit() {
        Intent intent = new Intent();
        if(email == null)
            email = "";
        intent.putExtra("name",email);
        setResult(RESULT_OK, intent);
        finish();
    }
}
