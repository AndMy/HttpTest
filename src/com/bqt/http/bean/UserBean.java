package com.bqt.http.bean;

import java.io.Serializable;

import org.json.JSONObject;

/**
 * 用户数据
 */
public class UserBean implements Serializable {
	private static final long serialVersionUID = 7667191114299135243L;

	/**
	 * 用户id
	 */
	private int uId;

	/**
	 * 用户名
	 */
	private String uName;

	/**
	 * 用户昵称
	 */
	private String uNickName;

	/**
	 * 用户头像地址
	 */
	private String uAvatarUrl;

	/**
	 * 临时用户
	 */
	private String uTempName;

	/**
	 * 用户类型，0 普通用户，1 主播，2 巡管，3 销售
	 */
	private int uType;

	/**
	 * 用户手机
	 */
	private String uPhone;

	/**
	 * 用户QQ
	 */
	private String uQQ;

	/**
	 * 用户性别，1男，2女
	 */
	private int uSex;

	/**
	 * 用户密码
	 */
	private String uPassword;

	/**
	 * 明星等级
	 */
	private int uStarLev;

	/**
	 * 财富等级
	 */
	private int uWealthLev;

	/**
	 * vip 类型，0  无，1 vip，2 svip，3 dvip
	 */
	private int vipType;

	/**
	 * vip剩余天数
	 */
	private int vipExpires;

	/**
	 * 用户注册类型
	 * 1 手机用户 
	 * 2 腾讯微博 
	 * 3 新浪微博
	 * 4 一键注册
	 * 5 QQ 
	 * 6 自动注册
	 * 7 微信登录
	 */
	private int uRegisterType;

	/**
	 * 个性签名
	 */
	private String uSignature;

	/**
	 * 用户余额
	 */
	private int uCoin;

	/**
	 * 免费礼物
	 */
	private int uFreeGift;

	/**
	 * 用户关注类型标签
	 */
	private String uAttnTags;

	/**
	 * 用户被关注数量
	 */
	private int uAttnedCount;

	/**
	 * 用户关注其他人数量
	 */
	private int uAttnOthersCount;

	/**
	 * 座驾时间
	 */
	private String uCarTime;

	/**
	 * 座驾
	 */
	private int uCarId;

	/**
	 * 用户所有座驾
	 */
	private String uAllCars;

	/**
	 * 用户当前明星升级经验值
	 */
	private long uCurStarExps;

	/**
	 * 用户下次明星升级的经验值
	 */
	private long uStarLevNextExps;

	/**
	 * 用户当前财富升级经验值
	 */
	private long uCurWealthExps;

	/**
	 * 用户下次财富升级的经验值
	 */
	private long uWealthLevNextExps;

	/**
	 * 用户Session
	 */
	private String uSessionId;

	/**
	 * 用户所有关注人的ids
	 */

	private String uAttnOthersIds;

	/**
	 * 用户开播提醒人的ids
	 */
	private String uLivingIds;

	/**
	 * 是否绑定手机
	 */
	private boolean uIsBindPhone;

	/**
	 * 是否购买同城
	 */
	private boolean uIsBuyTC;

	/**
	 * 第三方用户id
	 */
	private String third_uid;

	/**
	 * 第三方token
	 */
	private String third_token;

	/**
	 * 个人动态
	 */
	private String uDynamicJSON;

	/**
	 * 个人相册
	 */
	private String uAlbumJSON;

	/**
	 * 第三方有效时间
	 */
	private long third_expires;

	/**
	 * 临时额外身份1
	 */
	private String extra_identity;

	/**
	 * 临时额外身份2
	 */
	private String extra_identity2;

	/**
	 * 额外数据
	 */
	private String extraOther;

	/**
	 * 充值状态， 0 未充值，1已经充值 
	 */
	private int chargeStatus;

	/**
	 * 是否正在直播
	 */
	private int uIsLiving;

	/**
	 * 是否是守护
	 */
	private boolean isGuider;

