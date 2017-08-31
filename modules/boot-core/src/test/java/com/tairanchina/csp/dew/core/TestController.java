package com.tairanchina.csp.dew.core;

import com.ecfront.dew.common.Resp;
import com.ecfront.dew.common.StandardCode;
import com.tairanchina.csp.dew.core.validation.IdNumber;
import com.tairanchina.csp.dew.core.validation.Phone;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@Api(value = "测试", description = "test")
@RequestMapping(value = "/test/")
public class TestController {

    @GetMapping(value = "t")
    @ApiOperation(value = "fun1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "q", value = "query", paramType = "query", dataType = "string", required = true),
    })
    public Resp<String> t1(@RequestParam String q) {
        return Resp.success("successful");
    }

    @GetMapping(value = "t2")
    @ApiOperation(value = "fun2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "q", value = "query", paramType = "query", dataType = "string", required = true),
    })
    public Resp<String> t2(@RequestParam String q) {
        return Resp.badRequest("badrequest");
    }

    @GetMapping(value = "t3")
    @ApiOperation(value = "fun3")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "q", value = "query", paramType = "query", dataType = "string", required = true),
    })
    public String t3(@RequestParam String q) throws Exception {
        throw Dew.E.e("A000", new Exception("io error"));
    }

    @GetMapping(value = "t4")
    @ApiOperation(value = "fun4")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "q", value = "query", paramType = "query", dataType = "string", required = true),
    })
    public String t4(@RequestParam String q) throws IOException {
        throw Dew.E.e("A000", new IOException("io error"), StandardCode.UNAUTHORIZED);
    }

    @PostMapping(value = "t5")
    public String t5(@RequestBody @Valid SomeReq someReq) throws IOException {
        return "";
    }

    public static class SomeReq {
        @NotNull
        @Length(min = 2)
        @IdNumber(message = "身份证号错误")
        private String a;
        @Min(10)
        private int b;
        @Phone(message = "手机号错误")
        private String c;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }
    }

}
