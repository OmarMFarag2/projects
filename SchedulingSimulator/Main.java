import java.util.Scanner;
import java.util.ArrayList;
public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner (System.in);
        System.out.print("Enter number of partitions: ");
        scan = new Scanner(System.in);
        int NumOfPartitions= scan.nextInt();
        ArrayList<Partition> partitions= new ArrayList<>();
        for(int i=0;i<NumOfPartitions;i++) {
            System.out.print("enter partitions " + (i + 1) + " name ");
            scan = new Scanner(System.in);
            String name = scan.nextLine();
            System.out.print("enter partitions " + (i + 1) + " size: ");
            scan = new Scanner(System.in);
            int size = scan.nextInt();
            partitions.add(new Partition(name, size));
        }
        System.out.print("Enter number of processes : ");
        int process_num = scan.nextInt();
        Process[] process = new Process[process_num];
        for(int i=0;i<process_num;i++) {
            System.out.print("enter process " + (i + 1) + " name: ");
            scan = new Scanner(System.in);
            String name = scan.nextLine();
            System.out.print("enter process "+(i+1)+" size: ");
            scan = new Scanner(System.in);
            int size= scan.nextInt();
            process[i] = new Process(name,size);
        }
        System.out.println("select policy:- ");
        System.out.println("1-first fit");
        System.out.println("2-worst fit");
        System.out.println("3-best fit");
        System.out.println("choice : ");
        int choice =scan.nextInt();
        switch (choice)
        {
            case 1:first_fit(partitions,process,process_num);
                    break;
            case 2: Worst_fit(partitions,process,process_num);
                    break;
            case 3: Best_fit(partitions,process,process_num);
        }
    }
    public static void first_fit( ArrayList<Partition> part,Process[] p,int process_num)
    {
        ArrayList<Integer> NotAllocated= new ArrayList<>();
        boolean assigned=false;
        int count=0;
        for(int i=0;i<part.size();i++)
        {
            assigned=false;
            for(int j=0;j<process_num;j++)
            {
                if(part.get(i).remaining_space>=p[j].size&& p[j].assigned_partition_index==-1)
                {
                    p[j].assigned_partition_index=i;
                    part.get(i).assigned_process_index=j;
                    assigned=true;
                    part.get(i).remaining_space-=p[j].size;
                    if(part.get(i).remaining_space!=0)
                    {
                        part.add(i+1,new Partition("partition"+Integer.toString(part.size()),part.get(i).remaining_space));
                        part.get(i).remaining_space=0;
                    }
                    part.get(i).fitted=p[j].size;
                    count++;
                    System.out.print(part.get(i).name);
                    System.out.print(" ( "+part.get(i).fitted+" KB ) ");
                    System.out.println("=> "+p[j].name);
                    break;
                }
            }
            if(count==process_num)
                break;
            if(!assigned)
                System.out.println(part.get(i).name+" ( "+part.get(i).size+" KB ) => External fragment");

        }
        for (int i=0;i<process_num;i++) {
            if (p[i].assigned_partition_index == -1) {
                System.out.println("****** "+p[i].name + " Can't be allocated ******");
                NotAllocated.add(i);
            }
        }
        System.out.println("do you want to compact? 1-yes,2-no :");
        Scanner scan=new Scanner(System.in);
        int choice=scan.nextInt();
        if(choice==1)
            compaction(NotAllocated,part, p,process_num);
    }
    public static boolean compaction(ArrayList<Integer> NotAllocated,ArrayList<Partition> part,Process[] p,int process_num)
    {
        int sum=0;
        int current_Parts_size=part.size();
        boolean allocated=true;
        ArrayList<Integer> empty=new ArrayList<>();
        for(int i=0;i<part.size();i++)
        {
            if(part.get(i).remaining_space!=0)
            {
                sum+=part.get(i).remaining_space;
                empty.add(i);
            }
            else
            {
                System.out.print(part.get(i).name);
                System.out.print(" ( "+part.get(i).fitted+" KB ) ");
                System.out.println("=> "+p[part.get(i).assigned_process_index].name);
            }
        }
        for (int i=0;i<empty.size();i++)
            part.remove(empty.get(i)-i);
        part.add(new Partition("partition"+Integer.toString(current_Parts_size),sum));
        current_Parts_size+=1;
        for(int i=0;i<NotAllocated.size();i++)
        {
            for(int j=0;j<part.size();j++)
            {
                if(p[NotAllocated.get(i)].size<=part.get(j).remaining_space)
                {
                    p[NotAllocated.get(i)].assigned_partition_index=j;
                    part.get(j).assigned_process_index=NotAllocated.get(i);
                    part.get(j).remaining_space-=p[NotAllocated.get(i)].size;
                    if(part.get(j).remaining_space!=0)
                    {
                        part.add(new Partition("partition"+Integer.toString(current_Parts_size),part.get(j).remaining_space));
                        current_Parts_size+=1;
                        part.get(j).remaining_space=0;
                    }
                    part.get(j).fitted=p[NotAllocated.get(i)].size;
                    System.out.print(part.get(j).name);
                    System.out.print(" ( "+part.get(j).fitted+" KB ) ");
                    System.out.println("=> "+p[NotAllocated.get(i)].name);
                    break;
                }
            }
            if(p[NotAllocated.get(i)].assigned_partition_index==-1)
            {
                System.out.println(p[i].name + " Can't be allocated");
                allocated=false;
            }

        }
        for(int j=0;j<part.size();j++)
        {
            if (part.get(j).remaining_space!=0)
                System.out.println(part.get(j).name+" ( "+part.get(j).size+" KB ) => External fragment");

        }
        return allocated;

    }
    public static void Best_fit(ArrayList<Partition> part, Process[] p, int process_num) {
        ArrayList<Integer> NotAllocated=new ArrayList<>();
        for (int i = 0; i < process_num; i++) {
            int min_partition = Integer.MAX_VALUE;
            int Idx_min_partition = 0;
            boolean assign = false;
            for (int j = 0; j < part.size(); j++) {
                if (part.get(j).remaining_space >= p[i].size && part.get(j).remaining_space <= min_partition) {
                    min_partition = part.get(j).remaining_space;
                    Idx_min_partition = j;
                    assign = true;
                }
            }
            if (assign) {
                p[i].assigned_partition_index = Idx_min_partition;
                part.get(Idx_min_partition).assigned_process_index = i;
                min_partition -= p[i].size;
                part.get(Idx_min_partition).remaining_space -= p[i].size;
                part.get(Idx_min_partition).fitted = p[i].size;
                part.get(Idx_min_partition).remaining_space = 0;
                if (min_partition != 0) {
                    part.add(Idx_min_partition + 1, new Partition("partition" + Integer.toString(part.size()), min_partition));
                }
            } else {
                System.out.println("****** "+p[i].name + " Can't be allocated ******");
                NotAllocated.add(i);
            }
        }
        for (int i = 0; i < part.size(); i++) {
            if (part.get(i).remaining_space == 0) {
                System.out.print(part.get(i).name);
                System.out.print(" ( " + part.get(i).fitted + " KB ) ");
                System.out.println("=> " + p[part.get(i).assigned_process_index].name);
            } else {
                System.out.println(part.get(i).name + " ( " + part.get(i).size + " KB ) => External fragment");
            }
        }
        System.out.println("do you want to compact? 1-yes,2-no :");
        Scanner scan=new Scanner(System.in);
        int choice=scan.nextInt();
        if(choice==1)
            compaction(NotAllocated,part, p,process_num);
    }
    public static void Worst_fit(ArrayList<Partition> part, Process[] p, int process_num) {
        ArrayList<Integer> Added = new ArrayList<>();
        ArrayList<Integer> NotAllocated=new ArrayList<>();
        for (int i = 0; i < process_num; i++) {
            int max_partition = Integer.MIN_VALUE;
            int Idx_max_partition = 0;
            boolean assign = false;
            for (int j = 0; j < part.size(); j++) {
                if (part.get(j).remaining_space >= p[i].size && part.get(j).remaining_space >= max_partition) {
                    max_partition = part.get(j).remaining_space;
                    Idx_max_partition = j;
                    assign = true;
                }
            }
            if (assign) {
                p[i].assigned_partition_index = Idx_max_partition;
                part.get(Idx_max_partition).assigned_process_index = i;
                max_partition -= p[i].size;
                part.get(Idx_max_partition).remaining_space -= p[i].size;
                part.get(Idx_max_partition).fitted = p[i].size;
                part.get(Idx_max_partition).remaining_space = 0;
                if (max_partition != 0) {
                    part.add(Idx_max_partition + 1, new Partition("partition" + Integer.toString(part.size()), max_partition));
                }
            } else {
                System.out.println("****** "+p[i].name + " Can't be allocated ******");
                NotAllocated.add(i);
            }
        }
        for (int i = 0; i < part.size(); i++) {
            if (part.get(i).remaining_space == 0) {
                System.out.print(part.get(i).name);
                System.out.print(" ( " + part.get(i).fitted + " KB ) ");
                System.out.println("=> " + p[part.get(i).assigned_process_index].name);
            } else {
                System.out.println(part.get(i).name + " ( " + part.get(i).size + " KB ) => External fragment");
            }
        }
        System.out.println("do you want to compact? 1-yes,2-no :");
        Scanner scan=new Scanner(System.in);
        int choice=scan.nextInt();
        if(choice==1)
            compaction(NotAllocated,part, p,process_num);
    }
}