	/**
	 * 新人礼包
	 */
	private String giftPack;

	/**
	 * 未读消息
	 */
	private int uUnreadNewsCount;

	/**
	 * 秀钻数量
	 */
	private int uDiamond;

	/**
	 * 用户徽章（","隔开）
	 */
	private String badgeIds;

	/**
	 * 用户徽章剩余时间（","隔开）
	 */
	private String badgeTimes;

	/**
	 * 隐身状态 ，0 正常状态， 1 隐身
	 */
	private int hideStatus;

	/**
	 * 初始化用户信息
	 */
	public UserBean(JSONObject json) {
		if (json == null || json.toString().equals("{}")) return;

		setVipExpires(json.optInt("viptime", 0));
		setuAllCars(json.optString("allcar", ""));
		setuCarTime(json.optString("allcar_time", ""));
		//updateBackPkgGiftsHashMap(json.optJSONArray("backpack_gifts"));// 背包

		JSONObject msg = json.optJSONObject("msg");
		if (msg != null) {
			setuAttnOthersIds(msg.optString("follow_list", ""));
			setuLivingIds(msg.optString("playremind_list", ""));
			setuId(msg.optInt("id", 0));
			setuIsLiving(msg.optInt("is_play", 0));
			setuType(msg.optInt("user_type", 0));
			setuName(msg.optString("username", ""));
			setuNickName(msg.optString("nickname", ""));
			setuPhone(msg.optString("phone", ""));
			setuQQ(msg.optString("qq", ""));
			setuSex(msg.optInt("sex", 1));
			setuAvatarUrl(msg.optString("head_image", ""));
			setuStarLev(msg.optInt("star_level", 0));
			setuWealthLev(msg.optInt("wealth_level", 0));
			setuRegisterType(msg.optInt("register_type", 4));
			setuSignature(msg.optString("signature", ""));
			setuCoin(msg.optInt("gold", 0));
			setuFreeGift(msg.optInt("free_gift", 0));
			setuIsBindPhone(msg.optInt("is_binding", 0) == 0 ? false : true);
			setuIsBuyTC(msg.optInt("tc_buy", 0) == 0 ? false : true);
			setuSessionId(msg.optString("session_id", ""));
			setuAttnTags(msg.optString("tags", ""));
			setuAttnedCount(msg.optInt("at_follow_num", 0));
			setuAttnOthersCount(msg.optInt("follow_num", 0));
			setuCurStarExps(msg.optLong("star_integral", 0L));
			setuStarLevNextExps(msg.optLong("star_next_integral", 0L));
			setuCurWealthExps(msg.optLong("wealth_integral", 0L));
			setuWealthLevNextExps(msg.optLong("wealth_next_integral", 0L));
			setChargeStatus(msg.optInt("firstPay", 0));
			setuUnreadNewsCount(msg.optInt("news_count", 0));
			setDiamond(msg.optInt("diamond", 0));
			setHideStatus(msg.optInt("dvip_hidden"));
		}

		JSONObject ownObj = json.optJSONObject("userown");
		if (ownObj != null) {
			setVipType(ownObj.optInt("vip", 0));
			setuCarId(ownObj.optInt("car", 0));
		}
		// 个人动态
		setuDynamicJSON(json.optJSONObject("dynamic").toString());
		// 相册
		setuAlbumJSON(json.optJSONArray("albums").toString());

		setThird_uid("");
		setThird_token("");
		setThird_expires(0L);
		setGiftPack(msg.optString("giftPackage95MM", "[]"));
		//setUserFamily( FamilyBean.parseFromJson(json.optJSONObject("family_info")));//家族
	}

	//**************************************************************************************************************************
	//
	//																											get、set方法
	//
	//**************************************************************************************************************************

	public int getuId() {
		return uId;
	}

