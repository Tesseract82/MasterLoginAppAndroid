package com.example.unit271.loginmasterapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public String searchString;
    public ArrayList<personObject> teamHoursList, permanentTeamHoursList, formattedTeamHoursList;
    public boolean deletionState;
    Firebase dataRef;
    public static String filename = "NumberHolder";
    SharedPreferences teamNumData;
    private ListView teamNameListView;
    private Button deleteButton;
    SimpleDateFormat logTimeFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Firebase.setAndroidContext(this);
        deletionState = false;
        logTimeFormat = new SimpleDateFormat("MM-dd-yyyy-HHmm", Locale.US);
        teamNumData = getSharedPreferences(filename, 0);
        teamHoursList = new ArrayList<personObject>();
        formattedTeamHoursList = new ArrayList<personObject>();
        permanentTeamHoursList = new ArrayList<personObject>();
        teamHoursList.clear();
        formattedTeamHoursList.clear();
        permanentTeamHoursList.clear();
        teamNameListView = (ListView) findViewById(R.id.mainListView);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setEnabled(false);
        dataRef = new Firebase("https://loginapptestcc.firebaseio.com/People");
        retreiveAllPersonData();
    }

    public void adaptPersonList() {
        TextView searchText = (TextView) findViewById(R.id.editText);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchString = s.toString();
                teamHoursList.clear();
                for (int a = 0; a <= permanentTeamHoursList.size() - 1; a++) {
                    teamHoursList.add(permanentTeamHoursList.get(a));
                }
                formatString(searchString);
            }
        });
    }

    public void generatePersonList(final boolean deletionState) {
        teamNameListView.setAdapter(new BaseAdapter() {

            @Override
            public boolean areAllItemsEnabled() {
                return true;
            }

            @Override
            public boolean isEnabled(int position) {
                return true;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getCount() {
                return teamHoursList.size();
            }

            @Override
            public String getItem(int position) {
                return teamHoursList.get(position).getPersonName();
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (convertView == null) {
                    convertView = layoutInflater.inflate(android.R.layout.simple_list_item_2, parent, false);
                }
                TextView personString = (TextView) convertView.findViewById(android.R.id.text1);
                TextView subPersonString = (TextView) convertView.findViewById(android.R.id.text2);
                personString.setText(teamHoursList.get(position).getPersonName());
                if(teamHoursList.get(position).getTotalTime() >= 60) {
                    subPersonString.setText(teamHoursList.get(position).getTotalTime() / 60 + " h (PASSWORD: " + teamHoursList.get(position).getPassword() + ")");
                } else {
                    subPersonString.setText(teamHoursList.get(position).getTotalTime() + " m (PASSWORD: " + teamHoursList.get(position).getPassword() + ")");
                }
                if (!deletionState) {
                    personString.setTextColor(Color.BLACK);
                    subPersonString.setTextColor(Color.BLACK);
                } else {
                    personString.setTextColor(Color.RED);
                    subPersonString.setTextColor(Color.RED);
                }

                return convertView;
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        });

        teamNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!deletionState) {
                    Intent signInOther2 = new Intent(getBaseContext(), hoursViewingActivity.class);
                    signInOther2.putExtra("com.example.unit271.LoginMasterApp/Main", parent.getItemAtPosition(position).toString());
                    startActivity(signInOther2);
                } else {
                    dataRef.child(parent.getItemAtPosition(position).toString()).removeValue();
                    retreiveAllPersonData();
                }
            }
        });
        adaptPersonList();
    }

    public void formatString(String searchString) {
        BaseAdapter teamNameAdapter = (BaseAdapter) teamNameListView.getAdapter();
        for (int a = 0; a <= teamHoursList.size() - 1; a++) {
            if (!((teamHoursList.get(a)).getPersonName().toLowerCase().startsWith(searchString.toLowerCase()))) {
                teamHoursList.remove(a);
                a--;
            } else if (searchString.equals("")) {
                continue;
            } else {
                continue;
            }
        }
        teamNameAdapter.notifyDataSetChanged();
    }

    public void onClickDelete(View view) {
        deletionState = !deletionState;
        generatePersonList(deletionState);
    }

    public void retreiveAllPersonData() {
        teamHoursList.clear();
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot personSnapshot : dataSnapshot.getChildren()) {
                    personObject newPerson = personSnapshot.getValue(personObject.class);
                    newPerson.setPersonName(personSnapshot.getKey());
                    teamHoursList.add(newPerson);
                    permanentTeamHoursList.add(newPerson);
                }
                dataRef.removeEventListener(this);
                sortByHours();
                deleteButton.setEnabled(true);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void sortByHours() {
        for (int a = 0; a <= teamHoursList.size() - 1; a++) {
            int b = 0;
            int timeRunningTotal = 0;
            if(teamHoursList.get(a).getlogins() != null) {
                ArrayList<LoginOutObject> newLst = new ArrayList<>(teamHoursList.get(a).getlogins().values());
                Collections.sort(newLst, new Comparator<LoginOutObject>() {
                    @Override
                    public int compare(LoginOutObject lhs, LoginOutObject rhs) {
                        Date lhsD = new Date();
                        Date rhsD = new Date();
                        try {
                            lhsD = logTimeFormat.parse(lhs.getTime());
                            rhsD = logTimeFormat.parse(rhs.getTime());
                        } catch (ParseException pe) {
                            pe.printStackTrace();
                        }
                        return lhsD.compareTo(rhsD);
                    }
                });
                if (!newLst.get(0).getAction().equals("In")) {
                    newLst.get(0).setAction("In");
                }
                for (int c = 1; c <= newLst.size() - 1; c++) {
                    if (newLst.get(c).getAction().equals(newLst.get(c - 1).getAction())) {
                        if (newLst.get(c - 1).getAction().equals("In")) {
                            newLst.get(c).setAction("Out");
                        } else if (newLst.get(c - 1).getAction().equals("Out")) {
                            newLst.get(c).setAction("In");
                        }
                    }
                }
                Log.i("MAINACTIVITYLOOPS", teamHoursList.get(a).getPersonName());
                while (b <= newLst.size() - 2) {
                    Log.i("INDEX", b + "");
                    if (newLst.get(b) != null && newLst.get(b + 1) != null) {
                        Log.i("MAINACTIVITYLOOPS2", newLst.get(b).getTime() + " " + newLst.get(b).getAction() + " - " + newLst.get(b + 1).getTime() + " " + newLst.get(b + 1).getAction());
                        if (newLst.get(b).getAction().equals("In") && newLst.get(b + 1).getAction().equals("Out")) {
                            String date1String = newLst.get(b).getTime();
                            String date2String = newLst.get(b + 1).getTime();
                            try {
                                Date date1 = logTimeFormat.parse(date1String);
                                Date date2 = logTimeFormat.parse(date2String);
                                timeRunningTotal += ((date2.getTime() - date1.getTime()) / (1000 * 60));
                                Log.i("MAINACTIVITYLOOPS2", "fillGraphList INCREMENT " + timeRunningTotal);
                            } catch (ParseException pe) {
                                pe.printStackTrace();
                            }
                        } else {
                            Log.i("MAINACTIVITYLOOPS2", "fillGraphList ACTIONERROR");
                        }
                    } else {
                        Log.i("MAINACTIVITYLOOPS2", "fillGraphList NULL");
                    }
                    b++;
                }
                teamHoursList.get(a).setTotalTime(timeRunningTotal);
            }
        }
        for(int c = 0; c <= teamHoursList.size(); c++){
            int sortingConstant = 0;
            while(sortingConstant < teamHoursList.size() - 1){
                if(teamHoursList.get(sortingConstant).getTotalTime() < teamHoursList.get(sortingConstant + 1).getTotalTime()){
                    Collections.swap(teamHoursList, sortingConstant, sortingConstant + 1);
                }
                sortingConstant++;
            }
        }
        generatePersonList(deletionState);
    }
}
