(ns day2.b
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str]))

(defn- input [] (slurp (io/resource (str/replace *ns* #"\..$" "/input.txt"))))

(def play-notation {"A" :rock "B" :paper "C" :scissors})

(def result-notation {"X" :lose "Y" :draw "Z" :win})

(def scores {:rock 1 :paper 2 :scissors 3})

(def beats {:rock :scissors :paper :rock :scissors :paper})

(defn- round-score [opponent result]
  (case result
    :draw (+ 3 (scores opponent))
    :lose (scores (beats opponent))
    :win (+ 6 (scores (beats (beats opponent))))))

(defn- get-score [round]
  (let [opponent (play-notation (nth round 0)) result (result-notation (nth round 1))]
    (round-score opponent result)))

(defn -main []
  (->> (str/split-lines (input))
    (map #(str/split % #" "))
    (map get-score)
    (apply +)
    prn))

(-main)