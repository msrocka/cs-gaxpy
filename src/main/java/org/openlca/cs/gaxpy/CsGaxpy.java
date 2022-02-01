package org.openlca.cs.gaxpy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import org.openlca.core.matrix.format.CSCMatrix;
import org.openlca.util.OS;

public class CsGaxpy {

	private static final AtomicBoolean loaded = new AtomicBoolean(false);

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

	public static boolean isLoaded() {
		return loaded.get();
	}

	public static boolean loadFromFolder(File dir) {
		if (loaded.get())
			return true;
		synchronized (loaded) {
			if (loaded.get())
				return true;

			var file = new File(dir, getLibName());
			try {
				if (!file.exists()) {
					extractTo(file);
				}
				System.load(file.getAbsolutePath());
				loaded.set(true);
				return true;
			} catch (Throwable e) {
				System.err.println("Failed to load native library: "
						+ file.getAbsolutePath() + "\n" + e.getMessage());
				loaded.set(false);
				return false;
			}
		}
	}

	private static String getLibName() {
		return switch (OS.get()) {
		case WINDOWS -> "csgaxpy.dll";
		case MAC -> "csgaxpy.dylib";
		default -> "csgaxpy.so";
		};
	}

	private static void extractTo(File file) throws IOException {
		var dir = file.getParentFile();
		var name = file.getName();
		if (!dir.exists()) {
			Files.createDirectories(dir.toPath());
		}
		try (var stream = Objects.requireNonNull(
				CsGaxpy.class.getResourceAsStream(name))) {
			Files.copy(stream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
	}

}
