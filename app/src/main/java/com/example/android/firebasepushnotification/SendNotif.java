package com.example.android.firebasepushnotification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.firebasepushnotification.SendNotificationPack.APIService;
import com.example.android.firebasepushnotification.SendNotificationPack.Client;
import com.example.android.firebasepushnotification.SendNotificationPack.Data;
import com.example.android.firebasepushnotification.SendNotificationPack.MyResponse;
import com.example.android.firebasepushnotification.SendNotificationPack.NotificationSender;
import com.example.android.firebasepushnotification.SendNotificationPack.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendNotif extends AppCompatActivity {
    EditText UserTB,Title,Message;
    Button send;
    private APIService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notif);
        UserTB=findViewById(R.id.UserID);
        Title=findViewById(R.id.Title);
        Message=findViewById(R.id.Message);
        send=findViewById(R.id.button);

        // Link to Firebase Cloud Messaging API

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        //     UserTB.setText(FirebaseAuth.getInstance().getCurrentUser().getUid());

        // You can change the user ID depending on the requirement.
        // Below USER ID will get the notification.
        UserTB.setText("ob6Jhsvb7DTPEev5XE0cUP8lArJ3");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Tokens").child(UserTB.getText().toString().trim()).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String usertoken=dataSnapshot.getValue(String.class);
                        sendNotifications(usertoken, Title.getText().toString().trim(),Message.getText().toString().trim());
                        Toast.makeText(SendNotif.this, "Sent", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        UpdateToken();
    }

    private void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }

    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(SendNotif.this, "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }


}