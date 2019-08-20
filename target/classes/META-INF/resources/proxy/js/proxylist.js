/**
 * Created by ASUS on 2017/9/12.
 */
var app = new Vue({
	el: '#app',
	data: {
		pageNum: 50, //默认每页5条数据
		datas: [], //列表数据
		total: '', //总条数
		userInfoList: [],
		
		prevData:'',//搜索记录
		keywordOption: 1,
		keywordValue: "",
		optionKey: "",
		obj: {
			id:'', 								
			pid:'',						//父级代理的id
			status:'',					//禁用，启用状态
			delStatus:'',					//加扣款：禁用，启用状态
			loginAccount:'', 			//代理账号
			password:'',				//登录密码
			coinPssword:'',				//提款密码
			proxyName:'',				//代理姓名
			tell:'',					//手机号
			email:'', 					//邮箱
			qq:'',						//qq
			bankName:'',				//取款银行
			parentName:'',				//父级代理的名称
			bankAccount:'', 			//银行账户
			bankAddress:'', 			//开户分行
			rebateRatio:'',				//返点数
		},  //更新代理数据
		obj1: {
			id:'', 
			pid:'',
			status:1,
			loginAccount:'', 
			password:'',
			coinPssword:'',
			proxyName:'',
			tell:'',
			email:'', 
			qq:'',
			bankName:'',
			parentName:'',
			bankAccount:'', 
			bankAddress:'', 
			rebateRatio:'',
		},  //添加代理数据
		proxyInfo:[],//所有代理信息
		
		 //修改密码
        zhanghao1:'',//账号
    	pssword:'',//新密码
    	rePssword:'',//确认密码密码
    	passwordType:'1',
		
		flag:false,//代理账号是否存在标记
		flag_email:true,
		flag_tel:true,
        Rebackdata:[]
	},
	created: function() {
	},
	methods: {
        proxy_rebate:function(datas){
        	var _this = this, datas = datas.replace("ssc","时时彩").replace("k3","快3").replace("11x5","11选5").replace("3D","福彩3D").replace("PK10","PK10").replace('hk6','六合彩').replace('7xc','七星彩').replace('kl10f','快乐十分').replace('PCDD','北京28');
        	_this.Rebackdata = datas.split("@");
            layui.use('layer', function () {
            	var layer = layui.layer;
                layer.open({
                    type: 1,
					title:'查看返点数',
                    area: ['300px', '250px'], //宽高
                    content: $('.rebackRebeta')
                });
            })

		},
		//初始化获取数据   parseInt(this.pageNum)
		getdatas: function(num) {
			var _this = this;
			var params = {
				pageIndex: num,
				pageNum: parseInt(this.pageNum),
				pageSize: 10,
			}
			if(_this.prevData!=""){
            	_this.prevData.pageIndex=num;
            	params=_this.prevData;
            }
			var obj = {
				type: "get",
				data: params,
				dataType: "json",
				url: "/proxyinfo/selectByProxyInfo",
				success: function(data) {
					//console.log(data);
					if(data.code == 200) {
						_this.total = data.body.total;
						_this.datas = data.body.list;
						if(data.body.list) {
							$('#fenye').jqPaginator('option', {
								totalPages: data.body.pages,
								currentPage: 1
							});
						} else {
							$('#fenye').jqPaginator('option', {
								totalPages: 1,
								currentPage: 1
							});
						}
					}
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(obj);
		},
		//点击搜索框执行
        search: function () {
            var _this = this;
            var obj = {
                type: 'get',
                data: {
                	pageIndex: 1,
    				pageNum: parseInt(_this.pageNum),
    				pageSize: 10,
    				keywordOption:_this.keywordOption,
    				keywordValue: _this.keywordValue.trim(),
    				
                },
                dataType: "json",
                url: "/proxyinfo/selectByProxyInfo",
				success: function(data) {
					//console.log(_this.datas)
					if(data.code == 200) {
						_this.prevData=obj.data;
						_this.total = data.body.total;
						_this.datas = data.body.list;
						if(data.body.list.length > 0) {
							$('#fenye').jqPaginator('option', {
								totalPages: data.body.pages,
								currentPage: 1
							});
						} else {
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
            base.sendRequest(obj);
        },
        //点击搜索框执行
        nextInfo: function (index,item) {
        	var _this = this;
        	var obj = {
        			type: 'get',
        			data: {
        				pageIndex: 1,
        				pageNum: parseInt(_this.pageNum),
        				pageSize: 10,
        				proxyId:item.uid,
        				optionKey: index,
        			},
        			dataType: "json",
        			url: "/proxyinfo/selectByProxyInfo",
        			success: function(data) {
        				if(data.code == 200) {
        					_this.prevData=obj.data;
        					_this.total = data.body.total;
        					_this.datas = data.body.list;
        					if(data.body.list.length > 0) {
        						$('#fenye').jqPaginator('option', {
        							totalPages: data.body.pages,
        							currentPage: 1
        						});
        					} else {
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
        	base.sendRequest(obj);
        },
		//点击修改密码
        editPassword: function (item) {
        	var _this = this;
            this.zhanghao1 =item.loginAccount;
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.open({
                    title: '修改密码',
                    type: 1,
                    content: $('.editPassword'),
                    area: ['30%','40%'],
                    btn: ['保存', '关闭'],
                    yes: function (index,layero) {
                        var md5 = hex_md5(_this.zhanghao1+_this.pssword);
                        var md5_ = hex_md5(_this.zhanghao1+_this.oldPassword);
                        var data = {
                            userName: _this.zhanghao1,
                            password: md5,
                            passwordType:_this.passwordType,
                        };
                        if (_this.pssword.trim().length < 6 || _this.pssword.trim().length > 12) {
                            layer.msg('密码长度6-12位');
                            return;
                        }
                        if (_this.pssword.trim() != _this.rePssword.trim()) {
                            layer.msg('两次密码输入不一致');
                            return;
                        }
                    	var object = {
                    			type: 'post',
                    			data: data,
                    			dataType: 'json',
                    			url: '/proxyinfo/updateProxyPassword',
                    			success: function (data) {
                    				if (data.code == 200) {
                    					layer.closeAll('page');
                    					layer.msg(data.msg);
                    					setTimeout(function () {
                    						window.location.reload();
                    					}, 1000);
                    				} else {
                    					layer.msg(data.msg);
                    				}
                    			},
                    			error: function (msg) {
                    				//console.log(msg);
                    			}
                    	};
                    	base.sendRequest(object);
                    },
                    btn2: function () {
                    }
                })
            })
        },
        
      //删除代理
        del: function (id) {
        	var _this = this;
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.open({
                	title: '提示信息',
                    type: 1,
                    content: $('.popDel'),
                    area: ['30%','30%'],
                    btn: ['确定', '取消'],
                    yes: function () {
                        let data = {
                            id: id
                        };
                        let obj = {
                            type: 'post',
                            data: data,
                            dataType: 'json',
                            url: '/proxyinfo/deleteByProxyInfo',
                            success: function (data) {
                            	if(data.code == 200) {
            						window.location.reload();
            					}
                            },
                            error: function (msg) {
                                //console.log(msg);
                            }
                        };
                        base.sendRequest(obj);
                    },
                })
            })
        
        },
		
		// 
		selectProxyInfoAll: function() {
			var _this = this;
			var obj = {
					type: "get",
					data: {},
					dataType: "json",
					url: "/proxyinfo/selectProxyInfoAll",
					success: function(data) {
						if(data.code==200) {
							_this.proxyInfo=data.body;
						}
					},
					error: function(msg) {
						//console.log(msg);
					}
			};
			base.sendRequest(obj);
		},

		//点击改变状态
		changeType: function(id, status) {
			if(status == 1) {
				status = 2;
			} else {
				status = 1;
			}
			var obj = {
				type: 'post',
				data: {
					id: id,
					status: status
				},
				dataType: "json",
				url: '/proxyinfo/updateByStatus',
				success: function(data) {
					if(data.code == 200) {
						window.location.reload();
					}
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(obj);
		},
		// 账号格式校验
		check_loginAccount:function (ele) {
			//console.log(ele);
			if (ele.trim().length < 6 || ele.trim().length > 16 || ele.trim() === '') {
				layer.msg('格式错误！英文、数字6-16位');
				return;
			}
			let a = /[\u4e00-\u9fa5]/;
			let b = ele.match(a);
			if (b) {
				layer.msg('格式错误，不能包含中文!');
				return;
			}
		},
      //点击新增
        addProxy: function () {
        	this.obj1={status:1,};
            var _this = this;
//            _this.selectProxyInfoAll();
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.open({
                    title: '添加代理',
                    type: 1,
                    content: $('.addProxy'),
                    area: ['60%','70%'],
                    btn: ['保存', '关闭'],
                    yes: function () {
                        if (!_this.obj1.loginAccount||_this.obj1.loginAccount.trim() == '') {
                            layer.msg('帐号不能为空');
                            return;
                        }
                        if (!_this.obj1.password||_this.obj1.password.trim() == '') {
                            layer.msg('密码不能为空');
                            return;
                        }
                        if (_this.obj1.password.trim().length < 6 || _this.obj1.password.trim().length > 12) {
                            layer.msg('密码长度6-12位');
                            return;
						}
						if (!_this.flag_email||!_this.flag_tel){
							layer.msg("请检测邮箱和手机号格式是否正确")
							return;
						}
						if (_this.obj1.qq != ''&&_this.obj1.qq!=undefined) {
							if (_this.obj1.qq.length < 6) {
								layer.msg("请检查QQ账号的长度")
								return;
							}
						}
						// if (_this.obj1.bankAccount!=''&&_this.obj1.bankAccount!=undefined) {
						// 	if (_this.obj1.bankAccount.trim() != ''){
						// 		if (!(/^([1-9]{1})(\d{14}|\d{18})$/.test(_this.obj1.bankAccount))) {
						// 			layer.msg('银行帐号格式错误');
						// 			return;
						// 		}
						// 	}
						// }
						if (!_this.obj1.rebateRatio||_this.obj1.rebateRatio.toString().indexOf("-")!=-1||parseFloat(_this.obj1.rebateRatio)<0||parseFloat(_this.obj1.rebateRatio)>12) {
                    		layer.msg('打码量范围为0~12');
                            return;
                        }
                        var data = {
                    		pid:parseInt(_this.obj1.pid),
                			status:parseInt(_this.obj1.status),
                			loginAccount:_this.obj1.loginAccount,
                			password:hex_md5(_this.obj1.loginAccount+_this.obj1.password),
                			coinPssword:hex_md5(_this.obj1.loginAccount+_this.obj1.coinPssword),
                			proxyName:_this.obj1.proxyName,
                			tell:_this.obj1.tell,
                			email:_this.obj1.email,
                			qq:_this.obj1.qq,
                			bankName:_this.obj1.bankName,
                			bankAccount:_this.obj1.bankAccount,
                			bankAddress:_this.obj1.bankAddress,
                			rebateRatio:_this.obj1.rebateRatio,
						};
						
                        if (_this.flag==true) {
                            var obj = {
                                type: 'post',
                                data: data,
                                dataType: 'json',
                                url: '/proxyinfo/insertProxyInfo',
                                success: function (data) {
                                    ////console.log(data);
                                    if (data.code == 200) {
                                    	_this.obj1='';
                                        layer.closeAll('page');
                                        layer.msg(data.msg);
                                        setTimeout(function () {
                                            window.location.reload();
                                        }, 1000);
                                    } else {
										layer.msg(data.msg);
                                    }
                                },
                                error: function (msg) {
                                    //console.log(msg);
                                }
                            };
                            base.sendRequest(obj);
                        } else{
                            layer.msg('代理账号已存在');
                        }
                        
                    },
                    btn2: function () {
                    }
                })
            })
        },
		//点击添加修改代理弹出框
        editProxy: function(obj) {
			var _this = this;
			_this.obj={
					id:obj.id, 
					pid:obj.pid,
					delStatus:obj.delStatus,
					status:obj.status,
					loginAccount:obj.loginAccount, 
					proxyName:obj.proxyName,
					tell:obj.tell,
					email:obj.email, 
					qq:obj.qq,
					bankName:obj.bankName,
					parentName:obj.parentName,
					bankAccount:obj.bankAccount, 
					bankAddress:obj.bankAddress, 
					rebateRatio:obj.rebateRatio,
			};
			_this.selectProxyInfoAll();
			 layui.use('layer', function () {
	                var layer = layui.layer;
	                layer.open({
	                    title: '修改代理信息',
	                    type: 1,
	                    content: $('.editProxy'),
	                    area: ['60%','60%'],
	                    btn: ['确定'],
	                    yes: function () {
	                        var data = {
	                        	id:parseInt(_this.obj.id),
                        		pid:parseInt(_this.obj.pid),
                    			status:parseInt(_this.obj.status),
                    			delStatus:_this.obj.delStatus,
                    			loginAccount:_this.obj.loginAccount,
                    			proxyName:_this.obj.proxyName,
                    			tell:_this.obj.tell,
                    			email:_this.obj.email,
                    			qq:_this.obj.qq,
                    			bankName:_this.obj.bankName,
                    			bankAccount:_this.obj.bankAccount,
                    			bankAddress:_this.obj.bankAddress,
                    			rebateRatio:_this.obj.rebateRatio,
	                        };
							//console.log(data);
							if (!_this.flag_email || !_this.flag_tel) {
								layer.msg("请检测邮箱和手机号格式是否正确")
								return;
							}
							if (_this.obj.qq != '' && _this.obj.qq != undefined){
								if (_this.obj.qq.length < 6){
									layer.msg("请检查QQ账号的长度")
									return;
								}
							}
							if (!_this.obj.rebateRatio||_this.obj.rebateRatio.toString().indexOf("-")!=-1||parseFloat(_this.obj.rebateRatio)<0||parseFloat(_this.obj.rebateRatio)>12) {
	                    		layer.msg('打码量范围为0~12');
	                            return;
	                        }
							/*if (_this.obj.bankAccount != '' && _this.obj.bankAccount != undefined) {
								if (_this.obj.bankAccount.trim() != '') {
									if (!(/^([1-9]{1})(\d{14}|\d{18})$/.test(_this.obj.bankAccount))) {
										layer.msg('银行帐号格式错误');
										return;
									}
								}
							}*/
							// 验证代理返点值信息，当代理
	                        var obj = {
	                            type: 'post',
	                            data: data,
	                            dataType: 'json',
	                            url: '/proxyinfo/updateByProxyInfo',
	                            success: function (data) {
	                                if (data.code == 200) {
	                                    layer.closeAll('page');
	                                    layer.msg(data.msg);
	                                    setTimeout(function () {
	                                        window.location.reload();
	                                    }, 1000);
	                                }else{
	                                    layer.msg(data.msg);
	                                }
	                            },
	                            error: function (msg) {
	                                //console.log(msg);
	                            }
	                        };
	                        base.sendRequest(obj);
	                    },
	                    btn2: function () {
	                    }
	                })
	         })
	    },
	  //帐号失焦检验是否存在
        check_username:function () {
			if (this.obj1.loginAccount.trim().length < 6 || this.obj1.loginAccount.trim().length > 16 || this.obj1.loginAccount.trim() === '') {
				layer.msg('格式错误！英文、数字6-16位');
				return;
			}
			let a = /[\u4e00-\u9fa5]/;
			let b = this.obj1.loginAccount.match(a);
			if (b) {
				layer.msg('格式错误，不能包含中文!');
				return;
			}
            let _this = this;
            let obj = {
                type:'get',
                data:{loginAccount:_this.obj1.loginAccount},
                dataType:'json',
                url:'/proxyinfo/checkAccount',
                success:function (data) {
                    layui.use('layer',function () {
                        var layer = layui.layer;
                        if(data.code==200){
							_this.flag= true;
							//console.log(data.msg + '正确')
                            layer.msg(data.msg);
                        }else {
							_this.flag=false;
							//console.log(data.msg+'错误')
                            layer.msg(data.msg);
                        }
                    });
                },
                error:function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
		//点击修改信息取消按钮
		escPass: function() {
			$('.zhezhao').hide();
			$('.editProxy').hide();
			$('.addProxy').hide();
		},
		//检测修改会员手机号变更时格式是否正确
		check_iphone(ele) {
			if (ele != '') {
				let a = /^09\d{8}$/;
				let b = ele.match(a);
				if (b == null) {
					layer.msg('手机号码格式错误');
					this.flag_tel = false;
				} else {
					layer.msg('手机号码格式正确');
					this.flag_tel = true;
				}
			}else{
				this.flag_tel = true;
			}
		},
		//检测修改会员邮箱变更时格式是否正确
		check_email(ele) {
			//console.log("邮箱发生了改变")
			if (ele != '') {
				let a = /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,20}/;
				let b = ele.match(a);
				//console.log(b)
				if (b == null) {
					layer.msg('邮箱格式错误');
					this.flag_email = false;
				} else {
					layer.msg('邮箱格式正确');
					this.flag_email = true;
				}
			}else{
				this.flag_email = true;
			}
		},
	},
	watch: {
		//监听下拉框的值(每页显示多少条数据)
		pageNum: function() {
			this.getdatas(1);
		},
	}
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
	onPageChange: function(num, type) {
		app.getdatas(num);
	}
});