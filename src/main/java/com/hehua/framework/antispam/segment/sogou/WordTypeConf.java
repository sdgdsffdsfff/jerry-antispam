package com.hehua.framework.antispam.segment.sogou;

import java.util.Hashtable;

/**
 * sogou词库词性的标注类
 * 
 * @author liusha
 */

public class WordTypeConf {

    public static WordTypeConf instance = new WordTypeConf();

    private Hashtable<String, Integer> StrToPos = new Hashtable<String, Integer>();

    private Hashtable<Integer, String> PosToStr = new Hashtable<Integer, String>();

    private int width;

    public WordTypeConf() {
        this.width = 25;
    }

    public static WordTypeConf getIntance() {
        return instance;
    }

    public int getBitsWidth() {
        return width;
    }

    public int getBitsPos(String type) {

        if (!StrToPos.containsKey(type)) {
            int pos = StrToPos.size();
            StrToPos.put(type, pos);
            PosToStr.put(pos, type);
        }
        return StrToPos.get(type);
    }

    public String getBitsType(int pos) {
        if (PosToStr.containsKey(pos)) {
            return PosToStr.get(pos);
        } else {
            return "unknown";
        }
    }
}
