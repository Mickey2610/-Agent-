package com.mickey.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PersonalTool {
    @Tool(description = "如果你不认识这个人是谁，请来这个工具看看有没有信息")
    public String getPersonalInfo(String name) {

        log.info("========== 调用MCP工具：getPersonalInfo() ==========");
        log.info(String.format("========== 参数 name：%s ==========", name));

        if (name == "米琦"||name == "Mickey") {}
        String info = "个人信息\n" +
                "姓名：米琦\n" +
                "政治面貌：中共党员\n" +
                "应聘岗位：大模型应用开发工程师\n" +
                "手机：15348482610\n" +
                "邮箱：704625068@qq.com\n" +
                "生日：2001.11.3\n" +
                "籍贯：湖南怀化\n" +
                "\n" +
                "教育背景\n" +
                "中国科学院大学与福建农林大学联合培养\n" +
                "2023.9.～2026.7\n" +
                "人工智能\n" +
                "硕士研究生\n" +
                "GPA：3.49/5\n" +
                "研究方向：深度学习、计算机视觉、目标检测\n" +
                "学术成果：\n" +
                "国家发明专利《一种基于YOLO11s改进的无人机小目标检测模型建立检测方法》（已授权，QZP51872，二作/导师一作）    \n" +
                "荣誉证书：连续两年中国科学院福建物质结构研究所学业奖学金\n" +
                "职位：连续三年班级班长\n" +
                "中南林业科技大学\n" +
                "2019.9～2023.7\n" +
                "电子信息工程\n" +
                "本科\n" +
                "GPA：3.68/5\n" +
                "主修课程：编程基础（JAVA）、C语言、计算机架构、操作系统与并行系统、数据网络与分布式系统、数字电路与设计、模拟电路、通信系统、信号处理\n" +
                "荣誉证书：两年校奖学金、优秀班干部、优秀共青团干部、国家励志奖学金、优秀毕业生\n" +
                "资格证书：全国计算机二级（WPS）、英语六级497分、普通话二级甲等、C1驾驶证\n" +
                "职位：学院团委副书记\n" +
                "\n" +
                "实习经历\n" +
                "中国科学院泉州装备研究所 · 多媒体计算与物联网实验室\n" +
                "2024.7～2026.7\n" +
                "面向无人机平台的小目标检测算法研究 \n" +
                "研究内容：在Linux环境下基于RTX 3090 GPU优化YOLOv8s和YOLO11s算法，在大型公开的VisDrone-DET2019数据集上，高精度导向改进模型YOLOv8s-SPDX提升15.2%的mAP@0.5，轻量化导向模型YOLO11s-UAV提升7.8%的mAP@0.5，同时模型参数量减少55.3%，实现高效无人机小目标检测。目前已授权一篇国家发明专利，另外有两篇论文在投\n" +
                "福厦泉智能制造装备多源信息感知与AI识别平台  \n" +
                "2024.1～2025.10\n" +
                "技术栈：Linux · Docker · Traefik · TypeScript · 微服务架构 · 数据标注 · 模型训练与推理\n" +
                "工作内容：  \n" +
                "1. AI平台容器化部署  \n" +
                "   - 基于Docker Compose部署Ymir（模型训练/推理平台）、Label-Free（数据标注平台）和Redis、MangoDB等中间介，构建前后端分离微服务系统  \n" +
                "   - 集成Traefik网关实现容器服务动态路由与负载均衡，优化API通信效率  \n" +
                "2. 平台功能修复\n" +
                "   - 重构前端TypeScript视图组件，修复前端对后端数据响应格式的异常渲染，成功修复了语义分割和目标检测两大任务指标，封装了项目需求中的额外的Recall、ACC、F1-score等评价指标\n" +
                "3. AI模型全流程交付\n" +
                "   - 完成200张图像数据清洗、VOC格式标注及数据集划分，优化YOLOv7模型的训练参数，实现检测模型各项精度指标均≥90%，验证了AI平台的可行性和全流程性\n" +
                "\n" +
                "项目经历\n" +
                "基于AI大模型的企业级面试管理智能体应用  \n" +
                "2025.2～2025.6\n" +
                "技术栈：Vue3 · Spring Boot · DeepSeek · RAG · Redis-stack · SearXNG · MINIO · Spring AI · MySQL · MyBatis-Plus\n" +
                "工作内容：  \n" +
                "1. 开发智能面试系统\n" +
                "   - 前后端分离开发：后端基于Spring Boot+JWT构建安全登录认证体系，前端通过Vue3框架实现网页端和手机APP端双向交互，APP端，网页前端，后端分别使用HBuilder、VSCode、IntelliJ IDEA进行调试\n" +
                "   - 使用Docker集成中间件：利用Redis-stack集成Spring Cache技术降低数据库的负载，集成MINIO云存储多媒体数据，使用MySQL和MyBatis Plus进行数据库管理和写入，Apache Echarts和POI实行面试结果可视化和Excel导出  \n" +
                "2. 集成AI语言大模型\n" +
                "   - 开发AI面试官模块：基于Spring AI架构，调用百度语音SDK实时转译答题语音。实行Prompt工程，通过DeepSeek大模型智能评估应聘者作答质量，给出AI面试报告\n" +
                "   - 搭建RAG知识库：集成智能搜索界面，支持各类格式的文件上传进行知识库检索与SearXNG联网搜索，可绑定任意第三方搜索引擎，为面试官提供丰富的信息检索渠道\n" +
                "   - 开发MCP插件生态：通过自然的聊天对话实现管理员邮件收发、AI面试题库生成、面试系统所有数据库操作自动化的功能。\n" +
                "基于AI大模型的辅助编程智能体\n" +
                "2025.6～2025.9\n" +
                "技术栈：LangChain · LangGraph · MCP · Docker · Lima · 多智能体协同 · 私有化工具开发 \n" +
                "工作内容：  \n" +
                "1. 多智能体协同架构设计\n" +
                "   - 基于LangGraph构建规划、开发与测试三层Agent系统，实现复杂任务的分解与协同执行  \n" +
                "   - 集成LangChain实现对话记忆、工具调用等核心能力，支持多轮技术需求澄清与方案优化  \n" +
                "2. 私有化工具生态开发\n" +
                "   - 通过MCP协议开发20+工具（代码分析器、API生成器等），支持SSE/HTTP/Stdio三种通信模式  \n" +
                "   - 实现浏览器实时编程协作、知识库智能检索、沙盒环境控制等企业级功能  \n" +
                "3. 安全沙盒环境构建\n" +
                "   - 采用Lima+Docker搭建隔离开发环境，集成Nginx/MySQL/MongoDB等基础设施  \n" +
                "   - 实现AI生成代码的自动部署与预览，真正具备商业化落地的核心要素\n" +
                "\n" +
                "专业技能\n" +
                "英语六级497，有良好的英文阅读能力，无障碍使用各种国外专业软件和github开源项目\n" +
                "熟悉Java和Python后端开发，能够使用SpringBoot+SpringAI框架进行开发，并基于Langchain+LangGraph实现智能体Agent功能。掌握Postman接口测试工具，具备前后端分离开发测试经验。\n" +
                "了解Vue框架和TypeScript基础语法，能借助AI工具生成代码并根据业务需求完成界面定制。\n" +
                "具备机器学习和深度学习基础，有目标检测算法项目经验，熟悉从数据标注到模型训练推理的全流程。\n" +
                "熟练使用Linux系统，掌握Docker容器化部署技术。\n" +
                "在校期间担任宣传部部长，精通Photoshop设计软件，对前端UI设计有良好的审美能力。\n" +
                "有良好的技术博客维护习惯，精通Markdown，Latex和JupyterNotebook等文本语言，在CSDN拥有6000+的粉丝量，文章点赞收藏量均1000+，博客名：MickeyCV";
        return info;
    }
}
