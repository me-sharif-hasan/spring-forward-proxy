package com.iishanto.springforwardproxy.services.proxyservice.ProxyService.implementations.sourcemanipulationimpl;

class HtmlProcessor {
    public String processHtml(String output) {
        return replaceWithSlash(output);
    }

    //replace all http/https in the output begins with /
    private String replaceWithSlash(String output) {
        //keep http/https that not begins with x
        return output.replaceAll("(https|http)(://)","/$1$2");
    }
}
