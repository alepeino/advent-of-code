(ns day14.b
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str])
  (:require [infix.macros :refer [$=]]))

(defn- input []
  (slurp (io/resource (str/replace *ns* #"\..$" "/input.txt"))))

(defn- parse-int [s] (Integer/parseInt s))

(defn- parse-input [s]
  (->> (str/split-lines s)
    (map (fn [line] (->> (str/split line #" -> ")
                      (map #(->> (str/split % #",") (mapv parse-int))))))))

(defn- draw-line [state [[x1 y1] [x2 y2 :as b]]]
  (let [n      (int ($= √ ((x2 - x1) ** 2 + (y2 - y1) ** 2)))
        points (cons b (for [i (range n)]
                         [(int ($= x1 + (x2 - x1) * i / n))
                          (int ($= y1 + (y2 - y1) * i / n))]))]
    (reduce (fn [state point] (assoc-in state point :#)) state points)))

(defn- draw-path [state path]
  (->> path
    (partition 2 1)
    (reduce draw-line state)))

(defn- scan [paths]
  (reduce draw-path {} paths))

(defn- calculate-next-grain [state floor-line]
  (let [source [500 0]]
    (loop [state state
           pos   source]
      (if (-> pos second inc (>= floor-line))
        [(assoc-in state pos :o) false]
        (let [[x y] pos
              down       [x (inc y)]
              down-left  [(dec x) (inc y)]
              down-right [(inc x) (inc y)]
              next-pos   (->> [down down-left down-right] (remove #(get-in state %)) first)]
          (cond
            (nil? next-pos) [(assoc-in state pos :o) (= pos source)]
            :else (recur state next-pos)))))))

(defn- solve [state]
  (let [floor-line (->> state vals (map keys) (map (partial apply max)) (apply max) (+ 2))]
    (loop [state state
           n     0]
      (let [[next-state blocked] (calculate-next-grain state floor-line)]
        (if blocked (inc n) (recur next-state (inc n)))))))

(defn -main []
  (->> (input)
    parse-input
    scan
    solve
    prn))

(-main)
