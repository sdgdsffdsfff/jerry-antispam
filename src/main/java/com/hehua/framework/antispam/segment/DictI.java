package com.hehua.framework.antispam.segment;

import java.util.ArrayList;
import java.util.List;

/**
 * 词库的接口
 * 
 * @author liushaohui
 */

public interface DictI {

    /** 根据词库对内容进行分词 */
    public List<WordTerm> segment(String content);

    /** 重载词库 */
    public void reload();
}
