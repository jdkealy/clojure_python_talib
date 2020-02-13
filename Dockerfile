FROM clojure
RUN apt-get update
RUN apt-get install -y  \
        gcc \
        g++ \
        make \
        curl \
    && cd /tmp \
    && curl -L -O http://prdownloads.sourceforge.net/ta-lib/ta-lib-0.4.0-src.tar.gz \
    && tar -zxf ta-lib-0.4.0-src.tar.gz \
    && cd ta-lib/ \
    && ./configure --prefix=/usr \
    && make \
    && make install
RUN apt-get install -y python-pip
RUN pip install numpy
RUN pip install TA-Lib
RUN cd /app
RUN lein deps
CMD lein repl
