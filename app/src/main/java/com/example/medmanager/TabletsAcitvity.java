package com.example.medmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class TabletsAcitvity extends AppCompatActivity {

    ImageButton button2, button3, button4;
    RecyclerView rv;
    TabletAdapter ta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablets_acitvity);

        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);

        int[] newsImageArray = {
                R.drawable.logo1,
                R.drawable.logo1,
                R.drawable.logo1,
                R.drawable.logo1,
                R.drawable.logo1,
                R.drawable.logo1,
                R.drawable.logo1,
                R.drawable.logo1,
                R.drawable.logo1,
                R.drawable.logo1,
                R.drawable.logo1,
                R.drawable.logo1,
                R.drawable.logo1,



        };

        String[] medicines = {
                "Paracetamol",
                "Saridon",
                "Aspirin ",
                "Ibuprofen",
                "Omeprazole ",
                "Amoxicillin",
                "Ciprofloxacin",
                "Metformin",
                "Atorvastatin",
                "Metoprolol",
                "Alprazolam ",
                "Sertraline",
                "Pantoprazole"
        };

        String[] medicineDosages = {
                "Acetaminophen(Paracetamol): 500 mg to 1000 mg every 4 to 6 hours as needed, with a maximum of 4000 mg per day",
                "Saridon: One tablet (combination of paracetamol, propyphenazone, and caffeine) as directed by a physician",
                "Aspirin (Acetylsalicylic acid): 75 mg to 325 mg once daily for cardiovascular protection; higher doses for pain relief",
                "Ibuprofen: 200 mg to 400 mg every 4 to 6 hours as needed, with a maximum of 1200 mg per day",
                "Omeprazole (Proton Pump Inhibitor): 20 mg to 40 mg once daily for acid-related conditions, usually taken before a meal",
                "Amoxicillin (Antibiotic): 250 mg to 500 mg every 8 hours or 500 mg to 875 mg every 12 hours depending on the severity of the infection",
                "Ciprofloxacin (Antibiotic): 250 mg to 750 mg every 12 hours depending on the type and severity of the infection",
                "Metformin (Anti-diabetic): 500 mg to 1000 mg twice or three times daily with meals, with gradual dose escalation",
                "Atorvastatin (Statins for cholesterol): 10 mg to 80 mg once daily, usually taken at bedtime",
                "Metoprolol (Beta-blocker for hypertension): 25 mg to 100 mg once or twice daily, depending on the individual's condition",
                "Alprazolam (Benzodiazepine for anxiety): 0.25 mg to 0.5 mg three times daily, with doses adjusted based on individual response",
                "Sertraline (Selective Serotonin Reuptake Inhibitor - SSRI): 50 mg to 200 mg once daily, usually taken in the morning or evening",
                "Pantoprazole (Proton Pump Inhibitor): 20 mg to 40 mg once daily, usually taken before a meal"
        };

        rv = findViewById(R.id.dataRv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        ta = new TabletAdapter(this,medicines, medicineDosages,newsImageArray);
        rv.setAdapter(ta);






        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( TabletsAcitvity.this, MainActivity.class);
                startActivity(intent);

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( TabletsAcitvity.this, TabletsAcitvity.class);
                startActivity(intent);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TabletsAcitvity.this, profile.class);
                startActivity(intent);
            }
        });



    }



}