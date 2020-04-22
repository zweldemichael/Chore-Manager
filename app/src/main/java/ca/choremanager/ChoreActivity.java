package choremanager.ca.choremanager;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChoreActivity extends AppCompatActivity {


    EditText editChore;

    RecyclerView recylerView;

    DatabaseReference dataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chore);


        dataRef = FirebaseDatabase.getInstance().getReference().child("Chore").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dataRef.keepSynced(true);


        editChore = (EditText) findViewById(R.id.editChore);
        recylerView = (RecyclerView) findViewById(R.id.choreRecycler);
        recylerView.setLayoutManager(new LinearLayoutManager(this));


        findViewById(R.id.btnAddChore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editChore.getText())) {
                    Util_Func.Alert(ChoreActivity.this, "Empty Chore", "Enter the Chore First");
                    return;
                }

                dataRef.child(editChore.getText().toString()).setValue(editChore.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Util_Func.Alert(ChoreActivity.this, "Successfully", "Chore Successfully Added");
                            editChore.setText("");
                        }
                    }
                });

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseRecyclerAdapter<String, CategoryViewHolder> categoryAdapter = new FirebaseRecyclerAdapter<String, CategoryViewHolder>(
                String.class, android.R.layout.simple_list_item_1, CategoryViewHolder.class, dataRef
        ) {
            @Override
            protected void populateViewHolder(final CategoryViewHolder viewHolder, String model, final int position) {

                viewHolder.text.setText(model);

                viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(ChoreActivity.this);
                        builder.setTitle("Delete")
                                .setMessage("Do You want to Delete?")
                                .setIcon(R.mipmap.ic_launcher)

                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                          dataRef.child(viewHolder.text.getText().toString()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    Toast.makeText(ChoreActivity.this, "deleted sucessfully", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                        return false;
                    }
                });
            }

        };

        recylerView.setAdapter(categoryAdapter);
    }


    private static class CategoryViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView text;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            text = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }

}
