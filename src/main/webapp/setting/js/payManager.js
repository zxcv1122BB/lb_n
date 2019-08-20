
let type = 1;
	app = new Vue({
	el: '#main',
	data: {
		total: '', //总页数
		pageNum: 50, //每页显示条数
		datas: [],  //列表集合
		fastDatas:[],
		blankDatas:[],
		zGroups:[], //等级集合
		payTypeLists: [], //在线支付第三方支付方式
		payOnlineLists: [], //在线支付方式
		payOnline:[],
		payOnlineChi:[],

		payMes: '', //修改
		pay_state: 1, //状态
		is_default: 1, //是否默认
		show_mode: 0, //显示端
		payment_mode: '', //第三方支付方式
		shop_num: '', //商户编码
		shop_token: '', //商户密钥
		pay_account: '', //支付端账户
		pay_address:'',
		appId: '',
		min_money: '', //最小金额
		max_money: '', //最大金额
		callurl: '', //接口域名
		payicourl: 0, //支付图标地址----改
		payment_gateway: '', //支付网关
		order: '', //排序
		methname: 1, //在线支付
		
		pay_name: "",//快速入款
		account_name:'',   //账号名称
		QPcode_url:'',   //教程图片地址
		show_page:'',   //前段显示名称
		QRcode:'',   //二维码图片
		compname:'',   //所属公司
		coinurl:0,     //图标地址
		
		
		bank_account:'',//银行卡号
		account_adr:'',    //银行地址
		bank_name:'',
		
		vipLevel:[],   //限制等级
		
		submitFlag:false,    //是否能提交
		
		info:''    ,//备注
		
		
	},
	created: function() {
		this.$nextTick(function() {
			this.getlayer();
		})
		//初始化图片路径
		this.payicourl_list=["images/logoIcon/alipay.jpg","images/logoIcon/wxpay.jpg","images/logoIcon/wypay.jpg"];
	},
	methods: {
		//加载layer
		getlayer() {
			layui.use('layer', function() {
				let layer = layui.layer;
			})
		},
		//初始化数据
		getdatas: function(num) {
			let _this = this,
			data = {
				pageIndex: num,
				pageNum: parseInt(this.pageNum),
				pageSize: 10,
				payType:1
			},
			obj = {
				type: 'post',
				data: data,
				dataType: 'json',
				//url: '/pay/queryPayList',
				url: '/payType/queryPayList',
				success: function(data) {
					if(data.code == 200) {
						type = 1;
						_this.total = data.body.total;
						_this.datas = data.body.list;
						if(data.body.list.length > 0) {
							$('#fenye').jqPaginator('option', {
								totalPages: data.body.pages, //返回总页数
								currentPage: 1 //当前页
							});
						} else {
							$('#fenye').jqPaginator('option', {
								totalPages: 1, //返回总页数
								currentPage: 1 //当前页
							});
						}
					}else{
						_this.datas = [];
						$('#fenye').jqPaginator('option', {
							totalPages: 1, //返回总页数
							currentPage: 1 //当前页
						});
						layer.msg(data.msg)
					}
				},
				error: function(msg) {
					console.log(msg);
				}
			};
			base.sendRequest(obj);
		},
		//初始化数据
		getFastdatas: function(num) {
			let _this = this;
			data = {
				pageIndex: num,
				pageNum: parseInt(this.pageNum),
				pageSize: 10,
				payType: 3
			};
			obj = {
				type: 'post',
				data: data,
				dataType: 'json',
				// url: '/payQuick/queryPayList',
				url: '/payType/queryPayList',
				success: function(data) {
					if(data.code == 200) {
						type = 2;
						_this.total = data.body.total;
						_this.fastDatas = data.body.list;
						if(data.body.list.length > 0) {
							$('#fenye').jqPaginator('option', {
								totalPages: data.body.pages, //返回总页数
								currentPage: 1 //当前页
							});
						} else {
							$('#fenye').jqPaginator('option', {
								totalPages: 1, //返回总页数
								currentPage: 1 //当前页
							});
						}
					} else {
						_this.fastDatas = [];
						$('#fenye').jqPaginator('option', {
							totalPages: 1, //返回总页数
							currentPage: 1 //当前页
						});
						layer.msg(data.msg)
					}
				},
				error: function(msg) {
					console.log(msg);
				}
			};
			base.sendRequest(obj);
		},
		//初始化数据
		getBlankdatas: function(num) {
			let _this = this;
			data = {
				pageIndex: num,
				pageNum: parseInt(this.pageNum),
				pageSize: 10,
				payType: 2
			};
			obj = {
				type: 'post',
				data: data,
				dataType: 'json',
				// url: '/payBlank/queryPayList',
				url: '/payType/queryPayList',
				success: function(data) {
					if(data.code == 200) {
						type = 3;
						_this.total = data.body.total;
						_this.blankDatas = data.body.list;
						if(data.body.list.length > 0) {
							$('#fenye').jqPaginator('option', {
								totalPages: data.body.pages, //返回总页数
								currentPage: 1 //当前页
							});
						} else {
							$('#fenye').jqPaginator('option', {
								totalPages: 1, //返回总页数
								currentPage: 1 //当前页
							});
						}
					} else {
						_this.blankDatas = [];
						$('#fenye').jqPaginator('option', {
							totalPages: 1, //返回总页数
							currentPage: 1 //当前页
						});
						layer.msg(data.msg)
					}
				},
				error: function(msg) {
					console.log(msg);
				}
			};
			base.sendRequest(obj);
		},
		//获得已选择的等级
		getvipLevel:function(){
			let _this = this;
			for(var i= 0;i<_this.zGroups.length;i++){
				for(var j =0;j<_this.vipLevel.length;j++){
					if(_this.zGroups[i].vipName == _this.vipLevel[j]){
						$("#"+_this.zGroups[i].vipName).attr("checked","true");
					}
				}
			}
		},

		//弹出层
		tanMessage: function(typeId,item) {
			let _this = this;
			_this.submitFlag = false;
			_this.sendGroup();
			_this.getPayOnLineList();
			if(typeId==1){
				$("#onlinePay").attr("disabled","disabled");
//				_this.getPaytypeList(item.id);
				_this.methname = item.methname;
				_this.compname = item.compname;
				_this.max_money = item.max_money;
				_this.min_money = item.min_money;
				_this.shop_name = item.shop_name;
				_this.shop_num = item.shop_num;
				_this.pay_account = item.pay_account;
				_this.pay_address = item.callbackurl;
				_this.payment_gateway = item.payment_gateway;
				_this.order = item.pay_order;
				_this.callurl = item.callurl;
				// _this.appId = item.APPID;
				_this.shop_token = item.shop_token;
				

				if(item.payico_url){
					_this.payicourl = _this.payicourl_list.indexOf(item.payico_url.trim());
				}

				_this.payment_gateway = item.payment_gateway;
				_this.info=item.info;

				if(item.quit_level){
					_this.vipLevel = item.quit_level.split(",");
				}else{
					_this.vipLevel = [];
				}
				_this.getvipLevel();
				// if(!item.pay_state) {
				// 	_this.pay_state = "1";
				// } else {
					_this.pay_state = item.pay_state;
				// }
				// if(!item.show_mode) {
				// 	_this.show_mode = "0";
				// } else {
					_this.show_mode = item.show_mode;
				// }
				// if(!item.methname) {
				// 	_this.methname = "0"
				// } else {
					_this.methname = item.methname;
				// }
				// if(!item.is_default) {
				// 	_this.is_default = "1"
				// } else {
					_this.is_default = item.is_default;
				// }
				// if(!item.payment_mode) {
				// 	_this.payment_mode = '';
				// } else {
					_this.payment_mode = item.payment_mode;
				// }
			}else{
				$("#onlinePay").removeAttr("disabled","disabled");
				_this.methname = '0';
				_this.compname = '';
				_this.max_money = '';
				_this.min_money = '';
				_this.shop_name = '';
				_this.shop_num = '';
				_this.pay_account = '';
				_this.pay_address = '';
				_this.payment_mode = '';
				_this.show_mode = '0';
				_this.payment_gateway = '';
				_this.pay_state = '1';
				_this.is_default = '1';
				_this.order = '';
				_this.callurl = '';
				// _this.appId = '';
				_this.shop_token = '';
				_this.payicourl = 0;
//				_this.getPaytypeList('');
				_this.vipLevel = [];
			}
			layer.open({
				title: '在线入款设置',
				type: 1,
				content: $('.payEdit'),
				area: ['70%', '75%'],
				btn: ['保存', '关闭'],
				yes: function() {
					_this.noNull(1);
					if(_this.submitFlag){
						if(typeId==0){
							_this.insertMes()
						}else{
							_this.updateMes(item.id);
						}
					}
				},
				btn2: function() {
					layer.closeAll();
				}
			})
		},
		//添加
		insertMes: function() {
			this.compname = $("#onlinePay option:selected").text();
			// 在线支付        pay_type   1 在线支付
			// 前端显示的字段:
			// 在线支付方式	 compname
			// 商户编码	     shop_num
			// 商户密钥 / 商户识别码	 shop_token
			// 状态			 pay_state   是否启用, 1启用, 0禁止
			// 最小支付金额	 min_money
			// 最大支付金额	 max_money
			// 接口域名	     callurl
			// 支付通知地址	 callbackurl
			// 支付图标地址	 payico_url
			// 第三方支付方式	 payment_mode   0 微信 1 支付宝
			// 排序             pay_order
			// 限制使用等级	 quit_level
			// 显示类型		 show_mode   0所有终端都显示, 1, pc端显示, 2, 手机端显示, 3, APP端显示
			// 备注			 info
			// 支付类型         pay_type   1 在线支付 2 银行转账 3 快捷支付
			let _this = this,
			data = {
				methname: _this.methname,
				compname: _this.compname,
				max_money: _this.max_money,
				min_money: _this.min_money,
				shop_token: _this.shop_token,
				shop_num: _this.shop_num,
				// pay_account: _this.pay_account,
				callbackurl:_this.pay_address,
				payment_mode: _this.payment_mode,
				show_mode: _this.show_mode,
				paymentGateway: _this.payment_gateway,
				pay_state: _this.pay_state,
				isDefault: _this.is_default,
				pay_order: _this.order,
				callurl: _this.callurl,
				// appid: _this.appId,
				// pay_order: _this.shop_token,
				payico_url: _this.payicourl_list[_this.payicourl],
				quit_level:_this.vipLevel.join(','),
				info: _this.info,
				//区分类型1-在线支付 2-银行入款 3-快捷支付
				pay_type:1
			},
			obj = {
				type: 'post',
				data: data,
				dataType: 'json',
				// url: '/pay/addPayInfo',
				url: '/payType/addPayType',
				success: function(data) {
					if(data.code == 200) {
						layer.closeAll();
						layer.msg("添加成功！")
						setTimeout(function() {
							window.location.reload();
						}, 500)
					} else {
						layer.msg("添加失败！")
					}
				},
				error: function(msg) {
					layer.msg(msg);
				}
			};
			base.sendRequest(obj);
		},
		//修改数据
		updateMes: function(id) {
			let _this = this;
			data = {
				id:id,
				methname: _this.methname,
				compname: _this.compname,
				max_money: _this.max_money,
				min_money: _this.min_money,
				shop_token: _this.shop_token,
				shop_num: _this.shop_num,
				// pay_account: _this.pay_account,
				callbackurl: _this.pay_address,
				payment_mode: _this.payment_mode,
				show_mode: _this.show_mode,
				paymentGateway: _this.payment_gateway,
				pay_state: _this.pay_state,
				isDefault: _this.is_default,
				pay_order: _this.order,
				callurl: _this.callurl,
				// appid: _this.appId,
				payico_url: _this.payicourl_list[_this.payicourl],
				quit_level: _this.vipLevel.join(','),
				info: _this.info,
				
			};
			obj = {
				type: 'post',
				data: data,
				dataType: 'json',
				// url: '/pay/updatePayInfo',
				url: '/payType/updatePayType',
				success: function(data) {
					if(data.code == 200) {
						layer.closeAll();
						layer.msg("修改成功！")
						setTimeout(function() {
							window.location.reload();
						}, 500)
					} else {
						layer.msg("修改失败！")
					}
				},
				error: function(msg) {
					layer.msg(msg);
				}
			};
			base.sendRequest(obj);
		},
		//删除数据
		deleteMes: function(id) {
			console.log(id);
			let _this = this;
			layer.confirm('确认删除吗？', {
				title: '提示'
			}, function() {
				let data = {
					id: id,
				};
				obj = {
					type: 'post',
					data: data,
					dataType: 'json',
					// url: '/pay/delPayInfoByid',
					url: '/payType/delPayTypeByid',
					success: function(data) {
						if(data.code == 200) {
							layer.msg("删除成功！");
							setTimeout(function() {
								window.location.reload();
							}, 500)
						} else {
							layer.msg("删除失败！");
						}
					},
					error: function(msg) {
						layer.msg(msg);
					}
				};
				base.sendRequest(obj);
			});

		},
		//弹出层
		tanGeneralMessage: function(typeId,item) {
			let _this = this;
			_this.submitFlag = false;
			_this.sendGroup();
			if(typeId==1){
				$("#bankPay").attr("disabled","disabled");
				_this.bank_name = item.bank_name;
				_this.info = item.info;
				_this.bank_account = item.bank_account;
				_this.account_name = item.account_name;
				_this.account_adr = item.account_adr;
				_this.max_money = item.max_money;
				_this.min_money = item.min_money;
				_this.order = item.pay_order;

				_this.payicourl = _this.payicourl_list.indexOf(item.payico_url.trim());
				_this.show_mode = item.show_mode;
				if(item.quit_level){
					_this.vipLevel = item.quit_level.split(",");
				}else{
					_this.vipLevel = [];
				}
				_this.getvipLevel();
				// if(!item.pay_status) {
				// 	_this.pay_state = "1";
				// } else {
				// 	_this.pay_state = item.pay_status;
				// }
			}else{
				$("#bankPay").removeAttr("disabled","disabled");
				_this.bank_name = '';
				_this.info = '';
				_this.bank_account = '';
				_this.account_name = '';
				_this.account_adr = '';
				_this.max_money =  '';
				_this.min_money = '';
				_this.order = '';
				_this.payicourl = 0;
				_this.vipLevel=[];
				_this.pay_state = "1";
			}
			layer.open({
				title: '银行入款设置',
				type: 1,
				content: $('.generalDeposit'),
				area: ['60%', '95%'],
				btn: ['保存', '关闭'],
				yes: function() {
					_this.noNull(2)
					if(_this.submitFlag){
						if(typeId==0){
							_this.insertBlankMes()
						}else{
							_this.updateBlankMes(item.id);
						}
					}
				},
				btn2: function() {
					layer.closeAll();
				}
			})
		},
		//添加
		insertBlankMes: function() {
		
			let _this = this,
			data = {
				bank_name : _this.bank_name,
				bank_account: _this.bank_account,
				account_name: _this.account_name,
				account_adr: _this.account_adr,
				min_money: _this.min_money,
				max_money: _this.max_money,
				pay_order: _this.order,
				pay_state: _this.pay_state,
				payico_url: _this.order,
				info:_this.info,
				quit_level:_this.vipLevel.join(','),

				show_mode: _this.show_mode,
				pay_type:2
			},
			obj = {
				type: 'post',
				data: data,
				dataType: 'json',
				// url: '/payBlank/addPayInfo',
				url: '/payType/addPayType',
				success: function(data) {
					if(data.code == 200) {
						layer.closeAll();
						layer.msg("添加成功！")
						setTimeout(function() {
							window.location.reload();
						}, 500)
					} else {
						layer.msg("添加失败！")
					}
				},
				error: function(msg) {
					layer.msg(msg);
				}
			};
			console.log(data);
			base.sendRequest(obj);
		},
		//修改数据
		updateBlankMes: function(id) {
			let _this = this,
			data = {
				id : id,
				bank_name: _this.bank_name,
				bank_account: _this.bank_account,
				account_name: _this.account_name,
				account_adr: _this.account_adr,
				min_money: _this.min_money,
				max_money: _this.max_money,
				pay_order: _this.order,
				pay_state: _this.pay_state,
				payico_url: _this.payicourl_list[_this.payicourl],
				info: _this.info,
				quit_level: _this.vipLevel.join(','),

				show_mode: _this.show_mode,
			},
			obj = {
				type: 'post',
				data: data,
				dataType: 'json',
				// url: '/payBlank/updatePayInfo',
				url: '/payType/updatePayType',
				success: function(data) {
					if(data.code == 200) {
						layer.closeAll();
						layer.msg("修改成功！")
						setTimeout(function() {
							window.location.reload();
						}, 500)
					} else {
						layer.msg("修改失败！")
					}
				},
				error: function(msg) {
					layer.msg(msg);
				}
			};
			console.log(data);
			base.sendRequest(obj);
		},
		//删除数据
		deleteBlankMes: function(id) {
			console.log(id);
			let _this = this;
			layer.confirm('确认删除吗？', {
				title: '提示'
			}, function() {
				let data = {
					id: id,
				};
				obj = {
					type: 'post',
					data: data,
					dataType: 'json',
					// url: '/payBlank/delPayInfoByid',
					url: '/payType/delPayTypeByid',
					success: function(data) {
						if(data.code == 200) {
							layer.msg("删除成功！");
							setTimeout(function() {
								window.location.reload();
							}, 500)
						} else {
							layer.msg("删除失败！");
						}
					},
					error: function(msg) {
						layer.msg(msg);
					}
				};
				base.sendRequest(obj);
			});

		},
		//弹出层
		tanFastMessage: function(typeId,item) {
			let _this = this;
			_this.submitFlag = false;
			_this.sendGroup();
			if(typeId==1){
				$("#fastPay").attr("disabled","disabled");
				_this.pay_name = item.payment_mode;
				_this.compname = item.compname;
				_this.account_name = item.account_name;
				_this.max_money = item.max_money;
				_this.min_money = item.min_money;
				_this.pay_account = item.bank_account;
				_this.QPcode_url = item.QPcode_url;
				_this.order = item.pay_order;
				_this.show_page = item.show_page;
				_this.QRcode = item.QRcode;
				// _this.coinurl = item.coin_url;
				_this.info = item.info;
				_this.pay_state = item.pay_state;
				_this.show_mode = item.show_mode;
				//--改
				_this.coinurl = _this.payicourl_list.indexOf(item.payico_url.trim());

				if(item.quit_level){
					_this.vipLevel = item.quit_level.split(",");
				}else{
					_this.vipLevel = [];
				}
				_this.getvipLevel();
				// if(!item.pay_status) {
				// 	_this.pay_state = "1";
				// } else {
				// 	_this.pay_state = item.pay_status;
				// }
			}else{
				$("#fastPay").removeAttr("disabled","disabled");
				_this.compname = '';
				_this.info = '';
				_this.pay_name = "";
				_this.account_name = "";
				_this.max_money = "";
				_this.min_money = "";
				_this.pay_account = "";
				_this.QRcode = "";
				_this.QPcode_url = "";
				_this.order = "";
				_this.show_page = "";
				_this.payicourl = 0;
				_this.coinurl = 0;
				_this.vipLevel=[];
				_this.pay_state = "1";
			}
			layer.open({
				title: '',
				type: 1,
				content: $('.fastDeposit'),
				area: ['70%', '95%'],
				btn: ['保存', '关闭'],
				yes: function() {
					_this.noNull(3)
					if(_this.submitFlag){
						if(typeId==0){
							_this.insertFastMes()
						}else{
							_this.updateFastMes(item.id);
						}
					}
				},
				btn2: function() {
					layer.closeAll();
				}
			})
		},
		//添加
		insertFastMes: function() {
			let _this = this,
			data = {
				compname: _this.compname,
				payment_mode: _this.pay_name,
				bank_account: _this.pay_account,
				max_money: _this.max_money,
				min_money: _this.min_money,
				account_name: _this.account_name,
				QPcode_url: _this.QPcode_url,
				pay_state: _this.pay_state,
				pay_order: _this.order,
				QRcode: _this.QRcode,
				show_page: _this.show_page,
				quit_level: _this.vipLevel.join(','),
				info: _this.info,
				payico_url: _this.payicourl_list[_this.coinurl],
				coinurl:_this.payicourl_list[_this.coinurl],
				show_mode: _this.show_mode,
				
				pay_type:3
			},
			obj = {
				type: 'post',
				data: data,
				dataType: 'json',
				// url: '/payQuick/addPayInfo',
				url: '/payType/addPayType',
				success: function(data) {
					if(data.code == 200) {
						layer.closeAll();
						layer.msg("添加成功！")
						setTimeout(function() {
							window.location.reload();
						}, 500)
					} else {
						layer.msg("添加失败！")
					}
				},
				error: function(msg) {
					layer.msg(msg);
				}
			};
			base.sendRequest(obj);
		},
		//修改数据
		updateFastMes: function(id) {
			let _this = this;
			 
			data = {
				id : id,
				compname : _this.compname,
				payment_mode: _this.pay_name,
				bank_account: _this.pay_account,
				max_money: _this.max_money,
				min_money: _this.min_money,
				account_name: _this.account_name,
				QPcode_url: _this.QPcode_url,
				pay_state: _this.pay_state,
				pay_order: _this.order,
				QRcode: _this.QRcode,
				show_page: _this.show_page,
				quit_level:_this.vipLevel.join(','),
				info:_this.info,
				payico_url:_this.payicourl_list[_this.coinurl],
				show_mode: _this.show_mode,
			},
			obj = {
				type: 'post',
				data: data,
				dataType: 'json',
				// url: '/payQuick/updatePayInfo',
				url: '/payType/updatePayType',
				success: function(data) {
					if(data.code == 200) {
						layer.closeAll();
						layer.msg("修改成功！")
						setTimeout(function() {
							window.location.reload();
						}, 500)
					} else {
						layer.msg("修改失败！")
					}
				},
				error: function(msg) {
					layer.msg(msg);
				}
			};
			base.sendRequest(obj);
		},
		//删除数据
		deleteFastMes: function(id) {
			console.log(id);
			let _this = this;
			layer.confirm('确认删除吗？', {
				title: '提示'
			}, function() {
				let data = {
					id: id,
				};
				obj = {
					type: 'post',
					data: data,
					dataType: 'json',
					// url: '/payQuick/delPayInfoByid',
					url: '/payType/delPayTypeByid',
					success: function(data) {
						if(data.code == 200) {
							layer.msg("删除成功！");
							setTimeout(function() {
								window.location.reload();
							}, 500)
						} else {
							layer.msg("删除失败！");
						}
					},
					error: function(msg) {
						layer.msg(msg);
					}
				};
				base.sendRequest(obj);
			});

		},
		//表单验证
		noNull:function(id){
			let _this = this;
				reg =  /^[\u4e00-\u9fa5]+$/;     //必须为中文
				moneyReg = /^\d+(\.\d{1,2})?$/;
//				bankReg = /^(\d{16}|\d{19})$/;
			if(id==1){
				if(_this.methname==0){
					layer.msg("请选择在线支付方式")
					return;
				}else if(_this.shop_num == ""){
					layer.msg("商户编码不能为空")
					return;
				}else if(_this.min_money==""){
					layer.msg("最小金额不能为空")
					return;
				}else if(_this.pay_address == ""){
					layer.msg("支付通知地址不能为空")
					return;
				}else if(_this.max_money==""){
					layer.msg("最大金额不能为空")
					return;
				}else if(_this.callurl == ""){
					layer.msg("接口域名不能为空")
					return;
				}else{
					if(!moneyReg.test(_this.min_money)||!moneyReg.test(_this.max_money)){
						layer.msg("金额只能为数字")
						return;
					}else if(parseFloat(_this.min_money)>=_this.max_money){
						layer.msg("最小金额必须大于最大金额")
						return;
					} 
					// else if (_this.payicourl != undefined && _this.payicourl.length > 200){
					// 		layer.msg("图标地址最长为200字符")
					// 		return;
					// }
					else{
						_this.submitFlag = true;
					}
				}
				
			}else if(id==2){
				if(_this.bankName==""){
					layer.msg("银行名称不能为空")
					return;
				}else if(_this.bank_account==""){
					layer.msg("银行卡号不能为空")
					return;
				}else if(_this.account_name==""){
					layer.msg("持卡人姓名不能为空")
					return;
				}else if(_this.account_adr==""){
					layer.msg("银行地址不能为空")
					return;
				}else if(_this.min_money==""){
					layer.msg("最小金额不能为空")
					return;
				}else if(_this.max_money==""){
					layer.msg("最大金额不能为空")
					return;
				}
				// else if(_this.payicourl==""){
				// 	layer.msg("图标地址不能为空")
				// 	return;
				// }
				else{
					if(!reg.test(_this.bank_name)){
						layer.msg("银行名称只能为中文")
						return;
					}
//					else if(!bankReg.test(_this.bank_account)){
//						layer.msg("请输入正确的银行卡号")
//						return;
//					}
					else if(!reg.test(_this.account_name)){
						layer.msg("持卡人姓名只能为中文")
						return;
					}else if(!moneyReg.test(_this.min_money)||!moneyReg.test(_this.max_money)){
						layer.msg("金额只能为数字")
						return;
					}else if(parseFloat(_this.min_money)>=_this.max_money){
						layer.msg("最小金额必须大于最大金额")
						return;
					}
					// else if(_this.payicourl.length>200){
					// 	layer.msg("图标地址最长为200字符")
					// 	return;
					// } 
					else{
						_this.submitFlag = true;
					}
				}
			}else{
				
				if(_this.pay_name === "" ){
					layer.msg("请选择快速入款方式")
					return;
				}else if(_this.pay_account == ""){
					layer.msg("账号不能为空")
					return;
				}
				// else if(_this.pay_address == ""){
				// 	layer.msg("支付通知地址不能为空")
				// 	return;
				// }
				else if(_this.account_name == ""){
					layer.msg("账户姓名不能为空")
					return;
				}else if(_this.min_money==""){
					layer.msg("最小金额不能为空")
					return;
				}else if(_this.max_money==""){
					layer.msg("最大金额不能为空")
					return;
				}else if(_this.QRcode==""){
					layer.msg("二维码地址不能为空")
					return;
				}else if(_this.QPcode_url == ""){
					layer.msg("图标地址不能为空")
					return;
				}
				else if(_this.show_page == ""){
					layer.msg("前端显示名称不能为空")
					return;
				}
				else{
					if(!reg.test(_this.account_name.trim())){
						layer.msg("账户姓名只能为中文")
						return;
					}else if(!moneyReg.test(_this.min_money)||!moneyReg.test(_this.max_money)){
						layer.msg("金额只能为数字")
						return;
					}else if(parseFloat(_this.min_money)>=_this.max_money){
						layer.msg("最小金额必须大于最大金额")
						return;
					} else if(_this.QPcode_url.length>200){
						layer.msg("图标地址最长为200字符")
						return;
					}else if(_this.QRcode.length>200){
						layer.msg("二维码地址最长为200字符")
						return;
					}else{
						_this.submitFlag = true;
					}
				}
			}
		},
		//获取vip等级
        sendGroup: function () {
            let _this = this;
            let options = {
                type: "get",
                async: false,
                url: "/user/queryAllUsername",
                data: {},
                dataType: "json",
                success: function (data) {
                    if (data.code == 200) {
                        _this.zGroups = data.body;
                    }
                },
                error: function (msg) {
                    console.log(msg);
                }
            };
            base.sendRequest(options);
        },
        //第三方支付方式
		getPaytypeList: function(id) {
			let _this = this;
			data = {
				methodid: id,
			};
			obj = {
				type: 'post',
				data: data,
				dataType: 'json',
				url: '/pay/queryPaytypeListByMethid',
				success: function(data) {
					if(data.code == 200) {
						_this.payTypeLists = data.body;
					}
				},
				error: function(msg) {
					console.log(msg);
				}
			};
			base.sendRequest(obj);
		},
		//在线支付方式
		getPayOnLineList: function() {
			let _this = this;
			_this.payOnline = [];
			data = {

			};
			obj = {
				type: 'post',
				data: data,
				dataType: 'json',
				url: '/pay/queryPayOnlineNames',
				success: function(data) {
					if(data.code == 200) {
						_this.payOnlineLists = data.body;
						for(var key in _this.payOnlineLists) {
						  _this.payOnline.push({"value":_this.payOnlineLists[key],key});
						}
					}
				},
				error: function(msg) {
					console.log(msg);
				}
			};
			base.sendRequest(obj);
		},
		changeStatus:function(item,id){
			var _this = this;
			if(id==1){     //银行入款
				_this.bank_name = item.bank_name
				_this.bank_account = item.bank_account;
				_this.account_name = item.account_name;
				_this.account_adr = item.account_adr;
				_this.max_money = item.max_money;
				_this.min_money = item.min_money;
				_this.order = item.pay_order;

				_this.payicourl = _this.payicourl_list.indexOf(item.payico_url.trim());
				_this.show_mode = item.show_mode;
				_this.info = item.info;
				if(item.quit_level){
					_this.vipLevel = item.quit_level.split(",");
				}else{
					_this.vipLevel = [];
				}
				_this.getvipLevel();
				if (!item.pay_state || item.pay_state=='1') {
					_this.pay_state = "0";
				} else {
					_this.pay_state = '1';
				}
				_this.updateBlankMes(item.id);
			}else if(id==2){     //快速支付
				_this.pay_name = item.pay_name;
				_this.compname = item.comp_name;
				_this.account_name = item.account_name;
				_this.max_money = item.max_money;
				_this.min_money = item.min_money;
				_this.pay_account = item.pay_account;
				_this.QPcode_url = item.QPcode_url;
				_this.order = item.pay_order;
				_this.show_page = item.show_page;
				_this.QRcode = item.QRcode;
				_this.info=item.info;
				_this.coinurl=_this.payicourl_list.indexOf(item.payico_url.trim());
				_this.show_mode = item.show_mode;
				if(item.quit_level){
					_this.vipLevel = item.quit_level.split(",");
				}else{
					_this.vipLevel = [];
				}
				_this.getvipLevel();
				if (!item.pay_state || item.pay_state=='1') {
					_this.pay_state = "0";
				} else {
					_this.pay_state = '1';
				}
				_this.updateFastMes(item.id);
			}else{      //在线支付
				_this.methname = item.methname;
				_this.compname = item.compname;
				_this.max_money = item.max_money;
				_this.min_money = item.min_money;
				_this.shop_name = item.shop_name;
				_this.shop_num = item.shop_num;
				_this.pay_account = item.pay_account;
				_this.pay_address = item.callbackurl;
				_this.payment_gateway = item.payment_gateway;
				_this.order = item.pay_order;
				_this.callurl = item.callurl;
				// _this.appId = item.appId;
				_this.shop_token = item.shop_token;
				_this.payicourl = _this.payicourl_list.indexOf(item.payicourl);
				if(item.quit_level){
					_this.vipLevel = item.quit_level.split(",");
				}else{
					_this.vipLevel = [];
				}
				_this.getvipLevel();
				if(!item.pay_state||item.pay_state=='1') {
					_this.pay_state = "0";
				} else {
					_this.pay_state = '1';
				}
				if(!item.show_mode) {
					_this.show_mode = "0";
				} else {
					_this.show_mode = item.show_mode;
				}
				if(!item.methname) {
					_this.methname = "0"
				} else {
					_this.methname = item.methname;
				}
				if(!item.is_default) {
					_this.is_default = "1"
				} else {
					_this.is_default = item.is_default;
				}
				if(!item.payment_mode) {
					_this.payment_mode = '';
				} else {
					_this.payment_mode = item.payment_mode;
				}
				_this.updateMes(item.id);
			}
		}
	},
	watch: {
		//监听页码每页条数
		pageNum: function() {
			this.getdatas(1);
		},
	}
});

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

	onPageChange: function(num) {
		if(type==2){
			app.getBlankdatas(num);
		}else if(type==3){
			app.getFastdatas(num);
		}else{
			app.getdatas(num);
		}
		console.log()
	}
});