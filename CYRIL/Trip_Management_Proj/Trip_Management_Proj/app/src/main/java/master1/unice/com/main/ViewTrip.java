package master1.unice.com.main;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.net.Uri;


import com.unice.trip.db.Service;
import com.unice.trip.db.Trip;
import com.unice.trip.db.TripDao;
import com.unice.trip.db.TripManagementDatabase;
import com.unice.trip.db.User;
import com.unice.trip.util.ConsistencyValidator;
import com.unice.trip.util.CostCalculator;
import com.unice.trip.util.XMLUtil;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.SEND_SMS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ViewTrip extends AppCompatActivity {

    private EditText tripNameView;
    private EditText tripSourceView;
    private EditText tripDestinationView;
    private EditText tripStartDateView;
    private EditText tripEndDateView;
    private EditText tripCostView;
    private TripDao tripDao;
    private Trip gtrip;

    EditText startDate;
    EditText endDate;

    int calendarYear;
    int calendarMonth;
    int calendarDay;
    String totalcost;

    private static final int REQUEST_SEND_SMS = 10;
    private static final int PICK_CONTACT_REQUEST = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Calendar calendar = Calendar.getInstance();
        calendarYear = calendar.get(Calendar.YEAR);
        calendarMonth = calendar.get(Calendar.MONTH);
        calendarDay = calendar.get(Calendar.DAY_OF_MONTH);
        startDate = findViewById(R.id.Trip_Start_Date);
        startDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view,boolean hasfocus) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewTrip.this, new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewTrip.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month+=1;
                        endDate.setText(day + "/" + month + "/" + year);
                    }
                },calendarYear,calendarMonth,calendarDay);
                datePickerDialog.show();

            }
        });

        tripNameView = (EditText) findViewById(R.id.Trip_Name);
        tripSourceView = (EditText) findViewById(R.id.Trip_Source);
        tripDestinationView = (EditText) findViewById(R.id.Trip_Destination);
        tripStartDateView = (EditText) findViewById(R.id.Trip_Start_Date);
        tripEndDateView = (EditText) findViewById(R.id.Trip_End_Date);
        tripCostView = (EditText) findViewById(R.id.Trip_TotalCost);
        //Button createTripButton =(Button) findViewById(R.id.Trip_Button);
        String tripNameIntent = getIntent().getStringExtra("tripname");
        gtrip = TripManagementDatabase.getTripManagementDatabase(this).TripDao().getTrip(tripNameIntent);
        List<Service> services = TripManagementDatabase.getTripManagementDatabase(this).ServiceDao().getServiceforTripId(gtrip.getTId());
        totalcost = CostCalculator.calculateCost(services);
        tripNameView.setText(gtrip.getName());
        tripSourceView.setText(gtrip.source);
        tripDestinationView.setText(gtrip.destination); ;
        tripStartDateView.setText((gtrip.startdate));
        tripEndDateView.setText(gtrip.enddate);
        if(totalcost!=null) {
            tripCostView.setText(totalcost);
        }
        Button createServiceButton = findViewById(R.id.CreateService_Button);
        createServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createServiceIntent = new Intent(getApplicationContext(),CreateService.class);
                createServiceIntent.putExtra("tripname",gtrip.getName());
                startActivity(createServiceIntent);
            }
        });

        Button addServicesButton = findViewById(R.id.AddServices_Button);
        addServicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent addServicesIntent = new Intent(getApplicationContext(),AddServicesView.class);
                addServicesIntent.putExtra("tripname",gtrip.getName());
                startActivity(addServicesIntent);
            }
        });

        Button addImportedServicesButton = findViewById(R.id.AddImportedServices_Button);
        addImportedServicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent addServicesIntent = new Intent(getApplicationContext(),AddSharedServicesView.class);
                addServicesIntent.putExtra("tripname",gtrip.getName());
                startActivity(addServicesIntent);
            }
        });

        Button servicesforTripButton = findViewById(R.id.ViewServices_Trip_Button);
        servicesforTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent viewServicesIntent = new Intent(getApplicationContext(),ServicesView.class);
                viewServicesIntent.putExtra("tripname",gtrip.getName());
                viewServicesIntent.putExtra("condition","onlytrip");
                startActivity(viewServicesIntent);
            }
        });


        Button deleteTripButton = findViewById(R.id.Delete_Trip_Button);
        deleteTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                deleteTrip(gtrip.getName());
                Toast.makeText(getApplicationContext(),"Trip is Deleted Successfully",Toast.LENGTH_LONG).show();
                Intent tripsIntent = new Intent(getApplicationContext(),TripsView.class);
                startActivity(tripsIntent);
            }
        });

        Button updateTripButton = findViewById(R.id.Update_Trip_Button);
        deleteTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                updateTrip(gtrip.getName());
                Toast.makeText(getApplicationContext(),"Trip is Updated Successfully",Toast.LENGTH_LONG).show();
                Intent tripsIntent = new Intent(getApplicationContext(),TripsView.class);
                startActivity(tripsIntent);
            }
        });

        // Start of SMS

        /*final FloatingActionButton openSmsButton = (FloatingActionButton) findViewById(R.id.fab_share);
        openSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = "My first message";
                Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:5554"));
                smsIntent.putExtra("sms_body", content);

                if (smsIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(smsIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "INTENT NOT RESOLVED", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

       final FloatingActionButton smsButton =  findViewById(R.id.fab_share);
        smsButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                smsButtonClicked();
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

        Toast.makeText(getApplicationContext(),"Welcome to"+" "+" Trip View",Toast.LENGTH_LONG).show();

    }


    private void smsButtonClicked() {
        if ((ContextCompat.checkSelfPermission(ViewTrip.this, SEND_SMS) != PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(ViewTrip.this, READ_CONTACTS) != PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(ViewTrip.this, READ_SMS) != PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(ViewTrip.this, RECEIVE_SMS) != PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(ViewTrip.this,
                    new String[] { SEND_SMS, READ_CONTACTS,READ_SMS,RECEIVE_SMS },
                    REQUEST_SEND_SMS);
        } else {
            pickContact();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Permission denied " + requestCode, Toast.LENGTH_SHORT).show();
            return;
        }

        switch(requestCode) {
            case REQUEST_SEND_SMS:
                pickContact();
                break;
            default:
                Toast.makeText(getApplicationContext(), "WRONG REQUEST CODE in Permissions", Toast.LENGTH_SHORT).show();
        }
    }


    private void pickContact() {
        Intent i = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, PICK_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            try {
                ContentResolver cr = getContentResolver();
                Uri dataUri = data.getData();
                String[] projection = { ContactsContract.Contacts._ID };
                Cursor cursor = cr.query(dataUri, projection, null, null, null);

                if (null != cursor && cursor.moveToFirst()) {
                    String id = cursor
                            .getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String number = getPhoneNumber(id);
                    if (number == null) {
                        Toast.makeText(getApplicationContext(), "No number in contact", Toast.LENGTH_SHORT).show();
                    } else {
                        //final EditText addrText = (EditText) findViewById(R.id.location);
                        sendSMS("ME MESSAGE", number);
                    }
                }
            } catch (Exception e) {
                Log.e("ViewTrip", e.toString());
            }
        }
    }


    private String getPhoneNumber(String id) {
        ContentResolver cr = getContentResolver();
        String where = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id;
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, where, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        return null;
    }

    private void sendSMS(String content, String number) {
        SmsManager smsManager = SmsManager.getDefault();
        List<Service> retrievedServices = TripManagementDatabase.getTripManagementDatabase(this).ServiceDao().getServiceforTripId(gtrip.getTId());

        try {

            User user= TripManagementDatabase.getTripManagementDatabase(this).UserDao().getUser();
            List<String> smsInitialParts = new ArrayList<>();
            int count=0;
            String mess="<TripManager>"+";"+"ZV"+";"+user.getName()+";"+"<Trip>"+";"+gtrip.getName()+";"+gtrip.getSource()+";"+gtrip.getDestination()+";"+gtrip.getStartdate()+";"
                    +gtrip.getEnddate()+";"+"</Trip>"+";"+retrievedServices.size()+";";
            smsInitialParts.add(mess);
            count++;
            for(Service service:retrievedServices){
                String messervice="<Service>"+";"+service.getName()+";"+service.getType()+";"+service.getDescription()+";"+service.getCondition()+";"+service.getSource()+";"
                        +service.getDestination()+";"+service.getLocation()+";"+service.getStartDate()+";"+service.getEndDate()+";"+service.getCost().toString()+";"+"</Service>"+";";
                smsInitialParts.add(messervice);
                count++;
            }
            String finalmess="</TripManager>";
            count++;
            smsInitialParts.add(finalmess);

            String modifiedMess=smsInitialParts.get(0).replaceFirst("ZV",Integer.toString(count));
            smsInitialParts.set(0,modifiedMess);

            for (int i = 0; i < smsInitialParts.size(); i++) {
                String app = "TripXP;" + smsInitialParts.get(i);
                smsManager.sendTextMessage(number, null, app, null, null);
            }

        }catch(Exception e){
            Log.d("ViewTrip",e.getMessage());

        }


    }



    public void updateTrip(String tripName){

        String trname = tripNameView.getText().toString();
        String sour = tripSourceView.getText().toString();
        String des= tripDestinationView.getText().toString() ;
        String startDateSelect= tripStartDateView.getText().toString();
        String endDateSelect=tripEndDateView.getText().toString();
        Trip intrip = new Trip(trname,sour,des,startDateSelect,endDateSelect);
        List<Service> services = TripManagementDatabase.getTripManagementDatabase(this).ServiceDao().getServiceforTripId(gtrip.getTId());
        Map<Boolean,Object> validationResult = ConsistencyValidator.isValidTrip(intrip,services);
        if(validationResult.containsKey(true)) {
            TripManagementDatabase.getTripManagementDatabase(this).TripDao().updateTrip(gtrip.getTId(),trname,sour,des,startDateSelect,endDateSelect);
        }else{
            Toast.makeText(getApplicationContext(),(String)validationResult.get(false),Toast.LENGTH_LONG).show();
        }

    }


    public void deleteTrip(String tripName){
        TripManagementDatabase.getTripManagementDatabase(this).TripDao().removeTrip(tripName);
    }








}
