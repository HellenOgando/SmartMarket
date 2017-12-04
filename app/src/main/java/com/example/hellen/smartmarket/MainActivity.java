package com.example.hellen.smartmarket;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;
import com.example.hellen.smartmarket.Controllers.ManagerClass;
import com.example.hellen.smartmarket.Models.Book;


public class MainActivity extends AppCompatActivity {

    Button btnAddBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddBook = (Button) findViewById(R.id.btnAddBook);

        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new writeTable().execute();
            }
        });
    }

    private class writeTable extends AsyncTask<Void, Integer, Integer>{

        @Override
        protected Integer doInBackground(Void... voids) {

                ManagerClass managerClass = new ManagerClass();
                CognitoCachingCredentialsProvider credentialsProvider = managerClass.getCredentials(MainActivity.this);

                Book book = new Book();
                book.setTitle("Great Expectations");
                book.setAuthor("Charles Dickens");
                book.setPrice(1299);
                book.setIsbn("1234567890");
                book.setHardCover(false);

                if (credentialsProvider != null && book != null) {
                    DynamoDBMapper dynamoDBMapper = managerClass.initDynamoClient(credentialsProvider);
                    dynamoDBMapper.save(book);
                } else {
                    return 2;
                }
                return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer == 1){
                Toast.makeText(MainActivity.this, "Table Updated", Toast.LENGTH_SHORT).show();
            }else if(integer == 2){
                Toast.makeText(MainActivity.this, "An error ocurred", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

