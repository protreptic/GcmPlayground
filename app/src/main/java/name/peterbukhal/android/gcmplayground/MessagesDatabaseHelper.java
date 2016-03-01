package name.peterbukhal.android.gcmplayground;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by petronic on 01.03.16.
 */
public class MessagesDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "messages";
    public static final int VERSION = 1;

    public MessagesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table messages ("
                + "id integer primary key autoincrement,"
                + "time text,"
                + "message text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
