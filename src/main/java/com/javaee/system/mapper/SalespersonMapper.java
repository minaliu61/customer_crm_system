package com.javaee.system.mapper;

import com.javaee.system.po.Salesperson;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SalespersonMapper {
    Salesperson selectById(Long salespersonId);
    Salesperson selectByUserId(Long userId);
    List<Salesperson> selectAll();
    List<Salesperson> selectByCondition(Salesperson salesperson);
    List<Salesperson> selectByIds(@Param("ids") List<Long> ids);
    int insert(Salesperson salesperson);
    int update(Salesperson salesperson);
    int deleteById(Long salespersonId);
    int deleteBatch(@Param("ids") List<Long> ids);
    int count();
    /**
     * 查询销售人员月度统计
     * @param salespersonId 销售人员ID
     * @return 月度统计数据
     */
    List<Map<String, Object>> selectMonthlyStatistics(Long salespersonId);

    /**
     * 查询销售人员产品销售占比
     * @param salespersonId 销售人员ID
     * @return 产品销售占比数据
     */
    List<Map<String, Object>> selectProductRatio(Long salespersonId);

    List<Map<String, Object>> selectPerformance();
}