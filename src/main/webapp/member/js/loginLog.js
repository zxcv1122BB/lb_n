/**
 * Created by ASUS on 2017/9/18.
 */
var app = new Vue({
    el:'#app',
    data:{
        datas:[],   //列表数据
        total:'',   //信息总条数
        pageNum:50,  //每页显示的条数
        startTime:'', //查询开始时间
        endTime:'', //查询结束时间
    },
    created:function () {
        this.$nextTick(function () {
            this.get_time();       //加载开始/结束日期控件
        });
    },
    methods:{
        //初始化
        getdatas:function (num) {
            var _this = this;
            var data = {
                pageIndex: num,
                pageNum: parseInt(this.pageNum),
                pageSize: 10,
                startTime:this.startTime,
                endTime:this.endTime
            };
            //console.log(data);
            var obj = {
                type: 'post',
                data: data,
                dataType: 'json',
                url: '/log/selectAllLog',
                success: function (data) {
                    ////console.log(data);
                    if(data.code==200){
                        _this.total = data.body.total;
                        _this.datas = data.body.list;
                        if(data.body.list.length>0){
                            $('#fenye').jqPaginator('option', {
                                totalPages: data.body.pages,    //返回总页数
                                currentPage: 1
                            });
                        }else {
                            $('#fenye').jqPaginator('option', {
                                totalPages: 1,
                                currentPage: 1
                            });
                        }
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            if (_this.startTime != undefined || _this.endTime != undefined) {
                // 检测用户输入的时间是否符合规范（开始时间小于结束时间）
                if (_this.checkdate(_this.startTime, _this.endTime)) {
                    base.sendRequest(obj);
                }
                return;
            }
            base.sendRequest(obj);
        },
        //加载选择开始/结束日期控件
        get_time: function () {
            //	日期设置
            // laydate.render({
            //     elem: '#startDate', //指定元素
            //     format: 'yyyy-MM-dd HH:mm:ss',
            //     type: 'datetime',
            //     max: 0,
            //     value: ""
            // });
            // laydate.render({
            //     elem: '#endDate', //指定元素
            //     format: 'yyyy-MM-dd HH:mm:ss',
            //     type: 'datetime',
            //     max: 0,
            //     value: ""
            // });
            laydate.render({
                elem: '#startDate',
                format: 'yyyy-MM-dd'
            });
            laydate.render({
                elem: '#endDate',
                format: 'yyyy-MM-dd'
            });
            layui.use('layer',function () {
                let layer = layui.layer;
            })
        },
        // 点击查询
        inquiry:function () {
            this.startTime=$('#startDate').val().substr(0,10);
            this.endTime=$('#endDate').val().substr(0,10);
            layer.msg('加载中...',{time:500});
            this.getdatas(1);
        },
        // 检测时间的先后顺序
        checkdate: function (start, end) {
            //得到日期值并转化成日期格式，replace(//-/g, "//")是根据验证表达式把日期转化成长日期格式，这样
            //再进行判断就好判断了
            let sDate = new Date(start.replace(/\-/g, "\/"));
            let eDate = new Date(end.replace(/\-/g, "\/"));
            if (sDate > eDate) {
                layer.alert('结束时间不能早于开始时间', {
                    skin: 'layui-layer-molv'
                    , closeBtn: 0
                    , anim: 4 //动画类型
                });
                return false;
            }
            return true;
        }
    },
    watch:{
        pageNum:function () {
            this.getdatas(1);
        }
    }
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