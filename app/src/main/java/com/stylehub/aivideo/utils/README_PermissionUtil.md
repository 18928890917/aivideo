# PermissionUtil 权限管理工具使用说明

## 概述

`PermissionUtil` 是一个用于Android权限申请和管理的工具类，支持传统的Activity权限申请和现代的Compose权限管理。

## 功能特性

- ✅ 支持单个和多个权限检查
- ✅ 支持Android 13+的新媒体权限
- ✅ 提供Compose权限状态管理
- ✅ 支持权限申请结果回调
- ✅ 自动处理权限被永久拒绝的情况
- ✅ 提供便捷的扩展函数

## 权限列表

工具类预定义了常用的权限常量：

```kotlin
PermissionUtil.Permissions.CAMERA                    // 相机权限
PermissionUtil.Permissions.READ_EXTERNAL_STORAGE     // 读取外部存储
PermissionUtil.Permissions.WRITE_EXTERNAL_STORAGE    // 写入外部存储
PermissionUtil.Permissions.READ_MEDIA_IMAGES         // Android 13+ 图片权限
PermissionUtil.Permissions.READ_MEDIA_VIDEO          // Android 13+ 视频权限
PermissionUtil.Permissions.READ_MEDIA_AUDIO          // Android 13+ 音频权限
PermissionUtil.Permissions.INTERNET                  // 网络权限
PermissionUtil.Permissions.ACCESS_NETWORK_STATE      // 网络状态权限
```

## 使用方法

### 1. 基本权限检查

```kotlin
// 检查单个权限
val hasCamera = PermissionUtil.hasPermission(context, PermissionUtil.Permissions.CAMERA)

// 检查多个权限
val permissions = arrayOf(
    PermissionUtil.Permissions.CAMERA,
    PermissionUtil.Permissions.READ_EXTERNAL_STORAGE
)
val hasAllPermissions = PermissionUtil.hasPermissions(context, permissions)
```

### 2. 在Activity中申请权限

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 申请相机和存储权限
        val permissions = PermissionUtil.getCameraAndStoragePermissions()
        
        requestPermissionsWithCallback(
            permissions = permissions,
            onGranted = {
                // 权限已授予，执行相关操作
                startCamera()
            },
            onDenied = {
                // 权限被拒绝，显示说明
                showPermissionExplanation()
            },
            onPermanentlyDenied = {
                // 权限被永久拒绝，引导用户到设置页面
                PermissionUtil.openAppSettings(this)
            }
        )
    }
}
```

### 3. 在Compose中使用

```kotlin
@Composable
fun CameraScreen() {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(
        permissions = PermissionUtil.getCameraAndStoragePermissions()
    ) { granted ->
        if (granted) {
            // 权限已授予
            startCamera()
        } else {
            // 权限被拒绝
            showPermissionDialog()
        }
    }
    
    if (!permissionState.hasPermissions) {
        PermissionRequestDialog(
            permissions = PermissionUtil.getCameraAndStoragePermissions(),
            onPermissionGranted = { startCamera() },
            onPermissionDenied = { showPermissionDialog() },
            onDismiss = { /* 处理取消操作 */ }
        )
    }
}
```

### 4. 使用预定义的权限组合

```kotlin
// 获取相机和存储权限（自动适配Android版本）
val cameraAndStorage = PermissionUtil.getCameraAndStoragePermissions()

// 获取媒体权限（Android 13+使用新权限，低版本使用旧权限）
val mediaPermissions = PermissionUtil.getMediaPermissions()
```

### 5. 权限状态显示组件

```kotlin
@Composable
fun SettingsScreen() {
    Column {
        // 显示当前权限状态
        PermissionStatusCard()
        
        // 其他设置项...
    }
}
```

## 高级用法

### 1. 自定义权限申请

```kotlin
val launcher = PermissionUtil.requestPermissions(
    activity = this,
    permissions = arrayOf(PermissionUtil.Permissions.CAMERA),
    callback = object : PermissionUtil.PermissionCallback {
        override fun onPermissionGranted(permissions: Array<String>) {
            // 权限已授予
        }
        
        override fun onPermissionDenied(permissions: Array<String>) {
            // 权限被拒绝
        }
        
        override fun onPermissionPermanentlyDenied(permissions: Array<String>) {
            // 权限被永久拒绝
        }
    }
)
launcher.launch(arrayOf(PermissionUtil.Permissions.CAMERA))
```

### 2. 获取需要申请的权限

```kotlin
val allPermissions = PermissionUtil.getCameraAndStoragePermissions()
val requiredPermissions = PermissionUtil.getPermissionsToRequest(context, allPermissions)

if (requiredPermissions.isNotEmpty()) {
    // 有权限需要申请
    requestPermissions(requiredPermissions)
}
```

### 3. 检查权限说明是否应该显示

```kotlin
val shouldShowRationale = PermissionUtil.shouldShowRequestPermissionRationale(
    activity = this,
    permission = PermissionUtil.Permissions.CAMERA
)

if (shouldShowRationale) {
    // 显示权限说明对话框
    showPermissionExplanationDialog()
}
```

## 注意事项

1. **Android版本适配**: 工具类自动处理Android 13+的新媒体权限
2. **权限声明**: 确保在AndroidManifest.xml中声明了所需的权限
3. **生命周期管理**: Compose组件会自动处理生命周期，避免内存泄漏
4. **用户体验**: 建议在权限被拒绝时提供友好的说明和引导

## 常见问题

### Q: 为什么某些权限申请失败？
A: 检查AndroidManifest.xml中是否已声明相应权限，以及目标SDK版本是否支持该权限。

### Q: 如何处理权限被永久拒绝的情况？
A: 使用`PermissionUtil.openAppSettings(context)`引导用户到应用设置页面手动开启权限。

### Q: 在Compose中如何监听权限变化？
A: 使用`rememberPermissionState`会自动监听权限状态变化，并在权限状态改变时触发回调。

## 更新日志

- v1.0: 初始版本，支持基本权限申请和管理
- 支持Android 13+新媒体权限
- 提供Compose权限状态管理
- 添加便捷的扩展函数 