package com.changgou.search.controller;

import com.changgou.entity.Page;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.Set;

//@RestController
@Controller
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping("/list")
    public  String list(@RequestParam Map<String, String> searchMap, Model model) {
        // 处理特殊符号
        this.handlerSearchMap(searchMap);
        Map resultMap = searchService.search(searchMap);
        model.addAttribute("searchMap",searchMap);
        model.addAttribute("result",resultMap);
        // 分页
            /*
            * 1:总记录数
            * 2:当前页
            * 3:数据
            * */
        Page<SkuInfo> page = new Page<SkuInfo>(
               Long.parseLong(String.valueOf(resultMap.get("total"))),
                Integer.parseInt(String.valueOf(resultMap.get("pageNum"))),
                Page.pageSize
        );
        model.addAttribute("page",page);
         //拼装url
        StringBuilder url = new StringBuilder("/search/list");
        if(searchMap!=null && searchMap.size()>0) {
            url.append("?");
            for (String s : searchMap.keySet()) {
                if(!"sortRule".equals(s) && !"sortFiled".equals(s) && !"pageNum".equals(s)) {
                   url.append(s).append("=").append(searchMap.get(s)).append("&");
                }
            }
            String s = url.toString();
            s = s.substring(0,s.length()-1);
            model.addAttribute("url",s);

        } else {
            model.addAttribute("url",url);
        }


        return "search";
    }



    @GetMapping
    @ResponseBody
    public Map search(@RequestParam Map<String, String> searchMap) {
        // 特殊符号的处理
        this.handlerSearchMap(searchMap);
        Map search = searchService.search(searchMap);
        return search;
    }

    private void handlerSearchMap(Map<String, String> searchMap) {
        Set<Map.Entry<String, String>> entries = searchMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            // 已spec_开头
            if(entry.getKey().startsWith("spec_")) {
                searchMap.put(entry.getKey(),entry.getValue().replace("$2B","+"));
            }
        }
    }
}
