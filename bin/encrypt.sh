#!/bin/bash

set -e

UNENCRYPTED_STRING="$1"
if [ "$UNENCRYPTED_STRING" == "" ]; then
    echo ""
    echo "WARN : NO ARGUMENTS FOUND."
    echo ""
    echo "syntax:"
    echo "========================================="
    echo "$(basename $0) 'string to encrypt'"
    echo ""
    echo "examplesi :"
    echo "$(basename $0) 'hello world'"
    echo ""
    exit 0;
fi;

dir=$(dirname $0)

java -classpath "$dir/../lib/*" com.voltsolutions.security.Encryptor "$UNENCRYPTED_STRING"

