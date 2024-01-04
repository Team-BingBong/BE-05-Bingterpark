FROM docker.elastic.co/elasticsearch/elasticsearch:8.5.3
COPY plugin/hanhinsam-0.1.zip /plugin/hanhinsam.zip
RUN bin/elasticsearch-plugin install analysis-nori && elasticsearch-plugin install file:///plugin/hanhinsam.zip
