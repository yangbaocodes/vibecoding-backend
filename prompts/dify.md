我需要封装一个第三方服务模块dify

文档地址：https://dify.aistudio.ltd/app/adefd7dd-9ec3-462c-a300-7ac4921014a8/develop

服务地址：http://dify.aistudio.ltd/v1
Bearer token: app-blj7fVtI82UlI0HWyberMKe4
接口路径：/workflows/run
方法：POST
接口名：getresumeinfo

请求体: {
"inputs": {
"resumeUrl": {
"type": "document",
"transfer_method": "remote_url",
"url": "https://talent-minio-dev.aistudio.ltd/ai-file/f3fc43e8-9b31-49d7-b818-ceba52182c5a.docx"
}
}}

返回：{
    "code": 0,
    "name": "韩建生",
    "englishName": "hhhh",
    "experienceSummary": [
        {
            "company": "上海极洲信息科技集团有限公司",
            "position": "Java 开发工程师",
            "startDate": "2025-03-01",
            "endDate": "2025-04-01"
        },
        {
            "company": "付临门支付有限公司",
            "position": "Java 开发组长",
            "startDate": "2015-05-01",
            "endDate": "2024-11-01"
        }
    ],
    "education": [
        {
            "type": "本科",
            "school": "江苏理工学院",
            "major": "计算机科学与技术",
            "startDate": "2010-09-01",
            "endDate": "2014-06-01"
        }
    ],
    "workYears": 10,
    "technologies": [
        "Java（10年）",
        "SpringCloud（熟练）",
        "SpringBoot（熟练）",
        "MySQL（熟练）",
        "Oracle（熟练）",
        "Redis（熟练）",
        "Kafka（熟练）",
        "Zookeeper（熟练）",
        "RabbitMQ（熟练）",
        "FastDFS（熟练）",
        "Elasticsearch（熟练）",
        "Jenkins（熟练）",
        "Netty（熟练）",
        "Kubernetes（熟练）",
        "Docker（熟练）",
        "XXL-JOB（熟练）",
        "Dubbo（熟练）",
        "Loki（熟练）",
        "Grafana（熟练）",
        "SkyWalking（熟练）"
    ],
    "personalAdvantage": "熟悉 JVM 内存管理，多线程、线程安全、异步处理等方案，具备多线程思考编码能力；熟练掌握 SpringCloud 全家桶 + SpringBoot 微服务开发框架，理解 RPC 等核心技术原理；熟练 Oracle、MySQL 数据库事务以及并发控制；熟悉 Redis、Kafka、Zookeeper、RabbitMQ、FastDFS、Jenkins、Dubbo、Elasticsearch、Loki、Netty、Kubernetes、Docker、XXL-JOB 等底层中间件及分布式技术。",
    "technicalSkills": {
        "tools": [
            "IDEA",
            "Eclipse",
            "Git",
            "Nginx",
            "Tomcat",
            "WebLogic",
            "Docker",
            "Kubernetes",
            "Jenkins",
            "SkyWalking",
            "Grafana",
            "Loki"
        ],
        "languages": [
            "Java"
        ],
        "platforms": [
            "Windows"
        ],
        "databases": [
            "MySQL",
            "Oracle",
            "Redis"
        ],
        "operatingSystems": [
            "Windows"
        ],
        "frameworks": [
            "Spring Boot",
            "Spring Cloud",
            "SSM",
            "SSH",
            "MyBatis",
            "MyBatis Plus"
        ]
    },
    "projectExperience": [
        {
            "organization": "上海极洲信息科技集团有限公司",
            "projects": [
                {
                    "projectTitle": "极洲科技商城",
                    "client": "",
                    "startDate": "2025-03-01",
                    "endDate": "2025-04-01",
                    "operatingSystem": "windows",
                    "toolOrTechnology": "springcloud+springboot+mybatisplus+mysql+html+redis+RocketMq+xxljob+k8s+es+jekenis",
                    "projectDescription": "本项目主要是一个在支付宝小程序的租赁手机的商城项目，商家可以在我们管理平台上架手机或者美容仪，消费者可以通过支付宝小程序进行手机或者美容仪的租赁，该系统风控比较严，对接了极洲风控，新网银行风控，蚂蚁链风控，通过风控后可以进行下单，使用支付宝进行支付，支付成功后进行电子签约，然后发货，该商品是分期商品，需要用户手动归还或者系统自动进行代扣绑定银行卡。",
                    "projectRole": "",
                    "projectResponsibilities": "优化老系统：新增消息队列、新增 ES、用户信息加密脱敏、电子合同的对接"
                }
            ]
        },
        {
            "organization": "付临门支付有限公司",
            "projects": [
                {
                    "projectTitle": "付掌门",
                    "client": "",
                    "startDate": "2022-10-01",
                    "endDate": "2024-11-01",
                    "operatingSystem": "windows",
                    "toolOrTechnology": "springcloud+springboot+mybatis+mysql+html+redis+RabbitMq+xxljob+fastdfs+loki+grafana+jekenis",
                    "projectDescription": "本项目主要是用于公司自己的展业项目，公司可以发布自己的分润政策，自定义自己的分润规则。客户端有首页模块，业绩模块，学堂模块，商城模块，我的模块,积分系统，营销活动等等。管理平台有系统管理模块，代理商模块，交易模块，终端模块，分润查询模块，权限模块，市场管理，商学院管理，商户中心等等模块。",
                    "projectRole": "",
                    "projectResponsibilities": "搭建 springboot,springcloud 框架，使用 skywalking 做链路追踪，使用 jekenis 自动化部署，使用 loki 做统一日志管理。苹果商店上架，活跃商户 50 万。"
                },
                {
                    "projectTitle": "聚合码",
                    "client": "",
                    "startDate": "2020-11-01",
                    "endDate": "2022-09-01",
                    "operatingSystem": "windows",
                    "toolOrTechnology": "Nginx+Redis+Tomcat 集群+SSM+rabbitmq+Oracle",
                    "projectDescription": "聚合码收款是为了方便商家收款而开发的，聚合码支付是一种聚合支付的形式，它集成了多家支付通道，使得聚合码可以同时支持被集成通道的支付工具扫码付款，可以同时支付微信、支付宝、QQ 和银联云闪付等等多家主流支付通道。用户通过一张码扫码支付，未使用的码可以通过码注册，注册过的码直接可以支付，用户的商户中心做在公众号里面，里面包括交易查询，结算管理，二维码管理，密码管理，云喇叭管理等等。",
                    "projectRole": "",
                    "projectResponsibilities": "负责 SSM 框架，带队开发各业务模块，主要工作涉及需求整理分析，表设计，公众号的开发。日交易量 7000 万。"
                },
                {
                    "projectTitle": "聚合码管理平台",
                    "client": "",
                    "startDate": "2020-09-01",
                    "endDate": "2022-09-01",
                    "operatingSystem": "windows",
                    "toolOrTechnology": "Nginx+Redis+Tomcat 集群+SSM+rabbitmq+Oracle",
                    "projectDescription": "该管理平台是为了方便用户查看交易，代理商查看数据，风控审核，清算对账，营销管理而开发的。该管理平台有系统管理，商户信息管理，清算管理，交易查询管理，风控管理等等大模块。",
                    "projectRole": "",
                    "projectResponsibilities": "负责 SSM 框架，带队开发各业务模块，主要工作涉及需求整理分析，表设计，以及清算模块开发。"
                },
                {
                    "projectTitle": "扫码支付核心",
                    "client": "",
                    "startDate": "2018-06-01",
                    "endDate": "2020-08-01",
                    "operatingSystem": "windows",
                    "toolOrTechnology": "Nginx+Redis+Tomcat 集群+SSM+rabbitmq+Oracle",
                    "projectDescription": "我们付临门是一家有支付牌照的支付公司，该项目主要的是提供支付接口，负责和银联网联的对接。有进件模块，下单模块，风控模块，支付模块，代付模块，清结算模块。支持 pos 的正反扫，码牌的正扫，和开放支付通道。",
                    "projectRole": "",
                    "projectResponsibilities": "日交易量 3 亿。"
                },
                {
                    "projectTitle": "云商宝 App",
                    "client": "",
                    "startDate": "2017-08-01",
                    "endDate": "2018-05-01",
                    "operatingSystem": "windows",
                    "toolOrTechnology": "Zookeeper 集群+Dubbo+Spring+Mybatis+Oracle",
                    "projectDescription": "本项目，主要是线下收单业务的管理的 App，面向群体为代理商和商户，让用户查询管理数据更为方便，同时让代理商推广业务和分润提现更为简单。",
                    "projectRole": "",
                    "projectResponsibilities": "各模块都有接触。"
                },
                {
                    "projectTitle": "收单管理平台",
                    "client": "",
                    "startDate": "2016-09-01",
                    "endDate": "2017-07-01",
                    "operatingSystem": "windows",
                    "toolOrTechnology": "Nginx+Redis+Tomcat 集群+SSM+rabbitmq+Oracle",
                    "projectDescription": "本项目，主要是公司线下收单业务的一个业务管理平台，具体包括代理商模块，商户模块，终端模块，清结算模块，分润模块，以及各类主模块衍生出的一系列统计查询等模块。",
                    "projectRole": "",
                    "projectResponsibilities": "各模块都有接触。"
                },
                {
                    "projectTitle": "银嘉商城",
                    "client": "",
                    "startDate": "2015-06-01",
                    "endDate": "2016-08-01",
                    "operatingSystem": "windows",
                    "toolOrTechnology": "Nginx+Redis+Tomcat 集群+SSH+Oracle",
                    "projectDescription": "为了方便用户购物而开发的购物系统，店铺模块，商品模块，权限模块，对账模块，营销活动模块等等。本项目自己独立完成前台的页面开发，后台的业务逻辑的开发，我参与店铺模块。商品模块和营销活动模块。",
                    "projectRole": "",
                    "projectResponsibilities": "独立完成前台页面开发和后台业务逻辑开发，参与店铺模块、商品模块和营销活动模块。"
                }
            ]
        }
    ],
    "certificationsAndTraining": [
        {
            "name": "PMP",
            "issuer": "Project Management Association",
            "issueDate": "2023-01-01"
        }
    ],
    "professionalAchievements": [
        {
            "name": "XXXX发明专利",
            "issuer": "Project Management Association",
            "issueDate": "2023-01-01"
        }
    ]
}