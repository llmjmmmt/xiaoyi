package com.biligo.data.remote.api

import com.biligo.data.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * B站API服务接口
 */
interface BiliApiService {
    
    /**
     * 获取推荐视频列表
     */
    @GET("x/web-interface/index/top/rcmd")
    suspend fun getRecommendedVideos(
        @Query("ps") pageSize: Int = 20,
        @Query("fresh_idx") freshIndex: Int = 1,
        @Query("fresh_type") freshType: Int = 3
    ): Response<VideoListResponse>
    
    /**
     * 获取视频详情
     */
    @GET("x/web-interface/view")
    suspend fun getVideoDetail(
        @Query("bvid") bvid: String
    ): Response<VideoDetailResponse>
    
    /**
     * 获取视频播放地址
     */
    @GET("x/player/playurl")
    suspend fun getVideoPlayUrl(
        @Query("bvid") bvid: String,
        @Query("cid") cid: Long,
        @Query("qn") quality: Int = 112,  // 1080P+
        @Query("fnval") fnval: Int = 16,
        @Query("fourk") fourk: Int = 1
    ): Response<PlayUrlResponse>
    
    /**
     * 搜索视频
     */
    @GET("x/web-interface/search/type")
    suspend fun searchVideos(
        @Query("keyword") keyword: String,
        @Query("search_type") searchType: String = "video",
        @Query("page") page: Int = 1,
        @Query("pagesize") pageSize: Int = 20
    ): Response<SearchResponse>
    
    /**
     * 获取热门搜索词
     */
    @GET("x/web-interface/search/square")
    suspend fun getHotSearchKeywords(): Response<HotSearchResponse>
    
    /**
     * 获取用户个人信息
     */
    @GET("x/web-interface/nav")
    suspend fun getUserInfo(): Response<UserInfoResponse>
    
    /**
     * 获取用户投稿视频
     */
    @GET("x/space/wbi/arc/search")
    suspend fun getUserVideos(
        @Query("mid") userId: Long,
        @Query("ps") pageSize: Int = 20,
        @Query("tid") tid: Int = 0,
        @Query("keyword") keyword: String = "",
        @Query("order") order: String = "pubdate"
    ): Response<UserVideoResponse>
    
    /**
     * 获取用户收藏夹
     */
    @GET("x/v3/fav/folder/created/list-all")
    suspend fun getUserFavoriteFolders(
        @Query("up_mid") userId: Long
    ): Response<FavoriteFoldersResponse>
    
    /**
     * 获取收藏夹视频
     */
    @GET("x/v3/fav/resource/list")
    suspend fun getFavoriteVideos(
        @Query("media_id") mediaId: Long,
        @Query("pn") pageNumber: Int = 1,
        @Query("ps") pageSize: Int = 20
    ): Response<FavoriteVideosResponse>
    
    /**
     * 获取观看历史
     */
    @GET("x/v2/history")
    suspend fun getWatchHistory(
        @Query("pn") pageNumber: Int = 1,
        @Query("ps") pageSize: Int = 20
    ): Response<WatchHistoryResponse>
    
    /**
     * 获取弹幕
     */
    @GET("x/v1/dm/list.so")
    suspend fun getDanmakuList(
        @Query("oid") oid: Long  // 视频CID
    ): Response<DanmakuListResponse>
    
    /**
     * 发送弹幕
     */
    @POST("x/v2/dm/post")
    suspend fun sendDanmaku(
        @Body request: SendDanmakuRequest
    ): Response<DanmakuResponse>
    
    /**
     * 获取推荐UP主
     */
    @GET("x/web-interface/card/rank")
    suspend fun getRecommendedUsers(): Response<RecommendedUsersResponse>
    
    /**
     * 获取分区视频
     */
    @GET("x/web-interface/dynamic/region")
    suspend fun getRegionVideos(
        @Query("ps") pageSize: Int = 20,
        @Query("rid") regionId: Int = 1,
        @Query("pn") pageNumber: Int = 1
    ): Response<RegionVideosResponse>
    
