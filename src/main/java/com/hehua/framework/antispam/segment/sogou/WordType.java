package com.hehua.framework.antispam.segment.sogou;

import java.util.BitSet;

/**
 * 词性类
 * 
 * @author liushaohui
 * @version 1.0.0 2009年7岳29日
 */

public class WordType {

    /** 词性的代表位 */
    private BitSet wordType;

    public WordType() {
        wordType = new BitSet(WordTypeConf.getIntance().getBitsWidth());
    }

    public WordType(String type) {
        wordType = new BitSet(WordTypeConf.getIntance().getBitsWidth());
        String[] types = type.split(",");
        
        for (int i = 0; i < types.length; i++) {
            int pos = WordTypeConf.getIntance().getBitsPos(types[i]);
            if (pos != -1 && pos < WordTypeConf.getIntance().getBitsWidth()) {
                wordType.set(pos);
            }
        }
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < WordTypeConf.getIntance().getBitsWidth(); i++) {
            if (wordType.get(i)) {
                buf.append(WordTypeConf.getIntance().getBitsType(i) + ",");
            }
        }
        return buf.toString();
    }
}
