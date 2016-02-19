package todolist2.deepak.todo.db;

import android.provider.BaseColumns;

public class TaskContract {
	public static final String DB_NAME = "todolist";
	public static final int DB_VERSION = 2;
	public static final String TABLE = "tasks2";


	public class Columns {
		public static final String TASK = "task";
		public static final String _ID = BaseColumns._ID;
		public static final String DATE="date";
		public static final String TIME="time";
		public static final String LOC="loc";
	}
}
