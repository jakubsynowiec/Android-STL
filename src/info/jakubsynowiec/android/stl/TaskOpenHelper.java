package info.jakubsynowiec.android.stl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "TaskOpenHelper";

    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "stl.db";
    private static final String DB_TASK_TABLE = "task";

    private static final String DB_TASK_TABLE_KEY_ID = "_id";
    public static final int DB_TASK_TABLE_KEY_ID_COLUMN = 0;
    private static final String DB_TASK_TABLE_KEY_LABEL = "label";
    public static final int DB_TASK_TABLE_KEY_LABEL_COLUMN = 1;
    private static final String DB_TASK_TABLE_KEY_DATE = "datetime";
    public static final int DB_TASK_TABLE_KEY_DATE_COLUMN = 2;
    private static final String DB_TASK_TABLE_KEY_COMPLETED = "completed";
    public static final int DB_TASK_TABLE_KEY_COMPLETED_COLUMN = 3;
    private static final String DB_TASK_TABLE_KEY_PRIORITY = "priority";
    public static final int DB_TASK_TABLE_KEY_PRIORITY_COLUMN = 4;
    private static final String DB_TASK_TABLE_KEY_NOTES = "notes";
    public static final int DB_TASK_TABLE_KEY_NOTES_COLUMN = 5;

    Context context;
    SQLiteDatabase database;

    public TaskOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    public void openDatabase()
    {
        database = getWritableDatabase();
    }

    public long insertTask(String taskLabel)
    {
        ContentValues newTaskValues = new ContentValues();
        newTaskValues.put(DB_TASK_TABLE_KEY_LABEL, taskLabel);
        return database.insert(DB_TASK_TABLE, null, newTaskValues);
    }

    public boolean updateTask(Task t)
    {
        String where = DB_TASK_TABLE_KEY_ID + "=" + String.valueOf(t.get_id());
        int completed = t.isCompleted() ? 1 : 0;
        ContentValues taskValues = new ContentValues();
        taskValues.put(DB_TASK_TABLE_KEY_LABEL, t.getLabel());
        taskValues.put(DB_TASK_TABLE_KEY_COMPLETED, completed);
        taskValues.put(DB_TASK_TABLE_KEY_NOTES, t.getNote());
        taskValues.put(DB_TASK_TABLE_KEY_PRIORITY, t.getPriority());
        return database.update(DB_TASK_TABLE, taskValues, where, null) > 0;
    }

    public Cursor getAllTasks()
    {
        String[] columns = {DB_TASK_TABLE_KEY_ID, DB_TASK_TABLE_KEY_LABEL, DB_TASK_TABLE_KEY_DATE, DB_TASK_TABLE_KEY_COMPLETED, DB_TASK_TABLE_KEY_NOTES, DB_TASK_TABLE_KEY_PRIORITY};
        String where = DB_TASK_TABLE_KEY_COMPLETED + "=0";
        String order = DB_TASK_TABLE_KEY_DATE + " ASC, " + DB_TASK_TABLE_KEY_PRIORITY + " DESC";
        return database.query(DB_TASK_TABLE, columns, where, null, null, null, order, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE task ("
                   + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + "label TEXT,"
                   + "datetime TEXT DEFAULT (DATETIME('NOW')),"
                   + "completed INTEGER DEFAULT 0,"
                   + "priority INTEGER DEFAULT 2,"
                   + "notes INTEGER DEFAULT NULL"
                   + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void close() {
        super.close();    //To change body of overridden methods use File | Settings | File Templates.
        if (database != null) {
            database.close();
        }
    }
}
