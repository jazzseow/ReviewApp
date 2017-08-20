package com.example.review;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jesmond on 21/9/2016.
 */
public class CommentDialog extends DialogFragment {

    private Boolean newComment = false;
    private String commentText;

    public static CommentDialog newInstance(String title, int rid, int cid) {
        CommentDialog frag = new CommentDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("rid", rid);
        args.putInt("cid", cid);
        frag.setArguments(args);
        return frag;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        final int rid = getArguments().getInt("rid");
        final int cid = getArguments().getInt("cid");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_comment, null);

        TextView titleName = (TextView) view.findViewById(R.id.comment_dialog_title);
        titleName.setText(title);
        final EditText commentET = (EditText) view.findViewById(R.id.comment_dialog_edittext);

        final Button doneBtn = (Button) view.findViewById(R.id.comment_dialog_done);

        final Intent intent = new Intent(getActivity(), ReviewArticleActivity.class);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentText = commentET.getText().toString();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String dateTime = sdf.format(new Date());

                Comment comment = new Comment(0, 0, 0, 0, dateTime, rid, cid, 0, commentText);

                ReviewDBHandler dbHandler = new ReviewDBHandler(getActivity());
                dbHandler.addComment(comment);
                newComment = true;

                intent.putExtra("reviewID", rid);
                startActivity(intent);
                dismiss();
            }
        });

        commentET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    doneBtn.setTextColor(Color.parseColor("#A6A6A6"));
                    doneBtn.setEnabled(false);
                } else {
                    doneBtn.setTextColor(Color.parseColor("#D26464"));
                    doneBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        intent.putExtra("new comment", newComment);

        builder.setView(view);

        Dialog dialog = builder.create();
        return dialog;
    }
}