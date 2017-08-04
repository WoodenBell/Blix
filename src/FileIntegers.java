import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileIntegers {

	public static void main(String[] args) {
		for(String i:args) {
			File f = new File(i);
			FileInputStream fis;
			byte[] b;
			try {
				fis = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				fis = null;
			}
			try {
				b = new byte[fis.available()];
			} catch (IOException e1) {
				e1.printStackTrace();
				b = null;
			}
			try {
				fis.read(b);
				for(byte k:b) {
					System.out.print(k + "-");
				}
				System.out.print("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
