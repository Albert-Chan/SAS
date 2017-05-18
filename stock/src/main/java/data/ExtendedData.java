package data;

import java.util.Date;

public class ExtendedData implements IExtendedData{
	public long tick;
	public long averageVolume;
	public Date date;
	
	public static final ExtendedData EMPTY_DATA = new ExtendedData();
}
