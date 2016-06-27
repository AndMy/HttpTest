package com.bqt.http.bean;

import android.os.Environment;

/**
 * URL常量
 */
public class UrlOfServer {
	/**
	 * 积分兑换
	 */
	public static final String RQ_GUESS_EXCHARGE_TO_COIN = "/recreation/guess_exchange.php";

	/**
	 * 积分兑换信息
	 */
	public static final String RQ_GUESS_EXCHARGE_INFOR = "/recreation/guess_exchange_list.php";

	/**
	 * 视频监控
	 */
	public static final String RQ_MONITOR_VIDEO = "/log/video_log.php";

	/**
	 * 获取中奖记录
	 */
	public static final String RQ_GET_LOTTERTY_RECORD = "/recreation/draw_record.php";
	/**
	 * 获取.so信息
	 */
	public static final String RQ_GET_SO_VERSION = "/vedioplugins/down_vedio_plugins.php";

	/**
	 * 获取礼物信息
	 */
	public static final String RQ_GET_GIFTS_INFOR_V2 = "/gift/update_giftv2.php";

	/**
	 * 获取礼物信息
	 */
	public static final String RQ_GET_GIFTS_INFOR = "/gift/update_gift.php";

	/**
	 * 获取礼包
	 */
	public static final String RQ_BUY_GIFT_URL = "/pay2/httpspay/gifts_payv4.php";
	/**
	 * 举报用户接口
	 */
	public static final String RQ_INFORM = "/report/put_report.php";
	/**
	 * 周星礼物
	 */
	public static final String RQ_WEEKSTAR = "/hall_fame/week_gift.php";
	/**
	 * 主播周星礼物排名
	 */
	public static final String ANCHOR_WEEK_RANK = "/app/charts/week_star_ranking.php";

	/**
	 * 获取直播地址
	 */
	public static final String RQ_LIVE_URL = "/api/getkey.php";

	/**
	 * 获取昵称状态
	 */
	public static final String RQ_GET_NICK_NAME_STATE = "/user/check_nickname.php";

	/**
	 * 检查礼包状态
	 */
	public static final String RQ_GIFT_BAG_STATE = "/app/giftpackge/get_giftpackge_id.php";

	/**
	 * 购买物品，在6.1.0 版本该接口仅保留点歌,修改昵称购买，购买vip, svip, 座驾，使用下面新的接口
	 */
	public static final String RQ_BUY_GOODS = "/pay/pay_gold.php";

	/**
	 * 6.1.0版本使用新接口, 除点歌外
	 */
	public static final String RQ_BUY_GOODS_NEW = "/app/mallbuy/gold_buy.php";

	/**
	 * 个人档案 获取主播信息
	 */
	public static final String RQ_DOC_ANCHOR_DOCUMENT = "/user/skip_room.php";

	/**
	 * 个人档案
	 */
	public static final String RQ_USER_DOCUMENT = "/myprofile/profileinfo.php";
	/**
	 * 搜索主播
	 */
	public static final String SEARCH_ANCHOR = "/discovery/find_anchor.php";

	/**
	 * 关注
	 */
	public static final String RQ_ATTENTION = "/user/edit_follow.php";

	/**
	 * 开播提醒
	 */
	public static final String RQ_Living_Remind = "/user/playremind.php";

	/**
	 * 上传相片
	 */
	public static final String RQ_UPLOAD_PHOTO = "/user/photo.php";
	/**
	 * 关注
	 */
	public static final String RQ_USER_ATTEN = "/myprofile/follow_list.php";

	/**
	 * 我的
	 */
	public static final String RQ_MINE = "/myprofile/person_followdynamics.php";

	/**
	 * 最近访问
	 */
	public static final String RQ_LATEST_VISIT = "/woxiu/visited.php";

	/**
	 * 个性签名
	 */
	public static final String RQ_USER_SIGNATURE = "/user/edit_signature.php";

	/**
	 * 检查个性化账号是否弹窗
	 */
	public static final String PERSIONAL_CHEEK = "/user/verify_alter.php";

	/**
	 * 检查个性化账号修改
	 */
	public static final String PERSIONAL_MODIFY = "/user/alter_info.php";

	/**
	 * 我的动态
	 */
	public static final String RQ_PERSON_DYNAMIC = "/circle/person_dynamics.php";

	/**
	 * 切换座驾
	 */
	public static final String RQ_SWITCH_MOUNT = "/woxiu/choose_car.php";

	/**
	 * 周星明星榜榜
	 */
	public static final String RQ_WEEK_STAR_CHART = "/hall_fame/week_v2.php";

	/**
	 * 周星富豪榜
	 */
	public static final String RQ_WEEK_WEALTH_CHART = "/hall_fame/week_star_rich.php";

