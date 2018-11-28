package com.MultiCast.controller;

import com.MultiCast.model.User;
import com.MultiCast.service.UserService;
import com.MultiCast.util.MultiCast_Client;
import com.MultiCast.util.MultipartFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    MultiCast_Client m;

    @RequestMapping("/home")
    public String home(){
        return "home";
    }

    @RequestMapping("/manager")
    public String manager(){
        return "manager";
    }

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String welcome(){
        return "login";
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public String register(){
        return "register";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().removeAttribute("username");
        return "redirect:/";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView Register(Model model, HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        userService.addUser(username, password);
        httpSession.setAttribute("username", username);
        return new ModelAndView("redirect:/home");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void Login(Model model, HttpServletRequest request,HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("username:"+username);
        System.out.println("password:"+password);

        User user = userService.getUserByUserName(username);

        int flag = 0 ;//默认0不成功，1管理员，2用户
        if (user!=null && password.equals(user.getPassword())){
            session.setAttribute("username",user.getUsername());
            if (user.getAuthority() == 1){
                flag = 1;
                System.out.println("管理员");
            }else if(user.getAuthority() == 0) {
                flag = 2;
                System.out.println("用户");
            }
            else {
                flag = 0;
                System.out.println("有问题");
            }
        }else {
            flag = 0;
            System.out.println("密码错误");
        }
        System.out.println("flag is : "+flag);
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(flag);//返回登录信息
        out.flush();
        out.close();
    }




    @RequestMapping("/receive")
    public String receive(){
        m = MultiCast_Client.getInstance();
        m.init();
        m.receive();
        return "redirect:/home";
    }

    @RequestMapping("/stop")
    public String stop(){
        m = MultiCast_Client.getInstance();
        m.stop();
        return "redirect:/home";
    }


}