    /**
     * 获取排行榜
     */
    @GET("x/web-interface/ranking/v2")
    suspend fun getRankingList(
        @Query("rid") rid: Int = 0,  // 0-全站 1-动画 3-音乐 4-游戏 5-娱乐 36-科技 119-鬼畜 129-舞蹈
        @Query("type") type: String = "all",  // all-全部 origin-原创 rookie-新人
        @Query("day") day: Int = 3  // 3-三日排行 7-周排行
    ): Response<RankingListResponse>
    
    /**
     * 获取视频评论
     */
    @GET("x/v2/reply/main")
    suspend fun getVideoComments(
        @Query("oid") oid: Long,  // 视频AID
        @Query("type") type: Int = 1,
        @Query("mode") mode: Int = 3,  // 3-热门 2-时间
        @Query("pn") pageNumber: Int = 1,
        @Query("ps") pageSize: Int = 20
    ): Response<CommentResponse>
    
    /**
     * 获取相关视频推荐
     */
    @GET("x/web-interface/archive/related")
    suspend fun getRelatedVideos(
        @Query("bvid") bvid: String
    ): Response<RelatedVideosResponse>
    
    /**
    * 获取用户关注列表
     */
    @GET("x/relation/followings")
    suspend fun getUserFollowings(
        @Query("vmid") userId: Long,
        @Query("pn") pageNumber: Int = 1,
        @Query("ps") pageSize: Int = 20
    ): Response<FollowingsResponse>
    
    /**
     * 获取用户粉丝列表
     */
    @GET("x/relation/followers")
    suspend fun getUserFollowers(
        @Query("vmid") userId: Long,
        @Query("pn") pageNumber: Int = 1,
        @Query("ps") pageSize: Int = 20
    ): Response<FollowersResponse>
    
    /**
     * 获取未读消息数
     */
    @GET("x/msgfeed/unread")
    suspend fun getUnreadMessageCount(): Response<UnreadMessageResponse>
    
    /**
     * 获取系统通知
     */
    @GET("x/msgfeed/at")
    suspend fun getAtMessages(
        @Query("page_size") pageSize: Int = 20,
        @Query("offset") offset: String = ""
    ): Response<AtMessageResponse>
    
    /**
     * 获取直播列表
     */
    @GET("xlive/app-interface/v2/index/feed")
    suspend fun getLiveList(
        @Query("page") page: Int = 1,
        @Query("pagesize") pageSize: Int = 20
    ): Response<LiveListResponse>
    
    /**
     * 获取直播播放地址
     */
    @GET("xlive/app-room/v2/index/getRoomPlayInfo")
    suspend fun getLivePlayUrl(
        @Query("room_id") roomId: Long,
        @Query("quality") quality: Int = 4,
        @Query("protocol") protocol: String = "0,1",
        @Query("format") format: String = "0,1,2",
        @Query("codec") codec: String = "0,1"
    ): Response<LivePlayUrlResponse>
}

// 搜索响应
@Data
class SearchResponse(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: SearchData?
)

data class SearchData(
    @SerializedName("seid")
    val searchId: String,
    
    @SerializedName("page")
    val page: Int,
    
    @SerializedName("pagesize")
    val pageSize: Int,
    
    @SerializedName("numResults")
    val totalResults: Int,
    
    @SerializedName("numPages")
    val totalPages: Int,
    
    @SerializedName("result")
    val results: List<SearchResult>
)

@Parcelize
data class SearchResult(
    @SerializedName("type")
    val type: String,  // video, media_bangumi, media_ft, live, article
    
    @SerializedName("bvid")
    val bvid: String? = null,
    
    @SerializedName("aid")
    val aid: Long? = null,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("author")
    val author: String? = null,
    
    @SerializedName("play")
    val playCount: Long? = null,
    
    @SerializedName("pic")
    val cover: String? = null,
    
    @SerializedName("duration")
    val duration: String? = null,
    
    @SerializedName("cid")
    val cid: Long? = null
) : Parcelable

// 热门搜索响应
data class HotSearchResponse(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: HotSearchData?
)

data class HotSearchData(
    @SerializedName("trending")
    val trending: HotSearchList?
)

data class HotSearchList(
    @SerializedName("list")
    val keywords: List<HotSearchKeyword>
)

data class HotSearchKeyword(
    @SerializedName("keyword")
    val keyword: String,
    
    @SerializedName("show_name")
    val showName: String,
    
    @SerializedName("icon")
    val icon: String,
    
    @SerializedName("uri")
    val uri: String
)

