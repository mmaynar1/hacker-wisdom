package com.mmaynard.service;

import com.mmaynard.domain.AnsweredQuestion;
import org.springframework.cache.annotation.CachePut;

import java.util.List;

public interface MainService
{
    List<AnsweredQuestion> getAnsweredQuestionsCacheable();

    List<AnsweredQuestion> getAnsweredQuestionsCachePut();
}
