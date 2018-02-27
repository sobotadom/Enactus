package enactus.budget.enactus;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Dominik on 2018-02-26.
 */

public class Notification_Reciever extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent){
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent p = PendingIntent.getActivity(context,100, i,PendingIntent.FLAG_UPDATE_CURRENT);
        if (intent != null){
            //String msg = intent.getStringExtra("msg");
            //String passfail = intent.getStringExtra("passfail");


            NotificationCompat.Builder b = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentText("HEY")
                    .setContentTitle("LOL")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(p)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("HELLO"));

            NotificationManager m = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);



            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                String CHANNEL_ID = "my_channel";
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Noitfication", NotificationManager.IMPORTANCE_DEFAULT);
                m.createNotificationChannel(channel);
                b.setChannelId(CHANNEL_ID);

            }
            m.notify(1,b.build());
        }



    }




}
