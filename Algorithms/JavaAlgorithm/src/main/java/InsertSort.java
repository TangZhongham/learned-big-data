import java.util.Arrays;

public class InsertSort {

    public static void main(String[] args) {
        int[] array = new int[]{12, 34, -34, 454, 657, 33, 89, 67, 68, 99, -23, 34};
        System.out.println("排序前：" + Arrays.toString(array));
        insertionSort(array);
        System.out.println("排序后：" + Arrays.toString(array));
    }

    /**
     * 插入排序
     * @param array 待排序的数组
     */
    public static void insertionSort(int[] array) {
        if (array.length < 2) {
            return;
        }

        for (int i = 0; i < array.length; i ++) {
            int value = array[i];
            int j = i - 1;

            for ( ; j >= 0; j--) {
                if (array[j] > value) {
                    array[j + 1] = array[j];
                } else {
                    break;
                }
            }
            array[j + 1] = value;
        }
    }
}
