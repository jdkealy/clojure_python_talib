docker run --rm -it \
   -p 8888:8888 \
   --volume="$PWD":/app \
   clj
