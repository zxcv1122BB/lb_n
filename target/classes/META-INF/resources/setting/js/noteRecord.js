/**
 * Created by ASUS on 2017/9/29.
 */
let app = new Vue({
    el: ".main",
    data: {
    	page_num: 50,  //默认每页5条
        datas: [],//方案列表
        northReturnAwardRate:'',
        total:'',
    },
    created() {
        this.getlayer();
        this.getOnlyConfigure();
    },
    methods: {
        //加载layer
        getlayer() {
            layui.use("layer", function () {
                let layer = layui.layer;
            });
        },
        //获取系统配置
		getOnlyConfigure: function() {
			var _this = this,
				privacy = {
					type: "get",
					data: {configure:'northReturnAwardRate'},
					url: "/sysConfigure/getOnlyConfigure",
					success: function(data) {
//						//console.log(data);
						_this.northReturnAwardRate = data.body.sysConfig1;
					}
				};
			base.sendRequest(privacy);
		},
        //获取方案列表
        getdatas: function (num) {
        	matchId = localStorage.matchId
            let _this = this;
            let data = {
                pageIndex: num,
                pageNum: parseInt(this.page_num),
                pageSize: 5,
				
				matchId:matchId,
                
            };
            //console.log(data);
            let options = {
                type: "get",
                url: "/bets/queryBettingOrderList",
                data: data,
                dataType: 'json',
                success: function (data) {
                    //console.log(data);
                    if (data.code == 200) {
                        _this.datas = data.body.list;
                        _this.total = data.body.total;
                        //分页的(右边点击)
                        if(_this.datas.length>0){
                        	$('#fenye').jqPaginator('option', {
	                            totalPages: data.body.pages
	                        });
                        }
                    }

                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(options);
        },
        //手工开奖操作
        manualWork: function () {
            let _this = this;
            let options = {
                type: "post",
                url: "/bets/prizeOperation",
                data: {"matchId": localStorage.matchId},
                dataType: 'json',
                success: function (data) {
                    let msg = data.msg;
                    //弹出信息
                    layer.open({
                        title: '开奖处理结果',
                        content: msg,
                        area: '20%',         
                        yes:function (index) {
                            layer.close(index);
                            setTimeout(function () {
                                window.location.reload();
                            },500)
                        }

                    });
                    //_this.getdatas(1);
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(options);
        },
        //开奖回滚
        rollBackBtn:function(id,isCal,type){
        	var _this = this;
        	if(isCal==0||type==2){
        		return;
        	}
        	layer.open({  
                title: ['提示'],  
                content: '<div>确认要开奖回滚吗？</div>',  
                btn: ['确认', '取消'],  
                area: '20%',         
                shadeClose: true,  
                //回调函数  
                yes: function(index, layero){  
                   _this.rollBack(id);
                   layer.close(index);
                },  
                btn2: function(index, layero){  
                   layer.closeAll();
                },  
                cancel: function(index,layero){ //按右上角“X”按钮  
                   layer.closeAll();
                },  
          
        	});
        },
        rollBack:function(id){
        	let options = {
                type: "post",
                url: "/bets/prizeRollbackOperation",
                data: {"matchId": localStorage.matchId},
                dataType: 'json',
                success: function (data) {
                	layer.msg(data.msg)
                    setTimeout(function () {
                        window.location.reload();
                    },500)
                },
                error: function (msg) {
                	layer.msg("开奖回滚失败");
                    //console.log(msg);
                }
            };
            base.sendRequest(options);
        },
    },
    watch: {
        //监听页码下拉框的值
        page_num: function () {
            this.getdatas(1);
        }
    },
});
// 加载分页功能
$.jqPaginator('#fenye', {
    totalPages: 1,      //多少页数据
    visiblePages: 10,   //最多显示几页
    currentPage: 1,     //当前页
    wrapper: '<ul class="pagination"></ul>',
    first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',
    prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
    next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
    last: '<li class="last"><a href="javascript:void(0);">尾页</a></li>',
    page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',

    onPageChange: function (num, type) {
        app.getdatas(num);
    }
});