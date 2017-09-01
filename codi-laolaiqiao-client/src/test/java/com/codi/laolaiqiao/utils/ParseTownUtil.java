package com.codi.laolaiqiao.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;

public class ParseTownUtil {

    public static void main(String[] args) {
        listFiles("C:/Users/Administrator/Downloads/data_location-gh-pages/data_location-gh-pages/town/");
    }
    
    /**
     * 以字符为单位读取文件，常用于读文本，数字等类型的文件
     */
    public static void readFileByChars(File file, List<TownEntity> towns) {
        Reader reader = null;
        TownEntity town = new TownEntity();
        towns.add(town);
        StringBuffer sb = new StringBuffer();
        try {
            // 一次读一个字符
            reader = new InputStreamReader(new FileInputStream(file));
            int tempchar;
            while ((tempchar = reader.read()) != -1) {
                // 对于windows下，\r\n这两个字符在一起时，表示一个换行。
                // 但如果这两个字符分开显示时，会换两次行。
                // 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
                if (((char) tempchar) != '\r' && ((char) tempchar) != '{' && ((char) tempchar) != '}') {
                    sb.append((char) tempchar);
                }
            }
            reader.close();
            //System.out.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("出错了");
        }
        
        int index = file.getName().indexOf(".");
        town.setValue(file.getName().substring(0, index));
        String[] townArray = sb.toString().split(",");
        List<TownEntity> children = new ArrayList<TownEntity>();
        town.setChildren(children);
        for (String string : townArray) {
            string = string.replace("\"", "");
            TownEntity child = new TownEntity();
            if (StringUtils.isEmpty(string)) {
                continue;
            }
            String[] array = string.split(":");
            child.setValue(array[0]);
            child.setText(array[1]);
            children.add(child);
        }
    }
    
    public static void listFiles(String path){
        File dir = new File(path);
        File[] listFiles = dir.listFiles();
        List<TownEntity> towns = new ArrayList<TownEntity>();
        for(File f: listFiles){
            readFileByChars(f, towns);
            
        }
        
        for (TownEntity townEntity : towns) {
            writeFile(townEntity);
        }
    }
    
    public static void writeFile(TownEntity townEntity) {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter("E:\\work\\laolaiqiao\\town\\" + townEntity.getValue() + ".json");
            bw = new BufferedWriter(fw);
            bw.write(JSON.toJSONString(townEntity));

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

    }
}
