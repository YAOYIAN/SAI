package com.yya;

import com.yya.mcp.tool.DateTool;
import com.yya.mcp.tool.EmailTool;
import com.yya.mcp.tool.ProductTool;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@MapperScan("com.yya.mapper")
@SpringBootApplication
public class Application {
    //    http://localhost:9060/sse

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ToolCallbackProvider registMCPTools(DateTool dateTool, EmailTool emailTool, ProductTool productTool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(dateTool,emailTool,productTool)
                .build();
    }
}
// 请创建一个耐克鞋子的商品，价格是299元，库存是66双，预售
// 请删除编号为703944178050的商品信息
// 查询所有商品
// 请用表格的形式输出
// 修改编号为831595416247的商品，把价格改为279元
