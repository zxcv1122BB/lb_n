/**
 * 定时任务操作日志
 */
let app = new Vue({
	el: '#app',
	data: {
		datas: [], // 记录列表
		url:'http://192.168.1.10:8080/lsschedule',//请求url
		total:'',//保存总页数
		pageNum:50,//保存每页显示的记录条数   起始数值为5
//		url:'http://127.0.0.1:8080',//请求url
	},
	created: function() {
		this.$nextTick(function () {
            this.get_time();
            this.getlayer();
        })
	},

	methods: {
		//加载layer
        getlayer(){
            layui.use("layer", function () {
                let layer = layui.layer;
            });
		},
        // 利用ajax来查询记录列表
		selectAllData : function(num) {
			let _this = this;
			if(num==null){
				num=1;
			}
            let obj = {
				type: 'post',
				data: {
					'pageIndex' : num,
					'pageNum' :  parseInt(this.pageNum),
					'pageSize' : 10,
					'inputDate':$("#startDate").val()
				},
				dataType: 'json',
				url : "/job/getAllJobLogList",
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
					////console.log(data);
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(obj);
		},
		//点击查询按钮触发事件
		clickSelect:function(){
			this.selectAllData(1);
		},
		//重新执行定时任务
		aginStart:function(logId,requestUrl){
            let obj = {
					type: 'post',
					data: {
						'logId':logId,
						'url':requestUrl
					},
					dataType: 'json',
					url : "/job/implementFailTimer",
					success: function(data) {
						if (data.code == 200) {
							//执行成功之后将页面重新刷新
						   app.selectAllData();
					}
					//console.log(data);
					},
					error: function(msg) {
						//console.log(msg);
					}
				};
				base.sendRequest(obj);
		},
		 // 加载选择开始/结束日期控件
        get_time: function () {
            // 日期设置
            laydate.render({
                elem: '#startDate', // 指定元素
                format: 'yyyy-MM-dd HH:mm:ss',
                type: 'datetime',
                value: ""
            })
        },
	},
	//添加监听  监听页面下拉框的值
	watch : {
		// 监听下拉框的值(每页显示多少条数据)
		pageNum : function() {
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