package data.crawler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class FileComparator {

	public static void main(String[] args) {

		String path1 = "D:/StockAnalysis/data/mx/2012-08-21x/";
		String path2 = "D:/StockAnalysis/data/mx/2012-08-21/";

		File dir1 = new File(path1);
		if (!dir1.isDirectory()) {
			System.out.println(path1 + " is not a directory.");
		}
		File dir2 = new File(path2);
		if (!dir2.isDirectory()) {
			System.out.println(path2 + " is not a directory.");
		}

		try {
			for (File file : dir1.listFiles()) {
				String fileName = file.getName();
				if (!compareFile(file.getAbsolutePath(), path2 + fileName)) {
					System.out.println(fileName + " is not consistent.");
				}
			}
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}

	}

	private static boolean compareFile(String goldenFile, String testeeFile)
			throws Exception {
		long goldenFileSize = 0;
		long testeeFileSize = 0;
		File fTestee = new File(testeeFile);
		if (fTestee.exists())
			testeeFileSize = fTestee.length();
		else
			return false;
		File fGolden = new File(goldenFile);
		if (fGolden.exists())
			goldenFileSize = fGolden.length();
		else
			return false;
		if (goldenFileSize != testeeFileSize)
			return false;

		InputStream golden = null;
		InputStream testee = null;
		try {
			golden = new BufferedInputStream(new FileInputStream(goldenFile));
			testee = new BufferedInputStream(new FileInputStream(testeeFile));

			int tempGoldenByte = golden.read();
			while (tempGoldenByte != -1) {
				int tempTesteeByte = testee.read();
				if (tempTesteeByte != tempGoldenByte)
					return false;
				tempGoldenByte = golden.read();
			}
			if (-1 != testee.read()) {
				return false;
			}
			return true;
		} finally {
			golden.close();
			testee.close();
		}
	}

}
