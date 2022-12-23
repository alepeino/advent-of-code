(ns day13.b
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str]))

(defn- input []
  (slurp (io/resource (str/replace *ns* #"\..$" "/input.txt"))))

(defn- parse-input [s]
  (->> (str/split-lines s)
    (remove empty?)
    (map read-string)))

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
  (let [dividers #{[[2]] [[6]]}]
    (->> (input)
      parse-input
      (concat dividers)
      (sort before?)
      (keep-indexed (fn [index packet] (and (dividers packet) (inc index))))
      (apply *)
      prn)))

(-main)
