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

import com.ibbtek.alquraan.MainGui;
import static com.ibbtek.utilities.LogToFile.log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * ConfigToFile
 * @author Ibbtek <http://ibbtek.altervista.org/>
 */
public class Config {
    /**
     * saveConfig method
     * 
     * Save the last translation used in a file
     */
    public static void saveConfig(){
        File config= new File("config");
        
        if(config.exists()){
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new FileWriter(config));
                out.write(MainGui.trans);
            } catch (IOException ex) {
                log(ex,"info","Can't write in config file.");
            }finally{
                try {
                    out.close();
                } catch (IOException ex) {
                    log(ex,"info","Can't close config file after writing it.");
                }
            }
        }else{
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new FileWriter(config));
                out.write("English-Transliteration");
            } catch (IOException ex) {
                log(ex,"info","Can't write in config file.");
            }finally{
                try {
                    out.close();
                } catch (IOException ex) {
                    log(ex,"info","Can't close config file after writing it.");
                }
            }
        }
    }
    /**
     * loadConfig method
     * 
     * Load the last translation used from file
     */
    public static void loadConfig(){
        File config= new File("config");
        
        if(config.exists()){
            BufferedReader in = null;
            try {
                in = new BufferedReader(new FileReader(config));
                MainGui.trans = in.readLine();
            } catch (IOException ex) {
                log(ex,"info","Can't read config file.");
            }finally{
                try {
                    in.close();
                } catch (IOException ex) {
                    log(ex,"info","Can't close config file after reading it.");
                }
            }
        }else{
            Config.saveConfig();
            Config.loadConfig();
        }
    }
}
