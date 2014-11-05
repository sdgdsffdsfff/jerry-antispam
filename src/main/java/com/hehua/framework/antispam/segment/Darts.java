package com.hehua.framework.antispam.segment;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * double array tire 双数组字典树类
 * 
 * @author yangchaojiao
 * @version 1.0.0 2009年
 */

public class Darts implements Externalizable {

    private static final long serialVersionUID = 5420875306574617251L;

    // 节点信息
    protected int baseArray[];

    protected int checkArray[];

    // 保存节点已经使用
    transient private boolean usedArray[];

    protected TermExtraInfo extraNodeArray[];

    transient private int nextCheckPos;

    transient protected int maxCode = 0;

    protected boolean hasExtraData = true;

    private static int MAX_CJK_CODE = 65536;

    public Darts(List<Term> wordList) {
        build(wordList);
    }

    /**
     * 通过词库记录来来构建字典树
     * 
     * @param wordList 词条记录
     *        {@link com.hehua.framework.antispam.segment.Term}的表
     */
    private void build(List<Term> wordList) {

        if (wordList == null) {
            resize(1);
            baseArray[0] = 1;
            resize(maxCode + MAX_CJK_CODE);
            return;
        }
        int size = wordList.size();

        if (size > 0) {
            Collections.sort(wordList);
            resize(1);
            baseArray[0] = 1;
            nextCheckPos = 0;
            TermNode root_node = new TermNode();
            root_node.left = 0;
            root_node.right = size;
            root_node.depth = 0;
            List<TermNode> siblings = new ArrayList<TermNode>();
            fetch(wordList, root_node, siblings);
            insert(wordList, siblings);
            resize(maxCode + MAX_CJK_CODE);

        }
    }

    /**
     * 字典树中插入含有相同前缀的词
     * 
     * @param internalElements
     * @param siblings
     * @return
     */
    private int insert(List<Term> wordList, List<TermNode> siblings) {
        int begin = 0;
        int nonZeroCount = 0;
        boolean first = false;

        int pos = (siblings.get(0).code + 1 > nextCheckPos ? siblings.get(0).code + 1
                : nextCheckPos) - 1;
        if (pos >= usedArray.length) {
            resize(pos + 1);
        }
        while (true) {
            pos++;

            while (pos >= usedArray.length) {
                resize(pos + MAX_CJK_CODE);
            }

            if (checkArray[pos] != 0) {
                nonZeroCount++;
                continue;
            } else if (!first) {
                nextCheckPos = pos;
                first = true;
            }
            begin = pos - siblings.get(0).code;

            int t = begin + siblings.get(siblings.size() - 1).code;

            while (t >= usedArray.length) {
                resize(t + MAX_CJK_CODE);
            }

            if (usedArray[begin]) {
                continue;
            }
            boolean flag = false;

            for (int i = 1; i < siblings.size(); i++) {

                if (begin + siblings.get(i).code >= checkArray.length) {
                    System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
                            + siblings.get(i).code);
                }

                if (checkArray[begin + siblings.get(i).code] != 0) {
                    flag = true;
                    break;
                }
            }
            if (!flag) break;
        }

        if (1.0 * nonZeroCount / (pos - nextCheckPos + 1) >= 0.95) {
            nextCheckPos = pos;
        }
        usedArray[begin] = true;
        for (TermNode termNode : siblings) {
            checkArray[begin + termNode.code] = begin;
        }

        for (TermNode termNode : siblings) {
            List<TermNode> newSiblings = new ArrayList<TermNode>();
            if (fetch(wordList, termNode, newSiblings) == 0) {
                baseArray[begin + termNode.code] = -termNode.left - 1;
                if (hasExtraData) {
                    extraNodeArray[begin + termNode.code] = wordList.get(termNode.left).termExtraInfo;
                }
            } else {
                int ins = insert(wordList, newSiblings);
                baseArray[begin + termNode.code] = ins;
            }
            if (begin + termNode.code > maxCode) {
                maxCode = begin + termNode.code;
            }
        }

