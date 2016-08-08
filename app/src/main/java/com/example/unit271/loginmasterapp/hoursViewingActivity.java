package com.example.unit271.loginmasterapp;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class hoursViewingActivity extends AppCompatActivity {
    ArrayList<LoginOutObject> logsList;
    ArrayList<graphObject> graphList;
    ArrayList<String> spinnerList;
    String name;
    Firebase dataRef;
    double maxHours;
    double maxPixels;
    double scale;
    Spinner unitsSpinner;
    CheckBox viewHoursTextCb;
    String currentUnits;
    boolean viewHoursText;
    SimpleDateFormat logTimeFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hours_viewing);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        currentUnits = "Minutes";
        maxPixels = 0;
        scale = 0;
        viewHoursText = false;
        logTimeFormat = new SimpleDateFormat("MM-dd-yyyy-HHmm", Locale.US);
        name = getIntent().getStringExtra("com.example.unit271.LoginMasterApp/Main");
        viewHoursTextCb = (CheckBox) findViewById(R.id.hoursTextCheck);
        viewHoursTextCb.setChecked(false);
        logsList = new ArrayList<LoginOutObject>();
        graphList = new ArrayList<graphObject>();
        spinnerList = new ArrayList<String>();
        spinnerList.clear();
        spinnerList.add("Minutes");
        spinnerList.add("Hours");
        logsList.clear();
        graphList.clear();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitsSpinner = (Spinner) findViewById(R.id.unitsSpinner);
        unitsSpinner.setAdapter(spinnerAdapter);
        unitsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!parent.getItemAtPosition(position).toString().equals(currentUnits)){
                    adaptData(parent.getItemAtPosition(position).toString(), viewHoursText);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        TextView nameView = (TextView) findViewById(R.id.nameView2);
        nameView.setText(name);
        retrieveData();
    }

    public void retrieveData(){
        dataRef = new Firebase("https://loginapptestcc.firebaseio.com/People/" + name + "/Logins");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                logsList.clear();
                for (DataSnapshot infoSnapshot : dataSnapshot.getChildren()) {
                    logsList.add(infoSnapshot.getValue(LoginOutObject.class));
                }
                dataRef.removeEventListener(this);
                maxHours = 0;
                if (logsList.size() >= 2) {
                    fillGraphList(0);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void adaptData(String units, boolean viewHoursText1){
        Log.i("SSSSSIIIIIZEEE", graphList.size() + "");
        for(int a = 0; a <= graphList.size() - 1; a++) {
            if(graphList.get(a).getHours() > maxHours) {
                maxHours = graphList.get(a).getHours();
            }
        }
        LinearLayout lnr1 = (LinearLayout) findViewById(R.id.linearLayout);
        lnr1.removeAllViews();
        maxPixels = lnr1.getHeight();
        if(maxHours != 0 && maxPixels > 61) {
            scale = (maxPixels - 61) / maxHours;
        }
        ScaleView sv1 = new ScaleView(this, (int) Math.floor(maxPixels - 61), (int) Math.floor(maxHours), units);
        LinearLayout linL2 = (LinearLayout) findViewById(R.id.linearLayout2);
        linL2.removeAllViews();
        linL2.setOrientation(LinearLayout.VERTICAL);
        linL2.addView(sv1);
        TextView txtv2 = new TextView(this);
        txtv2.setText("Date");
        txtv2.setTextSize(15);
        txtv2.setTextColor(Color.DKGRAY);
        txtv2.setPadding(0, 0, 0, 0);
        linL2.addView(txtv2);
        for(int a = 0; a <= graphList.size() - 1; a++){
            if(graphList.get(a).getHours() > 0) {
                Log.i("GOODNEWS", "GOODNEWS");
                LinearLayout lnr2 = new LinearLayout(this);
                lnr2.setOrientation(LinearLayout.VERTICAL);
                TextView txtv = new TextView(this);
                txtv.setText(graphList.get(a).getStartDate());
                txtv.setTextSize(15);
                txtv.setTextColor(Color.GREEN);
                txtv.setPadding(0, 0, 0, 0);
                StudentHoursView shv = new StudentHoursView(this, (int) Math.floor(scale * graphList.get(a).getHours()), viewHoursText1, graphList.get(a).getHours(), units);
                lnr2.addView(shv);
                lnr2.addView(txtv);
                lnr2.setPadding(15, 0, 15, 0);
                lnr1.addView(lnr2);
            } else {
                Log.i("BADNEWS", "BADNEWS");
            }
        }
        currentUnits = units;
        viewHoursText = viewHoursText1;
    }

    public void fillGraphList(int nextIndex){
        String currentDate = logsList.get(nextIndex).getTime().substring(0, 10);
        int pivotIndex = 0;
        int hoursRunningTotal = 0;
        graphObject go1 = new graphObject();
        go1.setStartDate(currentDate);
        while((nextIndex + pivotIndex) <= logsList.size() - 2){
            Log.i("INDEX", nextIndex + pivotIndex + "");
            if(logsList.get(nextIndex + pivotIndex) != null && logsList.get(nextIndex + pivotIndex + 1) != null){
                if(logsList.get(nextIndex + pivotIndex).getAction().equals("In") && logsList.get(nextIndex + pivotIndex + 1).getAction().equals("Out")){
                    String date1String = logsList.get(nextIndex + pivotIndex).getTime();
                    String date2String = logsList.get(nextIndex + pivotIndex + 1).getTime();
                    try {
                        Date date1 = logTimeFormat.parse(date1String);
                        Date date2 = logTimeFormat.parse(date2String);
                        if(date1String.substring(0, 10).equals(currentDate)){
                            hoursRunningTotal += ((date2.getTime() - date1.getTime()) / (1000 * 60));
                            Log.i("hoursViewingActivity", "fillGraphList INCREMENT " + hoursRunningTotal);
                        } else {
                            if(hoursRunningTotal > 0) {
                                go1.setHours(hoursRunningTotal);
                                Log.i("hoursViewingActivityS", hoursRunningTotal + "");
                                graphList.add(go1);
                                Log.i("hoursViewingActivity", "fillGraphList ADDITION");
                            }
                            fillGraphList(nextIndex + pivotIndex);
                            break;
                        }
                    } catch(ParseException pe) {
                        pe.printStackTrace();
                    }
                } else {
                    Log.i("hoursViewingActivity", "fillGraphList ACTIONERROR");
                }
            } else {
                Log.i("hoursViewingActivity", "fillGraphList NULL");
            }
            pivotIndex++;
        }
        if((pivotIndex + nextIndex) > (logsList.size() - 2)) {
            if(hoursRunningTotal > 0) {
                Log.i("hoursViewingActivityS", hoursRunningTotal + "");
                go1.setHours(hoursRunningTotal);
                graphList.add(go1);
                Log.i("hoursViewingActivity", "fillGraphList FINALADDITION");
            }
            adaptData(currentUnits, viewHoursText);
        }
    }

    public void onHoursTextCheckClick(View view){
        adaptData(currentUnits, viewHoursTextCb.isChecked());
    }
}