	/**
	 * 魅力榜
	 */
	public static final String RQ_CHARM_CHART = "/hall_fame/charm.php";

	/**
	 * 明星榜
	 */
	public static final String RQ_STAR_CHART = "/hall_fame/star.php";

	/**
	 * 人气榜
	 */
	public static final String RQ_CHARM_POPULAR = "/hall_fame/popularity.php";

	/**
	 * 富豪榜
	 */
	public static final String RQ_WEALTH_CHART = "/hall_fame/rich.php";

	/**
	 * 发布动态
	 */
	public static final String RQ_SEND_DYNAMIC = "/circle/add_weibo.php";

	/**
	 * 直播大厅5.x
	 */
	public static final String RQ_LIVE = "/show/anchor_list_v3.php";

	/**
	 * 发表回复
	 */
	public static final String RQ_DYNAMIC_REPLY = "/circle/add_comment.php";

	/**
	 * 删除动态
	 */
	public static final String RQ_DYNAMIC_DELETE = "/circle/delete_weibo.php";

	/**
	 * 动态点赞
	 */
	public static final String RQ_DYNAMIC_PRAISE = "/circle/ding.php";

	/**
	 * 推荐主播列表
	 */
	public static final String RQ_RECOMMENT_ANCHOR = "/circle/recommend_list.php";

	/**
	 * 动态
	 */
	public static final String RQ_DYNAMIC = "/circle/dynamics.php";

	/**
	 * 动态详情
	 */
	public static final String RQ_DYNAMIC_DETAIL = "/circle/weibo_detail.php";

	/**
	 * 服务中心
	 */
	public static final String RQ_SERVER_CENTER = "/api/customerServerInfo.php";

	/**
	 * 新手宝典
	 */
	public static final String RQ_NEWER_HELP = "/api/rookiehelp.php";

	/**
	 * 更新版本
	 */
	public static final String RQ_VERSION_UPDATE = "/api/update.php";

	/**
	 * 4.0.6版本开启该接口
	 */
	public static final String RQ_REVERIFY_USER = "/user/sessionv2.php";

	/** 
	 * 积分奖励
	 */
	public static final String RQ_TASK_INTEGRAL = "/task/integral.php";

	/**
	 * 任务奖励--每日分享
	 */
	public static final String RQ_TASK_SHARE = "/task/share.php";

	/**
	 * apk保存路径
	 */
	public static final String APK_PATH = Environment.getExternalStorageDirectory() + "/95xiu/game";

	/**
	 * 关注列表，参数uid 为用户ID get方式 
	 */
	public static final String RQ_GIRL_ATTENTION = "/woxiu/my_keep_anchor.php";

	/**
	 * 点歌台主播歌单列表4.0.6开始新接口
	 */
	public static final String SONG_STATE_LIST = "/user/song_listv2.php";

	/**
	 * 已点歌单对接GET
	 */
	public static final String SONG_READY = "/user/song_ordering.php";

	/**
	 * 添加和取消 关注
	 */
	public static final String RQ_GIRL_KEEP_ATTENTION = "/woxiu/keep_anchor.php";

	/**
	 * 开播提醒推送
	 */
	public static final String RQ_KAIBO_ATTEN = "/user/anchor_remind.php";

	/**
	 * 登陆，5.x开始启用
	 */
	public static final String RQ_LOGIN = "/user/loginv2.php";

	/**
	 * 修改密码
	 */
	public static final String RQ_MODIFY_PASS = "/myprofile/editpassword.php";

	/**
	 * 编辑资料
	 */
	public static final String RQ_MSG_EDIT = "/myprofile/editinfo.php";

	/**
	 * 获取验证码
	 */
	public static final String RQ_CHECK_NUM = "/myprofile/sendsms.php";

	/**
	 * 重置密码
	 */
	public static final String RQ_RESET_PASS = "/user/resetpass.php";

	/**
	 * 验证码用于注册
	 */
	public static final String CHECK_NUM_FOR_REG = "1";

	/**
	 * 验证码用于找回密码
	 */
	public static final String CHECK_NUM_FOR_GETPW = "2";

	/**
	 * 绑定手机
	 */
	public static final String CHECK_NUM_BINDING = "3";

	/**
	 * 验证原手机
	 */
	public static final String CHECK_NUM_OLD_BINDING = "4";

	/**
	 * 验证码验证
	 */
	public static final String RQ_VERIFY = "/user/verify.php";

	/**
	 * 手机用户找回密码
	 */
	public static final String FIND_PASS_WORD = "/myprofile/findpassword.php";

	/**
	 * 获取服务器为用户分配的用户名
	 */
	public static final String RQ_GET_TEMP_USER_ID = "/user/getuser.php";

	public static final String RQ_REG_TEMP = "/user/reg.php";

	/**
	 * 更新金额信息返回结果
	 */
	public static final String RQ_UPDATA_MONEY = "/api/shop.php";

