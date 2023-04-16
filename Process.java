
public class Process {
	
	private String name;
	private int size;
	private int time;
	private int startTime;
	private boolean isAllocated;
	private boolean isTerminated;
	
	public Process(String name, int size, int time) {
		super();
		this.name = name;
		this.size = size;
		this.time = time;
		this.startTime = -1;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public boolean getIsAllocated() {
		return isAllocated;
	}

	public void setIsAllocated(boolean isAllocated) {
		this.isAllocated = isAllocated;
	}

	public boolean getIsTerminated() {
		return isTerminated;
	}

	public void setIsTerminated(boolean isTerminated) {
		this.isTerminated = isTerminated;
	}

}
