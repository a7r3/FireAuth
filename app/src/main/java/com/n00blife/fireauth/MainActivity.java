package com.n00blife.fireauth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FirebaseOptions primary = new FirebaseOptions.Builder()
                .setApiKey("getApiKeyByYourself")
                .setApplicationId("getApplicationId() returned null")
                .build();

        final ImageView imageView = findViewById(R.id.status_image);

        final TextView textView = findViewById(R.id.progress_text);

        final PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(MainActivity.this, "Success: " + phoneAuthCredential.getSmsCode(), Toast.LENGTH_LONG).show();
                imageView.setImageResource(R.drawable.ic_check_circle_black_24dp);
                textView.setVisibility(View.GONE);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
                imageView.setImageResource(R.drawable.ic_cancel_black_24dp);
                textView.setVisibility(View.GONE);
            }
        };

        Button verifyButton = findViewById(R.id.verify);

        final EditText phoneText = findViewById(R.id.phone);

        FirebaseApp.initializeApp(this, primary);

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(FirebaseApp.getInstance());

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phoneText.getText().length() != 10) { // bad xD
                    Toast.makeText(MainActivity.this,
                            "Enter a valid 10-digit number",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                textView.setVisibility(View.VISIBLE);

                PhoneAuthProvider phoneAuthProvider = PhoneAuthProvider.getInstance(firebaseAuth);
                phoneAuthProvider.verifyPhoneNumber(
                        phoneText.getText().toString(),
                        60,
                        TimeUnit.SECONDS,
                        MainActivity.this,
                        callbacks
                );
            }
        });

    }
}
