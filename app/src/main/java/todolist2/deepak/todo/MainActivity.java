package todolist2.deepak.todo;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import todolist2.deepak.todo.db.TaskContract;
import todolist2.deepak.todo.db.TaskDBHelper;
/**
 * Created by Deepak on 19-Feb-16.
 */

public class MainActivity extends ListActivity {
	private ListAdapter listAdapter;
	private TaskDBHelper helper;
	ImageView img;
	TextView nameTv;
String name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		nameTv=(TextView)findViewById(R.id.toolbar_title);
		Intent intent=getIntent();
		if(intent.hasExtra("name")){
			name=intent.getExtras().getString("name");
			nameTv.setText(name);
		}

		img=(ImageView)findViewById(R.id.imageView);

		updateUI();
		img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, AlarmDetail.class);
				startActivity(i);
			}
		});

	}


	private void updateUI() {
		helper = new TaskDBHelper(MainActivity.this);
		SQLiteDatabase sqlDB = helper.getReadableDatabase();
		Cursor cursor = sqlDB.query(TaskContract.TABLE,
				new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK,
						TaskContract.Columns.DATE, TaskContract.Columns.TIME, TaskContract.Columns.LOC},
				null, null, null, null, TaskContract.Columns.DATE+" DESC");

		listAdapter = new SimpleCursorAdapter(
				this,
				R.layout.task_view,
				cursor,
				new String[]{TaskContract.Columns.TASK, TaskContract.Columns.DATE,TaskContract.Columns.TIME,TaskContract.Columns.LOC},
				new int[]{R.id.taskTextView,R.id.date1,R.id.time1,R.id.loc1},
				0
		);

		this.setListAdapter(listAdapter);
	}

	public void onDoneButtonClick(View view) {
		View v = (View) view.getParent();
		String id="";
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		PendingIntent pi;
		Intent i = new Intent(this, AlarmReceiver.class);
		TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
		String task = taskTextView.getText().toString();
		Cursor c;
		SQLiteDatabase db=helper.getReadableDatabase();
		c = db.query(TaskContract.TABLE, null, TaskContract.Columns.TASK + " = ?", new String[]{task}, null, null, null);
		if(c.moveToFirst()) {
			id = c.getString(0);
			nm.cancel(Integer.parseInt(id));
		}

		pi = PendingIntent.getBroadcast(this, Integer.parseInt(id), i, PendingIntent.FLAG_UPDATE_CURRENT);
		am.cancel(pi);
		String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
				TaskContract.TABLE,
				TaskContract.Columns.TASK,
				task);


		helper = new TaskDBHelper(MainActivity.this);
		SQLiteDatabase sqlDB = helper.getWritableDatabase();
		sqlDB.execSQL(sql);
		updateUI();
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateUI();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		//System.exit(0);
		finish();
	}
}
