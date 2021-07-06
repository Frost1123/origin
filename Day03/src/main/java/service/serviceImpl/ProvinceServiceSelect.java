package service.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.ProvinceDao;
import dao.daoImpl.ProvinceDaoSelect;
import entity.Province;
import redis.clients.jedis.Jedis;
import service.ProvinceService;
import util.JedisPoolUtils;

import java.util.List;

public class ProvinceServiceSelect implements ProvinceService {
    private ProvinceDao dao = new ProvinceDaoSelect();
    @Override
    public List<Province> findAll() {
        return dao.query();
    }

    @Override
    public String findAllJson() {
        Jedis jedis = JedisPoolUtils.getJedis();
        String json = jedis.get("province");
        if(json == null || json.length() == 0){
            List<Province> list = dao.query();
            ObjectMapper mapper = new ObjectMapper();
            try {
                json = mapper.writeValueAsString(list);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            jedis.set("province",json);
            jedis.close();
        }
        return json;
    }
}
