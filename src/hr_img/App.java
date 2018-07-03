package hr_img;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class App {

	// always there
	private static String FILE_FOOTER = "resources/footer.png";
	private static String FILE_TEMPLATE = "resources/template.png";

	// put by the user
	private static String FILE_1 = "1.png";
	private static String FILE_2 = "2.png";

	// created by the program and deleted in the cleanup phase
	private static String FILE_1_CROPPED = "1_cr.png";
	private static String FILE_2_CROPPED = "2_cr.png";

	private static String FILE_FINAL = "1+2.png";

	public static void main(String[] args) {
		cropImage1();
		cropImage2();
		createOutput();
		cleanup();
	}

	private static void cleanup() {
		new File(FILE_1_CROPPED).delete();
		new File(FILE_2_CROPPED).delete();
	}

	private static void createOutput() {
		BufferedImage template = readImage(FILE_TEMPLATE);
		BufferedImage img1 = readImage(FILE_1_CROPPED);
		BufferedImage img2 = readImage(FILE_2_CROPPED);

		int img2y = template.getHeight() - img2.getHeight();

		Graphics2D graphics = template.createGraphics();
		graphics.drawImage(img2, 0, img2y, null);
		graphics.drawImage(img1, 0, 0, null);

		graphics.dispose();

		try {
			ImageIO.write(template, "png", new File(FILE_FINAL));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void cropImage2() {
		BufferedImage img = readImage(FILE_2);
		BufferedImage footerImg = readImage(FILE_FOOTER);

		int imgHeight = img.getHeight();
		int imgWidth = img.getWidth();

		int footerHeight = footerImg.getHeight();

		int[] footerArray = createPixelArray(footerImg);

		int y = 0;
		for (int i = 800; i < imgHeight - footerHeight; i++) {
			int[] imgArray = createPixelArray(img, i, 120);
			if (equalArrays(footerArray, imgArray)) {
				System.out.println("FOUND!");
				y = i;
			}
		}

		BufferedImage out = img.getSubimage(0, 0, imgWidth, y);

		try {
			ImageIO.write(out, "png", new File(FILE_2_CROPPED));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static boolean equalArrays(int[] a1, int[] a2) {
		if (a1.length != a2.length) {
			System.err.println("Arrays don't have the same size!");
			return false;
		}

		int count = 0;
		for (int i = 0; i < a1.length; i++) {
			count++;
			if (a1[i] != a2[1]) {
				return count > 101;
			}
		}
		return true;
	}

	private static int[] createPixelArray(BufferedImage img, int startingHeight, int startingWidth) {
		int count = 0;
		int[] array = new int[10405];
		for (int x = startingHeight; x < startingHeight + 102; x++) {
			for (int y = startingWidth; y < startingWidth + 102; y++) {
				array[count++] = img.getRGB(y, x);
			}
		}
		return array;
	}

	private static int[] createPixelArray(BufferedImage img) {
		return createPixelArray(img, 0, 0);
	}

	private static void cropImage1() {
		BufferedImage img = readImage(FILE_1);
		BufferedImage out = img.getSubimage(0, 817, img.getWidth(), 319);

		try {
			ImageIO.write(out, "png", new File(FILE_1_CROPPED));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static BufferedImage readImage(String path) {
		try {
			BufferedImage img = ImageIO.read(new File(path));
			return img;
		} catch (IOException e) {
			System.err.println("Problem while reading file: " + path);
		}
		return null;
	}

}
