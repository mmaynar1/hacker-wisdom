package com.mmaynard.service;

import com.mmaynard.domain.Item;

import java.util.List;

public interface HackerNewsService
{
    List getAskHNPosts();

    Item getItem(Integer id);
}
