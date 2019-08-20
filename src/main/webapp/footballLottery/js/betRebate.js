/***
 * 加载所有开奖记录
 */
var app = new Vue({
	el: '#app',
	data: {
		datas: [], //记录列表
		total: '', //信息总条数
		pageNum: 50,
		gameType:[],//所有彩种
		color:[],
	},
	created: function() {
//		this.getdatas();
		this.getAllColor();
	},

	//利用ajax来查询记录列表
	methods: {
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
		//获取返利
		getdatas: function(num) {
			var _this = this;
			var obj = {
				type: 'post',
				data: {
					'pageIndex':num,
					'pageNum': _this.pageNum,
					'pageSize': 10,
				},
				dataType: 'json',
				url: '/userRebate/qryRebateLog',
				success: function(data) {
					if(data.code == 200) {
						//取到后台传递多来的Body下面的List
						_this.datas = data.body.list;
						_this.total = data.body.total;
						if(data.body.list.length > 0) {
							$('#fenye').jqPaginator('option', {
								totalPages: data.body.pages, //返回总页数
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
		clickBack(id,rebate_type) {
			var _this = this;
			var index;
			layui.use('layer', function() {
				var layer = layui.layer;
				index = layer.load(2);
			})
			let data = {
				batch_no: id,
				rebate_type:rebate_type,
			};
			let obj = {
				type: "post",
				data: data,
				url: '/userRebate/userRebateRollBack',
				success: function(data) {
					if(data.code == 200) {
//						_this.datas[index].status = id;
						layer.close(index);
						window.location.reload();
						layer.msg(data.msg);
					}
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(obj);
		},
		selectColor:function(index){
			var _this = this;
			_this.color = _this.datas[index].support_type.split(',');
			layer.open({
				title: '查看返利彩种',
				type: 1,
				content: $('.color'),
				area: ['60%', '60%'],
				btn: ['确定'],
				yes: function(index1) {
					layer.closeAll();
				}
			})
		},
	},
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
		app.getdatas(num);
	}
});