package com.example.indoor_navigation_new;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HomeScreen extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 0;
    ListView listView;
    BluetoothAdapter adapter;
    BluetoothManager manager;
    List savedList;
    Set<BluetoothDevice> pairedDevices;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://indoor-navigation-new-866e0-default-rtdb.firebaseio.com/");
    DatabaseReference ref = database.getReference().child("User");;
    BluetoothDevice result;
    String name;
    String deviceName = "HC-05_LAB 1";
    String dev = "HC-05";
    String status = "1";
    String result1 = null;


    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        listView = findViewById(R.id.ListView);

        manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        adapter = manager.getAdapter();
        savedList = new ArrayList();

        HomeScreen homeScreen = new HomeScreen();
        if (manager == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Toast.makeText(this, "error: bluetooth not supported", Toast.LENGTH_LONG).show();
        }
        else if(!adapter.isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);

            adapter.enable();

            pairedDevices = adapter.getBondedDevices();

            if (adapter.isEnabled()) {

                Adapter adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, savedList);
                listView.setAdapter((ListAdapter) adapter1);

            }


            result = null;
            name = adapter.getName();

            for (BluetoothDevice bt : pairedDevices) {
                // System.out.println(bt.getName());
                savedList.add(bt.getName());
                if(bt.getName().contains(dev)) {
//                    unpairDevice(bt);
                    result = bt;
                    String[] rdev = deviceName.split("_",-2);
                    for (String s: rdev)
                        result1 = s;

                    // Firebase Insertion

                    ref.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            database.getReference().child("User").orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()) {

                                        for(DataSnapshot snap : snapshot.getChildren()){
                                            String key = snap.getKey();
                                            String UserName = name.toString();
                                            String name_ = (String) snapshot.child(key).child("name").getValue();
//                                            System.out.println(name_);
                                            if(name_.contains(UserName)){
                                                ref.child(key).child("Status").setValue("1");
                                                System.out.println("updated");
                                                Intent homeIntent = new Intent(HomeScreen.this,Search_bar.class);
                                                startActivity(homeIntent);
                                                finish();
                                                break;
                                            }else if(!name_.matches(UserName)){
                                                insertData();
                                                break;
                                            }else{
                                                ref.child(key).child("Status").setValue("0");
                                                break;
                                            }

                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

        }

        else if(adapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            adapter.enable();

            pairedDevices = adapter.getBondedDevices();

            Adapter adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, savedList);
            listView.setAdapter((ListAdapter) adapter1);

            Toast.makeText(this, "This are your paired devices", Toast.LENGTH_SHORT).show();

            result = null;
            name = adapter.getName();

            for (BluetoothDevice bt : pairedDevices) {
                // System.out.println(bt.getName());
                savedList.add(bt.getName());
                if(bt.getName().contains(dev)) {
//                    unpairDevice(bt);
                    result = bt;
                    String[] rdev = deviceName.split("_",-2);
                    for (String s: rdev)
                        result1 = s;

                    // Firebase Insertion

                    ref.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            database.getReference().child("User").orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()) {

                                        for(DataSnapshot snap : snapshot.getChildren()){
                                            String key = snap.getKey();
                                            String UserName = name.toString();
                                            String name_ = (String) snapshot.child(key).child("name").getValue();
//                                            System.out.println(name_);
                                            if(name_.contains(UserName)){
                                                ref.child(key).child("Status").setValue("1");
                                                System.out.println("updated");
                                                Intent homeIntent = new Intent(HomeScreen.this,Search_bar.class);
                                                startActivity(homeIntent);
                                                finish();
//                                                break;
                                            }else if(!name_.matches(UserName)){
                                                insertData();
                                                break;
                                            }else{
                                                ref.child(key).child("Status").setValue("0");
                                                break;
                                            }

                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (requestCode == REQUEST_ENABLE_BT) {  // Match the request code
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Bluetooth Turned on", Toast.LENGTH_LONG).show();
            } else {   // RESULT_CANCELED
                Toast.makeText(this, "error: turning on bluetooth", Toast.LENGTH_LONG).show();
            }
        }
    }


    private  void insertData(){
        String UserName = name.toString();
        String DeviceName = deviceName.toString();

        HashMap<String, String> user_data = new HashMap<>();
        user_data.put("name", UserName);
        user_data.put("devicename", result1);
        user_data.put("Status", status);

        ref.push().setValue(user_data);
        System.out.println("INSERTED");

        Intent homeIntent = new Intent(HomeScreen.this,Search_bar.class);
        startActivity(homeIntent);
        finish();

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

        public Users(String Name, String Devicename, Long Status) {
            name = Name;
            devicename = Devicename;
            Status = Status;
        }
    }


}
