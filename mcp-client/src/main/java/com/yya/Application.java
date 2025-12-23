package com.yya;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(),e.getValue()));
        SpringApplication.run(Application.class, args);
    }
}
// http://47.250.95.26/pages/spring-ai.html

// 常用命令
// docker ps -a
// docker images
// docker start 98348  (id)


// jps -l
// kill -9191 进程号

//   http://47.250.95.26:9060/sse

//  http://47.250.95.26:6080  搜索引擎

// cd /home/software/jars
// nohup java -jar mcp-server.jar >my-server.log 2>&1 &
// nohup java -jar mcp-client.jar >my-client.log 2>&1 &

// http://47.250.95.26:6080/   （公网ip） 修改首选项，比如搜索引擎
// /home/docker/SearXNG 中修改format: - json

// 进入/usr/local/nginx/sbin目录启动nginx
//    ```
//    ./nginx
//    ```
//    * 停止：./nginx -s stop

// nginx 进程失效重启：
// https://cloud.tencent.com/developer/article/1375799

// /home/docker/SearXNG


