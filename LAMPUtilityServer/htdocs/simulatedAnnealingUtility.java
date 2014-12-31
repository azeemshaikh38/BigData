import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class simulatedAnnealingUtility {

	public static ArrayList<Task> allTasks = new ArrayList<Task>();
	private static double totalWeight = 0;
	static int shifted = 0;
	static double weight;
	public static HashMap<Integer, Double> priceMap = new HashMap<Integer, Double>();
	public static HashMap<Integer, Double> priceMapNew = new HashMap<Integer, Double>();
	private static Integer[] sortedPriceHours = new Integer[24];
	private static Random randForAnnealing = new Random(System.currentTimeMillis());
	private static Integer[] renewableSource = new Integer[24];
	public static Integer[] powerArray = new Integer[24];
	public static Integer[] powerThreshold = new Integer[24];

	public void iterativeSchedule() {
		System.out.println("iterativeSchedule");
	}

	public static void main(String[] args) throws IOException {
		// Read file and create tasks;
		File appliancesFile = new File(args[0]);
		File priceByHourFile = new File(args[1]);
		BufferedReader appliancesBufferedFile = new BufferedReader(
				new FileReader(appliancesFile));
		BufferedReader priceBufferedFile = new BufferedReader(new FileReader(
				priceByHourFile));
		String line = null;
		while ((line = appliancesBufferedFile.readLine()) != null) {
			String[] applianceSplit = line.replaceAll("\\s+", "").split(",");
			allTasks.add(new Task(applianceSplit[0], 
					Integer.parseInt(applianceSplit[3]), 
					Integer.parseInt(applianceSplit[4]), 
					Integer.parseInt(applianceSplit[5]), 
					Integer.parseInt(applianceSplit[2]), 
					Double.parseDouble(applianceSplit[1])));
		}

		// Read file and get priceArray and sortedPriceHours
		line = null;
		int read = 0;
		while ((line = priceBufferedFile.readLine()) != null) {
			if (read == 1) {
				//System.out.println(line);
				String[] priceSplit = line.split(",");
				for (int i = 0; i < 24; i++) {
					priceMap.put(i, Double.parseDouble(priceSplit[i + 1]));
					priceMapNew.put(i, new Double(Double.parseDouble(priceSplit[i + 1]) + 1 + 9*randForAnnealing.nextDouble()));
				}
				read = 0;
			}
			if (line.contains("Start of Day Ahead Energy Price Data")) {
				read = 1;
			}
		}

		//Assign all extra variables
		for (int i=0; i<24; i++) {
                        renewableSource[i] = randForAnnealing.nextInt(70);
			powerThreshold[i] = randForAnnealing.nextInt(100);
			powerArray[i] = randForAnnealing.nextInt(20);	
                }

		//Iteration: Simulated Annealing
		ArrayList<Task> copyOfAllTaskList = new ArrayList<Task>();
		int simulatedAnnealingItr = 0;
		double minPrice = Double.POSITIVE_INFINITY;
		double minWattage = Double.POSITIVE_INFINITY;

		while (simulatedAnnealingItr < 100000) {
			randomizeAllTaskSchedule();
			getHourWisePower(powerArray);
			double totalPrice = getTotalPrice();
			//double totalWattage = getTotalWattage();
			//System.out.println("Iteration: " + simulatedAnnealingItr
			//		+ " Total Price: " + totalPrice + " minPrice: " + minPrice
			//		+ " minWattage: " + minWattage);
			if (totalPrice < minPrice) {
				minPrice = totalPrice;
				// Save the entire allTask
				copyOfAllTaskList.clear();
				for (int i = 0; i < allTasks.size(); i++) {
					copyOfAllTaskList
							.add(new Task(allTasks.get(i).getName(), allTasks
									.get(i).getStartTime(), allTasks.get(i)
									.getEndTime(), allTasks.get(i)
									.getNumberOfTasks(), allTasks.get(i)
									.getWattage(), allTasks.get(i).getWeight()));
					for (int j = 0; j < allTasks.get(i).assignedHours.size(); j++) {
						copyOfAllTaskList.get(i).assignedHours.add(new Integer(
								allTasks.get(i).assignedHours.get(j)));
					}
				}
			}
			simulatedAnnealingItr++;
		}

		//for (int i = 0; i < copyOfAllTaskList.size(); i++) {
		//	System.out.println(copyOfAllTaskList.get(i).getAssignedHours()
		//			.toString());
		//}
		allTasks.clear();
		for (int r = 0; r < copyOfAllTaskList.size(); r++) {
			allTasks.add(new Task(copyOfAllTaskList.get(r).getName(),
					copyOfAllTaskList.get(r).getStartTime(), copyOfAllTaskList
							.get(r).getEndTime(), copyOfAllTaskList.get(r)
							.getNumberOfTasks(), copyOfAllTaskList.get(r)
							.getWattage(), copyOfAllTaskList.get(r).getWeight()));
			for (int i = 0; i < copyOfAllTaskList.get(r).assignedHours.size(); i++) {
				allTasks.get(r).assignedHours.add(new Integer(copyOfAllTaskList
						.get(r).assignedHours.get(i)));
			}
		}

		getHourWisePower();	
		// for (int j = 0; j < copyOfAllTaskList.size(); j++) {
		// System.out.println(copyOfAllTaskList.get(j).getName() + "\n");
		// System.out.println(copyOfAllTaskList.get(j).assignedHours + "\n");
		// }
		//System.out.println("Total Price: " + getTotalPrice()
		//		+ " Total wattage: " + getTotalWattage());
	}

	public static void randomizeAllTaskSchedule() {
		double totalPenalty = 0;
		Iterator<Task> taskItr = allTasks.iterator();
		while (taskItr.hasNext()) {
			Task currentTask = taskItr.next();
			currentTask.assignedHours.clear();
			int randFound = 0;
			while (randFound < currentTask.getNumberOfTasks()) {
				int newRand = randForAnnealing.nextInt(24);
				if (!(currentTask.assignedHours.contains(newRand))
						&& ((currentTask.calculatePenalty(newRand) + totalPenalty) < 1)) {
					currentTask.assignedHours.add(newRand);
					randFound++;
					totalPenalty += currentTask.calculatePenalty(newRand);
				}
			}
		}
	}

	public static double getTotalPrice() {
		double sumPrice = 0;
		Iterator<Task> taskItr = allTasks.iterator();
		while (taskItr.hasNext()) {
			sumPrice += taskItr.next().getPrice(priceMap, priceMapNew, powerArray, powerThreshold);
		}
		return sumPrice;
	}

	public static double getTotalWattage() {
		double sumWattage = 0;
		Iterator<Task> taskItr = allTasks.iterator();
		while (taskItr.hasNext()) {
			Task tsk = taskItr.next();
			sumWattage += tsk.getWattage() * tsk.getNumberOfTasks();
		}
		return sumWattage;
	}

	public static void getHourWisePower() throws IOException {
		int[] wattArray = new int[24];
		PrintWriter fpOut = new PrintWriter("hourWiseDukePower.txt");
		Iterator<Task> taskItr = allTasks.iterator();
		while (taskItr.hasNext()) {
			Task tmpTask = taskItr.next();
			for (int i=0; i<tmpTask.assignedHours.size(); i++) {
				wattArray[tmpTask.assignedHours.get(i)] += tmpTask.getWattage();
				//powerArray[tmpTask.assignedHours.get(i)] += tmpTask.getWattage();
			}
		}
		for (int i=0; i < 24; i++) {
			if (renewableSource[i] < wattArray[i]) {
				fpOut.println(i+"\t"+(wattArray[i]-renewableSource[i]));
			} else {
				fpOut.println(i+"\t"+0);
			}
		}
		fpOut.close();
	}

	public static void getHourWisePower(Integer[] powerArr) {
                Iterator<Task> taskItr = allTasks.iterator();
                while (taskItr.hasNext()) {
                        Task tmpTask = taskItr.next();
                        for (int i=0; i<tmpTask.assignedHours.size(); i++) {
                                powerArray[tmpTask.assignedHours.get(i)] += tmpTask.getWattage();
                        }
                }
        }


}

