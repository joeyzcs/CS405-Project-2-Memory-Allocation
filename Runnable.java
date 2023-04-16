
public class Runnable {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String configPath = "src/config.txt";
		ConfigReader configReader = new ConfigReader(configPath);
		System.out.println("MEMORY_MAX = " + configReader.getMEMORY_MAX());
		System.out.println("PROC_SIZE_MAX = " + configReader.getPROC_SIZE_MAX());
		System.out.println("NUM_PROC = " + configReader.getNUM_PROC());
		System.out.println("MAX_PROC_TIME = " + configReader.getMAX_PROC_TIME());
		
		ContigousMemoryAllocator mmu = new ContigousMemoryAllocator(configReader.getMEMORY_MAX());
		
		//Randomly generate partitions given the max's above

	}

}
