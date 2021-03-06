package com.example.matthew.mobilecoursework;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Created by matthew on 02/12/2015.
 */
public class RssActivity extends AppCompatActivity {
    FragmentManager fmAboutDialgue;// needed for about

    private RssActivity local;// used to  store this class to be called in the async class later on
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);

        local = this;

        GetRSSDataTask task = new GetRSSDataTask();

        // Start download of feed in new thread
        task.execute("http://www.metoffice.gov.uk/public/data/PWSCache/WarningsRSS/Region/UK");

        fmAboutDialgue = this.getFragmentManager();
    }

    //Needs to be in a subclass to extend Async
    private class GetRSSDataTask extends AsyncTask<String, Void, List<RssItem> > {
        @Override
        protected List<RssItem> doInBackground(String... urls) {

            try {
                // Create RSS reader
                RssReader rssReader = new RssReader(urls[0]);

                // Parse RSS, get items
                return rssReader.getItems();

            } catch (Exception e) {
                Log.e("Cannot Load Rss Feed: ", e.getMessage());

                //ToDo: add a toast message when feed cant be loaded
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<RssItem> result) {

            // Get a ListView from main view
            ListView listView = (ListView) findViewById(R.id.listView2);

            // Create a list adapter
            ArrayAdapter<RssItem> adapter = new ArrayAdapter<RssItem>(local,android.R.layout.simple_list_item_1, result);
            // Set list adapter for the ListView
            listView.setAdapter(adapter);

        }

    }


    //Standard Code for menu used in all classes.
    //ToDo: Need to find a way of not repeating this code!
    //ToDo: This is the answer http://stackoverflow.com/questions/15775831/dont-repeat-menu-code
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.mMain:
                Intent mcMain = new Intent(this, MainActivity.class);
                this.startActivity(mcMain);
                return true;
            case R.id.mRss:
                Intent mcRss = new Intent(this, RssActivity.class);
                this.startActivity(mcRss);
                return true;
            case R.id.mAbout:
                DialogFragment mcAboutDlg = new clsAbout();
                mcAboutDlg.show(fmAboutDialgue, "mcAboutDlg");
                return true;
            case R.id.mSound:
                Intent mcSound = new Intent(this, clsSoundboard.class);
                this.startActivity(mcSound);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
