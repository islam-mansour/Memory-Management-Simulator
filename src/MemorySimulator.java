
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class MemorySimulator {
    
    private int MemorySize;
    private ArrayList<Partition> Partitions;
    
    public MemorySimulator(){
        this.MemorySize = 0;
        this.Partitions = new ArrayList<>();
    }
    
    public MemorySimulator(int MemorySize){
        this.MemorySize = MemorySize;
        this.Partitions = new ArrayList<>();
        this.Partitions.add(new Partition(0, MemorySize, "Free"));
    }
    
    public void Run(){
        
        
        int Ch;
        while( (Ch = getChoice()) != -1){
            
            if (Ch == 1){
                
                int PolicyType = 4;
                while(PolicyType > 3 || PolicyType < 1){
                    System.out.print("\nEnter Policy Type : ");
                    Scanner scanner = new Scanner(System.in);
                    PolicyType = scanner.nextInt();
                }
                
                if (!MemoryAllocate(PolicyType)){
                    
                    System.err.println("\nPartition Ocuupation Failed");
                    
                } else {
                    
                    System.out.println("\nPartition Ocuupation Succeded");
                    
                }
                
            } else if (Ch == 2){
                
                if (!MemoryDeallocate()){
                    
                    System.err.println("\nPartition Deallocation Faild");
                
                } else {
                    
                    System.out.println("\nPartition Deallocation Succeded");
                    
                }
                
            } else if (Ch == 3){
                
                ShowPartitions();
                
            } else if (Ch == 4){
                
                ContiguousBlocksDefragmentation();
                
            } else if (Ch == 5){
                
                nonContiguousBlocksDefragmentation();
                
            } else {
                
                System.err.println("\n Wrond Input");
                
            }
            
        }
        
    }
    
    private boolean MemoryAllocate(int PolicyType){
        
        int Size;
        System.out.print("\nEnter the Size of the Partition : ");
        Scanner scanner = new Scanner(System.in);
        Size = scanner.nextInt();
        
        Partition New = new Partition(Size, "Occupied");
        
        int ind = -1;
        if (PolicyType == 1){ // Best Fit
            
            ind = BestFit(New); // get index of Free partition that satisfies th ploicy
        
        } else if (PolicyType == 2){ // Worst Fit
            
            ind = WorstFit(New);
            
        } else if (PolicyType == 3){ // First Fit
            
            ind = FirstFit(New);
            
        }
        
        
        if (ind == -1) { // no free partition satisfies the policy
            
            return false;
                
        } else {
            
            Partition Chosen = this.Partitions.get(ind); // get the chosen partition returned from the policy
            int start = Chosen.getStartAddress();
            New.setStartAddress(start);
            
            //eliminating internal fragmentation)
            
            this.Partitions.remove(ind);
            this.Partitions.add(ind, New);
            
            int remainingSize = Chosen.getSize() - Size;
            if (remainingSize != 0){
                int startFree = start + Size;
                this.Partitions.add(ind+1, new Partition(startFree, remainingSize, "Free"));
            }
            
            
            return true;
            
        }
        
    }
    
    private int BestFit(Partition New){
        int ind = -1, mn = 1000000;
        for(int i=0; i<Partitions.size(); ++i){
            if (Partitions.get(i).getStatus().equals("Free")){
                if (Partitions.get(i).getSize() >= New.getSize() 
                     && Partitions.get(i).getSize() < mn){
                    mn = Partitions.get(i).getSize();
                    ind = i;
                }
            }
        }
        
        return ind;
    }
    
    private int WorstFit(Partition New){
        int ind = -1, mx = 0;
        for(int i=0; i<Partitions.size(); ++i){
            if (Partitions.get(i).getStatus().equals("Free")){
                if (Partitions.get(i).getSize() >= New.getSize() 
                     && Partitions.get(i).getSize() > mx){
                    mx = Partitions.get(i).getSize();
                    ind = i;
                }
            }
        }
        
        return ind;
    }
    
    private int FirstFit(Partition New){
        for(int i=0; i<Partitions.size(); ++i){
            if (Partitions.get(i).getStatus().equals("Free")){
                if (Partitions.get(i).getSize() >= New.getSize()){
                    return i;
                }
            }
        }
        
        return -1;
        
    }
    
    private boolean MemoryDeallocate(){
        
        ShowPartitions(); // Show all Partitions and its Status
        
        int startAddress;
        System.out.print("\n Enter the Start Address of the Partition : ");
        Scanner scanner = new Scanner(System.in);
        startAddress = scanner.nextInt();
        
        for(int i=0; i<Partitions.size(); ++i){
            
            if (Partitions.get(i).getStartAddress() == startAddress && Partitions.get(i).getStatus() != "Free"){
                
                Partitions.get(i).setStatus("Free");
                
                return true;
                
            }
            
        }
        
        return false;
        
    }
    
    private void ContiguousBlocksDefragmentation(){
        
        for(int i=0; i<this.Partitions.size()-1; ++i){
            
            if  ( this.Partitions.get(i).getStatus() == "Free"
               && this.Partitions.get(i+1).getStatus() == "Free"){
                
                this.Partitions.get(i).increaseSize(this.Partitions.get(i+1).getSize());
                this.Partitions.remove(i+1);
                
                i--;
            }
            
        }
        
    }
    
    private void nonContiguousBlocksDefragmentation(){
        
        int AllfreeSpace = getAllFreeSpaceinMemory();
        removeFreeParitions();
        int start = shiftOcuupiedPartitions();
        
        this.Partitions.add(new Partition(start, AllfreeSpace, "Free"));
        
    }
    
    private int shiftOcuupiedPartitions(){
        
        int start = 0;
        
        for(int i=0; i<this.Partitions.size(); ++i){
        
            if (this.Partitions.get(i).getStartAddress() != start){
                this.Partitions.get(i).setStartAddress(start);
            }
            
            start = this.Partitions.get(i).getStartAddress() + this.Partitions.get(i).getSize();
            
        }
        return start;
    }
    
    
    private void removeFreeParitions(){
        
        for(int i=0; i<this.Partitions.size(); ++i){
            
            if (this.Partitions.get(i).getStatus() == "Free"){
                
                this.Partitions.remove(i);
                i--;
                
            }
            
        }
        
    }
    
    private int getAllFreeSpaceinMemory(){
        
        int sum = 0;
        
        for(int i=0; i<this.Partitions.size(); ++i){
            
            if (this.Partitions.get(i).getStatus() == "Free"){
                sum += this.Partitions.get(i).getSize();
            }
            
        }
        
        return sum;
        
    }
    
    private void ShowPartitions(){
        
        System.out.println("\nCurrently Partitions in the Memory : ");
        
        for(int i=0; i<this.Partitions.size(); ++i){
            
            int start = this.Partitions.get(i).getStartAddress();
            int size = this.Partitions.get(i).getSize();
            String status = this.Partitions.get(i).getStatus();
            
            System.out.println(
                 "\tFrom: " + start + ","
               + "\tTo: " + (start+size-1) + ","
               + "\tStatus: " + status
            );
            
        }
    
    }
    
    private int getChoice(){
        
        System.out.println("\n1. Memory Allocation.");
        System.out.println("2. Memory Deallocation.");
        System.out.println("3. Show Partitions.");
        System.out.println("4. Contiguous Defragmentation");
        System.out.println("5. non Contiguous Defragmentation");
        System.out.println("-1. Exit.");
        
        int Choice = -2;
        Scanner scanner = new Scanner(System.in);
        
        while(Choice > 5 || Choice < -1){
            System.out.print("\nWhat do you Want to do : ");
            Choice = scanner.nextInt();
        }
        
        return Choice;
    }
    
    /*class SortByStartAddress implements Comparator<Partition>{

        @Override
        public int compare(Partition P1,Partition P2) {
           return P1.getStartAddress()-P2.getStartAddress();
        }
        
    };*/
    
}
