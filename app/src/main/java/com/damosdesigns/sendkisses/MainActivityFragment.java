package com.damosdesigns.sendkisses;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    MainActivity mActivity;

    public MainActivityFragment() {
    }

    private final String TAG = "MainActivityFragment";

    private View mRoot;
    private View mKissButton;
    private View mRecipientImage;
    private TextView mPhoneInput;

    private boolean mHasReadContactsPermission = false;
    private boolean mHasSMSPermission = false;
    private boolean mPhoneInputIsValid = true;

    private final int SMS_PERMISSION_REQUEST_CODE = 0;
    private final int READ_CONTACTS_PERMISSION_REQUEST_CODE = 1;
    private static final int CONTACT_PICKER_RESULT = 1001;
    private Vibrator vibe;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mActivity = (MainActivity) getActivity();

        mRoot = inflater.inflate(R.layout.fragment_main, container, false);
        mPhoneInput = (TextView) mRoot.findViewById(R.id.input_phone_number);

        mKissButton = mRoot.findViewById(R.id.kiss_button);
        mKissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibe = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(500);
                checkSMSPermission();
                if (mHasSMSPermission) {
                    if (mPhoneInputIsValid) {
                        if (sendSMS(getContext(), mPhoneInput.getText().toString(), "\uD83D\uDE17")) {
                            mActivity.incrementKissCount();
                        }
                    } else
                        Toast.makeText(getContext(), "Recipient doesn't have a supported phone number", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Permission to send SMS is disabled. Re-enable it in settings.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mRecipientImage = mRoot.findViewById(R.id.recipient_image);
        mRecipientImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkReadContactsPermission();
                if (mHasReadContactsPermission) {
                    doLaunchContactPicker(v);
                } else {
                    Toast.makeText(getContext(), "Permission to Read Contacts is disabled. Re-enable it in settings.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return mRoot;
    }


    private void checkSMSPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.SEND_SMS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.SEND_SMS},
                        SMS_PERMISSION_REQUEST_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            mHasSMSPermission = true;
        }
    }

    private void checkReadContactsPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_CONTACTS},
                        READ_CONTACTS_PERMISSION_REQUEST_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            mHasReadContactsPermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SMS_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // task you need to do.
                    mHasSMSPermission = true;
                    sendSMS(getContext(), mPhoneInput.getText().toString(), "Kiss!");

                } else {
                    mHasSMSPermission = false;
                    Toast.makeText(getContext(), "Sorry, but no kisses because you've disabled permissions", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }
            case READ_CONTACTS_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // task you need to do.
                    mHasSMSPermission = true;
                    sendSMS(getContext(), mPhoneInput.getText().toString(), "Kiss!");

                } else {
                    mHasSMSPermission = false;
                    Toast.makeText(getContext(), "Sorry, but no kisses because you've disabled Read Contacts permissions", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public void doLaunchContactPicker(View view) {

        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        getActivity().startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }

    public static boolean sendSMS(Context context, String phoneNumber, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, msg, null, null);
            Toast.makeText(context, "Kisses sent to your love!", Toast.LENGTH_LONG).show();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Dearest Apologies, message not sendable. Have you selected a recipient? Click the heart!", Toast.LENGTH_LONG).show();
            return false;
        }
    }


}
