/**
 * 
 */
package com.hehua.framework.antispam.keyword;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehua.framework.localcache.AbstractLocalCache;

/**
 * @author zhihua
 *
 */
@Component
public class DictLocalCache extends AbstractLocalCache<Dict> {

    @Autowired
    private KeywordDAO keywordDAO;

    @Override
    public String key() {
        return "keyworddict";
    }

    @Override
    public Dict load() {
        int filterType = 1;
        List<Keyword> keywords = keywordDAO.getFilterDataListByFilterType(filterType, new Date(),
                0, 100000, 1);
        System.out.println(keywords);
        return new Dict(keywords);
    }

}
