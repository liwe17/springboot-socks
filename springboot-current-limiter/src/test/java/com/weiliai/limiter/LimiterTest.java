package com.weiliai.limiter;

import com.weiliai.limiter.controller.TestLimitController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: Doug Li
 * @Date 2020/4/26
 * @Describe: 限流测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LimiterTest {

    private final CyclicBarrier cyclicBarrier = new CyclicBarrier(20);

    @Test
    public void testRedisLimiter(){
        ExecutorService threadPool = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 20; i++) {
            threadPool.execute(new TestLimit());
        }
        threadPool.shutdown();
    }

    class TestLimit implements Runnable{

        @Override
        public void run() {
            try {
                cyclicBarrier.await();
            }catch (Exception e){
                e.printStackTrace();
            }
            System.err.println(new TestLimitController().testRedisLimit());
        }
    }

}
