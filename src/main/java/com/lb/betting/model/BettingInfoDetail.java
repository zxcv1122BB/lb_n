package com.lb.betting.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author wz
 * @describe 投注信息详情
 * @date 2017年10月12日
 */
public class BettingInfoDetail implements Cloneable{
	
	private String matchId;//比赛id
	private String homeTeamName;//主队名称
	private String awayTeamName;//客队名称
	private String matchSessions;//比赛场次
	private Byte matchResult;//赛果 3胜 1平 0负
	private Date matchDate;//比赛时间 注入时为yyyy-MM-dd HH:mm:ss 通过set方法转为周几
	private String courtScore;//比分
	private String matchStatus;
	private String quizOptions;//竞猜项 eg:让球胜
	private Byte quizResults;//竞猜结果
	private String odds;//赔率
	private Byte guts = 0;//胆 1表示选择胆 0表示没有
	private Integer type;
	private String quizSign;
	private List<Map<String,Object>> guessList;//竞猜对象集合[{resultStr:'大',results:1,odds:1.59},{resultStr:'小'，results:0,odds:1.59}]
	private List<Map<String,Object>> resultList;//彩果对象集合[{resultStr:'大',results:1},{resultStr:'大',results:1,odds:1.59}]
	//A-F为足球使用
	private String notLetball;//A 不让球
	private String letball;//B 让球
	private String halfCourt;//C 半场比分
	private String totalGoal;//E 总进球
	private String upDown;//F 上下单双结果-单场，e.g：up_odd(上单)/up_even(上双)/down_odd(下单)/down_even（下双）
	private String scoreResultBj;//D 北京单场全场比分

	private String scoreResult;//D G(全场比分结果)
	//G-H为篮球使用
	private String letScore;//H(让分胜负)
	private String sizeScore;//I(大小分)
	private String winScore;//J(胜分差)

	private String homeScoreTradition;//主队总进球——传统,，e.g:0123
	private String awayScoreTradition;//客队总进球——传统，e.g:0123
	private String halfResultTratidion;//半场结果（胜平负，e.g:310）
	private String fullResultTradition;//全场结果（胜平负，e.g:310）
	/**
	 * @return the homeTeamName
	 */
	public String getHomeTeamName() {
		return homeTeamName;
	}
	/**
	 * @param homeTeamName the homeTeamName to set
	 */
	public void setHomeTeamName(String homeTeamName) {
		this.homeTeamName = homeTeamName;
	}
	/**
	 * @return the awayTeamName
	 */
	public String getAwayTeamName() {
		return awayTeamName;
	}
	/**
	 * @param awayTeamName the awayTeamName to set
	 */
	public void setAwayTeamName(String awayTeamName) {
		this.awayTeamName = awayTeamName;
	}
	/**
	 * @return the matchSessions
	 */
	public String getMatchSessions() {
		return matchSessions;
	}
	/**
	 * @param matchSessions the matchSessions to set
	 */
	public void setMatchSessions(String matchSessions) {
		this.matchSessions = matchSessions;
	}
	/**
	 * @return the matchResult
	 */
	public Byte getMatchResult() {
		return matchResult;
	}
	/**
	 * @param matchResult the matchResult to set
	 */
	public void setMatchResult(Byte matchResult) {
		this.matchResult = matchResult;
	}
	
