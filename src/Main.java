
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args){
        
        int MemorySize;
        System.out.print("Enter Memory Size : ");
        Scanner scanner = new Scanner(System.in);
        MemorySize = scanner.nextInt();
        
        MemorySimulator MS = new MemorySimulator(MemorySize);
        
        MS.Run();
        
        
    }
    
}
