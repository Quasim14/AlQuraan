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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

/**
 *
 * @author Ibbtek <http://ibbtek.altervista.org/>
 */
public class SerialTree {
    
    private TreeModel dtModel;
    private final String filePath;
    /**
     * 
     * @param filePath 
     */
    public SerialTree(String filePath){
        this.filePath=filePath;
        deserial();
    }
    /**
     * 
     * @param filePath
     * @param dtModel 
     */
    public SerialTree(String filePath,TreeModel dtModel){
        this.filePath=filePath;
        this.dtModel=dtModel;
        serial();
    }
    /**
     * serialize dataModel
     */
    private void serial(){
        try {
            FileOutputStream file = new FileOutputStream(filePath);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(getDtModel());
        } catch (IOException ex) {
            log(ex,"severe","Can't serialize "+filePath+" file");
        }
    }
    /**
     * deserialize dataModel
     */
    private void deserial(){
        try{
            FileInputStream file= new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(file);
            dtModel = (DefaultTreeModel) in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            log(ex,"severe","Can't serialize "+filePath+" file");
        }
    }
    /**
     * @return the dtModel
     */
    public TreeModel getDtModel() {
        return dtModel;
    }
}
