(ns day9.a
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

(defn- update-tail-position [state]
  (let [adjacent?  #(<= (Math/abs %) 1)
        bound-to-1 #(cond (zero? %) 0 (pos? %) 1 :else -1)
        diff       (apply mapv - ((juxt :head :tail) state))
        delta      (if (and (adjacent? (diff 0)) (adjacent? (diff 1))) [0 0] (map bound-to-1 diff))
        next-pos   (mapv + (state :tail) delta)]
    (-> state
      (assoc :tail next-pos)
      (update :tail-visited conj next-pos))))

(defn- apply-move [state direction]
  (-> state
    (update :head (partial mapv + (move-deltas direction)))
    update-tail-position))

(defn -main []
  (->> (input)
    str/split-lines
    (map parse-line)
    expand-moves
    (reduce apply-move {:head [0 0] :tail [0 0] :tail-visited #{[0 0]}})
    :tail-visited
    count
    prn))

(-main)