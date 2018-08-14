package dubbo.com.orrin.businessa.config;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author migu-orrin on 2018/8/13 013.
 */
public final class ExceptionUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionUtil.class);

	public static String handleException(String name, BlockException ex) {
		LOGGER.error("Oops: " + ex.getClass().getCanonicalName());
		return " 调用服务出错 " + ex.getClass() + " ***** " + ex.getMessage() ;
	}
}
