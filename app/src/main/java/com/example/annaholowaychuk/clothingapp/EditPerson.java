package com.example.annaholowaychuk.clothingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This class provides the user interface for editing a Person object.
 */
public class EditPerson extends AppCompatActivity {
    private EditText nameText;
    private EditText dateText;
    private EditText neckText;
    private EditText bustText;
    private EditText chestText;
    private EditText waistText;
    private EditText hipText;
    private EditText inseamText;
    private EditText commentText;
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_person);
        Button savePerson = (Button) findViewById(R.id.save_person);
        Button deletePerson = (Button) findViewById(R.id.delete_person);

        nameText = (EditText) findViewById(R.id.nameText);
        dateText = (EditText) findViewById(R.id.dateText);
        neckText = (EditText) findViewById(R.id.neckText);
        bustText = (EditText) findViewById(R.id.bustText);
        chestText = (EditText) findViewById(R.id.chestText);
        waistText = (EditText) findViewById(R.id.waistText);
        hipText = (EditText) findViewById(R.id.hipText);
        inseamText = (EditText) findViewById(R.id.inseamText);
        commentText = (EditText) findViewById(R.id.commentText);

        Intent intent = getIntent();
        String status = intent.getStringExtra("status");



        if (status.equals("existing")) {
            String json=intent.getStringExtra("Person");
            Gson gS = new Gson();
            person = gS.fromJson(json, Person.class);

            dateText.setText(dateToString(person.getDate()));
            nameText.setText(person.getName());
            neckText.setText(person.getNeck());
            bustText.setText(person.getBust());
            chestText.setText(person.getChest());
            waistText.setText(person.getWaist());
            hipText.setText(person.getHip());
            inseamText.setText(person.getInseam());
            commentText.setText(person.getComment());
            //TODO set date

        }

        /**
         * When Delete button is clicked, finish activity with result of "delete" and the index
         * of the person to be deleted.
         */
        deletePerson.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent i = new Intent();
                if (getIntent().getStringExtra("status").equals("existing")){
                    i.putExtra("index", getIntent().getStringExtra("index"));
                    i.putExtra("status", "delete");
                    setResult(1, i);
                    finish();
                }
                else {
                    i.putExtra("status","nothing");
                    setResult(1, i);
                    finish();
                }
            }
        });

        /**
         * When the Save button is clicked, create a new Person object and set attributes
         * according to the data fields. Then close app and send Person object as result back
         * to MainActivity.
         */
        savePerson.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                person = new Person();
                //String name = nameText.getText().toString();
                person.setName(nameText.getText().toString());

                //TODO add dateText
                person.setDate(stringToDate(dateText.getText().toString()));
                if (!stringEmpty(neckText.getText().toString())) {
                    person.setNeck(Float.valueOf(neckText.getText().toString()));
                }
                if (!stringEmpty(bustText.getText().toString())) {
                    person.setBust(Float.valueOf(bustText.getText().toString()));
                }
                if (!stringEmpty(chestText.getText().toString())) {
                    person.setChest(Float.valueOf(chestText.getText().toString()));
                }
                if (!stringEmpty(waistText.getText().toString())) {
                    person.setWaist(Float.valueOf(waistText.getText().toString()));
                }
                if (!stringEmpty(hipText.getText().toString())) {
                    person.setHip(Float.valueOf(hipText.getText().toString()));
                }
                if (!stringEmpty(inseamText.getText().toString())) {
                    person.setInseam(Float.valueOf(inseamText.getText().toString()));
                }
                person.setComment(commentText.getText().toString());


                Gson gS = new Gson();
                String target = gS.toJson(person);

                Intent i = new Intent();
                i.putExtra("Person", target);
                if (getIntent().getStringExtra("status").equals("existing")){
                    i.putExtra("index", getIntent().getStringExtra("index"));
                    i.putExtra("status", "existing");
                    setResult(1, i);
                    finish();
                }
                else {
                    i.putExtra("status", "new");
                    setResult(1, i);
                    finish();
                }

            }
        });

    }

    /**
     * Checks whether a given string has only numerical values
     * @param str
     * @return bool
     */
    //Taken from http://stackoverflow.com/questions/14206768/how-to-check-if-a-string-is-numeric on Feb. 6, 2017
    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    /**
     * Returns True is string is empty, False if not
     * @param s
     * @return
     */
    public Boolean stringEmpty (String s) {
        if (s.equals("")) {return true;}
        else{ return false;}
    }

    /**
     * Converts a date into a string with format yyyy-mm-dd
     * @param d
     * @return
     */
    public String dateToString(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String year = Integer.toString(cal.get(Calendar.YEAR));
        String month = Integer.toString(cal.get(Calendar.MONTH));
        String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        if (month.length() != 2) {
            month = "0"+month;
        }
        if (day.length() != 2) {
            day = "0"+day;
        }

        return year+"-"+month+"-"+day;
    }

    /**
     * Expects a String representing a date with format yyyy-mm-dd. Converts that string into
     * a date value. If the String is in the incorrect format it returns the current date.
     * @param s
     * @return
     */
    public Date stringToDate (String s) {
        Date d = null;
        String[] split;
        split = s.split("-");
        if (split.length != 3) {
            return d;
        }
        else if (!(isNumeric(split[0])&&isNumeric(split[1])&&isNumeric(split[2]))){
            return d;
        }
        int year = Integer.valueOf(split[0]);
        int month = Integer.valueOf(split[1]);
        int day = Integer.valueOf(split[2]);

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        return cal.getTime();

    }
}
