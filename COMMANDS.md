## How to launch Cassandra

Start single node cluster

```shell
docker network create cassandra-net
```

```shell
docker run --name cassandradb \
  --network cassandra-net \
  -p 9042:9042 \
  -d cassandra:latest
```

Need to wait before cluster is fully up and running and then launch CQL(Cassandra Query Language) shell
```shell
docker run -it --network cassandra-net --rm cassandra cqlsh cassandradb
```

Create a keyspace which is a grouping of tables
```cassandraql
create keyspace tacocloud
  with replication={'class':'SimpleStrategy', 'replication_factor':1}
  and durable_writes=true;
```
