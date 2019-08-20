'use strict';

$(function() {
	var hb = {
		dataStore: {
			num: 1,
			pageNum: 50,
			numRecord: 1,
			pageNumRecord: 5,
			prevStr: '',
			vipList: [0],

		},
		created: function created() {
			this.setDateInput();
			this.click();
			this.recordClick();
		},
		getVal: function getVal(elem) {
			return elem.find("option:selected").val();
		},
		//提示框
		tipsContent: function tipsContent(str, times) {
			layui.use('layer', function() {
				var layer = layui.layer;
				layer.msg(str, {
					time: times
				});
			});
		},
		//提示确认框
		checkConfirmFn: function checkConfirmFn(send) {
			var that = this;
			layui.use('layer', function() {
				var layer = layui.layer;
				layer.open({
					type: 1,
					title: '提示信息',
					content: "<div style='padding:15px;box-sizing: border-box;'>确定删除此信息?</div>",
					area: ['20%'],
					btn: ['取消', '确定'],
					yes: function yes(index, layero) {
						layer.close(index);
					},
					btn2: function btn2(index, layero) {
						base.sendRequest(send);
					},
					cancel: function cancel() {}
				});
			});
		},
		//字符串拼接---紅包管理
		data: function data(list) {
			var html = "";
			if(list == "" || list == null) {
				html += '<tr><td colspan="10">\u6682\u65E0\u6570\u636E</td></tr>';
			} else {
				for(var i = 0; i < list.length; i++) {
					var obj = list[i];
					html += '<tr><td>' + (obj.redPacketTitle ? obj.redPacketTitle : "-") + '</td><td>' + (obj.redPacketMoney ? obj.redPacketMoney : "-") + '</td><td>' + (obj.redPacketNum ? obj.redPacketNum : "-") + '</td><td>' + (obj.moneyMin ? obj.moneyMin : "-") + '</td><td>' + (obj.limitIpNum ? obj.limitIpNum : "-") + '</td><td>' + (obj.vipId ? obj.vipId : "-") + '</td><td>' + (obj.startTime ? obj.startTime : "-") + '</td><td>' + (obj.endTime ? obj.endTime : "-") + '</td>';
					if(obj.state==0||obj.state==1){
						html+='<td><div class="layui-form"><div class="layui-form-item"><div class="layui-input-block'+ (obj.state == 0 ? ' state' : '') +'" data-index="' + obj.id + '" style="margin-left: 0;"><div data-index="1" class="layui-unselect layui-form-switch layui-form-onswitch ' + (obj.state == 1 ? 'able' : 'able hide') + '"><em>已启用</em></div><div  data-index="0" class="layui-unselect layui-form-switch ' + (obj.state == 0 ? 'disabled' : 'disabled hide') + '"><em>未启用</em><i></i></div></div></div></div></td>';
					}else if(obj.state==-1){
						html+='<td><div>已结束</div></td>';
					}
					html+='<td><a href="javascript:;" data-index="' + obj.id + '" class="layui-btn layui-btn-mini delete layui-btn-danger">\u5220\u9664</a></td></tr>';
				}
			}
			$('#dataList').html(html);
		},
		//数据加载----紅包管理
		methods: function methods() {
			var that = this;
			var str = {
				"pageIndex": that.dataStore.num,
				"pageNum": that.dataStore.pageNum,
				"pageSize": 10
			};
			var load = {
				type: 'get',
				data: str,
				url: '/redPacketManagement/queryRedPacketManagementList',
				dataType: 'json',
				success: function success(data) {
					that.data(data.body.list);
					if(data.body.list == null || data.body.list == "") {
						$('#page').jqPaginator('option', {
							totalPages: 1,
							currentPage: 1
						});
						$('#paging_record .sumRecord').html(0);
					} else {
						$('#page').jqPaginator('option', {
							totalPages: data.body.pages
						});
						$('#paging_record .sumRecord').html(data.body.total);
					}
				}
			};
			base.sendRequest(load);
		},
		//相关点击事件---红包管理
		click: function click() {
			var that = this;
			//保存按钮
			$('#sendHb').on('click', '.checkboxList>span', function() {
				var vipNum = parseInt($(this).children('i').attr('data-index'));
				if($(this).children('i').is('.active')) {
					$(this).children('i').removeClass('active');
					for(var i = 0; i < that.dataStore.vipList.length; i++) {
						if(that.dataStore.vipList[i] == vipNum) {
							that.dataStore.vipList.splice(i, 1);
						}
					}
				} else {
					$(this).children('i').addClass('active');
					that.dataStore.vipList.push(vipNum);
				}
			});
			//发红包
			$('#addHb').on('click', function() {
				$('#sendHb .errorMsg').addClass('hide');
				layui.use('layer', function() {
					var layer = layui.layer;
					layer.open({
						type: 1,
						title: '发红包',
						offset: '25%',
						area: ['50%','70%'],
						content: $('#sendHb'),
						btn: ['关闭', '保存'],
						yes: function(index, layero) {
							layer.close(index);
						},
						btn2: function() {
							var str = {
								"redPacketTitle": "",
								"redPacketMoney": 0,
								"redPacketNum": 0,
								"moneyMin": 0,
								"limitIpNum": 0,
								"amountCodeMultiple": 0,
								"vipId": 0,
								"startTime": "",
								"endTime": ""
							};
							str.redPacketTitle = $('.title').val();
							str.redPacketMoney = $('.money').val() ? parseFloat($('.money').val()) : '';
							str.redPacketNum = $('.num').val() ? parseInt($('.num').val()) : '';
							str.moneyMin = $('.minMoney').val() ? parseFloat($('.minMoney').val()) : '';
							str.limitIpNum = $('.IPnum').val() ? parseInt($('.IPnum').val()) : '';
							str.amountCodeMultiple = $('.multiple').val() ? parseInt($('.multiple').val()) : '';
							str.vipId = that.dataStore.vipList.join(',');
							// if(!str.vipId){str.vipId="0";}
							str.startTime = $('#startTime').val();
							str.endTime = $('#endTime').val();
//							var checkData = [str.redPacketTitle, str.redPacketMoney, str.redPacketNum, str.moneyMin, str.amountCodeMultiple, str.limitIpNum, str.startTime, str.endTime, str.vipId];
							var checkData = [str.redPacketTitle, str.redPacketMoney, str.redPacketNum, str.moneyMin, str.amountCodeMultiple, str.limitIpNum, str.startTime, str.endTime];
							if(!that.checkData(checkData)) {
								//					$('#sendHb .errorMsg').removeClass('hide');
								if(str.redPacketTitle == null || str.redPacketTitle == "") {
									that.tipsContent("标题不能为空", 1000);
								} else if(str.redPacketMoney == "") {
									that.tipsContent("金额不能为空或0", 1000);
								} else if(str.redPacketNum == "") {
									that.tipsContent("个数不能为空或0", 1000);
								} else if(str.moneyMin == "") {
									that.tipsContent("最小金额不能为空或0", 1000);
								} else if(str.amountCodeMultiple == "") {
									that.tipsContent("打码量倍数不能为空或0", 1000);
								} else if(str.limitIpNum == "") {
									that.tipsContent("限制当天IP次数不能为空或0", 1000);
								} else if(str.startTime == null || str.startTime == "") {
									that.tipsContent("开始时间不能为空", 1000);
								} else if(str.endTime == null || str.endTime == "") {
									that.tipsContent("结束时间不能为空", 1000);
								}
//								else if(str.vipId == null || str.vipId == "") {
//									that.tipsContent("限制使用等级不能为空", 1000);
//								}
							} else {
//								if(str.redPacketMoney < 1) {
//									that.tipsContent("金额不能小于1", 1000);
//								} else
								if(str.redPacketNum < 1) {
									that.tipsContent("个数不能小于1", 1000);
								}
//								else if(str.moneyMin < 1) {
//									that.tipsContent("最小金额不能小于1", 1000);
//								}
								else if(str.amountCodeMultiple < 1) {
									that.tipsContent("打码量倍数不能小于1", 1000);
								} else if(str.limitIpNum < 1) {
									that.tipsContent("限制当天IP次数不能小于1", 1000);
								} else if(str.startTime == str.endTime) {
									that.tipsContent("开始时间与结束时间不能相同", 1000);
								} else {
									var sendSave = {
										type: 'post',
										data: str,
										url: '/redPacketManagement/addRedPacketManagement',
										dataType: 'json',
										success: function success(data) {
											layui.use('layer', function() {
												var layer = layui.layer;
												layer.closeAll('page');
											});
											$('.title').val("");
												$('.money').val("");
												$('.num').val("");
												$('.minMoney').val("");
												$('.IPnum').val("");
												$('.multiple').val("");
												$('#sendHb .checkboxList>span.active').removeClass("active");
												$('#sendHb .checkboxList>span:first-child').addClass("active");
												that.dataStore.vipList = [];
												$('#startTime').val("");
												$('#endTime').val("");
												$('#sendHb .checkboxList>span i.active').removeClass('active');
												$('#sendHb .checkboxList>span:first i').addClass('active');
											if(data.code == 200) {
												that.tipsContent("添加成功", 1000);
												that.methods();
											}else{
												that.tipsContent(data.msg, 1000);
											}
										}
									};
									base.sendRequest(sendSave);
								}
							}
							return false;
						}

					});
				});
			});
			//限制标题长度
			$('#sendHb').on('blur', '.title', function() {
				var c = $(this).val();
				if(c.length > 10) {
					//替换非数字字符
					var temp_amount = c.substring(0, 10);
					$(this).val(temp_amount);
					that.tipsContent('标题长度最多为10');
				}
			});
			//删除
			$('#dataList').on('click', 'a.delete', function(e) {
				e.preventDefault();
				var id = parseInt($(this).attr('data-index'));
				var str = {
					redPacketId: id
				};
				var sendDelete = {
					type: 'post',
					data: str,
					url: '/redPacketManagement/deleteRedPacketManagement',
					dataType: 'json',
					success: function success(data) {
						if(data.code == 200) {
							that.tipsContent('删除成功', 1000);
							that.methods();
						} else {
							that.tipsContent('删除失败', 1000);
						}
					}
				};
				that.checkConfirmFn(sendDelete);
			});
			//启用禁用状态切换
			$('#dataList').on('click', '.state', function() {
				var str = {};
				str.redPacketId = parseInt($(this).attr('data-index'));
				str.state = parseInt($(this).find('.hide').attr('data-index'));
				$(this).find('span').toggleClass('hide');
				var changeState = {
					type: 'post',
					data: str,
					url: '/redPacketManagement/isStartRedPacketManagement',
					dataType: 'json',
					success: function(data) {
						if(data.code == 200) {
							that.tipsContent('状态切换成功', 1000);
							that.methods();
						} else {
							that.tipsContent(data.msg, 1000);
							that.methods();
						}
					}
				};
				layui.use('layer',function(){
					var layer = layui.layer;
					layer.open({
						type: 1,
						skin:'confirm-class',
						title: '确认操作',
						area: '20%',
						content: '<div style="padding: 10px 20px;color: #e4393c;">'+(str.state==1?'是否启用':'是否停用')+'</div>',
						btn: ['取消', '确定'],
						yes: function() {
							layer.closeAll('page');
						},
						btn2:function(){
							base.sendRequest(changeState);
						},
					});
				});

			});
			//红包管理与抢红包记录切换
			$('#nav>span').on('click', function() {
				that.recordLoad();
				$('#nav>span.active').removeClass('active');
				$(this).addClass('active');
				if($(this).attr('data-index') == 1) {
					$('#manage').removeClass('hide');
					$('#record').addClass('hide');
				} else {
					$('#record').removeClass('hide');
					$('#manage').addClass('hide');
				}
			});
			//刷新按钮
			$('#refrech').on('click', function() {
				that.tipsContent("刷新中...", 1000);
				that.methods();
			});
			//页面显示数据条数--红包管理
			$('#paging_record .pageNum').on('change', function() {
				$('#page').jqPaginator('option', {
					currentPage: 1
				});
				that.dataStore.num = 1;
				that.dataStore.pageNum = parseInt(that.getVal($(this)));
				that.methods();
			});
		},
		//数据加载---抢红包记录
		recordLoad: function recordLoad() {
			var that = this;
			var str = {
				"pageIndex": that.dataStore.numRecord,
				"pageNum": that.dataStore.pageNumRecord,
				"pageSize": 10,
				"userName": "",
				"startTime": "",
				"endTime": ""
			};
			if(that.dataStore.prevStr != "") {
				that.dataStore.prevStr.pageIndex = that.dataStore.numRecord;
				str = that.dataStore.prevStr;
			}
			var sendLoad = {
				type: 'get',
				data: str,
				dataType: 'json',
				url: '/redPacketRecord/queryRedPacketRecordList',
				success: function(data) {
					that.recordTemp(data.body.list);
					if(data.body.list == null || data.body.list == "") {
						$('#pageHbrecord').jqPaginator('option', {
							totalPages: 1,
							currentPage: 1
						});
						$('#paging_hbrecord .sumRecord').html(0);
					} else {
						$('#pageHbrecord').jqPaginator('option', {
							totalPages: data.body.pages
						});
						$('#paging_hbrecord .sumRecord').html(data.body.total);
					}
				}
			};
			base.sendRequest(sendLoad);
		},
		//相关点击事件---抢红包记录
		recordClick: function recordClick() {
			var that = this;
			$('#btn_select').on('click', function() {
				var str = {
					"pageIndex": 1,
					"pageNum": 5,
					"pageSize": 10,
					"userName": "",
					"startTime": "",
					"endTime": ""
				};
				str.userName = $('#userName').val().trim();
				str.startTime = $('.labelList .startTime').val();
				str.endTime = $('.labelList .endTime').val();
				var checkData = [str.userName, str.startTime, str.endTime];
				that.dataStore.prevStr = str;
				var selectList = {
					type: 'get',
					data: str,
					dataType: 'json',
					url: '/redPacketRecord/queryRedPacketRecordList',
					success: function(data) {
						that.recordTemp(data.body.list);
						if(data.body.list == null || data.body.list == "") {
							$('#pageHbrecord').jqPaginator('option', {
								totalPages: 1,
								currentPage: 1
							});
							$('#paging_hbrecord .sumRecord').html(0);
						} else {
							$('#pageHbrecord').jqPaginator('option', {
								totalPages: data.body.pages
							});
							$('#paging_hbrecord .sumRecord').html(data.body.total);
						}
					}
				};
				base.sendRequest(selectList);
			});
			//刷新当前页
			$('#recordRefrech').on('click', function() {
				that.tipsContent("刷新中...", 1000);
				that.recordLoad();
			});
			//页面显示数据条数
			$('#paging_hbrecord .pageNum').on('change', function() {
				$('#pageHbrecord').jqPaginator('option', {
					currentPage: 1
				});
				that.dataStore.numRecord = 1;
				that.dataStore.pageNumRecord = parseInt(that.getVal($(this)));
				that.recordLoad();
			});
		},
		//字符串拼接---抢红包记录
		recordTemp: function(list) {
			var html = '';
			if(list == "" || list == null) {
				html += '<tr><td colspan="7">\u6682\u65E0\u6570\u636E</td></tr>';
			} else {
				for(var i = 0; i < list.length; i++) {
					var obj = list[i];
					//					html += '<tr><td>' + (obj.userName ? obj.userName : "-") + '</td><td>' + (obj.redPacketTitle ? obj.redPacketTitle : "-") + '</td><td><span class="c_red">' + (obj.redPacketMoney ? obj.redPacketMoney : "-") + '</span></td><td>' + (obj.createTime ? obj.createTime : "-") + '</td><td>' + (obj.ip ? obj.ip : "-") + '</td><td><span class="c_green">' + (obj.stateName ? obj.stateName : "-") + '</span></td><td><span class="c_blue">\u5904\u7406</span></td></tr>';
					html += '<tr><td>' + (obj.userName ? obj.userName : "-") + '</td><td>' + (obj.redPacketTitle ? obj.redPacketTitle : "-") + '</td><td><span class="c_red">' + (obj.redPacketMoney ? obj.redPacketMoney : "-") + '</span></td><td>' + (obj.createTime ? obj.createTime : "-") + '</td><td>' + (obj.ip ? obj.ip : "-") + '</td><td>已派发</td></tr>';
				}
			}
			$('#recordList').html(html);
		},
		//日期按钮设置
		setDateInput: function setDateInput() {
			laydate.render({
				elem: '#startTime', //指定元素
				format: 'yyyy-MM-dd HH:mm:ss',
				type: 'datetime',
				position: 'fixed'
			});
			var end = laydate.render({
				elem: '#endTime', //指定元素
				format: 'yyyy-MM-dd HH:mm:ss',
				type: 'datetime',
				position: 'fixed'
			});
			laydate.render({
				elem: '.startTime', //指定元素
				format: 'yyyy-MM-dd HH:mm:ss',
				type: 'datetime',
				position: 'fixed'
			});
			laydate.render({
				elem: '.endTime', //指定元素
				format: 'yyyy-MM-dd HH:mm:ss',
				type: 'datetime',
				position: 'fixed'
			});
			//發紅包vip等級加載
			var getVip = {
				type: 'get',
				data: {},
				url: '/userVIP/selectVips',
				success: function success(data) {
					//console.log(data);
					if(data.code == 200) {
						var html = "";
						html += '<span><i class="checkbox active" data-index="' + data.body[0].vipId + '"></i>' + data.body[0].vipName + '</span>';
						for(var i = 1; i < data.body.length; i++) {
							var obj = data.body[i];
							html += '<span><i class="checkbox" data-index="' + obj.vipId + '"></i>' + obj.vipName + '</span>';
						}
						$('#sendHb .checkboxList.memberGrade').html(html);
					}
				}
			};
			base.sendRequest(getVip);
		},
		//数据非空验证
		checkData: function checkData(data) {
			for(var i = 0; i < data.length; i++) {
				if(data[i] == '' || data[i] == null) {
					return false;
				}
			}
			return true;
		}
	};
	// 加载分页功能--红包管理
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
			hb.dataStore.num = num;
			hb.methods();
		}
	});
	// 加载分页功能--抢红包记录
	$.jqPaginator('#pageHbrecord', {
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
			hb.dataStore.numRecord = num;
			hb.recordLoad();
		}
	});
	hb.created();
});
