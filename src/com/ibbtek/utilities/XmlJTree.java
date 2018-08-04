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
import java.io.IOException;
import javax.swing.JTree;
import javax.swing.tree.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


/**
 * XmlJTree class
 * @author Ibbtek <http://ibbtek.altervista.org/>
 */
public final class XmlJTree extends JTree{
    
    DefaultTreeModel dtModel=null;
    
    /**
     * XmlJTree constructor
     * @param filePath
     */
    public XmlJTree(String filePath){
        Node root = null;
        /*
            Parse the xml file
        */
        try {
            DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(filePath);
            root = (Node) doc.getDocumentElement();
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            log(ex,"severe","Can't parse the xml file to construct the tree");
            return;
        }
        /*
            if any result set the appropriate model to the table
        */
        if(root!=null){
            dtModel= new DefaultTreeModel(quranTreeNode(root));
            this.setModel(dtModel);
        }
    }
    
    /**
     * quranTreeNode Method
     * construct the jTree from the quran xml file
     * @param root
     * @return DefaultMutableTreeNode
     */
    private DefaultMutableTreeNode quranTreeNode(Node root){
        DefaultMutableTreeNode dmtNode;
        switch (root.getNodeName()) {
            case "HolyQuran":
                dmtNode = new DefaultMutableTreeNode("القرآن");
                break;
            case "Chapter":
                dmtNode = new DefaultMutableTreeNode(
                        root.getAttributes().item(1).getTextContent()+'-'+
                                root.getAttributes().item(0).getTextContent());
                break;
            default:
                root = root.getFirstChild();
                dmtNode = new DefaultMutableTreeNode(root.getParentNode().
                        getAttributes().item(0).getTextContent());
                break;
        }
        NodeList nodeList=root.getChildNodes();
        for (int count = 0; count < nodeList.getLength(); count++){
            Node tempNode = nodeList.item(count);
            // make sure it's element node.
            if (tempNode.getNodeType() == Node.ELEMENT_NODE){
                if (tempNode.hasChildNodes()) {
                    // loop again if has child nodes
                    dmtNode.add(quranTreeNode(tempNode));
                }
            }
        }
        return dmtNode;
    }
}