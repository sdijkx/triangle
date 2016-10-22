package nl.mh.test.robot.domain;

import org.testng.annotations.Test;

import java.util.concurrent.*;

/**
 * Created by Marc on 16-6-2016.
 */
public class PhotoCoordinatesTest {

    @Test
    public void testInt() throws ExecutionException, InterruptedException {
        int a = 0;

        a = (int) 1.2;

        String kip = "";
        ExecutorService executor = Executors.newFixedThreadPool(4);

//        CompletableFuture future1 = CompletableFuture.supplyAsync(
//                () -> {
//                    for(int i =0; i < 1000; i++){
//                        System.out.println("AAA-->" + i);
//                    }return true;});
//
//        CompletableFuture future2 = CompletableFuture.supplyAsync(
//                () -> {
//                    for(int i =0; i < 10; i++){
//                        System.out.println("BBB-->" + i);
//                    }return true;});
//
//
//        future1.get();
//        future2.get();
//
//         future1 = CompletableFuture.supplyAsync(
//                () -> {
//                    for(int i =0; i < 1000; i++){
//                        System.out.println("CCC-->" + i);
//                    }return true;});
//
//         future2 = CompletableFuture.supplyAsync(
//                () -> {
//                    for(int i =0; i < 10; i++){
//                        System.out.println("DDD-->" + i);
//                    }return true;});
//
//
//        future1.get();
//        future2.get();

    }
}