	/**
	 * 绑定手机
	 */
	public static final String RQ_BINDING = "/user/binding.php";

	/**
	 * 解除手机绑定，用于重新绑定手机时，要先验证解除原来手机的绑定
	 */
	public static final String RQ_UNBIND_PHONE = "/myprofile/unbin.php";

	/**
	 * 注册接口
	 */
	public static final String RQ_REGISTER = "/user/registerv2.php";

	/**
	 * 历史记录的接口
	 */
	public static final String RQ_RECGIRL = "/woxiu/anchor_isstart.php";

	/**
	 * 同城主播
	 */
	public static final String RQ_TCGIRL = "/woxiu/anchor_city.php";

	/**
	 * 统计 点击排行
	 */
	public static final String CLICK = "/woxiu/anchor_click.php";

	/**
	 * 获取主播排行
	 */
	public static final String CHECK_ORDER = "/api/check_order.php";

	/**
	 * 切换 座驾，参数uid = 用户id，参数car = 座驾id
	 */
	public static final String RQ_CHOOSE_CAR = "/woxiu/choose_car.php";

	/**
	 * 修改昵称
	 */
	public static final String REPLACE_NICK = "/user/edit_nickname.php";

	/**
	 * 家族首页
	 */
	public static final String FAMILY_MAIN = "/family/index.php";

	/**
	 * 家族主播/成员接口 5.1.0版本上开始使用
	 */
	public static final String FAMILY_ANCHOR_MEMBER = "/app/family/family_member.php";

	/**
	 * 加入家族
	 */
	public static final String FAMILY_APPLY_JOIN = "/family/join_family.php";

	/**
	 *  搜索帮会
	 */
	public static final String FAMILY_SEARCH = "/app/family/familysearch.php";

	/**
	 * 获取座驾信息
	 */
	public static final String CHECK_CAR_INFOS = "/car/update_car.php";

	/**
	 * 同城主播
	 */
	public static final String SAME_CITY_ANCHORS = "/discovery/city_wide.php";

	/**
	 * 创建帮会记录接口
	 */
	public static final String GROUP_CREATE = "/app/log/user_bang.php";

	/**
	 * 帮会声望榜接口
	 */
	public static final String RQ_BANGHUI_REPUTATION = "/app/charts/family_reputationv2.php";

	/**
	 * 加入帮会列表接口
	 */
	public static final String RQ_FAMILY_JOIN_LIST = "/app/family/familyjoinlist.php";

	/**
	 *荣誉墙接口
	 */
	public static final String RQ_FAMILY_HONOR_WALL = "/app/family/honor_wall.php";

	/**
	 * 个人相册
	 */
	public static final String PERSIONAL_PHOTO = "/myprofile/viewphotos.php";

	/**
	 * 发现主界面
	 */
	public static final String MAIN_TAB_FIND = "/discovery/index.php";

	/**
	 * 即时礼物榜
	 */
	public static final String RQ_DISCOVERY_SORT_GIFT_ANCHORS = "/discovery/gif_topanchor.php";

	/**
	 * 消息
	 */
	public static final String RQ_NOTIFICATION = "/app/news/index.php";

	/**
	 * 请求活动消息和系统消息  （同一个接口）
	 */
	public static final String RQ_ACTIVITY_NOTIFICATION_OR_SYSTEM_NOTIFICAITON = "/app/news/news_details.php";

	/**
	 * 删除活动消息和系统消息  （同一个接口）
	 */
	public static final String RQ_DELETE_ACTIVITY_NOTIFICATION_OR_SYSTEM_NOTIFICAITON = "/app/news/edit_news.php";

	/**
	 * 帮会创建条件[检测]接口-bqt
	 */
	public static final String GROUP_CREATE_URL = "/app/family/familycreate.php";

	/**
	 * 帮会创建信息[填写]接口-bqt
	 */
	public static final String GROUP_REGISTER_URL = "/app/family/familyregister.php";

	/**
	 * 帮会[修改]信息填写接口-bqt
	 */
	public static final String GROUP_ALTER_URL = "/app/family/familygangeditinfo.php";

	/**
	 * 帮会[修改名称]信息接口-bqt
	 */
	public static final String GROUP_ALTER_NAME = "/app/family/familyname_edit.php";

	/**
	 * 审批申请加入帮会[列表]-bqt
	 */
	public static final String GROUP_APPLY_LIST = "/app/family/familygangjoin_list.php";

	/**
	 * 冠名主播[列表]-bqt
	 */
	public static final String GROUP_FAMILY_BANG_LIST = "/app/family/checkallfamilybang_detail.php";

	/**
	 * [清除]帮会申请加入消息-bqt
	 */
	public static final String GROUP_APPLY_CLEAN = "/app/family/clearalljoinnews.php";

