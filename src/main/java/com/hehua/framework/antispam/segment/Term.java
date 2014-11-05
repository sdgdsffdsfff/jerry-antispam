package com.hehua.framework.antispam.segment;

/**
 * 一条词的记录，包括词本身，以及词的一些词的外信息 {@link com.hehua.framework.antispam.segment.TermExtraInfo}
 * 可以包括词频率，词性等 可以通过继承 {@link segment.com.xiaonei.segment.TermExtraInfo} 来实现
 * 
 * @author liushaohui
 * @version 1.0.0 2009年7月24日
 */

public class Term implements Comparable<Term> {

    /** 一次条次的记录 */
    public char sequence[];

    /** 词的额外信息 */
    public TermExtraInfo termExtraInfo;

    /** 通过一个词构建一个词条 */
    public Term(char line[]) {
        this.sequence = line;
        this.termExtraInfo = null;
    }

    /** 通过一个词构建一个词条 */
    public Term(String word) {
        this.sequence = word.toCharArray();
        this.termExtraInfo = null;
    }

    /**
     * 通过词和额外信息来构造一条次的记录 额外信息可以继承 {@link com.hehua.framework.antispam.segment.TermExtraInfo}
     * 接口
     * 
     * @param word
     * @param node
     */
    public Term(char word[], TermExtraInfo node) {
        this.sequence = word;
        this.termExtraInfo = node;
    }

    /**
     * 通过词和额外信息来构造一条次的记录 额外信息可以继承 {@link com.hehua.framework.antispam.segment.TermExtraInfo}
     * 接口
     * 
     * @param word
     * @param node
     */
    public Term(String word, TermExtraInfo node) {
        this.sequence = word.toCharArray();
        this.termExtraInfo = node;
    }

    public String getSequence() {
        StringBuffer buf = new StringBuffer();
        for (char ch : sequence) {
            buf.append(ch);
        }
        return buf.toString();
    }

    /**
     * 两个词条的比较函数
     */
    @Override
    public int compareTo(Term that) {
        char[] a = this.sequence;
        char[] b = that.sequence;
        int loop = a.length > b.length ? b.length : a.length;
        for (int i = 0; i < loop; i++) {
            int c = a[i] - b[i];
            if (c != 0) {
                return c;
            }
        }
        return a.length - b.length;
    }
}
