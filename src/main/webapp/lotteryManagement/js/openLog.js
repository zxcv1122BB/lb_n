var app = new Vue({
	el : "#app",
	data : {
		datas : [],// 保存最近开奖日志
		openLog : '',// 保存单个的日志信息
		logs:[],//日志详情
		bets:[],//投注详情列表保存
		pageNum: 50,  //默认每页5条数据-绑定
		total: '',    //总条数-绑定
		
		 //下面参数是搜索时要传的:
        prevData:'',//搜索记录
	},
	mounted: function () {
		this.setDataTime();
	},
	methods : {
		// 获取方案列表
		getdatas : function(num) {
			var _this = this;
			var data = {
				'pageIndex' : num,
				'pageNum' : parseInt(this.pageNum),
				'pageSize' : 5,
				'team_name' : $("#team_name").val()
			};
			if(_this.prevData!=""){
            	_this.prevData.pageIndex=num;
            	data=_this.prevData;
            }
			var options = {
				type : "POST",
				url : "/openLog/getAllOpenLog",
				data : data,
				dataType : 'json',
				success : function(data) {
					//console.log(data);
					if (data.code == 200) {
						if(data.body){
							for (var i = 0; i < data.body.list.length; i++) {
								// 判断赛果 3胜1平0负
								if (data.body.list[i].match_result == 0) {
									data.body.list[i].match_result = '负';
								} else if (data.body.list[i].match_result == 1) {
									data.body.list[i].match_result = '平';
								} else if (data.body.list[i].match_result == 3) {
									data.body.list[i].match_result = '胜';
								}
							}
							_this.datas = data.body.list;
							_this.total = data.body.total;
							// 分页的(右边点击)
							 if(data.body.pages!=0){
		                            $('#fenye').jqPaginator('option', {
		                                totalPages: data.body.pages,
		                                currentPage:1
		                            });
		                        }else {
		                            $('#fenye').jqPaginator('option', {
		                                totalPages: 1,
		                                currentPage:1
		                            });
		                        }
							/*$('#fenye').jqPaginator('option', {
								totalPages : data.body.pages
							});*/
						}else{
							_this.datas = '';
							_this.total = 0;
							$('#fenye').jqPaginator('option', {
								totalPages : 1
							});
						}
					}
				},
				error : function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(options);
		},
		
		
		
		
		//获取投注信息列表
		selectBygameBet:function(calNo){
			var _this = this;
			var data = {
					'cal_no':calNo
			};
			var options = {
					type : "POST",
					url : "/openLog/selectBygameBet",
					data : data,
					dataType : 'json',
					success : function(data) {
						//console.log(data);
						if (data.code == 200) {
							_this.bets=data.body;
							// 展示弹出框
						}
						layui.use('layer', function() {
							var layer = layui.layer;
							layer.open({
								title : '投注信息',
								type : 1,
								content : $('#betDiv'),
								area: ['75%'],
								btn : ['关闭' ]
							})
						})
					},
					error : function(msg) {
						//console.log(msg);
					}
			};
			base.sendRequest(options);
			
		},
		
		// 获取方案列表
		search : function() {
			var _this = this;
			var data = {
					'pageIndex' : 1,
					'pageNum' : parseInt(this.pageNum),
					'pageSize' : 5,
					'team_name' : $("#team_name").val(),
					'openno' : $("#startDate").val()
			};
			var options = {
					type : "POST",
					url : "/openLog/getAllOpenLog",
					data : data,
					dataType : 'json',
					success : function(data) {
						//console.log(data);
						if (data.code == 200) {
							if(data.body){
								for (var i = 0; i < data.body.list.length; i++) {
									// 判断赛果 3胜1平0负
									if (data.body.list[i].match_result == 0) {
										data.body.list[i].match_result = '负';
									} else if (data.body.list[i].match_result == 1) {
										data.body.list[i].match_result = '平';
									} else if (data.body.list[i].match_result == 3) {
										data.body.list[i].match_result = '胜';
									}
								}
								_this.prevData=options.data;
								_this.datas = data.body.list;
								_this.total = data.body.total;
								// 分页的(右边点击)
								 if(data.body.pages!=0){
			                            $('#fenye').jqPaginator('option', {
			                                totalPages: data.body.pages,
			                                currentPage:1
			                            });
			                        }else {
			                            $('#fenye').jqPaginator('option', {
			                                totalPages: 1,
			                                currentPage:1
			                            });
			                        }
								/*$('#fenye').jqPaginator('option', {
									totalPages : data.body.pages
								});*/
							}else{
								_this.prevData=options.data;
								_this.datas = '';
								_this.total = 0;
								$('#fenye').jqPaginator('option', {
									totalPages : 1
								});
							}
						}
					},
					error : function(msg) {
						//console.log(msg);
					}
			};
			base.sendRequest(options);
		},
		//日期选择器设置
		setDataTime: function () {
			//	日期设置
			laydate.render({
				elem: '#startDate', //指定元素
				format: 'yyyy-MM-dd',
				value: ""
			});
		},
		// 获取详情
		selectByOpenno : function(openno) {
			var _this = this;
			var data = {
				'openno' : openno
			};
			var options = {
				type : "POST",
				url : "/openLog/selectByOpenno",
				data : data,
				dataType : 'json',
				success : function(data) {
					if (data.code == 200) {
						_this.openLog = data.body;
						for (var i = 0; i < data.body.openlogs.length; i++) {
							   var now = new Date(parseInt(data.body.openlogs[i].date));
				                y = now.getFullYear();
				                m = now.getMonth() + 1;
				                d = now.getDate();
							    data.body.openlogs[i].date = y + "-" + (m < 10 ? "0" + m : m) + "-" + (d < 10 ? "0" + d : d) + " " + now.toTimeString().substr(0, 8);
						}
						_this.logs=data.body.openlogs;
						// 展示弹出框
						layui.use('layer', function() {
							var layer = layui.layer;
							layer.open({
								title : '日志详情',
								type : 1,
								content : $('#dataDiv'),
								area : [ '75%' ],
								btn : ['关闭' ]
							})
						})
						
						
					}
				},
				error : function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(options);
		},

	},

	watch : {
		// 实实监听下拉框的值,如果有变动,就走以下方法(指得是每页显示多少条数据的变动)
		pageNum : function() {
			this.getdatas(1);// 就调用初始化方法, 传入第一页
		},
		/*// 监听页码下拉框的值
		page_num : function() {
			this.getdatas(1);
			this.getdatas1();
		}*/
	},
});
// 加载分页功能
$.jqPaginator('#fenye', {
	totalPages : 1, // 多少页数据
	visiblePages : 10, // 最多显示几页
	currentPage : 1, // 当前页
	wrapper : '<ul class="pagination"></ul>',
	first : '<li class="first"><a href="javascript:void(0);">首页</a></li>',
	prev : '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
	next : '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
	last : '<li class="last"><a href="javascript:void(0);">尾页</a></li>',
	page : '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',

	onPageChange : function(num, type) {
		app.getdatas(num);
		/* app.getdatas1(); */
	}
});