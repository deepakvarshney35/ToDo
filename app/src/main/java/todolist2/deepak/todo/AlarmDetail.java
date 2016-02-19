package todolist2.deepak.todo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import todolist2.deepak.todo.db.TaskContract;
import todolist2.deepak.todo.db.TaskDBHelper;

/**
 * Created by Deepak on 19-Feb-16.
 */
public class AlarmDetail extends Activity {
    EditText info,date,time,loc;
    private TaskDBHelper helper;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    private DatePickerDialog fromDatePickerDialog;
    TimePickerDialog timePickerDialog;
    Button save;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.alarm_detail);
        info=(EditText)findViewById(R.id.info);
        date=(EditText)findViewById(R.id.date);
        time=(EditText)findViewById(R.id.time);
        loc=(EditText)findViewById(R.id.loc);
        save=(Button)findViewById(R.id.save);
        date.setInputType(InputType.TYPE_NULL);
        time.setInputType(InputType.TYPE_NULL);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        timeFormatter=new SimpleDateFormat("hh:mm aa");
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        Calendar newTimeCalendar=Calendar.getInstance();
        timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time.setText(((hourOfDay)<10?"0"+hourOfDay:hourOfDay)+":"+ (minute<10?"0"+minute:minute));
            }
        },newTimeCalendar.get(Calendar.HOUR_OF_DAY),newTimeCalendar.get(Calendar.MINUTE),false);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper = new TaskDBHelper(AlarmDetail.this);
                String task=info.getText().toString();
                String datestr=date.getText().toString();
                String timestr=time.getText().toString();
                String locstr=loc.getText().toString();

                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.clear();
                values.put(TaskContract.Columns.TASK, task);
                values.put(TaskContract.Columns.DATE, datestr);
                values.put(TaskContract.Columns.TIME, timestr);
                values.put(TaskContract.Columns.LOC, locstr);


                db.insertWithOnConflict(TaskContract.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                onBackPressed();
            }
        });


        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    fromDatePickerDialog.show();
            }
        });



        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    timePickerDialog.show();
                }
            }
        });
/*
        loc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                }
            }
        });*/
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
                    Intent intent = intentBuilder.build(AlarmDetail.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST)
            if(resultCode == RESULT_OK) {

            final Place place = PlacePicker.getPlace(data,this);
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
           // String attributions = (String) place.getAttributions();
           // if (attributions == null) {
           //     attributions = "";
           // }
            loc.setText(name+","+address);
            Toast.makeText(getApplicationContext(),name+","+address,Toast.LENGTH_SHORT).show();
          //  mAttributions.setText(Html.fromHtml(attributions));

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}