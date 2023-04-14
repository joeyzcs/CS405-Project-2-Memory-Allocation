import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ContigousMemoryAllocator {
	private int size; // maximum memory size in bytes (B)
	private Map<String, Partition> allocMap; // map process to partition
	private List<Partition> partList; // list of memory partitions
	// constructor

	public ContigousMemoryAllocator(int size) {
		this.size = size;
		this.allocMap = new HashMap<>();
		this.partList = new ArrayList<>();
		this.partList.add(new Partition(0, size)); // add the first hole, which is the whole memory at start up
	}

	// prints the list of available commands
	public void print_help_message() {
		// TODO: add code below
		System.out.println("RQ <process> <size> to request memory of size for the process");
		System.out.println("RL <process> to release the memory of the process");
		System.out.println("STAT to show the memory allocation status");
		System.out.println("EXIT to exit");
		System.out.println("HELP to show the available commands");
	}

	// prints the allocation map (free + allocated) in ascending order of base
	// addresses
	public void print_status() {
		// TODO: add code below
		order_partitions(); // sort list of partitions in ascending order of base addresses
		System.out.printf("Partitions [Allocated = %d B, Free = %d B]:\n", allocated_memory(), free_memory());
		for (int i = 0; i < partList.size(); i++) {
			Partition part = partList.get(i); // get the partition at index i
			int start = part.getBase();
			int end = start + part.getLength() - 1;
			String status = part.isFree() ? "Free" : part.getProcess();
			String partSize = "(" + part.getLength() + "B)";
			System.out.printf("Address [%06d:%06d] %s %s\n", start, end, status, partSize);
		}
	}

	// get the size of total allocated memory
	private int allocated_memory() {
		// TODO: add code below
		int allocated = 0;
		for (Partition part : partList)
			if (!part.isFree())
				allocated += part.getLength();
		return allocated;

	}

	// get the size of total free memory
	private int free_memory() {
		// TODO: add code below
		int free = 0;
		for (Partition part : partList)
			if (part.isFree())
				free += part.getLength();
		return free;

	}

	// sort the list of partitions in ascending order of base addresses
	private void order_partitions() {
		// TODO: add code below
		partList.sort(new Comparator<Partition>() {

			@Override
			public int compare(Partition o1, Partition o2) {
				// TODO Auto-generated method stub
				return o1.getBase() - o2.getBase();
			}
		});

	}

	// implements the first fit memory allocation algorithm
	public int first_fit(String process, int size) {
		// TODO: add code below
		if (allocMap.containsKey(process)) // make sure process is not allocated before
			return -1; // duplicate process
		int index = 0;
		int alloc = -1;
		while (index < partList.size()) {
			Partition part = partList.get(index);
			if (part.isFree() && part.getLength() >= size) {
				Partition newPart = new Partition(part.getBase(), size);
				newPart.setFree(false);
				newPart.setProcess(process);
				partList.add(index, newPart);
				allocMap.put(process, newPart);
				// update current part
				part.setBase(part.getBase() + size);
				part.setLength(part.getLength() - size);
				if (part.getLength() == 0)
					partList.remove(part); // remove empty memory hole
				alloc = size;
				break;
			}
			index++; // try next hole
		}
		return alloc;
	}

	// release the allocated memory of a process
	public int release(String process) {
		// TODO: add code below
		int free = -1;
		for (Partition part : partList) {
			if (!part.isFree() && process.equals(part.getProcess())) {
				part.setFree(true);
				part.setProcess(null);
				free = part.getLength();
				break;
			}
		}
		if (free < 0)
			return free;
		merge_holes();
		return free;
	}

	// procedure to merge adjacent holes
	private void merge_holes() {
		// TODO: add code below
		order_partitions();
		int i = 0;
		while (i < partList.size() - 1) {
			Partition partI = partList.get(i);
			if (!partI.isFree()) {
				i++;
				continue;
			}
			int end_i = partI.getBase() + partI.getLength() - 1;
			int j = i + 1;
			while (j < partList.size() && partList.get(j).isFree()) {
				int start_j = partList.get(j).getBase();
				if (end_i - start_j == -1) {
					partI.setLength(partI.getLength() + partList.get(j).getLength());
					partList.remove(j);
				}
				break;
			}
			i++; // try next partition to merge
		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the max memory size: ");
		int size = Integer.parseInt(sc.nextLine());
		ContigousMemoryAllocator mmu = new ContigousMemoryAllocator(size);
		String configPath = "src/config.txt";
		ConfigReader configReader = new ConfigReader(configPath);
		System.out.println("MEMORY_MAX = " + configReader.getMEMORY_MAX());
		System.out.println("PROC_SIZE_MAX = " + configReader.getPROC_SIZE_MAX());
		System.out.println("NUM_PROC = " + configReader.getNUM_PROC());
		System.out.println("MAX_PROC_TIME = " + configReader.getMAX_PROC_TIME());
		while (true) {
			System.out.print("mmu> ");
			String line = sc.nextLine();
			String[] arr = line.split(" ");
			String command = arr[0].toUpperCase();
			if (command.equals("HELP")) {
				mmu.print_help_message();
			} else if (command.equals("EXIT")) {
				System.out.println("Exit program. Bye");
				break;
			} else if (command.equals("STAT")) {
				mmu.print_status();
			} else if (command.equals("RQ")) {
				if (arr.length < 3) {
					System.out.println("Not enough parameters!");
					continue;
				}
				String process = arr[1];
				int rqSize = Integer.parseInt(arr[2]);
				if (mmu.first_fit(process, rqSize) > 0)
					System.out.println("Allocated " + rqSize + " B to process " + process);
				else
					System.out.println("Duplicate process or not enough memory");
			} else if (command.equals("RL")) {
				if (arr.length < 2) {
					System.out.println("Not enough parameters!");
					continue;
				}
				String process = arr[1];
				if (mmu.release(process) > 0)
					System.out.println("Successfully released memory for process " + process);
				else
					System.out.println("Not found process!");
			} else {
				System.out.println("Invalid command!");
			}
		}
	}
}