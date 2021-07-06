package service;

import java.util.List;

public interface ProvinceService {
    public List<entity.Province> findAll();
    public String findAllJson();
}
