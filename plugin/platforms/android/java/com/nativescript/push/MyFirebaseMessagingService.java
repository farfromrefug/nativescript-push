package com.nativescript.push;

import java.util.Map;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * This class takes care of notifications received while the app is in the
 * foreground.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
  static final String TAG = "FirebasePlugin";

  static boolean isActive = false;

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    try {
      final JSONObject json = new JSONObject().put("foreground", isActive).put("from", remoteMessage.getFrom());

      final RemoteMessage.Notification not = remoteMessage.getNotification();
      if (not != null) {
        json.put("title", not.getTitle()).put("body", not.getBody());
      }

      final Map<String, String> data = remoteMessage.getData();
      final JSONObject data_json = new JSONObject();
      for (Map.Entry<String, String> stringStringEntry : data.entrySet()) {
        data_json.put(stringStringEntry.getKey(), stringStringEntry.getValue());
      }
      json.put("data", data_json);

      FirebasePlugin.executeOnNotificationReceivedCallback(json.toString());
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  /**
   * Called if InstanceID token is updated. This may occur if the security of the
   * previous token had been compromised. Note that this is called when the
   * InstanceID token is initially generated so this is where you would retrieve
   * the token.
   */
  @Override
  public void onNewToken(String registrationToken) {
    Log.d(TAG, "Firebase #onNewToken registrationToken=" + registrationToken);
    FirebasePlugin.executeOnPushTokenReceivedCallback(registrationToken);

    // If you want to send messages to this application instance or
    // manage this apps subscriptions on the server side, send the
    // Instance ID token to your app server.
  }
  /**
   * 
   * /* private void sendNotification(String messageBody) { Intent intent = new
   * Intent(this, MainActivity.class);
   * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); PendingIntent pendingIntent
   * = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
   * 
   * Uri defaultSoundUri=
   * RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
   * NotificationCompat.Builder notificationBuilder = new
   * NotificationCompat.Builder(this)
   * .setSmallIcon(R.drawable.ic_stat_ic_notification) .setContentTitle("FCM
   * Message") .setContentText(messageBody) .setAutoCancel(true)
   * .setSound(defaultSoundUri) .setContentIntent(pendingIntent);
   * 
   * NotificationManager notificationManager = (NotificationManager)
   * getSystemService(Context.NOTIFICATION_SERVICE);
   * 
   * // 0 = id of notification notificationManager.notify(0,
   * notificationBuilder.build()); }
   */

}
