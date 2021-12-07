import java.util.Random;


//  arrayByInsertion length = 100000 排序花费时间为：1549
//  arrayByBubble length = 100000 排序花费时间为：26729

public class InsertionSortVsBubbleSort {

    public static void main(String[] args) {
        insertionSortVsBubbleSort();
    }

    public static void insertionSortVsBubbleSort() {
        int dataLength = 100000;
        int[] arrayByInsertion = new int[dataLength];
        int[] arrayByBubble = new int[dataLength];
        Random random = new Random();
        for (int i = 0; i < dataLength; i++) {
            int value = random.nextInt();
            arrayByInsertion[i] = value;
            arrayByBubble[i] = value;
        }
        long start = System.currentTimeMillis();
        InsertSort.insertionSort(arrayByInsertion);
        long end = System.currentTimeMillis();
        long insertionTime = end - start;
        BubbleSort.bubbleSort(arrayByBubble);
        long end2 = System.currentTimeMillis();
        long bubbleTime = end2 - end;

        System.out.println("arrayByInsertion length = " + arrayByInsertion.length + " 排序花费时间为：" + insertionTime);
        System.out.println("arrayByBubble length = " + arrayByBubble.length + " 排序花费时间为：" + bubbleTime);

    }
}
