package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lb.activity.model.TurntablePrize;
import com.lb.activity.model.TurntablePrizeExample;

public interface TurntablePrizeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_prize
     *
     * @mbg.generated Tue Oct 10 11:03:22 CST 2017
     */
    long countByExample(TurntablePrizeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_prize
     *
     * @mbg.generated Tue Oct 10 11:03:22 CST 2017
     */
    int deleteByExample(TurntablePrizeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_prize
     *
     * @mbg.generated Tue Oct 10 11:03:22 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_prize
     *
     * @mbg.generated Tue Oct 10 11:03:22 CST 2017
     */
    int insert(TurntablePrize record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_prize
     *
     * @mbg.generated Tue Oct 10 11:03:22 CST 2017
     */
    int insertSelective(TurntablePrize record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_prize
     *
     * @mbg.generated Tue Oct 10 11:03:22 CST 2017
     */
    List<TurntablePrize> selectByExample(TurntablePrizeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_prize
     *
     * @mbg.generated Tue Oct 10 11:03:22 CST 2017
     */
    TurntablePrize selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_prize
     *
     * @mbg.generated Tue Oct 10 11:03:22 CST 2017
     */
    int updateByExampleSelective(@Param("record") TurntablePrize record, @Param("example") TurntablePrizeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_prize
     *
     * @mbg.generated Tue Oct 10 11:03:22 CST 2017
     */
    int updateByExample(@Param("record") TurntablePrize record, @Param("example") TurntablePrizeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_prize
     *
     * @mbg.generated Tue Oct 10 11:03:22 CST 2017
     */
    int updateByPrimaryKeySelective(TurntablePrize record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_prize
     *
     * @mbg.generated Tue Oct 10 11:03:22 CST 2017
     */
    int updateByPrimaryKey(TurntablePrize record);

	List<TurntablePrize> queryTurntablePrizeList(Map<String, Object> map);
}