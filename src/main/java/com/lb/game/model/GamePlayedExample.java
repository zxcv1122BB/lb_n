package com.lb.game.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GamePlayedExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public GamePlayedExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Byte value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Byte value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Byte value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Byte value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Byte value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Byte> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Byte> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Byte value1, Byte value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andGroupidIsNull() {
            addCriterion("groupId is null");
            return (Criteria) this;
        }

        public Criteria andGroupidIsNotNull() {
            addCriterion("groupId is not null");
            return (Criteria) this;
        }

        public Criteria andGroupidEqualTo(Short value) {
            addCriterion("groupId =", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidNotEqualTo(Short value) {
            addCriterion("groupId <>", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidGreaterThan(Short value) {
            addCriterion("groupId >", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidGreaterThanOrEqualTo(Short value) {
            addCriterion("groupId >=", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidLessThan(Short value) {
            addCriterion("groupId <", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidLessThanOrEqualTo(Short value) {
            addCriterion("groupId <=", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidIn(List<Short> values) {
            addCriterion("groupId in", values, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidNotIn(List<Short> values) {
            addCriterion("groupId not in", values, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidBetween(Short value1, Short value2) {
            addCriterion("groupId between", value1, value2, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidNotBetween(Short value1, Short value2) {
            addCriterion("groupId not between", value1, value2, "groupid");
            return (Criteria) this;
        }

        public Criteria andSortIsNull() {
            addCriterion("sort is null");
            return (Criteria) this;
        }

        public Criteria andSortIsNotNull() {
            addCriterion("sort is not null");
            return (Criteria) this;
        }

        public Criteria andSortEqualTo(Integer value) {
            addCriterion("sort =", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortNotEqualTo(Integer value) {
            addCriterion("sort <>", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortGreaterThan(Integer value) {
            addCriterion("sort >", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortGreaterThanOrEqualTo(Integer value) {
            addCriterion("sort >=", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortLessThan(Integer value) {
            addCriterion("sort <", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortLessThanOrEqualTo(Integer value) {
            addCriterion("sort <=", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortIn(List<Integer> values) {
            addCriterion("sort in", values, "sort");
            return (Criteria) this;
        }

        public Criteria andSortNotIn(List<Integer> values) {
            addCriterion("sort not in", values, "sort");
            return (Criteria) this;
        }

        public Criteria andSortBetween(Integer value1, Integer value2) {
            addCriterion("sort between", value1, value2, "sort");
            return (Criteria) this;
        }

        public Criteria andSortNotBetween(Integer value1, Integer value2) {
            addCriterion("sort not between", value1, value2, "sort");
            return (Criteria) this;
        }

        public Criteria andMaxcountIsNull() {
            addCriterion("maxCount is null");
            return (Criteria) this;
        }

        public Criteria andMaxcountIsNotNull() {
            addCriterion("maxCount is not null");
            return (Criteria) this;
        }

        public Criteria andMaxcountEqualTo(Integer value) {
            addCriterion("maxCount =", value, "maxcount");
            return (Criteria) this;
        }

        public Criteria andMaxcountNotEqualTo(Integer value) {
            addCriterion("maxCount <>", value, "maxcount");
            return (Criteria) this;
        }

        public Criteria andMaxcountGreaterThan(Integer value) {
            addCriterion("maxCount >", value, "maxcount");
            return (Criteria) this;
        }

        public Criteria andMaxcountGreaterThanOrEqualTo(Integer value) {
            addCriterion("maxCount >=", value, "maxcount");
            return (Criteria) this;
        }

        public Criteria andMaxcountLessThan(Integer value) {
            addCriterion("maxCount <", value, "maxcount");
            return (Criteria) this;
        }

        public Criteria andMaxcountLessThanOrEqualTo(Integer value) {
            addCriterion("maxCount <=", value, "maxcount");
            return (Criteria) this;
        }

        public Criteria andMaxcountIn(List<Integer> values) {
            addCriterion("maxCount in", values, "maxcount");
            return (Criteria) this;
        }

        public Criteria andMaxcountNotIn(List<Integer> values) {
            addCriterion("maxCount not in", values, "maxcount");
            return (Criteria) this;
        }

        public Criteria andMaxcountBetween(Integer value1, Integer value2) {
            addCriterion("maxCount between", value1, value2, "maxcount");
            return (Criteria) this;
        }

        public Criteria andMaxcountNotBetween(Integer value1, Integer value2) {
            addCriterion("maxCount not between", value1, value2, "maxcount");
            return (Criteria) this;
        }

        public Criteria andMaxbetIsNull() {
            addCriterion("maxBet is null");
            return (Criteria) this;
        }

        public Criteria andMaxbetIsNotNull() {
            addCriterion("maxBet is not null");
            return (Criteria) this;
        }

        public Criteria andMaxbetEqualTo(Integer value) {
            addCriterion("maxBet =", value, "maxbet");
            return (Criteria) this;
        }

        public Criteria andMaxbetNotEqualTo(Integer value) {
            addCriterion("maxBet <>", value, "maxbet");
            return (Criteria) this;
        }

        public Criteria andMaxbetGreaterThan(Integer value) {
            addCriterion("maxBet >", value, "maxbet");
            return (Criteria) this;
        }

        public Criteria andMaxbetGreaterThanOrEqualTo(Integer value) {
            addCriterion("maxBet >=", value, "maxbet");
            return (Criteria) this;
        }

        public Criteria andMaxbetLessThan(Integer value) {
            addCriterion("maxBet <", value, "maxbet");
            return (Criteria) this;
        }

        public Criteria andMaxbetLessThanOrEqualTo(Integer value) {
            addCriterion("maxBet <=", value, "maxbet");
            return (Criteria) this;
        }

        public Criteria andMaxbetIn(List<Integer> values) {
            addCriterion("maxBet in", values, "maxbet");
            return (Criteria) this;
        }

        public Criteria andMaxbetNotIn(List<Integer> values) {
            addCriterion("maxBet not in", values, "maxbet");
            return (Criteria) this;
        }

        public Criteria andMaxbetBetween(Integer value1, Integer value2) {
            addCriterion("maxBet between", value1, value2, "maxbet");
            return (Criteria) this;
        }

        public Criteria andMaxbetNotBetween(Integer value1, Integer value2) {
            addCriterion("maxBet not between", value1, value2, "maxbet");
            return (Criteria) this;
        }

        public Criteria andMinamountIsNull() {
            addCriterion("minAmount is null");
            return (Criteria) this;
        }

        public Criteria andMinamountIsNotNull() {
            addCriterion("minAmount is not null");
            return (Criteria) this;
        }

        public Criteria andMinamountEqualTo(Float value) {
            addCriterion("minAmount =", value, "minamount");
            return (Criteria) this;
        }

        public Criteria andMinamountNotEqualTo(Float value) {
            addCriterion("minAmount <>", value, "minamount");
            return (Criteria) this;
        }

        public Criteria andMinamountGreaterThan(Float value) {
            addCriterion("minAmount >", value, "minamount");
            return (Criteria) this;
        }

        public Criteria andMinamountGreaterThanOrEqualTo(Float value) {
            addCriterion("minAmount >=", value, "minamount");
            return (Criteria) this;
        }

        public Criteria andMinamountLessThan(Float value) {
            addCriterion("minAmount <", value, "minamount");
            return (Criteria) this;
        }

        public Criteria andMinamountLessThanOrEqualTo(Float value) {
            addCriterion("minAmount <=", value, "minamount");
            return (Criteria) this;
        }

        public Criteria andMinamountIn(List<Float> values) {
            addCriterion("minAmount in", values, "minamount");
            return (Criteria) this;
        }

        public Criteria andMinamountNotIn(List<Float> values) {
            addCriterion("minAmount not in", values, "minamount");
            return (Criteria) this;
        }

        public Criteria andMinamountBetween(Float value1, Float value2) {
            addCriterion("minAmount between", value1, value2, "minamount");
            return (Criteria) this;
        }

        public Criteria andMinamountNotBetween(Float value1, Float value2) {
            addCriterion("minAmount not between", value1, value2, "minamount");
            return (Criteria) this;
        }

        public Criteria andSatatusIsNull() {
            addCriterion("satatus is null");
            return (Criteria) this;
        }

        public Criteria andSatatusIsNotNull() {
            addCriterion("satatus is not null");
            return (Criteria) this;
        }

        public Criteria andSatatusEqualTo(Boolean value) {
            addCriterion("satatus =", value, "satatus");
            return (Criteria) this;
        }

        public Criteria andSatatusNotEqualTo(Boolean value) {
            addCriterion("satatus <>", value, "satatus");
            return (Criteria) this;
        }

        public Criteria andSatatusGreaterThan(Boolean value) {
            addCriterion("satatus >", value, "satatus");
            return (Criteria) this;
        }

        public Criteria andSatatusGreaterThanOrEqualTo(Boolean value) {
            addCriterion("satatus >=", value, "satatus");
            return (Criteria) this;
        }

        public Criteria andSatatusLessThan(Boolean value) {
            addCriterion("satatus <", value, "satatus");
            return (Criteria) this;
        }

        public Criteria andSatatusLessThanOrEqualTo(Boolean value) {
            addCriterion("satatus <=", value, "satatus");
            return (Criteria) this;
        }

        public Criteria andSatatusIn(List<Boolean> values) {
            addCriterion("satatus in", values, "satatus");
            return (Criteria) this;
        }

        public Criteria andSatatusNotIn(List<Boolean> values) {
            addCriterion("satatus not in", values, "satatus");
            return (Criteria) this;
        }

        public Criteria andSatatusBetween(Boolean value1, Boolean value2) {
            addCriterion("satatus between", value1, value2, "satatus");
            return (Criteria) this;
        }

        public Criteria andSatatusNotBetween(Boolean value1, Boolean value2) {
            addCriterion("satatus not between", value1, value2, "satatus");
            return (Criteria) this;
        }

        public Criteria andCreateDateIsNull() {
            addCriterion("create_date is null");
            return (Criteria) this;
        }

        public Criteria andCreateDateIsNotNull() {
            addCriterion("create_date is not null");
            return (Criteria) this;
        }

        public Criteria andCreateDateEqualTo(Date value) {
            addCriterion("create_date =", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotEqualTo(Date value) {
            addCriterion("create_date <>", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateGreaterThan(Date value) {
            addCriterion("create_date >", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateGreaterThanOrEqualTo(Date value) {
            addCriterion("create_date >=", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateLessThan(Date value) {
            addCriterion("create_date <", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateLessThanOrEqualTo(Date value) {
            addCriterion("create_date <=", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateIn(List<Date> values) {
            addCriterion("create_date in", values, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotIn(List<Date> values) {
            addCriterion("create_date not in", values, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateBetween(Date value1, Date value2) {
            addCriterion("create_date between", value1, value2, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotBetween(Date value1, Date value2) {
            addCriterion("create_date not between", value1, value2, "createDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateIsNull() {
            addCriterion("update_date is null");
            return (Criteria) this;
        }

        public Criteria andUpdateDateIsNotNull() {
            addCriterion("update_date is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateDateEqualTo(Date value) {
            addCriterion("update_date =", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateNotEqualTo(Date value) {
            addCriterion("update_date <>", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateGreaterThan(Date value) {
            addCriterion("update_date >", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateGreaterThanOrEqualTo(Date value) {
            addCriterion("update_date >=", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateLessThan(Date value) {
            addCriterion("update_date <", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateLessThanOrEqualTo(Date value) {
            addCriterion("update_date <=", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateIn(List<Date> values) {
            addCriterion("update_date in", values, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateNotIn(List<Date> values) {
            addCriterion("update_date not in", values, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateBetween(Date value1, Date value2) {
            addCriterion("update_date between", value1, value2, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateNotBetween(Date value1, Date value2) {
            addCriterion("update_date not between", value1, value2, "updateDate");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}