package com.poc.jbpm;

import java.util.HashMap;
import java.util.Map;

import org.drools.core.process.instance.WorkItemHandler;
import org.jbpm.process.workitem.rest.RESTWorkItemHandler;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

public class RestHandler implements WorkItemHandler {

	private KieSession kieSession;

	public RestHandler(KieSession kieSession) {
		this.kieSession = kieSession;
	}

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		try {
			RESTWorkItemHandler handler = new RESTWorkItemHandler();
			handler.executeWorkItem(workItem, manager);
			Map<String, Object> results = new HashMap<>();
			results = workItem.getResults();
			int statusCode = (Integer) results.get("Status");
			
			if (statusCode == 200) {
				String result = (String) results.get("Result");
				results.put("taskResult", result);
				manager.completeWorkItem(workItem.getId(), results);
			} else {
				manager.abortWorkItem(workItem.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		manager.abortWorkItem(workItem.getId());
	}

}
