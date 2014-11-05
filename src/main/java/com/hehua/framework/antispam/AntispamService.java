/**
 * 
 */
package com.hehua.framework.antispam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.hehua.framework.antispam.keyword.AntiKeywordModule;
import com.hehua.framework.antispam.keyword.Dict;
import com.hehua.framework.antispam.keyword.DictManager;

/**
 * @author zhihua
 *
 */
@Service
public class AntispamService {

    @Autowired
    private AntiKeywordModule antiKeywordModule;

    // 关键字检查
    public AntispamResult inspect(String text) {
        return antiKeywordModule.inspect(text);
    }

    public static void main(String[] args) {

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "classpath*:/spring/applicationContext*.xml");

        AntispamService antispamService = applicationContext.getBean(AntispamService.class);

        DictManager dictManager = applicationContext.getBean(DictManager.class);

        String world = "A片";

        Dict dict = dictManager.getDict();

        System.out.println(dict.getDarts().search(world));

        System.out.println(dict.lable(world));

        String[] texts = { "A片", "做证件测试消息", "hello制服诱world", "hello world 六制服诱四" };
        for (String text : texts) {
            AntispamResult inspect = antispamService.inspect(text);
            System.out.println(inspect);
        }
        System.exit(0);
    }
}