        return begin;
    }

    /**
     * 重新设置字典树的大小
     * 
     * @param size 字典树的大小
     */
    private void resize(int size) {
        int len = 0;
        if (baseArray != null) {
            if (size > baseArray.length) {
                len = baseArray.length;
            } else {
                len = size;
            }
        }
        // checkArray array
        int tmp[] = new int[size];
        if (baseArray != null) {
            System.arraycopy(baseArray, 0, tmp, 0, len);
        }
        baseArray = tmp;

        // baseArray array
        int tmp1[] = new int[size];
        if (checkArray != null) {
            System.arraycopy(checkArray, 0, tmp1, 0, len);
        }
        checkArray = tmp1;

        // usedArray array
        boolean tmp2[] = new boolean[size];
        if (usedArray != null) {
            System.arraycopy(usedArray, 0, tmp2, 0, len);
        }
        usedArray = tmp2;

        // extraNodeArray
        if (hasExtraData) {
            TermExtraInfo tmp3[] = new TermExtraInfo[size];
            if (extraNodeArray != null) {
                System.arraycopy(extraNodeArray, 0, tmp3, 0, len);
            }
            extraNodeArray = tmp3;
        }
    }

    /**
     * 获取含有相同前缀的词
     * 
     * @param words ： 源
     * @param parent ： 相同前缀的最后一个结点
     * @param siblings ：所有含有相同前缀的单词
     * @return
     */
    private int fetch(List<Term> words, TermNode parent, List<TermNode> siblings) {
        int prev = 0;
        TermNode preNode = null;
        for (int i = parent.left; i < parent.right; i++) {
            char word[] = words.get(i).sequence;
            int len = word.length;
            if (len < parent.depth) {
                continue;
            }
            int cur = 0;
            if (len != parent.depth) {
                cur = word[parent.depth] + 1;
            }

            if (prev > cur) {
                throw new RuntimeException("Fatal: given strings are not sorted.\n");
            }
            if (cur != prev || siblings.size() == 0) {
                TermNode tmpNode = new TermNode();
                tmpNode.depth = parent.depth + 1;
                tmpNode.code = cur; // 重新计算每个字的映射
                tmpNode.left = i;
                if (len == parent.depth + 1) {
                    tmpNode.termExtraInfo = words.get(i).termExtraInfo;
                }
                if (preNode != null) {
                    preNode.right = i;
                }
                preNode = tmpNode;
                siblings.add(tmpNode);
            }
            prev = cur;
        }

        if (preNode != null) {
            preNode.right = parent.right;
        }
        return siblings.size();
    }

    /**
     * 在字典树中查询是否含有某一个词
     * 
     * @param key 待测试的词
     * @return ：－1 表示不存在 否则返回排序后该词在字典的位置
     */

    public int search(String key) {
        return search(key.toCharArray(), 0, key.length());
    }

    /**
     * 查询字符数组的某一段是否在字典树中
     * 
     * @param key 字符数组
     * @param pos 起始位置
     * @param len 长度
     * @return －1 表示不存在 否则返回排序后该词在字典的位置
     */
    public int search(char key[], int pos, int len) {

        if (baseArray == null) {// 传入空字典，内部没有初始化，出现NPE
            return -1;
        }
        if (len == 0) {
            len = key.length;
        }
        int b = baseArray[0];
        int p;

        for (int i = pos; i < pos + len; i++) {// 此处应为pos+len，

            p = b + key[i] + 1;
            if (b == checkArray[p]) {
                b = baseArray[p];
            } else {
                return -1;
            }
        }
        p = b;
        int n = baseArray[p];
        if (b == checkArray[p] && n < 0) {
            return -n - 1;
        }
        return -1;
    }

    /**
     * 查询字符数组的某一段所有在字典中的前缀
     * 
     * @param key 字符数组
     * @param pos 起始位置
     * @param len 长度
     * @return 字符数组的某一段所有在字典中的前缀
     */
    public List<WordTerm> prefixSearch(char[] key, int pos, int len) {
        List<WordTerm> result = new ArrayList<WordTerm>();

        if (baseArray == null) {// 传入空字典，内部没有初始化，出现NPE
            return result;
        }

        int p, n, i, b = baseArray[0];

        for (i = pos; i < pos + len; ++i) {// 此处应为pos+len，
            p = b; // + 0;
            n = baseArray[p];
            if (b == checkArray[p] && n < 0) {
                WordTerm w = new WordTerm();
                w.position = -n - 1;
                w.begin = pos;
                w.length = i - pos;
                w.termExtraInfo = extraNodeArray[p];
                result.add(w);
            }
            p = b + (key[i]) + 1;
            if (b == checkArray[p]) {
                b = baseArray[p];
            } else {
                return result;
            }
        }
        p = b;
        n = baseArray[p];
        if (b == checkArray[p] && n < 0) {
            WordTerm w = new WordTerm();
            w.position = -n - 1;
            w.begin = pos;
            w.length = i - pos;
            w.termExtraInfo = extraNodeArray[p];
            result.add(w);
        }

        return result;
    }

    /**
     * 前向最大匹配，返回查询字符数组的某一段与在字典中最大匹配的单词
     * 
     * @param key 字符数组
     * @param pos 起始位置
     * @param len 长度
     * @return 在字典中与字符数组的某一段最大匹配的单词
     */
    public WordTerm prefixSearchMax(char[] key, int pos, int len) {

        if (baseArray == null) {// 传入空字典，内部没有初始化，出现NPE
            return null;
        }

        int p, n, i, b = baseArray[0];
        WordTerm w = null;

        for (i = pos; i < pos + len; ++i) {
            p = b; // + 0;
            n = baseArray[p];

            /**
             * 是一个单词
             */
            if (b == checkArray[p] && n < 0) {
                if (w == null) {
                    w = new WordTerm();
                }
                w.position = -n - 1;
                w.begin = pos;
                w.length = i - pos;

                if (hasExtraData) {
                    w.termExtraInfo = extraNodeArray[p];
                }
            }

            p = b + (key[i]) + 1;

            if (b == checkArray[p]) {
                b = baseArray[p];
            } else {
                return w;
            }
        }
        p = b;
        n = baseArray[p];

        if (b == checkArray[p] && n < 0) {
            if (w == null) {
                w = new WordTerm();
            }

            w.position = -n - 1;
            w.begin = pos;
            w.length = i - pos;

            if (hasExtraData) {
                w.termExtraInfo = extraNodeArray[p];
            }
        }
        return w;
    }

    /**
     * 扩展Darts，添加允许指定数量跳过字符的匹配方法</br> <br>
     * 扩展的前向匹配查找， 要以在匹配时跳过指定个数的字符。</br> <br>
     * 如指定跳过一个字符，则'a b c', 'abdc', 'a我bc'都可以匹配'abc'</br>
     * 
     * @param key 需要被匹配的字串
     * @param pos 从字串中哪个位置开始匹配
     * @param len 匹配多少个字符
     * @param skip 允许跳过的字符数(每两个字符间)
     * @return 一次匹配的结果(可能匹配到多个相互包含的词，如'abcd'和'abcde')
     */

    public List<WordTerm> prefixSearch(char[] key, int pos, int len, int skip) {
        if (skip < 0) {
            throw new IllegalArgumentException("Skip can not less than 0!");
        }
        if (skip == 0) {
            return prefixSearch(key, pos, len);
        }
        int p, n, i, b = baseArray[0];
        List<WordTerm> result = new ArrayList<WordTerm>();
        int remain = skip;
        for (i = pos; i < pos + len; ++i) {
            p = b; // + 0;
            n = baseArray[p];
            if (b == checkArray[p] && n < 0) {// 找到一个词
                WordTerm w = new WordTerm();
                w.position = -n - 1;
                w.begin = pos;
                w.length = i - pos;
                w.termExtraInfo = extraNodeArray[p];
                if (result.size() > 0) {// 如果已经跳字匹配但是没有成功，此时会增加一个重复错误匹配进去，这里处理
                    WordTerm w1 = result.get(result.size() - 1);// 取得上一个匹配结果
                    if (w1.position != w.position) {// 不是同一个词重复匹配
                        result.add(w);
                    }
                } else {
                    result.add(w);
                }
            }
            p = b + (key[i]) + 1;
            if (b == checkArray[p]) {// 还能往下接
                b = baseArray[p];
                remain = skip;// 还原还能跳过的字符数
            } else {
                if (remain == 0 || b == baseArray[0]) {// 不能再跳过字符了或者一开始就没有匹配到
                    return result;
                } else {
                    remain--;
                }
            }
        }
        p = b;
        n = baseArray[p];
        if (b == checkArray[p] && n < 0) {
            WordTerm w = new WordTerm();
            w.position = -n - 1;
            w.begin = pos;
            w.length = i - pos;
            w.termExtraInfo = extraNodeArray[p];
            if (result.size() > 0) {// 如果已经跳字匹配但是没有成功，此时会增加一个重复错误匹配进去，这里处理
                WordTerm w1 = result.get(result.size() - 1);// 取得上一个匹配结果
                if (w1.position != w.position) {// 不是同一个词重复匹配
                    result.add(w);
                }
            } else {
                result.add(w);
            }
        }
        return result;
    }

    /**
     * 扩展Darts，添加允许指定数量跳过字符的匹配方法</br> <br>
     * 扩展的前向匹配查找， 要以在匹配时跳过指定个数的字符。</br> <br>
     * 如指定跳过一个字符，则'a b c', 'abdc', 'a我bc'都可以匹配'abc'</br>
     * 
     * @param key 需要被匹配的字串
     * @param pos 从字串中哪个位置开始匹配
     * @param len 匹配多少个字符
     * @param skip 允许跳过的字符数(每两个字符间)
     * @return 一次匹配的结果(可能匹配到多个相互包含的词，如'abcd'和'abcde')
     */

    public WordTerm prefixSearchMax(char[] key, int pos, int len, int skip) {
        if (skip < 0) {
            throw new IllegalArgumentException("Skip can not less than 0!");
        }
        if (skip == 0) {
            return prefixSearchMax(key, pos, len);
        }
        int p, n, i, b = baseArray[0];
        WordTerm w = null;
        int remain = skip;
        boolean isSkipMatched = false;
        for (i = pos; i < pos + len; ++i) {
            p = b; // + 0;
            n = baseArray[p];
            if (b == checkArray[p] && n < 0) {
                if (w == null || !isSkipMatched || w.position != -n - 1) {// 如果是跳字匹配且没有发现新词，保留原词
                    if (w == null) {
                        w = new WordTerm();
                    }
                    w.position = -n - 1;
                    w.begin = pos;
                    w.length = i - pos;
                    if (hasExtraData) {
                        w.termExtraInfo = extraNodeArray[p];
                    }
                }
            }
            p = b + (key[i]) + 1;
            if (b == checkArray[p]) {// 还能往下接
                b = baseArray[p];
                remain = skip;// 还原还能跳过的字符数
            } else {
                if (remain == 0 || b == baseArray[0]) {// 不能再跳过字符了或者一开始就没有匹配到
                    return w;
                } else {
                    isSkipMatched = true;// 开始跳字匹配
                    remain--;
                }
            }
        }
        p = b;
        n = baseArray[p];
        if (b == checkArray[p] && n < 0) {
            if (w == null || !isSkipMatched || w.position != -n - 1) {// 如果是跳字匹配且没有发现新词，保留原词
                if (w == null) {
                    w = new WordTerm();
                }
                w.position = -n - 1;
                w.begin = pos;
                w.length = i - pos;
                if (hasExtraData) {
                    w.termExtraInfo = extraNodeArray[p];
                }
            }
        }
        return w;
    }

    /**
     * 恢复内容用
     * 
     * <pre>
     * protected int baseArray[]
     * protected int checkArray[]
     * protected TermExtraInfo extraNodeArray[]
     * </pre>
     */

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        hasExtraData = in.readBoolean();
        baseArray = (int[]) in.readObject();
        checkArray = (int[]) in.readObject();
        extraNodeArray = (TermExtraInfo[]) in.readObject();
    }

    /**
     * 保存内容用
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeBoolean(hasExtraData);
        out.writeObject(baseArray);
        out.writeObject(checkArray);
        out.writeObject(extraNodeArray);
    }

    /**
     * 字典树结点类
     */
    static class TermNode {

        int code; // 该结点字符的编码

        int depth; // 结点深度

        int left; // 在有序字典中包含单词的左范围

        int right; // 在有序字典中包含单词的右范围

        TermExtraInfo termExtraInfo; // 额外信息

        @Override
        public String toString() {
            return "TermNode [code=" + code + ", depth=" + depth + ", left=" + left + ", right="
                    + right + ", termExtraInfo=" + termExtraInfo + "]";
        }

    }
}
