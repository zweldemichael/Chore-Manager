package choremanager.ca.choremanager;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class ChoreCompActivity extends AppCompatActivity {
    RecyclerView recylerView;

    DatabaseReference dataRef,userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chore_comp);


        dataRef = FirebaseDatabase.getInstance().getReference().child("Chore_Assign").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dataRef.keepSynced(true);


        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userRef.keepSynced(true);


        recylerView = (RecyclerView) findViewById(R.id.choreCompleteRecycler);
        recylerView.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<choreAssignModel, viewHolder> subCategoryAdapter = new FirebaseRecyclerAdapter<choreAssignModel, viewHolder>(
                choreAssignModel.class, R.layout.single_chorecompleted, viewHolder.class, dataRef
        ) {
            @Override
            protected void populateViewHolder(final viewHolder viewHolder, final choreAssignModel model, int position) {

                viewHolder.setTitle(model.getValue());
                viewHolder.setDeadline(model.getDeadline());

                viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       

                    }
                });


            }
        };
        recylerView.setAdapter(subCategoryAdapter);
    }


    public static class viewHolder extends RecyclerView.ViewHolder {
        View mView;
        Button button;

        public viewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            button = (Button) mView.findViewById(R.id.btnComplete);
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

}
