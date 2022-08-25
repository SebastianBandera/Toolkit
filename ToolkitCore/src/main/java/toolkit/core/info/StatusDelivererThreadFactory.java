package toolkit.core.info;

import java.util.concurrent.ThreadFactory;

import common.string.StringUtils;

public class StatusDelivererThreadFactory implements ThreadFactory {

	private int secuence;
	
	public StatusDelivererThreadFactory() {
		this.secuence = 1;
	}
	
	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r);
		t.setName(StringUtils.concat("StatusDeliverer-thread-", String.valueOf(secuence)));
		
		secuence++;
		
		return t;
	}

}
