import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContigousMemoryAllocator {

	private int MAXsize; // maximum memory size in bytes (B)

	public Map<String, Partition> allocMap; // map process to partition
	private List<Partition> partList; // list of memory partitions

	// constructor
	public ContigousMemoryAllocator(int size) {
		this.MAXsize = size;
		this.allocMap = new HashMap<>();
		this.partList = new ArrayList<>();
		this.partList.add(new Partition(0, MAXsize)); // add the first hole, which is the whole memory at start up
	}

	// prints the allocation map (free + allocated) in ascending order of base
	// addresses
	public void print_status(int sysTime) {
		// TODO: add code below
		order_partitions(); // sort list of partitions in ascending order of base addresses
		System.out.printf("Partitions [Allocated = %d B, Free = %d B]:\n", allocated_memory(), free_memory());
		for (int i = 0; i < partList.size(); i++) {
			Partition part = partList.get(i); // get the partition at index i
			int start = part.getBase();
			int end = start + part.getLength() - 1;
			if(part.getStartTime() != sysTime) {
				part.setRemainingTime(part.getTime() - sysTime*1000);
			}
			String status = part.isFree() ? "Free" : part.getProcess() + "(" + part.getRemainingTime() + "ms)";
			String partSize = "(" + part.getLength() + "B)";
			System.out.printf("Address [%06d:%06d] %s %s\n", start, end, status, partSize);            //NEEDS TIME REMAINING
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
	public int first_fit(Process process, int sysTime) {
		// TODO: add code below
		if (allocMap.containsKey(process.getName())) // make sure process is not allocated before
			return -1; // duplicate process
		int index = 0;
		int alloc = -1;
		while (index < partList.size()) {
			Partition part = partList.get(index);
			if (part.isFree() && part.getLength() >= process.getSize()) {
				Partition newPart = new Partition(part.getBase(), process.getSize(), process.getTime(), sysTime);
				newPart.setFree(false);
				newPart.setProcess(process.getName());
				newPart.setRemainingTime(process.getTime());
				partList.add(index, newPart);
				allocMap.put(process.getName(), newPart);
				process.setStartTime(sysTime);
				// update current part
				part.setBase(part.getBase() + process.getSize());
				part.setLength(part.getLength() - process.getSize());
				if (part.getLength() == 0)
					partList.remove(part); // remove empty memory hole
				alloc = process.getSize();
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
}
