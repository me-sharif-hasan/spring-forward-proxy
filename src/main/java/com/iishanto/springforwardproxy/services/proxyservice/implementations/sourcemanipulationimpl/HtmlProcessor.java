package com.iishanto.springforwardproxy.services.proxyservice.implementations.sourcemanipulationimpl;


import java.net.URI;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class HtmlProcessor {
    public String processHtml(String output, URI currentUrl) {
        //first change all src/href/url to follow root
        output=processHtmlAndCssDocument(output,currentUrl);
        return replaceWithSlash(output);
    }

    private String processHtmlAndCssDocument(String doc, URI uri) {
        String regex = "(href|src|action,jsaction)=(['\"])(.*?)(['\"])";  // Corrected regex
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(doc);
        System.out.println("COMPILED M: "+uri.toString());
        List <Map.Entry<String,String>> matches=new ArrayList<>();
        while (matcher.find()) {
            if(matcher.group(3)!=null|| !Objects.equals(matcher.group(3), "")){
                String newUrl=matcher.group(3);
                if(matcher.group(3).startsWith("/")){
                    newUrl=uri.getHost()+matcher.group(3);
                    matches.add(new AbstractMap.SimpleEntry<>(matcher.group(3),newUrl));
                }else if (!(matcher.group(3).startsWith("http:")||matcher.group(3).startsWith("https:")||matcher.group(3).startsWith("wss|"))){
                    newUrl=uri.getHost()+(uri.toString().endsWith("/")?"":"/")+matcher.group(3);
                    matches.add(new AbstractMap.SimpleEntry<>(matcher.group(3),newUrl));
                }
                System.out.println("from: "+matcher.group(3)+" -> "+newUrl);
            }
        }
        matches.sort((o1, o2) -> o2.getKey().length()-o1.getKey().length());
        String []temp=new String[]{doc};
//        matches.forEach(stringStringEntry -> temp[0] = temp[0].replaceAll(stringStringEntry.getKey(), stringStringEntry.getValue()));

        return temp[0];
    }

    private String replaceWithSlash(String output) {
        return output.replaceAll("(https|http)(://)","/$1$2");
    }
}
