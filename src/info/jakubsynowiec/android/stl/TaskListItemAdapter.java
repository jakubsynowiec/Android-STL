package info.jakubsynowiec.android.stl;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class TaskListItemAdapter extends ArrayAdapter<Task> {
    private static final String TAG = "TaskListItemAdapter";

    private final Activity context;
    private final ArrayList<Task> tasks;

    private final TaskOpenHelper taskOpenHelper;

    public TaskListItemAdapter(Activity context, ArrayList<Task> tasks, TaskOpenHelper taskOpenHelper)
    {
        super(context, R.layout.tasklistitem, tasks);
        this.context = context;
        this.tasks = tasks;
        this.taskOpenHelper = taskOpenHelper;
    }

    static class ViewHolder {
        public CheckBox checkBox;
        public TextView label;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
        Task task;
		// Recycle existing view if passed as parameter
		// This will save memory and time on Android
		// This only works if the base layout for all classes are the same
		View taskListItem = convertView;
		if (taskListItem == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			taskListItem = inflater.inflate(R.layout.tasklistitem, null, true);
			holder = new ViewHolder();
			holder.label = (TextView) taskListItem.findViewById(R.id.TaskListItemLabel);
            holder.checkBox = (CheckBox) taskListItem.findViewById(R.id.TaskListItemCheckbox);
			taskListItem.setTag(holder);
		} else {
			holder = (ViewHolder) taskListItem.getTag();
		}

        task = (Task) tasks.get(position);

        if (task.isCompleted()) {
            holder.label.setPaintFlags(holder.label.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.label.setPaintFlags(holder.label.getPaintFlags() &~ Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.label.setText(task.getLabel());

        holder.checkBox.setChecked(task.isCompleted());
        holder.checkBox.setTag(position);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Task t = (Task) getItem((Integer) view.getTag());
                    int c = ((CheckBox) view).isChecked() ? 1 : 0;

                    t.setCompleted(c);
                    taskOpenHelper.updateTask(t);

                    notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });

		return taskListItem;
    }
}
