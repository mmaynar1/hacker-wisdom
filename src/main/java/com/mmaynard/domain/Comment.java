package com.mmaynard.domain;

import java.util.List;

public class Comment
{
    private String id;
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
