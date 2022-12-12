(ns day10.a
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str]))

(defn- input []
  (slurp (io/resource (str/replace *ns* #"\..$" "/input.txt"))))

(defn- parse-line [line]
  (let [[cmd args] (str/split line #" ")]
    (if (some? args) [cmd (Integer/parseInt args)] [cmd])))

(defn- apply-instructions [instructions]
  (loop [state        {:cycle 1 :x 1 :stack []}
         instructions instructions
         history      []]
    (let [next-state   (update state :cycle inc)
          next-history (conj history state)]
      (if (empty? (state :stack))
        (if (empty? instructions)
          history
          (let [instruction (first instructions)
                next-state  (case (instruction 0)
                              "noop" next-state
                              "addx" (update next-state :stack conj instruction))]
            (recur next-state (rest instructions) next-history)))
        (let [[_ args] (-> state :stack first)]
          (recur (-> next-state (update :stack pop) (update :x + args)) instructions next-history))))))

(defn -main []
  (->> (input)
    str/split-lines
    (map parse-line)
    apply-instructions
    (filter #(-> % :cycle #{20 60 100 140 180 220}))
    (map (juxt :cycle :x))
    (map (partial apply *))
    (apply +)
    prn))

(-main)