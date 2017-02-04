package Impl;

public class Writer {
	private Integer Id;
	
	
	
	public Writer(Integer id) {
		super();
		Id = id;
	}



	public void writer(int index, Object data){
		
		try {
			CacheManagerImpl.getCacheManagerImplInstance().write(index, data, Id);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
