/**
 * Created by ASUS on 2017/9/13.
 */
/**
 * Created by ASUS on 2017/9/13.
 */
try {
	document
			.writeln("<script type=\'text/javascript\' src=\'../js/md5.js\'></script>");
} catch (e) {

}
// 排序的函数
function objKeySort(arys) {
	// 先用Object内置类的keys方法获取要排序对象的属性名，再利用Array原型上的sort方法对获取的属性名进行排序，newkey是一个数组
	var newkey = Object.keys(arys).sort();
	// console.log('newkey='+newkey);
	var newObj = {}; // 创建一个新的对象，用于存放排好序的键值对
	for (var i = 0; i < newkey.length; i++) {
		// 遍历newkey数组
		newObj[newkey[i]] = arys[newkey[i]];
		// 向新创建的对象中按照排好的顺序依次增加键值对

	}
	return newObj; // 返回排好序的新对象
};
var base = {
   // BASE_URL : "http://ssg168.net/syshako",
	//ms_BASE_URL: "http://ssg168.net/lsapi1",
	 BASE_URL : "http://127.0.0.1:20815/syshako",
	//BASE_URL : "http://ssgcp.net/syshako",
	//ms_BASE_URL : "http://ssgcp.net/lsapi",
	// 加密ajax
	sendRequest : function(options) {

		if (options.url != '/getDepositAndWithdrawCount') {
			layui.use('layer', function() {
				let layer = layui.layer;
				layer.msg('加载中...', {
					time : 1000
				});
			});
		}

		options.data.timeStamp = new Date().getTime();
		if (!$.isEmptyObject(options.data)) {
			options.data = objKeySort(options.data);
			var str = '', strValue = '';
			for ( var j in options.data) {
				if (j == "sign") {
					delete options.data["sign"];
				}
				if (options.data[j] == undefined
						|| options.data[j] == 'undefined') {
					options.data[j] = '';
				}
				if (options.data[j] !== '') {
					strValue = JSON.stringify((""+options.data[j]+"").replace(/[/\r]*[/\n]*/g,""));
					strValue = strValue.replace(/[`~!$%^&()\-+={}':;,"'\[\]\/\//\\.<>?￥%…（）_+|【】‘；：”“’。，、？\s]/g,"");
					if (j != "sign") {
						str = (str + j + "=" + strValue + "&");
					}
				} else {
					if (j != "sign") {
						str = (str + j + "=" + "&");
					}
				}
			}
			str = str.substring(0, str.length - 1);
			console.log(str);
			if (md5) {
				options.data.sign = md5(str + "{" + localStorage.userName + "}")
						.toUpperCase();
			} else {
				options.data.sign = parent.md5(
						str + "{" + localStorage.userName + "}").toUpperCase();
			}

		}
		let self = this;
		return $
				.ajax({
					"type" : options.type,
					"async" : options.async ? options.async : false,
					"url" : this.BASE_URL + options.url,
					"data" : options.data,
					"xhrFields" : {
						withCredentials : true

					},
					"beforeSend" : function(request) {
						request.setRequestHeader("X-Authorization",
								localStorage.getItem("acessToken"))
					},
					"complete" : function(xhr) {
						if (xhr.readyState == 4 && xhr.status == 200) {
							if (options.success) {
								if (xhr.responseJSON != undefined) {
									if (xhr.responseJSON.code != null
											&& xhr.responseJSON.code == "600") {
										if (confirm("你已经在其他浏览器中登录!按确定,将返回登录页面")) {
											localStorage.clear();
											window.location.href = base.BASE_URL
													+ '/login.html';
										}
									} else {
										options.success.call(this,
												xhr.responseJSON, xhr);
									}
								} else {
									if (xhr.responseText) {
										xhr.responseJSON = JSON
												.parse(xhr.responseText);
										options.success.call(this,
												xhr.responseJSON, xhr);
									}
								}
							}
						} else {
							options.error.call();
						}
					}
				});
	},
	// 不加密ajax（目前用于检验验证码是否正确）
	sendRequest_2 : function(options) {

		layui.use('layer', function() {
			let layer = layui.layer;
			layer.msg('加载中...', {
				time : 1000
			});
		});

		let self = this;
		return $.ajax({
			"type" : options.type,
			"async" : options.async ? options.async : false,
			"url" : this.BASE_URL + options.url,
			"data" : options.data,
			"xhrFields" : { // 跨域
				withCredentials : true
			},
			"complete" : function(xhr) { // 请求完成时调用的方法
				if (xhr.readyState == 4 && xhr.status == 200) { // 请求成功时
					if (options.success) {
						options.success.call(this, xhr.responseJSON, xhr);
					}
				} else { // 请求失败时
					options.error.call();
				}
			}
		});
	}
};