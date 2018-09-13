package master1.unice.com.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unice.trip.db.Service;
import com.unice.trip.db.Trip;
import com.unice.trip.db.TripManagementDatabase;
import com.unice.trip.util.ConsistencyValidator;

import java.util.List;

import de.codecrafters.tableview.TableHeaderAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import master1.unice.com.main.R;

public class ServicesView extends AppCompatActivity {

    String [] serviceHeaders = {"Name","Type","Start","End","Cost"};
    String[][] services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_view);
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
        final TableView<String[]> tb = findViewById(R.id.Services_View);
        tb.setColumnCount(5);
        for(int i=0;i<4;i++){
            tb.setColumnWeight(i,2);
        }
        tb.setHeaderBackgroundColor(Color.parseColor("#2ecc79"));
        String isserviceforTrip = getIntent().getStringExtra("condition");
        String serviceforTrip = getIntent().getStringExtra("tripname");
        populateServiceData(isserviceforTrip,serviceforTrip);
        if(services==null || services.length==0) {
            Toast.makeText(getApplicationContext(),"Inconsistent/No Valid services Retrieved",Toast.LENGTH_LONG).show();
            services = new String[0][7];
            serviceHeaders[0]="No";
            serviceHeaders[1]="Valid ";
            serviceHeaders[2]="Services";
            for(int i=3;i<5;i++){
                serviceHeaders[i]="";
            }
        }
            tb.setHeaderAdapter(new SimpleTableHeaderAdapter(this,serviceHeaders));
            tb.setDataAdapter(new SimpleTableDataAdapter(this, services));
            tb.addDataClickListener(new TableDataClickListener<String[]>() {
                @Override
                public void onDataClicked(int rowIndex, String[] clickedData) {
                    String serviceSelected = clickedData[0];
                    Toast.makeText(getApplicationContext(), ((String[]) clickedData)[0], Toast.LENGTH_SHORT).show();
                    Intent viewServiceIntent = new Intent(getApplicationContext(), ViewService.class);
                    viewServiceIntent.putExtra("service-name", serviceSelected);
                    startActivity(viewServiceIntent);

                }
            });

        Toast.makeText(getApplicationContext(),"Welcome to"+" "+"Services View ",Toast.LENGTH_LONG).show();

    }


    public void populateServiceData(String condition,String trip){
        List<Service> serviceDatas;
        if(condition!=null&&trip!=null&&condition.equals("onlytrip")) {
            Trip retrievedTrip = TripManagementDatabase.getTripManagementDatabase(this).TripDao().getTrip(trip);
            List<Service> services = TripManagementDatabase.getTripManagementDatabase(this).ServiceDao().getServiceforTripId(retrievedTrip.getTId());
            serviceDatas =(List<Service>) ConsistencyValidator.reorderService(retrievedTrip,services).get(true);
        }
        else{
            serviceDatas = TripManagementDatabase.getTripManagementDatabase(this).ServiceDao().getServices();
        }
        if(serviceDatas==null) {
            Toast.makeText(getApplicationContext(),"Inconsistent/No Valid services",Toast.LENGTH_LONG).show();
        }else {
            services = new String[serviceDatas.size()][5];
            for (int i = 0; i < serviceDatas.size(); i++) {
                Service service = serviceDatas.get(i);
                services[i][0] = service.getName();
                services[i][1] = service.getType();
                services[i][2] = service.getStartDate();
                services[i][3] = service.getEndDate();
                services[i][4] = service.getCost().toString();


            }
        }
    }
}
