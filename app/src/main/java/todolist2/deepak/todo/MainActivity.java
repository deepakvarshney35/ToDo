package todolist2.deepak.todo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
				null, null, null, null, null);

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
		TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
		String task = taskTextView.getText().toString();



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
}
