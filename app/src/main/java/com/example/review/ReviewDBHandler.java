package com.example.review;

/**
 * Created by jesmond on 28/9/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class ReviewDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "reviewDB";

    public static final String TABLE_REVIEW = "review";
    public static final String REVIEW_ID = "id";
    public static final String REVIEW_TITLE = "title";
    public static final String REVIEW_CAT = "cat";
    public static final String REVIEW_OVR = "overall";
    public static final String REVIEW_ATTR1 = "attr1";
    public static final String REVIEW_ATTR2 = "attr2";
    public static final String REVIEW_ATTR3 = "attr3";
    public static final String REVIEW_TEXT= "reviewText";
    public static final String REVIEW_USER_ID = "userId";
    public static final String REVIEW_LIKES = "Likes";
    public static final String REVIEW_SCORE = "Score";
    public static final String REVIEW_VOTES = "Votes";
    public static final String REVIEW_PIC1 = "pic1";
    public static final String REVIEW_PIC2 = "pic2";
    public static final String REVIEW_VIEWS = "views";

    public static final String TABLE_COMMENT = "comment";
    public static final String COMMENT_ID = "commentId";
    public static final String COMMENT_USERID = "userId";
    public static final String COMMENT_LIKES = "likes";
    public static final String COMMENT_SCORE = "score";
    public static final String COMMENT_VOTES = "votes";
    public static final String COMMENT_TIME = "time";
    public static final String COMMENT_TEXT = "text";
    public static final String COMMENT_RID = "rId";
    public static final String COMMENT_CID = "cId";

    public static final String TABLE_CAT = "category";
    public static final String CAT_NAME = "catName";
    public static final String ATTR1_NAME = "attr1Name";
    public static final String ATTR2_NAME = "attr2Name";
    public static final String ATTR3_NAME = "attr3Name";

    public static final String TABLE_USER = "user";
    public static final String USER_ID = "userId";
    public static final String USER_NAME = "name";
    public static final String USER_NREVIEW = "nReview";
    public static final String USER_NCOMMENT = "nComment";
    public static final String USER_DOJ = "doj";
    public static final String USER_PIC = "pic";


    private static final String CREATE_REVIEW_TABLE = "CREATE TABLE " + TABLE_REVIEW + "("
            + REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + REVIEW_TITLE + " TEXT,"
            + REVIEW_CAT + " TEXT,"
            + REVIEW_OVR + " REAL,"
            + REVIEW_ATTR1 + " REAL,"
            + REVIEW_ATTR2 + " REAL,"
            + REVIEW_ATTR3 + " REAL,"
            + REVIEW_TEXT + " TEXT,"
            + REVIEW_USER_ID + " INTEGER,"
            + REVIEW_LIKES + " INTEGER,"
            + REVIEW_SCORE + " REAL,"
            + REVIEW_VOTES + " INTEGER,"
            + REVIEW_PIC1 + " TEXT,"
            + REVIEW_PIC2 + " TEXT,"
            + REVIEW_VIEWS + " INTEGER)";

    private static final String CREATE_COMMENT_TABLE = "CREATE TABLE " + TABLE_COMMENT + "("
            + COMMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COMMENT_USERID + " INTEGER,"
            + COMMENT_LIKES + " INTEGER,"
            + COMMENT_SCORE + " REAL,"
            + COMMENT_VOTES + " INTEGER,"
            + COMMENT_TIME + " TEXT,"
            + COMMENT_TEXT + " TEXT,"
            + COMMENT_RID + " INTEGER NOT NULL,"
            + COMMENT_CID + " INTEGER)";

    private static final String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CAT + "("
            + CAT_NAME + " TEXT PRIMARY KEY,"
            + ATTR1_NAME + " TEXT,"
            + ATTR2_NAME + " TEXT,"
            + ATTR3_NAME + " TEXT)";

    private static final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + USER_NAME + " TEXT,"
            + USER_NREVIEW + " INTEGER,"
            + USER_NCOMMENT + " INTEGER,"
            + USER_DOJ + " TEXT,"
            + USER_PIC + " TEXT)";


    public ReviewDBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_REVIEW_TABLE);
        db.execSQL(CREATE_COMMENT_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_USER_TABLE);

    }

    public void insertCategory() {
        Category category1 = new Category("Phone", "Performance", "Portability", "Battery Life");
        Category category2 = new Category("Laptop", "Performance", "Portability", "Battery Life");
        Category category3 = new Category("Printer", "Print Quality", "Refill Cost", "Price");

        addCategory(category1);
        addCategory(category2);
        addCategory(category3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEW);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        onCreate(db);
    }

    public void addReview(Review review) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(REVIEW_TITLE, review.getTitle());
        values.put(REVIEW_CAT, review.getCategory());
        values.put(REVIEW_OVR, review.getOverall());
        values.put(REVIEW_ATTR1, review.getAttr1());
        values.put(REVIEW_ATTR2, review.getAttr2());
        values.put(REVIEW_ATTR3, review.getAttr3());
        values.put(REVIEW_TEXT, review.getText());
        values.put(REVIEW_USER_ID, review.getUserID());
        values.put(REVIEW_LIKES, review.getLikes());
        values.put(REVIEW_SCORE, review.getScore());
        values.put(REVIEW_VOTES, review.getVotes());
        values.put(REVIEW_PIC1, review.getPic1());
        values.put(REVIEW_PIC2, review.getPic2());
        values.put(REVIEW_VIEWS, review.getViews());

        // Inserting Row
        db.insert(TABLE_REVIEW, null, values);
        db.close();
    }

    public void addComment(Comment comment){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COMMENT_USERID, comment.getUserID());
        values.put(COMMENT_LIKES, comment.getLikes());
        values.put(COMMENT_SCORE, comment.getScore());
        values.put(COMMENT_VOTES, comment.getVotes());
        values.put(COMMENT_TIME, comment.getTime());
        values.put(COMMENT_TEXT, comment.getText());
        values.put(COMMENT_RID, comment.getReviewID());
        values.put(COMMENT_CID, comment.getCommentID2());

        // Inserting Row
        db.insert(TABLE_COMMENT, null, values);
        db.close();
    }

    public void addCategory(Category category){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CAT + " WHERE " + CAT_NAME
                + " = '" + category.getCategoryName() + "'",null);

        if (!cursor.moveToFirst()) {
            values.put(CAT_NAME, category.getCategoryName());
            values.put(ATTR1_NAME, category.getAttr1Name());
            values.put(ATTR2_NAME, category.getAttr2Name());
            values.put(ATTR3_NAME, category.getAttr3Name());

            // Inserting Row
            db.insert(TABLE_CAT, null, values);
        }
        db.close();
    }

    public void addUser(User user){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(USER_NAME, user.getName());
        values.put(USER_NREVIEW, user.getnReview());
        values.put(USER_NCOMMENT, user.getnComment());
        values.put(USER_DOJ, user.getDoj());
        values.put(USER_PIC, user.getPic());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public Review getReview(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_REVIEW + " WHERE " + REVIEW_ID
                + " = " + id,null);

        if (cursor.moveToFirst()) {
            Review review = new Review(cursor.getInt(cursor.getColumnIndex(REVIEW_ID)),
                    cursor.getString(cursor.getColumnIndex(REVIEW_TITLE)),
                    cursor.getString(cursor.getColumnIndex(REVIEW_CAT)),
                    cursor.getDouble(cursor.getColumnIndex(REVIEW_OVR)),
                    cursor.getDouble(cursor.getColumnIndex(REVIEW_ATTR1)),
                    cursor.getDouble(cursor.getColumnIndex(REVIEW_ATTR2)),
                    cursor.getDouble(cursor.getColumnIndex(REVIEW_ATTR3)),
                    cursor.getString(cursor.getColumnIndex(REVIEW_TEXT)),
                    cursor.getInt(cursor.getColumnIndex(REVIEW_USER_ID)),
                    cursor.getInt(cursor.getColumnIndex(REVIEW_LIKES)),
                    cursor.getDouble(cursor.getColumnIndex(REVIEW_SCORE)),
                    cursor.getInt(cursor.getColumnIndex(REVIEW_VOTES)),
                    cursor.getString(cursor.getColumnIndex(REVIEW_PIC1)),
                    cursor.getString(cursor.getColumnIndex(REVIEW_PIC2)),
                    cursor.getInt(cursor.getColumnIndex(REVIEW_VIEWS)));

            return review;
        }
        else{
            return null;
        }

    }

    public Comment getComment(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COMMENT + " WHERE " + COMMENT_ID
                + " = " + id,null);

        if(cursor.moveToFirst()){
            Comment comment = new Comment( cursor.getInt(cursor.getColumnIndex(COMMENT_ID)),
                cursor.getInt(cursor.getColumnIndex(COMMENT_USERID)),
                cursor.getInt(cursor.getColumnIndex(COMMENT_LIKES)),
                cursor.getInt(cursor.getColumnIndex(COMMENT_VOTES)),
                cursor.getString(cursor.getColumnIndex(COMMENT_TIME)),
                cursor.getInt(cursor.getColumnIndex(COMMENT_RID)),
                cursor.getInt(cursor.getColumnIndex(COMMENT_CID)),
                cursor.getDouble(cursor.getColumnIndex(COMMENT_SCORE)),
                cursor.getString(cursor.getColumnIndex(COMMENT_TEXT)));
            return comment;
        }
        else{

            return null;
        }
    }

    public Category getCategory(String catName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CAT + " WHERE " + CAT_NAME
                + " = '" + catName + "'",null);

        if (cursor.moveToFirst()) {
            Category category = new Category(cursor.getString(cursor.getColumnIndex(CAT_NAME)),
                    cursor.getString(cursor.getColumnIndex(ATTR1_NAME)),
                    cursor.getString(cursor.getColumnIndex(ATTR2_NAME)),
                    cursor.getString(cursor.getColumnIndex(ATTR3_NAME)));

            return category;
        }
        else{
            return null;
        }
    }

    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + USER_ID
                + " = " + id + "", null);

        User user = new User( cursor.getInt(cursor.getColumnIndex(USER_ID)),
                cursor.getInt(cursor.getColumnIndex(USER_NREVIEW)),
                cursor.getInt(cursor.getColumnIndex(USER_NCOMMENT)),
                cursor.getString(cursor.getColumnIndex(USER_NAME)),
                cursor.getString(cursor.getColumnIndex(USER_DOJ)),
                cursor.getString(cursor.getColumnIndex(USER_PIC)));

        return user;
    }

    public List<Review> getAllReview() {
        List<Review> reviewsList = new ArrayList<Review>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REVIEW + " ORDER BY " + REVIEW_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Review review = new Review( cursor.getInt(cursor.getColumnIndex(REVIEW_ID)),
                        cursor.getString(cursor.getColumnIndex(REVIEW_TITLE)),
                        cursor.getString(cursor.getColumnIndex(REVIEW_CAT)),
                        cursor.getDouble(cursor.getColumnIndex(REVIEW_OVR)),
                        cursor.getDouble(cursor.getColumnIndex(REVIEW_ATTR1)),
                        cursor.getDouble(cursor.getColumnIndex(REVIEW_ATTR2)),
                        cursor.getDouble(cursor.getColumnIndex(REVIEW_ATTR3)),
                        cursor.getString(cursor.getColumnIndex(REVIEW_TEXT)),
                        cursor.getInt(cursor.getColumnIndex(REVIEW_USER_ID)),
                        cursor.getInt(cursor.getColumnIndex(REVIEW_LIKES)),
                        cursor.getDouble(cursor.getColumnIndex(REVIEW_SCORE)),
                        cursor.getInt(cursor.getColumnIndex(REVIEW_VOTES)),
                        cursor.getString(cursor.getColumnIndex(REVIEW_PIC1)),
                        cursor.getString(cursor.getColumnIndex(REVIEW_PIC2)),
                        cursor.getInt(cursor.getColumnIndex(REVIEW_VIEWS)));
                reviewsList.add(review);
            } while (cursor.moveToNext());
        }

        return reviewsList;

    }

    public List<Comment> getAllComment(int reviewID) {
        List<Comment> commentsList = new ArrayList<Comment>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_COMMENT + " WHERE " + COMMENT_RID +
                " = " + reviewID + " ORDER BY " + COMMENT_ID + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Comment comment = new Comment( cursor.getInt(cursor.getColumnIndex(COMMENT_ID)),
                        cursor.getInt(cursor.getColumnIndex(COMMENT_USERID)),
                        cursor.getInt(cursor.getColumnIndex(COMMENT_LIKES)),
                        cursor.getInt(cursor.getColumnIndex(COMMENT_VOTES)),
                        cursor.getString(cursor.getColumnIndex(COMMENT_TIME)),
                        cursor.getInt(cursor.getColumnIndex(COMMENT_RID)),
                        cursor.getInt(cursor.getColumnIndex(COMMENT_CID)),
                        cursor.getDouble(cursor.getColumnIndex(COMMENT_SCORE)),
                        cursor.getString(cursor.getColumnIndex(COMMENT_TEXT)));
                commentsList.add(comment);
            } while (cursor.moveToNext());
        }

        return commentsList;

    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<Category>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CAT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category(cursor.getString(cursor.getColumnIndex(CAT_NAME)),
                        cursor.getString(cursor.getColumnIndex(ATTR1_NAME)),
                        cursor.getString(cursor.getColumnIndex(ATTR2_NAME)),
                        cursor.getString(cursor.getColumnIndex(ATTR3_NAME)));
                categoryList.add(category);
            } while (cursor.moveToNext());
        }

        return categoryList;
    }

    public List<Review> getFilteredReviews(String sortBy, String category,
                                           int attr1, double attr1min, double attr1max,
                                           int attr2, double attr2min, double attr2max,
                                           int attr3, double attr3min, double attr3max){

        List<Review> reviewsList = new ArrayList<Review>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_REVIEW + "";

        if (category != null && category != "Any") {
            selectQuery = selectQuery + " WHERE cat = '" + category + "'";
            if (attr1 != -1) {
                selectQuery = selectQuery + " AND attr" + String.valueOf(attr1) + " >= " + (attr1min*10) + " AND attr" + String.valueOf(attr1) + " <= " + (attr1max*10);
            }
            if (attr2 != -1) {
                selectQuery = selectQuery + " AND attr" + String.valueOf(attr2) + " >= " + (attr2min*10) + " AND attr" + String.valueOf(attr2) + " <= " + (attr2max*10);
            }
            if (attr3 != -1) {
                selectQuery = selectQuery + " AND attr" + String.valueOf(attr3) + " >= " + (attr3min*10) + " AND attr" + String.valueOf(attr3) + " <= " + (attr3max*10);
            }
        }

        if (sortBy != null){
            if (sortBy == "Most recent"){
                selectQuery = selectQuery + " ORDER BY " + REVIEW_ID + " DESC";
            }
            else{
                selectQuery = selectQuery + " ORDER BY " + sortBy + " DESC";
            }
        }

        Log.i("1", "getFilteredReviews: "+selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Review review = new Review( cursor.getInt(cursor.getColumnIndex(REVIEW_ID)),
                        cursor.getString(cursor.getColumnIndex(REVIEW_TITLE)),
                        cursor.getString(cursor.getColumnIndex(REVIEW_CAT)),
                        cursor.getDouble(cursor.getColumnIndex(REVIEW_OVR)),
                        cursor.getDouble(cursor.getColumnIndex(REVIEW_ATTR1)),
                        cursor.getDouble(cursor.getColumnIndex(REVIEW_ATTR2)),
                        cursor.getDouble(cursor.getColumnIndex(REVIEW_ATTR3)),
                        cursor.getString(cursor.getColumnIndex(REVIEW_TEXT)),
                        cursor.getInt(cursor.getColumnIndex(REVIEW_USER_ID)),
                        cursor.getInt(cursor.getColumnIndex(REVIEW_LIKES)),
                        cursor.getDouble(cursor.getColumnIndex(REVIEW_SCORE)),
                        cursor.getInt(cursor.getColumnIndex(REVIEW_VOTES)),
                        cursor.getString(cursor.getColumnIndex(REVIEW_PIC1)),
                        cursor.getString(cursor.getColumnIndex(REVIEW_PIC2)),
                        cursor.getInt(cursor.getColumnIndex(REVIEW_VIEWS)));
                reviewsList.add(review);
            } while (cursor.moveToNext());
        }

        return reviewsList;
    }

    public List<Review> searchReview(String name){
        List<Review> reviewsList = new ArrayList<Review>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_REVIEW + " WHERE title = '" + name + "'", null);

        Log.i("search", "searchReview: "+"SELECT * FROM " + TABLE_REVIEW + " WHERE title = '" + name + "'");
        if (cursor.moveToFirst()) {
            do {
                Review review = new Review( cursor.getInt(cursor.getColumnIndex(REVIEW_ID)),
                        cursor.getString(cursor.getColumnIndex(REVIEW_TITLE)),
                        cursor.getString(cursor.getColumnIndex(REVIEW_CAT)),
                        cursor.getDouble(cursor.getColumnIndex(REVIEW_OVR)),
                        cursor.getDouble(cursor.getColumnIndex(REVIEW_ATTR1)),
                        cursor.getDouble(cursor.getColumnIndex(REVIEW_ATTR2)),
                        cursor.getDouble(cursor.getColumnIndex(REVIEW_ATTR3)),
                        cursor.getString(cursor.getColumnIndex(REVIEW_TEXT)),
                        cursor.getInt(cursor.getColumnIndex(REVIEW_USER_ID)),
                        cursor.getInt(cursor.getColumnIndex(REVIEW_LIKES)),
                        cursor.getDouble(cursor.getColumnIndex(REVIEW_SCORE)),
                        cursor.getInt(cursor.getColumnIndex(REVIEW_VOTES)),
                        cursor.getString(cursor.getColumnIndex(REVIEW_PIC1)),
                        cursor.getString(cursor.getColumnIndex(REVIEW_PIC2)),
                        cursor.getInt(cursor.getColumnIndex(REVIEW_VIEWS)));
                reviewsList.add(review);
            } while (cursor.moveToNext());
        }

        return reviewsList;
    }
    public int getNReview(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(id) FROM " + TABLE_REVIEW, null);

        if(cursor.moveToFirst()){
            return cursor.getInt(0);
        }

        return 0;
    }

    public int getNComment(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(commentId) FROM " + TABLE_COMMENT, null);

        if(cursor.moveToFirst()){
            return cursor.getInt(0);
        }

        return 0;
    }


    public void updateReview(Review review) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(REVIEW_ID, review.getReviewID());
        values.put(REVIEW_TITLE, review.getTitle());
        values.put(REVIEW_CAT, review.getCategory());
        values.put(REVIEW_OVR, review.getOverall());
        values.put(REVIEW_ATTR1, review.getAttr1());
        values.put(REVIEW_ATTR2, review.getAttr2());
        values.put(REVIEW_ATTR3, review.getAttr3());
        values.put(REVIEW_TEXT, review.getText());
        values.put(REVIEW_USER_ID, review.getUserID());
        values.put(REVIEW_LIKES, review.getLikes());
        Cursor scoreCursor = db.rawQuery("SELECT " + REVIEW_SCORE + " FROM " + TABLE_REVIEW + " WHERE id = " + review.getReviewID(), null);
        if (scoreCursor.moveToFirst() && scoreCursor.getDouble(0) != 0.0) {
            double averageScore = (scoreCursor.getDouble(0) + review.getScore() / 2.0);
            values.put(REVIEW_SCORE, averageScore);
        }
        else{
            values.put(REVIEW_SCORE, review.getScore());
        }
        values.put(REVIEW_VOTES, review.getVotes());
        values.put(REVIEW_PIC1, review.getPic1());
        values.put(REVIEW_PIC2, review.getPic2());
        values.put(REVIEW_VIEWS, review.getViews());

        // Inserting Row
        db.update(TABLE_REVIEW, values, "id = ? ", new String[] { Integer.toString(review.getReviewID()) } );
        db.close();
    }

    public void updateComment(Comment comment){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COMMENT_ID, comment.getCommentID());
        values.put(COMMENT_USERID, comment.getUserID());
        values.put(COMMENT_LIKES, comment.getLikes());
        Cursor scoreCursor = db.rawQuery("SELECT " + COMMENT_SCORE + " FROM " + TABLE_COMMENT + " WHERE commentId = " + comment.getCommentID(), null);
        if (scoreCursor.moveToFirst() && scoreCursor.getDouble(0) != 0.0) {
            double averageScore = (scoreCursor.getDouble(0) + comment.getScore() / 2.0);
            values.put(COMMENT_SCORE, averageScore);
        }
        else{
            values.put(COMMENT_SCORE, comment.getScore());
        }
        values.put(COMMENT_SCORE, comment.getScore());
        values.put(COMMENT_VOTES, comment.getVotes());
        values.put(COMMENT_TIME, comment.getTime());
        values.put(COMMENT_TEXT, comment.getText());
        values.put(COMMENT_RID, comment.getReviewID());
        values.put(COMMENT_CID, comment.getCommentID2());

        // Inserting Row
        db.update(TABLE_COMMENT, values, "commentId = ? ", new String[]{Integer.toString(comment.getCommentID())});
        db.close();
    }

    public void updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(USER_ID, user.getUserID());
        values.put(USER_NAME, user.getName());
        values.put(USER_NREVIEW, user.getnReview());
        values.put(USER_NCOMMENT, user.getnComment());
        values.put(USER_DOJ, user.getDoj());
        values.put(USER_PIC, user.getPic());

        // Inserting Row
        db.update(TABLE_USER, values, "id = ? ", new String[] { Integer.toString(user.getUserID()) } );
        db.close();
    }

    public void deleteReview(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REVIEW, REVIEW_ID + " = ?",
                new String[] { Integer.toString(id) });
        db.close();
    }

    public void deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, USER_ID + " = ?",
                new String[] { Integer.toString(id) });
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEW);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        onCreate(db);
        db.close();
    }
}