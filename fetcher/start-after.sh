#!/bin/sh

echo "start-after: $@"
waiting_on=1
while [[ $waiting_on -ne 0 ]]; do
   waiting_on=0
   for hosts in "$@"; do
      if nc -z ${hosts//:/ }; then 
         #echo "${hosts//:/ } is up"
         waiting_on=$((waiting_on+0))
      else
         #echo "${hosts//:/ } is down"
         waiting_on=$((waiting_on+1))
      fi
   done
   sleep 5
   #echo "waiting on: $waiting_on"
done

/usr/local/bin/node index.js