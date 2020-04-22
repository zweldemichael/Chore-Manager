package choremanager.ca.choremanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class allodeallocateActivity extends AppCompatActivity implements View.OnClickListener {


    RecyclerView recylerView;
    Spinner spinner, spinner1;

    DatabaseReference dataRef;
    DatabaseReference dataRef1;
    DatabaseReference insertDataRef;
    Map<String, String> map;

    TextView textDate;
    String myUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allodeallocate);

        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataRef = FirebaseDatabase.getInstance().getReference().child("Chore").child(myUID);

        dataRef.keepSynced(true);

        dataRef1 = FirebaseDatabase.getInstance().getReference().child("Users");
        dataRef1.keepSynced(true);

        insertDataRef = FirebaseDatabase.getInstance().getReference().child("Chore_Assign");
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        textDate = (TextView) findViewById(R.id.textDate);

        final List<String> list = new ArrayList<String>();
        list.add("Select the Chore");
        spinner.setSelection(0);


        final List<String> list1 = new ArrayList<String>();
        list1.add("Select User");
        spinner1.setSelection(0);


        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    list.add(data.getValue().toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        map = new HashMap<>();
        dataRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    map.put(data.child("name").getValue().toString(), data.child("uid").getValue().toString());
                    list1.add(data.child("name").getValue().toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i > 0) {
                    FirebaseRecyclerAdapter<choreAssignModel, viewHolder> subCategoryAdapter = new FirebaseRecyclerAdapter<choreAssignModel, viewHolder>(
                            choreAssignModel.class, R.layout.single_choreassigned, viewHolder.class, insertDataRef.child(map.get(spinner1.getSelectedItem().toString())).orderByChild("assignby").equalTo(myUID)  //insertDataRef.child(myUID).child(map.get(spinner1.getSelectedItem().toString()))
                    ) {
                        @Override
                        protected void populateViewHolder(final viewHolder viewHolder, final choreAssignModel model, int position) {


                            viewHolder.setTitle(model.getValue());
                            viewHolder.setDeadline(model.getDeadline());

//                            viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
//                                @Override
//                                public boolean onLongClick(View v) {
//                                    final AlertDialog.Builder builder = new AlertDialog.Builder(allodeallocateActivity.this);
//                                    builder.setTitle("Deallocate")
//                                            .setMessage("Do You want to Deallocate the Chore?")
//                                            .setIcon(R.mipmap.ic_launcher)
//
//                                            .setPositiveButton("Deallocate", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    insertDataRef.child(map.get(spinner1.getSelectedItem().toString())).child(model.getValue()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                            if (task.isSuccessful()) {
//                                                                insertDataRef.child(myUID).child(model.getValue()).removeValue();
//
//
//                                                                Util_Func.Alert(allodeallocateActivity.this, "Deallocated", " Dellocated Successfully");
//                                                            }
//                                                        }
//                                                    });
////                                                    insertDataRef.child(myUID).child(map.get(spinner1.getSelectedItem().toString())).child(model.getValue()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
////                                                        @Override
////                                                        public void onComplete(@NonNull Task<Void> task) {
////                                                            if (task.isSuccessful()) {
////                                                                insertDataRef.child(map.get(spinner1.getSelectedItem().toString())).child(myUID).child(model.getValue()).removeValue();
////
////
////                                                                Util_Func.Alert(allodeallocateActivity.this, "Deallocated", " Dellocated Successfully");
////                                                            }
////                                                        }
////                                                    });
//                                                }
//                                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.dismiss();
//                                        }
//                                    }).show();
//
//                                    return false;
//                                }
//                            });


                        }
                    };
                    recylerView.setAdapter(subCategoryAdapter);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list1);
        spinner1.setAdapter(adapter1);


        recylerView = (RecyclerView) findViewById(R.id.userChorRecycler);
        recylerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnDatePick).setOnClickListener(this);
        findViewById(R.id.btnAssign).setOnClickListener(this);


    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        View mView;


        public viewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String choreName) {
            TextView textView = (TextView) mView.findViewById(R.id.chorename);
            textView.setText(choreName);
        }

        public void setDeadline(String deadline) {
            TextView textView = (TextView) mView.findViewById(R.id.choredeadline);
            textView.setText("Deadline: " + deadline);
        }
    }


    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new
            DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {

                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                }

            };


    private void updateLabel() {

        String myFormat = "dd MMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textDate.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnDatePick) {
            new DatePickerDialog(allodeallocateActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        } else if (view.getId() == R.id.btnAssign) {
            if (spinner.getSelectedItemPosition() == 0) {
                Util_Func.Alert(allodeallocateActivity.this, "Select The Chore", "Please select the chore first");

                return;
            }
            if (spinner1.getSelectedItemPosition() == 0) {
                Util_Func.Alert(allodeallocateActivity.this, "Select User", "Please select the User first");

                return;
            }
            if (textDate.getText().toString().equals("Deadline: ")) {
                Util_Func.Alert(allodeallocateActivity.this, "No Deadline selected", "Click on pick date");

                return;
            }
            assignChore();
        }
    }


    private void assignChore() {

        final Map<String, String> mp = new HashMap<>();
        mp.put("value", spinner.getSelectedItem().toString());
        mp.put("deadline", textDate.getText().toString());
        mp.put("assignto", map.get(spinner1.getSelectedItem().toString()));


        insertDataRef.child(myUID).push().setValue(mp).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    final Map<String, String> mp1 = new HashMap<>();
                    mp1.put("value", spinner.getSelectedItem().toString());
                    mp1.put("deadline", textDate.getText().toString());
                    mp1.put("assignby", myUID);

                    insertDataRef.child(map.get(spinner1.getSelectedItem().toString())).push().setValue(mp1);
                    Util_Func.Alert(allodeallocateActivity.this, "Successully", "Successfully assigned the task");
                } else {
                    Toast.makeText(allodeallocateActivity.this, "not asssignasdf", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        insertDataRef.child(myUID).child(spinner.getSelectedItem().toString()).setValue(mp).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    insertDataRef.child(map.get(spinner1.getSelectedItem().toString())).child(myUID).child(spinner.getSelectedItem().toString()).setValue(mp);
//                    Util_Func.Alert(allodeallocateActivity.this, "Successully", "Successfully assigned the task");
//                }
//            }
//        });
    }
}
