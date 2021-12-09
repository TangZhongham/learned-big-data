public class BinarySearch {
    // the array must be sorted before!

    public static void main(String[] args) {

    }

    public static int binarySearch(int[] array, int value) {
        int low = 0;
        int high = array.length - 1;
        while (low <= high) {
            int middle = low + ((high - low) >> 1);
            if (array[middle] == value) {
                return middle;
                // 升序，当前中间值比待查询的值小，则在后半段，将最小值重置；
                // 降序数组则把判断是小于变成大于
            } else if (array[middle] < value) {
                low = middle + 1;
            } else {
                high = middle - 1;
            }
        }
        return -1;
    }

    public static int recursionBinarySearch(int[] array, int value) {
        return binarySearchInternally(array, 0, array.length - 1, value);
    }

    private static int binarySearchInternally(int[] array, int low, int high, int value) {
        if (low > high) {
            return -1;
        }

        int middle = low + ((high - low) >> 1);
        if (array[middle] == value) {
            return middle;
        } else if (array[middle] < value) {
            return binarySearchInternally(array, middle + 1, high, value);
        } else {
            return binarySearchInternally(array, low, middle - 1, value);
        }
    }
}
