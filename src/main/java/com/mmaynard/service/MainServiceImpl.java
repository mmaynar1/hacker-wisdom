package com.mmaynard.service;

import com.mmaynard.domain.AnsweredQuestion;
import com.mmaynard.domain.Comment;
import com.mmaynard.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


    private static final String API_URL = "https://hacker-news.firebaseio.com/v0/";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String test()
    {
        List<Integer> questions = restTemplate.getForObject(API_URL +"askstories.json?print=pretty", List.class);

        List<Post> posts = new ArrayList<>();
        for( Integer question : questions )
        {
            Post post = restTemplate.getForObject(API_URL + "item/"+ question +".json?print=pretty", Post.class);
            posts.add(post);
        }

        List<AnsweredQuestion> answeredQuestions = new ArrayList<>();
        for( Post post : posts )
        {
            AnsweredQuestion answeredQuestion = new AnsweredQuestion();
            answeredQuestion.setId(post.getId());
            answeredQuestion.setTitle(post.getTitle().replace("Ask HN:", ""));
            answeredQuestion.setLinks(new ArrayList<>());

            if( post.getKids() != null )
            {
                for (Integer kid : post.getKids())
                {
                    Comment comment = restTemplate.getForObject(API_URL + "item/" + kid + ".json?print=pretty", Comment.class);
                    List<String> links = findLinks( comment.getText() );
                    answeredQuestion.getLinks().addAll(links);
                }
            }

            answeredQuestions.add(answeredQuestion);
        }


        String html = "";
        for( AnsweredQuestion a : answeredQuestions )
        {
            if( a.getLinks().size() > 0 )
            {
                html += a.getTitle() + "<br>" + a.getLinks() + "<br><br>";
            }
        }
        return html;
    }

    private List<String> findLinks(String text)
    {
        if( text == null )
        {
            return new ArrayList<>();
        }

        List<String> links = new ArrayList<>();
        Pattern pattern = Pattern.compile("(<a href.+</a>)");
        Matcher matcher = pattern.matcher(text);
        if( matcher.find() )
        {
            for (int i = 0; i < matcher.groupCount(); i++)
            {
                links.add(matcher.group(i));
            }
        }
        return links;
    }
}