// 用户信息响应
data class UserInfoResponse(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: UserInfoData?
)

data class UserInfoData(
    @SerializedName("isLogin")
    val isLogin: Boolean,
    
    @SerializedName("email_verified")
    val emailVerified: Int,
    
    @SerializedName("face")
    val avatar: String,
    
    @SerializedName("level_info")
    val levelInfo: LevelInfo,
    
    @SerializedName("mid")
    val mid: Long,
    
    @SerializedName("mobile_verified")
    val mobileVerified: Int,
    
    @SerializedName("money")
    val money: Double,
    
    @SerializedName("moral")
    val moral: Int,
    
    @SerializedName("official")
    val official: OfficialInfo,
    
    @SerializedName("officialVerify")
    val officialVerify: OfficialVerify,
    
    @SerializedName("pendant")
    val pendant: Pendant,
    
    @SerializedName("scores")
    val scores: Int,
    
    @SerializedName("uname")
    val userName: String,
    
    @SerializedName("vipDueDate")
    val vipDueDate: Long,
    
    @SerializedName("vipStatus")
    val vipStatus: Int,
    
    @SerializedName("vipType")
    val vipType: Int,
    
    @SerializedName("wallet")
    val wallet: Wallet
)

data class LevelInfo(
    @SerializedName("current_level")
    val currentLevel: Int,
    
    @SerializedName("current_min")
    val currentMin: Int,
    
    @SerializedName("current_exp")
    val currentExp: Int,
    
    @SerializedName("next_exp")
    val nextExp: Int
)

data class OfficialInfo(
    @SerializedName("role")
    val role: Int,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("desc")
    val description: String,
    
    @SerializedName("type")
    val type: Int
)

data class OfficialVerify(
    @SerializedName("type")
    val type: Int,
    
    @SerializedName("desc")
    val description: String
)

data class Pendant(
    @SerializedName("pid")
    val pid: Int,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("image")
    val image: String,
    
    @SerializedName("expire")
    val expire: Long
)

data class Wallet(
    @SerializedName("mid")
    val mid: Long,
    
    @SerializedName("bcoin_balance")
    val bcoinBalance: Double,
    
    @SerializedName("coupon_balance")
    val couponBalance: Int,
    
    @SerializedName("coupon_due_time")
    val couponDueTime: Int
)

// 用户视频响应
data class UserVideoResponse(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: UserVideoData?
)

data class UserVideoData(
    @SerializedName("list")
    val videos: VideoListData,
    
    @SerializedName("page")
    val page: PageInfo
)

data class PageInfo(
    @SerializedName("pn")
    val pageNumber: Int,
    
    @SerializedName("ps")
    val pageSize: Int,
    
    @SerializedName("count")
    val count: Int
)

// 收藏夹响应
data class FavoriteFoldersResponse(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: FavoriteFoldersData?
)

data class FavoriteFoldersData(
    @SerializedName("count")
    val count: Int,
    
    @SerializedName("list")
    val folders: List<FavoriteFolder>
)

@Parcelize
data class FavoriteFolder(
    @SerializedName("id")
    val id: Long,
    
    @SerializedName("fid")
    val fid: Long,
    
    @SerializedName("mid")
    val mid: Long,
    
    @SerializedName("attr")
    val attr: Int,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("cover")
    val cover: String,
    
    @SerializedName("intro")
    val intro: String,
    
    @SerializedName("ctime")
    val createTime: Long,
    
    @SerializedName("mtime")
    val modifyTime: Long,
    
    @SerializedName("state")
    val state: Int,
    
    @SerializedName("fav_state")
    val favState: Int,
    
    @SerializedName("media_count")
    val mediaCount: Int
) : Parcelable

// 收藏夹视频响应
data class FavoriteVideosResponse(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: FavoriteVideosData?
)

data class FavoriteVideosData(
    @SerializedName("info")
    val info: FavoriteFolder,
    
    @SerializedName("medias")
    val medias: List<Video>,
    
    @SerializedName("has_more")
    val hasMore: Boolean
)

// 观看历史响应
data class WatchHistoryResponse(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: WatchHistoryData?
)

