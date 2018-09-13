package master1.unice.com.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.unice.trip.db.Service;
import com.unice.trip.db.Trip;
import com.unice.trip.db.TripManagementDatabase;
import com.unice.trip.util.ConsistencyValidator;

import java.util.List;
import java.util.Map;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class AddServicesView extends AppCompatActivity {

    String [] serviceHeaders ={"Name","Type","Description","Condition","StartDate","EndDate","Cost"};
    String[][] services;
    String tripName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addservices_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        tripName = getIntent().getStringExtra("tripname");
        final TableView<String[]> tb = findViewById(R.id.AddServices_View);
        tb.setColumnCount(7);
        tb.setHeaderBackgroundColor(Color.parseColor("#2ecc79"));
        populateServiceData();
        tb.setHeaderAdapter(new SimpleTableHeaderAdapter(this,serviceHeaders));
        tb.setDataAdapter(new SimpleTableDataAdapter(this,services));
        tb.addDataClickListener(new TableDataClickListener<String[]>() {
            @Override
            public void onDataClicked(int rowIndex, String[] clickedData) {
                String serviceName = clickedData[0];
                Toast.makeText(getApplicationContext(),((String[])clickedData)[0],Toast.LENGTH_SHORT).show();
                insertService(tripName,serviceName);
                Toast.makeText(getApplicationContext(),"Successfully Inserted",Toast.LENGTH_SHORT).show();
                Intent viewTripIntent = new Intent(getApplicationContext(),ViewTrip.class);
                viewTripIntent.putExtra("tripname",tripName);
                startActivity(viewTripIntent);
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
        Toast.makeText(getApplicationContext(),"Welcome to"+" "+"Add Services View",Toast.LENGTH_LONG).show();
        }


    public void populateServiceData(){
        List<Service> serviceDatas = TripManagementDatabase.getTripManagementDatabase(this).ServiceDao().getServices();
        services = new String[serviceDatas.size()][7];
        for(int i=0;i<serviceDatas.size();i++){
            Service service = serviceDatas.get(i);
            services[i][0]= service.getName();
            services[i][1]= service.getType();
            services[i][2]= service.getDescription();
            services[i][3]= service.getCondition();
            services[i][4]= service.getStartDate();
            services[i][5] = service.getEndDate();
            services[i][6] = service.getCost().toString();

        }
    }

    public void insertService(String trip,String service){
        Trip retrievedTrip = TripManagementDatabase.getTripManagementDatabase(this).TripDao().getTrip(trip);
        Service retrievedService = TripManagementDatabase.getTripManagementDatabase(this).ServiceDao().getServiceforServiceName(service);

        List<Service> services = TripManagementDatabase.getTripManagementDatabase(this).ServiceDao().getServiceforTripId(retrievedTrip.getTId());
        services.add(retrievedService);
        Map<Boolean,Object> validationResult = ConsistencyValidator.isValidTrip(retrievedTrip,services);

        if(validationResult.containsKey(true)) {
            TripManagementDatabase.getTripManagementDatabase(this).ServiceDao().updateServiceWithTrip(retrievedTrip.getTId(),retrievedService.getSId());
        }else{
            Toast.makeText(getApplicationContext(),(String)validationResult.get(false),Toast.LENGTH_LONG).show();
        }

    }
}
