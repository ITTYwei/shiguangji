package com.xiaowei.shiguangji.kv.biz;

import com.xiaowei.shiguangji.kv.biz.domain.dataobject.NoteContentDO;
import com.xiaowei.shiguangji.kv.biz.domain.repository.NoteContentRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

/**
 * @author: 魏玉石
 * @data: 2026/1/20
 * @description:
 */
@SpringBootTest
@Slf4j
public class CassandraTests {
    @Resource
    private NoteContentRepository noteContentRepository;
    @Test
    public void test() {
        log.info("测试");
        NoteContentDO contentDO = NoteContentDO.builder().id(UUID.randomUUID()).content("测试").build();
        noteContentRepository.insert(contentDO);

        List<NoteContentDO> all = noteContentRepository.findAll();
        log.info("查询结果：{}", all);
    }

}
