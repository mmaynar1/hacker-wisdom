package com.mmaynard.service;

import com.mmaynard.domain.AnsweredQuestion;
import com.mmaynard.domain.Item;
import com.mmaynard.util.RegexPatterns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

@Service
public class MainServiceImpl implements MainService
{
    //todo use same object for all items
    //todo improve regex to parse links better, maybe use jsoup instead
    //todo put restTemplate code in separate service
    //todo name things better
    //todo implement GUI w/ Thymeleaf and maybe Vue.js
    //todo https://stackoverflow.com/questions/54930449/how-do-i-solve-reached-the-maximum-number-of-uri-tags-for-http-client-requests
    //todo caching
    //todo load cache on startup
    @Autowired
    private HackerNewsService hackerNewsService;

    @Override
    @Cacheable("answeredQuestions")
    public List<AnsweredQuestion> getAnsweredQuestionsCacheable()
    {
        return getAnsweredQuestions();
    }

    @Override
    @CachePut("answeredQuestions")
    public List<AnsweredQuestion> getAnsweredQuestionsCachePut()
    {
        return getAnsweredQuestions();
    }

    private List<AnsweredQuestion> getAnsweredQuestions()
    {
        List<Integer> questions = hackerNewsService.getAskHNPosts();
        List<Item> posts = getItems(questions);
        return getAnsweredQuestions(posts);
    }

    private List<Item> getItems(List<Integer> ids)
    {
        List<Item> posts = new ArrayList<>();
        for( Integer id : ids )
        {
            Item post = hackerNewsService.getItem( id );
            posts.add(post);
        }
        return posts;
    }

    private List<AnsweredQuestion> getAnsweredQuestions(List<Item> posts)
    {
        List<AnsweredQuestion> answeredQuestions = new ArrayList<>();
        for( Item post : posts )
        {
            AnsweredQuestion answeredQuestion = new AnsweredQuestion();
            answeredQuestion.setId(post.getId());
            answeredQuestion.setTitle(post.getTitle().replace("Ask HN:", ""));
            answeredQuestion.setLinks(new ArrayList<>());

            if( post.getKids() != null )
            {
                for (Integer kid : post.getKids())
                {
                    Item comment = hackerNewsService.getItem( kid );
                    List<String> links = findLinks( comment.getText() );
                    answeredQuestion.getLinks().addAll(links);
                }

                if( answeredQuestion.getLinks().size() > 0 )
                {
                    answeredQuestions.add(answeredQuestion);
                }
            }
        }
        return answeredQuestions;
    }

    private List<String> findLinks(String text)
    {
        if( text == null )
        {
            return new ArrayList<>();
        }

        List<String> links = new ArrayList<>();
        Matcher matcher = RegexPatterns.A_HREF.matcher(text);
        if( matcher.find() )
        {
            String link = matcher.group(2);
            link = link.replace("&#x2F;", "/");
            links.add(link);
        }
        return links;
    }
}
