import java.util.*;

public class EditDistance implements EditDistanceInterface {

	int c_i, c_d, c_r;
	static int MAX = Integer.MAX_VALUE;
	static int UNDEF = -1;

	public EditDistance(int c_i, int c_d, int c_r) {
		this.c_i = c_i;
		this.c_d = c_d;
		this.c_r = c_r;
	}

	public int[][] getEditDistanceDP(String s1, String s2) {
		int[][] result = new int[s1.length() + 1][s2.length() + 1];
		for (int i = 0; i < s1.length() + 1; i++)
			result[i][0] = i * this.c_d;
		for (int j = 1; j < s2.length() + 1; j++)
			result[0][j] = j * this.c_i;
		for (int i = 1; i < s1.length() + 1; i++) {
			for (int j = 1; j < s2.length() + 1; j++) {
				if (s1.charAt(i - 1) == s2.charAt(j - 1))
					result[i][j] = result[i - 1][j - 1];
				else
					result[i][j] = result[i - 1][j - 1] + this.c_r;
				result[i][j] = (result[i - 1][j] + this.c_d > result[i][j]) ? result[i][j]
						: result[i - 1][j] + this.c_d;
				result[i][j] = (result[i][j - 1] + this.c_i > result[i][j]) ? result[i][j]
						: result[i][j - 1] + this.c_i;
			}
		}

		return result;

	}

	public List<String> getMinimalEditSequence(String s1, String s2) {

		int n = s1.length();
		int m = s2.length();
		int X = 1;
		int oldDistance = 0;
		int newDistance = 0;
		int[][] d = new int[n + 1][2 * (Math.abs(n - m) + X) + 1];

		//***************** Calculating distances ***********************
		
		// we will try different Xs until we find the best one :

		do {

			oldDistance = newDistance;

			/*
			 * we will use a matrix of shape (n+1 , 2*(abs(n-m)+X) + 1) of only the pairs
			 * that need to be calculated every line i contains distances of : [(i,
			 * i-(abs(n-m)+X)), ... , (i,i-1) , (i,i) , (i,i+1), ...., (i , i +
			 * (abs(n-m)+X))] means that (i,j) is on d[i][(abs(n-m)+X) - i + j] since to
			 * calculate (i,j) we need (i,j-1) to be calculated first and (i-1,j) , and
			 * (i-1, j-1) too and we want to do calculations line by line from column zero
			 * to the last column
			 */

			d = new int[n + 1][2 * (Math.abs(n - m) + X) + 1];

			/*
			 * let's set all cells to +inf !!! MAX + something positive becomes negative !!!
			 */

			for (int i = 0; i < n + 1; i++) {
				for (int j = 0; j < 2 * (Math.abs(n - m) + X) + 1; j++) {
					d[i][j] = MAX - c_i - c_d - c_r;
				}
			}

			/*
			 * now let's calculate all distances we need starting from (0,0) and going line
			 * by line if a needed element is out of bounds we assume that it is infinite so
			 * we can neglect it in the minimum
			 */

			// case i = 0
			for (int j = 0; j < Math.min((Math.abs(n - m) + X) + 1, m + 1); j++)
				if (j < (Math.abs(n - m) + X + 1))
					d[0][(Math.abs(n - m) + X) + j] = j * this.c_i;
			// case j = 0
			for (int i = 1; i < n + 1; i++)
				if ((Math.abs(n - m) + X + 1 > i))
					d[i][(Math.abs(n - m) + X) - i] = i * this.c_d;

			// other cases
			for (int i = 1; i < n + 1; i++) {
				for (int j = Math.max(i - (Math.abs(n - m) + X), 1); j < Math.min(i + (Math.abs(n - m) + X) + 1,
						m + 1); j++) {

					int k = (Math.abs(n - m) + X) - i + j;

					if (s1.charAt(i - 1) == s2.charAt(j - 1))
						d[i][k] = d[i - 1][k];
					else
						d[i][k] = d[i - 1][k] + this.c_r;

					if (k + 1 < 2 * (Math.abs(n - m) + X) + 1)
						d[i][k] = (d[i - 1][k + 1] + this.c_d > d[i][k]) ? d[i][k] : d[i - 1][k + 1] + this.c_d;

					if (k - 1 >= 0)
						d[i][k] = (d[i][k - 1] + this.c_i > d[i][k]) ? d[i][k] : d[i][k - 1] + this.c_i;
				}
			}

			newDistance = d[n][(Math.abs(n - m) + X) - n + m];
			X *= 2;

		} while (oldDistance != newDistance); // while there is an improvement we increase X

		
		//***************** reconstructing the solution ***********************

		X /= 2; // the X was actually multiplied by 2 but the loop stopped after this
		LinkedList<String> result = new LinkedList<>();
		int[] current = new int[] { n, m };

		while (current[0] != 0 && current[1] != 0) {

			// other cases
			int i = current[0];
			int j = current[1];
			int k = (Math.abs(n - m) + X) - i + j;

			if (s1.charAt(i - 1) != s2.charAt(j - 1) && d[i][k] == d[i - 1][k] + this.c_r) {
				result.add("replace(" + (i - 1) + "," + s2.charAt(j - 1) + ")");
				current = new int[] { i - 1, j - 1 };
			}

			else if (k + 1 < 2 * (Math.abs(n - m) + X) + 1 && (d[i - 1][k + 1] + this.c_d <= d[i][k])) {
				result.add("delete(" + (i - 1) + ")");
				current = new int[] { i - 1, j };
			}

			else if (k - 1 >= 0 && (d[i][k - 1] + this.c_i <= d[i][k])) {
				result.add("insert(" + i + "," + s2.charAt(j - 1) + ")");
				current = new int[] { i, j - 1 };
			} else {
				current = new int[] { i - 1, j - 1 };
			}
			
		}
		// case i = 0
		if (current[0] == 0)
			for (int k = 0; k < current[1]; k++) {
				result.add("insert(" + k + "," + s2.charAt(k) + ")");
			}

		// case j = 0
		if (current[1] == 0)
			for (int k = 0; k < current[0]; k++) {
				result.add("delete(" + 0 + ")");
			}

		return result;
	}
};
