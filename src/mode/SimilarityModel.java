package mode;

public class SimilarityModel {
	
 	public static double[][] diffrenceOfr(double[][] R, double r[][], double[][] d, int u, int m) {
		for (int i = 0; i < u; i++) {
			for (int j = 0; j < m; j++) {
				if (R[i][j] != 0){
					d[i][j] = R[i][j] - r[i][j];
				} else {
					d[i][j] = 0;
				}
			}
		}
		return d;
	}
	
	/*
	 * From difference table, compute similarity value between two movies a and b.  
	 */
	public static double similarity(double[][]r, int a, int b, int u, int m) {
		double sum0 = 0.0;
		double sum1 = 0.0;
		double sum2 = 0.0;
		for (int i = 0; i < u; i++) {
			if ( r[i][a]!= 0 && r[i][b] != 0) {
				sum0 = sum0 + (r[i][a] * r[i][b]);
				sum1 = sum1 + (r[i][a] * r[i][a]);
				sum2 = sum2 + (r[i][b] * r[i][b]);
			}
		}
		return (sum0)/Math.sqrt(sum1 * sum2);
	}
	
	/*
	 * construct similarity table.
	 */
	public static void similarityTable(double[][] s, double[][] r, int u, int m) {
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < m; j++) {
				if (i != j) {
					s[i][j] = similarity(r, i, j, u, m);
				}
			}
		}
		
	}
	
	/*
	 * compute movies similarity factor for a movie m rated by a user.
	 */
	public static double similarityFactor(double r[][], double s[][], int u, int m) {
		double []a = new double[s.length];
		int [] b = new int[s.length];
		for (int i = 0; i < s.length; i++) {
			b[i] = i;
		}
		b = sortedMoive(a, b,s, m);
		// get the most similar two movies b[0], b[1] with m, and test if they are truly similar.
		if (r[u][b[0]] == 0 && r[u][b[1]] != 0 && isSimilar(r,m,b[1])) {
			return (s[m][b[1]]*r[u][b[1]])/Math.abs(s[m][b[1]]);
		}else if(r[u][b[0]] != 0 && r[u][b[1]] == 0 && isSimilar(r,m,b[0])) {
			return (s[m][b[0]]*r[u][b[0]])/Math.abs(s[m][b[0]]);
		}else {
			if (isSimilar(r,m,b[0]) && isSimilar(r,m,b[1]) ) {
				return (s[m][b[0]]*r[u][b[0]]+s[m][b[1]]*r[u][b[1]])/(Math.abs(s[m][b[0]]) + Math.abs(s[m][b[1]]));
			} else if (isSimilar(r,m,b[0]) && !isSimilar(r,m,b[1])){
				return (s[m][b[0]]*r[u][b[0]])/Math.abs(s[m][b[0]]);
			} else if (isSimilar(r,m,b[1]) && !isSimilar(r,m,b[0])) {
				return (s[m][b[1]]*r[u][b[1]])/Math.abs(s[m][b[1]]);
			} else {
				return 0;
			}
		}
	}
	
	/*
	 * computer the final table
	 */
	public static double[][] finalModel(double R1[][], double r[][], double s[][], int u, int m){
		for (int i = 0; i < u; i++) {
			for (int j = 0; j < m; j++) {
				R1[i][j] = R1[i][j] + similarityFactor(r, s, i,j);
				if (R1[i][j] > 5) R1[i][j] = 5;
				if (R1[i][j] < 1) R1[i][j] = 1;
			}
		}
		return R1;
	}
	
	/*
	 * help function, return true if the percentage of users saw both movies m1 and m2 are greater than 10%. 
	 */
	public static boolean isSimilar(double r[][], int m1, int m2) {
		double totalNumberOfUsers = r.length;
		double counter = 0.0;
		for (int i = 0; i < totalNumberOfUsers; i++) {
			if (r[i][m1] != 0 && r[i][m2] != 0) {
				counter ++;
			}
		}
		if (counter/totalNumberOfUsers > 0.1) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * help function, from similarity table, get the sorted array for a single movie.
	 */
	private static int[] sortedMoive(double sortedMovies[], int indexOfa[], double s[][], int r) {
		int length = s.length;
		for (int i = 0; i < length; i++) {
			sortedMovies[i] = (s[r][i] >= 0 ? s[r][i] : -s[r][i]);
		}
		indexOfa = sort(sortedMovies, indexOfa);
		return indexOfa;
	}
	
	/*
	 * help function, using bubble sort to sort a array, return the index of sorted array.
	 */
	private static int[] sort(double[] a, int[] indexOfa) {
		int N = a.length;
		for (int j = 0; j < a.length; j++) {
			for (int i = 0; i < N-1; i++) {
				if (a[i] < a[i+1]) {
					double m = a[i];
					a[i] = a[i+1];
					a[i+1] = m;
					int t = indexOfa[i];
					indexOfa[i] = indexOfa[i+1];
					indexOfa[i+1] = t;
				}
			}
			N--;
		}
		return indexOfa;
	}
	
	public static void main(String[] args) {
		
		double[][] R = {{5,4,4,0,0},
				{0,3,5,0,4},
				{5,2,0,0,3},
				{0,0,3,1,2},
				{4,0,0,4,5},
				{0,3,0,3,5},
				{3,0,3,2,0},
				{5,0,4,0,5},
				{0,2,5,4,0},
				{0,0,5,3,4}
				};
		//baseline prediction table.
		double[][] R1 = new double[10][5];
		BaseLinePredictor.bp(R, R1, 10, 5);
		linearEquationSolver linearSoler = new linearEquationSolver();
		System.out.println("print the baseline prediction table");
		linearSoler.printMatrix(R1,10,5);
		
		//difference table.
		double[][] r = new double[10][5];
		r = diffrenceOfr(R, R1, r,10, 5);
		System.out.println("print the diffrence table");
		linearSoler.printMatrix(r,10,5);
		
		//similarity table.
		System.out.println("print the movies similatiry table");
		double[][] s = new double[5][5];
		similarityTable(s, r, 10, 5);
		linearSoler.printMatrix(s,5,5);
		
		//final prediction table.
		System.out.println("print the final table");
		R1 = finalModel(R1, r, s, 10, 5);
		linearSoler.printMatrix(R1,10,5);
	}
}



