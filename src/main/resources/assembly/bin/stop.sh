ps aux | grep MeetingRoomAdmin | grep -v grep
if [ $? -eq 0 ]; then
    echo "================="
    echo "stop service..."
    echo "================="
    ps -ef | grep MeetingRoomAdmin | grep -v grep | cut -c 9-15 | xargs kill -9
    sleep 5
    echo "stop" | jps
else
    echo "service is not running"
fi
