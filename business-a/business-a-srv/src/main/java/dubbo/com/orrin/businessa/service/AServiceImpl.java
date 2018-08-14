package dubbo.com.orrin.businessa.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import dubbo.com.orrin.businessa.BusinessAApplication;
import dubbo.com.orrin.businessa.client.api.AService;
import dubbo.com.orrin.businessa.client.api.BService;
import dubbo.com.orrin.businessa.config.ExceptionUtil;
import org.springframework.beans.factory.annotation.Value;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author migu-orrin on 2018/8/13 013.
 */
@Service(
		version = "1.0.0",
		application = "${dubbo.application.id}",
		protocol = "${dubbo.protocol.id}",
		registry = "${dubbo.registry.id}"

)
public class AServiceImpl implements AService {

	private AtomicInteger count = new AtomicInteger(0);
	private AtomicInteger nothingCount = new AtomicInteger(0);

	@Value("${dubbo.application.id}")
	private String applicationId;

	@Reference(version = "1.0.0",
			application = "business-b")
	private BService bservice;

	@Override
	public String sayHello(String name) {
		return "Hello, " + name + " (from "+ applicationId +")";
	}

	@Override
	@SentinelResource(value = BusinessAApplication.RES_KEY_SAYSOMETHING, blockHandler = "exceptionHandler", fallback = "saySomethingFallback")
	public String saySomething(String name) {
		String res = bservice.sayHello(String.valueOf(count.getAndIncrement()));
		return "saySomething , " + res;
	}

	/**
	 * Fallback 函数，函数签名与原函数一致.
	 */
	public String saySomethingFallback(String name) {
		return "saySomethingFallback " + name;
	}

	@Override
	@SentinelResource(value = BusinessAApplication.RES_KEY_SAYBYE, blockHandler = "handleException", blockHandlerClass = {ExceptionUtil.class})
	public String sayBye(String name) {
		return "sayBye , " + bservice.sayHello(String.valueOf(count.getAndIncrement()));
	}

	@Override
	@SentinelResource(value = BusinessAApplication.RES_KEY_SAYNOTHING)
	public String sayNothing() {
		nothingCount.getAndIncrement();
		/**
		 * 此方法RT = 1000 ~ 1100 ms
		 */
		Random rand =new Random(25);
		int random = rand.nextInt(100) + 1000;
		try {
			Thread.sleep(random);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return "say nothing " + random + "ms -->" + nothingCount;
	}

	public String exceptionHandler(String name, BlockException ex) {
		// Do some log here.
		ex.printStackTrace();
		return "Oops, error occurred at " + name;
	}

}
