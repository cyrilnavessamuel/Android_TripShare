package master1.unice.com.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import master1.unice.com.main.R;

public class SharedEvents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button viewTripsButton = findViewById(R.id.Shared_View_Trips_Button);
        viewTripsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewSharedTripsIntent = new Intent(getApplicationContext(),SharedTrips.class);
                startActivity(viewSharedTripsIntent);
            }
        });

        Button viewServicesButton = findViewById(R.id.Shared_View_Services_Button);
        viewServicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewSharedServicesIntent = new Intent(getApplicationContext(),SharedServices.class);
                startActivity(viewSharedServicesIntent);
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
        Toast.makeText(getApplicationContext(),"Welcome to"+" "+" Shared Events",Toast.LENGTH_LONG).show();

    }

}
