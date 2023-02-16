# Es + esrally

> How to boot ElasticSearch on docker with one line 
> + How to use esrally to benchmark


最开始得到 的 信息 就是 cpu 打不满，也能看到 cpu 确实不如。

后面还是 对比 docker 和 非docker 两边 jvm 参数 发现可疑的地方 （前提是有人发现网络好像有问题，可以事后了解一下怎么看某个程序的网络包、性能）

然后还是 jstack 进一步定位出来了。 

- 遇到疑难杂症，网上搜不到的，越要从细节入手，不要 “你以为jvm 几个参数会有大影响”
- 现象可能不是 meta 原因，可能是 原因导致的结果 （cpu 打不满）


## Install Manual 安装步骤 

yum remove git

rpm -ivh http://opensource.wandisco.com/centos/7/git/x86_64/wandisco-git-release-7-1.noarch.rpm

yum install git -y

yum install zlib-devel bzip2-devel openssl-devel ncurses-devel  epel-release gcc gcc-c++ xz-devel readline-devel gdbm-devel sqlite-devel tk-devel db4-devel libpcap-devel libffi-devel

yum install python3-devel

./install

tar -xvf rally-track-data-geonames.tar -C ~/

esrally configure

  tail -f .rally/logs/rally.log


开源 7 系列

curl  -XGET http://172.18.20.20:9200/_cat/nodes?v


esrally race --pipeline=benchmark-only --target-hosts=172.18.20.18:9200,172.18.20.19:9200,172.18.20.20:9200,172.18.20.29:9200,172.18.20.30:9200,172.18.20.31:9200 --offline --track=geonames  --challenge=append-no-conflicts --user-tag="tzh:docker-elasticsearch7"

```yaml
version: '3'
services:
  elasticsearch_n0:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.16.2
    container_name: elasticsearch_7.16.2
    privileged: true
    environment:
      - cluster.name=elasticsearch-cluster-tzh
      - node.name=node0
      - node.master=true
      - node.data=true
      - bootstrap.memory_lock=true
      - http.cors.enabled=true
      - http.cors.allow-origin=*
      - "ES_JAVA_OPTS=-Xms32768m -Xmx32768m -Xmn8192m"
      - "discovery.zen.ping.unicast.hosts=linux-idc-20-18,linux-idc-20-19,linux-idc-20-20,idc-linux-20-29,idc-linux-20-30,idc-linux-20-31"
      - "discovery.zen.minimum_master_nodes=2"
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - ./data/node0:/usr/share/elasticsearch/data
      - ./logs/node0:/usr/share/elasticsearch/logs
    ports:
      - 9200:9200
```

docker run -d --net host -e ES_JAVA_OPTS="-Xms32768m -Xmx32768m -Xmn8192m" \
-p 172.18.20.18:9200:9200 -p 172.18.20.18:9300:9300 \
-v /elk7/config/es.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
-v /elk7/data/:/usr/share/elasticsearch/data \
-v /elk7/logs/:/usr/share/elasticsearch/logs \
--name es01 docker.elastic.co/elasticsearch/elasticsearch:7.16.2


docker run -d --net host -e ES_JAVA_OPTS="-Xms32768m -Xmx32768m -Xmn8192m" -p 172.18.20.19:9200:9200 -p 172.18.20.19:9300:9300 -v /elk7/config/es.yml:/usr/share/elasticsearch/config/elasticsearch.yml -v /elk7/data/:/usr/share/elasticsearch/data -v /elk7/logs/:/usr/share/elasticsearch/logs --name es02 docker.elastic.co/elasticsearch/elasticsearch:7.16.2

docker run -d --net host -e ES_JAVA_OPTS="-Xms32768m -Xmx32768m -Xmn8192m" \
-p 172.18.20.20:9200:9200 -p 172.18.20.20:9300:9300 \
-v /elk7/config/es.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
-v /elk7/data/:/usr/share/elasticsearch/data \
-v /elk7/logs/:/usr/share/elasticsearch/logs \
--name es03 docker.elastic.co/elasticsearch/elasticsearch:7.16.2


docker run -d --net host -e ES_JAVA_OPTS="-Xms32768m -Xmx32768m -Xmn8192m" \
-p 172.18.20.29:9200:9200 -p 172.18.20.29:9300:9300 \
-v /elk7/config/es.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
-v /elk7/data/:/usr/share/elasticsearch/data \
-v /elk7/logs/:/usr/share/elasticsearch/logs \
--name es04 docker.elastic.co/elasticsearch/elasticsearch:7.16.2


