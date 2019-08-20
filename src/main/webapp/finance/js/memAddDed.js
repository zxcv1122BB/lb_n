'use strict';
var memAddDed = new Vue({
	el: "#container",
	data: {
		prevData: [],
		prvUser: ''
	},
	created: function() {},
	mounted: function() {},
	methods: {
		//提示框
		tipsContent: function tipsContent(str) {
			layui.use('layer', function() {
				var closeTiming = '';
				var layer = layui.layer;
				layer.msg(str, {
					time: 1000
				});
			});
		},
		//提示确认框
		checkConfirmFn: function(sendAdd) {
			var that = this;
			let operate = $("td.addDed>select").val() == 6 ? '加款' :  $("td.addDed>select").val() == 7 ? '扣款' : '赠送';
			layui.use('layer', function() {
				var layer = layui.layer;
				layer.open({
					type: 1,
					title: '添加提示',
					content: "<div style='padding:15px;box-sizing: border-box;'>确定为会员" + memAddDed.prvUser + operate + "</div>",
					area: ['22%'],
					btn: ['取消', '确认'],
					yes: function yes(index, layero) {
						layer.close(index);
					},
					btn2: function btn2(index, layero) {
                        $('.layui-layer-btn1').css('pointer-events','none');
						base.sendRequest(sendAdd);
					},
					cancel: function cancel() {}
				});
			});
		},
		//检查数据是否为空
		checkData: function checkData(data) {
			for(var i = 0; i < data.length; i++) {
				if(data[i] == "" || data[i] == null) {
					return false;
				}
			}
			return true;
		},
		getVal: function getVal(elem) {
			return elem.find("option:selected").val();
		},
		//查询
		select_btn_click: function() {
			var str = {
					userName: $('.labelList input[name="userName"]').val().trim()
				},
				that = this,
				checkStr = [str.userName];
			if(that.checkData(checkStr)) {
				var sendSelect = {
					type: 'get',
					data: str,
					dataType: 'json',
					url: '/calculate/queryUserByUserName',
					success: function success(data) {
						if(data.body == null || data.body == "") {
							that.tipsContent("找不到该会员");
						} else {
							$('.news_table .userName').html(data.body.userName);
							if(data.body.coin){
								$('.news_table .remainMoney').html(data.body.coin);
							}else{
								$('.news_table .remainMoney').html(0);
							}
							
							
							memAddDed.prvUser = data.body.userName;
						}
					}
				};
				base.sendRequest(sendSelect);
			} else {
				that.tipsContent("查询的会员账号不能为空，请填写");
			}
		},
		//添加确认
		btn_confirm_click: function() {
			var str = {
					"userName": "",
					"coinOperateType": 0,
					"coin": 0,
					"info": ""
				},
				that = this;
			str.userName = $('.news_table .userName').html();
			str.coinOperateType = parseInt(that.getVal($('.news_table .addDed')));
			str.coin = parseFloat($('.news_table .num input[name="number"]').val());
			str.info = $('.news_table .result').val();
			var checkStr = [str.userName, str.coinOperateType, str.coin, str.info];
			if(that.checkData(checkStr)) {
				if(isNaN(checkStr[2])) {
					that.tipsContent("操作金额只能为数字，请重新填写");
				} else if(checkStr[2] < 1) {
					that.tipsContent("操作金额要大于1，请重新填写");
				} else if(checkStr[3].length > 50) {
					that.tipsContent("操作原因过长，请重新输入(不大于50)");
				} else if(checkStr[2] > 100000000) {
					that.tipsContent("操作金额不能大于100000000，请重新填写")
				} else {
					var sendAdd = {
						type: 'post',
						data: str,
						dataType: 'json',
						url: '/calculate/addAndSubtractMoney',
						success: function(data) {
							if(data.code == 200) {
								that.tipsContent(data.msg);
								that.handerReset();
							} else {
								that.tipsContent(data.msg);
							}
						}
					};
					that.checkConfirmFn(sendAdd);
				}
			} else {
				if(checkStr[0] == null || checkStr[0] == "") {
					that.tipsContent("会员账号不能为空，请先查询");
				} else if(checkStr[2] == null || checkStr[2] == "") {
					that.tipsContent("操作金额不能为空，请填写");
				} else if(checkStr[3] == null || checkStr[3] == "") {
					that.tipsContent("操作原因不能为空，请填写");
				}
			}
		},
		//重置
		handerReset: function() {
			$('.labelList input[name="userName"]').val("");
			$('.news_table .userName').html("");
			$('.news_table .remainMoney').html("");
			$('.news_table .num input[name="number"]').val("");
			$('.news_table .result').val("");
		}
	},
});