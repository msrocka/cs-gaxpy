package org.openlca.cs.gaxpy;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openlca.core.matrix.format.CSCMatrix;

public class CsGaxpyTest {

	@Before
	public void setup() {
		CsGaxpy.loadFromFolder(new File("target"));
	}

	@Test
	public void test() {
		assertTrue(CsGaxpy.isLoaded());
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
