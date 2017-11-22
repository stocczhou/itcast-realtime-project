package cn.itcast.recommend.engine.service.impl;

import cn.itcast.recommend.engine.domain.Product;
import cn.itcast.recommend.engine.service.ProductService;
import cn.itcast.recommend.engine.utils.MyShardedJedisPool;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.ShardedJedis;

import java.util.ArrayList;
import java.util.List;


public class ProductServiceImpl implements ProductService {
    @Override
    public List<Product> baseInfo(List<String> productIds) {
        ShardedJedis jedis = MyShardedJedisPool.getShardedJedisPool().getResource();
        List<Product> list = new ArrayList<Product>();
        for (String id : productIds) {
            String json = jedis.get("recom:prod:" + id);
            if (StringUtils.isNotBlank(json)) {
                list.add(new Gson().fromJson(json, Product.class));
            }
        }
        return list;
    }
}
