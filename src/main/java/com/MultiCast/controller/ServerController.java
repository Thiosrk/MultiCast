package com.MultiCast.controller;

import com.MultiCast.model.Status;
import com.MultiCast.service.StatusService;
import com.MultiCast.util.MultiCast_Server;
import com.MultiCast.util.MultipartFileUtil;
import com.MultiCast.util.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

@Controller
public class ServerController {

    MultiCast_Server m;

    @Autowired
    StatusService statusService;

    @RequestMapping(value = "/start",method = RequestMethod.GET)
    public String start(HttpServletRequest request){
        HttpSession session = request.getSession();
        String filecontent = (String) session.getAttribute("filecontent");
        System.out.println("startserver!");
        System.out.println(filecontent);
        if (statusService.getStatusByHostname("server").getStatus()==1){
            return "started";
        }
        if(filecontent == null){
            return "nofile";
        }else {
            m = MultiCast_Server.getInstance();
            m.init();
            Status status = statusService.getStatusByHostname("server");
            status.setStatus(1);
            statusService.updateStatus(status);
            m.send(filecontent);
            return "redirect:/manager";
        }

    }

    @RequestMapping("/uploadfile")
    public String insert(HttpServletRequest request, HttpServletResponse response
            , @RequestParam("file") MultipartFile[] files) throws Exception{

        HttpSession session = request.getSession();
        MultipartFile multipartFiles = files[0];
        InputStreamReader inputStreamReader = new InputStreamReader(multipartFiles.getInputStream(),"UTF-8");
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String ans = "";
        try{
            String tmpString = null;
            //一行一行的读取文件里面的内容
            while((tmpString = reader.readLine()) != null){
                System.out.println(tmpString);
                ans += tmpString + "\n";//保存在ans里面
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(reader != null) {
                try{
                    reader.close();
                }catch(IOException e1){
                    e1.printStackTrace();
                }
            }
        }

        session.setAttribute("filecontent", ans);

        return "redirect:/manager";
    }

    @RequestMapping(value = "/close",method = RequestMethod.GET)
    public String stop(){
        Status status = statusService.getStatusByHostname("server");
        if (status.getStatus()==0){
            return "noserver1";
        }
        m = MultiCast_Server.getInstance();
        m.stop();
        status.setStatus(0);
        statusService.updateStatus(status);
        return "redirect:/manager";
    }



}
