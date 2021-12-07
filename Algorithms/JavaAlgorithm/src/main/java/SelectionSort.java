import java.util.Arrays;

public class SelectionSort {

    public static void main(String[] args) {
        int[] array = new int[]{12, 34, -34, 454, 657, 33, 89, 67, 68, 99, -23, 34};
        System.out.println("排序前：" + Arrays.toString(array));
        selectionSort(array);
        System.out.println("排序后：" + Arrays.toString(array));
    }

    /**
     * 选择排序，a表示数组，n表示数组大小
     * @param a 待排序数组
     */
    public static void selectionSort(int[] a) {
        int n = a.length;
        if (n <= 1) {
            return;
        }

        for (int i = 0; i < n - 1; ++i) {
            // 查找最小值
            int minIndex = i;
            for (int j = i + 1; j < n; ++j) {
                if (a[j] < a[minIndex]) {
                    minIndex = j;
                }
            }

            // 交换
            int tmp = a[i];
            a[i] = a[minIndex];
            a[minIndex] = tmp;
        }
    }

}
