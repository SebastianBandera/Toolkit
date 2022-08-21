package toolkit.core.modules;

import java.util.concurrent.ThreadFactory;

import common.string.StringUtils;

public class ModuleThreadFactory implements ThreadFactory {

	private final String id;
	private int secuence;
	
	public ModuleThreadFactory(String id) {
		this.id = id;
		this.secuence = 1;
	}
	
	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r);
		t.setName(StringUtils.concat("Module-", id, "-thread-", String.valueOf(secuence)));
		
		secuence++;
		
		return t;
	}

}
