#!/bin/bash

FUSE_BIN=/opt/binaries/fuse/jboss-fuse-full-6.2.1.redhat-084.zip
FUSE_DIR=jboss-fuse-6.2.1.redhat-084
SYM_LINK=jboss-fuse

# remove old installation
rm -rf $FUSE_DIR
rm $SYM_LINK

# install a fresh fuse
echo "unzipping jboss fuse"
echo "please wait...."

unzip -q $FUSE_BIN -d .


ln -s $FUSE_DIR $SYM_LINK
rm jboss-fuse/bin/*.bat

# comment out user admin
sed -i '' '/admin/s/^#//g' $SYM_LINK/etc/users.properties

echo "fabric:create --clean -m 127.0.0.1 -r manualip --wait-for-provisioning" | pbcopy

sh $SYM_LINK/bin/fuse
