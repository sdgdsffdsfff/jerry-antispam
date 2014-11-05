package com.hehua.framework.antispam.keyword;

/**
 * 
 * @ClassName: FilterKeyword
 * @Description: 过滤关键词类
 * @author shaohui.liu shaohui.liu@opi-corp.com
 * @date Apr 21, 2010 2:28:10 PM
 * 
 */
public class Keyword {

    // 数据库对应的id
    private int id;

    //关键词
    private String keyword;

    // 过滤类型
    private int filterType;

    // 词的类型
    private int infoType;

    // 词的级别
    private int level = 1;

    public Keyword() {

    }

    /**
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param id
     * @param keyword
     * @param filterType
     * @param infoType
     * @param level
     */
    public Keyword(int id, String keyword, int filterType, int infoType, int level) {
        super();
        this.id = id;
        this.keyword = keyword;
        this.filterType = filterType;
        this.infoType = infoType;
        this.level = level;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @param keyword the keyword to set
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * @return the filterType
     */
    public int getFilterType() {
        return filterType;
    }

    /**
     * @param filterType the filterType to set
     */
    public void setFilterType(int filterType) {
        this.filterType = filterType;
    }

    /**
     * @return the infoType
     */
    public int getInfoType() {
        return infoType;
    }

    /**
     * @param infoType the infoType to set
     */
    public void setInfoType(int infoType) {
        this.infoType = infoType;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /* (非 Javadoc)
     * <p>Title: toString</p>
     * <p>Description: </p>
     * @return
     * @see java.lang.Object#toString()
    */
    @Override
    public String toString() {
        return "FilterKeyword [filterType=" + filterType + ", id=" + id + ", infoType=" + infoType
                + ", keyword=" + keyword + ", level=" + level + "]";
    }

}
