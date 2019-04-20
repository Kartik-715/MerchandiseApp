package com.example.merchandiseapp;

import java.util.HashMap;

public class Rating
{
    private String Email;
    private String Stars;
    private String Comment;
    private String UID;
    private String PID;
    private String Group;
    private String IsPrivate;
    private String Category;

    public Rating()
    {
    }

    public Rating(String email, String stars, String comment, String UID, String PID, String group, String isPrivate, String category)
    {
        Email = email;
        Stars = stars;
        Comment = comment;
        this.UID = UID;
        this.PID = PID;
        Group = group;
        IsPrivate = isPrivate;
        Category = category;
    }


    public String getEmail()
    {
        return Email;
    }

    public void setEmail(String email)
    {
        Email = email;
    }

    public String getStars()
    {
        return Stars;
    }

    public void setStars(String stars)
    {
        Stars = stars;
    }

    public String getComment()
    {
        return Comment;
    }

    public void setComment(String comment)
    {
        Comment = comment;
    }

    public String getUID()
    {
        return UID;
    }

    public void setUID(String UID)
    {
        this.UID = UID;
    }

    public String getPID()
    {
        return PID;
    }

    public void setPID(String PID)
    {
        this.PID = PID;
    }

    public String getGroup()
    {
        return Group;
    }

    public void setGroup(String group)
    {
        Group = group;
    }

    public String getIsPrivate()
    {
        return IsPrivate;
    }

    public void setIsPrivate(String isPrivate)
    {
        IsPrivate = isPrivate;
    }

    public String getCategory()
    {
        return Category;
    }

    public void setCategory(String category)
    {
        Category = category;
    }
}
