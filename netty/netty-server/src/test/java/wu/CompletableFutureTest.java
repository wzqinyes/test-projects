package wu;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureTest {

    public static void main(String[] args){
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "aaaaaaaa");
        f1.thenAccept(System.out::println);

        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "bbbbbbbb");
        f2.thenAccept(System.out::println);

        CompletableFuture<Void> future = CompletableFuture.allOf(f1, f2);
        future.thenAccept(v -> System.out.println("join end ..."));

        future.join();  //阻塞

        System.out.println("end..........");


    }
}
