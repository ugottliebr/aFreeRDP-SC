#!/bin/bash

# Script to build aFreeRDP with librtpcsc
# Expects keystore, keystore password, key alias, key password as arguments in that order
# Expects that JAVA_HOME, ANDROID_HOME and ANDROID_NDK variables are set

# Parse arguments
if [ $# -ne 5 ]; then
	echo "Invalid arguments $@, expected debug/release, keystore, keystore password, key alias, key password"
	exit 1
fi

SCRIPTS_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
BASE_DIR="$(cd ${SCRIPTS_DIR}/../ && pwd)"

source ${SCRIPTS_DIR}/android-build-common.sh

FLAVOR=""
if [ "$1" == "debug" ]; then
	FLAVOR="assembleDebug"
else
	FLAVOR="assembleRelease"
fi
KEYSTORE=$2
KEYSTORE_PASSWORD=$3
KEY_ALIAS=$4
KEY_PASSWORD=$5

# Define needed variables
ANDROID_STUDIO_PROJECT_DIR="${BASE_DIR}/client/Android/Studio"
JNI_LIBS_DIR="${ANDROID_STUDIO_PROJECT_DIR}/freeRDPCore/src/main/jniLibs"
PATH_TO_RTPCSC="${BASE_DIR}/external/pcsc-android"
RELATIVE_PATH_IN_RTPCSC="lib/dynamic/librtpcsc.so"

# Build freerdp native libs (also compose openssl-freerdp.so from binary dep)
common_run ${SCRIPTS_DIR}/android-build-freerdp.sh --ndk $ANDROID_NDK --sdk $ANDROID_HOME

# Put binary deps to jni lib dir
common_run cp ${PATH_TO_RTPCSC}/android-armv7a-gcc/${RELATIVE_PATH_IN_RTPCSC} ${JNI_LIBS_DIR}/armeabi-v7a/libpcsclite.so
common_run cp ${PATH_TO_RTPCSC}/android-arm64-gcc/${RELATIVE_PATH_IN_RTPCSC} ${JNI_LIBS_DIR}/arm64-v8a/libpcsclite.so

# Build aFreeRDP apk
common_run cd $ANDROID_STUDIO_PROJECT_DIR
./gradlew ${FLAVOR} \
	-PkeystorePath="${KEYSTORE}" \
	-PkeystorePass="${KEYSTORE_PASSWORD}" \
	-PkeyAlias="${KEY_ALIAS}" \
	-PkeyPass="${KEY_PASSWORD}"
