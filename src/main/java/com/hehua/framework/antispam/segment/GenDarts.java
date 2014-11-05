/**
 * Copyright 2009-2011 xiaonei.com All right reserved.This software is the confidential and proprietary information of
 * Xiaonei.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Xiaonei.com.
 *
 *Create by XiaoNeiAntiSpamTeam
 */
package com.hehua.framework.antispam.segment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *类GenDarts.java的实现描述：
 * 
 * @author yangchaojiao
 * @createTime May 25, 2009 11:50:48 AM
 */
public class GenDarts {

    public static void genOne(String dic, String binDic) throws IOException {

        InputStream worddata = new FileInputStream(dic);
        BufferedReader in = new BufferedReader(new InputStreamReader(worddata, "UTF8"));
        String newword;
        List<Term> wordList = new ArrayList<Term>(800000);

        while ((newword = in.readLine()) != null) {
            if (newword.length() > 1) {
                wordList.add(new Term(newword.split("	")[0]));
            }

        }
        in.close();
        /*
         * public void build(List<char[]> wordList, VocabularyProcess process)
         * 可以使用 darts.build(wordList， null); 如果想自己处理词汇表的格式，
         * 那么就需要实现VocabularyProcess接口。
         */
        Darts dat = new Darts(wordList);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(binDic));
        oos.writeObject(dat);
        oos.close();
    }

    public static void main(String[] args) {
        String fileIn1 = "/tmp/sogou.dic";
        String fileOut1 = "/tmp/sogou.dic.bin";
        try {
            genOne(fileIn1, fileOut1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
