package com.example.review;

/**
 * Created by jesmond on 29/9/2016.
 */
public class Comment {

    private int commentID, userID, likes, votes, reviewID, commentID2;
    private double score;
    private String text , time;

    public Comment() {
    }

    public Comment(int commentID, int userID, int likes,
                   int votes, String time, int reviewID,
                   int commentID2, double score, String text) {
        this.commentID = commentID;
        this.userID = userID;
        this.likes = likes;
        this.votes = votes;
        this.time = time;
        this.reviewID = reviewID;
        this.commentID2 = commentID2;
        this.score = score;
        this.text = text;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public int getCommentID2() {
        return commentID2;
    }

    public void setCommentID2(int commentID2) {
        this.commentID2 = commentID2;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
