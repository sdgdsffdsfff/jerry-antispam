/**
 * 
 */
package com.hehua.framework.antispam;

/**
 * @author zhihua
 *
 */
public interface AntispamModule {

    public AntispamResult inspect(String text);
}
