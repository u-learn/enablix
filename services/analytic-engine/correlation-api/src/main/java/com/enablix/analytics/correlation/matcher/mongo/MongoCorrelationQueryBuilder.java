package com.enablix.analytics.correlation.matcher.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.matcher.MatchInputRecord;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.FilterCriteriaType;
import com.enablix.core.commons.xsdtopojo.FilterType;
import com.enablix.core.commons.xsdtopojo.MatchCriteriaType;
import com.enablix.core.commons.xsdtopojo.MatchType;
import com.enablix.core.mongo.search.SearchCriteria;
import com.enablix.core.mongo.search.SearchFilter;

@Component
public class MongoCorrelationQueryBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(MongoCorrelationQueryBuilder.class);
	
	@Autowired
	private FilterToMongoFilter filterMongoTx;
	
	public Criteria build(TemplateFacade template, String targetItemQId, FilterAttributIdResolver attrIdResolver,
			FilterCriteriaType filterCriteria, MatchCriteriaType matchCriteria, MatchInputRecord matchInput) {
		
		SearchCriteria sc = null;
		
		// Add filter criteria
		if (filterCriteria != null && CollectionUtil.isNotEmpty(filterCriteria.getFilter())) {
			for (FilterType filter : filterCriteria.getFilter()) {
				sc = addFilterToCriteria(template, targetItemQId, matchInput, sc, filter, attrIdResolver);
			}
			
		}

		// Add match criteria
		if (matchCriteria != null && CollectionUtil.isNotEmpty(matchCriteria.getMatch())) {
			// TODO: do we need match criteria or the filter criteria is sufficient
			for (MatchType match : matchCriteria.getMatch()) {
				sc = addFilterToCriteria(template, targetItemQId, matchInput, sc, match, attrIdResolver);
			}
		}

		Criteria queryCriteria = sc.toPredicate(new Criteria());
		
		LOGGER.debug("Correlation mongo query: {}", queryCriteria);
		
		return queryCriteria;
	}

	private SearchCriteria addFilterToCriteria(TemplateFacade template, String targetItemQId, 
			MatchInputRecord matchInput, SearchCriteria criteria, FilterType filter, FilterAttributIdResolver attrIdResolver) {
		
		SearchFilter searchFilter = filterMongoTx.createMongoFilter(
							filter, targetItemQId, matchInput, template, attrIdResolver);
		
		if (criteria == null) {
			criteria = new SearchCriteria(searchFilter);
		} else {
			criteria.and(searchFilter);
		}
		
		return criteria;
	}
	
}
