package choremanager.ca.choremanager;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class Activity_Register extends AppCompatActivity implements View.OnClickListener {


    private EditText edit_pass, edit_email, edit_repass;

    private TextInputLayout input_layout_email, input_layout_password, input_layout_repassword;


    private FirebaseAuth _auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__register);


        _auth = FirebaseAuth.getInstance();


        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_pass = (EditText) findViewById(R.id.edit_pass);
        edit_repass = (EditText) findViewById(R.id.edit_repass);


        input_layout_email = (TextInputLayout) findViewById(R.id.input_layout_email);
        input_layout_password = (TextInputLayout) findViewById(R.id.input_layout_password);
        input_layout_repassword = (TextInputLayout) findViewById(R.id.input_layout_repassword);


        //textchangelistener
        edit_email.addTextChangedListener(new MyTextWatcher(edit_email));
        edit_pass.addTextChangedListener(new MyTextWatcher(edit_pass));
        edit_repass.addTextChangedListener(new MyTextWatcher(edit_repass));


        findViewById(R.id.btn_register).setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_register:
                RegisterAccount(edit_email.getText().toString(), edit_pass.getText().toString());
                break;

        }

    }


    private void RegisterAccount(String email, String pass) {

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        if (!validateRePassword()) {
            return;
        }


        if (Util_Func.isNetworkAvaliable(Activity_Register.this)) {
            final AlertDialog dialog = new SpotsDialog(Activity_Register.this, "Creating new Account...");
            dialog.show();
            _auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        dialog.dismiss();

                        SharedPreferences.Editor editor = getSharedPreferences("checkabout", MODE_PRIVATE).edit();
                        editor.putBoolean("done", false);
                        editor.apply();

                        startActivity(new Intent(Activity_Register.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();


                    } else {
                        dialog.dismiss();
                        Util_Func.Alert(Activity_Register.this, "Email Error", "Email already exist, use different!");
                    }

                }
            });

        } else {
            Util_Func.Alert(Activity_Register.this, "No Internet Connection", "Enable Wifi or Mobile Data");
        }


    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edit_email:
                    validateEmail();
                    break;
                case R.id.edit_pass:
                    validatePassword();
                    break;
                case R.id.edit_repass:
                    validateRePassword();
                    break;


            }
        }
    }


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateEmail() {
        String email = edit_email.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            input_layout_email.setError(getString(R.string.err_msg_email));
            requestFocus(edit_email);
            return false;
        } else {
            input_layout_email.setErrorEnabled(false);
        }
        return true;
    }


    private boolean validateRePassword() {
        if (edit_pass.getText().toString().trim().isEmpty() || !(edit_pass.getText().toString().equals(edit_repass.getText().toString()))) {
            input_layout_repassword.setError(getString(R.string.err_msg_repassword));
            requestFocus(edit_repass);
            return false;
        } else {
            input_layout_repassword.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validatePassword() {
        if (edit_pass.getText().toString().trim().isEmpty() || edit_pass.getText().length() < 6) {
            input_layout_password.setError(getString(R.string.err_msg_password));
            requestFocus(edit_pass);
            return false;
        } else {
            input_layout_password.setErrorEnabled(false);
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }


}