data class WatchHistoryData(
    @SerializedName("cursor")
    val cursor: Cursor,
    
    @SerializedName("tab")
    val tab: List<HistoryTab>,
    
    @SerializedName("list")
    val list: List<HistoryItem>
)

data class Cursor(
    @SerializedName("max")
    val max: Long,
    
    @SerializedName("view_at")
    val viewAt: Long,
    
    @SerializedName("business")
    val business: String,
    
    @SerializedName("ps")
    val ps: Int
)

data class HistoryTab(
    @SerializedName("type")
    val type: String,
    
    @SerializedName("name")
    val name: String
)

data class HistoryItem(
    @SerializedName("title")
    val title: String,
    
    @SerializedName("long_title")
    val longTitle: String,
    
    @SerializedName("cover")
    val cover: String,
    
    @SerializedName("covers")
    val covers: List<String>,
    
    @SerializedName("uri")
    val uri: String,
    
    @SerializedName("history")
    val history: HistoryDetail,
    
    @SerializedName("videos")
    val videos: Int,
    
    @SerializedName("author_name")
    val authorName: String,
    
    @SerializedName("author_face")
    val authorFace: String,
    
    @SerializedName("author_mid")
    val authorMid: Long,
    
    @SerializedName("view_at")
    val viewAt: Long,
    
    @SerializedName("progress")
    val progress: Long,
    
    @SerializedName("badge")
    val badge: String,
    
    @SerializedName("show_title")
    val showTitle: String,
    
    @SerializedName("duration")
    val duration: Long,
    
    @SerializedName("current")
    val current: String,
    
    @SerializedName("total")
    val total: Int,
    
    @SerializedName("new_desc")
    val newDesc: String,
    
    @SerializedName("is_finish")
    val isFinish: Int,
    
    @SerializedName("is_fav")
    val isFav: Int,
    
    @SerializedName("kid")
    val kid: Long,
    
    @SerializedName("tag_name")
    val tagName: String,
    
    @SerializedName("live_status")
    val liveStatus: Int
)

data class HistoryDetail(
    @SerializedName("oid")
    val oid: Long,
    
    @SerializedName("epid")
    val epid: Long,
    
    @SerializedName("bvid")
    val bvid: String,
    
    @SerializedName("page")
    val page: Int,
    
    @SerializedName("cid")
    val cid: Long,
    
    @SerializedName("part")
    val part: String,
    
    @SerializedName("business")
    val business: String,
    
    @SerializedName("dt")
    val dt: Int
)

// 弹幕列表响应
data class DanmakuListResponse(
    val danmakuItems: List<DanmakuItem>
)

// 推荐UP主响应
data class RecommendedUsersResponse(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: List<User>?
)

// 分区视频响应
data class RegionVideosResponse(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: RegionVideosData?
)

data class RegionVideosData(
    @SerializedName("archives")
    val archives: List<Video>,
    
    @SerializedName("page")
    val page: PageInfo
)

// 排行榜响应
data class RankingListResponse(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: RankingListData?
)

data class RankingListData(
    @SerializedName("list")
    val list: List<RankingVideo>,
    
    @SerializedName("note")
    val note: String,
    
    @SerializedName("no_more")
    val noMore: Boolean
)

@Parcelize
data class RankingVideo(
    @SerializedName("aid")
    val aid: Long,
    
    @SerializedName("bvid")
    val bvid: String,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("author")
    val author: String,
    
    @SerializedName("mid")
    val mid: Long,
    
    @SerializedName("pic")
    val pic: String,
    
    @SerializedName("play")
    val play: Long,
    
    @SerializedName("video_review")
    val videoReview: Long,
    
    @SerializedName("duration")
    val duration: String,
    
    @SerializedName("review")
    val review: Long,
    
    @SerializedName("favorites")
    val favorites: Long,
    
    @SerializedName("create")
    val create: String,
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("pts")
    val pts: Int
) : Parcelable

// 评论响应
data class CommentResponse(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: CommentData?
)

data class CommentData(
    @SerializedName("page")
    val page: PageInfo,
    
    @SerializedName("replies")
    val replies: List<Comment>,
    
    @SerializedName("top")
    val topReplies: List<Comment>?
)

