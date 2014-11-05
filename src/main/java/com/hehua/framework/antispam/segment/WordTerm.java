package com.hehua.framework.antispam.segment;

/**
 * 分词以后的结果， 有2个含义，如果position == -1, 意味无法分的词 分词后的一个词条：
 * 为了节省空间，我们用词在原文中的位置(begin,length)来表示这个词
 */
public class WordTerm {

    /** 在字典中的位置 */
    public int position = -1;

    /** 输入文本中的位置 */
    public int begin;

    /** 该词的长度 */
    public int length;

    /** 该词条在字典中的额外信息 */
    public TermExtraInfo termExtraInfo;

    @Override
    public String toString() {
        return "[position=" + position + ",begin=" + begin + ",length=" + length
                + ",termExtraInfo=" + termExtraInfo + "]";
    }

    public String toString(char[] src) {
        return "[content=" + String.copyValueOf(src, begin, length) + ",position=" + position
                + ",begin=" + begin + ",length=" + length + ",termExtraInfo=" + termExtraInfo + "]";
    }

    public String toString(String src) {
        return "[content=" + src.substring(begin, begin + length) + ",position=" + position
                + ",begin=" + begin + ",length=" + length + ",termExtraInfo=" + termExtraInfo + "]";
    }
}
