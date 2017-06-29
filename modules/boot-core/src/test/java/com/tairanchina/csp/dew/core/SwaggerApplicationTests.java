package com.tairanchina.csp.dew.core;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import io.github.robwin.markup.builder.MarkupLanguage;
import io.github.robwin.swagger2markup.GroupBy;
import io.github.robwin.swagger2markup.Swagger2MarkupConverter;
import springfox.documentation.staticdocs.SwaggerResultHandler;
import springfox.documentation.swagger2.web.Swagger2Controller;

@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@RunWith(SpringRunner.class)
@SpringBootTest
public class SwaggerApplicationTests {

	 private String snippetDir = "target/generated-snippets";
	 private String outputDir = "target/asciidoc";
	 
	 @Autowired
	 private MockMvc mockMvc;
	 
	 @After
	    public void Test() throws Exception{
	        // 得到swagger.json,写入outputDir目录中
	        mockMvc.perform(get(Swagger2Controller.DEFAULT_URL).accept(MediaType.APPLICATION_JSON))
	                .andDo(SwaggerResultHandler.outputDirectory(outputDir).build())
	                .andExpect(status().isOk())
	                .andReturn();

	        // 读取上一步生成的swagger.json转成asciiDoc,写入到outputDir
	        // 这个outputDir必须和插件里面<generated></generated>标签配置一致
	        Swagger2MarkupConverter.from(outputDir + "/swagger.json")
	                .withPathsGroupedBy(GroupBy.TAGS)// 按tag排序
	                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)// 格式
	                .withExamples(snippetDir)
	                .build()
	                .intoFolder(outputDir);// 输出
	    }
	 
	 /**
	  * 如果不需要每个接口的返回结果，那直接运行测试类，就行，不需要走每个测试。
	  * @throws Exception
	  */
	@Test
	public void contextLoads() throws Exception {
//		 mockMvc.perform(get("/student1").param("name", "xxx")
//	                .accept(MediaType.APPLICATION_JSON))
//	                .andExpect(status().isOk())
//	                .andDo(MockMvcRestDocumentation.document("getStudent", preprocessResponse(prettyPrint())));
//
////	        Student student = new Student();
////	        student.setName("xxx");
////	        student.setAge(23);
////	        student.setAddress("湖北麻城");
////	        student.setCls("二年级");
////	        student.setSex("男");
//
//	        mockMvc.perform(post("/addStudent1").contentType(MediaType.APPLICATION_JSON) //addStudent1 为请求地址，文档中接口名
//	                .content(JSON.toJSONString("？")) // ？ 放置对象参数
//	                .accept(MediaType.APPLICATION_JSON))
//	                .andExpect(status().is2xxSuccessful())
//	                .andDo(MockMvcRestDocumentation.document("addStudent", preprocessResponse(prettyPrint()))); //addStudent为文档中接口方法名
	}

}
