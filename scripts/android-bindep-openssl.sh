#!/bin/bash

# Script to compose binary dep openssl into one lib as freerdp originally does
# Should be placed into freerdp_repo_root/scripts

BASE_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PATH_TO_OPENSSL="${BASE_DIR}/../external/openssl-static-1.1"

source $(dirname "${BASH_SOURCE[0]}")/android-build-common.sh

function compose {
	local toolchain=$1
	local toolchain_prefix=$2
	local sysroot_arch=$3
	local dir_in_openssl=$4
	local target_sdk_version=$5
	local dst_prefix=$6

	local toolchain_dir=${BASE_DIR}/${toolchain}
	local toolchain_bin=${toolchain_dir}/bin/${toolchain_prefix}-gcc
	local dst_dir=$BUILD_DST/$dst_prefix
	local soname="libfreerdp-openssl.so"
	
	# Copy includes to build
	common_run mkdir -p $dst_dir/include
	common_run cp -L -R ${PATH_TO_OPENSSL}/${dir_in_openssl}/include/openssl $dst_dir/include/

	# Create custom toolchain to compose libopenssl
	common_run ${ANDROID_NDK}/build/tools/make-standalone-toolchain.sh --platform=android-${target_sdk_version} \
		--toolchain=${toolchain} --install-dir=${toolchain_dir}

	# Compose libopenssl.so from libcrypto.a and libssl.a
	common_run ${toolchain_bin} --sysroot=${ANDROID_NDK}/platforms/android-${NDK_TARGET}/${sysroot_arch} -shared \
		-fPIC -Wl,-soname,${soname} -o ${dst_dir}/${soname} -Wl,-whole-archive \
		${PATH_TO_OPENSSL}/${dir_in_openssl}/lib/libcrypto.a ${PATH_TO_OPENSSL}/${dir_in_openssl}/lib/libssl.a \
		-Wl,-no-whole-archive

    # Remove custom toolchain
    common_run rm -rf $toolchain_dir
}

common_parse_arguments $@
common_clean $BUILD_DST

for ARCH in $BUILD_ARCH
do

	case $ARCH in
	 "armeabi-v7a")
		 compose "arm-linux-androideabi-4.9" "arm-linux-androideabi" \
			"arch-arm" "android-armv7a-gcc" "9" $ARCH
		 ;;
	 "arm64-v8a")
		 compose "aarch64-linux-android-4.9" "aarch64-linux-android" \
		 	"arch-arm64" "android-arm64-gcc" "21" $ARCH
		 ;;
	*)
		echo "[WARNING] Skipping unsupported architecture $ARCH"
		continue
		;;
	esac
done
