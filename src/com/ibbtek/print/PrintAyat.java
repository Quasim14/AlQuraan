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

package com.ibbtek.print;

import static com.ibbtek.utilities.LogToFile.log;
import java.io.IOException;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import org.w3c.dom.NodeList;

/**
 * PrintAyat class
 * @version 1.3
 * @author Ibbtek <http://ibbtek.altervista.org/>
 */
public class PrintAyat {
    
    private final JTextPane jTextPane;
    private final NodeList nlOrig;
    private final NodeList nlTrans;
    private final int sura;
    private final int aya;
    private final int nbr;
    /**
     * PrintAyat constructor
     * @param jTextPane
     * @param nlOrig
     * @param nlTrans
     * @param sura
     * @param aya
     * @param nbr
     */
    public PrintAyat(JTextPane jTextPane, NodeList nlOrig, NodeList nlTrans,
            int sura, int aya, int nbr){
        this.jTextPane=jTextPane;
        this.nlOrig=nlOrig;
        this.nlTrans=nlTrans;
        this.sura=sura;
        this.aya=aya;
        this.nbr=nbr;
        printAyat();
    }
    /**
     * printAyat Method
     * Print the ayat selectionned
     */
    private void printAyat(){
        /*
        Clear the quranTextPane
        */
        jTextPane.setText("");
        /*
        Create an HTML document
        */
        HTMLDocument doc = (HTMLDocument)jTextPane.getDocument();
        HTMLEditorKit editorKit =
                (HTMLEditorKit)jTextPane.getEditorKit();
        /*
        Call the quranHtml method and retrieve the text
        */
        String text = quranHtml();

        /*
        Insert Text in HTML document
        */
        try {
            editorKit.insertHTML(doc, doc.getLength(), text, 0, 0, null);
        } catch (BadLocationException | IOException ex) {
            log(ex,"severe","Can't insert HTML while printing Ayat");
            return;
        }
        /*
        Scroll to the anchor of the html page
        and adjust the caret position
        */
        jTextPane.scrollToReference("aya");
        if(jTextPane.getCaretPosition()-200>0)
            jTextPane.setCaretPosition(jTextPane.getCaretPosition()-200);
        else
            jTextPane.setCaretPosition(0);
    }
    
    /**
     *
     * @return String html page
     */
    private String quranHtml(){
        StringBuilder htmlQuran= new StringBuilder();
        /*
        Construct the header of the html
        Open html and body tags
        */
        htmlQuran.append(""+
                "<html>\n" +
                "  <head>\n" +
                "    <title>Al-Quran</title>\n" +
                "  </head>\n" +
                "  <body style=\"font-family:'DejaVu Sans';\">\n" +
                "    <h1 style=\" text-align: center;font-size:36px;"
                + "font-family:'Traditional Arabic';\">القرآن الكريم</h1>\n");
        /*
        get 7 ayat before and after the selectionned aya
        */
        for (int i = 1; i <= nlOrig.getLength(); i++) {
            if (aya - nbr <= i && i <= aya + nbr) {
                /*
                if first aya && selectionned aya
                */
                if(i==1 && i==aya){
                    String bism=nlOrig.item(i - 1).getTextContent();
                    String first=bism.substring(38);
                    bism=bism.substring(0,38);
                    /*
                    Print highlighted
                    */
                    htmlQuran.
                            append("    <p style=\"text-align: center;background-color:#E6E6FA;\"><a name=\"aya\"></a>\n").
                            append(bism).
                            append("    </p><p style=\"text-align: center;background-color:#E6E6FA;\">\n").
                            append(first).
                            append("    </p><p style=\"text-align: center;background-color:#E6E6FA;\"><span style=\"font-style:italic;\">\n« ").
                            append(nlTrans.item(i - 1).getTextContent()).
                            append(" »</span> (Coran ").
                            append(sura).
                            append("/").
                            append(i).
                            append(")</p>\n");
                    /*
                    if ONLY first aya
                    */
                }else if (i==1){
                    String bism=nlOrig.item(i - 1).getTextContent();
                    String first=bism.substring(38);
                    bism=bism.substring(0,38);
                    /*
                    Print NOT highlighted
                    */
                    htmlQuran.
                            append("    <p style=\"text-align: center;\">\n").
                            append(bism).
                            append("    </p><p style=\"text-align: center;\">\n").
                            append(first).
                            append("    </p><p style=\"text-align: center;\">\n<span style=\"font-style:italic;\">\n« ").
                            append(nlTrans.item(i - 1).getTextContent()).
                            append(" »</span> (Coran ").
                            append(sura).
                            append("/").
                            append(i).
                            append(")</p>\n");
                    /*
                    if ONLY selectionned aya
                    */
                }else if(i==aya){
                    /*
                    Print highlighted
                    */
                    htmlQuran.
                            append("    <p style=\"text-align: center;background-color:#E6E6FA;\"><a name=\"aya\"></a>\n").
                            append(nlOrig.item(i - 1).getTextContent()).
                            append("    </p><p style=\"text-align: center;background-color:#E6E6FA;\"><span style=\"font-style:italic;\">\n« ").
                            append(nlTrans.item(i - 1).getTextContent()).
                            append(" »</span> (Coran ").
                            append(sura).
                            append("/").
                            append(i).
                            append(")</p>\n");
                    /*
                    ANY OTHER CASE
                    */
                }else{
                    /*
                    Print NOT highlighted
                    */
                    htmlQuran.
                            append("    <p style=\"text-align: center;\">\n").
                            append(nlOrig.item(i - 1).getTextContent()).
                            append("    <p style=\"text-align: center;\">\n<span style=\"font-style:italic;\">\n« ").
                            append(nlTrans.item(i - 1).getTextContent()).
                            append(" »</span> (Coran ").
                            append(sura).
                            append("/").
                            append(i).
                            append(")\n");
                }
            }
        }
        /*
        Close the body and html tags
        */
        htmlQuran.append("  </body>\n</html>");
        /*
        return the html string text
        */
        return htmlQuran.toString();
    }
}
