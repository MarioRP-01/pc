package mergesort;

import java.util.ArrayList;
import java.util.List;

public class Commons {
    /**
     * Create a new ArrayList<Integer> with the elements of left and right
     * 
     * @param left
     * @param right
     * @return
     */
    public static List<Integer> merge(
        List<Integer> left, 
        List<Integer> right
    ) {
        var new_list = new ArrayList<Integer>();

        var i = 0;
        var j = 0;

        while (i < left.size() && j < right.size())
            if (left.get(i) < right.get(j)) {
                new_list.add(left.get(i++));
            } else {
                new_list.add(right.get(j++));
            }

        new_list.addAll(left.subList(i, left.size()));
        new_list.addAll(right.subList(j, right.size()));

        return new_list;
    }

    public static List<Integer> sort(List<Integer> list) {
        list.sort(Integer::compare);
        return list;
    }
}
