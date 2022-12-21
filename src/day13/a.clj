(ns day13.a
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str]))

(defn- input []
  (slurp (io/resource (str/replace *ns* #"\..$" "/input.txt"))))

(defn- parse-input [s]
  (->> (str/split s #"\n\n")
    (map (comp (partial map read-string) str/split-lines))))

(defn before? [left right]
  (if (and (coll? left) (coll? right))
    (cond
      (and (empty? left) (empty? right)) nil
      (empty? left) true
      (empty? right) false
      :else (let [decided (before? (first left) (first right))]
              (if (boolean? decided)
                decided
                (before? (subvec left 1) (subvec right 1)))))
    (cond
      (coll? left) (before? left [right])
      (coll? right) (before? [left] right)
      (= left right) nil
      :else (< left right))))

(defn -main []
  (->> (input)
    parse-input
    (map (partial apply before?))
    (map-indexed (fn [index result] (and result (inc index))))
    (filter boolean)
    (apply +)
    prn))

(-main)
