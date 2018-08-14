package dubbo.com.orrin.businessb.controller;

import dubbo.com.orrin.businessb.service.BServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author migu-orrin on 2018/8/13 013.
 */
@RestController
public class BController {

	@Autowired
	private BServiceImpl bservice;

	@GetMapping("/hello")
	public Mono<String> hello(String word) {
		return Mono.just(bservice.sayHello(word));
	}
}
