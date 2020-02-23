## 实验室寒假作业介绍

### 1.实现自定义ToolBar

> app的几个fragment中自定义了一个ToolBar
> 其中只有一个MainActivity活动，大部分操作在fragment中进行，所以遇到很多问题

![image](https://github.com/GZAY/Yichuguanjia2/blob/master/Picture/img12.png)
![image](https://github.com/GZAY/Yichuguanjia2/blob/master/Picture/img11.png)

### 2.实现列表下拉刷新

> 在灵感fragment中的viewpager中使用了SwipeRefreshLayout实现下拉列表刷新
> 使用 NestedScrollView包裹 RecyclerView实现嵌套滑动。

![image](https://github.com/GZAY/Yichuguanjia2/blob/master/Picture/img10.png)

### 3.实现多页滑动效果  

> 在灵感fragment中使用如下布局结构
>
> ```
> <DrawerLayout>
> 	<CoordinatorLayout>
>      <AppBarLayout>	
>          <Toolbar>
>          <TabLayout>
>      <AppBarLayout>	
>      <ViewPager>
> 	<CoordinatorLayout>
> 	<NavigationView>
> <DrawerLayout>
> ```
>
> TabLayout和ViewPager实现了多页滑动效果
  
![image](https://github.com/GZAY/Yichuguanjia2/blob/master/Picture/img2.png)  

### 4.侧滑栏效果

> 上面的布局结构中用DrawerLayout和NavigationView实现侧滑栏效果

![image](https://github.com/GZAY/Yichuguanjia2/blob/master/Picture/img8.png)

### 5.加载效果、网络请求，JSON数据获取与解析、使用开放接口

> 在ViewPager中实现图片展示，在加载图片之前用到了加载效果，用的是之前培训的接口，用网格布局管理器实现多列图片展示。Json数据获取与解析在第一行代码中学习了Json的解析方式。由于之前在网络请求中解决个Bug花了好几天，又没有找到合适的接口所以并未使用新的接口。

![image](https://github.com/GZAY/Yichuguanjia2/blob/master/Picture/img9.png)

### 6.使用拍照、相册选择图片

> 在mine fragment中可以对头像通过相册和拍照可以进行选择。提示框选的是自定义的Dialog，使用了卡片布局，后面会替换成PopupWindow的形式。

![image](https://github.com/GZAY/Yichuguanjia2/blob/master/Picture/img4.png)
![image](https://github.com/GZAY/Yichuguanjia2/blob/master/Picture/img5.png)
![image](https://github.com/GZAY/Yichuguanjia2/blob/master/Picture/img7.png)
![image](https://github.com/GZAY/Yichuguanjia2/blob/master/Picture/img6.png)

### 7.使用轮播图

> 首页使用了一个轮播图进行图片展示。

![image](https://github.com/GZAY/Yichuguanjia2/blob/master/Picture/img1.png)

## 总结

这次花了一半的时间看第一行代码，感觉后面的还是有点吃力，Java在多线程方面的知识很多忘了，看的时候又要去查。在实际写代码的时候遇到一堆bug,换了好几种实现方式，有两个bug卡了好几天，网上也查不到，加的安卓学习群问了也没人知道，心态崩溃。最后居然把原来的app卸载了就好了。。
不过期间，学到了很多解决bug的方法，常见的报错都看的懂了，知道了生命周期的知识点是多么重要，好多null object的报错都是因为初始化的问题。
