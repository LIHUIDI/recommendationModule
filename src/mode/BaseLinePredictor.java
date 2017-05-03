package mode;

public class BaseLinePredictor {

	public static double[][] bp(double[][] R, double[][] R1, int u, int m) {
		//compute the mean of R.
		double sumOfrate = 0.0;
		int numOfrate = 0;
		for (int i = 0; i < u; i++) {
			for (int j = 0; j < m; j++) {
				if (R[i][j] != 0) {
					numOfrate = numOfrate + 1;
					sumOfrate = (sumOfrate + R[i][j]);
				}
			}
		}
		double meanOfr = sumOfrate/numOfrate;
		
		////construct A and C
		meanOfr = 3.83;
		double c[] = new double[numOfrate];
		double[][] A = new double[numOfrate][u+m];
		int k = 0; 
		for (int i = 0; i < u; i++) {
			for (int j = 0; j < m; j++) {
				if (R[i][j] != 0) {
					A[k][i] = 1;
					A[k][j+u] = 1;
					c[k] = R[i][j] - meanOfr;
					k++;
				}
			}
		}
		
		// compute the t of a.
		double[][] tOfa = t (A, numOfrate, u+m);
		//compute (tOfa) * a;
		double[][] mm = mul (tOfa, A, u+m, numOfrate, u+m);
		//compute tOfa*c
		double[] aTc = new double[u+m];
		for (int i = 0; i < u+m; i++) {
			double sum = 0.0;
			for (int j = 0; j < numOfrate; j++) {
				sum += (tOfa[i][j] * c[j]);
			}
			aTc[i] = sum;
		}
		
		//solve linear equation
		double x[] = new double[u+m];
		linearEquationSolver.LUP_Solver(mm, aTc, x);
		//compute R1
		for (int i = 0; i < u; i++) {
			for (int j = 0; j < m; j++) {
				R1[i][j] = meanOfr + x[i] + x[u+j];
				if (R1[i][j] > 5) R1[i][j] = 5;
				if (R1[i][j] < 1) R1[i][j] = 1;
			}
		}
		return R1;
	}
	
	//matrix multiplication
	public static double[][] mul (double[][]a, double [][]b, int l, int w, int k) {
		double [][] mul = new double[l][k];
		for (int i = 0; i < l; i++) {
			for (int j = 0; j < k; j++) {
				double sum = 0.0;
				for (int c = 0; c < w; c++) {
						sum += a[i][c]*b[c][j];
				}
				mul[i][j] = sum;
			}
		}
		return mul;
	}
	
	public static double[][] t (double[][] a, int u, int m) {
		double[][] tOfa = new double[m][u];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < u; j++) {
				tOfa[i][j] = a[j][i];
			}
		}
		return tOfa;
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
		linearEquationSolver linearSoler = new linearEquationSolver();
		linearSoler.printMatrix(R,10,5);
		System.out.println("after baseline predictor...");
		double[][] R1 = new double[10][5];
		linearSoler.printMatrix(bp(R, R1, 10, 5),10,5);
	}
}


	

