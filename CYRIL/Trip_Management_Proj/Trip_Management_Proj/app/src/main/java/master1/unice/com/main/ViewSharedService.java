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
import com.unice.trip.db.SharedSingleService;
import com.unice.trip.db.TripManagementDatabase;

import java.util.Calendar;

import master1.unice.com.main.R;

public class ViewSharedService extends AppCompatActivity {

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
    EditText startDate;
    EditText endDate;

    int calendarYear;
    int calendarMonth;
    int calendarDay;
    SharedService retrievedService;
    String serviceTypeObject=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shared_service);
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
         serviceTypeObject=getIntent().getStringExtra("service-type");
        viewService(serviceTypeObject);
        final Calendar calendar = Calendar.getInstance();
        calendarYear = calendar.get(Calendar.YEAR);
        calendarMonth = calendar.get(Calendar.MONTH);
        calendarDay = calendar.get(Calendar.DAY_OF_MONTH);
        startDate = findViewById(R.id.SharedService_Start_Date);
        startDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view,boolean hasfocus) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewSharedService.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month+=1;
                        startDate.setText(day + "/" + month + "/" + year);
                    }
                },calendarYear,calendarMonth,calendarDay);
                datePickerDialog.show();

            }
        });


        endDate = findViewById(R.id.SharedService_End_Date);
        endDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view,boolean hasfocus) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewSharedService.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month+=1;
                        endDate.setText(day + "/" + month + "/" + year);
                    }
                },calendarYear,calendarMonth,calendarDay);
                datePickerDialog.show();

            }
        });

        Button deleteServiceButton = findViewById(R.id.DeleteSharedService_Button);
        deleteServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteService(retrievedService.getName(),serviceTypeObject);
                Toast.makeText(getApplicationContext(),"Service is Deleted Successfully",Toast.LENGTH_LONG).show();
                Intent servicesIntent = new Intent(getApplicationContext(),SharedServices.class);
                startActivity(servicesIntent);
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

        Toast.makeText(getApplicationContext(),"Welcome to"+" "+"Shared Service View ",Toast.LENGTH_LONG).show();

    }

    public void viewService(String isSingle){
        serviceNameView = (EditText) findViewById(R.id.SharedService_Name);
        serviceTypeView = (EditText) findViewById(R.id.SharedService_Type);
        serviceDescriptionView= findViewById(R.id.SharedService_Desription);
        serviceSourceView = findViewById(R.id.SharedService_Source);
        serviceDestinationView = findViewById(R.id.SharedService_Destination);
        serviceLocationView = findViewById(R.id.SharedService_Location);
        serviceConditionView = findViewById(R.id.SharedService_Condition);
        serviceStartDateView = findViewById(R.id.SharedService_Start_Date);
        serviceEndDateView = findViewById(R.id.SharedService_End_Date);
        serviceCostView = findViewById(R.id.SharedService_Cost);

        String currentService = getIntent().getStringExtra("service-name");
        if(isSingle.equalsIgnoreCase("sharedservice")) {
            retrievedService = TripManagementDatabase.getTripManagementDatabase(this).SharedServiceDao().getServiceforServiceName(currentService);
        }else if(isSingle.equalsIgnoreCase("sharedsingleservice")){
            SharedSingleService sharedSingleService = TripManagementDatabase.getTripManagementDatabase(this).SharedSingleServiceDao().getServiceforServiceName(currentService);
            SharedService sharedService = new SharedService(sharedSingleService.getName(), sharedSingleService.getType(),
                    sharedSingleService.getDescription(), sharedSingleService.getSource(), sharedSingleService.getDestination(),
                    sharedSingleService.getLocation(), sharedSingleService.getCondition(), sharedSingleService.getStartDate(),
                    sharedSingleService.getEndDate(), sharedSingleService.getCost(),sharedSingleService.getSender(),0);

            retrievedService = sharedService;
        }
        serviceNameView.setText(retrievedService.getName());
        serviceTypeView.setText(retrievedService.getType());
        serviceDescriptionView.setText(retrievedService.getDescription());
        serviceSourceView.setText(retrievedService.getSource());
        serviceDestinationView.setText(retrievedService.getDestination());
        serviceLocationView.setText(retrievedService.getLocation());
        serviceConditionView.setText(retrievedService.getCondition());
        serviceStartDateView.setText(retrievedService.getStartDate());
        serviceEndDateView.setText(retrievedService.getEndDate());
        serviceCostView.setText(retrievedService.getCost().toString());
    }

    public void deleteService(String serviceName,String isSingle){
        if(isSingle.equalsIgnoreCase("sharedservice")) {
            TripManagementDatabase.getTripManagementDatabase(this).SharedServiceDao().removeService(serviceName);
        }
        else if(isSingle.equalsIgnoreCase("sharedsingleservice")){
            TripManagementDatabase.getTripManagementDatabase(this).SharedSingleServiceDao().removeService(serviceName);
        }
    }




}
