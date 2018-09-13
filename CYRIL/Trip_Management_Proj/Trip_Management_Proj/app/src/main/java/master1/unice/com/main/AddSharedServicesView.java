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

import com.unice.trip.db.Service;
import com.unice.trip.db.SharedService;
import com.unice.trip.db.SharedSingleService;
import com.unice.trip.db.Trip;
import com.unice.trip.db.TripManagementDatabase;
import com.unice.trip.util.ConsistencyValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import master1.unice.com.main.R;

public class AddSharedServicesView extends AppCompatActivity {

    String [] serviceHeaders ={"Name","Type","StartDate","EndDate","Cost","Sender"};
    String[][] services;
    String tripName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shared_services_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        tripName = getIntent().getStringExtra("tripname");
        final TableView<String[]> tb = findViewById(R.id.AddSharedServices_View);
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
            Toast.makeText(getApplicationContext(), "Welcome to" + " " + "Add Shared Servics View ", Toast.LENGTH_LONG).show();


    }

    public void populateServiceData(){
        List<SharedSingleService> SharedSingleServiceDatas = TripManagementDatabase.getTripManagementDatabase(this).SharedSingleServiceDao().getServices();
        services = new String[SharedSingleServiceDatas.size()][6];
        for(int i=0;i<SharedSingleServiceDatas.size();i++){
            SharedSingleService service = SharedSingleServiceDatas.get(i);
            services[i][0]= service.getName();
            services[i][1]= service.getType();
            services[i][2]= service.getStartDate();
            services[i][3] = service.getEndDate();
            services[i][4] = service.getCost().toString();
            services[i][5] = service.getSender();

        }
    }
    private List<Service> mapSharedSingletoService(List<SharedSingleService>sharedSingleServices){
        List<Service> services = new ArrayList<>();
        for(SharedSingleService sharedSingleService:sharedSingleServices){
            Service service = new Service(sharedSingleService.getName(), sharedSingleService.getType(),
                    sharedSingleService.getDescription(), sharedSingleService.getSource(), sharedSingleService.getDestination(),
                    sharedSingleService.getLocation(), sharedSingleService.getCondition(), sharedSingleService.getStartDate(),
                    sharedSingleService.getEndDate(), sharedSingleService.getCost());
            services.add(service);
        }
        return services;
    }

    public void insertService(String trip,String serviceName){
        Trip retrievedTrip = TripManagementDatabase.getTripManagementDatabase(this).TripDao().getTrip(trip);
        SharedSingleService sharedSingleService = TripManagementDatabase.getTripManagementDatabase(this).SharedSingleServiceDao().getServiceforServiceName(serviceName);
        Service service = new Service(sharedSingleService.getName(), sharedSingleService.getType(),
                sharedSingleService.getDescription(), sharedSingleService.getSource(), sharedSingleService.getDestination(),
                sharedSingleService.getLocation(), sharedSingleService.getCondition(), sharedSingleService.getStartDate(),
                sharedSingleService.getEndDate(), sharedSingleService.getCost());
        service.setTripId(retrievedTrip.getTId());

        List<Service> services = TripManagementDatabase.getTripManagementDatabase(this).ServiceDao().getServiceforTripId(retrievedTrip.getTId());
        services.add(service);
        Map<Boolean,Object> validationResult = ConsistencyValidator.isValidTrip(retrievedTrip,services);

        if(validationResult.containsKey(true)) {
            TripManagementDatabase.getTripManagementDatabase(this).ServiceDao().InsertService(service);
        }else{
            Toast.makeText(getApplicationContext(),(String)validationResult.get(false),Toast.LENGTH_LONG).show();
        }

    }

}
