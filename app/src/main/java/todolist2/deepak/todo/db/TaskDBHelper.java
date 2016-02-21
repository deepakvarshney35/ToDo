package todolist2.deepak.todo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.gcm.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import todolist2.deepak.todo.Util;


public class TaskDBHelper extends SQLiteOpenHelper {
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");


	public TaskDBHelper(Context context) {
		super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqlDB) {

		String sqlQuery =
				String.format("CREATE TABLE %s (" +
						"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"%s TEXT, date TEXT, time TEXT, loc TEXT, datetime TEXT)", TaskContract.TABLE, TaskContract.Columns.TASK);

		Log.d("TaskDBHelper","Query to form table: "+sqlQuery);
		sqlDB.execSQL(sqlQuery);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
		sqlDB.execSQL("DROP TABLE IF EXISTS " + TaskContract.TABLE);
		onCreate(sqlDB);
	}

	public void shred(SQLiteDatabase db) {
		//db.delete(TaskContract.TABLE, AlarmMsg.COL_STATUS + " = ?", new String[]{AlarmMsg.CANCELLED});
	}
}
