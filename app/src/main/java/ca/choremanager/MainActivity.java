package choremanager.ca.choremanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth _auth;

    DatabaseReference dataRef;


    private NavigationView navigationView;

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _auth = FirebaseAuth.getInstance();

        dataRef = FirebaseDatabase.getInstance().getReference().child("Users");
        dataRef.keepSynced(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // inflating the recyclew view layout
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


    }

    private void nav_Data_Set() {

        dataRef.child(_auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {

                    String _getName = dataSnapshot.child("name").getValue().toString();
                    String _getUserType = dataSnapshot.child("usertype").getValue().toString();
                    final String _getImage = dataSnapshot.child("thumb").getValue().toString();

                    ((TextView) navigationView.getHeaderView(0).findViewById(R.id.navText1)).setText(_getName);
                    ((TextView) navigationView.getHeaderView(0).findViewById(R.id.navText2)).setText(_getUserType);
                    final CircleImageView circleImageView = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.navImage);

                    Picasso.with(MainActivity.this).load(_getImage).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.user).into(circleImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(MainActivity.this).load(_getImage).placeholder(R.drawable.user).into(circleImageView);
                        }
                    });

                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void GoTo_Login() {
        startActivity(new Intent(MainActivity.this, Activity_Login.class));
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser _currentUser = _auth.getCurrentUser();
        if (_currentUser == null) {
            GoTo_Login();
        } else {

            if (!getSharedPreferences("checkabout", MODE_PRIVATE).getBoolean("done", false)) {
                startActivity(new Intent(MainActivity.this, AccountActivity.class));

            } else {
                load();
                nav_Data_Set();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuSignOut) {
            _auth.signOut();
            GoTo_Login();
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_chore) {

            startActivity(new Intent(MainActivity.this, ChoreActivity.class));
        } else if (id == R.id.nav_alloc) {
            startActivity(new Intent(MainActivity.this, allodeallocateActivity.class));
        } else if (id == R.id.nav_comple) {
            startActivity(new Intent(MainActivity.this, ChoreCompActivity.class));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void load() {
        final FirebaseRecyclerAdapter<userModel, ViewHolder> categoryAdapter = new FirebaseRecyclerAdapter<userModel, ViewHolder>(
                userModel.class, R.layout.single_user, ViewHolder.class, dataRef
        ) {
            @Override
            protected void populateViewHolder(final ViewHolder viewHolder, final userModel model, int position) {

                viewHolder.setTitle(model.getName());

                //viewHolder.setPoints(model);
                viewHolder.setImage(model.getThumb(), MainActivity.this);
                viewHolder.setPoints(model.getPoints());
                viewHolder._view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        startActivity(new Intent(MainActivity.this, EachUserActivity.class).putExtra("uid", model.getUid()).putExtra("name",model.getName()).putExtra("points",model.getPoints()).putExtra("image",model.getImage()));
                    }
                });

            }

        };

        recyclerView.setAdapter(categoryAdapter);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View _view;

        public ViewHolder(View itemView) {
            super(itemView);
            _view = itemView;

        }

        void setImage(final String image, final Context context) {

            final CircleImageView imageView = (CircleImageView) _view.findViewById(R.id.image);

            Picasso.with(context).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.user).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError() {
                    Picasso.with(context).load(image).placeholder(R.drawable.user).into(imageView);
                }
            });
        }


        void setTitle(String title) {
            TextView textView = (TextView) _view.findViewById(R.id.title);
            textView.setText(title);

        }

        void setPoints(String points) {
            TextView textView = (TextView) _view.findViewById(R.id.point);
            textView.setText(points);
        }


    }
}