class Task {
	private String name;
	private int startTime;
	private int endTime;
	private int numberOfTasks;
	private int wattage;
	private double weight;
	public List<Integer> assignedHours = new ArrayList<Integer>();

	public Task(String name, int startTime, int endTime, int numberOfTasks,
			int wattage, double weight) {
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.numberOfTasks = numberOfTasks;
		this.wattage = wattage;
		this.weight = weight;
	}

	public void initialSchedule(Integer[] sortedPriceHours) {
		for (int i = 0; i < numberOfTasks; i++) {
			assignedHours
					.add(sortedPriceHours[sortedPriceHours.length - i - 1]);
		}
	}

	public double calculatePenalty(int hour) {
		if (hour < startTime) {
			return 1;
		} else if (hour >= endTime) {
			return ((hour - endTime + 1) * (weight));
		} else {
			return 0;
		}
	}

	public double getPrice(HashMap<Integer, Double> priceMap) {
		double sumPrice = 0;
		for (int i = 0; i < assignedHours.size(); i++) {
			sumPrice += wattage*priceMap.get(assignedHours.get(i));
		}
		return sumPrice;
	}

	public double getPrice(HashMap<Integer, Double> priceMapOld, HashMap<Integer, Double> priceMapNew, Integer[] powerUsage, Integer[] peakDemand ) {
                double sumPrice = 0;
                for (int i = 0; i < assignedHours.size(); i++) {
			if (powerUsage[assignedHours.get(i)] <= peakDemand[assignedHours.get(i)]) {
	                        sumPrice += wattage*priceMapOld.get(assignedHours.get(i));
			} else {
				sumPrice += wattage*priceMapNew.get(assignedHours.get(i));
			}
                }
                return sumPrice;
        }

	public int getWattage() {
		return wattage;
	}

	public int getNumberOfTasks() {
		return numberOfTasks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public List<Integer> getAssignedHours() {
		return assignedHours;
	}

	public void setAssignedHours(List<Integer> assignedHours) {
		this.assignedHours = assignedHours;
	}

	public void setNumberOfTasks(int numberOfTasks) {
		this.numberOfTasks = numberOfTasks;
	}

	public void setWattage(int wattage) {
		this.wattage = wattage;
	}

}
