package master1.unice.com.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.unice.trip.db.SharedTrip;
import com.unice.trip.db.Trip;
import com.unice.trip.db.TripManagementDatabase;

import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import master1.unice.com.main.R;

public class SharedTrips extends AppCompatActivity {

    String [] tripHeaders = {"Name","Source","Destination","Sender"};
    String[][] trips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_trips);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Back to Home",Toast.LENGTH_SHORT).show();
                Intent homeIntent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(homeIntent);
            }
        });

        final TableView<String[]> tb = (TableView<String[]>) findViewById(R.id.SharedTrips_View);
        tb.setColumnCount(5);
        tb.setHeaderBackgroundColor(Color.parseColor("#2ecc79"));
        populateTripData();
        tb.setHeaderAdapter(new SimpleTableHeaderAdapter(this,tripHeaders));
        tb.setDataAdapter(new SimpleTableDataAdapter(this,trips));
        tb.addDataClickListener(new TableDataClickListener<String[]>() {
            @Override
            public void onDataClicked(int rowIndex, String[] clickedData) {
                String trip_selected = clickedData[0];
                //Toast.makeText(getApplicationContext(), ((String[])clickedData)[0], Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), trip_selected, Toast.LENGTH_SHORT).show();
                Intent viewTripIntent = new Intent(getApplicationContext(),ViewSharedTrip.class);
                viewTripIntent.putExtra("tripname",trip_selected);
                startActivity(viewTripIntent);
            }
        });

        Toast.makeText(getApplicationContext(),"Welcome to"+" "+"Shared Trips ",Toast.LENGTH_LONG).show();

    }
    public void populateTripData(){
        List<SharedTrip> tripDatas = TripManagementDatabase.getTripManagementDatabase(this).SharedTripDao().getTrips();
        trips = new String[tripDatas.size()][5];
        for(int i=0;i<tripDatas.size();i++){
            SharedTrip trip = tripDatas.get(i);
            trips[i][0]=trip.getName();
            trips[i][1]=trip.getSource();
            trips[i][2]=trip.getDestination();
            trips[i][3]=trip.getSender();


        }
    }

}
