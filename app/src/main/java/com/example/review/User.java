package com.example.review;

/**
 * Created by jesmond on 29/9/2016.
 */
public class User {

    private int userID, nReview, nComment;
    private String name, doj, pic;

    public User() {
    }

    public User(int userID, int nReview, int nComment,
                String name, String doj, String pic) {
        this.userID = userID;
        this.nReview = nReview;
        this.nComment = nComment;
        this.name = name;
        this.doj = doj;
        this.pic = pic;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getnReview() {
        return nReview;
    }

    public void setnReview(int nReview) {
        this.nReview = nReview;
    }

    public int getnComment() {
        return nComment;
    }

    public void setnComment(int nComment) {
        this.nComment = nComment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
