(ns clojure-python-talib.talib-funcs
  (:require [libpython-clj.require :refer [require-python]]
            [libpython-clj.python :refer [py. py.. py.-] :as py]
            [tech.v2.datatype :as dtype]))

(require-python '[talib :as ta])
(require-python '[numpy :as np])

(defn get-sma [period key series]
  (let [sma  (vec  (ta/SMA  (->>
                             series
                             (map #(get % key))
                             vec
                             (np/array))
                            period))]
    (->> series
         (map-indexed (fn [idx e]
                (assoc e (keyword
                          (str "sma_" period))
                       (nth sma  idx)))))))

(defn get-bbands [key series]
  (let [s (->> series
               (map key)
               vec
               (np/array))]
    (ta/BBANDS s)))

(defn get-macd [key series]
  (let [s (->> series
               (map key)
               vec
               (np/array))]
    (ta/MACD s)))

(defn map-macd [key series]
  (let [[macd macdsignal macdhist] (get-macd key series)]
    (->> series
         (map-indexed (fn  [idx s]
                        (assoc s :macd_hist (nth  (vec  macdhist) idx)))))))

(defn get-smas [periods key series]
  (let [smas  (->> periods
                   (map (fn [period]
                          (vec  (ta/SMA  (->>
                                          series
                                          (map #(get % key))
                                          vec
                                          (np/array))
                                         period)))))]
    (->> series
         (map-indexed (fn [idx e]
                        (let [idxx (atom -1)]
                          (->> periods
                               (reduce (fn [e period]
                                         (swap! idxx inc)
                                         (assoc e (keyword
                                                   (str "sma_" period))
                                                (nth (nth smas @idxx)  idx))
                                         ) e))))))))


(comment

  (get-smas [10, 20] :open (map (fn [e]
                                  {:open e}
                                  ) (vec  (map double  (range 1000)))))
  )
