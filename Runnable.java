/* Author: Matthew Liffrig & Joseph Stiehm
 * Date: 4/16/2023
 * File: Runnable.java
 * Description: Runs the Contiguous Memory Allocator
*/
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Runnable {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		// variable declaration
		String configPath = "src/config.txt";
		List<Process> procList = new ArrayList<>();
		Random rand = new Random();
		int sysTime = 0;
		int remainingProcs;
		int nextProcessSize = 0;
		int nextProcessDuration = 0;
		String nextProcess = "N/A";


		// reads and prints current system configuration
		ConfigReader configReader = new ConfigReader(configPath);
		System.out.println("---Current Memory Configurations---");
		System.out.println("MEMORY_MAX = " + configReader.getMEMORY_MAX());
		System.out.println("PROC_SIZE_MAX = " + configReader.getPROC_SIZE_MAX());
		System.out.println("NUM_PROC = " + configReader.getNUM_PROC());
		System.out.println("MAX_PROC_TIME = " + configReader.getMAX_PROC_TIME());
		System.out.println("System Time: " + sysTime);

		// creates a ContigousMemoryAllocator
		ContiguousMemoryAllocator mmu = new ContiguousMemoryAllocator(configReader.getMEMORY_MAX());

		// Randomly generate partitions given the max's above

		for (int i = 0; i < configReader.getNUM_PROC(); i++) {
			procList.add(new Process("P" + Integer.toString(i), rand.nextInt(configReader.getPROC_SIZE_MAX() + 1),
					rand.nextInt(configReader.getMAX_PROC_TIME() + 1)));
		} //end for loop

		remainingProcs = procList.size();
		

		while (remainingProcs > 0) {

			System.out.println("\nPress Enter to continue by 1 second(1000ms).\n");
			scanner.nextLine();
			sysTime++;
			
			for (Process p : procList) {
				
				if (!p.getIsAllocated() && !p.getIsTerminated()) {
					if (mmu.first_fit(p, sysTime) > 0) {
						p.setIsAllocated(true);
						System.out.println("Allocated " + p.getSize() + "B to process " + p.getName());
					} else {
						System.out.println("Unable to allocate: " + p.getName());
					} //end conditional
				} //end conditional

			} //end for loop

			for (Process p : procList) {
				if (p.getIsAllocated() && !p.getIsTerminated() && p.getStartTime() + p.getTime() <= (sysTime * 1000)) {
					if (mmu.release(p.getName()) > 0) {
						p.setIsTerminated(true);
						remainingProcs--;
						System.out.println("Release memory allocated to process " + p.getName() + " " + p.getSize() + "B");
						mmu.merge_holes();
					} else {
						System.out.println("Process not found!");
					} //end conditional
				} //end conditional
			} //end for loop

			for (Process p : procList) {
				if(!p.getIsTerminated() && p.getStartTime() == -1) {
					nextProcess = p.getName();
					nextProcessSize = p.getSize();
					nextProcessDuration = p.getSize();
					break;
				}
			}
			System.out.print("\nNext Process to be allocated: " + nextProcess + " " + nextProcessSize + "B - " + nextProcessDuration + "ms" );
			System.out.println("\nSystem time: " + sysTime + " (seconds)");
			System.out.println("\n---FIRST FIT---\n");
			mmu.print_status(sysTime);
		} //end while(remainingProcs > 0)

		System.out.println("All processes terminated.");
		scanner.close();
		
	} //end main()
} //end Runnable()
