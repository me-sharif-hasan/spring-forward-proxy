package com.iishanto.springforwardproxy.services.proxyservice.ProxyService;

import org.springframework.stereotype.Service;

@Service
public interface ProxyService {
    String getModifiedHtmlSource(String url);
}
