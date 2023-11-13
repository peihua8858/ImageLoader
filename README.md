# ImageLoader
   一款针对Android平台下的图片加载器。<br>

[![Jitpack](https://jitpack.io/v/peihua8858/ImageLoader.svg)](https://github.com/peihua8858)
[![PRs Welcome](https://img.shields.io/badge/PRs-Welcome-brightgreen.svg)](https://github.com/peihua8858)
[![Star](https://img.shields.io/github/stars/peihua8858/ImageLoader.svg)](https://github.com/peihua8858/ImageLoader)


## 目录
-[最新版本](https://github.com/peihua8858/ImageLoader/releases/tag/1.1.0)<br>
-[如何引用](#如何引用)<br>
-[进阶使用](#进阶使用)<br>
-[如何提Issues](https://github.com/peihua8858/ImageLoader/wiki/%E5%A6%82%E4%BD%95%E6%8F%90Issues%3F)<br>
-[License](#License)<br>



## 如何引用
* 把 `maven { url 'https://jitpack.io' }` 加入到 repositories 中
* 添加如下依赖，末尾的「latestVersion」指的是ImageLoader [![Download](https://jitpack.io/v/peihua8858/ImageLoader.svg)](https://jitpack.io/#peihua8858/ImageLoader) 里的版本名称，请自行替换。
使用Gradle
```sh
repositories {
  google()
  maven { url 'https://jitpack.io' }
}

dependencies {
  // ImageLoader
  implementation 'com.github.peihua8858.ImageLoader:imageloader:${latestVersion}'
  implementation 'com.github.peihua8858.ImageLoader:glidefetcher:${latestVersion}'
}
```

或者Maven:

```xml
<dependency>
  <groupId>com.github.peihua8858</groupId>
  <artifactId>ImageLoader</artifactId>
  <version>${latestVersion}</version>
</dependency>
```

## 进阶使用

简单用例如下所示:

1、xml 布局文件

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:foreground="?attr/selectableItemBackground">

    <ImageView
        android:id="@+id/iv_image"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_gravity="center"
        android:scaleType="centerCrop"
        app:srcCompat="@drawable/ic_zaful_defualt_loading"
        tools:ignore="ContentDescription" />
</FrameLayout>
```
或者
```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:foreground="?attr/selectableItemBackground">

    <com.zaful.framework.widget.RatioImageView
        android:id="@+id/iv_image"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_gravity="center"
        android:scaleType="centerCrop"
        app:srcCompat="@drawable/ic_zaful_defualt_loading"
        tools:ignore="ContentDescription" />
</FrameLayout>
```
2、加载图片图片
```kotlin
import com.fz.imageloader.ImageLoader
//初始化图片加载器
ImageLoader.getInstance().createProcessor(new ImageGlideFetcher());

binding.apply {
     //java 或kotlin兼可
    ImageLoader.getInstance().loadImage(ImageOptions.build {
        setImageUrl(pictures[position])
        setTarget(ivImage)
    })
}

//或者
import com.fz.imageloader.ImageLoader
binding.apply {
     //java 或kotlin兼可
  ivImage.setImageUrl(pictures[position])
}
```
## License
```sh
Copyright 2023 peihua

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```


