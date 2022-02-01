package org.openlca.cs.gaxpy;

import org.openlca.core.matrix.format.CSCMatrix;

public class CsGaxpy {

	public static native void mul(
			int n,
			int[] columnPointers,
			int[] rowIndices,
			double[] matrixValues,
			double[] x,
			double[] y);

	public static double[] mul(CSCMatrix m, double[] x) {
		var y = new double[m.rows];
		mul(m.columns, m.columnPointers, m.rowIndices, m.values, x, y);
		return y;
	}
}
