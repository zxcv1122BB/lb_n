/**
 * Created by ASUS on 2017/9/29.
 */
let app = new Vue({
	el: "#app",
	data: {
		datas: [], //方案列表
		LotteryTypes: [], // 彩票列表
		userParticulars: [], //用户详情列表
		ordersParticulars: [], //订单详情列表
		ordersTwo: [],

		page_num: 50,

		total: "", //信息总条数
		roleId: localStorage.roleId,
		/*搜索绑定*/
		username: "", //用户名
		status: "", //状态
		outOfThrity: "0", //时间
		startTime: "", //时间开始
		endTime: "", //时间停止
		lowerAmount: "", //最低金额
		higherAmount: "", //最高金额
		lowerBonus: "", //最低奖金
		higherBonus: "", //最高奖金
		gameType: "", //彩票种类
		order: '', //订单id
		id: '',
		userLast: [], //
		actionMes: [], //投注比赛详情
		cal_no: '', //开奖批次
		winDetails: [], //中奖明细
		avg: 0,
		totals: 1,
		northReturnAwardRate: '',
		type: 0,
		
		selectDatas:[],
		tipsLoad:'',
		isClick:false,
	},
	created() {
		this.getlayer();
		this.getOnlyConfigure();
		this.selectAll();
	},
	methods: {
		//加载layer
		getlayer() {
			layui.use("layer", function() {
				let layer = layui.layer;
			});
		},
		//获取系统配置
		getOnlyConfigure: function() {
			var _this = this,
				privacy = {
					type: "get",
					data: {
						configure: 'northReturnAwardRate'
					},
					url: "/sysConfigure/getOnlyConfigure",
					success: function(data) {
						//						//console.log(data);
						if(data.code==200){
							_this.northReturnAwardRate = data.body.sysConfig1;
						}
					}
				};
			base.sendRequest(privacy);
		},
		//获取方案列表
		getdatas: function(num) {
			if(this.isClick){
				return
			}
			let _this = this;
			let data = {
				pageIndex: num,
				pageNum: parseInt(this.page_num),
				pageSize: 5,
				username: _this.username, //用户名
				status: _this.status, //状态
				outOfThrity: _this.outOfThrity, //时间
				startTime: _this.startTime, //时间开始
				endTime: _this.endTime, //时间停止
				lowerAmount: _this.lowerAmount, //最低金额
				higherAmount: _this.higherAmount, //最高金额
				lowerBonus: _this.lowerBonus, //最低奖金
				higherBonus: _this.higherBonus, //最高奖金
				gameType: _this.gameType, //彩票种类
				calNo: _this.cal_no, //开奖批次
				userType:$("#search_userType").find("option:selected").val()
			};
			if(num==1){ //今天
				var dateTime = new Date(),st, et;
					dateTime.setTime(dateTime.getTime());
					st = dateTime.getFullYear() + "-" + this.getzf(dateTime.getMonth() + 1) + "-" + this.getzf(dateTime.getDate());
					et = st;
					data.startTime=st + " " + "00:00:00";
					data.endTime=et + " " + "23:59:59";
			}
			let options = {
				type: "get",
				url: "/bets/queryBettingOrderList",
				data: data,
				dataType: 'json',
				success: function(data) {
					if(_this.tipsLoad){
						layer.closeAll();
						_this.tipsLoad="";
						_this.isClick=false;
					}
					//console.log(data);
					if(data.code == 200) {
						_this.datas = data.body.list;
						_this.total = data.body.total;
						for(var i in _this.datas){
							if(_this.datas[i].isCal==1){
								_this.datas[i].calTime = _this.datas[i].calTime.slice(5,16);
							}
							_this.datas[i].actionTime = _this.datas[i].actionTime.slice(5,16);
						}
						//分页的(右边点击)
						if(_this.datas.length != 0) {
							$(".none").hide();
							$('#fenye').jqPaginator('option', {
								totalPages: data.body.pages,
								currentPage: 1
							});
						} else {
							$('#fenye').jqPaginator('option', {
								totalPages: 1,
								currentPage: 1
							});
							$(".none").show();
						}
						
					}

				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			this.tipsLoad=
			layer.load(1, {
				shade: [0.1,'#fff'] //0.1透明度的白色背景
			});
			setTimeout(function(){
				base.sendRequest(options);
			},101);
			
		},
		clickBtn: function() {
			$('#personDetail').on('click', '.getMoreMsg', function() {
				$('#personDetail .hide').removeClass('hide');
				$(this).addClass('hide');
			});
		},
		selectAll: function() {
			var _this = this;
			var userDetail = {
				type: 'post',
				data: {},
				url: '/bets/getRecentlyBetCensus',
				success: function(data) {
					if(data.code == 200) {
						_this.selectDatas = data.body;
					}
					if(!_this.selectDatas){
						_this.selectDatas.todaysBetSum='';
				    	_this.selectDatas.todaysOpenSum='';
				    	_this.selectDatas.yesterdayBetSum='';
				    	_this.selectDatas.yesterdayOpenSum='';
				    	_this.selectDatas.sevenBetSum='';
				    	_this.selectDatas.sevenOpenSum='';
					}
				}
			};
			base.sendRequest(userDetail);
		},
		detail: function(userName) {
			var _this = this;
			var str = {
				userName: userName,
			};
			var userDetail = {
				type: 'post',
				data: str,
				url: '/memberDeposit/depositUserInfo',
				success: function(data) {
					if(data.code == 200) {
						if(data.body) {
							_this.personDetail(data);
						} else {
							_this.tipsContent("找不到该会员", 1000);
						}
					}

				}
			};
			base.sendRequest(userDetail);
		},
		//会员详情展示
		personDetail: function(list) {
			var html = "";
			if(list.body) {
				var obj = list.body;
				if(obj.userType ==1 || obj.userType==2) {
					obj.userType = '会员'
				} else if(obj.userType == 6) {
					obj.userType = '测试'
				}
				html += '<table class="layui-table"><colgroup><col width="25%"><col width="25%"><col width="25%"><col width="25%"></colgroup><tbody> <td style="text-align: right;">\u767B\u5F55\u5E10\u53F7:</td><td style="text-align: left;">' + (obj.userName ? obj.userName : '-') + '</td><td style="text-align: right;">\u6240\u5C5E\u4E0A\u7EA7:</td><td style="text-align: left;">' + (obj.proxyName ? obj.proxyName : '-') + '</td></tr><tr><td style="text-align: right;">\u4F1A\u5458\u59D3\u540D:</td><td style="text-align: left;">' + (obj.fullName ? obj.fullName : '-') + '</td><td style="text-align: right;">类别:</td><td style="text-align: left;">' + (obj.userType == 1 ? '代理' : obj.userType == 6 ? '测试账号':'会员') + '</td></tr><tr class="odd_row"><td style="text-align: right;">\u4F1A\u5458\u4F59\u989D:</td><td style="text-align: left;">' + (obj.coin ? obj.coin : '-') + '</td><td style="text-align: right;">\u79EF\u5206:</td><td style="text-align: left;">' + (obj.score ? obj.score : '0') + '</td></tr>';
				html += '<tr><td  style="text-align: right;">开元余额:</td><td colspan="3">'+ (obj.kycoin? obj.kycoin : '0')  +'</td></tr>';
				html += '<tr><td style="text-align: right;">\u6253\u7801\u91CF:</td><td style="text-align: left;">' + (obj.betsum ? obj.betsum : '0.00') + '</td><td style="text-align: right;">\u51FA\u6B3E\u6240\u9700\u6253\u7801\u91CF:</td><td style="text-align: left;">' + (obj.betsumNeed ? obj.betsumNeed : '0.00') + '</td></tr><tr class="odd_row"><td style="text-align: right;">\u72B6\u6001:</td><td style="text-align: left;">' + (obj.status ? obj.status == 2 ? '冻结' : '正常' : '-') + '</td><td style="text-align: right;">\u4F1A\u5458\u7B49\u7EA7:</td><td style="text-align: left;">' + (obj.vipName ? obj.vipName : '-') + '</td></tr><tr class="getMoreMsg" rowspan="2" style="background:#666;color:#fff;cursor:pointer"><td colspan="4"><span>\u5C55\u5F00\u663E\u793A\u66F4\u591A<span></td></tr><tr class="hide"><td style="text-align: right;">手机号:</td><td style="text-align: left;">' + (obj.phoneNumber ? obj.phoneNumber : '-') + '</td><td style="text-align: right;">\u5FAE\u4FE1\u53F7:</td><td style="text-align: left;">' + (obj.weixin ? obj.weixin : '-') + '</td></tr><tr class="odd_row hide"><td style="text-align: right;">LINE:</td><td style="text-align: left;">' + (obj.line ? obj.line : '-') + '</td><td style="text-align: right;">\u90AE\u7BB1:</td><td style="text-align: left;">' + (obj.email ? obj.email : '-') + '</td></tr><tr><td style="text-align: right;">银行帐号:</td><td colspan="3" style="text-align: left;"><span>' + (obj.bankAccount ? obj.bankAccount : '-') + '</span><span data-text="' + obj.bankAccount + '" class="copyBtn layui-btn layui-btn-mini">\u590D\u5236</span><br/><span class="layui-btn layui-btn-mini ' + (obj.bankBlacklistStatus == 0 ? 'layui-btn-danger' : '') + '">' + (obj.bankBlacklistStatus ? obj.bankBlacklistStatus == 1 ? '可用' : '禁用' : '') + '</sapn></td></tr><tr class="odd_row"><td style="text-align: right;">银行名称:</td><td style="text-align: left;">' + (obj.bankName ? obj.bankName : '-') + '</td><td style="text-align: right;">开户分行:</td><td style="text-align: left;">' + (obj.bankAddress ? obj.bankAddress : '-') + '</td></tr><tr><td style="text-align: right;">\u6CE8\u518CIP:</td><td style="text-align: left;">' + (obj.regIp ? obj.regIp : '-') + '</td><td style="text-align: right;">\u6CE8\u518C\u6765\u6E90:</td><td style="text-align: left;">' + (obj.channel ? (obj.channel == 1 ? "pc" : (obj.channel == 2 ? "wap" : (obj.channel == 3 ? "app" : " 管理员添加"))) : '-') + '</td></tr><tr class="odd_row"><td style="text-align: right;">\u6CE8\u518C\u7CFB\u7EDF:</td><td style="text-align: left;">' + (obj.regSystem ? obj.regSystem : '-') + '</td><td style="text-align: right;">\u6700\u540E\u767B\u5F55\u65F6\u95F4:</td><td style="text-align: left;">' + (obj.lastLoginTime ? obj.lastLoginTime : '-') + '</td></tr><tr><td style="text-align: right;">\u5907\u6CE8:</td><td colspan="3" style="text-align: left;">' + (obj.info ? obj.info : '-') + '</td></tr> </tbody></table>';
			} else {
				html = '<p>\u627E\u4E0D\u5230\u8BE5\u4F1A\u5458\u4FE1\u606F</p>';
			}

			$('#personDetail').html(html);
			layui.use('layer', function() {
				var layer = layui.layer;
				layer.open({
					type: 1,
					title: '会员详情',
					area: ['50%', '70%'],
					content: $('#personDetail'),
					btn: ['确定'],
					yes: function yes(index, layero) {
						layer.closeAll('page');
					}
				});
			});
		},
		//撤单操作
		cheDanBtn: function(id, isCal, status) {
			let _this = this;
			if(isCal == 1 || isCal == 2 || status == 2) {
				return;
			}
			layer.open({
				title: ['提示'],
				content: '<div>确认撤单吗？</div>',
				area: '20%',
				btn: ['确认撤单', '取消'],
				shadeClose: true,
				//回调函数  
				yes: function(index, layero) {
					_this.cheDan(id);
				},
				btn2: function(index, layero) {
					layer.closeAll();
				},
				cancel: function(index, layero) { //按右上角“X”按钮  
					layer.closeAll();
				},

			});
		},
		cheDan: function(id) {
			let _this = this;
			let data = {
				'betId': id
			};
			let options = {
				type: "post",
				url: "/bets/cancelTheOrder",
				data: data,
				dataType: 'json',
				success: function(data) {
					//console.log(data);
					if(data.code == 200) {
						//console.log("撤单成功");
						window.location.href = "raceColorBetting.html"
					}
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(options);
		},
		//手工开奖操作
		manualWork: function(id, judge, item) {
			let _this = this;
			if(item.isCal == 1 || item.isCal == 2 || item.status == 2) {
				return;
			}
			layer.open({
				title: ['提示'],
				content: '<div>确认要手动开奖吗？</div>',
				area: '20%',
				btn: ['确认', '取消'],
				shadeClose: true,
				//回调函数  
				yes: function(index, layero) {
					let burl = "";
					let bdata = "";
					let btype = "";
					if(item.type > 4) {
						burl = "/bets/prizeOperationByBet";
						btype = "post";
						bdata = {
							"betId": item.id
						};
					} else {
						burl = "/call/openmanage";
						btype = "get";
						bdata = {
							"id": id,
							"judge": judge,
							type: "1"
						};
					}
					let options = {
						type: btype,
						url: burl,
						data: bdata,
						dataType: 'json',
						success: function(data) {
							let msg = data.msg;
							//弹出信息
							layer.open({
								title: '开奖处理结果',
								content: msg,
								area: '30%',
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
					layer.close(index);
				},
				btn2: function(index, layero) {
					layer.closeAll();
				},
				cancel: function(index, layero) { //按右上角“X”按钮  
					layer.closeAll();
				},

			});
		},
		//开奖回滚
		rollBackBtn: function(id, isCal, type) {
			var _this = this;
			if(isCal == 0 || type == 2) {
				return;
			}
			layer.open({
				title: ['提示'],
				content: '<div>确认要开奖回滚吗？</div>',
				btn: ['确认', '取消'],
				area: '30%',
				shadeClose: true,
				//回调函数  
				yes: function(index, layero) {
					_this.rollBack(id);
					layer.close(index);
				},
				btn2: function(index, layero) {
					layer.closeAll();
				},
				cancel: function(index, layero) { //按右上角“X”按钮  
					layer.closeAll();
				},

			});
		},
		rollBack: function(id) {
			let options = {
				type: "post",
				url: "/bets/prizeRollbackOperation",
				data: {
					"betId": id
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
		//获取搜索彩票种类
		getLotteryTypes: function() {
			let _this = this;
			let options = {
				type: "get",
				url: "/bets/queryGameType",
				data: {},
				dataType: 'json',
				success: function(data) {
					////console.log(data)
					if(data.code == 200) {
						_this.LotteryTypes = data.body;
					}
				},
				error: function(msg) {
					//console.log(msg)
				}
			};
			base.sendRequest(options);
		},

		//方案搜索
		searchScheme: function() {
			if(this.isClick){
				return
			}
			let _this = this
			let data = {
				pageIndex: 1,
				pageNum: parseInt(this.page_num),
				pageSize: 5,

				username: _this.username, //用户名
				status: _this.status, //状态
				outOfThrity: _this.outOfThrity, //时间
				startTime: _this.startTime, //时间开始
				endTime: _this.endTime, //时间停止
				lowerAmount: _this.lowerAmount, //最低金额
				higherAmount: _this.higherAmount, //最高金额
				lowerBonus: _this.lowerBonus, //最低奖金
				higherBonus: _this.higherBonus, //最高奖金
				gameType: _this.gameType, //彩票种类
				calNo: _this.cal_no,
				orderId: _this.order, //订单号
				userType:$("#search_userType").find("option:selected").val()
			};
			let options = {
				type: "get",
				url: "/bets/queryBettingOrderList",
				data: data,
				dataType: "json",
				success: function(data) {
					if(_this.tipsLoad){
						layer.closeAll();
						_this.tipsLoad="";
						_this.isClick=false;
					}
					//console.log(data)
					if(data.code == 200) {
						_this.datas = data.body.list;
						_this.total = data.body.total;
						for(var i in _this.datas){
							if(_this.datas[i].isCal==1){
								_this.datas[i].calTime = _this.datas[i].calTime.slice(5,16);
							}
							_this.datas[i].actionTime = _this.datas[i].actionTime.slice(5,16);
						}
						//分页的(右边点击)
						if(_this.datas.length != 0) {
							$(".none").hide();
							$('#fenye').jqPaginator('option', {
								totalPages: data.body.pages,
								currentPage: 1
							});
						} else {
							$('#fenye').jqPaginator('option', {
								totalPages: 1,
								currentPage: 1
							});
							$(".none").show();
						}
					}
				},
				error: function(msg) {
					//console.log(msg)
				}
			};
			// 检测用户输入的时间是否符合规范（开始时间小于结束时间）
			if(!_this.checkdate(data.startTime, data.endTime)) {
				return;
			}
			if(_this.lowerAmount > _this.higherAmount) {
				layer.alert('最低金额不能高于最高金额', {
					skin: 'layui-layer-molv',
					closeBtn: 0,
					anim: 4 //动画类型
				});
				return;
			}
			if(_this.lowerBonus > _this.higherBonus) {
				layer.alert('最高奖金不能低于最低奖金', {
					skin: 'layui-layer-molv',
					closeBtn: 0,
					anim: 4 //动画类型
				});
				return;
			}
			this.tipsLoad=
			layer.load(1, {
				shade: [0.1,'#fff'] //0.1透明度的白色背景
			});
			setTimeout(function(){
				base.sendRequest(options);
			},101);
		},

		//点击用户名
		//      biddingUser: function (name) {
		//          let _this = this;
		//          let options = {
		//              type: "get",
		//              url: "/bets/queryUserDetail",
		//              data: {
		//                  username: name
		//              },
		//              dataType: "json",
		//              success: function (data) {
		//                  layer.open({
		//                      title: '查看会员详情',
		//                      type: 1,
		//                      content: $('.concealDiv'),
		//                      area:['40%','62%'],
		//                      btn: ['关闭'],
		//                      yes: function (index, layero) {
		//                          layer.closeAll('page');
		//                      }
		//                  });
		//                  ////console.log(data)
		//                  if (data.code == 200) {
		//                      _this.userParticulars = data.body;
		//                      _this.userLast = data.body.loginLog;
		//                      for (let i = 0; i < _this.userLast.length; i++) {
		//                          _this.userLast.loginTime = _this.userLast[0].loginTime;
		//                          _this.userLast.loginIp = _this.userLast[0].loginIp;
		//                      }
		//                  }
		//              },
		//              error: function (msg) {
		//                  //console.log(msg)
		//              }
		//          }
		//          base.sendRequest(options);
		//      },
		chooseWhich: function(item) {
			let _this = this;
			if(item.type > 4) {
				_this.seeMessage(item);
			} else {
				_this.orders(item);
			}
		},
		seeMessage: function(item) {
			let _this = this;
			_this.id = item.id;
			let options = {
				type: 'post',
				data: {
					'betId': item.id,
					'outOfThrity': _this.outOfThrity,
				},
				dataType: 'json',
				url: '/bets/getNumbersLotteryDetails',
				success: function(data) {
					////console.log(data);
					if(data.code == 200) {
						_this.ordersTwo = data.body;
						layui.use('layer', function() {
							var layer = layui.layer;
							layer.open({
								title: '查看订单详情',
								type: 1,
								content: $('.ordersTwo'),
								area: ['70%', '70%'],
								btn: ['关闭'],
								yes: function(index, layero) {
									layer.closeAll('page');
								}

							})
						})
						//console.log(_this.ordersTwo)
					} else {
						return false
					}
				},
				error: function(msg) {
					////console.log(msg);
				}
			};
			base.sendRequest(options);
		},
		//点击订单表
		orders: function(item) {
			let _this = this;
			_this.id = item.id;
			_this.type = item.type;
			let options = {
				type: "get",
				url: "/bets/queryBettingOrderDetail",
				data: {
					betId: item.id,
					outOfThrity: _this.outOfThrity,
				},
				success: function(data) {
					layer.open({
						title: '查看订单详情',
						type: 1,
						content: $('.ordersOne'),
						area: ['70%', '70%'],
						btn: ['关闭'],
						yes: function(index, layero) {
							layer.closeAll('page');
						}
					});
					_this.ordersParticulars = data.body;
					//console.log(_this.ordersParticulars);
				},
				error: function(msg) {
					//console.log(msg)
				}
			};
			base.sendRequest(options);

		},
		//点击查看中奖明细
		statusYes: function() {
			let _this = this;
			let options = {
				type: "post",
				url: "/bets/getActionDataResult",
				data: {
					betId: _this.id,
				},
				success: function(data) {
					////console.log(data);
					_this.winDetails = data.body;
					_this.totals = 1;
					_this.avg = 0;
					if(_this.winDetails) {
						layer.open({
							title: '查看中奖明细',
							type: 1,
							content: $('.winDetails'),
							area: ['50%', '70%'],
							btn: ['关闭'],
							yes: function(index, layero) {
								layer.closeAll('page');
							}
						});
						if(!_this.northReturnAwardRate) {
							_this.northReturnAwardRate = 65;
						} else {
							_this.northReturnAwardRate = _this.northReturnAwardRate;
						}
						for(let i = 0; i < _this.winDetails.length; i++) {
							////console.log(_this.winDetails[i]);
							_this.totals = 1;
							for(let j = 0; j < _this.winDetails[i].list.length; j++) {
								if(_this.winDetails[i].list[j].odds) {
									_this.totals *= _this.winDetails[i].list[j].odds;
								}
							}
							_this.totals = _this.totals * 2 * _this.winDetails[i].times;
							//北单返奖率
							if(_this.type == 3) {
								_this.totals = _this.totals * (_this.northReturnAwardRate / 100);
							}
							_this.winDetails[i].totals = parseFloat(_this.totals).toFixed(4);
							//console.log(_this.winDetails[i].totals + "111" + _this.totals)
						}
					} else {
						layer.msg('暂无数据')
					}
				},
				error: function(msg) {
					//console.log(msg)
				}
			};
			base.sendRequest(options);

		},
		//点击查看投注比赛详情
		actionData: function() {
			let _this = this;
			_this.actionMes = _this.ordersParticulars.list;
			//console.log(_this.actionMes)
			layer.open({
				title: '查看投注比赛详情',
				type: 1,
				content: $('.actionData'),
				area: ['50%', '70%'],
				btn: ['关闭'],
				yes: function() {
					layer.closeAll('page');
				}
			});
		},
		//隐藏用户详情和订单详情
		quit: function() {
			this.detailUser = false;
			this.isShow = false;
			this.ordersUser = false;
		},
		//点击清空
		clear: function() {
			let _this = this;
			$(".table input").val("");
			$('.btns a').removeClass('b_red');
			_this.username = ''; //用户名
			_this.status = ''; //状态
			_this.outOfThrity = '0'; //时间
			_this.startTime = ''; //时间开始
			_this.endTime = ''; //时间停止
			_this.lowerAmount = ''; //最低金额
			_this.higherAmount = ''; //最高金额
			_this.lowerBonus = ''; //最低奖金
			_this.higherBonus = ''; //最高奖金
			_this.gameType = ''; //彩票种类
			_this.order = ''; //订单id
		},
		// 检测时间的先后顺序
		checkdate: function(start, end) {
			//得到日期值并转化成日期格式，replace(//-/g, "//")是根据验证表达式把日期转化成长日期格式，这样
			//再进行判断就好判断了
			var sDate = new Date(start.replace(/\-/g, "\/"));
			var eDate = new Date(end.replace(/\-/g, "\/"));
			if(sDate > eDate) {
				layer.alert('结束时间不能早于开始时间', {
					skin: 'layui-layer-molv',
					closeBtn: 0,
					anim: 4 //动画类型
				});
				return false;
			}
			return true;
		},
		 //点击今日执行
        now: function () {
            $('.btns a').removeClass('b_red');
            $('#now').addClass('b_red');
            var dateTime = new Date();
            dateTime.setTime(dateTime.getTime());
            var s2 = dateTime.getFullYear() + "-" + this.getzf(dateTime.getMonth() + 1) + "-" + this.getzf(dateTime.getDate());
            this.startTime = s2 + " " + "00:00:00";
            this.endTime = s2 + " " + "23:59:59";
        },
        //点击昨日执行
        yes: function () {
            $('.btns a').removeClass('b_red');
            $('#yes').addClass('b_red');
            var dateTime = new Date();
            dateTime.setTime(dateTime.getTime() - 24 * 60 * 60 * 1000);
            var s1 = dateTime.getFullYear() + "-" + this.getzf(dateTime.getMonth() + 1) + "-" + this.getzf(dateTime.getDate());
            this.startTime = s1 + " " + "00:00:00";
            this.endTime = s1 + " " + "23:59:59";
        },
        //点击本周执行
        week: function () {
            $('.btns a').removeClass('b_red');
            $('#week').addClass('b_red');
            var dateTime = new Date(),
                st = this.getDateTime(0),
                et = dateTime.getFullYear() + "-" + this.getzf(dateTime.getMonth() + 1) + "-" + this.getzf(dateTime.getDate());
            this.startTime = st + " " + "00:00:00";
            this.endTime = et + " " + "23:59:59";
        },
        //点击上周执行
        lastWeek: function () {
            $('.btns a').removeClass('b_red');
            $('#lastWeek').addClass('b_red');
            var st = this.getDateTime(2),
                et = this.getDateTime(3);
            this.startTime = st + " " + "00:00:00";
             this.endTime = et + " " + "23:59:59";
        },
        //点击本月执行
        month: function () {
            $('.btns a').removeClass('b_red');
            $('#month').addClass('b_red');
            var dateTime = new Date(),
                st = this.getDateTime(4),
                et = dateTime.getFullYear() + "-" + this.getzf(dateTime.getMonth() + 1) + "-" + this.getzf(dateTime.getDate());
            this.startTime = st + " " + "00:00:00";
            this.endTime = et + " " + "23:59:59";
        },
        //点击上月执行
        lastMonth: function () {
            $('.btns a').removeClass('b_red');
            $('#lastMonth').addClass('b_red');
            var st = this.getDateTime(6);
            var et = this.getDateTime(7);
            this.startTime = st + " " + "00:00:00";
            this.endTime = et + " " + "23:59:59";
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
        }, //补0
        getzf: function (num) {
            if (parseInt(num) < 10) {
                num = '0' + num;
            }
            return num;
        },
        //加载选择开始/结束日期控件
//      get_time: function () {
//          //	日期设置
//          laydate.render({
//              elem: '#test1', //指定元素
//              format: 'yyyy-MM-dd HH:mm:ss',
//              type: 'datetime',
//              max: 0,
//              value: "",
//              done: function (value, date, endDate) {
//              	_this.startTime = value
//                  $('.btns a').removeClass('b_red');
//              }
//          });
//          laydate.render({
//              elem: '#test2', //指定元素,
//              format: 'yyyy-MM-dd HH:mm:ss',
//              type: 'datetime',
//              max: 0,
//              value: "",
//              done: function (value, date, endDate) {
//              	_this.endTime = value
//                  $('.btns a').removeClass('b_red');
//              }
//          })
//      },
		getlayer() {
			layui.use('layer', function () {
				var layer = layui.layer;
			})
		}
	},

	mounted: function() {
		let _this = this;
		this.clickBtn();
		this.getLotteryTypes();
		this.getlayer();
		//时间开始选择
		laydate.render({
			elem: "#test1", //指定元素
			format: 'yyyy-MM-dd HH:mm:ss',
			trigger: 'click',
			done: function(value, date, endDate) {
				/*//console.log(value); //得到日期生成的值，如：2017-08-18
				 //console.log(date); //得到日期时间对象：{year: 2017, month: 8, date: 18, hours: 0, minutes: 0, seconds: 0}
				 //console.log(endDate); //得结束的日期时间对象，开启范围选择（range: true）才会返回。对象成员*/
				_this.startTime = value;
				$('.btns a').removeClass('b_red');
			}
		});
		//时间结束选择
		laydate.render({
			elem: "#test2", //指定元素
			format: 'yyyy-MM-dd HH:mm:ss',
			trigger: 'click',
			done: function(endDate) {
				_this.endTime = endDate;
				$('.btns a').removeClass('b_red');
			}
		});

		//添加句柄
		document.addEventListener('keyup', function(e) {
			if(_this.detailUser && e.keyCode == 27) {
				_this.detailUser = false;
				_this.isShow = false;
			}
			if(_this.ordersUser && e.keyCode == 27) {
				_this.isShow = false;
				_this.ordersUser = false;
			}
		})
	},
	watch: {
		//监听页码下拉框的值
		page_num: function() {
			this.getdatas(1);
		}
	},
	beforeDestroy: function() {
		document.removeEventListener('keyup');
	}
});
$(function(){
	app.now();
	// 加载分页功能
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
})
