import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ConfigReader {

	private String configPath;
	private int MEMORY_MAX = 1024;
	private int PROC_SIZE_MAX = 512;
	private int NUM_PROC = 10;
	private int MAX_PROC_TIME = 10000;

	public ConfigReader(String configPath) {
		// TODO Auto-generated constructor stub
		this.configPath = configPath;
		read();
	}

	public void read() {
		try (Scanner s = new Scanner(new File(configPath))) {

			while (s.hasNextLine()) {
				String line = s.nextLine();
				String[] values = line.split(" *= *");

				switch (values[0].trim()) {
				case "MEMORY_MAX":
					MEMORY_MAX = Integer.parseInt(values[1].trim());
					break;
				case "PROC_SIZE_MAX":
					PROC_SIZE_MAX = Integer.parseInt(values[1].trim());
					break;
				case "NUM_PROC":
					NUM_PROC = Integer.parseInt(values[1].trim());
					break;
				case "MAX_PROC_TIME":
					MAX_PROC_TIME = Integer.parseInt(values[1].trim());
					break;
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getMEMORY_MAX() {
		return this.MEMORY_MAX;
	}

	public void setMEMORY_MAX(int MEMORY_MAX) {
		this.MEMORY_MAX = MEMORY_MAX;
	}

	public int getPROC_SIZE_MAX() {
		return this.PROC_SIZE_MAX;
	}

	public void setPROC_SIZE_MAX(int PROC_SIZE_MAX) {
		this.PROC_SIZE_MAX = PROC_SIZE_MAX;
	}

	public int getNUM_PROC() {
		return this.NUM_PROC;
	}

	public void setNUM_PROC(int NUM_PROC) {
		this.NUM_PROC = NUM_PROC;
	}

	public int getMAX_PROC_TIME() {
		return this.MAX_PROC_TIME;
	}

	public void setMAX_PROC_TIME(int MAX_PROC_TIME) {
		this.MAX_PROC_TIME = MAX_PROC_TIME;
	}

}
