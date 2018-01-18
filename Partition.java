public class Partition {
    
    private int StartAddress;
    private int Size;
    private String Status;
    
    public Partition(){
        this.StartAddress = -1;
        this.Status = "";
        this.Size = 0;
    }
    
    
    public Partition(int StartAddress, int Size, String Status){
        this.StartAddress = StartAddress;
        this.Status = Status;
        this.Size = Size;
    }
    public Partition(int Size, String Status){
        this.StartAddress = -1;
        this.Status = Status;
        this.Size = Size;
    }
    
    public void setStartAddress(int StartAddress){
        this.StartAddress = StartAddress;
    }
    
    public void setStatus(String Status){
        this.Status = Status;
    }
    
    public int getStartAddress(){
        return this.StartAddress;
    }
    
    public String getStatus(){
        return this.Status;
    }
    
    public void setSize(int Size){
        this.Size = Size;
    }
    
    public int getSize(){
        return this.Size;
    }
    
    public void increaseSize(int additionalSize){
        this.Size += additionalSize;
    }
    
    
}
