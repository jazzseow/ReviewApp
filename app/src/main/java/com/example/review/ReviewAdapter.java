package com.example.review;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jesmond on 30/9/2016.
 */
public class ReviewAdapter extends ArrayAdapter<Review> {

    private String name;
    private String photoPathOrLink;
    private boolean signedInFB;
    private boolean locallyChangedPhoto;

    public ReviewAdapter(Context context, int textViewResourceId, String name, String photoPathOrLink, boolean signedInFB, boolean locallyChangedPhoto) {
        super(context, textViewResourceId);
        this.name = name;
        this.photoPathOrLink = photoPathOrLink;
        this.signedInFB = signedInFB;
        this.locallyChangedPhoto = locallyChangedPhoto;
    }

    public ReviewAdapter(Context context, int resource, List<Review> items, String name, String photoPathOrLink, boolean signedInFB, boolean locallyChangedPhoto) {
        super(context, resource, items);
        this.name = name;
        this.photoPathOrLink = photoPathOrLink;
        this.signedInFB = signedInFB;
        this.locallyChangedPhoto = locallyChangedPhoto;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item_review, parent, false);
        }

        Review p = getItem(position);
        ReviewDBHandler dbHandler = new ReviewDBHandler(getContext());

        if (p != null) {
            ImageView reviewPic = (ImageView) v.findViewById(R.id.review_pic);
            ImageView profilePic = (ImageView) v.findViewById(R.id.profile_image);
            TextView reviewTitle = (TextView) v.findViewById(R.id.review_title);
            TextView reviewDes = (TextView) v.findViewById(R.id.review_des);
            TextView profileName = (TextView) v.findViewById(R.id.profile_name);
            TextView score = (TextView) v.findViewById(R.id.card_review_score);

            reviewTitle.setText(p.getTitle());
            reviewDes.setText(p.getText());
            reviewTitle.setText(p.getTitle());
            score.setText(String.valueOf(p.getOverall()));

            profileName.setText(name);

            if (!locallyChangedPhoto) {
                if (signedInFB)
                    new DownloadImage(profilePic).execute(photoPathOrLink);
                else
                    profilePic.setImageResource(R.drawable.com_facebook_profile_picture_blank_portrait);
            } else {
                Bitmap bitmap = BitmapFactory.decodeFile(photoPathOrLink);
                profilePic.setImageBitmap(bitmap);
            }

            Bitmap myBitmap = BitmapFactory.decodeFile(p.getPic1());
            reviewPic.setImageBitmap(myBitmap);

        }

        return v;
    }
}