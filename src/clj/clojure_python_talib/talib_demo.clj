(ns clojure-python-talib.talib-demo
  (:use [clojure.core.matrix])
  (:require [libpython-clj.require :refer [require-python]]
            [clojure.data.csv :as csv]
            [clojure-python-talib.talib-funcs :as talib]
            [clojure-python-talib.filter-funcs :as ffuncs]
            [clojure.core.matrix :as m]
            [clojure.java.io :as io]
            [clojure-python-talib.plot :as plot]
            [tech.v2.datatype :as dtype]
            [clojure.java.shell :as sh]
            [clj-time.format :as f]
            [libpython-clj.python :refer [py. py.. py.-] :as py]
            [tech.v2.datatype :as dtype]))

(require-python '[numpy :as np])

(def custom-formatter (f/formatter "yyyy-MM-dd"))

(def test-ary (np/array [[1 2][3 4]]))

(defn csv-as-map [path ]
  (let [csv (->>  (with-open [reader (io/reader path)]
                    (doall
                     (csv/read-csv reader))))
        headers (->> csv
                     first
                     (map keyword))]
    (->> csv
         (drop 1 )
         (map #(zipmap headers %))
         (map (fn [e]
                (assoc e
                       :open (read-string (:open e))
                       :high (read-string (:high e))
                       :low (read-string (:low e))
                       :volume (read-string (:volume e))
                       :close (read-string (:close e))
                       :date (f/parse custom-formatter (:date e))))))))


(defn m1 []
  (let [series  (let [series (csv-as-map "MU_daily.csv")]
                  (->> series
                       (drop 6000)
                       (talib/get-smas [20 50] :open)
                       (filter :sma_20)
                       (ffuncs/map-prev-close)
                       (ffuncs/map-next-close 3)
                       (filter identity)
                       (map ffuncs/prev-to-next-close-pct)
                       (ffuncs/filter-dir < :sma_20 :open)
                       (ffuncs/filter-gaps-up)
                       (ffuncs/filter-day-of-week :tues)
                       (ffuncs/filter-close-up-gap)))
        x (->> series
               (map :volume)
               vec )
        y (->> series
               (map :pct)
               vec )]
    (plot/scatter (np/array x) (np/array y))))


(defn m2 []
  (let [series  (let [series (csv-as-map "MU_daily.csv")]
                  (->> series
                       (drop 6000)
                       (talib/get-smas [20] :open)
                       (talib/map-macd :open)
                       (filter :sma_20)
                       (filter (fn [e]
                                 (not (Double/isNaN (:macd_hist e)))))
                       (ffuncs/map-next-close 5)
                       (filter identity)))
        x (->> series
               (map :macd_hist)
               vec )
        y (->> series
               (map :pct)
               vec )]
    (plot/scatter (np/array x) (np/array y))))


(comment

  (let [series (csv-as-map "MU_daily.csv")]
    (->> series
         (drop 6000)
         (talib/get-smas [20] :open)
         (talib/map-macd :open)
         (filter :sma_20)
         (filter (fn [e]
                   (not (Double/isNaN (:macd_hist e)))))
         (ffuncs/map-next-close 5)
         (filter identity)))



  (+ 1 1)
  (let [series (csv-as-map "MU_daily.csv")]
    (->> series
         (drop 6000)
         (talib/get-smas [20] :open)
         (talib/map-macd :open)))

  )
