(ns day4.b
  (:require [clojure.java.io :as io])
  (:require [clojure.set :as set])
  (:require [clojure.string :as str]))

(defn- input [] (slurp (io/resource (str/replace *ns* #"\..$" "/input.txt"))))

(defn- section-pairs [line]
  (let [[_ a1 a2 b1 b2] (re-matches #"(\d+)-(\d+),(\d+)-(\d+)" line)]
    [(set (range (Integer/parseInt a1) (inc (Integer/parseInt a2))))
     (set (range (Integer/parseInt b1) (inc (Integer/parseInt b2))))]))

(defn- overlap? [[set1 set2]] ((complement empty?) (set/intersection set1 set2)))

(defn -main []
  (->> (str/split-lines (input))
    (map section-pairs)
    (map overlap?)
    (filter true?)
    count
    prn))

(-main)