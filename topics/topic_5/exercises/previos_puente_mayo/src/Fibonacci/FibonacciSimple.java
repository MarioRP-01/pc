package Fibonacci;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveTask;

public class FibonacciSimple extends RecursiveTask<Integer> {

    private int n;
    private ConcurrentHashMap<Integer, Integer> cache;

    FibonacciSimple(Integer n) {
        this.n = n;
        this.cache = new ConcurrentHashMap<>();
    }

    FibonacciSimple(Integer n, ConcurrentHashMap<Integer, Integer> cache) {
        this.n = n;
        this.cache = cache;
    }

    @Override
    protected Integer compute() {

        if (n <= 1) return n;

        if (cache.containsKey(n)) return cache.get(n);

        var left = new FibonacciSimple(n - 1, cache);
        var right = new FibonacciSimple(n - 2, cache);

        left.fork();
        var rightResult = right.compute(); 
        var leftResult = left.join();

        Integer result = leftResult + rightResult;

        cache.putIfAbsent(n, result);

        return result;
    }
    
}
