package com.example.hellen.smartmarket;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.example.hellen.smartmarket.Controllers.ManagerClass;
import com.example.hellen.smartmarket.Models.Order;
import com.example.hellen.smartmarket.Models.Product;

import java.util.ArrayList;
import java.util.List;

import be.appfoundry.nfclibrary.utilities.sync.NfcReadUtilityImpl;
import mehdi.sakout.fancybuttons.FancyButton;

public class NFC2Activity extends AppCompatActivity{
    private PendingIntent pendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mTechLists;
    private NfcAdapter mNfcAdapter;
    private String prodID;
    List<Product> order = new ArrayList<Product>();
    private Product product;
    private FancyButton btnCheckOut;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc2);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mIntentFilters = new IntentFilter[]{new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)};
        mTechLists = new String[][]{new String[]{Ndef.class.getName()},
                new String[]{NdefFormatable.class.getName()}};

        btnCheckOut = (FancyButton) findViewById(R.id.btnCheckOut);
    }
    public void onResume(){
        super.onResume();
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, mIntentFilters, mTechLists);
        }

        if(order.size() > 0){
            btnCheckOut.setVisibility(View.VISIBLE);
        }
    }
    public void onPause(){
        super.onPause();
        if (mNfcAdapter != null)
        {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SparseArray<String> res = new NfcReadUtilityImpl().readFromTagWithSparseArray(intent);
        for (int i =0; i < res.size() ; i++ ) {
            prodID = res.valueAt(i);
        }

        new getProduct().execute();
    }

    private class getProduct extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {

            ManagerClass managerClass = new ManagerClass();
            CognitoCachingCredentialsProvider credentialsProvider = managerClass.getCredentials(NFC2Activity.this);

            if (credentialsProvider != null) {
                DynamoDBMapper dynamoDBMapper = managerClass.initDynamoClient(credentialsProvider);
                product = dynamoDBMapper.load(Product.class, prodID);
            } else {
                return 2;
            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer == 1){
                Toast.makeText(NFC2Activity.this, "Product Scanned", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(NFC2Activity.this, ProductActivity.class);
                Log.d("onPostExecute: ", product.getProductDesc());
                i.putExtra("prodDesc", product.getProductDesc());
                i.putExtra("prodPrice", product.getProductPrice());
                i.putExtra("prodQuantity", product.getProductQuantity());
                startActivity(i);

            }else if(integer == 2){
                Toast.makeText(NFC2Activity.this, "An error ocurred", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
