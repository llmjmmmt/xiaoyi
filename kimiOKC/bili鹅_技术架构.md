# bili鹅 技术架构设计

## 1. 技术栈选型

### 1.1 开发语言与框架
- **Kotlin**: 现代、简洁、安全的编程语言
- **Jetpack Compose**: Google推荐的现代UI工具包，适合TV应用
- **MVVM架构**: Model-View-ViewModel，分离关注点，便于测试和维护

### 1.2 核心库依赖

#### 视频播放
- **ExoPlayer**: Google开源的视频播放器，支持多种格式，性能优秀
  ```kotlin
  implementation("androidx.media3:media3-exoplayer:1.2.0")
  implementation("androidx.media3:media3-ui:1.2.0")
  ```

#### 网络通信
- **Retrofit**: 类型安全的HTTP客户端
  ```kotlin
  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  implementation("com.squareup.retrofit2:converter-gson:2.9.0")
  ```

#### 图片加载
- **Coil**: Kotlin优先的图片加载库
  ```kotlin
  implementation("io.coil-kt:coil-compose:2.5.0")
  ```

#### 数据存储
- **Room**: SQLite抽象层，用于本地数据库
- **DataStore**: 现代数据存储方案，替代SharedPreferences
  ```kotlin
  implementation("androidx.room:room-runtime:2.6.0")
  implementation("androidx.datastore:datastore-preferences:1.1.0")
  ```

#### 依赖注入
- **Hilt**: 简化依赖注入
  ```kotlin
  implementation("com.google.dagger:hilt-android:2.48")
  implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
  ```

### 1.3 API接口设计

#### B站API集成
由于B站官方API的限制，本项目采用以下方案：
- 使用B站公开接口获取视频信息
- 通过第三方API或逆向工程获取播放地址
- 实现登录态管理和用户认证

```kotlin
interface BiliApiService {
    // 获取推荐视频
    @GET("x/web-interface/index/top/rcmd")
    suspend fun getRecommendedVideos(
        @Query("ps") pageSize: Int = 20
    ): Response<VideoListResponse>
    
    // 获取视频详情
    @GET("x/web-interface/view")
    suspend fun getVideoDetail(
        @Query("bvid") bvid: String
    ): Response<VideoDetailResponse>
    
    // 获取视频播放地址
    @GET("x/player/playurl")
    suspend fun getVideoPlayUrl(
        @Query("bvid") bvid: String,
        @Query("cid") cid: Long,
        @Query("qn") quality: Int = 112 // 1080P+
    ): Response<PlayUrlResponse>
}
```

## 2. 项目结构设计

### 2.1 包结构
```
com.biligo/
├── data/                    # 数据层
│   ├── model/              # 数据模型
│   │   ├── User.kt
│   │   ├── Video.kt
│   │   └── Danmaku.kt
│   ├── repository/         # 数据仓库
│   │   ├── UserRepository.kt
│   │   └── VideoRepository.kt
│   ├── local/              # 本地数据源
│   │   ├── database/
│   │   └── datastore/
│   └── remote/             # 远程数据源
│       └── api/
├── domain/                  # 业务逻辑层
│   ├── usecase/            # 用例
│   └── model/              # 领域模型
├── ui/                      # 表现层
│   ├── screen/             # 屏幕界面
│   │   ├── login/
│   │   ├── home/
│   │   ├── video/
│   │   └── profile/
│   ├── component/          # 可复用组件
│   ├── theme/              # 主题样式
│   └── navigation/         # 导航
└── utils/                   # 工具类
    ├── Constants.kt
    └── Extensions.kt
```

### 2.2 核心类设计

#### 数据模型
```kotlin
// 视频数据模型
@Parcelize
data class Video(
    val bvid: String,
    val aid: Long,
    val title: String,
    val pic: String,  // 封面URL
    val author: String,
    val mid: Long,    // UP主ID
    val play: Long,   // 播放量
    val danmaku: Long, // 弹幕数
    val duration: String,
    val cid: Long     // 用于获取播放地址
) : Parcelable

// 用户信息
@Parcelize
data class User(
    val mid: Long,
    val uname: String,
    val face: String, // 头像URL
    val level: Int,
    val vipStatus: Int
) : Parcelable
```

