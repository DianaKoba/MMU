package com.hit.memoryunits;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.hit.algorithm.LRUAlgoCacheImpl;

public class MemoryManagementUnitTest {
	MemoryManagementUnit mmu;
	
	@Test
	public void testMemoryManagementUnit() {
		MemoryManagementUnit mmu = new MemoryManagementUnit(5,new LRUAlgoCacheImpl<>(5));
		assertTrue (mmu.getAlgo()!=null);
	}
	
	@Test
	public void testGetPages() throws IOException
	{
		MemoryManagementUnit mmu = new MemoryManagementUnit(4,new LRUAlgoCacheImpl<>(4));
		
		for (int i = 0 ; i < 5 ; i++){
			byte [] arr = new byte [3];
			for (int j = 0 ; j<arr.length;j++){
				arr[j]=(byte) j;
				
			}
			HardDisk.getInstance().hardDiskMap.put(new Long(i),new Page<byte[]>(new Long(i),arr));
		}
		Long[] arr1 = new Long[5];
		Page<byte[]>[] pArr;
		
		for (long i =0; i<5;i++){
			arr1[(int) i]=i;
		}
		pArr=mmu.getPages(arr1);	
		for(int i=0;i<pArr.length;i++){
			assertTrue(pArr[i]!=null); 
		}	
	}
}