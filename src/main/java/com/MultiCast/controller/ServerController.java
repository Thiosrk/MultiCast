package com.MultiCast.controller;

import com.MultiCast.util.MultiCast_Server;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ServerController {

    MultiCast_Server m;

    @RequestMapping(value = "/broadcast",method = RequestMethod.GET)
    public String welcome(){
        return "login";
    }

    @RequestMapping(value = "/start",method = RequestMethod.GET)
    public String start(){
        System.out.println("startserver!");
        m = MultiCast_Server.getInstance();
        m.init();
        m.send();
        return "redirect:/manager";
    }

    @RequestMapping(value = "/stop",method = RequestMethod.GET)
    public String stop(){
        m = MultiCast_Server.getInstance();
        m.stop();
        return "redirect:/manager";
    }

}
