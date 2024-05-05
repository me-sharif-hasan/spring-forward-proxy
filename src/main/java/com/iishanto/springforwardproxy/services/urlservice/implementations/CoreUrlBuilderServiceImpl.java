package com.iishanto.springforwardproxy.services.urlservice.implementations;

import com.iishanto.springforwardproxy.services.urlservice.UrlBuilderService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

@AllArgsConstructor
@Component
public class CoreUrlBuilderServiceImpl implements UrlBuilderService {
    Logger logger;
    @Override
    public String getConstructedUrl(URL url, URL referer) throws URISyntaxException {
        String requestedUrl=url.getPath()+"?"+url.getQuery();
        requestedUrl = requestedUrl.replaceFirst("/","");
        URI uri=new URI(requestedUrl);
        if(uri.getHost()==null&&referer!=null){
            String path=referer.getPath().replaceFirst("/","");
            URI originalRef=new URI(path);
            if(originalRef.getHost()!=null){
                String newUrl= originalRef +(originalRef.toString().endsWith("/")||uri.toString().endsWith("/")?"":"/")+uri;
                uri=new URI(newUrl);
            }
        }
        return uri.toString();
    }

    private String clean(String url){
        String []parts=url.split("/",4);
//        logger.info(Arrays.toString(parts));
        url=parts[3];
        return url;
    }
}