@Parcelize
data class Comment(
    @SerializedName("rpid")
    val rpid: Long,
    
    @SerializedName("oid")
    val oid: Long,
    
    @SerializedName("type")
    val type: Int,
    
    @SerializedName("mid")
    val mid: Long,
    
    @SerializedName("root")
    val root: Long,
    
    @SerializedName("parent")
    val parent: Long,
    
    @SerializedName("dialog")
    val dialog: Long,
    
    @SerializedName("count")
    val count: Int,
    
    @SerializedName("rcount")
    val rcount: Int,
    
    @SerializedName("floor")
    val floor: Int,
    
    @SerializedName("state")
    val state: Int,
    
    @SerializedName("fansgrade")
    val fansgrade: Int,
    
    @SerializedName("attr")
    val attr: Int,
    
    @SerializedName("like")
    val like: Long,
    
    @SerializedName("action")
    val action: Long,
    
    @SerializedName("member")
    val member: CommentMember,
    
    @SerializedName("content")
    val content: CommentContent,
    
    @SerializedName("replies")
    val replies: List<Comment>?,
    
    @SerializedName("assist")
    val assist: Int,
    
    @SerializedName("folder")
    val folder: CommentFolder,
    
    @SerializedName("up_action")
    val upAction: CommentUpAction,
    
    @SerializedName("show_follow")
    val showFollow: Boolean
) : Parcelable

@Parcelize
data class CommentMember(
    @SerializedName("mid")
    val mid: String,
    
    @SerializedName("uname")
    val uname: String,
    
    @SerializedName("sex")
    val sex: String,
    
    @SerializedName("sign")
    val sign: String,
    
    @SerializedName("avatar")
    val avatar: String,
    
    @SerializedName("rank")
    val rank: String,
    
    @SerializedName("DisplayRank")
    val displayRank: String,
    
    @SerializedName("face_nft_new")
    val faceNftNew: Int,
    
    @SerializedName("is_senior_member")
    val isSeniorMember: Int,
    
    @SerializedName("level_info")
    val levelInfo: LevelInfo,
    
    @SerializedName("pendant")
    val pendant: Pendant,
    
    @SerializedName("nameplate")
    val nameplate: Nameplate,
    
    @SerializedName("official_verify")
    val officialVerify: OfficialVerify,
    
    @SerializedName("vip")
    val vip: Vip,
    
    @SerializedName("fans_detail")
    val fansDetail: String,
    
    @SerializedName("following")
    val following: Int,
    
    @SerializedName("is_followed")
    val isFollowed: Int,
    
    @SerializedName("user_sailing")
    val userSailing: String,
    
    @SerializedName("is_contractor")
    val isContractor: Boolean,
    
    @SerializedName("contract_desc")
    val contractDesc: String
) : Parcelable

@Parcelize
data class Nameplate(
    @SerializedName("nid")
    val nid: Long,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("image")
    val image: String,
    
    @SerializedName("image_small")
    val imageSmall: String,
    
    @SerializedName("level")
    val level: String,
    
    @SerializedName("condition")
    val condition: String
) : Parcelable

@Parcelize
data class Vip(
    @SerializedName("vipType")
    val vipType: Int,
    
    @SerializedName("vipDueDate")
    val vipDueDate: Long,
    
    @SerializedName("vipStatus")
    val vipStatus: Int,
    
    @SerializedName("themeType")
    val themeType: Int,
    
    @SerializedName("label")
    val label: VipLabel
) : Parcelable

@Parcelize
data class VipLabel(
    @SerializedName("path")
    val path: String,
    
    @SerializedName("text")
    val text: String,
    
    @SerializedName("label_theme")
    val labelTheme: String,
    
    @SerializedName("text_color")
    val textColor: String,
    
    @SerializedName("bg_style")
    val bgStyle: Int,
    
    @SerializedName("bg_color")
    val bgColor: String,
    
    @SerializedName("border_color")
    val borderColor: String
) : Parcelable

@Parcelize
data class CommentContent(
    @SerializedName("message")
    val message: String,
    
    @SerializedName("plat")
    val plat: Int,
    
    @SerializedName("device")
    val device: String,
    
    @SerializedName("members")
    val members: List<Any>,
    
    @SerializedName("emote")
    val emote: Map<String, Emote>,
    
    @SerializedName("jump_url")
    val jumpUrl: Map<String, JumpUrl>,
    
    @SerializedName("max_line")
    val maxLine: Int
) : Parcelable

