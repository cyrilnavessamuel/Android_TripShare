package master1.unice.com.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.unice.trip.db.TripManagementDatabase;

import master1.unice.com.main.R;

public class HomeActivity extends AppCompatActivity {

    private EditText welcomeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String text="Welcome to Create Trip App";
        Toast toast;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String username = getIntent().getStringExtra("username");
        if(username==null || username.isEmpty()){
            username=TripManagementDatabase.getTripManagementDatabase(getApplicationContext()).UserDao().getUser().getName();
        }
        text=text+" "+"Mr"+" "+username.toUpperCase();
        toast = Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT);
        toast.show();
        welcomeView = (EditText) findViewById(R.id.Welcome);
        welcomeView.setText("Welcome to Trip Management!!"+" "+"Mr"+" "+username.toUpperCase());


        Button createTripButton = findViewById(R.id.Home_CreateTrip_Button);
        createTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createTripsIntent = new Intent(getApplicationContext(),CreateTrip.class);
                startActivity(createTripsIntent);
            }
        });

        Button viewTripsButton = findViewById(R.id.Home_View_Trips_Button);
        viewTripsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewTripsIntent = new Intent(getApplicationContext(),TripsView.class);
                startActivity(viewTripsIntent);
            }
        });

        Button viewServiceButton = findViewById(R.id.Home_ViewService_Button);
        viewServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent servicesViewIntent = new Intent(getApplicationContext(),ServicesView.class);
                startActivity(servicesViewIntent);
            }
        });

        Button viewSharedEventsButton = findViewById(R.id.Home_ViewSharedEvents_Button);
        viewSharedEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharedEventsIntent = new Intent(getApplicationContext(),SharedEvents.class);
                startActivity(sharedEventsIntent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Back to Home",Toast.LENGTH_SHORT).show();
                Intent homeIntent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(homeIntent);
            }
        });
        Toast.makeText(getApplicationContext(),"Welcome to"+" "+"Home Screen ",Toast.LENGTH_LONG).show();

    }

}
