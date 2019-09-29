package com.poum.cyclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.poum.cyclone.Model.CountryData;

public class AuthActivity extends AppCompatActivity {

    EditText phone;
    Spinner country;

    ProgressDialog progressDialog;


    FirebaseUser firebaseUser;

    @Override
    protected void onRestart() {
        super.onRestart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            Intent intent = new Intent(AuthActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);



        country = (Spinner) findViewById(R.id.country);
        country.setAdapter(new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, CountryData.CountryNames));

        phone = findViewById(R.id.phone);

        findViewById(R.id.btn_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(AuthActivity.this);
                progressDialog.setTitle("Vérification");
                progressDialog.setMessage("Patientez s'il vous plait ...");
                progressDialog.setProgressStyle(ProgressDialog.THEME_HOLO_DARK);
                progressDialog.show();
                progressDialog.setCancelable(true);

                final String code = CountryData.CountryCodes[country.getSelectedItemPosition()];
                final String number = phone.getText().toString().trim();

                if (number.isEmpty() || number.length() < 9) {
                    phone.setError("Un numéro valide est requis");
                    phone.requestFocus();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(1000);
                            String phonenumber = "+" + code + number;
                            Intent intent = new Intent(AuthActivity.this, VerificationActivity.class);
                            intent.putExtra("phonenumber", phonenumber);
                            startActivity(intent);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                    }
                }).start();
            }
        });
    }
}
