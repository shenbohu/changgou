package com.changgou.order.service.impl;

import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {
    private static  final String CART="cart_";
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private SpuFeign spuFeign;

    // 查询购物车列表
    @Override
    public Map List(String username) {
        Map map = new HashMap();
        List<OrderItem> orderItemList = redisTemplate.boundHashOps(CART + username).values();
        map.put("orderItemList",orderItemList);
        //总数量 总价格
        Integer totalNum = 0;
        Integer totalMoney = 0;
        for (OrderItem orderItem : orderItemList) {
            totalNum+=orderItem.getNum();
            totalMoney+=orderItem.getMoney();
        }
        map.put("totalNum",totalNum);
        map.put("totalMoney",totalMoney);
        return map;
    }

    // 添加购物车
    @Override
    public void addCart(String skuid, Integer num, String username) {
        // 1:查询redis的商品信心
        OrderItem  orderItem = (OrderItem) redisTemplate.boundHashOps(CART + username).get(skuid);
        //2: 如果存在  更新数量 价格
        if(orderItem!=null) {
            orderItem.setNum(orderItem.getNum()+num);
            if(orderItem.getNum()<=0) {
                // 删除商品
                redisTemplate.boundHashOps(CART+username).delete(skuid);
                return;
            }
            orderItem.setMoney(orderItem.getNum()*orderItem.getPrice());
            orderItem.setPayMoney(orderItem.getNum()*orderItem.getPrice());
        } else {
            Sku sku =  skuFeign.findById(skuid).getData();
            Spu spu = spuFeign.findSpuById(sku.getSpuId()).getData();
            // 封装orderitem
          orderItem = this.sku2OrderItem(sku,spu,num);
            //存入redis

        }

        //3:不存在  添加redis
        redisTemplate.boundHashOps(CART+username).put(skuid,orderItem);

    }

    private OrderItem sku2OrderItem(Sku sku, Spu spu, Integer num) {
        OrderItem orderItem = new OrderItem();
        orderItem.setSpuId(sku.getSpuId());
        orderItem.setSkuId(sku.getId());
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setNum(num);
        orderItem.setMoney(num*orderItem.getPrice());       //单价*数量
        orderItem.setPayMoney(num*orderItem.getPrice());    //实付金额
        orderItem.setImage(sku.getImage());
        orderItem.setWeight(sku.getWeight()*num);           //重量=单个重量*数量
        //分类ID设置
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        return orderItem;

    }
}