	public void setuId(int uId) {
		this.uId = uId;
	}

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}

	public String getuNickName() {
		return uNickName;
	}

	public void setuNickName(String uNickName) {
		this.uNickName = uNickName;
	}

	public String getuAvatarUrl() {
		return uAvatarUrl;
	}

	public void setuAvatarUrl(String uAvatarUrl) {
		this.uAvatarUrl = uAvatarUrl;
	}

	public int getuWealthLev() {
		return uWealthLev;
	}

	public void setuWealthLev(int uWealthLev) {
		this.uWealthLev = uWealthLev;
	}

	public int getVipType() {
		return vipType;
	}

	public void setVipType(int vipType) {
		this.vipType = vipType;
	}

	public int getuType() {
		return uType;
	}

	public void setuType(int uType) {
		this.uType = uType;
	}

	public String getuPhone() {
		return uPhone;
	}

	public void setuPhone(String uPhone) {
		this.uPhone = uPhone;
	}

	public String getuQQ() {
		return uQQ;
	}

	public void setuQQ(String uQQ) {
		this.uQQ = uQQ;
	}

	public String getuPassword() {
		return uPassword;
	}

	public void setuPassword(String uPassword) {
		this.uPassword = uPassword;
	}

	public int getuSex() {
		return uSex;
	}

	public void setuSex(int uSex) {
		this.uSex = uSex;
	}

	public int getuStarLev() {
		return uStarLev;
	}

	public void setuStarLev(int uStarLev) {
		this.uStarLev = uStarLev;
	}

	public int getVipExpires() {
		return vipExpires;
	}

	public void setVipExpires(int vipExpires) {
		this.vipExpires = vipExpires;
	}

	public int getuRegisterType() {
		return uRegisterType;
	}

	public void setuRegisterType(int uRegisterType) {
		this.uRegisterType = uRegisterType;
	}

	public String getuSignature() {
		return uSignature;
	}

	public void setuSignature(String uSignature) {
		this.uSignature = uSignature;
	}

	public int getuCoin() {
		return uCoin;
	}

	public void setuCoin(int uCoin) {
		this.uCoin = uCoin;
	}

	public String getuAttnTags() {
		return uAttnTags;
	}

	public void setuAttnTags(String uAttnTags) {
		this.uAttnTags = uAttnTags;
	}

	public int getuAttnedCount() {
		return uAttnedCount;
	}

	public void setuAttnedCount(int uAttnedCount) {
		this.uAttnedCount = uAttnedCount;
	}

	public int getuAttnOthersCount() {
		return uAttnOthersCount;
	}

	public void setuAttnOthersCount(int uAttnOthersCount) {
		this.uAttnOthersCount = uAttnOthersCount;
	}

	public String getuCarTime() {
		return uCarTime;
	}

	public void setuCarTime(String uCarTime) {
		this.uCarTime = uCarTime;
	}

	public int getuCarId() {
		return uCarId;
	}

	public void setuCarId(int uCarId) {
		this.uCarId = uCarId;
	}

	public String getuAllCars() {
		return uAllCars;
	}

	public void setuAllCars(String uAllCars) {
		this.uAllCars = uAllCars;
	}

	public long getuCurStarExps() {
		return uCurStarExps;
	}

	public void setuCurStarExps(long uCurStarExps) {
		this.uCurStarExps = uCurStarExps;
	}

	public long getuStarLevNextExps() {
		return uStarLevNextExps;
	}

	public void setuStarLevNextExps(long uStarLevNextExps) {
		this.uStarLevNextExps = uStarLevNextExps;
	}

	public long getuCurWealthExps() {
		return uCurWealthExps;
	}

	public void setuCurWealthExps(long uCurWealthExps) {
		this.uCurWealthExps = uCurWealthExps;
	}

	public long getuWealthLevNextExps() {
		return uWealthLevNextExps;
	}

	public void setuWealthLevNextExps(long uWealthLevNextExps) {
		this.uWealthLevNextExps = uWealthLevNextExps;
	}

	public String getuSessionId() {
		return uSessionId;
	}

	public void setuSessionId(String uSessionId) {
		this.uSessionId = uSessionId;
	}

	public String getuAttnOthersIds() {
		return uAttnOthersIds;
	}

	public void setuAttnOthersIds(String uAttnOthersIds) {
		this.uAttnOthersIds = uAttnOthersIds;
	}

	public String getuLivingIds() {
		return uLivingIds;
	}

	public void setuLivingIds(String uLivingIds) {
		this.uLivingIds = uLivingIds;
	}

	public boolean getuIsBindPhone() {
		return uIsBindPhone;
	}

	public void setuIsBindPhone(boolean uIsBindPhone) {
		this.uIsBindPhone = uIsBindPhone;
	}

	public boolean getuIsBuyTC() {
		return uIsBuyTC;
	}

	public void setuIsBuyTC(boolean uIsBuyTC) {
		this.uIsBuyTC = uIsBuyTC;
	}

	public String getThird_uid() {
		return third_uid;
	}

	public void setThird_uid(String third_uid) {
		this.third_uid = third_uid;
	}

	public String getThird_token() {
		return third_token;
	}

	public void setThird_token(String third_token) {
		this.third_token = third_token;
	}

	public long getThird_expires() {
		return third_expires;
	}

	public void setThird_expires(long third_expires) {
		this.third_expires = third_expires;
	}

	public String getuTempName() {
		return uTempName;
	}

	public void setuTempName(String uTempName) {
		this.uTempName = uTempName;
	}

	public String getuDynamicJSON() {
		return uDynamicJSON;
	}

	public void setuDynamicJSON(String uDynamicJSON) {
		this.uDynamicJSON = uDynamicJSON;
	}

	public String getuAlbumJSON() {
		return uAlbumJSON;
	}

	public void setuAlbumJSON(String uAlbumJSON) {
		this.uAlbumJSON = uAlbumJSON;
	}

	public String getExtra_identity() {
		return extra_identity;
	}

	public void setExtra_identity(String extra_identity) {
		this.extra_identity = extra_identity;
	}

	public String getExtra_identity2() {
		return extra_identity2;
	}

	public void setExtra_identity2(String extra_identity2) {
		this.extra_identity2 = extra_identity2;
	}

	public String getExtraOther() {
		return extraOther;
	}

	public void setExtraOther(String extraOther) {
		this.extraOther = extraOther;
	}

	public int getuIsLiving() {
		return uIsLiving;
	}

	public void setuIsLiving(int uIsLiving) {
		this.uIsLiving = uIsLiving;
	}

	public int getChargeStatus() {
		return chargeStatus;
	}

	public void setChargeStatus(int chargeStatus) {
		this.chargeStatus = chargeStatus;
	}

	public boolean isGuider() {
		return isGuider;
	}

	public void setGuider(boolean isGuider) {
		this.isGuider = isGuider;
	}

	public int getuFreeGift() {
		return uFreeGift;
	}

	public void setuFreeGift(int uFreeGift) {
		this.uFreeGift = uFreeGift;
	}

	public int getuUnreadNewsCount() {
		return uUnreadNewsCount;
	}

	public void setuUnreadNewsCount(int uUnreadNewsCount) {
		this.uUnreadNewsCount = uUnreadNewsCount;
	}

	public String getGiftPack() {
		return giftPack;
	}

	public void setGiftPack(String giftPack) {
		this.giftPack = giftPack;
	}

	public int getDiamond() {
		return uDiamond;
	}

	public void setDiamond(int uDiamond) {
		this.uDiamond = uDiamond;
	}

	public String getBadgeIds() {
		return badgeIds;
	}

	public void setBadgeIds(String badgeIds) {
		this.badgeIds = badgeIds;
	}

	public String getBadgeTimes() {
		return badgeTimes;
	}

	public void setBadgeTimes(String badgeTimes) {
		this.badgeTimes = badgeTimes;
	}

	public int getHideStatus() {
		return hideStatus;
	}

	public void setHideStatus(int hideStatus) {
		this.hideStatus = hideStatus;
	}
}