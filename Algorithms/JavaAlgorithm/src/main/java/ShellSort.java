import java.util.Arrays;

public class ShellSort {

    public static void main(String[] args) {
        int[] array = new int[]{8, 9, 1, 7, 2, 3, 5, 4, 6, 0};
        System.out.println("排序前：" + Arrays.toString(array));
//        shellChangeSort(array);
        shellSort(array);
//        shellMoveSort(array);
        System.out.println("排序后：" + Arrays.toString(array));
    }

    /**
     * 希尔排序 针对有序序列在插入时采用交换法
     * @param arr 待排序数组
     */
    public static void shellChangeSort(int[] arr) {
        //增量gap，并逐步缩小增量
        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
            // 从第gap个元素，逐个对其所在组进行直接插入排序操作
            for (int i = gap; i < arr.length; i++) {
                int j = i;
                while (j - gap >= 0 && arr[j] < arr[j-gap]) {
                    // 插入排序采用交换法
                    swap(arr, j,j - gap);
                    j -= gap;
                }
            }
        }
    }

    /**
     *  希尔排序 针对有序序列在插入时采用移动法
     * @param arr 待排序数组
     */
    public static void shellMoveSort(int[] arr) {
        // 增量gap，并逐步缩小增量
        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
            // 从第gap个元素，逐个对其所在组进行直接插入排序操作
            for (int i = gap; i < arr.length; i++) {
                int j = i;
                int temp = arr[j];
                if (arr[j] < arr[j - gap]) {
                    while(j - gap >= 0 && temp < arr[j - gap]) {
                        // 移动法
                        arr[j] = arr[j - gap];
                        j -= gap;
                    }
                    arr[j] = temp;
                }
            }
        }
    }

    /**
     *  交换数组元素
     * @param arr
     * @param a
     * @param b
     */
    public static void swap(int []arr, int a, int b) {
        arr[a] = arr[a] + arr[b];
        arr[b] = arr[a] - arr[b];
        arr[a] = arr[a] - arr[b];
    }

    /**
     * 另一种希尔排序写法
     * @param arr 待排序数组
     */
    private static void shellSort(int[] arr) {
        int len = arr.length;
        if (len == 1) {
            return;
        }

        int step = len / 2;
        while (step >= 1) {
            for (int i = step; i < len; i++) {
                int value = arr[i];
                int j = i - step;
                for (; j >= 0; j -= step) {
                    if (value < arr[j]) {
                        arr[j + step] = arr[j];
                    } else {
                        break;
                    }
                }
                arr[j + step] = value;
            }

            step = step / 2;
        }
    }
}