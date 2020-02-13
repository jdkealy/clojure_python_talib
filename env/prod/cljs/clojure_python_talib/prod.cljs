(ns clojure-python-talib.prod
  (:require [clojure-python-talib.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
