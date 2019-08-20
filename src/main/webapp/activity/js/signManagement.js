var dps = new Vue({
	el: "#container",
	data: {
		//增加修改
		sign: {
			signName: '',
			signType: 1,
			signCycle: 7,
			firstAmount: '',
			extraReward: '',
			startTime: '',
			endTime: '',
			signInfo: '',
			rewardModel: '',
		},
		
		type: [],     //
		rewardM: [],    //
		cycle: '',
		days: [],    //周期天数
		
		//分页器
		dataStore: {
			pageIndex: 1,
			pageNum: 50
		},
		//奖励详情
		reward: {
			extraReward: [],
			rewardModel: []
		},
		datas: [], //所有list
		total:0,
		signMessage: [], //查看详情保存的数据
		
		//签到详情
		userSign:[],
		userSignList:[],
		signTime:[],
	},
	created: function() {
		this.$nextTick(function () {
            this.getlayer();
            this.setDateTime();
        })
	},
	mounted: function() {
		var _this = this;
		_this.clickBtn();
		_this.setDateTime();
	},
	methods: {
		getlayer() {
            layui.use("layer", function () {
                let layer = layui.layer;
            });
        },
		setDateTime: function() {
			//日期设置
			laydate.render({
				elem: '#startDate', //指定元素
				format: 'yyyy-MM-dd HH:mm:ss',
				type: 'datetime',
				position: 'fixed',
			});
			laydate.render({
				elem: '#endDate', //指定元素
				format: 'yyyy-MM-dd HH:mm:ss',
				type: 'datetime',
				position: 'fixed',
			});
		},
		//查询数据
		getdatas: function(num) {
			var _this = this;
			var save = {
				type: "post",
				data: {
					"pageIndex": num,
					"pageNum": _this.dataStore.pageNum,
					"pageSize": 10,
				},
				url: "/sign/querySignInfoList",
				success: function(data) {
					if(data.code == 200) {
						//console.log(data)
						_this.datas = data.body.list;
						 _this.total = data.body.total;
						$('#page').jqPaginator('option', {
							totalPages: data.body.pages,
                            currentPage: 1
						});
					} else {
						 $('#page').jqPaginator('option', {
                             totalPages: 1,
                             currentPage: 1
                         });
					}
				},
				error: function error(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(save);
		},
		//查询签到用户
		getUserSignList: function(id) {
			layui.use('layer', function() {
				var layer = layui.layer;
				layer.open({
					type: 1,
					title: '签到用户详情',
					area: ['50%', '60%'],
					content: $('#userSign'),
					btn: ['关闭'],
					yes: function(index) {
						layer.closeAll('page');
					},
					btn2: function() {
					},
				});
			});
			var _this = this;
			var save = {
				type: "post",
				data: {
					signId:id,
				},
				url: "/sign/getUserSignList",
				success: function(data) {
					if(data.code == 200) {
						_this.userSign = data.body;
						_this.userSignList = data.body.list;
					}
				},
				error: function error(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(save);
		},
		//查询签到用户
		userSignDate: function(time) {
			var _this = this;
			layui.use('layer', function() {
				var layer = layui.layer;
				layer.open({
					type: 1,
					title: '用户签到时间',
					area: ['40%', '60%'],
					content: $('#userSignDate'),
					btn: ['关闭'],
					yes: function(index) {
						layer.closeAll('page');
					},
					btn2: function() {
					},
				});
			});
			_this.signTime = time.split(',');
		},
		//增加修改
		signBombbox: function(type, obj) {
			var _this = this;
			//修改
			if(type == 1) {
				//显示周期
				if(obj.signCycle != 7 && obj.signCycle != 30) {
					$('#signCycle').show();
					_this.cycle = obj.signCycle;
				} else {
					$('#signCycle' + obj.signCycle).attr('checked', 'checked');
				}
				//类型
				$('#signType' + obj.signType).attr('checked', 'checked');
				_this.signType(obj.signType, 1, obj.rewardModel);

				//额外奖励
				if(obj.extraReward) {
					$('#extraReward1').attr('checked', 'checked');
					_this.extraReward(1, 1, obj.extraReward);
				} else {
					$('#extraReward0').attr('checked', 'checked');
				}
				//other

				_this.sign.signName = obj.signName;
				_this.sign.signType = obj.signType;
				_this.sign.firstAmount = obj.firstAmount;
				_this.sign.extraReward = obj.extraReward;
				$('#startDate').val(obj.startTime);
				$('#endDate').val(obj.endTime);
				_this.sign.signInfo = obj.signInfo;
				_this.sign.rewardModel = obj.rewardModel;
			} else if(type == 2) { //增加
				//input置空
				_this.type = [];
				_this.rewardM = [];
				_this.cycle = '';
				_this.sign.signName = '';
				_this.signCycle(7);
				_this.signType(1);
				_this.extraReward(0);
				_this.sign.firstAmount = '';
				_this.sign.extraReward = '';
				$('#startDate').val("");
				$('#endDate').val("");
				_this.sign.signInfo = '';
				_this.sign.rewardModel = '';
			}
			layui.use('layer', function() {
				var layer = layui.layer;
				layer.open({
					type: 1,
					title: '新增签到活动',
					area: ['50%', '80%'],
					content: $('#addMessage'),
					btn: ['保存', '关闭'],
					yes: function(index) {
						if(_this.sign.signName==''){
							layer.msg('活动名称不能为空');
							return;
						}
						if(_this.sign.signCycle==0){
							if(_this.cycle==''){
								layer.msg("由于您选择自定义周期，请输入天数");
								return;
							}
						}
						if(_this.sign.firstAmount=='' || _this.sign.firstAmount<=0){
							layer.msg('基数奖励不能为空或必须大于0');
							return;
						}
						if(_this.sign.signType==0){
							for(var d=0;d<_this.days.length;d++){
								if(_this.type[d]==undefined || _this.type[d]<0){
									layer.msg('由于您选择奖励模式为自定义，第'+(d+1)+'天的奖励不能为空或大于等于0');
									return;
								}
							}
						}
						if(_this.sign.extraReward==1){
							for(var e=0;e<_this.days.length;e++){
								if(_this.rewardM[e]==undefined || _this.rewardM[e]<0){
									layer.msg('由于您选择额外奖励为是，第'+(e+1)+'天的奖励不能为空或大于等于0');
									return;
								}
							}
						}
						if($('#startDate').val()==''){
							layer.msg('起始时间不能为空');
							return;
						}
						if($('#endDate').val()==''){
							layer.msg('结束时间不能为空');
							return;
						}
						_this.addSign();
					},
					btn2: function() {
						layer.closeAll('page');
					},
				});
			});
		},
		//增加
		addSign: function() {
			var _this = this;
			//奖励模式
			var rewardModel = [];
			//额外奖励
			var extraReward = [];
			//基数奖励
			var firstAmount = _this.sign.firstAmount;
			//判断奖励模式 1=固定递增 2=固定倍增 3=自定义
			if(_this.sign.signType == 1) {
				for(var i = 1; i <= _this.sign.signCycle; i++) {
					var amount = parseFloat(firstAmount)*i;
					rewardModel.push(amount.toFixed(2));
				}
			} else if(_this.sign.signType == 2) {
				for(var i = 1; i <= _this.sign.signCycle; i++) {
					firstAmount = parseFloat(firstAmount)*i;
					rewardModel.push(firstAmount.toFixed(2));
				}
			}else if(_this.sign.signType == 0){
				rewardModel = _this.type;
			}
			_this.sign.rewardModel = rewardModel.join(",");
			//判断是否有额外奖励
			if(_this.sign.extraReward==1){
				for(var i = 0; i < _this.rewardM.length; i++) {
					if(_this.rewardM[i] != undefined) {
						extraReward.push((i + 1) + ':' + _this.rewardM[i]);
					}
				}
			}
			_this.sign.extraReward = extraReward.join(',');
			var save = {
				type: "post",
				data: {
					signName: _this.sign.signName,
					signType: _this.sign.signType,
					signCycle: _this.sign.signCycle,
					firstAmount: _this.sign.firstAmount,
					extraReward: _this.sign.extraReward,
					startTime: $('#startDate').val(),
					endTime: $('#endDate').val(),
					signInfo: _this.sign.signInfo,
					rewardModel: _this.sign.rewardModel
				},
				url: "/sign/addSignInfo",
				success: function(data) {
					if(data.code == 200) {
						layer.closeAll(page);
						layer.msg('添加成功');
						setTimeout(function() {
							window.location.reload();
						}, 1000)
					} else {
						layer.msg(data.msg);
					}
				},
				error: function error(msg) {
				}
			};
			base.sendRequest(save);
		},
		//修改
		updateSign: function(id) {
			var _this = this;
			var extraReward = [];
			var firstAmount = _this.sign.firstAmount;
			if(_this.sign.signType == 1) {
				for(var i = 0; i < _this.sign.signCycle; i++) {
					_this.type.push(firstAmount++);
				}
			} else if(_this.sign.signType == 2) {
				for(var i = 1; i <= _this.sign.signCycle; i++) {
					firstAmount = firstAmount;
					_this.type.push(firstAmount);
					firstAmount = firstAmount * 2;
				}
			}
			_this.sign.rewardModel = _this.type.join(',');
			for(var i = 0; i < _this.rewardM.length; i++) {
				if(_this.rewardM[i] != undefined) {
					extraReward.push((i + 1) + ':' + _this.rewardM[i]);
				}
			}
			_this.sign.extraReward = extraReward.join(',');
			if(_this.sign.signCycle==0){
				_this.sign.signCycle = _this.cycle;
			}
			var save = {
				type: "post",
				data: {
					signId: id,
					signName: _this.sign.signName,
					signType: _this.sign.signType,
					signCycle: _this.sign.signCycle,
					firstAmount: _this.sign.firstAmount,
					extraReward: _this.sign.extraReward,
					startTime: $('#startDate').val(),
					endTime: $('#endDate').val(),
					signInfo: _this.sign.signInfo,
					rewardModel: _this.sign.rewardModel
				},
				url: "/sign/updateSignInfo",
				success: function(data) {
					if(data.code == 200) {
						layer.closeAll(page);
						layer.msg('修改成功');
						setTimeout(function() {
							window.location.reload();
						}, 1000)
					} else {

					}
				},
				error: function error(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(save);
		},
		//选择周期
		signCycle: function(id) {
			var _this = this;
			$('#signType').hide();
			$('#extraReward').hide();
			$('#signType1').attr('checked','checked');
			$('#extraReward0').attr('checked','checked');
			if(id != 0) {
				_this.sign.signCycle = id;
				$('#signCycle').hide();
			} else {
				_this.sign.signCycle = 0;
				$('#signCycle').show();
			}
		},
		//选择类型
		signType: function(id, bs, rewardModel) {
			var _this = this;
			_this.sign.signType = id;
			if(id != 0) {
				$('#signType').hide();
			} else {
				var signCy;
				if(_this.sign.signCycle==0){
					if(_this.cycle=='' || _this.cycle==0){
						layer.msg('请输入周期天数');
						return;
					}
					signCy = _this.cycle;
				}else{
					signCy = _this.sign.signCycle;
				}
				_this.days = [];
				for(var i = 0; i < signCy; i++) {
					_this.days.push(i);
				}
				$('#signType').show();
				if(bs != 1) {
					_this.type = new Array(_this.sign.signCycle);
				} else {
					var model = [];
					_this.type = [];
					model = rewardModel.split(',');
					for(var i = 0; i < model.length; i++) {
						_this.type.push(parseInt(model[i]));
					}
				}
			}
		},
		//额外奖励
		extraReward: function(id, bs, extraReward) {
			var _this = this;
			_this.sign.extraReward = id;
			if(id != 1) {
				$('#extraReward').hide();
			} else {
				$('#extraReward').show();
				var signCy;
				if(_this.sign.signCycle==0){
					signCy = _this.cycle;
				}else{
					signCy = _this.sign.signCycle;
				}
				_this.days = [];
				for(var i = 0; i < signCy; i++) {
					_this.days.push(i);
				}
				if(bs != 1) {
					_this.rewardM = new Array(_this.sign.signCycle);
				} else {
					var extra = [];
					var extraInt = [];
					extra = extraReward.split(',');
					for(var i = 0; i < _this.sign.signCycle; i++) {
						var flag = false;
						for(var j = 0; j < extra.length; j++) {
							extraInt = extra[j].split(':');
							if((i + 1) == parseInt(extraInt[0])) {
								_this.rewardM.push(extraInt[1]);
								flag = true;
							}
						}
						extraInt[1] = parseInt(extraInt[1]);
						if(!flag) {
							_this.rewardM.push(undefined);
						}
					}
				}
			}
		},
		//删除信息
		deleteSign: function(obj) {
			if(obj.status == 1) {
				layer.msg('启用状态活动不能被删除', {
					time: 1000
				});
				return;
			}
			layer.open({
				title: '提示',
				content: '是否确认删除',
				area: '20%',
				btn: ['取消', '确认'],
				yes: function(index, layero) {
					layer.close(index);
				},
				btn2: function(index, layero) {
					var save = {
						type: "post",
						data: {
							signId: obj.id,
						},
						url: "/sign/removeSignInfo",
						success: function(data) {
							if(data.code == 200) {
								//						layer.close(index);
								layer.msg('删除成功');
								setTimeout(function() {
									window.location.reload();
								}, 1000)
							} else {
								layer.msg('删除失败');
							}
						},
						error: function error(msg) {
							//console.log(msg);
						}
					};
					base.sendRequest(save);
				},
			});
		},
		recoverySign: function(obj) {
			layer.open({
				title: '提示',
				content: '是否确认恢复',
				area:  '20%',
				btn: ['取消', '确认'],
				yes: function(index, layero) {
					layer.close(index);
				},
				btn2: function(index, layero) {
					var save = {
						type: "post",
						data: {
							signId: obj.id,
							isDelete: 1
						},
						url: "/sign/updateSignInfo",
						success: function(data) {
							if(data.code == 200) {
								//						layer.close(index);
								layer.msg('恢复成功');
								setTimeout(function() {
									window.location.reload();
								}, 1000)
							} else {

							}
						},
						error: function error(msg) {
							//console.log(msg);
						}
					};
					base.sendRequest(save);
				},
			});
		},
		//点击按钮
		clickBtn: function() {
			var that = this;
			//启用禁用按钮设置
			$('#dataList').on('click', '.status', function() {
				var str = {};
				str.signId = parseInt($(this).attr('data-index'));
				str.status = $(this).find('.hide').attr('data-index');
				$(this).find('span').toggleClass('hide');
				var changeState = {
					type: 'post',
					data: str,
					url: '/sign/updateSignInfo',
					dataType: 'json',
					success: function success(data) {
						if(data.code == 200) {
							layer.msg('状态切换成功');
							setTimeout(function() {
								window.location.reload();
							}, 1000)
						}
					}
				};
				layui.use('layer', function() {
					var layer = layui.layer;
					layer.open({
						type: 1,
						skin: 'confirm-class',
						title: '确认操作',
						area: '20%',
						content: '<div style="padding: 10px 20px;color: #e4393c;">' + (str.status == 1 ? '是否启用' : '是否停用') + '</div>',
						btn: ['取消', '确定'],
						yes: function() {
							layer.closeAll('page');
						},
						btn2: function() {
							base.sendRequest(changeState);
						},
					});
				});
			});

		},
		//查看详情
		openMessage: function(index) {
			var _this = this;
			_this.signMessage = _this.datas[index];
			layui.use('layer', function() {
				var layer = layui.layer;
				layer.open({
					type: 1,
					title: '签到活动详情',
					area: ['50%', '45%'],
					content: $('#openMessage'),
					btn: ['关闭'],
					yes: function() {
						layer.closeAll('page');
					},
					//					btn2: function() {
					//					},
				});
			});
		},
		//查看详情
		openReward: function(index) {
			var _this = this;
			var model = [];
			var extra = [];
			_this.reward.extraReward = [];
			_this.reward.rewardModel = [];
			model = _this.datas[index].rewardModel;
			extra = _this.datas[index].extraReward;
			if(extra) {
				extra = extra.split(',');
				for(var i = 0; i < extra.length; i++) {
					_this.reward.extraReward.push(extra[i].split(':'));
				}
			}
			if(model) {
				model = model.split(',');
				for(var i = 0; i < model.length; i++) {
					_this.reward.rewardModel.push(model[i]);
				}
			}
			layui.use('layer', function() {
				var layer = layui.layer;
				layer.open({
					type: 1,
					title: '奖励模式详情',
					area: ['50%', '60%'],
					content: $('#reward'),
					btn: ['关闭'],
					yes: function() {
						layer.closeAll('page');
					},
					btn2: function() {},
				});
			});
		},
	},
	watch: {
		//监听页码下拉框的值
		'dataStore.pageNum': function() {
			this.getdatas(1);
		},
	},
});
/*****状态编写******/
$(function() {
	// 加载分页功能
	$.jqPaginator('#page', {
		totalPages: 1, //多少页数据
		visiblePages: 10, //最多显示几页
		currentPage: 1, //当前页
		wrapper: '<ul class="pagination"></ul>',
		first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',
		prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
		next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
		last: '<li class="last"><a href="javascript:void(0);">尾页</a></li>',
		page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',

		onPageChange: function onPageChange(num) {
			dps.getdatas(num);
		}
	});

});