#### 仓库模式
```kotlin
class VideoRepository @Inject constructor(
    private val apiService: BiliApiService,
    private val videoDao: VideoDao
) {
    // 获取推荐视频
    suspend fun getRecommendedVideos(): Result<List<Video>> {
        return try {
            val response = apiService.getRecommendedVideos()
            if (response.isSuccessful) {
                Result.success(response.body()?.data ?: emptyList())
            } else {
                Result.failure(Exception("Failed to load videos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // 获取视频播放地址
    suspend fun getVideoPlayUrl(video: Video): Result<String> {
        return try {
            val response = apiService.getVideoPlayUrl(video.bvid, video.cid)
            if (response.isSuccessful) {
                val playUrl = response.body()?.data?.durl?.firstOrNull()?.url
                playUrl?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("No play URL found"))
            } else {
                Result.failure(Exception("Failed to get play URL"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### 2.3 ViewModel设计
```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val videoRepository: VideoRepository
) : ViewModel() {
    
    private val _videos = mutableStateListOf<Video>()
    val videos: List<Video> = _videos
    
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading
    
    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error
    
    init {
        loadRecommendedVideos()
    }
    
    fun loadRecommendedVideos() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            when (val result = videoRepository.getRecommendedVideos()) {
                is Result.Success -> {
                    _videos.clear()
                    _videos.addAll(result.data)
                }
                is Result.Failure -> {
                    _error.value = result.exception.message
                }
            }
            
            _isLoading.value = false
        }
    }
}
```

## 3. 视频播放架构

### 3.1 ExoPlayer集成
```kotlin
class VideoPlayerManager @Inject constructor(
    private val context: Context
) {
    private var exoPlayer: ExoPlayer? = null
    
    fun initializePlayer(): ExoPlayer {
        val player = ExoPlayer.Builder(context)
            .setSeekForwardIncrementMs(10000) // 10秒快进
            .setSeekBackIncrementMs(10000)    // 10秒快退
            .build()
            
        // 配置播放器
        player.trackSelectionParameters = player.trackSelectionParameters
            .buildUpon()
            .setMaxVideoSize(1920, 1080) // 最高1080P
            .build()
            
        exoPlayer = player
        return player
    }
    
    fun playVideo(videoUrl: String) {
        exoPlayer?.let { player ->
            val mediaItem = MediaItem.fromUri(videoUrl)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.play()
        }
    }
    
    fun releasePlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }
}
```

### 3.2 弹幕系统
```kotlin
class DanmakuManager {
    
    fun loadDanmaku(cid: Long): List<DanmakuItem> {
        // 从B站API获取弹幕数据
        // 解析XML格式的弹幕数据
        // 转换为DanmakuItem列表
    }
    
    @Composable
    fun DanmakuOverlay(
        danmakuList: List<DanmakuItem>,
        playerState: PlayerState
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 绘制弹幕
        }
    }
}
```

## 4. 电视端适配

### 4.1 遥控器导航
```kotlin
@Composable
fun TvFocusableCard(
    video: Video,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .clickable { onClick() }
            .scale(if (isFocused) 1.05f else 1f),
        elevation = if (isFocused) 8.dp else 4.dp
    ) {
        // 卡片内容
    }
}
```

### 4.2  leanback支持
```kotlin
// 继承leanback支持库
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 启用TV模式
        setContent {
            BiliGoTheme {
                // TV优化的UI
            }
        }
    }
}
```

## 5. 数据持久化

### 5.1 用户数据存储
```kotlin
class UserDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val USER_TOKEN = stringPreferencesKey("user_token")
    private val USER_INFO = stringPreferencesKey("user_info")
    
    val userToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_TOKEN]
    }
    
    suspend fun saveUserToken(token: String) {
        dataStore.edit { preferences ->
            preferences[USER_TOKEN] = token
        }
    }
    
    suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences.remove(USER_TOKEN)
            preferences.remove(USER_INFO)
        }
    }
}
```

### 5.2 观看历史
```kotlin
@Entity(tableName = "watch_history")
data class WatchHistory(
    @PrimaryKey val bvid: String,
    val title: String,
    val watchedAt: Long,
    val progress: Long, // 观看进度（毫秒）
    val duration: Long  // 视频总时长（毫秒）
)

@Dao
interface WatchHistoryDao {
    @Query("SELECT * FROM watch_history ORDER BY watchedAt DESC")
    fun getAllHistory(): Flow<List<WatchHistory>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: WatchHistory)
    
    @Query("DELETE FROM watch_history WHERE bvid = :bvid")
    suspend fun deleteHistory(bvid: String)
}
```

## 6. 错误处理与日志

### 6.1 全局异常处理
```kotlin
class BiliGoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // 设置全局异常处理器
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("BiliGo", "Uncaught exception in thread ${thread.name}", throwable)
            // 可以在这里添加崩溃上报逻辑
        }
    }
}
```

### 6.2 网络错误处理
```kotlin
sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure<T>(val exception: Exception) : Result<T>()
    class Loading<T> : Result<T>()
}

// 网络请求包装
suspend fun <T> safeApiCall(
    apiCall: suspend () -> Response<T>
): Result<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                Result.Success(it)
            } ?: Result.Failure(Exception("Empty response body"))
        } else {
            Result.Failure(Exception("API error: ${response.code()}"))
        }
    } catch (e: Exception) {
        Result.Failure(e)
    }
}
```

## 7. 性能优化

### 7.1 图片加载优化
```kotlin
// Coil配置
val imageLoader = ImageLoader.Builder(context)
    .memoryCache {
        MemoryCache.Builder()
            .maxSizePercent(0.25)
            .build()
    }
    .diskCache {
        DiskCache.Builder()
            .directory(context.cacheDir.resolve("image_cache"))
            .maxSizeBytes(512 * 1024 * 1024) // 512MB
            .build()
    }
    .build()
```

### 7.2 视频预加载
```kotlin
class VideoPreloadManager {
    private val preloadQueue = mutableListOf<String>()
    
    fun addToPreload(videoUrl: String) {
        if (preloadQueue.size < 3) { // 最多预加载3个
            preloadQueue.add(videoUrl)
            // 开始预加载
        }
    }
}
```

这个技术架构设计为bili鹅应用提供了完整的技术实现方案，涵盖了从底层数据到上层UI的所有层面，确保应用的稳定性、可维护性和良好的用户体验。