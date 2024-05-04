package com.iishanto.springforwardproxy.controller;

import com.iishanto.springforwardproxy.services.proxyservice.ProxyService.ProxyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@AllArgsConstructor
@Controller
public class HomeController {
    ProxyService proxyService;

    @GetMapping("/**")
    public String home(Model model, HttpServletRequest request){
        String url=request.getRequestURL().toString();
        String []parts=url.split("/",4);
        url=parts[3];
        System.out.println(url);
        String html=proxyService.getModifiedHtmlSource(url);
        model.addAttribute("html",html);
        return "home";
    }
}
