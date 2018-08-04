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

package com.ibbtek.listeners;

import com.ibbtek.search.SearchAya;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @version 1.3
 * @author Ibbtek <http://ibbtek.altervista.org/>
 */
public class TreeListener implements TreeSelectionListener {
    
    private final JTextPane jTextPane;
    
    /**
     * TreeListener class
     * @param jTextPane
     */
    public TreeListener(JTextPane jTextPane){
        this.jTextPane=jTextPane;
    }

    @Override
    public void valueChanged(TreeSelectionEvent se) {
        JTree tree = (JTree) se.getSource();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree
                .getLastSelectedPathComponent();
        if (selectedNode!=null && selectedNode.isLeaf()) {
            DefaultMutableTreeNode selectedSura =
                    (DefaultMutableTreeNode) se.getPath().
                            getParentPath().getLastPathComponent();
            int sura = Integer.parseInt(selectedSura.toString().
                    substring(selectedSura.toString().
                            lastIndexOf("-") + 1));
            int aya = Integer.parseInt(selectedNode.toString());
            SearchAya searchAya = new SearchAya(jTextPane, sura, aya, 3);
            
        }
    }
}