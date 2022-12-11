(ns day8.b
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str]))

(defn- input [] (slurp (io/resource (str/replace *ns* #"\..$" "/input.txt"))))

(defn- to-grid [s]
  (->> (str/split-lines s)
    (mapv (partial mapv #(Character/digit % 10)))))

(defn- viewing-distance [row starting-height]
  (let [indexes (range 0 (count row))]
    (loop [distance 0
           [x & more] indexes]
      (if (nil? x)
        distance
        (let [height (nth row x)]
          (if (>= height starting-height)
            (inc distance)
            (recur (inc distance) more)))))))

(defn- scenic-score [grid x y]
  (let [get-row         grid
        get-col         (fn [col] (map (fn [row] (get-in grid [row col])) (range (count grid))))
        starting-height (get-in grid [x y])]
    (*
      (viewing-distance (->> (get-row x) (drop (inc y))) starting-height)
      (viewing-distance (->> (get-row x) (take y) reverse) starting-height)
      (viewing-distance (->> (get-col y) (drop (inc x))) starting-height)
      (viewing-distance (->> (get-col y) (take x) reverse) starting-height))))

(defn- max-scenic-score [grid]
  (let [last-row (dec (count grid))
        last-col (dec (count (first grid)))]
    (loop [row       0
           col       0
           max-score 0]
      (if (and (= row last-row) (= col last-col))
        max-score
        (let [score    (scenic-score grid row col)
              next-row (if (= col last-col) (inc row) row)
              next-col (if (= col last-col) 0 (inc col))
              next-max (max max-score score)]
          (recur next-row next-col next-max))))))

(defn -main []
  (->> (input)
    to-grid
    max-scenic-score
    prn))

(-main)