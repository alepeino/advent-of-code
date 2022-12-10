(ns day8.a
  (:require [clojure.java.io :as io])
  (:require [clojure.set :as set])
  (:require [clojure.string :as str]))

(defn- input [] (slurp (io/resource (str/replace *ns* #"\..$" "/input.txt"))))

(defn- to-grid [s]
  (->> (str/split-lines s)
    (mapv (partial mapv #(Character/digit % 10)))))

(defn- visible-in-row [row reversed?]
  (let [indexes (if reversed? (map dec (range (count row) 0 -1)) (range 0 (count row)))]
    (loop [visible '()
           max     -1
           [x & more] indexes]
      (if (nil? x)
        visible
        (let [height (nth row x)]
          (if (> height max)
            (recur (cons x visible) height more)
            (recur visible max more)))))))

(defn- visible-trees [grid]
  (let [row-indexes (range (count grid))
        col-indexes (range (count (first grid)))
        get-row     grid
        get-col     (fn [col] (map (fn [row] (get-in grid [row col])) row-indexes))]
    (apply set/union
      (map set
        (list
          (for [row row-indexes, col (visible-in-row (get-row row) false)] [row col])
          (for [row row-indexes, col (visible-in-row (get-row row) true)] [row col])
          (for [col col-indexes, row (visible-in-row (get-col col) false)] [row col])
          (for [col col-indexes, row (visible-in-row (get-col col) true)] [row col]))))))

(defn -main []
  (->> (input)
    to-grid
    visible-trees
    count
    prn))

(-main)