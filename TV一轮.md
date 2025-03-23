---
title: TV一轮
language_tabs:
  - shell: Shell
  - http: HTTP
  - javascript: JavaScript
  - ruby: Ruby
  - python: Python
  - php: PHP
  - java: Java
  - go: Go
toc_footers: []
includes: []
search: true
code_clipboard: true
highlight_theme: darkula
headingLevel: 2
generator: "@tarslib/widdershins v4.0.29"

---

# TV一轮

Base URLs:

# Authentication

- HTTP Authentication, scheme: bearer

# NantingStoreExplore

## POST 用户注册

POST /api/users/register

> Body 请求参数

```json
"{\r\n    \"name\": \"Legendpei666\",\r\n    \"password\": \"114514\",\r\n    \"email\": \"1919180@sohu.com\",\r\n    \"phone\": \"71739123228\",\r\n    \"address\": \"广东省 广州市 番禺区 华南交通大学 门口天桥下\",\r\n    \"age\": \"19\",\r\n    \"gender\": \"男\",\r\n    \"role\": [\r\n        普通用户\r\n    ]\r\n}"
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|body|body|object| 否 ||none|
|» name|body|string| 是 | 用户名|不为空|
|» password|body|string| 是 | 密码|不为空|
|» email|body|string¦null| 否 | 邮箱|none|
|» phone|body|string| 是 | 电话|不为空|
|» address|body|string¦null| 否 | 地址|none|
|» age|body|string¦null| 否 | 年龄|none|
|» gender|body|string¦null| 否 | 性别|none|
|» role|body|[string]¦null| 否 | 角色|none|

> 返回示例

> 200 Response

```json
{
  "code": 200,
  "message": "User registered successfully",
  "data": {
    "userId": 1
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none||none|
|» message|string|true|none||none|
|» data|object|true|none||none|
|»» userId|integer|true|none||none|

## DELETE 取消评论点赞

DELETE /api/comments/{commentId}/like

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|commentId|path|string| 是 ||none|

> 返回示例

> 200 Response

```json
{
  "massage": "string"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» massage|string|true|none|信息|信息|

## POST 评论点赞

POST /api/comments/{commentId}/like

给评论点赞，需要验证

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|commentId|path|string| 是 ||none|

> 返回示例

> 200 Response

```json
{
  "massage": "string"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» massage|string|true|none|信息|信息|

## POST 用户登录

POST /api/users/login

> Body 请求参数

```json
{
  "name": "奈蒙",
  "password": "JTJyhbjhUfrcd_o"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|body|body|object| 否 ||none|
|» name|body|string| 是 | 用户名|ID 编号|
|» password|body|string| 是 | 密码|密码|

> 返回示例

> 200 Response

```json
{
  "code": 200,
  "message": "Login successful",
  "data": {
    "token": "JWT_TOKEN_HERE"
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none||none|
|» message|string|true|none|响应信息|none|
|» data|object|true|none|数据|none|
|»» token|string|true|none|Token|Token中包含subject(user:{id}),role,|

## DELETE 删除评论

DELETE /api/shops/{shopId}/comments/{commentId}

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|shopId|path|string| 是 ||none|
|commentId|path|number| 是 ||none|

> 返回示例

> 200 Response

```json
{
  "massage": "string"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» massage|string|true|none|信息|信息|

## GET 获取用户信息

GET /api/users/me

> 返回示例

> 200 Response

```json
{
  "likesNum": 0,
  "commentsNum": 0
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» likesNum|number|true|none|点赞数|none|
|» commentsNum|number|true|none|评论数|none|

## DELETE 取消商铺点赞

DELETE /api/shops/{shopId}/like

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|shopId|path|string| 是 ||none|

> 返回示例

> 200 Response

```json
{
  "massage": "string"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» massage|string|true|none|信息|信息|

## GET 点赞商铺

GET /api/shops/{shopId}/like

点赞商铺，需要验证身份

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|shopId|path|string| 是 ||none|

> 返回示例

> 200 Response

```json
{
  "message": "string"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» message|string|true|none||none|

## GET 详细点赞记录

GET /api/users/me/likes

返回点赞记录列表，根据token中的user:id获取用户点赞记录

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "message": "string",
  "data": {
    "userId": 0,
    "userName": "string",
    "totalLikes": 0,
    "likes": [
      {
        "id": 0,
        "userId": 0,
        "targetId": 0,
        "targetType": "string",
        "targetName": "string",
        "userName": "string",
        "createTime": "string"
      }
    ]
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none||none|
|» message|string|true|none||none|
|» data|object|true|none||none|
|»» userId|integer|true|none||none|
|»» userName|string|true|none||none|
|»» totalLikes|integer|true|none||none|
|»» likes|[object]|true|none||none|
|»»» id|integer|true|none||none|
|»»» userId|integer|true|none||none|
|»»» targetId|integer|true|none||none|
|»»» targetType|string|true|none||none|
|»»» targetName|string|true|none||none|
|»»» userName|string|true|none||none|
|»»» createTime|string|true|none||none|

## GET 获取详细评论信息

GET /api/users/me/comments

返回用户自己的评论记录列表，根据token中的user:id获取用户评论记录

> 返回示例

> 200 Response

```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "userId": 10086,
    "userName": "contrueCT",
    "totalComments": 3,
    "comments": [
      {
        "id": 1001,
        "userId": 10086,
        "targetId": 5001,
        "userName": "contrueCT",
        "targetName": "C++炒面",
        "content": "这家店的炒面非常正宗，下次还会再来！",
        "createTime": "2025-03-20T14:30:25"
      },
      {
        "id": 1002,
        "userId": 10086,
        "targetId": 5002,
        "userName": "contrueCT",
        "targetName": "Java炒粉",
        "content": "物美价廉，份量十足，是我的常去之处。",
        "createTime": "2025-03-21T18:15:32"
      },
      {
        "id": 1003,
        "userId": 10086,
        "targetId": 5003,
        "userName": "contrueCT",
        "targetName": "python炒饭",
        "content": "食材简单朴素，但是味道很好，就是出餐有点慢。",
        "createTime": "2025-03-22T02:45:18"
      }
    ]
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none||none|
|» message|string|true|none||none|
|» data|object|true|none||none|
|»» userId|integer|true|none||none|
|»» userName|string|true|none||none|
|»» totalComments|integer|true|none||none|
|»» comments|[object]|true|none||none|
|»»» id|integer|true|none||none|
|»»» userId|integer|true|none||none|
|»»» targetId|integer|true|none||none|
|»»» userName|string|true|none||none|
|»»» targetName|string|true|none||none|
|»»» content|string|true|none||none|
|»»» createTime|string|true|none||none|

## GET 获取商铺列表

GET /api/shops

获取商铺列表，无需登录验证，可选排序规则（点赞/评论）

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|sort|query|string| 否 ||排序规则，按点赞数或评论数排序|

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "message": "string",
  "data": {
    "totalStores": 0,
    "stores": [
      {
        "id": 0,
        "name": "string",
        "address": "string",
        "shortDescription": "string",
        "likesCount": 0,
        "commentsCount": 0
      }
    ]
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none||none|
|» message|string|true|none||none|
|» data|object|true|none||none|
|»» totalStores|integer|true|none||none|
|»» stores|[object]|true|none||none|
|»»» id|integer|true|none||none|
|»»» name|string|true|none||none|
|»»» address|string|true|none||none|
|»»» shortDescription|string|true|none||none|
|»»» likesCount|integer|true|none||none|
|»»» commentsCount|integer|true|none||none|

## GET 获取商铺详细信息

GET /api/shops/{shopId}

获取商铺详细信息，无需登录验证

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|shopId|path|string| 是 ||none|

> 返回示例

> 200 Response

```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "store": {
      "id": 1001,
      "name": "C++炒面",
      "address": "广东工业大学西生活区门口",
      "phone": "020-12345678",
      "description": "创立于2005年的C++炒面，是广工最著名的美食之一。店主毕业于计算机科学专业，将编程思维融入烹饪技艺中，创造出了独具特色的'指针炒面'、'多态煎蛋'等招牌菜品。每一份炒面都如同精心设计的代码，层次分明，结构严谨。无论是初学者还是'老程序员'都能在这里找到属于自己的美食记忆。",
      "likesCount": 156,
      "commentsCount": 42,
      "comments": [
        {
          "userId": 10089,
          "userName": "CodeMaster",
          "content": "他家的指针炒面太好吃了！里面的鸡蛋就像是完美封装的对象，一口下去就能感受到美味的继承关系！",
          "createTime": "2025-03-22T05:30:12"
        },
        {
          "userId": 10086,
          "userName": "contrueCT",
          "content": "每次考试挂科后我都会来这里吃一碗炒面压压惊，感觉比面向对象编程还好理解，五星推荐！",
          "createTime": "2025-03-21T18:24:36"
        },
        {
          "userId": 10090,
          "userName": "AlgorithmGirl",
          "content": "老板炒面的手法就像执行排序算法一样行云流水，而且价格实惠，学生党福音！",
          "createTime": "2025-03-21T12:15:48"
        },
        {
          "userId": 10091,
          "userName": "DebugKing",
          "content": "这家店的调料是独家秘方，每一口都能尝到不同的层次，就像是一个精心设计的递归函数！",
          "createTime": "2025-03-20T09:34:27"
        },
        {
          "userId": 10092,
          "userName": "WebDeveloper",
          "content": "饭点人有点多，需要排队，但绝对值得等待！炒面的味道堪比成功编译后运行的快感！",
          "createTime": "2025-03-19T20:12:05"
        }
      ],
      "storeOwners": [
        {
          "userId": 8001,
          "userName": "CppMaster",
          "role": "店主",
          "joinTime": "2005-03-15T08:30:00"
        },
        {
          "userId": 8002,
          "userName": "DataStructure",
          "role": "厨师长",
          "joinTime": "2010-05-20T09:15:30"
        }
      ]
    }
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none||none|
|» message|string|true|none||none|
|» data|object|true|none||none|
|»» store|object|true|none||none|
|»»» id|integer|true|none||none|
|»»» name|string|true|none||none|
|»»» address|string|true|none||none|
|»»» phone|string|true|none||none|
|»»» description|string|true|none||none|
|»»» likesCount|integer|true|none||none|
|»»» commentsCount|integer|true|none||none|
|»»» comments|[object]|true|none||none|
|»»»» userId|integer|true|none||none|
|»»»» userName|string|true|none||none|
|»»»» content|string|true|none||none|
|»»»» createTime|string|true|none||none|
|»»» storeOwners|[object]|true|none||none|
|»»»» userId|integer|true|none||none|
|»»»» userName|string|true|none||none|
|»»»» role|string|true|none||none|
|»»»» joinTime|string|true|none||none|

## POST 评论商铺

POST /api/shops/{shopId}/comments

评论商铺，需要验证身份

> Body 请求参数

```json
{
  "comment": "string"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|shopId|path|string| 是 ||none|
|body|body|object| 否 ||none|
|» comment|body|string| 是 | 评论|none|

> 返回示例

> 200 Response

```json
{
  "massage": "string"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» massage|string|true|none||none|

# 数据模型

