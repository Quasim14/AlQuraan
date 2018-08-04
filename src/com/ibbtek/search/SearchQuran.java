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
import com.ibbtek.utilities.ArabicNormalizer;
import static com.ibbtek.utilities.LogToFile.log;
import java.io.IOException;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
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
 * SearchQuran class
 * @version 1.3
 * @author Ibbtek <http://ibbtek.altervista.org/>
 */
public class SearchQuran{
    
    private final JTable jTable;
    private final DefaultTableModel model;
    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private XPathFactory xPathfactory;
    private final JTree jTree;
    private XPath xpath;
    private Document noTash;
    private String find;
    /**
     * SearchQuran constructor
     * @param find
     * @param jTable
     * @param jTree
     */
    public SearchQuran(String find, JTable jTable, JTree jTree){
        this.jTree=jTree;
        this.jTable=jTable;
        this.model = (DefaultTableModel) jTable.getModel();
        this.find=find.trim();
        findQuran();
        
    }
    /**
     * findQuran Method
     * query the Quran
     */
    private void findQuran(){
        /*
        Parse the quran xml files
        */
        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            xPathfactory = XPathFactory.newInstance();
            xpath = xPathfactory.newXPath();
            noTash = builder.parse(MainGui.quran);
//            noTash = builder.parse(new FileHandler().
//                getInternalURL("/data/quran/Arabic-No-Tashkeel.xml"));
        } catch (ParserConfigurationException | SAXException |
                IOException ex) {
            log(ex,"severe","Can't parse Quran xml file");
            return;
        }
        /*
        Normalize the searched string by calling the ArabicNormalizer method
        */
        find=new ArabicNormalizer(find).getOutput();
        /*
        search the text in the quran xml file without tashkiil
        by using XPath expression
        */
        NodeList nlOrig = null;
        try {
            String diacritics="\u0610\u0611\u0612\u0613\u0614\u0615\u0616\u0617"
                    + "\u0618\u0619\u061A\u06D6\u06D7\u06D8\u06D9\u06DA\u06DB"
                    + "\u06DC\u06DD\u06DE\u06DF\u06E0\u06E1\u06E2\u06E3\u06E4"
                    + "\u06E5\u06E6\u06E7\u06E8\u06E9\u06EA\u06EB\u06EC\u06ED"
                    + "\u0640\u064B\u064C\u064D\u064E\u064F\u0650\u0651\u0652"
                    + "\u0653\u0654\u0655\u0656\u0657\u0658\u0659\u065A\u065B"
                    + "\u065C\u065D\u065E\u065F\u0670";
            XPathExpression xpOrig = xpath.compile(
                    "//*[text()[contains(translate(.,'"+diacritics+"',''),'" 
                            + find + "')]]");
//            XPathExpression xpOrig = xpath.compile(
//                    "//*[contains(text(),'" + find + "')]");
            nlOrig = (NodeList) xpOrig.evaluate(
                    noTash, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            log(ex,"severe","Can't compile the XPathExpression");
            return;
        }
        /*
        if a result is found we get the data to display it in the table
        */
        if (nlOrig!=null) {
            //Save the current Tree path
            TreePath old =null;
            if(jTree.getSelectionPath()!=null) {
                old = jTree.getSelectionPath().getParentPath();
            }
            int rowCount=model.getRowCount();
            /*
            get rid of the previous search result by deleting every row
            */
            if (rowCount>0) {
                for (int i = 0; i < rowCount; i++) {
                    model.removeRow(0);
                }
            }
            /*
            getting all the results and add it one by one in the model table
            */
            for (int i = 1; i <= nlOrig.getLength(); i++) {
                String suraText = nlOrig.item(i - 1).getParentNode().
                            getAttributes().item(1).getTextContent();
                String ayaText = nlOrig.item(i - 1).getTextContent();
                String sura = nlOrig.item(i - 1).getParentNode().
                                    getAttributes().item(0).getTextContent();
                String aya = nlOrig.item(i - 1).getAttributes().item(0).
                            getTextContent();
                //show first result
                if(i==1){
                    
                    TreeNode rootNode  = (TreeNode) jTree.getModel().getRoot();
                    TreePath path = new TreePath(rootNode);
                    path = path.pathByAddingChild(
                            rootNode.getChildAt(Integer.parseInt(sura)-1));
                    jTree.expandPath(path);
                    jTree.setSelectionPath(path);
                    path = path.pathByAddingChild(
                            rootNode.getChildAt(Integer.parseInt(sura)-1).
                                    getChildAt(Integer.parseInt(aya)-1));
                    if(old!=null)jTree.collapsePath(old);
                    jTree.expandPath(path);
                    jTree.setSelectionPath(path);
                    jTree.scrollPathToVisible(path);
                }
                model.addRow(new Object[]{ayaText,suraText + '-'+ sura,aya});
            }
            /*
            set the table with the model we just fill in
            */
            jTable.setModel(model);
            if(jTable.getRowCount()!=0)
                jTable.setRowSelectionInterval(0, 0);
        }
    }
}
