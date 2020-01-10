#一、基本符号：* - +. >
基本上所有的markdown标记都是基于这四个符号或组合，需要注意的是，如果以基本符号开头的标记，注意基本符号后有一个用于分割标记符和内容的空格。
#二、标题
一级标题 # 

二级标题 ## 

三级标题 ### 

四级标题 #### 

五级标题 ##### 

六级标题 ###### 

#三、列表
###无序列表
//形式一
+ a
+ b
+ c

//形式二
- d
- e
- f 

//形式三
* g
* h
* i
###有序列表
//正常形式
1. abc
2. bcd
3. cde

//错序效果
2. fgh
3. ghi
5. hij

###嵌套列表
//无序列表嵌套
+ 123
    + abc
    + bcd
    + cde
+ 465
+ 789
//有序列表嵌套
1. abcd
    1. abcde
    2. abcde
    3. abcde
2. bcde
3. cdef
#四、引用说明区块
###正常形式
> 引用内容、说明内容。在语句前面加一个 > ，注意是英文的那个右尖括号，注意空格，引用因为是一个区块，理论上是应该什么内容都可以放，比如说：标题，列表，引用等等。
###嵌套区块
> 一级引用
>> 二级引用
>>> 三级引用
>>>> 四级引用
>>>>> 五级引用
>>>>>> 六级引用

#五、代码块
1. 少量代码，单行使用，直接用`包裹起来就行了
` shaoliangdaima,danhangshiyong `
2. 大量代码，需要多行使用，用```包裹起来
```
        daliangdaima,xuyaoduohangshiyong
        daliangdaima,xuyaoduohangshiyong
        daliangdaima,xuyaoduohangshiyong
        daliangdaima,xuyaoduohangshiyong
        daliangdaima,xuyaoduohangshiyong
```

#六、链接
1. 行内式
   链接的文字放在[]中，链接地址放在随后的()中，链接也可以带title属性，链接地址后面空一格，然后用引号引起来

[百度](https://www.百度.com "创作你的创作")

2. 参数式
   链接的文字放在[]中，链接地址放在随后的:后，链接地址后面空一格，然后用引号引起来


[简书]: https://www.jianshu.com "创作你的创作"
[简书] 
[简书]: https://www.jianshu.com  
[简书]: https://www.jianshu.com  
[简书]: <https://www.jianshu.com> 
 
#七、图片
1.行内式
和链接的形式差不多，图片的名字放在[]中，图片地址放在随后的()中，title属性（图片地址后面空一格，然后用引号引起来）,注意的是[]前要加上!
![my-logo.png](https://upload-images.jianshu.io/upload_images/13623636-6d878e3d3ef63825.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240 "my-logo")

 ，任何形式的转载都请联系作者获得授权并注明出处。
2.参数式
图片的文字放在[]中，图片地址放在随后的:后，title属性（图片地址后面空一格，然后用引号引起来）,注意引用图片的时候在[]前要加上!
#八、分割线
1. 分割线可以由* - _（星号，减号，底线）这3个符号的至少3个符号表示，注意至少要3个，且不需要连续，有空格也可以
---
- - -
------
***
* * *
******
___
_ _ _
______

#九、其他
1.强调字体
一个星号或者是一个下划线包起来，会转换为<em>倾斜，如果是2个，会转换为<strong>加粗
*md* 
   
**md**

_md_
   
 __md__ 
 

 
2. 转义
 基本上和js转义一样,\加需要转义的字符
 \\
 \*
 \+
 \-
 \`
 \_ 
 
3. 删除线
  用~~把需要显示删除线的字符包裹起来
  ~~删除~~
  
#十、表格

//例子一

|123|234|345|
| ------ | ------ | ------ |
|abc|bcd|cde|
|abc|bcd|cde|
|abc|bcd|cde|
//例子二

|123|234|345|
| :------| ------: | :------: |
|abc|bcd|cde|
|abc|bcd|cde|
|abc|bcd|cde|

//例子三

123|234|345
:-|:-:|-:
abc|bcd|cde
abc|bcd|cde
abc|bcd|cde
  

