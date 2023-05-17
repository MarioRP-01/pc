package mergesort;

import java.util.List;
import java.util.concurrent.RecursiveTask;

public class MergeSortForkJoin extends RecursiveTask<List<Integer>>{

    private final List<Integer> list;

    MergeSortForkJoin(List<Integer> collection) {

        if (collection == null) 
            throw new IllegalArgumentException("Collection cannot be null");

        this.list = collection;
    }

    @Override
    protected List<Integer> compute() {

        var size = list.size();

        if (size <= 2) return Commons.sort(list);

        var leftList = list.subList(0, size / 2);
        var rightList = list.subList(size / 2, size);

        var left = new MergeSortForkJoin(leftList);
        var right = new MergeSortForkJoin(rightList);

        left.fork();
        var rightResult = right.compute();
        var leftResult = left.join();
            
        return Commons.merge(leftResult, rightResult);
    }
}
