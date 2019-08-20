/**
 * Created by ASUS on 2017/9/22.
 */
var app = new Vue({
	el: '#app',
	data: {
		datas: [], //列表数据

		id: '', //修改信息记录的id
		name: '', //修改信息记录的名称
		rmb: '', //修改信息记录的金额
		url: '', //修改信息记录的图片地址
		remark: '', //修改信息记录的备注信息
		group: '', //修改等级唯一标志
		percent: 0, //用户返利

		addname: '', //添加信息记录的名称
		addrmb: '', //添加信息记录的金额
		addurl: '', //添加信息记录的图片地址
		addremark: '', //添加信息记录的备注信息
		addgroup: '', //

		members: [], //会员列表
		vipId: '', //点击成员转移时记录的id
		total: '', //总共有多少条记录
		page_num: 50, //默认每页5条记录

		search_input: '', //绑定搜索框
		prompt3: '', //为空时的提示

		selectVip: [], //可选择的vip等级信息
		member: [], //提交要修改的会员等级数组
		select_: '', //监听修改vip等级下拉框
		flag: false, //全选默认为假
		prompt4: '', //为空时的提示
		// 存储搜索条件中输入的内容
		store_search: '',

		percentId: '',

		chooseColor: '',
		gameType:'',   //全部彩种
		sys_config1:'',   //最低返利金额
		sys_config2:[],   //选择返利彩种
		checked:'',

	},
	created: function() {
		this.$nextTick(function() {
			this.getlayer();
			this.init();
			this.getAllColor();
		})
	},
	methods: {
		//加载layer
		getlayer() {
			layui.use('layer', function() {
				let layer = layui.layer;
			})
		},
		//数据初始化
		init: function() {
			let _this = this;
			let obj = {
				type: "get",
				data: {},
				dataType: 'json',
				url: '/userVIP/selectVips',
				success: function(data) {
					////console.log(data);
					if(data.code == 200) {
						_this.datas = data.body;
					}
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(obj);
		},
		checkedAll:function(){
			let _this = this;
		    if (!_this.checked) {//实现反选
		      _this.sys_config2 = [];
		    }else{//实现全选
		      _this.sys_config2 = [];
		      _this.gameType.forEach(function(item) {
		        _this.sys_config2.push(item.gameID);
		      });
		    }
		},
		//获取所有彩种
		getAllColor:function(){
			let _this = this;
			let data = {};
			let obj_ = {
				type: 'post',
				data: data,
				dataType: 'json',
				url: '/game/qryAllGameType',
				success: function(data) {
					if(data.code == 200) {
						_this.gameType = data.body;
					} else {
					}
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(obj_);
		},
		//选择彩种返利
		choColor: function() {
			let _this = this;
			_this.selectPercent();
			_this.sys_config1 = _this.percentId.sys_config1;
			if(_this.percentId.sys_config2){
				_this.sys_config2 = _this.percentId.sys_config2.split(',');
			}
			layer.open({
				title: '选择彩种返利',
				type: 1,
				content: $('.color'),
				area: ['56%', '56%'],
				btn: ['确定', '取消'],
				yes: function(index) {
					let arr = new Array();
					if(_this.sys_config2==''){
						layer.msg('必须选择至少一个彩种');
						return
					}
					let data1 = {
						id:_this.percentId.id,
						onOff:_this.percentId.on_off,//开关 1为开启 0为关闭
						sysConfig1:_this.sys_config1,//输入字段1
						sysConfig2:_this.sys_config2.join(','),//输入字段2
						configure:"userRebate"
					};
					arr.push(data1)
					let obj_ = {
						type: 'post',
						data: {"configureList": arr},
						dataType: 'json',
						url: '/sysConfigure/updateSysConfigure',
						success: function(data) {
							if(data.code == 200) {
								layer.closeAll();
								layer.msg(data.msg);
							} else {
								layer.msg(data.msg);
							}
						},
						error: function(msg) {
							//console.log(msg);
						}
					};
					base.sendRequest(obj_);
				},
				btn2: function() {}
			})
		},
		//查看用户返利状态
		selectPercent: function() {
			let _this = this;
			let obj = {
				type: 'post',
				data: {
					configure:"userRebate"
				},
				dataType: 'json',
				url: "/sysConfigure/qryByConfigure",
				success: function(data) {
					if(data.code == 200) {
						_this.percentId = data.body;
					} else {
					}
					////console.log(data);
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(obj);
		},
		//修改用户返利状态
		upPercent: function() {
			let _this = this;
			if(_this.percentId.on_off == 0) {
				_this.percentId.on_off = 1;
			} else {
				_this.percentId.on_off = 0;
			}
//			let obj = {
//				type: 'post',
//				data: {
//					onOff: _this.percentId,
//					configure:"userRebate"
//				},
//				dataType: 'json',
//				url: "/sysConfigure/updateSysConfigure",
//				success: function(data) {
//					if(data.code == 200) {
//						layer.msg(data.msg);
//					} else {
//						layer.msg(data.msg);
//					}
//					////console.log(data);
//				},
//				error: function(msg) {
//					//console.log(msg);
//				}
//			};
//			base.sendRequest(obj);
		},
		//点击改变状态执行
		changeType: function(vipid, status, index) {
			let _this = this;
			if(status == 0) {
				status = 1;
			} else {
				status = 0;
			}
			let obj = {
				type: 'get',
				data: {
					vip_Id: vipid,
					statu: status
				},
				dataType: 'json',
				url: "/userVIP/upVip",
				success: function(data) {
					if(data.code == 200) {
						_this.datas[index].status = status;
						layer.msg(data.msg);
					} else {
						layer.msg(data.msg);
					}
					////console.log(data);
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(obj);
		},
		//点击修改信息
		popedit: function(vipid) {
			this.prompt1 = '';
			this.rmb = '';
			this.url = '';
			this.remark = '';
			this.percent = 0;
			let _this = this;
			let obj = {
				type: 'get',
				data: {
					vipId: vipid
				},
				dataType: 'json',
				url: '/userVIP/selectById',
				success: function(data) {
					////console.log(data);
					if(data.code == 200) {
						_this.id = data.body.vipid;
						_this.name = data.body.vipname;
						_this.rmb = data.body.depositamount;
						_this.url = data.body.vipicourl;
						_this.remark = data.body.remark;
						_this.percent = data.body.percent;
					}
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(obj);
			layer.open({
				title: '修改等级信息',
				type: 1,
				content: $('.popEdit'),
				area: ['30%', '75%'],
				btn: ['确定', '取消'],
				yes: function(index) {
					if(_this.name.trim() == '') {
						layer.msg('名称不能为空');
						return;
					}
					if(_this.id != 0) {
						if(_this.rmb === '' || _this.rmb === 0) {
							layer.msg('金额不能为空');
							return;
						}
					}
					if(parseInt(_this.percent) < 0 || parseInt(_this.percent) > 99.99) {
						layer.msg('返利范围为0-99.99');
						return;
					}
					let data = {
						vipid: _this.id,
						vipname: _this.name,
						depositamount: _this.rmb,
						vipicourl: _this.url,
						remark: _this.remark,
						percent: _this.percent
					};
					let obj_ = {
						type: 'post',
						data: data,
						dataType: 'json',
						url: '/userVIP/updateVIP',
						success: function(data) {
							////console.log(data);
							if(data.code == 200) {
								layer.close(index);
								layer.msg(data.msg);
								setTimeout(function() {
									window.location.reload();
								}, 1000)
							} else {
								layer.msg(data.msg);
							}
						},
						error: function(msg) {
							//console.log(msg);
						}
					};
					base.sendRequest(obj_);
				},
				btn2: function() {}
			})
		},
		//点击添加按钮执行
		popAdd: function() {
			this.prompt = '';
			this.addname = '';
			this.addrmb = '';
			this.addurl = '';
			this.addremark = '';
			this.percent = 0;
			let _this = this;
			layer.open({
				title: '添加等级信息',
				type: 1,
				content: $('.popAdd'),
				area: ['40%', '75%'],
				btn: ['确定', '取消'],
				yes: function(index) {
					if(_this.addname.trim() == '' || _this.addname.trim().length > 10) {
						layer.msg('名称不能为空或长度超长(不超过10个字符)');
						return;
					}
					if(_this.addrmb == '') {
						layer.msg('金额不能为空');
						return;
					}
					if(_this.addurl.trim() == '' || _this.addurl.trim().length > 100) {
						layer.msg('图片地址不能为空或长度超长');
						return;
					}
					if(_this.addremark.trim() == '' || _this.addremark.trim().length > 50) {
						layer.msg('备注信息不能为空或长度超长');
						return;
					}
					if(parseInt(_this.percent) < 0 || parseInt(_this.percent) > 99.99) {
						layer.msg('返利范围为0-99.99');
						return;
					}
					var reg = new RegExp("^[0-9]*$");
					if(!reg.test(_this.group)) {
						layer.msg('唯一标志只能为数字');
						return;
					}

					let data = {
						vipname: _this.addname,
						depositamount: _this.addrmb,
						vipicourl: _this.addurl,
						remark: _this.addremark,
						group: _this.addgroup,
						percent: _this.percent,
						status: 0,
						// group:1
					};
					let obj = {
						type: 'post',
						data: data,
						dataType: 'json',
						url: '/userVIP/addVip',
						success: function(data) {
							////console.log(data);
							if(data.body.code == 200) {
								layer.msg(data.body.msg);
								setTimeout(function() {
									window.location.reload();
								}, 1000)
							} else {
								layer.msg(data.body.msg);
							}
						},
						error: function(msg) {
							//console.log(msg);
						}
					};
					base.sendRequest(obj);
				},
				btn2: function() {}
			});
		},
		//点击删除按钮执行
		del: function(vipid) {
			layer.open({
				title: '提示信息',
				type: 1,
				content: $('.popDel'),
				area: ['20%'],
				btn: ['确定', '取消'],
				yes: function(index) {
					let obj = {
						type: 'get',
						data: {
							vipId: vipid
						},
						dataType: 'json',
						url: '/userVIP/deleteVip',
						success: function(data) {
							////console.log(data);
							if(data.code == 200) {
								layer.close(index);
								layer.msg(data.msg);
								setTimeout(function() {
									window.location.reload();
								}, 500)
							} else {
								layer.msg(data.msg);
							}
						},
						error: function(msg) {
							//console.log(msg);
						}
					};
					base.sendRequest(obj);
				},
				btn2: function() {}
			})
		},
		//点击成员转移执行
		memberChange: function(vipid) {
			this.select_ = '';
			this.search_input = '';
			this.vipId = vipid;
			$('.zhezhao').show();
			$('.allMember').show();
			this.fenye();
			this.searchVip();
		},
		//加载成员列表
		getdatas: function(num) {
			let _this = this;
			let data = {
				pageIndex: num,
				pageNum: parseInt(this.page_num),
				pageSize: 10,
				username: this.search_input.trim(),
				VIP_ID: this.vipId
			};
			////console.log(num + ',' + this.page_num + ',' + this.vipId);
			let obj = {
				type: 'post',
				data: data,
				dataType: 'json',
				url: '/user/selectUsersByVip',
				success: function(data) {
					////console.log(data);
					if(data.code == 200) {
						_this.members = data.body.list;
						_this.total = data.body.total;
						if(data.body.list.length > 0) {
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
					}
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(obj);
		},
		//点击成员转移关闭按钮
		close: function() {
			$('.zhezhao').hide();
			$('.allMember').hide();
		},
		//点击搜索执行
		search: function() {
			let _this = this;
			this.search_input = this.store_search;
			let data = {
				pageIndex: 1,
				pageNum: parseInt(this.page_num),
				pageSize: 10,
				username: this.search_input.trim(),
				VIP_ID: this.vipId
			};
			let obj = {
				type: 'post',
				data: data,
				dataType: 'json',
				url: '/user/selectUsersByVip',
				success: function(data) {
					////console.log(data);
					if(data.code == 200) {
						_this.members = data.body.list;
						_this.total = data.body.total;
						if(data.body.list.length > 0) {
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

					}
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(obj);
		},
		//查询可选择的vip群信息
		searchVip: function() {
			let _this = this;
			let obj = {
				type: 'get',
				data: {
					stutas: 1
				},
				dataType: 'json',
				url: '/userVIP/selectOption',
				success: function(data) {
					////console.log(data);
					if(data.code == 200) {
						_this.selectVip = data.body;
					}
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(obj);
		},
		//点击全选按钮执行
		seleceAll: function() {
			if(this.flag == true) {
				this.member = [];
				for(var i = 0; i < this.members.length; i++) {
					this.member.push(this.members[i].uid);
				}
			} else {
				this.member = [];
			}
		},
		//点击成员转移里面的修改按钮执行
		edit: function() {
			if(this.member.length == 0) {
				this.prompt4 = '请选择至少一名会员';
				$('#prompt4').show();
				setTimeout(function() {
					$('#prompt4').hide('slow');
				}, 1000);
				return;
			}
			if(this.select_ === '') {
				this.prompt4 = '你要移动到哪个会员等级里呢';
				$('#prompt4').show();
				setTimeout(function() {
					$('#prompt4').hide('slow');
				}, 1000);
				return;
			}
			let data = {
				update_VIP_ID: this.select_,
				list: this.member
			};
			let obj = {
				type: 'post',
				data: data,
				dataType: 'json',
				url: '/userVIP/updateUserVip',
				success: function(data) {
					////console.log(data);
					layer.open({
						title: '',
						type: 1,
						content: '<div style="text-align: center;line-height:40px">' + data.msg + '</div>',
						area: ['10%', '5%']
					});
					setTimeout(function() {
						layer.close(layer.index);
						$('.zhezhao').hide();
						$('.allMember').hide();
						window.location.reload();
					}, 1000)
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(obj);
		},
		//分页插件
		fenye: function() {
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
		},

	},
	watch: {
		//监听
		member: function() {
			if(this.member.length == this.members.length) {
				this.flag = true;
			} else {
				this.flag = false;
			}
		},
		sys_config2:function(){
    		if(this.sys_config2.length==this.gameType.length){
    			this.checked=true;
    		}else{
    			this.checked=false;
    		}
    	},
		//监听页码下拉框
		page_num: function() {
			this.getdatas(1);
		}
	}
});