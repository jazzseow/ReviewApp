package com.example.review;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class NewReview extends AppCompatActivity{

    ReviewDBHandler db = null;
    Review newReview = null;

    private static final int SELECT_IMAGE = 100;
    private String TAG = "NewReviewActivity";

    private ImageView product1ImageView;
    private ImageView product2ImageView;

    private String filePath;
    private String filepath1;
    private String filepath2;

    boolean pic1Selected = false, pic2Selected = false;

    int picSelected = 0;
    //private Button addProductImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_review);

        product1ImageView = (ImageView) findViewById(R.id.product1ImageView);
        product2ImageView = (ImageView) findViewById(R.id.product2ImageView);
        //addProductImageButton = (Button) findViewById(R.id.addProductImage_Button);

        product1ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickFromGalleryIntent = new Intent(Intent.ACTION_PICK);
                pickFromGalleryIntent.setType("image/*");
                picSelected = 1;
                startActivityForResult(pickFromGalleryIntent, SELECT_IMAGE);
                pic1Selected = true;
            }
        });

        product2ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickFromGalleryIntent = new Intent(Intent.ACTION_PICK);
                pickFromGalleryIntent.setType("image/*");
                picSelected = 2;
                startActivityForResult(pickFromGalleryIntent, SELECT_IMAGE);
                pic2Selected = true;
            }
        });

        //addProductImageButton.setOnClickListener(this);

        db = new ReviewDBHandler(this);
        newReview = new Review();

        /******Seek bar Category updater code ******/
        final Spinner categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateCategories(getCategory());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /****** SeekBar TextView value updater code ******/

        //Get's the seek bars
        SeekBar seekBarOne = (SeekBar) findViewById(R.id.categoryOneSeekBar);
        SeekBar seekBarTwo = (SeekBar) findViewById(R.id.categoryTwoSeekBar);
        SeekBar seekBarThree = (SeekBar) findViewById(R.id.categoryThreeSeekBar);


        //Inner class to implement the listener to update the seek bar when it is moved
        class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateScoreText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                updateScoreText();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateScoreText();
            }
        }
        //Creates the listener for the seekbar
        SeekBarListener listener = new SeekBarListener();

        //Attaches the listener to the Seek bar
        seekBarOne.setOnSeekBarChangeListener(listener);
        seekBarTwo.setOnSeekBarChangeListener(listener);
        seekBarThree.setOnSeekBarChangeListener(listener);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //Returns the user created title for the review
    public String getReviewTitle() {
        EditText title = (EditText) findViewById(R.id.editTitleText);
        String titleString = title.getText().toString();
        return titleString;
    }

    //Returns the user choosen category
    public String getCategory() {
        Spinner category = (Spinner) findViewById(R.id.categorySpinner);
        String titleString = category.getSelectedItem().toString();
        return titleString;
    }

    //Updates the textViews with the value of the seek bars; triggered by the seek bar listener
    public void updateScoreText() {
        //Gets the text views and seekbars
        TextView scoreOne = (TextView) findViewById(R.id.SeekBarOneAmountTextView);
        TextView scoreTwo = (TextView) findViewById(R.id.SeekBarTwoAmountTextView);
        TextView scoreThree = (TextView) findViewById(R.id.SeekBarThreeAmountTextView);

        SeekBar seekBarOne = (SeekBar) findViewById(R.id.categoryOneSeekBar);
        SeekBar seekBarTwo = (SeekBar) findViewById(R.id.categoryTwoSeekBar);
        SeekBar seekBarThree = (SeekBar) findViewById(R.id.categoryThreeSeekBar);

        //Updates text view strings
        if (scoreOne != null) {
            if (seekBarOne != null) {
                scoreOne.setText(String.valueOf(seekBarOne.getProgress() / 10.0));
            }
        }
        if (scoreTwo != null) {
            if (seekBarTwo != null) {
                scoreTwo.setText(String.valueOf(seekBarTwo.getProgress() / 10.0));
            }
        }
        if (scoreThree != null) {
            if (seekBarThree != null) {
                scoreThree.setText(String.valueOf(seekBarThree.getProgress() / 10.0));
            }
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_review_toolbar_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.publish_button:
                if (getReviewTitle().length() == 0 || getReviewText().length() == 0){
                    Toast.makeText(this, "Some fields are empty", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else if (!pic1Selected || !pic2Selected){
                    Toast.makeText(this, "Requires 2 pictures to be added", Toast.LENGTH_SHORT).show();
                    return false;
                }
                updateDB();
                return true;
            case R.id.cancel_button:
                finish();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //Reference : http://stackoverflow.com/a/2508138
        super.onActivityResult(requestCode, resultCode, intent);
        if ((requestCode == SELECT_IMAGE) && (resultCode == RESULT_OK)) {
            Uri selectedImage = intent.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            Log.d(TAG, "Filepath" + filePath);

            if(picSelected == 1){
                product1ImageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
                filepath1 = filePath;
            }
            else if (picSelected == 2){
                product2ImageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
                filepath2 = filePath;
            }

            cursor.close();

            /*
            pref = getSharedPreferences("account", MODE_PRIVATE);
            editor = pref.edit();
            editor.putString("profilePhoto", filePath).commit();
            editor.putBoolean("locallyChangedPhoto", true).commit();
            */
        }
    }


    //TODO currently doesn't update from the database
    //Updates seekbar rating categories based on the database
    public void updateCategories(String selectedItem) {
        TextView categoryOne = (TextView) findViewById(R.id.categoryOneTextView);
        TextView categoryTwo = (TextView) findViewById(R.id.categoryTwoTextView);
        TextView categoryThree = (TextView) findViewById(R.id.categoryThreeTextView);

        switch (selectedItem) {
            case "Phone":
            case "Laptop":
                categoryOne.setText("Performance");
                categoryTwo.setText("Portability");
                categoryThree.setText("Battery Life");
                break;
            case "Printer":
                categoryOne.setText("Print Quality");
                categoryTwo.setText("Refill Cost");
                categoryThree.setText("Price");
                break;
        }
    }

    public void updateDB() {
        newReview = new Review(0, getReviewTitle(), getCategory(), getOverallScore(), getReviewScoreOne(),
                getReviewScoreTwo(), getReviewScoreThree(), getReviewText(), 0, 0, 0, 0, filepath1, filepath2, 0);

        db.addReview(newReview);

        Toast.makeText(this, "Review added!", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MainActivity.class));
    }

    public String getReviewText() {
        EditText reviewContent = (EditText) findViewById(R.id.reviewEditText);
        String reviewContentString = reviewContent.getText().toString();
        return reviewContentString;
    }

    public double getReviewScoreOne() {
        SeekBar seekBar = (SeekBar) findViewById(R.id.categoryOneSeekBar);
        double seekBarScore = seekBar.getProgress();
        return seekBarScore;
    }

    public double getReviewScoreTwo() {
        SeekBar seekBar = (SeekBar) findViewById(R.id.categoryTwoSeekBar);
        double seekBarScore = seekBar.getProgress();
        return seekBarScore;
    }

    public double getReviewScoreThree() {
        SeekBar seekBar = (SeekBar) findViewById(R.id.categoryThreeSeekBar);
        double seekBarScore = seekBar.getProgress();
        return seekBarScore;
    }

    public double getOverallScore() {
        DecimalFormat df = new DecimalFormat("#.00");
        double average = (getReviewScoreOne() + getReviewScoreTwo() + getReviewScoreThree()) / 30;
        return Double.parseDouble(df.format(average));
    }
}