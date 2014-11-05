package com.hehua.framework.antispam.keyword;

import com.hehua.framework.antispam.segment.TermExtraInfo;

public class TermExtraInfoType implements TermExtraInfo {

    public int id;//词在数据库中的id

    public int infoType;

    public int level;

    public TermExtraInfoType(int infoType, int level) {
        this.infoType = infoType;
        this.level = level;
    }

    public TermExtraInfoType(int id, int infoType, int level) {
        this.id = id;
        this.infoType = infoType;
        this.level = level;
    }

    public int getInfoType() {
        return this.infoType;
    }

    public int getLevel() {
        return this.level;
    }

    public int getID() {
        return this.id;
    }
}
