
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
// MINIMAL SQLite database JAVA Code
public class MainActivity extends AppCompatActivity  implements View.OnClickListener{
    Button buttonAdd;
    Button buttonRead;
    Button buttonClear;
    EditText evName;
    EditText evEmail;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdd = (Button) findViewById(R.id.btnAdd);
        buttonAdd.setOnClickListener((View.OnClickListener) this);

        buttonRead = (Button) findViewById(R.id.btnRead);
        buttonRead.setOnClickListener((View.OnClickListener) this);

        buttonClear = (Button) findViewById(R.id.btnClear);
        buttonClear.setOnClickListener((View.OnClickListener) this);

        evName = (EditText) findViewById(R.id.etName);
        evEmail = (EditText) findViewById(R.id.etEmail);

        dbHelper = new DBHelper(this); // Create object for creating and managing database versions
    }

    @Override
    public void onClick(View v) {
        String name = evName.getText().toString();
        String email = evEmail.getText().toString();

        SQLiteDatabase database = dbHelper.getWritableDatabase(); // Connecting to the Database
        ContentValues contentValues = new ContentValues(); // Create an object for the data


        switch (v.getId()) {

            case R.id.btnAdd:
                contentValues.put(DBHelper.KEY_NAME, name); // Adds a new row KEY_NAME to the table
                contentValues.put(DBHelper.KEY_MAIL, email); // Adds a new row KEY_MAIL to the table

                long rowID = database.insert(DBHelper.TABLE_CONTACTS, null, contentValues); // Write to the database and get its ID
                Log.d("SQLite","== == Row inserted, ID = " + rowID);
                break;

            case R.id.btnRead:
                // We make a request for all data from the TABLE_CONTACTS table, we get the cursor
                Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
                int ecounter = database.rawQuery("SELECT _ID FROM contacts", null).getCount();
                Log.d("== == SQLite","Count rows = " + String.valueOf(ecounter));

                if (cursor.moveToFirst()) { // Set the cursor position to the first line of the selection
                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                    int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                    int emailIndex = cursor.getColumnIndex(DBHelper.KEY_MAIL);
                    do {
                        Log.d("mLog", "== Record ID = " + cursor.getInt(idIndex) +
                                ", Name = " + cursor.getString(nameIndex) +
                                ", E-mail = " + cursor.getString(emailIndex));
                    } while (cursor.moveToNext());
                } else
                    Log.d("mLog","0 rows");

                cursor.close();
                break;

            case R.id.btnClear: // Delete all entries
                database.delete(DBHelper.TABLE_CONTACTS, null, null);
                break;
        }
        dbHelper.close();
    }





}
