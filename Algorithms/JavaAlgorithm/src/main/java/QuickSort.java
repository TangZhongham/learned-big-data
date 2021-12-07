import java.util.Arrays;

public class QuickSort {

    public static void main(String[] args) {
        int[] array = new int[]{12, 34, -34, 454, 657, 33, 89, 67, 68, 99, -23, 34};
        System.out.println("排序前：" + Arrays.toString(array));
        quickSort(array);
        System.out.println("排序后：" + Arrays.toString(array));
    }

    public static void quickSort(int[] array) {
        int n = array.length;
        quickSortInternally(array, 0, n - 1);
    }


    public static void quickSortInternally(int[] array, int left, int right) {
        if (left >= right) {
            return;
        }
        int part = partition(array, left, right);
        quickSortInternally(array, left, part - 1);
        quickSortInternally(array, part + 1, right);
    }

    private static int partition(int[] array, int left, int right) {
        int pivot = array[right];
        int i = left;
        for (int j = left; j < right; j++) {
            if (array[j] < pivot) {
                if (i == j) {
                    i++;
                } else {
                    int tmp = array[i];
                    array[i++] = array[j];
                    array[j] = tmp;
                }
            }
        }

        int tmp = array[i];
        array[i] = array[right];
        array[right] = tmp;

//        System.out.println("i = " + i);
        return i;
    }

}
