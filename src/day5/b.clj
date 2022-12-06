(ns day5.b
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

(defn- apply-move [state [amount from to]]
  (let [stack (state from)
        index (- (count stack) amount)]
    (-> state
          (update from #(subvec % 0 index))
          (update to #(apply conj % (subvec stack index))))))

(defn -main []
  (let [raw           (str/split (input) #"\n\n")
        initial-state (get-initial-state (nth raw 0))
        moves         (parse-moves (nth raw 1))]
    (->> moves
      (reduce apply-move initial-state)
      (map (comp last second))
      str/join
      prn)))

(-main)