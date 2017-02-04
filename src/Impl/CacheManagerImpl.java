package Impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Interfaces.IcacheManager;
/**
 * 
 * @author weihaoqu
 * @date feb 4 2017
 */
	
public class CacheManagerImpl implements IcacheManager {
	
	public Map<Integer, Reader> getReadInfo() {
		return readInfo;
	}

	public void setReadInfo(Map<Integer, Reader> readInfo) {
		this.readInfo = readInfo;
	}



	// for store the reader information and used to notify readers on stale data
	private ConcurrentHashMap<Integer, Object> lockMap = new ConcurrentHashMap<Integer, Object>();
	private ConcurrentHashMap<Integer, List<Integer>> readerList = new ConcurrentHashMap<Integer, List<Integer>>();
	private Map<Integer, Reader> readInfo=new HashMap<Integer, Reader>();
	
	private static Map<Integer,Object> cache = new HashMap<Integer,Object>();	
	// used for cache write and read
	private ReadWriteLock lock=new ReadWriteLock();
	private Lock lk=new ReentrantLock(); 
	
	private static boolean loadedCache=false;
	
	private static CacheManagerImpl imsInstance=null;
	 
	 private CacheManagerImpl(){
		 
	 }
	 
	 public static CacheManagerImpl getCacheManagerImplInstance(){
		 if(!loadedCache) throw new Error(" Please load cache info into the system!");
		 if(imsInstance==null){
			 imsInstance=new CacheManagerImpl();
		 }
		 return imsInstance;
	 };
	 public static void loadCache(Map<Integer, Object> cacheData){
		 loadedCache=true;
		 cache.putAll(cacheData);
	 }
	 
	
	@Override
	public Object read(Integer index, Integer readerId) throws InterruptedException {
		// TODO Auto-generated method stub
			if(index> cache.size() || index<0 ) throw new Error("invalid index");
			
			lock.lockRead();
			
			Object retData=cache.get(index);
			synchronized(getLock(index)){
				lk.lock();
				//System.out.println(index+ "ddd"+ readerList.containsKey(index));
				if(!readerList.containsKey(index)){
					readerList.put(index, new LinkedList<Integer>());
					//System.out.println(index+ "ddd"+ readerList.containsKey(index));
				}
				readerList.get(index).add(readerId);
				//System.out.println("add "+ readerId+ readerList.get(index).size());
				lk.unlock();
			}
			
			lock.unlockRead();
		
		return retData;
	}

	@Override
	public void write(Integer index, Object data, Integer writerId) throws InterruptedException {
		// TODO Auto-generated method stub
		if(index> cache.size() || index<0 ) throw new Error("invalid index");
		
		lock.lockWrite();
		Object ret=cache.put(index, data);
		System.out.println(ret==null);
		// index is already used, original data is stale
		if(ret!=null){
			List<Integer> notificationReaders;
			synchronized(getLock(index)){
				// get the reader needs to be notified and clears the list
				 notificationReaders=new ArrayList(readerList.get(index));
				 System.out.println(notificationReaders.size()+","+index);
				 for(Integer readerId: notificationReaders){
					 System.out.println("Reader "+ readerId +"'s cache data from position "+ index+" has gone stale!" );
					
					}
				// notifyReaders(notificationReaders,index);
				 readerList.get(index).clear();
			}
			
			
		}
		
		lock.unlockWrite();
	}
	
	private void notifyReaders(List<Integer> readerLists, Integer index){
		for(Integer readerId: readerLists){
			notify(readerId, index);
		}
	}
	
	private void notify(int readerId, Integer index){
		Reader r=readInfo.get(readerId);
		System.out.println("Reader "+ readerId +"'s cache data from position "+ index+" has gone stale!"+ " Pls notify " + r.getName() +"'s email: "+ r.getEmail() );
	}
	
	
	
	 private Object getLock(Integer key) {
		  return lockMap.computeIfAbsent(key, k -> new Object());
		}

}

 