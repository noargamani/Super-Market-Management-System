package BusinessLayer;

public class Constants {
    public static final String exit="Exit";
    public static final String Code=".,@CODE@.,";
    public static String goBack= "Go Back";

    public static String PhoneNumregex = "\\+?(\\d{1,3})[- ]?(\\d{3,})[- ]?(\\d{3,})[- ]?(\\d{1,})";

    public static class Pair<K, V> implements Comparable<Pair<Integer, Integer>> {
        public final K first;
        public final V second;

        public Pair(K key, V value) {
            this.first = key;
            this.second = value;
        }

        public boolean equals(Pair<K, V> pair) {
            return (this.first.equals(pair.first) && this.second.equals(pair.second));
        }


        public int compareTo(Pair<Integer, Integer> other) {
            Integer CompareMe1 = (Integer) this.first;
            Integer CompareMe2 = (Integer) this.second;
            int keyComparison = (CompareMe1.compareTo(other.first));
            if (keyComparison != 0) {
                return keyComparison;
            }
            // If keys are equal, compare values
            return CompareMe2.compareTo(other.second);
        }

        public String toString()
        {
            return first.toString() + " " + second.toString();
        }


// --Commented out by Inspection START (11/04/2023 03:50):
//        public Comparator<Pair<Integer, Integer>> comparator = new Comparator<Pair<Integer, Integer>>() {
//            @Override
//            public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
//                return o1.compareTo(o2);
//            }
//        };
// --Commented out by Inspection STOP (11/04/2023 03:50)
    }
    public static int binarySearch(int[] arr, int l, int r, int x)
    {
        if (r > l) {
            int mid = l + (r - l) / 2;

            // If the element is present at the
            // middle itself
            if (arr[mid] == x)
                return mid;

            // If element is smaller than mid, then
            // it can only be present in left sub array
            if (arr[mid] > x)
                return binarySearch(arr, l, mid - 1, x);

            // Else the element can only be present
            // in right sub array
            return binarySearch(arr, mid + 1, r, x);
        }
        else if(r == -1)
        {
            return 0;
        }
        // in array (r == l)
        else if(arr[r] <= x)
        {
            return r;
        }
        else
        {
            return r-1;
        }
    }
    public static int binarySearch(double[] arr, int l, int r, double x)
    {

        if (r > l) {
            int mid = l + (r - l) / 2;

            // If the element is present at the
            // middle itself
            if (arr[mid] == x)
                return mid;

            // If element is smaller than mid, then
            // it can only be present in left subarray
            if (arr[mid] > x)
                return binarySearch(arr, l, mid - 1, x);

            // Else the element can only be present
            // in right subarray
            return binarySearch(arr, mid + 1, r, x);
        }
        else if(r == -1)
        {
            return 0;
        }
        // in array (r == l)
        else if(arr[r] <= x)
        {
            return r;
        }
        else
        {
            return r-1;
        }
    }
}