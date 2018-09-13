package master1.unice.com.main;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.unice.trip.db.Trip;
import com.unice.trip.db.TripDao;
import com.unice.trip.db.TripManagementDatabase;
import com.unice.trip.util.ConsistencyValidator;

import java.util.Calendar;
import java.util.Map;

public class CreateTrip extends AppCompatActivity {

    private EditText tripNameView;
    private EditText tripSourceView;
    private EditText tripDestinationView;
    private EditText tripStartDateView;
    private EditText tripEndDateView;
    private TripDao tripDao;
    private Trip intrip;
    private TripManagementDatabase tripManagementDatabase;
    EditText startDate;
    EditText endDate;

    int calendarYear;
    int calendarMonth;
    int calendarDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__trip);
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
        final Calendar calendar = Calendar.getInstance();
        calendarYear = calendar.get(Calendar.YEAR);
        calendarMonth = calendar.get(Calendar.MONTH);
        calendarDay = calendar.get(Calendar.DAY_OF_MONTH);
        startDate = findViewById(R.id.Trip_Start_Date);
        startDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view,boolean hasfocus) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateTrip.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month+=1;
                        startDate.setText(day + "/" + month + "/" + year);
                    }
                },calendarYear,calendarMonth,calendarDay);
                datePickerDialog.show();

            }
        });


        endDate = findViewById(R.id.Trip_End_Date);
        endDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view,boolean hasfocus) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateTrip.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month+=1;
                        endDate.setText(day + "/" + month + "/" + year);
                    }
                },calendarYear,calendarMonth,calendarDay);
                datePickerDialog.show();

            }
        });




        Button createTripButton =(Button) findViewById(R.id.Trip_Button);
                createTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                intinser();
                Intent viewTrip = new Intent(getApplicationContext(),ViewTrip.class);
                viewTrip.putExtra("tripname",intrip.getName());
                startActivity(viewTrip);
            }
        });
                    Toast.makeText(getApplicationContext(), "Welcome to" + " " + "Create Trip", Toast.LENGTH_LONG).show();


    }

    void intinser(){
        tripNameView = (EditText) findViewById(R.id.Trip_Name);
        tripSourceView = (EditText) findViewById(R.id.Trip_Source);
        tripDestinationView = (EditText) findViewById(R.id.Trip_Destination);
        tripStartDateView = (EditText) findViewById(R.id.Trip_Start_Date);
        tripEndDateView = (EditText) findViewById(R.id.Trip_End_Date);
        String trname = tripNameView.getText().toString();
        String sour = tripSourceView.getText().toString();
        String des= tripDestinationView.getText().toString() ;
        String startDateSelect= tripStartDateView.getText().toString();
        String endDateSelect=tripEndDateView.getText().toString();
        intrip = new Trip(trname,sour,des,startDateSelect,endDateSelect);
        Map<Boolean,String> validationResult = ConsistencyValidator.isValidTripOnly(intrip);
        if(validationResult.containsKey(true)) {
            TripManagementDatabase.getTripManagementDatabase(this).TripDao().InsertTrip(intrip);
        }else{
            Toast.makeText(getApplicationContext(),validationResult.get(false),Toast.LENGTH_LONG).show();
        }
    }

}
