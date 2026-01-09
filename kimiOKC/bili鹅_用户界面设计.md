# bili鹅 用户界面设计

## 设计理念

### 设计原则
1. **遥控器优先**: 所有交互都针对遥控器操作优化
2. **简洁直观**: 界面清晰，层次分明，减少认知负担
3. **内容为王**: 突出视频内容，减少UI噪音
4. **大屏适配**: 充分利用电视大屏优势，展示更多内容

### 视觉风格
- **色彩**: 采用B站标志性的粉色(#FB7299)作为主色调，搭配深蓝色(#29303A)背景
- **字体**: 使用思源黑体，确保电视屏幕上的可读性
- **间距**: 充足的间距确保遥控器导航的准确性
- **焦点**: 明显的焦点状态，帮助用户了解当前选中位置

## 2. 主要界面设计

### 2.1 启动页面
```kotlin
@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF29303A)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo动画
            Image(
                painter = painterResource(R.drawable.biligo_logo),
                contentDescription = "bili鹅 Logo",
                modifier = Modifier.size(200.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 应用名称
            Text(
                text = "bili鹅",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFB7299)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 版本信息
            Text(
                text = "专为电视优化的B站客户端",
                fontSize = 20.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // 加载指示器
            CircularProgressIndicator(
                color = Color(0xFFFB7299),
                modifier = Modifier.size(48.dp)
            )
        }
    }
}
```

### 2.2 登录界面
```kotlin
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onSkipLogin: () -> Unit
) {
    var selectedOption by remember { mutableStateOf(0) } // 0: 扫码登录, 1: 跳过
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF29303A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 标题
            Text(
                text = "登录B站账号",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 48.dp)
            )
            
            // 扫码登录卡片
            LoginOptionCard(
                title = "扫码登录",
                description = "使用手机B站APP扫描二维码",
                icon = Icons.Default.QrCode,
                isSelected = selectedOption == 0,
                onClick = { selectedOption = 0 },
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            // 跳过登录
            LoginOptionCard(
                title = "暂不登录",
                description = "以游客身份浏览（部分功能受限）",
                icon = Icons.Default.Visibility,
                isSelected = selectedOption == 1,
                onClick = { selectedOption = 1 }
            )
            
            Spacer(modifier = Modifier.height(64.dp))
            
            // 确认按钮
            Button(
                onClick = {
                    if (selectedOption == 0) {
                        onLoginSuccess()
                    } else {
                        onSkipLogin()
                    }
                },
                modifier = Modifier
                    .width(300.dp)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFFB7299),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = if (selectedOption == 0) "开始扫码" else "进入应用",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun LoginOptionCard(
    title: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) Color(0xFFFB7299) else Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() },
        backgroundColor = Color(0xFF1A1A1A),
        elevation = if (isSelected) 8.dp else 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 图标
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = if (isSelected) Color(0xFFFB7299) else Color.White
            )
            
            Spacer(modifier = Modifier.width(24.dp))
            
            // 文字内容
            Column {
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color(0xFFFB7299) else Color.White
                )
                Text(
                    text = description,
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
```

### 2.3 主界面布局
```kotlin
@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    
    Scaffold(
        backgroundColor = Color(0xFF29303A),
        topBar = {
            MainTopBar()
        },
        bottomBar = {
            MainBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { paddingValues ->
        when (selectedTab) {
            0 -> HomeScreen()
            1 -> DiscoverScreen()
            2 -> ProfileScreen()
        }
    }
}

@Composable
fun MainTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color(0xFF1A1A1A))
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Logo
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.biligo_logo_small),
                contentDescription = "Logo",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "bili鹅",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFB7299)
            )
        }
        
        // 搜索框
        SearchBar(
            onSearch = { query -> /* 处理搜索 */ },
            modifier = Modifier.width(400.dp)
        )
        
        // 用户信息
        UserInfoChip()
    }
}

@Composable
fun MainBottomNavigation(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color(0xFF1A1A1A)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationItem(
            icon = Icons.Default.Home,
            label = "首页",
            isSelected = selectedTab == 0,
            onClick = { onTabSelected(0) }
        )
        NavigationItem(
            icon = Icons.Default.Explore,
            label = "发现",
            isSelected = selectedTab == 1,
            onClick = { onTabSelected(1) }
        )
        NavigationItem(
            icon = Icons.Default.Person,
            label = "我的",
            isSelected = selectedTab == 2,
            onClick = { onTabSelected(2) }
        )
    }
}

@Composable
fun NavigationItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 32.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(28.dp),
            tint = if (isSelected) Color(0xFFFB7299) else Color.White.copy(alpha = 0.6f)
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = if (isSelected) Color(0xFFFB7299) else Color.White.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
```

### 2.4 视频网格布局
```kotlin
@Composable
fun VideoGridScreen(
    videos: List<Video>,
    onVideoClick: (Video) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(videos) { video ->
            VideoCard(
                video = video,
                onClick = { onVideoClick(video) },
                modifier = Modifier.focusRequester(focusRequester)
            )
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun VideoCard(
    video: Video,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .width(280.dp)
            .onFocusChanged { isFocused = it.hasFocus }
            .focusable()
            .clickable { onClick() }
            .border(
                width = if (isFocused) 3.dp else 0.dp,
                color = if (isFocused) Color(0xFFFB7299) else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .scale(if (isFocused) 1.05f else 1f),\n        shape = RoundedCornerShape(12.dp),
        elevation = if (isFocused) CardDefaults.cardElevation(8.dp) else CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            // 视频缩略图
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                AsyncImage(
                    model = video.pic,
                    contentDescription = video.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // 时长标签
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = video.duration,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
                
                // 播放量和弹幕数
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                    Text(
                        text = formatCount(video.play),
                        fontSize = 12.sp,
                        color = Color.White,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Icon(
                        imageVector = Icons.Default.Message,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                    Text(
                        text = formatCount(video.danmaku),
                        fontSize = 12.sp,
                        color = Color.White,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
            
            // 视频信息
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                // 标题
                Text(
                    text = video.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // UP主
                Text(
                    text = video.author,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

// 格式化数字
fun formatCount(count: Long): String {
    return when {
        count >= 10000 -> "${"%.1f".format(count / 10000.0)}万"
        count >= 1000 -> "${"%.1f".format(count / 1000.0)}千"
        else -> count.toString()
    }
}
```

### 2.5 视频播放界面
```kotlin
@Composable
fun VideoPlayerScreen(
    video: Video,
    playUrl: String,
    onBack: () -> Unit
) {
    var showControls by remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(true) }
    var currentTime by remember { mutableStateOf(0L) }
    var duration by remember { mutableStateOf(0L) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 视频播放器
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    useController = false // 使用自定义控制器
                    player = ExoPlayer.Builder(context).build()
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // 视频信息覆盖层
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            VideoPlayerOverlay(
                video = video,
                isPlaying = isPlaying,
                currentTime = currentTime,
                duration = duration,
                onPlayPause = { isPlaying = !isPlaying },
                onSeek = { /* 处理跳转 */ },
                onBack = onBack
            )
        }
        
        // 点击显示/隐藏控制栏
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { showControls = !showControls }
        )
    }
}

@Composable
fun VideoPlayerOverlay(
    video: Video,
    isPlaying: Boolean,
    currentTime: Long,
    duration: Long,
    onPlayPause: () -> Unit,
    onSeek: (Long) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 顶部信息栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "返回",
                    tint = Color.White
                )
            }
            
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = video.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = video.author,
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // 底部控制栏
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(24.dp)
        ) {
            // 进度条
            Slider(
                value = currentTime.toFloat(),
                onValueChange = { onSeek(it.toLong()) },
                valueRange = 0f..duration.toFloat(),
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFFB7299),
                    activeTrackColor = Color(0xFFFB7299),
                    inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 播放控制
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 播放/暂停
                IconButton(
                    onClick = onPlayPause,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "暂停" else "播放",
                        tint = Color.White,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                
                // 时间显示
                Text(
                    text = "${formatTime(currentTime)} / ${formatTime(duration)}",
                    fontSize = 16.sp,
                    color = Color.White
                )
                
                // 其他控制按钮
                Row {
                    IconButton(onClick = { /* 弹幕开关 */ }) {
                        Icon(
                            imageVector = Icons.Default.Message,
                            contentDescription = "弹幕",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { /* 画质选择 */ }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "设置",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

// 格式化时间
fun formatTime(millis: Long): String {
    val seconds = millis / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    
    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes % 60, seconds % 60)
    } else {
        String.format("%d:%02d", minutes, seconds % 60)
    }
}
```

### 2.6 搜索界面
```kotlin
@Composable
fun SearchScreen(
    onSearch: (String) -> Unit,
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var hasFocus by remember { mutableStateOf(true) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF29303A))
            .padding(horizontal = 80.dp, vertical = 60.dp)
    ) {
        // 搜索框
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "返回",
                    tint = Color.White
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // 搜索输入框
            BasicTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(
                        Color(0xFF1A1A1A),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = if (hasFocus) 2.dp else 1.dp,
                        color = if (hasFocus) Color(0xFFFB7299) else Color.White.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .onFocusChanged { hasFocus = it.hasFocus },
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    color = Color.White
                ),
                decorationBox = { innerTextField ->
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "搜索视频、UP主...",
                            fontSize = 20.sp,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    }
                    innerTextField()
                }
            )
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // 热门搜索
        Text(
            text = "热门搜索",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // 热门搜索标签
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf(
                "原神", "王者荣耀", "英雄联盟",
                "动漫", "音乐", "美食",
                "科技", "生活", "搞笑"
            ).forEach { keyword ->
                HotSearchChip(
                    keyword = keyword,
                    onClick = { searchQuery = keyword }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // 搜索按钮
        Button(
            onClick = { onSearch(searchQuery) },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            enabled = searchQuery.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFFB7299),
                contentColor = Color.White,
                disabledBackgroundColor = Color.Gray,
                disabledContentColor = Color.White.copy(alpha = 0.5f)
            )
        ) {
            Text(
                text = "搜索",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun HotSearchChip(
    keyword: String,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .clickable { onClick() }
            .onFocusChanged { isFocused = it.hasFocus }
            .border(
                width = if (isFocused) 2.dp else 1.dp,
                color = if (isFocused) Color(0xFFFB7299) else Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(20.dp)
            ),
        backgroundColor = Color(0xFF1A1A1A),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = keyword,
            fontSize = 16.sp,
            color = if (isFocused) Color(0xFFFB7299) else Color.White,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
        )
    }
}
```

## 3. 遥控器交互设计

### 3.1 焦点管理
```kotlin
@Composable
fun TvFocusableItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (isFocused: Boolean) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    
    Box(
        modifier = modifier
            .onFocusChanged { isFocused = it.hasFocus }
            .focusable()
            .clickable { onClick() }
            .border(
                width = if (isFocused) 3.dp else 0.dp,
                color = if (isFocused) Color(0xFFFB7299) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .scale(if (isFocused) 1.05f else 1f)
    ) {
        content(isFocused)
    }
}
```

### 3.2 返回键处理
```kotlin
@Composable
fun HandleBackPress(onBack: () -> Unit) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    
    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
    }
    
    DisposableEffect(backDispatcher) {
        backDispatcher?.addCallback(backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}
```

### 3.3 方向键导航
```kotlin
@Composable
fun TvNavigationHandler(
    onUp: (() -> Unit)? = null,
    onDown: (() -> Unit)? = null,
    onLeft: (() -> Unit)? = null,
    onRight: (() -> Unit)? = null,
    onEnter: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onPreviewKeyEvent { keyEvent ->
                when (keyEvent.key) {
                    Key.DirectionUp -> {
                        onUp?.invoke()
                        true
                    }
                    Key.DirectionDown -> {
                        onDown?.invoke()
                        true
                    }
                    Key.DirectionLeft -> {
                        onLeft?.invoke()
                        true
                    }
                    Key.DirectionRight -> {
                        onRight?.invoke()
                        true
                    }
                    Key.Enter, Key.NumPadEnter -> {
                        onEnter?.invoke()
                        true
                    }
                    else -> false
                }
            }
    )
}
```

## 4. 响应式设计

### 4.1 不同屏幕尺寸适配
```kotlin
@Composable
fun ResponsiveVideoGrid() {
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
        // ... 其他配置
    ) {
        // 视频卡片列表
    }
}
```

这个用户界面设计为bili鹅应用提供了完整的TV端界面方案，充分考虑了遥控器操作的便利性和大屏显示的优势，确保用户能够舒适地浏览和观看B站内容。