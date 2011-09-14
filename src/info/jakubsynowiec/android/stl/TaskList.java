package info.jakubsynowiec.android.stl;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class TaskList extends Activity
{
    public static final String TAG = "TaskList";

    private Context context;

    private TaskOpenHelper taskOpenHelper;
    private SQLiteDatabase database;

    private Cursor taskQuery;

    private ListView listView;
    private ArrayList<Task> tasks;
    private TaskListItemAdapter itemAdapter;

    private void updateTaskList()
    {
        tasks.clear();
        taskQuery.requery();
        if(taskQuery.moveToFirst()) {
            Task t;
            do {
                t = new Task();
                t.set_id(taskQuery.getInt(taskQuery.getColumnIndex("_id")));
                t.setTitle(taskQuery.getString(taskQuery.getColumnIndex("label")));
                t.setDatetime(taskQuery.getString(taskQuery.getColumnIndex("datetime")));
                t.setCompleted(taskQuery.getInt(taskQuery.getColumnIndex("completed")));
                tasks.add(t);
            } while (taskQuery.moveToNext());
        }

        fillListData();
    }

    private void fillListData()
    {
        itemAdapter = new TaskListItemAdapter(this, tasks.toArray());
        listView.setAdapter(itemAdapter);
    }

    private void addNewTask(String taskDescription)
    {
        database.execSQL("INSERT INTO task ('label', 'datetime', 'completed') VALUES ('" + taskDescription + "', datetime(), 0);");
    }

    private void showNewTaskButtons()
    {
        setVisibilityOf(findViewById(R.id.NewTaskButtons), true);
        setVisibilityOf(findViewById(R.id.ControlButtons), false);
        setVisibilityOf(findViewById(R.id.TaskText), true);
    }

    private void showControlButtons()
    {
        setVisibilityOf(findViewById(R.id.NewTaskButtons), false);
        setVisibilityOf(findViewById(R.id.ControlButtons), true);
        setVisibilityOf(findViewById(R.id.TaskText), false);
    }

    private void setVisibilityOf(View v, boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        v.setVisibility(visibility);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        context = getApplicationContext();

        taskOpenHelper = new TaskOpenHelper(this);
        tasks = new ArrayList<Task>();
        listView = (ListView) findViewById(R.id.TaskList);

        ((Button) findViewById(R.id.btnNew)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showNewTaskButtons();
            }
        });

        ((Button) findViewById(R.id.btnCancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ((EditText) findViewById(R.id.TaskText)).setText("");
                showControlButtons();
            }
        });

        ((Button) findViewById(R.id.btnClear)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                updateTaskList();
            }
        });

        ((Button) findViewById(R.id.btnSave)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText editText = (EditText) findViewById(R.id.TaskText);
                String taskText = editText.getText().toString();
                if (taskText.length() == 0) {
                    editText.setError(context.getString(R.string.error_empty));
                } else {
                    addNewTask(taskText);
                    updateTaskList();
                    editText.setText("");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        database = taskOpenHelper.getWritableDatabase();
        taskQuery = database.rawQuery("SELECT _id, label, datetime, completed FROM task WHERE completed = 0 ORDER BY datetime ASC;", null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTaskList();
    }

    @Override
    protected void onPause() {
        super.onPause();

        taskQuery.close();
        database.close();
    }
}