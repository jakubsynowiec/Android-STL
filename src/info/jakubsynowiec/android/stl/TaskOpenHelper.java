package info.jakubsynowiec.android.stl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskOpenHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "stl.db";

    Context context;

    public TaskOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    //TODO: Method for fetching all tasks

    //TODO: Method for updating a task

    //TODO: Method for creating a new task

    //TODO: Strip SQL and use update/insert methods

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE task ("
                   + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + "label TEXT,"
                   + "datetime TEXT,"
                   + "completed INTEGER"
                   + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
