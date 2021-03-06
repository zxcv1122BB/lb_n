package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.lb.member.model.DepositGivingStrategy;
import com.lb.member.model.DepositGivingStrategyExample;
@Mapper
public interface DepositGivingStrategyMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table deposit_giving_strategy
     *
     * @mbg.generated Thu Sep 14 09:49:08 CST 2017
     */
    long countByExample(DepositGivingStrategyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table deposit_giving_strategy
     *
     * @mbg.generated Thu Sep 14 09:49:08 CST 2017
     */
    int deleteByExample(DepositGivingStrategyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table deposit_giving_strategy
     *
     * @mbg.generated Thu Sep 14 09:49:08 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table deposit_giving_strategy
     *
     * @mbg.generated Thu Sep 14 09:49:08 CST 2017
     */
    int insert(DepositGivingStrategy record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table deposit_giving_strategy
     *
     * @mbg.generated Thu Sep 14 09:49:08 CST 2017
     */
    int insertSelective(Map<String, Object> map);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table deposit_giving_strategy
     *
     * @mbg.generated Thu Sep 14 09:49:08 CST 2017
     */
    List<DepositGivingStrategy> selectByExample(DepositGivingStrategyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table deposit_giving_strategy
     *
     * @mbg.generated Thu Sep 14 09:49:08 CST 2017
     */
    DepositGivingStrategy selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table deposit_giving_strategy
     *
     * @mbg.generated Thu Sep 14 09:49:08 CST 2017
     */
    int updateByExampleSelective(@Param("record") DepositGivingStrategy record, @Param("example") DepositGivingStrategyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table deposit_giving_strategy
     *
     * @mbg.generated Thu Sep 14 09:49:08 CST 2017
     */
    int updateByExample(@Param("record") DepositGivingStrategy record, @Param("example") DepositGivingStrategyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table deposit_giving_strategy
     *
     * @mbg.generated Thu Sep 14 09:49:08 CST 2017
     */
    int updateByPrimaryKeySelective(Map<String, Object> map);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table deposit_giving_strategy
     *
     * @mbg.generated Thu Sep 14 09:49:08 CST 2017
     */
    int updateByPrimaryKey(DepositGivingStrategy record);

	List<Map<String, Object>> queryDepositGivingStrategyList(Map<String, Object> map);

	List<Map<String, Object>> queryAllDepositGivingStrategy();

	List<Map<String, Object>> codeInformation(Map<String, Object> map);
}