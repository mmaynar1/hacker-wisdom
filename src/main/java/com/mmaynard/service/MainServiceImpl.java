package com.mmaynard.service;

import com.mmaynard.domain.AnsweredQuestion;
import com.mmaynard.domain.Item;
import com.mmaynard.util.RegexPatterns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;

@Service
public class MainServiceImpl implements MainService
{
    private static final String CACHE_KEY = "answeredQuestions";

    @Autowired
    private HackerNewsService hackerNewsService;

    public MainServiceImpl(HackerNewsService hackerNewsService)
    {
        this.hackerNewsService = hackerNewsService;
    }

    @Override
    @Cacheable(CACHE_KEY)
    public List<AnsweredQuestion> getAnsweredQuestionsCacheable()
    {
        return getAnsweredQuestions();
    }

    @Override
    @CachePut(CACHE_KEY)
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
                List<String> links = new ArrayList<>();
                findAllLinks(post, links);
                answeredQuestion.getLinks().addAll(links);

                if( answeredQuestion.getLinks() != null && answeredQuestion.getLinks().size() > 0 )
                {
                    answeredQuestions.add(answeredQuestion);
                }
            }
        }

        removeDuplicates(answeredQuestions);
        sortLinks(answeredQuestions);

        return answeredQuestions;
    }

    private void removeDuplicates(List<AnsweredQuestion> answeredQuestions)
    {
        answeredQuestions.forEach(answeredQuestion -> {
            Set<String> uniqueLinks = new HashSet<>(answeredQuestion.getLinks());
            answeredQuestion.setLinks(new ArrayList<>(uniqueLinks));
        });
    }

    private void findAllLinks(Item post, List<String> links )
    {
        if( post != null && post.getKids() != null )
        {
            for (Integer kid : post.getKids())
            {
                Item comment = hackerNewsService.getItem(kid);
                links.addAll(findLinks(comment));
                findAllLinks(comment, links);
            }
        }
    }

    private Comparator <String> linksComparator = (link1, link2) -> {
        String result1 = getComparisonLink(link1);
        String result2 = getComparisonLink(link2);
        return result1.compareToIgnoreCase(result2);
    };

    private String getComparisonLink(String link)
    {
        String result = link;
        if( link.startsWith("https://www."))
        {
            result = link.substring(12);
        }
        else if( link.startsWith("https://"))
        {
            result = link.substring(8);
        }
        else if( link.startsWith("http://www."))
        {
            result = link.substring(11);
        }
        else if( link.startsWith("http://"))
        {
            result = link.substring(7);
        }
        return result;
    }

    private void sortLinks(List<AnsweredQuestion> answeredQuestions)
    {
        answeredQuestions.forEach(answeredQuestion -> answeredQuestion.getLinks().sort(linksComparator));
    }

    private List<String> findLinks(Item item)
    {
        if( item == null || item.getText() == null )
        {
            return new ArrayList<>();
        }

        List<String> links = new ArrayList<>();
        Matcher matcher = RegexPatterns.A_HREF.matcher(item.getText());
        if( matcher.find() )
        {
            String link = matcher.group(2);
            link = link.replace("&#x2F;", "/");
            links.add(link);
        }
        return links;
    }
}
