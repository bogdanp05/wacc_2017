package connectors

import com.datastax.driver.core.policies.{ConstantReconnectionPolicy, DefaultRetryPolicy, RoundRobinPolicy}
import com.outworkers.phantom.connectors.{CassandraConnection, ContactPoints}
import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters._


object Connector {
  private val config = ConfigFactory.load()

  private val hosts = config.getStringList("cassandra.host").asScala
  private val keyspace = config.getString("cassandra.keyspace")

  lazy val connector: CassandraConnection = ContactPoints(hosts, 9042).withClusterBuilder(
     _.withLoadBalancingPolicy(new RoundRobinPolicy())
      .withRetryPolicy(DefaultRetryPolicy.INSTANCE)
      .withReconnectionPolicy(new ConstantReconnectionPolicy(1000))).keySpace(keyspace)
}