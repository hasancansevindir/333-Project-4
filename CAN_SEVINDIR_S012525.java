import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class CAN_SEVINDIR_S012525 {
	private static final Scanner SCANNER = new Scanner(System.in);

	public static class KnapsackCalculator {
		private final List<KnapsackItem> items;
		public final int result;

		public KnapsackCalculator(int capacity, List<KnapsackItem> items) {
			this.items = items;
			// For using the loop based algorithm
//			result = calculateMaxProfit(capacity);

			// For using the recursive algorithm
			result = calculateMaxProfitRecursively(capacity, items.size());
		}

		private int calculateMaxProfitRecursively(int capacity, int itemsSize) {
			int[][] dp = new int[itemsSize + 1][capacity + 1];
			for (int[] array: dp)
				Arrays.fill(array, -1);
			return calculateMaxProfitRecursivelyImpl(capacity, itemsSize, dp);
		}

		private int calculateMaxProfitRecursivelyImpl(int capacity, int itemsSize, int[][] dp) {
			if (itemsSize == 0 || capacity == 0)
				return 0;

			if (dp[itemsSize][capacity] != -1)
				return dp[itemsSize][capacity];

			int lastItemWeight = items.get(itemsSize - 1).weight;
			if (lastItemWeight > capacity) {
				dp[itemsSize][capacity] = calculateMaxProfitRecursivelyImpl(capacity, itemsSize - 1, dp);
				return dp[itemsSize][capacity];
			}
			int lastItemValue = items.get(itemsSize - 1).value;
			dp[itemsSize][capacity] = Math.max(
					lastItemValue + calculateMaxProfitRecursivelyImpl(capacity - lastItemWeight, itemsSize - 1, dp),
					calculateMaxProfitRecursivelyImpl(capacity, itemsSize - 1, dp)
			);
			return dp[itemsSize][capacity];
		}

		private int calculateMaxProfit(int capacity) {
			int size = items.size();
			int[][] knapsack = new int[size + 1][capacity + 1];
			for (int i = 1; i <= size; i++) {
				for (int weight = 0; weight <= capacity; weight++) {
					if (items.get(i - 1).weight <= weight)
						knapsack[i][weight] = Math.max(
								items.get(i - 1).value + knapsack[i - 1][weight - items.get(i - 1).weight],
								knapsack[i - 1][weight]
						);
					else
						knapsack[i][weight] = knapsack[i - 1][weight];
				}
			}

			return knapsack[size][capacity];
		}

		// calculates the maximum amount weight that can be put in the knapsack with the given capacity
	}

	// utility class for keeping values with their corresponding weights as pairs
	public static class KnapsackItem {
		public final int value;
		public final int weight;

		public KnapsackItem(int value, int weight) {
			this.value = value;
			this.weight = weight;
		}
	}

	// utility function take inputs from command line with a text message shown beforehand
	public static String input(String message) {
		System.out.print(message);
		return SCANNER.nextLine();
	}

	// utility function to handle errors more easily
	public static void assertThat(boolean expression, String message) {
		if (expression) return;
		System.err.println("Assertion failed! Cause: \n\t" + message);
		System.exit(-1);
	}

	public static void main(String[] args) {

		int numberOfItems = Integer.parseInt(input("Enter the number of items: "));

		// weights of the knapsack items
		int[] weights = Arrays.stream(input("Enter weights of items: ").split("\\s+"))
				.mapToInt(Integer::parseInt)
				.toArray();

		assertThat(numberOfItems == weights.length, "Number of items did not match with the actual amount of weights entered");

		// values of the knapsack items
		int[] profits = Arrays.stream(input("Enter profit values of items: ").split("\\s+"))
				.mapToInt(Integer::parseInt)
				.toArray();

		assertThat(numberOfItems == profits.length, "Number of items did not match with the actual amount of profit values entered");

		// highest possible weight that the knapsack can hold
		int weightCapacity = Integer.parseInt(input("Enter the maximum weight capacity: "));
		int minProfitRequired = Integer.parseInt(input("Enter the minimum profit required: "));

		// list of knapsack items, basically a list of pairs, the result of zipping
		// weight and profit arrays together to construct knapsack items
		List<KnapsackItem> items = IntStream.range(0, numberOfItems)
				.mapToObj(i -> new KnapsackItem(profits[i], weights[i]))
				.collect(Collectors.toList());

		KnapsackCalculator knapsackCalculator = new KnapsackCalculator(weightCapacity, items);
		int maxPossibleProfit = knapsackCalculator.result;
		System.out.println(minProfitRequired <= maxPossibleProfit ? "YES" : "NO");
		System.out.println("Because the maximum profit possible is " + maxPossibleProfit);
	}
}