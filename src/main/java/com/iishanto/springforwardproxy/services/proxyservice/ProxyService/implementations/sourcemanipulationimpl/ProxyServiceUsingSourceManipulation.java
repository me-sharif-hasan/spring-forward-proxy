package com.iishanto.springforwardproxy.services.proxyservice.ProxyService.implementations.sourcemanipulationimpl;

import com.iishanto.springforwardproxy.services.proxyservice.ProxyService.ProxyService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@AllArgsConstructor
@Component
public class ProxyServiceUsingSourceManipulation implements ProxyService {
    RestClient restClient;
    RestTemplate restTemplate;
    @Override
    public String getModifiedHtmlSource(String url) {
        try{
            AtomicBoolean isTextType= new AtomicBoolean(false);
            HttpEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,null,String.class);
            response.getHeaders().forEach((key, value) -> {
                System.out.println(key + " : " + value.getFirst() + " : " + key.equalsIgnoreCase("content-type"));
                if (key.equalsIgnoreCase("content-type")) {
                    String contentType = value.getFirst();
                    if (contentType.toLowerCase().contains("text") || contentType.toLowerCase().contains("html") || contentType.toLowerCase().contains("xml") || contentType.toLowerCase().contains("json") || contentType.toLowerCase().contains("javascript") || contentType.toLowerCase().contains("css") || contentType.toLowerCase().contains("plain")) {
                        isTextType.set(true);
                    }
                }
            });


            HtmlProcessor htmlProcessor=new HtmlProcessor();
            if(isTextType.get()){
                return htmlProcessor.processHtml(response.getBody());
            }else{
                return response.getBody();
            }

        }catch (Exception e){
            return "Error: "+e.getMessage();
        }
    }
}

@Data
class HttpResponse{
    byte[] body;
    Map<String, List<String>> headers;
}