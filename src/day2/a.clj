(ns day2.a
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str]))

(defn- input [] (slurp (io/resource (str/replace *ns* #"\..$" "/input.txt"))))

(def notation {"A" :rock
               "X" :rock
               "B" :paper
               "Y" :paper
               "C" :scissors
               "Z" :scissors})

(def scores {:rock 1 :paper 2 :scissors 3})

(def beats {:rock :scissors :paper :rock :scissors :paper})

(defn- round-score [opponent player]
  (cond (= opponent player) 3
        (= opponent (beats player)) 6
        :else 0))

(defn- get-score [round]
  (let [opponent (notation (nth round 0)) player (notation (nth round 1))]
    (+ (round-score opponent player) (scores player))))

(defn -main []
  (->> (str/split-lines (input))
    (map #(str/split % #" "))
    (map get-score)
    (apply +)
    prn))

(-main)