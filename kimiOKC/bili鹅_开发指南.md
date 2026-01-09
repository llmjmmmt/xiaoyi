# bili鹅 - 开发指南

## 目录

1. [开发环境搭建](#开发环境搭建)
2. [项目架构](#项目架构)
3. [核心模块开发](#核心模块开发)
4. [TV端适配](#tv端适配)
5. [性能优化](#性能优化)
6. [测试与调试](#测试与调试)
7. [发布与部署](#发布与部署)

## 开发环境搭建

### 1. 安装Android Studio

下载并安装最新版本的Android Studio：
- 官网：https://developer.android.com/studio
- 推荐版本：Android Studio Arctic Fox (2020.3.1) 或更高版本

### 2. 配置Android SDK

确保安装了以下SDK组件：
- Android SDK Platform 21 (Android 5.0)
- Android SDK Platform 28 (Android 9.0)
- Android SDK Platform 33 (Android 13.0)
- Android SDK Build-Tools
- Android SDK Platform-Tools
- Android Emulator

### 3. 安装Kotlin插件

Android Studio已内置Kotlin支持，确保Kotlin插件已启用：
- File → Settings → Plugins → 搜索"Kotlin"
- 确保Kotlin插件已安装并启用

### 4. 配置环境变量

在系统环境变量中添加：
```bash
# Linux/macOS
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/emulator
export PATH=$PATH:$ANDROID_HOME/tools
export PATH=$PATH:$ANDROID_HOME/tools/bin
export PATH=$PATH:$ANDROID_HOME/platform-tools

# Windows
set ANDROID_HOME=C:\Users\%USERNAME%\AppData\Local\Android\Sdk
set PATH=%PATH%;%ANDROID_HOME%\emulator
set PATH=%PATH%;%ANDROID_HOME%\tools
set PATH=%PATH%;%ANDROID_HOME%\tools\bin
set PATH=%PATH%;%ANDROID_HOME%\platform-tools
```

## 项目架构

### MVVM架构

本项目采用MVVM（Model-View-ViewModel）架构：

```
┌─────────────────────────────────────────────────────────────┐
│                            View                             │
│                    (Composable UI)                          │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                        ViewModel                            │
│                    (UI State & Logic)                       │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      Repository                             │
│                 (Data Source Coordinator)                   │
└─────────────────────────────────────────────────────────────┘
                              │
              ┌───────────────┴───────────────┐
              ▼                               ▼
┌──────────────────────────┐  ┌──────────────────────────┐
│      Remote Data         │  │      Local Data          │
│    (API & Network)       │  │  (Database & Cache)      │
└──────────────────────────┘  └──────────────────────────┘
```

### 包结构

```
com.biligo/
├── data/                      # 数据层
│   ├── model/                # 数据模型
│   │   ├── User.kt
│   │   ├── Video.kt
│   │   └── Danmaku.kt
│   ├── repository/           # 数据仓库
│   │   ├── UserRepository.kt
│   │   └── VideoRepository.kt
│   ├── local/                # 本地数据源
│   │   ├── database/
│   │   ├── datastore/
│   │   └── cache/
│   └── remote/               # 远程数据源
│       └── api/
├── domain/                    # 业务逻辑层
│   ├── usecase/              # 用例
│   └── model/                # 领域模型
├── ui/                        # 表现层
│   ├── screen/               # 屏幕界面
│   │   ├── login/
│   │   ├── home/
│   │   ├── video/
│   │   └── profile/
│   ├── component/            # 可复用组件
│   ├── theme/                # 主题样式
│   └── navigation/           # 导航
├── utils/                     # 工具类
│   ├── Constants.kt
│   ├── Extensions.kt
│   └── NetworkUtils.kt
└── di/                        # 依赖注入
    └── AppModule.kt
```

## 核心模块开发

### 1. 视频播放模块

#### ExoPlayer集成

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
            .setMaxVideoSize(1920, 1080)
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

#### 视频播放界面

```kotlin
@Composable
fun VideoPlayerScreen(
    videoUrl: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val playerView = remember { PlayerView(context) }
    val player = remember { ExoPlayer.Builder(context).build() }
    
    DisposableEffect(Unit) {
        val mediaItem = MediaItem.fromUri(videoUrl)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
        
        onDispose {
            player.release()
        }
    }
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { playerView },
            modifier = Modifier.fillMaxSize()
        ) {
            it.player = player
            it.useController = true
        }
    }
}
```

### 2. 用户认证模块

#### 登录ViewModel

```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _loginState = mutableStateOf<LoginState>(LoginState.Idle)
    val loginState: State<LoginState> = _loginState
    
    fun loginWithQRCode() {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            
            try {
                // 生成二维码
                val qrCodeData = userRepository.generateQRCode()
                _loginState.value = LoginState.QRCodeGenerated(qrCodeData)
                
                // 轮询检查扫码状态
                pollLoginStatus(qrCodeData.qrCodeKey)
                
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "登录失败")
            }
        }
    }
    
    private fun pollLoginStatus(qrCodeKey: String) {
        viewModelScope.launch {
            var retryCount = 0
            val maxRetries = 60 // 5分钟超时
            
            while (retryCount < maxRetries) {
                delay(5000) // 5秒检查一次
                
                try {
                    val status = userRepository.checkQRCodeStatus(qrCodeKey)
                    
                    when (status.code) {
                        86090 -> {
                            // 已扫码，待确认
                            _loginState.value = LoginState.QRCodeScanned
                        }
                        0 -> {
                            // 登录成功
                            _loginState.value = LoginState.Success(status.userInfo)
                            return@launch
                        }
                        else -> {
                            // 其他状态
                        }
                    }
                    
                    retryCount++
                } catch (e: Exception) {
                    Timber.e(e, "检查扫码状态失败")
                    retryCount++
                }
            }
            
            _loginState.value = LoginState.Error("二维码已过期")
        }
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class QRCodeGenerated(val qrCodeData: QRCodeData) : LoginState()
    object QRCodeScanned : LoginState()
    data class Success(val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
}
```

### 3. 视频列表模块

#### 视频ViewModel

```kotlin
@HiltViewModel
class VideoViewModel @Inject constructor(
    private val videoRepository: VideoRepository
) : ViewModel() {
    
    private val _videos = mutableStateListOf<Video>()
    val videos: List<Video> = _videos
    
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading
    
    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error
    
    private var currentPage = 1
    private var isLastPage = false
    
    init {
        loadVideos()
    }
    
    fun loadVideos() {
        if (_isLoading.value || isLastPage) return
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val result = videoRepository.getRecommendedVideos(currentPage)
                
                when (result) {
                    is Result.Success -> {
                        if (result.data.isEmpty()) {
                            isLastPage = true
                        } else {
                            _videos.addAll(result.data)
                            currentPage++
                        }
                    }
                    is Result.Failure -> {
                        _error.value = result.exception.message
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun refresh() {
        currentPage = 1
        isLastPage = false
        _videos.clear()
        loadVideos()
    }
    
    fun searchVideos(keyword: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val result = videoRepository.searchVideos(keyword)
                
                when (result) {
                    is Result.Success -> {
                        _videos.clear()
                        _videos.addAll(result.data)
                    }
                    is Result.Failure -> {
                        _error.value = result.exception.message
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
```

## TV端适配

### 1. 遥控器导航

#### 焦点管理

```kotlin
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvFocusableCard(
    video: Video,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .onFocusChanged { isFocused = it.hasFocus }
            .focusable()
            .clickable { onClick() }
            .border(
                width = if (isFocused) 3.dp else 0.dp,
                color = if (isFocused) Color(0xFFFB7299) else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .scale(if (isFocused) 1.05f else 1f),
        shape = RoundedCornerShape(12.dp),
        elevation = if (isFocused) CardDefaults.cardElevation(8.dp) else CardDefaults.cardElevation(4.dp)
    ) {
        // 卡片内容
    }
}
```

#### 方向键处理

```kotlin
@Composable
fun HandleDirectionKeys(
    onUp: () -> Unit = {},
    onDown: () -> Unit = {},
    onLeft: () -> Unit = {},
    onRight: () -> Unit = {},
    onEnter: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onPreviewKeyEvent { keyEvent ->
                when (keyEvent.key) {
                    Key.DirectionUp -> {
                        onUp()
                        true
                    }
                    Key.DirectionDown -> {
                        onDown()
                        true
                    }
                    Key.DirectionLeft -> {
                        onLeft()
                        true
                    }
                    Key.DirectionRight -> {
                        onRight()
                        true
                    }
                    Key.Enter, Key.NumPadEnter -> {
                        onEnter()
                        true
                    }
                    else -> false
                }
            }
    )
}
```

### 2. Leanback支持

#### 集成Leanback库

```kotlin
dependencies {
    implementation("androidx.leanback:leanback:1.2.0-alpha02")
    implementation("androidx.tv:tv-foundation:1.0.0-alpha10")
    implementation("androidx.tv:tv-material:1.0.0-alpha10")
}
```

#### 创建Leanback活动

```kotlin
class LeanbackMainActivity : FragmentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            BiliGoTheme {
                LeanbackMainScreen()
            }
        }
    }
}

@Composable
fun LeanbackMainScreen() {
    val rows = remember { generateSampleRows() }
    
    TvLazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(rows) { row ->
            VideoRow(
                title = row.title,
                videos = row.videos,
                onVideoClick = { video ->
                    // 处理视频点击
                }
            )
        }
    }
}

@Composable
fun VideoRow(
    title: String,
    videos: List<Video>,
    onVideoClick: (Video) -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        
        TvLazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(videos) { video ->
                TvFocusableCard(
                    video = video,
                    onClick = { onVideoClick(video) }
                )
            }
        }
    }
}
```

### 3. 大屏UI适配

#### 响应式布局

```kotlin
@Composable
fun ResponsiveVideoGrid(videos: List<Video>) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    
    // 根据屏幕宽度决定列数
    val columns = when {
        screenWidth >= 1920 -> 5 // 4K电视
        screenWidth >= 1280 -> 4 // 1080P电视
        screenWidth >= 960 -> 3  // 小屏电视
        else -> 2
    }
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(videos) { video ->
            VideoCard(
                video = video,
                onClick = { /* 处理点击 */ }
            )
        }
    }
}
```

## 性能优化

### 1. 图片加载优化

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

### 2. 视频预加载

```kotlin
class VideoPreloadManager {
    private val preloadQueue = mutableListOf<String>()
    private val maxPreloadCount = 3
    
    fun addToPreload(videoUrl: String) {
        if (preloadQueue.size < maxPreloadCount) {
            preloadQueue.add(videoUrl)
            startPreload(videoUrl)
        }
    }
    
    private fun startPreload(videoUrl: String) {
        // 实现预加载逻辑
    }
}
```

### 3. 内存优化

```kotlin
// 使用rememberSaveable保存状态
@Composable
fun VideoListScreen() {
    val videos = rememberSaveable { mutableStateListOf<Video>() }
    
    // 使用LazyColumn优化长列表
    LazyColumn {
        items(videos, key = { it.bvid }) { video ->
            VideoItem(video = video)
        }
    }
}

// 使用derivedStateOf优化计算
@Composable
fun VideoStats(videos: List<Video>) {
    val totalViews by remember(videos) {
        derivedStateOf { videos.sumOf { it.playCount } }
    }
    
    Text("总播放量: ${totalViews}")
}
```

## 测试与调试

### 1. 单元测试

```kotlin
class VideoViewModelTest {
    
    @Mock
    private lateinit var videoRepository: VideoRepository
    
    private lateinit var viewModel: VideoViewModel
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = VideoViewModel(videoRepository)
    }
    
    @Test
    fun `loadVideos should update videos list when successful`() = runTest {
        // Given
        val mockVideos = listOf(
            Video(bvid = "BV1", title = "Video 1", /* ... */),
            Video(bvid = "BV2", title = "Video 2", /* ... */)
        )
        
        whenever(videoRepository.getRecommendedVideos(1))
            .thenReturn(Result.Success(mockVideos))
        
        // When
        viewModel.loadVideos()
        
        // Then
        assertEquals(mockVideos, viewModel.videos)
        assertEquals(false, viewModel.isLoading.value)
        assertEquals(null, viewModel.error.value)
    }
}
```

### 2. UI测试

```kotlin
class VideoScreenTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun videoList_shouldDisplayVideos_whenLoaded() {
        // Given
        val mockVideos = listOf(
            Video(bvid = "BV1", title = "Video 1", /* ... */),
            Video(bvid = "BV2", title = "Video 2", /* ... */)
        )
        
        // When
        composeTestRule.setContent {
            BiliGoTheme {
                VideoList(videos = mockVideos, onVideoClick = {})
            }
        }
        
        // Then
        composeTestRule.onNodeWithText("Video 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Video 2").assertIsDisplayed()
    }
}
```

### 3. 调试工具

#### 使用ADB调试

```bash
# 查看日志
adb logcat -s BiliGo

# 查看设备信息
adb devices

# 安装APK
adb install app-debug.apk

# 卸载应用
adb uninstall com.biligo

# 抓取屏幕截图
adb exec-out screencap -p > screenshot.png

# 录制屏幕
adb shell screenrecord /sdcard/demo.mp4
```

#### 使用Stetho调试

```kotlin
class BiliGoApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }
}
```

## 发布与部署

### 1. 生成签名APK

#### 生成密钥库

```bash
keytool -genkey -v -keystore biligo-release.keystore -alias biligo -keyalg RSA -keysize 2048 -validity 10000
```

#### 配置签名信息

在 `app/build.gradle.kts` 中添加：

```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("biligo-release.keystore")
            storePassword = "your_store_password"
            keyAlias = "biligo"
            keyPassword = "your_key_password"
        }
    }
    
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
```

### 2. 构建Release版本

```bash
./gradlew assembleRelease
```

### 3. 发布到应用商店

#### 准备发布材料

- 应用图标（512x512 PNG）
- 截图（至少4张，16:9或9:16）
- 应用描述
- 隐私政策链接
- 版本说明

#### 发布到各大应用商店

1. **Google Play Store**
   - 注册开发者账号（$25一次性费用）
   - 上传APK和素材
   - 填写应用信息
   - 提交审核

2. **国内应用商店**
   - 华为应用市场
   - 小米应用商店
   - 应用宝
   - 360手机助手
   - 百度手机助手

### 4. 版本管理

#### 版本号规范

- 版本名称：`主版本号.次版本号.修订号`（如：1.2.3）
- 版本代码：整数，每次发布递增

#### 版本更新策略

```kotlin
object Version {
    const val VERSION_NAME = "1.0.0"
    const val VERSION_CODE = 1
    
    // 版本更新说明
    val RELEASE_NOTES = listOf(
        "初始版本发布",
        "支持用户登录",
        "视频播放功能",
        "TV端适配"
    )
}
```

## 最佳实践

### 1. 代码规范

- 使用Kotlin标准代码风格
- 添加适当的注释和文档
- 遵循单一职责原则
- 使用不可变数据

### 2. 性能优化

- 避免不必要的重组
- 使用remember和derivedStateOf
- 合理使用协程
- 优化图片加载

### 3. 安全规范

- 不要在代码中硬编码敏感信息
- 使用ProGuard混淆代码
- 验证所有用户输入
- 使用HTTPS进行网络通信

### 4. 版本控制

- 使用Git进行版本控制
- 遵循语义化版本规范
- 编写清晰的提交信息
- 使用分支进行功能开发

## 常见问题

### 开发问题

1. **编译失败**
   - 检查Gradle版本
   - 检查依赖版本兼容性
   - 清理缓存重新编译

2. **运行时崩溃**
   - 查看Logcat日志
   - 检查空指针异常
   - 验证数据格式

3. **UI显示异常**
   - 检查Compose版本
   - 验证主题配置
   - 检查布局约束

### 性能问题

1. **内存泄漏**
   - 使用LeakCanary检测
   - 及时释放资源
   - 避免长生命周期引用

2. **卡顿**
   - 使用Systrace分析
   - 优化复杂计算
   - 减少过度绘制

3. **网络慢**
   - 使用缓存策略
   - 压缩数据
   - 优化请求频率

## 相关资源

### 官方文档

- [Android Developer](https://developer.android.com/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Kotlin](https://kotlinlang.org/docs/)
- [ExoPlayer](https://exoplayer.dev/)

### 开源项目

- [官方示例](https://github.com/android)
- [Compose示例](https://github.com/android/compose-samples)
- [TV应用示例](https://github.com/android/tv-samples)

### 技术社区

- [Stack Overflow](https://stackoverflow.com/questions/tagged/android)
- [Reddit](https://www.reddit.com/r/androiddev/)
- [CSDN](https://www.csdn.net/)

## 更新日志

### 2026-01-06
- 📚 创建开发指南文档
- 🎨 添加UI开发最佳实践
- 🔧 添加性能优化技巧
- 🐛 添加常见问题解答

---

希望这份开发指南能帮助你更好地理解和开发bili鹅应用。如有问题，欢迎反馈。