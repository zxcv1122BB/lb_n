package com.lb.log.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public LogInfoExample() {
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

        public Criteria andLogidIsNull() {
            addCriterion("logId is null");
            return (Criteria) this;
        }

        public Criteria andLogidIsNotNull() {
            addCriterion("logId is not null");
            return (Criteria) this;
        }

        public Criteria andLogidEqualTo(String value) {
            addCriterion("logId =", value, "logid");
            return (Criteria) this;
        }

        public Criteria andLogidNotEqualTo(String value) {
            addCriterion("logId <>", value, "logid");
            return (Criteria) this;
        }

        public Criteria andLogidGreaterThan(String value) {
            addCriterion("logId >", value, "logid");
            return (Criteria) this;
        }

        public Criteria andLogidGreaterThanOrEqualTo(String value) {
            addCriterion("logId >=", value, "logid");
            return (Criteria) this;
        }

        public Criteria andLogidLessThan(String value) {
            addCriterion("logId <", value, "logid");
            return (Criteria) this;
        }

        public Criteria andLogidLessThanOrEqualTo(String value) {
            addCriterion("logId <=", value, "logid");
            return (Criteria) this;
        }

        public Criteria andLogidLike(String value) {
            addCriterion("logId like", value, "logid");
            return (Criteria) this;
        }

        public Criteria andLogidNotLike(String value) {
            addCriterion("logId not like", value, "logid");
            return (Criteria) this;
        }

        public Criteria andLogidIn(List<String> values) {
            addCriterion("logId in", values, "logid");
            return (Criteria) this;
        }

        public Criteria andLogidNotIn(List<String> values) {
            addCriterion("logId not in", values, "logid");
            return (Criteria) this;
        }

        public Criteria andLogidBetween(String value1, String value2) {
            addCriterion("logId between", value1, value2, "logid");
            return (Criteria) this;
        }

        public Criteria andLogidNotBetween(String value1, String value2) {
            addCriterion("logId not between", value1, value2, "logid");
            return (Criteria) this;
        }

        public Criteria andRemarkidIsNull() {
            addCriterion("remarkId is null");
            return (Criteria) this;
        }

        public Criteria andRemarkidIsNotNull() {
            addCriterion("remarkId is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkidEqualTo(Integer value) {
            addCriterion("remarkId =", value, "remarkid");
            return (Criteria) this;
        }

        public Criteria andRemarkidNotEqualTo(Integer value) {
            addCriterion("remarkId <>", value, "remarkid");
            return (Criteria) this;
        }

        public Criteria andRemarkidGreaterThan(Integer value) {
            addCriterion("remarkId >", value, "remarkid");
            return (Criteria) this;
        }

        public Criteria andRemarkidGreaterThanOrEqualTo(Integer value) {
            addCriterion("remarkId >=", value, "remarkid");
            return (Criteria) this;
        }

        public Criteria andRemarkidLessThan(Integer value) {
            addCriterion("remarkId <", value, "remarkid");
            return (Criteria) this;
        }

        public Criteria andRemarkidLessThanOrEqualTo(Integer value) {
            addCriterion("remarkId <=", value, "remarkid");
            return (Criteria) this;
        }

        public Criteria andRemarkidIn(List<Integer> values) {
            addCriterion("remarkId in", values, "remarkid");
            return (Criteria) this;
        }

        public Criteria andRemarkidNotIn(List<Integer> values) {
            addCriterion("remarkId not in", values, "remarkid");
            return (Criteria) this;
        }

        public Criteria andRemarkidBetween(Integer value1, Integer value2) {
            addCriterion("remarkId between", value1, value2, "remarkid");
            return (Criteria) this;
        }

        public Criteria andRemarkidNotBetween(Integer value1, Integer value2) {
            addCriterion("remarkId not between", value1, value2, "remarkid");
            return (Criteria) this;
        }

        public Criteria andRemoteaddrIsNull() {
            addCriterion("remoteAddr is null");
            return (Criteria) this;
        }

        public Criteria andRemoteaddrIsNotNull() {
            addCriterion("remoteAddr is not null");
            return (Criteria) this;
        }

        public Criteria andRemoteaddrEqualTo(String value) {
            addCriterion("remoteAddr =", value, "remoteaddr");
            return (Criteria) this;
        }

        public Criteria andRemoteaddrNotEqualTo(String value) {
            addCriterion("remoteAddr <>", value, "remoteaddr");
            return (Criteria) this;
        }

        public Criteria andRemoteaddrGreaterThan(String value) {
            addCriterion("remoteAddr >", value, "remoteaddr");
            return (Criteria) this;
        }

        public Criteria andRemoteaddrGreaterThanOrEqualTo(String value) {
            addCriterion("remoteAddr >=", value, "remoteaddr");
            return (Criteria) this;
        }

        public Criteria andRemoteaddrLessThan(String value) {
            addCriterion("remoteAddr <", value, "remoteaddr");
            return (Criteria) this;
        }

        public Criteria andRemoteaddrLessThanOrEqualTo(String value) {
            addCriterion("remoteAddr <=", value, "remoteaddr");
            return (Criteria) this;
        }

        public Criteria andRemoteaddrLike(String value) {
            addCriterion("remoteAddr like", value, "remoteaddr");
            return (Criteria) this;
        }

        public Criteria andRemoteaddrNotLike(String value) {
            addCriterion("remoteAddr not like", value, "remoteaddr");
            return (Criteria) this;
        }

        public Criteria andRemoteaddrIn(List<String> values) {
            addCriterion("remoteAddr in", values, "remoteaddr");
            return (Criteria) this;
        }

        public Criteria andRemoteaddrNotIn(List<String> values) {
            addCriterion("remoteAddr not in", values, "remoteaddr");
            return (Criteria) this;
        }

        public Criteria andRemoteaddrBetween(String value1, String value2) {
            addCriterion("remoteAddr between", value1, value2, "remoteaddr");
            return (Criteria) this;
        }

        public Criteria andRemoteaddrNotBetween(String value1, String value2) {
            addCriterion("remoteAddr not between", value1, value2, "remoteaddr");
            return (Criteria) this;
        }

        public Criteria andRequesturlIsNull() {
            addCriterion("requestUrl is null");
            return (Criteria) this;
        }

        public Criteria andRequesturlIsNotNull() {
            addCriterion("requestUrl is not null");
            return (Criteria) this;
        }

        public Criteria andRequesturlEqualTo(String value) {
            addCriterion("requestUrl =", value, "requesturl");
            return (Criteria) this;
        }

        public Criteria andRequesturlNotEqualTo(String value) {
            addCriterion("requestUrl <>", value, "requesturl");
            return (Criteria) this;
        }

        public Criteria andRequesturlGreaterThan(String value) {
            addCriterion("requestUrl >", value, "requesturl");
            return (Criteria) this;
        }

        public Criteria andRequesturlGreaterThanOrEqualTo(String value) {
            addCriterion("requestUrl >=", value, "requesturl");
            return (Criteria) this;
        }

        public Criteria andRequesturlLessThan(String value) {
            addCriterion("requestUrl <", value, "requesturl");
            return (Criteria) this;
        }

        public Criteria andRequesturlLessThanOrEqualTo(String value) {
            addCriterion("requestUrl <=", value, "requesturl");
            return (Criteria) this;
        }

        public Criteria andRequesturlLike(String value) {
            addCriterion("requestUrl like", value, "requesturl");
            return (Criteria) this;
        }

        public Criteria andRequesturlNotLike(String value) {
            addCriterion("requestUrl not like", value, "requesturl");
            return (Criteria) this;
        }

        public Criteria andRequesturlIn(List<String> values) {
            addCriterion("requestUrl in", values, "requesturl");
            return (Criteria) this;
        }

        public Criteria andRequesturlNotIn(List<String> values) {
            addCriterion("requestUrl not in", values, "requesturl");
            return (Criteria) this;
        }

        public Criteria andRequesturlBetween(String value1, String value2) {
            addCriterion("requestUrl between", value1, value2, "requesturl");
            return (Criteria) this;
        }

        public Criteria andRequesturlNotBetween(String value1, String value2) {
            addCriterion("requestUrl not between", value1, value2, "requesturl");
            return (Criteria) this;
        }

        public Criteria andMethodIsNull() {
            addCriterion("method is null");
            return (Criteria) this;
        }

        public Criteria andMethodIsNotNull() {
            addCriterion("method is not null");
            return (Criteria) this;
        }

        public Criteria andMethodEqualTo(String value) {
            addCriterion("method =", value, "method");
            return (Criteria) this;
        }

        public Criteria andMethodNotEqualTo(String value) {
            addCriterion("method <>", value, "method");
            return (Criteria) this;
        }

        public Criteria andMethodGreaterThan(String value) {
            addCriterion("method >", value, "method");
            return (Criteria) this;
        }

        public Criteria andMethodGreaterThanOrEqualTo(String value) {
            addCriterion("method >=", value, "method");
            return (Criteria) this;
        }

        public Criteria andMethodLessThan(String value) {
            addCriterion("method <", value, "method");
            return (Criteria) this;
        }

        public Criteria andMethodLessThanOrEqualTo(String value) {
            addCriterion("method <=", value, "method");
            return (Criteria) this;
        }

        public Criteria andMethodLike(String value) {
            addCriterion("method like", value, "method");
            return (Criteria) this;
        }

        public Criteria andMethodNotLike(String value) {
            addCriterion("method not like", value, "method");
            return (Criteria) this;
        }

        public Criteria andMethodIn(List<String> values) {
            addCriterion("method in", values, "method");
            return (Criteria) this;
        }

        public Criteria andMethodNotIn(List<String> values) {
            addCriterion("method not in", values, "method");
            return (Criteria) this;
        }

        public Criteria andMethodBetween(String value1, String value2) {
            addCriterion("method between", value1, value2, "method");
            return (Criteria) this;
        }

        public Criteria andMethodNotBetween(String value1, String value2) {
            addCriterion("method not between", value1, value2, "method");
            return (Criteria) this;
        }

        public Criteria andParamsIsNull() {
            addCriterion("params is null");
            return (Criteria) this;
        }

        public Criteria andParamsIsNotNull() {
            addCriterion("params is not null");
            return (Criteria) this;
        }

        public Criteria andParamsEqualTo(String value) {
            addCriterion("params =", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsNotEqualTo(String value) {
            addCriterion("params <>", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsGreaterThan(String value) {
            addCriterion("params >", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsGreaterThanOrEqualTo(String value) {
            addCriterion("params >=", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsLessThan(String value) {
            addCriterion("params <", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsLessThanOrEqualTo(String value) {
            addCriterion("params <=", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsLike(String value) {
            addCriterion("params like", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsNotLike(String value) {
            addCriterion("params not like", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsIn(List<String> values) {
            addCriterion("params in", values, "params");
            return (Criteria) this;
        }

        public Criteria andParamsNotIn(List<String> values) {
            addCriterion("params not in", values, "params");
            return (Criteria) this;
        }

        public Criteria andParamsBetween(String value1, String value2) {
            addCriterion("params between", value1, value2, "params");
            return (Criteria) this;
        }

        public Criteria andParamsNotBetween(String value1, String value2) {
            addCriterion("params not between", value1, value2, "params");
            return (Criteria) this;
        }

        public Criteria andExceptionIsNull() {
            addCriterion("exception is null");
            return (Criteria) this;
        }

        public Criteria andExceptionIsNotNull() {
            addCriterion("exception is not null");
            return (Criteria) this;
        }

        public Criteria andExceptionEqualTo(String value) {
            addCriterion("exception =", value, "exception");
            return (Criteria) this;
        }

        public Criteria andExceptionNotEqualTo(String value) {
            addCriterion("exception <>", value, "exception");
            return (Criteria) this;
        }

        public Criteria andExceptionGreaterThan(String value) {
            addCriterion("exception >", value, "exception");
            return (Criteria) this;
        }

        public Criteria andExceptionGreaterThanOrEqualTo(String value) {
            addCriterion("exception >=", value, "exception");
            return (Criteria) this;
        }

        public Criteria andExceptionLessThan(String value) {
            addCriterion("exception <", value, "exception");
            return (Criteria) this;
        }

        public Criteria andExceptionLessThanOrEqualTo(String value) {
            addCriterion("exception <=", value, "exception");
            return (Criteria) this;
        }

        public Criteria andExceptionLike(String value) {
            addCriterion("exception like", value, "exception");
            return (Criteria) this;
        }

        public Criteria andExceptionNotLike(String value) {
            addCriterion("exception not like", value, "exception");
            return (Criteria) this;
        }

        public Criteria andExceptionIn(List<String> values) {
            addCriterion("exception in", values, "exception");
            return (Criteria) this;
        }

        public Criteria andExceptionNotIn(List<String> values) {
            addCriterion("exception not in", values, "exception");
            return (Criteria) this;
        }

        public Criteria andExceptionBetween(String value1, String value2) {
            addCriterion("exception between", value1, value2, "exception");
            return (Criteria) this;
        }

        public Criteria andExceptionNotBetween(String value1, String value2) {
            addCriterion("exception not between", value1, value2, "exception");
            return (Criteria) this;
        }

        public Criteria andOperatedateIsNull() {
            addCriterion("operateDate is null");
            return (Criteria) this;
        }

        public Criteria andOperatedateIsNotNull() {
            addCriterion("operateDate is not null");
            return (Criteria) this;
        }

        public Criteria andOperatedateEqualTo(Date value) {
            addCriterion("operateDate =", value, "operatedate");
            return (Criteria) this;
        }

        public Criteria andOperatedateNotEqualTo(Date value) {
            addCriterion("operateDate <>", value, "operatedate");
            return (Criteria) this;
        }

        public Criteria andOperatedateGreaterThan(Date value) {
            addCriterion("operateDate >", value, "operatedate");
            return (Criteria) this;
        }

        public Criteria andOperatedateGreaterThanOrEqualTo(Date value) {
            addCriterion("operateDate >=", value, "operatedate");
            return (Criteria) this;
        }

        public Criteria andOperatedateLessThan(Date value) {
            addCriterion("operateDate <", value, "operatedate");
            return (Criteria) this;
        }

        public Criteria andOperatedateLessThanOrEqualTo(Date value) {
            addCriterion("operateDate <=", value, "operatedate");
            return (Criteria) this;
        }

        public Criteria andOperatedateIn(List<Date> values) {
            addCriterion("operateDate in", values, "operatedate");
            return (Criteria) this;
        }

        public Criteria andOperatedateNotIn(List<Date> values) {
            addCriterion("operateDate not in", values, "operatedate");
            return (Criteria) this;
        }

        public Criteria andOperatedateBetween(Date value1, Date value2) {
            addCriterion("operateDate between", value1, value2, "operatedate");
            return (Criteria) this;
        }

        public Criteria andOperatedateNotBetween(Date value1, Date value2) {
            addCriterion("operateDate not between", value1, value2, "operatedate");
            return (Criteria) this;
        }

        public Criteria andTimeoutIsNull() {
            addCriterion("timeOut is null");
            return (Criteria) this;
        }

        public Criteria andTimeoutIsNotNull() {
            addCriterion("timeOut is not null");
            return (Criteria) this;
        }

        public Criteria andTimeoutEqualTo(Date value) {
            addCriterion("timeOut =", value, "timeout");
            return (Criteria) this;
        }

        public Criteria andTimeoutNotEqualTo(Date value) {
            addCriterion("timeOut <>", value, "timeout");
            return (Criteria) this;
        }

        public Criteria andTimeoutGreaterThan(Date value) {
            addCriterion("timeOut >", value, "timeout");
            return (Criteria) this;
        }

        public Criteria andTimeoutGreaterThanOrEqualTo(Date value) {
            addCriterion("timeOut >=", value, "timeout");
            return (Criteria) this;
        }

        public Criteria andTimeoutLessThan(Date value) {
            addCriterion("timeOut <", value, "timeout");
            return (Criteria) this;
        }

        public Criteria andTimeoutLessThanOrEqualTo(Date value) {
            addCriterion("timeOut <=", value, "timeout");
            return (Criteria) this;
        }

        public Criteria andTimeoutIn(List<Date> values) {
            addCriterion("timeOut in", values, "timeout");
            return (Criteria) this;
        }

        public Criteria andTimeoutNotIn(List<Date> values) {
            addCriterion("timeOut not in", values, "timeout");
            return (Criteria) this;
        }

        public Criteria andTimeoutBetween(Date value1, Date value2) {
            addCriterion("timeOut between", value1, value2, "timeout");
            return (Criteria) this;
        }

        public Criteria andTimeoutNotBetween(Date value1, Date value2) {
            addCriterion("timeOut not between", value1, value2, "timeout");
            return (Criteria) this;
        }

        public Criteria andUseridIsNull() {
            addCriterion("userId is null");
            return (Criteria) this;
        }

        public Criteria andUseridIsNotNull() {
            addCriterion("userId is not null");
            return (Criteria) this;
        }

        public Criteria andUseridEqualTo(Integer value) {
            addCriterion("userId =", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotEqualTo(Integer value) {
            addCriterion("userId <>", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThan(Integer value) {
            addCriterion("userId >", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThanOrEqualTo(Integer value) {
            addCriterion("userId >=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThan(Integer value) {
            addCriterion("userId <", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThanOrEqualTo(Integer value) {
            addCriterion("userId <=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridIn(List<Integer> values) {
            addCriterion("userId in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotIn(List<Integer> values) {
            addCriterion("userId not in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridBetween(Integer value1, Integer value2) {
            addCriterion("userId between", value1, value2, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotBetween(Integer value1, Integer value2) {
            addCriterion("userId not between", value1, value2, "userid");
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