	/**
	 * 帮会申请加入[审核]接口-bqt
	 */
	public static final String GROUP_APPLY_DEAL = "/app/family/familygangjoin_deal.php";

	/**
	 * 秀钻兑换接口
	 */
	public static final String RQ_COINS_EXCHARGE = "/user2/diamond_exchange.php";

	/**
	 * 任命副帮主接口
	 */
	public static final String RQ_ASSISTANT_MANAGER = "/app/family/familydeputyleader.php";

	/**
	 * 解除副帮主接口
	 */
	public static final String RQ_DISMISS_MANAGER = "/app/family/deputyleaderquite.php";

	/**
	 * 帮会成员踢除成员接口
	 */
	public static final String RQ_FIRE_MEMBER = "/app/family/familymemberkick.php";

	/**
	 * 帮会详情信息
	 */
	public static final String RQ_FAMILY_DETAIL = "/app/family/familyprofile.php";

	/**
	 * 帮会签到
	 */
	public static final String RQ_FAMILY_SIGN = "/app/family/familysign.php";

	/**
	 * 设置是否隐身
	 */
	public static final String RQ_SETTING_HIDE = "/app/user/divp_hidden.php";

	/**
	 * 头条免费礼物
	 */
	public static final String RQ_HEADLINE_GIFT = "/app/user/get_headlines_gift.php";

	/**
	 *  推荐应用
	 */
	public static final String RQ_RECOMMD_APP = "/api/game.php";

	/**
	 * 下载反馈
	 */
	public static final String SEND_DOWNLOAD_STATUS = "/api/gameDown.php";

	//**************************************************************************************************************************
	//                                                                                                               链接
	//**************************************************************************************************************************

	/**
	 * 一元购地址
	 */
	public static final String WEB_ONE_BUY = "http://www.95xiu.com/one/phone";

	/**
	 * 刷量
	 */
	public static final String FRESH_MM = "http://api.234wan.com/api/newactive.php";

	public static final String FRUIT_GAME = "http://fruit.92xiu.com?key=";

	/**
	 * 帮会简介链接
	 */
	public static final String GROUP_INTRODUCTION_URL = "http://api2.95xiu.com/web/active.php?id=107";

	//**************************************************************************************************************************
	//                                                                                                               其他
	//**************************************************************************************************************************

	/**
	 * 请求成功
	 */
	public static final int REQUEST_SUCCESS = 1;
	/**
	 * 请求失败
	 */
	public static final int REQUEST_FAIL = 0;

	/**
	 * 下载动作
	 */
	public static final String ACTION = "android.intent.action.95xiu.download";

	/**
	 * 下载等待中
	 */
	public static final int DOWNLOAD_WAIT = 0;

	/**
	 * 下载开始
	 */
	public static final int DOWNLOAD_START = 1;

	/**
	 * 下载任务启动
	 */
	public static final int DOWNLOAD_DOWNLOAD = 2;

	/**
	 * 下载中
	 */
	public static final int DOWNLOAD_DOWNLOADING = 3;

	/**
	 * 下载暂停
	 */
	public static final int DOWNLOAD_PAUSE = 4;

	/**
	 * 下载取消
	 */
	public static final int DOWNLOAD_CANCEL = 6;

	/**
	 * 下载失败
	 */
	public static final int DOWNLOAD_FAIL = 7;

	/**
	 * 下载成功
	 */
	public static final int DOWNLOAD_SUCCESS = 8;

	/**
	 * 启动安装
	 */
	public static final int DOWNLOAD_INSTALL = 9;

	/**
	 * 安装成功
	 */
	public static final int INSTALL_SUCCESS = 10;

	/**
	 * 安装失败
	 */
	public static final int INSTALL_FAIL = 11;

	/**
	 * 任务已存在
	 */
	public static final int TASK_EXIST = 12;

	/**
	 * 任务添加
	 */
	public static final int TASK_ADD = 13;

	/**
	 * 任务获取失败
	 */
	public static final int TASK_FAIL = 14;

	/**
	 * 下载任务开始
	 */
	public static final int TASK_START = 15;

	/**
	 * 检查服务是否被杀的时间间隔
	 */
	public static final long START_SERVICE_TIME = 20000L;

	/**
	 * 验证码有效期2分钟
	 */
	public static final int TIME_OUT = 120;

	/**
	 *  中国移动
	 */
	public static final int CMCC = 1;

	/**
	 * 中国电信
	 */
	public static final int Telecom = 2;

	/**
	 * 中国联通
	 */
	public static final int Unicom = 3;

	/**
	 * 未知运营商
	 */
	public static final int Un_known = 0;

	public static String FAMILY_INFO = "family_info";
	public static final int TOP_RANK = 0;
	public static final int UNTOP_RANK = 1;
}