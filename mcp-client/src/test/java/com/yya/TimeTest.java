package com.yya;

import cn.hutool.core.date.StopWatch;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TimeTest {
    @Test
    public void testTime(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("任务1");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        stopWatch.stop();

        stopWatch.start("任务2");
        try {
            Thread.sleep(660);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        stopWatch.stop();


        stopWatch.start("任务3");
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        System.out.println(stopWatch.shortSummary());
        System.out.println("总耗时："+stopWatch.getTotalTimeMillis());
        System.out.println("任务总数："+stopWatch.getTaskCount());
    }
}
