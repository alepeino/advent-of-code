(ns day5.a
  (:require [clojure.string :as str]))

(defn transpose [m]
  (apply mapv vector m))

(defn- input []
  (slurp "src/day5/res/input.txt"))

(defn- to-stack-tuple [[stack-id & items]]
  [stack-id (filterv (partial not= \space) items)])

(defn- get-initial-state [s]
  (->> s
    str/split-lines
    reverse
    transpose
    (filter (comp #(Character/isDigit %) first))
    (map to-stack-tuple)
    (into (sorted-map))))

(defn- parse-moves [lines]
  (->> lines
    str/split-lines
    (map #(re-matches #"move (\d+) from (\d+) to (\d+)" %))
    (map (fn [[_ amount from to]]
           [(Integer/parseInt amount) (first from) (first to)]))))

(defn- expand-moves [moves]
  (apply concat (map (fn [[amount from to]] (repeat amount [from to])) moves)))

(defn- apply-move [state [from to]]
  (-> state
    (update from #(subvec % 0 (dec (count %))))
    (update to #(conj % (last (state from))))))

(defn -main []
  (let [raw           (str/split (input) #"\n\n")
        initial-state (get-initial-state (nth raw 0))
        moves         (expand-moves (parse-moves (nth raw 1)))]
    (->> moves
      (reduce apply-move initial-state)
      (map (comp last second))
      str/join
      prn)))

(-main)