package com.example.medmanager;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medmanager.mydatabase.MedicalDB;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

// Activity class showing its medications
public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {


    public RecyclerView medList;// RecyclerView showing the user's medication list
    public MedicineListAdapter medListAdapter;// Medication list adapter
    public FloatingActionButton medFab;// Add medication button



    // UI elements for drug information entry
    Button medTime;
    ImageButton button2, button3, button4;
    EditText medName, medQty;
    Switch isRepeat;
    ChipGroup chipGroup;
    Chip pzt, sal, crs, prs, cum, cts, pzr;//mon tue wed thu



    public MedicalDB DbHelper;// Database object


    // Method called when the activity is created
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);


        // Creating the database object
        DbHelper = MedicalDB.getInstance(getApplicationContext());

        // Connect views to activity
        medList = findViewById(R.id.med_list);
        medFab = findViewById(R.id.med_fab);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);

;
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"You are on Home", Toast.LENGTH_LONG).show();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TabletsAcitvity.class);
                startActivity(intent);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, profile.class);
                startActivity(intent);
            }
        });




        // Setting the layout manager for RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        medList.setLayoutManager(linearLayoutManager);

        // Creating the medication list adapter and assigning the data
        medListAdapter = new MedicineListAdapter(getApplicationContext(), DbHelper);
        medListAdapter.setUserData(DbHelper.getAllMedicine(DbHelper.getWritableDatabase()));
        medList.setAdapter(medListAdapter);

        // Listener called when the drug addition button is clicked
        medFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Method to display the drug adding dialog
                medicineAdder().show();
            }
        });
    }




    //Method that creates the drug adding dialog
    private AlertDialog medicineAdder() {

        // Defining the layout that specifies the content of the dialog
        View layout = View.inflate(this, R.layout.add_med_dialog, null);

        // Drug details
        medName = layout.findViewById(R.id.add_med_name);
        medQty = layout.findViewById(R.id.add_med_qty);
        medTime = layout.findViewById(R.id.add_med_time);

        // UI elements
        isRepeat = layout.findViewById(R.id.repeat_switch);
        chipGroup = layout.findViewById(R.id.chip_group);
        setChildrenEnabled(chipGroup, false);
        pzt = layout.findViewById(R.id.pazartesi);
        sal = layout.findViewById(R.id.sali);
        crs = layout.findViewById(R.id.carsamba);
        prs = layout.findViewById(R.id.persembe);
        cum = layout.findViewById(R.id.cuma);
        cts = layout.findViewById(R.id.cumartesi);
        pzr = layout.findViewById(R.id.pazar);




        // Listener called when clicking for time selection
        medTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });




        // Listener called when the repeat switch is clicked
        isRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Enable days if repeat is on, otherwise disable
                if (!isRepeat.isChecked()) {
                    setChildrenEnabled(chipGroup, false);
                } else {
                    setChildrenEnabled(chipGroup, true);
                }
            }
        });




        // AlertDialog constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);//Assigning Layout to AlertDialog




        // Actions to be taken when clicking the "ADD" button
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Convert the drug amount to a number
                int qty = 0;//default value
                if (!"".equals(medQty.getText().toString()))
                    qty = Integer.parseInt(medQty.getText().toString());


                // Set the format of the days
                String days = "0000000";//default value
                if (isRepeat.isChecked()) {
                    days = setDaysFormat(pzr, pzt, sal, crs, prs, cum, cts);
                }

                // Perform drug addition
                DbHelper.addMedicine(DbHelper.getWritableDatabase(),medName.getText().toString(), qty, medTime.getText().toString(), days);

                // Update medication list and readjust RecyclerView
                medListAdapter.setUserData(DbHelper.getAllMedicine(DbHelper.getWritableDatabase()));
                medListAdapter.notifyDataSetChanged();
                medList.setAdapter(medListAdapter);
            }
        });




        // Actions to be taken when clicking the "CANCEL" button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();  // Returning the created AlertDialog
    }



    // Method to determine the format of the days
    public String setDaysFormat(Chip pzr, Chip pzt, Chip sal, Chip crs, Chip prs, Chip cum, Chip cts) {
        String dayString = "" + (pzr.isChecked() ? "1" : "0") + (pzt.isChecked() ? "1" : "0") + (sal.isChecked() ? "1" : "0") + (crs.isChecked() ? "1" : "0") + (prs.isChecked() ? "1" : "0") + (cum.isChecked() ? "1" : "0") + (cts.isChecked() ? "1" : "0");
        return dayString;
    }

    //Method that sets the clickability of all elements in the ChipGroup
    public void setChildrenEnabled(ChipGroup chipGroup, Boolean enable) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            chipGroup.getChildAt(i).setEnabled(enable);
        }
    }

    //Method called when time is selected
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Write the selected time to the screen
        medTime.setText(hourOfDay + ":" + minute);
    }

}
