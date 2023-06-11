public class Partition {
    public int size;
    public int fitted;
    public int assigned_process_index;
    public String name;
    public int remaining_space;
    public Partition()
    {
        name="";
        size=0;
        remaining_space=0;
        fitted=0;
        assigned_process_index=-1;
    }
    public Partition(String name,int size)
    {
        this.name=name;
        this.size=size;
        remaining_space=size;
        fitted=0;
        assigned_process_index=-1;
    }
}
