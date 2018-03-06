package afterapps.com.firebaseim.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import afterapps.com.firebaseim.R;
import afterapps.com.firebaseim.home.MainActivity;

import static android.app.PendingIntent.getActivity;

/*
 * Created by Mahmoud on 3/13/2017.
 */

public class MessagingService extends FirebaseMessagingService {


    private static final String TAG = "MyFirebaseMsgService";
    private PowerManager.WakeLock wakeLock = null;
    private String title;
    private String message;
    private Bitmap resource;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (wakeLock != null) {
            return;
        }

        PowerManager pm = (PowerManager) getApplication().getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "");

        wakeLock.acquire();

//setExactAndAllowWhileIdle() 을 쓰면 될수도
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }

        if (remoteMessage.getNotification() == null) {
            title = remoteMessage.getData().get("name");
            message = remoteMessage.getData().get("content");
        } else {
            title = remoteMessage.getNotification().getTitle();
            message = remoteMessage.getNotification().getBody();
        }


        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        sendNotification(message);
        Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

    }


    private void sendNotification(String messageBody) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        resource = BitmapFactory.decodeResource(getResources(), R.drawable.bluebucket);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_message);
        builder.setTicker("Android Image downloaded.");
        builder.setContentTitle(title);
        builder.setContentText(messageBody);
        builder.setLargeIcon(resource);
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setSound(defaultSoundUri);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle
                .bigLargeIcon(resource)
                .bigPicture(resource)
                .setBigContentTitle("big title")
                .setSummaryText("Click on the image for full screen preview");

        builder.setStyle(bigPictureStyle);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, builder.build());
    }


}
