package com.offcn.job;

import com.offcn.constants.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Set;

@Component
public class CleanImgJob {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Scheduled(cron = "0 0/2 * * * ?")
    private void task(){
        clean(RedisConstant.SETMEAL_PIC_UPLOAD,RedisConstant.SETMEAL_PIC_DB);
    }

    private void clean(String key1,String key2) {
        Set<String> difference = redisTemplate.opsForSet().difference(key1,key2);
        if(difference.size()>0){
            for(String val : difference){
                //从我们的图片的硬盘位置移除
                //删除指定位置的本地图片(删除成功返回true,失败返回false)
                System.out.println(new File("D:\\files\\"+val).delete());
                //从redis位置移除
                redisTemplate.boundSetOps(RedisConstant.SETMEAL_PIC_UPLOAD).remove(val);
            }
        }
    }
}
