# bili鹅 - B站第三方电视版应用

<div align="center">

![Logo](docs/logo.png)

**专为电视优化的B站客户端**

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.10-blue.svg)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-5.0%2B-green.svg)](https://www.android.com/)
[![API](https://img.shields.io/badge/API-21%2B-orange.svg)](https://developer.android.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Version](https://img.shields.io/badge/Version-1.0.0-red.svg)](CHANGELOG.md)

</div>

## 📱 应用截图

<div align="center">

| 启动页面 | 主界面 | 视频播放 |
|:--------:|:------:|:--------:|
| ![Splash](screenshots/splash.png) | ![Home](screenshots/home.png) | ![Player](screenshots/player.png) |

| 搜索页面 | 个人中心 | 设置页面 |
|:--------:|:--------:|:--------:|
| ![Search](screenshots/search.png) | ![Profile](screenshots/profile.png) | ![Settings](screenshots/settings.png) |

</div>

## ✨ 功能特性

### 🎯 核心功能
- ✅ **用户登录** - 支持扫码登录和游客模式
- ✅ **视频浏览** - 推荐视频、分区浏览
- ✅ **视频播放** - 高清播放、弹幕显示
- ✅ **搜索功能** - 关键词搜索、热门搜索
- ✅ **个人中心** - 观看历史、收藏管理
- ✅ **遥控器适配** - 专为电视遥控器优化

### 🎨 界面特色
- 📺 **大屏优化** - 专为电视大屏设计
- 🎮 **遥控器友好** - 完全适配遥控器操作
- ⚡ **流畅体验** - 优化的性能表现
- 🎨 **简洁美观** - 现代化的UI设计

### 🔧 技术特性
- 🚀 **Kotlin + Jetpack Compose** - 现代Android开发技术
- 📱 **ExoPlayer** - 强大的视频播放引擎
- 🌐 **Retrofit** - 高效的网络请求
- 💉 **Hilt** - 依赖注入框架
- 📊 **MVVM架构** - 清晰的项目架构

## 🚀 快速开始

### 环境要求
- Android Studio Arctic Fox (2020.3.1) 或更高版本
- Kotlin 1.8+
- Android SDK API 21+ (Android 5.0+)
- JDK 8 或更高版本

### 安装运行

#### 方法一：下载APK安装
1. 前往 [Releases](https://github.com/biligo/bili-go/releases) 页面
2. 下载最新版本的 `app-release.apk`
3. 将APK文件拷贝到电视设备
4. 在电视上安装APK（需开启"未知来源"权限）

#### 方法二：源码编译
```bash
# 克隆项目
git clone https://github.com/biligo/bili-go.git
cd bili-go

# 构建Debug版本
./gradlew assembleDebug

# 构建Release版本
./gradlew assembleRelease

# 安装到设备
./gradlew installDebug
```

### 遥控器操作指南

| 按键 | 功能 |
|------|------|
| 方向键（↑↓←→） | 移动焦点 |
| 确定键（OK） | 选择/播放暂停 |
| 返回键 | 返回上一级 |
| 菜单键 | 显示菜单 |

## 📖 文档

- [📋 项目概述](bili鹅_项目概述.md) - 项目整体介绍
- [🏗️ 技术架构](bili鹅_技术架构.md) - 技术架构设计
- [🎨 用户界面设计](bili鹅_用户界面设计.md) - UI设计方案
- [📖 使用说明](bili鹅_使用说明.md) - 用户使用指南
- [🔧 开发指南](bili鹅_开发指南.md) - 开发者文档

## 🛠️ 开发

### 项目结构
```
com.biligo/
├── data/              # 数据层
│   ├── model/        # 数据模型
│   ├── repository/   # 数据仓库
│   ├── local/        # 本地数据源
│   └── remote/       # 远程数据源
├── domain/            # 业务逻辑层
├── ui/                # 表现层
│   ├── screen/       # 屏幕界面
│   ├── component/    # 可复用组件
│   └── theme/        # 主题样式
└── utils/             # 工具类
```

### 核心依赖

```kotlin
// Jetpack Compose
implementation("androidx.compose.ui:ui:1.5.4")
implementation("androidx.compose.material3:material3:1.1.2")

// 视频播放
implementation("androidx.media3:media3-exoplayer:1.2.0")

// 网络请求
implementation("com.squareup.retrofit2:retrofit:2.9.0")

// 图片加载
implementation("io.coil-kt:coil-compose:2.5.0")

// 依赖注入
implementation("com.google.dagger:hilt-android:2.48")
```

### 构建命令

```bash
# 清理项目
./gradlew clean

# 构建Debug APK
./gradlew assembleDebug

# 构建Release APK
./gradlew assembleRelease

# 运行测试
./gradlew test

# 代码检查
./gradlew lint
```

## 🐛 常见问题

### Q: 无法安装APK？
**A:** 检查电视是否开启"未知来源"安装权限，路径：设置 → 安全 → 未知来源。

### Q: 视频无法播放？
**A:** 检查网络连接，或尝试重新登录。部分视频可能有版权限制。

### Q: 遥控器无响应？
**A:** 检查遥控器电池，或尝试重启应用。确保遥控器已正确配对。

### Q: 弹幕不显示？
**A:** 检查弹幕开关是否开启，或视频是否支持弹幕功能。

查看更多问题请访问 [FAQ](docs/FAQ.md)

## 🤝 贡献

欢迎提交Issue和Pull Request！

### 开发流程
1. Fork 本项目
2. 创建您的功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交您的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开一个 Pull Request

### 代码规范
- 使用Kotlin标准代码风格
- 遵循MVVM架构
- 添加适当的注释
- 编写单元测试

## 📄 开源协议

本项目采用 **MIT** 协议开源，详见 [LICENSE](LICENSE) 文件。

```
MIT License

Copyright (c) 2026 bili鹅

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## 🙏 致谢

感谢以下开源项目：

- [Jetpack Compose](https://developer.android.com/jetpack/compose) - 现代UI工具包
- [ExoPlayer](https://exoplayer.dev/) - 强大的视频播放器
- [Retrofit](https://square.github.io/retrofit/) - 类型安全的HTTP客户端
- [Coil](https://coil-kt.github.io/coil/) - Kotlin图片加载库
- [Hilt](https://dagger.dev/hilt/) - 依赖注入框架

## 📞 联系我们

- 📧 邮箱：support@biligo.com
- 💬 QQ群：123456789
- 🌐 官网：www.biligo.com
- 🐙 GitHub：[bili-go](https://github.com/biligo/bili-go)

## 📊 数据统计

![GitHub stars](https://img.shields.io/github/stars/biligo/bili-go?style=social)
![GitHub forks](https://img.shields.io/github/forks/biligo/bili-go?style=social)
![GitHub issues](https://img.shields.io/github/issues/biligo/bili-go)
![GitHub pull requests](https://img.shields.io/github/issues-pr/biligo/bili-go)

---

<div align="center">

**Made with ❤️ by bili鹅 Team**

*让大屏观看更精彩！*

</div>