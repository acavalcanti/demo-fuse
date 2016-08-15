package org.jboss.fuse.route;

import org.apache.camel.Exchange;
import org.apache.camel.component.elasticsearch.ElasticsearchConfiguration;
import org.jboss.fuse.service.ElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchArticleToElasticRoute extends OnExceptionElasticSearch {

    final static Logger LOG = LoggerFactory.getLogger(ElasticSearchService.class);

    @Override
    public void configure() throws Exception {
    	
        from("direct:searchById").id("searchbyid-direct-route")
                .log("Search article by ID Service called !")
                .setHeader(ElasticsearchConfiguration.PARAM_INDEX_NAME).simple("{{indexname}}")
                .setHeader(ElasticsearchConfiguration.PARAM_INDEX_TYPE).simple("{{indextype}}")
                .setHeader(ElasticsearchConfiguration.PARAM_OPERATION).constant(ElasticsearchConfiguration.OPERATION_GET_BY_ID)
                .setBody().simple("${header.id}")
                .to("elasticsearch://{{clustername}}?ip={{address}}")
                .beanRef("elasticSearchService", "getBlog")
                .choice()
                    .when().simple("${body} == null")
                        .setBody().simple("No article has been retrieved from the ES DB for this id ${header.id}.")
                        .setHeader(Exchange.CONTENT_TYPE).constant("text/plain")
                .endChoice();                


        from("direct:searchByUser").id("searchbyuser-direct-route")
                .log("Search articles by user Service called !")
                .setHeader(ElasticsearchConfiguration.PARAM_INDEX_NAME).simple("{{indexname}}")
                .setHeader(ElasticsearchConfiguration.PARAM_INDEX_TYPE).simple("{{indextype}}")
                .beanRef("elasticSearchService", "getBlogs")
                .choice()
                    .when().simple("${body.isEmpty} == 'true'")
                        .setBody().simple("No articles have been retrieved from the ES DB for this user ${header.user}.")
                        .setHeader(Exchange.CONTENT_TYPE).constant("text/plain")
                .endChoice();

        
        super.configure();
    }
}
