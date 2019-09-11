package com.example.mainuddin.myapplication;



import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;



public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    ListView list;
    public static final int PICKFILE_RESULT_CODE = 1;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private Button insert,backup,restore,ok,quiz;
    private EditText retext;
    SearchView searchView;
    MyListAdapter adapter;
    static int size;
    TextView textView;
    static int bestscore = 0;

    private Uri fileUri;
    private String filePath;
  //  static List<String> maintitle = new ArrayList<>();
    //static List<String> subtitle =  new ArrayList<>();
    static List<word> contactList = new ArrayList<word>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_main);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }catch (Exception e){
            Log.d("error",e.getMessage());
        }



        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        mDb = mDBHelper.getWritableDatabase();



        // Select All Query
        String selectQuery = "SELECT  * FROM " + "Words_table";


        final Cursor cursor = mDb.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                word word = new word();
                word.ID = Integer.parseInt(cursor.getString(0));
                word.WORD = cursor.getString(1);
                word.MEANING = cursor.getString(2);

               contactList.add(word);

               // maintitle.add(word.WORD);
               // subtitle.add(word.MEANING);
            } while (cursor.moveToNext());
        }

        size = contactList.size();
        adapter=new MyListAdapter(this);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        textView = findViewById(R.id.board);
        final Intent intent = getIntent();
        bestscore = (intent.getIntExtra("sb",0)> bestscore)?intent.getIntExtra("sb",0) :bestscore;
        textView.setText("Total Words : \n"+String.valueOf(contactList.size())+"\nCurrent score : \n"+String.valueOf(bestscore));




        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent(view.getContext(), word_details.class);
                //String s = view.findViewById(R.id.subtitle).toString();
                //String s = (String) parent.getI;
                myIntent.putExtra("message",contactList.get(position).WORD);
                myIntent.putExtra("meaning",contactList.get(position).MEANING);
                myIntent.putExtra("id",position);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);

            }
        });




        searchView = (SearchView) findViewById(R.id.searchView);


        searchView.setOnQueryTextListener(this);



        // return contact list
        insert = (Button) findViewById(R.id.insert);
        insert.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent(view.getContext(), add_page.class);
                //String s = view.findViewById(R.id.subtitle).toString();
                //String s = (String) parent.getI;
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);


            }
        });

        // return contact list
        backup = (Button) findViewById(R.id.backup);

        backup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                File sd = Environment.getExternalStorageDirectory();
                File data = Environment.getDataDirectory();
                FileChannel source=null;
                FileChannel destination=null;
                String currentDBPath = "/data/"+ getPackageName() +"/databases/"+DatabaseHelper.DB_NAME;
                String retreivedDBPAth = getDatabasePath(DatabaseHelper.DB_NAME).getPath();
                String backupDBPath = "/storage/extSdCard/mydatabase";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath+".db");
                File retrievedDB = new File(retreivedDBPAth);
                Log.d("PATHS", " CurrentDB=" +
                        currentDBPath + "\n\t" + currentDB.getPath() +
                        "\n\tExists=" + String.valueOf(currentDB.exists()) +
                        "\nBackup=" + backupDBPath + "\n\t" + backupDB.getPath() +
                        "\n\tExists=" + String.valueOf(backupDB.exists()) +
                        "\nRetrieved DB=" + retreivedDBPAth + "\n\t" + retrievedDB.getPath() +
                        "\n\tExists=" + String.valueOf(retrievedDB.exists())
                );
                try {
                    source = new FileInputStream(currentDB).getChannel();
                    (new File(backupDB.getParent())).mkdirs(); //<<<<<<<<<<<<<< ADDED
                    destination = new FileOutputStream(backupDB).getChannel();
                    destination.transferFrom(source, 0, source.size());
                    source.close();
                    destination.close();
                    Toast.makeText(getApplicationContext(),"Done.",Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(myIntent, 0);
                } catch(IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Opps."+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                /* use date including file name for arrange them and preventing to make file with the same*/


            }


        });

        restore = (Button) findViewById(R.id.restore);
        ok = (Button) findViewById(R.id.click);
        retext = findViewById(R.id.retext);
        ok.setEnabled(false);
        restore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                    Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseFile.setType("*/*");
                    chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                    startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);

                  //  Toast.makeText(onNewIntent();)
                retext.setText(filePath);
                ok.setEnabled(true);




            }


        });




        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                File DbFile=new File("data/data/com.example.mainuddin.myapplication/word.db");

                File sd = Environment.getExternalStorageDirectory();
                File data = Environment.getDataDirectory();
                FileChannel source=null;
                FileChannel destination=null;
                String currentDBPath = "/data/"+ getPackageName() +"/databases/"+DatabaseHelper.DB_NAME;
                String currentDB = getDatabasePath(DatabaseHelper.DB_NAME).getPath();
                String[] parts = fileUri.getLastPathSegment().split(":");
                String part1 = parts[0]; // 004
                String part2 = parts[1]; // 0

                if(!part2.contains(".db")){
                    Toast.makeText(getApplicationContext(),"Use : "+".db"+" Files.",Toast.LENGTH_SHORT).show();

                }else{
                    String retreivedDBPAth= Environment.getExternalStorageDirectory()+"/"+part2;
                    //File currentDB = new File(data, currentDBPath);
                   // File backupDB = new File(sd, backupDBPath+".db");
                    File retrievedDB = new File(retreivedDBPAth);
                    System.out.println(fileUri.getLastPathSegment());
                    try {
                        source = new FileInputStream(retrievedDB).getChannel();
                        destination = new FileOutputStream(currentDB).getChannel();
                        destination.transferFrom(source, 0, source.size());
                        source.close();
                        destination.close();
                        Toast.makeText(getApplicationContext(),"Done.",Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(myIntent, 0);

                    } catch(IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Opps."+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }





            }


        });

        quiz = findViewById(R.id.quiz);
        quiz.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent myIntent = new Intent(v.getContext(), quiz_page.class);
                //String s = view.findViewById(R.id.subtitle).toString();
                //String s = (String) parent.getI;
                myIntent.putExtra("s",0);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);




            }


        });





    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    fileUri = data.getData();
                    filePath = fileUri.getPath();

                }

                break;
        }

    }

    public static Boolean ExportDB(String DATABASE_NAME , String packageName , String folderName){
        //DATABASE_NAME including ".db" at the end like "mayApp.db"
        String DBName = DATABASE_NAME.substring(0, DATABASE_NAME.length() - 3);
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = "/data/"+ packageName +"/databases/"+DATABASE_NAME; // getting app db path

        File sd = Environment.getExternalStorageDirectory(); // getting phone SD card path
        String backupPath = sd.getAbsolutePath() + folderName; // if you want to set backup in specific folder name
        /* be careful , foldername must initial like this : "/myFolder" . dont forget "/" at begin of folder name
            you could define foldername like this : "/myOutterFolder/MyInnerFolder" and so on ...
        */
        File dir = new File(backupPath);
        if(!dir.exists()) // if there was no folder at this path , it create it .
        {
            dir.mkdirs();
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        Date date = new Date();
        /* use date including file name for arrange them and preventing to make file with the same*/
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(backupPath, DBName +"("+ dateFormat.format(date)+").db");
        try {
            if (currentDB.exists() && !backupDB.exists()) {
                source = new FileInputStream(currentDB).getChannel();
                destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
                return true;
            }
            return false;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.filter(s);
        return false;
    }
}


