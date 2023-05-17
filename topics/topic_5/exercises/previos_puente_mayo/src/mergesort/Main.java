package mergesort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Supplier;

public class Main {
    
    private static final List<Integer> LIST = new ArrayList<>();
    private static final int SIZE = 5;
    private static final int MAX = 1000;

    public static void main(String[] args) {
        populateCollectionOfInteger(LIST);
        
        System.out.format("List: %s\n\n", LIST);

        Supplier<List<Integer>> supplier = () -> new ArrayList<>(LIST);

        var start = System.currentTimeMillis();

        try (var pool = new ForkJoinPool()) {

            var merged_list = pool.invoke(
                new MergeSortForkJoin(supplier.get())
            );
            System.out.format("Merged list: %s\n\n", merged_list);
            pool.shutdown();
        }
        
        var stop1 = System.currentTimeMillis();

        var merged_list_stream_reducer = 
            new MergeSortStreamReducer().merge_sort(supplier);

        System.out.format(
            "Merged list: %s\n\n", 
            merged_list_stream_reducer
        );

        var stop2 = System.currentTimeMillis();

        var merged_list_stream_collect = 
            new MergeSortStreamCollect().merge_sort(supplier);

        System.out.format(
            "Merged list: %s\n\n", 
            merged_list_stream_collect
        );

        var stop3 = System.currentTimeMillis();

        System.out.format(
            "ForkJoin: %s s\nStreamReducer: %s\n Stream Collect: %s s\n", 
            (stop1 - start) / 1000d, 
            (stop2 - stop1) / 1000d,
            (stop3 - stop2) / 1000d
        );
    }

    public static void populateCollectionOfInteger(
        Collection<Integer> collection
    ) {
        var random = new Random();

        for (int i = 0; i < SIZE; i++) {
            collection.add(random.nextInt() % MAX);
        }
    }
}
