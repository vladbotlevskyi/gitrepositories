package com.example.gitapinext.fcm;


import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.gitapinext.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;


public class SubscribeToTopicUpdate {

    public static void subscribe(Activity activity){
        FirebaseMessaging.getInstance().subscribeToTopic("update")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "successfully subscribed";
                        if (!task.isSuccessful()) {
                            msg = "failed to subscribe";
                        }
                        Log.d("SUBSCRIPTION", msg);
                    }
                });
    }
}
