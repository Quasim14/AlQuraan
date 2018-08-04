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
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JLabel;

/**
 * JLabelLink class
 * @author Ibbtek <http://ibbtek.altervista.org/>
 */
public class JLabelLink extends JLabel{
    
    JLabelLink myJLabel=this;
    /**
     * JLabelLink constructor
     * @param urlORmail
     * @param txt
     */
    public JLabelLink(String urlORmail,String txt){
        if(validEmail(urlORmail))
            sendMail(urlORmail,txt);
        else
            goWebsite(urlORmail,txt);
    }
    /**
     * goWebsite Method
     * make the jLabel looks like a website link and open a browser in click
     * @param uri
     * @param txt
     */
    private void goWebsite(final String url, final String txt) {
        this.setText("<html><a href=\"\">"+txt+"</a></html>");
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                FileHandler page = new FileHandler();
                page.openURL(url);
            }
            @Override
            public void mouseEntered(MouseEvent e){
                myJLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e){
                myJLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
    /**
     * sendMail Method
     * make the jLabel look like a link and open default email client on click
     * will not work if the OS doesn't support the Desktop API
     * @param mail
     * @param subject
     */
    private void sendMail(final String mail, final String subject) {
        this.setText("<html><a href=\"\">"+mail+"</a></html>");
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(Desktop.isDesktopSupported()){
                    try {
                        Desktop.getDesktop().mail(new URI("mailto:"+mail+
                                "?subject="+subject));
                    } catch (URISyntaxException | IOException ex) {
                        log(ex,"info","Can't open client mail");
                    }
                }
            }
            @Override
            public void mouseEntered(MouseEvent e){
                myJLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e){
                myJLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
    /**
     * validEmail Method
     * @param txt
     * @return boolean: true if it's a valid email address and false if not
     */
    private boolean validEmail(String txt){
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(txt);
        return matcher.matches();
    }
}
