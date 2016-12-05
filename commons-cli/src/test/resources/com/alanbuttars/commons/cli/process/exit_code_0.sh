#!/bin/sh

echo "info 1"
>&2 echo "error 1"
echo "info 2"
>&2 echo "error 2"

exit 0