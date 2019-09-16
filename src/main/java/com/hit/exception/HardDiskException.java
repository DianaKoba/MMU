package com.hit.exception;
import java.io.IOException;
import java.io.Serializable;

public class HardDiskException extends IOException implements Serializable 
{
	private static final long serialVersionUID = 1L;
	private ActionError status;

	public static enum ActionError 
	{
		PAGE_FAULT,
		PAGE_REPLACEMENT 
	}

	public HardDiskException() 
	{
		super();
	}
	
	public HardDiskException(String msg)  
	{
		super(msg);
	}	
	
	public HardDiskException(String msg, HardDiskException.ActionError status) 
	{
		super(msg + " " + status);
		this.status = status;
	}
	
	public static HardDiskException.ActionError[] values()
	{
		ActionError[] value = new ActionError[] { ActionError.PAGE_FAULT, ActionError.PAGE_REPLACEMENT };
		
		return value;
	
	}
	
	public static HardDiskException.ActionError valueOf(String name)
	{
		if(name.equals("PAGE_FAULT"))
		{
			return ActionError.PAGE_FAULT;
		}
		else if(name.equals("PAGE_REPLACEMENT"))
		{
			return ActionError.PAGE_REPLACEMENT;
		}
		else if(name.length() == 0)
		{
			throw new NullPointerException();
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}
}

