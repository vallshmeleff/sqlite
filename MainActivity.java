
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{
    Button buttonAdd;
    Button buttonRead;
    Button buttonClear;
    Button buttonPrev;
    Button buttonNext;
    EditText evName;
    EditText evEmail;
    long rowID;
    int ie=0; // First record pointer
    int ecounter; // How many records are in the database

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

        buttonPrev = (Button) findViewById(R.id.btnPrev);
        buttonPrev.setOnClickListener((View.OnClickListener) this);

        buttonNext = (Button) findViewById(R.id.btnNext);
        buttonNext.setOnClickListener((View.OnClickListener) this);

        evName = (EditText) findViewById(R.id.etName);
        evEmail = (EditText) findViewById(R.id.etEmail);

        dbHelper = new DBHelper(this); // Create an object for creating and managing database versions
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

                rowID = database.insert(DBHelper.TABLE_CONTACTS, null, contentValues); // Write to the database and get its ID
                Log.d("SQLite","== == Row inserted, ID = " + rowID);
                break;

            case R.id.btnRead: // Set Recor 0
                // We make a request for all data from the TABLE_CONTACTS table, we get the cursor
                Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
                ecounter = database.rawQuery("SELECT _ID FROM NTable", null).getCount();
                Log.d("== == SQLite","Count rows = " + String.valueOf(ecounter));
                TextView evCounter = (TextView)findViewById(R.id.Rowcount);
                evCounter.setText("Total records: " + String.valueOf(ecounter) + " Row ID:  " + Objects.toString(rowID, null));

                int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                int emailIndex = cursor.getColumnIndex(DBHelper.KEY_MAIL);

                ie =0;
                cursor.moveToPosition(ie); // Go to first record Record 0
                Log.d("mLog", "== == SQLite == == " + " ie: " + String.valueOf(ie) + " ID: " + cursor.getInt(idIndex) +
                        ", Name = " + cursor.getString(nameIndex) +
                        ", E-mail = " + cursor.getString(emailIndex));

                cursor.close();
                break;

            case R.id.btnClear: // Delete all entries
                database.delete(DBHelper.TABLE_CONTACTS, null, null);
                break;

            case R.id.btnNext: // Next record
                Cursor cursorN = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
                ecounter = database.rawQuery("SELECT _ID FROM NTable", null).getCount();
                idIndex = cursorN.getColumnIndex(DBHelper.KEY_ID);
                nameIndex = cursorN.getColumnIndex(DBHelper.KEY_NAME);
                emailIndex = cursorN.getColumnIndex(DBHelper.KEY_MAIL);

                ie++;
                if (ie > ecounter-1) {
                    ie = 0;
                }
                cursorN.moveToPosition(ie); // Go to post

                Log.d("SQLite","====== Record =======" + " ie: " + String.valueOf(ie));
                cursorN.moveToPosition(ie); // Перейти к записи
                Log.d("mLog", "== == SQLite == == " + " ie: " + String.valueOf(ie) + " ID: " + cursorN.getInt(idIndex) +
                        ", Name = " + cursorN.getString(nameIndex) +
                        ", E-mail = " + cursorN.getString(emailIndex));

                cursorN.close();
                break;

            case R.id.btnPrev: // Previous record
                Cursor cursorP = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
                ecounter = database.rawQuery("SELECT _ID FROM NTable", null).getCount();
                idIndex = cursorP.getColumnIndex(DBHelper.KEY_ID);
                nameIndex = cursorP.getColumnIndex(DBHelper.KEY_NAME);
                emailIndex = cursorP.getColumnIndex(DBHelper.KEY_MAIL);

                ie--;
                if (ie <0 ) {
                    ie = ecounter-1;
                }
                cursorP.moveToPosition(ie); // Go to post

                Log.d("SQLite","====== Record 2 =======" + " ie: " + String.valueOf(ie));
                cursorP.moveToPosition(ie); // Перейти к записи
                Log.d("mLog", "== == SQLite == == " + " ie: " + String.valueOf(ie) + " ID: " + cursorP.getInt(idIndex) +
                        ", Name = " + cursorP.getString(nameIndex) +
                        ", E-mail = " + cursorP.getString(emailIndex));

                cursorP.close();
                break;
        }

        dbHelper.close();
    }

}
