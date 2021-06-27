package com.mmaynard.service;

import com.mmaynard.domain.AnsweredQuestion;
import java.util.List;

public interface MainService
{
    List<AnsweredQuestion> getAnsweredQuestions();
}
