package todolist2.deepak.todo;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import todolist2.deepak.todo.db.TaskContract;
/**
 * Created by Deepak on 21-Feb-16.
 */

public class AlarmReceiver extends BroadcastReceiver {
	
//	private static final String TAG = "AlarmReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		String alarmId = intent.getStringExtra(TaskContract.Columns._ID);
		String task=intent.getStringExtra(TaskContract.Columns.TASK);
		String date=intent.getStringExtra(TaskContract.Columns.DATE);
		String time=intent.getStringExtra(TaskContract.Columns.TIME);
		String loc=intent.getStringExtra(TaskContract.Columns.LOC);
		Log.d("alarmId", alarmId + "");
		Intent snoozeIntent=new Intent(context,AlarmService.class);
		snoozeIntent.setAction(AlarmService.KEEP);
		snoozeIntent.putExtra("alarmid", alarmId);
		PendingIntent snoozePendingIntent = PendingIntent.getActivity(context, Integer.parseInt(alarmId), snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);


		Intent cancelIntent=new Intent(context,AlarmService.class);
		cancelIntent.setAction(AlarmService.CANCEL);
		cancelIntent.putExtra("alarmid", alarmId);
		PendingIntent cancelPendingIntent = PendingIntent.getService(context, Integer.parseInt(alarmId), cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setContentTitle(task);
		mBuilder.setContentText("LOC: "+loc);
		mBuilder.setContentInfo("TIME: "+time);
		mBuilder.setContentInfo("DATE: "+date);

		mBuilder.setSmallIcon(R.drawable.plus_pressed);
		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		mBuilder.setSound(alarmSound);
		mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
		mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
		mBuilder.addAction(R.drawable.snooze,"Snooze",snoozePendingIntent);
		mBuilder.addAction(R.drawable.plus_unpressed, "Cancel", cancelPendingIntent);
		Intent notificationIntent=new Intent(context,MainActivity.class);

		PendingIntent pi = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		mBuilder.setContentIntent(pi);


		nm.notify(Integer.parseInt(alarmId), mBuilder.build());

	}

}
