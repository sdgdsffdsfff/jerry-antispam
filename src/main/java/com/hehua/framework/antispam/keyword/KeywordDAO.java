package com.hehua.framework.antispam.keyword;

import java.util.Date;
import java.util.List;

import javax.inject.Named;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 
 * @ClassName: FilterDataDao
 * @Description: 关键词过滤访问数据库类
 * @author shaohui.liu shaohui.liu@opi-corp.com
 * @date Apr 1, 2010 4:27:31 PM
 * 
 */
@Named
public interface KeywordDAO {

    @Select("select id,keyword from antispam_keyword")
    public List<Keyword> getFilterDataListByFilterType(@Param("filterType") int filterType,
            @Param("date") Date date, @Param("offset") int offset, @Param("limit") int limit,
            @Param("isenable") int isenable);

}
