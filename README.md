# DMHY-Java
动漫花园爬虫

![动漫花园网站](http://static.zybuluo.com/NEGAI/jrucvisgdgq8gg7dco1cnnhp/image_1b8jgs6v0cblpkumbvimdill9.png)


爬取的结果

![本地目录](http://static.zybuluo.com/NEGAI/pxa0pn883ddi5v1fuh2zvltr/image_1b8jh33s03g73j01dsa179vgvum.png)

![二级目录](http://static.zybuluo.com/NEGAI/g7lg8tszzo6naetwueu2vdo5/image_1b8jieeh2nc1qqp1fbo1g5t1cql13.png)

##爬虫说明
由于动漫花园的二级页面总是报`Connection reset`异常，所以程序只能抓取首页信息，爬虫会抓取每个页面上的分类、标题和磁力链接，然后按照初始化时给的目录地址，在目录下创建以标题命名的二级目录，最后将磁力链接写入二级目录下的`torrent.txt`文件中。演示图片中只测试抓取了1000页的内容，动漫花园首页总共3800多页，可以在主方法中修改`for`循环语句的循环控制变量选择需要下载的页面。运行中会显示当前下载页面位置。
该版本属于测试版本，陆续会修改一些方法和进一步封装。
