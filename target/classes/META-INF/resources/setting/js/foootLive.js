//获取日期
/*function GetDateStr(AddDayCount) {
 let dd = new Date();
 dd.setDate(dd.getDate() + AddDayCount);//获取AddDayCount天后的日期
 let y = dd.getFullYear();
 let m = dd.getMonth() + 1;//获取当前月份的日期
 let d = dd.getDate();
 return y + "-" + m + "-" + d;
 }
 for (let i = 0; i > -9; i--) {
 $("#dateSelect").append("<option value='" + GetDateStr(i) + "'>" + GetDateStr(i) + "</option>");
 }*/

//文本框禁用空格
function keyUp() {
	if(event.keyCode == 32) {
		event.returnValue = false;
	}
}

//数据交互
let app = new Vue({
	el: '.main',
	data: {
		datas: [], //记录列表
		pageNum: 50, //默认每页5条
		total: '', //信息总条数

		odd: [], //记录原始数据用做修改比分

		win: '', //赔率详情
		//更改比分
		matchId: '',
		letballCourtScore: '',
		letballNumber: '',
		courtScore: '',
		caiGuo: [],

		// prevData: '',//搜索记录
		league: '', //主队名或者客队名
		matchStatus: '', //开奖结果

		no_end: '', //监听未结束时间
		store_matchId: '',
		store_league: '',

		//查看投注记录
		bet_total: '',
		bet_pageNum: 50,
		bet_datas: [],
		bet_matchId: '',
	},
	created: function() {
		this.$nextTick(function() {
			this.getlayer();
			this.get_time(); //加载开始/结束日期控件
		})
	},

	methods: {
		//加载layer
		getlayer() {
			layui.use('layer', function() {
				let layer = layui.layer;
			});
		},
		//加载选择开始/结束日期控件
		get_time: function() {
			//	日期设置
			laydate.render({
				elem: '#startDate', //指定元素
				format: 'yyyy-MM-dd ',
				type: 'datetime',
			});
			laydate.render({
				elem: '#endDate', //指定元素,
				format: 'yyyy-MM-dd ',
				type: 'datetime',
			})
		},
		//利用ajax来查询记录列表
		getdatas: function(num, flag) {
			let start = $('#startDate').val();
			let end = $('#endDate').val();
			if(flag) {
				this.matchId = this.store_matchId;
				this.league = this.store_league;
			}
			let data = {
				pageIndex: num,
				pageNum: parseInt(this.pageNum),
				pageSize: 10,
				startTime: start,
				endTime: end,
				league: this.league.trim(), //主队名或者客队名
				matchStatus: this.matchStatus, //开奖结果
				hour: this.no_end,
				matchId: this.matchId.trim() //赛事编号
			};
			// if (_this.prevData != "") {
			//     _this.prevData.pageIndex = num;
			//     datass = _this.prevData;
			// }
			let _this = this;
			////console.log(data);
			let obj = {
				type: 'post',
				data: data,
				dataType: 'json',
				url: '/football/selectDay',
				success: function(data) {
					////console.log(data);
					if(data.code == 200) {
						if(data.body.list.length != 0) {
							//取到后台传递多来的Body下面的List
							_this.datas = data.body.list;
							_this.total = data.body.total;
							for(let i = 0; i < _this.datas.length; i++) {
								if(_this.datas[i].courtScore) {
									_this.datas[i].aa = _this.datas[i].courtScore.split(':')[0];
									_this.datas[i].bb = _this.datas[i].courtScore.split(':')[1];
								} else {
									_this.datas[i].aa = '';
									_this.datas[i].bb = '';
								}
								if(_this.datas[i].halfCourtScore) {
									_this.datas[i].cc = _this.datas[i].halfCourtScore.split(':')[0];
									_this.datas[i].dd = _this.datas[i].halfCourtScore.split(':')[1];
								} else {
									_this.datas[i].cc = '';
									_this.datas[i].dd = '';
								}
							}
							let brr = JSON.stringify(_this.datas);
							_this.odd = JSON.parse(brr);
							//判断彩果
							for(let j = 0; j < _this.datas.length; j++) {
								if(_this.datas[j].courtScore != null) {
									let arr = _this.datas[j].courtScore.split(":");
									if(arr[0] > arr[1]) {
										_this.caiGuo[j] = "胜"
									} else if(arr[1] > arr[0]) {
										_this.caiGuo[j] = "负"
									} else {
										_this.caiGuo[j] = "平"
									}
								}
							}
							$('#fenye').jqPaginator('option', {
								totalPages: data.body.pages, //返回总页数
								currentPage: 1 //当前页
							});
						} else {
							_this.datas = data.body.list;
							_this.total = data.body.total;
							$('#fenye').jqPaginator('option', {
								totalPages: 1, //返回总页数
								currentPage: 1 //当前页
							});
						}
					}
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(obj);
		},
		//点击查询
		search: function() {
			this.no_end = '';
			this.getdatas(1, true);
		},
		//查看赔率详情
		clickSele: function(matchId) {
			let _this = this;
			let selectTwo = {
				type: 'get',
				data: {
					'id': matchId
				},
				dataType: 'json',
				url: '/oddsinfo/selectByMatchId',
				success: function(data) {
					if(data.code == 200) {
						//取到后台传递多来的Body下面的数据
						_this.win = data.body.footballOddsInfo;
						layer.open({
							title: '查看赔率详情',
							type: 1,
							content: $('.analyMoreTable'),
							area: ['95%', '95%'],
						})
					} else {
						layer.msg('暂无数据！');
					}
					////console.log(data);
				},
				error: function(msg) {
					//console.log(msg);
				}
			};

			base.sendRequest(selectTwo);
		},
		//更改比分
		changeFoot: function(matchId, letballNumber, aa, bb, cc, dd, index) {
			if(letballNumber === '' || aa === '' || bb === '' || cc === '' || dd === '') {
				layer.msg('输入框不能为空');
				return;
			}
			let data = {
				matchId: matchId,
				letballNumber: letballNumber,
				courtScore: aa + ':' + bb,
				halfCourtScore: cc + ':' + dd,
			};
			////console.log(data);
			let obj = {
				type: "post",
				url: '/football/updateMatchInfoData',
				dataType: "json",
				data: data,
				success: function(data) {
					////console.log(data);
					layer.msg(data.msg);
					setTimeout(function() {
						window.location.reload();
					}, 500)
				},
				error: function(msg) {
					//console.log(msg)
				}
			};
			base.sendRequest(obj);
			// let letballInputVal = $("[name=letball]").val();
			// letballInputVal = letballInputVal.replace(/^(-|\+)?\d+$/, "");
			// let halfInputVal = $("[name=half]").val();
			// halfInputVal = halfInputVal.replace(/^\d+:+\d$/, "");
			// let courtInputVal = $("[name=court]").val();
			// courtInputVal = courtInputVal.replace(/^\d+:+\d$/, "");
			// let _this = this;
			// if (!letballInputVal && !halfInputVal && !courtInputVal) {
			//     let data = {
			//         'matchId': matchId,
			//         'halfCourtScore': halfCourtScore,
			//         'letballNumber': letballNumber,
			//         'courtScore': courtScore,
			//     };
			//     ////console.log(data);
			//     let options = {
			//         type: "post",
			//         url: '/football/updateMatchInfoData',
			//         dataType: "json",
			//         data: data,
			//         success: function (data) {
			//             ////console.log(data);
			//             layer.msg(data.msg);
			//             _this.getdatas(1);
			//         },
			//         error: function (msg) {
			//             //console.log(msg)
			//         }
			//     };
			//     base.sendRequest(options);
			// }

		},
		/*        //我也不知道是什么
		        openManage: function (id, judge) {
		            var _this = this;
		            var options = {
		                type: "get",
		                url: "/call/openmanage",
		                data: {"id": id, "judge": judge, type:"2"},
		                dataType: 'json',
		                success: function (data) {
		                    var msg = data.msg;
		                    //弹出信息
		                    layer.open({
		                        title: '什么鬼'
		                        , content: msg

		                    });
		                    _this.getdatas(1);
		                },
		                error: function (msg) {
		                    //console.log(msg);
		                }
		            };
		            base.sendRequest(options);
		        },*/
		//更改赔率详情
		changeFootWin: function(id, winWin, winDraw, winLose, drawWin, drawDraw, drawLose, loseWin, drawLose, loseLose, win10, win20, win21, win30, win31, win32, win40, win41, win42, win50, win51, win52, winOther, draw00, draw11, draw22, draw33, drawOther, lose01, lose02, lose12, lose03, lose13, lose23, lose04, lose14, lose24, lose05, lose15, lose25, loseOther, totalGoal0, totalGoal1, totalGoal2, totalGoal3, totalGoal4, totalGoal5, totalGoal6, moreThan7) {
			let data = {
				'id': id,
				'winWin': winWin,
				'winDraw': winDraw,
				'winLose': winLose,
				'drawWin': drawWin,
				'drawDraw': drawDraw,
				'drawLose': drawLose,
				'loseWin': loseWin,
				'loseLose': loseLose,
				'win10': win10,
				'win20': win20,
				'win21': win21,
				'win30': win30,
				'win31': win31,
				'win32': win32,
				'win40': win40,
				'win41': win41,
				'win42': win42,
				'win50': win50,
				'win51': win51,
				'win52': win52,
				'winOther': winOther,
				'draw00': draw00,
				'draw11': draw11,
				'draw22': draw22,
				'draw33': draw33,
				'drawOther': drawOther,
				'lose01': lose01,
				'lose02': lose02,
				'lose12': lose12,
				'lose03': lose03,
				'lose13': lose13,
				'lose23': lose23,
				'lose04': lose04,
				'lose14': lose14,
				'lose24': lose24,
				'lose05': lose05,
				'lose15': lose15,
				'lose25': lose25,
				'loseOther': loseOther,
				'totalGoal0': totalGoal0,
				'totalGoal1': totalGoal1,
				'totalGoal2': totalGoal2,
				'totalGoal3': totalGoal3,
				'totalGoal4': totalGoal4,
				'totalGoal5': totalGoal5,
				'totalGoal6': totalGoal6,
				'moreThan7': moreThan7
			};
			//console.log(data);
			// //console.log(winWin);
			////console.log(winOther);
			let options = {
				type: "post",
				url: '/oddsinfo/updatePeiLv',
				dataType: "json",
				data: data,
				success: function(data) {
					//console.log(data);
					layer.msg(data.msg);
				},
				error: function(msg) {
					//console.log(msg)
				}
			};
			base.sendRequest(options);
		},
		getBetting: function(id) {
			let _this = this;
			_this.bet_matchId = id;
			layui.use('layer', function() {
				let layer = layui.layer;
				layer.open({
					title: '查看投注纪录',
					type: 1,
					content: $('.betting'),
					area: ['80%', '80%'],
				})
			});
			_this.selectMess(1);
//			setPage();
		},
		//查看投注纪录
		selectMess: function(num) {
			//      	matchId = localStorage.matchId
			let _this = this;
			if(!_this.bet_matchId){
				return;
			}
			let data = {
				pageIndex: num,
				pageNum: parseInt(this.bet_pageNum),
				pageSize: 5,

				matchId: _this.bet_matchId,

			};
			//console.log(data);
			let options = {
				type: "get",
				url: "/bets/queryBettingOrderList",
				data: data,
				dataType: 'json',
				success: function(data) {
					//console.log(data);
					if(data.code == 200) {
						_this.bet_datas = data.body.list;
						_this.bet_total = data.body.total;
						//分页的(右边点击)
						if(data.body.list.length > 0) {
							$('#fenye1').jqPaginator('option', {
								totalPages: data.body.pages
							});
						}else{
							$('#fenye1').jqPaginator('option', {
								totalPages: 1,
								currentPage: 1
							});
						}
					}

				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(options);
		},
		//开奖回滚
		manualWorkBtn: function() {
			let _this = this;
			layer.open({
				title: ['提示'],
				content: '<div>确认要手动开奖吗？</div>',
				area: '20%',         
				btn: ['确认', '取消'],
				shadeClose: true,
				//回调函数  
				yes: function(index, layero) {
					_this.manualWork();
					layer.close(index);
				},
				btn2: function(index, layero) {
					layer.close(index);
				},
				cancel: function(index, layero) { //按右上角“X”按钮  
					layer.close(index);
				},

			});
		},
		//手工开奖操作
		manualWork: function() {
			let _this = this;
			let options = {
				type: "post",
				url: "/bets/prizeOperation",
				data: {
					"matchId": _this.bet_matchId,
					"matchType":"MatchRecord"
				},
				dataType: 'json',
				success: function(data) {
					let msg = data.msg;
					//弹出信息
					layer.open({
						title: '开奖处理结果',
						content: msg,
						area: '20%',         
						yes: function(index) {
							layer.close(index);
							setTimeout(function() {
								window.location.reload();
							}, 500)
						}

					});
					//_this.getdatas(1);
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(options);
		},
		//开奖回滚
		rollBackBtn: function() {
			let _this = this;
			layer.open({
				title: ['提示'],
				content: '<div>确认要开奖回滚吗？</div>',
				area: '20%',         
				btn: ['确认', '取消'],
				shadeClose: true,
				//回调函数  
				yes: function(index, layero) {
					_this.rollBack();
					layer.close(index);
				},
				btn2: function(index, layero) {
					layer.close(index);
				},
				cancel: function(index, layero) { //按右上角“X”按钮  
					layer.close(index);
				},

			});
		},
		rollBack: function() {
			let _this = this;
			let options = {
				type: "post",
				url: "/bets/prizeRollbackOperation",
				data: {
					"matchId": _this.bet_matchId,
				},
				dataType: 'json',
				success: function(data) {
					layer.msg(data.msg)
					setTimeout(function() {
						window.location.reload();
					}, 500)
				},
				error: function(msg) {
					layer.msg("开奖回滚失败");
					//console.log(msg);
				}
			};
			base.sendRequest(options);
		},

	},
	watch: {
		//监听页码每页条数
		pageNum: function() {
			this.getdatas(1);
		},
		//未结束的时间
		no_end() {
			if(this.no_end != '') {
				$('#startDate').val('');
				$('#endDate').val('');
				this.matchStatus = '';
				this.league = '';
				this.getdatas(1);
			} else {
				$('#startDate').val('');
				$('#endDate').val('');
				this.matchStatus = '';
				this.league = '';
				this.no_end = '';
				this.getdatas(1);
			}
			//let _this = this;
			// if (newValue != '') {
			//     let data = {
			//         pageIndex: 1,
			//         pageNum: parseInt(this.pageNum),
			//         pageSize: 10,
			//         hour:newValue
			//     };
			//     //console.log(data);
			//     let obj = {
			//         type: 'post',
			//         data: data,
			//         dataType: 'json',
			//         url: '/football/selectDay',
			//         success(data){
			//             //console.log(data);
			//             if(data.code==200){
			//                 _this.datas = data.body.list;
			//                 _this.total = data.body.total;
			//                 $('#fenye').jqPaginator('option', {
			//                     totalPages: data.body.pages,    //返回总页数
			//                     currentPage: 1                  //当前页
			//                 });
			//             }
			//         },
			//         error(msg){
			//             //console.log(msg);
			//         }
			//     };
			//     base.sendRequest(obj);
			// }
		}
	}
});
//加载分页功能
$.jqPaginator('#fenye', {
	totalPages: 1, //多少页数据
	visiblePages: 10, //最多显示几页
	currentPage: 1, //当前页
	wrapper: '<ul class="pagination"></ul>',
	first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',
	prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
	next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
	last: '<li class="last"><a href="javascript:void(0);">尾页</a></li>',
	page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',

	onPageChange: function(num, type) {
		app.getdatas(num);
	}
});
	$.jqPaginator('#fenye1', {
		totalPages: 1, //多少页数据
		visiblePages: 10, //最多显示几页
		currentPage: 1, //当前页
		wrapper: '<ul class="pagination"></ul>',
		first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',
		prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
		next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
		last: '<li class="last"><a href="javascript:void(0);">尾页</a></li>',
		page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
	
		onPageChange: function(num, type) {
			app.selectMess(num);
		}
	});	
