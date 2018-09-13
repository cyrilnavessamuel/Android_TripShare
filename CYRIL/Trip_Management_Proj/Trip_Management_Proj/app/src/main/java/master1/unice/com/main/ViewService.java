package master1.unice.com.main;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.unice.trip.db.Service;
import com.unice.trip.db.Trip;
import com.unice.trip.db.TripManagementDatabase;
import com.unice.trip.db.User;
import com.unice.trip.util.ConsistencyValidator;
import com.unice.trip.util.XMLUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.SEND_SMS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ViewService extends AppCompatActivity {

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
    Service retrievedService;

    private static final int REQUEST_SEND_SMS = 10;
    private static final int PICK_CONTACT_REQUEST = 20;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*Button fab = findViewById(R.id.Service_Button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*//*

                viewService();
            }
        });*/
        viewService();
        final Calendar calendar = Calendar.getInstance();
        calendarYear = calendar.get(Calendar.YEAR);
        calendarMonth = calendar.get(Calendar.MONTH);
        calendarDay = calendar.get(Calendar.DAY_OF_MONTH);
        startDate = findViewById(R.id.Service_Start_Date);
        startDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view,boolean hasfocus) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewService.this, new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewService.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month+=1;
                        endDate.setText(day + "/" + month + "/" + year);
                    }
                },calendarYear,calendarMonth,calendarDay);
                datePickerDialog.show();

            }
        });

        Button deleteServiceButton = findViewById(R.id.DeleteService_Button);
        deleteServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                deleteService(retrievedService.getName());
                Toast.makeText(getApplicationContext(),"Service is Deleted Successfully",Toast.LENGTH_LONG).show();
                Intent servicesIntent = new Intent(getApplicationContext(),ServicesView.class);
                startActivity(servicesIntent);
            }
        });

        Button updateServiceButton = findViewById(R.id.UpdateService_Button);
        updateServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                updateService(retrievedService.getName());
                Toast.makeText(getApplicationContext(),"Service is Updated Successfully",Toast.LENGTH_LONG).show();
                Intent servicesIntent = new Intent(getApplicationContext(),ServicesView.class);
                startActivity(servicesIntent);
            }
        });

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

        Toast.makeText(getApplicationContext(),"Welcome to"+" "+"Service View ",Toast.LENGTH_LONG).show();

    }

    private void smsButtonClicked() {
        if ((ContextCompat.checkSelfPermission(ViewService.this, SEND_SMS) != PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(ViewService.this, READ_CONTACTS) != PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(ViewService.this, READ_SMS) != PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(ViewService.this, RECEIVE_SMS) != PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(ViewService.this,
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
                Log.e("ViewService", e.toString());
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
        //smsManager.sendTextMessage(number, null, content, null, null);
        //List<Service> retrievedServices = TripManagementDatabase.getTripManagementDatabase(this).ServiceDao().getServiceforTripId(gtrip.getTId());
        /*File file= XMLUtil.generateXML(gtrip,retrievedServices,getFilesDir().getPath());
        byte[] data = new byte[(int)file.length()];
        try {


            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(data);
        }
        catch(IOException ioException){
            Log.d("ViewTrip",ioException.getMessage());
        }*/
        //smsManager.sendTextMessage(number, null, content, null, null);
        //short a=8907;
        //smsManager.sendDataMessage(number,null,a,data,null,null);
        try {
            //PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
            //PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED"), 0);
            //String mess = new String(Files.readAllBytes(file.toPath()));
            User user= TripManagementDatabase.getTripManagementDatabase(this).UserDao().getUser();
            List<String> smsInitialParts = new ArrayList<>();
            int count=0;
            String mess="<TripManager>"+";"+"ZV"+";"+user.getName()+";"+"<Service>"+";"+retrievedService.getName()+";"+retrievedService.getType()+";"+retrievedService.getDescription()+";"+retrievedService.getCondition()+";"+retrievedService.getSource()+";"
                        +retrievedService.getDestination()+";";
            smsInitialParts.add(mess);
            count++;
            String messfinal=retrievedService.getLocation()+";"+retrievedService.getStartDate()+";"+retrievedService.getEndDate()+";"+retrievedService.getCost().toString()+";"+"</Service>"+";"+"</TripManager>";
            smsInitialParts.add(messfinal);
            count++;
            String modifiedMess=smsInitialParts.get(0).replaceFirst("ZV",Integer.toString(count));
            smsInitialParts.set(0,modifiedMess);
            //ArrayList<String> smsInitialParts = smsManager.divideMessage(mess);
            //String modifiedMess=mess.replaceFirst("ZV",Integer.toString(smsInitialParts.size()));
            //ArrayList<String> smsBodyParts = smsManager.divideMessage(modifiedMess);
            //ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
            //ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
            /*for (int i = 0; i < smsBodyParts.size(); i++) {
                sentPendingIntents.add(sentPendingIntent);
                deliveredPendingIntents.add(deliveredPendingIntent);
            }*/

            for (int i = 0; i < smsInitialParts.size(); i++) {
                String app = "TripXP;" + smsInitialParts.get(i);
                smsManager.sendTextMessage(number, null, app, null, null);
            }

            //smsManager.sendTextMessage(number, null, content, null, null);
            //smsManager.sendTextMessage(number, null,mess , null, null);
            //smsManager.sendMultipartTextMessage(number, null, smsBodyParts, null, null);
        }catch(Exception e){
            Log.d("ViewService",e.getMessage());

        }


    }


    public void viewService(){
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

        String currentService = getIntent().getStringExtra("service-name");
        retrievedService = TripManagementDatabase.getTripManagementDatabase(this).ServiceDao().getServiceforServiceName(currentService);
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

    public void deleteService(String serviceName){
        TripManagementDatabase.getTripManagementDatabase(this).ServiceDao().removeService(serviceName);
    }

    public void updateService(String serviceName){
        Trip trip = TripManagementDatabase.getTripManagementDatabase(this).TripDao().getTripforID(retrievedService.getTripId());
        List<Service> services = TripManagementDatabase.getTripManagementDatabase(this).ServiceDao().getServiceforTripId(retrievedService.getTripId());
        String name=serviceNameView.getText().toString();
        String type=serviceTypeView.getText().toString();
        String description=serviceDescriptionView.getText().toString();
        String source=serviceSourceView.getText().toString();
        String destination=serviceDestinationView.getText().toString();
        String location=serviceLocationView.getText().toString();
        String condition=serviceConditionView.getText().toString();
        String startDate=serviceStartDateView.getText().toString();
        String endDate=serviceEndDateView.getText().toString();
        Float cost=Float.parseFloat(serviceCostView.getText().toString());
        for(Service service:services){
            if(service.getName().equalsIgnoreCase(serviceName)){
                service.setName(name);
                service.setType(type);
                service.setDescription(description);
                service.setSource(source);
                service.setDestination(destination);
                service.setLocation(location);
                service.setCondition(condition);
                service.setStartDate(startDate);
                service.setEndDate(endDate);
                service.setCost(cost);
            }
        }
        Map<Boolean,Object> validationResult = ConsistencyValidator.isValidTrip(trip,services);
        if(validationResult.containsKey(true)) {
            TripManagementDatabase.getTripManagementDatabase(this).ServiceDao().updateService(retrievedService.getSId(),name,type,description,source,destination,location,condition,startDate,endDate,cost);
        }else{
            Toast.makeText(getApplicationContext(),(String)validationResult.get(false),Toast.LENGTH_LONG).show();
        }

    }


}
