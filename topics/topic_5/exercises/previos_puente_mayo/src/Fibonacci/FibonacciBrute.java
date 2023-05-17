package Fibonacci;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveTask;

public class FibonacciBrute extends RecursiveTask<Integer> {

    private final int n;
    private final ConcurrentHashMap<Integer, Integer> cache;

    FibonacciBrute(Integer n) {
        this.n = n;
        this.cache = new ConcurrentHashMap<>();
    }

    FibonacciBrute(Integer n, ConcurrentHashMap<Integer, Integer> cache) {
        this.n = n;
        this.cache = cache;
    }

    @Override
    protected Integer compute() {

        for (int i = 0; i < n; i++) {
            doOperation(i);
        }

        return doOperation(n);
    }
    
    private Integer doOperation(Integer n) {

        if (n <= 1) return n;

        if (cache.containsKey(n)) return cache.get(n);

        var left = new FibonacciBrute(n - 1, cache);
        var right = new FibonacciBrute(n - 2, cache);

        left.fork();
        var rightResult = right.compute();
        var leftResult = left.join();

        Integer result = leftResult + rightResult;

        cache.putIfAbsent(n, result);

        return result;
    }
}
