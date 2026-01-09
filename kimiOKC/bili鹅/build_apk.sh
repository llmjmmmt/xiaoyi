#!/bin/bash

# bili鹅 APK 构建脚本

echo "=========================================="
echo "开始构建 bili鹅 APK 文件"
echo "=========================================="

# 检查Java环境
echo "检查Java环境..."
if ! command -v java &> /dev/null; then
    echo "错误: 未找到Java环境，请先安装Java JDK 8或更高版本"
    exit 1
fi

java -version

# 检查Android SDK
echo "检查Android SDK..."
if [ -z "$ANDROID_HOME" ]; then
    echo "错误: 未设置ANDROID_HOME环境变量"
    echo "请设置 ANDROID_HOME 指向您的Android SDK目录"
    exit 1
fi

echo "ANDROID_HOME: $ANDROID_HOME"

# 检查Gradle
echo "检查Gradle..."
if ! command -v gradle &> /dev/null; then
    echo "错误: 未找到Gradle，请先安装Gradle"
    exit 1
fi

gradle -version

# 清理项目
echo "=========================================="
echo "清理项目..."
echo "=========================================="
./gradlew clean

# 构建Debug版本
echo "=========================================="
echo "构建Debug版本..."
echo "=========================================="
./gradlew assembleDebug

# 构建Release版本
echo "=========================================="
echo "构建Release版本..."
echo "=========================================="
./gradlew assembleRelease

# 检查构建结果
if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
    echo "=========================================="
    echo "Debug APK 构建成功!"
    echo "文件路径: app/build/outputs/apk/debug/app-debug.apk"
    echo "=========================================="
fi

if [ -f "app/build/outputs/apk/release/app-release.apk" ]; then
    echo "=========================================="
    echo "Release APK 构建成功!"
    echo "文件路径: app/build/outputs/apk/release/app-release.apk"
    echo "=========================================="
fi

echo "=========================================="
echo "构建完成!"
echo "=========================================="