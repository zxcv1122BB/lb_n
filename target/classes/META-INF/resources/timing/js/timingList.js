var app = new Vue({
	el: '#app',
	data: {
		datas: [], // 记录列表
		total: '', // 总页数
		pageNum: 50, // 默认每页5条数据-绑定
		jobData: '', // 保存根据编号查询的单个任务对象
		// jobValue:'',
		jobId: '',
		jobName: '',
		jobGroup: '',
		cronExpression: '',
		beanClass: '',
		jobStatus: '',
		executeMethod: '',
		jobDesc: 'desc'
	},
	// created : function() {
	// this.selectAllData();
	// },

	// 利用ajax来查询记录列表
	methods: {
		//封装一个方法  验证cron表达式
		repCron: function(value) {
//			var value = element; //传递过来真正要验证的字符串

			if(value != "") {
				value = value.replace(/(^\s*)|(\s*$)/g, ""); //去掉前后空白
				var arr = value.split(/\s+/); //用空白分割

				if(arr.length != 6 && arr.length != 7) {
					layer.msg("表达式必须是 由5个或者6个空格隔开，如 0 0 12 * * ?");
					return false;
				} else {
					// 为了清晰起见，我将规则拆分来写
					var reg1 = /^([0-5]?\d)([\/\-][0-5]?\d)?$/; //形如23 23/34 45-59
					var reg2 = /^[0-5]?\d(,[0-5]?\d)*$/; //形如 12,43,56  
					var reg3 = /^\*$/; //匹配 *

					if(!(reg1.test(arr[0]) || reg2.test(arr[0]) || reg3.test(arr[0]))) {
						layer.msg("第1位是秒，允许的值（0-59 ,-*/）如 （2,47,23-45,5/6）");
						return false;
					}
					if(!(reg1.test(arr[1]) || reg2.test(arr[1]) || reg3.test(arr[1]))) {
						layer.msg("第2位是分，允许的值（0-59 ,-*/）如 （2,47,23-45,5/6）");
						return false;
					}
					reg1 = /^(([0-1]?\d)|(2[0-3]))([\/\-](([0-1]?\d)|(2[0-3])))?$/; //形如23 12/18 7-19
					reg2 = /^(([0-1]?\d)|(2[0-3]))(,(([0-1]?\d)|(2[0-3])))*$/; //形如12,15,20
					if(!(reg1.test(arr[2]) || reg2.test(arr[2]) || reg3.test(arr[2]))) {
						layer.msg("第3位是小时，允许的值（0-23 ,-*/）如 （3,8,21-23,4/7,*）");
						return false;
					}
					reg1 = /^(([1-9])|([12]\d)|(3[01]))([\/\-](([1-9])|([12]\d)|(3[01])))?$/; //形如1 12/18 7-26
					reg2 = /^(([1-9])|([12]\d)|(3[01]))(,(([1-9])|([12]\d)|(3[01])))*$/; //形如23,25,30
					reg3 = /^(\*|\?)$/; //形如 
					var reg4 = /^(((([1-9])|([12]\d)|(3[01]))[WC])|(LW?))$/; //形如12W 13C L LW
					if(!(reg1.test(arr[3]) || reg2.test(arr[3]) || reg3.test(arr[3]) || reg4.test(arr[3]))) {
						layer.msg("第4位是日，允许的值（1-31 ,-*/？L W C）如 （1,20,4-8,3/5,2C,8W,L,LW等）");
						return false;
					}
					//reg1=/^(([1-9])|(1[0-2]))$/;  ok 1-12
					reg1 = /^(([1-9])|(1[0-2]))([\/\-](([1-9])|(1[0-2])))?$/; //形如1 3/6 7-10
					reg2 = /^(([1-9])|(1[0-2]))(,(([1-9])|(1[0-2])))*$/; //形如3,5,8
					reg3 = /^\*$/; //形如 * 
					reg4 = /^((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEPT)|(OCT)|(NOV)|(DEC))(\-((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEPT)|(OCT)|(NOV)|(DEC)))?$/i; //12个月份
					var reg5 = /^((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEPT)|(OCT)|(NOV)|(DEC))(,((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEPT)|(OCT)|(NOV)|(DEC)))*$/i; //12个月份

					if(!(reg1.test(arr[4]) || reg2.test(arr[4]) || reg3.test(arr[4]) || reg4.test(arr[4]) || reg5.test(arr[4]))) {
						layer.msg("第5位是月，允许的值（1-12 ,-*/ JAN-DEC）如 （1,10,2-6,JAN,MAY-JUN等）");
						return false;

					}
					reg1 = /^[1-7]([\/\-][1-7])?$/; //形如1 3/6 2-5
					reg2 = /^[1-7](,[1-7])*$/; //形如3,5,6
					reg3 = /^(\*|\?|L)$/; //形如 * ? L
					reg4 = /^((MON)|(TUES)|(WED)|(THUR)|(FRI)|(SAT)|(SUN))([\-]((MON)|(TUES)|(WED)|(THUR)|(FRI)|(SAT)|(SUN)))?$/i; //形如 7个星期 -连接
					reg5 = /^((MON)|(TUES)|(WED)|(THUR)|(FRI)|(SAT)|(SUN))(,((MON)|(TUES)|(WED)|(THUR)|(FRI)|(SAT)|(SUN)))*$/i; //形如 7个星期 ，枚举
					var reg6 = /^[1-7][LC]$/; //形如 3L 4C
					var reg7 = /^[1-7]?#[1-5]$/; //形如 #4  6#3

					if(!(reg1.test(arr[5]) || reg2.test(arr[5]) || reg3.test(arr[5]) || reg4.test(arr[5]) || reg5.test(arr[5]) || reg6.test(arr[5]) || reg7.test(arr[5]))) {
						layer.msg("第6位是周儿，允许的值（1-7 ,-*/? L C # SUN-SAT）如 （1,2,1-5,?,3C,4L,4#2,SUN等）");
						return false;
					}
					if(arr.length == 7) {
						//reg1=/^((19[7-9]\d)|(20\d\d))$/; //  1979-2099
						reg1 = /^((19[7-9]\d)|(20\d\d))([\/\-]((19[7-9]\d)|(20\d\d)))?$/;
						reg2 = /^((19[7-9]\d)|(20\d\d))(,((19[7-9]\d)|(20\d\d)))*$/;
						reg3 = /^(\*|(empty))$/i;
						if(!(reg1.test(arr[6]) || reg2.test(arr[6]) || reg3.test(arr[6]))) {
							layer.msg("第7位是年(可选字段)，允许的值（empty,1979-2099 ,-*/）如 (2013,empty,2012,2013 2012-2013等)");
							return false;
						}

					}
					return value;
				}
			} else {
				return value;
			}
		},

		// 添加一个定时任务
		addTimer: function(jobName, jobGroup, cronExpression, beanClass,
			executeMethod, jobDesc) {
		    //首先需要验证cron表达式是否合法
		    if(cronExpression != app.repCron(cronExpression)){
		    	return;
		    }
			var _this = this;
			var obj = {
				type: 'post',
				data: {
					'jobName': _this.jobName,
					'jobGroup': _this.jobGroup,
					'cronExpression': _this.cronExpression,
					'beanClass': _this.beanClass,
					'executeMethod': _this.executeMethod,
					'jobDesc': _this.jobDesc,
					'jobStatus': _this.jobStatus
				},
				dataType: 'json',
				url: '/job/addTimer',
				success: function(data) {
					if(data.code == 200) {
						layui.use('layer', function() {
							var layer = layui.layer;
							layer.msg(data.msg);
							layer.closeAll('page');
							// 刷新页面
							app.selectAllData();
						})
					}
					//console.log(data);
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(obj);

		},

		// 修改定时任务
		updateOneTimer: function(jobId, jobName, jobGroup, cronExpression,
			beanClass, executeMethod, jobDesc) {
			//首先需要验证cron表达式是否合法
		    if(cronExpression != app.repCron(cronExpression)){
		    	return;
		    }
			var _this = this;
			var obj = {
				type: 'post',
				data: {
					'jobId': jobId,
					'jobName': jobName,
					'jobGroup': jobGroup,
					'cronExpression': cronExpression,
					'beanClass': beanClass,
					'executeMethod': executeMethod,
					'jobDesc': jobDesc
				},
				dataType: 'json',
				url: '/job/updateOneTimer',
				success: function(data) {
					if(data.code == 200) {
						layui.use('layer', function() {
							var layer = layui.layer;
							layer.msg(data.msg);
							layer.closeAll('page');
							// 刷新页面
							app.selectAllData();
						})
					}
					//console.log(data);
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(obj);
		},

		// 删除定时任务
		deleteOneTimer: function(jobId) {
			var _this = this;
			var obj = {
				type: 'post',
				data: {
					'jobId': jobId
				},
				dataType: 'json',
				url: '/job/deleteOneTimer',
				success: function(data) {
					if(data.code == 200) {
						layui.use('layer', function() {
							var layer = layui.layer;
							layer.msg(data.msg);
							// 刷新页面
							app.selectAllData();
						})
					}
					//console.log(data);
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(obj);

		},

		// 根据任务名称查询任务详情
		getByJobDetil: function(jobId) {
			var _this = this;
			var obj = {
				type: 'post',
				data: {
					'jobId': jobId
				},
				dataType: 'json',
				url: '/job/getByJobDetil',
				success: function(data) {
					if(data.code == 200) {
						// 弹出新数据
						_this.jobData = data.body;
					}
					//console.log(data);
				},
				error: function(msg) {
					layui.use('layer', function() {
						var layer = layui.layer;
						layer.msg('操作失败，出现错误')
					})
				}
			};
			base.sendRequest(obj);
		},

		// 启用定时任务
		startOneTimer: function(jobId) {
			var _this = this;
			var obj = {
				type: 'post',
				data: {
					'jobId': jobId
				},
				dataType: 'json',
				url: '/job/startOneTimer',
				success: function(data) {
					if(data.code == 200) {
						layui.use('layer', function() {
							var layer = layui.layer;
							layer.msg(data.msg);
							// 刷新页面
							app.selectAllData();
						})
						//console.log(data);
					}
				},
				error: function(msg) {
					layui.use('layer', function() {
						var layer = layui.layer;
						layer.msg('操作失败，出现错误')
					})
				}
			};
			base.sendRequest(obj);
		},

		// 暂停一个定时任务 定时任务管理
		stopOneTimer: function(jobId) {
			var _this = this;
			var obj = {
				type: 'post',
				data: {
					'jobId': jobId
				},
				dataType: 'json',
				url: '/job/stopOneTimer',
				success: function(data) {
					if(data.code == 200) {
						if(data.code == 200) {
							layui.use('layer', function() {
								var layer = layui.layer;
								layer.msg(data.msg);
								// 刷新页面
								app.selectAllData();
							})
						}
						//console.log(data);
					}
				},
				error: function(msg) {
					layui.use('layer', function() {
						var layer = layui.layer;
						layer.msg('操作失败，出现错误')
					})
				}
			};
			base.sendRequest(obj);
		},

		// 页面加载 完成 加载所有定时任务的功能
		selectAllData: function(num) {
			var _this = this;
			if(num == null) {
				num = 1;
			}
			var obj = {
				type: 'post',
				data: {
					'pageIndex': num,
					'pageNum': parseInt(this.pageNum),
					'pageSize': 10
				},
				dataType: 'json',
				url: '/job/getAllJobList',
				success: function(data) {
					if(data.code == 200) {
						// 取到后台传递多来的Body下面的List
						_this.datas = data.body.list;
						_this.total = data.body.total;
						if(data.body.list.length > 0) {
							$('#fenye').jqPaginator('option', {
								totalPages: data.body.pages, // 返回总页数
								currentPage: 1
							});
						} else {
							$('#fenye').jqPaginator('option', {
								totalPages: 1,
								currentPage: 1
							});
						}
					}
					//console.log(data);
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(obj);
		},

		// 删除的时候 完成询问
		deleteData: function(jobId) {
			layui.use('layer', function() {
				var layer = layui.layer;
				layer.confirm('您确定删除吗？', {
					btn: ['确定', '取消']
						// 按钮
				}, function() {
					app.deleteOneTimer(jobId);
				})
			})
		},

		// 完成多选
		clickAllCheck: function() {
			// 获取到多选框的选中状态
			var flag = $("#checkAll").is(':checked');
			// 更改状态
			var objChevkBox = $(".checkBoxs");
			if(flag == true) {
				for(var i = 0; i < objChevkBox.length; i++) {
					objChevkBox[i].checked = true;
				}
			} else {
				for(var i = 0; i < objChevkBox.length; i++) {
					objChevkBox[i].checked = false;
				}
			}
		},

		// 展示添加弹出窗
		showAddUi: function() {
			var _this = this;
			layui.use('layer', function() {
				var layer = layui.layer;
				// 每次打开页面之前 将div中的输入框的值去除
				$("#showAddUi input").val("");
				$("#showAddUi textarea").val("");
				layer.open({
					title: '添加定时任务',
					type: 1,
					content: $('#showAddUi'),
					area:['50%','70%'],
					btn: ['保存', '关闭'],
					yes: function(index, layero) {
						if(_this.jobName == '' || _this.jobName == null) {
							layer.msg('任务名称不能为空');
							return;
						}
						if(_this.jobGroup == '' || _this.jobGroup == null) {
							layer.msg('任务组不能为空');
							return;
						}
						if(_this.cronExpression == '' ||
							_this.cronExpression == null) {
							layer.msg('时间表达式不能为空');
							return;
						}
						if(_this.beanClass == '' || _this.beanClass == null) {
							layer.msg('执行类不能为空');
							return;
						}
						if(_this.executeMethod == '' ||
							_this.executeMethod == null) {
							layer.msg('执行方法不能为空');
							return;
						}
						if(_this.jobStatus == '' || _this.jobStatus == null) {
							layer.msg('任务状态不能为空');
							return;
						}
						if(_this.jobDesc == '' || _this.jobDesc == null) {
							layer.msg('备注信息不能为空');
							return;
						}
						app.addTimer(_this.jobName, _this.jobGroup,
							_this.cronExpression, _this.beanClass,
							_this.executeMethod, _this.jobDesc);
					}
				})
			})
		},

		// 展示修改弹出窗
		showUpdateUi: function(jobId) {
			// 首先调用查询单个的方法
			app.getByJobDetil(jobId);
			layui.use('layer', function() {
				var layer = layui.layer;
				layer.open({
					title: '修改定时任务',
					type: 1,
					content: $('#showUpdateUi'),
					area:['50%','58%'],
					btn: ['修改', '关闭'],
					yes: function(index, layero) {
						if($("#jobName").val() == '' ||
							$("#jobName").val() == null) {
							layer.msg('任务名称不能为空');
							return;
						}
						if($("#jobGroup").val() == '' ||
							$("#jobGroup").val() == null) {
							layer.msg('任务组不能为空');
							return;
						}
						if($("#cronExpression").val() == '' ||
							$("#cronExpression").val() == null) {
							layer.msg('时间表达式不能为空');
							return;
						}
						if($("#beanClass").val() == '' ||
							$("#beanClass").val() == null) {
							layer.msg('执行类不能为空');
							return;
						}
						if($("#executeMethod").val() == '' ||
							$("#executeMethod").val() == null) {
							layer.msg('执行方法不能为空');
							return;
						}
						if($("#jobDesc") == '' || $("#jobDesc") == null) {
							layer.msg('备注信息不能为空');
							return;
						}
						// 此处写确定修改的操作
						app.updateOneTimer(jobId, $("#jobName").val(), $(
								"#jobGroup").val(), $("#cronExpression").val(),
							$("#beanClass").val(), $("#executeMethod")
							.val(), $("#jobDesc").val());
						// layer.closeAll('page');
					}
				})
			})
		},

	},

	watch: {
		// 监听下拉框的值(每页显示多少条数据)
		pageNum: function() {
			app.selectAllData(1);
		},
	}
});

// 加载分页功能
$.jqPaginator('#fenye', {
	totalPages: 1, // 多少页数据
	visiblePages: 10, // 最多显示几页
	currentPage: 1, // 当前页
	wrapper: '<ul class="pagination"></ul>',
	first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',
	prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
	next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
	last: '<li class="last"><a href="javascript:void(0);">尾页</a></li>',
	page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',

	onPageChange: function(num) {
		app.selectAllData(num);
	}
});