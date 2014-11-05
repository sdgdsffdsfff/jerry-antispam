/**
 * 
 */
package com.hehua.framework.antispam.keyword;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhihua
 *
 */
@Component
public class DictManager {

    @Autowired
    private DictLocalCache dictLocalCache;

    public Dict getDict() {
        return dictLocalCache.get();
    }

}
