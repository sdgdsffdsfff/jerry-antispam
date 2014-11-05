/**
 * 
 */
package com.hehua.framework.antispam.keyword;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehua.framework.antispam.AntispamException;
import com.hehua.framework.antispam.AntispamModule;
import com.hehua.framework.antispam.AntispamResult;
import com.hehua.framework.antispam.normalization.ExtractInfomation;

/**
 * @author zhihua
 *
 */
@Component
public class AntiKeywordModule implements AntispamModule {

    @Autowired
    private DictManager dictManager;

    @Override
    public AntispamResult inspect(String text) {

        AntispamResult result = new AntispamResult(text);

        if (text == null || text.length() == 0) {
            return result;
        }

        Dict dict = dictManager.getDict();
        if (dict == null) {
            return result;
        }

        // 保留图片
        String highLight = dict.lable(text);
        if (highLight != null) {
            result.addException(AntispamException.SpamKeyword);
            result.setHighlightText(highLight);
            return result;
        }
        // 滤掉空白符
        String noblankContent = ExtractInfomation.getInstance().extractAllExceptBlank(text);
        highLight = dict.lable(noblankContent);
        if (highLight != null) {
            result.addException(AntispamException.SpamKeyword);
            result.setHighlightText(highLight);
            return result;
        }

        // 提取所有的英文、数字还有汉字
        String entityContent = ExtractInfomation.getInstance().extractEntity(text);
        highLight = dict.lable(entityContent);

        if (highLight != null) {
            result.addException(AntispamException.SpamKeyword);
            result.setHighlightText(highLight);
            return result;
        }

        // 提取所有的汉字
        String chineseContent = ExtractInfomation.getInstance().extractChineseWords(entityContent);
        highLight = dict.lable(chineseContent);
        if (highLight != null) {
            result.addException(AntispamException.SpamKeyword);
            result.setHighlightText(highLight);
            return result;
        }
        return result;
    }

}
