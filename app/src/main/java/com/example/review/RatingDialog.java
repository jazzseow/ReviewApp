package com.example.review;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

/**
 * Created by jesmond on 28/9/2016.
 */
public class RatingDialog extends DialogFragment {

    private ReviewDBHandler dbHandler = null;

    private SharedPreferences pref;
    private int reviewID = -1;
    private int commentID = -1;

    private int isVoted = 0;
    private int isLiked = 0;
    private double rating = 0.0;

    private Review review;
    private Comment comment;
    private String tag;

    public static RatingDialog newInstance(String title, int rid, int cid) {
        RatingDialog frag = new RatingDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("rid", rid);
        args.putInt("cid", cid);
        frag.setArguments(args);
        return frag;
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_rate, null);

        dbHandler = new ReviewDBHandler(getActivity());

        pref = getActivity().getSharedPreferences("review", Context.MODE_PRIVATE);
        reviewID = getArguments().getInt("rid");
        commentID = getArguments().getInt("cid");

        final ImageView likes = (ImageView) view.findViewById(R.id.rating_likes);
        final ImageView dislikes = (ImageView) view.findViewById(R.id.rating_dislikes);

        final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rating_score);

        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    v.setSelected(false);
                    isLiked = 0;
                } else {
                    dislikes.setSelected(false);
                    v.setSelected(true);
                    isLiked = 1;
                }
            }
        });

        dislikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    v.setSelected(false);
                    isLiked = 0;
                } else {
                    likes.setSelected(false);
                    v.setSelected(true);
                    isLiked = -1;
                }
            }
        });

        ImageView votes = (ImageView) view.findViewById(R.id.rating_vote);
        votes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    v.setSelected(false);
                    isVoted = 0;
                } else {
                    v.setSelected(true);
                    isVoted = 1;
                }
            }
        });

        Button doneBtn = (Button) view.findViewById(R.id.rate_dialog_done);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tag == "Review") {
                    rating = (double) ratingBar.getRating();
                    review = dbHandler.getReview(reviewID);
                    if (review != null) {
                        review.setScore(review.getScore() + rating);
                        review.setLikes(review.getLikes() + isLiked);
                        review.setVotes(review.getVotes() + isVoted);
                        dbHandler.updateReview(review);
                        dismiss();
                        Intent intent = new Intent(getActivity(), ReviewArticleActivity.class);
                        intent.putExtra("reviewID", reviewID);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), String.format("Review is null, id is: %d", reviewID), Toast.LENGTH_LONG).show();
                        dismiss();
                    }
                }
                else if (tag == "Comment"){
                    rating = (double) ratingBar.getRating();
                    comment = dbHandler.getComment(commentID);
                    if (comment != null) {
                        comment.setScore(comment.getScore() + rating);
                        comment.setLikes(comment.getLikes() + isLiked);
                        comment.setVotes(comment.getVotes() + isVoted);
                        dbHandler.updateComment(comment);
                        dismiss();
                        Intent intent = new Intent(getActivity(), ReviewArticleActivity.class);
                        intent.putExtra("reviewID", reviewID);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), String.format("Comment is null, id is: %d", commentID), Toast.LENGTH_LONG).show();
                        dismiss();
                    }
                }
            }
        });

        builder.setView(view);

        Dialog dialog = builder.create();
        return dialog;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        this.tag = tag;
        super.show(manager, tag);
    }
}
