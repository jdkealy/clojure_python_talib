xhost +
IP=$(ifconfig en0 | grep inet | awk '$1=="inet" {print $2}')
docker run --rm -it \
       -p 3004:3004 \
       -p 3005:3005 \
       --env="DISPLAY="$IP:0 \
       --volume="$PWD":/app \
       clj
