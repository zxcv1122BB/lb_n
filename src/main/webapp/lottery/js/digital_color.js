let app = new Vue({
	el: '#app',
	data: {
		selectAll: [], //所有大项
		sel: 0, //监听下拉框的值
		datas: [], //数据

		game_name: '', //绑定一级玩法中文名
		href_url: [], //绑定跳转开奖url
		href_url2: [], //绑定到跳转投注url
		pic_url: [], //图片url
		support_week: [], //支持周几
		style: [], //样式
		status: [], //状态 
		sys_config1: [], //配置信息1
		sys_config2: [], //配置信息2
		sort: '', //顺序
		show_name: [], //
		show_type: [],
		is_hot: [], //是否热门

		showName: [],
		hrefUrl: [], //绑定跳转开奖url
		hrefUrl2: [], //绑定到跳转投注url
		picUrl: [], //图片url
		showStyle: [], //样式
		sysConfig1: [], //配置信息1
		sysConfig2: [], //配置信息2
		showType: [],
		isHot: [],

		big_datas: [], //大类数据(二级玩法)
		big_editName: [], //修改名称(二级玩法)
		big_editSort: [], //修改排序(二级玩法)

		arr: [],
		small_datas: [], //小类数据(三级玩法)
		sec_datas: [],
		abc: 0, //控制选中后active的变量(三级玩法)
		cue: '', //小类编辑里面的玩法提示(三级玩法)
		winningNote: '', //小类编辑里面的中奖说明(三级玩法)
		winningCase: '', //小类编辑里面的中奖案例(三级玩法)

		show_datas: [], //大类数组
		show_index: '', //大类数组id
		showIndex: '', //大类切换id

		page_num: 5,
		total: '',

		pageIndex: '',

		more_prize: [], //多赔率
		max_prize:[],
		min_prize:[],

        endTime:'',
        startTime:'',
        currentIssue:'',
        showOtherPrams: false,

		

	},
	created() {
		this.getlayer();
        this.get_time();
	},
	methods: {
		//加载layer
		getlayer() {
			layui.use('layer', function() {
				let layer = layui.layer;
			});
		},

        //加载选择开始/结束日期控件
        get_time: function () {
            //	日期设置
            laydate.render({
                elem: '#startDate', //指定元素
                format: 'yyyy-MM-dd HH:mm:ss',
                type: 'datetime',
                maxDate:'%y-%M-%d',
                value: "",
                done: function (value, date, endDate) {
                    $('.search a').removeClass('b_red');
                }
            });
            laydate.render({
                elem: '#endDate', //指定元素,
                format: 'yyyy-MM-dd HH:mm:ss',
                type: 'datetime',
                maxDate:'%y-%M-%d',
                value: "",
                done: function (value, date, endDate) {
                    $('.search a').removeClass('b_red');
                }
            })
        },
		//获取数据
		getdatas(num) {
			let _this = this;
			let data = {};
			_this.pageIndex = num;
			data = {
				pageIndex: num,
				pageNum: parseInt(this.page_num),
				pageSize: 5,
				act_id: 1,
			};
			//console.log(data)
			let options = {
				type: "post",
				data: data,
				url: '/game/getGameInfo',
				success: function(data) {
					////console.log(data);
					if(data.code == 200) {
						_this.datas = data.body.list;
						_this.total = data.body.total;
						//分页的(右边点击)
						$('#fenye').jqPaginator('option', {
							totalPages: data.body.pages
						});
					}
				},
				error: function(msg) {
					//console.log(msg);
				}

			};
			base.sendRequest(options);
		},
		changeStatus(id, item, index) {
			var _this = this;
			let data = {
				act_id: 1,
				id: item.gameID,
				status: id,

			};
			let obj = {
				type: "post",
				data: data,
				url: '/game/updateGameInfo',
				success: function(data) {
					////console.log(data);
					if(data.code == 200) {
						_this.datas[index].status = id;

						layer.msg(data.msg);
					}
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(obj);
		},
		chanShow(id) {
			if(this.show_type.length > 1) {
				if(this.showType[this.showIndex][id] == 0) {
					this.showType[this.showIndex][id] = '1';
				} else {
					this.showType[this.showIndex][id] = '0';
				}
				//console.log(this.showType)
			} else {
				if(this.showType[id] != 1) {
					this.showType[id] = '1';
				} else {
					this.showType[id] = '0';
				}
				//console.log(this.showType)
			}
		},
		//点击编辑
		edit(item, index, showIndex) {
            if(item.gameID==41){
                this.showOtherPrams = true;

            }else{
                this.showOtherPrams = false;
            }
			this.show_name = [];
			this.style = [];
			this.href_url = [];
			this.href_url2 = [];
			this.pic_url = [];
			this.is_hot = [];
			this.show_type = [];
			this.sys_config1 = [];
			this.sys_config2 = [];
			if(item == 0) {
				item = this.show_datas;
			} else {
				this.show_datas = item;
			}
			if(index == -1) {
				index = this.show_index
			} else {
				this.show_index = index;
			}
			this.showIndex = showIndex;
			//console.log(this.show_index)
			if(item.day_weak == undefined) {
				item.day_weak = '';
				this.support_week = [];
			} else {
				if(item.day_weak.length == 0) {
					this.support_week = [];
				} else {
					this.support_week = item.day_weak.split(',');
				}
			}
			if(item.gameName == undefined) {
				item.gameName = '';
				this.game_name = item.gameName;
			} else {
				this.game_name = item.gameName;
			}









//			if(item.style == undefined) {
//				item.style = [];
//				this.style = item.style;
//			} else {
//				if(item.style.indexOf("#") >= 0) {
//					this.style = item.style.split('#');
//					this.showStyle = this.style[showIndex];
//				} else {
//					this.showStyle = item.style;
//				}
//			}
			if(item.show_type == undefined) {
				item.show_type = [];
				this.show_type = item.show_type;
			} else {
				if(item.show_type.indexOf("#") >= 0) {
					this.show_type = item.show_type.split('#');
					for(var i = 0; i < this.show_type.length; i++) {
						this.showType[i] = this.show_type[i].split('|');
					}

				} else {
					this.showType = item.show_type.split('|');
				}
				if(this.show_type.length > 1) {
					for(var i = 0; i < this.showType[showIndex].length; i++) {
						if(this.showType[showIndex][i] == 1) {
							$('#show_' + i).prop('checked', 'checked');
						} else {
							$('#show_' + i).removeProp('checked');
						}
					}
				} else {
					for(var i = 0; i < this.showType.length; i++) {
						if(this.showType[i] == 1) {
							$('#show_' + i).prop('checked', 'checked');
						} else {
							$('#show_' + i).removeProp('checked');
						}
					}
				}
			}
			if(item.sys_config1 == undefined) {
				item.sys_config1 = [];
				this.sys_config1 = item.sys_config1;
			} else {
				if(item.sys_config1.indexOf("#") >= 0) {
					this.sys_config1 = item.sys_config1.split('#');
					this.sysConfig1 = this.sys_config1[showIndex];
				} else {
					this.sysConfig1 = item.sys_config1;
				}
			}
			if(item.is_hot == undefined) {
				item.is_hot = [];
				this.is_hot = item.is_hot;
			} else {
				if(item.is_hot.indexOf("#") >= 0) {
					this.is_hot = item.is_hot.split('#');
					this.isHot = this.is_hot[showIndex];
				} else {
					this.isHot = item.is_hot;
				}
			}
//			if(item.sys_config2 == undefined) {
//				item.sys_config2 = [];
//				this.sys_config2 = item.sys_config2;
//			} else {
//				if(item.sys_config2.indexOf("#") >= 0) {
//					this.sys_config2 = item.sys_config2.split('#');
//					this.sysConfig2 = this.sys_config2[showIndex];
//				} else {
//					this.sysConfig2 = item.sys_config2;
//				}
//			}
			if(item.sort == undefined) {
                item.sort = '';
                this.sort = item.sort;
            } else {
                this.sort = item.sort;
            }
            if(item.current_issue == undefined) {
                item.current_issue = '';
                this.currentIssue = item.current_issue;
            } else {
                this.currentIssue = item.current_issue;
            }
            if(item.start_time == undefined) {
                item.start_time = '';
                this.startTime = item.start_time;
            } else {
                this.startTime = item.start_time;
            }

            if(item.end_time == undefined) {
                item.end_time = '';
                this.endTime = item.end_time;
            } else {
                this.endTime = item.end_time;
            }



			if(item.show_name == undefined) {
				item.show_name = [];
				this.show_name = item.show_name;
			} else {
				if(item.show_name.indexOf("#") >= 0) {
					this.show_name = item.show_name.split('#');
					this.showName = this.show_name[showIndex];
				} else {
					this.showName = item.show_name;
				}
			}
//			if(item.forward_url == undefined) {
//				item.forward_url = [];
//				this.href_url = item.forward_url;
//			} else {
//				if(item.forward_url.indexOf("#") >= 0) {
//					this.href_url = item.forward_url.split('#');
//					this.hrefUrl = this.href_url[showIndex];
//				} else {
//					this.hrefUrl = item.forward_url;
//				}
//			}
//			if(item.bet_url == undefined) {
//				item.bet_url = [];
//				this.href_url2 = item.bet_url;
//			} else {
//				if(item.bet_url.indexOf("#") >= 0) {
//					this.href_url2 = item.bet_url.split('#');
//					this.hrefUrl2 = this.href_url2[showIndex];
//				} else {
//					this.hrefUrl2 = item.bet_url;
//				}
//			}
//			if(item.pic_url == undefined) {
//				item.pic_url = [];
//				this.pic_url = item.pic_url;
//			} else {
//				if(item.pic_url.indexOf("#") >= 0) {
//					this.pic_url = item.pic_url.split('#');
//					this.picUrl = this.pic_url[showIndex];
//				} else {
//					this.picUrl = item.pic_url;
//				}
//			}




			let _this = this;
			layer.open({
				title: '修改一级玩法',
				type: 1,
				content: $('.pop_edit'),
				area: ['70%', '90%'],
				btn: ['确定', '取消'],
				yes: function(index2) {
					if(!_this.showName){
						layer.msg("彩种名称不能为空!");
						return
					}
					var sys_config1,
//					sys_config2,
//					pic_url, 
//					href_url,
//					href_url2, 
//					style,
					showtype,
					is_hot;
					if(_this.show_type.length > 1) {
						for(var i = 0; i < _this.showType.length; i++) {
							_this.showType[i] = _this.showType[i].join('|');
						}
						showtype = _this.showType.join('#');
					} else {
						showtype = _this.showType.join('|');
					}
					if(_this.is_hot.length > 1) {
						is_hot = _this.is_hot.join('#');
					} else {
						is_hot = _this.isHot;
					}
					if(_this.sys_config1) {
						sys_config1 = _this.sys_config1.join('#');
					} else {
						layer.msg('配置信息1不可为空');
						return;
					}
					//					if(_this.is_hot) {
					//						is_hot = _this.is_hot.join('#');
					//					} else {
					//						layer.msg('是否热门不可为空')
					//						return;
					//					}
//					if(_this.style) {
//						style = _this.style.join('#');
//					} else {
//						layer.msg('样式不可为空')
//						return;
//					}
//					if(_this.sys_config2) {
//						sys_config2 = _this.sys_config2.join('#');
//
//					} else {
//						layer.msg('配置信息2不可为空')
//						return;
//					}
//					if(_this.pic_url) {
//						pic_url = _this.pic_url.join('#');
//
//					} else {
//						layer.msg('图片路径不可为空')
//						return;
//					}
//					if(_this.href_url) {
//						href_url = _this.href_url.join('#');
//
//					} else {
//						layer.msg('跳转开奖不可为空')
//						return;
//					}
//					if(_this.href_url2) {
//						href_url2 = _this.href_url2.join('#');
//
//					} else {
//						layer.msg('跳转投注不可为空')
//						return;
//					}
                    let str = _this.support_week.toString();
					var datapramas = {};
					if(item.gameID==41){
                        _this.startTime = $('#startDate').val();
                        _this.endTime = $('#endDate').val();
						datapramas ={
                            act_id: 1,
                            id: item.gameID,
                            showName: _this.showName,
                            gameName:_this.game_name,
                            day_weak: str,
                            game_type: item.game_type,
                            is_hot: is_hot,
                            kind_name: item.kind_name,
                            max_issue: item.max_issue,
                            status: item.status,
							// sort: item.sort,
							sort:_this.sort,
                            show_type: showtype,
                            end_time: _this.endTime,
                            start_time: _this.startTime,
                            current_issue: _this.currentIssue,
						}
					}else {
                        datapramas = {
                            act_id: 1,
                            id: item.gameID,
                            showName: _this.showName,
                            gameName:_this.game_name,
                            day_weak: str,
                            game_type: item.game_type,
                            is_hot: is_hot,
                            kind_name: item.kind_name,
                            max_issue: item.max_issue,
                            status: item.status,
							// sort: item.sort,
							sort:_this.sort,
                            sys_config1: sys_config1,
                            show_type: showtype,
						}

					}


					// let data = datapramas;
					//console.log(data)
					let obj = {
						type: "post",
						data: datapramas,
						url: '/game/updateGameInfo',
						success: function(data) {
							////console.log(data);
							if(data.code == 200) {
								window.location.reload();
								//								_this.datas[index].gameName = _this.game_name;
								//								_this.datas[index].forward_url = _this.href_url;
								//								_this.datas[index].bet_url = _this.href_url2;
								//								_this.datas[index].pic_url = _this.pic_url;
								//								_this.datas[index].day_weak = str;
								//								_this.datas[index].is_hot = _this.isHot;
								//								_this.datas[index].style = _this.style;
								//								_this.datas[index].sys_config1 = _this.sys_config1;
								//								_this.datas[index].sys_config2 = _this.sys_config2;
								//								_this.datas[index].sort = _this.sort;
								layer.msg(data.msg);
								layer.closeAll();
							}else {
                                layer.msg(data.msg);
							}
						},
						error: function(msg) {
							//console.log(msg);
						}
					};
					base.sendRequest(obj);
				}
			});
			$('.layui-layer-shade').hide();
			$('#layui-layer-shade1').show();
		},
		//点击大类设置
		set_big(id) {
			let _this = this;
			let data = {
				act_id: 2,
				id: id,
				//				pageIndex: _this.pageIndex,
				//	            pageNum: parseInt(_this.page_num),
				//	            pageSize: 5,
			};
			let obj = {
				type: "post",
				data: data,
				url: '/game/getGameInfo',
				success: function(data) {
					if(data.code == 200) {
						_this.big_datas = data.body;
						layer.open({
							title: '彩票玩法大类设置',
							type: 1,
							content: $('.pop_big'),
							area: ['60%', '60%'],
							btn: ['确定'],
							yes: function(index) {
								layer.close(index);
							}
						})
					}
				}
			};
			base.sendRequest(obj);
		},
		//点击大类里面的编辑
		edit_big(item, index) {
			this.big_editName = item.groupName;
			this.big_editSort = item.sort;
			let _this = this;
			layer.open({
				title: '玩法大类修改',
				type: 1,
				content: $('.pop_bigSet'),
				area: ['40%', '40%'],
				btn: ['确定', '取消'],
				yes: function(index2) {
					if(_this.big_editName.trim() == '' || _this.big_editName.trim().length > 10) {
						layer.msg('名称不能为空或长度不超过10个字');
						return;
					}
					if(_this.big_editSort == '' || _this.big_editSort.length > 3) {
						layer.msg('排序不能为空或长度不超过3位');
						return;
					}
					let data = {
						act_id: 2,
						id: item.id,
						groupName: _this.big_editName,
						sort: _this.big_editSort,
						status: item.status,
						type: item.type
					};
					let obj = {
						type: 'post',
						data: data,
						dataType: 'json',
						url: '/game/updateGameInfo',
						success: function(data) {
							////console.log(data);
							if(data.code == 200) {
								_this.big_datas[index].groupName = _this.big_editName;
								_this.big_datas[index].sort = _this.big_editSort;
								layer.close(index2);
							} else {
								layer.msg(data.msg);
							}
						},
						error: function(msg) {
							//console.log(msg);
						}
					};
					base.sendRequest(obj);
				}
			})
		},
		//点击大类里面的状态
		change(item, index) {
			let _this = this;
			let a = 0;
			if(item.status == 1) {
				a = 0;
			} else {
				a = 1;
			}
			let data = {
				act_id: 2,
				id: item.id,
				groupName: item.groupName,
				sort: item.sort,
				status: a,
				type: item.type
			};
			let obj = {
				type: 'post',
				data: data,
				dataType: 'json',
				url: '/game/updateGameInfo',
				success: function(data) {
					////console.log(data);
					if(data.code == 200) {
						_this.big_datas[index].status = a;
						layer.msg(data.msg);
					} else {
						layer.msg(data.msg);
					}
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(obj);
		},
		//点击小类设置
		set_small(id, index) {
			let _this = this;
			let data = {
				act_id: 2,
				id: id,
				//				pageIndex: _this.pageIndex,
				//	            pageNum: parseInt(this.page_num),
				//	            pageSize: 5,
			};
			let obj = {
				type: "post",
				data: data,
				url: '/game/getGameInfo',
				success: function(data) {
					////console.log(data);
					if(data.code == 200) {
						_this.arr = data.body;
						_this.getallDatas(_this.arr[0].id, 0, id);
						layer.open({
							title: '彩票玩法小类设置',
							type: 1,
							content: $('.pop_small'),
							area: ['80%', '80%'],
							btn: ['确定'],
							yes: function(index) {
								layer.close(index);
								_this.keep;
								_this.abc = 0;
							},
							cancel: function(index, layero) {
								_this.abc = 0;
							}
						})
					}
				}
			};
			base.sendRequest(obj);
		},
		getallDatas(id, index, gameid) {
			let _this = this;

			let data = {
				act_id: 3,
				id: gameid,
				//				pageIndex: _this.pageIndex,
				//	            pageNum: parseInt(this.page_num),
				//	            pageSize: 5,
			};
			let obj = {
				type: "post",
				data: data,
				url: '/game/getGameInfo',
				success: function(data) {
					////console.log(data);
					if(data.code == 200) {
						_this.sec_datas = data.body;
						_this.get_threeDatas(id, index)
					}
				}
			};
			base.sendRequest(obj);
		},
		//获取三级获取
		get_threeDatas(id, index) {
			let _this = this;
			_this.abc = index;
			_this.small_datas = [],str="";
			for(var i = 0; i < _this.sec_datas.length; i++) {
				if(id == _this.sec_datas[i].groupId) {
					_this.sec_datas[i].o_min_prize=_this.sec_datas[i].min_prize+"";
					_this.sec_datas[i].o_max_prize=_this.sec_datas[i].max_prize+"";
					_this.small_datas.push(_this.sec_datas[i]);
				}
			}
			//console.log(id, _this.small_datas);
		},
		//点击小类设置里面的保存
		keep(item, index) {
			if(item.game_tips == undefined) {
				item.game_tips = '';
			}
			if(item.win_explain == undefined) {
				item.win_explain = '';
			}
			if(item.win_example == undefined) {
				item.win_example = '';
			}
			if(item.min_prize === '' || item.min_prize == undefined) {
				layer.msg('最小赔率不能为空');
				return;
			}
			if(item.max_prize === '' || item.max_prize == undefined) {
				layer.msg('最大赔率不能为空');
				return;
			}
			if(item.max_reward === '' || item.max_reward == undefined || item.max_reward.length > 20) {
				layer.msg('最大返利不能为空');
				return;
			}
			if(item.maxAmount === '' || item.maxAmount == undefined || item.maxAmount.length > 20) {
				layer.msg('最大投注金额不能为空');
				return;
			}
			if(item.minAmount === '' || item.minAmount == undefined || item.minAmount.length > 20) {
				layer.msg('最小投注金额不能为空');
				return;
			}
			if(item.sort === '' || item.sort == undefined || item.sort.length > 20) {
				layer.msg('排序不能为空');
				return;
			}
			let _this = this;
			let data = {
				act_id: 3,
				id: item.id3,
				status: item.status,
				game_tips: item.game_tips,
				groupId: item.groupId,
				invok_function: item.invok_function,
				maxBet: item.maxBet,
				max_prize: item.max_prize,
				max_reward: item.max_reward,
				maxAmount: item.maxAmount,
				minAmount: item.minAmount,
				min_prize: item.min_prize,
				sort: item.sort,
				name: item.name,
				open_invok: item.open_invok,
				type: item.type,
				win_example: item.win_example,
				win_explain: item.win_explain,
			};
			let obj = {
				type: 'post',
				data: data,
				dataType: 'json',
				url: '/game/updateGameInfo',
				success: function(data) {
					////console.log(data);
					if(data.code == 200) {
						layer.msg(data.msg);
					} else {
						layer.msg(data.msg);
					}
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(obj);
		},
		//点击小类设置里面的编辑
		popSmallEdit(item, index) {
			if(item.game_tips == undefined) {
				this.cue = '';
			} else {
				this.cue = item.game_tips;
			}
			if(item.win_explain == undefined) {
				this.winningNote = '';
			} else {
				this.winningNote = item.win_explain;
			}
			if(item.win_example == undefined) {
				this.winningCase = '';
			} else {
				this.winningCase = item.win_example;
			}
			let _this = this;
			layer.open({
				title: '玩法小类修改',
				type: 1,
				content: $('.pop_smallSet'),
				area: ['70%', '60%'],
				btn: ['确定', '取消'],
				yes: function(index2) {
					if(_this.cue.trim() == '' || _this.cue.trim().length > 254) {
						layer.msg('玩法提示不能为空或长度过长(不超过254个字)');
						return;
					}
					if(_this.winningNote.trim() == '' || _this.winningNote.trim().length > 254) {
						layer.msg('中奖说明不能为空或长度过长(不超过254个字)');
						return;
					}
					if(_this.winningCase.trim() == '' || _this.winningCase.trim().length > 254) {
						layer.msg('中奖案例不能为空或长度过长(不超过254个字)');
						return;
					}
					let data = {
						act_id: 3,
						id: item.id3,
						status: item.status,
						game_tips: _this.cue,
						groupId: item.groupId,
						invok_function: item.invok_function,
						maxBet: item.maxBet,
						max_prize: item.max_prize,
						max_reward: item.max_reward,
						maxAmount: item.maxAmount,
						minAmount: item.minAmount,
						min_prize: item.min_prize,
						name: item.name,
						open_invok: item.open_invok,
						sort: item.sort,
						type: item.type,
						win_example: _this.winningCase,
						win_explain: _this.winningNote,
					};
					let obj = {
						type: 'post',
						data: data,
						dataType: 'json',
						url: '/game/updateGameInfo',
						success: function(data) {
							////console.log(data);
							if(data.code == 200) {
								_this.small_datas[index].game_tips = _this.cue;
								_this.small_datas[index].win_example = _this.winningCase;
								_this.small_datas[index].win_explain = _this.winningNote;
								layer.msg(data.msg);
								layer.close(index2);
							} else {
								layer.msg(data.msg);
							}
						},
						error: function(msg) {
							//console.log(msg);
						}
					};
					base.sendRequest(obj);
				}
			})
		},
		//点击小类里面的状态
		change2(item, index) {
			let _this = this;
			let a = 0;
			if(item.status == 1) {
				a = 0;
			} else {
				a = 1;
			}
			let data = {
				act_id: 3,
				id: item.id3,
				status: a,
				game_tips: item.game_tips,
				groupId: item.groupId,
				invok_function: item.invok_function,
				maxBet: item.maxBet,
				max_prize: item.max_prize,
				max_reward: item.max_reward,
				maxAmount: item.maxAmount,
				minAmount: item.minAmount,
				min_prize: item.min_prize,
				name: item.name,
				open_invok: item.open_invok,
				sort: item.sort,
				type: item.type,
				win_example: item.win_example,
				win_explain: item.win_explain,
			};
			let obj = {
				type: 'post',
				data: data,
				dataType: 'json',
				url: '/game/updateGameInfo',
				success: function(data) {
					////console.log(data);
					if(data.code == 200) {
						_this.small_datas[index].status = a;
						layer.msg(data.msg);
					} else {
						layer.msg(data.msg);
					}
				},
				error: function(msg) {
					//console.log(msg);
				}
			};
			base.sendRequest(obj);
		},
		prizeKey: function(index) {
			var _this = this;
//			_this.more_prize = [];
//			_this.max_prize = [];
//			_this.min_prize = [];
			if(_this.small_datas[index].prize_key) {
				_this.more_prize = _this.small_datas[index].prize_key.split('|');
				_this.max_prize = _this.small_datas[index].max_prize.split('|');
				_this.min_prize = _this.small_datas[index].min_prize.split('|');
				layer.open({
					title: '玩法小类修改',
					type: 1,
					content: $('.prize_key'),
					area: ['50%', '50%'],
					btn: ['确定', '取消'],
					yes: function(index2) {
						_this.small_datas[index].prize_key= _this.more_prize.join('|');
						_this.small_datas[index].max_prize = _this.max_prize.join('|');
						_this.small_datas[index].min_prize = _this.min_prize.join('|');
						layer.close(index2);
					}
				})
			}
		}
	},
	watch: {
		// startTime: function(){
		// 	var getTime = new Date(this.startTime);
		// 	console.log(getTime.getMinutes() +3.5);
		//
		// },

        // currentIssue:function(){
        //     this.currentIssue =this.currentIssue.replace(/[^0-9]/g,'');
		//
		// },

//		showStyle: function() {
//			this.style[this.showIndex] = this.showStyle;
//		},
//		picUrl: function() {
//			this.pic_url[this.showIndex] = this.picUrl;
//		},
//		sysConfig1: function() {
//			this.sys_config1[this.showIndex] = this.sysConfig1;
//		},
//		sysConfig2: function() {
//			this.sys_config2[this.showIndex] = this.sysConfig2;
//		},
		hrefUrl: function() {
			this.href_url[this.showIndex] = this.hrefUrl;
		},
		hrefUrl2: function() {
			this.href_url2[this.showIndex] = this.hrefUrl2;
		},
		isHot: function() {
			this.is_hot[this.showIndex] = this.isHot;
		},
		page_num: function() {
			this.getdatas(1);
		},
		min_prize:function(){
			for(var i=0;i<this.min_prize.length;i++){
				if(this.min_prize[i]<1){
					this.min_prize[i]=1;
				}
			}
		},
		max_prize:function(){
			for(var i=0;i<this.max_prize.length;i++){
				if(this.max_prize[i]<1){
					this.max_prize[i]=1;
				}
			}
		},
		showName:function(val){
			if(!val){
				layer.msg("彩种名称不能为空!");
			}
		}
	}
});

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