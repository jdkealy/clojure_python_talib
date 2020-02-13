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
RUN apt-get install -y python3-pip
RUN pip3 install numpy
RUN pip3 install TA-Lib
WORKDIR /app
COPY project.clj /app/project.clj
RUN cd /app
RUN lein deps
RUN apt-get -y install libxrender1 libxtst6 libxi6
CMD /bin/bash
