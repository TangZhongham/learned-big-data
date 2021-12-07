import java.util.Arrays;

public class BubbleSort {

    public static void main(String[] args) {
        int[] array = new int[]{1, 2, 4, 85, 47, 65, 78, 99, 81};
        System.out.println("排序前：" + Arrays.toString(array));
//        bubbleSort(array);
        bubbleDownSort(array);
        System.out.println("排序后：" + Arrays.toString(array));
    }

    /**
     * 冒泡排序
     * @param array 待排序的数组
     */
    public static void bubbleSort(int[] array) {
        if (array.length < 2) {
            return;
        }

        for (int i = 0; i < array.length; i++) {
            boolean isChanged = false;
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    isChanged = true;
                    int tmp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = tmp;
                }
            }
            // 如果上一轮没有进行任何的数据交换，则说明已经有序直接退出
            if (!isChanged) {
                break;
            }
        }
    }

    /**
     * 冒泡排序的改进方案
     *
     * 每次记录最后一次进行位置交换的点，作为下次比较的边界，节省了该部分的比较操作
     *
     * @param array 待排序的数组
     */
    public static void bubbleSortOptimization(int[] array) {
        if (array.length < 2) {
            return;
        }

        // 记录最后一次交换的点
        int lastChangePoint = 0;
        // 记录无序数组的边界，每次比较到该处即可，后面已经是有序的
        int sortBoeder = array.length - 1;

        for (int i = 0; i < array.length - 1; i++) {
            boolean isChanged = false;
            for (int j = 0; j < array.length - sortBoeder; j++) {
                if (array[j] > array[j + 1]) {
                    swap(array, j, j + 1);
                    lastChangePoint = j;
                    isChanged = true;
                }
            }
            sortBoeder = lastChangePoint;
            // 如果上一轮没有进行任何的数据交换，则说明已经有序直接退出
            if (!isChanged) {
                break;
            }
        }

    }

    /**
     * 交换数组中 两个下标位置的值
     * @param array 待排序数组
     * @param i 需要交换的下标 1
     * @param k 需要交换的下标 2
     */
    private static void swap(int[] array, int i, int k) {
        int tmp = array[i];
        array[i] = array[k];
        array[k] = tmp;
    }


    /**
     * 向下冒泡。可能比冒泡更易懂？
     *
     * 算法概要：
     * 从0开始，用这个元素去跟后面的所有元素比较，如果发现这个元素大于后面的某个元素，则交换。
     * 3 2 6 4 5 1
     * 第一趟是从 index=0 也就是 3， 开始跟index=1及其后面的数字比较
     *  3 大于 2，交换，变为 2 3 6 4 5 1，此时index=0的位置变为了2
     *    接下来将用2跟index=2比较
     *  2 不大于 6 不交换
     *  2 不大于 4 不交换
     *  2 不大于 5 不交换
     *  2 大于 1，交换，变为 1 3 6 4 5 2，第一趟排序完成。
     *
     * 第二趟是从 index=1 也就是 3，开始跟index=2及其后面的数字比较
     *  3 不大于 6 不交换
     *  3 不大于 4 不交换
     *  3 不大于 5 不交换
     *  3 大于 2，交换，变为 1 2 6 4 5 3，第二趟排序完成。
     *
     * 第三趟是从 index=2 也就是 6，开始跟index=3及其后面的数字比较
     *  6 大于 4，交换，变为 1 2 4 6 5 3, 此时 index = 2 的位置变为了4
     *     接下来将用4跟index=4比较
     *  4 不大于 5 不交换
     *  4 大于 3，交换，变为 1 2 3 6 5 4，第三趟排序完成。
     *
     * 第四趟是从 index=3 也就是 6，开始跟index=4及其后面的数字比较
     *  6 大于 5，交换，变为 1 2 3 5 6 4, 此时 index = 3 的位置变为了5
     *     接下来将用5跟index=5比较
     *  5 大于 4，交换，变为 1 2 3 4 6 5, 第四趟排序完成。
     *
     * 第五趟是从 index=4 也就是 6，开始跟index=5及其后面的数字比较
     *  6 大于 5，交换，变为 1 2 3 4 5 6, 此时 index = 4 的位置变为了5
     *     接下来将用5跟index=6比较
     *  index = 6 已经不满足 index < length 的条件，整个排序完成。
     */
    private static void bubbleDownSort(int[] arr) {
        int len = arr.length;
        if (len == 1) {
            return;
        }

        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                if (arr[i] > arr[j]) {
                    int tmp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = tmp;
                }
            }
        }
    }

}


