public class Process {
    public int size;
    public String name;
    public int assigned_partition_index;
    public Process()
    {
        name="";
        size=0;
        assigned_partition_index=-1;
    }
    public Process(String name,int size)
    {
        this.name=name;
        this.size=size;
        assigned_partition_index=-1;
    }
}
