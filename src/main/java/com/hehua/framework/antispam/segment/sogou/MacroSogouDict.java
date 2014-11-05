package com.hehua.framework.antispam.segment.sogou;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.hehua.framework.antispam.segment.Darts;
import com.hehua.framework.antispam.segment.DictI;
import com.hehua.framework.antispam.segment.Term;
import com.hehua.framework.antispam.segment.WordTerm;

/**
 * sougou词库，根据搜狗提供的词表建立的词库
 * 
 * @author liushaohui
 * @version 1.0.0 搜狗词库 2009年7月27日
 */

public class MacroSogouDict implements DictI {

    public static MacroSogouDict instance = new MacroSogouDict();

    private Darts darts = null;

    private MacroSogouDict() {
        init();
    }

    /** 单例模式 */
    public static MacroSogouDict getInstance() {
        return instance;
    }

    /** 初始化 */
    public void init() {
        InputStream istream = MacroSogouDict.class.getResourceAsStream("resource/macro-sogou.dic");

        BufferedReader bin = null;

        try {
            bin = new BufferedReader(new InputStreamReader(istream, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
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
                list.add(new Term(word));
            }
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
    public List<WordTerm> segment(String content) {
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
     * 
     * @param content
     * @return
     */
    public List<Term> segmentOutTerm(String content) {
        if (this.darts == null) {
            return null;
        }
        if (content == null) {
            return null;
        }
        List<WordTerm> words = segment(content);
        ArrayList<Term> termList = new ArrayList<Term>();
        for (WordTerm word : words) {
            termList.add(new Term(content.substring(word.begin, word.begin + word.length),
                    word.termExtraInfo));
        }
        return termList;
    }

    /**
     * 获取内容里面未分出词的碎片
     * 
     * @param content
     * @return
     */
    public List<String> getFragment(String content) {

        List<String> fragments = new ArrayList<String>();

        if (this.darts == null) {
            fragments.add(content);
            return fragments;
        }

        if (content == null) {
            return fragments;
        }

        char[] array = content.toCharArray();
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < content.length(); i++) {
            WordTerm w = darts.prefixSearchMax(array, i, array.length - i);
            if (w != null && w.length > 0 && w.position != -1) {
                if (buf.length() > 0) {
                    fragments.add(buf.toString());
                    buf.delete(0, buf.length());
                }
                i += w.length - 1;
            } else {
                buf.append(content.charAt(i));
            }
        }
        return fragments;
    }
}
