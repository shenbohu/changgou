package com.itheima.canal.util;

import com.alibaba.otter.canal.protocol.CanalEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CanalUtil {

    /**
     * 列集合转换为 map
     * @param columnsList
     * @return
     */
    public  static  Map<String,String> convertToMap(List<CanalEntry.Column> columnsList) {
        Map<String,String> map=new HashMap( );
        columnsList.forEach( c-> map.put( c.getName(),c.getValue() ) );
        return map;
    }

}
