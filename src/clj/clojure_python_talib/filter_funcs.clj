(ns clojure-python-talib.filter-funcs
  (:require
   [clj-time.predicates :as pr]))

(defn filter-high-gt-prev-close [series]
  (->> series
       (filter (fn [{:keys [prev-close open high ]}]
                 (> high prev-close)))))

(defn filter-close-down-gap [series]
  (->> series
       (filter (fn [{:keys [prev-close open high ]}]
                 (> high prev-close)))))

(defn filter-gaps-down [series]
  (->> series
       (filter (fn [{:keys [prev-close open]}]
                 (< open prev-close)))))

(defn filter-close-up-gap [series]
  (->> series
       (filter (fn [{:keys [prev-close open low ]}]
                 (< low prev-close)))))



(defn filter-gaps-up [series]
  (->> series
       (filter (fn [{:keys [prev-close open]}]
                 (> open prev-close)))))

(defn filter-day-of-week [day series]
  (let [predictate (case day
                     :mon pr/monday?
                     :tues pr/tuesday?
                     :wed pr/wednesday?
                     :thurs pr/thursday?
                     :fri pr/friday?)]

    (->> series
         (filter (fn [{:keys [date]}]
                   (predictate date))))))

(defn prev-to-next-close-pct [{:keys [prev-close next-close] :as s}]
  (let [pct (/ (- next-close prev-close) prev-close)]
    (assoc s :pct (* 100  pct))))

(defn filter-dir [ dir k1 k2 series]
  (->> series
       (filter (fn [e]
                 (dir (get e k1) (get e k2))))))

(defn map-prev-close [series]
  (->> series
       (map-indexed (fn [idx {:keys [close open] :as i }]
                      (let [prev-close (:close  (nth series idx))
                            prev-pct (* 100  (/ (- open  prev-close) prev-close))]
                        (assoc i
                               :prev-pct prev-pct
                               :prev-close prev-close))))))

(defn map-prev-low [periods series]
  (->> series
       (map-indexed (fn [idx {:keys [low] :as e}]
                      e))))

(defn map-next-close [ndays series]
  (->>
   series
   (map-indexed (fn [idx i]
                  (let [next (try
                               (nth series (+ ndays  idx))
                               (catch Exception e nil ))]
                    (if next
                      (assoc i :next-close (:close next))
                      nil))))))

(defn gap-down-get-high [series]
  (->> series
       (drop 1)
       (map-prev-close)
       (map-next-close)

       (filter identity)
       filter-gaps-down
       filter-high-gt-prev-close
       (map prev-to-next-close-pct)))
