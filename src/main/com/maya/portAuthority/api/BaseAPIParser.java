package com.maya.portAuthority.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class BaseAPIParser {//implements APIParser {

    protected InputStream getInputStream(String urlString) {
        try {
        	URL feedURL=new URL(urlString);
            return feedURL.openConnection().getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
