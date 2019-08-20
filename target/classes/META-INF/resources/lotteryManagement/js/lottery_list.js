/***
 * 加载所有开奖记录
 */
var app = new Vue({
	el: '#app',
	data: {
		datas: [], //记录列表
		twos: [], //耳机筛选条件记录
		three: [],
		total: '', //信息总条数
		pageNum: 50,
		playId:11,
	},
	created: function() {
		this.getdatas();
		this.gettwos();
		this.getthree();
	},

	//利用ajax来查询记录列表
	methods: {
		//获取开奖记录列表  playtypeid
		getdatas: function(num) {
			var _this = this;
			var gameTypeId=$("#playId").val();
			if(gameTypeId==0){
				gameTypeId=11;
			}
			if(num==null){
				num=1;
			}
			var obj = {
				type: 'post',
				data: {
					'pageIndex':num,
					'pageNum': 5,
					'pageSize': 10,
					'playtypeid': gameTypeId,
					'leagueName':$("#name").val()
				},
				dataType: 'json',
				url: '/football/theLottery',
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

		gettwos: function() {
			var _this = this;
			var selectTwo = {
				type: 'get',
				data: {
					'id': 1
				},
				//				dataType: 'json',
				url: '/game/selectPlayGroup',
				success: function(data) {
					if(data.code == 200) {
						//取到后台传递多来的Body下面的List
						_this.twos = data.body;
					}
					//console.log(data);
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(selectTwo);
		},

		getthree: function() {
			var _this = this;
			var selectThree = {
				type: 'get',
				data: {
					'id': 1
				},
				//				dataType: 'json',
				url: '/game/selectPlayGame',
				success: function(data) {
					if(data.code == 200) {
						//取到后台传递多来的Body下面的List
						_this.three = data.body;
					}
					//console.log(data);
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(selectThree);
		},
		
		clickThree: function() {
			app.getdatas(1);
		},

		
		//点击查询按钮  条件筛选
		likeSelect: function() {
			app.getdatas(1);
		},

		//点击二级查询分类的方法
		clickTwo: function(objValue) {
			$('.add_btn>button').removeClass('aa');
			$('#button'+objValue).addClass('aa');
			var _this = this;
			var selectThree = {
				type: 'get',
				data: {
					'id': objValue
				},
				url: '/game/selectPlayGame',
				success: function(data) {
					if(data.code == 200) {
						//取到后台传递多来的Body下面的List
						_this.three = data.body;
					}
					//console.log(data);
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(selectThree);

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