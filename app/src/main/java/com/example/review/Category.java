package com.example.review;

/**
 * Created by jesmond on 29/9/2016.
 */
public class Category {

    private String categoryName, attr1Name, attr2Name, attr3Name;

    public Category() {
    }

    public Category(String categoryName, String attr1Name, String attr2Name, String attr3Name) {
        this.categoryName = categoryName;
        this.attr1Name = attr1Name;
        this.attr2Name = attr2Name;
        this.attr3Name = attr3Name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getAttr1Name() {
        return attr1Name;
    }

    public void setAttr1Name(String attr1Name) {
        this.attr1Name = attr1Name;
    }

    public String getAttr2Name() {
        return attr2Name;
    }

    public void setAttr2Name(String attr2Name) {
        this.attr2Name = attr2Name;
    }

    public String getAttr3Name() {
        return attr3Name;
    }

    public void setAttr3Name(String attr3Name) {
        this.attr3Name = attr3Name;
    }
}
