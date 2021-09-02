#!/bin/bash
bazel run --stamp \
  --define "maven_repo=https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/" \
  --define "maven_user=edlav" \
  --define "maven_password=$1" \
  --define gpg_sign=true \
  '//src:client.publish'
