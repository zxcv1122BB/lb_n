/**
 * Created by ASUS on 2017/9/21.
 */
let app = new Vue({
    el: "#app",
    data: {
        datas: [],
        datas1: [],		//基本设置列表
        //binding:0, //网站开关按钮绑定值
        //title:"", //网站维护原因输入绑定
        hint: "",
        list: [],
        list2: [],
        prompt_message: false,//添加 || 删除 提示信息的div
        preObj: {},//展示数值jq对象
        preDiv: {},//输入框jq对象
        gameType:[],
        percentId:'',
        checked:'',
        sysConfig2:[],
    },
    created(){
        this.getlayer();
    },
    mounted: function () {
        this.getdatas(); //手动调用展示系统开关列表
        this.getdatas1(); //手动调用展示系统开关列表
    },
    methods: {
        //加载layer和element
        getlayer(){
            layui.use(["layer", "element"], function () {
                let layer = layui.layer,
                    element = layui.element;
            });
        },
        //获取网站基本设置中系统开关列表
        getdatas: function () {
            let _this = this;
            let options = {
                type: "get",
                url: "/sysConfigure/querySysConfigureModule",
                dataType: 'json',
                data: {},
                success: function (data) {
                    //console.log(data);
                    if (data.code == 200) {
                        _this.datas = data.body;
                    }
                },
                error: function (msg) {
                    //console.log(msg)
                }

            };
            base.sendRequest(options);
        },
        getdatas1: function () {
            let _this = this
            let options = {
                type: "get",
                url: "/sysConfigure/querySysConfigure",
                dataType: 'json',
                data: {"moduleId": 1},
                success: function (data) {
                    //console.log(data);
                    if (data.code == 200) {
                        _this.datas1 = data.body;
                    }
                },
                error: function (msg) {
                    //console.log(msg)
                }
            };
            base.sendRequest(options);
        },
        changeType: function (onOff, index) {
            let obj = this.datas1[index];
            if (obj.onOff == 0) {
                //$('#'+onOff).addClass('layui-form-onswitch');
                obj.onOff = 1;
            } else {
                // $('#'+onOff).removeClass('layui-form-onswitch');
                obj.onOff = 0;
            }

            //this.$set(this.datas1, index, obj);
        },
        insertText: function (onOff, obj, type) {
            //debugger;
            let _this = this;
            _this.cancelPre(_this);
            let test = "";
            if (type === 1) {
                test = obj.sysConfig1;
            } else if (type === 2) {
                test = obj.sysConfig2;
            }
            let $obj = $('#' + onOff + type + "-div");
            $obj.toggleClass("hide");
            $obj.toggleClass("show");
            let $input = $('#' + onOff + type + "-input");
            $input.val(test);
            $('#' + onOff + type).hide();
            _this.preObj = $('#' + onOff + type);
            _this.preDiv = $obj;
            if (obj.dataType === 2) {
                _this.bindDate('#' + onOff + type + "-input");
            }
        },
        bindDate: function (inputId) {
            laydate.render({
                elem: inputId,
                type: 'time'
            });
        },
        cancelPre: function (obj) {
            if (obj.preObj.length === 1 && obj.preObj.length === 1) {
                obj.preObj.show();
                obj.preDiv.addClass("hide");
                obj.preDiv.removeClass("show");
                obj.preObj = {};
                obj.preDiv = {};
            }
        },
        changeText: function (onOff, type, index) {//改变文本内容后
            let _this = this;
            let $input = $('#' + onOff + type + "-input");
            let text1 = $input.val();
            if (text1 === '') {//未输入需求
                _this.hint = "请输入" + _this.datas1[index].configureExplain;
                _this.prompt_message = true;
                setTimeout(function () {
                    _this.prompt_message = false;
                }, 500);
            } else {
                let $obj = $('#' + onOff + type + "-div");
                let flag = true;
                let reg = /\d/;
                let dataType = _this.datas1[index].dataType;
                let text2 = _this.datas1[index].sysConfig2;
                if (type === 1) {
                    if (dataType === 1) {//数字格式
                        flag = reg.test(text1);
                        text1 = parseInt(text1);
                        text2 = parseInt(text2);
                        if(flag && text1<=0){
                        	flag = false;
                        	_this.hint = "请输入大于0的数";
                            _this.prompt_message = true;
                            setTimeout(function () {
                                _this.prompt_message = false;
                            }, 500);
                        }
                    } else if (dataType === 2) {//时间格式
                        reg = /\d{2}:\d{2}:\d{2}/;
                        flag = reg.test(text1);
                    } else if (dataType === 4) {
                        _this.list2 = _this.datas1[index].list2;
                        for (let i in _this.list2) {
                            if (text1 === _this.list2[i].id) {
                                _this.datas1[index].sysConfig2 = _this.list2[i].name;
                            }
                        }
                    }else{
                    	if(!text1 || $.trim(text1).length<=0 || $.trim(text1).length>=200){
                    		flag = false;
                    		 _this.hint = "请正确输入内容";
                             _this.prompt_message = true;
                             setTimeout(function () {
                                 _this.prompt_message = false;
                             }, 500);
                    	}
                    }
                    if (_this.datas1[index].isInput === 3 || _this.datas1[index].isInput === 4) {

                        flag = text1 < text2;
                        if (!flag) {
                            _this.hint = "请输入正确的范围";
                            _this.prompt_message = true;
                            setTimeout(function () {
                                _this.prompt_message = false;
                            }, 500);
                        }
                    }
                    if (flag) {
                        _this.datas1[index].sysConfig1 = text1;
                    }

                } else if (type === 2) {
                    if (dataType === 1) {//数字格式
                    	text1 = parseInt(text1);
                        text2 = parseInt(text2);
                        flag = reg.test(text1);
                        if(flag && text1<=0){
                        	flag = false;
                        	_this.hint = "请输入大于0的数";
                            _this.prompt_message = true;
                            setTimeout(function () {
                                _this.prompt_message = false;
                            }, 500);
                        }
                    } else if (dataType === 2) {//时间格式
                        reg = /\d{2}:\d{2}:\d{2}/;
                        flag = reg.test(text1);
                    }else{
                    	if(!text1 || $.trim(text1).length<=0 || $.trim(text1).length>=200){
                    		flag = false;
                    		 _this.hint = "请正确输入内容";
                             _this.prompt_message = true;
                             setTimeout(function () {
                                 _this.prompt_message = false;
                             }, 500);
                    	}
                    }

                    if (flag) {
                        if (_this.datas1[index].isInput === 3 || _this.datas1[index].isInput === 4) {
                            let text2 = _this.datas1[index].sysConfig1;
                            flag = text2 < text1;
                        }
                        if (flag) {
                            _this.datas1[index].sysConfig2 = text1;
                        } else {
                            _this.hint = "请输入正确的范围";
                            _this.prompt_message = true;
                            setTimeout(function () {
                                _this.prompt_message = false;
                            }, 500);
                        }

                    } else {
                        _this.hint = "请输入正确的格式";
                        _this.prompt_message = true;
                        setTimeout(function () {
                            _this.prompt_message = false;
                        }, 500);
                    }
                    //_this.datas1[index].sysConfig2=text1;
                }

                if (flag) {
                    $('#' + onOff + type).show();
                    $obj.toggleClass("hide");
                    $obj.toggleClass("show");
                    _this.preObj = {};
                    _this.preDiv = {};
                }

            }

            //this.$set(this.datas1, index, obj);
        },
        cancelText: function (onOff, type, index) {//取消改变文本内容后
            let _this = this;
            let $obj = $('#' + onOff + type + "-div");
            $('#' + onOff + type).show();

            $obj.toggleClass("hide");
            $obj.toggleClass("show");
            _this.preObj = {};
            _this.preDiv = {};
        },
        //点击系统菜单配置切换内容
        systemChange: function (moduleId) {
            let _this = this;
            _this.cancelPre(_this);
            let data = {
                "moduleId": moduleId
            };
            let options = {
                type: "get",
                url: "/sysConfigure/querySysConfigure",
                dataType: "json",
                data: data,
                success: function (data) {
                    if (data.code == 200) {
                        _this.datas1 = data.body;
                        $("#myClickTest li").removeClass("layui-this");//展示第一个样式
                        $("#moduleId_" + moduleId).addClass("layui-this");
                    }
                    //console.log(data)
                },
                error: function (msg) {
                    //console.log(msg)
                }
            };
            base.sendRequest(options);
        },
        saveData: function () {//保存系统设置数据
            let _this = this;
            let obj = _this.datas1;
            let flag = true;
            let text1 = "请输入";
            for (let i in obj) {
                if ((obj[i].isInput === 1 && obj[i].onOff === 1 || obj[i].isInput === 2 && obj[i].onOff === 0) && (!obj[i].sysConfig1 || $.trim(obj[i].sysConfig1) === '')) {
                    flag = false;
                    text1 += obj[i].configureExplain + "!";
                } else if ((obj[i].isInput === 3 && obj[i].onOff === 1 || obj[i].isInput === 4 && obj[i].onOff === 0) && (!obj[i].sysConfig2 || !obj[i].sysConfig1 ||
                    $.trim(obj[i].sysConfig2) === '' || $.trim(obj[i].sysConfig1) === '')) {

                    flag = false;
                    text1 += obj[i].configureExplain + "!";
                }
            }
            if (flag) {
                let options = {
                    type: "post",
                    url: "/sysConfigure/updateSysConfigure",
                    dataType: "json",
                    data: {"configureList": JSON.stringify(obj)},
                    success: function (data) {
                        if(data.code==200){          
                             	layer.msg(data.msg,{time:2000})
                        }
                    },
                    error: function (msg) {
                        //console.log(msg)
                    }
                };
                base.sendRequest(options);
            } else {
                _this.hint = text1;
                _this.prompt_message = true;
                setTimeout(function () {
                    _this.prompt_message = false;
                }, 500);
            }

        },
        //展示列表
        updateTextarea: function (info, index) {
            $('.layui-layer-btn0').css('background-color', '#009688');
            let _this = this;
            layer.open({
                title: '编辑'+info.configureExplain,
                type: 1,
                content:'<textarea id="'+info.configure+'_Textarea" name="" required lay-verify="required" placeholder="请输入'+info.configureExplain+'" class="layui-textarea" style="height:200px;">'+info.sysConfig1+'</textarea>',
                area: ['52%', '40%'],
                btn: ['确定', '取消'],
                yes: function (text) {
                   let result = $('#'+info.configure+'_Textarea').val();
                   if(result && $.trim(result) != ''){
                	   _this.datas1[index].sysConfig1 = $.trim(result);
                	   layer.closeAll('page');
                   }else{
                	   layer.msg('请正确输入内容');
                   }
                },
                btn2: function () {

                }
            })
        },
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
        //展示列表
        updateText: function (info, index) {
            $('.layui-layer-btn0').css('background-color', '#009688');
            let _this = this;
            _this.getAllColor();
            _this.percentId = info;
            _this.sysConfig2 = _this.percentId.sysConfig2.split(',');
            console.log(_this.sysConfig2)
            layer.open({
                title: '选择返利彩种',
                type: 1,
                content: $('.color'),
                area: ['56%', '56%'],
                btn: ['确定', '取消'],
				yes: function(test) {
					if(_this.sysConfig2==''){
						layer.msg('必须选择至少一个彩种');
						return
					}
					_this.datas1[index].sysConfig2 =  _this.sysConfig2.join(',');
					console.log(_this.datas1[index].sysConfig2)
                	layer.closeAll('page');
				},
                btn2: function () {

                }
            })
        },
        checkedAll:function(){
			let _this = this;
		    if (!_this.checked) {//实现反选
		      _this.sysConfig2 = [];
		    }else{//实现全选
		      _this.sysConfig2 = [];
		      _this.gameType.forEach(function(item) {
		        _this.sysConfig2.push(item.gameID);
		      });
		    }
		},
        //展示列表
        listDetail: function (list, index) {
            $('.layui-layer-btn0').css('background-color', '#009688');
            this.list = list;
            let _this = this;
            layer.open({
                title: '返利配置详情',
                type: 1,
                content: $('.sysListData'),
                area: ['52%', '37%'],
                btn: ['确定', '取消'],
                yes: function () {
                    let result = _this.list;
                    let t = true;
                    for (let i in result) {
                        if (!result[i].startRange || !result[i].endRange || $.trim(result[i].startRange) === '' || $.trim(result[i].endRange) === '') {
                            t = false;
                        } else if (parseInt(result[i].startRange) > parseInt(result[i].endRange)) {
                            t = false;
                        }
                    }
                    if (t) {
                        _this.datas.list = _this.list;
                        layer.closeAll('page');
                        layer.msg('保存成功!');
                    } else {
                        layer.msg('请输入正确的范围');
                    }
                },
                btn2: function () {

                }
            })
        },
        addDetail: function () {
            let _this = this;
            let item = {startRange: '', endRange: '', value: ""};
            _this.list.push(item);
        },
        delDetail: function (index) {
            let _this = this;
            _this.list.splice(index, 1);
            //layer.msg('删除成功');
        }
    },
    watch:{
    	sysConfig2:function(){
    		if(this.sysConfig2.length==this.gameType.length){
    			this.checked=true;
    		}else{
    			this.checked=false;
    		}
    	}
    }
});
//app.systemChange(1);//没用
$(function () {
    $("#mylay-tab-title div:first").addClass("layui-show");//默认加载第一个系统模块
    $("#myClickTest li:first").addClass("layui-this");//展示第一个样式
});
