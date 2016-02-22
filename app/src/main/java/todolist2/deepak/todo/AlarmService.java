package todolist2.deepak.todo;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import todolist2.deepak.todo.db.TaskContract;
import todolist2.deepak.todo.db.TaskDBHelper;
/**
 * Created by Deepak on 20-Feb-16.
 */

public class AlarmService extends IntentService {
	
	private static final String TAG = "AlarmService";
	private TaskDBHelper helper;
	public static final String POPULATE = "POPULATE";
	public static final String CREATE = "CREATE";
	public static final String KEEP = "KEEP";
	public static final String CANCEL = "CANCEL";
	SQLiteDatabase db;
	private IntentFilter matcher;
	NotificationManager nm;

	public AlarmService() {
		super(TAG);
		matcher = new IntentFilter();

		matcher.addAction(POPULATE);
		matcher.addAction(CREATE);
		matcher.addAction(KEEP);
		matcher.addAction(CANCEL);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		String action = intent.getAction();
		Log.i("action",action);
		String alarmId = intent.getStringExtra("alarmid");
	//	Log.d("alarmccancel",alarmId);
		helper = new TaskDBHelper(AlarmService.this);
		db = helper.getReadableDatabase();
		if (matcher.matchAction(action)) {
			if (POPULATE.equals(action)) {
				execute(CREATE, alarmId);
			}

			if(KEEP.equals(action)){
				nm.cancel(Integer.parseInt(alarmId));
				Log.d("yoy","ypy");
			}
			
			if (CANCEL.equals(action)) {
				execute(CANCEL, alarmId);
			}			
		}
	}

	/**
	 * @param action
	 * @param args {alarmId, alarmMsgId, startTime, endTime}
	 */	
	private void execute(String action, String... args) {
		Intent i;
		PendingIntent pi;				
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Cursor c;
		
		String alarmId = (args!=null && args.length>0) ? args[0] : null;
	//	Log.d("count",TaskContract.Columns._ID);
		c = db.query(TaskContract.TABLE, null, TaskContract.Columns._ID+" = ?", new String[]{alarmId}, null, null, null);

		if(c.moveToFirst()) {
			long now = System.currentTimeMillis();
			long time, diff;

			i = new Intent(this, AlarmReceiver.class);

			i.putExtra(TaskContract.Columns._ID, alarmId);
			i.putExtra(TaskContract.Columns.TASK,c.getString(1));
			i.putExtra(TaskContract.Columns.DATE,c.getString(2));
			i.putExtra(TaskContract.Columns.TIME,c.getString(3));
			i.putExtra(TaskContract.Columns.LOC,c.getString(4));
	//id		Log.d("hehe", c.getString(0));
	//task		Log.d("hehe",c.getString(1));
	//date		Log.d("hehe",c.getString(2));
//time
			pi = PendingIntent.getBroadcast(this, Integer.parseInt(alarmId), i, PendingIntent.FLAG_UPDATE_CURRENT);


			time = c.getLong(c.getColumnIndex(TaskContract.Columns.DATETIME));
			diff = time - now + (long) Util.MIN;
			if (CREATE.equals(action)) {
				if (diff > 0 && diff < Util.YEAR) {
					if((time-15*60*1000)<=System.currentTimeMillis()){
						am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 30 * 60 * 1000, pi);
					}else{
						am.setRepeating(AlarmManager.RTC_WAKEUP, time - 15 * 60 * 1000, 30 * 60 * 1000, pi);
					}
				}
			} else if (CANCEL.equals(action)) {
				am.cancel(pi);
				nm.cancel(Integer.parseInt(alarmId));


			}
		}

	}

}
