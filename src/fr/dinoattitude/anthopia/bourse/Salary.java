package fr.dinoattitude.anthopia.bourse;

public enum Salary {
	
	LEVEL_0(0, 20, 0),
	LEVEL_10(10, 20, 10),
	LEVEL_50(50, 20, 15),
	LEVEL_60(60, 25, 15),
	LEVEL_70(70, 25, 20),
	LEVEL_90(90, 30, 20),
	LEVEL_99(99, 35, 25),
	LEVEL_MAX(100, 40, 30);
	
	private int level, gainAlloc, gainSalary;

	private Salary(int level, int gainAlloc, int gainSalary) {
		this.level = level;
		this.gainAlloc = gainAlloc;
		this.gainSalary = gainSalary;
	}

	public int getLevel() {
		return level;
	}

	public int getGainAlloc() {
		return gainAlloc;
	}

	public int getGainSalary() {
		return gainSalary;
	}

}
