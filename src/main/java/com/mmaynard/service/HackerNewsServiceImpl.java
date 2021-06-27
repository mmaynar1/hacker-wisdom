package com.mmaynard.service;

import com.mmaynard.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HackerNewsServiceImpl implements HackerNewsService
{
    private static final String API_URL = "https://hacker-news.firebaseio.com/v0/";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List getAskHNPosts()
    {
        return restTemplate.getForObject(API_URL + "askstories.json?print=pretty", List.class);
    }

    @Override
    public Item getItem(Integer id)
    {
        Map<String,Object> uriVariables = new HashMap<>();
        uriVariables.put("id", id);
        return restTemplate.getForObject(API_URL + "item/{id}.json?print=pretty", Item.class, uriVariables);
    }
}
