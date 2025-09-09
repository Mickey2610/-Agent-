package com.mickey.controller.rag;

import com.mickey.Entity.ChatEntity;
import com.mickey.grace.result.GraceJSONResult;
import com.mickey.grace.result.ResponseStatusEnum;
import com.mickey.service.rag.ChatService;
import com.mickey.service.rag.DocumentService;
import com.mickey.utils.LeeResult;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName HelloController
 * @Author 风间影月
 * @Version 1.0
 * @Description HelloController
 **/
@Slf4j
@RestController
@RequestMapping("rag")
public class RagController {

    @Resource
    private DocumentService documentService;

    @Resource
    private ChatService chatService;

    @PostMapping("/uploadRagDoc")
    public GraceJSONResult uploadRagDoc(@RequestParam("file") MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        List<Document> documentList = new ArrayList<>();
        if (originalFilename != null) {
            String fileExtension = originalFilename.
                    substring(originalFilename.
                            lastIndexOf(".") + 1)
                    .toLowerCase();

            if (fileExtension.equals("txt")) {
                 documentList= documentService
                        .loadTxtText(file.getResource(), file.getOriginalFilename());
            } else {
                documentList = documentService.loadOtherText(file);
            }
            log.info("Document text: {}", documentList);
            return GraceJSONResult.ok(documentList);
        }
        return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO);
    }

    /**
     * 执行纯向量相似度搜索
     * 1. 将用户查询编码为向量
     * 2. 在Redis向量库中执行k-NN搜索
     * 3. 返回Top-K相关文档（未经过LLM处理）
     */
    @GetMapping("/doSearch")
    public LeeResult doSearch(@RequestParam String question) {
        return LeeResult.ok(documentService.doSearch(question));
    }

    /**
     * 执行完整RAG流程：
     * 1. Retrieval：同doSearch获取相关文档
     * 2. Augmentation：将文档注入prompt上下文
     * 3. Generation：调用大模型生成最终回答
     */
    @PostMapping("/search")
    public void search(@RequestBody ChatEntity chatEntity, HttpServletResponse response) {
        log.info("✅ Controller方法已触发：/rag/search");
        List<Document> list = documentService.doSearch(chatEntity.getMessage());
        response.setCharacterEncoding("UTF-8");
        chatService.doChatRagSearch(chatEntity, list);
    }

}
