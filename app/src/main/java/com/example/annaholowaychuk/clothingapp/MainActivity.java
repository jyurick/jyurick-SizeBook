package com.example.annaholowaychuk.clothingapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.example.annaholowaychuk.clothingapp.R.id.peopleList;

/**
 * This class handles the user interaction display for the list view of Person objects.
 * It also performs the deleting, adding and saving of Person objects in the file.
 *
 */

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String FILENAME = "sizeBook.sav";

    private ListView peopleList;
    private TextView counter;

    private ArrayList<Person> personsList;
    private ArrayAdapter<Person> adapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * This class sets the functionality of the two buttons, Clear and Add Person. Clear will
     * delete all Persons in the file. Add Person will bring the user to a new edit person activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addPerson = (Button) findViewById(R.id.add_person);
        peopleList = (ListView) findViewById(R.id.peopleList);
        counter = (TextView) findViewById(R.id.countText);
        Button clearPeople = (Button) findViewById(R.id.clear_people);
        peopleList.setOnItemClickListener(MainActivity.this);

        addPerson.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditPerson.class);
                intent.putExtra("status", "new");
                startActivityForResult(intent, 1);

            }
        });

        clearPeople.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                personsList.clear();
                adapter.notifyDataSetChanged();
                saveInFile();
                counter.setText(Integer.toString(personsList.size()));
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * If a Person object is clicked from the list view, the edit person activity will begin
     * and that Person's data will be loaded into the data fields.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    //Taken and modified from http://stackoverflow.com/questions/13281197/android-how-to-create-clickable-listview on Feb. 5, 2017
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Person person = personsList.get(position);
        Gson gS = new Gson();
        String target = gS.toJson(person);

        Intent i = new Intent(MainActivity.this, EditPerson.class);
        i.putExtra("Person", target);
        i.putExtra("status", "existing");
        i.putExtra("index", Integer.toString(position));
        startActivityForResult(i, 1);
    }

    /**
     * Retrieves the result from EditPerson activity. Based on the result,
     * this function will do one of the three:
     * 1. Add a new Person to the list
     * 2. Modify a Person in the list
     * 3. Delete a person from the list
     * @param requestCode
     * @param resultCode
     * @param data
     */
    //Taken and modified from http://www.javatpoint.com/android-startactivityforresult-example on Feb 5, 2017
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==1)
        {
            if (data.getStringExtra("status").equals("existing")) {
                String index = data.getStringExtra("index");
                String name=data.getStringExtra("Person");
                Gson gS = new Gson();
                Person test = gS.fromJson(name, Person.class);

                if (test.getName() != "") {
                    personsList.remove(Integer.valueOf(index));
                    personsList.set(Integer.valueOf(index), test);
                    adapter.notifyDataSetChanged();
                    saveInFile();
                    counter.setText(Integer.toString(personsList.size()));
                }
            }
            if (data.getStringExtra("status").equals("new")){
                String name = data.getStringExtra("Person");
                Gson gS = new Gson();
                Person test = gS.fromJson(name, Person.class);
                if (test.getName() != "") {
                    personsList.add(test);
                    adapter.notifyDataSetChanged();
                    saveInFile();
                    counter.setText(Integer.toString(personsList.size()));
                }
            }
            if (data.getStringExtra("status").equals("delete")){
                Person p = new Person();
                int index = Integer.valueOf(data.getStringExtra("index"));
                personsList.remove(index);
                adapter.notifyDataSetChanged();
                saveInFile();
                counter.setText(Integer.toString(personsList.size()));
            }
        }




    }

    /**
     * Called when the application is first started.
     */
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        loadFromFile();
        counter.setText(Integer.toString(personsList.size()));
        adapter = new ArrayAdapter<Person>(this,
                R.layout.list_item, personsList);
        peopleList.setAdapter(adapter);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    /**
     * Trims extra spaces using regular expression.
     *
     * @param inputString
     * @return
     */
    private String trimExtraSpaces(String inputString) {
        inputString = inputString.replaceAll("\\s+", " ");
        return inputString;
    }


    /**
     * Loads personsList from specified file.
     *
     *
     * @throws FileNotFoundException if the file doesn't exist.
     */
    private void loadFromFile() {

        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<Person>>() {
            }.getType();

            personsList = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            personsList = new ArrayList<Person>();
            //new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            new RuntimeException();
        }

    }

    /**
     * Saves personsList to specified file in JSON format.
     *
     * @throws FileNotFoundException if file folder doesn't exist
     */
    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(personsList, out);
            out.flush();

            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
