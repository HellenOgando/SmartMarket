package com.example.hellen.smartmarket;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NfcAdapter;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import be.appfoundry.nfclibrary.activities.NfcActivity;
import be.appfoundry.nfclibrary.exceptions.InsufficientCapacityException;
import be.appfoundry.nfclibrary.exceptions.ReadOnlyTagException;
import be.appfoundry.nfclibrary.exceptions.TagNotPresentException;
import be.appfoundry.nfclibrary.tasks.interfaces.AsyncOperationCallback;
import be.appfoundry.nfclibrary.tasks.interfaces.AsyncUiCallback;
import be.appfoundry.nfclibrary.utilities.async.WriteCallbackNfcAsync;
import be.appfoundry.nfclibrary.utilities.async.WriteEmailNfcAsync;
import be.appfoundry.nfclibrary.utilities.interfaces.NfcReadUtility;
import be.appfoundry.nfclibrary.utilities.interfaces.NfcWriteUtility;
import be.appfoundry.nfclibrary.utilities.sync.NfcReadUtilityImpl;

public class AdminActivity extends NfcActivity {

    private static final String TAG = AdminActivity.class.getName();

    NfcReadUtility mNfcReadUtility = new NfcReadUtilityImpl();
    ProgressDialog mProgressDialog;

    AsyncUiCallback mAsyncUiCallback = new AsyncUiCallback() {
        @Override
        public void callbackWithReturnValue(Boolean result) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (result) {
                Toast.makeText(AdminActivity.this, "Write has been done!", Toast.LENGTH_LONG).show();
            }

            Log.d(TAG,"Received our result : " + result);

        }

        @Override
        public void onProgressUpdate(Boolean... values) {
            if (values.length > 0 && values[0] && mProgressDialog != null) {
                mProgressDialog.setMessage("Writing");
                Log.d(TAG,"Writing !");
            }
        }

        @Override
        public void onError(Exception e) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            Log.i(TAG,"Encountered an error !",e);
        }
    };

    AsyncOperationCallback mAsyncOperationCallback;
    private AsyncTask<Object, Void, Boolean> mTask;

    private Button btnAdd;
    private EditText etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        etText = (EditText) findViewById(R.id.etText);
        btnAdd = (Button) findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String text = etText.getText().toString();
                if (text.length() > 0) {

                    mAsyncOperationCallback = new AsyncOperationCallback() {
                        @Override
                        public boolean performWrite(NfcWriteUtility writeUtility) throws ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException, FormatException {
                            return writeUtility.writeTextToTagFromIntent(text, getIntent());
                        }
                    };
                    showDialog();
                } else {
                    showNoInputToast();
                }
            }
        });

        enableBeam();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getNfcAdapter() != null) {
            getNfcAdapter().disableForegroundDispatch(this);
        }
    }

    @Override
    public void onNewIntent(final Intent paramIntent) {
        super.onNewIntent(paramIntent);

        if (mAsyncOperationCallback != null && mProgressDialog != null && mProgressDialog.isShowing()) {
            new WriteCallbackNfcAsync(mAsyncUiCallback, mAsyncOperationCallback).executeWriteOperation();
            mAsyncOperationCallback = null;
        } else {
            for (String data : mNfcReadUtility.readFromTagWithMap(paramIntent).values()) {
                Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showNoInputToast() {
        Toast.makeText(this,"No Input", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        if (mTask != null) {
            mTask.cancel(true);
            mTask = null;
        }
    }

    public void showDialog() {
        mProgressDialog = new ProgressDialog(AdminActivity.this);
        mProgressDialog.setTitle("Waiting for Tag");
        mProgressDialog.setMessage("Waiting for Tag");
        mProgressDialog.show();
    }

    private TextView retrieveElement(int id) {
        TextView element = (TextView) findViewById(id);
        return (element != null) && ((TextView) findViewById(id)).getText() != null && !"".equals(((TextView) findViewById(id)).getText().toString()) ? element : null;
    }

}
