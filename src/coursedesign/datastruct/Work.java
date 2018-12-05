package coursedesign.datastruct;

import java.util.*;

public class Work
{
	public Work(){}
	public Work(Work work){
		early_start=work.early_start;
		duration=work.duration;
		early_end=work.early_end;
		late_start=work.late_start;
		free=work.free;
		late_end=work.late_end;
		name=work.name;
	}
	public String name="";
	public int early_start,duration,early_end,late_start,free,late_end;
}
