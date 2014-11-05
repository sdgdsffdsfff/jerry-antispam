package com.hehua.framework.antispam.keyword;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.hehua.framework.antispam.normalization.CharNormalization;
import com.hehua.framework.antispam.segment.Darts;
import com.hehua.framework.antispam.segment.Term;
import com.hehua.framework.antispam.segment.WordTerm;

/**
 * 切词器类
 * 
 * @ClassName: Dict
 * @Description: 切词器类
 * @author shaohui.liu shaohui.liu@opi-corp.com
 * @date Apr 6, 2010 2:26:06 PM
 * 
 */
public class Dict {

    private final Logger logger = LogManager.getLogger(Dict.class);

    /** 字典树 */
    private Darts darts;

    /** 左标记 */
    private static final String[] TAG_START = { "<span style='background:yellow;'>",
            "<span style='background:red;'>" };

    /** 右标记 */
    private static final String TAG_END = "</span>";

    private int skip;// 跳词设置

    private static final Set<String> liangciSet;// 包含量词的表

    static {
        liangciSet = new HashSet<>(Arrays.asList(new String[] { "公", "千瓦", "千克", "把", "般", "班",
                "瓣", "磅", "帮", "包", "辈", "杯", "本", "笔", "柄", "拨", "部", "餐", "册", "层", "场", "场",
                "成", "尺", "重", "出", "处", "串", "幢", "床", "次", "簇", "撮", "打", "袋", "代", "担", "档",
                "道", "滴", "点", "顶", "栋", "堵", "度", "端", "段", "对", "堆", "队", "顿", "吨", "朵", "发",
                "番", "方", "分", "份", "封", "峰", "付", "幅", "副", "服", "杆", "个", "根", "股", "挂", "管",
                "行", "盒", "户", "壶", "伙", "记", "级", "剂", "架", "家", "加", "件", "间", "绞", "角", "届",
                "截", "节", "斤", "茎", "局", "具", "句", "卷", "圈", "卡", "客", "棵", "颗", "克", "孔", "口",
                "块", "捆", "类", "里", "粒", "辆", "两", "列", "立", "立", "领", "缕", "轮", "摞", "毛", "枚",
                "门", "米", "面", "秒", "名", "亩", "幕", "排", "派", "盘",
                "泡",
                "喷",
                "盆",
                "匹",
                "批", //"片",
                "篇", "撇", "瓶", "平", "期", "起", "爿", "千", "千", "顷", "曲", "圈", "群", "人", "扇", "勺",
                "身", "升", "手", "首", "束", "双", "丝", "艘", "所", "台", "摊", "滩", "趟", "堂", "套", "天",
                "条", "挑", "贴", "挺", "筒", "桶", "通", "头", "团", "坨", "丸", "碗", "位", "尾", "味", "窝",
                "席", "线", "箱", "项", "些", "牙", "眼", "样", "页", "英", "员", "元", "则", "盏", "丈", "章",
                "张", "阵", "支", "枝", "只", "种", "轴", "株", "幢", "桩", "桌", "宗", "组", "尊", "座", "公尺",
                "公分", "公里", "公顷", "公升", "立方英尺", "立方米", "平方公里" }));
    }

    /**
     * 构造函数
     * 
     * @param type
     */
    public Dict(List<Keyword> filterKeywords) {
        this.skip = 0;
        this.darts = initDarts(filterKeywords);
    }

    /**
     * 构造函数
     * 
     * @param type
     */
    public Dict(List<Keyword> filterKeywords, int skip) {
        this.skip = skip;
        this.darts = initDarts(filterKeywords);
    }

