package com.enablix.core.condition;

import java.util.List;

import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.commons.xsdtopojo.BasicConditionType;
import com.enablix.core.commons.xsdtopojo.GroupConditionType;

public interface ConditionEvaluator {

	boolean evaluate(ContentDataRecord record, GroupConditionType condition);
	
	boolean evaluate(ContentDataRecord record, BasicConditionType condition);
	
	List<ContentDataRecord> findMatchingRecords(ConditionEvalContext context, GroupConditionType condition);
	
	List<ContentDataRecord> findMatchingRecords(ConditionEvalContext context, BasicConditionType condition);
	
}
