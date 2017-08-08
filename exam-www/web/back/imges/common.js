/**
 * Created by gaigechunfeng on 2017/3/26.
 */

(function (window, $) {

    var parseBasePath = function () {

        return '/www';
    }
    var errorLog = function () {
        if (console && typeof console.error === 'function') {
            console.error.call(null, arguments);
        }
    }
    var posCode = '省法院=	001;兰州中院=	002;城关区法院=	003;七里河区法院=	004;西固区法院=	005;红古区法院=	006;永登县法院=	007;皋兰县法院=	008;榆中县法院=	009;兰州新区法院=	010;嘉峪关中院=	011;城区法院=	012;金昌中院=	013;金川区法院=	014;永昌县法院=	015;白银中院=	016;白银区法院=	017;平川区法院=	018;景泰县法院=	019;会宁县法院=	020;靖远县法院=	021;天水中院=	022;秦州区法院=	023;麦积区法院=	024;张家川县法院=	025;武山县法院=	026;清水县法院=	027;甘谷县法院=	028;秦安县法院=	029;酒泉中院=	030;肃州区法院=	031;玉门市法院=	032;敦煌市法院=	033;瓜州县法院=	034;金塔县法院=	035;肃北县法院=	036;阿克塞县法院=	037;张掖中院=	038;甘州区法院=	039;高台县法院=	040;临泽县法院=	041;肃南县法院=	042;民乐县法院=	043;山丹县法院=	044;武威中院=	045;凉州区法院=	046;古浪县法院=	047;天祝县法院=	048;定西中院=	049;安定区法院=	050;通渭县法院=	051;陇西县法院=	052;临洮县法院=	053;岷县法院=	054;漳县法院=	055;渭源县法院=	056;陇南中院=	057;武都区法院=	058;西和县法院=	059;礼县法院=	060;徽县法院=	061;成县法院=	062;宕昌县法院=	063;两当县法院=	064;康县法院=	065;文县法院=	066;平凉中院=	067;崆峒区法院=	068;庄浪县法院=	069;华亭县法院=	070;静宁县法院=	071;泾川县法院=	072;崇信县法院=	073;灵台县法院=	074;庆阳中院=	075;西峰区法院=	076;正宁县法院=	077;镇原县法院=	078;庆城县法院=	079;宁县法院=	080;华池县法院=	081;环县法院=	082;合水县法院=	083;庆阳林区基层法院=	084;临夏中院=	085;临夏县法院=	086;积石山县法院=	087;康乐县法院=	088;广河县法院=	089;东乡县法院=	090;永靖县法院=	091;甘南中院=	092;夏河县法院=	093;碌曲县法院=	094;玛曲县法院=	095;临潭县法院=	096;卓尼县法院=	097;迭部县法院=	098;舟曲县法院=	099;兰州铁路运输中级法院=	100;兰州铁路运输中级法院（机动名额）=	101;兰州铁路运输法院=	102;武威铁路运输法院=	103;甘肃省林区中级法院=	104;甘肃省林区中级法院（机动名额）=	105;卓尼林区基层法院=	106;迭部林区基层法院=	107;舟曲林区基层法院=	108;文县林区基层法院=	109;甘肃矿区人民法院=	110;甘肃矿区人民法院（机动名额）=	111;';

    window.App = {

        basePath: parseBasePath(),
        currUser: null,
        currTime: null,
        isFront: self.location.href.indexOf(parseBasePath() + '/front/') > 0,
        getPosCode: function () {
            var kvs = posCode.split(';'), obj = {};
            for (var i = 0, len = kvs.length; i < len; i++) {
                var kv = kvs[i];
                var kvArr = kv.split("=");
                if (kvArr.length === 2) {
                    obj[$.trim(kvArr[1])] = $.trim(kvArr[0]);
                }
            }
            return obj;
        },
        renderPosSelect: function (eid, appendAll) {

            var posCode = App.getPosCode(), html = [];
            if (appendAll) {
                html.push('<option value="">全部</option>');
            }
            for (var k in posCode) {
                html.push('<option value="' + k + '">' + (posCode[k] + '-' + k) + '</option>')
            }
            $('#' + eid).html(html.join(''))
        },
        parseFileName: function (fullName) {

            var pos;
            if ((pos = fullName.lastIndexOf("\\")) > 0) {
                return fullName.substring(pos + 1);
            }
            return fullName;
        },
        requestFullScreen: function () {
            var docElm = document.documentElement;

            //W3C
            if (docElm.requestFullscreen) {
                docElm.requestFullscreen();
            }
            //FireFox
            else if (docElm.mozRequestFullScreen) {
                docElm.mozRequestFullScreen();
            }
            //Chrome等
            else if (docElm.webkitRequestFullScreen) {
                docElm.webkitRequestFullScreen();
            }
            //IE11
            else if (elem.msRequestFullscreen) {
                elem.msRequestFullscreen();
            }
        },
        stopEvent: function (e) {

            e.preventDefault();
            e.stopImmediatePropagation();

            e.returnValue = false;
            return false;
        },
        logout: function (notAlert) {

            var _fn = function () {
                $.post(App.basePath + '/front/logout.do', {}, function (data) {

                    self.location.reload()
                }, 'json');
            }
            if (notAlert) {
                _fn();
            } else {
                if (confirm('你确认要退出系统吗？')) {
                    _fn();
                }
            }

        },
        getParam: function (key) {

            var l = window.self.location.href;
            var o = {};
            if (l.indexOf("?") > 0) {
                l = l.substring(l.lastIndexOf("?") + 1);
                if (l.indexOf("&") >= 0) {
                    var ls = l.split("&");
                    for (var i = 0, len = ls.length; i < len; i++) {
                        var sl = ls[i]
                        if (sl.indexOf('=') >= 0) {
                            var pos = sl.indexOf("=");
                            var k = sl.substring(0, pos),
                                v = sl.substring(pos + 1);

                            o[k] = v;
                        }
                    }

                } else if (l.indexOf("=") >= 0) {

                    var pos = l.indexOf("=");
                    var k = l.substring(0, pos),
                        v = l.substring(pos + 1);

                    o[k] = v;
                }
            }

            return o[key];
        },
        checkAdminLogin: function () {

            return $.get(App.basePath + "/back/checkLogin.do", {}, function (data) {

                if (data && data.success) {
                    App.currUser = data.obj;
                }
            }, "json");
        },
        checkLogin: function () {
            return $.get(App.basePath + "/front/checkLogin.do?_d=" + new Date().getTime(), {}, function (d) {

                if (d && d.success) {
                    var isLoopStart = !!App.currTime;
                    App.currUser = d && d.obj && d.obj.user ? d.obj.user : null;
                    // App.currTime = d.time ? parseInt(d.time) : new Date().getTime();
                    App.currTime = parseInt(d.obj.time);

                    var _countDown = function () {
                        App.currTime += 1000;
                        setTimeout(arguments.callee, 1000);
                    }

                    if (!isLoopStart) {
                        setTimeout(_countDown, 1000);
                    }

                    setTimeout(App.checkLogin, 5000);
                }
            }, "json");
        },
        check2AdminLogin: function (data) {

            if (!data) {
                errorLog('check2AdminLogin error,return value is null');
                return;
            }
            if (!data.success) {
                window.location.assign(parseBasePath() + "/back/back_login.html");
            } else {
                window.App.currUser = data.obj;
            }
        },
        toFormJson: function (form) {

            var inputs = $(':text,:password', form);
            var data = {};

            inputs.each(function (i, input) {

                var $in = $(input),
                    k = $in.attr('name');
                data[k] = $.trim($in.val());
            });

            return data;
        },
        error: errorLog,
        showTime: function (el, t) {

            t = t || 1000;
            var _fn = function () {
                if (!App.currTime) return;
                var time = new Date(App.currTime);
                var year = time.getFullYear();
                var month = time.getMonth() + 1;
                var day = time.getDate();
                var hour = time.getHours();
                var minute = time.getMinutes();
                var second = time.getSeconds();
                var weekArr = ['星期天', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
                var week = weekArr[time.getDay()]

                hour = hour < 10 ? '0' + hour : hour;
                minute = minute < 10 ? '0' + minute : minute;
                second = second < 10 ? '0' + second : second;

                var txt = "<strong>" + (App.currUser ? (App.isFront ? App.currUser.name : App.currUser.username) : '') + "</strong>,您好，现在是:" + year + "年" + month + "月" + day + "日&nbsp;&nbsp;" + week + "&nbsp;&nbsp;" + hour + ":" + minute + ":" + second + "&nbsp;&nbsp;";
                $('#' + el).html(txt);

                setTimeout(_fn, t)
            }

            _fn();
            // window.setInterval(_fn, t);
        },
        logoutAdmin: function () {
            if (confirm('您确认要退出系统吗？')) {
                $.post(App.basePath + "/back/logout.do", {}, function (data) {

                    window.top.location.assign(App.basePath + "/back/back_login.html");
                }, "JSON");
            }
        },
        changePwd: function () {
            var btn = $(this),
                form = btn.parents('form'),
                oldPwdInput = $('input[name=oldPwd]', form),
                newPwdInput = $('input[name=newPwd]', form),
                newPwdaInput = $('input[name=newPwda]', form)

            if (!$.trim(oldPwdInput.val())) {
                alert('请输入原密码！');
                return false;
            }
            if (!$.trim(newPwdInput.val())) {
                alert('请输入新密码！');
                return false;
            }
            if (!$.trim(newPwdaInput.val())) {
                alert('请重复输入新密码！');
                return false;
            }
            if ($.trim(newPwdInput.val()) !== $.trim(newPwdaInput.val())) {
                alert('重复输入的密码不一致！');
                return false;
            }

            $.post(App.basePath + '/back/changePwd.do', App.toFormJson(form), function (data) {

                if (!data) {
                    return
                }
                if (data.success) {
                    alert('修改密码成功！');
                } else {
                    alert('发生错误：' + data.errmsg);
                }
            }, "json");
        },
        renderTable: function (containerId, url, renderRecord, pageId, doneFn) {

            var pageInfo = {
                currPage: 1,
                pageSize: 10,
                allPage: 1
            }
            var _load,
                _firstFn = function () {
                    pageInfo.currPage = 1;
                    _load();
                },
                _prevFn = function () {
                    if (pageInfo.currPage > 1) {
                        pageInfo.currPage--;
                        _load();
                    }
                },
                _nextFn = function () {
                    if (pageInfo.currPage < pageInfo.allPage) {
                        pageInfo.currPage++;
                        _load();
                    }
                },
                _lastFn = function () {
                    pageInfo.currPage = pageInfo.allPage;
                    _load();
                }


            _load = function () {
                $.get(App.basePath + url, pageInfo, function (data) {

                    if (data && data.success) {
                        var obj = data.obj,
                            list = obj.list;

                        pageInfo.allPage = obj.allPage || 1;
                        if (list) {
                            var html = [];
                            for (var i = 0; i < list.length; i++) {
                                if (typeof renderRecord === 'function') {
                                    html.push(renderRecord(list[i], i, pageInfo));
                                }
                            }
                            $('#' + containerId).html(html.join(''));
                        }

                        var pageHtml = '<a href="javascript:void(0);" id="firstPageBtn">首 页</a>&nbsp; ' +
                            '<a href="javascript:void(0);" id="prevPageBtn">上一页</a>&nbsp;' +
                            '第<span class="STYLE3">' + pageInfo.currPage + '</span> 页 ' +
                            '<a href="javascript:void(0);" id="nextPageBtn">下一页</a>&nbsp; ' +
                            '<a href="javascript:void(0);" id="lastPageBtn">尾 页</a>&nbsp;' +
                            '共 <span class="STYLE3">' + pageInfo.allPage + ' </span>页';
                        $('#' + pageId).html(pageHtml);

                        $('#firstPageBtn').on('click', _firstFn)
                        $('#prevPageBtn').on('click', _prevFn);
                        $('#nextPageBtn').on('click', _nextFn);
                        $('#lastPageBtn').on('click', _lastFn);
                    }

                    if (typeof doneFn === 'function') {
                        doneFn(data);
                    }
                }, 'json');
            }

            _load();
        },
        audioplayer: function (id, file, loop) {
            var audioplayer = document.getElementById(id);
            if (audioplayer != null) {
                document.body.removeChild(audioplayer);
            }

            if (typeof(file) != 'undefined') {
                if (navigator.userAgent.indexOf("MSIE") > 0) {// IE

                    var player = document.createElement('bgsound');
                    player.id = id;
                    player.src = file['mp3'];
                    player.setAttribute('autostart', 'true');
                    if (loop) {
                        player.setAttribute('loop', 'infinite');
                    }
                    document.body.appendChild(player);

                } else { // Other FF Chome Safari Opera

                    var player = document.createElement('audio');
                    player.id = id;
                    player.setAttribute('autoplay', 'autoplay');
                    if (loop) {
                        player.setAttribute('loop', 'loop');
                    }
                    document.body.appendChild(player);

                    if (file['mp3']) {
                        var mp3 = document.createElement('source');
                        mp3.src = file['mp3'];
                        mp3.type = 'audio/mpeg';
                        player.appendChild(mp3);
                    }

                    if (file['ogg']) {
                        var ogg = document.createElement('source');
                        ogg.src = file['ogg'];
                        ogg.type = 'audio/ogg';
                        player.appendChild(ogg);
                    }


                }
            }
        },
        getTimeStamp: function (t) {
            var h = Math.floor(t / 3600000),
                m = Math.floor((t - h * 3600000) / 60000),
                s = Math.floor((t - h * 3600000 - m * 60000) / 1000);

            var hh = h < 10 ? '0' + h : h,
                mm = m < 10 ? '0' + m : m,
                ss = s < 10 ? '0' + s : s;

            return {
                h: hh,
                m: mm,
                s: ss
            }
        }
    };

})(window, jQuery);