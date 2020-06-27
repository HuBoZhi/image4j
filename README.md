# image4j
- 在原基础上封装了工具类，进行转换的对接，提高转换的准确度

# 函数及参数说明：
工具类封装类多个功能函数：
建议仅使用 Convert 函数，会根据输入的文件路径自动执行相应操作<br>
在main函数中展示了所有图片的转换过程，<br>
有些转换之所以会有中间变量，是因为直接转换会造成失真或转换失败的情况<br>

---

#### 参数说明：
  以下功能函数中的 strInputFilePath 和 strOutputFilePath 均为字符串类型
  - strInputFilePath  : 原始文件路径
  - strOutputFilePath : 目标文件路径
  - formatName ：目标类型

---

#### 函数说明：

* 将图片JPG/PNG/BMP转换为ICO
``` java
public static boolean ConvertIamgeToICO(String strInputFilePath, String strOutputFilePath) 
```
* 将ICO转换为图片JPG/PNG/BMP
``` java
public static boolean ConvertICOToImage(String strInputFilePath, String strOutputFilePath) 
```
* 将图片JPG/PNG/BMP转换为图片JPG/PNG/BMP
``` java
public static boolean ConvertImageToImage(String strInputFilePath, String strOutputFilePath)  
```
* 判断是否为JPG/PNG/BMP
``` java
private static boolean isImage(String file) 
```
* 辅助转换工具：将图片JPG/PNG转换为图片PNG/JPG
``` java
private static boolean ImageConvert(String strInputFilePath, String strOutputFilePath,String formatName)  
````
* 推荐使用函数，隐藏所有处理过程，只要输入源文件及目标文件路径即可
``` java
public static boolean Convert(String strInputFilePath, String strOutputFilePath) 
```

# 调用方法
``` java
// 以下为图片转换过程以及函数调用方法（基本一样）
// 返回参数都是布尔类型，成功为true，失败为false

        //jpg -> ico
        Convert("D:\\Temp\\timg.jpg","D:\\Temp\\timg.ico");

        //jpg -> png
        Convert("D:\\Temp\\timg.jpg","D:\\Temp\\timg_jpg.png");

        //jpg -> bmp
        Convert("D:\\Temp\\timg.jpg","D:\\Temp\\timg_jpg.bmp");

        //png -> jpg -> ico
        Convert("D:\\Temp\\timg.png","D:\\Temp\\timg_png.ico");

        //png -> jpg
        Convert("D:\\Temp\\timg.png","D:\\Temp\\timg_png.jpg");

        //png -> bmp
        Convert("D:\\Temp\\timg.png","D:\\Temp\\timg_png.bmp");

        //ico -> png
        Convert("D:\\Temp\\timg.ico","D:\\Temp\\timg_ico.png");

        //ico -> png -> jpg
        Convert("D:\\Temp\\timg.ico","D:\\Temp\\timg_ico.jpg");

        //ico -> bmp
        Convert("D:\\Temp\\timg.ico","D:\\Temp\\timg_ico.bmp");
```
