package com.spiralzz.mma.locationmanagerold;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HistoryActivity extends ActionBarActivity {

    private ListView lv;

    List<String> names;
    ArrayAdapter<String> arrayAdapter;

    Bundle savedInstanceState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history2);

        this.savedInstanceState = savedInstanceState;
        lv = (ListView) findViewById(R.id.listView);


        names = new ArrayList<String>();

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        List<Location> locations = db.getAllLocations();

        for (Location location : locations) {
            String log = "Id: "+location.getID()+" ,Name: " + location.getName() + " ,Coordinates: " + location.getCoordinates();
            // Writing Contacts to log





            Log.d("Name: ", log);
            names.add(location.getName() + " (" + location.getCoordinates() + ")");
        }





            arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    names);

            lv.setAdapter(arrayAdapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (lv.getItemAtPosition(position).toString());
                String answer = str.substring(str.indexOf("(")+1,str.indexOf(")"));

                Context context = getApplicationContext();
                //CharSequence text = "Hello toast!";
                int duration = Toast.LENGTH_SHORT;



                ClipboardManager clipboard = (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);

                ClipData clip = ClipData.newPlainText("text", answer);
                clipboard.setPrimaryClip(clip);

                Toast toast = Toast.makeText(context, answer + " have been copied", duration);
                toast.show();

                List<String> items = Arrays.asList(answer.split("\\s*,\\s*"));

                toast = Toast.makeText(context, "geo:" + items.get(0) + "," + items.get(1), duration);
                toast.show();

                Uri gmmIntentUri = Uri.parse("geo:" + items.get(0) + "," + items.get(1) + "?q=" + items.get(0)+","+items.get(1));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.clear_history) {
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            db.delete();

            finish();
            startActivity(getIntent());



        }

        return super.onOptionsItemSelected(item);
    }
}
