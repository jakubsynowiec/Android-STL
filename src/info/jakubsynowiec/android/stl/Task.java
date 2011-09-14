package info.jakubsynowiec.android.stl;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {

    public static final String TAG = "Task";

    private int _id;
    private String title;
    private Date datetime;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
