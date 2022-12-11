(ns day9.b
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str]))

(defn- input [] (slurp (io/resource (str/replace *ns* #"\..$" "/input.txt"))))

(defn- parse-line [line]
  (let [[direction amount] (str/split line #" ")]
    [(first direction) (Integer/parseInt amount)]))

(defn- expand-moves [moves]
  (mapcat (fn [[direction amount]] (repeat amount direction)) moves))

(def move-deltas {\R [1 0]
                  \L [-1 0]
                  \U [0 1]
                  \D [0 -1]})

(defn- adjacent? [n] (<= (Math/abs n) 1))
(defn- bound-to-1 [n] (cond (zero? n) 0 (pos? n) 1 :else -1))

(defn- update-tail-position [head tail]
  (let [diff  (mapv - head tail)
        delta (if (and (adjacent? (diff 0)) (adjacent? (diff 1))) [0 0] (map bound-to-1 diff))]
    (mapv + tail delta)))

(defn- update-tail-positions [state]
  (let [next-state (update state :tails
                     #(loop [head       (state :head)
                             [tail & more] %
                             next-tails []]
                        (if (nil? tail)
                          next-tails
                          (let [next-tail (update-tail-position head tail)]
                            (recur next-tail more (conj next-tails next-tail))))))]
    (update next-state :tail-visited conj (-> next-state :tails last))))

(defn- apply-move [state direction]
  (-> state
    (update :head (partial mapv + (move-deltas direction)))
    update-tail-positions))

(defn -main []
  (->> (input)
    str/split-lines
    (map parse-line)
    expand-moves
    (reduce apply-move {:head [0 0] :tails (repeat 9 [0 0]) :tail-visited #{[0 0]}})
    :tail-visited
    count
    prn))

(-main)