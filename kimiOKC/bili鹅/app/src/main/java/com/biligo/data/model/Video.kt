package com.biligo.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

/**
 * 视频信息数据模型
 */
@Parcelize
data class Video(
    @SerializedName("bvid")
    val bvid: String,  // BV号
    
    @SerializedName("aid")
    val aid: Long,  // AV号
    
    @SerializedName("title")
    val title: String,  // 标题
    
    @SerializedName("pic")
    val cover: String,  // 封面URL
    
    @SerializedName("author")
    val author: String,  // UP主名称
    
    @SerializedName("mid")
    val authorId: Long,  // UP主ID
    
    @SerializedName("play")
    val playCount: Long,  // 播放量
    
    @SerializedName("danmaku")
    val danmakuCount: Long,  // 弹幕数
    
    @SerializedName("duration")
    val duration: String,  // 时长 (格式: mm:ss 或 hh:mm:ss)
    
    @SerializedName("cid")
    val cid: Long,  // 用于获取播放地址的ID
    
    @SerializedName("desc")
    val description: String? = null,  // 简介
    
    @SerializedName("pubdate")
    val publishTime: Long = 0,  // 发布时间
    
    @SerializedName("like")
    val likeCount: Long = 0,  // 点赞数
    
    @SerializedName("coin")
    val coinCount: Long = 0,  // 投币数
    
    @SerializedName("favorite")
    val favoriteCount: Long = 0,  // 收藏数
    
    @SerializedName("share")
    val shareCount: Long = 0,  // 分享数
    
    @SerializedName("view_at")
    var viewProgress: Long = 0  // 观看进度（本地字段）
) : Parcelable {
    /**
     * 获取格式化的播放量
     */
    fun getFormattedPlayCount(): String {
        return when {
            playCount >= 10000 -> "${"%.1f".format(playCount / 10000.0)}万"
            playCount >= 1000 -> "${"%.1f".format(playCount / 1000.0)}千"
            else -> playCount.toString()
        }
    }
    
    /**
     * 获取格式化的弹幕数
     */
    fun getFormattedDanmakuCount(): String {
        return when {
            danmakuCount >= 10000 -> "${"%.1f".format(danmakuCount / 10000.0)}万"
            danmakuCount >= 1000 -> "${"%.1f".format(danmakuCount / 1000.0)}千"
            else -> danmakuCount.toString()
        }
    }
}

/**
 * 视频列表响应
 */
data class VideoListResponse(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: VideoListData?
)

data class VideoListData(
    @SerializedName("item")
    val videos: List<Video>,
    
    @SerializedName("has_more")
    val hasMore: Boolean = false,
    
    @SerializedName("next_offset")
    val nextOffset: String? = null
)

/**
 * 视频详情响应
 */
data class VideoDetailResponse(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: VideoDetailData?
)

data class VideoDetailData(
    @SerializedName("View")
    val video: Video,
    
    @SerializedName("Tags")
    val tags: List<VideoTag>,
    
    @SerializedName("pages")
    val pages: List<VideoPage>,
    
    @SerializedName("subtitle")
    val subtitles: List<SubtitleInfo>?
)

/**
 * 视频标签
 */
@Parcelize
data class VideoTag(
    @SerializedName("tag_id")
    val tagId: Long,
    
    @SerializedName("tag_name")
    val tagName: String
) : Parcelable

/**
 * 视频分P信息
 */
@Parcelize
data class VideoPage(
    @SerializedName("cid")
    val cid: Long,
    
    @SerializedName("page")
    val page: Int,
    
    @SerializedName("part")
    val part: String,  // 分P标题
    
    @SerializedName("duration")
    val duration: Int  // 时长（秒）
) : Parcelable

/**
 * 字幕信息
 */
@Parcelize
data class SubtitleInfo(
    @SerializedName("id")
    val id: Long,
    
    @SerializedName("lan")
    val language: String,
    
    @SerializedName("lan_doc")
    val languageName: String,
    
    @SerializedName("subtitle_url")
    val subtitleUrl: String
) : Parcelable

/**
 * 视频播放地址响应
 */
data class PlayUrlResponse(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: PlayUrlData?
)

data class PlayUrlData(
    @SerializedName("from")
    val from: String,
    
    @SerializedName("result")
    val result: String,
    
    @SerializedName("quality")
    val quality: Int,
    
    @SerializedName("format")
    val format: String,
    
    @SerializedName("timelength")
    val timeLength: Long,
    
    @SerializedName("accept_format")
    val acceptFormat: String,
    
    @SerializedName("accept_description")
    val acceptDescription: List<String>,
    
    @SerializedName("accept_quality")
    val acceptQuality: List<Int>,
    
    @SerializedName("video_codecid")
    val videoCodecId: Int,
    
    @SerializedName("seek_param")
    val seekParam: String,
    
    @SerializedName("seek_type")
    val seekType: String,
    
    @SerializedName("durl")
    val videoUrls: List<VideoUrl>,
    
    @SerializedName("support_formats")
    val supportFormats: List<SupportFormat>?
)

data class VideoUrl(
    @SerializedName("order")
    val order: Int,
    
    @SerializedName("length")
    val length: Long,
    
    @SerializedName("size")
    val size: Long,
    
    @SerializedName("ahead")
    val ahead: String,
    
    @SerializedName("vhead")
    val vhead: String,
    
    @SerializedName("url")
    val url: String,
    
    @SerializedName("backup_url")
    val backupUrl: List<String>?
)

data class SupportFormat(
    @SerializedName("quality")
    val quality: Int,
    
    @SerializedName("format")
    val format: String,
    
    @SerializedName("new_description")
    val description: String,
    
    @SerializedName("display_desc")
    val displayDesc: String,
    
    @SerializedName("superscript")
    val superscript: String
)

/**
 * 视频质量定义
 */
object VideoQuality {
    const val QUALITY_240P = 6
    const val QUALITY_360P = 16
    const val QUALITY_480P = 32
    const val QUALITY_720P = 64
    const val QUALITY_720P60 = 74
    const val QUALITY_1080P = 80
    const val QUALITY_1080P_PLUS = 112
    const val QUALITY_1080P60 = 116
    const val QUALITY_4K = 120
    const val QUALITY_HDR = 125
    const val QUALITY_DOLBY = 126
    
    fun getQualityDescription(quality: Int): String {
        return when (quality) {
            QUALITY_240P -> "240P"
            QUALITY_360P -> "360P"
            QUALITY_480P -> "480P"
            QUALITY_720P -> "720P"
            QUALITY_720P60 -> "720P60"
            QUALITY_1080P -> "1080P"
            QUALITY_1080P_PLUS -> "1080P+"
            QUALITY_1080P60 -> "1080P60"
            QUALITY_4K -> "4K"
            QUALITY_HDR -> "HDR"
            QUALITY_DOLBY -> "杜比视界"
            else -> "自动"
        }
    }
    
    fun getSupportedQualities(): List<Int> {
        return listOf(
            QUALITY_480P,
            QUALITY_720P,
            QUALITY_1080P,
            QUALITY_1080P_PLUS
        )
    }
}