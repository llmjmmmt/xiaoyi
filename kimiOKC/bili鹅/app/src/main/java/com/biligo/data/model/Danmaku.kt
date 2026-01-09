package com.biligo.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

/**
 * 弹幕信息数据模型
 */
@Parcelize
data class DanmakuItem(
    @SerializedName("id")
    val id: Long,  // 弹幕ID
    
    @SerializedName("progress")
    val progress: Long,  // 出现时间（毫秒）
    
    @SerializedName("mode")
    val mode: Int,  // 弹幕模式 (1-滚动 2-底部 3-顶部 4-反转 5-特殊 6-代码 7-BAS)
    
    @SerializedName("fontsize")
    val fontSize: Int,  // 字体大小
    
    @SerializedName("color")
    val color: Int,  // 颜色
    
    @SerializedName("midHash")
    val userHash: String,  // 用户哈希
    
    @SerializedName("content")
    val content: String,  // 弹幕内容
    
    @SerializedName("ctime")
    val createTime: Long,  // 创建时间
    
    @SerializedName("weight")
    val weight: Int,  // 权重
    
    @SerializedName("action")
    val action: String,  // 动作
    
    @SerializedName("animation")
    val animation: String,  // 动画效果
    
    @SerializedName("pool")
    val pool: Int,  // 弹幕池
    
    @SerializedName("attr")
    val attr: Int  // 属性
) : Parcelable

/**
 * 弹幕模式定义
 */
object DanmakuMode {
    const val SCROLL = 1      // 滚动弹幕
    const val BOTTOM = 2      // 底部弹幕
    const val TOP = 3         // 顶部弹幕
    const val REVERSE = 4     // 反转弹幕
    const val SPECIAL = 5     // 特殊弹幕
    const val CODE = 6        // 代码弹幕
    const val BAS = 7         // BAS弹幕
    
    fun getModeDescription(mode: Int): String {
        return when (mode) {
            SCROLL -> "滚动"
            BOTTOM -> "底部"
            TOP -> "顶部"
            REVERSE -> "反转"
            SPECIAL -> "特殊"
            CODE -> "代码"
            BAS -> "BAS"
            else -> "未知"
        }
    }
}

/**
 * 弹幕颜色定义
 */
object DanmakuColor {
    const val WHITE = 0xFFFFFF
    const val RED = 0xFF0000
    const val BLUE = 0x0000FF
    const val GREEN = 0x00FF00
    const val YELLOW = 0xFFFF00
    const val PURPLE = 0x800080
    const val CYAN = 0x00FFFF
    const val ORANGE = 0xFFA500
    const val PINK = 0xFF69B4
    const val GRAY = 0x808080
    
    fun getColorList(): List<Int> {
        return listOf(
            WHITE,
            RED,
            BLUE,
            GREEN,
            YELLOW,
            PURPLE,
            CYAN,
            ORANGE,
            PINK,
            GRAY
        )
    }
    
    fun getColorName(color: Int): String {
        return when (color) {
            WHITE -> "白色"
            RED -> "红色"
            BLUE -> "蓝色"
            GREEN -> "绿色"
            YELLOW -> "黄色"
            PURPLE -> "紫色"
            CYAN -> "青色"
            ORANGE -> "橙色"
            PINK -> "粉色"
            GRAY -> "灰色"
            else -> "自定义"
        }
    }
}

/**
 * 弹幕字体大小定义
 */
object DanmakuFontSize {
    const val SMALL = 18
    const val NORMAL = 25
    const val LARGE = 36
    const val EXTRA_LARGE = 45
    
    fun getFontSizeList(): List<Int> {
        return listOf(SMALL, NORMAL, LARGE, EXTRA_LARGE)
    }
    
    fun getFontSizeName(size: Int): String {
        return when (size) {
            SMALL -> "小"
            NORMAL -> "中"
            LARGE -> "大"
            EXTRA_LARGE -> "特大"
            else -> "自定义"
        }
    }
}

/**
 * 弹幕池定义
 */
object DanmakuPool {
    const val NORMAL = 0      // 普通弹幕池
    const val SUBTITLE = 1    // 字幕弹幕池
    const val SPECIAL = 2     // 特殊弹幕池
}

/**
 * 弹幕发送请求
 */
data class SendDanmakuRequest(
    @SerializedName("type")
    val type: Int = 1,
    
    @SerializedName("oid")
    val oid: Long,  // 视频CID
    
    @SerializedName("msg")
    val message: String,  // 弹幕内容
    
    @SerializedName("aid")
    val aid: Long,  // 视频AID
    
    @SerializedName("bvid")
    val bvid: String,  // 视频BVID
    
    @SerializedName("progress")
    val progress: Int,  // 出现时间（毫秒）
    
    @SerializedName("color")
    val color: Int = DanmakuColor.WHITE,  // 颜色
    
    @SerializedName("fontsize")
    val fontSize: Int = DanmakuFontSize.NORMAL,  // 字体大小
    
    @SerializedName("mode")
    val mode: Int = DanmakuMode.SCROLL,  // 弹幕模式
    
    @SerializedName("pool")
    val pool: Int = DanmakuPool.NORMAL,  // 弹幕池
    
    @SerializedName("plat")
    val platform: Int = 2,  // 平台 (1-web 2-android 3-ios)
    
    @SerializedName("csrf")
    val csrf: String = ""  // CSRF token
)

/**
 * 弹幕响应
 */
data class DanmakuResponse(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: DanmakuResponseData?
)

data class DanmakuResponseData(
    @SerializedName("dmid")
    val danmakuId: Long,
    
    @SerializedName("dm_mask")
    val danmakuMask: String,
    
    @SerializedName("reply")
    val reply: String,
    
    @SerializedName("mode")
    val mode: Int,
    
    @SerializedName("msg")
    val message: String
)

/**
 * 弹幕设置
 */
data class DanmakuSettings(
    val enabled: Boolean = true,  // 是否启用弹幕
    val fontSize: Int = DanmakuFontSize.NORMAL,  // 字体大小
    val opacity: Float = 1.0f,  // 透明度
    val speed: Float = 1.0f,  // 滚动速度
    val displayArea: Float = 1.0f,  // 显示区域 (0.0-1.0)
    val showTop: Boolean = true,  // 显示顶部弹幕
    val showBottom: Boolean = true,  // 显示底部弹幕
    val showScroll: Boolean = true,  // 显示滚动弹幕
    val showColor: Boolean = true,  // 显示彩色弹幕
    val blockRepeat: Boolean = false,  // 屏蔽重复弹幕
    val blockScroll: Boolean = false,  // 屏蔽滚动弹幕
    val blockTop: Boolean = false,  // 屏蔽顶部弹幕
    val blockBottom: Boolean = false,  // 屏蔽底部弹幕
    val blockColor: Boolean = false,  // 屏蔽彩色弹幕
    val blockVisitor: Boolean = false,  // 屏蔽游客弹幕
    val blockLevel: Int = 0,  // 屏蔽等级 (0-不屏蔽 1-低 2-中 3-高)
    val blockKeywords: List<String> = emptyList()  // 屏蔽关键词
)