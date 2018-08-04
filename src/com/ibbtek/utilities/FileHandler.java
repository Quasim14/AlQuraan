/*
 * The MIT License
 *
 * AlQuraan
 *
 * Copyright 2015 Ibbtek <http://ibbtek.altervista.org/>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.ibbtek.utilities;

import static com.ibbtek.utilities.LogToFile.log;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author Ibbtek <http://ibbtek.altervista.org/>
 */
public class FileHandler {

    String url;
    URI uri;
    /**
     * getInternalURL method
     * 
     * Copy a file from inside the *.jar to 
     * temporary location to be able to read it 
     * 
     * @param url (String)
     * @return String
     */
    public String getInternalURL(String url) {
        this.url = url;
        try {
            InputStream is = getClass().getResourceAsStream(this.url);
            File file = File.createTempFile("tmp", this.url.replaceAll("^.*\\.(.*)$", ".$1"));
            file.deleteOnExit();
            Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            this.url = file.getAbsolutePath();
        } catch (IOException ex) {
            log(ex, "info", "Can't convert internalURL");
        }
        return this.url;
    }
    /**
     * openURL method
     * 
     * automaticaly open a file form inside the *.jar archive in the browser
     * or any http link
     * 
     * @param url (String)
     */
    public void openURL(String url) {
        this.url = url;
        //Check if the url is valid if not convert it to internalURL
        try {
            URL checkURL = new URL(this.url);
            uri = checkURL.toURI();
        } catch (MalformedURLException | URISyntaxException ex) {
            uri = new File(getInternalURL(this.url)).toURI();
        }

        /*
         If the Desktop APi is supported
         */
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException ex) {
                log(ex, "info", "Can't open browser");
            }
            /*
             If the Desktop API is not supported by the OS try a to execute
             the adequate command
             */
        } else {
            /*
             Get the full name of the operating system
             */
            String os = System.getProperty("os.name").toLowerCase();
            Runtime rt = Runtime.getRuntime();
            try {
                /*
                 if it's a Windows operating system
                 */
                if (os.contains("win")) {
                    rt.exec("rundll32 url.dll,FileProtocolHandler " + this.url);
                    /*
                     if it's a Mac OS    
                     */
                } else if (os.contains("mac")) {
                    rt.exec("open " + this.url);
                    /*
                     if it's a Linux    
                     */
                } else {
                    String browsers[] = {"firefox", "mozilla-firefox",
                        "mozilla", "konqueror", "netscape", "opera"};
                    StringBuilder cmd = new StringBuilder();
                    for (int i = 0; i < browsers.length; i++) {
                        cmd.append(i == 0 ? "" : " || ").append(browsers[i]).
                                append(" \"").append(this.url).append("\" ");
                    }
                    rt.exec(new String[]{"sh", "-c", cmd.toString()});
                }
            } catch (IOException ex) {
                log(ex, "info", "Can't open browser");
            }
        }
    }
}
