package com.mmaynard.controller;

import com.mmaynard.domain.AnsweredQuestion;
import com.mmaynard.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class MainController
{
    @Autowired
    private MainService mainService;

    public MainController(MainService mainService)
    {
        this.mainService = mainService;
    }

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String test( Model model )
    {
        List<AnsweredQuestion> answeredQuestions = mainService.getAnsweredQuestions();
        model.addAttribute("answeredQuestions", answeredQuestions );
        return "index";
    }
}
