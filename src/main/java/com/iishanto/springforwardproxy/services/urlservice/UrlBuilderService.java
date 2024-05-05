package com.iishanto.springforwardproxy.services.urlservice;

import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.net.URL;

@Service
public interface UrlBuilderService {
    String getConstructedUrl(URL url, @Nullable URL referer) throws URISyntaxException;
}
