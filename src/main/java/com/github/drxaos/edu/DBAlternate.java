package com.github.drxaos.edu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by home-pc on 14.12.2016.
 */

public class DBAlternate{
    static public Map<String,List<Object>> tableAndList= new HashMap<String, List<Object>>();

    static boolean IsExzistTable(String  tableName) {
        return tableAndList.containsKey(tableName);
    }

}
