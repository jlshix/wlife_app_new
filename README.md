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
