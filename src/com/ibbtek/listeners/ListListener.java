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

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * ListListener class
 * @version 1.3
 * @author Ibbtek <http://ibbtek.altervista.org/>
 */
public class ListListener implements ListSelectionListener{
    
    private final JTable quranTable;
    private final JTree quranTree;
    /**
     * ListListener constructor
     * @param quranTable
     * @param quranTree 
     */
    public ListListener(JTable quranTable,JTree quranTree){
        this.quranTable=quranTable;
        this.quranTree=quranTree;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
        if (e.getValueIsAdjusting() || !lsm.isSelectionEmpty()) {
            String sura = (String) quranTable.getValueAt(
                    quranTable.getSelectedRow(), 1);
            String aya = (String) quranTable.getValueAt(
                    quranTable.getSelectedRow(), 2);
            TreePath old =null;
            if(quranTree.getSelectionPath()!=null) {
                old = quranTree.getSelectionPath().getParentPath();
            }
            // Get Root Node from the model ...
            TreeNode rootNode  = (TreeNode) quranTree.getModel().getRoot();
            // ... and its path in the tree from the tree.
            TreePath path = new TreePath(rootNode);
            
            // Iterate over the roots children
            for(int i=0; i<rootNode.getChildCount(); i++) {
                TreeNode child = rootNode.getChildAt(i);
                if(child.toString().equals(sura)) { //you have project objects, right?
                    path = path.pathByAddingChild(child);
                    // Iterate of the project's children (root's grandchildren)
                    for(int j=0; j<child.getChildCount(); j++) {
                        TreeNode grandchild = child.getChildAt(j);
                        if(grandchild.toString().equals(aya)) {
                            path = path.pathByAddingChild(grandchild);
                            if(old!=null)quranTree.collapsePath(old);
                            quranTree.expandPath(path);
                            quranTree.setSelectionPath(path);
                            quranTree.scrollPathToVisible(path);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }
}
