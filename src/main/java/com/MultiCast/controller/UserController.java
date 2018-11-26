package com.MultiCast.controller;

import com.MultiCast.model.User;
import com.MultiCast.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView Register(Model model, HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
//        userService.creat(name, password);
        httpSession.setAttribute("username", username);
        return new ModelAndView("redirect:/index");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView Login(Model model, HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        String username = request.getParameter("username");
        User user = userService.getUserByUserName(username);

        if (request.getParameter("password").equals(user.getPassword())) {
            httpSession.setAttribute("username", username);
            return new ModelAndView("redirect:/index");
        } else {
            return new ModelAndView("redirect:/passwordWrong");
        }
    }


}
