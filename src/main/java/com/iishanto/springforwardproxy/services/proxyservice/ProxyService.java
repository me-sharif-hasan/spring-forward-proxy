package com.iishanto.springforwardproxy.services.proxyservice;

import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public interface ProxyService {
    HttpResponse getModifiedHtmlSource(String url, HttpHeaders headers);

    @Data
    class HttpResponse{
        byte[] body;
        Map<String, List<String>> headers;
    }
}

