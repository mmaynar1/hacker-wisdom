package com.mmaynard.service;

import com.mmaynard.domain.AnsweredQuestion;
import com.mmaynard.domain.Item;
import com.mmaynard.util.RegexPatterns;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String test()
    {
        List<Integer> questions = hackerNewsService.getAskHNPosts();

        List<Item> posts = new ArrayList<>();
        for( Integer question : questions )
        {
            Item post = hackerNewsService.getItem( question );
            posts.add(post);
        }

        List<AnsweredQuestion> answeredQuestions = getAnsweredQuestions(posts);
        return buildHtml(answeredQuestions);
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

    private String buildHtml(List<AnsweredQuestion> answeredQuestions)
    {
        String html = "";
        for( AnsweredQuestion a : answeredQuestions )
        {
            if( a.getLinks().size() > 0 )
            {
                html += a.getTitle() + "<br>";
                for( String link : a.getLinks())
                {
                    html +=  "<a href=\"" + link + "\">" + link + "</a><br>";
                }
                html += "<br><br>";
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
        Matcher matcher = RegexPatterns.A_HREF.matcher(text);
        if( matcher.find() )
        {
            links.add(matcher.group(2));
        }
        return links;
    }
}
