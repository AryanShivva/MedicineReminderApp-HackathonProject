package com.example.medmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medmanager.mydatabase.MedicalDB;

import java.util.Calendar;

// RecyclerView adapter for medication list
public class MedicineListAdapter extends RecyclerView.Adapter {
    // Cursor that holds drug list data
    private Cursor med_list;
    // Application context
    public Context context;
    // Database helper class
    public MedicalDB helper;

    //Creating the adapter
    public MedicineListAdapter(Context context, MedicalDB helper) {
        this.context = context;
        this.helper = helper;
    }

    //Method that updates the data set
    public void setUserData(Cursor cursor) {
        this.med_list = cursor;
        if (med_list != null) {
            med_list.moveToFirst();
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MedicineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Creating the ViewHolder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_card, parent, false);
        MedicineHolder vh = new MedicineHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (med_list != null) {
            //Filling the corresponding elements of the ViewHolder
            ((MedicineHolder) holder).medName.setText(med_list.getString(1));
            ((MedicineHolder) holder).qty.setText(" Quantity : " + med_list.getInt(2));
            ((MedicineHolder) holder).id = med_list.getInt(0);
            ((MedicineHolder) holder).time.setText("Time : "+med_list.getString(3));

            // Set the status of the switching button for medication status (on/off)
            boolean isChecked = med_list.getInt(5) == 1 ? true : false;
            ((MedicineHolder) holder).toggleSwitch.setChecked(isChecked);

            // Listener called when the switching button is clicked
            ((MedicineHolder) holder).toggleSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Update medication status
                    if (((MedicineHolder) holder).toggleSwitch.isChecked()) {
                        helper.setEnable(helper.getWritableDatabase(), ((MedicineHolder) holder).id, 1);
                    } else {
                        helper.setEnable(helper.getWritableDatabase(), ((MedicineHolder) holder).id, 0);
                    }


                    // Set alarm for notification
                    Cursor c = helper.getMedicine(helper.getWritableDatabase(), ((MedicineHolder) holder).id);
                    c.moveToFirst();
                    String[] raw_time = c.getString(3).split(":");
                    int hour = Integer.parseInt(raw_time[0]);
                    int min = Integer.parseInt(raw_time[1]);

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, hour);
                    cal.set(Calendar.MINUTE, min);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);

                    Calendar now = Calendar.getInstance();
                    now.set(Calendar.SECOND, 0);
                    now.set(Calendar.MILLISECOND, 0);

                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
                    intent.putExtra("medName", c.getString(1));
                    intent.putExtra("medQty", c.getString(2));

                    if (((MedicineHolder) holder).toggleSwitch.isChecked()) {
                        //Check recurrence days
                        String days = c.getString(4);
                        if (days.equals("0000000")) {
                            // If no day is specified, set to next day
                            if (cal.before(now)) {
                                cal.add(Calendar.DATE, 1);
                            }
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,((MedicineHolder) holder).id, intent, 0);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

                            Toast.makeText(context, "Reminder" + c.getString(1) + " time for medicine " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ", history " + cal.get(Calendar.DATE) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR) + " was set to ", Toast.LENGTH_LONG).show();
                        } else {
                            // If day is specified, set separate alarm for each day
                            int ct = 1;
                            for (char d : days.toCharArray()) {
                                if (d == '1') {
                                    cal.set(Calendar.DAY_OF_WEEK, ct);
                                    if (cal.before(now)) {
                                        cal.add(Calendar.DATE, 7);
                                    }
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(((MedicineHolder) holder).id + "" + ct), intent, 0);
                                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
                                }
                                ct++;
                            }
                            Toast.makeText(context, "Reminder " + c.getString(1) + " time for medicine " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + " was set to.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // If the medicine is turned off, cancel the corresponding alarm
                        String days = c.getString(4);
                        if (days.equals("0000000")) {
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,((MedicineHolder) holder).id, intent, 0);
                            alarmManager.cancel(pendingIntent);
                        } else {
                            int ct = 1;
                            for (char d : days.toCharArray()) {
                                if (d == '1') {
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(((MedicineHolder) holder).id + "" + ct), intent, 0);
                                    alarmManager.cancel(pendingIntent);
                                }
                                ct++;
                            }
                        }
                    }
                }
            });

            // Listener called when the drug delete button is clicked
            ((MedicineHolder) holder).deleteMed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Perform drug deletion
                    helper.deleteMedicine(helper.getWritableDatabase(), ((MedicineHolder) holder).id);
                    // Update dataset and readjust RecyclerView
                    setUserData(helper.getAllMedicine(helper.getWritableDatabase()));
                }
            });

            // Move to next drug in cursor
            med_list.moveToNext();
        }
    }

    @Override
    public int getItemCount() {

        //Return the number of elements held by the adapter
        return med_list.getCount();
    }

    // ViewHolder class that holds the medication element
    public class MedicineHolder extends RecyclerView.ViewHolder {
        TextView medName, time, qty;
        ImageButton deleteMed;
        int id;
        Switch toggleSwitch;

        //Creating the ViewHolder
        public MedicineHolder(@NonNull View itemView) {
            super(itemView);
            // Assigning elements in ViewHolder
            medName = (TextView) itemView.findViewById(R.id.med_name);
            time = (TextView) itemView.findViewById(R.id.med_time);
            qty = (TextView) itemView.findViewById(R.id.med_quantity);
            deleteMed = (ImageButton) itemView.findViewById(R.id.delete_med);
            toggleSwitch = (Switch) itemView.findViewById(R.id.toggle_switch);
        }
    }
}
