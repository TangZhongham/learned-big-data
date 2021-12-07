import java.util.Arrays;

public class BinaryInsertSort {
    public static void main(String[] args) {
        int[] array = new int[]{12, 34, -34, 454, 657, 33, 89, 67, 68, 99, -23, 34};
        System.out.println("排序前：" + Arrays.toString(array));
        binaryInsertSort(array);
        System.out.println("排序后：" + Arrays.toString(array));
    }

    /**
     * 二分插入排序
     * @param array 待排序的数组
     */
    public static void binaryInsertSort(int[] array) {
        if (array.length < 2) {
            return;
        }
        // 记录当轮的值
        int value = 0;
        int low = 0, high = 0, middle = 0;
        for (int i = 1; i < array.length; i++) {
            low = 0;
            value = array[i];
            high = i - 1;
            while (low <= high) {
                middle = (high + low) >> 1;
                if (value < array[middle]) {
                    // 插入点为低半区，否则插入点在高半区
                    high = middle - 1;
                } else {
                    low = middle + 1;
                }
            }

            // 已经用临时变量value记录了需要挪动的位置值，此时从后往前将插入点后面所有元素往后挪动一位
            for (int j = i - 1; j >= low; j--) {
                array[j+ 1] = array[j];
            }
            //都往后挪动完了，再插入准确位置
            array[low] = value;
        }
    }
}
