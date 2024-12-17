package com.example.lap08;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    String dbName = "Contact.db";
    String dbPath = "/databases/";
    SQLiteDatabase db = null;
    ArrayAdapter<Contact> adapter;
    ListView Lvcontact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        xuLyCopy();
        addview();
        hienthiContact();
    }
    private void hienthiContact() {
        try {
            db = openOrCreateDatabase(dbName, MODE_PRIVATE, null);
            Cursor cursor = db.rawQuery("SELECT * FROM Contact", null);
            while (cursor.moveToNext()) {
                int ma = cursor.getInt(0);
                String ten = cursor.getString(1);
                String dienthoai = cursor.getString(2);
                adapter.add(new Contact(ma, ten, dienthoai));
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("SQL Error", e.toString());
            Toast.makeText(this, "Lỗi khi truy vấn dữ liệu!", Toast.LENGTH_SHORT).show();
        }
    }
    private void addview() {
        Lvcontact=findViewById(R.id.Lvcontact);
        adapter=new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1);
        Lvcontact.setAdapter(adapter);
    }

    private void xuLyCopy() {
        try {
            File dbFile = getDatabasePath(dbName);
            if (!dbFile.exists()) {
                copyDataFromAsset();
                Toast.makeText(MainActivity.this, "Database đã được copy thành công!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Database đã tồn tại!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("CopyError", e.toString());
            Toast.makeText(MainActivity.this, "Lỗi khi copy database!", Toast.LENGTH_LONG).show();
        }
    }
    private void copyDataFromAsset() {
        try {
            InputStream myInput = getAssets().open(dbName);
            String outFileName = getDatabasePath(dbName).getPath(); // Đường dẫn đúng cho database
            File outFile = new File(outFileName);

            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }

            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
            Log.d("DB", "Database copied successfully");
        } catch (Exception e) {
            Log.e("CopyError", "Error copying database", e);
        }
    }



}