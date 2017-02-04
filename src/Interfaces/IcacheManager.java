package Interfaces;
/**
 * 
 * @author weihaoqu
 *
 */

public interface IcacheManager {
		
	Object read(Integer index, Integer readerId) throws InterruptedException;
	
	void write(Integer index, Object data, Integer writerId) throws InterruptedException;
	
	
}
