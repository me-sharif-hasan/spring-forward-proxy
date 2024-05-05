package com.iishanto.springforwardproxy.controller;

import com.iishanto.springforwardproxy.services.proxyservice.ProxyService;
import com.iishanto.springforwardproxy.services.urlservice.UrlBuilderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@AllArgsConstructor
@Controller
public class HomeController {
    ProxyService proxyService;
    UrlBuilderService urlBuilderService;
    Logger logger;
    @GetMapping("/**")
    public ResponseEntity<byte[]> home(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String queryParams) throws MalformedURLException, URISyntaxException {
        String url=request.getRequestURL().toString()+(request.getQueryString()!=null?"?"+request.getQueryString():"");
        logger.info("CONTROL: "+url);
        URL requestedUrl=new URI(url).toURL();
        String refererUrl = request.getHeader("referer");
        URL referer=null;
        if(refererUrl!=null){
            referer = new URI(refererUrl).toURL();
        }
        url=urlBuilderService.getConstructedUrl(requestedUrl,referer);

        HttpHeaders requestHeaders=new HttpHeaders();
        request.getHeaderNames().asIterator().forEachRemaining(new Consumer<String>() {
            @Override
            public void accept(String s) {
                if(!s.equals("accept-encoding")) requestHeaders.add(s,request.getHeader(s));
            }
        });
        if(requestHeaders.get("referer")!=null){
            List ref=new ArrayList();
            ref.add(referer.getPath().replaceFirst("/",""));
            requestHeaders.put("referer",ref);
        }
        ProxyService.HttpResponse httpResponse=proxyService.getModifiedHtmlSource(url,requestHeaders);
        MultiValueMap <String,String> header=new HttpHeaders();
        if(httpResponse!=null){
            Map<String, List<String>> headers=httpResponse.getHeaders();
            if(headers!=null) {
                logger.info("setting headers");
                headers.forEach((s, strings) -> {
                    logger.info(s+" : "+strings.getFirst());
                    response.setHeader(s,strings.getFirst());
                    header.set(s,strings.getFirst());
                });
                logger.info("Finished");
            }
            logger.info("returning");
            return new ResponseEntity<>(httpResponse.getBody(),header, HttpStatus.OK);
        }
        return new ResponseEntity<>(new byte[10],header,HttpStatus.NOT_FOUND);
    }
}
