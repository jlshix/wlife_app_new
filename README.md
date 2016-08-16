# WLife_v03
WLife_v02 项目重构

## 2016-07-11

- java文件分类管理
    - activity
    - fragment
    - adapter
    - data
    - util

- xUtils 跟findViewById & volley 说再见

- WelcomeActivity逻辑更改
    - 直接跳转至LoginActivity判断
    - 入口统一

- 去除CircularProgressButton
    - 使用ProgressDialog
    - 使用Snackbar

- LoginActivity 去除Card

- RegisterActivity 去除Card

## 2016-07-12

- MainActivity参考ZigSys的设计

- bindGateActivity 参考ZigSys的设计
    - todo 添加按照房间选择

## 2016-07-16

- addDeviceActivity 建成,需要重新设计

- 登录流程优化 使用ProgressDialog

- 确定在ZigSys后台和数据库基础上修改

- Fragment Item 新增上下文菜单,操作逻辑更明显

- Msg 由 Fragment 更改为 Activity 并增加筛选功能

- 设置页面使用 PreferenceScreen 原生设计

## 2016-07-18

- 二维码绑定网关功能已完成 绑定设备待定

- 更改数据库 新增sign 与 place字段

- 设备icon可根据数据库更改颜色 待完成分布图

## 2016-07-19

- 实现布局的动态加载与分布图的设备位置显示

- 设置页面完成大半 新增右滑返回

- 优化登录和解绑流程

## 2016-07-20

- 实现天气的位置选择与状态查看

## 2016-07-22

- 实现家庭留言板功能

## 2016-07-23

- addDeviceActivity 完善
- 新增添加命令的Activity 待转成Dialog

## 2016-07-24

- 新增MemberActivity实现家庭成员管理
- 新增AccountFragment实现个人账号管理
- 新增语音识别 待增加解析规则

## 2016-07-25

- 新增 VoiceListActivity 实现语音命令管理
- 新增 AddOrderActivity 实现语音命令或模式的添加

## 2016-07-26

- 更改 colorPrimary
- 修复 NestedScrollVIew 与 RecyclerView 嵌套时滑动不流畅的问题
- 更改 color 新增沉浸式状态栏
- 新增 MOdeActivity 用于快捷设置模式


## 2016-07-27

- 新增图标 优化界面
- 优化登录逻辑

## 2016-08-02

- 新增用户反馈

## 2016-08-07

- 新增家电相关 Fragment 及 Adapter data
- 新增 addApplianceActivity
- 实现上下文菜单 可重命名 删除 更改位置

## 2016-08-08

- 集成环信 armeabi-v7a 问题已解决

## 2016-08-09

- 更改邮箱为手机号 验证码待定

## 2016-08-10

- 指定部分父活动
- 新增收到推送的跳转

# 2016-08-13

- 新增温湿度光照统计 正在等待数据源

# 2016-08-14

- 新增温湿度光照统计 待解决刷新

## 2016-08-16

- 温湿度光照统计功能完成



## TODO

- schedule list and add
