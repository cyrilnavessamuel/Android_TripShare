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
import com.unice.trip.db.SharedTrip;
import com.unice.trip.db.Trip;
import com.unice.trip.db.TripManagementDatabase;
import com.unice.trip.util.ConsistencyValidator;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import master1.unice.com.main.R;

public class SharedServices extends AppCompatActivity {

    String [] serviceHeaders ={"Name","Type","Description","Sender","Cost"};
    String[][] services;
    String isserviceforTrip = null;
    String serviceforTrip =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_services);
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
        final TableView<String[]> tb = findViewById(R.id.SharedServices_View);
        tb.setColumnCount(7);
        tb.setHeaderBackgroundColor(Color.parseColor("#2ecc79"));
         isserviceforTrip = getIntent().getStringExtra("condition");
         serviceforTrip = getIntent().getStringExtra("tripname");
        populateServiceData(isserviceforTrip,serviceforTrip);
        if(services==null || services.length==0) {
            Toast.makeText(getApplicationContext(),"Inconsistent/No Valid services Retrieved",Toast.LENGTH_LONG).show();
            services = new String[0][5];
            serviceHeaders[0]="No";
            serviceHeaders[1]="Valid ";
            serviceHeaders[2]="Services";
            for(int i=3;i<5;i++){
                serviceHeaders[i]="";
            }
        }
        tb.setHeaderAdapter(new SimpleTableHeaderAdapter(this,serviceHeaders));
        tb.setDataAdapter(new SimpleTableDataAdapter(this,services));
        tb.addDataClickListener(new TableDataClickListener<String[]>() {
            @Override
            public void onDataClicked(int rowIndex, String[] clickedData) {
                String serviceSelected = clickedData[0];
                Toast.makeText(getApplicationContext(),((String[])clickedData)[0],Toast.LENGTH_SHORT).show();
                Intent viewServiceIntent = new Intent(getApplicationContext(),ViewSharedService.class);
                viewServiceIntent.putExtra("service-name",serviceSelected);
                if(isserviceforTrip!=null&&serviceforTrip!=null&&isserviceforTrip.equals("onlytrip")){
                    viewServiceIntent.putExtra("service-type","sharedservice");
                }
                else{
                    viewServiceIntent.putExtra("service-type","sharedsingleservice");
                }
                startActivity(viewServiceIntent);

            }
        });
        Toast.makeText(getApplicationContext(),"Welcome to"+" "+"Shared Services ",Toast.LENGTH_LONG).show();

    }

    public void populateServiceData(String condition,String trip){
        List<SharedService> serviceDatas=null;
        List<SharedSingleService> sharedSingleServiceDatas;
        if(condition!=null&&trip!=null&&condition.equals("onlytrip")) {
            SharedTrip retrievedTrip = TripManagementDatabase.getTripManagementDatabase(this).SharedTripDao().getTrip(trip);
            List<SharedService> services = TripManagementDatabase.getTripManagementDatabase(this).SharedServiceDao().getServiceforTripId(retrievedTrip.getTId());
            serviceDatas = (List<SharedService>)ConsistencyValidator.reorderSharedService(retrievedTrip,services).get(true);
        }
        else{
            sharedSingleServiceDatas = TripManagementDatabase.getTripManagementDatabase(this).SharedSingleServiceDao().getServices();
            serviceDatas = mapSingletoShared(sharedSingleServiceDatas);
        }
        if(serviceDatas==null) {
            Toast.makeText(getApplicationContext(),"Inconsistent/No Valid services",Toast.LENGTH_LONG).show();
        }else{
            services = new String[serviceDatas.size()][7];
            for (int i = 0; i < serviceDatas.size(); i++) {
                SharedService service = serviceDatas.get(i);
                services[i][0] = service.getName();
                services[i][1] = service.getType();
                services[i][2] = service.getDescription();
                services[i][3] = service.getSender();
                services[i][6] = service.getCost().toString();

            }
        }
    }

    private List<SharedService> mapSingletoShared(List<SharedSingleService>sharedSingleServices){
        List<SharedService> sharedServices = new ArrayList<>();
        for(SharedSingleService sharedSingleService:sharedSingleServices){
            SharedService sharedService = new SharedService(sharedSingleService.getName(), sharedSingleService.getType(),
                    sharedSingleService.getDescription(), sharedSingleService.getSource(), sharedSingleService.getDestination(),
                    sharedSingleService.getLocation(), sharedSingleService.getCondition(), sharedSingleService.getStartDate(),
                    sharedSingleService.getEndDate(), sharedSingleService.getCost(),sharedSingleService.getSender(),0);
            sharedServices.add(sharedService);
        }
        return sharedServices;
    }

    private List<Service> mapSharedtoNormal(List<SharedService>sharedServices){
        List<Service> services = new ArrayList<>();
        for(SharedService sharedService:sharedServices){
            Service service = new Service(sharedService.getName(), sharedService.getType(),
                    sharedService.getDescription(), sharedService.getSource(), sharedService.getDestination(),
                    sharedService.getLocation(), sharedService.getCondition(), sharedService.getStartDate(),
                    sharedService.getEndDate(), sharedService.getCost());
            services.add(service);
        }
        return services;
    }



}
