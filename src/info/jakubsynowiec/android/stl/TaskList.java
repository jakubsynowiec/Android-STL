package info.jakubsynowiec.android.stl;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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

    private ListView listView;
    private ArrayList<Task> tasks;
    private TaskListItemAdapter itemAdapter;

    private void updateTaskList()
    {
        // clear list
        tasks.clear();
        Cursor taskQuery = taskOpenHelper.getAllTasks();
        // if there are tasks in query then add them to list
        if (taskQuery.moveToFirst()) {
            Task t;
            do {
                try {
                    // create a new task
                    t = new Task();
                    // and set parameters
                    t.set_id(taskQuery.getInt(TaskOpenHelper.DB_TASK_TABLE_KEY_ID_COLUMN));
                    t.setLabel(taskQuery.getString(TaskOpenHelper.DB_TASK_TABLE_KEY_LABEL_COLUMN));
                    t.setDatetime(taskQuery.getString(TaskOpenHelper.DB_TASK_TABLE_KEY_DATE_COLUMN));
                    t.setCompleted(taskQuery.getInt(TaskOpenHelper.DB_TASK_TABLE_KEY_COMPLETED_COLUMN));
                    // add task to list
                    tasks.add(t);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            } while (taskQuery.moveToNext());
        }
        taskQuery.close();
        fillListData();
    }

    private void fillListData()
    {
        itemAdapter = new TaskListItemAdapter(this, tasks, taskOpenHelper);
        listView.setAdapter(itemAdapter);
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
                    taskOpenHelper.insertTask(taskText);
                    updateTaskList();
                    editText.setText("");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        taskOpenHelper.openDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTaskList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        taskOpenHelper.close();
    }
}