package choremanager.ca.choremanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class EachUserActivity extends AppCompatActivity {

    String uid;

    private RecyclerView recyclerView;
    DatabaseReference dataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_user);
        uid = getIntent().getExtras().getString("uid");

        dataRef = FirebaseDatabase.getInstance().getReference().child("Chore_Assign").child(uid);
        dataRef.keepSynced(true);

        // inflating the recyclew view layout
        recyclerView = (RecyclerView) findViewById(R.id.recyclerToDo);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(EachUserActivity.this));


        String name = getIntent().getExtras().getString("name");
        String points = getIntent().getExtras().getString("points");
        final String image = getIntent().getExtras().getString("image");

        final CircleImageView imageView = (CircleImageView) findViewById(R.id.image);

        TextView textName = (TextView) findViewById(R.id.title);
        TextView textPoint = (TextView) findViewById(R.id.point);

        textName.setText(name);
        textPoint.setText(points);


        Picasso.with(EachUserActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.user).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError() {
                Picasso.with(EachUserActivity.this).load(image).placeholder(R.drawable.user).into(imageView);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<choreAssignModel, viewHolder> subCategoryAdapter = new FirebaseRecyclerAdapter<choreAssignModel, viewHolder>(
                choreAssignModel.class, R.layout.signle_choreassignedmain, viewHolder.class, dataRef
        ) {
            @Override
            protected void populateViewHolder(final viewHolder viewHolder, final choreAssignModel model, int position) {


                viewHolder.setTitle(String.valueOf(viewHolder.getAdapterPosition() + 1) + ")  " + model.getValue());
                viewHolder.setDeadline("  " + model.getDeadline());


            }
        };
        recyclerView.setAdapter(subCategoryAdapter);


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


}
