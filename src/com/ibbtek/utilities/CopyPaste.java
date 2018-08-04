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

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 * CopyPaste class
 *
 * @author Ibbtek <http://ibbtek.altervista.org/>
 */
public class CopyPaste extends EventQueue {

    public JPopupMenu popup;
    JTable table;
    public BasicAction cut, copy, paste, selectAll;

    public CopyPaste() {
        //createPopupMenu();
    }

    /**
     * createPopupMenu method
     *
     * @param text (JTextComponent)
     */
    private void createPopupMenu(JTextComponent text) {
        cut = new CutAction("Cut", new javax.swing.ImageIcon(getClass().
                getResource("/data/images/cut.png")));
        copy = new CopyAction("Copy", new javax.swing.ImageIcon(getClass().
                getResource("/data/images/copy.png")));
        paste = new PasteAction("Paste", new javax.swing.ImageIcon(getClass().
                getResource("/data/images/paste.png")));
        selectAll = new SelectAllAction("Select All", new javax.swing.ImageIcon(getClass().getResource("/data/images/select.png")));
        cut.setTextComponent(text);
        copy.setTextComponent(text);
        paste.setTextComponent(text);
        selectAll.setTextComponent(text);

        popup = new JPopupMenu();
        popup.add(cut);
        popup.add(copy);
        popup.add(paste);
        popup.addSeparator();
        popup.add(selectAll);
    }

    /**
     * showPopup method
     *
     * @param parent (Component)
     * @param me (MouseEvent)
     */
    public void showPopup(Component parent, MouseEvent me) {
        popup.validate();
        popup.show(parent, me.getX(), me.getY());
    }

    @Override
    protected void dispatchEvent(AWTEvent event) {
        super.dispatchEvent(event);
        if (!(event instanceof MouseEvent)) {
            return;
        }
        MouseEvent me = (MouseEvent) event;
        if (!me.isPopupTrigger()) {
            return;
        }
        if (!(me.getSource() instanceof Component)) {
            return;
        }
        Component comp = SwingUtilities.getDeepestComponentAt((Component) me.getSource(), me.getX(), me.getY());
        if (!(comp instanceof JTextComponent)) {
            return;
        }
        if (MenuSelectionManager.defaultManager().getSelectedPath().length > 0) {
            return;
        }
        createPopupMenu((JTextComponent) comp);
        showPopup((Component) me.getSource(), me);
    }
    /**
     * BasicAction class
     */
    public abstract class BasicAction extends AbstractAction {

        JTextComponent comp;

        public BasicAction(String text, Icon icon) {
            super(text, icon);
            putValue(Action.SHORT_DESCRIPTION, text);
        }

        public void setTextComponent(JTextComponent comp) {
            this.comp = comp;
        }

        @Override
        public abstract void actionPerformed(ActionEvent e);
    }
    /**
     * CutAction class
     */
    public class CutAction extends BasicAction {

        public CutAction(String text, Icon icon) {
            super(text, icon);
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl X"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            comp.cut();
        }

        @Override
        public boolean isEnabled() {
            return comp != null && comp.isEditable()
                    && comp.getSelectedText() != null;
        }
    }
    /**
     * CopyAction class
     */
    public class CopyAction extends BasicAction {

        public CopyAction(String text, Icon icon) {
            super(text, icon);
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl C"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            comp.copy();
        }

        @Override
        public boolean isEnabled() {
            return comp != null && comp.getSelectedText() != null;
        }
    }
    /**
     * PAsteAction class
     */
    public class PasteAction extends BasicAction {

        public PasteAction(String text, Icon icon) {
            super(text, icon);
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl V"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            comp.paste();
        }

        @Override
        public boolean isEnabled() {
            Transferable content
                    = Toolkit.getDefaultToolkit().getSystemClipboard()
                    .getContents(null);
            return comp != null && comp.isEnabled() && comp.isEditable()
                    && content.isDataFlavorSupported(DataFlavor.stringFlavor);
        }
    }
    /**
     * SelectAllAction class
     */
    public class SelectAllAction extends BasicAction {

        public SelectAllAction(String text, Icon icon) {
            super(text, icon);
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl A"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            comp.selectAll();
        }

        @Override
        public boolean isEnabled() {
            return comp != null && comp.isEnabled()
                    && comp.getText().length() > 0
                    && (comp.getSelectedText() == null
                    || comp.getSelectedText().length() < comp.getText().length());
        }
    }
}