@Parcelize
data class Emote(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("package_id")
    val packageId: Int,
    
    @SerializedName("state")
    val state: Int,
    
    @SerializedName("type")
    val type: Int,
    
    @SerializedName("attr")
    val attr: Int,
    
    @SerializedName("text")
    val text: String,
    
    @SerializedName("url")
    val url: String,
    
    @SerializedName("meta")
    val meta: EmoteMeta,
    
    @SerializedName("mtime")
    val mtime: Long
) : Parcelable

@Parcelize
data class EmoteMeta(
    @SerializedName("size")
    val size: Int,
    
    @SerializedName("alias")
    val alias: String
) : Parcelable

@Parcelize
data class JumpUrl(
    @SerializedName("title")
    val title: String,
    
    @SerializedName("state")
    val state: Int,
    
    @SerializedName("prefix_icon")
    val prefixIcon: String,
    
    @SerializedName("app_url_schema")
    val appUrlSchema: String,
    
    @SerializedName("app_name")
    val appName: String,
    
    @SerializedName("app_package_name")
    val appPackageName: String
) : Parcelable

@Parcelize
data class CommentFolder(
    @SerializedName("has_folded")
    val hasFolded: Boolean,
    
    @SerializedName("is_folded")
    val isFolded: Boolean,
    
    @SerializedName("rule")
    val rule: String
) : Parcelable

@Parcelize
data class CommentUpAction(
    @SerializedName("like")
    val like: Boolean,
    
    @SerializedName("reply")
    val reply: Boolean
) : Parcelable

// 相关视频响应
data class RelatedVideosResponse(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: List<Video>?
)

// 关注列表响应
data class FollowingsResponse(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: FollowingsData?
)

data class FollowingsData(
    @SerializedName("list")
    val list: List<FollowingUser>,
    
    @SerializedName("total")
    val total: Int
)

@Parcelize
data class FollowingUser(
    @SerializedName("mid")
    val mid: Long,
    
    @SerializedName("attribute")
    val attribute: Int,
    
    @SerializedName("mtime")
    val mtime: Long,
    
    @SerializedName("tag")
    val tag: List<Int>,
    
    @SerializedName("special")
    val special: Int,
    
    @SerializedName("uname")
    val uname: String,
    
    @SerializedName("face")
    val face: String,
    
    @SerializedName("sign")
    val sign: String,
    
    @SerializedName("official_verify")
    val officialVerify: OfficialVerify,
    
    @SerializedName("vip")
    val vip: Vip,
    
    @SerializedName("nft_interaction")
    val nftInteraction: String,
    
    @SerializedName("level_info")
    val levelInfo: LevelInfo
) : Parcelable

// 粉丝列表响应
data class FollowersResponse(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: FollowersData?
)

data class FollowersData(
    @SerializedName("list")
    val list: List<FollowerUser>,
    
    @SerializedName("total")
    val total: Int
)

@Parcelize
data class FollowerUser(
    @SerializedName("mid")
    val mid: Long,
    
    @SerializedName("attribute")
    val attribute: Int,
    
    @SerializedName("mtime")
    val mtime: Long,
    
    @SerializedName("tag")
    val tag: List<Int>,
    
    @SerializedName("special")
    val special: Int,
    
    @SerializedName("uname")
    val uname: String,
    
    @SerializedName("face")
    val face: String,
    
    @SerializedName("sign")
    val sign: String,
    
    @SerializedName("official_verify")
    val officialVerify: OfficialVerify,
    
    @SerializedName("vip")
    val vip: Vip,
    
    @SerializedName("level_info")
    val levelInfo: LevelInfo
) : Parcelable

// 未读消息响应
data class UnreadMessageResponse(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: UnreadMessageData?
)

data class UnreadMessageData(
    @SerializedName("at")
    val at: Int,
    
    @SerializedName("chat")
    val chat: Int,
    
    @SerializedName("like")
    val like: Int,
    
    @SerializedName("reply")
    val reply: Int,
    
    @SerializedName("sys_msg")
    val sysMsg: Int,
    
    @SerializedName("up")
    val up: Int
)

// @消息响应
data class AtMessageResponse(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: AtMessageData?
)

