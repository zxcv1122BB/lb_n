/*!
 * UEditor Mini�汾
 * version: 1.2.2
 * build: Thu Dec 22 2016 16:36:17 GMT+0800 (CST)
 */

(function($){

UMEDITOR_CONFIG = window.UMEDITOR_CONFIG || {};

window.UM = {
    plugins : {},

    commands : {},

    I18N : {},

    version : "1.2.2"
};

var dom = UM.dom = {};
/**
 * ������ж�ģ��
 * @file
 * @module UE.browser
 * @since 1.2.6.1
 */

/**
 * �ṩ���������ģ��
 * @unfile
 * @module UE.browser
 */
var browser = UM.browser = function(){
    var agent = navigator.userAgent.toLowerCase(),
        opera = window.opera,
        browser = {
            /**
             * @property {boolean} ie ��⵱ǰ������Ƿ�ΪIE
             * @example
             * ```javascript
             * if ( UE.browser.ie ) {
         *     console.log( '��ǰ�������IE' );
         * }
             * ```
             */
            ie		:  /(msie\s|trident.*rv:)([\w.]+)/.test(agent),

            /**
             * @property {boolean} opera ��⵱ǰ������Ƿ�ΪOpera
             * @example
             * ```javascript
             * if ( UE.browser.opera ) {
         *     console.log( '��ǰ�������Opera' );
         * }
             * ```
             */
            opera	: ( !!opera && opera.version ),

            /**
             * @property {boolean} webkit ��⵱ǰ������Ƿ���webkit�ں˵������
             * @example
             * ```javascript
             * if ( UE.browser.webkit ) {
         *     console.log( '��ǰ�������webkit�ں������' );
         * }
             * ```
             */
            webkit	: ( agent.indexOf( ' applewebkit/' ) > -1 ),

            /**
             * @property {boolean} mac ��⵱ǰ������Ƿ���������macƽ̨��
             * @example
             * ```javascript
             * if ( UE.browser.mac ) {
         *     console.log( '��ǰ�����������macƽ̨��' );
         * }
             * ```
             */
            mac	: ( agent.indexOf( 'macintosh' ) > -1 ),

            /**
             * @property {boolean} quirks ��⵱ǰ������Ƿ��ڡ�����ģʽ����
             * @example
             * ```javascript
             * if ( UE.browser.quirks ) {
         *     console.log( '��ǰ��������д��ڡ�����ģʽ��' );
         * }
             * ```
             */
            quirks : ( document.compatMode == 'BackCompat' )
        };

    /**
     * @property {boolean} gecko ��⵱ǰ������ں��Ƿ���gecko�ں�
     * @example
     * ```javascript
     * if ( UE.browser.gecko ) {
    *     console.log( '��ǰ������ں���gecko�ں�' );
    * }
     * ```
     */
    browser.gecko =( navigator.product == 'Gecko' && !browser.webkit && !browser.opera && !browser.ie);

    var version = 0;

    // Internet Explorer 6.0+
    if ( browser.ie ){


        var v1 =  agent.match(/(?:msie\s([\w.]+))/);
        var v2 = agent.match(/(?:trident.*rv:([\w.]+))/);
        if(v1 && v2 && v1[1] && v2[1]){
            version = Math.max(v1[1]*1,v2[1]*1);
        }else if(v1 && v1[1]){
            version = v1[1]*1;
        }else if(v2 && v2[1]){
            version = v2[1]*1;
        }else{
            version = 0;
        }

        browser.ie11Compat = document.documentMode == 11;
        /**
         * @property { boolean } ie9Compat ��������ģʽ�Ƿ�Ϊ IE9 ����ģʽ
         * @warning ������������IE�� ���ֵΪundefined
         * @example
         * ```javascript
         * if ( UE.browser.ie9Compat ) {
         *     console.log( '��ǰ�����������IE9����ģʽ��' );
         * }
         * ```
         */
        browser.ie9Compat = document.documentMode == 9;

        /**
         * @property { boolean } ie8 ���������Ƿ���IE8�����
         * @warning ������������IE�� ���ֵΪundefined
         * @example
         * ```javascript
         * if ( UE.browser.ie8 ) {
         *     console.log( '��ǰ�������IE8�����' );
         * }
         * ```
         */
        browser.ie8 = !!document.documentMode;

        /**
         * @property { boolean } ie8Compat ��������ģʽ�Ƿ�Ϊ IE8 ����ģʽ
         * @warning ������������IE�� ���ֵΪundefined
         * @example
         * ```javascript
         * if ( UE.browser.ie8Compat ) {
         *     console.log( '��ǰ�����������IE8����ģʽ��' );
         * }
         * ```
         */
        browser.ie8Compat = document.documentMode == 8;

        /**
         * @property { boolean } ie7Compat ��������ģʽ�Ƿ�Ϊ IE7 ����ģʽ
         * @warning ������������IE�� ���ֵΪundefined
         * @example
         * ```javascript
         * if ( UE.browser.ie7Compat ) {
         *     console.log( '��ǰ�����������IE7����ģʽ��' );
         * }
         * ```
         */
        browser.ie7Compat = ( ( version == 7 && !document.documentMode )
            || document.documentMode == 7 );

        /**
         * @property { boolean } ie6Compat ��������ģʽ�Ƿ�Ϊ IE6 ģʽ ���߹���ģʽ
         * @warning ������������IE�� ���ֵΪundefined
         * @example
         * ```javascript
         * if ( UE.browser.ie6Compat ) {
         *     console.log( '��ǰ�����������IE6ģʽ���߹���ģʽ��' );
         * }
         * ```
         */
        browser.ie6Compat = ( version < 7 || browser.quirks );

        browser.ie9above = version > 8;

        browser.ie9below = version < 9;

    }

    // Gecko.
    if ( browser.gecko ){
        var geckoRelease = agent.match( /rv:([\d\.]+)/ );
        if ( geckoRelease )
        {
            geckoRelease = geckoRelease[1].split( '.' );
            version = geckoRelease[0] * 10000 + ( geckoRelease[1] || 0 ) * 100 + ( geckoRelease[2] || 0 ) * 1;
        }
    }

    /**
     * @property { Number } chrome ��⵱ǰ������Ƿ�ΪChrome, ����ǣ��򷵻�Chrome�Ĵ�汾��
     * @warning ������������chrome�� ���ֵΪundefined
     * @example
     * ```javascript
     * if ( UE.browser.chrome ) {
     *     console.log( '��ǰ�������Chrome' );
     * }
     * ```
     */
    if (/chrome\/(\d+\.\d)/i.test(agent)) {
        browser.chrome = + RegExp['\x241'];
    }

    /**
     * @property { Number } safari ��⵱ǰ������Ƿ�ΪSafari, ����ǣ��򷵻�Safari�Ĵ�汾��
     * @warning ������������safari�� ���ֵΪundefined
     * @example
     * ```javascript
     * if ( UE.browser.safari ) {
     *     console.log( '��ǰ�������Safari' );
     * }
     * ```
     */
    if(/(\d+\.\d)?(?:\.\d)?\s+safari\/?(\d+\.\d+)?/i.test(agent) && !/chrome/i.test(agent)){
        browser.safari = + (RegExp['\x241'] || RegExp['\x242']);
    }


    // Opera 9.50+
    if ( browser.opera )
        version = parseFloat( opera.version() );

    // WebKit 522+ (Safari 3+)
    if ( browser.webkit )
        version = parseFloat( agent.match( / applewebkit\/(\d+)/ )[1] );

    /**
     * @property { Number } version ��⵱ǰ������汾��
     * @remind
     * <ul>
     *     <li>IEϵ�з���ֵΪ5,6,7,8,9,10��</li>
     *     <li>geckoϵ�л᷵��10900��158900��</li>
     *     <li>webkitϵ�л᷵����build�� (�� 522��)</li>
     * </ul>
     * @example
     * ```javascript
     * console.log( '��ǰ������汾���ǣ� ' + UE.browser.version );
     * ```
     */
    browser.version = version;

    /**
     * @property { boolean } isCompatible ��⵱ǰ������Ƿ��ܹ���UEditor���ü���
     * @example
     * ```javascript
     * if ( UE.browser.isCompatible ) {
     *     console.log( '�������UEditor�ܹ����ü���' );
     * }
     * ```
     */
    browser.isCompatible =
        !browser.mobile && (
            ( browser.ie && version >= 6 ) ||
                ( browser.gecko && version >= 10801 ) ||
                ( browser.opera && version >= 9.5 ) ||
                ( browser.air && version >= 1 ) ||
                ( browser.webkit && version >= 522 ) ||
                false );
    return browser;
}();
//��ݷ�ʽ
var ie = browser.ie,
    webkit = browser.webkit,
    gecko = browser.gecko,
    opera = browser.opera;
/**
 * @file
 * @name UM.Utils
 * @short Utils
 * @desc UEditor��װʹ�õľ�̬���ߺ���
 * @import editor.js
 */
var utils = UM.utils = {
    /**
     * �������飬����nodeList
     * @name each
     * @grammar UM.utils.each(obj,iterator,[context])
     * @since 1.2.4+
     * @desc
     * * obj Ҫ����Ķ���
     * * iterator ����ķ���,�����ĵ�һ���Ǳ����ֵ���ڶ���������������obj
     * * context  iterator��������
     * @example
     * UM.utils.each([1,2],function(v,i){
     *     console.log(v)//ֵ
     *     console.log(i)//����
     * })
     * UM.utils.each(document.getElementsByTagName('*'),function(n){
     *     console.log(n.tagName)
     * })
     */
    each : function(obj, iterator, context) {
        if (obj == null) return;
        if (obj.length === +obj.length) {
            for (var i = 0, l = obj.length; i < l; i++) {
                if(iterator.call(context, obj[i], i, obj) === false)
                    return false;
            }
        } else {
            for (var key in obj) {
                if (obj.hasOwnProperty(key)) {
                    if(iterator.call(context, obj[key], key, obj) === false)
                        return false;
                }
            }
        }
    },

    makeInstance:function (obj) {
        var noop = new Function();
        noop.prototype = obj;
        obj = new noop;
        noop.prototype = null;
        return obj;
    },
    /**
     * ��source�����е�������չ��target������
     * @name extend
     * @grammar UM.utils.extend(target,source)  => Object  //������չ
     * @grammar UM.utils.extend(target,source,true)  ==> Object  //������չ
     */
    extend:function (t, s, b) {
        if (s) {
            for (var k in s) {
                if (!b || !t.hasOwnProperty(k)) {
                    t[k] = s[k];
                }
            }
        }
        return t;
    },
    extend2:function (t) {
        var a = arguments;
        for (var i = 1; i < a.length; i++) {
            var x = a[i];
            for (var k in x) {
                if (!t.hasOwnProperty(k)) {
                    t[k] = x[k];
                }
            }
        }
        return t;
    },
    /**
     * ģ��̳л��ƣ�subClass�̳�superClass
     * @name inherits
     * @grammar UM.utils.inherits(subClass,superClass) => subClass
     * @example
     * function SuperClass(){
     *     this.name = "С��";
     * }
     * SuperClass.prototype = {
     *     hello:function(str){
     *         console.log(this.name + str);
     *     }
     * }
     * function SubClass(){
     *     this.name = "С��";
     * }
     * UM.utils.inherits(SubClass,SuperClass);
     * var sub = new SubClass();
     * sub.hello("���Ϻ�!"); ==> "С�����Ϻã�"
     */
    inherits:function (subClass, superClass) {
        var oldP = subClass.prototype,
            newP = utils.makeInstance(superClass.prototype);
        utils.extend(newP, oldP, true);
        subClass.prototype = newP;
        return (newP.constructor = subClass);
    },

    /**
     * ��ָ����context��Ϊfn�����ģ�Ҳ����this
     * @name bind
     * @grammar UM.utils.bind(fn,context)  =>  fn
     */
    bind:function (fn, context) {
        return function () {
            return fn.apply(context, arguments);
        };
    },

    /**
     * �����ӳ�delayִ�еĺ���fn
     * @name defer
     * @grammar UM.utils.defer(fn,delay)  =>fn   //�ӳ�delay����ִ��fn������fn
     * @grammar UM.utils.defer(fn,delay,exclusion)  =>fn   //�ӳ�delay����ִ��fn����exclusionΪ�棬�򻥳�ִ��fn
     * @example
     * function test(){
     *     console.log("�ӳ������");
     * }
     * //�ǻ����ӳ�ִ��
     * var testDefer = UM.utils.defer(test,1000);
     * testDefer();   =>  "�ӳ������";
     * testDefer();   =>  "�ӳ������";
     * //�����ӳ�ִ��
     * var testDefer1 = UM.utils.defer(test,1000,true);
     * testDefer1();   =>  //���β�ִ��
     * testDefer1();   =>  "�ӳ������";
     */
    defer:function (fn, delay, exclusion) {
        var timerID;
        return function () {
            if (exclusion) {
                clearTimeout(timerID);
            }
            timerID = setTimeout(fn, delay);
        };
    },

    /**
     * ����Ԫ��item������array�е�����, ���Ҳ�������-1
     * @name indexOf
     * @grammar UM.utils.indexOf(array,item)  => index|-1  //Ĭ�ϴ����鿪ͷ����ʼ����
     * @grammar UM.utils.indexOf(array,item,start)  => index|-1  //startָ����ʼ���ҵ�λ��
     */
    indexOf:function (array, item, start) {
        var index = -1;
        start = this.isNumber(start) ? start : 0;
        this.each(array, function (v, i) {
            if (i >= start && v === item) {
                index = i;
                return false;
            }
        });
        return index;
    },

    /**
     * �Ƴ�����array�е�Ԫ��item
     * @name removeItem
     * @grammar UM.utils.removeItem(array,item)
     */
    removeItem:function (array, item) {
        for (var i = 0, l = array.length; i < l; i++) {
            if (array[i] === item) {
                array.splice(i, 1);
                i--;
            }
        }
    },

    /**
     * ɾ���ַ�str����β�ո�
     * @name trim
     * @grammar UM.utils.trim(str) => String
     */
    trim:function (str) {
        return str.replace(/(^[ \t\n\r]+)|([ \t\n\r]+$)/g, '');
    },

    /**
     * ���ַ�list(��','�ָ�)��������listת�ɹ�ϣ����
     * @name listToMap
     * @grammar UM.utils.listToMap(list)  => Object  //Object����{test:1,br:1,textarea:1}
     */
    listToMap:function (list) {
        if (!list)return {};
        list = utils.isArray(list) ? list : list.split(',');
        for (var i = 0, ci, obj = {}; ci = list[i++];) {
            obj[ci.toUpperCase()] = obj[ci] = 1;
        }
        return obj;
    },

    /**
     * ��str�е�html���ת��,Ĭ�Ͻ�ת��''&<">''�ĸ��ַ���Զ���reg��ȷ����Ҫת����ַ�
     * @name unhtml
     * @grammar UM.utils.unhtml(str);  => String
     * @grammar UM.utils.unhtml(str,reg)  => String
     * @example
     * var html = '<body>You say:"��ã�Baidu & UEditor!"</body>';
     * UM.utils.unhtml(html);   ==>  &lt;body&gt;You say:&quot;��ã�Baidu &amp; UEditor!&quot;&lt;/body&gt;
     * UM.utils.unhtml(html,/[<>]/g)  ==>  &lt;body&gt;You say:"��ã�Baidu & UEditor!"&lt;/body&gt;
     */
    unhtml:function (str, reg) {
        return str ? str.replace(reg || /[&<">'](?:(amp|lt|quot|gt|#39|nbsp);)?/g, function (a, b) {
            if (b) {
                return a;
            } else {
                return {
                    '<':'&lt;',
                    '&':'&amp;',
                    '"':'&quot;',
                    '>':'&gt;',
                    "'":'&#39;'
                }[a]
            }

        }) : '';
    },
    /**
     * ��str�е�ת���ַ�ԭ��html�ַ�
     * @name html
     * @grammar UM.utils.html(str)  => String   //��ϸ�μ�<code><a href = '#unhtml'>unhtml</a></code>
     */
    html:function (str) {
        return str ? str.replace(/&((g|l|quo)t|amp|#39);/g, function (m) {
            return {
                '&lt;':'<',
                '&amp;':'&',
                '&quot;':'"',
                '&gt;':'>',
                '&#39;':"'"
            }[m]
        }) : '';
    },
    /**
     * ��css��ʽת��Ϊ�շ����ʽ����font-size => fontSize
     * @name cssStyleToDomStyle
     * @grammar UM.utils.cssStyleToDomStyle(cssName)  => String
     */
    cssStyleToDomStyle:function () {
        var test = document.createElement('div').style,
            cache = {
                'float':test.cssFloat != undefined ? 'cssFloat' : test.styleFloat != undefined ? 'styleFloat' : 'float'
            };

        return function (cssName) {
            return cache[cssName] || (cache[cssName] = cssName.toLowerCase().replace(/-./g, function (match) {
                return match.charAt(1).toUpperCase();
            }));
        };
    }(),
    /**
     * ��̬�����ļ���doc�У�������obj���������ԣ����سɹ���ִ�лص�����fn
     * @name loadFile
     * @grammar UM.utils.loadFile(doc,obj)
     * @grammar UM.utils.loadFile(doc,obj,fn)
     * @example
     * //ָ�����ص���ǰdocument��һ��script�ļ������سɹ���ִ��function
     * utils.loadFile( document, {
     *     src:"test.js",
     *     tag:"script",
     *     type:"text/javascript",
     *     defer:"defer"
     * }, function () {
     *     console.log('���سɹ���')
     * });
     */
    loadFile:function () {
        var tmpList = [];

        function getItem(doc, obj) {
            try {
                for (var i = 0, ci; ci = tmpList[i++];) {
                    if (ci.doc === doc && ci.url == (obj.src || obj.href)) {
                        return ci;
                    }
                }
            } catch (e) {
                return null;
            }

        }

        return function (doc, obj, fn) {
            var item = getItem(doc, obj);
            if (item) {
                if (item.ready) {
                    fn && fn();
                } else {
                    item.funs.push(fn)
                }
                return;
            }
            tmpList.push({
                doc:doc,
                url:obj.src || obj.href,
                funs:[fn]
            });
            if (!doc.body) {
                var html = [];
                for (var p in obj) {
                    if (p == 'tag')continue;
                    html.push(p + '="' + obj[p] + '"')
                }
                doc.write('<' + obj.tag + ' ' + html.join(' ') + ' ></' + obj.tag + '>');
                return;
            }
            if (obj.id && doc.getElementById(obj.id)) {
                return;
            }
            var element = doc.createElement(obj.tag);
            delete obj.tag;
            for (var p in obj) {
                element.setAttribute(p, obj[p]);
            }
            element.onload = element.onreadystatechange = function () {
                if (!this.readyState || /loaded|complete/.test(this.readyState)) {
                    item = getItem(doc, obj);
                    if (item.funs.length > 0) {
                        item.ready = 1;
                        for (var fi; fi = item.funs.pop();) {
                            fi();
                        }
                    }
                    element.onload = element.onreadystatechange = null;
                }
            };
            element.onerror = function () {
                throw Error('The load ' + (obj.href || obj.src) + ' fails,check the url settings of file umeditor.config.js ')
            };
            doc.getElementsByTagName("head")[0].appendChild(element);
        }
    }(),
    /**
     * �ж�obj�����Ƿ�Ϊ��
     * @name isEmptyObject
     * @grammar UM.utils.isEmptyObject(obj)  => true|false
     * @example
     * UM.utils.isEmptyObject({}) ==>true
     * UM.utils.isEmptyObject([]) ==>true
     * UM.utils.isEmptyObject("") ==>true
     */
    isEmptyObject:function (obj) {
        if (obj == null) return true;
        if (this.isArray(obj) || this.isString(obj)) return obj.length === 0;
        for (var key in obj) if (obj.hasOwnProperty(key)) return false;
        return true;
    },

    /**
     * ͳһ����ɫֵʹ��16������ʽ��ʾ
     * @name fixColor
     * @grammar UM.utils.fixColor(name,value) => value
     * @example
     * rgb(255,255,255)  => "#ffffff"
     */
    fixColor:function (name, value) {
        if (/color/i.test(name) && /rgba?/.test(value)) {
            var array = value.split(",");
            if (array.length > 3)
                return "";
            value = "#";
            for (var i = 0, color; color = array[i++];) {
                color = parseInt(color.replace(/[^\d]/gi, ''), 10).toString(16);
                value += color.length == 1 ? "0" + color : color;
            }
            value = value.toUpperCase();
        }
        return  value;
    },

    /**
     * ��ȿ�¡���󣬴�source��target
     * @name clone
     * @grammar UM.utils.clone(source) => anthorObj �µĶ����������source�ĸ���
     * @grammar UM.utils.clone(source,target) => target����source���������ݣ�����Ḳ��
     */
    clone:function (source, target) {
        var tmp;
        target = target || {};
        for (var i in source) {
            if (source.hasOwnProperty(i)) {
                tmp = source[i];
                if (typeof tmp == 'object') {
                    target[i] = utils.isArray(tmp) ? [] : {};
                    utils.clone(source[i], target[i])
                } else {
                    target[i] = tmp;
                }
            }
        }
        return target;
    },
    /**
     * ת��cm/pt��px
     * @name transUnitToPx
     * @grammar UM.utils.transUnitToPx('20pt') => '27px'
     * @grammar UM.utils.transUnitToPx('0pt') => '0'
     */
    transUnitToPx:function (val) {
        if (!/(pt|cm)/.test(val)) {
            return val
        }
        var unit;
        val.replace(/([\d.]+)(\w+)/, function (str, v, u) {
            val = v;
            unit = u;
        });
        switch (unit) {
            case 'cm':
                val = parseFloat(val) * 25;
                break;
            case 'pt':
                val = Math.round(parseFloat(val) * 96 / 72);
        }
        return val + (val ? 'px' : '');
    },
    /**
     * ��̬���css��ʽ
     * @name cssRule
     * @grammar UM.utils.cssRule('��ӵ���ʽ�Ľڵ����',['��ʽ'��'�ŵ��ĸ�document��'])
     * @grammar UM.utils.cssRule('body','body{background:#ccc}') => null  //��body��ӱ�����ɫ
     * @grammar UM.utils.cssRule('body') =>��ʽ���ַ�  //ȡ��keyֵΪbody����ʽ������,���û���ҵ�keyֵ�ȹص���ʽ�����ؿգ�����ղ��Ǹ�������ɫ�������� body{background:#ccc}
     * @grammar UM.utils.cssRule('body','') =>null //��ո��keyֵ�ı�����ɫ
     */
    cssRule:browser.ie && browser.version != 11 ? function (key, style, doc) {
        var indexList, index;
        doc = doc || document;
        if (doc.indexList) {
            indexList = doc.indexList;
        } else {
            indexList = doc.indexList = {};
        }
        var sheetStyle;
        if (!indexList[key]) {
            if (style === undefined) {
                return ''
            }
            sheetStyle = doc.createStyleSheet('', index = doc.styleSheets.length);
            indexList[key] = index;
        } else {
            sheetStyle = doc.styleSheets[indexList[key]];
        }
        if (style === undefined) {
            return sheetStyle.cssText
        }
        sheetStyle.cssText = style || ''
    } : function (key, style, doc) {
        doc = doc || document;
        var head = doc.getElementsByTagName('head')[0], node;
        if (!(node = doc.getElementById(key))) {
            if (style === undefined) {
                return ''
            }
            node = doc.createElement('style');
            node.id = key;
            head.appendChild(node)
        }
        if (style === undefined) {
            return node.innerHTML
        }
        if (style !== '') {
            node.innerHTML = style;
        } else {
            head.removeChild(node)
        }
    }

};
/**
 * �ж�str�Ƿ�Ϊ�ַ�
 * @name isString
 * @grammar UM.utils.isString(str) => true|false
 */
/**
 * �ж�array�Ƿ�Ϊ����
 * @name isArray
 * @grammar UM.utils.isArray(obj) => true|false
 */
/**
 * �ж�obj�����Ƿ�Ϊ����
 * @name isFunction
 * @grammar UM.utils.isFunction(obj)  => true|false
 */
/**
 * �ж�obj�����Ƿ�Ϊ����
 * @name isNumber
 * @grammar UM.utils.isNumber(obj)  => true|false
 */
utils.each(['String', 'Function', 'Array', 'Number', 'RegExp', 'Object'], function (v) {
    UM.utils['is' + v] = function (obj) {
        return Object.prototype.toString.apply(obj) == '[object ' + v + ']';
    }
});
/**
 * @file
 * @name UM.EventBase
 * @short EventBase
 * @import editor.js,core/utils.js
 * @desc UE���õ��¼����࣬�̳д���Ķ�Ӧ�ཫ��ȡaddListener,removeListener,fireEvent������
 * ��UE�У�Editor�Լ�����uiʵ��̳��˸��࣬�ʿ����ڶ�Ӧ��ui�����Լ�editor������ʹ������������
 */
var EventBase = UM.EventBase = function () {};

EventBase.prototype = {
    /**
     * ע���¼�������
     * @name addListener
     * @grammar editor.addListener(types,fn)  //typesΪ�¼���ƣ�������ÿո�ָ�
     * @example
     * editor.addListener('selectionchange',function(){
     *      console.log("ѡ���Ѿ��仯��");
     * })
     * editor.addListener('beforegetcontent aftergetcontent',function(type){
     *         if(type == 'beforegetcontent'){
     *             //do something
     *         }else{
     *             //do something
     *         }
     *         console.log(this.getContent) // this��ע����¼��ı༭��ʵ��
     * })
     */
    addListener:function (types, listener) {
        types = utils.trim(types).split(' ');
        for (var i = 0, ti; ti = types[i++];) {
            getListener(this, ti, true).push(listener);
        }
    },
    /**
     * �Ƴ��¼�������
     * @name removeListener
     * @grammar editor.removeListener(types,fn)  //typesΪ�¼���ƣ�������ÿո�ָ�
     * @example
     * //changeCallbackΪ������
     * editor.removeListener("selectionchange",changeCallback);
     */
    removeListener:function (types, listener) {
        types = utils.trim(types).split(' ');
        for (var i = 0, ti; ti = types[i++];) {
            utils.removeItem(getListener(this, ti) || [], listener);
        }
    },
    /**
     * �����¼�
     * @name fireEvent
     * @grammar editor.fireEvent(types)  //typesΪ�¼���ƣ�������ÿո�ָ�
     * @example
     * editor.fireEvent("selectionchange");
     */
    fireEvent:function () {
        var types = arguments[0];
        types = utils.trim(types).split(' ');
        for (var i = 0, ti; ti = types[i++];) {
            var listeners = getListener(this, ti),
                r, t, k;
            if (listeners) {
                k = listeners.length;
                while (k--) {
                    if(!listeners[k])continue;
                    t = listeners[k].apply(this, arguments);
                    if(t === true){
                        return t;
                    }
                    if (t !== undefined) {
                        r = t;
                    }
                }
            }
            if (t = this['on' + ti.toLowerCase()]) {
                r = t.apply(this, arguments);
            }
        }
        return r;
    }
};
/**
 * ��ö�����ӵ�м������͵����м�����
 * @public
 * @function
 * @param {Object} obj  ��ѯ�������Ķ���
 * @param {String} type �¼�����
 * @param {Boolean} force  Ϊtrue�ҵ�ǰ����type���͵�������������ʱ������һ���ռ���������
 * @returns {Array} ����������
 */
function getListener(obj, type, force) {
    var allListeners;
    type = type.toLowerCase();
    return ( ( allListeners = ( obj.__allListeners || force && ( obj.__allListeners = {} ) ) )
        && ( allListeners[type] || force && ( allListeners[type] = [] ) ) );
}


///import editor.js
///import core/dom/dom.js
///import core/utils.js
/**
 * dtd html���廯��������
 * @constructor
 * @namespace dtd
 */
var dtd = dom.dtd = (function() {
    function _( s ) {
        for (var k in s) {
            s[k.toUpperCase()] = s[k];
        }
        return s;
    }
    var X = utils.extend2;
    var A = _({isindex:1,fieldset:1}),
        B = _({input:1,button:1,select:1,textarea:1,label:1}),
        C = X( _({a:1}), B ),
        D = X( {iframe:1}, C ),
        E = _({hr:1,ul:1,menu:1,div:1,blockquote:1,noscript:1,table:1,center:1,address:1,dir:1,pre:1,h5:1,dl:1,h4:1,noframes:1,h6:1,ol:1,h1:1,h3:1,h2:1}),
        F = _({ins:1,del:1,script:1,style:1}),
        G = X( _({b:1,acronym:1,bdo:1,'var':1,'#':1,abbr:1,code:1,br:1,i:1,cite:1,kbd:1,u:1,strike:1,s:1,tt:1,strong:1,q:1,samp:1,em:1,dfn:1,span:1}), F ),
        H = X( _({sub:1,img:1,embed:1,object:1,sup:1,basefont:1,map:1,applet:1,font:1,big:1,small:1}), G ),
        I = X( _({p:1}), H ),
        J = X( _({iframe:1}), H, B ),
        K = _({img:1,embed:1,noscript:1,br:1,kbd:1,center:1,button:1,basefont:1,h5:1,h4:1,samp:1,h6:1,ol:1,h1:1,h3:1,h2:1,form:1,font:1,'#':1,select:1,menu:1,ins:1,abbr:1,label:1,code:1,table:1,script:1,cite:1,input:1,iframe:1,strong:1,textarea:1,noframes:1,big:1,small:1,span:1,hr:1,sub:1,bdo:1,'var':1,div:1,object:1,sup:1,strike:1,dir:1,map:1,dl:1,applet:1,del:1,isindex:1,fieldset:1,ul:1,b:1,acronym:1,a:1,blockquote:1,i:1,u:1,s:1,tt:1,address:1,q:1,pre:1,p:1,em:1,dfn:1}),

        L = X( _({a:0}), J ),//a���ܱ��п������԰���
        M = _({tr:1}),
        N = _({'#':1}),
        O = X( _({param:1}), K ),
        P = X( _({form:1}), A, D, E, I ),
        Q = _({li:1,ol:1,ul:1}),
        R = _({style:1,script:1}),
        S = _({base:1,link:1,meta:1,title:1}),
        T = X( S, R ),
        U = _({head:1,body:1}),
        V = _({html:1});

    var block = _({address:1,blockquote:1,center:1,dir:1,div:1,dl:1,fieldset:1,form:1,h1:1,h2:1,h3:1,h4:1,h5:1,h6:1,hr:1,isindex:1,menu:1,noframes:1,ol:1,p:1,pre:1,table:1,ul:1}),

        empty =  _({area:1,base:1,basefont:1,br:1,col:1,command:1,dialog:1,embed:1,hr:1,img:1,input:1,isindex:1,keygen:1,link:1,meta:1,param:1,source:1,track:1,wbr:1});

    return  _({

        // $ ��ʾ�Զ�������

        // body���Ԫ���б�.
        $nonBodyContent: X( V, U, S ),

        //��ṹԪ���б�
        $block : block,

        //����Ԫ���б�
        $inline : L,

        $inlineWithA : X(_({a:1}),L),

        $body : X( _({script:1,style:1}), block ),

        $cdata : _({script:1,style:1}),

        //�Ապ�Ԫ��
        $empty : empty,

        //�����Ապϣ���������rangeѡ�����
        $nonChild : _({iframe:1,textarea:1}),
        //�б�Ԫ���б�
        $listItem : _({dd:1,dt:1,li:1}),

        //�б��Ԫ���б�
        $list: _({ul:1,ol:1,dl:1}),

        //������Ϊ�ǿյ�Ԫ��
        $isNotEmpty : _({table:1,ul:1,ol:1,dl:1,iframe:1,area:1,base:1,col:1,hr:1,img:1,embed:1,input:1,link:1,meta:1,param:1,h1:1,h2:1,h3:1,h4:1,h5:1,h6:1}),

        //���û���ӽڵ�Ϳ���ɾ���Ԫ���б?��span,a
        $removeEmpty : _({a:1,abbr:1,acronym:1,address:1,b:1,bdo:1,big:1,cite:1,code:1,del:1,dfn:1,em:1,font:1,i:1,ins:1,label:1,kbd:1,q:1,s:1,samp:1,small:1,span:1,strike:1,strong:1,sub:1,sup:1,tt:1,u:1,'var':1}),

        $removeEmptyBlock : _({'p':1,'div':1}),

        //��tableԪ�����Ԫ���б�
        $tableContent : _({caption:1,col:1,colgroup:1,tbody:1,td:1,tfoot:1,th:1,thead:1,tr:1,table:1}),
        //��ת���ı�ǩ
        $notTransContent : _({pre:1,script:1,style:1,textarea:1}),
        html: U,
        head: T,
        style: N,
        script: N,
        body: P,
        base: {},
        link: {},
        meta: {},
        title: N,
        col : {},
        tr : _({td:1,th:1}),
        img : {},
        embed: {},
        colgroup : _({thead:1,col:1,tbody:1,tr:1,tfoot:1}),
        noscript : P,
        td : P,
        br : {},
        th : P,
        center : P,
        kbd : L,
        button : X( I, E ),
        basefont : {},
        h5 : L,
        h4 : L,
        samp : L,
        h6 : L,
        ol : Q,
        h1 : L,
        h3 : L,
        option : N,
        h2 : L,
        form : X( A, D, E, I ),
        select : _({optgroup:1,option:1}),
        font : L,
        ins : L,
        menu : Q,
        abbr : L,
        label : L,
        table : _({thead:1,col:1,tbody:1,tr:1,colgroup:1,caption:1,tfoot:1}),
        code : L,
        tfoot : M,
        cite : L,
        li : P,
        input : {},
        iframe : P,
        strong : L,
        textarea : N,
        noframes : P,
        big : L,
        small : L,
        //trace:
        span :_({'#':1,br:1,b:1,strong:1,u:1,i:1,em:1,sub:1,sup:1,strike:1,span:1}),
        hr : L,
        dt : L,
        sub : L,
        optgroup : _({option:1}),
        param : {},
        bdo : L,
        'var' : L,
        div : P,
        object : O,
        sup : L,
        dd : P,
        strike : L,
        area : {},
        dir : Q,
        map : X( _({area:1,form:1,p:1}), A, F, E ),
        applet : O,
        dl : _({dt:1,dd:1}),
        del : L,
        isindex : {},
        fieldset : X( _({legend:1}), K ),
        thead : M,
        ul : Q,
        acronym : L,
        b : L,
        a : X( _({a:1}), J ),
        blockquote :X(_({td:1,tr:1,tbody:1,li:1}),P),
        caption : L,
        i : L,
        u : L,
        tbody : M,
        s : L,
        address : X( D, I ),
        tt : L,
        legend : L,
        q : L,
        pre : X( G, C ),
        p : X(_({'a':1}),L),
        em :L,
        dfn : L
    });
})();

/**
 * @file
 * @name UM.dom.domUtils
 * @short DomUtils
 * @import editor.js, core/utils.js,core/browser.js,core/dom/dtd.js
 * @desc UEditor��װ�ĵײ�dom������
 */

function getDomNode(node, start, ltr, startFromChild, fn, guard) {
    var tmpNode = startFromChild && node[start],
        parent;
    !tmpNode && (tmpNode = node[ltr]);
    while (!tmpNode && (parent = (parent || node).parentNode)) {
        if (parent.tagName == 'BODY' || guard && !guard(parent)) {
            return null;
        }
        tmpNode = parent[ltr];
    }
    if (tmpNode && fn && !fn(tmpNode)) {
        return  getDomNode(tmpNode, start, ltr, false, fn);
    }
    return tmpNode;
}
var attrFix = ie && browser.version < 9 ? {
        tabindex: "tabIndex",
        readonly: "readOnly",
        "for": "htmlFor",
        "class": "className",
        maxlength: "maxLength",
        cellspacing: "cellSpacing",
        cellpadding: "cellPadding",
        rowspan: "rowSpan",
        colspan: "colSpan",
        usemap: "useMap",
        frameborder: "frameBorder"
    } : {
        tabindex: "tabIndex",
        readonly: "readOnly"
    },
    styleBlock = utils.listToMap([
        '-webkit-box', '-moz-box', 'block' ,
        'list-item' , 'table' , 'table-row-group' ,
        'table-header-group', 'table-footer-group' ,
        'table-row' , 'table-column-group' , 'table-column' ,
        'table-cell' , 'table-caption'
    ]);
var domUtils = dom.domUtils = {
    //�ڵ㳣��
    NODE_ELEMENT: 1,
    NODE_DOCUMENT: 9,
    NODE_TEXT: 3,
    NODE_COMMENT: 8,
    NODE_DOCUMENT_FRAGMENT: 11,

    //λ�ù�ϵ
    POSITION_IDENTICAL: 0,
    POSITION_DISCONNECTED: 1,
    POSITION_FOLLOWING: 2,
    POSITION_PRECEDING: 4,
    POSITION_IS_CONTAINED: 8,
    POSITION_CONTAINS: 16,
    //ie6ʹ������Ļ���һ�οհ׳���
    fillChar: ie && browser.version == '6' ? '\ufeff' : '\u200B',
    //-------------------------Node����--------------------------------
    keys: {
        /*Backspace*/ 8: 1, /*Delete*/ 46: 1,
        /*Shift*/ 16: 1, /*Ctrl*/ 17: 1, /*Alt*/ 18: 1,
        37: 1, 38: 1, 39: 1, 40: 1,
        13: 1 /*enter*/
    },
    breakParent:function (node, parent) {
        var tmpNode,
            parentClone = node,
            clone = node,
            leftNodes,
            rightNodes;
        do {
            parentClone = parentClone.parentNode;
            if (leftNodes) {
                tmpNode = parentClone.cloneNode(false);
                tmpNode.appendChild(leftNodes);
                leftNodes = tmpNode;
                tmpNode = parentClone.cloneNode(false);
                tmpNode.appendChild(rightNodes);
                rightNodes = tmpNode;
            } else {
                leftNodes = parentClone.cloneNode(false);
                rightNodes = leftNodes.cloneNode(false);
            }
            while (tmpNode = clone.previousSibling) {
                leftNodes.insertBefore(tmpNode, leftNodes.firstChild);
            }
            while (tmpNode = clone.nextSibling) {
                rightNodes.appendChild(tmpNode);
            }
            clone = parentClone;
        } while (parent !== parentClone);
        tmpNode = parent.parentNode;
        tmpNode.insertBefore(leftNodes, parent);
        tmpNode.insertBefore(rightNodes, parent);
        tmpNode.insertBefore(node, rightNodes);
        domUtils.remove(parent);
        return node;
    },
    trimWhiteTextNode:function (node) {
        function remove(dir) {
            var child;
            while ((child = node[dir]) && child.nodeType == 3 && domUtils.isWhitespace(child)) {
                node.removeChild(child);
            }
        }
        remove('firstChild');
        remove('lastChild');
    },
    /**
     * ��ȡ�ڵ�A����ڽڵ�B��λ�ù�ϵ
     * @name getPosition
     * @grammar UM.dom.domUtils.getPosition(nodeA,nodeB)  =>  Number
     * @example
     *  switch (returnValue) {
     *      case 0: //��ȣ�ͬһ�ڵ�
     *      case 1: //�޹أ��ڵ㲻����
     *      case 2: //���棬���ڵ�Aͷ��λ�ڽڵ�Bͷ���ĺ���
     *      case 4: //ǰ�ã����ڵ�Aͷ��λ�ڽڵ�Bͷ����ǰ��
     *      case 8: //������ڵ�A���ڵ�B��
     *      case 10://������ͣ����ڵ�A�������ڵ�B�ұ��ڵ�B��ʵ���ϣ�����ض����棬����returnValue��ʵ�ϲ������8�������
     *      case 16://����ڵ�A��ڵ�B
     *      case 20://������ͣ����ڵ�A����ǰ�ýڵ�A�Ұ�ڵ�B��ͬ������ض�ǰ�ã�����returnValue��ʵ��Ҳ�������16�����
     *  }
     */
    getPosition: function (nodeA, nodeB) {
        // ��������ڵ���ͬһ���ڵ�
        if (nodeA === nodeB) {
            // domUtils.POSITION_IDENTICAL
            return 0;
        }
        var node,
            parentsA = [nodeA],
            parentsB = [nodeB];
        node = nodeA;
        while (node = node.parentNode) {
            // ���nodeB��nodeA�����Ƚڵ�
            if (node === nodeB) {
                // domUtils.POSITION_IS_CONTAINED + domUtils.POSITION_FOLLOWING
                return 10;
            }
            parentsA.push(node);
        }
        node = nodeB;
        while (node = node.parentNode) {
            // ���nodeA��nodeB�����Ƚڵ�
            if (node === nodeA) {
                // domUtils.POSITION_CONTAINS + domUtils.POSITION_PRECEDING
                return 20;
            }
            parentsB.push(node);
        }
        parentsA.reverse();
        parentsB.reverse();
        if (parentsA[0] !== parentsB[0]) {
            // domUtils.POSITION_DISCONNECTED
            return 1;
        }
        var i = -1;
        while (i++, parentsA[i] === parentsB[i]) {
        }
        nodeA = parentsA[i];
        nodeB = parentsB[i];
        while (nodeA = nodeA.nextSibling) {
            if (nodeA === nodeB) {
                // domUtils.POSITION_PRECEDING
                return 4
            }
        }
        // domUtils.POSITION_FOLLOWING
        return  2;
    },

    /**
     * ���ؽڵ�node�ڸ��ڵ��е�����λ��
     * @name getNodeIndex
     * @grammar UM.dom.domUtils.getNodeIndex(node)  => Number  //����ֵ��0��ʼ
     */
    getNodeIndex: function (node, ignoreTextNode) {
        var preNode = node,
            i = 0;
        while (preNode = preNode.previousSibling) {
            if (ignoreTextNode && preNode.nodeType == 3) {
                if (preNode.nodeType != preNode.nextSibling.nodeType) {
                    i++;
                }
                continue;
            }
            i++;
        }
        return i;
    },

    /**
     * ���ڵ�node�Ƿ��ڽڵ�doc�����ϣ�ʵ�����Ǽ���Ƿ�doc��
     * @name inDoc
     * @grammar UM.dom.domUtils.inDoc(node,doc)   =>  true|false
     */
    inDoc: function (node, doc) {
        return domUtils.getPosition(node, doc) == 10;
    },
    /**
     * ����node�ڵ�����Ƚڵ�
     * @name findParent
     * @grammar UM.dom.domUtils.findParent(node)  => Element  // ֱ�ӷ���node�ڵ�ĸ��ڵ�
     * @grammar UM.dom.domUtils.findParent(node,filterFn)  => Element  //filterFnΪ���˺���node��Ϊ�����trueʱ�ŻὫnode��Ϊ���Ҫ��Ľڵ㷵��
     * @grammar UM.dom.domUtils.findParent(node,filterFn,includeSelf)  => Element  //includeSelfָ���Ƿ������
     */
    findParent: function (node, filterFn, includeSelf) {
        if (node && !domUtils.isBody(node)) {
            node = includeSelf ? node : node.parentNode;
            while (node) {
                if (!filterFn || filterFn(node) || domUtils.isBody(node)) {
                    return filterFn && !filterFn(node) && domUtils.isBody(node) ? null : node;
                }
                node = node.parentNode;
            }
        }
        return null;
    },
    /**
     * ͨ��tagName����node�ڵ�����Ƚڵ�
     * @name findParentByTagName
     * @grammar UM.dom.domUtils.findParentByTagName(node,tagNames)   =>  Element  //tagNames֧�����飬��ִ�Сд
     * @grammar UM.dom.domUtils.findParentByTagName(node,tagNames,includeSelf)   =>  Element  //includeSelfָ���Ƿ������
     * @grammar UM.dom.domUtils.findParentByTagName(node,tagNames,includeSelf,excludeFn)   =>  Element  //excludeFnָ�������������������trueʱ���Ըýڵ�
     */
    findParentByTagName: function (node, tagNames, includeSelf, excludeFn) {
        tagNames = utils.listToMap(utils.isArray(tagNames) ? tagNames : [tagNames]);
        return domUtils.findParent(node, function (node) {
            return tagNames[node.tagName] && !(excludeFn && excludeFn(node));
        }, includeSelf);
    },
    /**
     * ���ҽڵ�node�����Ƚڵ㼯��
     * @name findParents
     * @grammar UM.dom.domUtils.findParents(node)  => Array  //����һ�����Ƚڵ����鼯�ϣ���������
     * @grammar UM.dom.domUtils.findParents(node,includeSelf)  => Array  //����һ�����Ƚڵ����鼯�ϣ�includeSelfָ���Ƿ������
     * @grammar UM.dom.domUtils.findParents(node,includeSelf,filterFn)  => Array  //����һ�����Ƚڵ����鼯�ϣ�filterFnָ����������������true��node����ѡȡ
     * @grammar UM.dom.domUtils.findParents(node,includeSelf,filterFn,closerFirst)  => Array  //����һ�����Ƚڵ����鼯�ϣ�closerFirstΪtrue�Ļ���node��ֱ�Ӹ��׽ڵ�������ĵ�0��
     */
    findParents: function (node, includeSelf, filterFn, closerFirst) {
        var parents = includeSelf && ( filterFn && filterFn(node) || !filterFn ) ? [node] : [];
        while (node = domUtils.findParent(node, filterFn)) {
            parents.push(node);
        }
        return closerFirst ? parents : parents.reverse();
    },

    /**
     * �ڽڵ�node��������½ڵ�newNode
     * @name insertAfter
     * @grammar UM.dom.domUtils.insertAfter(node,newNode)  => newNode
     */
    insertAfter: function (node, newNode) {
        return node.parentNode.insertBefore(newNode, node.nextSibling);
    },

    /**
     * ɾ��ڵ�node�������keepChildrenָ���Ƿ����ӽڵ�
     * @name remove
     * @grammar UM.dom.domUtils.remove(node)  =>  node
     * @grammar UM.dom.domUtils.remove(node,keepChildren)  =>  node
     */
    remove: function (node, keepChildren) {

        var parent = node.parentNode,
            child;
        if (parent) {
            if (keepChildren && node.hasChildNodes()) {
                while (child = node.firstChild) {
                    parent.insertBefore(child, node);
                }
            }
            parent.removeChild(node);
        }
        return node;
    },


    /**
     * ȡ��node�ڵ����һ���ֵܽڵ㣬 ���ýڵ����û���ֵܽڵ㣬 ��ݹ�����丸�ڵ�֮��ĵ�һ���ֵܽڵ㣬
     * ֱ���ҵ����������Ľڵ���ߵݹ鵽BODY�ڵ�֮��Ż����
     * @method getNextDomNode
     * @param { Node } node ��Ҫ��ȡ�����ֵܽڵ�Ľڵ����
     * @return { Node | NULL } ��������������Ľڵ㣬 �򷵻ظýڵ㣬 ���򷵻�NULL
     * @example
     * ```html
     *     <body>
     *      <div id="test">
     *          <span></span>
     *      </div>
     *      <i>xxx</i>
     * </body>
     * <script>
     *
     *     //output: i�ڵ�
     *     console.log( UE.dom.domUtils.getNextDomNode( document.getElementById( "test" ) ) );
     *
     * </script>
     * ```
     * @example
     * ```html
     * <body>
     *      <div>
     *          <span></span>
     *          <i id="test">xxx</i>
     *      </div>
     *      <b>xxx</b>
     * </body>
     * <script>
     *
     *     //����idΪtest��i�ڵ�֮��û���ֵܽڵ㣬 ������丸�ڵ㣨div��������ֵܽڵ�
     *     //output: b�ڵ�
     *     console.log( UE.dom.domUtils.getNextDomNode( document.getElementById( "test" ) ) );
     *
     * </script>
     * ```
     */

    /**
     * ȡ��node�ڵ����һ���ֵܽڵ㣬 ���startFromChild��ֵΪture�����Ȼ�ȡ���ӽڵ㣬
     * ������ӽڵ���ֱ�ӷ��ص�һ���ӽڵ㣻���û���ӽڵ����startFromChild��ֵΪfalse��
     * ��ִ��<a href="#UE.dom.domUtils.getNextDomNode(Node)">getNextDomNode(Node node)</a>�Ĳ��ҹ�̡�
     * @method getNextDomNode
     * @param { Node } node ��Ҫ��ȡ�����ֵܽڵ�Ľڵ����
     * @param { Boolean } startFromChild ���ҹ���Ƿ�����ӽڵ㿪ʼ
     * @return { Node | NULL } ��������������Ľڵ㣬 �򷵻ظýڵ㣬 ���򷵻�NULL
     * @see UE.dom.domUtils.getNextDomNode(Node)
     */
    getNextDomNode:function (node, startFromChild, filterFn, guard) {
        return getDomNode(node, 'firstChild', 'nextSibling', startFromChild, filterFn, guard);
    },
    getPreDomNode:function (node, startFromChild, filterFn, guard) {
        return getDomNode(node, 'lastChild', 'previousSibling', startFromChild, filterFn, guard);
    },

    /**
     * ���ڵ�node�Ƿ�����bookmark�ڵ�
     * @name isBookmarkNode
     * @grammar UM.dom.domUtils.isBookmarkNode(node)  => true|false
     */
    isBookmarkNode: function (node) {
        return node.nodeType == 1 && node.id && /^_baidu_bookmark_/i.test(node.id);
    },
    /**
     * ��ȡ�ڵ�node���ڵ�window����
     * @name  getWindow
     * @grammar UM.dom.domUtils.getWindow(node)  => window����
     */
    getWindow: function (node) {
        var doc = node.ownerDocument || node;
        return doc.defaultView || doc.parentWindow;
    },

    /**
     * ��ȡ��nodeA��nodeB���Ĺ��������Ƚڵ�
     * @method  getCommonAncestor
     * @param { Node } nodeA ��һ���ڵ�
     * @param { Node } nodeB �ڶ����ڵ�
     * @remind ����������ڵ���ͬһ���ڵ㣬 ��ֱ�ӷ��ظýڵ㡣
     * @return { Node | NULL } ���δ�ҵ������ڵ㣬 ����NULL�� ���򷵻����Ĺ������Ƚڵ㡣
     * @example
     * ```javascript
     * var commonAncestor = UE.dom.domUtils.getCommonAncestor( document.body, document.body.firstChild );
     * //output: true
     * console.log( commonAncestor.tagName.toLowerCase() === 'body' );
     * ```
     */
    getCommonAncestor:function (nodeA, nodeB) {
        if (nodeA === nodeB)
            return nodeA;
        var parentsA = [nodeA] , parentsB = [nodeB], parent = nodeA, i = -1;
        while (parent = parent.parentNode) {
            if (parent === nodeB) {
                return parent;
            }
            parentsA.push(parent);
        }
        parent = nodeB;
        while (parent = parent.parentNode) {
            if (parent === nodeA)
                return parent;
            parentsB.push(parent);
        }
        parentsA.reverse();
        parentsB.reverse();
        while (i++, parentsA[i] === parentsB[i]) {
        }
        return i == 0 ? null : parentsA[i - 1];

    },
    /**
     * ���node�ڵ���������Ϊ�յ��ֵ�inline�ڵ�
     * @method clearEmptySibling
     * @param { Node } node ִ�еĽڵ���� ���ýڵ������������ֵܽڵ��ǿյ�inline�ڵ㣬
     * ����Щ�ֵܽڵ㽫��ɾ��
     * @grammar UE.dom.domUtils.clearEmptySibling(node,ignoreNext)  //ignoreNextָ���Ƿ�����ұ߿սڵ�
     * @grammar UE.dom.domUtils.clearEmptySibling(node,ignoreNext,ignorePre)  //ignorePreָ���Ƿ������߿սڵ�
     * @example
     * ```html
     * <body>
     *     <div></div>
     *     <span id="test"></span>
     *     <i></i>
     *     <b></b>
     *     <em>xxx</em>
     *     <span></span>
     * </body>
     * <script>
     *
     *      UE.dom.domUtils.clearEmptySibling( document.getElementById( "test" ) );
     *
     *      //output: <div></div><span id="test"></span><em>xxx</em><span></span>
     *      console.log( document.body.innerHTML );
     *
     * </script>
     * ```
     */

    /**
     * ���node�ڵ���������Ϊ�յ��ֵ�inline�ڵ㣬 ���ignoreNext��ֵΪtrue��
     * ����Զ��ұ��ֵܽڵ�Ĳ�����
     * @method clearEmptySibling
     * @param { Node } node ִ�еĽڵ���� ���ýڵ������������ֵܽڵ��ǿյ�inline�ڵ㣬
     * @param { Boolean } ignoreNext �Ƿ���Ժ��Զ��ұߵ��ֵܽڵ�Ĳ���
     * ����Щ�ֵܽڵ㽫��ɾ��
     * @see UE.dom.domUtils.clearEmptySibling(Node)
     */

    /**
     * ���node�ڵ���������Ϊ�յ��ֵ�inline�ڵ㣬 ���ignoreNext��ֵΪtrue��
     * ����Զ��ұ��ֵܽڵ�Ĳ����� ���ignorePre��ֵΪtrue������Զ�����ֵܽڵ�Ĳ�����
     * @method clearEmptySibling
     * @param { Node } node ִ�еĽڵ���� ���ýڵ������������ֵܽڵ��ǿյ�inline�ڵ㣬
     * @param { Boolean } ignoreNext �Ƿ���Ժ��Զ��ұߵ��ֵܽڵ�Ĳ���
     * @param { Boolean } ignorePre �Ƿ���Ժ��Զ���ߵ��ֵܽڵ�Ĳ���
     * ����Щ�ֵܽڵ㽫��ɾ��
     * @see UE.dom.domUtils.clearEmptySibling(Node)
     */
    clearEmptySibling:function (node, ignoreNext, ignorePre) {
        function clear(next, dir) {
            var tmpNode;
            while (next && !domUtils.isBookmarkNode(next) && (domUtils.isEmptyInlineElement(next)
                //���ﲻ�ܰѿո��������ɿո�ɵ����������ּ�Ŀո񶪵���
                || !new RegExp('[^\t\n\r' + domUtils.fillChar + ']').test(next.nodeValue) )) {
                tmpNode = next[dir];
                domUtils.remove(next);
                next = tmpNode;
            }
        }
        !ignoreNext && clear(node.nextSibling, 'nextSibling');
        !ignorePre && clear(node.previousSibling, 'previousSibling');
    },

    /**
     * ��һ���ı��ڵ�node��ֳ������ı��ڵ㣬offsetָ�����λ��
     * @name split
     * @grammar UM.dom.domUtils.split(node,offset)  =>  TextNode  //���ش��з�λ�ÿ�ʼ�ĺ�һ���ı��ڵ�
     */
    split: function (node, offset) {
        var doc = node.ownerDocument;
        if (browser.ie && offset == node.nodeValue.length) {
            var next = doc.createTextNode('');
            return domUtils.insertAfter(node, next);
        }
        var retval = node.splitText(offset);
        //ie8��splitText�������childNodes,�����ֶ�������ĸ���
        if (browser.ie8) {
            var tmpNode = doc.createTextNode('');
            domUtils.insertAfter(retval, tmpNode);
            domUtils.remove(tmpNode);
        }
        return retval;
    },

    /**
     * ���ڵ�node�Ƿ�Ϊ�սڵ㣨�����ո񡢻��С�ռλ����ַ�
     * @name  isWhitespace
     * @grammar  UM.dom.domUtils.isWhitespace(node)  => true|false
     */
    isWhitespace: function (node) {
        return !new RegExp('[^ \t\n\r' + domUtils.fillChar + ']').test(node.nodeValue);
    },
    /**
     * ��ȡԪ��element�����viewport��λ�����
     * @name getXY
     * @grammar UM.dom.domUtils.getXY(element)  => Object //����������{x:left,y:top}
     */
    getXY: function (element) {
        var x = 0, y = 0;
        while (element.offsetParent) {
            y += element.offsetTop;
            x += element.offsetLeft;
            element = element.offsetParent;
        }
        return { 'x': x, 'y': y};
    },
    /**
     * ���ڵ�node�Ƿ��ǿ�inline�ڵ�
     * @name  isEmptyInlineElement
     * @grammar   UM.dom.domUtils.isEmptyInlineElement(node)  => 1|0
     * @example
     * <b><i></i></b> => 1
     * <b><i></i><u></u></b> => 1
     * <b></b> => 1
     * <b>xx<i></i></b> => 0
     */
    isEmptyInlineElement: function (node) {
        if (node.nodeType != 1 || !dtd.$removeEmpty[ node.tagName ]) {
            return 0;
        }
        node = node.firstChild;
        while (node) {
            //����Ǵ�����bookmark�����
            if (domUtils.isBookmarkNode(node)) {
                return 0;
            }
            if (node.nodeType == 1 && !domUtils.isEmptyInlineElement(node) ||
                node.nodeType == 3 && !domUtils.isWhitespace(node)
                ) {
                return 0;
            }
            node = node.nextSibling;
        }
        return 1;

    },


    /**
     * ���ڵ�node�Ƿ�Ϊ��Ԫ��
     * @name isBlockElm
     * @grammar UM.dom.domUtils.isBlockElm(node)  => true|false
     */
    isBlockElm: function (node) {
        return node.nodeType == 1 && (dtd.$block[node.tagName] || styleBlock[domUtils.getComputedStyle(node, 'display')]) && !dtd.$nonChild[node.tagName];
    },


    /**
     * ԭ��getElementsByTagName�ķ�װ
     * @name getElementsByTagName
     * @grammar UM.dom.domUtils.getElementsByTagName(node,tagName)  => Array  //�ڵ㼯������
     */
    getElementsByTagName: function (node, name, filter) {
        if (filter && utils.isString(filter)) {
            var className = filter;
            filter = function (node) {
                var result = false;
                $.each(utils.trim(className).replace(/[ ]{2,}/g, ' ').split(' '), function (i, v) {
                    if ($(node).hasClass(v)) {
                        result = true;
                        return false;
                    }
                })
                return result;
            }
        }
        name = utils.trim(name).replace(/[ ]{2,}/g, ' ').split(' ');
        var arr = [];
        for (var n = 0, ni; ni = name[n++];) {
            var list = node.getElementsByTagName(ni);
            for (var i = 0, ci; ci = list[i++];) {
                if (!filter || filter(ci))
                    arr.push(ci);
            }
        }
        return arr;
    },


    /**
     * ���ýڵ�node�����ӽڵ㲻�ᱻѡ��
     * @name unSelectable
     * @grammar UM.dom.domUtils.unSelectable(node)
     */
    unSelectable: ie && browser.ie9below || browser.opera ? function (node) {
        //for ie9
        node.onselectstart = function () {
            return false;
        };
        node.onclick = node.onkeyup = node.onkeydown = function () {
            return false;
        };
        node.unselectable = 'on';
        node.setAttribute("unselectable", "on");
        for (var i = 0, ci; ci = node.all[i++];) {
            switch (ci.tagName.toLowerCase()) {
                case 'iframe' :
                case 'textarea' :
                case 'input' :
                case 'select' :
                    break;
                default :
                    ci.unselectable = 'on';
                    node.setAttribute("unselectable", "on");
            }
        }
    } : function (node) {
        node.style.MozUserSelect =
            node.style.webkitUserSelect =
                    node.style.msUserSelect =
                        node.style.KhtmlUserSelect = 'none';
    },
    /**
     * ɾ��ڵ�node�ϵ�����attrNames��attrNamesΪ�����������
     * @name  removeAttributes
     * @grammar UM.dom.domUtils.removeAttributes(node,attrNames)
     * @example
     * //Before remove
     * <span style="font-size:14px;" id="test" name="followMe">xxxxx</span>
     * //Remove
     * UM.dom.domUtils.removeAttributes(node,["id","name"]);
     * //After remove
     * <span style="font-size:14px;">xxxxx</span>
     */
    removeAttributes: function (node, attrNames) {
        attrNames = utils.isArray(attrNames) ? attrNames : utils.trim(attrNames).replace(/[ ]{2,}/g, ' ').split(' ');
        for (var i = 0, ci; ci = attrNames[i++];) {
            ci = attrFix[ci] || ci;
            switch (ci) {
                case 'className':
                    node[ci] = '';
                    break;
                case 'style':
                    node.style.cssText = '';
                    !browser.ie && node.removeAttributeNode(node.getAttributeNode('style'))
            }
            node.removeAttribute(ci);
        }
    },
    /**
     * ��doc�´���һ����ǩ��Ϊtag������Ϊattrs��Ԫ��
     * @name createElement
     * @grammar UM.dom.domUtils.createElement(doc,tag,attrs)  =>  Node  //���ش����Ľڵ�
     */
    createElement: function (doc, tag, attrs) {
        return domUtils.setAttributes(doc.createElement(tag), attrs)
    },
    /**
     * Ϊ�ڵ�node�������attrs��attrsΪ���Լ�ֵ��
     * @name setAttributes
     * @grammar UM.dom.domUtils.setAttributes(node,attrs)  => node
     */
    setAttributes: function (node, attrs) {
        for (var attr in attrs) {
            if (attrs.hasOwnProperty(attr)) {
                var value = attrs[attr];
                switch (attr) {
                    case 'class':
                        //ie��Ҫ����ֵ��setAttribute��������
                        node.className = value;
                        break;
                    case 'style' :
                        node.style.cssText = node.style.cssText + ";" + value;
                        break;
                    case 'innerHTML':
                        node[attr] = value;
                        break;
                    case 'value':
                        node.value = value;
                        break;
                    default:
                        node.setAttribute(attrFix[attr] || attr, value);
                }
            }
        }
        return node;
    },

    /**
     * ��ȡԪ��element�ļ�����ʽ
     * @name getComputedStyle
     * @grammar UM.dom.domUtils.getComputedStyle(element,styleName)  => String //���ض�Ӧ��ʽ��Ƶ���ʽֵ
     * @example
     * getComputedStyle(document.body,"font-size")  =>  "15px"
     * getComputedStyle(form,"color")  =>  "#ffccdd"
     */
    getComputedStyle: function (element, styleName) {
        return utils.transUnitToPx(utils.fixColor(styleName, $(element).css(styleName)));
    },

    /**
     * ��ֹ�¼�Ĭ����Ϊ
     * @param {Event} evt    ��Ҫ��֯���¼�����
     */
    preventDefault: function (evt) {
        evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false);
    },

    /**
     * ɾ��Ԫ��elementָ������ʽ
     * @method removeStyle
     * @param { Element } element ��Ҫɾ����ʽ��Ԫ��
     * @param { String } styleName ��Ҫɾ�����ʽ��
     * @example
     * ```html
     * <span id="test" style="color: red; background: blue;"></span>
     *
     * <script>
     *
     *     var testNode = document.getElementById("test");
     *
     *     UE.dom.domUtils.removeStyle( testNode, 'color' );
     *
     *     //output: background: blue;
     *     console.log( testNode.style.cssText );
     *
     * </script>
     * ```
     */
    removeStyle:function (element, name) {
        if(browser.ie ){
            //���color�ȵ�������һ��
            if(name == 'color'){
                name = '(^|;)' + name;
            }
            element.style.cssText = element.style.cssText.replace(new RegExp(name + '[^:]*:[^;]+;?','ig'),'')
        }else{
            if (element.style.removeProperty) {
                element.style.removeProperty (name);
            }else {
                element.style.removeAttribute (utils.cssStyleToDomStyle(name));
            }
        }


        if (!element.style.cssText) {
            domUtils.removeAttributes(element, ['style']);
        }
    },

    /**
     * ��ȡԪ��element��ĳ����ʽֵ
     * @name getStyle
     * @grammar UM.dom.domUtils.getStyle(element,name)  => String
     */
    getStyle: function (element, name) {
        var value = element.style[ utils.cssStyleToDomStyle(name) ];
        return utils.fixColor(name, value);
    },
    /**
     * ΪԪ��element������ʽ����ֵ
     * @name setStyle
     * @grammar UM.dom.domUtils.setStyle(element,name,value)
     */
    setStyle: function (element, name, value) {
        element.style[utils.cssStyleToDomStyle(name)] = value;
        if (!utils.trim(element.style.cssText)) {
            this.removeAttributes(element, 'style')
        }
    },

    /**
     * ɾ��_moz_dirty����
     * @function
     */
    removeDirtyAttr: function (node) {
        for (var i = 0, ci, nodes = node.getElementsByTagName('*'); ci = nodes[i++];) {
            ci.removeAttribute('_moz_dirty');
        }
        node.removeAttribute('_moz_dirty');
    },
    /**
     * �����ӽڵ������
     * @function
     * @param {Node}    node    ���ڵ�
     * @param  {Function}    fn    �����ӽڵ�Ĺ�����Ϊ�գ���õ������ӽڵ������
     * @return {Number}    ��������ӽڵ������
     */
    getChildCount: function (node, fn) {
        var count = 0, first = node.firstChild;
        fn = fn || function () {
            return 1;
        };
        while (first) {
            if (fn(first)) {
                count++;
            }
            first = first.nextSibling;
        }
        return count;
    },

    /**
     * �ж��Ƿ�Ϊ�սڵ�
     * @function
     * @param {Node}    node    �ڵ�
     * @return {Boolean}    �Ƿ�Ϊ�սڵ�
     */
    isEmptyNode: function (node) {
        return !node.firstChild || domUtils.getChildCount(node, function (node) {
            return  !domUtils.isBr(node) && !domUtils.isBookmarkNode(node) && !domUtils.isWhitespace(node)
        }) == 0
    },

    /**
     * �жϽڵ��Ƿ�Ϊbr
     * @function
     * @param {Node}    node   �ڵ�
     */
    isBr: function (node) {
        return node.nodeType == 1 && node.tagName == 'BR';
    },
    
    isEmptyBlock: function (node, reg) {
        if (node.nodeType != 1)
            return 0;
        reg = reg || new RegExp('[ \t\r\n' + domUtils.fillChar + ']', 'g');
        if (node[browser.ie ? 'innerText' : 'textContent'].replace(reg, '').length > 0) {
            return 0;
        }
        for (var n in dtd.$isNotEmpty) {
            if (node.getElementsByTagName(n).length) {
                return 0;
            }
        }
        return 1;
    },

    //�ж��Ƿ��Ǳ༭���Զ���Ĳ���
    isCustomeNode: function (node) {
        return node.nodeType == 1 && node.getAttribute('_ue_custom_node_');
    },
    fillNode: function (doc, node) {
        var tmpNode = browser.ie ? doc.createTextNode(domUtils.fillChar) : doc.createElement('br');
        node.innerHTML = '';
        node.appendChild(tmpNode);
    },
    isBoundaryNode: function (node, dir) {
        var tmp;
        while (!domUtils.isBody(node)) {
            tmp = node;
            node = node.parentNode;
            if (tmp !== node[dir]) {
                return false;
            }
        }
        return true;
    },
    isFillChar: function (node, isInStart) {
        return node.nodeType == 3 && !node.nodeValue.replace(new RegExp((isInStart ? '^' : '' ) + domUtils.fillChar), '').length
    },
    isBody: function(node){
        return $(node).hasClass('edui-body-container');
    }
};
var fillCharReg = new RegExp(domUtils.fillChar, 'g');
///import editor.js
///import core/utils.js
///import core/browser.js
///import core/dom/dom.js
///import core/dom/dtd.js
///import core/dom/domUtils.js
/**
 * @file
 * @name UM.dom.Range
 * @anthor zhanyi
 * @short Range
 * @import editor.js,core/utils.js,core/browser.js,core/dom/domUtils.js,core/dom/dtd.js
 * @desc Range��Χʵ���࣬������UEditor�ײ�����࣬ͳһw3cRange��ieRange֮��Ĳ��죬�����ӿں�����
 */
(function () {
    var guid = 0,
        fillChar = domUtils.fillChar,
        fillData;

    /**
     * ����range��collapse״̬
     * @param  {Range}   range    range����
     */
    function updateCollapse(range) {
        range.collapsed =
            range.startContainer && range.endContainer &&
                range.startContainer === range.endContainer &&
                range.startOffset == range.endOffset;
    }

    function selectOneNode(rng){
        return !rng.collapsed && rng.startContainer.nodeType == 1 && rng.startContainer === rng.endContainer && rng.endOffset - rng.startOffset == 1
    }
    function setEndPoint(toStart, node, offset, range) {
        //���node���Ապϱ�ǩҪ����
        if (node.nodeType == 1 && (dtd.$empty[node.tagName] || dtd.$nonChild[node.tagName])) {
            offset = domUtils.getNodeIndex(node) + (toStart ? 0 : 1);
            node = node.parentNode;
        }
        if (toStart) {
            range.startContainer = node;
            range.startOffset = offset;
            if (!range.endContainer) {
                range.collapse(true);
            }
        } else {
            range.endContainer = node;
            range.endOffset = offset;
            if (!range.startContainer) {
                range.collapse(false);
            }
        }
        updateCollapse(range);
        return range;
    }


    /**
     * @name Range
     * @grammar new UM.dom.Range(document)  => Range ʵ��
     * @desc ����һ����document�󶨵Ŀյ�Rangeʵ��
     * - ***startContainer*** ��ʼ�߽�������ڵ�,������elementNode������textNode
     * - ***startOffset*** �����ڵ��е�ƫ�����������elementNode����childNodes�еĵڼ����������textNode����nodeValue�ĵڼ����ַ�
     * - ***endContainer*** ����߽�������ڵ�,������elementNode������textNode
     * - ***endOffset*** �����ڵ��е�ƫ�����������elementNode����childNodes�еĵڼ����������textNode����nodeValue�ĵڼ����ַ�
     * - ***document*** ��range������document����
     * - ***collapsed*** �Ƿ��Ǳպ�״̬
     */
    var Range = dom.Range = function (document,body) {
        var me = this;
        me.startContainer =
            me.startOffset =
                me.endContainer =
                    me.endOffset = null;
        me.document = document;
        me.collapsed = true;
        me.body = body;
    };

    /**
     * ɾ��fillData
     * @param doc
     * @param excludeNode
     */
    function removeFillData(doc, excludeNode) {
        try {
            if (fillData && domUtils.inDoc(fillData, doc)) {
                if (!fillData.nodeValue.replace(fillCharReg, '').length) {
                    var tmpNode = fillData.parentNode;
                    domUtils.remove(fillData);
                    while (tmpNode && domUtils.isEmptyInlineElement(tmpNode) &&
                        //safari��contains��bug
                        (browser.safari ? !(domUtils.getPosition(tmpNode,excludeNode) & domUtils.POSITION_CONTAINS) : !tmpNode.contains(excludeNode))
                        ) {
                        fillData = tmpNode.parentNode;
                        domUtils.remove(tmpNode);
                        tmpNode = fillData;
                    }
                } else {
                    fillData.nodeValue = fillData.nodeValue.replace(fillCharReg, '');
                }
            }
        } catch (e) {
        }
    }

    /**
     *
     * @param node
     * @param dir
     */
    function mergeSibling(node, dir) {
        var tmpNode;
        node = node[dir];
        while (node && domUtils.isFillChar(node)) {
            tmpNode = node[dir];
            domUtils.remove(node);
            node = tmpNode;
        }
    }

    function execContentsAction(range, action) {
        //����߽�
        //range.includeBookmark();
        var start = range.startContainer,
            end = range.endContainer,
            startOffset = range.startOffset,
            endOffset = range.endOffset,
            doc = range.document,
            frag = doc.createDocumentFragment(),
            tmpStart, tmpEnd;
        if (start.nodeType == 1) {
            start = start.childNodes[startOffset] || (tmpStart = start.appendChild(doc.createTextNode('')));
        }
        if (end.nodeType == 1) {
            end = end.childNodes[endOffset] || (tmpEnd = end.appendChild(doc.createTextNode('')));
        }
        if (start === end && start.nodeType == 3) {
            frag.appendChild(doc.createTextNode(start.substringData(startOffset, endOffset - startOffset)));
            //is not clone
            if (action) {
                start.deleteData(startOffset, endOffset - startOffset);
                range.collapse(true);
            }
            return frag;
        }
        var current, currentLevel, clone = frag,
            startParents = domUtils.findParents(start, true), endParents = domUtils.findParents(end, true);
        for (var i = 0; startParents[i] == endParents[i];) {
            i++;
        }
        for (var j = i, si; si = startParents[j]; j++) {
            current = si.nextSibling;
            if (si == start) {
                if (!tmpStart) {
                    if (range.startContainer.nodeType == 3) {
                        clone.appendChild(doc.createTextNode(start.nodeValue.slice(startOffset)));
                        //is not clone
                        if (action) {
                            start.deleteData(startOffset, start.nodeValue.length - startOffset);
                        }
                    } else {
                        clone.appendChild(!action ? start.cloneNode(true) : start);
                    }
                }
            } else {
                currentLevel = si.cloneNode(false);
                clone.appendChild(currentLevel);
            }
            while (current) {
                if (current === end || current === endParents[j]) {
                    break;
                }
                si = current.nextSibling;
                clone.appendChild(!action ? current.cloneNode(true) : current);
                current = si;
            }
            clone = currentLevel;
        }
        clone = frag;
        if (!startParents[i]) {
            clone.appendChild(startParents[i - 1].cloneNode(false));
            clone = clone.firstChild;
        }
        for (var j = i, ei; ei = endParents[j]; j++) {
            current = ei.previousSibling;
            if (ei == end) {
                if (!tmpEnd && range.endContainer.nodeType == 3) {
                    clone.appendChild(doc.createTextNode(end.substringData(0, endOffset)));
                    //is not clone
                    if (action) {
                        end.deleteData(0, endOffset);
                    }
                }
            } else {
                currentLevel = ei.cloneNode(false);
                clone.appendChild(currentLevel);
            }
            //�������ͬ�����ұߵ�һ���Ѿ�����ʼ����
            if (j != i || !startParents[i]) {
                while (current) {
                    if (current === start) {
                        break;
                    }
                    ei = current.previousSibling;
                    clone.insertBefore(!action ? current.cloneNode(true) : current, clone.firstChild);
                    current = ei;
                }
            }
            clone = currentLevel;
        }
        if (action) {
            range.setStartBefore(!endParents[i] ? endParents[i - 1] : !startParents[i] ? startParents[i - 1] : endParents[i]).collapse(true);
        }
        tmpStart && domUtils.remove(tmpStart);
        tmpEnd && domUtils.remove(tmpEnd);
        return frag;
    }
    Range.prototype = {
        /**
         * @name deleteContents
         * @grammar range.deleteContents()  => Range
         * @desc ɾ��ǰѡ��Χ�е��������ݲ�����rangeʵ����ʱ��range�Ѿ�����˱պ�״̬
         * @example
         * DOM Element :
         * <b>x<i>x[x<i>xx]x</b>
         * //ִ�з�����
         * <b>x<i>x<i>|x</b>
         * ע��range�ı���
         * range.startContainer => b
         * range.startOffset  => 2
         * range.endContainer => b
         * range.endOffset => 2
         * range.collapsed => true
         */
        deleteContents:function () {
            var txt;
            if (!this.collapsed) {
                execContentsAction(this, 1);
            }
            if (browser.webkit) {
                txt = this.startContainer;
                if (txt.nodeType == 3 && !txt.nodeValue.length) {
                    this.setStartBefore(txt).collapse(true);
                    domUtils.remove(txt);
                }
            }
            return this;
        },
        inFillChar : function(){
            var start = this.startContainer;
            if(this.collapsed && start.nodeType == 3
                && start.nodeValue.replace(new RegExp('^' + domUtils.fillChar),'').length + 1 == start.nodeValue.length
                ){
                return true;
            }
            return false;
        },
        /**
         * @name  setStart
         * @grammar range.setStart(node,offset)  => Range
         * @desc    ����range�Ŀ�ʼλ��λ��node�ڵ��ڣ�ƫ����Ϊoffset
         * ���node��elementNode��offsetָ����childNodes�еĵڼ����������textNode��offsetָ����nodeValue�ĵڼ����ַ�
         */
        setStart:function (node, offset) {
            return setEndPoint(true, node, offset, this);
        },
        /**
         * ����range�Ľ���λ��λ��node�ڵ㣬ƫ����Ϊoffset
         * ���node��elementNode��offsetָ����childNodes�еĵڼ����������textNode��offsetָ����nodeValue�ĵڼ����ַ�
         * @name  setEnd
         * @grammar range.setEnd(node,offset)  => Range
         */
        setEnd:function (node, offset) {
            return setEndPoint(false, node, offset, this);
        },
        /**
         * ��Range��ʼλ�����õ�node�ڵ�֮��
         * @name  setStartAfter
         * @grammar range.setStartAfter(node)  => Range
         * @example
         * <b>xx<i>x|x</i>x</b>
         * ִ��setStartAfter(i)��
         * range.startContainer =>b
         * range.startOffset =>2
         */
        setStartAfter:function (node) {
            return this.setStart(node.parentNode, domUtils.getNodeIndex(node) + 1);
        },
        /**
         * ��Range��ʼλ�����õ�node�ڵ�֮ǰ
         * @name  setStartBefore
         * @grammar range.setStartBefore(node)  => Range
         * @example
         * <b>xx<i>x|x</i>x</b>
         * ִ��setStartBefore(i)��
         * range.startContainer =>b
         * range.startOffset =>1
         */
        setStartBefore:function (node) {
            return this.setStart(node.parentNode, domUtils.getNodeIndex(node));
        },
        /**
         * ��Range����λ�����õ�node�ڵ�֮��
         * @name  setEndAfter
         * @grammar range.setEndAfter(node)  => Range
         * @example
         * <b>xx<i>x|x</i>x</b>
         * setEndAfter(i)��
         * range.endContainer =>b
         * range.endtOffset =>2
         */
        setEndAfter:function (node) {
            return this.setEnd(node.parentNode, domUtils.getNodeIndex(node) + 1);
        },
        /**
         * ��Range����λ�����õ�node�ڵ�֮ǰ
         * @name  setEndBefore
         * @grammar range.setEndBefore(node)  => Range
         * @example
         * <b>xx<i>x|x</i>x</b>
         * ִ��setEndBefore(i)��
         * range.endContainer =>b
         * range.endtOffset =>1
         */
        setEndBefore:function (node) {
            return this.setEnd(node.parentNode, domUtils.getNodeIndex(node));
        },
        /**
         * ��Range��ʼλ�����õ�node�ڵ��ڵĿ�ʼλ��
         * @name  setStartAtFirst
         * @grammar range.setStartAtFirst(node)  => Range
         */
        setStartAtFirst:function (node) {
            return this.setStart(node, 0);
        },
        /**
         * ��Range��ʼλ�����õ�node�ڵ��ڵĽ���λ��
         * @name  setStartAtLast
         * @grammar range.setStartAtLast(node)  => Range
         */
        setStartAtLast:function (node) {
            return this.setStart(node, node.nodeType == 3 ? node.nodeValue.length : node.childNodes.length);
        },
        /**
         * ��Range����λ�����õ�node�ڵ��ڵĿ�ʼλ��
         * @name  setEndAtFirst
         * @grammar range.setEndAtFirst(node)  => Range
         */
        setEndAtFirst:function (node) {
            return this.setEnd(node, 0);
        },
        /**
         * ��Range����λ�����õ�node�ڵ��ڵĽ���λ��
         * @name  setEndAtLast
         * @grammar range.setEndAtLast(node)  => Range
         */
        setEndAtLast:function (node) {
            return this.setEnd(node, node.nodeType == 3 ? node.nodeValue.length : node.childNodes.length);
        },

        /**
         * ѡ�������ָ���ڵ�,�����ذ�ýڵ��range
         * @name  selectNode
         * @grammar range.selectNode(node)  => Range
         */
        selectNode:function (node) {
            return this.setStartBefore(node).setEndAfter(node);
        },
        /**
         * ѡ��node�ڲ������нڵ㣬�����ض�Ӧ��range
         * @name selectNodeContents
         * @grammar range.selectNodeContents(node)  => Range
         * @example
         * <b>xx[x<i>xxx</i>]xxx</b>
         * ִ�к�
         * <b>[xxx<i>xxx</i>xxx]</b>
         * range.startContainer =>b
         * range.startOffset =>0
         * range.endContainer =>b
         * range.endOffset =>3
         */
        selectNodeContents:function (node) {
            return this.setStart(node, 0).setEndAtLast(node);
        },

        /**
         * ��¡һ���µ�range����
         * @name  cloneRange
         * @grammar range.cloneRange() => Range
         */
        cloneRange:function () {
            var me = this;
            return new Range(me.document).setStart(me.startContainer, me.startOffset).setEnd(me.endContainer, me.endOffset);

        },

        /**
         * ��ѡ��պϵ�β������toStartΪ�棬��պϵ�ͷ��
         * @name  collapse
         * @grammar range.collapse() => Range
         * @grammar range.collapse(true) => Range   //�պ�ѡ��ͷ��
         */
        collapse:function (toStart) {
            var me = this;
            if (toStart) {
                me.endContainer = me.startContainer;
                me.endOffset = me.startOffset;
            } else {
                me.startContainer = me.endContainer;
                me.startOffset = me.endOffset;
            }
            me.collapsed = true;
            return me;
        },

        /**
         * ����range�ı߽磬ʹ��"����"����С��λ��
         * @name  shrinkBoundary
         * @grammar range.shrinkBoundary()  => Range  //range��ʼλ�úͽ���λ�ö�����μ�<code><a href="#adjustmentboundary">adjustmentBoundary</a></code>
         * @grammar range.shrinkBoundary(true)  => Range  //������ʼλ�ã����Խ���λ��
         * @example
         * <b>xx[</b>xxxxx] ==> <b>xx</b>[xxxxx]
         * <b>x[xx</b><i>]xxx</i> ==> <b>x[xx]</b><i>xxx</i>
         * [<b><i>xxxx</i>xxxxxxx</b>] ==> <b><i>[xxxx</i>xxxxxxx]</b>
         */
        shrinkBoundary:function (ignoreEnd) {
            var me = this, child,
                collapsed = me.collapsed;
            function check(node){
                return node.nodeType == 1 && !domUtils.isBookmarkNode(node) && !dtd.$empty[node.tagName] && !dtd.$nonChild[node.tagName]
            }
            while (me.startContainer.nodeType == 1 //��element
                && (child = me.startContainer.childNodes[me.startOffset]) //�ӽڵ�Ҳ��element
                && check(child)) {
                me.setStart(child, 0);
            }
            if (collapsed) {
                return me.collapse(true);
            }
            if (!ignoreEnd) {
                while (me.endContainer.nodeType == 1//��element
                    && me.endOffset > 0 //����ǿ�Ԫ�ؾ��˳� endOffset=0��ôendOffst-1Ϊ��ֵ��childNodes[endOffset]����
                    && (child = me.endContainer.childNodes[me.endOffset - 1]) //�ӽڵ�Ҳ��element
                    && check(child)) {
                    me.setEnd(child, child.childNodes.length);
                }
            }
            return me;
        },

        /**
         * ����߽������������textNode,�͵���elementNode��
         * @name trimBoundary
         * @grammar range.trimBoundary([ignoreEnd])  => Range //true���Խ���߽�
         * @example
         * DOM Element :
         * <b>|xxx</b>
         * startContainer = xxx; startOffset = 0
         * //ִ�к󱾷�����
         * startContainer = <b>;  startOffset = 0
         * @example
         * Dom Element :
         * <b>xx|x</b>
         * startContainer = xxx;  startOffset = 2
         * //ִ�б�������xxx��ʵʵ���ڵ��зֳ�����TextNode
         * startContainer = <b>; startOffset = 1
         */
        trimBoundary:function (ignoreEnd) {
            this.txtToElmBoundary();
            var start = this.startContainer,
                offset = this.startOffset,
                collapsed = this.collapsed,
                end = this.endContainer;
            if (start.nodeType == 3) {
                if (offset == 0) {
                    this.setStartBefore(start);
                } else {
                    if (offset >= start.nodeValue.length) {
                        this.setStartAfter(start);
                    } else {
                        var textNode = domUtils.split(start, offset);
                        //���½���߽�
                        if (start === end) {
                            this.setEnd(textNode, this.endOffset - offset);
                        } else if (start.parentNode === end) {
                            this.endOffset += 1;
                        }
                        this.setStartBefore(textNode);
                    }
                }
                if (collapsed) {
                    return this.collapse(true);
                }
            }
            if (!ignoreEnd) {
                offset = this.endOffset;
                end = this.endContainer;
                if (end.nodeType == 3) {
                    if (offset == 0) {
                        this.setEndBefore(end);
                    } else {
                        offset < end.nodeValue.length && domUtils.split(end, offset);
                        this.setEndAfter(end);
                    }
                }
            }
            return this;
        },
        /**
         * ���ѡ�����ı��ı߽��ϣ�����չѡ���ı��ĸ��ڵ���
         * @name  txtToElmBoundary
         * @example
         * Dom Element :
         * <b> |xxx</b>
         * startContainer = xxx;  startOffset = 0
         * //������ִ�к�
         * startContainer = <b>; startOffset = 0
         * @example
         * Dom Element :
         * <b> xxx| </b>
         * startContainer = xxx; startOffset = 3
         * //������ִ�к�
         * startContainer = <b>; startOffset = 1
         */
        txtToElmBoundary:function (ignoreCollapsed) {
            function adjust(r, c) {
                var container = r[c + 'Container'],
                    offset = r[c + 'Offset'];
                if (container.nodeType == 3) {
                    if (!offset) {
                        r['set' + c.replace(/(\w)/, function (a) {
                            return a.toUpperCase();
                        }) + 'Before'](container);
                    } else if (offset >= container.nodeValue.length) {
                        r['set' + c.replace(/(\w)/, function (a) {
                            return a.toUpperCase();
                        }) + 'After' ](container);
                    }
                }
            }

            if (ignoreCollapsed || !this.collapsed) {
                adjust(this, 'start');
                adjust(this, 'end');
            }
            return this;
        },

        /**
         * �ڵ�ǰѡ��Ŀ�ʼλ��ǰ����һ���ڵ����fragment��range�Ŀ�ʼλ�û��ڲ���ڵ��ǰ��
         * @name  insertNode
         * @grammar range.insertNode(node)  => Range //node������textNode,elementNode,fragment
         * @example
         * Range :
         * xxx[x<p>xxxx</p>xxxx]x<p>sdfsdf</p>
         * �����Node :
         * <p>ssss</p>
         * ִ�б��������Range :
         * xxx[<p>ssss</p>x<p>xxxx</p>xxxx]x<p>sdfsdf</p>
         */
        insertNode:function (node) {
            var first = node, length = 1;
            if (node.nodeType == 11) {
                first = node.firstChild;
                length = node.childNodes.length;
            }
            this.trimBoundary(true);
            var start = this.startContainer,
                offset = this.startOffset;
            var nextNode = start.childNodes[ offset ];
            if (nextNode) {
                start.insertBefore(node, nextNode);
            } else {
                start.appendChild(node);
            }
            if (first.parentNode === this.endContainer) {
                this.endOffset = this.endOffset + length;
            }
            return this.setStartBefore(first);
        },
        /**
         * ���ù��պ�λ��,toEnd����Ϊtrueʱ��꽫�պϵ�ѡ��Ľ�β
         * @name  setCursor
         * @grammar range.setCursor([toEnd])  =>  Range   //toEndΪtrueʱ�����պϵ�ѡ���ĩβ
         */
        setCursor:function (toEnd, noFillData) {
            return this.collapse(!toEnd).select(noFillData);
        },
        /**
         * ������ǰrange��һ����ǩ����¼�µ�ǰrange��λ�ã����㵱dom���ı�ʱ�������һ�ԭ����ѡ��λ��
         * @name createBookmark
         * @grammar range.createBookmark([serialize])  => Object  //{start:��ʼ���,end:������,id:serialize} serializeΪ��ʱ����ʼ�������ǲ���ڵ��id�������ǲ���ڵ������
         */
        createBookmark:function (serialize, same) {
            var endNode,
                startNode = this.document.createElement('span');
            startNode.style.cssText = 'display:none;line-height:0px;';
            startNode.appendChild(this.document.createTextNode('\u200D'));
            startNode.id = '_baidu_bookmark_start_' + (same ? '' : guid++);

            if (!this.collapsed) {
                endNode = startNode.cloneNode(true);
                endNode.id = '_baidu_bookmark_end_' + (same ? '' : guid++);
            }
            this.insertNode(startNode);
            if (endNode) {
                this.collapse().insertNode(endNode).setEndBefore(endNode);
            }
            this.setStartAfter(startNode);
            return {
                start:serialize ? startNode.id : startNode,
                end:endNode ? serialize ? endNode.id : endNode : null,
                id:serialize
            }
        },
        /**
         *  �ƶ��߽絽��ǩλ�ã���ɾ��������ǩ�ڵ�
         *  @name  moveToBookmark
         *  @grammar range.moveToBookmark(bookmark)  => Range //�õ�ǰ��rangeѡ����bookmark��λ��,bookmark��������range.createBookmark������
         */
        moveToBookmark:function (bookmark) {
            var start = bookmark.id ? this.document.getElementById(bookmark.start) : bookmark.start,
                end = bookmark.end && bookmark.id ? this.document.getElementById(bookmark.end) : bookmark.end;
            this.setStartBefore(start);
            domUtils.remove(start);
            if (end) {
                this.setEndBefore(end);
                domUtils.remove(end);
            } else {
                this.collapse(true);
            }
            return this;
        },

        /**
         * ����Range�ı߽磬ʹ��"��С"������ʵ�λ��
         * @name adjustmentBoundary
         * @grammar range.adjustmentBoundary() => Range   //�μ�<code><a href="#shrinkboundary">shrinkBoundary</a></code>
         * @example
         * <b>xx[</b>xxxxx] ==> <b>xx</b>[xxxxx]
         * <b>x[xx</b><i>]xxx</i> ==> <b>x[xx</b>]<i>xxx</i>
         */
        adjustmentBoundary:function () {
            if (!this.collapsed) {
                while (!domUtils.isBody(this.startContainer) &&
                    this.startOffset == this.startContainer[this.startContainer.nodeType == 3 ? 'nodeValue' : 'childNodes'].length &&
                    this.startContainer[this.startContainer.nodeType == 3 ? 'nodeValue' : 'childNodes'].length
                    ) {

                    this.setStartAfter(this.startContainer);
                }
                while (!domUtils.isBody(this.endContainer) && !this.endOffset &&
                    this.endContainer[this.endContainer.nodeType == 3 ? 'nodeValue' : 'childNodes'].length
                    ) {
                    this.setEndBefore(this.endContainer);
                }
            }
            return this;
        },

        /**
         * �õ�һ���ԱպϵĽڵ�,�����ڻ�ȡ�Ապ͵Ľڵ㣬����ͼƬ�ڵ�
         * @name  getClosedNode
         * @grammar range.getClosedNode()  => node|null
         * @example
         * <b>xxxx[<img />]xxx</b>
         */
        getClosedNode:function () {
            var node;
            if (!this.collapsed) {
                var range = this.cloneRange().adjustmentBoundary().shrinkBoundary();
                if (selectOneNode(range)) {
                    var child = range.startContainer.childNodes[range.startOffset];
                    if (child && child.nodeType == 1 && (dtd.$empty[child.tagName] || dtd.$nonChild[child.tagName])) {
                        node = child;
                    }
                }
            }
            return node;
        },
        /**
         * ��ݵ�ǰrangeѡ�����ݽڵ㣨��ҳ���ϱ���Ϊ������ʾ��
         * @name select
         * @grammar range.select();  => Range
         */
        select:browser.ie ? function (noFillData, textRange) {
            var nativeRange;
            if (!this.collapsed)
                this.shrinkBoundary();
            var node = this.getClosedNode();
            if (node && !textRange) {
                try {
                    nativeRange = this.document.body.createControlRange();
                    nativeRange.addElement(node);
                    nativeRange.select();
                } catch (e) {}
                return this;
            }
            var bookmark = this.createBookmark(),
                start = bookmark.start,
                end;
            nativeRange = this.document.body.createTextRange();
            nativeRange.moveToElementText(start);
            nativeRange.moveStart('character', 1);
            if (!this.collapsed) {
                var nativeRangeEnd = this.document.body.createTextRange();
                end = bookmark.end;
                nativeRangeEnd.moveToElementText(end);
                nativeRange.setEndPoint('EndToEnd', nativeRangeEnd);
            } else {
                if (!noFillData && this.startContainer.nodeType != 3) {
                    //ʹ��<span>|x<span>�̶�ס���
                    var tmpText = this.document.createTextNode(fillChar),
                        tmp = this.document.createElement('span');
                    tmp.appendChild(this.document.createTextNode(fillChar));
                    start.parentNode.insertBefore(tmp, start);
                    start.parentNode.insertBefore(tmpText, start);
                    //����b,i,uʱ���������i�ϱߵ�b
                    removeFillData(this.document, tmpText);
                    fillData = tmpText;
                    mergeSibling(tmp, 'previousSibling');
                    mergeSibling(start, 'nextSibling');
                    nativeRange.moveStart('character', -1);
                    nativeRange.collapse(true);
                }
            }
            this.moveToBookmark(bookmark);
            tmp && domUtils.remove(tmp);
            //IE������״̬�²�֧��range������catchһ��
            try {
                nativeRange.select();
            } catch (e) {
            }
            return this;
        } : function (notInsertFillData) {
            function checkOffset(rng){

                function check(node,offset,dir){
                    if(node.nodeType == 3 && node.nodeValue.length < offset){
                        rng[dir + 'Offset'] = node.nodeValue.length
                    }
                }
                check(rng.startContainer,rng.startOffset,'start');
                check(rng.endContainer,rng.endOffset,'end');
            }
            var win = domUtils.getWindow(this.document),
                sel = win.getSelection(),
                txtNode;
            //FF�¹ر��Զ�����ʱ�������ڹر�dialogʱ����
            //ff�����body.focus�����ܶ�λ�պϹ�굽�༭����
            browser.gecko ? this.body.focus() : win.focus();
            if (sel) {
                sel.removeAllRanges();
                // trace:870 chrome/safari�����br���ڱպϵ�range���ܶ�λ ����ȥ�����ж�
                // this.startContainer.nodeType != 3 &&! ((child = this.startContainer.childNodes[this.startOffset]) && child.nodeType == 1 && child.tagName == 'BR'
                if (this.collapsed && !notInsertFillData) {
//                    //opear���û�нڵ���ţ�ԭ��Ĳ��ܹ���λ,������body�ĵ�һ������հ׽ڵ�
//                    if (notInsertFillData && browser.opera && !domUtils.isBody(this.startContainer) && this.startContainer.nodeType == 1) {
//                        var tmp = this.document.createTextNode('');
//                        this.insertNode(tmp).setStart(tmp, 0).collapse(true);
//                    }
//
                    //�����������ı��ڵ�����
                    //�������µ����
                    //<b>|xxxx</b>
                    //<b>xxxx</b>|xxxx
                    //xxxx<b>|</b>
                    var start = this.startContainer,child = start;
                    if(start.nodeType == 1){
                        child = start.childNodes[this.startOffset];

                    }
                    if( !(start.nodeType == 3 && this.startOffset)  &&
                        (child ?
                            (!child.previousSibling || child.previousSibling.nodeType != 3)
                            :
                            (!start.lastChild || start.lastChild.nodeType != 3)
                            )
                        ){
                        txtNode = this.document.createTextNode(fillChar);
                        //����ǰ����
                        this.insertNode(txtNode);
                        removeFillData(this.document, txtNode);
                        mergeSibling(txtNode, 'previousSibling');
                        mergeSibling(txtNode, 'nextSibling');
                        fillData = txtNode;
                        this.setStart(txtNode, browser.webkit ? 1 : 0).collapse(true);
                    }
                }
                var nativeRange = this.document.createRange();
                if(this.collapsed && browser.opera && this.startContainer.nodeType == 1){
                    var child = this.startContainer.childNodes[this.startOffset];
                    if(!child){
                        //��ǰ��£
                        child = this.startContainer.lastChild;
                        if( child && domUtils.isBr(child)){
                            this.setStartBefore(child).collapse(true);
                        }
                    }else{
                        //���£
                        while(child && domUtils.isBlockElm(child)){
                            if(child.nodeType == 1 && child.childNodes[0]){
                                child = child.childNodes[0]
                            }else{
                                break;
                            }
                        }
                        child && this.setStartBefore(child).collapse(true)
                    }

                }
                //��createAddress���һλ��Ĳ�׼�������������΢��
                checkOffset(this);
                nativeRange.setStart(this.startContainer, this.startOffset);
                nativeRange.setEnd(this.endContainer, this.endOffset);
                sel.addRange(nativeRange);
            }
            return this;
        },
      

        createAddress : function(ignoreEnd,ignoreTxt){
            var addr = {},me = this;

            function getAddress(isStart){
                var node = isStart ? me.startContainer : me.endContainer;
                var parents = domUtils.findParents(node,true,function(node){return !domUtils.isBody(node)}),
                    addrs = [];
                for(var i = 0,ci;ci = parents[i++];){
                    addrs.push(domUtils.getNodeIndex(ci,ignoreTxt));
                }
                var firstIndex = 0;

                if(ignoreTxt){
                    if(node.nodeType == 3){
                        var tmpNode = node.previousSibling;
                        while(tmpNode && tmpNode.nodeType == 3){
                            firstIndex += tmpNode.nodeValue.replace(fillCharReg,'').length;
                            tmpNode = tmpNode.previousSibling;
                        }
                        firstIndex +=  (isStart ? me.startOffset : me.endOffset)// - (fillCharReg.test(node.nodeValue) ? 1 : 0 )
                    }else{
                        node =  node.childNodes[ isStart ? me.startOffset : me.endOffset];
                        if(node){
                            firstIndex = domUtils.getNodeIndex(node,ignoreTxt);
                        }else{
                            node = isStart ? me.startContainer : me.endContainer;
                            var first = node.firstChild;
                            while(first){
                                if(domUtils.isFillChar(first)){
                                    first = first.nextSibling;
                                    continue;
                                }
                                firstIndex++;
                                if(first.nodeType == 3){
                                    while( first && first.nodeType == 3){
                                        first = first.nextSibling;
                                    }
                                }else{
                                    first = first.nextSibling;
                                }
                            }
                        }
                    }

                }else{
                    firstIndex = isStart ? domUtils.isFillChar(node) ? 0 : me.startOffset  : me.endOffset
                }
                if(firstIndex < 0){
                    firstIndex = 0;
                }
                addrs.push(firstIndex);
                return addrs;
            }
            addr.startAddress = getAddress(true);
            if(!ignoreEnd){
                addr.endAddress = me.collapsed ? [].concat(addr.startAddress) : getAddress();
            }
            return addr;
        },
        moveToAddress : function(addr,ignoreEnd){
            var me = this;
            function getNode(address,isStart){
                var tmpNode = me.body,
                    parentNode,offset;
                for(var i= 0,ci,l=address.length;i<l;i++){
                    ci = address[i];
                    parentNode = tmpNode;
                    tmpNode = tmpNode.childNodes[ci];
                    if(!tmpNode){
                        offset = ci;
                        break;
                    }
                }
                if(isStart){
                    if(tmpNode){
                        me.setStartBefore(tmpNode)
                    }else{
                        me.setStart(parentNode,offset)
                    }
                }else{
                    if(tmpNode){
                        me.setEndBefore(tmpNode)
                    }else{
                        me.setEnd(parentNode,offset)
                    }
                }
            }
            getNode(addr.startAddress,true);
            !ignoreEnd && addr.endAddress &&  getNode(addr.endAddress);
            return me;
        },
        equals : function(rng){
            for(var p in this){
                if(this.hasOwnProperty(p)){
                    if(this[p] !== rng[p])
                        return false
                }
            }
            return true;

        },
        scrollIntoView : function(){
            var $span = $('<span style="padding:0;margin:0;display:block;border:0">&nbsp;</span>');
            this.cloneRange().insertNode($span.get(0));
            var winScrollTop = $(window).scrollTop(),
                winHeight = $(window).height(),
                spanTop = $span.offset().top;
            if(spanTop < winScrollTop-winHeight || spanTop > winScrollTop + winHeight ){
                if(spanTop > winScrollTop + winHeight){
                    window.scrollTo(0,spanTop - winHeight + $span.height())
                }else{
                    window.scrollTo(0,winScrollTop - spanTop)
                }

            }
            $span.remove();
        },
        getOffset : function(){
            var bk = this.createBookmark();
            var offset = $(bk.start).css('display','inline-block').offset();
            this.moveToBookmark(bk);
            return offset
        }
    };
})();
///import editor.js
///import core/browser.js
///import core/dom/dom.js
///import core/dom/dtd.js
///import core/dom/domUtils.js
///import core/dom/Range.js
/**
 * @class UM.dom.Selection    Selection��
 */
(function () {

    function getBoundaryInformation( range, start ) {
        var getIndex = domUtils.getNodeIndex;
        range = range.duplicate();
        range.collapse( start );
        var parent = range.parentElement();
        //���ڵ���û���ӽڵ㣬ֱ���˳�
        if ( !parent.hasChildNodes() ) {
            return  {container:parent, offset:0};
        }
        var siblings = parent.children,
            child,
            testRange = range.duplicate(),
            startIndex = 0, endIndex = siblings.length - 1, index = -1,
            distance;
        while ( startIndex <= endIndex ) {
            index = Math.floor( (startIndex + endIndex) / 2 );
            child = siblings[index];
            testRange.moveToElementText( child );
            var position = testRange.compareEndPoints( 'StartToStart', range );
            if ( position > 0 ) {
                endIndex = index - 1;
            } else if ( position < 0 ) {
                startIndex = index + 1;
            } else {
                //trace:1043
                return  {container:parent, offset:getIndex( child )};
            }
        }
        if ( index == -1 ) {
            testRange.moveToElementText( parent );
            testRange.setEndPoint( 'StartToStart', range );
            distance = testRange.text.replace( /(\r\n|\r)/g, '\n' ).length;
            siblings = parent.childNodes;
            if ( !distance ) {
                child = siblings[siblings.length - 1];
                return  {container:child, offset:child.nodeValue.length};
            }

            var i = siblings.length;
            while ( distance > 0 ){
                distance -= siblings[ --i ].nodeValue.length;
            }
            return {container:siblings[i], offset:-distance};
        }
        testRange.collapse( position > 0 );
        testRange.setEndPoint( position > 0 ? 'StartToStart' : 'EndToStart', range );
        distance = testRange.text.replace( /(\r\n|\r)/g, '\n' ).length;
        if ( !distance ) {
            return  dtd.$empty[child.tagName] || dtd.$nonChild[child.tagName] ?
            {container:parent, offset:getIndex( child ) + (position > 0 ? 0 : 1)} :
            {container:child, offset:position > 0 ? 0 : child.childNodes.length}
        }
        while ( distance > 0 ) {
            try {
                var pre = child;
                child = child[position > 0 ? 'previousSibling' : 'nextSibling'];
                distance -= child.nodeValue.length;
            } catch ( e ) {
                return {container:parent, offset:getIndex( pre )};
            }
        }
        return  {container:child, offset:position > 0 ? -distance : child.nodeValue.length + distance}
    }

    /**
     * ��ieRangeת��ΪRange����
     * @param {Range}   ieRange    ieRange����
     * @param {Range}   range      Range����
     * @return  {Range}  range       ����ת�����Range����
     */
    function transformIERangeToRange( ieRange, range ) {
        if ( ieRange.item ) {
            range.selectNode( ieRange.item( 0 ) );
        } else {
            var bi = getBoundaryInformation( ieRange, true );
            range.setStart( bi.container, bi.offset );
            if ( ieRange.compareEndPoints( 'StartToEnd', ieRange ) != 0 ) {
                bi = getBoundaryInformation( ieRange, false );
                range.setEnd( bi.container, bi.offset );
            }
        }
        return range;
    }

    /**
     * ���ieRange
     * @param {Selection} sel    Selection����
     * @return {ieRange}    �õ�ieRange
     */
    function _getIERange( sel,txtRange ) {
        var ieRange;
        //ie���п��ܱ���
        try {
            ieRange = sel.getNative(txtRange).createRange();
        } catch ( e ) {
            return null;
        }
        var el = ieRange.item ? ieRange.item( 0 ) : ieRange.parentElement();
        if ( ( el.ownerDocument || el ) === sel.document ) {
            return ieRange;
        }
        return null;
    }

    var Selection = dom.Selection = function ( doc,body ) {
        var me = this;
        me.document = doc;
        me.body = body;
        if ( browser.ie9below ) {
            $( body).on('beforedeactivate', function () {
                me._bakIERange = me.getIERange();
            } ).on('activate', function () {
                try {
                    var ieNativRng =  _getIERange( me );
                    if ( (!ieNativRng || !me.rangeInBody(ieNativRng)) && me._bakIERange ) {
                        me._bakIERange.select();
                    }
                } catch ( ex ) {
                }
                me._bakIERange = null;
            } );
        }
    };

    Selection.prototype = {
        hasNativeRange : function(){
            var rng;
            if(!browser.ie || browser.ie9above){
                var nativeSel = this.getNative();
                if(!nativeSel.rangeCount){
                    return false;
                }
                rng = nativeSel.getRangeAt(0);
            }else{
                rng = _getIERange(this);
            }
            return this.rangeInBody(rng);
        },
        /**
         * ��ȡԭ��seleciton����
         * @public
         * @function
         * @name    UM.dom.Selection.getNative
         * @return {Selection}    ���selection����
         */
        getNative:function (txtRange) {
            var doc = this.document;
            try {
                return !doc ? null : browser.ie9below || txtRange? doc.selection : domUtils.getWindow( doc ).getSelection();
            } catch ( e ) {
                return null;
            }
        },
        /**
         * ���ieRange
         * @public
         * @function
         * @name    UM.dom.Selection.getIERange
         * @return {ieRange}    ����ieԭ���Range
         */
        getIERange:function (txtRange) {
            var ieRange = _getIERange( this,txtRange );
            if ( !ieRange  || !this.rangeInBody(ieRange,txtRange)) {
                if ( this._bakIERange ) {
                    return this._bakIERange;
                }
            }
            return ieRange;
        },
        rangeInBody : function(rng,txtRange){
            var node = browser.ie9below || txtRange ? rng.item ? rng.item() : rng.parentElement() : rng.startContainer;

            return node === this.body || domUtils.inDoc(node,this.body);
        },
        /**
         * ���浱ǰѡ���range��ѡ��Ŀ�ʼ�ڵ�
         * @public
         * @function
         * @name    UM.dom.Selection.cache
         */
        cache:function () {
            this.clear();
            this._cachedRange = this.getRange();
            this._cachedStartElement = this.getStart();
            this._cachedStartElementPath = this.getStartElementPath();
        },

        getStartElementPath:function () {
            if ( this._cachedStartElementPath ) {
                return this._cachedStartElementPath;
            }
            var start = this.getStart();
            if ( start ) {
                return domUtils.findParents( start, true, null, true )
            }
            return [];
        },
        /**
         * ��ջ���
         * @public
         * @function
         * @name    UM.dom.Selection.clear
         */
        clear:function () {
            this._cachedStartElementPath = this._cachedRange = this._cachedStartElement = null;
        },
        /**
         * �༭���Ƿ�õ���ѡ��
         */
        isFocus:function () {
            return this.hasNativeRange()

        },
        /**
         * ��ȡѡ���Ӧ��Range
         * @public
         * @function
         * @name    UM.dom.Selection.getRange
         * @returns {UM.dom.Range}    �õ�Range����
         */
        getRange:function () {
            var me = this;
            function optimze( range ) {
                var child = me.body.firstChild,
                    collapsed = range.collapsed;
                while ( child && child.firstChild ) {
                    range.setStart( child, 0 );
                    child = child.firstChild;
                }
                if ( !range.startContainer ) {
                    range.setStart( me.body, 0 )
                }
                if ( collapsed ) {
                    range.collapse( true );
                }
            }

            if ( me._cachedRange != null ) {
                return this._cachedRange;
            }
            var range = new dom.Range( me.document,me.body );
            if ( browser.ie9below ) {
                var nativeRange = me.getIERange();
                if ( nativeRange  && this.rangeInBody(nativeRange)) {

                    try{
                        transformIERangeToRange( nativeRange, range );
                    }catch(e){
                        optimze( range );
                    }

                } else {
                    optimze( range );
                }
            } else {
                var sel = me.getNative();
                if ( sel && sel.rangeCount && me.rangeInBody(sel.getRangeAt( 0 ))) {
                    var firstRange = sel.getRangeAt( 0 );
                    var lastRange = sel.getRangeAt( sel.rangeCount - 1 );
                    range.setStart( firstRange.startContainer, firstRange.startOffset ).setEnd( lastRange.endContainer, lastRange.endOffset );
                    if ( range.collapsed && domUtils.isBody( range.startContainer ) && !range.startOffset ) {
                        optimze( range );
                    }
                } else {
                    //trace:1734 �п����Ѿ�����dom�����ˣ���ʶ�Ľڵ�
                    if ( this._bakRange && (this._bakRange.startContainer === this.body || domUtils.inDoc( this._bakRange.startContainer, this.body )) ){
                        return this._bakRange;
                    }
                    optimze( range );
                }
            }

            return this._bakRange = range;
        },

        /**
         * ��ȡ��ʼԪ�أ�����״̬����
         * @public
         * @function
         * @name    UM.dom.Selection.getStart
         * @return {Element}     ��ÿ�ʼԪ��
         */
        getStart:function () {
            if ( this._cachedStartElement ) {
                return this._cachedStartElement;
            }
            var range = browser.ie9below ? this.getIERange() : this.getRange(),
                tmpRange,
                start, tmp, parent;
            if ( browser.ie9below ) {
                if ( !range ) {
                    //todo ���һ��ֵ���ܻ�������
                    return this.document.body.firstChild;
                }
                //controlԪ��
                if ( range.item ){
                    return range.item( 0 );
                }
                tmpRange = range.duplicate();
                //����ie��<b>x</b>[xx] �պϺ� <b>x|</b>xx
                tmpRange.text.length > 0 && tmpRange.moveStart( 'character', 1 );
                tmpRange.collapse( 1 );
                start = tmpRange.parentElement();
                parent = tmp = range.parentElement();
                while ( tmp = tmp.parentNode ) {
                    if ( tmp == start ) {
                        start = parent;
                        break;
                    }
                }
            } else {
                start = range.startContainer;
                if ( start.nodeType == 1 && start.hasChildNodes() ){
                    start = start.childNodes[Math.min( start.childNodes.length - 1, range.startOffset )];
                }
                if ( start.nodeType == 3 ){
                    return start.parentNode;
                }
            }
            return start;
        },
        /**
         * �õ�ѡ���е��ı�
         * @public
         * @function
         * @name    UM.dom.Selection.getText
         * @return  {String}    ѡ���а���ı�
         */
        getText:function () {
            var nativeSel, nativeRange;
            if ( this.isFocus() && (nativeSel = this.getNative()) ) {
                nativeRange = browser.ie9below ? nativeSel.createRange() : nativeSel.getRangeAt( 0 );
                return browser.ie9below ? nativeRange.text : nativeRange.toString();
            }
            return '';
        }
    };
})();
/**
 * @file
 * @name UM.Editor
 * @short Editor
 * @import editor.js,core/utils.js,core/EventBase.js,core/browser.js,core/dom/dtd.js,core/dom/domUtils.js,core/dom/Range.js,core/dom/Selection.js,plugins/serialize.js
 * @desc �༭�����࣬��༭���ṩ�Ĵ󲿷ֹ��ýӿ�
 */
(function () {
    var uid = 0, _selectionChangeTimer;

    /**
     * @private
     * @ignore
     * @param form  �༭�����ڵ�formԪ��
     * @param editor  �༭��ʵ�����
     */
    function setValue(form, editor) {
        var textarea;
        if (editor.textarea) {
            if (utils.isString(editor.textarea)) {
                for (var i = 0, ti, tis = domUtils.getElementsByTagName(form, 'textarea'); ti = tis[i++];) {
                    if (ti.id == 'umeditor_textarea_' + editor.options.textarea) {
                        textarea = ti;
                        break;
                    }
                }
            } else {
                textarea = editor.textarea;
            }
        }
        if (!textarea) {
            form.appendChild(textarea = domUtils.createElement(document, 'textarea', {
                'name': editor.options.textarea,
                'id': 'umeditor_textarea_' + editor.options.textarea,
                'style': "display:none"
            }));
            //��Ҫ������textarea
            editor.textarea = textarea;
        }
        textarea.value = editor.hasContents() ?
            (editor.options.allHtmlEnabled ? editor.getAllHtml() : editor.getContent(null, null, true)) :
            ''
    }
    function loadPlugins(me){
        //��ʼ�����
        for (var pi in UM.plugins) {
            if(me.options.excludePlugins.indexOf(pi) == -1){
                UM.plugins[pi].call(me);
                me.plugins[pi] = 1;
            }
        }
        me.langIsReady = true;

        me.fireEvent("langReady");
    }
    function checkCurLang(I18N){
        for(var lang in I18N){
            return lang
        }
    }
    /**
     * UEditor�༭����
     * @name Editor
     * @desc ����һ����༭��ʵ��
     * - ***container*** �༭����������
     * - ***iframe*** �༭�������ڵ�iframe����
     * - ***window*** �༭�������ڵ�window
     * - ***document*** �༭�������ڵ�document����
     * - ***body*** �༭�������ڵ�body����
     * - ***selection*** �༭�����ѡ�����
     */
    var Editor = UM.Editor = function (options) {
        var me = this;
        me.uid = uid++;
        EventBase.call(me);
        me.commands = {};
        me.options = utils.extend(utils.clone(options || {}), UMEDITOR_CONFIG, true);
        me.shortcutkeys = {};
        me.inputRules = [];
        me.outputRules = [];
        //����Ĭ�ϵĳ�������
        me.setOpt({
            isShow: true,
            initialContent: '',
            initialStyle:'',
            autoClearinitialContent: false,
            textarea: 'editorValue',
            focus: false,
            focusInEnd: true,
            autoClearEmptyNode: true,
            fullscreen: false,
            readonly: false,
            zIndex: 999,
            enterTag: 'p',
            lang: 'zh-cn',
            langPath: me.options.UMEDITOR_HOME_URL + 'lang/',
            theme: 'default',
            themePath: me.options.UMEDITOR_HOME_URL + 'themes/',
            allHtmlEnabled: false,
            autoSyncData : true,
            autoHeightEnabled : true,
            excludePlugins:''
        });
        me.plugins = {};
        if(!utils.isEmptyObject(UM.I18N)){
            //�޸�Ĭ�ϵ���������
            me.options.lang = checkCurLang(UM.I18N);
            loadPlugins(me)
        }else{
            utils.loadFile(document, {
                src: me.options.langPath + me.options.lang + "/" + me.options.lang + ".js",
                tag: "script",
                type: "text/javascript",
                defer: "defer"
            }, function () {
                loadPlugins(me)
            });
        }

    };
    Editor.prototype = {
        /**
         * ���༭��ready��ִ�д����fn,���༭���Ѿ����ready��������ִ��fn��fn���е�this�Ǳ༭��ʵ��
         * �󲿷ֵ�ʵ��ӿڶ���Ҫ���ڸ÷����ڲ�ִ�У�������IE�¿��ܻᱨ�?
         * @name ready
         * @grammar editor.ready(fn) fn�ǵ��༭����Ⱦ�ú�ִ�е�function
         * @example
         * var editor = new UM.ui.Editor();
         * editor.render("myEditor");
         * editor.ready(function(){
         *     editor.setContent("��ӭʹ��UEditor��");
         * })
         */
        ready: function (fn) {
            var me = this;
            if (fn) {
                me.isReady ? fn.apply(me) : me.addListener('ready', fn);
            }
        },
        /**
         * Ϊ�༭������Ĭ�ϲ���ֵ�����û�����Ϊ�գ�����Ĭ������Ϊ׼
         * @grammar editor.setOpt(key,value);      //����һ����ֵ��
         * @grammar editor.setOpt({ key:value});   //����һ��json����
         */
        setOpt: function (key, val) {
            var obj = {};
            if (utils.isString(key)) {
                obj[key] = val
            } else {
                obj = key;
            }
            utils.extend(this.options, obj, true);
        },
        getOpt:function(key){
            return this.options[key] || ''
        },
        /**
         * ��ٱ༭��ʵ�����
         * @name destroy
         * @grammar editor.destroy();
         */
        destroy: function () {

            var me = this;
            me.fireEvent('destroy');
            var container = me.container.parentNode;
            if(container === document.body){
                container = me.container;
            }
            var textarea = me.textarea;
            if (!textarea) {
                textarea = document.createElement('textarea');
                container.parentNode.insertBefore(textarea, container);
            } else {
                textarea.style.display = ''
            }

            textarea.style.width = me.body.offsetWidth + 'px';
            textarea.style.height = me.body.offsetHeight + 'px';
            textarea.value = me.getContent();
            textarea.id = me.key;
            if(container.contains(textarea)){
                $(textarea).insertBefore(container);
            }
            container.innerHTML = '';

            domUtils.remove(container);
            UM.clearCache(me.id);
            //trace:2004
            for (var p in me) {
                if (me.hasOwnProperty(p)) {
                    delete this[p];
                }
            }

        },
        initialCont : function(holder){

            if(holder){
                holder.getAttribute('name') && ( this.options.textarea = holder.getAttribute('name'));
                if (holder && /script|textarea/ig.test(holder.tagName)) {
                    var newDiv = document.createElement('div');
                    holder.parentNode.insertBefore(newDiv, holder);
                    this.options.initialContent = UM.htmlparser(holder.value || holder.innerHTML|| this.options.initialContent).toHtml();
                    holder.className && (newDiv.className = holder.className);
                    holder.style.cssText && (newDiv.style.cssText = holder.style.cssText);

                    if (/textarea/i.test(holder.tagName)) {
                        this.textarea = holder;
                        this.textarea.style.display = 'none';

                    } else {
                        holder.parentNode.removeChild(holder);
                        holder.id && (newDiv.id = holder.id);
                    }
                    holder = newDiv;
                    holder.innerHTML = '';
                }
                return holder;
            }else{
                return null;
            }

        },
        /**
         * ��Ⱦ�༭����DOM��ָ��������������ֻ�ܵ���һ��
         * @name render
         * @grammar editor.render(containerId);    //����ָ��һ������ID
         * @grammar editor.render(containerDom);   //Ҳ����ֱ��ָ����������
         */
        render: function (container) {
            var me = this,
                options = me.options,
                getStyleValue=function(attr){
                    return parseInt($(container).css(attr));
                };

            if (utils.isString(container)) {
                container = document.getElementById(container);
            }
            if (container) {
                this.id = container.getAttribute('id');
                UM.setEditor(this);
                utils.cssRule('edui-style-body',me.options.initialStyle,document);

                container = this.initialCont(container);

                container.className += ' edui-body-container';

                if(options.initialFrameWidth){
                    options.minFrameWidth = options.initialFrameWidth
                }else{
                    //��û��ֵ����д����
                    options.minFrameWidth = options.initialFrameWidth = $(container).width() || UM.defaultWidth;
                }
                if(options.initialFrameHeight){
                    options.minFrameHeight = options.initialFrameHeight
                }else{

                    options.initialFrameHeight = options.minFrameHeight = $(container).height() || UM.defaultHeight;
                }

                container.style.width = /%$/.test(options.initialFrameWidth) ?  '100%' : options.initialFrameWidth -
                    getStyleValue("padding-left")-
                    getStyleValue("padding-right")  +'px';

                var height = /%$/.test(options.initialFrameHeight) ?  '100%' : (options.initialFrameHeight - getStyleValue("padding-top")- getStyleValue("padding-bottom") );
                if(this.options.autoHeightEnabled){
                    container.style.minHeight = height +'px';
                    container.style.height = '';
                    if(browser.ie && browser.version <= 6){
                        container.style.height = height ;
                        container.style.setExpression('height', 'this.scrollHeight <= ' + height + ' ? "' + height + 'px" : "auto"');
                    }
                }else{
                    $(container).height(height)
                }
                container.style.zIndex = options.zIndex;
                this._setup(container);

            }
        },
        /**
         * �༭����ʼ��
         * @private
         * @ignore
         * @param {Element} doc �༭��Iframe�е��ĵ�����
         */
        _setup: function (cont) {
            var me = this,
                options = me.options;

            cont.contentEditable = true;
            document.body.spellcheck = false;

            me.document = document;
            me.window = document.defaultView || document.parentWindow;
            me.body = cont;
            me.$body = $(cont);
            me.selection = new dom.Selection(document,me.body);
            me._isEnabled = false;
            //gecko��ʼ�����ܵõ�range,�޷��ж�isFocus��
            var geckoSel;
            if (browser.gecko && (geckoSel = this.selection.getNative())) {
                geckoSel.removeAllRanges();
            }
            this._initEvents();
            //Ϊform�ύ�ṩһ�����ص�textarea
            for (var form = cont.parentNode; form && !domUtils.isBody(form); form = form.parentNode) {
                if (form.tagName == 'FORM') {
                    me.form = form;
                    if(me.options.autoSyncData){
                        $(cont).on('blur',function(){
                            setValue(form,me);
                        })
                    }else{
                        $(form).on('submit', function () {
                            setValue(this, me);
                        })
                    }
                    break;
                }
            }
            if (options.initialContent) {
                if (options.autoClearinitialContent) {
                    var oldExecCommand = me.execCommand;
                    me.execCommand = function () {
                        me.fireEvent('firstBeforeExecCommand');
                        return oldExecCommand.apply(me, arguments);
                    };
                    this._setDefaultContent(options.initialContent);
                } else
                    this.setContent(options.initialContent, false, true);
            }

            //�༭������Ϊ������

            if (domUtils.isEmptyNode(me.body)) {
                me.body.innerHTML = '<p>' + (browser.ie ? '' : '<br/>') + '</p>';
            }
            //���Ҫ��focus, �Ͱѹ�궨λ�����ݿ�ʼ
            if (options.focus) {
                setTimeout(function () {
                    me.focus(me.options.focusInEnd);
                    //����Զ�����ţ��Ͳ���Ҫ��selectionchange;
                    !me.options.autoClearinitialContent && me._selectionChange();
                }, 0);
            }
            if (!me.container) {
                me.container = cont.parentNode;
            }

            me._bindshortcutKeys();
            me.isReady = 1;
            me.fireEvent('ready');
            options.onready && options.onready.call(me);
            if(!browser.ie || browser.ie9above){

                $(me.body).on( 'blur focus', function (e) {
                    var nSel = me.selection.getNative();
                    //chrome�»����alt+tab�л�ʱ������ѡ��λ�ò���
                    if (e.type == 'blur') {
                        if(nSel.rangeCount > 0 ){
                            me._bakRange = nSel.getRangeAt(0);
                        }
                    } else {
                        try {
                            me._bakRange && nSel.addRange(me._bakRange)
                        } catch (e) {
                        }
                        me._bakRange = null;
                    }
                });
            }

            !options.isShow && me.setHide();
            options.readonly && me.setDisabled();
        },
        /**
         * ͬ���༭������ݣ�Ϊ�ύ�����׼������Ҫ���������ֶ��ύ�����
         * @name sync
         * @grammar editor.sync(); //�ӱ༭�����������ϲ��ң�����ҵ���ͬ�����
         * @grammar editor.sync(formID); //formID�ƶ�һ��Ҫͬ����ݵ�form��id,�༭������ݻ�ͬ������ָ��form��
         * @desc
         * ��̨ȡ����ݵü�ֵʹ���������ϵ�''name''���ԣ����û�о�ʹ�ò������''textarea''
         * @example
         * editor.sync();
         * form.sumbit(); //form�����Ѿ�ָ����formԪ��
         *
         */
        sync: function (formId) {
            var me = this,
                form = formId ? document.getElementById(formId) :
                    domUtils.findParent(me.body.parentNode, function (node) {
                        return node.tagName == 'FORM'
                    }, true);
            form && setValue(form, me);
        },
        /**
         * ���ñ༭���߶�
         * @name setHeight
         * @grammar editor.setHeight(number);  //����ֵ������λ
         */
        setHeight: function (height,notSetHeight) {
            !notSetHeight && (this.options.initialFrameHeight = height);
            if(this.options.autoHeightEnabled){
                $(this.body).css({
                    'min-height':height + 'px'
                });
                if(browser.ie && browser.version <= 6 && this.container){
                    this.container.style.height = height ;
                    this.container.style.setExpression('height', 'this.scrollHeight <= ' + height + ' ? "' + height + 'px" : "auto"');
                }
            }else{
                $(this.body).height(height)
            }
            this.fireEvent('resize');
        },
        /**
         * ���ñ༭�����
         * @name setWidth
         * @grammar editor.setWidth(number);  //����ֵ������λ
         */
        setWidth:function(width){
            this.$container && this.$container.width(width);
            $(this.body).width(width - $(this.body).css('padding-left').replace('px','') * 1 - $(this.body).css('padding-right').replace('px','') * 1);
            this.fireEvent('resize');
        },
        addshortcutkey: function (cmd, keys) {
            var obj = {};
            if (keys) {
                obj[cmd] = keys
            } else {
                obj = cmd;
            }
            utils.extend(this.shortcutkeys, obj)
        },
        _bindshortcutKeys: function () {
            var me = this, shortcutkeys = this.shortcutkeys;
            me.addListener('keydown', function (type, e) {
                var keyCode = e.keyCode || e.which;
                for (var i in shortcutkeys) {
                    var tmp = shortcutkeys[i].split(',');
                    for (var t = 0, ti; ti = tmp[t++];) {
                        ti = ti.split(':');
                        var key = ti[0], param = ti[1];
                        if (/^(ctrl)(\+shift)?\+(\d+)$/.test(key.toLowerCase()) || /^(\d+)$/.test(key)) {
                            if (( (RegExp.$1 == 'ctrl' ? (e.ctrlKey || e.metaKey) : 0)
                                && (RegExp.$2 != "" ? e[RegExp.$2.slice(1) + "Key"] : 1)
                                && keyCode == RegExp.$3
                                ) ||
                                keyCode == RegExp.$1
                                ) {
                                if (me.queryCommandState(i,param) != -1)
                                    me.execCommand(i, param);
                                domUtils.preventDefault(e);
                            }
                        }
                    }

                }
            });
        },
        /**
         * ��ȡ�༭������
         * @name getContent
         * @grammar editor.getContent()  => String //���༭����ֻ���ַ�"&lt;p&gt;&lt;br /&gt;&lt;/p/&gt;"�᷵�ؿա�
         * @grammar editor.getContent(fn)  => String
         * @example
         * getContentĬ���ǻ��ֵ���hasContents���жϱ༭���Ƿ�Ϊ�գ�����ǣ���ֱ�ӷ��ؿ��ַ�
         * ��Ҳ���Դ���һ��fn������hasContents�Ĺ����������жϵĹ���
         * editor.getContent(function(){
         *     return false //�༭��û������ ��getContentֱ�ӷ��ؿ�
         * })
         */
        getContent: function (cmd, fn,notSetCursor,ignoreBlank,formatter) {
            var me = this;
            if (cmd && utils.isFunction(cmd)) {
                fn = cmd;
                cmd = '';
            }
            if (fn ? !fn() : !this.hasContents()) {
                return '';
            }
            me.fireEvent('beforegetcontent');
            var root = UM.htmlparser(me.body.innerHTML,ignoreBlank);
            me.filterOutputRule(root);
            me.fireEvent('aftergetcontent',root);
            return  root.toHtml(formatter);
        },
        /**
         * ȡ�������html���룬����ֱ����ʾ�������html�ĵ�
         * @name getAllHtml
         * @grammar editor.getAllHtml()  => String
         */
        getAllHtml: function () {
            var me = this,
                headHtml = [],
                html = '';
            me.fireEvent('getAllHtml', headHtml);
            if (browser.ie && browser.version > 8) {
                var headHtmlForIE9 = '';
                utils.each(me.document.styleSheets, function (si) {
                    headHtmlForIE9 += ( si.href ? '<link rel="stylesheet" type="text/css" href="' + si.href + '" />' : '<style>' + si.cssText + '</style>');
                });
                utils.each(me.document.getElementsByTagName('script'), function (si) {
                    headHtmlForIE9 += si.outerHTML;
                });
            }
            return '<html><head>' + (me.options.charset ? '<meta http-equiv="Content-Type" content="text/html; charset=' + me.options.charset + '"/>' : '')
                + (headHtmlForIE9 || me.document.getElementsByTagName('head')[0].innerHTML) + headHtml.join('\n') + '</head>'
                + '<body ' + (ie && browser.version < 9 ? 'class="view"' : '') + '>' + me.getContent(null, null, true) + '</body></html>';
        },
        /**
         * �õ��༭���Ĵ��ı����ݣ����ᱣ�������ʽ
         * @name getPlainTxt
         * @grammar editor.getPlainTxt()  => String
         */
        getPlainTxt: function () {
            var reg = new RegExp(domUtils.fillChar, 'g'),
                html = this.body.innerHTML.replace(/[\n\r]/g, '');//ieҪ��ȥ��\n�ڴ���
            html = html.replace(/<(p|div)[^>]*>(<br\/?>|&nbsp;)<\/\1>/gi, '\n')
                .replace(/<br\/?>/gi, '\n')
                .replace(/<[^>/]+>/g, '')
                .replace(/(\n)?<\/([^>]+)>/g, function (a, b, c) {
                    return dtd.$block[c] ? '\n' : b ? b : '';
                });
            //ȡ�����Ŀո����c2a0�������룬�����������\u00a0
            return html.replace(reg, '').replace(/\u00a0/g, ' ').replace(/&nbsp;/g, ' ');
        },

        /**
         * ��ȡ�༭���еĴ��ı�����,û�ж����ʽ
         * @name getContentTxt
         * @grammar editor.getContentTxt()  => String
         */
        getContentTxt: function () {
            var reg = new RegExp(domUtils.fillChar, 'g');
            //ȡ�����Ŀո����c2a0�������룬�����������\u00a0
            return this.body[browser.ie ? 'innerText' : 'textContent'].replace(reg, '').replace(/\u00a0/g, ' ');
        },

        /**
         * ��html���õ��༭����, ��������ڳ�ʼ��ʱ��༭������ֵ����������ready�����ڲ�ִ��
         * @name setContent
         * @grammar editor.setContent(html)
         * @example
         * var editor = new UM.ui.Editor()
         * editor.ready(function(){
         *     //��Ҫready��ִ�У�������ܱ���
         *     editor.setContent("��ӭʹ��UEditor��");
         * })
         */
        setContent: function (html, isAppendTo, notFireSelectionchange) {
            var me = this;

            me.fireEvent('beforesetcontent', html);
            var root = UM.htmlparser(html);
            me.filterInputRule(root);
            html = root.toHtml();


            me.body.innerHTML = (isAppendTo ? me.body.innerHTML : '') + html;


            function isCdataDiv(node){
                return  node.tagName == 'DIV' && node.getAttribute('cdata_tag');
            }
            //���ı�����inline�ڵ���p��ǩ
            if (me.options.enterTag == 'p') {

                var child = this.body.firstChild, tmpNode;
                if (!child || child.nodeType == 1 &&
                    (dtd.$cdata[child.tagName] || isCdataDiv(child) ||
                        domUtils.isCustomeNode(child)
                        )
                    && child === this.body.lastChild) {
                    this.body.innerHTML = '<p>' + (browser.ie ? '&nbsp;' : '<br/>') + '</p>' + this.body.innerHTML;

                } else {
                    var p = me.document.createElement('p');
                    while (child) {
                        while (child && (child.nodeType == 3 || child.nodeType == 1 && dtd.p[child.tagName] && !dtd.$cdata[child.tagName])) {
                            tmpNode = child.nextSibling;
                            p.appendChild(child);
                            child = tmpNode;
                        }
                        if (p.firstChild) {
                            if (!child) {
                                me.body.appendChild(p);
                                break;
                            } else {
                                child.parentNode.insertBefore(p, child);
                                p = me.document.createElement('p');
                            }
                        }
                        child = child.nextSibling;
                    }
                }
            }
            me.fireEvent('aftersetcontent');
            me.fireEvent('contentchange');

            !notFireSelectionchange && me._selectionChange();
            //�����ѡ��
            me._bakRange = me._bakIERange = me._bakNativeRange = null;
            //trace:1742 setContent��gecko�ܵõ���������
            var geckoSel;
            if (browser.gecko && (geckoSel = this.selection.getNative())) {
                geckoSel.removeAllRanges();
            }
            if(me.options.autoSyncData){
                me.form && setValue(me.form,me);
            }
        },

        /**
         * �ñ༭����ý��㣬toEndȷ��focusλ��
         * @name focus
         * @grammar editor.focus([toEnd])   //Ĭ��focus���༭��ͷ����toEndΪtrueʱfocus������β��
         */
        focus: function (toEnd) {
            try {
                var me = this,
                    rng = me.selection.getRange();
                if (toEnd) {
                    rng.setStartAtLast(me.body.lastChild).setCursor(false, true);
                } else {
                    rng.select(true);
                }
                this.fireEvent('focus');
            } catch (e) {
            }
        },
        /**
         * ʹ�༭����ʧȥ����
         */
        blur:function(){
            var sel = this.selection.getNative();
            sel.empty ? sel.empty() : sel.removeAllRanges();
            this.fireEvent('blur')
        },
        /**
         * �жϱ༭����ǰ�Ƿ����˽���
         */
        isFocus : function(){
            if(this.fireEvent('isfocus')===true){
                return true;
            }
            return this.selection.isFocus();
        },

        /**
         * ��ʼ��UE�¼��������¼�����
         * @private
         * @ignore
         */
        _initEvents: function () {
            var me = this,
                cont = me.body,
                _proxyDomEvent = function(){
                    me._proxyDomEvent.apply(me, arguments);
                };

            $(cont)
                .on( 'click contextmenu mousedown keydown keyup keypress mouseup mouseover mouseout selectstart', _proxyDomEvent)
                .on( 'focus blur', _proxyDomEvent)
                .on('mouseup keydown', function (evt) {
                    //������selectionchange
                    if (evt.type == 'keydown' && (evt.ctrlKey || evt.metaKey || evt.shiftKey || evt.altKey)) {
                        return;
                    }
                    if (evt.button == 2)return;
                    me._selectionChange(250, evt);
                });
        },
        /**
         * �����¼�����
         * @private
         * @ignore
         */
        _proxyDomEvent: function (evt) {
            return this.fireEvent(evt.type.replace(/^on/, ''), evt);
        },
        /**
         * �仯ѡ��
         * @private
         * @ignore
         */
        _selectionChange: function (delay, evt) {
            var me = this;
            //�й�����selectionchange Ϊ�˽��δfocusʱ���source���ܴ�����Ĺ�����״̬�����⣨source����notNeedUndo=1��
//            if ( !me.selection.isFocus() ){
//                return;
//            }


            var hackForMouseUp = false;
            var mouseX, mouseY;
            if (browser.ie && browser.version < 9 && evt && evt.type == 'mouseup') {
                var range = this.selection.getRange();
                if (!range.collapsed) {
                    hackForMouseUp = true;
                    mouseX = evt.clientX;
                    mouseY = evt.clientY;
                }
            }
            clearTimeout(_selectionChangeTimer);
            _selectionChangeTimer = setTimeout(function () {
                if (!me.selection.getNative()) {
                    return;
                }
                //�޸�һ��IE�µ�bug: �����һ����ѡ����ı��м�ʱ��������mouseup���һ��ʱ����ȡ����range����selection��typeΪNone�µĴ���ֵ.
                //IE������û�����קһ����ѡ���ı����򲻻ᴥ��mouseup�¼���������������⴦�?�������Ӱ��
                var ieRange;
                if (hackForMouseUp && me.selection.getNative().type == 'None') {
                    ieRange = me.document.body.createTextRange();
                    try {
                        ieRange.moveToPoint(mouseX, mouseY);
                    } catch (ex) {
                        ieRange = null;
                    }
                }
                var bakGetIERange;
                if (ieRange) {
                    bakGetIERange = me.selection.getIERange;
                    me.selection.getIERange = function () {
                        return ieRange;
                    };
                }
                me.selection.cache();
                if (bakGetIERange) {
                    me.selection.getIERange = bakGetIERange;
                }
                if (me.selection._cachedRange && me.selection._cachedStartElement) {
                    me.fireEvent('beforeselectionchange');
                    // �ڶ�������causeByUiΪtrue������û�������ɵ�selectionchange.
                    me.fireEvent('selectionchange', !!evt);
                    me.fireEvent('afterselectionchange');
                    me.selection.clear();
                }
            }, delay || 50);
        },
        _callCmdFn: function (fnName, args) {
            args = Array.prototype.slice.call(args,0);
            var cmdName = args.shift().toLowerCase(),
                cmd, cmdFn;
            cmd = this.commands[cmdName] || UM.commands[cmdName];
            cmdFn = cmd && cmd[fnName];
            //û��querycommandstate����û��command�Ķ�Ĭ�Ϸ���0
            if ((!cmd || !cmdFn) && fnName == 'queryCommandState') {
                return 0;
            } else if (cmdFn) {
                return cmdFn.apply(this, [cmdName].concat(args));
            }
        },

        /**
         * ִ�б༭����cmdName����ɸ��ı��༭Ч��
         * @name execCommand
         * @grammar editor.execCommand(cmdName)   => {*}
         */
        execCommand: function (cmdName) {
            if(!this.isFocus()){
                var bakRange = this.selection._bakRange;
                if(bakRange){
                    bakRange.select()
                }else{
                    this.focus(true)
                }

            }
            cmdName = cmdName.toLowerCase();
            var me = this,
                result,
                cmd = me.commands[cmdName] || UM.commands[cmdName];
            if (!cmd || !cmd.execCommand) {
                return null;
            }
            if (!cmd.notNeedUndo && !me.__hasEnterExecCommand) {
                me.__hasEnterExecCommand = true;
                if (me.queryCommandState.apply(me,arguments) != -1) {
                    me.fireEvent('saveScene');
                    me.fireEvent('beforeexeccommand', cmdName);
                    result = this._callCmdFn('execCommand', arguments);
                    (!cmd.ignoreContentChange && !me._ignoreContentChange) && me.fireEvent('contentchange');
                    me.fireEvent('afterexeccommand', cmdName);
                    me.fireEvent('saveScene');
                }
                me.__hasEnterExecCommand = false;
            } else {
                result = this._callCmdFn('execCommand', arguments);
                (!me.__hasEnterExecCommand && !cmd.ignoreContentChange && !me._ignoreContentChange) && me.fireEvent('contentchange')
            }
            (!me.__hasEnterExecCommand && !cmd.ignoreContentChange && !me._ignoreContentChange) && me._selectionChange();
            return result;
        },
        /**
         * ��ݴ����command�����ѡ�༭����ǰ��ѡ���������״̬
         * @name  queryCommandState
         * @grammar editor.queryCommandState(cmdName)  => (-1|0|1)
         * @desc
         * * ''-1'' ��ǰ�������
         * * ''0'' ��ǰ�������
         * * ''1'' ��ǰ�����Ѿ�ִ�й���
         */
        queryCommandState: function (cmdName) {
            try{
                return this._callCmdFn('queryCommandState', arguments);
            }catch(e){
                return 0
            }

        },

        /**
         * ��ݴ����command�����ѡ�༭����ǰ��ѡ�����������ص�ֵ
         * @name  queryCommandValue
         * @grammar editor.queryCommandValue(cmdName)  =>  {*}
         */
        queryCommandValue: function (cmdName) {
            try{
                return this._callCmdFn('queryCommandValue', arguments);
            }catch(e){
                return null
            }
        },
        /**
         * ���༭�������Ƿ������ݣ�����tags�еĽڵ����ͣ�ֱ�ӷ���true
         * @name  hasContents
         * @desc
         * Ĭ�����ı����ݣ����������½ڵ㶼����Ϊ�ǿ�
         * <code>{table:1,ul:1,ol:1,dl:1,iframe:1,area:1,base:1,col:1,hr:1,img:1,embed:1,input:1,link:1,meta:1,param:1}</code>
         * @grammar editor.hasContents()  => (true|false)
         * @grammar editor.hasContents(tags)  =>  (true|false)  //���ĵ��а�tags�������Ӧ��tag��ֱ�ӷ���true
         * @example
         * editor.hasContents(['span']) //���༭��������Щ������Ϊ�ǿ�
         */
        hasContents: function (tags) {
            if (tags) {
                for (var i = 0, ci; ci = tags[i++];) {
                    if (this.body.getElementsByTagName(ci).length > 0) {
                        return true;
                    }
                }
            }
            if (!domUtils.isEmptyBlock(this.body)) {
                return true
            }
            //��ʱ���,����������ǩ�����ڣ�������Ϊ�ǿ�
            tags = ['div'];
            for (i = 0; ci = tags[i++];) {
                var nodes = domUtils.getElementsByTagName(this.body, ci);
                for (var n = 0, cn; cn = nodes[n++];) {
                    if (domUtils.isCustomeNode(cn)) {
                        return true;
                    }
                }
            }
            return false;
        },
        /**
         * ���ñ༭���������������tabʹ��ͬһ���༭��ʵ��
         * @name  reset
         * @desc
         * * ��ձ༭������
         * * ��ջ����б�
         * @grammar editor.reset()
         */
        reset: function () {
            this.fireEvent('reset');
        },
        isEnabled: function(){
            return this._isEnabled != true;
        },

        setEnabled: function () {
            var me = this, range;

            me.body.contentEditable = true;

            /* �ָ�ѡ�� */
            if (me.lastBk) {
                range = me.selection.getRange();
                try {
                    range.moveToBookmark(me.lastBk);
                    delete me.lastBk
                } catch (e) {
                    range.setStartAtFirst(me.body).collapse(true)
                }
                range.select(true);
            }

            /* �ָ�query���� */
            if (me.bkqueryCommandState) {
                me.queryCommandState = me.bkqueryCommandState;
                delete me.bkqueryCommandState;
            }

            /* �ָ�ԭ���¼� */
            if (me._bkproxyDomEvent) {
                me._proxyDomEvent = me._bkproxyDomEvent;
                delete me._bkproxyDomEvent;
            }

            /* �����¼� */
            me.fireEvent('setEnabled');
        },
        /**
         * ���õ�ǰ�༭������Ա༭
         * @name enable
         * @grammar editor.enable()
         */
        enable: function () {
            return this.setEnabled();
        },
        setDisabled: function (except, keepDomEvent) {
            var me = this;

            me.body.contentEditable = false;
            me._except = except ? utils.isArray(except) ? except : [except] : [];

            /* ��������ѡ�� */
            if (!me.lastBk) {
                me.lastBk = me.selection.getRange().createBookmark(true);
            }

            /* ���ݲ�����query���� */
            if(!me.bkqueryCommandState) {
                me.bkqueryCommandState = me.queryCommandState;
                me.queryCommandState = function (type) {
                    if (utils.indexOf(me._except, type) != -1) {
                        return me.bkqueryCommandState.apply(me, arguments);
                    }
                    return -1;
                };
            }

            /* ���ݲ�ǽԭ���¼� */
            if(!keepDomEvent && !me._bkproxyDomEvent) {
                me._bkproxyDomEvent = me._proxyDomEvent;
                me._proxyDomEvent = function () {
                    return false;
                };
            }

            /* �����¼� */
            me.fireEvent('selectionchange');
            me.fireEvent('setDisabled', me._except);
        },
        /** ���õ�ǰ�༭���򲻿ɱ༭,except�е��������
         * @name disable
         * @grammar editor.disable()
         * @grammar editor.disable(except)  //��������Ҳ����ʹ������disable���˴����õ�������Ȼ����ִ��
         * @example
         * //���ù������г�ӴֺͲ���ͼƬ֮������й���
         * editor.disable(['bold','insertimage']);//�����ǵ�һ��String,Ҳ������Array
         */
        disable: function (except) {
            return this.setDisabled(except);
        },
        /**
         * ����Ĭ������
         * @ignore
         * @private
         * @param  {String} cont Ҫ���������
         */
        _setDefaultContent: function () {
            function clear() {
                var me = this;
                if (me.document.getElementById('initContent')) {
                    me.body.innerHTML = '<p>' + (ie ? '' : '<br/>') + '</p>';
                    me.removeListener('firstBeforeExecCommand focus', clear);
                    setTimeout(function () {
                        me.focus();
                        me._selectionChange();
                    }, 0)
                }
            }

            return function (cont) {
                var me = this;
                me.body.innerHTML = '<p id="initContent">' + cont + '</p>';

                me.addListener('firstBeforeExecCommand focus', clear);
            }
        }(),
        /**
         * show�����ļ��ݰ汾
         * @private
         * @ignore
         */
        setShow: function () {
            var me = this, range = me.selection.getRange();
            if (me.container.style.display == 'none') {
                //�п������ݶ�ʧ��
                try {
                    range.moveToBookmark(me.lastBk);
                    delete me.lastBk
                } catch (e) {
                    range.setStartAtFirst(me.body).collapse(true)
                }
                //ie��focusʵЧ���������˸��ӳ�
                setTimeout(function () {
                    range.select(true);
                }, 100);
                me.container.style.display = '';
            }

        },
        /**
         * ��ʾ�༭��
         * @name show
         * @grammar editor.show()
         */
        show: function () {
            return this.setShow();
        },
        /**
         * hide�����ļ��ݰ汾
         * @private
         * @ignore
         */
        setHide: function () {
            var me = this;
            if (!me.lastBk) {
                me.lastBk = me.selection.getRange().createBookmark(true);
            }
            me.container.style.display = 'none'
        },
        /**
         * ���ر༭��
         * @name hide
         * @grammar editor.hide()
         */
        hide: function () {
            return this.setHide();
        },
        /**
         * ����ƶ���·������ȡ��Ӧ��������Դ
         * @name  getLang
         * @grammar editor.getLang(path)  =>  ��JSON|String) ·����ݵ���langĿ¼�µ������ļ���·���ṹ
         * @example
         * editor.getLang('contextMenu.delete') //���ǰ�����ģ��Ƿ����ǵ���ɾ��
         */
        getLang: function (path) {
            var lang = UM.I18N[this.options.lang];
            if (!lang) {
                throw Error("not import language file");
            }
            path = (path || "").split(".");
            for (var i = 0, ci; ci = path[i++];) {
                lang = lang[ci];
                if (!lang)break;
            }
            return lang;
        },
        /**
         * ����༭����ǰ���ݵĳ���
         * @name  getContentLength
         * @grammar editor.getContentLength(ingoneHtml,tagNames)  =>
         * @example
         * editor.getLang(true)
         */
        getContentLength: function (ingoneHtml, tagNames) {
            var count = this.getContent(false,false,true).length;
            if (ingoneHtml) {
                tagNames = (tagNames || []).concat([ 'hr', 'img', 'iframe']);
                count = this.getContentTxt().replace(/[\t\r\n]+/g, '').length;
                for (var i = 0, ci; ci = tagNames[i++];) {
                    count += this.body.getElementsByTagName(ci).length;
                }
            }
            return count;
        },
        addInputRule: function (rule,ignoreUndo) {
            rule.ignoreUndo = ignoreUndo;
            this.inputRules.push(rule);
        },
        filterInputRule: function (root,isUndoLoad) {
            for (var i = 0, ci; ci = this.inputRules[i++];) {
                if(isUndoLoad && ci.ignoreUndo){
                    continue;
                }
                ci.call(this, root)
            }
        },
        addOutputRule: function (rule,ignoreUndo) {
            rule.ignoreUndo = ignoreUndo;
            this.outputRules.push(rule)
        },
        filterOutputRule: function (root,isUndoLoad) {
            for (var i = 0, ci; ci = this.outputRules[i++];) {
                if(isUndoLoad && ci.ignoreUndo){
                    continue;
                }
                ci.call(this, root)
            }
        }
    };
    utils.inherits(Editor, EventBase);
})();

/**
 * @file
 * @name UM.filterWord
 * @short filterWord
 * @desc ��������wordճ��������ַ�
 * @import editor.js,core/utils.js
 * @anthor zhanyi
 */
var filterWord = UM.filterWord = function () {

    //�Ƿ���word����������
    function isWordDocument( str ) {
        return /(class="?Mso|style="[^"]*\bmso\-|w:WordDocument|<(v|o):|lang=)/ig.test( str );
    }
    //ȥ��С��
    function transUnit( v ) {
        v = v.replace( /[\d.]+\w+/g, function ( m ) {
            return utils.transUnitToPx(m);
        } );
        return v;
    }

    function filterPasteWord( str ) {
        return str.replace(/[\t\r\n]+/g,' ')
            .replace( /<!--[\s\S]*?-->/ig, "" )
            //ת��ͼƬ
            .replace(/<v:shape [^>]*>[\s\S]*?.<\/v:shape>/gi,function(str){
                //opera���Լ�������image������ֱ�ӷ��ؿ�
                if(browser.opera){
                    return '';
                }
                try{
                    //�п�����bitmapռΪͼ�����ã�ֱ�ӹ��˵�����Ҫ������ճ��excel�����
                    if(/Bitmap/i.test(str)){
                        return '';
                    }
                    var width = str.match(/width:([ \d.]*p[tx])/i)[1],
                        height = str.match(/height:([ \d.]*p[tx])/i)[1],
                        src =  str.match(/src=\s*"([^"]*)"/i)[1];
                    return '<img width="'+ transUnit(width) +'" height="'+transUnit(height) +'" src="' + src + '" />';
                } catch(e){
                    return '';
                }
            })
            //���wps��ӵĶ����ǩ����
            .replace(/<\/?div[^>]*>/g,'')
            //ȥ�����������
            .replace( /v:\w+=(["']?)[^'"]+\1/g, '' )
            .replace( /<(!|script[^>]*>.*?<\/script(?=[>\s])|\/?(\?xml(:\w+)?|xml|meta|link|style|\w+:\w+)(?=[\s\/>]))[^>]*>/gi, "" )
            .replace( /<p [^>]*class="?MsoHeading"?[^>]*>(.*?)<\/p>/gi, "<p><strong>$1</strong></p>" )
            //ȥ�����������
            .replace( /\s+(class|lang|align)\s*=\s*(['"]?)([\w-]+)\2/ig, function(str,name,marks,val){
                //����list�ı�ʾ
                return name == 'class' && val == 'MsoListParagraph' ? str : ''
            })
            //�������font/span����ƥ��&nbsp;�п����ǿո�
            .replace( /<(font|span)[^>]*>(\s*)<\/\1>/gi, function(a,b,c){
                return c.replace(/[\t\r\n ]+/g,' ')
            })
            //����style������
            .replace( /(<[a-z][^>]*)\sstyle=(["'])([^\2]*?)\2/gi, function( str, tag, tmp, style ) {
                var n = [],
                    s = style.replace( /^\s+|\s+$/, '' )
                        .replace(/&#39;/g,'\'')
                        .replace( /&quot;/gi, "'" )
                        .split( /;\s*/g );

                for ( var i = 0,v; v = s[i];i++ ) {

                    var name, value,
                        parts = v.split( ":" );

                    if ( parts.length == 2 ) {
                        name = parts[0].toLowerCase();
                        value = parts[1].toLowerCase();
                        if(/^(background)\w*/.test(name) && value.replace(/(initial|\s)/g,'').length == 0
                            ||
                            /^(margin)\w*/.test(name) && /^0\w+$/.test(value)
                            ){
                            continue;
                        }

                        switch ( name ) {
                            case "mso-padding-alt":
                            case "mso-padding-top-alt":
                            case "mso-padding-right-alt":
                            case "mso-padding-bottom-alt":
                            case "mso-padding-left-alt":
                            case "mso-margin-alt":
                            case "mso-margin-top-alt":
                            case "mso-margin-right-alt":
                            case "mso-margin-bottom-alt":
                            case "mso-margin-left-alt":
                            //ie�»���ּ���һ������
                            //case "mso-table-layout-alt":
                            case "mso-height":
                            case "mso-width":
                            case "mso-vertical-align-alt":
                                //trace:1819 ff�»������padding��table��
                                if(!/<table/.test(tag))
                                    n[i] = name.replace( /^mso-|-alt$/g, "" ) + ":" + transUnit( value );
                                continue;
                            case "horiz-align":
                                n[i] = "text-align:" + value;
                                continue;

                            case "vert-align":
                                n[i] = "vertical-align:" + value;
                                continue;

                            case "font-color":
                            case "mso-foreground":
                                n[i] = "color:" + value;
                                continue;

                            case "mso-background":
                            case "mso-highlight":
                                n[i] = "background:" + value;
                                continue;

                            case "mso-default-height":
                                n[i] = "min-height:" + transUnit( value );
                                continue;

                            case "mso-default-width":
                                n[i] = "min-width:" + transUnit( value );
                                continue;

                            case "mso-padding-between-alt":
                                n[i] = "border-collapse:separate;border-spacing:" + transUnit( value );
                                continue;

                            case "text-line-through":
                                if ( (value == "single") || (value == "double") ) {
                                    n[i] = "text-decoration:line-through";
                                }
                                continue;
                            case "mso-zero-height":
                                if ( value == "yes" ) {
                                    n[i] = "display:none";
                                }
                                continue;
//                                case 'background':
//                                    break;
                            case 'margin':
                                if ( !/[1-9]/.test( value ) ) {
                                    continue;
                                }

                        }

                        if ( /^(mso|column|font-emph|lang|layout|line-break|list-image|nav|panose|punct|row|ruby|sep|size|src|tab-|table-border|text-(?:decor|trans)|top-bar|version|vnd|word-break)/.test( name )
                            ||
                            /text\-indent|padding|margin/.test(name) && /\-[\d.]+/.test(value)
                            ) {
                            continue;
                        }

                        n[i] = name + ":" + parts[1];
                    }
                }
                return tag + (n.length ? ' style="' + n.join( ';').replace(/;{2,}/g,';') + '"' : '');
            })
            .replace(/[\d.]+(cm|pt)/g,function(str){
                return utils.transUnitToPx(str)
            })

    }

    return function ( html ) {
        return (isWordDocument( html ) ? filterPasteWord( html ) : html);
    };
}();
///import editor.js
///import core/utils.js
///import core/dom/dom.js
///import core/dom/dtd.js
///import core/htmlparser.js
//ģ��Ľڵ���
//by zhanyi
(function () {
    var uNode = UM.uNode = function (obj) {
        this.type = obj.type;
        this.data = obj.data;
        this.tagName = obj.tagName;
        this.parentNode = obj.parentNode;
        this.attrs = obj.attrs || {};
        this.children = obj.children;
    };
    var notTransAttrs = {
        'href':1,
        'src':1,
        '_src':1,
        '_href':1,
        'cdata_data':1
    };

    var notTransTagName = {
        style:1,
        script:1
    };

    var indentChar = '    ',
        breakChar = '\n';

    function insertLine(arr, current, begin) {
        arr.push(breakChar);
        return current + (begin ? 1 : -1);
    }

    function insertIndent(arr, current) {
        //��������
        for (var i = 0; i < current; i++) {
            arr.push(indentChar);
        }
    }

    //����uNode�ľ�̬����
    //֧�ֱ�ǩ��html
    uNode.createElement = function (html) {
        if (/[<>]/.test(html)) {
            return UM.htmlparser(html).children[0]
        } else {
            return new uNode({
                type:'element',
                children:[],
                tagName:html
            })
        }
    };
    uNode.createText = function (data,noTrans) {
        return new UM.uNode({
            type:'text',
            'data':noTrans ? data : utils.unhtml(data || '')
        })
    };
    function nodeToHtml(node, arr, formatter, current) {
        switch (node.type) {
            case 'root':
                for (var i = 0, ci; ci = node.children[i++];) {
                    //��������
                    if (formatter && ci.type == 'element' && !dtd.$inlineWithA[ci.tagName] && i > 1) {
                        insertLine(arr, current, true);
                        insertIndent(arr, current)
                    }
                    nodeToHtml(ci, arr, formatter, current)
                }
                break;
            case 'text':
                isText(node, arr);
                break;
            case 'element':
                isElement(node, arr, formatter, current);
                break;
            case 'comment':
                isComment(node, arr, formatter);
        }
        return arr;
    }

    function isText(node, arr) {
        if(node.parentNode.tagName == 'pre'){
            //Դ��ģʽ������html��ǩ��������ת�����?ֱ�����
            arr.push(node.data)
        }else{
            arr.push(notTransTagName[node.parentNode.tagName] ? utils.html(node.data) : node.data.replace(/[ ]{2}/g,' &nbsp;'))
        }

    }

    function isElement(node, arr, formatter, current) {
        var attrhtml = '';
        if (node.attrs) {
            attrhtml = [];
            var attrs = node.attrs;
            for (var a in attrs) {
                //��������
                //<p>'<img src='http://nsclick.baidu.com/u.gif?&asdf=\"sdf&asdfasdfs;asdf'></p>
                //����ߵ�\"��ת����Ҫ����innerHTMLֱ�ӱ��ض��ˣ�����src
                //�п������Ĳ���
                attrhtml.push(a + (attrs[a] !== undefined ? '="' + (notTransAttrs[a] ? utils.html(attrs[a]).replace(/["]/g, function (a) {
                    return '&quot;'
                }) : utils.unhtml(attrs[a])) + '"' : ''))
            }
            attrhtml = attrhtml.join(' ');
        }
        arr.push('<' + node.tagName +
            (attrhtml ? ' ' + attrhtml  : '') +
            (dtd.$empty[node.tagName] ? '\/' : '' ) + '>'
        );
        //��������
        if (formatter  &&  !dtd.$inlineWithA[node.tagName] && node.tagName != 'pre') {
            if(node.children && node.children.length){
                current = insertLine(arr, current, true);
                insertIndent(arr, current)
            }

        }
        if (node.children && node.children.length) {
            for (var i = 0, ci; ci = node.children[i++];) {
                if (formatter && ci.type == 'element' &&  !dtd.$inlineWithA[ci.tagName] && i > 1) {
                    insertLine(arr, current);
                    insertIndent(arr, current)
                }
                nodeToHtml(ci, arr, formatter, current)
            }
        }
        if (!dtd.$empty[node.tagName]) {
            if (formatter && !dtd.$inlineWithA[node.tagName]  && node.tagName != 'pre') {

                if(node.children && node.children.length){
                    current = insertLine(arr, current);
                    insertIndent(arr, current)
                }
            }
            arr.push('<\/' + node.tagName + '>');
        }

    }

    function isComment(node, arr) {
        arr.push('<!--' + node.data + '-->');
    }

    function getNodeById(root, id) {
        var node;
        if (root.type == 'element' && root.getAttr('id') == id) {
            return root;
        }
        if (root.children && root.children.length) {
            for (var i = 0, ci; ci = root.children[i++];) {
                if (node = getNodeById(ci, id)) {
                    return node;
                }
            }
        }
    }

    function getNodesByTagName(node, tagName, arr) {
        if (node.type == 'element' && node.tagName == tagName) {
            arr.push(node);
        }
        if (node.children && node.children.length) {
            for (var i = 0, ci; ci = node.children[i++];) {
                getNodesByTagName(ci, tagName, arr)
            }
        }
    }
    function nodeTraversal(root,fn){
        if(root.children && root.children.length){
            for(var i= 0,ci;ci=root.children[i];){
                nodeTraversal(ci,fn);
                //ci���滻�����������Ͳ����� fn��
                if(ci.parentNode ){
                    if(ci.children && ci.children.length){
                        fn(ci)
                    }
                    if(ci.parentNode) i++
                }
            }
        }else{
            fn(root)
        }

    }
    uNode.prototype = {

        /**
         * ��ǰ�ڵ����ת����html�ı�
         * @method toHtml
         * @return { String } ����ת�����html�ַ�
         * @example
         * ```javascript
         * node.toHtml();
         * ```
         */

        /**
         * ��ǰ�ڵ����ת����html�ı�
         * @method toHtml
         * @param { Boolean } formatter �Ƿ��ʽ������ֵ
         * @return { String } ����ת�����html�ַ�
         * @example
         * ```javascript
         * node.toHtml( true );
         * ```
         */
        toHtml:function (formatter) {
            var arr = [];
            nodeToHtml(this, arr, formatter, 0);
            return arr.join('')
        },

        /**
         * ��ȡ�ڵ��html����
         * @method innerHTML
         * @warning ����ڵ��type����'element'����ڵ�ı�ǩ��Ʋ���dtd�б��ֱ�ӷ��ص�ǰ�ڵ�
         * @return { String } ���ؽڵ��html����
         * @example
         * ```javascript
         * var htmlstr = node.innerHTML();
         * ```
         */

        /**
         * ���ýڵ��html����
         * @method innerHTML
         * @warning ����ڵ��type����'element'����ڵ�ı�ǩ��Ʋ���dtd�б��ֱ�ӷ��ص�ǰ�ڵ�
         * @param { String } htmlstr ����Ҫ���õ�html����
         * @return { UM.uNode } ���ؽڵ㱾��
         * @example
         * ```javascript
         * node.innerHTML('<span>text</span>');
         * ```
         */
        innerHTML:function (htmlstr) {
            if (this.type != 'element' || dtd.$empty[this.tagName]) {
                return this;
            }
            if (utils.isString(htmlstr)) {
                if(this.children){
                    for (var i = 0, ci; ci = this.children[i++];) {
                        ci.parentNode = null;
                    }
                }
                this.children = [];
                var tmpRoot = UM.htmlparser(htmlstr);
                for (var i = 0, ci; ci = tmpRoot.children[i++];) {
                    this.children.push(ci);
                    ci.parentNode = this;
                }
                return this;
            } else {
                var tmpRoot = new UM.uNode({
                    type:'root',
                    children:this.children
                });
                return tmpRoot.toHtml();
            }
        },

        /**
         * ��ȡ�ڵ�Ĵ��ı�����
         * @method innerText
         * @warning ����ڵ��type����'element'����ڵ�ı�ǩ��Ʋ���dtd�б��ֱ�ӷ��ص�ǰ�ڵ�
         * @return { String } ���ؽڵ�Ĵ��ı�����
         * @example
         * ```javascript
         * var textStr = node.innerText();
         * ```
         */

        /**
         * ���ýڵ�Ĵ��ı�����
         * @method innerText
         * @warning ����ڵ��type����'element'����ڵ�ı�ǩ��Ʋ���dtd�б��ֱ�ӷ��ص�ǰ�ڵ�
         * @param { String } textStr ����Ҫ���õ��ı�����
         * @return { UM.uNode } ���ؽڵ㱾��
         * @example
         * ```javascript
         * node.innerText('<span>text</span>');
         * ```
         */
        innerText:function (textStr,noTrans) {
            if (this.type != 'element' || dtd.$empty[this.tagName]) {
                return this;
            }
            if (textStr) {
                if(this.children){
                    for (var i = 0, ci; ci = this.children[i++];) {
                        ci.parentNode = null;
                    }
                }
                this.children = [];
                this.appendChild(uNode.createText(textStr,noTrans));
                return this;
            } else {
                return this.toHtml().replace(/<[^>]+>/g, '');
            }
        },

        /**
         * ��ȡ��ǰ�����data����
         * @method getData
         * @return { Object } ���ڵ��typeֵ��elemenet�����ؿ��ַ����򷵻ؽڵ��data����
         * @example
         * ```javascript
         * node.getData();
         * ```
         */
        getData:function () {
            if (this.type == 'element')
                return '';
            return this.data
        },

        /**
         * ��ȡ��ǰ�ڵ��µĵ�һ���ӽڵ�
         * @method firstChild
         * @return { UM.uNode } ���ص�һ���ӽڵ�
         * @example
         * ```javascript
         * node.firstChild(); //���ص�һ���ӽڵ�
         * ```
         */
        firstChild:function () {
//            if (this.type != 'element' || dtd.$empty[this.tagName]) {
//                return this;
//            }
            return this.children ? this.children[0] : null;
        },

        /**
         * ��ȡ��ǰ�ڵ��µ����һ���ӽڵ�
         * @method lastChild
         * @return { UM.uNode } �������һ���ӽڵ�
         * @example
         * ```javascript
         * node.lastChild(); //�������һ���ӽڵ�
         * ```
         */
        lastChild:function () {
//            if (this.type != 'element' || dtd.$empty[this.tagName] ) {
//                return this;
//            }
            return this.children ? this.children[this.children.length - 1] : null;
        },

        /**
         * ��ȡ�͵�ǰ�ڵ�����ͬ���׽ڵ��ǰһ���ڵ�
         * @method previousSibling
         * @return { UM.uNode } ����ǰһ���ڵ�
         * @example
         * ```javascript
         * node.children[2].previousSibling(); //�����ӽڵ�node.children[1]
         * ```
         */
        previousSibling : function(){
            var parent = this.parentNode;
            for (var i = 0, ci; ci = parent.children[i]; i++) {
                if (ci === this) {
                    return i == 0 ? null : parent.children[i-1];
                }
            }

        },

        /**
         * ��ȡ�͵�ǰ�ڵ�����ͬ���׽ڵ�ĺ�һ���ڵ�
         * @method nextSibling
         * @return { UM.uNode } ���غ�һ���ڵ�,�Ҳ�������null
         * @example
         * ```javascript
         * node.children[2].nextSibling(); //����У������ӽڵ�node.children[3]
         * ```
         */
        nextSibling : function(){
            var parent = this.parentNode;
            for (var i = 0, ci; ci = parent.children[i++];) {
                if (ci === this) {
                    return parent.children[i];
                }
            }
        },

        /**
         * ���µĽڵ��滻��ǰ�ڵ�
         * @method replaceChild
         * @param { UM.uNode } target Ҫ�滻�ɸýڵ����
         * @param { UM.uNode } source Ҫ���滻���Ľڵ�
         * @return { UM.uNode } �����滻֮��Ľڵ����
         * @example
         * ```javascript
         * node.replaceChild(newNode, childNode); //��newNode�滻childNode,childNode��node���ӽڵ�
         * ```
         */
        replaceChild:function (target, source) {
            if (this.children) {
                if(target.parentNode){
                    target.parentNode.removeChild(target);
                }
                for (var i = 0, ci; ci = this.children[i]; i++) {
                    if (ci === source) {
                        this.children.splice(i, 1, target);
                        source.parentNode = null;
                        target.parentNode = this;
                        return target;
                    }
                }
            }
        },

        /**
         * �ڽڵ���ӽڵ��б����λ�ò���һ���ڵ�
         * @method appendChild
         * @param { UM.uNode } node Ҫ����Ľڵ�
         * @return { UM.uNode } ���ظղ�����ӽڵ�
         * @example
         * ```javascript
         * node.appendChild( newNode ); //��node�ڲ����ӽڵ�newNode
         * ```
         */
        appendChild:function (node) {
            if (this.type == 'root' || (this.type == 'element' && !dtd.$empty[this.tagName])) {
                if (!this.children) {
                    this.children = []
                }
                if(node.parentNode){
                    node.parentNode.removeChild(node);
                }
                for (var i = 0, ci; ci = this.children[i]; i++) {
                    if (ci === node) {
                        this.children.splice(i, 1);
                        break;
                    }
                }
                this.children.push(node);
                node.parentNode = this;
                return node;
            }


        },

        /**
         * �ڴ���ڵ��ǰ�����һ���ڵ�
         * @method insertBefore
         * @param { UM.uNode } target Ҫ����Ľڵ�
         * @param { UM.uNode } source �ڸò���ڵ�ǰ�����
         * @return { UM.uNode } ���ظղ�����ӽڵ�
         * @example
         * ```javascript
         * node.parentNode.insertBefore(newNode, node); //��node�ڵ�������newNode
         * ```
         */
        insertBefore:function (target, source) {
            if (this.children) {
                if(target.parentNode){
                    target.parentNode.removeChild(target);
                }
                for (var i = 0, ci; ci = this.children[i]; i++) {
                    if (ci === source) {
                        this.children.splice(i, 0, target);
                        target.parentNode = this;
                        return target;
                    }
                }

            }
        },

        /**
         * �ڴ���ڵ�ĺ������һ���ڵ�
         * @method insertAfter
         * @param { UM.uNode } target Ҫ����Ľڵ�
         * @param { UM.uNode } source �ڸò���ڵ�������
         * @return { UM.uNode } ���ظղ�����ӽڵ�
         * @example
         * ```javascript
         * node.parentNode.insertAfter(newNode, node); //��node�ڵ�������newNode
         * ```
         */
        insertAfter:function (target, source) {
            if (this.children) {
                if(target.parentNode){
                    target.parentNode.removeChild(target);
                }
                for (var i = 0, ci; ci = this.children[i]; i++) {
                    if (ci === source) {
                        this.children.splice(i + 1, 0, target);
                        target.parentNode = this;
                        return target;
                    }

                }
            }
        },

        /**
         * �ӵ�ǰ�ڵ���ӽڵ��б��У��Ƴ�ڵ�
         * @method removeChild
         * @param { UM.uNode } node Ҫ�Ƴ�Ľڵ�����
         * @param { Boolean } keepChildren �Ƿ����Ƴ�ڵ���ӽڵ㣬������true���Զ����Ƴ�ڵ���ӽڵ���뵽�Ƴ��λ��
         * @return { * } ���ظ��Ƴ���ӽڵ�
         * @example
         * ```javascript
         * node.removeChild(childNode,true); //��node���ӽڵ��б����Ƴ�child�ڵ㣬���Ұ�child���ӽڵ���뵽�Ƴ��λ��
         * ```
         */
        removeChild:function (node,keepChildren) {
            if (this.children) {
                for (var i = 0, ci; ci = this.children[i]; i++) {
                    if (ci === node) {
                        this.children.splice(i, 1);
                        ci.parentNode = null;
                        if(keepChildren && ci.children && ci.children.length){
                            for(var j= 0,cj;cj=ci.children[j];j++){
                                this.children.splice(i+j,0,cj);
                                cj.parentNode = this;

                            }
                        }
                        return ci;
                    }
                }
            }
        },

        /**
         * ��ȡ��ǰ�ڵ������Ԫ�����ԣ�����ȡattrs�����µ�����ֵ
         * @method getAttr
         * @param { String } attrName Ҫ��ȡ���������
         * @return { * } ����attrs�����µ�����ֵ
         * @example
         * ```javascript
         * node.getAttr('title');
         * ```
         */
        getAttr:function (attrName) {
            return this.attrs && this.attrs[attrName.toLowerCase()]
        },

        /**
         * ���õ�ǰ�ڵ������Ԫ�����ԣ�������attrs�����µ�����ֵ
         * @method setAttr
         * @param { String } attrName Ҫ���õ��������
         * @param { * } attrVal Ҫ���õ�����ֵ�����������õ����Զ�
         * @return { * } ����attrs�����µ�����ֵ
         * @example
         * ```javascript
         * node.setAttr('title','����');
         * ```
         */
        setAttr:function (attrName, attrVal) {
            if (!attrName) {
                delete this.attrs;
                return;
            }
            if(!this.attrs){
                this.attrs = {};
            }
            if (utils.isObject(attrName)) {
                for (var a in attrName) {
                    if (!attrName[a]) {
                        delete this.attrs[a]
                    } else {
                        this.attrs[a.toLowerCase()] = attrName[a];
                    }
                }
            } else {
                if (!attrVal) {
                    delete this.attrs[attrName]
                } else {
                    this.attrs[attrName.toLowerCase()] = attrVal;
                }

            }
        },
        hasAttr: function( attrName ){
            var attrVal = this.getAttr( attrName );
            return ( attrVal !== null ) && ( attrVal !== undefined );
        },
        /**
         * ��ȡ��ǰ�ڵ��ڸ��ڵ��µ�λ������
         * @method getIndex
         * @return { Number } ����������ֵ�����û�и��ڵ㣬����-1
         * @example
         * ```javascript
         * node.getIndex();
         * ```
         */
        getIndex:function(){
            var parent = this.parentNode;
            for(var i= 0,ci;ci=parent.children[i];i++){
                if(ci === this){
                    return i;
                }
            }
            return -1;
        },

        /**
         * �ڵ�ǰ�ڵ��£����id���ҽڵ�
         * @method getNodeById
         * @param { String } id Ҫ���ҵ�id
         * @return { UM.uNode } �����ҵ��Ľڵ�
         * @example
         * ```javascript
         * node.getNodeById('textId');
         * ```
         */
        getNodeById:function (id) {
            var node;
            if (this.children && this.children.length) {
                for (var i = 0, ci; ci = this.children[i++];) {
                    if (node = getNodeById(ci, id)) {
                        return node;
                    }
                }
            }
        },

        /**
         * �ڵ�ǰ�ڵ��£����Ԫ����Ʋ��ҽڵ��б�
         * @method getNodesByTagName
         * @param { String } tagNames Ҫ���ҵ�Ԫ�����
         * @return { Array } �����ҵ��Ľڵ��б�
         * @example
         * ```javascript
         * node.getNodesByTagName('span');
         * ```
         */
        getNodesByTagName:function (tagNames) {
            tagNames = utils.trim(tagNames).replace(/[ ]{2,}/g, ' ').split(' ');
            var arr = [], me = this;
            utils.each(tagNames, function (tagName) {
                if (me.children && me.children.length) {
                    for (var i = 0, ci; ci = me.children[i++];) {
                        getNodesByTagName(ci, tagName, arr)
                    }
                }
            });
            return arr;
        },

        /**
         * �����ʽ��ƣ���ȡ�ڵ����ʽֵ
         * @method getStyle
         * @param { String } name Ҫ��ȡ����ʽ���
         * @return { String } ������ʽֵ
         * @example
         * ```javascript
         * node.getStyle('font-size');
         * ```
         */
        getStyle:function (name) {
            var cssStyle = this.getAttr('style');
            if (!cssStyle) {
                return ''
            }
            var reg = new RegExp('(^|;)\\s*' + name + ':([^;]+)','i');
            var match = cssStyle.match(reg);
            if (match && match[0]) {
                return match[2]
            }
            return '';
        },

        /**
         * ��ڵ�������ʽ
         * @method setStyle
         * @param { String } name Ҫ���õĵ���ʽ���
         * @param { String } val Ҫ���õĵ���ֵ
         * @example
         * ```javascript
         * node.setStyle('font-size', '12px');
         * ```
         */
        setStyle:function (name, val) {
            function exec(name, val) {
                var reg = new RegExp('(^|;)\\s*' + name + ':([^;]+;?)', 'gi');
                cssStyle = cssStyle.replace(reg, '$1');
                if (val) {
                    cssStyle = name + ':' + utils.unhtml(val) + ';' + cssStyle
                }

            }

            var cssStyle = this.getAttr('style');
            if (!cssStyle) {
                cssStyle = '';
            }
            if (utils.isObject(name)) {
                for (var a in name) {
                    exec(a, name[a])
                }
            } else {
                exec(name, val)
            }
            this.setAttr('style', utils.trim(cssStyle))
        },
        hasClass: function( className ){
            if( this.hasAttr('class') ) {
                var classNames = this.getAttr('class').split(/\s+/),
                    hasClass = false;
                $.each(classNames, function(key, item){
                    if( item === className ) {
                        hasClass = true;
                    }
                });
                return hasClass;
            } else {
                return false;
            }
        },
        addClass: function( className ){

            var classes = null,
                hasClass = false;

            if( this.hasAttr('class') ) {

                classes = this.getAttr('class');
                classes = classes.split(/\s+/);

                classes.forEach( function( item ){

                    if( item===className ) {
                        hasClass = true;
                        return;
                    }

                } );

                !hasClass && classes.push( className );

                this.setAttr('class', classes.join(" "));

            } else {
                this.setAttr('class', className);
            }

        },
        removeClass: function( className ){
            if( this.hasAttr('class') ) {
                var cl = this.getAttr('class');
                cl = cl.replace(new RegExp('\\b' + className + '\\b', 'g'),'');
                this.setAttr('class', utils.trim(cl).replace(/[ ]{2,}/g,' '));
            }
        },
        /**
         * ����һ������ݹ����ǰ�ڵ��µ����нڵ�
         * @method traversal
         * @param { Function } fn ����ڵ��ʱ������ڵ���Ϊ�������д˺���
         * @example
         * ```javascript
         * traversal(node, function(){
         *     console.log(node.type);
         * });
         * ```
         */
        traversal:function(fn){
            if(this.children && this.children.length){
                nodeTraversal(this,fn);
            }
            return this;
        }
    }
})();

//html�ַ�ת����uNode�ڵ�
//by zhanyi
var htmlparser = UM.htmlparser = function (htmlstr,ignoreBlank) {
    //todo ԭ���ķ�ʽ  [^"'<>\/] ��\/�Ͳ�������� <TD vAlign=top background=../AAA.JPG> ����ı�ǩ��
    //��ȥ���ˣ����ϵ�ԭ�����ˣ������ȼ�¼
    var re_tag = /<(?:(?:\/([^>]+)>)|(?:!--([\S|\s]*?)-->)|(?:([^\s\/>]+)\s*((?:(?:"[^"]*")|(?:'[^']*')|[^"'<>])*)\/?>))/g,
        re_attr = /([\w\-:.]+)(?:(?:\s*=\s*(?:(?:"([^"]*)")|(?:'([^']*)')|([^\s>]+)))|(?=\s|$))/g;

    //ie��ȡ�õ�html���ܻ���\n���ڣ�Ҫȥ�����ڴ���replace(/[\t\r\n]*/g,'');���������\n����ȥ��
    var allowEmptyTags = {
        b:1,code:1,i:1,u:1,strike:1,s:1,tt:1,strong:1,q:1,samp:1,em:1,span:1,
        sub:1,img:1,sup:1,font:1,big:1,small:1,iframe:1,a:1,br:1,pre:1
    };
    htmlstr = htmlstr.replace(new RegExp(domUtils.fillChar, 'g'), '');
    if(!ignoreBlank){
        htmlstr = htmlstr.replace(new RegExp('[\\r\\t\\n'+(ignoreBlank?'':' ')+']*<\/?(\\w+)\\s*(?:[^>]*)>[\\r\\t\\n'+(ignoreBlank?'':' ')+']*','g'), function(a,b){
            //br��ʱ��������
            if(b && allowEmptyTags[b.toLowerCase()]){
                return a.replace(/(^[\n\r]+)|([\n\r]+$)/g,'');
            }
            return a.replace(new RegExp('^[\\r\\n'+(ignoreBlank?'':' ')+']+'),'').replace(new RegExp('[\\r\\n'+(ignoreBlank?'':' ')+']+$'),'');
        });
    }

    var notTransAttrs = {
        'href':1,
        'src':1
    };

    var uNode = UM.uNode,
        needParentNode = {
            'td':'tr',
            'tr':['tbody','thead','tfoot'],
            'tbody':'table',
            'th':'tr',
            'thead':'table',
            'tfoot':'table',
            'caption':'table',
            'li':['ul', 'ol'],
            'dt':'dl',
            'dd':'dl',
            'option':'select'
        },
        needChild = {
            'ol':'li',
            'ul':'li'
        };

    function text(parent, data) {

        if(needChild[parent.tagName]){
            var tmpNode = uNode.createElement(needChild[parent.tagName]);
            parent.appendChild(tmpNode);
            tmpNode.appendChild(uNode.createText(data));
            parent = tmpNode;
        }else{

            parent.appendChild(uNode.createText(data));
        }
    }

    function element(parent, tagName, htmlattr) {
        var needParentTag;
        if (needParentTag = needParentNode[tagName]) {
            var tmpParent = parent,hasParent;
            while(tmpParent.type != 'root'){
                if(utils.isArray(needParentTag) ? utils.indexOf(needParentTag, tmpParent.tagName) != -1 : needParentTag == tmpParent.tagName){
                    parent = tmpParent;
                    hasParent = true;
                    break;
                }
                tmpParent = tmpParent.parentNode;
            }
            if(!hasParent){
                parent = element(parent, utils.isArray(needParentTag) ? needParentTag[0] : needParentTag)
            }
        }
        //��dtd����Ƕ��
//        if(parent.type != 'root' && !dtd[parent.tagName][tagName])
//            parent = parent.parentNode;
        var elm = new uNode({
            parentNode:parent,
            type:'element',
            tagName:tagName.toLowerCase(),
            //���ԱպϵĴ���һ��
            children:dtd.$empty[tagName] ? null : []
        });
        //������Դ��ڣ���������
        if (htmlattr) {
            var attrs = {}, match;
            while (match = re_attr.exec(htmlattr)) {
                attrs[match[1].toLowerCase()] = notTransAttrs[match[1].toLowerCase()] ? (match[2] || match[3] || match[4]) : utils.unhtml(match[2] || match[3] || match[4])
            }
            elm.attrs = attrs;
        }

        parent.children.push(elm);
        //������ԱպϽڵ㷵�ظ��׽ڵ�
        return  dtd.$empty[tagName] ? parent : elm
    }

    function comment(parent, data) {
        parent.children.push(new uNode({
            type:'comment',
            data:data,
            parentNode:parent
        }));
    }

    var match, currentIndex = 0, nextIndex = 0;
    //���ø�ڵ�
    var root = new uNode({
        type:'root',
        children:[]
    });
    var currentParent = root;

    while (match = re_tag.exec(htmlstr)) {
        currentIndex = match.index;
        try{
            if (currentIndex > nextIndex) {
                //text node
                text(currentParent, htmlstr.slice(nextIndex, currentIndex));
            }
            if (match[3]) {

                if(dtd.$cdata[currentParent.tagName]){
                    text(currentParent, match[0]);
                }else{
                    //start tag
                    currentParent = element(currentParent, match[3].toLowerCase(), match[4]);
                }


            } else if (match[1]) {
                if(currentParent.type != 'root'){
                    if(dtd.$cdata[currentParent.tagName] && !dtd.$cdata[match[1]]){
                        text(currentParent, match[0]);
                    }else{
                        var tmpParent = currentParent;
                        while(currentParent.type == 'element' && currentParent.tagName != match[1].toLowerCase()){
                            currentParent = currentParent.parentNode;
                            if(currentParent.type == 'root'){
                                currentParent = tmpParent;
                                throw 'break'
                            }
                        }
                        //end tag
                        currentParent = currentParent.parentNode;
                    }

                }

            } else if (match[2]) {
                //comment
                comment(currentParent, match[2])
            }
        }catch(e){}

        nextIndex = re_tag.lastIndex;

    }
    //���������ı������п��ܶ��������������ֶ��ж�һ��
    //���� <li>sdfsdfsdf<li>sdfsdfsdfsdf
    if (nextIndex < htmlstr.length) {
        text(currentParent, htmlstr.slice(nextIndex));
    }
    return root;
};
/**
 * @file
 * @name UM.filterNode
 * @short filterNode
 * @desc ��ݸ�Ĺ�����˽ڵ�
 * @import editor.js,core/utils.js
 * @anthor zhanyi
 */
var filterNode = UM.filterNode = function () {
    function filterNode(node,rules){
        switch (node.type) {
            case 'text':
                break;
            case 'element':
                var val;
                if(val = rules[node.tagName]){
                    if(val === '-'){
                        node.parentNode.removeChild(node)
                    }else if(utils.isFunction(val)){
                        var parentNode = node.parentNode,
                            index = node.getIndex();
                        val(node);
                        if(node.parentNode){
                            if(node.children){
                                for(var i = 0,ci;ci=node.children[i];){
                                    filterNode(ci,rules);
                                    if(ci.parentNode){
                                        i++;
                                    }
                                }
                            }
                        }else{
                            for(var i = index,ci;ci=parentNode.children[i];){
                                filterNode(ci,rules);
                                if(ci.parentNode){
                                    i++;
                                }
                            }
                        }


                    }else{
                        var attrs = val['$'];
                        if(attrs && node.attrs){
                            var tmpAttrs = {},tmpVal;
                            for(var a in attrs){
                                tmpVal = node.getAttr(a);
                                //todo ֻ�ȶ�style��������
                                if(a == 'style' && utils.isArray(attrs[a])){
                                    var tmpCssStyle = [];
                                    utils.each(attrs[a],function(v){
                                        var tmp;
                                        if(tmp = node.getStyle(v)){
                                            tmpCssStyle.push(v + ':' + tmp);
                                        }
                                    });
                                    tmpVal = tmpCssStyle.join(';')
                                }
                                if(tmpVal){
                                    tmpAttrs[a] = tmpVal;
                                }

                            }
                            node.attrs = tmpAttrs;
                        }
                        if(node.children){
                            for(var i = 0,ci;ci=node.children[i];){
                                filterNode(ci,rules);
                                if(ci.parentNode){
                                    i++;
                                }
                            }
                        }
                    }
                }else{
                    //���������۳��ӽڵ㲢ɾ��ýڵ�,cdata����
                    if(dtd.$cdata[node.tagName]){
                        node.parentNode.removeChild(node)
                    }else{
                        var parentNode = node.parentNode,
                            index = node.getIndex();
                        node.parentNode.removeChild(node,true);
                        for(var i = index,ci;ci=parentNode.children[i];){
                            filterNode(ci,rules);
                            if(ci.parentNode){
                                i++;
                            }
                        }
                    }
                }
                break;
            case 'comment':
                node.parentNode.removeChild(node)
        }

    }
    return function(root,rules){
        if(utils.isEmptyObject(rules)){
            return root;
        }
        var val;
        if(val = rules['-']){
            utils.each(val.split(' '),function(k){
                rules[k] = '-'
            })
        }
        for(var i= 0,ci;ci=root.children[i];){
            filterNode(ci,rules);
            if(ci.parentNode){
                i++;
            }
        }
        return root;
    }
}();
///import core
/**
 * @description ��������
 * @name baidu.editor.execCommand
 * @param   {String}   cmdName     inserthtml�������ݵ�����
 * @param   {String}   html                Ҫ���������
 * @author zhanyi
 */
UM.commands['inserthtml'] = {
    execCommand: function (command,html,notNeedFilter){
        var me = this,
            range,
            div;
        if(!html){
            return;
        }
        if(me.fireEvent('beforeinserthtml',html) === true){
            return;
        }
        range = me.selection.getRange();
        div = range.document.createElement( 'div' );
        div.style.display = 'inline';

        if (!notNeedFilter) {
            var root = UM.htmlparser(html);
            //�����˹��˹�����Ƚ��й���
            if(me.options.filterRules){
                UM.filterNode(root,me.options.filterRules);
            }
            //ִ��Ĭ�ϵĴ���
            me.filterInputRule(root);
            html = root.toHtml()
        }
        div.innerHTML = utils.trim( html );

        if ( !range.collapsed ) {
            var tmpNode = range.startContainer;
            if(domUtils.isFillChar(tmpNode)){
                range.setStartBefore(tmpNode)
            }
            tmpNode = range.endContainer;
            if(domUtils.isFillChar(tmpNode)){
                range.setEndAfter(tmpNode)
            }
            range.txtToElmBoundary();
            //����߽���ܷŵ���br��ǰ�ߣ�Ҫ��br�����
            // x[xxx]<br/>
            if(range.endContainer && range.endContainer.nodeType == 1){
                tmpNode = range.endContainer.childNodes[range.endOffset];
                if(tmpNode && domUtils.isBr(tmpNode)){
                    range.setEndAfter(tmpNode);
                }
            }
            if(range.startOffset == 0){
                tmpNode = range.startContainer;
                if(domUtils.isBoundaryNode(tmpNode,'firstChild') ){
                    tmpNode = range.endContainer;
                    if(range.endOffset == (tmpNode.nodeType == 3 ? tmpNode.nodeValue.length : tmpNode.childNodes.length) && domUtils.isBoundaryNode(tmpNode,'lastChild')){
                        me.body.innerHTML = '<p>'+(browser.ie ? '' : '<br/>')+'</p>';
                        range.setStart(me.body.firstChild,0).collapse(true)

                    }
                }
            }
            !range.collapsed && range.deleteContents();
            if(range.startContainer.nodeType == 1){
                var child = range.startContainer.childNodes[range.startOffset],pre;
                if(child && domUtils.isBlockElm(child) && (pre = child.previousSibling) && domUtils.isBlockElm(pre)){
                    range.setEnd(pre,pre.childNodes.length).collapse();
                    while(child.firstChild){
                        pre.appendChild(child.firstChild);
                    }
                    domUtils.remove(child);
                }
            }

        }


        var child,parent,pre,tmp,hadBreak = 0, nextNode;
        //���ǰλ��ѡ����fillcharҪ�ɵ���Ҫ����������
        if(range.inFillChar()){
            child = range.startContainer;
            if(domUtils.isFillChar(child)){
                range.setStartBefore(child).collapse(true);
                domUtils.remove(child);
            }else if(domUtils.isFillChar(child,true)){
                child.nodeValue = child.nodeValue.replace(fillCharReg,'');
                range.startOffset--;
                range.collapsed && range.collapse(true)
            }
        }
        while ( child = div.firstChild ) {
            if(hadBreak){
                var p = me.document.createElement('p');
                while(child && (child.nodeType == 3 || !dtd.$block[child.tagName])){
                    nextNode = child.nextSibling;
                    p.appendChild(child);
                    child = nextNode;
                }
                if(p.firstChild){

                    child = p
                }
            }
            range.insertNode( child );
            nextNode = child.nextSibling;
            if ( !hadBreak && child.nodeType == domUtils.NODE_ELEMENT && domUtils.isBlockElm( child ) ){

                parent = domUtils.findParent( child,function ( node ){ return domUtils.isBlockElm( node ); } );
                if ( parent && parent.tagName.toLowerCase() != 'body' && !(dtd[parent.tagName][child.nodeName] && child.parentNode === parent)){
                    if(!dtd[parent.tagName][child.nodeName]){
                        pre = parent;
                    }else{
                        tmp = child.parentNode;
                        while (tmp !== parent){
                            pre = tmp;
                            tmp = tmp.parentNode;

                        }
                    }


                    domUtils.breakParent( child, pre || tmp );
                    //ȥ��break��ǰһ������Ľڵ�  <p>|<[p> ==> <p></p><div></div><p>|</p>
                    var pre = child.previousSibling;
                    domUtils.trimWhiteTextNode(pre);
                    if(!pre.childNodes.length){
                        domUtils.remove(pre);
                    }
                    //trace:2012,�ڷ�ie��������п���ʣ�µĽڵ��п��ܲ��ܵ��������brռλ

                    if(!browser.ie &&
                        (next = child.nextSibling) &&
                        domUtils.isBlockElm(next) &&
                        next.lastChild &&
                        !domUtils.isBr(next.lastChild)){
                        next.appendChild(me.document.createElement('br'));
                    }
                    hadBreak = 1;
                }
            }
            var next = child.nextSibling;
            if(!div.firstChild && next && domUtils.isBlockElm(next)){

                range.setStart(next,0).collapse(true);
                break;
            }
            range.setEndAfter( child ).collapse();

        }

        child = range.startContainer;

        if(nextNode && domUtils.isBr(nextNode)){
            domUtils.remove(nextNode)
        }
        //��chrome�����пհ�չλ��
        if(domUtils.isBlockElm(child) && domUtils.isEmptyNode(child)){
            if(nextNode = child.nextSibling){
                domUtils.remove(child);
                if(nextNode.nodeType == 1 && dtd.$block[nextNode.tagName]){

                    range.setStart(nextNode,0).collapse(true).shrinkBoundary()
                }
            }else{

                try{
                    child.innerHTML = browser.ie ? domUtils.fillChar : '<br/>';
                }catch(e){
                    range.setStartBefore(child);
                    domUtils.remove(child)
                }

            }

        }
        //����true��Ϊ��ɾ������ʱ��ɾ���Σ���һ����ɾ��fillData
        try{
            if(browser.ie9below && range.startContainer.nodeType == 1 && !range.startContainer.childNodes[range.startOffset]){
                var start = range.startContainer,pre = start.childNodes[range.startOffset-1];
                if(pre && pre.nodeType == 1 && dtd.$empty[pre.tagName]){
                    var txt = this.document.createTextNode(domUtils.fillChar);
                    range.insertNode(txt).setStart(txt,0).collapse(true);
                }
            }
            setTimeout(function(){
                range.select(true);
            })

        }catch(e){}


        setTimeout(function(){
            range = me.selection.getRange();
            range.scrollIntoView();
            me.fireEvent('afterinserthtml');
        },200);
    }
};

///import core
///import plugins\inserthtml.js
///commands ����ͼƬ������ͼƬ�Ķ��뷽ʽ
///commandsName  InsertImage,ImageNone,ImageLeft,ImageRight,ImageCenter
///commandsTitle  ͼƬ,Ĭ��,����,����,����
///commandsDialog  dialogs\image
/**
 * Created by .
 * User: zhanyi
 * for image
 */
//注释images
//UM.commands['insertimage'] = {
//  execCommand:function (cmd, opt) {
//      opt = utils.isArray(opt) ? opt : [opt];
//      if (!opt.length) {
//          return;
//      }
//      var me = this;
//      var html = [], str = '', ci;
//      ci = opt[0];
//      if (opt.length == 1) {
//          str = '<img src="' + ci.src + '" ' + (ci._src ? ' _src="' + ci._src + '" ' : '') +
//              (ci.width ? 'width="' + ci.width + '" ' : '') +
//              (ci.height ? ' height="' + ci.height + '" ' : '') +
//              (ci['floatStyle'] == 'left' || ci['floatStyle'] == 'right' ? ' style="float:' + ci['floatStyle'] + ';"' : '') +
//              (ci.title && ci.title != "" ? ' title="' + ci.title + '"' : '') +
//              (ci.border && ci.border != "0" ? ' border="' + ci.border + '"' : '') +
//              (ci.alt && ci.alt != "" ? ' alt="' + ci.alt + '"' : '') +
//              (ci.hspace && ci.hspace != "0" ? ' hspace = "' + ci.hspace + '"' : '') +
//              (ci.vspace && ci.vspace != "0" ? ' vspace = "' + ci.vspace + '"' : '') + '/>';
//          if (ci['floatStyle'] == 'center') {
//              str = '<p style="text-align: center">' + str + '</p>';
//          }
//          html.push(str);
//
//      } else {
//          for (var i = 0; ci = opt[i++];) {
//              str = '<p ' + (ci['floatStyle'] == 'center' ? 'style="text-align: center" ' : '') + '><img src="' + ci.src + '" ' +
//                  (ci.width ? 'width="' + ci.width + '" ' : '') + (ci._src ? ' _src="' + ci._src + '" ' : '') +
//                  (ci.height ? ' height="' + ci.height + '" ' : '') +
//                  ' style="' + (ci['floatStyle'] && ci['floatStyle'] != 'center' ? 'float:' + ci['floatStyle'] + ';' : '') +
//                  (ci.border || '') + '" ' +
//                  (ci.title ? ' title="' + ci.title + '"' : '') + ' /></p>';
//              html.push(str);
//          }
//      }
//
//      me.execCommand('insertHtml', html.join(''), true);
//  }
//};
///import core
///commands �����ʽ,����,����,����,���˶���
///commandsName  JustifyLeft,JustifyCenter,JustifyRight,JustifyJustify
///commandsTitle  �������,���ж���,���Ҷ���,���˶���
/**
 * @description ��������
 * @name UM.execCommand
 * @param   {String}   cmdName     justifyִ�ж��뷽ʽ������
 * @param   {String}   align               ���뷽ʽ��left����right���ң�center���У�justify���˶���
 * @author zhanyi
 */
UM.plugins['justify']=function(){
    var me = this;
    $.each('justifyleft justifyright justifycenter justifyfull'.split(' '),function(i,cmdName){
        me.commands[cmdName] = {
            execCommand:function (cmdName) {
                return this.document.execCommand(cmdName);
            },
            queryCommandValue: function (cmdName) {
                var val = this.document.queryCommandValue(cmdName);
                return   val === true || val === 'true' ? cmdName.replace(/justify/,'') : '';
            },
            queryCommandState: function (cmdName) {
                return this.document.queryCommandState(cmdName) ? 1 : 0
            }
        };
    })
};

///import core
///import plugins\removeformat.js
///commands ������ɫ,����ɫ,�ֺ�,����,�»���,ɾ����
///commandsName  ForeColor,BackColor,FontSize,FontFamily,Underline,StrikeThrough
///commandsTitle  ������ɫ,����ɫ,�ֺ�,����,�»���,ɾ����
/**
 * @description ����
 * @name UM.execCommand
 * @param {String}     cmdName    ִ�еĹ������
 * @param {String}    value             �����ֵ
 */
UM.plugins['font'] = function () {
    var me = this,
        fonts = {
            'forecolor': 'forecolor',
            'backcolor': 'backcolor',
            'fontsize': 'fontsize',
            'fontfamily': 'fontname'
        },
        cmdNameToStyle = {
            'forecolor': 'color',
            'backcolor': 'background-color',
            'fontsize': 'font-size',
            'fontfamily': 'font-family'
        },
        cmdNameToAttr = {
            'forecolor': 'color',
            'fontsize': 'size',
            'fontfamily': 'face'
        };
    me.setOpt({
        'fontfamily': [
            { name: 'songti', val: '����,SimSun'},
            { name: 'yahei', val: '΢���ź�,Microsoft YaHei'},
            { name: 'kaiti', val: '����,����_GB2312, SimKai'},
            { name: 'heiti', val: '����, SimHei'},
            { name: 'lishu', val: '����, SimLi'},
            { name: 'andaleMono', val: 'andale mono'},
            { name: 'arial', val: 'arial, helvetica,sans-serif'},
            { name: 'arialBlack', val: 'arial black,avant garde'},
            { name: 'comicSansMs', val: 'comic sans ms'},
            { name: 'impact', val: 'impact,chicago'},
            { name: 'timesNewRoman', val: 'times new roman'},
            { name: 'sans-serif',val:'sans-serif'}
        ],
        'fontsize': [10, 12,  16, 18,24, 32,48]
    });

    me.addOutputRule(function (root) {
        utils.each(root.getNodesByTagName('font'), function (node) {
            if (node.tagName == 'font') {
                var cssStyle = [];
                for (var p in node.attrs) {
                    switch (p) {
                        case 'size':
                            var val =  node.attrs[p];
                            $.each({
                                '10':'1',
                                '12':'2',
                                '16':'3',
                                '18':'4',
                                '24':'5',
                                '32':'6',
                                '48':'7'
                            },function(k,v){
                                if(v == val){
                                    val = k;
                                    return false;
                                }
                            });
                            cssStyle.push('font-size:' + val + 'px');
                            break;
                        case 'color':
                            cssStyle.push('color:' + node.attrs[p]);
                            break;
                        case 'face':
                            cssStyle.push('font-family:' + node.attrs[p]);
                            break;
                        case 'style':
                            cssStyle.push(node.attrs[p]);
                    }
                }
                node.attrs = {
                    'style': cssStyle.join(';')
                };
            }
            node.tagName = 'span';
            if(node.parentNode.tagName == 'span' && node.parentNode.children.length == 1){
                $.each(node.attrs,function(k,v){

                    node.parentNode.attrs[k] = k == 'style' ? node.parentNode.attrs[k] + v : v;
                })
                node.parentNode.removeChild(node,true);
            }
        });
    });
    for(var p in fonts){
        (function (cmd) {
            me.commands[cmd] = {
                execCommand: function (cmdName,value) {
                    if(value == 'transparent'){
                        return;
                    }
                    var rng = this.selection.getRange();
                    if(rng.collapsed){
                        var span = $('<span></span>').css(cmdNameToStyle[cmdName],value)[0];
                        rng.insertNode(span).setStart(span,0).setCursor();
                    }else{
                        if(cmdName == 'fontsize'){
                            value  = {
                                '10':'1',
                                '12':'2',
                                '16':'3',
                                '18':'4',
                                '24':'5',
                                '32':'6',
                                '48':'7'
                            }[(value+"").replace(/px/,'')]
                        }
                        this.document.execCommand(fonts[cmdName],false, value);
                        if(browser.gecko){
                            $.each(this.$body.find('a'),function(i,a){
                                var parent = a.parentNode;
                                if(parent.lastChild === parent.firstChild && /FONT|SPAN/.test(parent.tagName)){
                                    var cloneNode = parent.cloneNode(false);
                                    cloneNode.innerHTML = a.innerHTML;
                                    $(a).html('').append(cloneNode).insertBefore(parent);

                                    $(parent).remove();
                                }
                            });
                        }
                        if(!browser.ie){
                            var nativeRange = this.selection.getNative().getRangeAt(0);
                            var common = nativeRange.commonAncestorContainer;
                            var rng = this.selection.getRange(),
                                bk = rng.createBookmark(true);

                            $(common).find('a').each(function(i,n){
                                var parent = n.parentNode;
                                if(parent.nodeName == 'FONT'){
                                    var font = parent.cloneNode(false);
                                    font.innerHTML = n.innerHTML;
                                    $(n).html('').append(font);
                                }
                            });
                            rng.moveToBookmark(bk).select()
                        }
                        return true
                    }

                },
                queryCommandValue: function (cmdName) {
                    var start = me.selection.getStart();
                    var val = $(start).css(cmdNameToStyle[cmdName]);
                    if(val === undefined){
                        val = $(start).attr(cmdNameToAttr[cmdName])
                    }
                    return val ? utils.fixColor(cmdName,val).replace(/px/,'') : '';
                },
                queryCommandState: function (cmdName) {
                    return this.queryCommandValue(cmdName)
                }
            };
        })(p);
    }
};
///import core
///commands ������,ȡ������
///commandsName  Link,Unlink
///commandsTitle  ������,ȡ������
///commandsDialog  dialogs\link
/**
 * ������
 * @function
 * @name UM.execCommand
 * @param   {String}   cmdName     link���볬����
 * @param   {Object}  options         url��ַ��title���⣬target�Ƿ����ҳ
 * @author zhanyi
 */
/**
 * ȡ������
 * @function
 * @name UM.execCommand
 * @param   {String}   cmdName     unlinkȡ������
 * @author zhanyi
 */

UM.plugins['link'] = function(){
    var me = this;

    me.setOpt('autourldetectinie',false);
    //��ie�½���autolink
    if(browser.ie && this.options.autourldetectinie === false){
        this.addListener('keyup',function(cmd,evt){
            var me = this,keyCode = evt.keyCode;
            if(keyCode == 13 || keyCode == 32){
                var rng = me.selection.getRange();
                var start = rng.startContainer;
                if(keyCode == 13){
                    if(start.nodeName == 'P'){
                        var pre = start.previousSibling;
                        if(pre && pre.nodeType == 1){
                            var pre = pre.lastChild;
                            if(pre && pre.nodeName == 'A' && !pre.getAttribute('_href')){
                                domUtils.remove(pre,true);
                            }
                        }
                    }
                }else if(keyCode == 32){
                   if(start.nodeType == 3 && /^\s$/.test(start.nodeValue)){
                       start = start.previousSibling;
                       if(start && start.nodeName == 'A' && !start.getAttribute('_href')){
                           domUtils.remove(start,true);
                       }
                   }
                }

            }


        });
    }

    this.addOutputRule(function(root){
        $.each(root.getNodesByTagName('a'),function(i,a){
            var _href = a.getAttr('_href');
            if(!/^(ftp|https?|\/|file)/.test(_href)){
                _href = 'http://' + _href;
            }
            a.setAttr('href', _href);
            a.setAttr('_href')
            if(a.getAttr('title')==''){
                a.setAttr('title')
            }
        })
    });
    this.addInputRule(function(root){
        $.each(root.getNodesByTagName('a'),function(i,a){
            a.setAttr('_href', a.getAttr('href'));
        })
    });
    me.commands['link'] = {
        execCommand : function( cmdName, opt ) {

            var me = this;
            var rng = me.selection.getRange();
            opt._href && (opt._href = utils.unhtml(opt._href, /[<">'](?:(amp|lt|quot|gt|#39|nbsp);)?/g));
            opt.href && (opt.href = utils.unhtml(opt.href, /[<">'](?:(amp|lt|quot|gt|#39|nbsp);)?/g));
            if(rng.collapsed){
                var start = rng.startContainer;
                if(start = domUtils.findParentByTagName(start,'a',true)){
                    $(start).attr(opt);
                    rng.selectNode(start).select()
                }else{
                    rng.insertNode($('<a>' + opt.href +'</a>').attr(opt)[0]).select()

                }

            }else{
                me.document.execCommand('createlink',false,'_umeditor_link');
                utils.each(domUtils.getElementsByTagName(me.body,'a',function(n){

                    return n.getAttribute('href') == '_umeditor_link'
                }),function(l){
                    if($(l).text() == '_umeditor_link'){
                        $(l).text(opt.href);
                    }
                    domUtils.setAttributes(l,opt);
                    rng.selectNode(l).select()
                })
            }

        },
        queryCommandState:function(){
            return this.queryCommandValue('link') ? 1 : 0;
        },
        queryCommandValue:function(){
            var path = this.selection.getStartElementPath();
            var result;
            $.each(path,function(i,n){
                if(n.nodeName == "A"){
                    result = n;
                    return false;
                }
            })
            return result;
        }
    };
    me.commands['unlink'] = {
        execCommand : function() {
            this.document.execCommand('unlink');
        }
    };
};
///import core
///commands ��ӡ
///commandsName  Print
///commandsTitle  ��ӡ
/**
 * @description ��ӡ
 * @name baidu.editor.execCommand
 * @param   {String}   cmdName     print��ӡ�༭������
 * @author zhanyi
 */
UM.commands['print'] = {
    execCommand : function(){
        var me = this,
            id = 'editor_print_' + +new Date();

        $('<iframe src="" id="' + id + '" name="' + id + '" frameborder="0"></iframe>').attr('id', id)
            .css({
                width:'0px',
                height:'0px',
                'overflow':'hidden',
                'float':'left',
                'position':'absolute',
                top:'-10000px',
                left:'-10000px'
            })
            .appendTo(me.$container.find('.edui-dialog-container'));

        var w = window.open('', id, ''),
            d = w.document;
        d.open();
        d.write('<html><head></head><body><div>'+this.getContent(null,null,true)+'</div><script>' +
            "setTimeout(function(){" +
            "window.print();" +
            "setTimeout(function(){" +
            "window.parent.$('#" + id + "').remove();" +
            "},100);" +
            "},200);" +
            '</script></body></html>');
        d.close();
    },
    notNeedUndo : 1
};
///import core
///commands ��ʽ
///commandsName  Paragraph
///commandsTitle  �����ʽ
/**
 * ������ʽ
 * @function
 * @name UM.execCommand
 * @param   {String}   cmdName     paragraph�������ִ������
 * @param   {String}   style               ��ǩֵΪ��'p', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6'
 * @param   {String}   attrs               ��ǩ������
 * @author zhanyi
 */
UM.plugins['paragraph'] = function() {
    var me = this;
    me.setOpt('paragraph',{'p':'', 'h1':'', 'h2':'', 'h3':'', 'h4':'', 'h5':'', 'h6':''});
    me.commands['paragraph'] = {
        execCommand : function( cmdName, style ) {
            return this.document.execCommand('formatBlock',false,'<' + style + '>');
        },
        queryCommandValue : function() {
            try{
                var  val = this.document.queryCommandValue('formatBlock')
            }catch(e){
            }
            return val ;
        }
    };
};

///import core
///import plugins\inserthtml.js
///commands �ָ���
///commandsName  Horizontal
///commandsTitle  �ָ���
/**
 * �ָ���
 * @function
 * @name UM.execCommand
 * @param {String}     cmdName    horizontal����ָ���
 */
UM.plugins['horizontal'] = function(){
    var me = this;
    me.commands['horizontal'] = {
        execCommand : function(  ) {
            this.document.execCommand('insertHorizontalRule');
            var rng = me.selection.getRange().txtToElmBoundary(true),
                start = rng.startContainer;
            if(domUtils.isBody(rng.startContainer)){
                var next = rng.startContainer.childNodes[rng.startOffset];
                if(!next){
                    next = $('<p></p>').appendTo(rng.startContainer).html(browser.ie ? '&nbsp;' : '<br/>')[0]
                }
                rng.setStart(next,0).setCursor()
            }else{

                while(dtd.$inline[start.tagName] && start.lastChild === start.firstChild){

                    var parent = start.parentNode;
                    parent.appendChild(start.firstChild);
                    parent.removeChild(start);
                    start = parent;
                }
                while(dtd.$inline[start.tagName]){
                    start = start.parentNode;
                }
                if(start.childNodes.length == 1 && start.lastChild.nodeName == 'HR'){
                    var hr = start.lastChild;
                    $(hr).insertBefore(start);
                    rng.setStart(start,0).setCursor();
                }else{
                    hr = $('hr',start)[0];
                    domUtils.breakParent(hr,start);
                    var pre = hr.previousSibling;
                    if(pre && domUtils.isEmptyBlock(pre)){
                        $(pre).remove()
                    }
                    rng.setStart(hr.nextSibling,0).setCursor();
                }

            }
        }
    };

};


///import core
///commands ����ĵ�
///commandsName  ClearDoc
///commandsTitle  ����ĵ�
/**
 *
 * ����ĵ�
 * @function
 * @name UM.execCommand
 * @param   {String}   cmdName     cleardoc����ĵ�
 */

UM.commands['cleardoc'] = {
    execCommand : function() {
        var me = this,
            range = me.selection.getRange();
        me.body.innerHTML = "<p>"+(ie ? "" : "<br/>")+"</p>";
        range.setStart(me.body.firstChild,0).setCursor(false,true);
        setTimeout(function(){
            me.fireEvent("clearDoc");
        },0);

    }
};


///import core
///commands ���������
///commandsName  Undo,Redo
///commandsTitle  ����,����
/**
 * @description ����
 * @author zhanyi
 */

UM.plugins['undo'] = function () {
    var saveSceneTimer;
    var me = this,
        maxUndoCount = me.options.maxUndoCount || 20,
        maxInputCount = me.options.maxInputCount || 20,
        fillchar = new RegExp(domUtils.fillChar + '|<\/hr>', 'gi');// ie���������</hr>
    var noNeedFillCharTags = {
        ol:1,ul:1,table:1,tbody:1,tr:1,body:1
    };
    var orgState = me.options.autoClearEmptyNode;
    function compareAddr(indexA, indexB) {
        if (indexA.length != indexB.length)
            return 0;
        for (var i = 0, l = indexA.length; i < l; i++) {
            if (indexA[i] != indexB[i])
                return 0
        }
        return 1;
    }

    function compareRangeAddress(rngAddrA, rngAddrB) {
        if (rngAddrA.collapsed != rngAddrB.collapsed) {
            return 0;
        }
        if (!compareAddr(rngAddrA.startAddress, rngAddrB.startAddress) || !compareAddr(rngAddrA.endAddress, rngAddrB.endAddress)) {
            return 0;
        }
        return 1;
    }

    function UndoManager() {
        this.list = [];
        this.index = 0;
        this.hasUndo = false;
        this.hasRedo = false;
        this.undo = function () {
            if (this.hasUndo) {
                if (!this.list[this.index - 1] && this.list.length == 1) {
                    this.reset();
                    return;
                }
                while (this.list[this.index].content == this.list[this.index - 1].content) {
                    this.index--;
                    if (this.index == 0) {
                        return this.restore(0);
                    }
                }
                this.restore(--this.index);
            }
        };
        this.redo = function () {
            if (this.hasRedo) {
                while (this.list[this.index].content == this.list[this.index + 1].content) {
                    this.index++;
                    if (this.index == this.list.length - 1) {
                        return this.restore(this.index);
                    }
                }
                this.restore(++this.index);
            }
        };

        this.restore = function () {
            var me = this.editor;
            var scene = this.list[this.index];
            var root = UM.htmlparser(scene.content.replace(fillchar, ''));
            me.options.autoClearEmptyNode = false;
            me.filterInputRule(root,true);
            me.options.autoClearEmptyNode = orgState;
            //trace:873
            //ȥ��չλ��
            me.body.innerHTML = root.toHtml();
            me.fireEvent('afterscencerestore');
            //����undo��ո�չλ������
            if (browser.ie) {
                utils.each(domUtils.getElementsByTagName(me.document,'td th caption p'),function(node){
                    if(domUtils.isEmptyNode(node)){
                        domUtils.fillNode(me.document, node);
                    }
                })
            }

            try{
                var rng = new dom.Range(me.document,me.body).moveToAddress(scene.address);
                if(browser.ie && rng.collapsed && rng.startContainer.nodeType == 1){
                    var tmpNode = rng.startContainer.childNodes[rng.startOffset];
                    if( !tmpNode || tmpNode.nodeType == 1 && dtd.$empty[tmpNode]){
                        rng.insertNode(me.document.createTextNode(' ')).collapse(true);
                    }
                }
                rng.select(noNeedFillCharTags[rng.startContainer.nodeName.toLowerCase()]);
            }catch(e){}

            this.update();
            this.clearKey();
            //���ܰ��Լ�reset��
            me.fireEvent('reset', true);
        };

        this.getScene = function () {
            var me = this.editor;
            var rng = me.selection.getRange(),
                rngAddress = rng.createAddress(false,true);
            me.fireEvent('beforegetscene');
            var root = UM.htmlparser(me.body.innerHTML,true);
            me.options.autoClearEmptyNode = false;
            me.filterOutputRule(root,true);
            me.options.autoClearEmptyNode = orgState;
            var cont = root.toHtml();
            browser.ie && (cont = cont.replace(/>&nbsp;</g, '><').replace(/\s*</g, '<').replace(/>\s*/g, '>'));
            me.fireEvent('aftergetscene');
            return {
                address:rngAddress,
                content:cont
            }
        };
        this.save = function (notCompareRange,notSetCursor) {
            clearTimeout(saveSceneTimer);
            var currentScene = this.getScene(notSetCursor),
                lastScene = this.list[this.index];
            //������ͬλ����ͬ����
            if (lastScene && lastScene.content == currentScene.content &&
                ( notCompareRange ? 1 : compareRangeAddress(lastScene.address, currentScene.address) )
                ) {
                return;
            }
            this.list = this.list.slice(0, this.index + 1);
            this.list.push(currentScene);
            //��������������ˣ��Ͱ���ǰ���޳�
            if (this.list.length > maxUndoCount) {
                this.list.shift();
            }
            this.index = this.list.length - 1;
            this.clearKey();
            //����undo/redo״̬
            this.update();

        };
        this.update = function () {
            this.hasRedo = !!this.list[this.index + 1];
            this.hasUndo = !!this.list[this.index - 1];
        };
        this.reset = function () {
            this.list = [];
            this.index = 0;
            this.hasUndo = false;
            this.hasRedo = false;
            this.clearKey();
        };
        this.clearKey = function () {
            keycont = 0;
            lastKeyCode = null;
        };
    }

    me.undoManger = new UndoManager();
    me.undoManger.editor = me;
    function saveScene() {
        this.undoManger.save();
    }

    me.addListener('saveScene', function () {
        var args = Array.prototype.splice.call(arguments,1);
        this.undoManger.save.apply(this.undoManger,args);
    });

    me.addListener('beforeexeccommand', saveScene);
    me.addListener('afterexeccommand', saveScene);

    me.addListener('reset', function (type, exclude) {
        if (!exclude) {
            this.undoManger.reset();
        }
    });
    me.commands['redo'] = me.commands['undo'] = {
        execCommand:function (cmdName) {
            this.undoManger[cmdName]();
        },
        queryCommandState:function (cmdName) {
            return this.undoManger['has' + (cmdName.toLowerCase() == 'undo' ? 'Undo' : 'Redo')] ? 0 : -1;
        },
        notNeedUndo:1
    };

    var keys = {
            //  /*Backspace*/ 8:1, /*Delete*/ 46:1,
            /*Shift*/ 16:1, /*Ctrl*/ 17:1, /*Alt*/ 18:1,
            37:1, 38:1, 39:1, 40:1

        },
        keycont = 0,
        lastKeyCode;
    //���뷨״̬�²������ַ���
    var inputType = false;
    me.addListener('ready', function () {
        $(this.body).on('compositionstart', function () {
            inputType = true;
        }).on('compositionend', function () {
            inputType = false;
        })
    });
    //��ݼ�
    me.addshortcutkey({
        "Undo":"ctrl+90", //undo
        "Redo":"ctrl+89,shift+ctrl+z" //redo

    });
    var isCollapsed = true;
    me.addListener('keydown', function (type, evt) {

        var me = this;
        var keyCode = evt.keyCode || evt.which;
        if (!keys[keyCode] && !evt.ctrlKey && !evt.metaKey && !evt.shiftKey && !evt.altKey) {
            if (inputType)
                return;

            if(!me.selection.getRange().collapsed){
                me.undoManger.save(false,true);
                isCollapsed = false;
                return;
            }
            if (me.undoManger.list.length == 0) {
                me.undoManger.save(true);
            }
            clearTimeout(saveSceneTimer);
            function save(cont){

                if (cont.selection.getRange().collapsed)
                    cont.fireEvent('contentchange');
                cont.undoManger.save(false,true);
                cont.fireEvent('selectionchange');
            }
            saveSceneTimer = setTimeout(function(){
                if(inputType){
                    var interalTimer = setInterval(function(){
                        if(!inputType){
                            save(me);
                            clearInterval(interalTimer)
                        }
                    },300)
                    return;
                }
                save(me);
            },200);

            lastKeyCode = keyCode;
            keycont++;
            if (keycont >= maxInputCount ) {
                save(me)
            }
        }
    });
    me.addListener('keyup', function (type, evt) {
        var keyCode = evt.keyCode || evt.which;
        if (!keys[keyCode] && !evt.ctrlKey && !evt.metaKey && !evt.shiftKey && !evt.altKey) {
            if (inputType)
                return;
            if(!isCollapsed){
                this.undoManger.save(false,true);
                isCollapsed = true;
            }
        }
    });

};

///import core
///import plugins/inserthtml.js
///import plugins/undo.js
///import plugins/serialize.js
///commands ճ��
///commandsName  PastePlain
///commandsTitle  ���ı�ճ��ģʽ
/**
 * @description ճ��
 * @author zhanyi
 */
UM.plugins['paste'] = function () {
    function getClipboardData(callback) {
        var doc = this.document;
        if (doc.getElementById('baidu_pastebin')) {
            return;
        }
        var range = this.selection.getRange(),
            bk = range.createBookmark(),
        //�������������div
            pastebin = doc.createElement('div');
        pastebin.id = 'baidu_pastebin';
        // Safari Ҫ��div���������ݣ�����ճ�����ݽ���
        browser.webkit && pastebin.appendChild(doc.createTextNode(domUtils.fillChar + domUtils.fillChar));
        this.body.appendChild(pastebin);
        //trace:717 ���ص�span���ܵõ�top
        //bk.start.innerHTML = '&nbsp;';
        bk.start.style.display = '';

        pastebin.style.cssText = "position:absolute;width:1px;height:1px;overflow:hidden;left:-1000px;white-space:nowrap;top:" +
        //Ҫ�����ڹ��ƽ�е�λ�ü��룬���������������
        $(bk.start).position().top  + 'px';

        range.selectNodeContents(pastebin).select(true);

        setTimeout(function () {
            if (browser.webkit) {
                for (var i = 0, pastebins = doc.querySelectorAll('#baidu_pastebin'), pi; pi = pastebins[i++];) {
                    if (domUtils.isEmptyNode(pi)) {
                        domUtils.remove(pi);
                    } else {
                        pastebin = pi;
                        break;
                    }
                }
            }
            try {
                pastebin.parentNode.removeChild(pastebin);
            } catch (e) {
            }
            range.moveToBookmark(bk).select(true);
            callback(pastebin);
        }, 0);
    }

    var me = this;


    function filter(div) {
        var html;
        if (div.firstChild) {
            //ȥ��cut����ӵı߽�ֵ
            var nodes = domUtils.getElementsByTagName(div, 'span');
            for (var i = 0, ni; ni = nodes[i++];) {
                if (ni.id == '_baidu_cut_start' || ni.id == '_baidu_cut_end') {
                    domUtils.remove(ni);
                }
            }

            if (browser.webkit) {

                var brs = div.querySelectorAll('div br');
                for (var i = 0, bi; bi = brs[i++];) {
                    var pN = bi.parentNode;
                    if (pN.tagName == 'DIV' && pN.childNodes.length == 1) {
                        pN.innerHTML = '<p><br/></p>';
                        domUtils.remove(pN);
                    }
                }
                var divs = div.querySelectorAll('#baidu_pastebin');
                for (var i = 0, di; di = divs[i++];) {
                    var tmpP = me.document.createElement('p');
                    di.parentNode.insertBefore(tmpP, di);
                    while (di.firstChild) {
                        tmpP.appendChild(di.firstChild);
                    }
                    domUtils.remove(di);
                }

                var metas = div.querySelectorAll('meta');
                for (var i = 0, ci; ci = metas[i++];) {
                    domUtils.remove(ci);
                }

                var brs = div.querySelectorAll('br');
                for (i = 0; ci = brs[i++];) {
                    if (/^apple-/i.test(ci.className)) {
                        domUtils.remove(ci);
                    }
                }
            }
            if (browser.gecko) {
                var dirtyNodes = div.querySelectorAll('[_moz_dirty]');
                for (i = 0; ci = dirtyNodes[i++];) {
                    ci.removeAttribute('_moz_dirty');
                }
            }
            if (!browser.ie) {
                var spans = div.querySelectorAll('span.Apple-style-span');
                for (var i = 0, ci; ci = spans[i++];) {
                    domUtils.remove(ci, true);
                }
            }

            //ie��ʹ��innerHTML���������\r\n�ַ�Ҳ�����&nbsp;������˵�
            html = div.innerHTML;//.replace(/>(?:(\s|&nbsp;)*?)</g,'><');

            //����wordճ���������������
            html = UM.filterWord(html);
            //ȡ���˺��Կհ׵ĵڶ�������ճ���������Щ���пհ׵ģ��ᱻ������صı�ǩ
            var root = UM.htmlparser(html);
            //�����˹��˹�����Ƚ��й���
            if (me.options.filterRules) {
                UM.filterNode(root, me.options.filterRules);
            }
            //ִ��Ĭ�ϵĴ���
            me.filterInputRule(root);
            //���chrome�Ĵ���
            if (browser.webkit) {
                var br = root.lastChild();
                if (br && br.type == 'element' && br.tagName == 'br') {
                    root.removeChild(br)
                }
                utils.each(me.body.querySelectorAll('div'), function (node) {
                    if (domUtils.isEmptyBlock(node)) {
                        domUtils.remove(node)
                    }
                })
            }
            html = {'html': root.toHtml()};
            me.fireEvent('beforepaste', html, root);
            //����Ĭ�ϵ�ճ���Ǻ�ߵ����ݾͲ�ִ���ˣ�������ճ��
            if(!html.html){
                return;
            }

            me.execCommand('insertHtml', html.html, true);
            me.fireEvent("afterpaste", html);
        }
    }


    me.addListener('ready', function () {
        $(me.body).on( 'cut', function () {
            var range = me.selection.getRange();
            if (!range.collapsed && me.undoManger) {
                me.undoManger.save();
            }
        }).on(browser.ie || browser.opera ? 'keydown' : 'paste', function (e) {
            //ie��beforepaste�ڵ���Ҽ�ʱҲ�ᴥ���������ü�ؼ��̲Ŵ���
            if ((browser.ie || browser.opera) && ((!e.ctrlKey && !e.metaKey) || e.keyCode != '86')) {
                return;
            }
            getClipboardData.call(me, function (div) {
                filter(div);
            });
        });

    });
};


///import core
///commands �����б�,�����б�
///commandsName  InsertOrderedList,InsertUnorderedList
///commandsTitle  �����б�,�����б�
/**
 * �����б�
 * @function
 * @name UM.execCommand
 * @param   {String}   cmdName     insertorderlist���������б�
 * @param   {String}   style               ֵΪ��decimal,lower-alpha,lower-roman,upper-alpha,upper-roman
 * @author zhanyi
 */
/**
 * ��������
 * @function
 * @name UM.execCommand
 * @param   {String}   cmdName     insertunorderlist���������б�
 * * @param   {String}   style            ֵΪ��circle,disc,square
 * @author zhanyi
 */

UM.plugins['list'] = function () {
    var me = this;

    me.setOpt( {
        'insertorderedlist':{
            'decimal':'',
            'lower-alpha':'',
            'lower-roman':'',
            'upper-alpha':'',
            'upper-roman':''
        },
        'insertunorderedlist':{
            'circle':'',
            'disc':'',
            'square':''
        }
    } );

    this.addInputRule(function(root){
        utils.each(root.getNodesByTagName('li'), function (node) {
            if(node.children.length == 0){
                node.parentNode.removeChild(node);
            }
        })
    });
    me.commands['insertorderedlist'] =
    me.commands['insertunorderedlist'] = {
            execCommand:function (cmdName) {
                this.document.execCommand(cmdName);
                var rng = this.selection.getRange(),
                    bk = rng.createBookmark(true);

                this.$body.find('ol,ul').each(function(i,n){
                    var parent = n.parentNode;
                    if(parent.tagName == 'P' && parent.lastChild === parent.firstChild){
                        $(n).children().each(function(j,li){
                            var p = parent.cloneNode(false);
                            $(p).append(li.innerHTML);
                            $(li).html('').append(p);
                        });
                        $(n).insertBefore(parent);
                        $(parent).remove();
                    }

                    if(dtd.$inline[parent.tagName]){
                        if(parent.tagName == 'SPAN'){

                            $(n).children().each(function(k,li){
                                var span = parent.cloneNode(false);
                                if(li.firstChild.nodeName != 'P'){

                                    while(li.firstChild){
                                        span.appendChild(li.firstChild)
                                    };
                                    $('<p></p>').appendTo(li).append(span);
                                }else{
                                    while(li.firstChild){
                                        span.appendChild(li.firstChild)
                                    };
                                    $(li.firstChild).append(span);
                                }
                            })

                        }
                        domUtils.remove(parent,true)
                    }
                });




                rng.moveToBookmark(bk).select();
                return true;
            },
            queryCommandState:function (cmdName) {
                return this.document.queryCommandState(cmdName);
            }
        };
};


///import core
///import plugins/serialize.js
///import plugins/undo.js
///commands �鿴Դ��
///commandsName  Source
///commandsTitle  �鿴Դ��
(function (){
    var sourceEditors = {
        textarea: function (editor, holder){
            var textarea = holder.ownerDocument.createElement('textarea');
            textarea.style.cssText = 'resize:none;border:0;padding:0;margin:0;overflow-y:auto;outline:0';
            // todo: IE��ֻ��onresize���Կ���... �ܾ���
            if (browser.ie && browser.version < 8) {

                textarea.style.width = holder.offsetWidth + 'px';
                textarea.style.height = holder.offsetHeight + 'px';
                holder.onresize = function (){
                    textarea.style.width = holder.offsetWidth + 'px';
                    textarea.style.height = holder.offsetHeight + 'px';
                };
            }
            holder.appendChild(textarea);
            return {
                container : textarea,
                setContent: function (content){
                    textarea.value = content;
                },
                getContent: function (){
                    return textarea.value;
                },
                select: function (){
                    var range;
                    if (browser.ie) {
                        range = textarea.createTextRange();
                        range.collapse(true);
                        range.select();
                    } else {
                        //todo: chrome���޷����ý���
                        textarea.setSelectionRange(0, 0);
                        textarea.focus();
                    }
                },
                dispose: function (){
                    holder.removeChild(textarea);
                    // todo
                    holder.onresize = null;
                    textarea = null;
                    holder = null;
                }
            };
        }
    };

    UM.plugins['source'] = function (){
        var me = this;
        var opt = this.options;
        var sourceMode = false;
        var sourceEditor;

        opt.sourceEditor = 'textarea';

        me.setOpt({
            sourceEditorFirst:false
        });
        function createSourceEditor(holder){
            return sourceEditors.textarea(me, holder);
        }

        var bakCssText;
        //�����Դ��ģʽ��getContent���ܵõ����µ���������
        var oldGetContent = me.getContent,
            bakAddress;

        me.commands['source'] = {
            execCommand: function (){

                sourceMode = !sourceMode;
                if (sourceMode) {
                    bakAddress = me.selection.getRange().createAddress(false,true);
                    me.undoManger && me.undoManger.save(true);
                    if(browser.gecko){
                        me.body.contentEditable = false;
                    }

//                    bakCssText = me.body.style.cssText;
                    me.body.style.cssText += ';position:absolute;left:-32768px;top:-32768px;';


                    me.fireEvent('beforegetcontent');
                    var root = UM.htmlparser(me.body.innerHTML);
                    me.filterOutputRule(root);
                    root.traversal(function (node) {
                        if (node.type == 'element') {
                            switch (node.tagName) {
                                case 'td':
                                case 'th':
                                case 'caption':
                                    if(node.children && node.children.length == 1){
                                        if(node.firstChild().tagName == 'br' ){
                                            node.removeChild(node.firstChild())
                                        }
                                    };
                                    break;
                                case 'pre':
                                    node.innerText(node.innerText().replace(/&nbsp;/g,' '))

                            }
                        }
                    });

                    me.fireEvent('aftergetcontent');

                    var content = root.toHtml(true);

                    sourceEditor = createSourceEditor(me.body.parentNode);

                    sourceEditor.setContent(content);

                    var getStyleValue=function(attr){
                        return parseInt($(me.body).css(attr));
                    };
                    $(sourceEditor.container).width($(me.body).width()+getStyleValue("padding-left")+getStyleValue("padding-right"))
                        .height($(me.body).height());
                    setTimeout(function (){
                        sourceEditor.select();
                    });
                    //����getContent��Դ��ģʽ��ȡֵҲ�������µ����
                    me.getContent = function (){
                        return sourceEditor.getContent() || '<p>' + (browser.ie ? '' : '<br/>')+'</p>';
                    };
                } else {
                    me.$body.css({
                        'position':'',
                        'left':'',
                        'top':''
                    });
//                    me.body.style.cssText = bakCssText;
                    var cont = sourceEditor.getContent() || '<p>' + (browser.ie ? '' : '<br/>')+'</p>';
                    //�����block�ڵ�ǰ��Ŀո�,�п��ܻ������У���ʱ������
                    cont = cont.replace(new RegExp('[\\r\\t\\n ]*<\/?(\\w+)\\s*(?:[^>]*)>','g'), function(a,b){
                        if(b && !dtd.$inlineWithA[b.toLowerCase()]){
                            return a.replace(/(^[\n\r\t ]*)|([\n\r\t ]*$)/g,'');
                        }
                        return a.replace(/(^[\n\r\t]*)|([\n\r\t]*$)/g,'')
                    });
                    me.setContent(cont);
                    sourceEditor.dispose();
                    sourceEditor = null;
                    //��ԭgetContent����
                    me.getContent = oldGetContent;
                    var first = me.body.firstChild;
                    //trace:1106 ��ɾ����ˣ��±߻ᱨ�?���Բ���һ��pռλ
                    if(!first){
                        me.body.innerHTML = '<p>'+(browser.ie?'':'<br/>')+'</p>';
                    }
                    //Ҫ��ifmΪ��ʾʱff����ȡ��selection,���򱨴�
                    //���ﲻ�ܱȽ�λ����
                    me.undoManger && me.undoManger.save(true);
                    if(browser.gecko){
                        me.body.contentEditable = true;
                    }
                    try{
                        me.selection.getRange().moveToAddress(bakAddress).select();
                    }catch(e){}

                }
                this.fireEvent('sourcemodechanged', sourceMode);
            },
            queryCommandState: function (){
                return sourceMode|0;
            },
            notNeedUndo : 1
        };
        var oldQueryCommandState = me.queryCommandState;


        me.queryCommandState = function (cmdName){
            cmdName = cmdName.toLowerCase();
            if (sourceMode) {
                //Դ��ģʽ�¿��Կ���������
                return cmdName in {
                    'source' : 1,
                    'fullscreen' : 1
                } ? oldQueryCommandState.apply(this, arguments)  : -1
            }
            return oldQueryCommandState.apply(this, arguments);
        };

    };

})();
///import core
///import plugins/undo.js
///commands ���ûس���ǩp��br
///commandsName  EnterKey
///commandsTitle  ���ûس���ǩp��br
/**
 * @description ����س�
 * @author zhanyi
 */
UM.plugins['enterkey'] = function() {
    var hTag,
        me = this,
        tag = me.options.enterTag;
    me.addListener('keyup', function(type, evt) {

        var keyCode = evt.keyCode || evt.which;
        if (keyCode == 13) {
            var range = me.selection.getRange(),
                start = range.startContainer,
                doSave;

            //������h1-h6��߻س�����Ƕ��p������
            if (!browser.ie) {

                if (/h\d/i.test(hTag)) {
                    if (browser.gecko) {
                        var h = domUtils.findParentByTagName(start, [ 'h1', 'h2', 'h3', 'h4', 'h5', 'h6','blockquote','caption','table'], true);
                        if (!h) {
                            me.document.execCommand('formatBlock', false, '<p>');
                            doSave = 1;
                        }
                    } else {
                        //chrome remove div
                        if (start.nodeType == 1) {
                            var tmp = me.document.createTextNode(''),div;
                            range.insertNode(tmp);
                            div = domUtils.findParentByTagName(tmp, 'div', true);
                            if (div) {
                                var p = me.document.createElement('p');
                                while (div.firstChild) {
                                    p.appendChild(div.firstChild);
                                }
                                div.parentNode.insertBefore(p, div);
                                domUtils.remove(div);
                                range.setStartBefore(tmp).setCursor();
                                doSave = 1;
                            }
                            domUtils.remove(tmp);

                        }
                    }

                    if (me.undoManger && doSave) {
                        me.undoManger.save();
                    }
                }
                //û��վλ�����ֶ��е�����
                browser.opera &&  range.select();
            }else{
                me.fireEvent('saveScene',true,true)
            }
        }
    });

    me.addListener('keydown', function(type, evt) {
        var keyCode = evt.keyCode || evt.which;
        if (keyCode == 13) {//�س�
            if(me.fireEvent('beforeenterkeydown')){
                domUtils.preventDefault(evt);
                return;
            }
            me.fireEvent('saveScene',true,true);
            hTag = '';


            var range = me.selection.getRange();

            if (!range.collapsed) {
                //��td����ɾ
                var start = range.startContainer,
                    end = range.endContainer,
                    startTd = domUtils.findParentByTagName(start, 'td', true),
                    endTd = domUtils.findParentByTagName(end, 'td', true);
                if (startTd && endTd && startTd !== endTd || !startTd && endTd || startTd && !endTd) {
                    evt.preventDefault ? evt.preventDefault() : ( evt.returnValue = false);
                    return;
                }
            }
            if (tag == 'p') {


                if (!browser.ie) {

                    start = domUtils.findParentByTagName(range.startContainer, ['ol','ul','p', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6','blockquote','caption'], true);

                    //opera��ִ��formatblock����table�ĳ����������⣬�س���operaԭ��֧�ֺܺã�������ʱ��operaȥ���������ԭ���command
                    //trace:2431
                    if (!start && !browser.opera) {

                        me.document.execCommand('formatBlock', false, '<p>');

                        if (browser.gecko) {
                            range = me.selection.getRange();
                            start = domUtils.findParentByTagName(range.startContainer, 'p', true);
                            start && domUtils.removeDirtyAttr(start);
                        }


                    } else {
                        hTag = start.tagName;
                        start.tagName.toLowerCase() == 'p' && browser.gecko && domUtils.removeDirtyAttr(start);
                    }

                }

            }

        }
    });

    browser.ie && me.addListener('setDisabled',function(){
        $(me.body).find('p').each(function(i,p){
            if(domUtils.isEmptyBlock(p)){
                p.innerHTML = '&nbsp;'
            }
        })
    })
};

///import core
///commands Ԥ��
///commandsName  Preview
///commandsTitle  Ԥ��
/**
 * Ԥ��
 * @function
 * @name UM.execCommand
 * @param   {String}   cmdName     previewԤ���༭������
 */
UM.commands['preview'] = {
    execCommand : function(){
        var w = window.open('', '_blank', ''),
            d = w.document,
            c = this.getContent(null,null,true),
            path = this.getOpt('UMEDITOR_HOME_URL'),
            formula = c.indexOf('mathquill-embedded-latex')!=-1 ?
                '<link rel="stylesheet" href="' + path + 'third-party/mathquill/mathquill.css"/>' +
                '<script src="' + path + 'third-party/jquery.min.js"></script>' +
                '<script src="' + path + 'third-party/mathquill/mathquill.min.js"></script>':'';
        d.open();
        d.write('<html><head>' + formula + '</head><body><div>'+c+'</div></body></html>');
        d.close();
    },
    notNeedUndo : 1
};

///import core
///commands �Ӵ�,б��,�ϱ�,�±�
///commandsName  Bold,Italic,Subscript,Superscript
///commandsTitle  �Ӵ�,��б,�±�,�ϱ�
/**
 * b u i�Ȼ���ʵ��
 * @function
 * @name UM.execCommands
 * @param    {String}    cmdName    bold�Ӵ֡�italicб�塣subscript�ϱꡣsuperscript�±ꡣ
*/
UM.plugins['basestyle'] = function(){
    var basestyles = ['bold','underline','superscript','subscript','italic','strikethrough'],
        me = this;
    //��ӿ�ݼ�
    me.addshortcutkey({
        "Bold" : "ctrl+66",//^B
        "Italic" : "ctrl+73", //^I
        "Underline" : "ctrl+shift+85",//^U
        "strikeThrough" : 'ctrl+shift+83' //^s
    });
    //�������Ĳ�����
    me.addOutputRule(function(root){
        $.each(root.getNodesByTagName('b i u strike s'),function(i,node){
            switch (node.tagName){
                case 'b':
                    node.tagName = 'strong';
                    break;
                case 'i':
                    node.tagName = 'em';
                    break;
                case 'u':
                    node.tagName = 'span';
                    node.setStyle('text-decoration','underline');
                    break;
                case 's':
                case 'strike':
                    node.tagName = 'span';
                    node.setStyle('text-decoration','line-through')
            }
        });
    });
    $.each(basestyles,function(i,cmd){
        me.commands[cmd] = {
            execCommand : function( cmdName ) {
                var rng = this.selection.getRange();
                if(rng.collapsed && this.queryCommandState(cmdName) != 1){
                    var node = this.document.createElement({
                        'bold':'strong',
                        'underline':'u',
                        'superscript':'sup',
                        'subscript':'sub',
                        'italic':'em',
                        'strikethrough':'strike'
                    }[cmdName]);
                    rng.insertNode(node).setStart(node,0).setCursor(false);
                    return true;
                }else{
                    return this.document.execCommand(cmdName)
                }

            },
            queryCommandState : function(cmdName) {
                if(browser.gecko){
                    return this.document.queryCommandState(cmdName)
                }
                var path = this.selection.getStartElementPath(),result = false;
                $.each(path,function(i,n){
                    switch (cmdName){
                        case 'bold':
                            if(n.nodeName == 'STRONG' || n.nodeName == 'B'){
                                result = 1;
                                return false;
                            }
                            break;
                        case 'underline':
                            if(n.nodeName == 'U' || n.nodeName == 'SPAN' && $(n).css('text-decoration') == 'underline'){
                                result = 1;
                                return false;
                            }
                            break;
                        case 'superscript':
                            if(n.nodeName == 'SUP'){
                                result = 1;
                                return false;
                            }
                            break;
                        case 'subscript':
                            if(n.nodeName == 'SUB'){
                                result = 1;
                                return false;
                            }
                            break;
                        case 'italic':
                            if(n.nodeName == 'EM' || n.nodeName == 'I'){
                                result = 1;
                                return false;
                            }
                            break;
                        case 'strikethrough':
                            if(n.nodeName == 'S' || n.nodeName == 'STRIKE' || n.nodeName == 'SPAN' && $(n).css('text-decoration') == 'line-through'){
                                result = 1;
                                return false;
                            }
                            break;
                    }
                })
                return result
            }
        };
    })
};


///import core
///import plugins/inserthtml.js
///commands ��Ƶ
///commandsName InsertVideo
///commandsTitle  ������Ƶ
///commandsDialog  dialogs\video
UM.plugins['video'] = function (){
    var me =this,
        div;

    /**
     * ����������Ƶ�ַ��
     * @param url ��Ƶ��ַ
     * @param width ��Ƶ���
     * @param height ��Ƶ�߶�
     * @param align ��Ƶ����
     * @param toEmbed �Ƿ���flash������ʾ
     * @param addParagraph  �Ƿ���Ҫ���P ��ǩ
     */
    function creatInsertStr(url,width,height,id,align,toEmbed){
        return  !toEmbed ?

                '<img ' + (id ? 'id="' + id+'"' : '') + ' width="'+ width +'" height="' + height + '" _url="'+url+'" class="edui-faked-video"'  +
                ' src="' + me.options.UMEDITOR_HOME_URL+'themes/default/images/spacer.gif" style="background:url('+me.options.UMEDITOR_HOME_URL+'themes/default/images/videologo.gif) no-repeat center center; border:1px solid gray;'+(align ? 'float:' + align + ';': '')+'" />'

                :
                '<embed type="application/x-shockwave-flash" class="edui-faked-video" pluginspage="http://www.macromedia.com/go/getflashplayer"' +
                ' src="' + url + '" width="' + width  + '" height="' + height  + '"'  + (align ? ' style="float:' + align + '"': '') +
                ' wmode="transparent" play="true" loop="false" menu="false" allowscriptaccess="never" allowfullscreen="true" >';
    }

    function switchImgAndEmbed(root,img2embed){
        utils.each(root.getNodesByTagName(img2embed ? 'img' : 'embed'),function(node){
            if(node.getAttr('class') == 'edui-faked-video'){

                var html = creatInsertStr( img2embed ? node.getAttr('_url') : node.getAttr('src'),node.getAttr('width'),node.getAttr('height'),null,node.getStyle('float') || '',img2embed);
                node.parentNode.replaceChild(UM.uNode.createElement(html),node)
            }
        })
    }

    me.addOutputRule(function(root){
        switchImgAndEmbed(root,true)
    });
    me.addInputRule(function(root){
        switchImgAndEmbed(root)
    });

    me.commands["insertvideo"] = {
        execCommand: function (cmd, videoObjs){
            videoObjs = utils.isArray(videoObjs)?videoObjs:[videoObjs];
            var html = [],id = 'tmpVedio';
            for(var i=0,vi,len = videoObjs.length;i<len;i++){
                 vi = videoObjs[i];
                 vi.url = utils.unhtml(vi.url, /[<">'](?:(amp|lt|quot|gt|#39|nbsp);)?/g);
                 html.push(creatInsertStr( vi.url, vi.width || 420,  vi.height || 280, id + i,vi.align,false));
            }
            me.execCommand("inserthtml",html.join(""),true);

        },
        queryCommandState : function(){
            var img = me.selection.getRange().getClosedNode(),
                flag = img && (img.className == "edui-faked-video");
            return flag ? 1 : 0;
        }
    };
};
///import core
///commands ȫѡ
///commandsName  SelectAll
///commandsTitle  ȫѡ
/**
 * ѡ������
 * @function
 * @name UM.execCommand
 * @param   {String}   cmdName    selectallѡ�б༭�������������
 * @author zhanyi
*/
UM.plugins['selectall'] = function(){
    var me = this;
    me.commands['selectall'] = {
        execCommand : function(){
            //ȥ����ԭ���selectAll,��Ϊ����ֱ���͵�����Ϊ��ʱ�����ܳ��ֱպ�״̬�Ĺ��
            var me = this,body = me.body,
                range = me.selection.getRange();
            range.selectNodeContents(body);
            if(domUtils.isEmptyBlock(body)){
                //opera�����Զ��ϲ���Ԫ�ص���ߣ�Ҫ�ֶ�����һ��
                if(browser.opera && body.firstChild && body.firstChild.nodeType == 1){
                    range.setStartAtFirst(body.firstChild);
                }
                range.collapse(true);
            }
            range.select(true);
        },
        notNeedUndo : 1
    };


    //��ݼ�
    me.addshortcutkey({
         "selectAll" : "ctrl+65"
    });
};

//UM.plugins['removeformat'] = function () {
//    var me = this;
//    me.commands['removeformat'] = {
//        execCommand: function () {
//            me.document.execCommand('removeformat');
//
//            /* ����ie8��firefoxѡ��������ʱ,����ʽ��bug */
//            if (browser.gecko || browser.ie8 || browser.webkit) {
//                var nativeRange = this.selection.getNative().getRangeAt(0),
//                    common = nativeRange.commonAncestorContainer,
//                    rng = me.selection.getRange(),
//                    bk = rng.createBookmark();
//
//                function isEleInBookmark(node, bk){
//                    if ( (domUtils.getPosition(node, bk.start) & domUtils.POSITION_FOLLOWING) &&
//                        (domUtils.getPosition(bk.end, node) & domUtils.POSITION_FOLLOWING) ) {
//                        return true;
//                    } else if ( (domUtils.getPosition(node, bk.start) & domUtils.POSITION_CONTAINS) ||
//                        (domUtils.getPosition(node, bk.end) & domUtils.POSITION_CONTAINS) ) {
//                        return true;
//                    }
//                    return false;
//                }
//
//                $(common).find('a').each(function (k, a) {
//                    if ( isEleInBookmark(a, bk) ) {
//                        a.removeAttribute('style');
//                    }
//                });
//
//            }
//        }
//    };
//
//};
//


UM.plugins['removeformat'] = function(){
    var me = this;
    me.setOpt({
        'removeFormatTags': 'b,big,code,del,dfn,em,font,i,ins,kbd,q,samp,small,span,strike,strong,sub,sup,tt,u,var',
        'removeFormatAttributes':'class,style,lang,width,height,align,hspace,valign'
    });
    me.commands['removeformat'] = {
        execCommand : function( cmdName, tags, style, attrs,notIncludeA ) {

            var tagReg = new RegExp( '^(?:' + (tags || this.options.removeFormatTags).replace( /,/g, '|' ) + ')$', 'i' ) ,
                removeFormatAttributes = style ? [] : (attrs || this.options.removeFormatAttributes).split( ',' ),
                range = new dom.Range( this.document ),
                bookmark,node,parent,
                filter = function( node ) {
                    return node.nodeType == 1;
                };

            function isRedundantSpan (node) {
                if (node.nodeType == 3 || node.tagName.toLowerCase() != 'span'){
                    return 0;
                }
                if (browser.ie) {
                    //ie ���ж�ʵЧ������ֻ�ܼ���style���ж�
                    //return node.style.cssText == '' ? 1 : 0;
                    var attrs = node.attributes;
                    if ( attrs.length ) {
                        for ( var i = 0,l = attrs.length; i<l; i++ ) {
                            if ( attrs[i].specified ) {
                                return 0;
                            }
                        }
                        return 1;
                    }
                }
                return !node.attributes.length;
            }
            function doRemove( range ) {

                var bookmark1 = range.createBookmark();
                if ( range.collapsed ) {
                    range.enlarge( true );
                }

                //���ܰ�a��ǩ����
                if(!notIncludeA){
                    var aNode = domUtils.findParentByTagName(range.startContainer,'a',true);
                    if(aNode){
                        range.setStartBefore(aNode);
                    }

                    aNode = domUtils.findParentByTagName(range.endContainer,'a',true);
                    if(aNode){
                        range.setEndAfter(aNode);
                    }

                }


                bookmark = range.createBookmark();

                node = bookmark.start;

                //�п�ʼ
                while ( (parent = node.parentNode) && !domUtils.isBlockElm( parent ) ) {
                    domUtils.breakParent( node, parent );
                    domUtils.clearEmptySibling( node );
                }
                if ( bookmark.end ) {
                    //�н���
                    node = bookmark.end;
                    while ( (parent = node.parentNode) && !domUtils.isBlockElm( parent ) ) {
                        domUtils.breakParent( node, parent );
                        domUtils.clearEmptySibling( node );
                    }

                    //��ʼȥ����ʽ
                    var current = domUtils.getNextDomNode( bookmark.start, false, filter ),
                        next;
                    while ( current ) {
                        if ( current == bookmark.end ) {
                            break;
                        }

                        next = domUtils.getNextDomNode( current, true, filter );

                        if ( !dtd.$empty[current.tagName.toLowerCase()] && !domUtils.isBookmarkNode( current ) ) {
                            if ( tagReg.test( current.tagName ) ) {
                                if ( style ) {
                                    domUtils.removeStyle( current, style );
                                    if ( isRedundantSpan( current ) && style != 'text-decoration'){
                                        domUtils.remove( current, true );
                                    }
                                } else {
                                    domUtils.remove( current, true );
                                }
                            } else {
                                //trace:939  ���ܰ�list�ϵ���ʽȥ��
                                if(!dtd.$tableContent[current.tagName] && !dtd.$list[current.tagName]){
                                    domUtils.removeAttributes( current, removeFormatAttributes );
                                    if ( isRedundantSpan( current ) ){
                                        domUtils.remove( current, true );
                                    }
                                }

                            }
                        }
                        current = next;
                    }
                }
                //trace:1035
                //trace:1096 ���ܰ�td�ϵ���ʽȥ��������߿�
                var pN = bookmark.start.parentNode;
                if(domUtils.isBlockElm(pN) && !dtd.$tableContent[pN.tagName] && !dtd.$list[pN.tagName]){
                    domUtils.removeAttributes(  pN,removeFormatAttributes );
                }
                pN = bookmark.end.parentNode;
                if(bookmark.end && domUtils.isBlockElm(pN) && !dtd.$tableContent[pN.tagName]&& !dtd.$list[pN.tagName]){
                    domUtils.removeAttributes(  pN,removeFormatAttributes );
                }
                range.moveToBookmark( bookmark ).moveToBookmark(bookmark1);
                //�������Ĵ��� <b><bookmark></b>
                var node = range.startContainer,
                    tmp,
                    collapsed = range.collapsed;
                while(node.nodeType == 1 && domUtils.isEmptyNode(node) && dtd.$removeEmpty[node.tagName]){
                    tmp = node.parentNode;
                    range.setStartBefore(node);
                    //trace:937
                    //���½���߽�
                    if(range.startContainer === range.endContainer){
                        range.endOffset--;
                    }
                    domUtils.remove(node);
                    node = tmp;
                }

                if(!collapsed){
                    node = range.endContainer;
                    while(node.nodeType == 1 && domUtils.isEmptyNode(node) && dtd.$removeEmpty[node.tagName]){
                        tmp = node.parentNode;
                        range.setEndBefore(node);
                        domUtils.remove(node);

                        node = tmp;
                    }


                }
            }



            range = this.selection.getRange();
            if(!range.collapsed) {
                doRemove( range );
                range.select();
            }

        }

    };

};
/*
 *   ���������ļ���������
 */
UM.plugins['keystrokes'] = function() {
    var me = this;
    var collapsed = true;
    me.addListener('keydown', function(type, evt) {
        var keyCode = evt.keyCode || evt.which,
            rng = me.selection.getRange();

        //����ȫѡ�����
        if(!rng.collapsed && !(evt.ctrlKey || evt.shiftKey || evt.altKey || evt.metaKey) && (keyCode >= 65 && keyCode <=90
            || keyCode >= 48 && keyCode <= 57 ||
            keyCode >= 96 && keyCode <= 111 || {
            13:1,
            8:1,
            46:1
        }[keyCode])
            ){

            var tmpNode = rng.startContainer;
            if(domUtils.isFillChar(tmpNode)){
                rng.setStartBefore(tmpNode)
            }
            tmpNode = rng.endContainer;
            if(domUtils.isFillChar(tmpNode)){
                rng.setEndAfter(tmpNode)
            }
            rng.txtToElmBoundary();
            //����߽���ܷŵ���br��ǰ�ߣ�Ҫ��br�����
            // x[xxx]<br/>
            if(rng.endContainer && rng.endContainer.nodeType == 1){
                tmpNode = rng.endContainer.childNodes[rng.endOffset];
                if(tmpNode && domUtils.isBr(tmpNode)){
                    rng.setEndAfter(tmpNode);
                }
            }
            if(rng.startOffset == 0){
                tmpNode = rng.startContainer;
                if(domUtils.isBoundaryNode(tmpNode,'firstChild') ){
                    tmpNode = rng.endContainer;
                    if(rng.endOffset == (tmpNode.nodeType == 3 ? tmpNode.nodeValue.length : tmpNode.childNodes.length) && domUtils.isBoundaryNode(tmpNode,'lastChild')){
                        me.fireEvent('saveScene');
                        me.body.innerHTML = '<p>'+(browser.ie ? '' : '<br/>')+'</p>';
                        rng.setStart(me.body.firstChild,0).setCursor(false,true);
                        me._selectionChange();
                        return;
                    }
                }
            }
        }

        //����backspace
        if (keyCode == 8) {
            rng = me.selection.getRange();
            collapsed = rng.collapsed;
            if(me.fireEvent('delkeydown',evt)){
                return;
            }
            var start,end;
            //���ⰴ����ɾ�������Ч������
            if(rng.collapsed && rng.inFillChar()){
                start = rng.startContainer;

                if(domUtils.isFillChar(start)){
                    rng.setStartBefore(start).shrinkBoundary(true).collapse(true);
                    domUtils.remove(start)
                }else{
                    start.nodeValue = start.nodeValue.replace(new RegExp('^' + domUtils.fillChar ),'');
                    rng.startOffset--;
                    rng.collapse(true).select(true)
                }
            }
            //���ѡ��controlԪ�ز���ɾ�������
            if (start = rng.getClosedNode()) {
                me.fireEvent('saveScene');
                rng.setStartBefore(start);
                domUtils.remove(start);
                rng.setCursor();
                me.fireEvent('saveScene');
                domUtils.preventDefault(evt);
                return;
            }
            //��ֹ��table�ϵ�ɾ��
            if (!browser.ie) {
                start = domUtils.findParentByTagName(rng.startContainer, 'table', true);
                end = domUtils.findParentByTagName(rng.endContainer, 'table', true);
                if (start && !end || !start && end || start !== end) {
                    evt.preventDefault();
                    return;
                }
            }
            start = rng.startContainer;
            if(rng.collapsed && start.nodeType == 1){
                var currentNode = start.childNodes[rng.startOffset-1];
                if(currentNode && currentNode.nodeType == 1 && currentNode.tagName == 'BR'){
                    me.fireEvent('saveScene');
                    rng.setStartBefore(currentNode).collapse(true);
                    domUtils.remove(currentNode);
                    rng.select();
                    me.fireEvent('saveScene');
                }
            }

            //trace:3613
            if(browser.chrome){
                if(rng.collapsed){

                    while(rng.startOffset == 0 && !domUtils.isEmptyBlock(rng.startContainer)){
                        rng.setStartBefore(rng.startContainer)
                    }
                    var pre = rng.startContainer.childNodes[rng.startOffset-1];
                    if(pre && pre.nodeName == 'BR'){
                        rng.setStartBefore(pre);
                        me.fireEvent('saveScene');
                        $(pre).remove();
                        rng.setCursor();
                        me.fireEvent('saveScene');
                    }

                }
            }
        }
        //trace:1634
        //ff��del���������յ�ʱ��Ҳ��ɾ��
        if(browser.gecko && keyCode == 46){
            var range = me.selection.getRange();
            if(range.collapsed){
                start = range.startContainer;
                if(domUtils.isEmptyBlock(start)){
                    var parent = start.parentNode;
                    while(domUtils.getChildCount(parent) == 1 && !domUtils.isBody(parent)){
                        start = parent;
                        parent = parent.parentNode;
                    }
                    if(start === parent.lastChild)
                        evt.preventDefault();
                    return;
                }
            }
        }
    });
    me.addListener('keyup', function(type, evt) {
        var keyCode = evt.keyCode || evt.which,
            rng,me = this;
        if(keyCode == 8){
            if(me.fireEvent('delkeyup')){
                return;
            }
            rng = me.selection.getRange();
            if(rng.collapsed){
                var tmpNode,
                    autoClearTagName = ['h1','h2','h3','h4','h5','h6'];
                if(tmpNode = domUtils.findParentByTagName(rng.startContainer,autoClearTagName,true)){
                    if(domUtils.isEmptyBlock(tmpNode)){
                        var pre = tmpNode.previousSibling;
                        if(pre && pre.nodeName != 'TABLE'){
                            domUtils.remove(tmpNode);
                            rng.setStartAtLast(pre).setCursor(false,true);
                            return;
                        }else{
                            var next = tmpNode.nextSibling;
                            if(next && next.nodeName != 'TABLE'){
                                domUtils.remove(tmpNode);
                                rng.setStartAtFirst(next).setCursor(false,true);
                                return;
                            }
                        }
                    }
                }
                //���?ɾ��bodyʱ��Ҫ���¸�p��ǩչλ
                if(domUtils.isBody(rng.startContainer)){
                    var tmpNode = domUtils.createElement(me.document,'p',{
                        'innerHTML' : browser.ie ? domUtils.fillChar : '<br/>'
                    });
                    rng.insertNode(tmpNode).setStart(tmpNode,0).setCursor(false,true);
                }
            }


            //chrome�����ɾ����inline��ǩ����������м��䣬���������ֻ��ǻ����ϸղ�ɾ��ı�ǩ������������ѡһ�ξͲ�����
            if( !collapsed && (rng.startContainer.nodeType == 3 || rng.startContainer.nodeType == 1 && domUtils.isEmptyBlock(rng.startContainer))){
                if(browser.ie){
                    var span = rng.document.createElement('span');
                    rng.insertNode(span).setStartBefore(span).collapse(true);
                    rng.select();
                    domUtils.remove(span)
                }else{
                    rng.select()
                }

            }
        }

    })
};
/**
 * �Զ�����ݸ�
 */
UM.plugins['autosave'] = function() {


    var me = this,
        //����ѭ������
        lastSaveTime = new Date(),
        //��С������ʱ��
        MIN_TIME = 20,
        //auto save key
        saveKey = null;


    //Ĭ�ϼ��ʱ��
    me.setOpt('saveInterval', 500);

    //�洢ý���װ
    var LocalStorage = UM.LocalStorage = ( function () {

        var storage = window.localStorage || getUserData() || null,
            LOCAL_FILE = "localStorage";

        return {

            saveLocalData: function ( key, data ) {

                if ( storage && data) {
                    storage.setItem( key, data  );
                    return true;
                }

                return false;

            },

            getLocalData: function ( key ) {

                if ( storage ) {
                    return storage.getItem( key );
                }

                return null;

            },

            removeItem: function ( key ) {

                storage && storage.removeItem( key );

            }

        };

        function getUserData () {

            var container = document.createElement( "div" );
            container.style.display = "none";

            if( !container.addBehavior ) {
                return null;
            }

            container.addBehavior("#default#userdata");

            return {

                getItem: function ( key ) {

                    var result = null;

                    try {
                        document.body.appendChild( container );
                        container.load( LOCAL_FILE );
                        result = container.getAttribute( key );
                        document.body.removeChild( container );
                    } catch ( e ) {
                    }

                    return result;

                },

                setItem: function ( key, value ) {

                    document.body.appendChild( container );
                    container.setAttribute( key, value );
                    container.save( LOCAL_FILE );
                    document.body.removeChild( container );

                },
//               ��ʱû���õ�
//                clear: function () {
//
//                    var expiresTime = new Date();
//                    expiresTime.setFullYear( expiresTime.getFullYear() - 1 );
//                    document.body.appendChild( container );
//                    container.expires = expiresTime.toUTCString();
//                    container.save( LOCAL_FILE );
//                    document.body.removeChild( container );
//
//                },

                removeItem: function ( key ) {

                    document.body.appendChild( container );
                    container.removeAttribute( key );
                    container.save( LOCAL_FILE );
                    document.body.removeChild( container );

                }

            };

        }

    } )();

    function save ( editor ) {

        var saveData = null;

        if ( new Date() - lastSaveTime < MIN_TIME ) {
            return;
        }

        if ( !editor.hasContents() ) {
            //���ﲻ�ܵ���������ɾ�� ������¼���ѭ��
            saveKey && LocalStorage.removeItem( saveKey );
            return;
        }

        lastSaveTime = new Date();

        editor._saveFlag = null;

        saveData = me.body.innerHTML;

        if ( editor.fireEvent( "beforeautosave", {
            content: saveData
        } ) === false ) {
            return;
        }

        LocalStorage.saveLocalData( saveKey, saveData );

        editor.fireEvent( "afterautosave", {
            content: saveData
        } );

    }

    me.addListener('ready', function(){
        var _suffix = "-drafts-data",
            key = null;

        if ( me.key ) {
            key = me.key + _suffix;
        } else {
            key = ( me.container.parentNode.id || 'ue-common' ) + _suffix;
        }

        //ҳ���ַ+�༭��ID ����Ψһ
        saveKey = ( location.protocol + location.host + location.pathname ).replace( /[.:\/]/g, '_' ) + key;
    });

    me.addListener('contentchange', function(){

        if ( !saveKey ) {
            return;
        }

        if ( me._saveFlag ) {
            window.clearTimeout( me._saveFlag );
        }

        if ( me.options.saveInterval > 0 ) {

            me._saveFlag = window.setTimeout( function () {

                save( me );

            }, me.options.saveInterval );

        } else {

            save(me);

        }

    })


    me.commands['clearlocaldata'] = {
        execCommand:function (cmd, name) {
            if ( saveKey && LocalStorage.getLocalData( saveKey ) ) {
                LocalStorage.removeItem( saveKey )
            }
        },
        notNeedUndo: true,
        ignoreContentChange:true
    };

    me.commands['getlocaldata'] = {
        execCommand:function (cmd, name) {
            return saveKey ? LocalStorage.getLocalData( saveKey ) || '' : '';
        },
        notNeedUndo: true,
        ignoreContentChange:true
    };

    me.commands['drafts'] = {
        execCommand:function (cmd, name) {
            if ( saveKey ) {
                me.body.innerHTML = LocalStorage.getLocalData( saveKey ) || '<p>'+(browser.ie ? '&nbsp;' : '<br/>')+'</p>';
                me.focus(true);
            }
        },
        queryCommandState: function () {
            return saveKey ? ( LocalStorage.getLocalData( saveKey ) === null ? -1 : 0 ) : -1;
        },
        notNeedUndo: true,
        ignoreContentChange:true
    }

};

/**
 * @description
 * 1.�Ϸ��ļ����༭�����Զ��ϴ������뵽ѡ��
 * 2.����ճ����ͼƬ���Զ��ϴ������뵽ѡ��
 * @author Jinqn
 * @date 2013-10-14
 */
UM.plugins['autoupload'] = function () {

    var me = this;

    me.setOpt('pasteImageEnabled', true);
    me.setOpt('dropFileEnabled', true);
    var sendAndInsertImage = function (file, editor) {
        //ģ�����
        var fd = new FormData();
        fd.append(editor.options.imageFieldName || 'upfile', file, file.name || ('blob.' + file.type.substr('image/'.length)));
        fd.append('type', 'ajax');
        var xhr = new XMLHttpRequest();
        xhr.open("post", me.options.imageUrl, true);
        xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
        xhr.addEventListener('load', function (e) {
            try {
                var json = eval('('+e.target.response+')'),
                    link = json.url,
                    picLink = me.options.imagePath + link;
                editor.execCommand('insertimage', {
                    src: picLink,
                    _src: picLink
                });
            } catch (er) {
            }
        });
        xhr.send(fd);
    };

    function getPasteImage(e) {
        return e.clipboardData && e.clipboardData.items && e.clipboardData.items.length == 1 && /^image\//.test(e.clipboardData.items[0].type) ? e.clipboardData.items : null;
    }

    function getDropImage(e) {
        return  e.dataTransfer && e.dataTransfer.files ? e.dataTransfer.files : null;
    }

    me.addListener('ready', function () {
        if (window.FormData && window.FileReader) {
            var autoUploadHandler = function (e) {
                var hasImg = false,
                    items;
                //��ȡճ����ļ��б�����Ϸ��ļ��б�
                items = e.type == 'paste' ? getPasteImage(e.originalEvent) : getDropImage(e.originalEvent);
                if (items) {
                    var len = items.length,
                        file;
                    while (len--) {
                        file = items[len];
                        if (file.getAsFile) file = file.getAsFile();
                        if (file && file.size > 0 && /image\/\w+/i.test(file.type)) {
                            sendAndInsertImage(file, me);
                            hasImg = true;
                        }
                    }
                    if (hasImg) return false;
                }

            };
            me.getOpt('pasteImageEnabled') && me.$body.on('paste', autoUploadHandler);
            me.getOpt('dropFileEnabled') && me.$body.on('drop', autoUploadHandler);

            //ȡ���Ϸ�ͼƬʱ���ֵ����ֹ��λ����ʾ
            me.$body.on('dragover', function (e) {
                if (e.originalEvent.dataTransfer.types[0] == 'Files') {
                    return false;
                }
            });
        }
    });

};
/**
 * ��ʽ���
 */
UM.plugins['formula'] = function () {
    var me = this;

    function getActiveIframe() {
        return me.$body.find('iframe.edui-formula-active')[0] || null;
    }

    function blurActiveIframe(){
        var iframe = getActiveIframe();
        iframe && iframe.contentWindow.formula.blur();
    }

    me.addInputRule(function (root) {
        $.each(root.getNodesByTagName('span'), function (i, node) {
            if (node.hasClass('mathquill-embedded-latex')) {
                var firstChild, latex = '';
                while(firstChild = node.firstChild()){
                    latex += firstChild.data;
                    node.removeChild(firstChild);
                }
                node.tagName = 'iframe';
                node.setAttr({
                    'frameborder': '0',
                    'src': me.getOpt('UMEDITOR_HOME_URL') + 'dialogs/formula/formula.html',
                    'data-latex': utils.unhtml(latex)
                });
            }
        });
    });
    me.addOutputRule(function (root) {
        $.each(root.getNodesByTagName('iframe'), function (i, node) {
            if (node.hasClass('mathquill-embedded-latex')) {
                node.tagName = 'span';
                node.appendChild(UM.uNode.createText(node.getAttr('data-latex')));
                node.setAttr({
                    'frameborder': '',
                    'src': '',
                    'data-latex': ''
                });
            }
        });
    });
    me.addListener('click', function(){
        blurActiveIframe();
    });
    me.addListener('afterexeccommand', function(type, cmd){
        if(cmd != 'formula') {
            blurActiveIframe();
        }
    });

    me.commands['formula'] = {
        execCommand: function (cmd, latex) {
            var iframe = getActiveIframe();
            if (iframe) {
                iframe.contentWindow.formula.insertLatex(latex);
            } else {
                me.execCommand('inserthtml', '<span class="mathquill-embedded-latex">' + latex + '</span>');
                browser.ie && browser.ie9below && setTimeout(function(){
                    var rng = me.selection.getRange(),
                        startContainer = rng.startContainer;
                    if(startContainer.nodeType == 1 && !startContainer.childNodes[rng.startOffset]){
                        rng.insertNode(me.document.createTextNode(' '));
                        rng.setCursor()
                    }
                },100)
            }
        },
        queryCommandState: function (cmd) {
            return 0;
        },
        queryCommandValue: function (cmd) {
            var iframe = getActiveIframe();
            return iframe && iframe.contentWindow.formula.getLatex();
        }
    }

};

/**
 * @file xssFilter.js
 * @desc xss������
 * @author robbenmu
 */

UM.plugins.xssFilter = function() {

	var config = UMEDITOR_CONFIG;
	var whiteList = config.whiteList;

	function filter(node) {

		var tagName = node.tagName;
		var attrs = node.attrs;

		if (!whiteList.hasOwnProperty(tagName)) {
			node.parentNode.removeChild(node);
			return false;
		}

		UM.utils.each(attrs, function (val, key) {

			if (whiteList[tagName].indexOf(key) === -1) {
				node.setAttr(key);
			}
		});
	}

	// ���inserthtml\paste�Ȳ����õĹ��˹���
	if (whiteList && config.xssFilterRules) {
		this.options.filterRules = function () {

			var result = {};

			UM.utils.each(whiteList, function(val, key) {
				result[key] = function (node) {
					return filter(node);
				};
			});

			return result;
		}();
	}

	var tagList = [];

	UM.utils.each(whiteList, function (val, key) {
		tagList.push(key);
	});

	// ���input���˹���
	//
	if (whiteList && config.inputXssFilter) {
		this.addInputRule(function (root) {

			root.traversal(function(node) {
				if (node.type !== 'element') {
					return false;
				}
				filter(node);
			});
		});
	}
	// ���output���˹���
	//
	if (whiteList && config.outputXssFilter) {
		this.addOutputRule(function (root) {

			root.traversal(function(node) {
				if (node.type !== 'element') {
					return false;
				}
				filter(node);
			});
		});
	}

};
(function ($) {
    //��jquery����չ
    $.parseTmpl = function parse(str, data) {
        var tmpl = 'var __p=[],print=function(){__p.push.apply(__p,arguments);};' + 'with(obj||{}){__p.push(\'' + str.replace(/\\/g, '\\\\').replace(/'/g, "\\'").replace(/<%=([\s\S]+?)%>/g,function (match, code) {
            return "',obj." + code.replace(/\\'/g, "'") + ",'";
        }).replace(/<%([\s\S]+?)%>/g,function (match, code) {
                return "');" + code.replace(/\\'/g, "'").replace(/[\r\n\t]/g, ' ') + "__p.push('";
            }).replace(/\r/g, '\\r').replace(/\n/g, '\\n').replace(/\t/g, '\\t') + "');}return __p.join('');";
        var func = new Function('obj', tmpl);
        return data ? func(data) : func;
    };
    $.extend2 = function (t, s) {
        var a = arguments,
            notCover = $.type(a[a.length - 1]) == 'boolean' ? a[a.length - 1] : false,
            len = $.type(a[a.length - 1]) == 'boolean' ? a.length - 1 : a.length;
        for (var i = 1; i < len; i++) {
            var x = a[i];
            for (var k in x) {
                if (!notCover || !t.hasOwnProperty(k)) {
                    t[k] = x[k];
                }
            }
        }
        return t;
    };

    $.IE6 = !!window.ActiveXObject && parseFloat(navigator.userAgent.match(/msie (\d+)/i)[1]) == 6;

    //����ui�Ļ���
    var _eventHandler = [];
    var _widget = function () {
    };
    var _prefix = 'edui';
    _widget.prototype = {
        on: function (ev, cb) {
            this.root().on(ev, $.proxy(cb, this));
            return this;
        },
        off: function (ev, cb) {
            this.root().off(ev, $.proxy(cb, this));
            return this;
        },
        trigger: function (ev, data) {
            return  this.root().trigger(ev, data) === false ? false : this;
        },
        root: function ($el) {
            return this._$el || (this._$el = $el);
        },
        destroy: function () {

        },
        data: function (key, val) {
            if (val !== undefined) {
                this.root().data(_prefix + key, val);
                return this;
            } else {
                return this.root().data(_prefix + key)
            }
        },
        register: function (eventName, $el, fn) {
            _eventHandler.push({
                'evtname': eventName,
                '$els': $.isArray($el) ? $el : [$el],
                handler: $.proxy(fn, $el)
            })
        }
    };

    //��jqʵ�����õ��󶨵�widgetʵ��
    $.fn.edui = function (obj) {
        return obj ? this.data('eduiwidget', obj) : this.data('eduiwidget');
    };

    function _createClass(ClassObj, properties, supperClass) {
        ClassObj.prototype = $.extend2(
            $.extend({}, properties),
            (UM.ui[supperClass] || _widget).prototype,
            true
        );
        ClassObj.prototype.supper = (UM.ui[supperClass] || _widget).prototype;
        //��class��defaultOpt �ϲ�
        if( UM.ui[supperClass] && UM.ui[supperClass].prototype.defaultOpt ) {

            var parentDefaultOptions = UM.ui[supperClass].prototype.defaultOpt,
                subDefaultOptions = ClassObj.prototype.defaultOpt;

            ClassObj.prototype.defaultOpt = $.extend( {}, parentDefaultOptions, subDefaultOptions || {} );

        }
        return ClassObj
    }

    var _guid = 1;

    function mergeToJQ(ClassObj, className) {
        $[_prefix + className] = ClassObj;
        $.fn[_prefix + className] = function (opt) {
            var result, args = Array.prototype.slice.call(arguments, 1);

            this.each(function (i, el) {
                var $this = $(el);
                var obj = $this.edui();
                if (!obj) {
                    ClassObj(!opt || !$.isPlainObject(opt) ? {} : opt, $this);
                    $this.edui(obj)
                }
                if ($.type(opt) == 'string') {
                    if (opt == 'this') {
                        result = obj;
                    } else {
                        result = obj[opt].apply(obj, args);
                        if (result !== obj && result !== undefined) {
                            return false;
                        }
                        result = null;
                    }

                }
            });

            return result !== null ? result : this;
        }
    }

    UM.ui = {
        define: function (className, properties, supperClass) {
            var ClassObj = UM.ui[className] = _createClass(function (options, $el) {
                    var _obj = function () {
                    };
                    $.extend(_obj.prototype, ClassObj.prototype, {
                            guid: className + _guid++,
                            widgetName: className
                        }
                    );
                    var obj = new _obj;
                    if ($.type(options) == 'string') {
                        obj.init && obj.init({});
                        obj.root().edui(obj);
                        obj.root().find('a').click(function (evt) {
                            evt.preventDefault()
                        });
                        return obj.root()[_prefix + className].apply(obj.root(), arguments)
                    } else {
                        $el && obj.root($el);
                        obj.init && obj.init(!options || $.isPlainObject(options) ? $.extend2(options || {}, obj.defaultOpt || {}, true) : options);
                        try{
                            obj.root().find('a').click(function (evt) {
                                evt.preventDefault()
                            });
                        }catch(e){
                        }

                        return obj.root().edui(obj);
                    }

                },properties, supperClass);

            mergeToJQ(ClassObj, className);
        }
    };

    $(function () {
        $(document).on('click mouseup mousedown dblclick mouseover', function (evt) {
            $.each(_eventHandler, function (i, obj) {
                if (obj.evtname == evt.type) {
                    $.each(obj.$els, function (i, $el) {
                        if ($el[0] !== evt.target && !$.contains($el[0], evt.target)) {
                            obj.handler(evt);
                        }
                    })
                }
            })
        })
    })
})(jQuery);
//button ��
UM.ui.define('button', {
    tpl: '<<%if(!texttype){%>div class="edui-btn edui-btn-<%=icon%> <%if(name){%>edui-btn-name-<%=name%><%}%>" unselectable="on" onmousedown="return false" <%}else{%>a class="edui-text-btn"<%}%><% if(title) {%> data-original-title="<%=title%>" <%};%>> ' +
        '<% if(icon) {%><div unselectable="on" class="edui-icon-<%=icon%> edui-icon"></div><% }; %><%if(text) {%><span unselectable="on" onmousedown="return false" class="edui-button-label"><%=text%></span><%}%>' +
        '<%if(caret && text){%><span class="edui-button-spacing"></span><%}%>' +
        '<% if(caret) {%><span unselectable="on" onmousedown="return false" class="edui-caret"></span><% };%></<%if(!texttype){%>div<%}else{%>a<%}%>>',
    defaultOpt: {
        text: '',
        title: '',
        icon: '',
        width: '',
        caret: false,
        texttype: false,
        click: function () {
        }
    },
    init: function (options) {
        var me = this;

        me.root($($.parseTmpl(me.tpl, options)))
            .click(function (evt) {
                me.wrapclick(options.click, evt)
            });

        me.root().hover(function () {
            if(!me.root().hasClass("edui-disabled")){
                me.root().toggleClass('edui-hover')
            }
        })

        return me;
    },
    wrapclick: function (fn, evt) {
        if (!this.disabled()) {
            this.root().trigger('wrapclick');
            $.proxy(fn, this, evt)()
        }
        return this;
    },
    label: function (text) {
        if (text === undefined) {
            return this.root().find('.edui-button-label').text();
        } else {
            this.root().find('.edui-button-label').text(text);
            return this;
        }
    },
    disabled: function (state) {
        if (state === undefined) {
            return this.root().hasClass('edui-disabled')
        }
        this.root().toggleClass('edui-disabled', state);
        if(this.root().hasClass('edui-disabled')){
            this.root().removeClass('edui-hover')
        }
        return this;
    },
    active: function (state) {
        if (state === undefined) {
            return this.root().hasClass('edui-active')
        }
        this.root().toggleClass('edui-active', state)

        return this;
    },
    mergeWith: function ($obj) {
        var me = this;
        me.data('$mergeObj', $obj);
        $obj.edui().data('$mergeObj', me.root());
        if (!$.contains(document.body, $obj[0])) {
            $obj.appendTo(me.root());
        }
        me.on('click',function () {
            me.wrapclick(function () {
                $obj.edui().show();
            })
        }).register('click', me.root(), function (evt) {
                $obj.hide()
            });
    }
});
//toolbar ��
(function () {
    UM.ui.define('toolbar', {
        tpl: '<div class="edui-toolbar"  ><div class="edui-btn-toolbar" unselectable="on" onmousedown="return false"  ></div></div>'
          ,
        init: function () {
            var $root = this.root($(this.tpl));
            this.data('$btnToolbar', $root.find('.edui-btn-toolbar'))
        },
        appendToBtnmenu : function(data){
            var $cont = this.data('$btnToolbar');
            data = $.isArray(data) ? data : [data];
            $.each(data,function(i,$item){
                $cont.append($item)
            })
        }
    });
})();

//menu ��
UM.ui.define('menu',{
    show : function($obj,dir,fnname,topOffset,leftOffset){

        fnname = fnname || 'position';
        if(this.trigger('beforeshow') === false){
            return;
        }else{
            this.root().css($.extend({display:'block'},$obj ? {
                top : $obj[fnname]().top + ( dir == 'right' ? 0 : $obj.outerHeight()) - (topOffset || 0),
                left : $obj[fnname]().left + (dir == 'right' ?  $obj.outerWidth() : 0) -  (leftOffset || 0)
            }:{}))
            this.trigger('aftershow');
        }
    },
    hide : function(all){
        var $parentmenu;
        if(this.trigger('beforehide') === false){
            return;
        } else {

            if($parentmenu = this.root().data('parentmenu')){
                if($parentmenu.data('parentmenu')|| all)
                    $parentmenu.edui().hide();
            }
            this.root().css('display','none');
            this.trigger('afterhide');
        }
    },
    attachTo : function($obj){
        var me = this;
        if(!$obj.data('$mergeObj')){
            $obj.data('$mergeObj',me.root());
            $obj.on('wrapclick',function(evt){
                me.show()
            });
            me.register('click',$obj,function(evt){
               me.hide()
            });
            me.data('$mergeObj',$obj)
        }
    }
});
//dropmenu ��
UM.ui.define('dropmenu', {
    tmpl: '<ul class="edui-dropdown-menu" aria-labelledby="dropdownMenu" >' +
        '<%for(var i=0,ci;ci=data[i++];){%>' +
        '<%if(ci.divider){%><li class="edui-divider"></li><%}else{%>' +
        '<li <%if(ci.active||ci.disabled){%>class="<%= ci.active|| \'\' %> <%=ci.disabled||\'\' %>" <%}%> data-value="<%= ci.value%>">' +
        '<a href="#" tabindex="-1"><em class="edui-dropmenu-checkbox"><i class="edui-icon-ok"></i></em><%= ci.label%></a>' +
        '</li><%}%>' +
        '<%}%>' +
        '</ul>',
    defaultOpt: {
        data: [],
        click: function () {

        }
    },
    init: function (options) {
        var me = this;
        var eventName = {
            click: 1,
            mouseover: 1,
            mouseout: 1
        };

        this.root($($.parseTmpl(this.tmpl, options))).on('click', 'li[class!="edui-disabled edui-divider edui-dropdown-submenu"]',function (evt) {
            $.proxy(options.click, me, evt, $(this).data('value'), $(this))()
        }).find('li').each(function (i, el) {
                var $this = $(this);
                if (!$this.hasClass("edui-disabled edui-divider edui-dropdown-submenu")) {
                    var data = options.data[i];
                    $.each(eventName, function (k) {
                        data[k] && $this[k](function (evt) {
                            $.proxy(data[k], el)(evt, data, me.root)
                        })
                    })
                }
            })

    },
    disabled: function (cb) {
        $('li[class!=edui-divider]', this.root()).each(function () {
            var $el = $(this);
            if (cb === true) {
                $el.addClass('edui-disabled')
            } else if ($.isFunction(cb)) {
                $el.toggleClass('edui-disabled', cb(li))
            } else {
                $el.removeClass('edui-disabled')
            }

        });
    },
    val: function (val) {
        var currentVal;
        $('li[class!="edui-divider edui-disabled edui-dropdown-submenu"]', this.root()).each(function () {
            var $el = $(this);
            if (val === undefined) {
                if ($el.find('em.edui-dropmenu-checked').length) {
                    currentVal = $el.data('value');
                    return false
                }
            } else {
                $el.find('em').toggleClass('edui-dropmenu-checked', $el.data('value') == val)
            }
        });
        if (val === undefined) {
            return currentVal
        }
    },
    addSubmenu: function (label, menu, index) {
        index = index || 0;

        var $list = $('li[class!=edui-divider]', this.root());
        var $node = $('<li class="edui-dropdown-submenu"><a tabindex="-1" href="#">' + label + '</a></li>').append(menu);

        if (index >= 0 && index < $list.length) {
            $node.insertBefore($list[index]);
        } else if (index < 0) {
            $node.insertBefore($list[0]);
        } else if (index >= $list.length) {
            $node.appendTo($list);
        }
    }
}, 'menu');
//splitbutton ��
///import button
UM.ui.define('splitbutton',{
    tpl :'<div class="edui-splitbutton <%if (name){%>edui-splitbutton-<%= name %><%}%>"  unselectable="on" <%if(title){%>data-original-title="<%=title%>"<%}%>><div class="edui-btn"  unselectable="on" ><%if(icon){%><div  unselectable="on" class="edui-icon-<%=icon%> edui-icon"></div><%}%><%if(text){%><%=text%><%}%></div>'+
            '<div  unselectable="on" class="edui-btn edui-dropdown-toggle" >'+
                '<div  unselectable="on" class="edui-caret"><\/div>'+
            '</div>'+
        '</div>',
    defaultOpt:{
        text:'',
        title:'',
        click:function(){}
    },
    init : function(options){
        var me = this;
        me.root( $($.parseTmpl(me.tpl,options)));
        me.root().find('.edui-btn:first').click(function(evt){
            if(!me.disabled()){
                $.proxy(options.click,me)();
            }
        });
        me.root().find('.edui-dropdown-toggle').click(function(){
            if(!me.disabled()){
                me.trigger('arrowclick')
            }
        });
        me.root().hover(function () {
            if(!me.root().hasClass("edui-disabled")){
                me.root().toggleClass('edui-hover')
            }
        });

        return me;
    },
    wrapclick:function(fn,evt){
        if(!this.disabled()){
            $.proxy(fn,this,evt)()
        }
        return this;
    },
    disabled : function(state){
        if(state === undefined){
            return this.root().hasClass('edui-disabled')
        }
        this.root().toggleClass('edui-disabled',state).find('.edui-btn').toggleClass('edui-disabled',state);
        return this;
    },
    active:function(state){
        if(state === undefined){
            return this.root().hasClass('edui-active')
        }
        this.root().toggleClass('edui-active',state).find('.edui-btn:first').toggleClass('edui-active',state);
        return this;
    },
    mergeWith:function($obj){
        var me = this;
        me.data('$mergeObj',$obj);
        $obj.edui().data('$mergeObj',me.root());
        if(!$.contains(document.body,$obj[0])){
            $obj.appendTo(me.root());
        }
        me.root().delegate('.edui-dropdown-toggle','click',function(){
            me.wrapclick(function(){
                $obj.edui().show();
            })
        });
        me.register('click',me.root().find('.edui-dropdown-toggle'),function(evt){
            $obj.hide()
        });
    }
});
/**
 * Created with JetBrains PhpStorm.
 * User: hn
 * Date: 13-7-10
 * Time: ����3:07
 * To change this template use File | Settings | File Templates.
 */
UM.ui.define('colorsplitbutton',{

    tpl : '<div class="edui-splitbutton <%if (name){%>edui-splitbutton-<%= name %><%}%>"  unselectable="on" <%if(title){%>data-original-title="<%=title%>"<%}%>><div class="edui-btn"  unselectable="on" ><%if(icon){%><div  unselectable="on" class="edui-icon-<%=icon%> edui-icon"></div><%}%><div class="edui-splitbutton-color-label" <%if (color) {%>style="background: <%=color%>"<%}%>></div><%if(text){%><%=text%><%}%></div>'+
            '<div  unselectable="on" class="edui-btn edui-dropdown-toggle" >'+
            '<div  unselectable="on" class="edui-caret"><\/div>'+
            '</div>'+
            '</div>',
    defaultOpt: {
        color: ''
    },
    init: function( options ){

        var me = this;

        me.supper.init.call( me, options );

    },
    colorLabel: function(){
        return this.root().find('.edui-splitbutton-color-label');
    }

}, 'splitbutton');
//popup ��
UM.ui.define('popup', {
    tpl: '<div class="edui-dropdown-menu edui-popup"'+
        '<%if(!<%=stopprop%>){%>onmousedown="return false"<%}%>'+
        '><div class="edui-popup-body" unselectable="on" onmousedown="return false"><%=subtpl%></div>' +
        '<div class="edui-popup-caret"></div>' +
        '</div>',
    defaultOpt: {
        stopprop:false,
        subtpl: '',
        width: '',
        height: ''
    },
    init: function (options) {
        this.root($($.parseTmpl(this.tpl, options)));
        return this;
    },
    mergeTpl: function (data) {
        return $.parseTmpl(this.tpl, {subtpl: data});
    },
    show: function ($obj, posObj) {
        if (!posObj) posObj = {};

        var fnname = posObj.fnname || 'position';
        if (this.trigger('beforeshow') === false) {
            return;
        } else {
            this.root().css($.extend({display: 'block'}, $obj ? {
                top: $obj[fnname]().top + ( posObj.dir == 'right' ? 0 : $obj.outerHeight()) - (posObj.offsetTop || 0),
                left: $obj[fnname]().left + (posObj.dir == 'right' ? $obj.outerWidth() : 0) - (posObj.offsetLeft || 0),
                position: 'absolute'
            } : {}));

            this.root().find('.edui-popup-caret').css({
                top: posObj.caretTop || 0,
                left: posObj.caretLeft || 0,
                position: 'absolute'
            }).addClass(posObj.caretDir || "up")

        }
        this.trigger("aftershow");
    },
    hide: function () {
        this.root().css('display', 'none');
        this.trigger('afterhide')
    },
    attachTo: function ($obj, posObj) {
        var me = this
        if (!$obj.data('$mergeObj')) {
            $obj.data('$mergeObj', me.root());
            $obj.on('wrapclick', function (evt) {
                me.show($obj, posObj)
            });
            me.register('click', $obj, function (evt) {
                me.hide()
            });
            me.data('$mergeObj', $obj)
        }
    },
    getBodyContainer: function () {
        return this.root().find(".edui-popup-body");
    }
});
//scale ��
UM.ui.define('scale', {
    tpl: '<div class="edui-scale" unselectable="on">' +
        '<span class="edui-scale-hand0"></span>' +
        '<span class="edui-scale-hand1"></span>' +
        '<span class="edui-scale-hand2"></span>' +
        '<span class="edui-scale-hand3"></span>' +
        '<span class="edui-scale-hand4"></span>' +
        '<span class="edui-scale-hand5"></span>' +
        '<span class="edui-scale-hand6"></span>' +
        '<span class="edui-scale-hand7"></span>' +
        '</div>',
    defaultOpt: {
        $doc: $(document),
        $wrap: $(document)
    },
    init: function (options) {
        if(options.$doc) this.defaultOpt.$doc = options.$doc;
        if(options.$wrap) this.defaultOpt.$wrap = options.$wrap;
        this.root($($.parseTmpl(this.tpl, options)));
        this.initStyle();
        this.startPos = this.prePos = {x: 0, y: 0};
        this.dragId = -1;
        return this;
    },
    initStyle: function () {
        utils.cssRule('edui-style-scale', '.edui-scale{display:none;position:absolute;border:1px solid #38B2CE;cursor:hand;}' +
            '.edui-scale span{position:absolute;left:0;top:0;width:7px;height:7px;overflow:hidden;font-size:0px;display:block;background-color:#3C9DD0;}'
            + '.edui-scale .edui-scale-hand0{cursor:nw-resize;top:0;margin-top:-4px;left:0;margin-left:-4px;}'
            + '.edui-scale .edui-scale-hand1{cursor:n-resize;top:0;margin-top:-4px;left:50%;margin-left:-4px;}'
            + '.edui-scale .edui-scale-hand2{cursor:ne-resize;top:0;margin-top:-4px;left:100%;margin-left:-3px;}'
            + '.edui-scale .edui-scale-hand3{cursor:w-resize;top:50%;margin-top:-4px;left:0;margin-left:-4px;}'
            + '.edui-scale .edui-scale-hand4{cursor:e-resize;top:50%;margin-top:-4px;left:100%;margin-left:-3px;}'
            + '.edui-scale .edui-scale-hand5{cursor:sw-resize;top:100%;margin-top:-3px;left:0;margin-left:-4px;}'
            + '.edui-scale .edui-scale-hand6{cursor:s-resize;top:100%;margin-top:-3px;left:50%;margin-left:-4px;}'
            + '.edui-scale .edui-scale-hand7{cursor:se-resize;top:100%;margin-top:-3px;left:100%;margin-left:-3px;}');
    },
    _eventHandler: function (e) {
        var me = this,
            $doc = me.defaultOpt.$doc;
        switch (e.type) {
            case 'mousedown':
                var hand = e.target || e.srcElement, hand;
                if (hand.className.indexOf('edui-scale-hand') != -1) {
                    me.dragId = hand.className.slice(-1);
                    me.startPos.x = me.prePos.x = e.clientX;
                    me.startPos.y = me.prePos.y = e.clientY;
                    $doc.bind('mousemove', $.proxy(me._eventHandler, me));
                }
                break;
            case 'mousemove':
                if (me.dragId != -1) {
                    me.updateContainerStyle(me.dragId, {x: e.clientX - me.prePos.x, y: e.clientY - me.prePos.y});
                    me.prePos.x = e.clientX;
                    me.prePos.y = e.clientY;
                    me.updateTargetElement();
                }
                break;
            case 'mouseup':
                if (me.dragId != -1) {
                    me.dragId = -1;
                    me.updateTargetElement();
                    var $target = me.data('$scaleTarget');
                    if ($target.parent()) me.attachTo(me.data('$scaleTarget'));
                }
                $doc.unbind('mousemove', $.proxy(me._eventHandler, me));
                break;
            default:
                break;
        }
    },
    updateTargetElement: function () {
        var me = this,
            $root = me.root(),
            $target = me.data('$scaleTarget');
        $target.css({width: $root.width(), height: $root.height()});
        me.attachTo($target);
    },
    updateContainerStyle: function (dir, offset) {
        var me = this,
            $dom = me.root(),
            tmp,
            rect = [
                //[left, top, width, height]
                [0, 0, -1, -1],
                [0, 0, 0, -1],
                [0, 0, 1, -1],
                [0, 0, -1, 0],
                [0, 0, 1, 0],
                [0, 0, -1, 1],
                [0, 0, 0, 1],
                [0, 0, 1, 1]
            ];

        if (rect[dir][0] != 0) {
            tmp = parseInt($dom.offset().left) + offset.x;
            $dom.css('left', me._validScaledProp('left', tmp));
        }
        if (rect[dir][1] != 0) {
            tmp = parseInt($dom.offset().top) + offset.y;
            $dom.css('top', me._validScaledProp('top', tmp));
        }
        if (rect[dir][2] != 0) {
            tmp = $dom.width() + rect[dir][2] * offset.x;
            $dom.css('width', me._validScaledProp('width', tmp));
        }
        if (rect[dir][3] != 0) {
            tmp = $dom.height() + rect[dir][3] * offset.y;
            $dom.css('height', me._validScaledProp('height', tmp));
        }
    },
    _validScaledProp: function (prop, value) {
        var $ele = this.root(),
            $wrap = this.defaultOpt.$doc,
            calc = function(val, a, b){
                return (val + a) > b ? b - a : value;
            };

        value = isNaN(value) ? 0 : value;
        switch (prop) {
            case 'left':
                return value < 0 ? 0 : calc(value, $ele.width(), $wrap.width());
            case 'top':
                return value < 0 ? 0 : calc(value, $ele.height(),$wrap.height());
            case 'width':
                return value <= 0 ? 1 : calc(value, $ele.offset().left, $wrap.width());
            case 'height':
                return value <= 0 ? 1 : calc(value, $ele.offset().top, $wrap.height());
        }
    },
    show: function ($obj) {
        var me = this;
        if ($obj) me.attachTo($obj);
        me.root().bind('mousedown', $.proxy(me._eventHandler, me));
        me.defaultOpt.$doc.bind('mouseup', $.proxy(me._eventHandler, me));
        me.root().show();
        me.trigger("aftershow");
    },
    hide: function () {
        var me = this;
        me.root().unbind('mousedown', $.proxy(me._eventHandler, me));
        me.defaultOpt.$doc.unbind('mouseup', $.proxy(me._eventHandler, me));
        me.root().hide();
        me.trigger('afterhide')
    },
    attachTo: function ($obj) {
        var me = this,
            imgPos = $obj.offset(),
            $root = me.root(),
            $wrap = me.defaultOpt.$wrap,
            posObj = $wrap.offset();

        me.data('$scaleTarget', $obj);
        me.root().css({
            position: 'absolute',
            width: $obj.width(),
            height: $obj.height(),
            left: imgPos.left - posObj.left - parseInt($wrap.css('border-left-width')) - parseInt($root.css('border-left-width')),
            top: imgPos.top - posObj.top - parseInt($wrap.css('border-top-width')) - parseInt($root.css('border-top-width'))
        });
    },
    getScaleTarget: function () {
        return this.data('$scaleTarget')[0];
    }
});
//colorpicker ��
UM.ui.define('colorpicker', {
    tpl: function (opt) {
        var COLORS = (
            'ffffff,000000,eeece1,1f497d,4f81bd,c0504d,9bbb59,8064a2,4bacc6,f79646,' +
                'f2f2f2,7f7f7f,ddd9c3,c6d9f0,dbe5f1,f2dcdb,ebf1dd,e5e0ec,dbeef3,fdeada,' +
                'd8d8d8,595959,c4bd97,8db3e2,b8cce4,e5b9b7,d7e3bc,ccc1d9,b7dde8,fbd5b5,' +
                'bfbfbf,3f3f3f,938953,548dd4,95b3d7,d99694,c3d69b,b2a2c7,92cddc,fac08f,' +
                'a5a5a5,262626,494429,17365d,366092,953734,76923c,5f497a,31859b,e36c09,' +
                '7f7f7f,0c0c0c,1d1b10,0f243e,244061,632423,4f6128,3f3151,205867,974806,' +
                'c00000,ff0000,ffc000,ffff00,92d050,00b050,00b0f0,0070c0,002060,7030a0,').split(',');

        var html = '<div unselectable="on" onmousedown="return false" class="edui-colorpicker<%if (name){%> edui-colorpicker-<%=name%><%}%>" >' +
            '<table unselectable="on" onmousedown="return false">' +
            '<tr><td colspan="10">'+opt.lang_themeColor+'</td> </tr>' +
            '<tr class="edui-colorpicker-firstrow" >';

        for (var i = 0; i < COLORS.length; i++) {
            if (i && i % 10 === 0) {
                html += '</tr>' + (i == 60 ? '<tr><td colspan="10">'+opt.lang_standardColor+'</td></tr>' : '') + '<tr' + (i == 60 ? ' class="edui-colorpicker-firstrow"' : '') + '>';
            }
            html += i < 70 ? '<td><a unselectable="on" onmousedown="return false" title="' + COLORS[i] + '" class="edui-colorpicker-colorcell"' +
                ' data-color="#' + COLORS[i] + '"' +
                ' style="background-color:#' + COLORS[i] + ';border:solid #ccc;' +
                (i < 10 || i >= 60 ? 'border-width:1px;' :
                    i >= 10 && i < 20 ? 'border-width:1px 1px 0 1px;' :
                        'border-width:0 1px 0 1px;') +
                '"' +
                '></a></td>' : '';
        }
        html += '</tr></table></div>';
        return html;
    },
    init: function (options) {
        var me = this;
        me.root($($.parseTmpl(me.supper.mergeTpl(me.tpl(options)),options)));

        me.root().on("click",function (e) {
            me.trigger('pickcolor',  $(e.target).data('color'));
        });
    }
}, 'popup');
/**
 * Created with JetBrains PhpStorm.
 * User: hn
 * Date: 13-5-29
 * Time: ����8:01
 * To change this template use File | Settings | File Templates.
 */

(function(){

    var widgetName = 'combobox',
        itemClassName = 'edui-combobox-item',
        HOVER_CLASS = 'edui-combobox-item-hover',
        ICON_CLASS = 'edui-combobox-checked-icon',
        labelClassName = 'edui-combobox-item-label';

    UM.ui.define( widgetName, ( function(){

        return {
            tpl: "<ul class=\"dropdown-menu edui-combobox-menu<%if (comboboxName!=='') {%> edui-combobox-<%=comboboxName%><%}%>\" unselectable=\"on\" onmousedown=\"return false\" role=\"menu\" aria-labelledby=\"dropdownMenu\">" +
                "<%if(autoRecord) {%>" +
                "<%for( var i=0, len = recordStack.length; i<len; i++ ) {%>" +
                "<%var index = recordStack[i];%>" +
                "<li class=\"<%=itemClassName%><%if( selected == index ) {%> edui-combobox-checked<%}%>\" data-item-index=\"<%=index%>\" unselectable=\"on\" onmousedown=\"return false\">" +
                "<span class=\"edui-combobox-icon\" unselectable=\"on\" onmousedown=\"return false\"></span>" +
                "<label class=\"<%=labelClassName%>\" style=\"<%=itemStyles[ index ]%>\" unselectable=\"on\" onmousedown=\"return false\"><%=items[index]%></label>" +
                "</li>" +
                "<%}%>" +
                "<%if( i ) {%>" +
                "<li class=\"edui-combobox-item-separator\"></li>" +
                "<%}%>" +
                "<%}%>" +
                "<%for( var i=0, label; label = items[i]; i++ ) {%>" +
                "<li class=\"<%=itemClassName%><%if( selected == i ) {%> edui-combobox-checked<%}%> edui-combobox-item-<%=i%>\" data-item-index=\"<%=i%>\" unselectable=\"on\" onmousedown=\"return false\">" +
                "<span class=\"edui-combobox-icon\" unselectable=\"on\" onmousedown=\"return false\"></span>" +
                "<label class=\"<%=labelClassName%>\" style=\"<%=itemStyles[ i ]%>\" unselectable=\"on\" onmousedown=\"return false\"><%=label%></label>" +
                "</li>" +
                "<%}%>" +
                "</ul>",
            defaultOpt: {
                //��¼ջ��ʼ�б�
                recordStack: [],
                //�������б�
                items: [],
		        //item��Ӧ��ֵ�б�
                value: [],
                comboboxName: '',
                selected: '',
                //�Զ���¼
                autoRecord: true,
                //����¼����
                recordCount: 5
            },
            init: function( options ){

                var me = this;

                $.extend( me._optionAdaptation( options ), me._createItemMapping( options.recordStack, options.items ), {
                    itemClassName: itemClassName,
                    iconClass: ICON_CLASS,
                    labelClassName: labelClassName
                } );

                this._transStack( options );

                me.root( $( $.parseTmpl( me.tpl, options ) ) );

                this.data( 'options', options ).initEvent();

            },
            initEvent: function(){

                var me = this;

                me.initSelectItem();

                this.initItemActive();

            },
            /**
             * ��ʼ��ѡ����
             */
            initSelectItem: function(){

                var me = this,
                    labelClass = "."+labelClassName;

                me.root().delegate('.' + itemClassName, 'click', function(){

                    var $li = $(this),
                        index = $li.attr('data-item-index');

                    me.trigger('comboboxselect', {
                        index: index,
                        label: $li.find(labelClass).text(),
                        value: me.data('options').value[ index ]
                    }).select( index );

                    me.hide();

                    return false;

                });

            },
            initItemActive: function(){
                var fn = {
                    mouseenter: 'addClass',
                    mouseleave: 'removeClass'
                };
                if ($.IE6) {
                    this.root().delegate( '.'+itemClassName,  'mouseenter mouseleave', function( evt ){
                        $(this)[ fn[ evt.type ] ]( HOVER_CLASS );
                    }).one('afterhide', function(){
                    });
                }
            },
            /**
             * ѡ����������
             * @param index ������
             * @returns {*} �����ڶ�Ӧ�������򷵻ظ�����򷵻�null
             */
            select: function( index ){

                var itemCount = this.data('options').itemCount,
                    items = this.data('options').autowidthitem;

                if ( items && !items.length ) {
                    items = this.data('options').items;
                }

                if( itemCount == 0 ) {
                    return null;
                }

                if( index < 0 ) {

                    index = itemCount + index % itemCount;

                } else if ( index >= itemCount ) {

                    index = itemCount-1;

                }

                this.trigger( 'changebefore', items[ index ] );

                this._update( index );

                this.trigger( 'changeafter', items[ index ] );

                return null;

            },
            selectItemByLabel: function( label ){

                var itemMapping = this.data('options').itemMapping,
                    me = this,
                    index = null;

                !$.isArray( label ) && ( label = [ label ] );

                $.each( label, function( i, item ){

                    index = itemMapping[ item ];

                    if( index !== undefined ) {

                        me.select( index );
                        return false;

                    }

                } );

            },
            /**
             * ת����¼ջ
             */
            _transStack: function( options ) {

                var temp = [],
                    itemIndex = -1,
                    selected = -1;

                $.each( options.recordStack, function( index, item ){

                    itemIndex = options.itemMapping[ item ];

                    if( $.isNumeric( itemIndex ) ) {

                        temp.push( itemIndex );

                        //selected�ĺϷ��Լ��
                        if( item == options.selected ) {
                            selected = itemIndex;
                        }

                    }

                } );

                options.recordStack = temp;
                options.selected = selected;
                temp = null;

            },
            _optionAdaptation: function( options ) {

                if( !( 'itemStyles' in options ) ) {

                    options.itemStyles = [];

                    for( var i = 0, len = options.items.length; i < len; i++ ) {
                        options.itemStyles.push('');
                    }

                }

                options.autowidthitem = options.autowidthitem || options.items;
                options.itemCount = options.items.length;

                return options;

            },
            _createItemMapping: function( stackItem, items ){

                var temp = {},
                    result = {
                        recordStack: [],
                        mapping: {}
                    };

                $.each( items, function( index, item ){
                    temp[ item ] = index;
                } );

                result.itemMapping = temp;

                $.each( stackItem, function( index, item ){

                    if( temp[ item ] !== undefined ) {
                        result.recordStack.push( temp[ item ] );
                        result.mapping[ item ] = temp[ item ];
                    }

                } );

                return result;

            },
            _update: function ( index ) {

                var options = this.data("options"),
                    newStack = [],
                    newChilds = null;

                $.each( options.recordStack, function( i, item ){

                    if( item != index ) {
                        newStack.push( item );
                    }

                } );

                //ѹ�����µļ�¼
                newStack.unshift( index );

                if( newStack.length > options.recordCount ) {
                    newStack.length = options.recordCount;
                }

                options.recordStack = newStack;
                options.selected = index;

                newChilds = $( $.parseTmpl( this.tpl, options ) );

                //������Ⱦ
                this.root().html( newChilds.html() );

                newChilds = null;
                newStack = null;

            }
        };

    } )(), 'menu' );

})();

/**
 * Combox �������
 * User: hn
 * Date: 13-5-29
 * Time: ����8:01
 * To change this template use File | Settings | File Templates.
 */

(function(){

    var widgetName = 'buttoncombobox';

    UM.ui.define( widgetName, ( function(){

        return {
            defaultOpt: {
                //��ť��ʼ����
                label: '',
                title: ''
            },
            init: function( options ) {

                var me = this;

                var btnWidget = $.eduibutton({
                    caret: true,
                    name: options.comboboxName,
                    title: options.title,
                    text: options.label,
                    click: function(){
                        me.show( this.root() );
                    }
                });

                me.supper.init.call( me, options );

                //����change�� �ı�button��ʾ����
                me.on('changebefore', function( e, label ){
                    btnWidget.eduibutton('label', label );
                });

                me.data( 'button', btnWidget );

                me.attachTo(btnWidget)

            },
            button: function(){
                return this.data( 'button' );
            }
        }

    } )(), 'combobox' );

})();

/*modal ��*/
UM.ui.define('modal', {
    tpl: '<div class="edui-modal" tabindex="-1" >' +
        '<div class="edui-modal-header">' +
        '<div class="edui-close" data-hide="modal"></div>' +
        '<h3 class="edui-title"><%=title%></h3>' +
        '</div>' +
        '<div class="edui-modal-body"  style="<%if(width){%>width:<%=width%>px;<%}%>' +
        '<%if(height){%>height:<%=height%>px;<%}%>">' +
        ' </div>' +
        '<% if(cancellabel || oklabel) {%>' +
        '<div class="edui-modal-footer">' +
        '<div class="edui-modal-tip"></div>' +
        '<%if(oklabel){%><div class="edui-btn edui-btn-primary" data-ok="modal"><%=oklabel%></div><%}%>' +
        '<%if(cancellabel){%><div class="edui-btn" data-hide="modal"><%=cancellabel%></div><%}%>' +
        '</div>' +
        '<%}%></div>',
    defaultOpt: {
        title: "",
        cancellabel: "",
        oklabel: "",
        width: '',
        height: '',
        backdrop: true,
        keyboard: true
    },
    init: function (options) {
        var me = this;

        me.root($($.parseTmpl(me.tpl, options || {})));

        me.data("options", options);
        if (options.okFn) {
            me.on('ok', $.proxy(options.okFn, me))
        }
        if (options.cancelFn) {
            me.on('beforehide', $.proxy(options.cancelFn, me))
        }

        me.root().delegate('[data-hide="modal"]', 'click', $.proxy(me.hide, me))
            .delegate('[data-ok="modal"]', 'click', $.proxy(me.ok, me));

        $('[data-hide="modal"],[data-ok="modal"]',me.root()).hover(function(){
            $(this).toggleClass('edui-hover')
        });
    },
    toggle: function () {
        var me = this;
        return me[!me.data("isShown") ? 'show' : 'hide']();
    },
    show: function () {

        var me = this;

        me.trigger("beforeshow");

        if (me.data("isShown")) return;

        me.data("isShown", true);

        me.escape();

        me.backdrop(function () {
            me.autoCenter();
            me.root()
                .show()
                .focus()
                .trigger('aftershow');
        })
    },
    showTip: function ( text ) {
        $( '.edui-modal-tip', this.root() ).html( text ).fadeIn();
    },
    hideTip: function ( text ) {
        $( '.edui-modal-tip', this.root() ).fadeOut( function (){
            $(this).html('');
        } );
    },
    autoCenter: function () {
        //ie6�²��ô�����
        !$.IE6 && this.root().css("margin-left", -(this.root().width() / 2));
    },
    hide: function () {
        var me = this;

        me.trigger("beforehide");

        if (!me.data("isShown")) return;

        me.data("isShown", false);

        me.escape();

        me.hideModal();
    },
    escape: function () {
        var me = this;
        if (me.data("isShown") && me.data("options").keyboard) {
            me.root().on('keyup', function (e) {
                e.which == 27 && me.hide();
            })
        }
        else if (!me.data("isShown")) {
            me.root().off('keyup');
        }
    },
    hideModal: function () {
        var me = this;
        me.root().hide();
        me.backdrop(function () {
            me.removeBackdrop();
            me.trigger('afterhide');
        })
    },
    removeBackdrop: function () {
        this.$backdrop && this.$backdrop.remove();
        this.$backdrop = null;
    },
    backdrop: function (callback) {
        var me = this;
        if (me.data("isShown") && me.data("options").backdrop) {
            me.$backdrop = $('<div class="edui-modal-backdrop" />').click(
                me.data("options").backdrop == 'static' ?
                    $.proxy(me.root()[0].focus, me.root()[0])
                    : $.proxy(me.hide, me)
            )
        }
        me.trigger('afterbackdrop');
        callback && callback();

    },
    attachTo: function ($obj) {
        var me = this
        if (!$obj.data('$mergeObj')) {

            $obj.data('$mergeObj', me.root());
            $obj.on('click', function () {
                me.toggle($obj)
            });
            me.data('$mergeObj', $obj)
        }
    },
    ok: function () {
        var me = this;
        me.trigger('beforeok');
        if (me.trigger("ok", me) === false) {
            return;
        }
        me.hide();
    },
    getBodyContainer: function () {
        return this.root().find('.edui-modal-body')
    }
});


/*tooltip ��*/
UM.ui.define('tooltip', {
    tpl: '<div class="edui-tooltip" unselectable="on" onmousedown="return false">' +
        '<div class="edui-tooltip-arrow" unselectable="on" onmousedown="return false"></div>' +
        '<div class="edui-tooltip-inner" unselectable="on" onmousedown="return false"></div>' +
        '</div>',
    init: function (options) {
        var me = this;
        me.root($($.parseTmpl(me.tpl, options || {})));
    },
    content: function (e) {
        var me = this,
            title = $(e.currentTarget).attr("data-original-title");

        me.root().find('.edui-tooltip-inner')['text'](title);
    },
    position: function (e) {
        var me = this,
            $obj = $(e.currentTarget);

        me.root().css($.extend({display: 'block'}, $obj ? {
            top: $obj.outerHeight(),
            left: (($obj.outerWidth() - me.root().outerWidth()) / 2)
        } : {}))
    },
    show: function (e) {
        if ($(e.currentTarget).hasClass('edui-disabled')) return;

        var me = this;
        me.content(e);
        me.root().appendTo($(e.currentTarget));
        me.position(e);
        me.root().css('display', 'block');
    },
    hide: function () {
        var me = this;
        me.root().css('display', 'none')
    },
    attachTo: function ($obj) {
        var me = this;

        function tmp($obj) {
            var me = this;

            if (!$.contains(document.body, me.root()[0])) {
                me.root().appendTo($obj);
            }

            me.data('tooltip', me.root());

            $obj.each(function () {
                if ($(this).attr("data-original-title")) {
                    $(this).on('mouseenter', $.proxy(me.show, me))
                        .on('mouseleave click', $.proxy(me.hide, me))

                }
            });

        }

        if ($.type($obj) === "undefined") {
            $("[data-original-title]").each(function (i, el) {
                tmp.call(me, $(el));
            })

        } else {
            if (!$obj.data('tooltip')) {
                tmp.call(me, $obj);
            }
        }
    }
});

/*tab ��*/
UM.ui.define('tab', {
    init: function (options) {
        var me = this,
            slr = options.selector;

        if ($.type(slr)) {
            me.root($(slr, options.context));
            me.data("context", options.context);

            $(slr, me.data("context")).on('click', function (e) {
                me.show(e);
            });
        }
    },
    show: function (e) {

        var me = this,
            $cur = $(e.target),
            $ul = $cur.closest('ul'),
            selector,
            previous,
            $target,
            e;

        selector = $cur.attr('data-context');
        selector = selector && selector.replace(/.*(?=#[^\s]*$)/, '');

        var $tmp = $cur.parent('li');

        if (!$tmp.length || $tmp.hasClass('edui-active')) return;

        previous = $ul.find('.edui-active:last a')[0];

        e = $.Event('beforeshow', {
            target: $cur[0],
            relatedTarget: previous
        });

        me.trigger(e);

        if (e.isDefaultPrevented()) return;

        $target = $(selector, me.data("context"));

        me.activate($cur.parent('li'), $ul);
        me.activate($target, $target.parent(), function () {
            me.trigger({
                type: 'aftershow', relatedTarget: previous
            })
        });
    },
    activate: function (element, container, callback) {
        if (element === undefined) {
            return $(".edui-tab-item.edui-active",this.root()).index();
        }

        var $active = container.find('> .edui-active');

        $active.removeClass('edui-active');

        element.addClass('edui-active');

        callback && callback();
    }
});


//button ��
UM.ui.define('separator', {
    tpl: '<div class="edui-separator" unselectable="on" onmousedown="return false" ></div>',
    init: function (options) {
        var me = this;
        me.root($($.parseTmpl(me.tpl, options)));
        return me;
    }
});
/**
 * @file adapter.js
 * @desc adapt ui to editor
 * @import core/Editor.js, core/utils.js
 */

(function () {
    var _editorUI = {},
        _editors = {},
        _readyFn = [],
        _activeWidget = null,
        _widgetData = {},
        _widgetCallBack = {},
        _cacheUI = {},
        _maxZIndex = null;

    utils.extend(UM, {
        defaultWidth : 500,
        defaultHeight : 500,
        registerUI: function (name, fn) {
            utils.each(name.split(/\s+/), function (uiname) {
                _editorUI[uiname] = fn;
            })
        },

        setEditor : function(editor){
            !_editors[editor.id] && (_editors[editor.id] = editor);
        },
        registerWidget : function(name,pro,cb){
            _widgetData[name] = $.extend2(pro,{
                $root : '',
                _preventDefault:false,
                root:function($el){
                    return this.$root || (this.$root = $el);
                },
                preventDefault:function(){
                    this._preventDefault = true;
                },
                clear:false
            });
            if(cb){
                _widgetCallBack[name] = cb;
            }
        },
        getWidgetData : function(name){
            return _widgetData[name]
        },
        setWidgetBody : function(name,$widget,editor){
            if(!editor._widgetData){

                utils.extend(editor,{
                    _widgetData : {},
                    getWidgetData : function(name){
                        return this._widgetData[name];
                    },
                    getWidgetCallback : function(widgetName){
                        var me = this;
                        return function(){
                            return  _widgetCallBack[widgetName].apply(me,[me,$widget].concat(Array.prototype.slice.call(arguments,0)))
                        }
                    }
                })

            }
            var pro = _widgetData[name];
            if(!pro){
                return null;
            }
            pro = editor._widgetData[name];
            if(!pro){
                pro = _widgetData[name];
                pro = editor._widgetData[name] = $.type(pro) == 'function' ? pro : utils.clone(pro);
            }

            pro.root($widget.edui().getBodyContainer());

            pro.initContent(editor,$widget);
            if(!pro._preventDefault){
                pro.initEvent(editor,$widget);
            }

            pro.width &&  $widget.width(pro.width);


        },
        setActiveWidget : function($widget){
            _activeWidget = $widget;
        },
        getEditor: function (id, options) {
            var editor = _editors[id] || (_editors[id] = this.createEditor(id, options));
            _maxZIndex = _maxZIndex ? Math.max(editor.getOpt('zIndex'), _maxZIndex):editor.getOpt('zIndex');
            return editor;
        },
        setTopEditor: function(editor){
            $.each(_editors, function(i, o){
                if(editor == o) {
                    editor.$container && editor.$container.css('zIndex', _maxZIndex + 1);
                } else {
                    o.$container && o.$container.css('zIndex', o.getOpt('zIndex'));
                }
            });
        },
        clearCache : function(id){
            if ( _editors[id]) {
                delete  _editors[id]
            }
        },
        delEditor: function (id) {
            var editor;
            if (editor = _editors[id]) {
                editor.destroy();
            }
        },
        ready: function( fn ){
            _readyFn.push( fn );
        },
        createEditor: function (id, opt) {
            var editor = new UM.Editor(opt);
            var T = this;

            editor.langIsReady ? $.proxy(renderUI,T)() : editor.addListener("langReady", $.proxy(renderUI,T));
            function renderUI(){


                var $container = this.createUI('#' + id, editor);
                editor.key=id;
                editor.ready(function(){
                    $.each( _readyFn, function( index, fn ){
                        $.proxy( fn, editor )();
                    } );
                });
                var options = editor.options;
                if(options.initialFrameWidth){
                    options.minFrameWidth = options.initialFrameWidth
                }else{
                    options.minFrameWidth = options.initialFrameWidth = editor.$body.width() || UM.defaultWidth;
                }

                $container.css({
                    width: options.initialFrameWidth,
                    zIndex:editor.getOpt('zIndex')
                });

                //ie6�»���ͼƬ
                UM.browser.ie && UM.browser.version === 6 && document.execCommand("BackgroundImageCache", false, true);

                editor.render(id);


                //���tooltip;
                $.eduitooltip && $.eduitooltip('attachTo', $("[data-original-title]",$container)).css('z-index',editor.getOpt('zIndex')+1);

                $container.find('a').click(function(evt){
                    evt.preventDefault()
                });

                editor.fireEvent("afteruiready");
            }

            return editor;

        },
        createUI: function (id, editor) {
            var $editorCont = $(id),
                $container = $('<div class="edui-container"><div class="edui-editor-body"></div></div>').insertBefore($editorCont);
            editor.$container = $container;
            editor.container = $container[0];

            editor.$body = $editorCont;

            //������ie9+���ϵİ汾�У��Զ���������ʱ�ģ���Ӱ����
            if(browser.ie && browser.ie9above){
                var $span = $('<span style="padding:0;margin:0;height:0;width:0"></span>');
                $span.insertAfter($container);
            }
            //��ʼ��ע���ui���
            $.each(_editorUI,function(n,v){
                var widget = v.call(editor,n);
                if(widget){
                    _cacheUI[n] = widget;
                }

            });

            $container.find('.edui-editor-body').append($editorCont).before(this.createToolbar(editor.options, editor));

            $container.find('.edui-toolbar').append($('<div class="edui-dialog-container"></div>'));


            return $container;
        },
        createToolbar: function (options, editor) {
            var $toolbar = $.eduitoolbar(), toolbar = $toolbar.edui();
            //���������˵��б�

            if (options.toolbar && options.toolbar.length) {
                var btns = [];
                $.each(options.toolbar,function(i,uiNames){
                    $.each(uiNames.split(/\s+/),function(index,name){
                        if(name == '|'){
                                $.eduiseparator && btns.push($.eduiseparator());
                        }else{
                            var ui = _cacheUI[name];
                            if(name=="fullscreen"){
                                ui&&btns.unshift(ui);
                            }else{
                                ui && btns.push(ui);
                            }
                        }

                    });
                    btns.length && toolbar.appendToBtnmenu(btns);
                });
            } else {
                $toolbar.find('.edui-btn-toolbar').remove()
            }
            return $toolbar;
        }

    })


})();



UM.registerUI(
	'bold italic redo undo underline strikethrough superscript subscript insertorderedlist insertunorderedlist ' +
    'cleardoc selectall link unlink print preview justifyleft justifycenter justifyright justifyfull removeformat horizontal drafts',
//	'bold italic redo undo underline strikethrough superscript subscript insertorderedlist insertunorderedlist ' +
//  'cleardoc selectall link unlink print preview justifyleft justifycenter justifyright justifyfull removeformat horizontal drafts',
    function(name) {
        var me = this;
        var $btn = $.eduibutton({
            icon : name,
            click : function(){
                me.execCommand(name);
            },
            title: this.getLang('labelMap')[name] || ''
        });

        this.addListener('selectionchange',function(){
            var state = this.queryCommandState(name);
            $btn.edui().disabled(state == -1).active(state == 1)
        });
        return $btn;
    }
);


/**
 * ȫ�����
 */

(function(){

    //״̬����
    var STATUS_CACHE = {},
    //״ֵ̬�б�
        STATUS_LIST = [ 'width', 'height', 'position', 'top', 'left', 'margin', 'padding', 'overflowX', 'overflowY' ],
        CONTENT_AREA_STATUS = {},
    //ҳ��״̬
        DOCUMENT_STATUS = {},
        DOCUMENT_ELEMENT_STATUS = {},

        FULLSCREENS = {};


    UM.registerUI('fullscreen', function( name ){

        var me = this,
            $button = $.eduibutton({
                'icon': 'fullscreen',
                'title': (me.options.labelMap && me.options.labelMap[name]) || me.getLang("labelMap." + name),
                'click': function(){
                    //�л�
                    me.execCommand( name );
                    UM.setTopEditor(me);
                }
            });

        me.addListener( "selectionchange", function () {

            var state = this.queryCommandState( name );
            $button.edui().disabled( state == -1 ).active( state == 1 );

        } );

        //�л���ȫ��
        me.addListener('ready', function(){

            me.options.fullscreen && Fullscreen.getInstance( me ).toggle();

        });

        return $button;

    });

    UM.commands[ 'fullscreen' ] = {

        execCommand: function (cmdName) {

            Fullscreen.getInstance( this ).toggle();

        },
        queryCommandState: function (cmdName) {

            return this._edui_fullscreen_status;
        },
        notNeedUndo: 1

    };

    function Fullscreen( editor ) {

        var me = this;

        if( !editor ) {
            throw new Error('invalid params, notfound editor');
        }

        me.editor = editor;

        //��¼��ʼ����ȫ�����
        FULLSCREENS[ editor.uid ] = this;

        editor.addListener('destroy', function(){
            delete FULLSCREENS[ editor.uid ];
            me.editor = null;
        });

    }

    Fullscreen.prototype = {

        /**
         * ȫ��״̬�л�
         */
        toggle: function(){

            var editor = this.editor,
            //��ǰ�༭��������״̬
                _edui_fullscreen_status = this.isFullState();
            editor.fireEvent('beforefullscreenchange', !_edui_fullscreen_status );

            //����״̬
            this.update( !_edui_fullscreen_status );

            !_edui_fullscreen_status ? this.enlarge() : this.revert();

            editor.fireEvent('afterfullscreenchange', !_edui_fullscreen_status );
            if(editor.body.contentEditable === 'true'){
                editor.fireEvent( 'fullscreenchanged', !_edui_fullscreen_status );
            }

            editor.fireEvent( 'selectionchange' );

        },
        /**
         * ִ�зŴ�
         */
        enlarge: function(){

            this.saveSataus();

            this.setDocumentStatus();

            this.resize();

        },
        /**
         * ȫ����ԭ
         */
        revert: function(){

            //��ԭCSS���ʽ
            var options = this.editor.options,
                height = /%$/.test(options.initialFrameHeight) ?  '100%' : (options.initialFrameHeight - this.getStyleValue("padding-top")- this.getStyleValue("padding-bottom") - this.getStyleValue('border-width'));

            $.IE6 && this.getEditorHolder().style.setExpression('height', 'this.scrollHeight <= ' + height + ' ? "' + height + 'px" : "auto"');

            //��ԭ����״̬
            this.revertContainerStatus();

            this.revertContentAreaStatus();

            this.revertDocumentStatus();

        },
        /**
         * ����״̬
         * @param isFull ��ǰ״̬�Ƿ���ȫ��״̬
         */
        update: function( isFull ) {
            this.editor._edui_fullscreen_status = isFull;
        },
        /**
         * ����ǰ�༭���Ĵ�С, ���ǰ�༭��������ȫ��״̬�� ��������
         */
        resize: function(){

            var $win = null,
                height = 0,
                width = 0,
                borderWidth = 0,
                paddingWidth = 0,
                editor = this.editor,
                me = this,
                bound = null,
                editorBody = null;

            if( !this.isFullState() ) {
                return;
            }

            $win = $( window );
            width = $win.width();
            height = $win.height();
            editorBody = this.getEditorHolder();
            //�ı��༭��border���
            borderWidth = parseInt( domUtils.getComputedStyle( editorBody, 'border-width' ), 10 ) || 0;
            //����border���
            borderWidth += parseInt( domUtils.getComputedStyle( editor.container, 'border-width' ), 10 ) || 0;
            //����padding
            paddingWidth += parseInt( domUtils.getComputedStyle( editorBody, 'padding-left' ), 10 ) + parseInt( domUtils.getComputedStyle( editorBody, 'padding-right' ), 10 ) || 0;

            //�ɵ�css���ʽ
            $.IE6 && editorBody.style.setExpression( 'height', null );

            bound = this.getBound();

            $( editor.container ).css( {
                width: width + 'px',
                height: height + 'px',
                position: !$.IE6 ? 'fixed' : 'absolute',
                top: bound.top,
                left: bound.left,
                margin: 0,
                padding: 0,
                overflowX: 'hidden',
                overflowY: 'hidden'
            } );

            $( editorBody ).css({
                width: width - 2*borderWidth - paddingWidth + 'px',
                height: height - 2*borderWidth - ( editor.options.withoutToolbar ? 0 : $( '.edui-toolbar', editor.container ).outerHeight() ) - $( '.edui-bottombar', editor.container).outerHeight() + 'px',
                overflowX: 'hidden',
                overflowY: 'auto'
            });

        },
        /**
         * ����״̬
         */
        saveSataus: function(){

            var styles = this.editor.container.style,
                tmp = null,
                cache = {};

            for( var i= 0, len = STATUS_LIST.length; i<len; i++ ) {

                tmp = STATUS_LIST[ i ];
                cache[ tmp ] = styles[ tmp ];

            }

            STATUS_CACHE[ this.editor.uid ] = cache;

            this.saveContentAreaStatus();
            this.saveDocumentStatus();

        },
        saveContentAreaStatus: function(){

            var $holder = $(this.getEditorHolder());

            CONTENT_AREA_STATUS[ this.editor.uid ] = {
                width: $holder.css("width"),
                overflowX: $holder.css("overflowX"),
                overflowY: $holder.css("overflowY"),
                height: $holder.css("height")
            };

        },
        /**
         * ������ָ��editor��ص�ҳ���״̬
         */
        saveDocumentStatus: function(){

            var $doc = $( this.getEditorDocumentBody() );

            DOCUMENT_STATUS[ this.editor.uid ] = {
                overflowX: $doc.css( 'overflowX' ),
                overflowY: $doc.css( 'overflowY' )
            };
            DOCUMENT_ELEMENT_STATUS[ this.editor.uid ] = {
                overflowX: $( this.getEditorDocumentElement() ).css( 'overflowX'),
                overflowY: $( this.getEditorDocumentElement() ).css( 'overflowY' )
            };

        },
        /**
         * �ָ�����״̬
         */
        revertContainerStatus: function(){
            $( this.editor.container ).css( this.getEditorStatus() );
        },
        /**
         * �ָ��༭��״̬
         */
        revertContentAreaStatus: function(){
            var holder = this.getEditorHolder(),
                state = this.getContentAreaStatus();

            if ( this.supportMin() ) {
                delete state.height;
                holder.style.height = null;
            }

            $( holder ).css( state );
        },
        /**
         * �ָ�ҳ��״̬
         */
        revertDocumentStatus: function() {

            var status = this.getDocumentStatus();
            $( this.getEditorDocumentBody() ).css( 'overflowX', status.body.overflowX );
            $( this.getEditorDocumentElement() ).css( 'overflowY', status.html.overflowY );

        },
        setDocumentStatus: function(){
            $(this.getEditorDocumentBody()).css( {
                overflowX: 'hidden',
                overflowY: 'hidden'
            } );
            $(this.getEditorDocumentElement()).css( {
                overflowX: 'hidden',
                overflowY: 'hidden'
            } );
        },
        /**
         * ��⵱ǰ�༭���Ƿ���ȫ��״̬ȫ��״̬
         * @returns {boolean} �Ƿ���ȫ��״̬
         */
        isFullState: function(){
            return !!this.editor._edui_fullscreen_status;
        },
        /**
         * ��ȡ�༭��״̬
         */
        getEditorStatus: function(){
            return STATUS_CACHE[ this.editor.uid ];
        },
        getContentAreaStatus: function(){
            return CONTENT_AREA_STATUS[ this.editor.uid ];
        },
        getEditorDocumentElement: function(){
            return this.editor.container.ownerDocument.documentElement;
        },
        getEditorDocumentBody: function(){
            return this.editor.container.ownerDocument.body;
        },
        /**
         * ��ȡ�༭�������
         */
        getEditorHolder: function(){
            return this.editor.body;
        },
        /**
         * ��ȡ�༭��״̬
         * @returns {*}
         */
        getDocumentStatus: function(){
            return {
                'body': DOCUMENT_STATUS[ this.editor.uid ],
                'html': DOCUMENT_ELEMENT_STATUS[ this.editor.uid ]
            };
        },
        supportMin: function () {

            var node = null;

            if ( !this._support ) {

                node = document.createElement("div");

                this._support = "minWidth" in node.style;

                node = null;

            }

            return this._support;

        },
        getBound: function () {

            var tags = {
                    html: true,
                    body: true
                },
                result = {
                    top: 0,
                    left: 0
                },
                offsetParent = null;

            if ( !$.IE6 ) {
                return result;
            }

            offsetParent = this.editor.container.offsetParent;

            if( offsetParent && !tags[ offsetParent.nodeName.toLowerCase() ] ) {
                tags = offsetParent.getBoundingClientRect();
                result.top = -tags.top;
                result.left = -tags.left;
            }

            return result;

        },
        getStyleValue: function (attr) {
            return parseInt(domUtils.getComputedStyle( this.getEditorHolder() ,attr));
        }
    };


    $.extend( Fullscreen, {
        /**
         * ����resize
         */
        listen: function(){

            var timer = null;

            if( Fullscreen._hasFullscreenListener ) {
                return;
            }

            Fullscreen._hasFullscreenListener = true;

            $( window ).on( 'resize', function(){

                if( timer !== null ) {
                    window.clearTimeout( timer );
                    timer = null;
                }

                timer = window.setTimeout(function(){

                    for( var key in FULLSCREENS ) {
                        FULLSCREENS[ key ].resize();
                    }

                    timer = null;

                }, 50);

            } );

        },

        getInstance: function ( editor ) {

            if ( !FULLSCREENS[editor.uid  ] ) {
                new Fullscreen( editor );
            }

            return FULLSCREENS[editor.uid  ];

        }

    });

    //��ʼ����
    Fullscreen.listen();

})();
//UM.registerUI(
//	'link image video map formula'
//	,function(name){
//
//  var me = this, currentRange, $dialog,
//      opt = {
//          title: (me.options.labelMap && me.options.labelMap[name]) || me.getLang("labelMap." + name),
//          url: me.options.UMEDITOR_HOME_URL + 'dialogs/' + name + '/' + name + '.js'
//      };
//
//  var $btn = $.eduibutton({
//      icon: name,
//      title: this.getLang('labelMap')[name] || ''
//  });
//  //����ģ�����
//  utils.loadFile(document,{
//      src: opt.url,
//      tag: "script",
//      type: "text/javascript",
//      defer: "defer"
//  },function(){
//      //�������
//      var data = UM.getWidgetData(name);
//      if(!data) return;
//      if(data.buttons){
//          var ok = data.buttons.ok;
//          if(ok){
//              opt.oklabel = ok.label || me.getLang('ok');
//              if(ok.exec){
//                  opt.okFn = function(){
//                      return $.proxy(ok.exec,null,me,$dialog)()
//                  }
//              }
//          }
//          var cancel = data.buttons.cancel;
//          if(cancel){
//              opt.cancellabel = cancel.label || me.getLang('cancel');
//              if(cancel.exec){
//                  opt.cancelFn = function(){
//                      return $.proxy(cancel.exec,null,me,$dialog)()
//                  }
//              }
//          }
//      }
//      data.width && (opt.width = data.width);
//      data.height && (opt.height = data.height);
//
//      $dialog = $.eduimodal(opt);
//
//      $dialog.attr('id', 'edui-dialog-' + name).addClass('edui-dialog-' + name)
//          .find('.edui-modal-body').addClass('edui-dialog-' + name + '-body');
//
//      $dialog.edui().on('beforehide',function () {
//          var rng = me.selection.getRange();
//          if (rng.equals(currentRange)) {
//              rng.select()
//          }
//      }).on('beforeshow', function () {
//              var $root = this.root(),
//                  win = null,
//                  offset = null;
//              currentRange = me.selection.getRange();
//              if (!$root.parent()[0]) {
//                  me.$container.find('.edui-dialog-container').append($root);
//              }
//
//              //IE6�� ���⴦��, ͨ�������ж�λ
//              if( $.IE6 ) {
//
//                  win = {
//                      width: $( window ).width(),
//                      height: $( window ).height()
//                  };
//                  offset = $root.parents(".edui-toolbar")[0].getBoundingClientRect();
//                  $root.css({
//                      position: 'absolute',
//                      margin: 0,
//                      left: ( win.width - $root.width() ) / 2 - offset.left,
//                      top: 100 - offset.top
//                  });
//
//              }
//              UM.setWidgetBody(name,$dialog,me);
//              UM.setTopEditor(me);
//      }).on('afterbackdrop',function(){
//          this.$backdrop.css('zIndex',me.getOpt('zIndex')+1).appendTo(me.$container.find('.edui-dialog-container'))
//          $dialog.css('zIndex',me.getOpt('zIndex')+2)
//      }).on('beforeok',function(){
//          try{
//              currentRange.select()
//          }catch(e){}
//      }).attachTo($btn)
//  });
//
//
//
//
//  me.addListener('selectionchange', function () {
//      var state = this.queryCommandState(name);
//      $btn.edui().disabled(state == -1).active(state == 1)
//  });
//  return $btn;
//});

UM.registerUI(
//	'emotion formula'
'formula'
	,
function( name ){
    var me = this,
        url  = me.options.UMEDITOR_HOME_URL + 'dialogs/' +name+ '/'+name+'.js';

    var $btn = $.eduibutton({
        icon: name,
        title: this.getLang('labelMap')[name] || ''
    });

    //����ģ�����
    utils.loadFile(document,{
        src: url,
        tag: "script",
        type: "text/javascript",
        defer: "defer"
    },function(){
        var opt = {
            url : url
        };
        //�������
        var data = UM.getWidgetData(name);

        data.width && (opt.width = data.width);
        data.height && (opt.height = data.height);

        $.eduipopup(opt).css('zIndex',me.options.zIndex + 1)
            .addClass('edui-popup-' + name)
            .edui()
            .on('beforeshow',function(){
                var $root = this.root();
                if(!$root.parent().length){
                    me.$container.find('.edui-dialog-container').append($root);
                }
                UM.setWidgetBody(name,$root,me);
                UM.setTopEditor(me);
            }).attachTo($btn,{
                offsetTop:-5,
                offsetLeft:10,
                caretLeft:11,
                caretTop:-8
            });
        me.addListener('selectionchange', function () {
            var state = this.queryCommandState(name);
            $btn.edui().disabled(state == -1).active(state == 1);
        });
    });
    return $btn;

} );
UM.registerUI('imagescale',function () {
    var me = this,
        $imagescale;

    me.setOpt('imageScaleEnabled', true);

    if (browser.webkit && me.getOpt('imageScaleEnabled')) {

        me.addListener('click', function (type, e) {
            var range = me.selection.getRange(),
                img = range.getClosedNode(),
                target = e.target;

            /* �����һ��ͼƬ�ĺ���,�˸��ǲ���ʧ fix:3652 */
            if (img && img.tagName == 'IMG' && target == img) {

                if (!$imagescale) {
                    $imagescale = $.eduiscale({'$wrap':me.$container}).css('zIndex', me.options.zIndex);
                    me.$container.append($imagescale);

                    var _keyDownHandler = function () {
                        $imagescale.edui().hide();
                    }, _mouseDownHandler = function (e) {
                        var ele = e.target || e.srcElement;
                        if (ele && ele.className.indexOf('edui-scale') == -1) {
                            _keyDownHandler(e);
                        }
                    }, timer;

                    $imagescale.edui()
                        .on('aftershow', function () {
                            $(document).bind('keydown', _keyDownHandler);
                            $(document).bind('mousedown', _mouseDownHandler);
                            me.selection.getNative().removeAllRanges();
                        })
                        .on('afterhide', function () {
                            $(document).unbind('keydown', _keyDownHandler);
                            $(document).unbind('mousedown', _mouseDownHandler);
                            var target = $imagescale.edui().getScaleTarget();
                            if (target.parentNode) {
                                me.selection.getRange().selectNode(target).select();
                            }
                        })
                        .on('mousedown', function (e) {
                            me.selection.getNative().removeAllRanges();
                            var ele = e.target || e.srcElement;
                            if (ele && ele.className.indexOf('edui-scale-hand') == -1) {
                                timer = setTimeout(function() {
                                    $imagescale.edui().hide();
                                }, 200);
                            }
                        })
                        .on('mouseup', function (e) {
                            var ele = e.target || e.srcElement;
                            if (ele && ele.className.indexOf('edui-scale-hand') == -1) {
                                clearTimeout(timer);
                            }
                        });
                }
                $imagescale.edui().show($(img));

            } else {
                if ($imagescale && $imagescale.css('display') != 'none') $imagescale.edui().hide();

            }
        });

        me.addListener('click', function (type, e) {
            if (e.target.tagName == 'IMG') {
                var range = new dom.Range(me.document, me.body);
                range.selectNode(e.target).select();
            }
        });

    }
});
UM.registerUI('autofloat',function(){
    var me = this,
        lang = me.getLang();
    me.setOpt({
        autoFloatEnabled: true,
        topOffset: 0
    });
    var optsAutoFloatEnabled = me.options.autoFloatEnabled !== false,
        topOffset = me.options.topOffset;


    //���̶�toolbar��λ�ã���ֱ���˳�
    if(!optsAutoFloatEnabled){
        return;
    }
    me.ready(function(){
        var LteIE6 = browser.ie && browser.version <= 6,
            quirks = browser.quirks;

        function checkHasUI(){
            if(!UM.ui){
                alert(lang.autofloatMsg);
                return 0;
            }
            return 1;
        }
        function fixIE6FixedPos(){
            var docStyle = document.body.style;
            docStyle.backgroundImage = 'url("about:blank")';
            docStyle.backgroundAttachment = 'fixed';
        }
        var	bakCssText,
            placeHolder = document.createElement('div'),
            toolbarBox,orgTop,
            getPosition=function(element){
                var bcr;
                //trace  IE6���ڿ��Ʊ༭������ʱ���ܻᱨ�?catchһ��
                try{
                    bcr = element.getBoundingClientRect();
                }catch(e){
                    bcr={left:0,top:0,height:0,width:0}
                }
                var rect = {
                    left: Math.round(bcr.left),
                    top: Math.round(bcr.top),
                    height: Math.round(bcr.bottom - bcr.top),
                    width: Math.round(bcr.right - bcr.left)
                };
                var doc;
                while ((doc = element.ownerDocument) !== document &&
                    (element = domUtils.getWindow(doc).frameElement)) {
                    bcr = element.getBoundingClientRect();
                    rect.left += bcr.left;
                    rect.top += bcr.top;
                }
                rect.bottom = rect.top + rect.height;
                rect.right = rect.left + rect.width;
                return rect;
            };
        var isFullScreening = false;
        function setFloating(){
            if(isFullScreening){
                return;
            }
            var toobarBoxPos = domUtils.getXY(toolbarBox),
                origalFloat = domUtils.getComputedStyle(toolbarBox,'position'),
                origalLeft = domUtils.getComputedStyle(toolbarBox,'left');
            toolbarBox.style.width = toolbarBox.offsetWidth + 'px';
            toolbarBox.style.zIndex = me.options.zIndex * 1 + 1;
            toolbarBox.parentNode.insertBefore(placeHolder, toolbarBox);
            if (LteIE6 || (quirks && browser.ie)) {
                if(toolbarBox.style.position != 'absolute'){
                    toolbarBox.style.position = 'absolute';
                }
                toolbarBox.style.top = (document.body.scrollTop||document.documentElement.scrollTop) - orgTop + topOffset  + 'px';
            } else {
                if(toolbarBox.style.position != 'fixed'){
                    toolbarBox.style.position = 'fixed';
                    toolbarBox.style.top = topOffset +"px";
                    ((origalFloat == 'absolute' || origalFloat == 'relative') && parseFloat(origalLeft)) && (toolbarBox.style.left = toobarBoxPos.x + 'px');
                }
            }
        }
        function unsetFloating(){

            if(placeHolder.parentNode){
                placeHolder.parentNode.removeChild(placeHolder);
            }
            toolbarBox.style.cssText = bakCssText;
        }

        function updateFloating(){
            var rect3 = getPosition(me.container);
            var offset=me.options.toolbarTopOffset||0;
            if (rect3.top < 0 && rect3.bottom - toolbarBox.offsetHeight > offset) {
                setFloating();
            }else{
                unsetFloating();
            }
        }
        var defer_updateFloating = utils.defer(function(){
            updateFloating();
        },browser.ie ? 200 : 100,true);

        me.addListener('destroy',function(){
            $(window).off('scroll resize',updateFloating);
            me.removeListener('keydown', defer_updateFloating);
        });

        if(checkHasUI(me)){
            toolbarBox = $('.edui-toolbar',me.container)[0];
            me.addListener("afteruiready",function(){
                setTimeout(function(){
                    orgTop = $(toolbarBox).offset().top;
                },100);
            });
            bakCssText = toolbarBox.style.cssText;
            placeHolder.style.height = toolbarBox.offsetHeight + 'px';
            if(LteIE6){
                fixIE6FixedPos();
            }

            $(window).on('scroll resize',updateFloating);
            me.addListener('keydown', defer_updateFloating);
            me.addListener('resize', function(){
                unsetFloating();
                placeHolder.style.height = toolbarBox.offsetHeight + 'px';
                updateFloating();
            });

            me.addListener('beforefullscreenchange', function (t, enabled){
                if (enabled) {
                    unsetFloating();
                    isFullScreening = enabled;
                }
            });
            me.addListener('fullscreenchanged', function (t, enabled){
                if (!enabled) {
                    updateFloating();
                }
                isFullScreening = enabled;
            });
            me.addListener('sourcemodechanged', function (t, enabled){
                setTimeout(function (){
                    updateFloating();
                },0);
            });
            me.addListener("clearDoc",function(){
                setTimeout(function(){
                    updateFloating();
                },0);

            })
        }
    })


});
UM.registerUI('source',function(name){
    var me = this;
    me.addListener('fullscreenchanged',function(){
        me.$container.find('textarea').width(me.$body.width() - 10).height(me.$body.height())

    });
    var $btn = $.eduibutton({
        icon : name,
        click : function(){
            me.execCommand(name);
            UM.setTopEditor(me);
        },
        title: this.getLang('labelMap')[name] || ''
    });

    this.addListener('selectionchange',function(){
        var state = this.queryCommandState(name);
        $btn.edui().disabled(state == -1).active(state == 1)
    });
    return $btn;
});

UM.registerUI('paragraph fontfamily fontsize', function( name ) {

    var me = this,
        label = (me.options.labelMap && me.options.labelMap[name]) || me.getLang("labelMap." + name),
        options = {
            label: label,
            title: label,
            comboboxName: name,
            items: me.options[ name ] || [],
            itemStyles: [],
            value: [],
            autowidthitem: []
        },
        $combox = null,
        comboboxWidget = null;
    if(options.items.length == 0){
        return null;
    }
    switch ( name ) {

        case 'paragraph':
            options = transForParagraph( options );
            break;

        case 'fontfamily':
            options = transForFontfamily( options );
            break;

        case 'fontsize':
            options = transForFontsize( options );
            break;

    }

    //ʵ��
    $combox =  $.eduibuttoncombobox(options).css('zIndex',me.getOpt('zIndex') + 1);
    comboboxWidget =  $combox.edui();

    comboboxWidget.on('comboboxselect', function( evt, res ){
                        me.execCommand( name, res.value );
                    }).on("beforeshow", function(){
                        if( $combox.parent().length === 0 ) {
                            $combox.appendTo(  me.$container.find('.edui-dialog-container') );
                        }
                        UM.setTopEditor(me);
                    });


    //״̬����
    this.addListener('selectionchange',function( evt ){

        var state  = this.queryCommandState( name ),
            value = this.queryCommandValue( name );

        //���ð�ť״̬
        comboboxWidget.button().edui().disabled( state == -1 ).active( state == 1 );
        if(value){
            //����label
            value = value.replace(/['"]/g, '').toLowerCase().split(/['|"]?\s*,\s*[\1]?/);

            comboboxWidget.selectItemByLabel( value );
        }


    });

    return comboboxWidget.button().addClass('edui-combobox');

    /**
     * �������Ӧ���ߺ���
     * @param word ��������
     * @param hasSuffix �Ƿ��к�׺
     */
    function wordCountAdaptive ( word, hasSuffix ) {

        var $tmpNode = $('<span>' ).html( word ).css( {
                display: 'inline',
                position: 'absolute',
                top: -10000000,
                left: -100000
            } ).appendTo( document.body),
            width = $tmpNode.width();

        $tmpNode.remove();
        $tmpNode = null;

        if( width < 50 ) {

            return word;

        } else {

            word = word.slice( 0, hasSuffix ? -4 : -1 );

            if( !word.length ) {
                return '...';
            }

            return wordCountAdaptive( word + '...', true );

        }

    }


    //�������ת��
    function transForParagraph ( options ) {

        var tempItems = [];

        for( var key in options.items ) {

            options.value.push( key );
            tempItems.push( key );
            options.autowidthitem.push( wordCountAdaptive( key ) );

        }

        options.items = tempItems;
        options.autoRecord = false;

        return options;

    }

    //�������ת��
    function transForFontfamily ( options ) {

        var temp = null,
            tempItems = [];

        for( var i = 0, len = options.items.length; i < len; i++ ) {

            temp = options.items[ i ].val;
            tempItems.push( temp.split(/\s*,\s*/)[0] );
            options.itemStyles.push('font-family: ' + temp);
            options.value.push( temp );
            options.autowidthitem.push( wordCountAdaptive( tempItems[ i ] ) );

        }

        options.items = tempItems;

        return options;

    }

    //�����С����ת��
    function transForFontsize ( options ) {

        var temp = null,
            tempItems = [];

        options.itemStyles = [];
        options.value = [];

        for( var i = 0, len = options.items.length; i < len; i++ ) {

            temp = options.items[ i ];
            tempItems.push( temp );
            options.itemStyles.push('font-size: ' + temp +'px');

        }

        options.value = options.items;
        options.items = tempItems;
        options.autoRecord = false;

        return options;

    }

});


UM.registerUI('forecolor backcolor', function( name ) {
    function getCurrentColor() {
        return domUtils.getComputedStyle( $colorLabel[0], 'background-color' );
    }

    var me = this,
        $colorPickerWidget = null,
        $colorLabel = null,
        $btn = null;

    //querycommand
    this.addListener('selectionchange', function(){

        var state = this.queryCommandState( name );
        $btn.edui().disabled( state == -1 ).active( state == 1 );

    });

    $btn = $.eduicolorsplitbutton({
        icon: name,
        caret: true,
        name: name,
        title: me.getLang("labelMap")[name],
        click: function() {
            me.execCommand( name, getCurrentColor() );
        }
    });

    $colorLabel = $btn.edui().colorLabel();

    $colorPickerWidget = $.eduicolorpicker({
        name: name,
        lang_clearColor: me.getLang('clearColor') || '',
        lang_themeColor: me.getLang('themeColor') || '',
        lang_standardColor: me.getLang('standardColor') || ''
    })
        .on('pickcolor', function( evt, color ){
            window.setTimeout( function(){
                $colorLabel.css("backgroundColor", color);
                me.execCommand( name, color );
            }, 0 );
        })
        .on('show',function(){
            UM.setActiveWidget( colorPickerWidget.root() );
        }).css('zIndex',me.getOpt('zIndex') + 1);

    $btn.edui().on('arrowclick',function(){
        if(!$colorPickerWidget.parent().length){
            me.$container.find('.edui-dialog-container').append($colorPickerWidget);
        }
        $colorPickerWidget.edui().show($btn,{
            caretDir:"down",
            offsetTop:-5,
            offsetLeft:8,
            caretLeft:11,
            caretTop:-8
        });
        UM.setTopEditor(me);
    }).register('click', $btn, function () {
            $colorPickerWidget.edui().hide()
        });

    return $btn;

});

})(jQuery)