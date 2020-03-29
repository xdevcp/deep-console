```
@description 用户管理 接口详情 示例
@author  wu
@version 1.1 update on 2019-12-10
@log  1.0->1.1 加请求方式 get/post/...
```

**目录**


[TOC]

---

## 8.1 用户管理

#### 8.1.1.1 用户查询接口

- 接口描述 `用户查询`

- 测试地址 `/manager/user/query`

- 请求方式 `get`

- 请求参数

|参数              |类型       |必需|描述
|------------------|-----------|----|------------
|token             |string     | 是 | 用户token
|account           |string     | 否 | 用户账号

> 参数备注：account用户账号为系统登陆账号

- 返回参数

|参数               |类型       |必需|描述
|-------------------|-----------|----|------------
|id                 | string    | 是 | 用户ID
|account	        | string    | 是 | 用户账号
|accountName        | string    | 是 | 用户昵称
|email              | string    | 是 | 邮箱
|profilePhoto       | string    | 是 | 头像
|status             | string    | 是 | 账号状态
|passwordErrorCount | string    | 是 | 密码错误次数
|mobile             | string    | 是 | 手机号
|genealogyCode      | string    | 是 | 组织结构代码
|organizationId     | string    | 是 | 组织ID

- 示例 (用postman测试，拷贝出cURL)

```
Example Request
--------------------------
curl -X GET http://127.0.0.1:6868/manager/user/query?token=123&account=admin

Example Response - success
--------------------------
{
    "message": "SUCCESS",
    "code": 1,
    "codeName": "SUCCESS",
    "data": {
        "records": [
            {
                "id": 1,
                "account": "admin",
                "accountName": "测试账户",
                "email": "admin@qq.com",
                "profilePhoto": null,
                "status": 1,
                "passwordErrorCount": null,
                "mobile": null,
                "genealogyCode": "system/sys1/",
                "organizationId": 0
            }
        ],
        "total": 1,
        "size": 20,
        "current": 1,
        "orders": [],
        "searchCount": true,
        "pages": 1
    }
}
```

#### 8.1.1.2 用户新增操作接口

- 接口描述 `用户新增`

- 测试地址 `/manager/user/add`

- 请求方式 `post`

- 请求参数

|参数               |类型       |必需|描述
|-------------------|-----------|----|------------
registerType        | string    | 是 | 注册方式 0账户名 1手机 2邮箱
account             | string    | 否 | 登录账号
accountName         | string    | 否 | 用户昵称
email               | string    | 否 | 邮箱
mobile              | string    | 否 | 手机号
organizationId      | string    | 否 | 组织ID
genealogyCode       | string    | 否 | 组织结构代码

> 参数备注：registerType必带

- 返回参数

|参数               |类型       |必需|描述
|-------------------|-----------|----|------------
id                  | string    | 是 | 用户ID
account             | string    | 是 | 用户账号
accountName         | string    | 是 | 用户昵称
email               | string    | 是 | 邮箱
profilePhoto        | string    | 是 | 头像
status              | string    | 是 | 账号状态
mobile              | string    | 是 | 手机号
genealogyCode       | string    | 是 | 组织结构代码
organizationId      | string    | 是 | 组织ID

- 示例

```
Example Request
--------------------------
curl -X POST \
  http://127.0.0.1:6868/manager/user/add \
  -H 'Content-Type: application/json' \
  -d '{
    "registerType": 1,
    "account": "",
    "mobile": "13512430412",
    "email": "test@qq.com",
    "organizationId": "1",
    "genealogyCode": "/a/b/c"
}'

Example Response - success
--------------------------
{
    "message": "SUCCESS",
    "code": 1,
    "codeName": "SUCCESS",
    "data": {
        "id": 13,
        "account": "111",
        "accountName": null,
        "email": "test@qq.com",
        "profilePhoto": null,
        "status": 1,
        "mobile": "13512430412",
        "genealogyCode": "/a/b/c",
        "organizationId": 1
    }
}

Example Response - fail
--------------------------
{
    "message": "账号已存在",
    "code": 14010,
    "codeName": "ACCOUNT_ALREADY_EXIST",
    "data": null
}
```
