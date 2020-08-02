package com.example.android.firebasepushnotification.SendNotificationPack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAmIgAnNA:APA91bHcZhn-X7cFqgWUFeFCoRfKaQzDS_C8xaV6Uqd-K4Lj9mxkPLzphzzmLka0cLA3pKxLgIMh9KzFkmqnej65TCyx6_jrDL_9IG-tbbdaLbVn1kFK_Tn-bCGJYVn6uq_zLZ_YOc9h" // Your server key refer to video for finding your server key
           // Authorization Key is nothing but Firebase Server Key :p


            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);

}
