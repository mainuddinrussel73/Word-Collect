package com.example.mainuddin.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class quiz_page extends AppCompatActivity {

    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private Button btnDisplay,nxt,previousquiz;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private TextView textView,scoress;
    static int score = 0;


    List<String> word = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_list);

        final Intent intent = getIntent();
        score = intent.getIntExtra("s",0);
        scoress = findViewById(R.id.scores);
        scoress.setText(String.valueOf(score));
        btnDisplay = findViewById(R.id.btnDisplay);
        btnDisplay.setEnabled(true);
        textView = findViewById(R.id.quiz_question);

        while (word.isEmpty()){
            word = randomword();
        }
        textView.setText(word.get(0));

        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup);

        for (int i = 0; i < radioGroup .getChildCount(); i++) {


            ((RadioButton) radioGroup.getChildAt(i)).setText(randommean());
        }

        double randomDouble = Math.random();
        randomDouble = randomDouble * 3 + 0;
        int randomInt = (int) randomDouble;
        ((RadioButton) radioGroup.getChildAt(randomInt)).setText(word.get(1));


        addListenerOnButton(word);
        nxt = findViewById(R.id.nextquiz);

        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                word.clear();
                Intent myIntent = new Intent(view.getContext(), quiz_page.class);
                //String s = view.findViewById(R.id.subtitle).toString();
                //String s = (String) parent.getI;
                myIntent.putExtra("s",score);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);

            }
        });
        previousquiz = findViewById(R.id.previousquiz);
        previousquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                word.clear();
                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                //String s = view.findViewById(R.id.subtitle).toString();
                //String s = (String) parent.getI;
                myIntent.putExtra("sb",score);
                MainActivity.contactList.clear();
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);

            }
        });


    }

    public void addListenerOnButton(List<String> s) {

        radioSexGroup = (RadioGroup) findViewById(R.id.radioGroup);
        final List<String> sr = s;
        int selectedId = radioSexGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        //btnDisplay.setEnabled(true);
        System.out.println(selectedId);

        btnDisplay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {




                int selectedId = radioSexGroup.getCheckedRadioButtonId();

                if(selectedId>0){
                    btnDisplay.setEnabled(true);
                    radioSexButton = (RadioButton) findViewById(selectedId);

                    if(sr.get(1).equals(radioSexButton.getText())){
                        Toast.makeText(quiz_page.this,
                                "congrats", Toast.LENGTH_SHORT).show();


                        for (int i = 0; i < radioSexGroup .getChildCount(); i++) {


                            if(sr.get(1).equals(((RadioButton) radioSexGroup.getChildAt(i)).getText())){
                                radioSexGroup.getChildAt(i).setBackgroundColor(Color.GREEN);
                                //rb_flash.setTextColor(Color.BLACK);
                            }

                        }
                        score++;
                        btnDisplay.setEnabled(false);

                        scoress.setText(String.valueOf(score));
                    }else {
                        Toast.makeText(quiz_page.this,
                                "Alas!", Toast.LENGTH_SHORT).show();


                        for (int i = 0; i < radioSexGroup .getChildCount(); i++) {


                            if(sr.get(1).equals(((RadioButton) radioSexGroup.getChildAt(i)).getText())){
                                  radioSexGroup.getChildAt(i).setBackgroundColor(Color.RED);
                                //rb_flash.setTextColor(Color.BLACK);
                            }

                        }

                        score--;
                        if(score<0)score = 0;

                        scoress.setText(String.valueOf(score));
                        btnDisplay.setEnabled(false);
                    }
                }else {
                    Toast.makeText(quiz_page.this,
                            "Select an option.", Toast.LENGTH_SHORT).show();
                }
                // find the radiobutton by returned id



            }

        });


    }
    public List<String> randomword(){
        List<String> str = new ArrayList<>();
        double randomDouble = Math.random();
        randomDouble = randomDouble * MainActivity.size + 1;
        int randomInt = (int) randomDouble;

        mDBHelper = new DatabaseHelper(quiz_page.this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        mDb = mDBHelper.getWritableDatabase();



        // Select All Query
        String selectQuery = "SELECT  * FROM " + "Words_table";

        String query = "SELECT * FROM Words_table WHERE ID =" + randomInt;
        System.out.println(randomInt);

        Cursor  cursor = mDb.rawQuery(query,null);

        if (cursor != null&& cursor.moveToFirst()) {
            str.add(cursor.getString(cursor.getColumnIndex("WORD")));
            str.add(cursor.getString(cursor.getColumnIndex("MEANING")));
            cursor.close();
        }
        return str;
    }

    public String randommean(){
        String str = new String();
        double randomDouble = Math.random();
        randomDouble = randomDouble * MainActivity.size + 1;
        int randomInt = (int) randomDouble;

        mDBHelper = new DatabaseHelper(quiz_page.this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        mDb = mDBHelper.getWritableDatabase();



        // Select All Query
        String selectQuery = "SELECT  * FROM " + "Words_table";

        String query = "SELECT * FROM Words_table WHERE ID =" + randomInt;
        System.out.println(randomInt);

        Cursor  cursor = mDb.rawQuery(query,null);

        if (cursor != null&& cursor.moveToFirst()) {
            str = cursor.getString(cursor.getColumnIndex("MEANING"));
            cursor.close();
        }
        return str;
    }
}
