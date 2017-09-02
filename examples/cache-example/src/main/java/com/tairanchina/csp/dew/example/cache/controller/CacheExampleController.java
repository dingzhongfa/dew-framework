package com.tairanchina.csp.dew.example.cache.controller;

import com.tairanchina.csp.dew.example.cache.dto.CacheExampleDTO;
import com.tairanchina.csp.dew.example.cache.service.CacheExampleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CacheExampleController {

    private static final Logger logger = LoggerFactory.getLogger(CacheExampleController.class);

    @Autowired
    private CacheExampleService cacheExampleService;

    @GetMapping("/exe")
    public void exe() {
        logger.info("=======Save=======");
        CacheExampleDTO dto = cacheExampleService.save(new CacheExampleDTO().setId("1").setName("泰然城"));
        assert dto.getId().equals("1");
        // Cache hit!
        dto = cacheExampleService.getById("1");
        assert dto.getName().equals("泰然城");

        logger.info("=======Update=======");
        cacheExampleService.update("1", dto.setName("泰然集团"));
        // Cache hit!
        dto = cacheExampleService.getById("1");
        assert dto.getName().equals("泰然集团");

        logger.info("=======Delete=======");
        cacheExampleService.delete("1");
        dto = cacheExampleService.getById("1");
        assert dto == null;

        logger.info("=======Find=======");
        cacheExampleService.save(new CacheExampleDTO().setId("2").setName("泰然城"));
        cacheExampleService.save(new CacheExampleDTO().setId("3").setName("蜂贷"));
        List<CacheExampleDTO> dtos = cacheExampleService.find();
        assert dtos.size() == 2;
        // Cache hit!
        dtos = cacheExampleService.find();
        assert dtos.size() == 2;
        cacheExampleService.deleteAll();
        dtos = cacheExampleService.find();
        assert dtos.size() == 0;
    }

}