data class AtMessageData(
    @SerializedName("items")
    val items: List<AtMessageItem>,
    
    @SerializedName("offset")
    val offset: String,
    
    @SerializedName("update_num")
    val updateNum: Int
)

data class AtMessageItem(
    @SerializedName("id")
    val id: Long,
    
    @SerializedName("user")
    val user: AtMessageUser,
    
    @SerializedName("item")
    val item: AtMessageContent,
    
    @SerializedName("at_time")
    val atTime: Long
)

data class AtMessageUser(
    @SerializedName("mid")
    val mid: Long,
    
    @SerializedName("uname")
    val uname: String,
    
    @SerializedName("face")
    val face: String
)

data class AtMessageContent(
    @SerializedName("business")
    val business: String,
    
    @SerializedName("id")
    val id: Long,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("image")
    val image: String,
    
    @SerializedName("uri")
    val uri: String,
    
    @SerializedName("source_content")
    val sourceContent: String
)

// 直播列表响应
data class LiveListResponse(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: LiveListData?
)

data class LiveListData(
    @SerializedName("list")
    val list: List<LiveRoom>,
    
    @SerializedName("has_more")
    val hasMore: Boolean
)

@Parcelize
data class LiveRoom(
    @SerializedName("roomid")
    val roomId: Long,
    
    @SerializedName("uid")
    val uid: Long,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("uname")
    val uname: String,
    
    @SerializedName("face")
    val face: String,
    
    @SerializedName("cover")
    val cover: String,
    
    @SerializedName("live_status")
    val liveStatus: Int,  // 0-未开播 1-直播中 2-轮播中
    
    @SerializedName("area_v2_id")
    val areaV2Id: Int,
    
    @SerializedName("area_v2_name")
    val areaV2Name: String,
    
    @SerializedName("area_v2_parent_id")
    val areaV2ParentId: Int,
    
    @SerializedName("area_v2_parent_name")
    val areaV2ParentName: String,
    
    @SerializedName("online")
    val online: Int,
    
    @SerializedName("short_id")
    val shortId: Long,
    
    @SerializedName("link")
    val link: String,
    
    @SerializedName("live_time")
    val liveTime: String,
    
    @SerializedName("watched_show")
    val watchedShow: WatchedShow
) : Parcelable

@Parcelize
data class WatchedShow(
    @SerializedName("switch")
    val switch: Boolean,
    
    @SerializedName("num")
    val num: Int,
    
    @SerializedName("text_small")
    val textSmall: String,
    
    @SerializedName("text_large")
    val textLarge: String,
    
    @SerializedName("icon")
    val icon: String,
    
    @SerializedName("icon_location")
    val iconLocation: String,
    
    @SerializedName("icon_web")
    val iconWeb: String
) : Parcelable

// 直播播放地址响应
data class LivePlayUrlResponse(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: LivePlayUrlData?
)

data class LivePlayUrlData(
    @SerializedName("playurl_info")
    val playurlInfo: PlayurlInfo
)

data class PlayurlInfo(
    @SerializedName("conf_json")
    val confJson: String,
    
    @SerializedName("playurl")
    val playurl: Playurl
)

data class Playurl(
    @SerializedName("cid")
    val cid: Long,
    
    @SerializedName("g_qn_desc")
    val gQnDesc: List<QualityDescription>,
    
    @SerializedName("stream")
    val stream: List<Stream>
)

data class QualityDescription(
    @SerializedName("qn")
    val qn: Int,
    
    @SerializedName("desc")
    val desc: String
)

data class Stream(
    @SerializedName("protocol_name")
    val protocolName: String,
    
    @SerializedName("format")
    val format: List<Format>
)

data class Format(
    @SerializedName("format_name")
    val formatName: String,
    
    @SerializedName("codec")
    val codec: List<Codec>
)

data class Codec(
    @SerializedName("codec_name")
    val codecName: String,
    
    @SerializedName("current_qn")
    val currentQn: Int,
    
    @SerializedName("accept_qn")
    val acceptQn: List<Int>,
    
    @SerializedName("base_url")
    val baseUrl: String,
    
    @SerializedName("url_info")
    val urlInfo: List<UrlInfo>
)

data class UrlInfo(
    @SerializedName("host")
    val host: String,
    
    @SerializedName("extra")
    val extra: String
)