/**
 * Created by ASUS on 2017/9/23.
 */
var app=new Vue({
    el:"#app",
    data:{
        datas:[], //获取会员注册列表
        Agencys:[] // 获取代理注册列表

    },
    methods:{
        //获取注册选项设置-会员注册列表
        getdatas:function(){
            var _this=this;
            var data={
                type:1 //请求参数，type=1为会员注册
            };
            var options={
                type:"get",
                url:"/sys/queryRegisterConfigure",
                dataType:"json",
                data:data,
                success:function(data){
                    if(data.code==200){
                        _this.datas=data.body;
                    }
                    //console.log(data)
                },
                error:function(msg){
                    //console.log(msg)
                }
            };
            base.sendRequest(options);
        },
        //获取注册选项设置-代理注册列表
        getAgencys:function(){
            var _this=this;
            var data={
                type:2
            };
            var options={
                type:"get",
                url:"/sys/queryRegisterConfigure",
                dataType:"json",
                data:data,
                success:function(data){
                    if(data.code==200){
                        _this.Agencys=data.body;
                    }
                    //console.log(data)
                },
                error:function(msg){
                    //console.log(msg)
                }
            };
            base.sendRequest(options);
        },
        /*会员注册显示按钮停用*/
        showStop:function(option,index){
            option.isShow = 0;
            var data={
                id:index,
                isShow:option.isShow
            };
            var options={
                type:"post",
                url:"/sys/updateMemberRegisterConfigure",
                dataType:"json",
                data:data,
                success:function(data){
                    //console.log(data)

                },
                error:function(msg){
                    //console.log(msg)
                }
            };
            base.sendRequest(options);
        },
        /*会员注册显示按钮启用*/
        showUsing:function(option,index){
            option.isShow = 1;
            var data={
                id:index,
                isShow:option.isShow
            };
            var options={
                type:"post",
                url:"/sys/updateMemberRegisterConfigure",
                dataType:"json",
                data:data,
                success:function(data){
                    //console.log(data)
                },
                error:function(msg){
                    //console.log(msg)
                }
            };
            base.sendRequest(options);
        },
        /*会员注册必输入按钮停用*/
        importStop:function(option,index){
            option.isInput = 0;
            var data={
                id:index,
                isInput:option.isInput
            };
            var options={
                type:"post",
                url:"/sys/updateMemberRegisterConfigure",
                dataType:"json",
                data:data,
                success:function(data){
                    //console.log(data)
                },
                error:function(msg){
                    //console.log(msg)
                }
            };
            base.sendRequest(options);
        },
        /*会员注册必输入按钮启用*/
        importUsing:function(option,index){
            option.isInput = 1;
            var data={
                id:index,
                isInput:option.isInput
            };
            var options={
                type:"post",
                url:"/sys/updateMemberRegisterConfigure",
                dataType:"json",
                data:data,
                success:function(data){
                    //console.log(data)
                },
                error:function(msg){
                    //console.log(msg)
                }
            };
            base.sendRequest(options);
        },
        /*会员注册验证按钮停用*/
        verifyStop:function(option,index){
            option.isCheck = 0;
            var data={
                id:index,
                isCheck:option.isCheck
            };
            var options={
                type:"post",
                url:"/sys/updateMemberRegisterConfigure",
                dataType:"json",
                data:data,
                success:function(data){
                    //console.log(data)
                },
                error:function(msg){
                    //console.log(msg)
                }
            };
            base.sendRequest(options);
        },
        /*会员注册验证按钮启用*/
        verifyiUsing:function(option,index){
            option.isCheck = 1;
            var data={
                id:index,
                isCheck:option.isCheck
            };
            var options={
                type:"post",
                url:"/sys/updateMemberRegisterConfigure",
                dataType:"json",
                data:data,
                success:function(data){
                    //console.log(data)
                },
                error:function(msg){
                    //console.log(msg)
                }
            };
            base.sendRequest(options);
        },
        /*会员注册唯一按钮停用*/
        onlyStop:function(option,index){
            option.isOnly = 0;
            var data={
                id:index,
                isOnly:option.isOnly
            };
            var options={
                type:"post",
                url:"/sys/updateMemberRegisterConfigure",
                dataType:"json",
                data:data,
                success:function(data){
                    //console.log(data)
                },
                error:function(msg){
                    //console.log(msg)
                }
            };
            base.sendRequest(options);
        },
        /*会员注册唯一按钮启用*/
        onlyUsing:function(option,index){
            option.isOnly = 1;
            var data={
                id:index,
                isOnly:option.isOnly
            };
            var options={
                type:"post",
                url:"/sys/updateMemberRegisterConfigure",
                dataType:"json",
                data:data,
                success:function(data){
                    //console.log(data)
                },
                error:function(msg){
                    //console.log(msg)
                }
            };
            base.sendRequest(options);
        },
        changeRegister:function(){
        	$("#register1").toggleClass("layui-this");
        	$("#register2").toggleClass("layui-this");
        	$("#registerItem1").toggleClass("layui-show");
        	$("#registerItem2").toggleClass("layui-show");
        }
    },
    mounted:function(){
        this.getdatas(); //手动调用获取注册选项设置-会员注册列表
        this.getAgencys(); //手动调用获取注册选项设置-代理注册列表
    },
    watch:{

    }
});