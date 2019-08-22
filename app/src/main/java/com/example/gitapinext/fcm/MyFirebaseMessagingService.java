package com.example.gitapinext.fcm;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.gitapinext.app.AppCoordinator;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "log_tag";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        String name = null;
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            name = remoteMessage.getData().get("userId");

        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            name = remoteMessage.getNotification().getBody();
        }
        if (name != null) {
            String finalName = name;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    AppCoordinator.getInstance().getRepository().setCountByUser(finalName);
                }
            });
        }
    }

//    private void sendNotification(Chat chat) {
//        Intent intent = new Intent(this, ChatActivity.class);
//        intent.putExtra("token", chat.getCompanionToken());
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                0);
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(chat.getCompanionName())
//                .setContentText(chat.getMessage())
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0, notificationBuilder.build());
//    }


}
