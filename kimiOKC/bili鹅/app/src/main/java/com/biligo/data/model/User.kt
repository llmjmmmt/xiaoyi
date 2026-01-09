package com.biligo.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

/**
 * 用户信息数据模型
 */
@Parcelize
data class User(
    @SerializedName("mid")
    val mid: Long,  // 用户ID
    
    @SerializedName("uname")
    val userName: String,  // 用户名
    
    @SerializedName("face")
    val avatar: String,  // 头像URL
    
    @SerializedName("level")
    val level: Int,  // 等级
    
    @SerializedName("vipStatus")
    val vipStatus: Int,  // 大会员状态
    
    @SerializedName("sign")
    val signature: String? = null,  // 个性签名
    
    @SerializedName("follower")
    val followers: Int = 0,  // 粉丝数
    
    @SerializedName("following")
    val following: Int = 0,  // 关注数
    
    @SerializedName("archive_view")
    val videoViewCount: Long = 0,  // 稿件播放量
    
    @SerializedName("likes")
    val likesCount: Long = 0  // 获赞数
) : Parcelable

/**
 * 用户登录响应
 */
data class LoginResponse(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: LoginData?
)

data class LoginData(
    @SerializedName("access_token")
    val accessToken: String,
    
    @SerializedName("refresh_token")
    val refreshToken: String,
    
    @SerializedName("expires_in")
    val expiresIn: Long,
    
    @SerializedName("token_type")
    val tokenType: String,
    
    @SerializedName("user")
    val user: User
)

/**
 * 用户状态
 */
sealed class UserState {
    object Loading : UserState()
    data class LoggedIn(val user: User) : UserState()
    object NotLoggedIn : UserState()
    data class Error(val message: String) : UserState()
}