package view;
import java.util.concurrent.Semaphore;
import controller.DungeonsDragonsThread;

public class Main {

	public static void main(String[] args) {
		DungeonsDragonsThread t1 = new DungeonsDragonsThread();
		DungeonsDragonsThread t2 = new DungeonsDragonsThread();
		DungeonsDragonsThread t3 = new DungeonsDragonsThread();
		DungeonsDragonsThread t4 = new DungeonsDragonsThread();
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
	}

}
