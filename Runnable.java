import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Runnable {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);

		String configPath = "src/config.txt";
		List<Process> procList = new ArrayList<>();
		Random rand = new Random();
		int sysTime = 0;

		ConfigReader configReader = new ConfigReader(configPath);
		System.out.println("MEMORY_MAX = " + configReader.getMEMORY_MAX());
		System.out.println("PROC_SIZE_MAX = " + configReader.getPROC_SIZE_MAX());
		System.out.println("NUM_PROC = " + configReader.getNUM_PROC());
		System.out.println("MAX_PROC_TIME = " + configReader.getMAX_PROC_TIME());

		ContigousMemoryAllocator mmu = new ContigousMemoryAllocator(configReader.getMEMORY_MAX());

		// Randomly generate partitions given the max's above

		for (int i = 0; i < configReader.getNUM_PROC(); i++) {
			procList.add(new Process("P" + Integer.toString(i), rand.nextInt(configReader.getPROC_SIZE_MAX() + 1),
					rand.nextInt(configReader.getMAX_PROC_TIME() + 1)));
		}

		int remainingProcs = procList.size();

		while (remainingProcs > 0) {

			System.out.println("Press Enter to continue by 1 second");

			scanner.nextLine();
			sysTime++;

			for (Process p : procList) {

				if (!p.getIsAllocated() && !p.getIsTerminated()) {
					if (mmu.first_fit(p, sysTime) > 0) {
						p.setIsAllocated(true);
						System.out.println("Allocated " + p.getSize() + "B to process " + p.getName());
					} else {
						System.out.println("Unable to allocate: " + p.getName());
					}
				}

			}

			for (Process p : procList) {
				if (p.getIsAllocated() && !p.getIsTerminated() && p.getStartTime() + p.getTime() <= (sysTime * 1000)) {
					if (mmu.release(p.getName()) > 0) {
						p.setIsTerminated(true);
						remainingProcs--;
						System.out.println("Release memory allocated to process " + p.getName());
					} else {
						System.out.println("Process not found!");
					}
				}
			}

			System.out.println("System time: " + sysTime + " (seconds)");
			mmu.print_status();
		}

		System.out.println("All processes terminated.");
		scanner.close();

//		for (int i = 0; i < procList.size(); i++) {
//
//			/*
//			 * for(int j = 0; j < procList.size(); j++) { if(procList.get(j).getStartTime()
//			 * + procList.get(j).getTime() == sysTime) { System.out.println("Test");
//			 * if(mmu.release(procList.get(j).getName()) > 0)
//			 * System.out.println("Release memory allocated to process " +
//			 * procList.get(j).getName()); else System.out.println("Process not found!"); }
//			 * }
//			 */
//
//			if (i == 0)
//				System.out.println("\n---FIRST FIT---");
//			if (mmu.first_fit(procList.get(i), sysTime) > 0)
//				System.out.println(
//						"Allocated " + procList.get(i).getSize() + "B to process " + procList.get(i).getName());
//			else
//				System.out.println("Unable to allocate: " + procList.get(i).getName());
//
//			// mmu.first_fit(procList.get(i), sysTime);
//		}
//
//		System.out.println();
//		mmu.print_status();

	}

}
