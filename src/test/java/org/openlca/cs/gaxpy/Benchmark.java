package org.openlca.cs.gaxpy;

import java.io.File;

import org.openlca.core.DataDir;
import org.openlca.core.library.LibMatrix;
import org.openlca.core.matrix.format.CSCMatrix;

public class Benchmark {

	public static void main(String[] args) {
		System.out.println("  load native library");
		CsGaxpy.loadFromFolder(new File("target"));

		System.out.println("  load library data");
		var lib = DataDir.getLibrary("en15804_00.00.001");
		var s = lib.getColumn(LibMatrix.INV, 42).get();
		var rawB = lib.getMatrix(LibMatrix.B).get();
		var matrixB = rawB instanceof CSCMatrix csc
			? csc
			: CSCMatrix.of(rawB);

		System.out.println("  check first result");
		var javaResult = matrixB.multiply(s);
		var nativeResult = CsGaxpy.mul(matrixB, s);
		double diff = 0.0;
		for (int i = 0; i < javaResult.length; i++) {
			diff += Math.abs(javaResult[i] - nativeResult[i]);
		}
		System.out.println("  diff = " + diff);

		System.out.println("iteration | Java [ms] | JNI+Rust [ms]");
		for (int i = 0; i < 10; i++) {
			long rustStart = System.nanoTime();
			CsGaxpy.mul(matrixB, s);
			double rustTime = ((double)(System.nanoTime() - rustStart)) / 1e6;

			long javaStart = System.nanoTime();
			matrixB.multiply(s);
			double javaTime = ((double)(System.nanoTime() - javaStart)) / 1e6;
			System.out.printf("%d         | %.3f     | %.3f%n", i, javaTime, rustTime);
		}

	}
}
