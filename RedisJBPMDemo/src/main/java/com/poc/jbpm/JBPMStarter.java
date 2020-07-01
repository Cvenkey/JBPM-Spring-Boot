package com.poc.jbpm;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.pojos.User;

@Component
public class JBPMStarter {
   
	public boolean startJbpmProcess(User user, String processId) {
		boolean navigateToNext = Boolean.FALSE;
		try {
			KieServices ks = KieServices.Factory.get();
			KieContainer kContainer = ks.getKieClasspathContainer();
			KieSession ksession = kContainer.newKieSession("ksession-process");
			Map<String, Object> params = new HashMap<>();
			ObjectMapper mapper = new ObjectMapper();
			String userDetails = mapper.writeValueAsString(user);
			params.put("userDetails", userDetails);
			ksession.getWorkItemManager().registerWorkItemHandler("Web Service", new RestHandler(ksession));
			ProcessInstance processInstance = ksession.startProcess(processId, params);
			RuleFlowProcessInstance rfpi = (RuleFlowProcessInstance) processInstance;
			user = (User) rfpi.getVariable("userObject");
			if (processInstance.getState() == ProcessInstance.STATE_COMPLETED && user.getResponse().equals("ack"))
				navigateToNext = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return navigateToNext;
	}
}
