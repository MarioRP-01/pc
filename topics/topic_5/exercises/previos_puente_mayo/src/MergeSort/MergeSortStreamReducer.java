package MergeSort;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MergeSortStreamReducer {
    
    public List<Integer> merge_sort(Supplier<List<Integer>> list) {

        List<Integer> identity = new ArrayList<>();
        return list.get().stream().parallel()
            .reduce(
                identity,
                (list_aux, element) -> {
                    List<Integer> new_list = new ArrayList<Integer>(list_aux);
                    if (new_list.isEmpty()) {
                        new_list.add(element);
                        return new_list;
                    }
                    var i = 0;
                    while (
                        i < new_list.size() && 
                        element > new_list.get(i)
                    ) i++;
                    new_list.add(i, element);
                    return new_list;
                },
                Commons::merge
            );
    }
}