(ns clojure-python-talib.plot
  (:require [libpython-clj.require :refer [require-python]]
            [clojure.data.csv :as csv]
            [clojure-python-talib.talib-funcs :as talib]
            [clojure.java.io :as io]
            [tech.v2.datatype :as dtype]
            [clojure.java.shell :as sh]
            [clj-time.format :as f]
            [libpython-clj.python :refer [py. py.. py.-] :as py]
            [tech.v2.datatype :as dtype]))

(def mplt (py/import-module "matplotlib"))
(py. mplt "use" "Agg")
(require-python '[matplotlib.pyplot :as pyplot])
(require-python 'matplotlib.backends.backend_agg)
(require-python 'numpy)

(defmacro with-show
  "Takes forms with mathplotlib.pyplot to then show locally"
  [& body]
  `(let [_# (matplotlib.pyplot/clf)
         fig# (matplotlib.pyplot/figure)
         agg-canvas# (matplotlib.backends.backend_agg/FigureCanvasAgg fig#)]
     ~(cons 'do body)
     (py. agg-canvas# "draw")
     (matplotlib.pyplot/savefig "temp.png")
     (sh/sh "feh" "temp.png")))

(defn show-chart [x y]
  (with-show
    (matplotlib.pyplot/plot x y)))

(defn scatter [x y]
  (with-show
    (matplotlib.pyplot/scatter x y)))
