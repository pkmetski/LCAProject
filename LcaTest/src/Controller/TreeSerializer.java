package Controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import Model.Tree;

public class TreeSerializer {
	private static final String DIR = "C:\\Trees\\";

	public static void serialize(Tree tree) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		try {
			o.writeObject(tree);
			String filename = getFilename(tree.getN(), tree.getB(), tree.getD());
			saveToFile(b.toByteArray(), filename);
			o.flush();
			o.close();
			b.flush();
			b.close();
		} catch (StackOverflowError ex) {
			return;
		}
	}

	public static Tree deserialize(int N, int B, int D) throws IOException,
			ClassNotFoundException {
		byte[] bytes = null;
		try {
			bytes = loadFromFile(getFilename(N, B, D));
		} catch (Exception e) {
			return null;
		}
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o = new ObjectInputStream(b);
		Tree tree = (Tree) o.readObject();
		o.close();
		b.close();
		return tree;
	}

	private static void saveToFile(byte[] data, String filename)
			throws IOException {
		FileOutputStream fos = new FileOutputStream(DIR + filename);
		fos.write(data);
		fos.close();
	}

	private static byte[] loadFromFile(String filename) throws IOException {
		Path p = FileSystems.getDefault().getPath(DIR, filename);
		return Files.readAllBytes(p);
	}

	private static String getFilename(int N, int B, int D) {
		return String.format("%s_%s_%s", N, B, D);
	}
}
