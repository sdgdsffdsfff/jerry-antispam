package com.hehua.framework.antispam.segment.keyword;

import java.util.ArrayList;
import java.util.List;

import com.hehua.framework.antispam.segment.Darts;
import com.hehua.framework.antispam.segment.Term;
import com.hehua.framework.antispam.segment.WordTerm;

/**
 * 
 * 
 * @ClassName: SimpleDict
 * @Description: 简单的词典结构，用于高亮
 * @author shaohui.liu shaohui.liu@opi-corp.com
 * @date May 19, 2010 11:19:54 AM
 * 
 */
public class SimpleDict {

    private static final long serialVersionUID = 1L;

    /** 字典树 */
    private Darts darts;

    /** 左标记 */
    private static final String TAG_START = "<span style='background:yellow;'>";

    /** 右标记 */
    private static final String TAG_END = "</span>";

    private int skip;// 跳词X设置

    /**
     * 构造函数
     * 
     * @param type
     */
    public SimpleDict(List<String> strList) {
        this.skip = 0;
        List<Term> termList = new ArrayList<Term>();
        for (String str : strList) {
            termList.add(new Term(str));
        }
        this.darts = new Darts(termList);
    }

    /**
     * 构造函数
     * 
     * @param type
     */
    public SimpleDict(List<String> strList, int skip) {
        this.skip = skip;
        List<Term> termList = new ArrayList<Term>();
        for (String str : strList) {
            termList.add(new Term(str));
        }
        this.darts = new Darts(termList);
    }

    /**
     * 标记内容，没有需要高亮的时候返回null
     * 
     * @param content 要切的内容
     *@param info_type 希望用那种类型（政治、色情）的词来标记
     * @return 标记的违禁
     * @throws InterruptedException
     */

    public String lable(String content) {
        if (this.darts == null) {
            return null;
        }
        if (content == null) {
            return null;
        }
        int counter = 0;

        StringBuffer sb = new StringBuffer();

        char[] array = content.toCharArray();

        boolean find = false;

        for (int i = 0; i < content.length(); i++) {

            WordTerm w = darts.prefixSearchMax(array, i, array.length - i, this.skip);

            if (w == null || w.length < 1 || w.position == -1) {
                sb.append(array[i]);
            } else {

                find = true;
                String word = new String(array, w.begin, w.length);
                sb.append(TAG_START);
                sb.append(word);
                sb.append(TAG_END);
                // 保证长度必须要大于1
                if (w.length >= 1) {
                    i = i + w.length - 1;
                }
            }
            // 做检测，如果循环的次数
            counter++;
            if (counter > content.length()) {
                break;
            }
        }
        if (!find) {
            return null;
        }
        return sb.toString();
    }

    /**
     * 
     * @Title: highLight
     * @Description: 高亮内容，没有需要高亮的时候不返回null
     * @param content 内容
     * @return
     * @return String
     * @throws
     */

    public String highlight(String content) {
        if (this.darts == null) {
            return content;
        }
        if (content == null) {
            return null;
        }
        int counter = 0;
        boolean find = false;

        StringBuffer sb = new StringBuffer();

        char[] array = content.toCharArray();

        for (int i = 0; i < content.length(); i++) {

            WordTerm w = darts.prefixSearchMax(array, i, array.length - i, this.skip);

            if (w == null || w.length < 1 || w.position == -1) {
                sb.append(array[i]);
            } else {
                find = true;
                String word = new String(array, w.begin, w.length);
                sb.append(TAG_START);
                sb.append(word);
                sb.append(TAG_END);
                // 保证长度必须要大于1
                if (w.length >= 1) {
                    i = i + w.length - 1;
                }
            }
            // 做检测，如果循环的次数
            counter++;
            if (counter > content.length()) {
                break;
            }
        }
        if (!find) {
            return content;
        }
        return sb.toString();
    }

    /**
     * 
     * @Title: highLight
     * @Description: 高亮内容，没有需要高亮的时候不返回null
     * @param content 内容
     * @param leftTag 左标签
     * @param rightTag 右标签
     * @return
     * @return String
     * @throws
     */
    public String highlight(String content, String leftTag, String rightTag) {
        if (this.darts == null) {
            return content;
        }
        if (content == null) {
            return null;
        }
        int counter = 0;
        boolean find = false;

        StringBuffer sb = new StringBuffer();

        char[] array = content.toCharArray();

        for (int i = 0; i < content.length(); i++) {

            WordTerm w = darts.prefixSearchMax(array, i, array.length - i, this.skip);

            if (w == null || w.length < 1 || w.position == -1) {
                sb.append(array[i]);
            } else {
                find = true;
                String word = new String(array, w.begin, w.length);
                sb.append(leftTag);
                sb.append(word);
                sb.append(rightTag);
                // 保证长度必须要大于1
                if (w.length >= 1) {
                    i = i + w.length - 1;
                }
            }
            // 做检测，如果循环的次数
            counter++;
            if (counter > content.length()) {
                break;
            }
        }
        if (!find) {
            return content;
        }
        return sb.toString();
    }

    /**
     * 输入文章，开始分词
     * 
     * @param content :待分词的文章
     * @return ： 违禁的词数组
     */

    public ArrayList<WordTerm> segment(String content) {
        if (this.darts == null) {
            return null;
        }

        if (content == null) {
            return null;
        }
        ArrayList<WordTerm> words = new ArrayList<WordTerm>();
        char[] array = content.toCharArray();
        for (int i = 0; i < content.length(); i++) {
            WordTerm w = darts.prefixSearchMax(array, i, array.length - i, this.skip);

            if (w == null || w.length < 1 || w.position == -1) {
                continue;
            }
            words.add(w);
            i += w.length - 1;
        }
        return words;
    }

    /**
     * 输入文章，开始分词
     * 
     * @param content :待分词的文章
     * @return ： 违禁的词数组
     */

    public ArrayList<String> getKeywords(String content) {
        if (this.darts == null) {
            return null;
        }

        if (content == null) {
            return null;
        }
        ArrayList<String> words = new ArrayList<String>();

        char[] array = content.toCharArray();
        for (int i = 0; i < content.length(); i++) {
            WordTerm w = darts.prefixSearchMax(array, i, array.length - i, this.skip);

            if (w == null || w.length < 1 || w.position == -1) {
                continue;
            }
            words.add(content.substring(w.begin, w.begin + w.length));
            i += w.length - 1;
        }
        return words;
    }

    /**
     * 导入词表
     * 
     * @param hashtable
     */

    public Darts getDarts() {
        return this.darts;
    }
}
