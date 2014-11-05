package com.hehua.framework.antispam.segment;

/**
 * 关键词词库的接口
 * 
 * @author liushaohui
 */
public interface KeywordDictI extends DictI {

    /** 根据关键词词库对内容进行标记 */
    public String lable(String content);

}
