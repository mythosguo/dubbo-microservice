package dubbo.com.orrin.businessa;

import com.alibaba.csp.sentinel.init.InitExecutor;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author migu-orrin on 2018/8/13 013.
 */
@ComponentScan("dubbo.com.orrin")
@SpringBootApplication
public class BusinessAApplication {

	public static final String RES_KEY_SAYHELLO = "dubbo.com.orrin.businessa.service.AServiceImpl:sayHello(java.lang.String)";
	public static final String RES_KEY_SAYSOMETHING = "dubbo.com.orrin.businessa.service.AServiceImpl:saySomething(java.lang.String)";
	public static final String RES_KEY_SAYBYE = "dubbo.com.orrin.businessa.service.AServiceImpl:sayBye(java.lang.String)";
	public static final String INTERFACE_RES_KEY = "dubbo.com.orrin.businessa.service.AServiceImpl";

	public static final String RES_KEY_SAYNOTHING = "dubbo.com.orrin.businessa.service.AServiceImpl:sayNothing";

	public static void main(String[] args) {
		InitExecutor.doInit();
		initFlowRule();
		SpringApplication.run(BusinessAApplication.class, args);
	}

	private static void initFlowRule() {
		FlowRule flowRuleHello = new FlowRule();
		flowRuleHello.setResource(RES_KEY_SAYHELLO);
		flowRuleHello.setCount(5);
		flowRuleHello.setGrade(RuleConstant.FLOW_GRADE_QPS);
		flowRuleHello.setLimitApp("default");


		FlowRule flowRuleSomething = new FlowRule();
		flowRuleSomething.setResource(RES_KEY_SAYSOMETHING);
		flowRuleSomething.setCount(10);
		flowRuleSomething.setGrade(RuleConstant.FLOW_GRADE_QPS);
		flowRuleSomething.setLimitApp("default");


		FlowRule flowRuleBye = new FlowRule();
		flowRuleBye.setResource(RES_KEY_SAYBYE);
		flowRuleBye.setCount(15);
		flowRuleBye.setGrade(RuleConstant.FLOW_GRADE_QPS);
		flowRuleBye.setLimitApp("default");

		List<FlowRule> flowRuleList = new ArrayList<>();
		flowRuleList.add(flowRuleHello);
		flowRuleList.add(flowRuleSomething);
		flowRuleList.add(flowRuleBye);

		FlowRuleManager.loadRules(flowRuleList);


		DegradeRule degradeRuleSomething = new DegradeRule();
		degradeRuleSomething.setResource(RES_KEY_SAYSOMETHING);
		degradeRuleSomething.setCount(1000);
		degradeRuleSomething.setGrade(RuleConstant.DEGRADE_GRADE_RT);
		degradeRuleSomething.setLimitApp("default");
		degradeRuleSomething.setTimeWindow(5);

		DegradeRuleManager.loadRules(Collections.singletonList(degradeRuleSomething));

		SystemRule systemRule = new SystemRule();
		systemRule.setResource(RES_KEY_SAYNOTHING);
		systemRule.setAvgRt(1050);
		//CPU核数*2.5
		systemRule.setHighestSystemLoad(10);
		systemRule.setLimitApp("default");
		SystemRuleManager.loadRules(Collections.singletonList(systemRule));
	}
}
