package org.openlca.cs.gaxpy;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openlca.core.matrix.format.CSCMatrix;

public class CsGaxpyTest {

	@Before
	public void setup() {
		var libDir = new File("target/release");
		var libFile = new File(libDir, "csgaxpy.dll");
		System.load(libFile.getAbsolutePath());
	}

	@Test
	public void test() {
		var matrix = CSCMatrix.of(new double[][] {
				{ 1.0, 0.0, 3.0 },
				{ 0.0, 2.0, 0.0 }
		});
		double[] x = { 1.0, 2.0, 3.0 };
		double[] y = CsGaxpy.mul(matrix, x);
		double[] expected = { 10.0, 4.0 };
		Assert.assertArrayEquals(expected, y, 1e-10);
	}
}
