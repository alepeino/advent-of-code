(ns day5.b
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str]))

(defn- input [] (slurp (io/resource (str/replace *ns* #"\..$" "/input.txt"))))

(defn transpose [m]
  (apply mapv vector m))

(def to-stack-tuple
  (juxt last (comp (partial drop-while #{\space}) butlast)))

(defn- get-initial-state [s]
  (->> s
    str/split-lines
    transpose
    (filter (comp #(Character/isDigit %) last))
    (map to-stack-tuple)
    (into (sorted-map))))

(defn- parse-moves [lines]
  (->> lines
    str/split-lines
    (map #(re-matches #"move (\d+) from (\d+) to (\d+)" %))
    (map (fn [[_ amount from to]]
           [(Integer/parseInt amount) (first from) (first to)]))))

(defn- apply-move [state [amount from to]]
  (-> state
    (update from (partial drop amount))
    (update to (partial concat (take amount (state from))))))

(defn -main []
  (let [raw           (str/split (input) #"\n\n")
        initial-state (get-initial-state (nth raw 0))
        moves         (parse-moves (nth raw 1))]
    (->> moves
      (reduce apply-move initial-state)
      (map (comp first second))
      str/join
      prn)))

(-main)