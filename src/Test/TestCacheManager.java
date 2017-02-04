package Test;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import Impl.CacheManagerImpl;
import Impl.Reader;
import Impl.Writer;

/**
 * 
 * @author weihaoqu
 *
 */
public class TestCacheManager {
	private static ExecutorService executorService;
	private static int defaultThreads=10; // it depends
	
	@BeforeClass
	public static void initial(){
	Map<Integer, Object> cache=new HashMap<Integer, Object>();
	cache.put(1,"number One");
	cache.put(2,"number Two");
	cache.put(3,"number Three");
	CacheManagerImpl.loadCache(cache);
	executorService = Executors.newFixedThreadPool(defaultThreads);
}
	@Test
   public void simulate() throws InterruptedException {
		List<Callable<Object>> tasks=new ArrayList<Callable<Object>>();
		
		tasks.add(read(1,"tom","tom@**.com",2));
		tasks.add(read(2,"carl","carl@**.com",2));
		tasks.add(read(3,"jace","jack@**.com",2));
		
		executorService.invokeAll(tasks);
		List<Callable<Object>> tasks2=new ArrayList<Callable<Object>>();
		tasks2.add(write(4,2, "ChangeNumberTwo"));
		executorService.invokeAll(tasks2);
		
   }
	
	@AfterClass
	public static void finishedTest(){
	if(!executorService.isShutdown())
		executorService.shutdown();
}
	
	private Callable<Object> read(Integer id, String Name, String email, Integer index){
		Callable<Object> c=new Callable<Object>(){

			@Override
			public Object call() throws Exception {
				// TODO Auto-generated method stub
				Reader reader=new Reader(Name, id, email);
				 return reader.read(index);
				
			}
			
		};
		return c;
	}
	
	private Callable<Object> write(Integer id, Integer index, Object data){
		Callable<Object> c=new Callable<Object>(){

			@Override
			public Object call() throws Exception {
				// TODO Auto-generated method stub
				Writer writer=new Writer( id);
				  writer.writer(index, data);
				  return new Object();
				
			}
			
		};
		return c;
	}
}
