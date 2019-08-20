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
		datas: [], //列表信息
		typelist: [], //彩种
		selectTypelist:[],//当前选择彩种，用于开奖预设数据设置
		selectNumlist:[],//存储设置好的开奖号码
		games: [], //期号
		pageNum: 10, //默认每页5条
		total: 0, //信息总条数
		matchStatus: '', //开奖结果
		store_matchId: '',
		store_league: '',
		ischeck: '',
		ischeck1: '',

		matchId: '',
		typeId: '',

		//查看投注记录
		bet_total: '',
		bet_pageNum: 10,
		bet_datas: [],
		bet_matchId: '',

		luckNum: '',
		issue: '',

		isUpDataSelectNumList:0,

		isFrise:"",

		szcNumber:"",//数字彩号码
		szcNumber_tips:"",//数字彩号码提示


		newIssueList:[],//生成的最新期数20
		isShowIssue :"",

		isShowSzcNumber:"",
		ranSzcList:[],
	},
	created: function() {
		this.$nextTick(function() {
			this.now();
			this.getlayer();
			this.get_time();
			this.getPlayType();
			// this.ischeck = '';
		})
	},

	methods: {
		// 加载layer
		getlayer() {
			layui.use('layer', function() {
				let layer = layui.layer;
			})
		},
		
		//利用ajax来查询记录列表
		getdatas: function(num1) {
			let _this = this;
			let start = $('#startDate').val();
			let end = $('#endDate').val();
			let typeid = $("#typelist").find("option:selected").val();
			let dateSelect = $("#dateSelect").val().trim('');
			let status = $("#status").find("option:selected").val();
			if(!this.isFrise){
				var dateTime = new Date();
				dateTime.setTime(dateTime.getTime());
				var s2 = dateTime.getFullYear() + "-" + this.getzf(dateTime.getMonth() + 1) + "-" + this.getzf(dateTime.getDate());
				start=s2 + " " + "00:00:00";
				end=s2 + " " + "23:59:59";
				typeid=_this.typelist[0].gameID;
				this.isFrise="1";
			}
			let datass = {
				pageIndex: num1,
				pageNum: parseInt(this.pageNum),
				pageSize: 10,
				//          	matchStatus: _this.matchStatus,
				startTime: start,
				endTime: end,
				typeId: typeid,
				actionIssue: dateSelect,
				status: status,

			};
			let obj = {
				type: 'post',
				data: datass,
				dataType: 'json',
				url: '/systemColor/querySystemColorNum',
				success: function(data) {
					if(data.code == 200) {
						if(data.body.list.length !== 0) {
							_this.datas = data.body.list;
							_this.total = data.body.total;
							$('#fenye').jqPaginator('option', {
								totalPages: data.body.pages,
								currentPage: 1
							});
						} else {
							$('#fenye').jqPaginator('option', {
								totalPages: 1,
								currentPage: 1
							});
						}
					} else {
						_this.datas = [];
						$('#fenye').jqPaginator('option', {
								totalPages: 1,
								currentPage: 1
							});

					}

				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(obj);
		},

		//获取彩种
		getPlayType: function() {
			let _this = this;
			let obj = {
				type: 'post',
				data: {},
				dataType: 'json',
				url: '/systemColor/getSystemColorType',
				success: function(data) {
					if(data.code==200&&data.body.length!=0){
						
						_this.typelist = data.body;

						_this.ischeck = _this.typelist[0].gameID;
						//---改
						_this.setSelectNumlist(1)


						if(!_this.isFrise){
							_this.getdatas(1);
						}
						
					}
					
					//      			_this.games = data.body.issueList;
				},
				error:function(){
					_this.getdatas(1);
				}

			}

			base.sendRequest(obj);
		},
		//设置当前选择的玩法类型
		setSelectNumlist:function(index){
			var _this=this,html="";
			_this.selectTypelist = _this.typelist[index];
			_this.selectNumlist=[];

			if (_this.selectTypelist.lucknum_length){
				for (var i = 0; i < _this.selectTypelist.lucknum_length;i++){
					_this.selectNumlist.push("");
					if(_this.selectTypelist.is_repeat != 1){
						html+=i+",";
					}else{
						html+="3,";
					}
					
				}
			}
			//提示设置
			_this.szcNumber_tips="例:"+html.substring(0,html.length-1);
		},
		//检查
		checkSelectNumList:function(){
			if(!this.szcNumber){
				return ""
			}
			var _this = this,
				 // list = _this.selectNumlist.join(",").split(","),
				list=_this.szcNumber.trim().split(","),
				repeatList=[];
			if (!_this.issue){
				layer.msg("期号未设置!")
				return 1
			}
			//判断长度
			if(list.length!=_this.selectTypelist.lucknum_length){
				layer.msg("开奖号码格式不正确!");
				return 1
			}
			for(var i in list){
				list[i]=list[i].replace(".","");
				//数字类型校验
				if (isNaN(list[i]) || !list[i]){
					layer.msg("开奖号码格式不正确!");
					return 1
				}
				//数字范围校验
				if (list[i] < _this.selectTypelist.min_num){
					layer.msg("开奖号码数字不能小于" + _this.selectTypelist.min_num+"!")
					return 1
				} else if (list[i] > _this.selectTypelist.max_num){
					layer.msg("开奖号码数字不能大于" + _this.selectTypelist.max_num + "!")
					return 1
				}	
				//数字是否允许重复的校验
				if (_this.selectTypelist.is_repeat != 1&&repeatList.indexOf(list[i]) !== -1 ){
					layer.msg("开奖号码设置了重复数字!")
					return 1
				}
				repeatList.push(list[i]);
			}
			//补零
			if (_this.selectTypelist.is_add_zero==1){
				for (var i in list){
					if (list[i] &&parseInt(list[i])<10){
						list[i] = "0" + parseInt(list[i])+"";
					}else{
						list[i] =parseInt(list[i]) + "";
					}
				}
			}else{
				for (var i in list) {
					list[i] =parseInt(list[i]);
				}
			}
			

			if (_this.selectTypelist.luck_number.indexOf('+') !== -1){
				var html="";
				for (var i in list) {
					if(i<list.length-2){
						html+=list[i]+",";
					} else if (i == list.length - 2){
						html += list[i] + "+";
					}else{
						html += list[i];
					}
				}
				
				return html
			}else{
				return list.join(",")
			}

		},
		//获取期号
		//       getGames:function(){
		//      	let _this= this;
		//      	let type = $("#typelist").find("option:selected").val();
		//      	let obj = {
		//      		type: 'post',
		//      		data: {
		//      			typeId:type
		//      		},
		//      		dataType: 'json',
		//      		url:'/maint/getDigitalLotteryIssueList',
		//      		success: function(data){
		////      			//console.log(data);
		//      			_this.games = data.body;
		//      		}
		//      	
		//      	}
		//      	
		//       base.sendRequest(obj); 	
		//      },
		//点击今日执行
        now: function () {
            $('.search a').removeClass('b_red');
            $('#now').addClass('b_red');
            var dateTime = new Date();
            dateTime.setTime(dateTime.getTime());
            var s2 = dateTime.getFullYear() + "-" + this.getzf(dateTime.getMonth() + 1) + "-" + this.getzf(dateTime.getDate());
            $("#startDate").val(s2 + " " + "00:00:00");
            $("#endDate").val(s2 + " " + "23:59:59");
        },
        //点击昨日执行
        yes: function () {
            $('.search a').removeClass('b_red');
            $('#yes').addClass('b_red');
            var dateTime = new Date();
            dateTime.setTime(dateTime.getTime() - 24 * 60 * 60 * 1000);
            var s1 = dateTime.getFullYear() + "-" + this.getzf(dateTime.getMonth() + 1) + "-" + this.getzf(dateTime.getDate());
            $("#startDate").val(s1 + " " + "00:00:00");
            $("#endDate").val(s1 + " " + "23:59:59");
        },
        //点击本周执行
        week: function () {
            $('.search a').removeClass('b_red');
            $('#week').addClass('b_red');
            var dateTime = new Date(),
                st = this.getDateTime(0),
                et = dateTime.getFullYear() + "-" + this.getzf(dateTime.getMonth() + 1) + "-" + this.getzf(dateTime.getDate());
            $("#startDate").val(st + " " + "00:00:00");
            $("#endDate").val(et + " " + "23:59:59");
        },
        //点击上周执行
        lastWeek: function () {
            $('.search a').removeClass('b_red');
            $('#lastWeek').addClass('b_red');
            var st = this.getDateTime(2),
                et = this.getDateTime(3);
            $("#startDate").val(st + " " + "00:00:00");
            $("#endDate").val(et + " " + "23:59:59");
        },
        //点击本月执行
        month: function () {
            $('.search a').removeClass('b_red');
            $('#month').addClass('b_red');
            var dateTime = new Date(),
                st = this.getDateTime(4),
                et = dateTime.getFullYear() + "-" + this.getzf(dateTime.getMonth() + 1) + "-" + this.getzf(dateTime.getDate());
            $("#startDate").val(st + " " + "00:00:00");
            $("#endDate").val(et + " " + "23:59:59");
        },
        //点击上月执行
        lastMonth: function () {
            $('.search a').removeClass('b_red');
            $('#lastMonth').addClass('b_red');
            var st = this.getDateTime(6);
            var et = this.getDateTime(7);
            $("#startDate").val(st + " " + "00:00:00");
            $("#endDate").val(et + " " + "23:59:59");
        },
        //日期设置
        getDateTime: function (index) {
            var now = new Date(); //当前日期
            var nowDayOfWeek = now.getDay()||7; //今天本周的第几天
            var nowDay = now.getDate(); //当前日
            var nowMonth = now.getMonth(); //当前月
            var nowYear = now.getYear(); //当前年
            nowYear += (nowYear < 2000) ? 1900 : 0; //
            var lastMonthDate = new Date(); //上月日期
            lastMonthDate.setDate(1);
            lastMonthDate.setMonth(lastMonthDate.getMonth() - 1);
            var lastYear = lastMonthDate.getFullYear();
            var lastMonth = lastMonthDate.getMonth();

            //格式化日期：yyyy-MM-dd
            function formatDate(date) {
                var myyear = date.getFullYear();
                var mymonth = date.getMonth() + 1;
                var myweekday = date.getDate();
                if (mymonth < 10) {
                    mymonth = "0" + mymonth;
                }
                if (myweekday < 10) {
                    myweekday = "0" + myweekday;
                }
                return (myyear + "-" + mymonth + "-" + myweekday);
            }

            //获得某月的天数
            function getMonthDays(myMonth) {
                var monthStartDate = new Date(nowYear, myMonth, 1);
                var monthEndDate = new Date(nowYear, myMonth + 1, 1);
                var days = (monthEndDate - monthStartDate) / (1000 * 60 * 60 * 24);
                return days;
            }

            //获得本季度的开始月份
            function getQuarterStartMonth() {
                var quarterStartMonth = 0;
                if (nowMonth < 3) {
                    quarterStartMonth = 0;
                }
                if (2 < nowMonth && nowMonth < 6) {
                    quarterStartMonth = 3;
                }
                if (5 < nowMonth && nowMonth < 9) {
                    quarterStartMonth = 6;
                }
                if (nowMonth > 8) {
                    quarterStartMonth = 9;
                }
                return quarterStartMonth;
            }

            //获得本周的开始日期
            function getWeekStartDate() {
                ////console.log(nowYear, nowMonth, nowDay - nowDayOfWeek);
                var weekStartDate = new Date(nowYear, nowMonth, nowDay + 1 - nowDayOfWeek);
                return formatDate(weekStartDate);
            }

            //获得本周的结束日期
            function getWeekEndDate() {
                var weekEndDate = new Date(nowYear, nowMonth, nowDay + 1 + (6 - nowDayOfWeek));
                return formatDate(weekEndDate);
            }

            //获得上周的开始日期
            function getLastWeekStartDate() {
                var weekStartDate = new Date(nowYear, nowMonth, nowDay + 1 - nowDayOfWeek - 7);
                return formatDate(weekStartDate);
            }

            //获得上周的结束日期
            function getLastWeekEndDate() {
                var weekEndDate = new Date(nowYear, nowMonth, nowDay + 1 - nowDayOfWeek - 1);
                return formatDate(weekEndDate);
            }

            //获得本月的开始日期
            function getMonthStartDate() {
                var monthStartDate = new Date(nowYear, nowMonth, 1);
                return formatDate(monthStartDate);
            }

            //获得本月的结束日期
            function getMonthEndDate() {
                var monthEndDate = new Date(nowYear, nowMonth, getMonthDays(nowMonth));
                return formatDate(monthEndDate);
            }

            //获得上月开始时间
            function getLastMonthStartDate() {
                var lastMonthStartDate = new Date(lastYear, lastMonth, 1);
                return formatDate(lastMonthStartDate);
            }

            //获得上月结束时间
            function getLastMonthEndDate() {
                var lastMonthEndDate = new Date(lastYear, lastMonth, getMonthDays(lastMonth));
                return formatDate(lastMonthEndDate);
            }

            if (index == 0) {
                var k = getWeekStartDate();
                return k
            } else if (index == 1) {
                var k = getWeekEndDate();
                return k
            } else if (index == 2) {
                var k = getLastWeekStartDate();
                return k
            } else if (index == 3) {
                var k = getLastWeekEndDate();
                return k
            } else if (index == 4) {
                var k = getMonthStartDate();
                return k
            } else if (index == 5) {
                var k = getMonthEndDate();
                return k
            } else if (index == 6) {
                var k = getLastMonthStartDate();
                return k
            } else if (index == 7) {
                var k = getLastMonthEndDate();
                return k
            }
        },
        //补0
        getzf: function (num) {
            if (parseInt(num) < 10) {
                num = '0' + num;
            }
            return num;
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

		//点击查询
		search: function() {
			this.no_end = '';
			this.getdatas(1);
		},

		//查看投注纪录
		selectMess: function(num) {
			//      	matchId = localStorage.matchId
			//			let type = $("#typelist").find("option:selected").val();
			let actionIssue = this.actionIssue;
			let _this = this;
			let data = {
				pageIndex: num,
				pageNum: parseInt(this.bet_pageNum),
				pageSize: 5,
				gameType: _this.typeId,
				actionIssue: _this.actionIssue,

			};

			let options = {
				type: "get",
				url: "/bets/queryBettingOrderList",
				data: data,
				dataType: 'json',
				success: function(data) {
					if(data.code == 200) {
						_this.bet_datas = data.body.list;
						_this.bet_total = data.body.total;
						//分页的(右边点击)
						if(data.body.list.length > 0) {
							$('#fenye1').jqPaginator('option', {
								totalPages: data.body.pages
							});
						} else {
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
		//设置
		setSzcNumber:function(str){
			this.szcNumber=str;
			this.showSzcNumber("");
			this.isClick=true;
		},
		//切换显示开奖号码
		showSzcNumber:function(index){
			this.isShowSzcNumber=index;
			this.isShowIssue="";
		},
		//生成随机开奖号码
		randow_szc:function(type,val){
			this.ranSzcList=[];
			var html="",list=[],item="",
			headStr="",
			max=this.selectTypelist.max_num,
			min=this.selectTypelist.min_num,
			maxLen=this.selectTypelist.lucknum_length,
			isAdd=this.selectTypelist.luck_number.indexOf("+")!=-1?1:'';
			if(type==1){//单个数字
				for(var j=0;j<5;j++ ){
					list=[];
					var html=val+",";
					list.push(val);
					if (this.selectTypelist.is_repeat != 1){
						for(var i=0;i<maxLen-1;i++ ){
							item=parseInt(Math.random()*(max-min+1)+min)
							if(list.indexOf(item)!=-1){
								i--;
							}else{
								if(isAdd&&i==(maxLen-3)){
									html+=item+"+";
									list.push(item);
								}else{
									html+=item+",";
									list.push(item);
								}
							}
						}
					}else{
						for(var i=0;i<maxLen-1;i++ ){
							html+=parseInt(Math.random()*(max-min+1)+min)+",";
						}
					}
					html=html.substring(0,html.length-1);
					this.ranSzcList.push(html);
				}
				
			}else{//一个数组
				var list=val.split(","),listLen=list.length;
				if(list[listLen-1]==""){
					listLen-=1;
				}
				for(var i=0;i<listLen;i++){
					headStr+=list[i]+",";
					
				}
				for(var j=0;j<5;j++ ){
					var html=headStr;
					list=headStr.substring(0,headStr.length-1).split(",");
					if (this.selectTypelist.is_repeat != 1){
						for(var i=0;i<maxLen-listLen;i++ ){
							item=parseInt(Math.random()*(max-min+1)+min)
							if(list.indexOf(item)!=-1){
								i--;
							}else{
								if(isAdd&&i==(maxLen-3)){
									html+=item+",";
									list.push(item);
								}else{
									html+=item+",";
									list.push(item);
								}
							}
						}
					}else{
						for(var i=0;i<maxLen-listLen;i++ ){
							
							if(isAdd&&i==(maxLen-3)){
									html+=parseInt(Math.random()*(max-min+1)+min)+"+";
								}else{
									html+=parseInt(Math.random()*(max-min+1)+min)+",";
								}
						}
					}
					html=html.substring(0,html.length-1);
					this.ranSzcList.push(html);
				}

			}
			this.showSzcNumber(1);
		},
		setShowIssue:function(item){
			this.issue=item;
			this.showIssue("");
		},
		//切换显示期号
		showIssue:function(index){
			this.isShowIssue=index;
			this.isShowSzcNumber="";
		},
		//生成最新的20期
		getNewIssue:function(oneTypeId){
			let _this=this;
			let options = {
				type: "get",
				url: "/systemColor/getTopTwentyNewIssues",
				data: {
					"oneTypeId":oneTypeId,
					"num" :20,
				},
				dataType: 'json',
				success: function(data) {
					if(data.code == 200) {
						_this.newIssueList=data.body;
					}
	
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(options);
		},
		
		//新增
		insert: function() {
			let _this = this;
			//      	_this.getPlayType();
			_this.issue = '';
			_this.luckNum = '';
			_this.ischeck1 = this.typelist[0].gameID;
			_this.isUpDataSelectNumList = 0;
			$('.addText').removeAttr("disabled");
			if (_this.selectNumlist){
				for (var i in _this.selectNumlist) {
					_this.$set(_this.selectNumlist, i, "");
				}
			}
			_this.szcNumber="";
			
			_this.getNewIssue(_this.ischeck1);
			layui.use('layer', function() {
				let layer = layui.layer;
				layer.open({
					title: '预设开奖号码',
					type: 1,
					content: $('.addColor'),
					btn: ['确定', '取消'],
					area: ['50%', '70%'],
					yes: function(index) {
						var luckNum=_this.checkSelectNumList();
						if (luckNum==1){
							return 
						}else{
							var times=0,
							obj = {
								type: 'post',
								data: {
									oneTypeId: _this.ischeck1,
									issue: _this.issue,
									luckNumber: luckNum
								},
								dataType: 'json',
								url: '/systemColor/preinstallLuckNum',
								success: function (data) {
									if(data.code==200){
										for (var i in _this.selectNumlist){
											_this.$set(_this.selectNumlist,i,"");
										}
										_this.szcNumber="";
										if (times==0){
											times++;
											layer.open({
												title: '信息',
												type: 1,
												btn: ['确定', '取消'],
												content: "<div style='padding: 10px;text-indent: 17px;font-size: 14px;'>"+data.msg+"</div>",
												area: '40%',
												yes: function (indexs) {
													layer.close(indexs);
													obj.data.sureFlag = 1;
													base.sendRequest(obj);
												},
												btn2: function (indexs) {
													layer.close(indexs);
												}
											})
										}else{
											setTimeout(function () {
												layer.close(index);
												layer.msg(data.msg);
											}, 500)
										}
								
									}else{
										layer.msg(data.msg);
										
									}
									
									
								}

							}
							base.sendRequest(obj);
							
							
						}
						

						
					},
					btn2: function() {
						layer.closeAll()
					}
				})
			});
			//console.log(_this.typelist)
		},
		update: function(typeId,issue,luckNum) {
			let _this = this;
			//      	_this.getPlayType();
			_this.issue = issue;
		
			_this.ischeck1 = typeId;
			luckNum = luckNum.replace("+",",").split(",");
			_this.luckNum = luckNum;
			_this.selectNumlist = luckNum;
			_this.isUpDataSelectNumList=1;

			_this.szcNumber="";
			// if (_this.selectNumlist) {
			// 	for (var i in _this.selectNumlist) {
			// 		_this.$set(_this.selectNumlist, i, luckNum[i]);
			// 	}
			// }
			$('.addText').attr("disabled",true);
			layui.use('layer', function() {
				let layer = layui.layer;
				layer.open({
					title: '修改开奖号码',
					type: 1,
					content: $('.addColor'),
					btn: ['确定', '取消'],
					area: ['50%', '50%'],
					yes: function(index) {
						var luckNum = _this.checkSelectNumList();
						if (luckNum == 1) {
							return
						} else {
							let obj = {
								type: 'post',
								data: {
									oneTypeId: _this.ischeck1,
									issue: _this.issue,
									luckNumber: luckNum
								},
								dataType: 'json',
								url: '/systemColor/updateSetLuckNum',
								success: function (data) {
									if (data.code == 200) {
										for (var i in _this.selectNumlist) {
											_this.$set(_this.selectNumlist, i, "");
										}
										 setTimeout(function () {
											layer.close(index);
											 layer.msg(data.msg);
										}, 500)
									} else {
										layer.msg(data.msg);

									}
								}

							}

							base.sendRequest(obj);
						}
						
					},
					btn2: function() {
						layer.closeAll()
					}
				})
			});
			//console.log(_this.typelist)
		},

		getBetting: function(typeId, actionIssue,luckNum) {
			let _this = this;
			_this.typeId = typeId;
			_this.actionIssue = actionIssue;
			_this.luckNum = luckNum;
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
				url: "/systemColor/openSetLuckNum",
				data: {
					issue: _this.actionIssue,
					oneTypeId: _this.typeId,
					luckNumber: _this.luckNum,
				},
				dataType: 'json',
				success: function(data) {
					//console.log(data);
					let msg = data.msg;
					//弹出信息
					layer.open({
						title: '开奖处理结果',
						area: '20%',         
						content: msg,
						yes: function(index) {
							layer.close(index);
							layer.msg(data.msg)
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
			//			let typeid = $("#typelist").find("option:selected").val();
			let options = {
				type: "post",
				url: "/bets/prizeRollbackOperation",
				data: {
					actionIssue: _this.actionIssue,
					type: _this.typeId,
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

		//监听彩种类型设置
		ischeck1:function(val){
			// console.log(val);
			var _this=this;
			
			_this.typelist.map(function(item,index){
				if(item.gameID==val){
					_this.setSelectNumlist(index);
				}
			})

			//获取期数
			_this.getNewIssue(_this.ischeck1);
			//清空数据
			_this.szcNumber="";
			_this.isShowSzcNumber="";
			_this.isShowIssue="";
			_this.ranSzcList=[];
			
		},

		selectNumlist:function(val){
			if (this.isChange == 1){
				return
			}
			this.isChange=1;
			var _this = this, list = val.join(",").split(",");
			if (_this.selectTypelist.is_add_zero==1){
				for (var i in list){
					if (list[i] &&parseInt(list[i])<10){
						list[i] = "0" + parseInt(list[i])+"";
					}else{
						list[i] =parseInt(list[i]) + "";
					}
				}
			}else{
				for (var i in list) {
					list[i] =parseInt(list[i]);
				}
			}
			
			setTimeout(function(){
				_this.isChange =0
			},100)
			_this.selectNumlist=list;

		},
		//监听开奖号码
		szcNumber:function(val){
			var _this=this;
			if(val){
				if(this.isClick){
				setTimeout(function(){
					_this.isClick="";
				},300)
				return
				}
				if(!isNaN(parseInt(val))){
					if(val.indexOf(",")!=-1){
						this.randow_szc(2,val);
					}else{
						this.randow_szc(1,parseInt(val));
					}
					
				}
			}
			
			
		},

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
		
		if(app.isFrise){
			app.getdatas(num);
		}else{
			app.getPlayType();
		}
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