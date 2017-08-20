package com.example.review;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReviewArticleActivity extends AppCompatActivity {

    private Intent intent;

    private ImageView profilePic, pic1, pic2;

    private ReviewDBHandler dbHandler = new ReviewDBHandler(this);
    public int reviewID;

    private SharedPreferences pref;
    boolean signedInFB, isLocallyEdited, locallyChangedPhoto;

    boolean isImageFitToScreen = false;
    private ViewGroup mContainerView;

    private  List<Comment> comments;
    String name, surname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContainerView = (ViewGroup) findViewById(R.id.container);

        ScrollView scrollView = (ScrollView) this.findViewById(R.id.scrollView);
        scrollView.requestFocus();

        intent = getIntent();
        reviewID = intent.getIntExtra("reviewID", 0);

        Review review = dbHandler.getReview(reviewID);
        setTitle(review.getTitle());

        //first card
        TextView reviewer = (TextView) findViewById(R.id.article_profile_name);
        profilePic = (ImageView) findViewById(R.id.article_profile_pic);

        pref = getSharedPreferences("account", MODE_PRIVATE);
        signedInFB = pref.getBoolean("signedInAsFB", true);
        isLocallyEdited = pref.getBoolean("locallyEdited", false);
        locallyChangedPhoto = pref.getBoolean("locallyChangedPhoto", false);

        name = pref.getString("name", "nil");
        surname = pref.getString("surname", "nil");

        if(signedInFB == true) {
            String imageUrl = pref.getString("imageUrl", "nil");
            if(!locallyChangedPhoto)
                new DownloadImage(profilePic).execute(imageUrl);
            else
                profilePic.setImageBitmap(BitmapFactory.decodeFile(pref.getString("profilePhoto", "NULL")));
            if(!isLocallyEdited)
                reviewer.setText(name + " " + surname);
            else
                reviewer.setText(pref.getString("editedName", "NULL"));
        } else if (signedInFB == false) {
            if(!locallyChangedPhoto)
                profilePic.setImageResource(R.drawable.com_facebook_profile_picture_blank_portrait);
            else
                profilePic.setImageBitmap(BitmapFactory.decodeFile(pref.getString("profilePhoto", "NULL")));
            if(!isLocallyEdited)
                reviewer.setText(name + " " + surname);
            else
                reviewer.setText(pref.getString("editedName", "NULL"));
        } else {
            profilePic.setImageResource(R.drawable.com_facebook_profile_picture_blank_portrait);
        }


        //2nd card

        TextView attr1 = (TextView) findViewById(R.id.article_attr1);
        TextView attr2 = (TextView) findViewById(R.id.article_attr2);
        TextView attr3 = (TextView) findViewById(R.id.article_attr3);

        String categoryName = review.getCategory();
        Category cat = dbHandler.getCategory(categoryName);

        attr1.setText(cat.getAttr1Name());
        attr2.setText(cat.getAttr2Name());
        attr3.setText(cat.getAttr3Name());

        ProgressBar overallProgress = (ProgressBar) findViewById(R.id.article_overall_bar);
        ProgressBar attr1Progress = (ProgressBar) findViewById(R.id.article_attr1_bar);
        ProgressBar attr2Progress = (ProgressBar) findViewById(R.id.article_attr2_bar);
        ProgressBar attr3Progress = (ProgressBar) findViewById(R.id.article_attr3_bar);

        double overallValue = (review.getAttr1() + review.getAttr2() + review.getAttr3())/3;

        overallProgress.setProgress((int)(overallValue));
        attr1Progress.setProgress((int)(review.getAttr1()));
        attr2Progress.setProgress((int)(review.getAttr2()));
        attr3Progress.setProgress((int)(review.getAttr3()));

        TextView overallScore = (TextView) findViewById(R.id.article_overall_score);
        TextView attr1Score = (TextView) findViewById(R.id.article_attr1_score);
        TextView attr2Score = (TextView) findViewById(R.id.article_attr2_score);
        TextView attr3Score = (TextView) findViewById(R.id.article_attr3_score);

        overallScore.setText(String.valueOf(review.getOverall()));
        attr1Score.setText(String.valueOf(review.getAttr1()/10));
        attr2Score.setText(String.valueOf(review.getAttr2()/10));
        attr3Score.setText(String.valueOf(review.getAttr3()/10));


        //Pics
        pic1 = (ImageView) findViewById(R.id.article_img1);
        pic2 = (ImageView) findViewById(R.id.article_img2);

        if(pic1 != null){
            Bitmap myBitmap = BitmapFactory.decodeFile(review.getPic1());
            pic1.setImageBitmap(myBitmap);
        }

        if(pic2 != null){
            Bitmap myBitmap = BitmapFactory.decodeFile(review.getPic2());
            pic2.setImageBitmap(myBitmap);
        }


        //review text
        TextView title = (TextView) findViewById(R.id.article_title);
        TextView text  = (TextView) findViewById(R.id.article_review);
        TextView stars = (TextView) findViewById(R.id.article_score3);
        TextView votes = (TextView) findViewById(R.id.article_score2);
        TextView likes = (TextView) findViewById(R.id.article_score1);

        title.setText(review.getTitle());
        text.setText(review.getText());
        stars.setText(String.valueOf(review.getScore()));
        votes.setText(String.valueOf(review.getVotes()));
        likes.setText(String.valueOf(review.getLikes()));

        final LinearLayout articleLL = (LinearLayout) findViewById(R.id.article_ll);

        if (articleLL != null) {
            articleLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout articleRCC = (LinearLayout) findViewById(R.id.article_rcc);

                    if (articleRCC.getVisibility() == View.GONE) {
                        articleLL.setBackgroundColor(0xfffef6ec);
                        articleRCC.setVisibility(View.VISIBLE);
                    }
                    else if (articleRCC.getVisibility() == View.VISIBLE) {
                        articleLL.setBackgroundColor(0xfffefaf4);
                        articleRCC.setVisibility(View.GONE);
                    }
                }
            });
        }

        TextView rateArticleTV = (TextView) findViewById(R.id.article_rate);
        if (rateArticleTV != null) {
            rateArticleTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RatingDialog ratingDialog = RatingDialog.newInstance("Rating",reviewID,-1);
                    ratingDialog.show(getFragmentManager(), "Review");
                }
            });
        }

        TextView commentArticleTV = (TextView) findViewById(R.id.article_comment);
        if (commentArticleTV != null) {
            commentArticleTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommentDialog commentDialog = CommentDialog.newInstance("Comment",reviewID,-1);
                    commentDialog.show(getFragmentManager(), "commentdialog");
                }
            });
        }

        TextView copyArticleTV = (TextView) findViewById(R.id.article_copy);
        if (copyArticleTV != null) {
            copyArticleTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ReviewArticleActivity.this , "Article copied", Toast.LENGTH_SHORT).show();
                }
            });
        }


