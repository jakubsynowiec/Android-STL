package info.jakubsynowiec.android.stl;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {

    public static final String TAG = "Task";

    private int _id;
    private String label;
    private Date datetime;
    private int priority;
    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    private boolean completed;

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = (completed != 0);
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.datetime = simpleDateFormat.parse(datetime);
        } catch (ParseException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }
}
