package com.example.review;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Simran on 17-Sep-16.
 */
public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final int SELECT_IMAGE = 100;

    private Button changePhotoButton;
    private Button changeUsernameButton;
    private Button deleteReviewsButton;

    private TextView usernameTextView;
    private TextView dateJoinedTextView;
    private TextView numOfReviewsTextView;
    private TextView numOfCommentsTextView;

    private ImageView profilePhotoImageView;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private boolean signedInFB, isLocallyEdited, locallyChangedPhoto;

    private String TAG = "UpdateProfileActivity";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);

        pref = getSharedPreferences("account", MODE_PRIVATE);
        signedInFB = pref.getBoolean("signedInAsFB", true);
        isLocallyEdited = pref.getBoolean("locallyEdited", false);
        locallyChangedPhoto = pref.getBoolean("locallyChangedPhoto", false);

        String name = pref.getString("name", "nil");
        String surname = pref.getString("surname", "nil");

        usernameTextView = (TextView) findViewById(R.id.username_TextView);
        if(!isLocallyEdited) {
            usernameTextView.setText(name + " " + surname);
        }
        else if (isLocallyEdited){
            usernameTextView.setText(pref.getString("editedName", "NULL"));
        }

        dateJoinedTextView = (TextView) findViewById(R.id.dateJoined_TextView);
        numOfReviewsTextView = (TextView) findViewById(R.id.numOfReviews_TextView);
        numOfCommentsTextView = (TextView) findViewById(R.id.numOfComments_TextView);

        ReviewDBHandler dbHandler = new ReviewDBHandler(getApplicationContext());
        numOfReviewsTextView.setText(String.valueOf(dbHandler.getNReview()));
        numOfCommentsTextView.setText(String.valueOf(dbHandler.getNComment()));

        changePhotoButton = (Button) findViewById(R.id.changePhoto_Button);
        changeUsernameButton = (Button) findViewById(R.id.changeName_Button);
        deleteReviewsButton = (Button) findViewById(R.id.deleteReview_Button);

        profilePhotoImageView = (ImageView) findViewById(R.id.photo);
        if(!locallyChangedPhoto) {
            if (!signedInFB) {
                profilePhotoImageView.setImageResource(R.drawable.com_facebook_profile_picture_blank_portrait);
            } else if (signedInFB) {
                String imageUrl = pref.getString("imageUrl", "nil");
                new DownloadImage(profilePhotoImageView).execute(imageUrl);
            }
        }
        else {
            profilePhotoImageView.setImageBitmap(BitmapFactory.decodeFile(pref.getString("profilePhoto", "NULL")));
        }

        changePhotoButton.setOnClickListener(this);
        changeUsernameButton.setOnClickListener(this);
        deleteReviewsButton.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.changePhoto_Button:
                changePhoto();
                break;

            case R.id.changeName_Button:
                displayChangeUsernameDialog();
                break;

            case R.id.deleteReview_Button:
                ReviewDBHandler dbHandler = new ReviewDBHandler(this);
                dbHandler.deleteAll();
                break;
        }
    }

    public void changePhoto()
    {
        Toast.makeText(this, "Change Photo", Toast.LENGTH_SHORT).show();

        Intent pickFromGalleryIntent = new Intent(Intent.ACTION_PICK);
        pickFromGalleryIntent.setType("image/*");
        startActivityForResult(pickFromGalleryIntent, SELECT_IMAGE);
    }

    public void displayChangeUsernameDialog()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Change Username");
        alertDialog.setMessage("Enter new username:");

        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        alertDialog.setView(input);

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newUsername = input.getText().toString();
                changeUsername(newUsername);
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public void changeUsername(String newUsername)
    {
//        Toast.makeText(this, "Add code to change username", Toast.LENGTH_SHORT).show();

        pref = getSharedPreferences("account", MODE_PRIVATE);
        editor = pref.edit();
        editor.putBoolean("locallyEdited", true);
        editor.putString("editedName", newUsername);
        editor.commit();

        usernameTextView.setText(newUsername);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        //Reference : http://stackoverflow.com/a/2508138
        super.onActivityResult(requestCode, resultCode, intent);
        if((requestCode == SELECT_IMAGE) && (resultCode == RESULT_OK))
        {
            Uri selectedImage = intent.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            Log.d(TAG, "Filepath" + filePath);
            cursor.close();
            profilePhotoImageView.setImageBitmap(BitmapFactory.decodeFile(filePath));

            pref = getSharedPreferences("account", MODE_PRIVATE);
            editor = pref.edit();
            editor.putString("profilePhoto", filePath).commit();
            editor.putBoolean("locallyChangedPhoto", true).commit();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

