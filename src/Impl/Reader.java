package Impl;

/**
 * 
 * @author weihaoqu
 * @date Feb 4 2017
 */

public class Reader {
		public String getName() {
		return Name;
	}



	public void setName(String name) {
		Name = name;
	}



	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}

		private String Name;
		private Integer id;
		private String email;		
	
		public Reader(String name, Integer id, String email) {
			super();
			Name = name;
			this.id = id;
			this.email = email;
		}



		public void signIn(){
			CacheManagerImpl.getCacheManagerImplInstance().getReadInfo().put(id, this);
		}
		
		public Object read( Integer index) throws InterruptedException{
			
			return CacheManagerImpl.getCacheManagerImplInstance().read(index, id);
			
		}
}