//        final String[] item = {"Ks","Jes","Ah Bee","Gm","Zach","SJ"};
//        List<String> list = new ArrayList<String>(Arrays.asList(item));
//
//        CommentItemAdapter comment = new CommentItemAdapter(this, R.layout.list_item_comment,R.id.comment_user,list);
//        final MyListView listView = (MyListView) findViewById(R.id.comment_listview);
//        listView.setAdapter(comment);

        comments = dbHandler.getAllComment(reviewID);

        for (Comment comment : comments){
            addItem(comment, name + " " + surname);
        }

        mContainerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void addItem(final Comment comment, String name) {
        // Instantiate a new "row" view.
        final ViewGroup newView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.list_item_comment, mContainerView, false);

        final LinearLayout commentLL = (LinearLayout) newView.findViewById(R.id.list_item_comment);
        final LinearLayout commentRCC = (LinearLayout) newView.findViewById(R.id.comment_rcc);
        TextView rate = (TextView) newView.findViewById(R.id.comment_rate);
        TextView reply = (TextView) newView.findViewById(R.id.comment_reply);
        TextView copy = (TextView) newView.findViewById(R.id.comment_copy);
        TextView commentScore1 = (TextView) newView.findViewById(R.id.comment_score1);
        TextView commentScore2 = (TextView) newView.findViewById(R.id.comment_score2);
        TextView commentScore3 = (TextView) newView.findViewById(R.id.comment_score3);
        TextView userName = (TextView) newView.findViewById(R.id.comment_user);
        TextView time = (TextView) newView.findViewById(R.id.comment_time);
        TextView commentTxt = (TextView) newView.findViewById(R.id.comment);

        userName.setText(name);
        commentTxt.setText(comment.getText());
        commentScore1.setText(String.valueOf(comment.getLikes()));
        commentScore2.setText(String.valueOf(comment.getVotes()));
        commentScore3.setText(String.valueOf(comment.getScore()));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String dateTime = sdf.format(new Date());

        int currentYear = Integer.parseInt(dateTime.substring(0,4));
        int currentMonth = Integer.parseInt(dateTime.substring(4,6));
        int currentDay = Integer.parseInt(dateTime.substring(6,8));
        int currentHour = Integer.parseInt(dateTime.substring(9,11));
        int currentMin = Integer.parseInt(dateTime.substring(11,13));

        String cmmtDateTime = comment.getTime();

        int commentYear = Integer.parseInt(cmmtDateTime.substring(0,4));
        int commentMonth = Integer.parseInt(cmmtDateTime.substring(4,6));
        int commentDay = Integer.parseInt(cmmtDateTime.substring(6,8));
        int commentHour = Integer.parseInt(cmmtDateTime.substring(9,11));
        int commentMin = Integer.parseInt(cmmtDateTime.substring(11,13));

        if(currentYear - commentYear == 1){
            time.setText("1 year ago");
        }
        else if (currentYear - commentYear > 1){
            time.setText(String.valueOf(currentYear - commentYear) + " years ago");
        }
        else if (currentMonth - commentMonth == 1){
            time.setText("1 month ago");
        }
        else if (currentMonth - commentMonth > 1){
            time.setText(String.valueOf(currentMonth - commentMonth) + " months ago");
        }
        else if (currentDay - commentDay == 1){
            time.setText("1 day ago");
        }
        else if (currentDay - commentDay > 1){
            time.setText(String.valueOf(currentDay - commentDay) + " days ago");
        }
        else if (currentHour - commentHour == 1){
            time.setText("1 hour ago");
        }
        else if (currentHour - commentHour > 1){
            time.setText(String.valueOf(currentHour - commentHour) + " hours ago");
        }
        else if (currentMin - commentMin == 1){
            time.setText("1 minute ago");
        }
        else if (currentMin - commentMin > 1){
            time.setText(String.valueOf(currentMin - commentMin) + " minutes ago");
        }
        else{
            time.setText("Less than a minute ago");
        }

        commentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentRCC.getVisibility() == View.GONE){
                    commentLL.setBackgroundColor(0xfffaecec);
                    commentRCC.setVisibility(View.VISIBLE);

                }
                else if (commentRCC.getVisibility() == View.VISIBLE){
                    commentLL.setBackgroundColor(0xffffffff);
                    commentRCC.setVisibility(View.GONE);
                }
            }

        });

        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {;
                CommentDialog commentDialog = CommentDialog.newInstance("Reply",reviewID, 1);
                commentDialog.show(getFragmentManager(), "Reply");
                if (intent.getBooleanExtra("new comment", false)){
                    updateComment();
                }

            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(newView.getContext(), "Comment copied", Toast.LENGTH_SHORT).show();
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = newView.getContext();
                FragmentManager fm = ((Activity) context).getFragmentManager();
                RatingDialog ratingDialog = RatingDialog.newInstance("Rating", reviewID, comment.getCommentID());
                ratingDialog.show(getFragmentManager(), "Comment");
            }
        });

        // Because mContainerView has android:animateLayoutChanges set to true,
        // adding this view is automatically animated.
        mContainerView.addView(newView, 0);
    }

    private void updateComment() {
        comments = dbHandler.getAllComment(reviewID);
        addItem(comments.get(comments.size()-1), name + " " + surname);
    }

}