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
    java -Xms256m -Xmx512m -server -Xloggc:../logs/gc.log -verbose:gc -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=../logs  -jar ../lib/MeetingRoomAdmin.jar
fi
