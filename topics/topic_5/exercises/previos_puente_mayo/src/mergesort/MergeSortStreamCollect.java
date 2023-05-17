package mergesort;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MergeSortStreamCollect {
    
    public List<Integer> merge_sort(Supplier<List<Integer>> list) {

        return list.get().stream().parallel()
            .collect(
                ArrayList<Integer>::new,
                (list_acc, element) -> {
                    if (list_acc.isEmpty()) {
                        list_acc.add(element);  
                        return;
                    }
                    var i = 0;
                    while (
                        i < list_acc.size() && 
                        element > list_acc.get(i)
                    ) i++;
                    list_acc.add(i, element);
                },
                (list_acc, list_aux) -> {
                    var merged_list = Commons.merge(list_acc, list_aux);
                    list_acc.clear();
                    list_acc.addAll(merged_list);
                }
            );
    }
}
