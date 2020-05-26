#!/usr/bin/env bash

ARTIFACT=lxc-bu
MAINCLASS=de.my5t3ry.LcxBu
VERSION=0.0.1.BUILD-SNAPSHOT

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

rm -rf target
mkdir -p target/native-image

echo "Packaging $ARTIFACT with Maven"
mvn -ntp package -T 8 >target/native-image/output.txt

JAR="$ARTIFACT-$VERSION-exec.jar"
rm -f $ARTIFACT
echo "Unpacking $JAR"
cp target/classes/resources.conf target/native-image
cd target/native-image
jar -xvf ../$JAR
cp -R META-INF BOOT-INF/classes

LIBPATH=$(find BOOT-INF/lib | tr '\n' ':')
CP=BOOT-INF/classes:$LIBPATH

GRAALVM_VERSION=$(native-image --version)
echo "Compiling $ARTIFACT with $GRAALVM_VERSION"
{
  time native-image \
    --verbose \
    -Dspring.native.verbose=true \
    --no-server \
    --no-fallback \
    -H:Name=$ARTIFACT \
    --initialize-at-build-time=java.sql.DriverManager,org.hibernate.internal.util.ReflectHelper \
    -H:+ReportExceptionStackTraces \
    -H:ResourceConfigurationFiles=resources.conf \
    -Dspring.native.remove-unused-autoconfig=true \
    -cp $CP $MAINCLASS
}

if [[ -f $ARTIFACT ]]; then
  printf "${GREEN}SUCCESS${NC}\n"
  mv ./$ARTIFACT ..
  exit 0
else
  cat output.txt
  printf "${RED}FAILURE${NC}: an error occurred when compiling the native-image.\n"
  exit 1
fi
