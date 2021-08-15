package com.offcn.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("t_menu")
public class Menu extends Model {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    @TableField("linkUrl")
    private String linkurl;

    private String path;

    private Integer priority;

    private String icon;

    private String description;

    @TableField("parentMenuId")
    private Integer parentmenuid;

    private Integer level;


}
