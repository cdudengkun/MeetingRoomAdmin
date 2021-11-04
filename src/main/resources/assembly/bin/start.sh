ps aux | grep MeetingRoomAdmin | grep -v grep
if [ $? -eq 0 ]; then
    echo "================="
    echo "service is running, so start abort..."
    echo "================="
    echo | jps
else
    echo "================="
    echo "start service..."
    echo "================="
    nohup java -Xms256m -Xmx512m -server -Xloggc:../logs/gc.log -verbose:gc -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=../logs ../lib/MeetingRoomAdmin.jar > /dev/null 2>&1 &
    echo | jps
    #sleep 5
    #echo | ./log.sh
fi
