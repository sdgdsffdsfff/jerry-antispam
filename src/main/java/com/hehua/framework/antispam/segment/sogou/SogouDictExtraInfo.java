package com.hehua.framework.antispam.segment.sogou;

import com.hehua.framework.antispam.segment.TermExtraInfo;

public class SogouDictExtraInfo implements TermExtraInfo {

    public int frequence;

    public WordType wordType;

    public SogouDictExtraInfo(int frequence, WordType wordType) {
        this.frequence = frequence;
        this.wordType = wordType;
    }
}