docker run -d --net host -e ES_JAVA_OPTS="-Xms32768m -Xmx32768m -Xmn8192m" \
-p 172.18.20.30:9200:9200 -p 172.18.20.30:9300:9300 \
-v /elk7/config/es.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
-v /elk7/data/:/usr/share/elasticsearch/data \
-v /elk7/logs/:/usr/share/elasticsearch/logs \
--name es05 docker.elastic.co/elasticsearch/elasticsearch:7.16.2


docker run  -d  --net host -e ES_JAVA_OPTS="-Xms32768m -Xmx32768m -Xmn8192m" \
-p 172.18.20.31:9200:9200 -p 172.18.20.31:9300:9300 \
-v /elk7/config/es.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
-v /elk7/data/:/usr/share/elasticsearch/data \
-v /elk7/logs/:/usr/share/elasticsearch/logs \
--name es06 docker.elastic.co/elasticsearch/elasticsearch:7.16.2


7.x 性能

18 节点

/root/.rally/benchmarks/races/e2da7604-9897-47e2-b994-987c5a5afb4a

------------------------------------------------------
    _______             __   _____
   / ____(_)___  ____ _/ /  / ___/_________  ________
  / /_  / / __ \/ __ `/ /   \__ \/ ___/ __ \/ ___/ _ \
 / __/ / / / / / /_/ / /   ___/ / /__/ /_/ / /  /  __/
/_/   /_/_/ /_/\__,_/_/   /____/\___/\____/_/   \___/
------------------------------------------------------
            
|                                                         Metric |                   Task |       Value |    Unit |
|---------------------------------------------------------------:|-----------------------:|------------:|--------:|
|                     Cumulative indexing time of primary shards |                        |     33.2171 |     min |
|             Min cumulative indexing time across primary shards |                        |           0 |     min |
|          Median cumulative indexing time across primary shards |                        |      3.2946 |     min |
|             Max cumulative indexing time across primary shards |                        |     16.8212 |     min |
|            Cumulative indexing throttle time of primary shards |                        |           0 |     min |
|    Min cumulative indexing throttle time across primary shards |                        |           0 |     min |
| Median cumulative indexing throttle time across primary shards |                        |           0 |     min |
|    Max cumulative indexing throttle time across primary shards |                        |           0 |     min |
|                        Cumulative merge time of primary shards |                        |     6.15537 |     min |
|                       Cumulative merge count of primary shards |                        |          82 |         |
|                Min cumulative merge time across primary shards |                        |           0 |     min |
|             Median cumulative merge time across primary shards |                        |     1.00938 |     min |
|                Max cumulative merge time across primary shards |                        |     1.37433 |     min |
|               Cumulative merge throttle time of primary shards |                        |      0.7806 |     min |
|       Min cumulative merge throttle time across primary shards |                        |           0 |     min |
|    Median cumulative merge throttle time across primary shards |                        |    0.136433 |     min |
|       Max cumulative merge throttle time across primary shards |                        |    0.165233 |     min |
|                      Cumulative refresh time of primary shards |                        |     1.08193 |     min |
|                     Cumulative refresh count of primary shards |                        |         135 |         |
|              Min cumulative refresh time across primary shards |                        |           0 |     min |
|           Median cumulative refresh time across primary shards |                        |    0.168983 |     min |
|              Max cumulative refresh time across primary shards |                        |    0.200817 |     min |
|                        Cumulative flush time of primary shards |                        |    0.839583 |     min |
|                       Cumulative flush count of primary shards |                        |          13 |         |
|                Min cumulative flush time across primary shards |                        |           0 |     min |
|             Median cumulative flush time across primary shards |                        |    0.132933 |     min |
|                Max cumulative flush time across primary shards |                        |    0.161117 |     min |
|                                             Total Young Gen GC |                        |      47.165 |       s |
|                                               Total Old Gen GC |                        |           0 |       s |
|                                                     Store size |                        |     3.25466 |      GB |
|                                                  Translog size |                        | 4.09782e-07 |      GB |
|                                         Heap used for segments |                        |     1.17541 |      MB |
|                                       Heap used for doc values |                        |   0.0344734 |      MB |
|                                            Heap used for terms |                        |    0.968025 |      MB |
|                                            Heap used for norms |                        |    0.092041 |      MB |
|                                           Heap used for points |                        |           0 |      MB |
|                                    Heap used for stored fields |                        |   0.0808716 |      MB |
|                                                  Segment count |                        |         117 |         |
|                                                 Min Throughput |           index-append |     38545.7 |  docs/s |
|                                              Median Throughput |           index-append |      156426 |  docs/s |
|                                                 Max Throughput |           index-append |      203274 |  docs/s |
|                                        50th percentile latency |           index-append |     1258.49 |      ms |
|                                        90th percentile latency |           index-append |     2113.98 |      ms |
|                                        99th percentile latency |           index-append |      4052.2 |      ms |
|                                      99.9th percentile latency |           index-append |     5795.84 |      ms |
|                                       100th percentile latency |           index-append |     6708.62 |      ms |
|                                   50th percentile service time |           index-append |     1258.49 |      ms |
|                                   90th percentile service time |           index-append |     2113.98 |      ms |
|                                   99th percentile service time |           index-append |      4052.2 |      ms |
|                                 99.9th percentile service time |           index-append |     5795.84 |      ms |
|                                  100th percentile service time |           index-append |     6708.62 |      ms |
|                                                     error rate |           index-append |           0 |       % |
|                                                 Min Throughput |            index-stats |       90.05 |   ops/s |
|                                              Median Throughput |            index-stats |       90.06 |   ops/s |
|                                                 Max Throughput |            index-stats |       90.11 |   ops/s |
|                                        50th percentile latency |            index-stats |     3.32643 |      ms |
|                                        90th percentile latency |            index-stats |      3.9387 |      ms |
|                                        99th percentile latency |            index-stats |     8.18071 |      ms |
|                                      99.9th percentile latency |            index-stats |     25.2783 |      ms |
|                                       100th percentile latency |            index-stats |      32.987 |      ms |
|                                   50th percentile service time |            index-stats |     3.25251 |      ms |
|                                   90th percentile service time |            index-stats |     3.85062 |      ms |
|                                   99th percentile service time |            index-stats |     4.97091 |      ms |
|                                 99.9th percentile service time |            index-stats |     19.0432 |      ms |
|                                  100th percentile service time |            index-stats |     32.9194 |      ms |
|                                                     error rate |            index-stats |           0 |       % |
|                                                 Min Throughput |             node-stats |       60.66 |   ops/s |
|                                              Median Throughput |             node-stats |        67.1 |   ops/s |
|                                                 Max Throughput |             node-stats |       68.93 |   ops/s |
|                                        50th percentile latency |             node-stats |     2268.57 |      ms |
|                                        90th percentile latency |             node-stats |      3411.1 |      ms |
|                                        99th percentile latency |             node-stats |     3734.96 |      ms |
|                                      99.9th percentile latency |             node-stats |      3766.3 |      ms |
|                                       100th percentile latency |             node-stats |      3769.6 |      ms |
|                                   50th percentile service time |             node-stats |      13.666 |      ms |
|                                   90th percentile service time |             node-stats |     16.9881 |      ms |
|                                   99th percentile service time |             node-stats |     28.4708 |      ms |
|                                 99.9th percentile service time |             node-stats |     34.5167 |      ms |
|                                  100th percentile service time |             node-stats |     54.7704 |      ms |
|                                                     error rate |             node-stats |           0 |       % |
|                                                 Min Throughput |                   term |     1987.72 |   ops/s |
|                                              Median Throughput |                   term |     2000.08 |   ops/s |
|                                                 Max Throughput |                   term |     2000.21 |   ops/s |
|                                        50th percentile latency |                   term |     4.06258 |      ms |
|                                        90th percentile latency |                   term |     128.452 |      ms |
|                                        99th percentile latency |                   term |     351.945 |      ms |
|                                      99.9th percentile latency |                   term |     484.634 |      ms |
|                                     99.99th percentile latency |                   term |      548.45 |      ms |
|                                       100th percentile latency |                   term |     571.793 |      ms |
|                                   50th percentile service time |                   term |      3.9648 |      ms |
|                                   90th percentile service time |                   term |     32.0861 |      ms |
|                                   99th percentile service time |                   term |     148.759 |      ms |
|                                 99.9th percentile service time |                   term |     258.928 |      ms |
|                                99.99th percentile service time |                   term |     356.893 |      ms |
|                                  100th percentile service time |                   term |     388.257 |      ms |
|                                                     error rate |                   term |           0 |       % |
|                                                 Min Throughput |                 phrase |     4377.67 |   ops/s |
|                                              Median Throughput |                 phrase |     4628.19 |   ops/s |
|                                                 Max Throughput |                 phrase |     4689.99 |   ops/s |
|                                        50th percentile latency |                 phrase |     11534.2 |      ms |
|                                        90th percentile latency |                 phrase |     15908.5 |      ms |
|                                        99th percentile latency |                 phrase |     17127.2 |      ms |
|                                      99.9th percentile latency |                 phrase |     17413.1 |      ms |
|                                     99.99th percentile latency |                 phrase |     17507.5 |      ms |
|                                       100th percentile latency |                 phrase |     17528.2 |      ms |
|                                   50th percentile service time |                 phrase |     10.8275 |      ms |
|                                   90th percentile service time |                 phrase |      40.509 |      ms |
|                                   99th percentile service time |                 phrase |     169.053 |      ms |
|                                 99.9th percentile service time |                 phrase |     236.104 |      ms |
|                                99.99th percentile service time |                 phrase |     265.423 |      ms |
|                                  100th percentile service time |                 phrase |     341.184 |      ms |
|                                                     error rate |                 phrase |           0 |       % |
|                                                 Min Throughput |   country_agg_uncached |      137.55 |   ops/s |
|                                              Median Throughput |   country_agg_uncached |      138.63 |   ops/s |
|                                                 Max Throughput |   country_agg_uncached |      139.38 |   ops/s |
|                                        50th percentile latency |   country_agg_uncached |       55752 |      ms |
|                                        90th percentile latency |   country_agg_uncached |     65880.6 |      ms |
|                                        99th percentile latency |   country_agg_uncached |     69539.1 |      ms |
|                                      99.9th percentile latency |   country_agg_uncached |     70923.8 |      ms |
|                                     99.99th percentile latency |   country_agg_uncached |     71648.2 |      ms |
|                                       100th percentile latency |   country_agg_uncached |     71792.9 |      ms |
|                                   50th percentile service time |   country_agg_uncached |     707.094 |      ms |
|                                   90th percentile service time |   country_agg_uncached |      1008.1 |      ms |
|                                   99th percentile service time |   country_agg_uncached |     1302.35 |      ms |
|                                 99.9th percentile service time |   country_agg_uncached |     1539.79 |      ms |
|                                99.99th percentile service time |   country_agg_uncached |     1871.89 |      ms |
|                                  100th percentile service time |   country_agg_uncached |     1923.09 |      ms |
|                                                     error rate |   country_agg_uncached |           0 |       % |
|                                                 Min Throughput |     country_agg_cached |     5590.02 |   ops/s |
|                                              Median Throughput |     country_agg_cached |     5926.37 |   ops/s |
|                                                 Max Throughput |     country_agg_cached |     6129.79 |   ops/s |
|                                        50th percentile latency |     country_agg_cached |     23242.6 |      ms |
|                                        90th percentile latency |     country_agg_cached |     28215.6 |      ms |
|                                        99th percentile latency |     country_agg_cached |     29041.1 |      ms |
|                                      99.9th percentile latency |     country_agg_cached |     29167.8 |      ms |
|                                     99.99th percentile latency |     country_agg_cached |       29199 |      ms |
|                                       100th percentile latency |     country_agg_cached |     29204.9 |      ms |
|                                   50th percentile service time |     country_agg_cached |     6.80253 |      ms |
|                                   90th percentile service time |     country_agg_cached |     26.6673 |      ms |
|                                   99th percentile service time |     country_agg_cached |     141.934 |      ms |
|                                 99.9th percentile service time |     country_agg_cached |     213.048 |      ms |
|                                99.99th percentile service time |     country_agg_cached |     298.343 |      ms |
|                                  100th percentile service time |     country_agg_cached |     303.551 |      ms |
|                                                     error rate |     country_agg_cached |           0 |       % |
|                                                 Min Throughput |                 scroll |      284.99 | pages/s |
|                                              Median Throughput |                 scroll |      285.27 | pages/s |
|                                                 Max Throughput |                 scroll |      285.75 | pages/s |
|                                        50th percentile latency |                 scroll | 2.16587e+06 |      ms |
|                                        90th percentile latency |                 scroll | 2.51201e+06 |      ms |
|                                        99th percentile latency |                 scroll | 2.58732e+06 |      ms |
|                                      99.9th percentile latency |                 scroll | 2.59404e+06 |      ms |
|                                     99.99th percentile latency |                 scroll | 2.59529e+06 |      ms |
|                                       100th percentile latency |                 scroll | 2.59566e+06 |      ms |
|                                   50th percentile service time |                 scroll |     8479.13 |      ms |
|                                   90th percentile service time |                 scroll |     11110.5 |      ms |
|                                   99th percentile service time |                 scroll |     13666.8 |      ms |
|                                 99.9th percentile service time |                 scroll |     15174.8 |      ms |
|                                99.99th percentile service time |                 scroll |     19014.9 |      ms |
|                                  100th percentile service time |                 scroll |     22761.7 |      ms |
|                                                     error rate |                 scroll |           0 |       % |
|                                                 Min Throughput |             expression |        57.3 |   ops/s |
|                                              Median Throughput |             expression |       57.81 |   ops/s |
|                                                 Max Throughput |             expression |       57.88 |   ops/s |
|                                        50th percentile latency |             expression |      109514 |      ms |
|                                        90th percentile latency |             expression |      127406 |      ms |
|                                        99th percentile latency |             expression |      132800 |      ms |
|                                      99.9th percentile latency |             expression |      137417 |      ms |
|                                       100th percentile latency |             expression |      137906 |      ms |
|                                   50th percentile service time |             expression |     971.044 |      ms |
|                                   90th percentile service time |             expression |     1380.36 |      ms |
|                                   99th percentile service time |             expression |     1813.71 |      ms |
|                                 99.9th percentile service time |             expression |     2218.29 |      ms |
|                                  100th percentile service time |             expression |     2429.13 |      ms |
|                                                     error rate |             expression |           0 |       % |
|                                                 Min Throughput |        painless_static |       40.65 |   ops/s |
|                                              Median Throughput |        painless_static |       40.98 |   ops/s |
|                                                 Max Throughput |        painless_static |       41.04 |   ops/s |
|                                        50th percentile latency |        painless_static |      230587 |      ms |
|                                        90th percentile latency |        painless_static |      267927 |      ms |
|                                        99th percentile latency |        painless_static |      278697 |      ms |
|                                      99.9th percentile latency |        painless_static |      282959 |      ms |
|                                       100th percentile latency |        painless_static |      283254 |      ms |
|                                   50th percentile service time |        painless_static |     1421.81 |      ms |
|                                   90th percentile service time |        painless_static |     2167.62 |      ms |
|                                   99th percentile service time |        painless_static |     2861.98 |      ms |
|                                 99.9th percentile service time |        painless_static |     3295.66 |      ms |
|                                  100th percentile service time |        painless_static |     3591.89 |      ms |
|                                                     error rate |        painless_static |           0 |       % |
|                                                 Min Throughput |       painless_dynamic |       44.85 |   ops/s |
|                                              Median Throughput |       painless_dynamic |       45.09 |   ops/s |
|                                                 Max Throughput |       painless_dynamic |       45.28 |   ops/s |
|                                        50th percentile latency |       painless_dynamic |      195541 |      ms |
|                                        90th percentile latency |       painless_dynamic |      226605 |      ms |
|                                        99th percentile latency |       painless_dynamic |      234965 |      ms |
|                                      99.9th percentile latency |       painless_dynamic |      239055 |      ms |
|                                       100th percentile latency |       painless_dynamic |      239294 |      ms |
|                                   50th percentile service time |       painless_dynamic |     1315.39 |      ms |
|                                   90th percentile service time |       painless_dynamic |     1962.67 |      ms |
|                                   99th percentile service time |       painless_dynamic |      2599.4 |      ms |
|                                 99.9th percentile service time |       painless_dynamic |     3739.72 |      ms |
|                                  100th percentile service time |       painless_dynamic |     4106.83 |      ms |
|                                                     error rate |       painless_dynamic |           0 |       % |
|                                                 Min Throughput | large_prohibited_terms |       23.64 |   ops/s |
|                                              Median Throughput | large_prohibited_terms |        23.7 |   ops/s |
|                                                 Max Throughput | large_prohibited_terms |       23.79 |   ops/s |
|                                        50th percentile latency | large_prohibited_terms |      772950 |      ms |
|                                        90th percentile latency | large_prohibited_terms |      893343 |      ms |
|                                        99th percentile latency | large_prohibited_terms |      922067 |      ms |
|                                      99.9th percentile latency | large_prohibited_terms |      926300 |      ms |
|                                       100th percentile latency | large_prohibited_terms |      928063 |      ms |
|                                   50th percentile service time | large_prohibited_terms |     3842.03 |      ms |
|                                   90th percentile service time | large_prohibited_terms |     5104.27 |      ms |
|                                   99th percentile service time | large_prohibited_terms |     6346.36 |      ms |
|                                 99.9th percentile service time | large_prohibited_terms |     7389.74 |      ms |
|                                  100th percentile service time | large_prohibited_terms |     7885.32 |      ms |
|                                                     error rate | large_prohibited_terms |           0 |       % |


----------------------------------
[INFO] SUCCESS (took 5685 seconds)
----------------------------------




