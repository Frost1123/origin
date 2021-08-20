package com.offcn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.offcn.pojo.Member;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zzw
 * @since 2021-08-13
 */
public interface MemberMapper extends BaseMapper<Member> {
    Integer findMemberCountBeforeDate(String today);

    Integer findMemberCountByDate(String today);

    Integer findMemberTotalCount();

    Integer findMemberCountAfterDate(String thisWeekMonday);
}
