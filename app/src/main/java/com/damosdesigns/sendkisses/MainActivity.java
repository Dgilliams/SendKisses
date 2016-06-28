package com.damosdesigns.sendkisses;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView mPhoneInput;
    private TextView mRecipientName;
    private TextView mTitle;
    private View mPlus;

    private int kissCount = 0;

    private static final int CONTACT_PICKER_RESULT = 1001;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPhoneInput = (TextView) findViewById(R.id.input_phone_number);
        mRecipientName = (TextView) findViewById(R.id.recipient_name);
        mTitle = (TextView) findViewById(R.id.kiss_title);
        mPlus = findViewById(R.id.plus_icon);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    Cursor cursor = null;
                    String phone = "";
                    String name = "";
                    try {
                        Uri result = data.getData();
                        Log.v(TAG, "Got a contact result: "
                                + result.toString());

                        // get the contact id from the Uri
                        String id = result.getLastPathSegment();

                        // query for everything phone
                        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{id},
                                null);

                        int phoneIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA);
                        int nameIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);

                        // let's just get the first phone
                        if (cursor.moveToFirst()) {
                            phone = cursor.getString(phoneIdx);
                            name = cursor.getString(nameIdx);
                            Log.v(TAG, "Got phone: " + phone);
                        } else {
                            Log.w(TAG, "No results");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to get phone data", e);
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }

                        setUserPic(phone, name);

                        if (phone.length() == 0) {
                            Toast.makeText(this, "No phone found for contact.", Toast.LENGTH_LONG).show();
                        }

                    }

                    break;
            }

        } else {
            Log.w(TAG, "Warning: activity result not ok");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setUserPic(String phone, String name) {
        if (phone != null) {
            mPhoneInput.setText(phone);
            mPhoneInput.setVisibility(View.VISIBLE);
        }
        if (name != null) {
            mRecipientName.setText("Sending kisses to " + name);
            mRecipientName.setVisibility(View.VISIBLE);
        }

        mTitle.setVisibility(View.VISIBLE);
        mPlus.setVisibility(View.INVISIBLE);
    }


    private void setUserPicToPlus() {
        mPlus.setVisibility(View.VISIBLE);
    }

    public void incrementKissCount() {
        mTitle.setText(++kissCount + " Kisses Sent!");
    }
}
