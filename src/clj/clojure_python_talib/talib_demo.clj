(ns clojure-python-talib.talib-demo
  (:require [libpython-clj.require :refer [require-python]]
            [libpython-clj.python :refer [py. py.. py.-] :as py]
            [tech.v2.datatype :as dtype]))

(require-python '[numpy :as np])
(require-python '[talib :as ta])
