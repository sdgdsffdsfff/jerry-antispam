/**  
 * Filename:    MeaningfulDict.java  
 * Description: 
 * Copyright:   Copyright (c)2009  
 * Company:     opi-corp  
 * @author:     yantao.qiao  
 * @version:    1.0  
 * Create at:   Jul 31, 2009 12:19:34 PM  
 *  
 * Modification History:  
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * Jul 31, 2009   yantao.qiao      1.0         1.0 Version  
 */
package com.hehua.framework.antispam.segment.sogou;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.hehua.framework.antispam.segment.Darts;
import com.hehua.framework.antispam.segment.DictI;
import com.hehua.framework.antispam.segment.Term;
import com.hehua.framework.antispam.segment.WordTerm;


/**
 * @author yantaoqiao
 *
 */
public class Windows implements DictI {

    public Windows() {
        init();
    }
    
    private Darts darts = null;
    
    public void init() {
        InputStream istream = AdsDict.class.getResourceAsStream("resource/windows.dic");
        BufferedReader bin = null;

        try {
            bin = new BufferedReader(new InputStreamReader(istream, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ArrayList<Term> list = new ArrayList<Term>();
        String record = null;
        try {
            while ((record = bin.readLine()) != null) {

                String[] str = record.split("\t");
                if (str.length < 1) {
                    continue;
                }
                String word = str[0];
//                int freqence = Integer.parseInt(str[1]);
                WordType wordType = null;

                /** 注意：词库中有一些没有词性的标注 */
                if (str.length == 2) {
                    wordType = new WordType();
                } else {
                    wordType = new WordType(str[2]);
                }
                SogouDictExtraInfo info = new SogouDictExtraInfo(10, wordType);
                list.add(new Term(word, info));
            }
            bin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Darts newDarts = new Darts(list);
        darts = newDarts;
    }
    
    @Override
    public void reload() {
        init();
    }

    @Override
    public ArrayList<WordTerm> segment(String content) {
        ArrayList<WordTerm> words = new ArrayList<WordTerm>();
        if (this.darts == null) {
            return words;
        }

        if (content == null) {
            return words;
        }
        char[] array = content.toCharArray();

        for (int i = 0; i < content.length(); i++) {
            WordTerm w = darts.prefixSearchMax(array, i, array.length - i);
            if (w != null && w.position != -1) {
                words.add(w);
                if (w.length > 0) {
                    i += w.length - 1;
                }
            }
        }
        return words;
    }
    /**
     * shu
     * @param content
     * @return
     */
    public ArrayList<Term> segmentOutTerm(String content) {
        if (this.darts == null) {
            return null;
        }
        if (content == null) {
            return null;
        }
        ArrayList<WordTerm> words = segment(content);
        ArrayList<Term> termList = new ArrayList<Term>();
        for (WordTerm word : words) {
            termList.add(new Term(content.substring(word.begin, word.begin + word.length),
                    word.termExtraInfo));
        }
        return termList;
    }
    
}
