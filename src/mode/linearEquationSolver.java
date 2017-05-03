package mode;
public class linearEquationSolver {
	
	//a is a n*n matrix, p is a compact permutation matrix p.
	private static void LUP_DECOMPOSITION (double[][] a, int[] p) {
		int n = a.length;
		for (int i = 0; i < n; i++) {
			p[i] = i;
		}
		for (int k = 0; k < n-1; k++) {
			//choose max value as pivot, swap rows.
			double m = 0.0;
			int e = 0;
			for (int i = k; i < n; i++) {
				if (Math.abs(a[i][k]) > m) {
					m = Math.abs(a[i][k]);
					e = i;
				}
			}
			if (m == 0) {
				System.out.println ("singlular matrix");
				System.exit(0);
			} else {
				int t = p[e]; p[e] = p[k]; p[k] = t;
				for (int i = 0; i < n; i++) {
					double tt = a[k][i]; a[k][i] = a[e][i]; a[e][i] = tt;
				}
			}
			//LU_DECOMPOSITION
			for (int i = k+1; i < n; i++) {
				a[i][k] = a[i][k]/a[k][k];
				for (int j = k+1; j < n; j++) {
					a[i][j] = a[i][j] - a[i][k]*a[k][j];
				}
			}
			
		}
	}
	
	public static void LUP_Solver(double a[][], double B[], double x[]) {
		int n = a.length;
		int [] p = new int[n];
		LUP_DECOMPOSITION (a, p);
		//compute pb
		double b[] = new double[n];
		for (int i = 0; i < n; i++) {
			b[i] = B[p[i]];
		}
		//compute y
		double[] y = new double[n];
		y[0] = b[0];
		for (int i = 1; i < n; i++) {
			double sum = 0.0;
			for (int j = 0; j < i; j++) {
				sum += (a[i][j] * y[j]);
			}
			y[i] = b[i] - sum;
		}
		
		//compute x
		x[n-1] = y[n-1]/a[n-1][n-1];
		for (int i = n-2; i >=0; i--) {
			double sum = 0.0;
			for (int j = i+1; j < n; j++) {
				sum += (a[i][j]*x[j]);
			}
			x[i] = (y[i] - sum)/a[i][i];
		}
	}
	
	public void printMatrix (double[][] a,int n,int m) {

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				System.out.print(a[i][j]+" ");
			}
			System.out.println("");
		}
	}
	
	static void printArray (double[] a) {
		int n = a.length;
		for (int i = 0; i < n; i++) {
				System.out.println(a[i]+" ");
		}
	}
	
	static void printArray (int[] a) {
		int n = a.length;
		for (int i = 0; i < n; i++) {
				System.out.println(a[i]+" ");
		}
	}
	
	public static void main(String[] args) {
		double[][] a = new double[][]{
			{1,2,0},
			{3,4,4},
			{5,6,3},
		};
		double[] b = {3.0, 7.0, 8.0};
		double[] x = new double[3];
		LUP_Solver(a,b,x);
		printArray(x);
	}
}
