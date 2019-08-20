'use strict';

$(function () {
	var dzp = {
		dataStore: {
			num: 1,
			pageNum: 50,
			awardsNum: 1,
			awardsPageNum: 50,
			recordNum: 1,
			recordPageNum: 50,
			recordPrevData: '',
			jx_isamend: false
		},
		created: function created() {
			this.setDateInput();
			this.methods();
			this.manageClick();
			this.awardsClick();
			this.recordClick();
		},
		getVal: function getVal(elem) {
			return elem.find("option:selected").val();
		},
		//提示框
		tipsContent: function tipsContent(str, times) {
			layui.use('layer', function () {
				var closeTiming = '';
				var layer = layui.layer;
				layer.msg(str, {
					time: times
				});
			});
		},
		//提示确认框
		checkConfirmFn: function checkConfirmFn(send) {
			var that = this;
			layui.use('layer', function () {
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
		//日期按钮设置
		setDateInput: function setDateInput() {
			laydate.render({
				elem: '#startTime', //指定元素
				format: 'yyyy-MM-dd HH:mm:ss',
				type: 'datetime',
				position: 'fixed'
			});
			laydate.render({
				elem: '#endTime', //指定元素
				format: 'yyyy-MM-dd HH:mm:ss',
				type: 'datetime',
				position: 'fixed'
			});
			laydate.render({
				elem: '.recordStartTime', //指定元素
				format: 'yyyy-MM-dd HH:mm:ss',
				type: 'datetime',
				position: 'fixed'
			});
			laydate.render({
				elem: '.recordEndTime', //指定元素
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
					if (data.code == 200) {
						var html = "";
						html += '<span><i class="checkbox active" data-index="' + data.body[0].vipId + '"></i>' + data.body[0].vipName + '</span>';
						for (var i = 1; i < data.body.length; i++) {
							var obj = data.body[i];
							html += '<span><i class="checkbox" data-index="' + obj.vipId + '"></i>' + obj.vipName + '</span>';
						}
						$('#sendZp .checkboxList.memberGrade').html(html);
					}
				}
			};
			base.sendRequest(getVip);
		},
		//数据非空验证
		checkData: function checkData(data) {
			for (var i = 0; i < data.length; i++) {
				if (data[i] === '' || data[i] === null ) {
					return false;
				}
			}
			return true;
		},
		//字符串拼接-----大转盘管理
		data: function data(list) {
			var html = "";
			if (list == "" || list == null) {
				html += '<tr><td colspan="10">\u6682\u65E0\u6570\u636E</td></tr>';
			} else {
				for (var i = 0; i < list.length; i++) {
					var obj = list[i];
					html += '<tr><td>' + (obj.bigTurntableTitle ? obj.bigTurntableTitle : "-") + '</td><td>' + (obj.coin ? obj.coin : '-') + '</td><td>' + (obj.limitIpNum ? obj.limitIpNum : '-') + '</td><td>' + (obj.vipName ? obj.vipName : '-') + '</td><td>' + (obj.startTime ? obj.startTime : '-') + '</td><td>' + (obj.endTime ? obj.endTime : '-') + '</td><td><div class="layui-form">                            <div class="layui-form-item">                            <div class="layui-input-block state" data-index="' + obj.bid + '" style="margin-left: 0;">                               <div data-index="1" class="layui-unselect layui-form-switch layui-form-onswitch ' + (obj.state == 1 ? 'able' : 'able hide') + '">                                   <em>\u542F\u7528</em>                                 <i></i>                               </div>                               <div  data-index="0" class="layui-unselect layui-form-switch ' + (obj.state == 0 ? 'disabled' : 'disabled hide') + '">                                   <em>\u505C\u7528</em>                                    <i></i>                                </div>                            </div>                        </div>                    </div></td><td><a href="#" data-index="' + obj.bid + '" class="layui-btn layui-btn-mini delete  layui-btn-danger">\u5220\u9664</a></td></tr>';
				}
			}
			$('#dataList').html(html);
		},
		//数据加载-----大转盘管理
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
				url: '/bigTurntable/queryBigTurntableList',
				dataType: 'json',
				success: function success(data) {
					that.data(data.body.list);
					$('#page').jqPaginator('option', {
						totalPages: data.body.pages ? data.body.pages : 1
					});
					$('#paging .sumRecord').html(data.body.total);
				}
			};
			base.sendRequest(load);
		},
		//点击事件---------大转盘管理
		manageClick: function manageClick() {
			var that = this;
			//添加转盘
			$('#addZp').on('click', function () {
				$('#sendZp .errorMsg').addClass('hide');
				layui.use('layer', function () {
					var layer = layui.layer;
					layer.open({
						type: 1,
						title: '添加大转盘',
						offset: '25%',
						area: ['50%','70%'],
						content: $('#sendZp'),
						btn: ['关闭', '保存'],
						yes: function yes(index, layero) {
							layer.close(index);
						},
						btn2: function btn2(index, layero) {
							var str = {
								"bigTurntableTitle": "",
								"amount": 0,
								"limitIpNum": 0,
								"vipId": 0,
								"startTime": "",
								"endTime": ""
							};
							str.bigTurntableTitle = $('.title').val();
							str.coin = $('.money').val() ? parseInt($('.money').val()) : "";
							str.limitIpNum = $('#sendZp .IPnum').val() ? parseInt($('#sendZp .IPnum').val()) : "";
							str.vipId = parseInt(memberGrade);
							str.startTime = $('#startTime').val();
							str.endTime = $('#endTime').val();
							var checkData = [str.bigTurntableTitle, //标题
							str.coin, //积分总额
							str.limitIpNum, //限制当前IP次数
							str.startTime, //开始时间
							str.endTime, //结束时间
							str.vipId //限制使用等级
							];
							if (!that.checkData(checkData)) {
								if (str.bigTurntableTitle == null || str.bigTurntableTitle == "") {
									that.tipsContent("标题不能为空", 1000);
								} else if (str.coin == "") {
									that.tipsContent("积分总额不能为空或0", 1000);
								} else if (str.limitIpNum == "") {
									that.tipsContent("限制当天IP次数不能为空或0", 1000);
								} else if (str.startTime == null || str.startTime == "") {
									that.tipsContent("开始时间不能为空", 1000);
								} else if (str.endTime == null || str.endTime == "") {
									that.tipsContent("结束时间不能为空", 1000);
								} 
							} else {
								if(str.coin<=0){
									that.tipsContent("积分总额不能小于或等于0", 1000);
								}else if(str.limitIpNum<=0){
									that.tipsContent("限制当天IP次数不能小于或等于0", 1000);
								}else if(str.startTime==str.endTime){
									that.tipsContent("开始时间与结束时间不能相同", 1000);
								}else if (str.vipId === null || str.vipId === ""|| typeof(str.vipId)==undefined) {
									that.tipsContent("限制使用等级不能为空", 1000);
								}else{
								var sendSave = {
									type: 'post',
									data: str,
									url: '/bigTurntable/addBigTurntable',
									dataType: 'json',
									success: function (data) {
											$('.title').val("");
											$('.rouletteType').val(1);
											$('.money').val("");
											$('.minMoney').val("");
											$('.IPnum').val("");
											$('.multiple').val("");
											$('.checkboxList>span>i.active').removeClass('active');
											$('.checkboxList>span>i:eq(1)').addClass('active');
											$('#startTime').val("");
											$('#endTime').val("");
										if (data.code == 200) {
											that.tipsContent("添加成功", 1000);
											layui.use('layer', function () {
												var layer = layui.layer;
												layer.closeAll("page");
											});
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
			//保存按钮	
			var memberGrade = 0;
			$('#sendZp').on('click', '.checkboxList>span', function () {
				memberGrade = $(this).children('i').attr('data-index');
				$('#sendZp .checkboxList>span>i.active').removeClass('active');
				$(this).children('i').addClass('active');
			});
			//删除			
			$('#dataList').on('click', 'a.delete', function (e) {
				e.preventDefault();
				var id = parseInt($(this).attr('data-index'));
				var str = {
					bid: id
				};
				var sendDelete = {
					type: 'post',
					data: str,
					url: '/bigTurntable/deleteBigTurntable',
					dataType: 'json',
					success: function success(data) {
						if (data.code == 200) {
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
			$('#dataList').on('click', '.state', function () {
				var str = {};
				str.bid = parseInt($(this).attr('data-index'));
				str.state = parseInt($(this).find('.hide').attr('data-index'));
				$(this).find('span').toggleClass('hide');
				var changeState = {
					type: 'post',
					data: str,
					url: '/bigTurntable/isStartBigTurntable',
					dataType: 'json',
					success: function success(data) {
						if (data.code == 200) {
							that.tipsContent('状态切换成功', 500);
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
			//限制标题长度
			$('#sendZp').on('blur', '.title', function() {
				var c = $(this).val();
				if(c.length > 10) {
					//替换非数字字符  
					var temp_amount = c.substring(0, 10);
					$(this).val(temp_amount);
					that.tipsContent('标题长度最多为10');
				}
			});
			//页面显示数据条数
			$('#paging .pageNum').on('change', function () {
				$('#page').jqPaginator('option', {
					currentPage: 1
				});
				that.dataStore.num = 1;
				that.dataStore.pageNum = parseInt(that.getVal($(this)));
				that.methods();
			});
			//大转盘管理、大转盘奖项、大转盘记录切换
			$('#nav>span').on('click', function () {
				$('#nav>span.active').removeClass('active');
				$(this).addClass('active');
				if ($(this).attr('data-index') == 1) {
					$('#rouletteManage').removeClass('hide');
					$('#rouletteAwards').addClass('hide');
					$('#rouletteRecord').addClass('hide');
				} else if ($(this).attr('data-index') == 2) {
					that.awardsLoad();
					$('#rouletteManage').addClass('hide');
					$('#rouletteAwards').removeClass('hide');
					$('#rouletteRecord').addClass('hide');
				} else {
					$('#rouletteAwards').addClass('hide');
					$('#rouletteManage').addClass('hide');
					$('#rouletteRecord').removeClass('hide');
				}
			});
			//刷新按钮
			//刷新当前页
			$('#refrech').on('click', function () {
				that.tipsContent("刷新中...", 1000);
				that.methods();
			});
		},
		//*****************************************************//
		//字符串拼接-------大转盘奖项
		awardsData: function awardsData(list) {
			var html = "";
			if(list.length>1){
                for (var i = 0; i < list.length; i++) {
                    var obj = list[i];
                    html += '<tr><td>' + (obj.id ? obj.id : '-') + '</td><td>' + (obj.bigTurntableTitle ? obj.bigTurntableTitle : '-') + '</td><td>' + (obj.prizeContent ? obj.prizeContent : '-') + '</td><td>' + (obj.prizeValue ? obj.prizeValue : '-') + '</td><td>' + (obj.prizeNum ? obj.prizeNum : '-') + '</td><td>' + (obj.prizeWeight ? obj.prizeWeight : '-') + '</td><td><div class="layui-form">                            <div class="layui-form-item">                            <div class="layui-input-block state" data-index="' + obj.id + '" style="margin-left: 0;">                               <div data-index="1" class="layui-unselect layui-form-switch layui-form-onswitch ' + (obj.state == 1 ? 'able' : 'able hide') + '">                                   <em>\u542F\u7528</em>                                 <i></i>                               </div>                               <div  data-index="0" class="layui-unselect layui-form-switch ' + (obj.state == 0 ? 'disabled' : 'disabled hide') + '">                                   <em>\u505C\u7528</em>                                    <i></i>                                </div>                            </div>                        </div>                    </div></td><td> <a href="#" class="amend layui-btn layui-btn-mini" data-index="' + obj.id + '">\u4FEE\u6539</a> <a href="#" class="delete layui-btn layui-btn-mini layui-btn-danger" data-index="' + obj.id + '">\u5220\u9664</a></td></tr>';
                }
			}else{
                html+='<tr><td colspan="6">暂无数据</td></tr>';
			}
            $('#rouletteAwardsList').html(html);
		},
		//数据加载---------大转盘奖项
		awardsLoad: function awardsLoad() {
			var that = this;
			var str = {
				"pageIndex": that.dataStore.awardsNum,
				"pageNum": that.dataStore.awardsPageNum,
				"pageSize": 10
			};
			var awardsload = {
				type: 'get',
				data: str,
				url: '/turntablePrize/queryTurntablePrizeList',
				dataType: 'json',
				success: function success(data) {
					that.awardsData(data.body.list);
					if (data.body.list) {
						$('#awardsPage').jqPaginator('option', {
							totalPages: data.body.pages ? data.body.pages : 1
						});
						$('#paging_awards .sumRecord').html(data.body.total);
					}
				}
			};
			base.sendRequest(awardsload);
		},
		//点击事件---------大转盘奖项
		awardsClick: function awardsClick() {
			var that = this;
			//添加--大转盘奖项
			$('#addAwards').on('click', function () {
				var html="";
				if (!that.dataStore.jx_isamend) {
					var getAllZpMsg={
						type:"get",
						data:{},
						url:"/bigTurntable/queryAllBigTurntable",
						success:function(data){
							html+="<option value=''>请选择大转盘</option>";
							for(var i=0;i<data.body.length;i++){
								var obj=data.body[i];
								html+="<option value='"+obj.bid+"'>"+obj.bigTurntableTitle+"</option>";
							}
							$('#allZpMsg').html(html);
							$('#sendJx .jxContent').val('');//奖励内容
							$('#sendJx .jxNum').val('');//奖励数量
							$('#sendJx .jxValue').val('');//奖励金额
							$('#sendJx .weight').val('');
						}
					};
					base.sendRequest(getAllZpMsg);
				}
				layui.use('layer', function () {
					var layer = layui.layer;
					layer.open({
						type: 1,
						title: that.dataStore.jx_isamend?'修改大转盘':'添加大转盘奖项',
						area: ['50%', '60%'],
						content: $("#sendJx"),
						btn: ['关闭', '确认'],
						cancel: function(index, layero){ 
							that.dataStore.jx_isamend=false;
						} ,
						yes: function (index, layero) {
							layer.close(index);
						},
						btn2: function (index, layero) {
							var str = {
								"prizeContent": $('#sendJx .jxContent').val(),//奖励内容
								"prizeNum": $('#sendJx .jxNum').val(),//奖励数量
								"prizeValue": $('#sendJx .jxValue').val(),//奖励金额
								"prizeWeight": $('#sendJx .weight').val(),
								"turntableId": that.getVal($('#allZpMsg'))
							},sendJx;
							function isInteger(obj) {
							 return typeof obj === 'number' && obj%1 === 0
							}
							if(str.turntableId==""){
								that.tipsContent('请选择大转盘', 500);
							}else if(isInteger(str.prizeNum)||str.prizeNum==""){
								that.tipsContent('奖励数量只能为整数', 500);
							}else if(isInteger(str.prizeValue)||str.prizeValue==""){
								that.tipsContent('奖励金额必须是数字', 500);
							}else if(isInteger(str.prizeWeight)||str.prizeWeight==""){
								that.tipsContent('权重只能为整数', 500);
							}else{
								if (that.dataStore.jx_isamend) {
									//修改
									that.dataStore.jx_isamend=false;
									str.id = that.dataStore.jx_amendId;
									str.state = that.dataStore.jx_amendState;
									sendJx = {
										type: 'post',
										data: str,
										url: '/turntablePrize/updateTurntablePrize',
										success: function (data) {
											if (data.code == 200) {
												that.awardsLoad();
												layer.closeAll('page');
												that.tipsContent('修改大转盘奖项成功', 500);
											} else {
												layer.closeAll('page');
												that.tipsContent('修改大转盘奖项失败', 500);
											}
										}
									};
								} else {
									str.prizeNum=parseInt(str.prizeNum);
									str.prizeValue=parseInt(str.prizeValue);
									str.prizeWeight=parseInt(str.prizeWeight);
									$('#sendJx .jxContent').val(""),
									$('#sendJx .jxNum').val(""),
									$('#sendJx .jxValue').val(""),
									$('#sendJx .weight').val("");
									//添加
									sendJx = {
										type: 'post',
										data: str,
										url: '/turntablePrize/addTurntablePrize',
										success: function success(data) {
											if (data.code == 200) {
												that.awardsLoad();
												layer.closeAll('page');
												that.tipsContent('添加大转盘奖项成功', 500);
											} else {
												layer.closeAll('page');
												that.tipsContent('添加大转盘奖项失败', 500);
											}
										}
									};
								}
								base.sendRequest(sendJx);	
							};
							return false;
						}
					});
				});
			});
			//启用禁用按钮设置--大转盘奖项
			$('#rouletteAwardsList').on('click', '.state', function() {
				var str = {};
				str.id = parseInt($(this).attr('data-index'));
				str.state = parseInt($(this).find('.hide').attr('data-index'));
				$(this).find('span').toggleClass('hide');
				var changeState = {
					type: 'post',
					data: str,
					url: '/turntablePrize/isStartTurntablePrize',
					dataType: 'json',
					success: function (data) {
						if(data.code == 200) {
							that.tipsContent('状态切换成功', 500);
							that.awardsLoad();
						}else{
							that.tipsContent('状态切换失败', 500);
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
			//页面显示数据条数--大转盘奖项
			$('#paging_awards .pageNum').on('change', function () {
				$('#awardsPage').jqPaginator('option', {
					currentPage: 1
				});
				that.dataStore.awardsNum = 1;
				that.dataStore.awardsPageNum = parseInt(that.getVal($(this)));
				that.awardsLoad();
			});
			//修改大转盘奖项按钮点击事件
			$('#rouletteAwards').on('click', '.amend', function () {
				var str = {
					id: parseInt($(this).attr('data-index'))
				};
				var sendDetele = {
					type: 'get',
					data: str,
					url: '/turntablePrize/queryTurntablePrizeById',
					success: function success(data) {
						//console.log(data);
						if (data.code == 200) {
							var html="",turntableId=data.body.turntableId,getAllZpMsg={
								type:"get",
								data:{},
								url:"/bigTurntable/queryAllBigTurntable",
								success:function(data){
									html+="<option value=''>请选择大转盘</option>";
									for(var i=0;i<data.body.length;i++){
										var obj=data.body[i];
										html+="<option value='"+obj.bid+"'>"+obj.bigTurntableTitle+"</option>";
									}
									$('#allZpMsg').html(html);
									$('#allZpMsg').val(turntableId);
//									$('#allZpMsg').find("option[value='"+turntableId+"']").attr("selected",true);
								}
							};
							base.sendRequest(getAllZpMsg);
							that.dataStore.jx_isamend = true;
							that.dataStore.jx_amendId = data.body.id;
							that.dataStore.jx_amendState = data.body.state;
							$('#sendJx .jxContent').val(data.body.prizeContent),
							$('#sendJx .jxNum').val(data.body.prizeNum),
							$('#sendJx .jxValue').val(data.body.prizeValue),
							$('#sendJx .weight').val(data.body.prizeWeight),
							
							$('#addAwards').trigger("click");
						} else {
							that.tipsContent('打开失败', 500);
						}
					}
				};
				base.sendRequest(sendDetele);
			});
			//删除大转盘奖项
			$('#rouletteAwards').on('click', '.delete', function () {
				var str = {
					id: parseInt($(this).attr('data-index'))
				};
				var sendDetele = {
					type: 'post',
					data: str,
					url: '/turntablePrize/deleteTurntablePrize',
					success: function success(data) {
						if (data.code == 200) {
							that.awardsLoad();
							that.tipsContent('删除成功', 500);
						} else {
							that.tipsContent('删除失败', 500);
						}
					}
				};
				that.checkConfirmFn(sendDetele);
			});
			//刷新当前页
			$('#refrechJx').on('click', function () {
				that.tipsContent("刷新中...", 1000);
				that.awardsLoad();
			});
		},
		//****************************************************//
		//字符串拼接-----大转盘记录
		recordData: function (list) {
			var html = "";
			if(list.length>0){
				for (var i = 0; i < list.length; i++) {
					var obj = list[i];
	//					html += '<tr><td>' + (obj.userName ? obj.userName : '-') + '</td><td>' + (obj.bigTurntableTitle ? obj.bigTurntableTitle : '-') + '</td><td>' + (obj.prizeContent ? obj.prizeContent : '-') + '</td><td>' + (obj.createTime ? obj.createTime : '-') + '</td><td>' + (obj.ip ? obj.ip : '-') + '</td><td>' + (obj.state ? obj.state == 1 ? '未开启' : '开启' : '-') + '</td><td><span class="layui-btn  layui-btn-mini">' + (obj.state ? obj.state == 1 ? '未开启' : '开启' : '-') + '<span></td></tr>';
					html += '<tr><td>' + (obj.userName ? obj.userName : '-') + '</td><td>' + (obj.bigTurntableTitle ? obj.bigTurntableTitle : '-') + '</td><td>' + (obj.prizeContent ? obj.prizeContent : '-') + '</td><td>' + (obj.createTime ? obj.createTime : '-') + '</td><td>' + (obj.ip ? obj.ip : '-') + '</td><td>' + (obj.state ? obj.state == 1 ? '未开启' : '开启' : '-') + '</td></tr>';
				}
			}else{
					html+='<tr><td colspan="6">暂无数据</td></tr>';
				}
			$('#recordList').html(html);
		},
		//数据加载-------大转盘记录
		recordLoad: function recordLoad(str) {
			var that = this;
			var recordload = {
				type: 'get',
				data: str,
				url: '/bigTurntableRecord/queryBigTurntableRecordList',
				dataType: 'json',
				success: function success(data) {
					that.recordData(data.body.list);
					if (data.body.list) {
						$('#recordPage').jqPaginator('option', {
							totalPages: data.body.pages ? data.body.pages : 1
						});
						$('#paging_record .sumRecord').html(data.body.total);
					}
				}
			};
			base.sendRequest(recordload);
		},
		//点击事件-------大转盘记录
		recordClick: function recordClick() {
			var that = this;
			//查询--大转盘记录
			$('#rouletteRecord').on('click', '.btn_select', function () {
				that.dataStore.recordNum = 1;
				var str = {
					pageIndex: that.dataStore.recordNum,
					pageNum: that.dataStore.recordPageNum,
					pageSize: 10,
					userName: $('#rouletteRecord .userName').val().trim(),
					startTime: $('#rouletteRecord .recordStartTime').val(),
					endTime: $('#rouletteRecord .recordEndTime').val()
				};
				that.dataStore.recordPrevData = str;
				that.recordLoad(str);
			});
			//页面显示数据条数--大转盘记录
			$('#paging_record .pageNum').on('change', function () {
				$('#recordPage').jqPaginator('option', {
					currentPage: 1
				});
				that.dataStore.recordNum = 1;
				that.dataStore.recordPageNum = parseInt(that.getVal($(this)));
				var str = {
					"pageIndex": that.dataStore.recordNum,
					"pageNum": that.dataStore.recordPageNum,
					"pageSize": 10
				};
				if (that.dataStore.recordPrevData != "") {
					that.dataStore.recordPrevData.pageIndex = that.dataStore.recordNum, that.dataStore.pageNum = that.dataStore.recordPageNum, str = that.dataStore.recordPrevData;
				}
				that.recordLoad(str);
			});
			//刷新当前页
			$('#recordRefrech').on('click', function () {
				that.tipsContent("刷新中...", 1000);
				var str = {
					"pageIndex": 1,
					"pageNum": 5,
					"pageSize": 10
				};
				that.recordLoad(str);
			});
		}

	};
	// 加载分页功能--大轉盤管理
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
			dzp.dataStore.num = num;
			dzp.methods();
		}
	});
	// 加载分页功能--大转盘奖项
	$.jqPaginator('#awardsPage', {
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
			dzp.dataStore.awardsNum = num;
			dzp.awardsLoad();
		}
	});
	// 加载分页功能--大转盘记录
	$.jqPaginator('#recordPage', {
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
			dzp.dataStore.recordNum = num;
			var str = {
				"pageIndex": dzp.dataStore.recordNum,
				"pageNum": dzp.dataStore.recordPageNum,
				"pageSize": 10
			};
			if (dzp.dataStore.recordPrevData != "") {
				dzp.dataStore.recordPrevData.pageIndex = dzp.dataStore.recordNum, dzp.dataStore.recordPrevData.pageNum = dzp.dataStore.recordPageNum, str = dzp.dataStore.recordPrevData;
			}
			dzp.recordLoad(str);
		}
	});
	dzp.created();
});