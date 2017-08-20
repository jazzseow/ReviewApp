package com.example.review;

/**
 * Created by jesmond on 28/9/2016.
 */
public class Review {

    private int reviewID, userID, likes, votes, views;
    private String title, category, text, pic1, pic2;
    private double attr1, attr2, attr3, score, overall;

    Review(){};

    Review(int reviewID, String title, String category,
           double overall, double attr1, double attr2, double attr3,
           String text, int userID, int likes,
           double score, int votes, String pic1,
           String pic2, int views){
        this.reviewID = reviewID;
        this.title = title;
        this.category = category;
        this.overall = overall;
        this.attr1 = attr1;
        this.attr2 = attr2;
        this.attr3 = attr3;
        this.text = text;
        this.userID = userID;
        this.likes = likes;
        this.score = score;
        this.votes = votes;
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.views = views;
    }

    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public double getAttr1() {
        return attr1;
    }

    public void setAttr1(double attr1) {
        this.attr1 = attr1;
    }

    public double getAttr2() {
        return attr2;
    }

    public void setAttr2(double attr2) {
        this.attr2 = attr2;
    }

    public double getAttr3() {
        return attr3;
    }

    public void setAttr3(double attr3) {
        this.attr3 = attr3;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getOverall() {
        return overall;
    }

    public void setOverall(double overall) {
        this.overall = overall;
    }
}