	public String getMatchDate() {
		if(this.matchDate == null) {
			return "";
		}
		Calendar changeTime = Calendar.getInstance();
		changeTime.setTime(matchDate);
		if(type == null || type == 2) {
			return getWeek(changeTime.get(Calendar.DAY_OF_WEEK));
		}else if(type == 1 || type == 4) {//在竞彩足球中日期是根据12点到第二天1点算在一起
			if(changeTime.get(Calendar.HOUR_OF_DAY) < 12) {
				changeTime.add(Calendar.DAY_OF_MONTH, -1);
			}
		}else if(type == 3) {
			if(changeTime.get(Calendar.HOUR_OF_DAY) < 10) {
				changeTime.add(Calendar.DAY_OF_MONTH, -1);
			}
		}
		return getWeek(changeTime.get(Calendar.DAY_OF_WEEK));
	}
	private String getWeek(int week) {
		String weekStr = "";
        /*星期日:Calendar.SUNDAY=1
         *星期一:Calendar.MONDAY=2
         *星期二:Calendar.TUESDAY=3
         *星期三:Calendar.WEDNESDAY=4
         *星期四:Calendar.THURSDAY=5
         *星期五:Calendar.FRIDAY=6
         *星期六:Calendar.SATURDAY=7 */
        switch (week) {
            case 1:
                weekStr = "周日";
                break;
            case 2:
                weekStr = "周一";
                break;
            case 3:
                weekStr = "周二";
                break;
            case 4:
                weekStr = "周三";
                break;
            case 5:
                weekStr = "周四";
                break;
            case 6:
                weekStr = "周五";
                break;
            case 7:
                weekStr = "周六";
                break;
            default:
                break;
        }
        return weekStr;
	}
	public String getMatchDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
		return matchDate!=null?sdf.format(matchDate):"";
	}
	/**
	 * @param matchDate the matchDate to set
	 */
	public void setMatchDate(Date matchDate) {
		this.matchDate = matchDate;
	}
	/**
	 * @return the courtScore
	 */
	public String getCourtScore() {
		return courtScore;
	}
	/**
	 * @param courtScore the courtScore to set
	 */
	public void setCourtScore(String courtScore) {
		this.courtScore = courtScore;
	}
	/**
	 * @return the quizOptions
	 */
	public String getQuizOptions() {
		return quizOptions;
	}
	/**
	 * @param quizOptions the quizOptions to set
	 */
	public void setQuizOptions(String quizOptions) {
		this.quizOptions = quizOptions;
	}
	/**
	 * @return the odds
	 */
	public String getOdds() {
		return odds;
	}
	/**
	 * @param odds the odds to set
	 */
	public void setOdds(String odds) {
		this.odds = odds;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BettingInfoDetail [homeTeamName=" + homeTeamName + ", awayTeamName=" + awayTeamName + ", matchSessions="
				+ matchSessions + ", matchResult=" + matchResult + ", matchDate=" + matchDate + ", courtScore="
				+ courtScore + ", quizOptions=" + quizOptions + ", odds=" + odds + "]";
	}
	/**
	 * @return the guts
	 */
	public Byte getGuts() {
		return guts;
	}
	/**
	 * @param guts the guts to set
	 */
	public void setGuts(Byte guts) {
		this.guts = guts;
	}
	/**
	 * @return the quizResults
	 */
	public Byte getQuizResults() {
		return quizResults;
	}
	/**
	 * @param quizResults the quizResults to set
	 */
	public void setQuizResults(Byte quizResults) {
		this.quizResults = quizResults;
	}
	/**
	 * @return the guessList
	 */
	public List<Map<String, Object>> getGuessList() {
		return guessList;
	}
	/**
	 * @param guessList the guessList to set
	 */
	public void setGuessList(List<Map<String, Object>> guessList) {
		this.guessList = guessList;
	}
	/**
	 * @return the resultList
	 */
	public List<Map<String, Object>> getResultList() {
		return resultList;
	}
	/**
	 * @param resultList the resultList to set
	 */
	public void setResultList(List<Map<String, Object>> resultList) {
		this.resultList = resultList;
	}
	/**
	 * @return the notLetball
	 */
	public String getNotLetball() {
		return notLetball;
	}
	/**
	 * @param notLetball the notLetball to set
	 */
	public void setNotLetball(String notLetball) {
		this.notLetball = notLetball;
	}
	/**
	 * @return the letball
	 */
	public String getLetball() {
		return letball;
	}
	/**
	 * @param letball the letball to set
	 */
	public void setLetball(String letball) {
		this.letball = letball;
	}
	/**
	 * @return the halfCourt
	 */
	public String getHalfCourt() {
		return halfCourt;
	}
	/**
	 * @param halfCourt the halfCourt to set
	 */
	public void setHalfCourt(String halfCourt) {
		this.halfCourt = halfCourt;
	}
	/**
	 * @return the totalGoal
	 */
	public String getTotalGoal() {
		return totalGoal;
	}
	/**
	 * @param totalGoal the totalGoal to set
	 */
	public void setTotalGoal(String totalGoal) {
		this.totalGoal = totalGoal;
	}
	/**
	 * @return the upDown
	 */
	public String getUpDown() {
		return upDown;
	}
	/**
	 * @param upDown the upDown to set
	 */
	public void setUpDown(String upDown) {
		this.upDown = upDown;
	}
	/**
	 * @return the scoreResultBj
	 */
	public String getScoreResultBj() {
		return scoreResultBj;
	}
	/**
	 * @param scoreResultBj the scoreResultBj to set
	 */
	public void setScoreResultBj(String scoreResultBj) {
		this.scoreResultBj = scoreResultBj;
	}
	/**
	 * @return the scoreResult
	 */
	public String getScoreResult() {
		return scoreResult;
	}
	/**
	 * @param scoreResult the scoreResult to set
	 */
	public void setScoreResult(String scoreResult) {
		this.scoreResult = scoreResult;
	}
	/**
	 * @return the letScore
	 */
	public String getLetScore() {
		return letScore;
	}
	/**
	 * @param letScore the letScore to set
	 */
	public void setLetScore(String letScore) {
		this.letScore = letScore;
	}
	/**
	 * @return the sizeScore
	 */
	public String getSizeScore() {
		return sizeScore;
	}
	/**
	 * @param sizeScore the sizeScore to set
	 */
	public void setSizeScore(String sizeScore) {
		this.sizeScore = sizeScore;
	}
	/**
	 * @return the winScore
	 */
	public String getWinScore() {
		return winScore;
	}
	/**
	 * @param winScore the winScore to set
	 */
	public void setWinScore(String winScore) {
		this.winScore = winScore;
	}
	@Override
	public BettingInfoDetail clone(){
		try {
			return (BettingInfoDetail) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * @return the matchId
	 */
	public String getMatchId() {
		return matchId;
	}
	/**
	 * @param matchId the matchId to set
	 */
	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}
	/**
	 * @return the matchStatus
	 */
	public String getMatchStatus() {
		return matchStatus;
	}
	/**
	 * @param matchStatus the matchStatus to set
	 */
	public void setMatchStatus(String matchStatus) {
		this.matchStatus = matchStatus;
	}
	/**
	 * @return the homeScoreTradition
	 */
	public String getHomeScoreTradition() {
		return homeScoreTradition;
	}
	/**
	 * @param homeScoreTradition the homeScoreTradition to set
	 */
	public void setHomeScoreTradition(String homeScoreTradition) {
		this.homeScoreTradition = homeScoreTradition;
	}
	/**
	 * @return the awayScoreTradition
	 */
	public String getAwayScoreTradition() {
		return awayScoreTradition;
	}
	/**
	 * @param awayScoreTradition the awayScoreTradition to set
	 */
	public void setAwayScoreTradition(String awayScoreTradition) {
		this.awayScoreTradition = awayScoreTradition;
	}
	/**
	 * @return the halfResultTratidion
	 */
	public String getHalfResultTratidion() {
		return halfResultTratidion;
	}
	/**
	 * @param halfResultTratidion the halfResultTratidion to set
	 */
	public void setHalfResultTratidion(String halfResultTratidion) {
		this.halfResultTratidion = halfResultTratidion;
	}
	/**
	 * @return the fullResultTradition
	 */
	public String getFullResultTradition() {
		return fullResultTradition;
	}
	/**
	 * @param fullResultTradition the fullResultTradition to set
	 */
	public void setFullResultTradition(String fullResultTradition) {
		this.fullResultTradition = fullResultTradition;
	}
	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * @return the quizSign
	 */
	public String getQuizSign() {
		return quizSign;
	}
	/**
	 * @param quizSign the quizSign to set
	 */
	public void setQuizSign(String quizSign) {
		this.quizSign = quizSign;
	}
	
}
