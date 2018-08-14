package dubbo.com.orrin.businessb.service;

import com.alibaba.dubbo.config.annotation.Service;
import dubbo.com.orrin.businessa.client.api.BService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author migu-orrin on 2018/8/13 013.
 */
@Service(
		version = "1.0.0",
		application = "${dubbo.application.id}",
		protocol = "${dubbo.protocol.id}",
		registry = "${dubbo.registry.id}"

)
public class BServiceImpl implements BService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BServiceImpl.class);

	@Value("${dubbo.application.id}")
	private String applicationId;


	@Override
	public String sayHello(String name) {
		int count = 0;
		try {
			count = Integer.parseInt(name);
			Thread.sleep(count * 400);
		}catch (InterruptedException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}

		return "Hello, " + name + " (from "+ applicationId +")";
	}

}
