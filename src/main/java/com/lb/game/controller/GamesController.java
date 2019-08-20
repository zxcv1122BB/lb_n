package com.lb.game.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.game.model.GamePlayGroup;
import com.lb.game.model.GamePlayed;
import com.lb.game.model.GameType;
import com.lb.game.model.GameTypeExample;
import com.lb.game.service.GamePlayedService;
import com.lb.game.service.GameTypeService;
import com.lb.game.service.impl.GamePlayGroupServiceImpl;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.ResponseUtils;

@RestController
@RequestMapping("/games")
public class GamesController extends BaseController{
	@Autowired
	private GameTypeService gameTypeService;
	@Autowired
	private GamePlayGroupServiceImpl gamePlayGroupServiceImpl;
	@Autowired
	private GamePlayedService gamePlayedService;
	// 查询一级筛选条件
	@RequestMapping(method = RequestMethod.GET, value = "/selectGameType")
	public ModelAndView selectGameType() {
		List<GameType> list = gameTypeService.selectByExample(new GameTypeExample());
		return ResponseUtils.jsonView(list);
	}
	
	//查询分组信息
	@SuppressWarnings("static-access")
	@RequestMapping(method = RequestMethod.GET, value = "/selectPlayGroup")
	public ModelAndView selectPlayGroup(HttpServletRequest request) {
		Map<String, Object> map = this.jsonToMap(request);
		//参数编号
	    Integer id = Integer.valueOf(map.get("id").toString());
		List<GamePlayGroup> list = gamePlayGroupServiceImpl.selectAllFootballGamePlayGroup(id);
		return ResponseUtils.jsonView(list);
	}
	
	//查询最后一个分组信息
	@SuppressWarnings("static-access")
	@RequestMapping(method = RequestMethod.GET, value = "/selectPlayGame")
	public ModelAndView selectPlayGame(HttpServletRequest request) {
		Map<String, Object> map = this.jsonToMap(request);
		//参数编号
	    Integer id = Integer.valueOf(map.get("id").toString());
		List<GamePlayed> list = gamePlayedService.selectFootballGamePlayedByGroup(id);
		return ResponseUtils.jsonView(list);
	}

}
