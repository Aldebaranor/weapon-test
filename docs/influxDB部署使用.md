# influxdb 部署
```yml
version: "3.4"

# https://docs.influxdata.com/influxdb/v1.7/administration/config
services:
  influxdb:
    image: influxdb:1.7-alpine
    environment:
      - INFLUXDB_ADMIN_ENABLED=true 
      - INFLUXDB_ADMIN_USER=${INFLUXDB_ADMIN_USER:-root}
      - INFLUXDB_ADMIN_PASSWORD=${INFLUXDB_ADMIN_PASSWORD:-root}
      - INFLUXDB_DB=test
      - INFLUXDB_HTTP_LOG_ENABLED=false
      - INFLUXDB_REPORTING_DISABLED=true
      - INFLUXDB_USER=${INFLUXDB_USER:-test}
      - INFLUXDB_USER_PASSWORD=${INFLUXDB_USER_PASSWORD:-test}
    ports:
      - "8083:8083"
      - "8086:8086"
    deploy:
      mode: replicated
      replicas: 1
      resources:
        limits:
          memory: 2048M
        reservations:
          memory: 1024M
    volumes:
      - ./local_bind_volume_dir:/var/lib/influxdb
networks:
  ingress: ## 网络组名称
    external:
      name: juntai-network
```

# influxdb 使用见代码 
- [InfluxdbTemplate.java](./modules/weapon-common-base/src/main/java/com/soul/weapon/influxdb/InfluxdbTemplate.java)
