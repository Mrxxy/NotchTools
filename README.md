![image](http://upload-images.jianshu.io/upload_images/1342432-256fcfc6b347c68d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
# 1、概述

刘海屏指的是手机屏幕上方由于追求极致边框而采用的方案，表现为在顶部有块黑色遮挡，长得像刘海，所以叫刘海屏。

目前google在Android P上已经对刘海屏的适配进行了统一，所以在targetApi >= 28上可以使用谷歌官方推荐的适配方案进行刘海屏适配。但是在Android O版本的刘海屏如何适配呢？这就是本文要重点阐述的内容了：

1、对国内四大厂商（华为、小米、OPPO、VIVO）对Android O 版本刘海屏的适配方案进行介绍；
2、对Android P版本的刘海屏进行适配；
3、提出对Android O 版本刘海屏的通用解决方案，包括全屏占用刘海屏、全屏不占用刘海屏两种情况；
4、提出适配工具解决方案，让你的应用简单快捷的适配全面屏

# 2、适配与未适配的效果对比
因为相比普通常规手机而言，刘海屏顶部中间会突出一块刘海区域，所以会在给Actiivty设置全屏Flag的时候有一些不同。本文所涉及到的刘海屏适配都是在给Activity的window设置SYSTEM_UI_FLAG_FULLSCREEN（全屏flag）前提下的，在显示状态栏的情况下（不管是状态栏透明或者不透明），不是本文讨论的核心，我们的所说的刘海屏适配只是针对全屏沉浸式（状态栏隐藏）的情况下。

在设置SYSTEM_UI_FLAG_FULLSCREEN了Flag后，国内厂商的刘海屏手机对于此表现的默认显示效果都是有差异的，具体为：
1、华为手机默认是全屏但是不占用刘海区域；
2、小米手机默认是全屏但是不占用刘海区域；
3、oppo手机默认是全屏且占用刘海区域，可在设置里单独给APP设置是否隐藏刘海；
4、vivo手机默认是全屏且占用刘海区域，可在设置里单独给APP设置是否隐藏刘海；

备注：通过实验及查阅文档发现，华为和小米适配方案是类似的且适配方案成熟，oppo、和vivo的适配介绍是类似的，且基本无适配方案。华为、小米会分别对全屏占用刘海、全屏不占用刘海两种情况分别提供了方法。而oppo、vivo只提供了是否有刘海这个一个方法，并没有提供适配方案。


所以我们再全屏的情况下需要对四大厂商做下适配，不然有可能一个App在不同手机上表现不一致、或者会对UI做了截断，影响使用体验：

![占用刘海显示的刘海屏](http://upload-images.jianshu.io/upload_images/1342432-783ffc65ec8f43b8.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/300)
![未占用刘海显示的刘海屏](http://upload-images.jianshu.io/upload_images/1342432-645b64c23839db95.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/300)

# 3、适配方案

Android O的刘海屏适配方案可分为两种情况：

## 3、1 全屏且占用刘海屏的适配方案

对于需要全屏且占用刘海屏显示的情况，如沉浸式游戏、沉浸式阅读（需要把态栏隐藏），适配时可以采用如下步骤：
1、在Activity中使用setSystemUiVisibility设置全屏的一些标识；
2、根据不同厂商的适配规则（官网有提供）设置不同的flag（大都通过反射），来让App全屏沉浸式显示；
3、根据厂商提供的Api，获取刘海的高度，来调节一些View的间距，达到适配目的。

## 3、2 全屏但不占用刘海的适配方案

这种适配方案一般采用如下步骤:
1、去各大手机厂商官网找到对应的全屏但不占用刘海的方案，目前只有小米、华为提供了具体方法来设置是否占用刘海区域，oppo和vivo只提供了机型是否是刘海屏手机的方法，但未提供适配方案;
2、华为和小米都有具体方案来适配全屏不占用刘海的情况，这里主要对vivo和oppo进行适配。
这里要说明下，在系统设置里可以设置刘海是否隐藏，不过华为、小米是全局设置，而ov是可以单独对app进行设置，又因为ov没有提供具体的适配方案，所以对于ov的全屏适配达不到完美的适配，在不占用刘海时顶部会留出一块黑边。

## 3.3 华为手机刘海屏适配方案

![华为手机刘海屏适配方案](http://upload-images.jianshu.io/upload_images/1342432-b3ae8d1037f3eb85.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


### 3.3.1 判断华为手机是否为刘海屏手机

``` java
 @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean isNotchScreen(Window window) {
        boolean isNotchScreen = false;
        try {
            ClassLoader cl = window.getContext().getClassLoader();
            Class HwNotchSizeUtil =   cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            isNotchScreen = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            LogUtils.d(TAG, "hasNotchInScreen ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            LogUtils.d(TAG, "hasNotchInScreen NoSuchMethodException");
        } catch (Exception e) {
            LogUtils.d(TAG, "hasNotchInScreen Exception");
        } finally {
            return isNotchScreen;
        }
    }
```
### 3.3.2 获取华为手机的刘海屏高度

``` java

        @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int getNotchHeight(Window window) {
        if (!isNotchScreen(window)) {
            return 0;
        }
        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = window.getContext().getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e) {
        } catch (Exception e) {
        } finally {
            return ret[1];
        }
    }
```

### 3.3.3 设置页面在华为刘海屏手机使用刘海区

``` java
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setFullScreenWindowLayoutInDisplayCutout(Window window) {
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        try {
            Class layoutParamsExCls = Class.forName("com.huawei.android.view.LayoutParamsEx");
            Constructor con=layoutParamsExCls.getConstructor(WindowManager.LayoutParams.class);
            Object layoutParamsExObj=con.newInstance(layoutParams);
            Method method=layoutParamsExCls.getMethod("addHwFlags", int.class);
            method.invoke(layoutParamsExObj, FLAG_NOTCH_SUPPORT);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |InstantiationException
                | InvocationTargetException e) {
            Log.e("test", "hw add notch screen flag api error");
        } catch (Exception e) {
            Log.e("test", "other Exception");
        }
    }
```

### 3.3.4 设置页面在华为刘海屏手机不使用刘海区

``` java
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void setNotFullScreenWindowLayoutInDisplayCutout (Window window) {
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        try {
            Class layoutParamsExCls = Class.forName("com.huawei.android.view.LayoutParamsEx");
            Constructor con=layoutParamsExCls.getConstructor(WindowManager.LayoutParams.class);
            Object layoutParamsExObj=con.newInstance(layoutParams);
            Method method=layoutParamsExCls.getMethod("clearHwFlags", int.class);
            method.invoke(layoutParamsExObj, FLAG_NOTCH_SUPPORT);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |InstantiationException
                | InvocationTargetException e) {
            Log.e("test", "hw clear notch screen flag api error");
        } catch (Exception e) {
            Log.e("test", "other Exception");
        }
    }
```

## 3.4 小米手机刘海屏适配方案

### 3.4.1 判断小米手机是否为刘海屏手机

``` java
 @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean isNotchScreen(Window window) {
        return "1".equals(SystemProperties.getInstance().get("ro.miui.notch"));
    }
```

### 3.4.2 获取小米手机刘海屏高度

``` java

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int getNotchHeight(Window window) {
        if (!isNotchScreen(window)) {
            return 0;
        }

        int result = 0;
        if (window == null) {
            return 0;
        }
        Context context = window.getContext();
        if (isHideNotch(window.getContext())) {
            result = NotchStatusBarUtils.getStatusBarHeight(context);
        } else {
            result = getRealNotchHeight(context);
        }
        return result;
    }
```

### 3.4.3 设置页面在小米刘海屏手机使用刘海区

在 WindowManager.LayoutParams 增加 extraFlags 成员变量，用以声明该 window 是否使用耳朵区，其中，extraFlags 有以下变量：

``` java
0x00000100 开启配置
0x00000200 竖屏配置
0x00000400 横屏配置
```

组合后表示 Window 的配置，如：

``` java
0x00000100 | 0x00000200 竖屏绘制到耳朵区
0x00000100 | 0x00000400 横屏绘制到耳朵区
0x00000100 | 0x00000200 | 0x00000400 横竖屏都绘制到耳朵区
```

控制 extraFlags 时注意只控制这几位，不要影响其他位。可以用 Window 的 addExtraFlags 和 clearExtraFlags 来修改, 这两个方法是 MIUI 增加的方法，需要反射调用。

设置页面在小米刘海屏手机使用刘海区代码如下：

``` java
    public void fullScreenUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        super.fullScreenUseStatus(activity, notchCallBack);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isNotchScreen(activity.getWindow())) {
            //开启配置
            int FLAG_NOTCH = 0x00000100 | 0x00000200 | 0x00000400;
            try {
                Method method = Window.class.getMethod("addExtraFlags", int.class);
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                method.invoke(activity.getWindow(), FLAG_NOTCH);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
```

### 3.4.4 设置页面在小米刘海屏手机不使用刘海区


``` java
   @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void fullScreenDontUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        super.fullScreenDontUseStatus(activity, notchCallBack);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isNotchScreen(activity.getWindow())) {
            //开启配置
            int FLAG_NOTCH = 0x00000100 | 0x00000400;
            try {
                Method method = Window.class.getMethod("addExtraFlags", int.class);
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                method.invoke(activity.getWindow(), FLAG_NOTCH);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
```

### 3.4.5 小米刘海屏手机上刘海屏高度与状态栏高度差异

由于 Notch 设备的状态栏高度与正常机器不一样，因此在需要使用状态栏高度时，不建议写死一个值，而应该改为读取系统的值。

以下是获取当前设备状态栏高度的方法：

``` java
int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
if (resourceId > 0) {
result = context.getResources().getDimensionPixelSize(resourceId);
}
```

以下是获取当前设备刘海高度的方法：

``` java
int resourceId = context.getResources().getIdentifier("notch_height", "dimen", "android");
if (resourceId > 0) {
result = context.getResources().getDimensionPixelSize(resourceId);
}
```

## 3.5 OPPO手机刘海屏适配方案

![image](http://upload-images.jianshu.io/upload_images/1342432-f4000f2279b92058.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 3.5.1 判断OPPO手机是否为刘海屏手机

``` java
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean isNotchScreen(Window window) {
        if (window == null) {
            return false;
        }
        return window.getContext().getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }
```

### 3.5.2 获取OPPO手机刘海屏高度

目前OPPO刘海屏适配的官网上并没有给出获取刘海高度的方法，也没有给出占用刘海区域、不占用刘海区域的方法。
官网上给的图上的刘海的固定高度是80px，这里通过获取状态栏高度的方法得到值也是80，大概猜测OPPO手机的刘海高度是和状态栏高度一样的。

``` JAVA
   @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int getNotchHeight(Window window) {
        if (!isNotchScreen(window)) {
            return 0;
        }

        return NotchStatusBarUtils.getStatusBarHeight(window.getContext());
    }
```

### 3.5.3 设置页面在OPPO刘海屏手机使用刘海区

OPPO手机并没有像小米、华为手机一样提供具体的方法设置刘海区域，但是OPPO手机在全屏状态下默认是占用刘海区域的，是完全沉浸式的，所以只需设置全屏Flag即可：

``` java
 public static void setFullScreenWithSystemUi(final Window window, boolean setListener) {
        int systemUiVisibility = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            systemUiVisibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        window.getDecorView().setSystemUiVisibility(systemUiVisibility);

        if (setListener) {
            window.getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if (visibility == 0) {
                        setFullScreenWithSystemUi(window, false);
                    }
                }
            });
        }
    }
```

### 3.5.4 设置页面在OPPO刘海屏手机不使用刘海区

因为OPPO手机默认是全屏占用刘海区域的，所以如果想达到全屏且不占用刘海区域的话，需要在Activty的顶部通过添加一个状态栏高度的黑色布局，来下移整体布局，从而视觉上看起来是已经适配了。

``` java
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void fullScreenDontUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        NotchStatusBarUtils.setFullScreenWithSystemUi(activity.getWindow(), true);
        onBindCallBackWithNotchProperty(activity, notchCallBack);
        if (isNotchScreen(activity.getWindow())) {
            NotchStatusBarUtils.setFakeNotchView(activity.getWindow());
        }
    }

protected void onBindCallBackWithNotchProperty(Activity activity, OnNotchCallBack notchCallBack) {
        if (notchCallBack != null) {
            NotchProperty notchProperty = new NotchProperty();
            notchProperty.setNotchHeight(getNotchHeight(activity.getWindow()));
            notchProperty.setNotch(isNotchScreen(activity.getWindow()));
            if (notchCallBack != null) {
                notchCallBack.onNotchPropertyCallback(notchProperty);
            }
        }
    }
```

代码中通过NotchStatusBarUtils.setFakeNotchView为你的layout布局中最顶部的View容器添加一个状态栏高度的View，从而使整体布局下移，也就达到了全屏但是不占用状态栏区域的目的。

setFakeNotchView的方法如下：

``` java
   public static void setFakeNotchView(Window window) {
        ViewGroup notchContainer = removeFakeNotchView(window);
        if (notchContainer == null) {
            return;
        }
        View view = new View(window.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                NotchTools.getFullScreenTools().getNotchHeight(window)));
        view.setBackgroundColor(Color.BLACK);
        notchContainer.addView(view);
    }


   public static ViewGroup removeFakeNotchView(Window window) {
        ViewGroup notchContainer = getNotchContainer(window);
        if (notchContainer == null) {
            return null;
        }
        int childCount = notchContainer.getChildCount();
        if (childCount > 0) {
            notchContainer.removeAllViews();
        }
        return notchContainer;
    }

    public static ViewGroup getNotchContainer(Window window) {

        View decorView = window.getDecorView();
        if (decorView == null) {
            return null;
        }
        return decorView.findViewWithTag(NotchTools.NOTCH_CONTAINER);
    }

```

原理是在自己的Activity布局中防止一个viewContainer，然后给这个viewContainer设置一个TAG，在适配OPPO机型全屏不占用刘海情况时可以通过findViewWithTag来找到这个viewConatiner，然后在这个viewContainer中添加一个刘海高度的黑色布局，即达到整体布局下移的视觉效果，具体可参考源码。

## 3.6 VIVO手机刘海屏适配方案

### 3.6.1 判断VIVO手机是否为刘海屏手机

``` java
  @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean isNotchScreen(Window window) {
        if (window == null) {
            return false;
        }
        if (mClass == null) {
            ClassLoader classLoader = window.getContext().getClassLoader();
            try {
                mClass = classLoader.loadClass("android.util.FtFeature");
                mMethod = mClass.getMethod("isFeatureSupport", Integer.TYPE);
                return (boolean) mMethod.invoke(mClass, 0x00000020);
            } catch (ClassNotFoundException e) {
                return false;
            } catch (NoSuchMethodException e) {
                return false;
            } catch (IllegalAccessException e) {
                return false;
            } catch (InvocationTargetException e) {
                return false;
            }
        } else {
            if (mMethod == null) {
                try {
                    mMethod = mClass.getMethod("isFeatureSupport", Integer.TYPE);
                } catch (NoSuchMethodException e) {
                    return false;
                }
                try {
                    return (boolean) mMethod.invoke(mClass, 0x00000020);
                } catch (IllegalAccessException e) {
                    return false;
                } catch (InvocationTargetException e) {
                    return false;
                }
            }
        }
        return false;
    }
```

### 3.6.2 设置页面在VIVO刘海屏手机使用刘海区

与oopo一致


### 3.6.3 设置页面在VIVO刘海屏手机不使用刘海区

与oppo一致

## 3.7 Android P版本适配

### 3.7.1 Android P刘海介绍

Android P 提供了 3 种显示模式供开发者选择，分别是：

默认模式（LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT）
刘海区绘制模式（ LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES）
刘海区不绘制模式（LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER）

由于各个厂商的刘海或者凹口形状、位置不一， 开发者可以通过 WindowInsets.getDisplayCutout()  来获得 DisplayCutout object。

### 3.7.2 判断手机是否为刘海屏手机

``` java
 @RequiresApi(api = 28)
    @Override
    public boolean isNotchScreen(Window window) {
        WindowInsets windowInsets = window.getDecorView().getRootWindowInsets();
        if (windowInsets == null) {
            return false;
        }

        DisplayCutout displayCutout = windowInsets.getDisplayCutout();
        if(displayCutout == null || displayCutout.getBoundingRects() == null){
            return false;
        }

        return true;
    }
```

### 3.7.3 获取刘海高度

``` java
    @RequiresApi(api = 28)
    @Override
    public int getNotchHeight(Window window) {
        int notchHeight = 0;
        WindowInsets windowInsets = window.getDecorView().getRootWindowInsets();
        if (windowInsets == null) {
            return 0;
        }

        DisplayCutout displayCutout = windowInsets.getDisplayCutout();
        if(displayCutout == null || displayCutout.getBoundingRects() == null){
            return 0;
        }

        notchHeight = displayCutout.getSafeInsetTop();

        return notchHeight;
    }
```

### 3.7.4 Android P全屏不占用刘海

``` java
    @RequiresApi(api = 28)
    @Override
    public void fullScreenDontUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        super.fullScreenDontUseStatus(activity, notchCallBack);
        if (isNotchScreen(activity.getWindow())) {
            WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
            attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
            activity.getWindow().setAttributes(attributes);
        }
    }
```

### 3.7.5 Android P全屏占用刘海

``` java
   @RequiresApi(api = 28)
    @Override
    public void fullScreenUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        super.fullScreenUseStatus(activity, notchCallBack);
        if (isNotchScreen(activity.getWindow())) {
            WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
            attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            activity.getWindow().setAttributes(attributes);
        }
    }
```

### 3.7.6 适配Android P时注意事项

适配Android P时我们需要获取到WindowInsets对象，这个对象是通过window.getDecorView().getRootWindowInsets()获取的。我们一般在给Activity适配时需要在Activity的onCreate方法中完成，但是这个时候获取到的WindowInsets对象可能为null，所以在给Android P适配时需要通过handler.post方法完成，如：

``` java
ThreadUtils.post2UI(new Runnable() {
            @Override
            public void run() {
                if (notchScreenSupport == null) {
                    checkScreenSupportInit(activity.getWindow());
                }
                if (notchScreenSupport != null) {
                    notchScreenSupport.fullScreenDontUseStatus(activity, notchCallBack);
                }
            }
        });
```

## 3.8 在旋转屏幕时的适配

我们一般会在Activity的onCreate方法中对Activity进行刘海适配，但是在一些涉及到视频播放的场景下，会有横屏旋转隐藏状态栏、竖屏时显示状态栏的情况，大部分这些逻辑都是写在底层视频播放逻辑中的，所以我们在做涉及到有可能重设状态栏Flag的情况下，需要进行一些设置，具体为：
1、在Activity的onCreate中设置SYSTEM_UI_FLAG_FULLSCREEN,且完成刘海屏适配
2、在Activity的onWindowFocusChanged方法中，再次调用刘海屏适配方法，防止横屏时重置了相关适配，然后再回到竖屏了因为flag的配置不对，导致显示异常。

# 4 [NotchTools](https://github.com/zhangzhun132/NotchTools/tree/master)适配工具

基于前面的适配规则，简单的对刘海屏全屏适配封装了一个工具----[NotchTools](https://github.com/zhangzhun132/NotchTools/tree/master)。NotchTools的初衷是尽可能简单的进行刘海屏适配，其中包括全屏占用刘海区域、全屏不占用刘海屏区域两种情况，对于透明状态栏的情况，没有进行适配（原理是和全屏占用刘海屏区域是一样的），[NotchTools](https://github.com/zhangzhun132/NotchTools/tree/master)只处理全屏（隐藏状态栏）的情况。

特别说明：

对于使用刘海区域的情况，因为有时候需要对layout文件的部分view进行下移刘海或者状态栏的高度达到适配的目的，但有的时候又不希望对布局或view进行下移，所以NotchTools工具在全屏且占用刘海区域的情况下未做下移处理，使用者可以在OnNotchCallBack的回调中，获得状态栏高度，然后自行完成下移操作，具体代码在FullScreenUseNotchActivity有给出使用方式。

## 4.1 如何使用

### 4.1.1 NotchTools适配全屏但不占用刘海情况

使用方法为在Activity的onCreate方法中使用如下代码：
``` java
NotchTools.getFullScreenTools().fullScreenDontUseStatus(this, new OnNotchCallBack() {
            @Override
            public void onNotchPropertyCallback(NotchProperty notchProperty) {
                
            }
        });

或者
NotchTools.getFullScreenTools().fullScreenDontUseStatus(this);

```

### 4.1.2 NotchTools适配全屏且占用刘海情况

同理，在需要处理的Activity的onCreate中使用如下代码：

``` java
NotchTools.getFullScreenTools().fullScreenUseStatus(this, new OnNotchCallBack() {
            @Override
            public void onNotchPropertyCallback(NotchProperty notchProperty) {
                int marginTop = notchProperty.getMarginTop();
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mBackView.getLayoutParams();
                layoutParams.topMargin += marginTop;
                mBackView.setLayoutParams(layoutParams);
            }
        });
```

对于需要使用刘海区域的适配中，使用者需要在OnNotchCallBack回调的onNotchPropertyCallback方法中获取状态栏高度notchHeight，然后自行去为自己的布局或者View做下移操作，这样灵活性更好一点。

### 4.1.3 NotchTools额外说明

NotchTools中的Activity都继承了BaseActivity，BaseActivity的代码如下：

``` java
    /**
     * 刘海容器
     */
    private FrameLayout mNotchContainer;
    /**
     * 主内容区
     */
    private FrameLayout mContentContainer;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base);
        mNotchContainer = findViewById(R.id.notch_container);
        mNotchContainer.setTag(NotchTools.NOTCH_CONTAINER);
        mContentContainer = findViewById(R.id.content_container);
        onBindContentContainer(layoutResID);
    }

    private void onBindContentContainer(int layoutResID) {
        LayoutInflater.from(this).inflate(layoutResID, mContentContainer, true);
    }
```

BaseActivity内部重载了setContentView(int layoutResID)方法，在layoutResID的上方添加了一个Framelayout,也就是刘海的父容器，默认情况下他是一个空布局，但是在全屏不占用刘海的情况下，为了适配OV手机（OV手机没有具体方法来实现全屏不占用刘海）,所以需要在mNotchContainer中放入刘海高度的黑色View，来下移整体布局，达到适配目的。

# 5 总结

本文讨论了Android O及Android P机型中适配刘海屏的原理、方法、和解决方案，并提供了源码供参考。

题外话：对于Android O上的各大产商提供的适配方案，有的厂商在官网上明确说明了会在Android P上进行兼容，也就是O的适配方案依然在未来的P机型上可行。但是有的厂商已经在官网明确说明了在未来的Android P上不会兼容O的适配方案，所以，适配还是任重而道远。


![参考](https://upload-images.jianshu.io/upload_images/1342432-34f4f6ffeb8e798c.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


