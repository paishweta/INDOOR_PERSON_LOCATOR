package com.example.indoor_navigation_new;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Search_bar extends AppCompatActivity {

    private AutoCompleteTextView mSearchField;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://indoor-navigation-new-866e0-default-rtdb.firebaseio.com/");
    DatabaseReference ref = database.getReference("User");

    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);

        mSearchField = (AutoCompleteTextView) findViewById(R.id.search_field);
        listView = (ListView) findViewById(R.id.listView);


        ValueEventListener event = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                populateSearch(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ref.addListenerForSingleValueEvent(event);

    }

    private void populateSearch(DataSnapshot snapshot) {
        ArrayList<String> names = new ArrayList<>();

        if(snapshot.exists()){
            for(DataSnapshot ds : snapshot.getChildren()){
                String name = ds.child("name").getValue(String.class);
                names.add(name);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,names);
            mSearchField.setAdapter(adapter);

            mSearchField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String name = mSearchField.getText().toString();
                    searchUser(name);
                }
            });

      }else{
            Log.d("Users","No data found");
        }

    }

    private void searchUser(String name) {

        Query query = ref.orderByChild("name").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    ArrayList<String> listuser = new ArrayList<>();

                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String value = ds.child("Status").getValue(String.class);
                        System.out.println(value);

                        Users user = new Users(ds.child("name").getValue(String.class), ds.child("devicename").getValue(String.class), (String) ds.child("Status").getValue());
                        if (value.equals("1")) {
                            listuser.add(user.getName() + "  is in  " + user.getDeviceName());

                        }else if(value.toString().equals("0")) {
                            listuser.add(user.getName() + "  not in  " + user.getDeviceName());
                        }
                        else{
                            listuser.add(user.getName() + "is not present in college campus");
                        }
                    }

                    ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,listuser);
                    listView.setAdapter(adapter1);
                }else{
                    Log.d("Users","No data found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    class Users {

        public String name, devicename;
        public Long Status;


        public Users(){

        }

        public String getName() {
            return name;
        }

        public String getDeviceName() {
            return devicename;
        }

        public Long getStatus() {
            return Status;
        }

        public void setName(String name) {
            name = name;
        }

        public void setDeviceName(String Devicename) {
            devicename = Devicename;
        }

        public void setStatus(Long Status) {
            Status = Status;
        }

        public Users(String Name, String Devicename, String Status) {
            name = Name;
            devicename = Devicename;
            Status = Status;
        }
    }

}
