package com.mmaynard.controller;

import com.mmaynard.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
        String html = mainService.test();
        model.addAttribute("test", html );
        return "index";
    }
}
