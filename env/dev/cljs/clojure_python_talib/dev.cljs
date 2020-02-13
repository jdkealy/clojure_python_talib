(ns ^:figwheel-no-load clojure-python-talib.dev
  (:require
    [clojure-python-talib.core :as core]
    [devtools.core :as devtools]))

(extend-protocol IPrintWithWriter
  js/Symbol
  (-pr-writer [sym writer _]
    (-write writer (str "\"" (.toString sym) "\""))))

(devtools/install!)

(enable-console-print!)

(core/init!)
