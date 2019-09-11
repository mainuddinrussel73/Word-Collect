package com.example.mainuddin.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;


public class word_details extends AppCompatActivity {


    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private Button update,delete;
    String words,meanings;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item_details);
        final Intent intent = getIntent();
        final EditText word = (EditText) findViewById(R.id.word);
        final EditText meaning = (EditText) findViewById(R.id.meaning);
        word.setText(intent.getStringExtra("message"));
        meaning.setText(intent.getStringExtra("meaning"));


        word.setEnabled(false);
        meaning.setEnabled(false);
        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        mDb = mDBHelper.getWritableDatabase();

        update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);

        update.setOnClickListener(new View.OnClickListener() {

            int i = 0;
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                i++;


                if (i == 1) {
                    word.setEnabled(true);
                    meaning.setEnabled(true);
                } else if (i == 2) {
                    //Double click
                    i = 0;
                    words = word.getText().toString();
                    meanings = meaning.getText().toString();
                    id = intent.getExtras().getInt("id");
                    id++;
                    boolean b = updateEntry(String.valueOf(id),words,meanings);
                    if(b==true){
                        Toast.makeText(getApplicationContext(),"Done.",Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(myIntent, 0);
                    }else {
                        Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                    }
                    word.setEnabled(false);
                    meaning.setEnabled(false);
                }


            }
        });
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                id = intent.getExtras().getInt("id");
                id++;
                int b = deleteData(String.valueOf(id));
                if(b==1){
                    Toast.makeText(getApplicationContext(),"Done.",Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(myIntent, 0);
                }else {
                    Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    public Integer deleteData (String id) {
        return mDb.delete("Words_table", "ID = ?",new String[] {id});
    }


    public boolean  updateEntry(String ID,String words, String meaning)
    {

        ContentValues updatedValues = new ContentValues();
        updatedValues.put("ID",ID);
        updatedValues.put("WORD", words);
        updatedValues.put("MEANING", meaning);
        String where="ID = ?";


        mDb=mDBHelper.getReadableDatabase();
       int b =  mDb.update("Words_table",updatedValues,"ID = ?",new String[] {ID});


        mDb.close();
        if(b==1)return true;
        else return false;

    }

}