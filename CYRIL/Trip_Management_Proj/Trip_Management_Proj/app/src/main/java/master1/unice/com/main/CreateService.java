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
import com.unice.trip.db.Trip;
import com.unice.trip.db.TripManagementDatabase;
import com.unice.trip.util.ConsistencyValidator;

import java.util.Calendar;
import java.util.List;
import java.util.Map;


public class CreateService extends AppCompatActivity {

    private EditText serviceNameView;
    private EditText serviceTypeView;
    private EditText serviceDescriptionView;
    private EditText serviceSourceView;
    private EditText serviceDestinationView;
    private EditText serviceLocationView;
    private EditText serviceConditionView;
    private EditText serviceStartDateView;
    private EditText serviceEndDateView;
    private EditText serviceCostView;
    private String name;
    private String tripName;
    EditText startDate;
    EditText endDate;

    int calendarYear;
    int calendarMonth;
    int calendarDay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button insertservice = findViewById(R.id.Service_Button);
        tripName = getIntent().getStringExtra("tripname");
        insertservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                insertService(tripName);
                Intent serviceIntent = new Intent(getApplicationContext(),ViewService.class);
                serviceIntent.putExtra("service-name",name);
                startActivity(serviceIntent);
            }
        });

        final Calendar calendar = Calendar.getInstance();
        calendarYear = calendar.get(Calendar.YEAR);
        calendarMonth = calendar.get(Calendar.MONTH);
        calendarDay = calendar.get(Calendar.DAY_OF_MONTH);
        startDate = findViewById(R.id.Service_Start_Date);
        startDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view,boolean hasfocus) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateService.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month+=1;
                        startDate.setText(day + "/" + month + "/" + year);
                    }
                },calendarYear,calendarMonth,calendarDay);
                datePickerDialog.show();

            }
        });


        endDate = findViewById(R.id.Service_End_Date);
        endDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view,boolean hasfocus) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateService.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month+=1;
                        endDate.setText(day + "/" + month + "/" + year);
                    }
                },calendarYear,calendarMonth,calendarDay);
                datePickerDialog.show();

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
        Toast.makeText(getApplicationContext(),"Welcome to"+" "+" Create Service",Toast.LENGTH_LONG).show();

    }

    public void insertService(String tripName){
        serviceNameView = (EditText) findViewById(R.id.Service_Name);
        serviceTypeView = (EditText) findViewById(R.id.Service_Type);
        serviceDescriptionView= findViewById(R.id.Service_Desription);
        serviceSourceView = findViewById(R.id.Service_Source);
        serviceDestinationView = findViewById(R.id.Service_Destination);
        serviceLocationView = findViewById(R.id.Service_Location);

        serviceConditionView = findViewById(R.id.Service_Condition);
        serviceStartDateView = findViewById(R.id.Service_Start_Date);
        serviceEndDateView = findViewById(R.id.Service_End_Date);
        serviceCostView = findViewById(R.id.Service_Cost);
        name= serviceNameView.getText().toString();
        String type=serviceTypeView.getText().toString();
        String description=serviceDescriptionView.getText().toString();
        String source=serviceSourceView.getText().toString();
        String destination=serviceDestinationView.getText().toString();
        String location=serviceLocationView.getText().toString();
        String condition=serviceConditionView.getText().toString();
        String startDate=serviceStartDateView.getText().toString();
        String endDate=serviceEndDateView.getText().toString();
        float cost= Float.parseFloat(serviceCostView.getText().toString());

        Trip retrievedTrip= TripManagementDatabase.getTripManagementDatabase(this).TripDao().getTrip(tripName);

        Service service = new Service(name,type,description,source,destination,location,condition,startDate,endDate,cost);
        service.setTripId(retrievedTrip.getTId());
        //validation
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
