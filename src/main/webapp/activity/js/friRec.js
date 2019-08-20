"use strict";

$(function () {
	var friRec = {
		created: function created() {
			this.methods();
			this.click();
		},
		dataStore: {
			num: 1,
			pageNum: 50
		},
		methods: function methods() {
			var that = this;
			var str = {
				"pageIndex": that.dataStore.num,
				"pageNum": that.dataStore.pageNum,
				"pageSize": 10
			},
			    that = this;
			var send = {
				type: 'get',
				data: str,
				dataType: "josn",
				url: '/friendRecommendationReward/queryFriendRecommendationRewardList',
				success: function success(data) {
					that.temp(data);
				}
			};
			base.sendRequest(send);
		},
		temp: function temp(data) {
			var that = this,
			    list = data.body.list,
			    html = '';
			if (list == null || list == "") {
				$('#page').jqPaginator('option', {
					totalPages: 1,
					currentPage: 1
				});
				$('#paging_record .sumRecord').html(0);
				html += "<tr><td colspan=\"5\">\u6682\u65E0\u6570\u636E</td></tr>";
			} else {
				$('#page').jqPaginator('option', {
					totalPages: data.body.pages
				});
				$('#paging_record .sumRecord').html(data.body.total);
				for (var i = 0; i < list.length; i++) {
					var obj = list[i];
					html += "<tr><td>" + (obj.userName ? obj.userName : "-") + "</td><td>" + (obj.recommendationUser ? obj.recommendationUser : "-") + "</td><td>" + (obj.amount ? obj.amount : "-") + "</td><td>" + (obj.createdDate ? obj.createdDate : "-") + "</td><td>" + (obj.ip ? obj.ip : "-") + "</td></tr>";
				}
			}
			$('#friRec_recordList').html(html)
		},
		getVal: function getVal(elem) {
			return elem.find("option:selected").val();
		},
		click: function click() {
			var that = this;
			//页面显示数据条数
			$('#paging_record .pageNum').on('change', function () {
				$('#page').jqPaginator('option', {
					currentPage: 1
				});
				that.dataStore.num = 1;
				that.dataStore.pageNum = parseInt(that.getVal($(this)));
				that.methods();
			});
		}
		// 加载分页功能
	};$.jqPaginator('#page', {
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
			friRec.dataStore.num = num;
			friRec.methods();
		}
	});
	friRec.created();
});