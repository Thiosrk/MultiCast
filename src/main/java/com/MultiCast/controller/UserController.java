package com.MultiCast.controller;

import com.MultiCast.model.Status;
import com.MultiCast.model.User;
import com.MultiCast.service.StatusService;
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

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    StatusService statusService;

    MultiCast_Client m;

    HashMap<Long,String> map;

    int broadcastflag = 0;

    @RequestMapping("/home")
    public String home(){
        return "home";
    }

    @RequestMapping("/manager")
    public String manager(Model model){
        int status = statusService.getStatusByHostname("server").getStatus();
        model.addAttribute("status",status);
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
        request.getSession().removeAttribute("word");
        request.getSession().removeAttribute("key");
        request.getSession().removeAttribute("question");
        request.getSession().removeAttribute("answer");
        return "redirect:/";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView Register(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        userService.addUser(username, password);
        httpSession.setAttribute("username", username);
        return new ModelAndView("redirect:/home");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void Login(HttpServletRequest request,HttpServletResponse response) throws IOException {
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
        broadcastflag= 1;
        return "redirect:/home";
    }

    @RequestMapping("/next")
    public String next(HttpServletRequest request){
        HttpSession session = request.getSession();
        m = MultiCast_Client.getInstance();
        map = m.getWordsMap();
        String word;
        if (statusService.getStatusByHostname("server").getStatus()==0){
            return "noserver";
        }
        if(broadcastflag == 0){
            return "nostart";
        }
        long key;
        if (session.getAttribute("word") == null){
            Iterator iterator = map.entrySet().iterator();
            Map.Entry entry = (Map.Entry) iterator.next();
            key = (long) entry.getKey();
            word = map.get(key);
        }else {
            key = (long)session.getAttribute("key");
            key++;
            word = map.get(key);
        }

        System.out.println("get next word :"+word);
        session.setAttribute("key",key);
        session.setAttribute("word",word);
        return "redirect:/home";
    }

    @RequestMapping("/answer")
    public String answer(HttpServletRequest request){
        HttpSession session = request.getSession();
        m = MultiCast_Client.getInstance();
        map = m.getWordsMap();
        if (broadcastflag == 0 ){
            return "nostart";
        }
        List<Long> keys = new ArrayList<>(map.keySet());
        String word;
        long key = keys.get(new Random().nextInt(keys.size()-1));
        word = map.get(key);
        String[] words = word.split(" ");
        String eng = words[0];
        String ch = words[1];
        System.out.println(eng+" "+ch);
        int flag = new Random().nextInt(2);
        if (flag == 1){
            session.setAttribute("question",eng);
            session.setAttribute("answer",ch);
        }else {
            session.setAttribute("question",ch);
            session.setAttribute("answer",eng);
        }
        return "redirect:/home";
    }

    @RequestMapping("/stop")
    public String stop(){
        m = MultiCast_Client.getInstance();
        broadcastflag = m.stop();
        if (broadcastflag==0){
            return "nostart";
        }
        return "redirect:/home";
    }


}