    public List<WordTerm> queryTerm(String content) {
        if (this.darts == null) {
            return null;
        }
        if (content == null) {
            return null;
        }

        int counter = 0;

        StringBuilder sb = new StringBuilder();

        char[] array = content.toCharArray();

        List<WordTerm> termList = new ArrayList<>();

        for (int i = 0; i < content.length(); i++) {

            WordTerm w = darts.prefixSearchMax(array, i, array.length - i, this.skip);

            if (w == null || w.length < 1 || w.position == -1) {
                sb.append(array[i]);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("debug >>>>>>> " + ((TermExtraInfoType) (w.termExtraInfo)).id);
                }
                String word = new String(array, w.begin, w.length);
                if (isMistake(array, w.begin, w.length)) {
                    continue;
                }
                termList.add(w);

                int level = ((TermExtraInfoType) (w.termExtraInfo)).level - 1;

                if (level >= 0 && level <= 1) {
                    sb.append(TAG_START[level]);
                    sb.append(word);
                    sb.append(TAG_END);
                }

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

        return termList;
    }

    /**
     * 标记违禁内容
     * 
     * @param content 要切的内容
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

        StringBuilder sb = new StringBuilder();

        boolean find = false;
        char[] array = content.toCharArray();

        for (int i = 0; i < content.length(); i++) {

            WordTerm w = darts.prefixSearchMax(array, i, array.length - i, this.skip);

            if (w == null || w.length < 1 || w.position == -1) {
                sb.append(array[i]);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("debug >>>>>>> " + ((TermExtraInfoType) (w.termExtraInfo)).id);
                }
                String word = new String(array, w.begin, w.length);
                if (isMistake(array, w.begin, w.length)) {
                    continue;
                }
                find = true;
                int level = ((TermExtraInfoType) (w.termExtraInfo)).level - 1;

                if (level >= 0 && level <= 1) {
                    sb.append(TAG_START[level]);
                    sb.append(word);
                    sb.append(TAG_END);
                }

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

        if (find) {
            return sb.toString();
        } else {
            return null; // 如果没有找到违禁的内容返回空
        }
    }

    /**
     * 测试是否存在误会
     * 
     * @param words
     * @param p
     * @param len
     * @return
     */
    private boolean isMistake(char[] words, int p, int len) {
        if (true) {
            return false;
        }

        for (int i = p; i < p + len; i++) {
            if (!Character.isDigit(words[i])) {
                return false;
            }
        }
        int before = 0;
        int i = p - 1;

        while (i >= 0 && Character.isDigit(words[i]) && before <= 4) {
            before++;
            i--;
        }
        int after = 0;
        i = p + len;

        while (i < words.length && Character.isDigit(words[i]) && (after + before) <= 4) {
            after++;
            i++;
        }

        if (before + after >= 3) {
            return true;
        }
        int j = 0;
        while (p + len + j < words.length) {
            if (!Character.isDigit(words[p + len + j])) break;
            j++;
        }

        // 暂时只考虑长度为1的量词

        if (p + len + j < words.length && liangciSet.contains("" + words[p + len + j])) {
            return true;
        }
        return false;
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

        boolean find = false;
        char[] array = content.toCharArray();

        for (int i = 0; i < content.length(); i++) {
            WordTerm w = darts.prefixSearchMax(array, i, array.length - i, this.skip);

            if (w != null && w.position != -1) {
                find = true;
                words.add(w);
                i += w.length - 1;
            }
        }

        if (find) {
            return words;
        } else {
            return null;
        }
    }

    /**
     * 获取关键词列表
     * 
     * @param content
     * @return
     */
    public List<String> getKeywordList(String content) {
        List<String> keyList = new ArrayList<String>();
        ArrayList<WordTerm> termList = segment(content);
        if (termList != null) {
            for (WordTerm w : termList) {
                keyList.add(content.substring(w.begin, w.begin + w.length));
            }
        }
        return keyList;
    }

    /**
     * 重新载入词库
     */
    private Darts initDarts(List<Keyword> filterKeywords) {
        Set<String> keyset = new HashSet<String>(filterKeywords.size());
        List<Term> terms = new ArrayList<Term>(filterKeywords.size());
        for (Keyword filterKeyword : filterKeywords) {
            String key = filterKeyword.getKeyword();

            key = CharNormalization.compositeTextConvert(key, true, true, true, false, false,
                    false, true, false);
            if (key == null || key.length() < 1) {
                continue;
            }
            if (!keyset.contains(key)) {
                keyset.add(key);
                terms.add(new Term(key.toCharArray(), new TermExtraInfoType(filterKeyword.getId(),
                        filterKeyword.getInfoType(), filterKeyword.getLevel())));
            }
        }
        return new Darts(terms);
    }

    public Darts getDarts() {
        return this.darts;
    }

}
