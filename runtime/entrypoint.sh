#!/usr/bin/env bash

usage() {
  echo "entrypoint.sh [start|stop|restart]"
}

start() {
  local config=$1
  echo "Starting flamestream worker, config path: $config"
  local java_ops="-Daeron.term.buffer.length=4194304 -Daeron.mtu.length=16384 -Xms500m -Xmx500m -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+HeapDumpOnOutOfMemoryError"
  echo "java_ops=$java_ops"
  local main="com.spbsu.flamestream.runtime.WorkerApplication"
  local command="while :; do java $java_ops -cp lib/*:flamestream-runtime-1.0-SNAPSHOT.jar $main $config > worker.log 2>&1; done"
  nohup bash -c "${command}" &
  local pid=$!
  echo "Pid=$pid"
  echo $pid > flamestream.pid
}

stop() {
  echo "Stopping flamestream worker"
  if [[ ! -f flamestream.pid ]]; then
    echo "No flamestream worker was found"
    return 1
  fi

  # Kill the whole process group
  local pid=$(cat flamestream.pid)
  local pgid=$(ps -o pgid= $pid | grep -o [0-9]*)
  echo "Pid=${pid}, pgid=${pgid}"
  kill -- "-$pgid"
}

main() {
  local target=$1
  case "$target" in
    start) start "$2";;
    stop) stop;;
    restart) stop ||:; start "$2";;
    *) usage;;
  esac
}

main "$@"