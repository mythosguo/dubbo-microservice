package dubbo.com.orrin.businessa.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.dubbo.config.annotation.Reference;
import dubbo.com.orrin.businessa.BusinessAApplication;
import dubbo.com.orrin.businessa.client.api.AService;
import dubbo.com.orrin.businessa.client.api.BService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author migu-orrin on 2018/8/13 013.
 */
@RestController
public class AController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AController.class);

	@Autowired
	private AService aservice;

	@Reference(version = "1.0.0",
			application = "business-b")
	private BService bservice;

	@GetMapping("/hello")
	public Mono<String> hello(String word) {
		return Mono.just(aservice.sayHello(word));
	}

	@GetMapping("/hello/b/{count}")
	public Mono<String> helloB(@PathVariable("count") int count, String word) {
		Map<Integer, String> resultMap = new HashMap<>();


		for (int i = 0; i < count; i++) {
			String result = "";
			Entry entry = null;
			try {
				entry = SphU.entry(BusinessAApplication.RES_KEY_SAYHELLO, EntryType.IN);

				// Write your biz code here.
				// <<BIZ CODE>>
				result = bservice.sayHello(word);

			} catch (Throwable t) {
				LOGGER.error("error");
				LOGGER.error(t.getMessage());

				if (!BlockException.isBlockException(t)) {
					Tracer.trace(t);
				}
				result = "bservice is error";
			} finally {
				if (entry != null) {
					entry.exit();
				}
			}
			resultMap.put(i, result);
		}


		return Mono.just(resultMap.toString());
	}


	@GetMapping("/bye/b/{count}")
	public Mono<String> bye(@PathVariable("count") int count, String word) {
		Map<Integer, String> resultMap = new HashMap<>();
		for (int i = 0; i < count; i++) {
			resultMap.put(i, aservice.sayBye(word));
		}


		return Mono.just(resultMap.toString());
	}

	@GetMapping("/something/b/{count}")
	public Mono<String> something(@PathVariable("count") int count, String word) {
		Map<Integer, String> resultMap = new HashMap<>();
		for (int i = 0; i < count; i++) {
			resultMap.put(i, aservice.saySomething(word));
		}

		return Mono.just(resultMap.toString());
	}
}
