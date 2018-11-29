package com.MultiCast.controller;

import com.MultiCast.util.MultiCast_Server;
import com.MultiCast.util.MultipartFileUtil;
import com.MultiCast.util.WebSocketServer;
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

    @RequestMapping(value = "/broadcast",method = RequestMethod.GET)
    public String welcome(){
        return "login";
    }

    @RequestMapping(value = "/start",method = RequestMethod.GET)
    public String start(HttpServletRequest request){
        HttpSession session = request.getSession();
        String filecontent = (String) session.getAttribute("filecontent");
        System.out.println("startserver!");
        System.out.println(filecontent);
        m = MultiCast_Server.getInstance();
        m.init();
        m.send(filecontent);
        return "redirect:/manager";
    }

    @RequestMapping("/uploadfile")
    public String insert(HttpServletRequest request, HttpServletResponse response
            , @RequestParam("file") MultipartFile[] files) throws Exception{

        HttpSession session = request.getSession();

//        MultipartFileUtil.empty();
//        MultipartFileUtil.toFiles(files);
//        File file = MultipartFileUtil.getFileList().get(0);
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
        m = MultiCast_Server.getInstance();
        m.stop();
        return "redirect:/manager";
    }



}
