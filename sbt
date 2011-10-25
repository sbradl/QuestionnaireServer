java -Xmx1024M -XX:PermSize=256M -XX:MaxPermSize=1024M -XX:+CMSClassUnloadingEnabled -jar `dirname $0`/sbt-launch.jar "$@"
