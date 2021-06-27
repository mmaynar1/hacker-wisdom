package com.mmaynard.domain;

import java.util.List;

public class Item
{
    private String id;
    private String title;
    private String text;
    private List<Integer> kids;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public List<Integer> getKids()
    {
        return kids;
    }

    public void setKids(List<Integer> kids)
    {
        this.kids = kids;
    }
}
