package dao.daoImpl;

import dao.ProvinceDao;
import entity.Province;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import util.JDBCUtils;

import java.util.List;

public class ProvinceDaoSelect implements ProvinceDao {
    @Override
    public List<Province> query() {
        JdbcTemplate jt = new JdbcTemplate(JDBCUtils.getDataSource());
        String sql = "select * from province";
        return jt.query(sql,new BeanPropertyRowMapper<Province>(Province.class));
    }
}
