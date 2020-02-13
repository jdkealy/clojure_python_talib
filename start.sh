xhost +
docker run --rm -it \
       -p 3004:3004 \
       -p 3005:3005 \
       --env="DISPLAY=192.168.0.4:0" \
       --volume="$PWD":/app \
       clj
