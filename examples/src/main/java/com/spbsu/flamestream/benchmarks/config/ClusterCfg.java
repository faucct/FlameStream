package com.spbsu.flamestream.benchmarks.config;

/**
 * User: Artem
 * Date: 18.08.2017
 */
public interface ClusterCfg {
  boolean isLocal();

  LocalClusterCfg localClusterCfg();

  RealClusterCfg realClusterCfg();
}