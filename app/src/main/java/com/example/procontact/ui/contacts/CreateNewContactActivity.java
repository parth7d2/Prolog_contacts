package com.example.procontact.ui.contacts;



import android.Manifest;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.procontact.MainActivity;
import com.example.procontact.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class CreateNewContactActivity extends AppCompatActivity {

    // creating a new variable for our edit text and button.
    private EditText firstNameEt, lastNameEt, phoneMobileEt, phoneHomeEt, emailEt, addressEt;
    private FloatingActionButton fabSave;
    private ImageView thumbnailIv;

    //TAG
    private static final String TAG = "CONTACT_TAG";
    //Write contact permission request constant
    private static final int WRITE_CONTACT_PERMISSION_CODE = 100;
    //Image pick(gallery) intent constant
    private static final int IMAGE_PICK_GALLERY_CODE = 200;
    // array of permissions to request for Write contact
    private String[] contactPermissions;

    //image Uri
    private Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_contact);

        //init permission array, need olny write contact permission
        contactPermissions = new String[]{Manifest.permission.WRITE_CONTACTS};
        // on below line we are initializing our variables.
//        nameEdt = findViewById(R.id.idEdtName);
//        phoneEdt = findViewById(R.id.idEdtPhoneNumber);
//        emailEdt = findViewById(R.id.idEdtEmail);
//        addContactEdt = findViewById(R.id.idBtnAddContact);
        thumbnailIv = findViewById(R.id.thumbnailIv);
        fabSave = findViewById(R.id.fabSave);
        firstNameEt = findViewById(R.id.firstNameEt);
        lastNameEt = findViewById(R.id.lastNameEt);
        phoneMobileEt = findViewById(R.id.phoneMobileEt);
        phoneHomeEt =findViewById(R.id.phoneHomeEt);
        emailEt = findViewById(R.id.emailEt);
        addressEt = findViewById(R.id.addressEt);

        // on below line we are adding on click listener for our button.
        thumbnailIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting text from our edit text.
//                String name = nameEdt.getText().toString();
//                String phone = phoneEdt.getText().toString();
//                String email = emailEdt.getText().toString();

                // on below line we are making a text validation.
//                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(phone)) {
//                    Toast.makeText(CreateNewContactActivity.this, "Please enter the data in all fields. ", Toast.LENGTH_SHORT).show();
//                } else {
//                    // calling a method to add contact.
//                    addContact(name, email, phone);
//                }
                openGalleryIntent();
            }
        });

        // fabSave click, to save contact in contacts list
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(isWriteContactPermissionEnabled()) {
                   // permission already enabled, save contact
                   saveContact();

//                if (TextUtils.isEmpty(firstName) && TextUtils.isEmpty(email) && TextUtils.isEmpty(phoneMobile)) {
//                    Toast.makeText(CreateNewContactActivity.this, "Please enter the data in all fields. ", Toast.LENGTH_SHORT).show();
//                } else {
//                    // calling a method to add contact.
//                    addContact(firstName, lastName, phoneMobile, phoneHome, email, address);
//                }

               }
               else{
                   // permission not enabled, request
                   requestWriteContactPermission();
               }
            }
        });

    }



    private void saveContact() {
        // input data
        String firstName = firstNameEt.getText().toString().trim();
        String lastName = lastNameEt.getText().toString().trim();
        String phoneMobile = phoneMobileEt.getText().toString().trim();
        String phoneHome = phoneHomeEt.getText().toString().trim();
        String email = emailEt.getText().toString().trim();
        String address = addressEt.getText().toString().trim();

        ArrayList<ContentProviderOperation> cpo = new ArrayList<>();

        //contact id
        int rawContactId = cpo.size();

        cpo.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE,null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME,null)
        .build());

        //Add First, Last name
        cpo.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID,rawContactId)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, firstName)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, lastName)
                .build());

        //Add Phone number(Mobile)
        cpo.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneMobile)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());

        //Add Phone number(Home)
        cpo.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,phoneHome)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                .build());

        //Add email
        cpo.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE,ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK) //add/change any type
                .build());

        //Add address
        cpo.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.SipAddress.DATA, address)
                .withValue(ContactsContract.CommonDataKinds.SipAddress.TYPE, ContactsContract.CommonDataKinds.SipAddress.TYPE_WORK)
                .build());



        //get image, convert image to bytes to store in contact
        byte[] imageBytes = imageUriToBytes();

        if(imageBytes != null){
            //contact with image
            //add image
            cpo.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId)
                    .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, imageBytes)
                    .build());
        }
        else {
            //contact without image
        }

        //save contact
        try{
            ContentProviderResult[] results = getContentResolver().applyBatch(ContactsContract.AUTHORITY, cpo);
            Log.d(TAG, "saveContact: Saved...");
            Toast.makeText(this, "Saved...", Toast.LENGTH_SHORT).show();

        } catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "saveContact: " + e.getMessage());
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private byte[] imageUriToBytes() {
        Bitmap bitmap;
        ByteArrayOutputStream baos = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image_uri);
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            return baos.toByteArray();
        }
        catch (Exception e){
            Log.d(TAG, "imageUriToBytes: " + e.getMessage());
            return null;
        }
    }

    private void openGalleryIntent() {
        //intent to pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private boolean isWriteContactPermissionEnabled(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestWriteContactPermission(){
        ActivityCompat.requestPermissions(this,contactPermissions, WRITE_CONTACT_PERMISSION_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // handle permission result
//
        if(grantResults.length>0){
            if(requestCode == WRITE_CONTACT_PERMISSION_CODE){
                boolean haveWriteContactPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(haveWriteContactPermission){
                    // permissoion granted, save contact
                    saveContact();
                }
                else{
                    // permission denied, can't save contact
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

//    private void addContact(String firstname, String lastname, String mobile, String phone, em) {
//        // in this method we are calling an intent and passing data to that
//        // intent for adding a new contact.
//        Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
//        contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
//        contactIntent
//                .putExtra(ContactsContract.Intents.Insert.NAME, name)
//                .putExtra(ContactsContract.Intents.Insert.PHONE, phone)
//                .putExtra(ContactsContract.Intents.Insert.EMAIL, email);
//        startActivityForResult(contactIntent, 1);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == 1) {
//            // we are checking if the request code is 1
//            if (resultCode == Activity.RESULT_OK) {
//                // if the result is ok we are displaying a toast message.
//                Toast.makeText(this, "Contact has been added.", Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(CreateNewContactActivity.this, MainActivity.class);
//                startActivity(i);
//            }
//            // else we are displaying a message as contact addition has cancelled.
//            if (resultCode == Activity.RESULT_CANCELED) {
//                Toast.makeText(this, "Cancelled Added Contact",
//                        Toast.LENGTH_SHORT).show();
//            }
//        }

        if(resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                // image picked
                image_uri = data.getData();
                // set to imageView
                thumbnailIv.setImageURI(image_uri);
            }
        }
        else{
                //cancelled
                Toast.makeText(this, "Cancelled...",Toast.LENGTH_SHORT).show();
        }
    }
}
