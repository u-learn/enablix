package com.enablix.services.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.commons.xsdtopojo.ParamSetType;
import com.enablix.core.commons.xsdtopojo.ParameterType;
import com.enablix.core.commons.xsdtopojo.QualityConfigType;

public class ParamSetUtil {

	public static String getStringValue(ParamSetType paramSet, String paramName) {
		ParameterType parameter = getParameter(paramSet, paramName);
		return parameter == null ? null : parameter.getValue();
	}
	
	public static Boolean getBooleanValue(ParamSetType paramSet, String paramName) {
		return getBooleanValue(paramSet, paramName, null);
	}
	
	public static Boolean getBooleanValue(ParamSetType paramSet, String paramName, Boolean defaultValue) {
		ParameterType parameter = getParameter(paramSet, paramName);
		return parameter == null ? defaultValue : Boolean.parseBoolean(parameter.getValue());
	}
	
	private static ParameterType getParameter(ParamSetType paramSet, String paramName) {
		
		for (ParameterType param : paramSet.getParam()) {
			if (paramName.equals(param.getName())) {
				return param;
			}
		}
		
		return null;
	}
	
	public static boolean isBooleanAndTrue(ParamSetType paramSet, String paramName) {
		return paramSet != null && getBooleanValue(paramSet, paramName, Boolean.FALSE);
	}

	public static boolean isParamPresent(ParamSetType paramSet, String paramName) {
		return paramSet != null && getParameter(paramSet, paramName) != null;
	}
	
	public static boolean hasParamSet(QualityConfigType qualityConfig, String paramSetName) {
		
		if (qualityConfig != null) {
			
			List<ParamSetType> paramSets = qualityConfig.getParamSet();
			
			if (CollectionUtil.isNotEmpty(paramSets)) {
			
				for (ParamSetType paramSet : paramSets) {
					if (paramSetName.equals(paramSet.getName())) {
						return true;
					}
				}
			}
		}
		
		return false;
	}

	public static ParamSetType getParamSet(QualityConfigType qualityConfig, String paramSetName) {
		
		if (qualityConfig != null) {
			
			List<ParamSetType> paramSets = qualityConfig.getParamSet();
			
			if (CollectionUtil.isNotEmpty(paramSets)) {
			
				for (ParamSetType paramSet : paramSets) {
					if (paramSetName.equals(paramSet.getName())) {
						return paramSet;
					}
				}
			}
		}
		
		return null;
		
	}

	public static Collection<String> getStringValues(ParamSetType paramSet, String paramName) {

		Collection<String> values = new HashSet<>();
		
		for (ParameterType param : paramSet.getParam()) {
			if (paramName.equals(param.getName())) {
				values.add(param.getValue());
			}
		}
		
		return values;
	}
	
}
