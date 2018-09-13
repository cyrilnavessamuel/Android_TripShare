package master1.unice.com.main;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.unice.trip.db.Service;
import com.unice.trip.db.SharedService;
import com.unice.trip.db.SharedTrip;
import com.unice.trip.db.SharedTripDao;
import com.unice.trip.db.Trip;
import com.unice.trip.db.TripDao;
import com.unice.trip.db.TripManagementDatabase;
import com.unice.trip.util.ConsistencyValidator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import master1.unice.com.main.R;

public class ViewSharedTrip extends AppCompatActivity {

    private EditText tripNameView;
    private EditText tripSourceView;
    private EditText tripDestinationView;
    private EditText tripStartDateView;
    private EditText tripEndDateView;
    private SharedTripDao tripDao;
    private SharedTrip gtrip;

    EditText startDate;
    EditText endDate;

    int calendarYear;
    int calendarMonth;
    int calendarDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shared_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final Calendar calendar = Calendar.getInstance();
        calendarYear = calendar.get(Calendar.YEAR);
        calendarMonth = calendar.get(Calendar.MONTH);
        calendarDay = calendar.get(Calendar.DAY_OF_MONTH);
        startDate = findViewById(R.id.SharedTrip_Start_Date);
        startDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view,boolean hasfocus) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewSharedTrip.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month+=1;
                        startDate.setText(day + "/" + month + "/" + year);
                    }
                },calendarYear,calendarMonth,calendarDay);
                datePickerDialog.show();

            }
        });

        endDate = findViewById(R.id.SharedTrip_End_Date);
        endDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view,boolean hasfocus) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewSharedTrip.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month+=1;
                        endDate.setText(day + "/" + month + "/" + year);
                    }
                },calendarYear,calendarMonth,calendarDay);
                datePickerDialog.show();

            }
        });

        tripNameView = (EditText) findViewById(R.id.SharedTrip_Name);
        tripSourceView = (EditText) findViewById(R.id.SharedTrip_Source);
        tripDestinationView = (EditText) findViewById(R.id.SharedTrip_Destination);
        tripStartDateView = (EditText) findViewById(R.id.SharedTrip_Start_Date);
        tripEndDateView = (EditText) findViewById(R.id.SharedTrip_End_Date);

        //Button createTripButton =(Button) findViewById(R.id.Trip_Button);
        String tripNameIntent = getIntent().getStringExtra("tripname");
        gtrip = TripManagementDatabase.getTripManagementDatabase(this).SharedTripDao().getTrip(tripNameIntent);
        tripNameView.setText(gtrip.getName());
        tripSourceView.setText(gtrip.source);
        tripDestinationView.setText(gtrip.destination); ;
        tripStartDateView.setText((gtrip.startdate));
        tripEndDateView.setText(gtrip.enddate);



        Button servicesforTripButton = findViewById(R.id.ViewSharedServices_Trip_Button);
        servicesforTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent viewServicesIntent = new Intent(getApplicationContext(),SharedServices.class);
                viewServicesIntent.putExtra("tripname",gtrip.getName());
                viewServicesIntent.putExtra("condition","onlytrip");
                startActivity(viewServicesIntent);
            }
        });

        Button importSharedTripButton = findViewById(R.id.Import_SharedTrip_Button);
        importSharedTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                importSharedTrip(gtrip.getName());
                Toast.makeText(getApplicationContext(),"Trip is added Successfully",Toast.LENGTH_LONG).show();
                Intent tripsIntent = new Intent(getApplicationContext(),TripsView.class);
                startActivity(tripsIntent);
            }
        });


        Button deleteTripButton = findViewById(R.id.Delete_SharedTrip_Button);
        deleteTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteTrip(gtrip.getName());
                Toast.makeText(getApplicationContext(),"Trip is Deleted Successfully",Toast.LENGTH_LONG).show();
                Intent tripsIntent = new Intent(getApplicationContext(),TripsView.class);
                startActivity(tripsIntent);
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

        Toast.makeText(getApplicationContext(),"Welcome to"+" "+"Shared Trip View ",Toast.LENGTH_LONG).show();

    }

    public void deleteTrip(String tripName){
        TripManagementDatabase.getTripManagementDatabase(this).SharedTripDao().removeTrip(tripName);
    }

    public void importSharedTrip(String tripName){
        String trname = tripNameView.getText().toString();
        String sour = tripSourceView.getText().toString();
        String des= tripDestinationView.getText().toString() ;
        String startDateSelect= tripStartDateView.getText().toString();
        String endDateSelect=tripEndDateView.getText().toString();
        Trip trip = new Trip(trname,sour,des,startDateSelect,endDateSelect);
        List<SharedService> sharedServices = TripManagementDatabase.getTripManagementDatabase(this).SharedServiceDao().getServiceforTripId(gtrip.getTId());

        List<Service> services= mapSharedtoNormal(sharedServices);
        Map<Boolean,Object> validationResult = ConsistencyValidator.isValidTrip(trip,services);
        if(validationResult.containsKey(true)) {
            long tripid= TripManagementDatabase.getTripManagementDatabase(this).TripDao().InsertTrip(trip);
            for(SharedService sharedService:sharedServices){
                Service service = new Service(sharedService.getName(), sharedService.getType(),
                        sharedService.getDescription(), sharedService.getSource(), sharedService.getDestination(),
                        sharedService.getLocation(), sharedService.getCondition(), sharedService.getStartDate(),
                        sharedService.getEndDate(), sharedService.getCost());
                service.setTripId((int)tripid);
                TripManagementDatabase.getTripManagementDatabase(this).ServiceDao().InsertService(service);
            }
        }else{
            Toast.makeText(getApplicationContext(),(String)validationResult.get(false),Toast.LENGTH_LONG).show();
        }


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
