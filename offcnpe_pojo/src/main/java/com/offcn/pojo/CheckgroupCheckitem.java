package com.offcn.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author zzw
 * @since 2021-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_checkgroup_checkitem")
public class CheckgroupCheckitem extends Model {

    private static final long serialVersionUID = 1L;

      private Integer checkgroupId;

    private Integer checkitemId;

    public Integer getCheckgroupId() {
        return checkgroupId;
    }

    public void setCheckgroupId(Integer checkgroupId) {
        this.checkgroupId = checkgroupId;
    }

    public Integer getCheckitemId() {
        return checkitemId;
    }

    public void setCheckitemId(Integer checkitemId) {
        this.checkitemId = checkitemId;
    }
}
