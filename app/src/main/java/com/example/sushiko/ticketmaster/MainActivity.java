package com.example.sushiko.ticketmaster;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ticketmaster.api.discovery.DiscoveryApi;
import com.ticketmaster.api.discovery.operation.SearchEventsOperation;
import com.ticketmaster.api.discovery.response.PagedResponse;
import com.ticketmaster.discovery.model.Event;
import com.ticketmaster.discovery.model.Events;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText et_search;
    TextView title,info,date,venue;
    Event event;
    public String apikey = "KLEDbDrUK3YCMbgO1eQQHPIRhoVhkxBA";
    public DiscoveryApi api = new DiscoveryApi(apikey);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_search = findViewById(R.id.search_bar);
        title = findViewById(R.id.tv_title);
        info = findViewById(R.id.tv_info);
        date = findViewById(R.id.tv_date);
        venue = findViewById(R.id.tv_venue);
        event=new Event();
    }


    public void onClick(View view) {


        new Search(event).execute(et_search.getText().toString());
    }

    public class Search extends AsyncTask<String,String,String>{
        String when, what, where;
        Event event;

        Search(Event event){
            this.event = event;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                PagedResponse<Events> page = null;
                page = api.searchEvents(new SearchEventsOperation().keyword(strings[0]));
                List<Event> events = page.getContent().getEvents();

                event = events.get(1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // Set Title
            title.setText(event.getName());

            // Set Dates
            when = event.getDates().getStart().getLocalDate()+" @ "+event.getDates().getStart().getLocalTime();
            date.setText(when);

            // Get Info
            if(event.getInfo()!=null){info.setText(event.getInfo());}
            else{info.setText("No description available.");}

            // Get Location
            //where = event.getPlace().getCity().getName();
            //venue.setText(where);
        }
    }
}
