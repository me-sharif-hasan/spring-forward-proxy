package com.iishanto.springforwardproxy.services.proxyservice.implementations.sourcemanipulationimpl;

import com.iishanto.springforwardproxy.services.proxyservice.ProxyService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@AllArgsConstructor
@Component
public class ProxyServiceUsingSourceManipulation implements ProxyService {
    RestClient restClient;
    RestTemplate restTemplate;
    Logger logger;
    @Override
    public HttpResponse getModifiedHtmlSource(String url, HttpHeaders headers) {
        HttpResponse httpResponse=new HttpResponse();
        try{
            AtomicBoolean isTextType= new AtomicBoolean(false);
            HttpEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(headers),byte[].class);
            httpResponse.setHeaders(response.getHeaders());
            response.getHeaders().forEach((key, value) -> {
                if (key.equalsIgnoreCase("content-type")) {
                    String contentType = value.getFirst();
                    if (contentType.toLowerCase().contains("text") || contentType.toLowerCase().contains("html") || contentType.toLowerCase().contains("xml") || contentType.toLowerCase().contains("json") || contentType.toLowerCase().contains("javascript") || contentType.toLowerCase().contains("css") || contentType.toLowerCase().contains("plain")) {
                        isTextType.set(true);
                    }
                }
            });


            HtmlProcessor htmlProcessor=new HtmlProcessor();
            if(isTextType.get()){
                httpResponse.setBody(htmlProcessor.processHtml(new String(Objects.requireNonNull(response.getBody())),new URI(url)).getBytes());
            }else{
                logger.info("URL: "+url+"is loaded as resource");
                httpResponse.setBody(Objects.requireNonNull(response.getBody()));
            }
            return httpResponse;
        }catch (Exception e){
            logger.info("URL error: "+url);
            return httpResponse;
        }
    }
}