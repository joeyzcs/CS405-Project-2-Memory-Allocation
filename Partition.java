public class Partition {
	// the representation of each memory partition
	private int base; // base address
	private int length; // partition size
	private int time; // duration that process is in memory
	private boolean bFree; // status: free or allocated
	private String process; // assigned process if allocated
	private int remainingTime;

	// constructor method
	public Partition(int base, int length) {
		this.base = base;
		this.length = length;
		this.time = 0;
		this.bFree = true; // free by default when creating
		this.process = null; // unallocated to any process
	}

	public Partition(int base, int length, int time) {
		this.base = base;
		this.length = length;
		this.time = time;
		this.bFree = true; // free by default when creating
		this.process = null; // unallocated to any process
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public boolean isFree() {
		return bFree;
	}

	public void setFree(boolean bFree) {
		this.bFree = bFree;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public int getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
	}

	public String toString() {
		return "base=" + base + ", length=" + length + ", bFree=" + bFree + ", process=" + process;
	}

}
