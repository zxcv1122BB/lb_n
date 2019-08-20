/**
 * Created by ASUS on 2017/9/19.
 */
var app = new Vue({
    el: '#app',
    data: {
              status_: '',//修改信息里面的状态
              datas: [],//运营分析
              titles_: [],//标题
              webName:"lsCanal"
              
    },
    created: function () {
    	this.getdatas("0");
    	this.getTitles();
    },
    methods: {
    	getTitles:function(){
            let _this = this;
            let data = {};
            let obj = {
                type: 'get',
                data: data,
                dataType: 'json',
                url: '/selectWebTitleList',
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
                       _this.titles_ = data.body;
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
    	},
        //初始化数据
        getdatas: function (_name) {
            let _this = this;
        	if(_name!="0"&&_name!=1){//点击
        		_this.webName= _name;
        	}
        	if(_name===1){//刷新
        		_this.webName= $("#webName").html();
        	}
            let data = {
            	webName:_this.webName
            };  
            let obj = {
                type: 'get',
                data: data,
                dataType: 'json',
                url: '/webListener',
                success: function (data) {
                    ////console.log(data);
                    $("#states").html(data.msg);
                    $("#webDescribe").html(data.describe);
                    $("#webCHName").html( $("#"+_name).html());
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        }      
       
    },
    watch: {
       
    }
});


