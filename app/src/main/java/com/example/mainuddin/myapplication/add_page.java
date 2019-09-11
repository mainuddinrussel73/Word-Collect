package com.example.mainuddin.myapplication;

import android.app.AppComponentFactory;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

public class add_page extends AppCompatActivity {



    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
     EditText word ;
     EditText meaning;
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        word =  (EditText) findViewById(R.id.word1);
        meaning = (EditText) findViewById(R.id.meaning1);

        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        mDb = mDBHelper.getWritableDatabase();
        done = (Button) findViewById(R.id.done);


        done.setOnClickListener(new View.OnClickListener() {

            int i = 0;
            @Override
            public void onClick(View v) {
                boolean b = insertData(word.getText().toString(),meaning.getText().toString());
                if(b==true){
                    Toast.makeText(getApplicationContext(),"Done.",Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(myIntent, 0);
                }else{
                    Toast.makeText(getApplicationContext(),"opps.",Toast.LENGTH_SHORT).show();
                }



            }
        });
    }

    public boolean insertData(String words,String meanings) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("WORD",words);
        contentValues.put("MEANING",meanings);
        long result = mDb.insert("Words_table",null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
}
