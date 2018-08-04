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

package com.ibbtek.search;

import com.ibbtek.alquraan.MainGui;
import static com.ibbtek.alquraan.MainGui.trans;
import com.ibbtek.print.PrintAyat;
import static com.ibbtek.utilities.LogToFile.log;
import java.io.File;
import java.io.IOException;
import javax.swing.JTextPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * SearchAya class
 * @version 1.3
 * @author Ibbtek <http://ibbtek.altervista.org/>
 */
public class SearchAya {
    
    private final JTextPane jTextPane;
    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private XPathFactory xPathfactory;
    private XPath xpath;
    private Document docOrig;
    private Document docTrans;
    private final int sura;
    private final int aya;
    private final int nbr;
    /**
     * SearchAya constructor
     * @param jTextPane
     * @param sura
     * @param aya
     * @param nbr 
     */
    public SearchAya(JTextPane jTextPane, int sura, int aya, int nbr){
        
        this.jTextPane=jTextPane;
        this.sura=sura;
        this.aya=aya;
        this.nbr=nbr;
        findAya();
    }
    /**
     * findAya Method
     * find an aya and sen it to the PrintAyat class
     */
    private void findAya(){
        /*
            Parse original and translation xml files
        */
        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            xPathfactory = XPathFactory.newInstance();
            xpath = xPathfactory.newXPath();
            docOrig = builder.parse(MainGui.quran);
            if(new File(
                    MainGui.appPath+"/data/trans/"+trans+".xml").exists())
                docTrans = builder.parse(
                        MainGui.appPath+"/data/trans/"+trans+".xml");
            else
                docTrans = builder.parse(MainGui.transliteration);
        } catch (ParserConfigurationException | SAXException |
                IOException ex) {
            log(ex,"severe","Can't parse Quran xml file");
            return;
        }
        NodeList nlOrig = null;
        NodeList nlTrans = null;
        /*
            search the sura and retrieve the data through a XPath expression
        */
        try {
            XPathExpression xpOrig = xpath.compile(
                    "//Chapter[@ChapterID='" + sura + "']/*/text()");
            nlOrig = (NodeList) xpOrig.evaluate(
                    docOrig, XPathConstants.NODESET);
            nlTrans = (NodeList) xpOrig.evaluate(
                    docTrans, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            log(ex,"severe","Can't compile the XPathExpression");
            return;
        }
        /*
            if there was any result, just send it to the PrintAyat class
            to display it
        */
        if (nlOrig!=null && nlTrans!=null) {
            PrintAyat print= new PrintAyat(jTextPane, nlOrig, nlTrans,
                    sura, aya, nbr);
        }
    }
    
}
