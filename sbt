java -Xmx512M -XX:PermSize=256M -XX:MaxPermSize=256M -XX:+CMSClassUnloadingEnabled -jar `dirname $0`/sbt-launch.jar "$@"
